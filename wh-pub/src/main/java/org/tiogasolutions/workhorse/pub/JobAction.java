package org.tiogasolutions.workhorse.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * Created by jacobp on 4/1/2015.
 */
public class JobAction {

  private final ActionType actionType;
  private final String command;
  private final File workingDirectory;
  private long timeout;
  private TimeUnit timeoutUnit;

  @JsonCreator
  private JobAction(@JsonProperty("actionType") ActionType actionType,
                    @JsonProperty("command") String command,
                    @JsonProperty("workingDirectory") String dir,
                    @JsonProperty("timeout") long timeout,
                    @JsonProperty("timeoutUnit") TimeUnit timeoutUnit) {

    this.command = command;
    this.actionType = actionType;

    this.workingDirectory = ((dir == null) ? new File("") : new File(dir)).getAbsoluteFile();

    this.timeout = timeout;
    this.timeoutUnit = timeoutUnit;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public String getCommand() {
    return command;
  }

  public File getWorkingDirectory() {
    return workingDirectory;
  }

  public long getTimeout() {
    return timeout;
  }

  public TimeUnit getTimeoutUnit() {
    return timeoutUnit;
  }

  public static JobAction newJobAction(ActionType actionType, String command, File workingDirectory, long timeout, TimeUnit timeoutUnit) {
    return new JobAction(actionType, command, workingDirectory.getAbsolutePath(), timeout, timeoutUnit);
  }

  public static JobAction newJobAction(ActionType actionType, String command, Path workingDirectory, long timeout, TimeUnit timeoutUnit) {
    return new JobAction(actionType, command, workingDirectory.toFile().getAbsolutePath(), timeout, timeoutUnit);
  }
}