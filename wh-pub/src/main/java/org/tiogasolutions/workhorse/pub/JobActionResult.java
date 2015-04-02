package org.tiogasolutions.workhorse.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jacobp on 4/1/2015.
 */
public class JobActionResult {

  private final int exitValue;
  private final String output;

  @JsonCreator
  public JobActionResult(@JsonProperty("exitValue") int exitValue,
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
}
