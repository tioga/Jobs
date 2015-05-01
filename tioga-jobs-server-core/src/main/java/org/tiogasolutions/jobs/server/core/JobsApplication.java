package org.tiogasolutions.jobs.server.core;

import org.tiogasolutions.lib.spring.jaxrs.TiogaSpringApplication;

import java.util.*;

public class JobsApplication extends TiogaSpringApplication {

  public JobsApplication(String profile, String springFile) {
    super(profile, springFile, createProperties(), createClasses(), createSingletons());
  }

  private static Set<Object> createSingletons() {
    return Collections.emptySet();
  }

  private static Map<String,Object> createProperties() {
    Map<String,Object> properties = new HashMap<>();

    properties.put("app.admin.context", "/api/v1/admin");
    properties.put("app.client.context", "/api/v1/client");

    return properties;
  }

  private static Set<Class<?>> createClasses() {
    Set<Class<?>> classes = new HashSet<>();
    return Collections.unmodifiableSet(classes);
  }
}
