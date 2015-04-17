package org.tiogasolutions.jobs.agent.grizzly;

import org.glassfish.jersey.server.ResourceConfig;
import org.tiogasolutions.jobs.agent.JobsApplication;

public class JobsResourceConfig extends ResourceConfig {

  public JobsResourceConfig(JobsApplication application) {

    application.getClasses().forEach(this::register);

    application.getSingletons().forEach(this::register);

    addProperties(application.getProperties());
  }
}
