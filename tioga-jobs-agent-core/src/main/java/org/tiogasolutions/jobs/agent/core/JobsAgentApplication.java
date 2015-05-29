/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package org.tiogasolutions.jobs.agent.core;

import org.tiogasolutions.jobs.agent.core.resources.RootResourceV1;
import org.tiogasolutions.jobs.agent.core.support.JobsFilter;
import org.tiogasolutions.jobs.agent.core.support.JobsJaxRsExceptionMapper;
import org.tiogasolutions.jobs.agent.core.support.JobsReaderWriterProvider;
import org.tiogasolutions.jobs.agent.core.view.LocalResourceMessageBodyWriter;
import org.tiogasolutions.jobs.agent.core.view.ThymeleafMessageBodyWriter;
import org.tiogasolutions.lib.spring.jaxrs.TiogaSpringApplication;

import java.util.*;

public class JobsAgentApplication extends TiogaSpringApplication {

  public JobsAgentApplication(String profile, String springFile) {
    super(profile, springFile, createProperties(), createClasses(), createSingletons());
  }

  private static Set<Object> createSingletons() {
    return Collections.emptySet();
  }

  private static Map<String,Object> createProperties() {
    Map<String,Object> properties = new HashMap<>();

    properties.put("app.admin.context", "/api/v1/admin");
    properties.put("app.client.context", "/api/v1/client");

    properties.put("system.username", "system");
    properties.put("system.password", "password");

    return properties;
  }

  private static Set<Class<?>> createClasses() {
    Set<Class<?>> classes = new HashSet<>();

    classes.add(JobsFilter.class);
    classes.add(RootResourceV1.class);
    classes.add(JobsReaderWriterProvider.class);
    classes.add(JobsJaxRsExceptionMapper.class);
    classes.add(ThymeleafMessageBodyWriter.class);
    classes.add(LocalResourceMessageBodyWriter.class);

    return Collections.unmodifiableSet(classes);
  }
}
