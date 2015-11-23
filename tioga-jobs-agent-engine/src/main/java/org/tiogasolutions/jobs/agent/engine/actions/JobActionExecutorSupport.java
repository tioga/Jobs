package org.tiogasolutions.jobs.agent.engine.actions;

import org.tiogasolutions.jobs.agent.engine.resources.JobVariable;
import org.tiogasolutions.jobs.kernel.entities.JobExecutionRequestEntity;

public abstract class JobActionExecutorSupport implements JobActionExecutor {

  public String substitute(JobExecutionRequestEntity request, String value) {
    JobVariable variable = JobVariable.findFirst(value);
    while (variable != null) {
      value = variable.replace(request.getSubstitutions(), value);
      variable = JobVariable.findFirst(value);
    }
    return value;
  }
}
