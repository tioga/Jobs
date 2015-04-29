package org.tiogasolutions.jobs.agent.core.resources;

import javax.ws.rs.Path;

public class ClientResourceV1 {

  public ClientResourceV1() {
  }

  @Path("/jobs")
  public JobsResourceV1 getJobsResourceResource() {
    return new JobsResourceV1();
  }
}
