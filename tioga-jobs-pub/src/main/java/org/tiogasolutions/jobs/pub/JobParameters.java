package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JobParameters {

  private final boolean synchronous;
  private final String callbackUrl;
  private final Map<String,String> substitutions = new HashMap<>();

  @JsonCreator
  private JobParameters(@JsonProperty("synchronous") boolean synchronous,
                        @JsonProperty("callbackUrl") String callbackUrl,
                        @JsonProperty("substitutions") Map<String,String> substitutions) {

    this.synchronous = synchronous;
    this.callbackUrl = callbackUrl;

    if (substitutions != null) {
      this.substitutions.putAll(substitutions);
    }
  }

  public Map<String, String> getSubstitutions() {
    return substitutions;
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
    return new JobParameters(true, null, Collections.emptyMap());
  }

  public static JobParameters createSynchronous(Map<String,String> substitutionsMap) {
    return new JobParameters(true, null, substitutionsMap);
  }

  public static JobParameters createAsynchronous(String callbackUrl) {
    return new JobParameters(false, callbackUrl, Collections.emptyMap());
  }

  public static JobParameters createAsynchronous(String callbackUrl, Map<String,String> substitutionsMap) {
    return new JobParameters(false, callbackUrl, substitutionsMap);
  }
}
