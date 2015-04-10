package org.tiogasolutions.jobs.agent.resources;

import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;

import javax.ws.rs.Path;

public class ClientResourceV1 {

  private final JobDefinitionStore jobDefinitionStore;
  private final ExecutionContextManager ecm;

  public ClientResourceV1(ExecutionContextManager ecm, JobDefinitionStore jobDefinitionStore) {
    this.ecm = ecm;
    this.jobDefinitionStore = jobDefinitionStore;
  }

  @Path("/jobs")
  public JobsResourceV1 getNotificationsResource() {
    return new JobsResourceV1(ecm, jobDefinitionStore);
  }
}
