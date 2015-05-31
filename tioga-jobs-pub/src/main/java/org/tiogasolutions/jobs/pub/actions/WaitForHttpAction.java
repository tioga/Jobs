package org.tiogasolutions.jobs.pub.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tiogasolutions.jobs.pub.ActionType;
import org.tiogasolutions.jobs.pub.JobAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaitForHttpAction implements JobAction {

  private final String httpRequest;
  private final String username;
  private final String password;

  private final String regex;
  private final List<Integer> statuses = new ArrayList<>();
  private final String[] acceptedResponseTypes;

  private final String lock;
  private final long timeout;
  private final TimeUnit timeoutUnit;

  @JsonCreator
  private WaitForHttpAction(@JsonProperty("httpRequest") String httpRequest,
                            @JsonProperty("username") String username,
                            @JsonProperty("password") String password,
                            @JsonProperty("regex") String regex,
                            @JsonProperty("statuses") Integer[] statuses,
                            @JsonProperty("acceptedResponseTypes") String[] acceptedResponseTypes,
                            @JsonProperty("lock") String lock,
                            @JsonProperty("timeout") long timeout,
                            @JsonProperty("timeoutUnit") TimeUnit timeoutUnit) {

    this.httpRequest = httpRequest;
    this.username = username;
    this.password = password;

    this.regex = regex;
    Collections.addAll(this.statuses, statuses);
    this.acceptedResponseTypes = acceptedResponseTypes;

    this.lock = lock;
    this.timeout = timeout;
    this.timeoutUnit = timeoutUnit;
  }

  public List<Integer> getStatuses() {
    return statuses;
  }

  public String getRegex() {
    return regex;
  }

  public String[] getAcceptedResponseTypes() {
    return acceptedResponseTypes;
  }

  public String getHttpRequest() {
    return httpRequest;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public long getTimeout() {
    return timeout;
  }

  public TimeUnit getTimeoutUnit() {
    return timeoutUnit;
  }

  @Override
  public ActionType getActionType() {
    return ActionType.WAIT_FOR_HTTP;
  }

  @Override
  public String getLock() {
    return lock;
  }

  @Override
  public String getLabel() {
    return "Wait for " + httpRequest;
  }
}
