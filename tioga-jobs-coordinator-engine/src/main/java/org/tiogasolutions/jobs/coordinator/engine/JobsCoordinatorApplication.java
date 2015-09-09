package org.tiogasolutions.jobs.coordinator.engine;

import org.tiogasolutions.jobs.common.engine.JobsJaxRsExceptionMapper;
import org.tiogasolutions.jobs.common.engine.JobsReaderWriterProvider;
import org.tiogasolutions.jobs.common.engine.view.LocalResourceMessageBodyWriter;
import org.tiogasolutions.jobs.common.engine.view.ThymeleafMessageBodyWriter;
import org.tiogasolutions.jobs.coordinator.engine.resources.v1.CoordinatorRootResourceV1;
import org.tiogasolutions.lib.spring.jaxrs.TiogaSpringApplication;

import java.util.*;

public class JobsCoordinatorApplication extends TiogaSpringApplication {

  public JobsCoordinatorApplication(String profile, String springFile) {
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

    // Filters
    // classes.add(JobsFilter.class);

    // Resources
    classes.add(CoordinatorRootResourceV1.class);

    // Shared
    classes.add(JobsReaderWriterProvider.class);
    classes.add(JobsJaxRsExceptionMapper.class);
    classes.add(ThymeleafMessageBodyWriter.class);
    classes.add(LocalResourceMessageBodyWriter.class);

    return Collections.unmodifiableSet(classes);
  }
}
