package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jacobp on 4/1/2015.
 */
public class JobDefinition {

  private final String jobDefinitionId;
  private final String revision;
  private final List<JobAction> jobActions = new ArrayList<>();

  @JsonCreator
  public JobDefinition(@JsonProperty("jobDefinitionId") String jobDefinitionId,
                       @JsonProperty("revision") String revision,
                       @JsonProperty("actions") Collection<? extends JobAction> actions) {

    this.jobDefinitionId = jobDefinitionId;
    this.revision = revision;

    if (actions != null) {
      this.jobActions.addAll(actions);
    }
  }

  public String getJobDefinitionId() {
    return jobDefinitionId;
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

    JobDefinition jobDefinition = (JobDefinition) o;

    if (!jobDefinitionId.equals(jobDefinition.jobDefinitionId)) return false;
    return getClass().equals(jobDefinition.getClass());
  }

  @Override
  public int hashCode() {
    int result = jobDefinitionId.hashCode();
    result = 31 * result + getClass().hashCode();
    return result;
  }
}
