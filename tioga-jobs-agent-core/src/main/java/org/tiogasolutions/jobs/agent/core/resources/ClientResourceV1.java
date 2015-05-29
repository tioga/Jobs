package org.tiogasolutions.jobs.agent.core.resources;

import org.tiogasolutions.jobs.kernel.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;

import javax.ws.rs.Path;

public class ClientResourceV1 {

  private final JobDefinitionStore jobDefinitionStore;
  private final JobExecutionRequestStore jobExecutionRequestStore;

  public ClientResourceV1(JobExecutionRequestStore jobExecutionRequestStore, JobDefinitionStore jobDefinitionStore) {
    this.jobDefinitionStore = jobDefinitionStore;
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  @Path("/jobs")
  public JobsResourceV1 getJobsResourceResource() {
    return new JobsResourceV1(jobExecutionRequestStore, jobDefinitionStore);
  }
}
