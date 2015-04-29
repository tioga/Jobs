package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;

public class JobActionResult {

  private final String out;
  private final String err;

  private final String command;
  private final String failure;

  private final ZonedDateTime createdAt;
  private final ZonedDateTime completedAt;

    @JsonCreator
  private JobActionResult(@JsonProperty("command") String command,
                          @JsonProperty("out") String out,
                          @JsonProperty("err") String err,
                          @JsonProperty("createdAt") ZonedDateTime createdAt,
                          @JsonProperty("completedAt") ZonedDateTime completedAt,
                          @JsonProperty("failure") String failure) {

    this.command = command;
    this.failure = failure;

    this.createdAt = createdAt;
    this.completedAt = completedAt;

    this.out = (out == null) ? null : out.replace("\r", "");
    this.err = (err == null) ? null : err.replace("\r", "");
  }

  public boolean hasFailure() {
    return failure != null;
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

  public String getFailure() {
    return failure;
  }

  public String getOut() {
    return out;
  }

  public String getErr() {
    return err;
  }

  public static JobActionResult finished(String command, ZonedDateTime createdAt, int exitValue, String out, String err) {
    String failure = (exitValue == 0) ? null : String.valueOf(exitValue);
    return new JobActionResult(command, out, err, createdAt, ZonedDateTime.now(), failure);
  }

  public static JobActionResult fail(String command, ZonedDateTime createdAt, Exception ex, String out, String err) {
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));
    String stackTrace = writer.toString();
    return new JobActionResult(command, out, err, createdAt, ZonedDateTime.now(), stackTrace);
  }
}
