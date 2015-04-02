package org.tiogasolutions.workhorse.agent.resources;

import org.tiogasolutions.workhorse.agent.entities.JobDefinitionStore;
import org.tiogasolutions.workhorse.agent.support.ExecutionContextManager;

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
