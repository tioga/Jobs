package org.tiogasolutions.jobs.agent.resources;

import org.tiogasolutions.jobs.agent.JobsApplication;
import org.tiogasolutions.jobs.agent.entities.DomainProfileEntity;
import org.tiogasolutions.jobs.agent.entities.DomainProfileStore;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

public class AdminResourceV1 {

  public AdminResourceV1() {
  }

  @Path("/domains/{domainName}/request")
  public RequestResourceV1 getRequestResource(@Context Application app,
                                              @PathParam("domainName") String domainName) {

    DomainProfileStore store = (DomainProfileStore) app.getProperties().get(DomainProfileStore.class.getName());
    DomainProfileEntity domainProfile = store.getByDomainName(domainName);

    JobsApplication.get(app, ExecutionContextManager.class).create(domainProfile);
    return new RequestResourceV1(domainProfile);
  }
}
