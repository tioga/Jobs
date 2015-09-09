package org.tiogasolutions.jobs.coordinator.engine.resources.v1;

import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;
import org.tiogasolutions.dev.common.net.InetMediaType;
import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.jobs.common.engine.LinkBuilder;
import org.tiogasolutions.jobs.kernel.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.pub.JobDefinition;
import org.tiogasolutions.jobs.pub.ResponseInfo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Produces({MediaType.APPLICATION_JSON})
public class AdminResourceV1 {

  private final UriInfo uriInfo;
  private final JobDefinitionStore jobDefinitionStore;

  public AdminResourceV1(UriInfo uriInfo, JobDefinitionStore jobDefinitionStore) {
    this.uriInfo = uriInfo;
    this.jobDefinitionStore = jobDefinitionStore;
  }

  @GET
  public Response getDefaultPage() {
    LinkBuilder linkBuilder = new LinkBuilder(uriInfo);
    linkBuilder.link("self").path("api/v1/admin").build();
    linkBuilder.link("client").path("api/v1/client").build();

    return Response.ok(new ResponseInfo(200, linkBuilder.toMap()))
      .links(linkBuilder.toLinkArray())
      .build();
  }

  @GET
  @Path("/domains/{domainName}/jobs")
  public Response getDomainProfile(@PathParam("domainName") String domainName) {
    try {
      List<JobDefinition> jobDefinitions = new ArrayList<>();
      jobDefinitionStore.getAll().stream().forEach(jobEntity -> jobDefinitions.add(jobEntity.toJobDefinition()));
      QueryResult<JobDefinition> result = ListQueryResult.newComplete(JobDefinition.class, jobDefinitions);
      return Response.ok(result).build();

    } catch(ApiNotFoundException e) {
      return Response.status(404).entity(e).build();
    }
  }
}
