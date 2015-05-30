package org.tiogasolutions.jobs.agent.core.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tiogasolutions.dev.common.exceptions.ApiException;
import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;
import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.dev.domain.query.QueryResult;
import org.tiogasolutions.jobs.agent.core.actions.JobActionExecutor;
import org.tiogasolutions.jobs.agent.core.actions.OsActionExecutor;
import org.tiogasolutions.jobs.kernel.entities.JobDefinitionEntity;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestEntity;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.kernel.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.pub.*;
import org.tiogasolutions.jobs.pub.JobAction;
import org.tiogasolutions.jobs.pub.JobActionResult;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.*;

public class JobsResourceV1 {

  private static final Log log = LogFactory.getLog(JobsResourceV1.class);
  private static final ExecutorService executor = Executors.newCachedThreadPool();

  private final JobDefinitionStore jobDefinitionStore;
  private final JobExecutionRequestStore jobExecutionRequestStore;

  private final Map<ActionType,JobActionExecutor> executors = new HashMap<>();

  public JobsResourceV1(JobExecutionRequestStore jobExecutionRequestStore, JobDefinitionStore jobDefinitionStore) {
    this.jobDefinitionStore = jobDefinitionStore;
    this.jobExecutionRequestStore = jobExecutionRequestStore;

    executors.put(ActionType.osCommand, new OsActionExecutor(jobExecutionRequestStore));
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public QueryResult<JobDefinition> getJobs(@Context Application app) {
    List<JobDefinition> jobDefinitions = new ArrayList<>();
    jobDefinitionStore.getAll().stream().forEach(jobEntity -> jobDefinitions.add(jobEntity.toJobDefinition()));
    return ListQueryResult.newComplete(JobDefinition.class, jobDefinitions);
  }

  private JobDefinitionEntity loadJob(Application app, String jobDefinitionId) {

    JobDefinitionEntity jobDefinitionEntity = jobDefinitionStore.getByDocumentId(jobDefinitionId);

    if (jobDefinitionEntity == null) {
      String msg = String.format("The job definition \"%s\" does not exist.", jobDefinitionId);
      throw ApiNotFoundException.notFound(msg);
    }

    return jobDefinitionEntity;
  }

  @GET
  @Path("/{jobDefinitionId}")
  @Produces(MediaType.APPLICATION_JSON)
  public JobDefinition getJob(@Context Application app,
                              @PathParam("jobDefinitionId") String jobDefinitionId) throws Exception {

    return loadJob(app, jobDefinitionId).toJobDefinition();
  }

  @POST
  @Path("/{jobDefinitionId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response execute(@Context Application app,
                                     @Context UriInfo uriInfo,
                                     @PathParam("jobDefinitionId") String jobDefinitionId,
                                     JobParameters jobParameters) throws Exception {

    return execute(app, uriInfo, jobDefinitionId, -1, jobParameters);
  }

  @POST
  @Path("/{jobDefinitionId}/actions/{actionIndex}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response execute(@Context Application app,
                                     @Context UriInfo uriInfo,
                                     @PathParam("jobDefinitionId") String jobDefinitionId,
                                     @PathParam("actionIndex") int actionIndex,
                                     JobParameters jobParameters) throws Exception {

    if (jobParameters == null) {
      throw ApiException.badRequest("The job parameters must be specified.");
    }

    JobDefinitionEntity jobDefinitionEntity = loadJob(app, jobDefinitionId);
    JobExecutionRequestEntity requestEntity = JobExecutionRequestEntity.newEntity(jobDefinitionEntity, jobParameters);
    jobExecutionRequestStore.create(requestEntity);

    JobExecutionRequest request;
    if (jobParameters.isSynchronous()) {
      request = executeJob(app, requestEntity, jobDefinitionEntity, actionIndex);

    } else {
      executor.submit(() -> executeJob(app, requestEntity, jobDefinitionEntity, actionIndex));
      request = requestEntity.toJobExecutionRequest();
    }

    if (requestEntity.hasFailure()) {
      return Response.status(500).entity(request).build();
    } else if (jobParameters.isSynchronous()) {
      return Response.status(200).entity(request).build();
    } else {
      return Response.status(202).entity(request).build();
    }
  }

  private JobExecutionRequest executeJob(Application app, JobExecutionRequestEntity request, JobDefinitionEntity jobDefinitionEntity, int actionIndex) throws Exception {

    List<JobAction> actions = jobDefinitionEntity.getJobActions();
    if (actionIndex >= 0) {
      actions = singletonList(actions.get(actionIndex));
    }

    for (JobAction action : actions) {

      JobActionResult result;

      if (action.getLock() == null) {
        result = processAction(app, request, action);

      } else {
        log.warn(String.format("Locking action %s for request %s", action.getLabel(), request.getJobExecutionRequestId()));
        synchronized (action.getLock().intern()) {
          result = processAction(app, request, action);
        }
      }

      if (result.hasFailure()) {
        break;
      }
    }

    // One last update to make sure everything is current.
    jobExecutionRequestStore.update(request);

    return request.toJobExecutionRequest();
  }

  private JobActionResult processAction(Application app, JobExecutionRequestEntity request, JobAction action) throws Exception {
    log.warn(String.format("Executing action %s for request %s", action.getLabel(), request.getJobExecutionRequestId()));

    ActionType actionType = action.getActionType();
    JobActionExecutor executor = executors.get(actionType);

    if (executor == null) {
      String msg = String.format("The action type \"%s\" is not supported.", actionType);
      throw new UnsupportedOperationException(msg);
    }

    ZonedDateTime startedAt = ZonedDateTime.now();

    try {
      return executor.execute(app, request, action, startedAt);

    } catch (Exception ex) {
      log.error("Unhandled exception while processing action", ex);
      return JobActionResult.fail(action.getLabel(), startedAt, ex, "", "");
    }
  }
}
