package org.tiogasolutions.jobs.kernel.resources;

import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.pub.JobExecutionRequest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

public class RequestResourceV1 {

  private final JobExecutionRequestStore jobExecutionRequestStore;


  public RequestResourceV1(JobExecutionRequestStore jobExecutionRequestStore) {
    this.jobExecutionRequestStore = jobExecutionRequestStore;
  }

  @GET
  public QueryResult<JobExecutionRequest> getJobExecutionRequests() {
    List<JobExecutionRequest> requests = new ArrayList<>();
    jobExecutionRequestStore.getAll().stream().forEach(request -> requests.add(request.toJobExecutionRequest()));
    return ListQueryResult.newComplete(JobExecutionRequest.class, requests);
  }

  @DELETE
  public void deleteAll(@Context Application app) {
    jobExecutionRequestStore.getAll().forEach(jobExecutionRequestStore::delete);
  }
}
