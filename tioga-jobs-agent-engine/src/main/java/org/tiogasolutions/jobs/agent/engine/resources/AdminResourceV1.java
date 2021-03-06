package org.tiogasolutions.jobs.agent.engine.resources;

import org.tiogasolutions.jobs.kernel.entities.DomainProfileEntity;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileStore;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class AdminResourceV1 {

  private final ExecutionContextManager executionContextManager;
  private final DomainProfileStore domainProfileStore;
  private final JobExecutionRequestStore jobExecutionRequestStore;

  public AdminResourceV1(ExecutionContextManager executionContextManager, DomainProfileStore domainProfileStore, JobExecutionRequestStore jobExecutionRequestStore) {
    this.executionContextManager = executionContextManager;
    this.domainProfileStore = domainProfileStore;
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  @Path("/domains/{domainName}/request")
  public RequestResourceV1 getRequestResource(@PathParam("domainName") String domainName) {

    DomainProfileEntity domainProfile = domainProfileStore.getByDomainName(domainName);

    executionContextManager.create(domainProfile);
    return new RequestResourceV1(jobExecutionRequestStore);
  }
}
