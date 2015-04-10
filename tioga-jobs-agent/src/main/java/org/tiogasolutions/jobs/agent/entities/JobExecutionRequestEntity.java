package org.tiogasolutions.jobs.agent.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import org.tiogasolutions.dev.common.id.uuid.TimeUuid;
import org.tiogasolutions.jobs.pub.JobExecution;
import org.tiogasolutions.jobs.pub.JobExecutionRequest;

@CouchEntity(JobExecutionRequestStore.JOB_EXECUTION_REQUEST_DESIGN_NAME)
public class JobExecutionRequestEntity {

  private final String jobExecutionRequestId;
  private final String revision;
  private final JobExecution jobExecution;

  @JsonCreator
  public JobExecutionRequestEntity(@JsonProperty("jobExecutionRequestId") String jobExecutionRequestId,
                                   @JsonProperty("revision") String revision,
                                   @JsonProperty("jobExecution") JobExecution jobExecution) {

    this.jobExecutionRequestId = jobExecutionRequestId;
    this.revision = revision;
    this.jobExecution = jobExecution;
  }

  @CouchId
  public String getJobExecutionRequestId() {
    return jobExecutionRequestId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public JobExecution getJobExecution() {
    return jobExecution;
  }

  public static JobExecutionRequestEntity newEntity(JobExecution jobExecution) {
    return new JobExecutionRequestEntity(TimeUuid.randomUUID().toString(), null, jobExecution);
  }

  public JobExecutionRequest toJobExecutionRequestEntity() {
    return new JobExecutionRequest(
      jobExecutionRequestId,
      revision,
      jobExecution
    );
  }
}
