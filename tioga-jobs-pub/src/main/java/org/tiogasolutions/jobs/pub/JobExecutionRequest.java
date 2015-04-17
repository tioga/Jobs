package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class JobExecutionRequest {

  private final String jobExecutionRequestId;
  private final String revision;

  private final String jobDefinitionId;

  private final JobParameters jobParameters;
  private final List<JobActionResult> results = new ArrayList<>();

  @JsonCreator
  public JobExecutionRequest(@JsonProperty("jobExecutionRequestId") String jobExecutionRequestId,
                             @JsonProperty("revision") String revision,
                             @JsonProperty("jobDefinitionId") String jobDefinitionId,
                             @JsonProperty("jobExecution") JobParameters jobParameters,
                             @JsonProperty("results") List<JobActionResult> results) {

    this.jobExecutionRequestId = jobExecutionRequestId;
    this.revision = revision;
    this.jobDefinitionId = jobDefinitionId;
    this.jobParameters = jobParameters;

    if (results != null) {
      this.results.addAll(results);
    }
  }

  public String getJobExecutionRequestId() {
    return jobExecutionRequestId;
  }

  public String getRevision() {
    return revision;
  }

  public JobParameters getJobParameters() {
    return jobParameters;
  }

  public List<JobActionResult> getResults() {
    return results;
  }

  public String getJobDefinitionId() {
    return jobDefinitionId;
  }
}
