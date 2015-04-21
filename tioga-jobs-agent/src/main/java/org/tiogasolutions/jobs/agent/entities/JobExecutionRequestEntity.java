package org.tiogasolutions.jobs.agent.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import org.tiogasolutions.dev.common.id.uuid.TimeUuid;
import org.tiogasolutions.jobs.pub.JobDefinition;
import org.tiogasolutions.jobs.pub.JobParameters;
import org.tiogasolutions.jobs.pub.JobActionResult;
import org.tiogasolutions.jobs.pub.JobExecutionRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CouchEntity(JobExecutionRequestStore.JOB_EXECUTION_REQUEST_DESIGN_NAME)
public class JobExecutionRequestEntity {

  private final String jobExecutionRequestId;
  private final String revision;

  private final String jobDefinitionId;

  private final JobParameters jobParameters;
  private final List<JobActionResult> results = new ArrayList<>();

  @JsonCreator
  public JobExecutionRequestEntity(@JsonProperty("jobExecutionRequestId") String jobExecutionRequestId,
                                   @JsonProperty("revision") String revision,
                                   @JsonProperty("jobDefinitionId") String jobDefinitionId,
                                   @JsonProperty("jobParameters") JobParameters jobParameters,
                                   @JsonProperty("results") List<JobActionResult> results) {

    this.jobExecutionRequestId = jobExecutionRequestId;
    this.revision = revision;
    this.jobDefinitionId = jobDefinitionId;
    this.jobParameters = jobParameters;

    if (results != null) {
      this.results.addAll(results);
    }
  }

  @CouchId
  public String getJobExecutionRequestId() {
    return jobExecutionRequestId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public JobParameters getJobParameters() {
    return jobParameters;
  }

  public List<JobActionResult> getResults() {
    return Collections.unmodifiableList(results);
  }

  public String getJobDefinitionId() {
    return jobDefinitionId;
  }

  public void completed(List<JobActionResult> results) {
    this.results.clear();
    this.results.addAll(results);
  }

  @JsonIgnore
  public Map<String, String> getSubstitutions() {
    return jobParameters.getSubstitutions();
  }

  public JobExecutionRequest toJobExecutionRequest() {
    return new JobExecutionRequest(
      jobExecutionRequestId,
      revision,
      jobDefinitionId,
      jobParameters,
      results
    );
  }

  public static JobExecutionRequestEntity newEntity(JobDefinitionEntity jobDefinition, JobParameters jobParameters) {
    return new JobExecutionRequestEntity(
      TimeUuid.randomUUID().toString(), null,
      jobDefinition.getJobDefinitionId(),
      jobParameters,
      Collections.emptyList());
  }
}
