/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.jobs.agent.engine.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileStore;
import org.tiogasolutions.jobs.kernel.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class RootResourceV1 {

  private static final Log log = LogFactory.getLog(RootResourceV1.class);

  @Autowired
  private ExecutionContextManager executionContextManager;

  @Autowired
  private DomainProfileStore domainProfileStore;

  @Autowired
  private JobExecutionRequestStore jobExecutionRequestStore;

  @Autowired
  private JobDefinitionStore jobDefinitionStore;

  private @Context UriInfo uriInfo;

  public RootResourceV1() throws Exception {
    log.info("Created ");
  }

  @GET @Path("/ping")
  @Produces(MediaType.TEXT_HTML)
  public Response healthCheck$GET() {
    return Response.status(Response.Status.OK).build();
  }

  @Path("/api/v1/client")
  public ClientResourceV1 getClientResource() throws Exception {
    return new ClientResourceV1(executionContextManager, jobExecutionRequestStore, jobDefinitionStore);
  }

  @Path("/api/v1/admin")
  public AdminResourceV1 getAdminResource() throws Exception {
    return new AdminResourceV1(executionContextManager, domainProfileStore, jobExecutionRequestStore);
  }
}

