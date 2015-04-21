package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DomainProfile {

  private final String domainName;

  @JsonCreator
  public DomainProfile(@JsonProperty("domainName") String domainName) {
    this.domainName = domainName;
  }

  public String getDomainName() {
    return domainName;
  }
}
