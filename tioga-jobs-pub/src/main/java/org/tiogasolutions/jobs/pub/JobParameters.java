package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jacobp on 4/1/2015.
 */
public class JobParameters {

  private final boolean synchronous;
  private final String callbackUrl;

  @JsonCreator
  private JobParameters(@JsonProperty("synchronous") boolean synchronous,
                        @JsonProperty("callbackUrl") String callbackUrl) {

    this.synchronous = synchronous;
    this.callbackUrl = callbackUrl;
  }

  public boolean isSynchronous() {
    return synchronous;
  }

  @JsonIgnore
  public boolean isAsynchronous() {
    return synchronous == false;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public static JobParameters createSynchronous() {
    return new JobParameters(true, null);
  }

  public static JobParameters createAsynchronous(String callbackUrl) {
    return new JobParameters(false, callbackUrl);
  }
}
