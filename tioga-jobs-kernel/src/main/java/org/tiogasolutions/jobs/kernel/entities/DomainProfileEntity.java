package org.tiogasolutions.jobs.kernel.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import org.tiogasolutions.jobs.pub.DomainProfile;

@CouchEntity
public class DomainProfileEntity {

  private final String domainProfileId;
  private final String revision;
  private final String domainName;
  private final DomainStatus domainStatus;
  private final String apiUsername;
  private final String apiPassword;
  private final String jobsDbName;

  @JsonCreator
  public DomainProfileEntity(@JsonProperty("domainProfileId") String domainProfileId,
                             @JsonProperty("revision") String revision,
                             @JsonProperty("domainName") String domainName,
                             @JsonProperty("domainStatus") DomainStatus domainStatus,
                             @JsonProperty("apiUsername") String apiUsername,
                             @JsonProperty("apiPassword") String apiPassword,
                             @JsonProperty("jobsDbName") String jobsDbName) {

    this.domainProfileId = domainProfileId;
    this.revision = revision;
    this.domainName = domainName;
    this.domainStatus = domainStatus;
    this.apiUsername = apiUsername;
    this.apiPassword = apiPassword;
    this.jobsDbName = jobsDbName;
  }

  @CouchId
  public String getDomainProfileId() {
    return domainProfileId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public String getDomainName() {
    return domainName;
  }

  public DomainStatus getDomainStatus() {
    return domainStatus;
  }

  public String getApiUsername() {
    return apiUsername;
  }

  public String getApiPassword() {
    return apiPassword;
  }

  public String getJobsDbName() {
    return jobsDbName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DomainProfileEntity that = (DomainProfileEntity) o;

    return domainProfileId.equals(that.domainProfileId);
  }

  @Override
  public int hashCode() {
    return domainProfileId.hashCode();
  }

  public DomainProfile toDomainProfile() {
    return new DomainProfile(domainName);
  }
}
