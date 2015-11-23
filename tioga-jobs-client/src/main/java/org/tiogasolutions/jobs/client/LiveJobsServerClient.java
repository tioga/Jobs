package org.tiogasolutions.jobs.client;

import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.dev.jackson.TiogaJacksonTranslator;
import org.tiogasolutions.jobs.jackson.JobsObjectMapper;
import org.tiogasolutions.jobs.pub.JobDefinition;
import org.tiogasolutions.jobs.pub.JobExecutionRequest;
import org.tiogasolutions.jobs.pub.JobParameters;
import org.tiogasolutions.lib.jaxrs.jackson.SimpleRestClient;

import java.net.URI;
import java.util.List;

public class LiveJobsServerClient implements JobsServerClient {

  private final SimpleRestClient client;

//  public LiveJobsServerClient(String userName, String password) {
//    JobsObjectMapper objectMapper = new JobsObjectMapper();
//    TiogaJacksonTranslator translator = new TiogaJacksonTranslator(objectMapper);
//    client = new SimpleRestClient(translator, "http://www.cosmicpush.com/api/v2", userName, password);
//  }

  public LiveJobsServerClient(String userName, String password, URI uri) {
    JobsObjectMapper objectMapper = new JobsObjectMapper();
    TiogaJacksonTranslator translator = new TiogaJacksonTranslator(objectMapper);
    client = new SimpleRestClient(translator, uri.toASCIIString(), userName, password);
  }

  public LiveJobsServerClient(SimpleRestClient client) {
    this.client = client;
  }

  public QueryResult<JobDefinition> getJobs() {
    String path = "/client/jobs";
    return client.getQueryResult(JobDefinition.class, path);
  }

  public JobDefinition getJob(String jobId) {
    String path = String.format("/client/jobs/%s", jobId);
    return client.get(JobDefinition.class, path);
  }

  public JobExecutionRequest start(String jobId, JobParameters parameters) {
    String path = String.format("/client/jobs/%s", jobId);
    return client.post(JobExecutionRequest.class, path, parameters);
  }
}
