package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jacobp on 4/1/2015.
 */
public class JobExecution {

  private final String callbackUrl;

  @JsonCreator
  private JobExecution(@JsonProperty("callbackUrl") String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public static JobExecution create(String callbackUrl) {
    return new JobExecution(callbackUrl);
  }
}
