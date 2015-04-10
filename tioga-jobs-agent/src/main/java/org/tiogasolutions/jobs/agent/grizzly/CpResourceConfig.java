package org.tiogasolutions.jobs.agent.grizzly;

import org.glassfish.jersey.server.ResourceConfig;
import org.tiogasolutions.jobs.agent.WhApplication;

public class CpResourceConfig extends ResourceConfig {

  public CpResourceConfig(WhApplication application) {

    application.getClasses().forEach(this::register);

    application.getSingletons().forEach(this::register);

    addProperties(application.getProperties());
  }
}
