package org.tiogasolutions.jobs.agent.resources;

import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;
import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionEntity;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.pub.JobDefinition;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class JobsResourceV1 {

  private final JobDefinitionStore jobDefinitionStore;
  private final ExecutionContextManager ecm;

  public JobsResourceV1(ExecutionContextManager ecm, JobDefinitionStore jobDefinitionStore) {
    this.ecm = ecm;
    this.jobDefinitionStore = jobDefinitionStore;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public QueryResult<JobDefinition> getJobs() {
    List<JobDefinition> jobDefinitions = new ArrayList<>();
    jobDefinitionStore.getAll().stream().forEach( jobEntity -> jobDefinitions.add(jobEntity.toJobDefinition()) );
    return ListQueryResult.newComplete(JobDefinition.class, jobDefinitions);
  }

  @Path("/{jobId}")
  public JobResourceV1 getJobResourceV1(@PathParam("jobId") String jobId) throws Exception {
    JobDefinitionEntity jobDefinitionEntity = jobDefinitionStore.getByDocumentId(jobId);

    if (jobDefinitionEntity == null) {
      String msg = String.format("The job \"%s\" does not exist.", jobId);
      throw ApiNotFoundException.notFound(msg);
    }

    return new JobResourceV1(ecm, jobDefinitionEntity);
  }
}
