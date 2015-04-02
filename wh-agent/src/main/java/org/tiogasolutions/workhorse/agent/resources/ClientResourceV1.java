package org.tiogasolutions.workhorse.agent.resources;

import org.tiogasolutions.workhorse.agent.entities.JobStore;
import org.tiogasolutions.workhorse.agent.support.ExecutionContextManager;

import javax.ws.rs.Path;

public class ClientResourceV1 {

  private final JobStore jobStore;
  private final ExecutionContextManager ecm;

  public ClientResourceV1(ExecutionContextManager ecm, JobStore jobStore) {
    this.ecm = ecm;
    this.jobStore = jobStore;
  }

  @Path("/jobs")
  public JobsResourceV1 getNotificationsResource() {
    return new JobsResourceV1(ecm, jobStore);
  }
}
