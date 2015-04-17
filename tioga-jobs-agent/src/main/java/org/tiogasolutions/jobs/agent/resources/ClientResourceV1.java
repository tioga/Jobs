package org.tiogasolutions.jobs.agent.resources;

import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;

import javax.ws.rs.Path;

public class ClientResourceV1 {

  public ClientResourceV1() {
  }

  @Path("/jobs")
  public JobsResourceV1 getJobsResourceResource() {
    return new JobsResourceV1();
  }
}
