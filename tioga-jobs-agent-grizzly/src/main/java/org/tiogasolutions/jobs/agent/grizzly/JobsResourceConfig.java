package org.tiogasolutions.jobs.agent.grizzly;

import org.glassfish.jersey.server.ResourceConfig;
import org.tiogasolutions.jobs.agent.core.JobsAgentApplication;

public class JobsResourceConfig extends ResourceConfig {

  public JobsResourceConfig(JobsAgentApplication application) {

    application.getClasses().forEach(this::register);

    application.getSingletons().forEach(this::register);

    addProperties(application.getProperties());
  }
}
