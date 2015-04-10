package org.tiogasolutions.jobs.agent.resources;

import org.tiogasolutions.dev.common.IoUtils;
import org.tiogasolutions.dev.common.ReflectUtils;
import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestEntity;
import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionEntity;
import org.tiogasolutions.jobs.pub.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobResourceV1 {

  private final ExecutionContextManager ecm;
  private final JobDefinitionEntity jobDefinitionEntity;
  private final JobExecutionRequestStore jobExecutionRequestStore;

  private static final ExecutorService executor = Executors.newCachedThreadPool();

  public JobResourceV1(ExecutionContextManager ecm, JobDefinitionEntity jobDefinitionEntity, JobExecutionRequestStore jobExecutionRequestStore) {
    this.ecm = ecm;
    this.jobDefinitionEntity = jobDefinitionEntity;
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JobDefinition getJob() throws Exception {
    return jobDefinitionEntity.toJobDefinition();
  }


  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public JobExecutionRequest execute(JobExecution jobExecution) throws Exception {

    JobExecutionRequestEntity request = JobExecutionRequestEntity.newEntity(jobExecution);
    jobExecutionRequestStore.create(request);

    if (StringUtils.isNotBlank(jobExecution.getCallbackUrl())) {
      executor.submit( () -> executeJob(request) );
      return request.toJobExecutionRequestEntity();

    } else {
      return executeJob(request);
    }
  }

  private JobExecution executeJob(JobExecutionRequestEntity request) throws Exception {
    List<JobActionResult> results = new ArrayList<>();

    for (JobAction jobAction : jobDefinitionEntity.getJobActions()) {
      ActionType actionType = jobAction.getActionType();
      if (actionType.isOsCommand()) {
        JobActionResult result = processOsCommand(jobAction);
        results.add(result);

      } else {
        String msg = String.format("The action type \"%s\" is not supported.", actionType);
        throw new UnsupportedOperationException(msg);
      }
    }
    return JobExecution.completed(results);
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

    return new JobActionResult(exitValue, output);
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
