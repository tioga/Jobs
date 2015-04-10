package org.tiogasolutions.jobs.agent.resources;

import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;

import javax.ws.rs.Path;

public class ClientResourceV1 {

  private final ExecutionContextManager ecm;
  private final JobDefinitionStore jobDefinitionStore;
  private final JobExecutionRequestStore jobExecutionRequestStore;

  public ClientResourceV1(ExecutionContextManager ecm, JobDefinitionStore jobDefinitionStore, JobExecutionRequestStore jobExecutionRequestStore) {
    this.ecm = ecm;
    this.jobDefinitionStore = jobDefinitionStore;
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  @Path("/jobs")
  public JobsResourceV1 getJobsResourceResource() {
    return new JobsResourceV1(ecm, jobDefinitionStore);
  }
}
