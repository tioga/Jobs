package org.tiogasolutions.jobs.agent.engine.resources;

import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileEntity;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestEntity;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

public class RequestResourceV1 {

  private final JobExecutionRequestStore jobExecutionRequestStore;

  private final DomainProfileEntity domainProfile;

  public RequestResourceV1(JobExecutionRequestStore jobExecutionRequestStore, DomainProfileEntity domainProfile) {
    this.domainProfile = domainProfile;
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  @DELETE
  public void deleteAll(@Context Application app) {
    for (JobExecutionRequestEntity request : jobExecutionRequestStore.getAll()) {
      jobExecutionRequestStore.delete(request);
    }
  }
}
