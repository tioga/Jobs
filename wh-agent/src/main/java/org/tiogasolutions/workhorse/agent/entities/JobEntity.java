package org.tiogasolutions.workhorse.agent.entities;

import com.couchace.annotations.CouchEntity;
import com.couchace.annotations.CouchId;
import com.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.id.uuid.TimeUuid;
import org.tiogasolutions.workhorse.pub.Job;
import org.tiogasolutions.workhorse.pub.JobAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@CouchEntity(JobStore.JOB_DESIGN_NAME)
public class JobEntity {

  private final String jobId;
  private final String revision;
  private final List<JobAction> jobActions = new ArrayList<>();

  @JsonCreator
  public JobEntity(@JsonProperty("jobId") String jobId,
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

  public Job toJob() {
    return new Job(jobId, revision, jobActions);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    JobEntity jobEntity = (JobEntity) o;

    if (!jobId.equals(jobEntity.jobId)) return false;
    return getClass().equals(jobEntity.getClass());

  }

  @Override
  public int hashCode() {
    int result = jobId.hashCode();
    result = 31 * result + getClass().hashCode();
    return result;
  }

  public static JobEntity newEntity(JobAction... jobActions) {
    return newEntity(Arrays.asList(jobActions));
  }

  public static JobEntity newEntity(Collection<? extends JobAction> actions) {
    return new JobEntity(
      TimeUuid.randomUUID().toString(),
      null,
      actions
    );
  }
}
