package org.tiogasolutions.jobs.agent.resources;

import org.tiogasolutions.dev.common.IoUtils;
import org.tiogasolutions.dev.common.ReflectUtils;
import org.tiogasolutions.dev.common.exceptions.ApiException;
import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;
import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.jobs.agent.JobsApplication;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionEntity;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestEntity;
import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.pub.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobsResourceV1 {

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
                                     @PathParam("jobDefinitionId") String jobDefinitionId,
                                     JobParameters jobParameters) throws Exception {

    if (jobParameters == null) {
      throw ApiException.badRequest("The job parameters must be specified.");
    }

    JobDefinitionEntity jobDefinitionEntity = loadJob(app, jobDefinitionId);
    JobExecutionRequestEntity request = JobExecutionRequestEntity.newEntity(jobDefinitionEntity, jobParameters);
    JobsApplication.get(app, JobExecutionRequestStore.class).create(request);

    if (jobParameters.isSynchronous()) {
      return executeJob(app, request, jobDefinitionEntity);

    } else {
      executor.submit(() -> executeJob(app, request, jobDefinitionEntity));
      return request.toJobExecutionRequestEntity();
    }
  }

  private JobExecutionRequest executeJob(Application app, JobExecutionRequestEntity request, JobDefinitionEntity jobDefinitionEntity) throws Exception {
    List<JobActionResult> results = new ArrayList<>();

    for (JobAction jobAction : jobDefinitionEntity.getJobActions()) {
      processAction(results, jobAction);
    }

    request.completed(results);
    JobsApplication.get(app, JobExecutionRequestStore.class).update(request);

    return request.toJobExecutionRequestEntity();
  }

  private void processAction(List<JobActionResult> results, JobAction jobAction) throws Exception {
    try {
      ActionType actionType = jobAction.getActionType();
      if (actionType.isOsCommand()) {
        JobActionResult result = processOsCommand(jobAction);
        results.add(result);

      } else {
        String msg = String.format("The action type \"%s\" is not supported.", actionType);
        throw new UnsupportedOperationException(msg);
      }
    } catch (Exception ex) {
      results.add(JobActionResult.fail(ex));
    }
  }

  private JobActionResult processOsCommand(JobAction jobAction) throws Exception {
    String command = jobAction.getCommand();
    List<String> commands = splitCommand(command);
    String[] commandArray = ReflectUtils.toArray(String.class, commands);

    File workingDir = jobAction.getWorkingDirectory();
    if (workingDir.exists() == false) {
      String msg = "The specified working directory does not exist: " + workingDir.getAbsolutePath();
      throw new FileNotFoundException(msg);
    }

    Process process = new ProcessBuilder()
      .command(commandArray)
      .directory(workingDir)
      .redirectErrorStream(true)
      .start();

    process.waitFor(jobAction.getTimeout(), jobAction.getTimeoutUnit());

    int exitValue = process.exitValue();
    String output = IoUtils.toString(process.getInputStream());

    return JobActionResult.finished(exitValue, output);
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
