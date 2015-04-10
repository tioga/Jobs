package org.tiogasolutions.jobs.agent.resources;

import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;
import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionEntity;
import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.pub.JobDefinition;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class JobsResourceV1 {

  private final ExecutionContextManager ecm;
  private final JobDefinitionStore jobDefinitionStore;
  private final JobExecutionRequestStore jobExecutionRequestStore;

  public JobsResourceV1(ExecutionContextManager ecm, JobDefinitionStore jobDefinitionStore, JobExecutionRequestStore jobExecutionRequestStore) {
    this.ecm = ecm;
    this.jobDefinitionStore = jobDefinitionStore;
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public QueryResult<JobDefinition> getJobs() {
    List<JobDefinition> jobDefinitions = new ArrayList<>();
    jobDefinitionStore.getAll().stream().forEach( jobEntity -> jobDefinitions.add(jobEntity.toJobDefinition()) );
    return ListQueryResult.newComplete(JobDefinition.class, jobDefinitions);
  }

  @Path("/{jobDefinitionId}")
  public JobResourceV1 getJobResourceV1(@PathParam("jobDefinitionId") String jobDefinitionId) throws Exception {
    JobDefinitionEntity jobDefinitionEntity = jobDefinitionStore.getByDocumentId(jobDefinitionId);

    if (jobDefinitionEntity == null) {
      String msg = String.format("The job definition \"%s\" does not exist.", jobDefinitionId);
      throw ApiNotFoundException.notFound(msg);
    }

    return new JobResourceV1(ecm, jobDefinitionEntity, jobExecutionRequestStore);
  }
}
