package org.tiogasolutions.workhorse.agent.resources;

import org.crazyyak.dev.domain.query.ListQueryResult;
import org.crazyyak.dev.domain.query.QueryResult;
import org.tiogasolutions.workhorse.agent.entities.JobEntity;
import org.tiogasolutions.workhorse.agent.entities.JobStore;
import org.tiogasolutions.workhorse.agent.support.ExecutionContextManager;
import org.tiogasolutions.workhorse.pub.Job;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class JobsResourceV1 {

  private final JobStore jobStore;
  private final ExecutionContextManager ecm;

  public JobsResourceV1(ExecutionContextManager ecm, JobStore jobStore) {
    this.ecm = ecm;
    this.jobStore = jobStore;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public QueryResult<Job> getJobs() {
    List<Job> jobs = new ArrayList<>();
    jobStore.getAll().stream().forEach( jobEntity -> jobs.add(jobEntity.toJob()) );
    return ListQueryResult.newComplete(Job.class, jobs);
  }

  @Path("/{jobId}")
  public JobResourceV1 getJobResourceV1(@PathParam("jobId") String jobId) throws Exception {
    JobEntity jobEntity = jobStore.getByDocumentId(jobId);
    return new JobResourceV1(ecm, jobEntity);
  }
}
