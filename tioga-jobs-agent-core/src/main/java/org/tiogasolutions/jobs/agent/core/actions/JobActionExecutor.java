package org.tiogasolutions.jobs.agent.core.actions;

import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestEntity;
import org.tiogasolutions.jobs.pub.JobAction;
import org.tiogasolutions.jobs.pub.JobActionResult;

import java.time.ZonedDateTime;

public interface JobActionExecutor {

  JobActionResult execute(JobExecutionRequestEntity request, JobAction action, ZonedDateTime startedAt) throws Exception;

}
