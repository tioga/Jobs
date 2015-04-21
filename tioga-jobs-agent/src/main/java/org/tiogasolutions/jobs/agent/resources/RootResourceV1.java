/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.jobs.agent.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tiogasolutions.jobs.agent.JobsApplication;
import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.support.JobsCouchServer;
import org.tiogasolutions.jobs.agent.view.Thymeleaf;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.agent.view.ThymeleafViewFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class RootResourceV1 extends RootResourceSupport {

  private static final Log log = LogFactory.getLog(RootResourceV1.class);

  private @Context UriInfo uriInfo;

  public RootResourceV1() throws Exception {
    log.info("Created ");
  }

  @Override
  public UriInfo getUriInfo() {
    return uriInfo;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf getWelcome() throws Exception {
    return new Thymeleaf(ThymeleafViewFactory.WELCOME);
  }

  @GET @Path("/ping")
  @Produces(MediaType.TEXT_HTML)
  public Response healthCheck$GET() {
    return Response.status(Response.Status.OK).build();
  }

  @Path("/api/v1/client")
  public ClientResourceV1 getClientResource() throws Exception {
    return new ClientResourceV1();
  }

  @Path("/api/v1/admin")
  public AdminResourceV1 getAdminResource() throws Exception {
    return new AdminResourceV1();
  }
}

