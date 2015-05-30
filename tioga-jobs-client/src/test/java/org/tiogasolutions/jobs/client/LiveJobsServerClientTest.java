package org.tiogasolutions.jobs.client;

import org.glassfish.jersey.test.JerseyTestNg;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.jobs.agent.core.JobsAgentApplication;
import org.tiogasolutions.jobs.agent.core.TestFactory;
import org.tiogasolutions.jobs.pub.*;
import org.tiogasolutions.jobs.pub.JobAction;
import org.tiogasolutions.jobs.pub.JobActionResult;
import org.tiogasolutions.jobs.pub.OsAction;

import javax.inject.Inject;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

@Test(enabled = false)
public class LiveJobsServerClientTest extends JerseyTestNg.ContainerPerClassTest {

  private final String jobAId = "deac9600-d96e-11e4-adce-b8ca3a8e2d05";
  private final String jobBId = "08f20270-be1b-11e4-af58-b8ca3a8e2d05";

  @Inject
  private TestFactory testFactory;

  @Override
  protected JobsAgentApplication configure() {
    return new JobsAgentApplication("test", "classpath:/spring-test-jobs-client.xml");
  }

  @BeforeClass
  public void beforeClass() throws Exception {
  }

  protected Invocation.Builder request(WebTarget webTarget) {
    String httpAuth = toHttpAuth(TestFactory.API_KEY, TestFactory.API_PASSWORD);
    return webTarget.request().header("Authorization", httpAuth);
  }

  protected String toHttpAuth(String username, String password) {
    return testFactory.toHttpAuth(username, password);
  }

  private LiveJobsServerClient getClient() {
    URI uri = UriBuilder.fromUri(getBaseUri()).path("api").path("v1").build();
    return new LiveJobsServerClient(TestFactory.API_KEY, TestFactory.API_PASSWORD, uri);
  }

  public void testExecuteJobA() throws Exception {
    JobParameters parameters = JobParameters.createSynchronous();
    JobExecutionRequest request = getClient().start(jobAId, parameters);

    assertNotNull(request);
    assertNotNull(request.getRevision());
    assertNotNull(request.getJobExecutionRequestId());

    parameters = request.getJobParameters();
    assertNotNull(parameters);
    assertEquals(parameters.getCallbackUrl(), null);
    assertTrue(parameters.isSynchronous());
    assertFalse(parameters.isAsynchronous());

    List<JobActionResult> results = request.getResults();
    assertNotNull(results);
    assertEquals(results.size(), 1);

    JobActionResult result = results.get(0);
    assertNotNull(result);
    assertEquals(result.hasFailure(), false);

    assertNotNull(result.getOut());
    String out = result.getOut().trim();

    assertTrue(out.startsWith("Volume in drive "));
    assertTrue(out.contains("Directory of c:\\"));
    assertTrue(out.endsWith("bytes free"));

    assertEquals(result.getErr(), "");
  }

  public void testExecuteJobB() throws Exception {
    JobParameters parameters = JobParameters.createSynchronous();
    JobExecutionRequest request = getClient().start(jobBId, parameters);

    assertNotNull(request);
    assertNotNull(request.getRevision());
    assertNotNull(request.getJobExecutionRequestId());

    parameters = request.getJobParameters();
    assertNotNull(parameters);
    assertEquals(parameters.getCallbackUrl(), null);
    assertTrue(parameters.isSynchronous());
    assertFalse(parameters.isAsynchronous());

    List<JobActionResult> results = request.getResults();
    assertNotNull(results);
    assertEquals(results.size(), 1);

    JobActionResult result = results.get(0);
    assertNotNull(result);
    assertEquals(result.getFailure(), String.valueOf(Integer.MIN_VALUE));

    assertNotNull(result.getOut());
    String out = result.getOut().trim();

    assertTrue(out.startsWith("java.io.IOException: Cannot run program \"fail-doing-this-and-that\""));
    assertTrue(out.contains("Caused by: java.io.IOException"));
    assertTrue(out.endsWith("... 30 more"));

    assertEquals(result.getErr(), "");
  }

  public void testGetJobA() throws Exception {
    JobDefinition jobDef = getClient().getJob(jobAId);
    validateJobA(jobDef);
  }

  public void testGetJobB() throws Exception {
    JobDefinition jobDef = getClient().getJob(jobBId);
    validateJobB(jobDef);
  }

  public void testGetJobs() throws Exception {
    QueryResult<JobDefinition> result = getClient().getJobs();

    assertNotNull(result);
    assertEquals(result.getSize(), 2);
    assertEquals(result.getContainsType(), JobDefinition.class);

    for (JobDefinition jobDef : result) {
      if (jobAId.equals(jobDef.getJobDefinitionId())) {
        validateJobA(jobDef);
      } else if (jobBId.equals(jobDef.getJobDefinitionId())) {
        validateJobB(jobDef);
      }
    }
  }

  private void validateJobA(JobDefinition jobDef) {
    assertNotNull(jobDef);
    assertEquals(jobDef.getJobDefinitionId(), jobAId);
    assertNotNull(jobDef.getRevision());

    List<JobAction> actions = jobDef.getJobActions();
    assertNotNull(actions);
    assertEquals(actions.size(), 1);

    OsAction action = (OsAction)actions.get(0);
    assertNotNull(action);
    assertTrue(action.getActionType().isOsCommand());
    assertEquals(action.getCommand(), "C:\\Windows\\system32\\cmd.exe /c dir");
    assertEquals(action.getTimeout(), 60);
    assertEquals(action.getTimeoutUnit(), TimeUnit.SECONDS);
    assertEquals(action.getWorkingDirectory(), new File("c:\\"));
  }

  private void validateJobB(JobDefinition jobDef) {
    assertNotNull(jobDef);
    assertEquals(jobDef.getJobDefinitionId(), jobBId);
    assertNotNull(jobDef.getRevision());

    List<JobAction> actions = jobDef.getJobActions();
    assertNotNull(actions);
    assertEquals(actions.size(), 1);

    OsAction action = (OsAction)actions.get(0);
    assertNotNull(action);
    assertTrue(action.getActionType().isOsCommand());
    assertEquals(action.getCommand(), "fail-doing-this-and-that");
    assertEquals(action.getTimeout(), 60);
    assertEquals(action.getTimeoutUnit(), TimeUnit.SECONDS);
    assertEquals(action.getWorkingDirectory(), new File("c:\\"));
  }
}
