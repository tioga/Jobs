package org.tiogasolutions.jobs.kernel.actions;

import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestEntity;
import org.tiogasolutions.jobs.pub.JobAction;
import org.tiogasolutions.jobs.pub.JobActionResult;
import org.tiogasolutions.jobs.pub.actions.WaitForHttpAction;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InternalActionExecutor extends JobActionExecutorSupport {

  public InternalActionExecutor() {
  }

  @Override
  public JobActionResult execute(JobExecutionRequestEntity request, JobAction jobAction, ZonedDateTime startedAt) throws Exception {

    if (jobAction instanceof WaitForHttpAction) {
      return executeWaitForHttpAction(request, (WaitForHttpAction) jobAction, startedAt);

    } else {
      String msg = String.format("The action %s cannot be processed by %s", jobAction.getClass().getName(), getClass().getName());
      throw new UnsupportedOperationException(msg);
    }
  }

  private JobActionResult executeWaitForHttpAction(JobExecutionRequestEntity request, WaitForHttpAction action, ZonedDateTime startedAt) throws Exception {

    String httpRequest = action.getHttpRequest();
    httpRequest = super.substitute(request, httpRequest);

    long elapsed = Duration.between(startedAt, ZonedDateTime.now()).toMillis();
    long timeout = TimeUnit.MILLISECONDS.convert(action.getTimeout(), action.getTimeoutUnit());

    String err = "";
    String body = null;

    String command = String.format("Wait for %s", httpRequest);
    if (action.getRegex() != null) {
      command += String.format(", match \"%s\"", action.getRegex());
    }

    while (elapsed < timeout) {
      Response response = get(action, httpRequest);
      body = response.readEntity(String.class);

      if (action.getStatuses().contains(response.getStatus()) == false) {
        err += String.format("Invalid status code %s\n", response.getStatus());
      } else {
        if (action.getRegex() == null) {
          // No regular expression, all is good
          return JobActionResult.finished(command, startedAt, body);
        } else {
          // We have a regular expression so we need to validate it.
          Pattern pattern = Pattern.compile(action.getRegex());
          Matcher matcher = pattern.matcher(body);

          if (matcher.find()) {
            String text = matcher.group(0);
            return JobActionResult.finished(command, startedAt, text);
          } else {
            err += "Regular expression match failed\n";
          }
        }
      }
      Thread.sleep(1000);
      elapsed = Duration.between(startedAt, ZonedDateTime.now()).toMillis();
    }

    err = StringUtils.whiteSpaceToNull(err);
    return JobActionResult.timeoutFailure(command, startedAt, body, err);
  }

  public static Response get(WaitForHttpAction action, String httpRequest) {
    Client client = ClientBuilder.newBuilder().build();
    UriBuilder uriBuilder = UriBuilder.fromUri(httpRequest);

    WebTarget target = client.target(uriBuilder);
    Invocation.Builder builder = target.request(action.getAcceptedResponseTypes());

    if (StringUtils.isNotBlank(action.getUsername())) {
      builder.header("Authorization", getBasicAuthentication(action.getUsername(), action.getPassword()));
    }

    return builder.get(Response.class);
  }

  private static String getBasicAuthentication(String username, String password) {
    try {
      String token = username + ":" + password;
      return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));

    } catch (UnsupportedEncodingException ex) {
      throw new IllegalStateException("Cannot encode with UTF-8", ex);
    }
  }
}
