package org.tiogasolutions.jobs.agent.core.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tiogasolutions.dev.common.IoUtils;
import org.tiogasolutions.dev.common.ReflectUtils;
import org.tiogasolutions.dev.common.exceptions.ApiException;
import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;
import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.jobs.agent.core.JobsApplication;
import org.tiogasolutions.jobs.kernel.entities.JobDefinitionEntity;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestEntity;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.kernel.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.pub.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.*;

public class JobsResourceV1 {

  private static final Log log = LogFactory.getLog(JobsResourceV1.class);
  private static final ExecutorService executor = Executors.newCachedThreadPool();

  public JobsResourceV1() {
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public QueryResult<JobDefinition> getJobs(@Context Application app) {
    List<JobDefinition> jobDefinitions = new ArrayList<>();
    JobsApplication.get(app, JobDefinitionStore.class).getAll().stream().forEach(jobEntity -> jobDefinitions.add(jobEntity.toJobDefinition()));
    return ListQueryResult.newComplete(JobDefinition.class, jobDefinitions);
  }

  private JobDefinitionEntity loadJob(Application app, String jobDefinitionId) {

    JobDefinitionStore store = JobsApplication.get(app, JobDefinitionStore.class);
    JobDefinitionEntity jobDefinitionEntity = store.getByDocumentId(jobDefinitionId);

    if (jobDefinitionEntity == null) {
      String msg = String.format("The job definition \"%s\" does not exist.", jobDefinitionId);
      throw ApiNotFoundException.notFound(msg);
    }

    return jobDefinitionEntity;
  }

  @GET
  @Path("/{jobDefinitionId}")
  @Produces(MediaType.APPLICATION_JSON)
  public JobDefinition getJob(@Context Application app,
                              @PathParam("jobDefinitionId") String jobDefinitionId) throws Exception {

    return loadJob(app, jobDefinitionId).toJobDefinition();
  }

  @POST
  @Path("/{jobDefinitionId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public JobExecutionRequest execute(@Context Application app,
                                     @Context UriInfo uriInfo,
                                     @PathParam("jobDefinitionId") String jobDefinitionId,
                                     JobParameters jobParameters) throws Exception {

    return execute(app, uriInfo, jobDefinitionId, -1, jobParameters);
  }

  @POST
  @Path("/{jobDefinitionId}/actions/{actionIndex}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public JobExecutionRequest execute(@Context Application app,
                                     @Context UriInfo uriInfo,
                                     @PathParam("jobDefinitionId") String jobDefinitionId,
                                     @PathParam("actionIndex") int actionIndex,
                                     JobParameters jobParameters) throws Exception {

    if (jobParameters == null) {
      throw ApiException.badRequest("The job parameters must be specified.");
    }

    JobDefinitionEntity jobDefinitionEntity = loadJob(app, jobDefinitionId);
    JobExecutionRequestEntity request = JobExecutionRequestEntity.newEntity(jobDefinitionEntity, jobParameters);
    JobsApplication.get(app, JobExecutionRequestStore.class).create(request);

    if (jobParameters.isSynchronous()) {
      return executeJob(app, request, jobDefinitionEntity, actionIndex);

    } else {
      executor.submit(() -> executeJob(app, request, jobDefinitionEntity, actionIndex));
      return request.toJobExecutionRequest();
    }
  }

  private JobExecutionRequest executeJob(Application app, JobExecutionRequestEntity request, JobDefinitionEntity jobDefinitionEntity, int actionIndex) throws Exception {

    List<JobAction> actions = jobDefinitionEntity.getJobActions();
    if (actionIndex >= 0) {
      actions = singletonList(actions.get(actionIndex));
    }

    for (JobAction jobAction : actions) {
      if (processAction(app, request, jobAction).isFailure()) {
        break;
      }
    }

    // One last update to make sure everything is current.
    JobsApplication.get(app, JobExecutionRequestStore.class).update(request);

    return request.toJobExecutionRequest();
  }

  private JobActionResult processAction(Application app, JobExecutionRequestEntity request, JobAction jobAction) throws Exception {
    ActionType actionType = jobAction.getActionType();
    if (actionType.isOsCommand()) {
      return processOsCommand(app, request, jobAction);

    } else {
      String msg = String.format("The action type \"%s\" is not supported.", actionType);
      throw new UnsupportedOperationException(msg);
    }
  }

  private JobActionResult processOsCommand(Application app, JobExecutionRequestEntity request, JobAction jobAction) {

    JobActionResult result;
    String command = jobAction.getCommand();
    ZonedDateTime startedAt = ZonedDateTime.now();

    try {
      JobVariable variable = JobVariable.findFirst(command);
      while (variable != null) {
        command = variable.replace(request.getSubstitutions(), command);
        variable = JobVariable.findFirst(command);
      }

      List<String> commands = splitCommand(command);
      String[] commandArray = ReflectUtils.toArray(String.class, commands);

      File workingDir = jobAction.getWorkingDirectory();
      if (workingDir.exists() == false) {
        String msg = "The specified working directory does not exist: " + workingDir.getAbsolutePath();
        throw new FileNotFoundException(msg);
      }

      File outFile = File.createTempFile("process-out-", ".txt");
      outFile.deleteOnExit();

      File errFile = File.createTempFile("process-err-", ".txt");
      errFile.deleteOnExit();

      Process process = new ProcessBuilder()
        .command(commandArray)
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.to(outFile))
        .redirectError(ProcessBuilder.Redirect.to(errFile))
        .start();

      process.waitFor(jobAction.getTimeout(), jobAction.getTimeoutUnit());

      if (process.isAlive()) {
        process.destroyForcibly();
      }

      int exitValue = process.exitValue();

      String out = IoUtils.toString(outFile);
      if (outFile.delete() == false) {
        log.warn("Unable to delete temp file: " + outFile.getAbsolutePath());
      }

      String err = IoUtils.toString(errFile);
      if (errFile.delete() == false) {
        log.warn("Unable to delete temp file: " + errFile.getAbsolutePath());
      }

      result = JobActionResult.finished(command, startedAt, exitValue, out, err);

    } catch (Exception ex) {
      result = JobActionResult.fail(command, startedAt, ex);
    }

    request.addResult(result);
    JobsApplication.get(app, JobExecutionRequestStore.class).update(request);

    return result;
  }

  public static List<String> splitCommand(String command) {

    boolean inString = false;
    List<String> commands = new ArrayList<>();
    StringBuilder builder = new StringBuilder();

    for (char chr : command.toCharArray()) {
      if (inString) {
        if (chr != '"') {
          builder.append(chr);
        } else {
          // we are closing a string.
          inString = false;
          finish(builder, commands);
        }
      } else if (chr == '"') {
        // We are starting a string
        inString = true;

      } else if (Character.isWhitespace(chr)) {
        finish(builder, commands);

      } else {
        builder.append(chr);
      }
    }

    finish(builder, commands);

    return commands;
  }

  private static void finish(StringBuilder builder, List<String> commands) {
    if (builder.length() > 0) {
      commands.add(builder.toString());
    }
    builder.delete(0, builder.length());
  }
}
