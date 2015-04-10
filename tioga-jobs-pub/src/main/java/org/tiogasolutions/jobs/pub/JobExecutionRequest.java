package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jacobp on 4/10/2015.
 */
public class JobExecutionRequest {

  private final String jobExecutionRequestId;
  private final String revision;
  private final JobExecution jobExecution;

  @JsonCreator
  public JobExecutionRequest(@JsonProperty("jobExecutionRequestId") String jobExecutionRequestId,
                             @JsonProperty("revision") String revision,
                             @JsonProperty("jobExecution") JobExecution jobExecution) {

    this.jobExecutionRequestId = jobExecutionRequestId;
    this.revision = revision;
    this.jobExecution = jobExecution;
  }

  public String getJobExecutionRequestId() {
    return jobExecutionRequestId;
  }

  public String getRevision() {
    return revision;
  }

  public JobExecution getJobExecution() {
    return jobExecution;
  }
}
