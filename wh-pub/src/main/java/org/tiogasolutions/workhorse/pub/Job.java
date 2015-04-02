package org.tiogasolutions.workhorse.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jacobp on 4/1/2015.
 */
public class Job {

  private final String jobId;
  private final String revision;
  private final List<JobAction> jobActions = new ArrayList<>();

  @JsonCreator
  public Job(@JsonProperty("jobId") String jobId,
             @JsonProperty("revision") String revision,
             @JsonProperty("actions") Collection<? extends JobAction> actions) {

    this.jobId = jobId;
    this.revision = revision;

    if (actions != null) {
      this.jobActions.addAll(actions);
    }
  }

  public String getJobId() {
    return jobId;
  }

  public String getRevision() {
    return revision;
  }

  public List<JobAction> getJobActions() {
    return jobActions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Job job = (Job) o;

    if (!jobId.equals(job.jobId)) return false;
    return getClass().equals(job.getClass());
  }

  @Override
  public int hashCode() {
    int result = jobId.hashCode();
    result = 31 * result + getClass().hashCode();
    return result;
  }
}
