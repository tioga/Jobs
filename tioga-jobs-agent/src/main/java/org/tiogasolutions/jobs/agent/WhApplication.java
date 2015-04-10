/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package org.tiogasolutions.jobs.agent;

import org.apache.log4j.Level;
import org.tiogasolutions.app.logging.LogUtils;
import org.tiogasolutions.jobs.agent.support.WhFilter;
import org.tiogasolutions.jobs.agent.support.WhReaderWriterProvider;
import org.tiogasolutions.jobs.agent.view.ThymeleafMessageBodyWriter;
import org.tiogasolutions.jobs.agent.resources.RootResourceV1;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.support.WhJaxRsExceptionMapper;
import org.tiogasolutions.jobs.agent.view.LocalResourceMessageBodyWriter;

import javax.ws.rs.core.Application;
import java.util.*;

public class WhApplication extends Application {

  private final Set<Class<?>> classes;
  private final Map<String, Object> properties;

  public static final ExecutionContextManager executionContextManager = new ExecutionContextManager();

  public WhApplication() throws Exception {
    // Make sure our logging is working before ANYTHING else.
    LogUtils logUtils = new LogUtils();
    logUtils.initConsoleAppender(Level.WARN, LogUtils.DEFAULT_PATTERN);

    Map<String, Object> properties = new HashMap<>();
    Set<Class<?>> classes = new HashSet<>();

    properties.put("app.admin.context", "/api/v1/admin");
    properties.put("app.client.context", "/api/v1/client");

    properties.put("system.username", "system");
    properties.put("system.password", "password");

    classes.add(WhFilter.class);
    classes.add(RootResourceV1.class);
    classes.add(WhReaderWriterProvider.class);
    classes.add(WhJaxRsExceptionMapper.class);
    classes.add(ThymeleafMessageBodyWriter.class);
    classes.add(LocalResourceMessageBodyWriter.class);

    this.classes = Collections.unmodifiableSet(classes);
    this.properties = Collections.unmodifiableMap(properties);

    checkForDuplicates();
  }

  private void checkForDuplicates() {
    Set<Class> existing = new HashSet<>();

    for (Class type : classes) {
      if (type == null) continue;
      if (existing.contains(type)) {
        String msg = String.format("The class %s has already been registered.", type.getName());
        throw new IllegalArgumentException(msg);
      }
    }

    existing.clear();
  }

  @Override
  public Map<String, Object> getProperties() {
    return properties;
  }
  @Override
  public Set<Class<?>> getClasses() {
    return classes;
  }
}
