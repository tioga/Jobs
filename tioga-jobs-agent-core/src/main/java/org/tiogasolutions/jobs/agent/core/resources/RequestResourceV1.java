package org.tiogasolutions.jobs.agent.core.resources;

import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileEntity;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestEntity;

import javax.ws.rs.DELETE;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

public class RequestResourceV1 {

  private final DomainProfileEntity domainProfile;

  public RequestResourceV1(DomainProfileEntity domainProfile) {
    this.domainProfile = domainProfile;
  }

  @DELETE
  public void deleteAll(@Context Application app) {
    JobExecutionRequestStore store = (JobExecutionRequestStore)app.getProperties().get(JobExecutionRequestStore.class.getName());
    for (JobExecutionRequestEntity request : store.getAll()) {
      store.delete(request);
    }
  }
}
