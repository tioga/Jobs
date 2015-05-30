package org.tiogasolutions.jobs.agent.core.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tiogasolutions.dev.common.IoUtils;
import org.tiogasolutions.dev.common.ReflectUtils;
import org.tiogasolutions.jobs.agent.core.resources.JobVariable;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestEntity;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.pub.JobAction;
import org.tiogasolutions.jobs.pub.JobActionResult;
import org.tiogasolutions.jobs.pub.OsAction;

import javax.ws.rs.core.Application;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class OsActionExecutor implements JobActionExecutor {

  private static final Log log = LogFactory.getLog(OsActionExecutor.class);

  private final JobExecutionRequestStore jobExecutionRequestStore;

  public OsActionExecutor(JobExecutionRequestStore jobExecutionRequestStore) {
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  public JobActionResult execute(Application app, JobExecutionRequestEntity request, JobAction jobAction, ZonedDateTime startedAt) throws Exception {

    if (jobAction instanceof OsAction == false) {
      String msg = String.format("The action %s cannot be processed by %s", jobAction.getClass().getName(), getClass().getName());
      throw new UnsupportedOperationException(msg);
    }

    OsAction action = (OsAction)jobAction;

    JobActionResult result;
    String command = action.getCommand();

    String out = null;
    String err = null;

    try {
      JobVariable variable = JobVariable.findFirst(command);
      while (variable != null) {
        command = variable.replace(request.getSubstitutions(), command);
        variable = JobVariable.findFirst(command);
      }

      List<String> commands = splitCommand(command);
      String[] commandArray = ReflectUtils.toArray(String.class, commands);

      File workingDir = action.getWorkingDirectory();
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

      process.waitFor(action.getTimeout(), action.getTimeoutUnit());

      if (process.isAlive()) {
        process.destroyForcibly();
      }

      int exitValue = process.exitValue();

      out = IoUtils.toString(outFile);
      if (outFile.delete() == false) {
        log.warn("Unable to delete temp file: " + outFile.getAbsolutePath());
      }

      err = IoUtils.toString(errFile);
      if (errFile.delete() == false) {
        log.warn("Unable to delete temp file: " + errFile.getAbsolutePath());
      }

      result = JobActionResult.finished(command, startedAt, exitValue, out, err);

    } catch (IllegalThreadStateException e) {
      result = JobActionResult.timeoutFailure(command, startedAt);

    } catch (Exception ex) {
      result = JobActionResult.fail(command, startedAt, ex, out, err);
    }

    request.addResult(result);
    jobExecutionRequestStore.update(request);

    return result;
  }

  protected static List<String> splitCommand(String command) {

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
