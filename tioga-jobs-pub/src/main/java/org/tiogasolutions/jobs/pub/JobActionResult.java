package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.PrintWriter;
import java.io.StringWriter;

public class JobActionResult {

  private final int exitValue;
  private final String output;

  @JsonCreator
  private JobActionResult(@JsonProperty("exitValue") int exitValue,
                          @JsonProperty("output") String output) {

    this.exitValue = exitValue;
    this.output = output;
  }

  public int getExitValue() {
    return exitValue;
  }

  public String getOutput() {
    return output;
  }

  public static JobActionResult finished(int exitValue, String output) {
    return new JobActionResult(exitValue, output);
  }

  public static JobActionResult fail(Exception ex) {
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));
    String output = writer.toString();
    return new JobActionResult(Integer.MIN_VALUE, output);
  }
}
