package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;

public class JobActionResult {

  private final int exitValue;
  private final String out;
  private final String err;
  private final String command;

  private final ZonedDateTime createdAt;
  private final ZonedDateTime completedAt;

    @JsonCreator
  private JobActionResult(@JsonProperty("command") String command,
                          @JsonProperty("exitValue") int exitValue,
                          @JsonProperty("out") String out,
                          @JsonProperty("err") String err,
                          @JsonProperty("createdAt") ZonedDateTime createdAt,
                          @JsonProperty("completedAt") ZonedDateTime completedAt) {

    this.exitValue = exitValue;
    this.command = command;

    this.createdAt = createdAt;
    this.completedAt = completedAt;

    this.out = (out == null) ? null : out.replace("\r", "");
    this.err = (err == null) ? null : err.replace("\r", "");
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public ZonedDateTime getCompletedAt() {
    return completedAt;
  }

  public String getCommand() {
    return command;
  }

  public int getExitValue() {
    return exitValue;
  }

  public String getOut() {
    return out;
  }

  public String getErr() {
    return err;
  }

  public static JobActionResult finished(String command, ZonedDateTime createdAt, int exitValue, String out, String err) {
    return new JobActionResult(command, exitValue, out, err, createdAt, ZonedDateTime.now());
  }

  public static JobActionResult fail(String command, ZonedDateTime createdAt, Exception ex) {
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));
    String stackTrace = writer.toString();
    return new JobActionResult(command, Integer.MIN_VALUE, null, stackTrace, createdAt, ZonedDateTime.now());
  }

  public boolean isFailure() {
    return exitValue != 0;
  }
}
