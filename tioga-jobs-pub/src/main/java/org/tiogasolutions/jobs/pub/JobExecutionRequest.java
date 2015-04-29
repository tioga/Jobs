package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobExecutionRequest {

  private final String jobExecutionRequestId;
  private final String revision;

  private final String jobDefinitionId;

  private final JobParameters jobParameters;
  private final List<JobActionResult> results = new ArrayList<>();

  private String summary;
  private final int actionCount;

  private final ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  @JsonCreator
  public JobExecutionRequest(@JsonProperty("jobExecutionRequestId") String jobExecutionRequestId,
                             @JsonProperty("revision") String revision,
                             @JsonProperty("jobDefinitionId") String jobDefinitionId,
                             @JsonProperty("summary") String summary,
                             @JsonProperty("actionCount") int actionCount,
                             @JsonProperty("jobParameters") JobParameters jobParameters,
                             @JsonProperty("results") List<JobActionResult> results,
                             @JsonProperty("createdAt") ZonedDateTime createdAt,
                             @JsonProperty("updatedAt") ZonedDateTime updatedAt) {

    this.jobExecutionRequestId = jobExecutionRequestId;
    this.revision = revision;

    this.summary = summary;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;

    this.actionCount = actionCount;
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

  public boolean hasFailure() {
    for (JobActionResult result : results) {
      if (result.hasFailure()) {
        return true;
      }
    }
    return false;
  }
}
