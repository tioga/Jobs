package org.tiogasolutions.jobs.coordinator.engine.resources.v1;

import org.tiogasolutions.jobs.common.engine.LinkBuilder;
import org.tiogasolutions.jobs.common.engine.resources.AbstractRootResource;
import org.tiogasolutions.jobs.kernel.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.pub.ResponseInfo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
@Produces({MediaType.APPLICATION_JSON})
public class CoordinatorRootResourceV1 extends AbstractRootResource {

  @Inject
  private JobDefinitionStore jobDefinitionStore;

  @Context
  private UriInfo uriInfo;

  public CoordinatorRootResourceV1() {
  }

  @Override
  public UriInfo getUriInfo() {
    return uriInfo;
  }

  @GET
  public Response getRoot() {
    LinkBuilder linkBuilder = new LinkBuilder(uriInfo);
    linkBuilder.link("admin").path("api/v1/admin").build();
    linkBuilder.link("client").path("api/v1/client").build();
    return Response.ok(new ResponseInfo(200, linkBuilder.toMap()))
      .links(linkBuilder.toLinkArray())
      .build();
  }

  @Path("/api/v1/admin")
  public AdminResourceV1 getAdminResource() {
    return new AdminResourceV1(uriInfo, jobDefinitionStore);
  }
}
