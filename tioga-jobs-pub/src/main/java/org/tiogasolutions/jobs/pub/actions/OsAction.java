package org.tiogasolutions.jobs.pub.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tiogasolutions.jobs.pub.ActionType;
import org.tiogasolutions.jobs.pub.JobAction;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class OsAction implements JobAction {

  private final String command;
  private final File workingDirectory;

  private final String lock;

  private final long timeout;
  private final TimeUnit timeoutUnit;

  @JsonCreator
  private OsAction(@JsonProperty("command") String command,
                   @JsonProperty("workingDirectory") String dir,
                   @JsonProperty("lock") String lock,
                   @JsonProperty("timeout") long timeout,
                   @JsonProperty("timeoutUnit") TimeUnit timeoutUnit) {

    this.command = command;
    this.workingDirectory = ((dir == null) ? new File("") : new File(dir)).getAbsoluteFile();

    this.lock = lock;

    this.timeout = timeout;
    this.timeoutUnit = timeoutUnit;
  }

  @JsonIgnore
  public ActionType getActionType() {
    return ActionType.OS_COMMAND;
  }

  public String getCommand() {
    return command;
  }

  public File getWorkingDirectory() {
    return workingDirectory;
  }

  public String getLock() {
    return lock;
  }

  @Override
  public String getLabel() {
    return String.format("OS Command [%s]", command);
  }

  public long getTimeout() {
    return timeout;
  }

  public TimeUnit getTimeoutUnit() {
    return timeoutUnit;
  }

  public static OsAction create(String command, File workingDirectory, String lock, long timeout, TimeUnit timeoutUnit) {
    return new OsAction(command, workingDirectory.getAbsolutePath(), lock, timeout, timeoutUnit);
  }

  public static OsAction create(String command, Path workingDirectory, String lock, long timeout, TimeUnit timeoutUnit) {
    return new OsAction(command, workingDirectory.toFile().getAbsolutePath(), lock, timeout, timeoutUnit);
  }
}
