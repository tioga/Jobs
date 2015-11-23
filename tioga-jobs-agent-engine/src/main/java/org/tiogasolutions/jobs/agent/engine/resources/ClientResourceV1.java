package org.tiogasolutions.jobs.agent.engine.resources;

import org.tiogasolutions.jobs.kernel.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;
import org.tiogasolutions.jobs.pub.DomainProfile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class ClientResourceV1 {

  private final JobDefinitionStore jobDefinitionStore;
  private final JobExecutionRequestStore jobExecutionRequestStore;
  private final ExecutionContextManager executionContextManager;

  public ClientResourceV1(ExecutionContextManager executionContextManager, JobExecutionRequestStore jobExecutionRequestStore, JobDefinitionStore jobDefinitionStore) {
    this.executionContextManager = executionContextManager;
    this.jobDefinitionStore = jobDefinitionStore;
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public DomainProfile getDomainProfile() {
    return executionContextManager.getExecutionContext().getDomainProfileEntity().toDomainProfile();
  }

  @GET
  @Path("sign-in")
  @Produces({MediaType.APPLICATION_JSON})
  public DomainProfile signIn() {
    return getDomainProfile();
  }

  @Path("/jobs")
  public JobsResourceV1 getJobsResourceV1() {
    return new JobsResourceV1(jobExecutionRequestStore, jobDefinitionStore);
  }

  @Path("/requests")
  public RequestResourceV1 getRequestResourceV1() {
    return new RequestResourceV1(jobExecutionRequestStore);
  }
}
