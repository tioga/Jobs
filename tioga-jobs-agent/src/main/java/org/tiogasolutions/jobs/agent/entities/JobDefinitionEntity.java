package org.tiogasolutions.jobs.agent.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import org.tiogasolutions.dev.common.id.uuid.TimeUuid;
import org.tiogasolutions.jobs.pub.JobDefinition;
import org.tiogasolutions.jobs.pub.JobAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@CouchEntity(JobDefinitionStore.JOB_DESIGN_NAME)
public class JobDefinitionEntity {

  private final String jobId;
  private final String revision;
  private final List<JobAction> jobActions = new ArrayList<>();

  @JsonCreator
  public JobDefinitionEntity(@JsonProperty("jobId") String jobId,
                             @JsonProperty("revision") String revision,
                             @JsonProperty("actions") Collection<? extends JobAction> actions) {

    this.jobId = jobId;
    this.revision = revision;

    if (actions != null) {
      this.jobActions.addAll(actions);
    }
  }

  @CouchId
  public String getJobId() {
    return jobId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public List<JobAction> getJobActions() {
    return jobActions;
  }

  public JobDefinition toJobDefinition() {
    return new JobDefinition(jobId, revision, jobActions);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    JobDefinitionEntity jobDefinitionEntity = (JobDefinitionEntity) o;

    if (!jobId.equals(jobDefinitionEntity.jobId)) return false;
    return getClass().equals(jobDefinitionEntity.getClass());

  }

  @Override
  public int hashCode() {
    int result = jobId.hashCode();
    result = 31 * result + getClass().hashCode();
    return result;
  }

  public static JobDefinitionEntity newEntity(JobAction... jobActions) {
    return newJobDefinition(TimeUuid.randomUUID().toString(), Arrays.asList(jobActions));
  }

  public static JobDefinitionEntity newEntity(Collection<? extends JobAction> jobActions) {
    return newJobDefinition(TimeUuid.randomUUID().toString(), jobActions);
  }

  public static JobDefinitionEntity newJobDefinition(String id, Collection<? extends JobAction> actions) {
    return new JobDefinitionEntity(id, null, actions);
  }
}
