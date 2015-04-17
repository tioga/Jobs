/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package org.tiogasolutions.jobs.agent;

import org.apache.log4j.Level;
import org.tiogasolutions.app.logging.LogUtils;
import org.tiogasolutions.jobs.agent.entities.DomainDatabaseConfig;
import org.tiogasolutions.jobs.agent.entities.DomainProfileStore;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.agent.support.*;
import org.tiogasolutions.jobs.agent.view.ThymeleafMessageBodyWriter;
import org.tiogasolutions.jobs.agent.resources.RootResourceV1;
import org.tiogasolutions.jobs.agent.view.LocalResourceMessageBodyWriter;

import javax.ws.rs.core.Application;
import java.util.*;

public class JobsApplication extends Application {

  private final Set<Class<?>> classes;
  private final Map<String, Object> properties;

  private final String databaseName;
  private final String dbPrefix;
  private final String dbSuffix;

  public JobsApplication(String databaseName, String dbPrefix, String dbSuffix) {

    this.dbPrefix = dbPrefix;
    this.dbSuffix = dbSuffix;
    this.databaseName = databaseName;

    // Make sure our logging is working before ANYTHING else.
    LogUtils logUtils = new LogUtils();
    logUtils.initConsoleAppender(Level.WARN, LogUtils.DEFAULT_PATTERN);

    Map<String, Object> properties = new HashMap<>();
    Set<Class<?>> classes = new HashSet<>();

    properties.put("app.admin.context", "/api/v1/admin");
    properties.put("app.client.context", "/api/v1/client");

    properties.put("system.username", "system");
    properties.put("system.password", "password");

    classes.add(JobsFilter.class);
    classes.add(RootResourceV1.class);
    classes.add(JobsReaderWriterProvider.class);
    classes.add(JobsJaxRsExceptionMapper.class);
    classes.add(ThymeleafMessageBodyWriter.class);
    classes.add(LocalResourceMessageBodyWriter.class);

    init(properties);

    this.classes = Collections.unmodifiableSet(classes);
    this.properties = Collections.unmodifiableMap(properties);

    checkForDuplicates();
  }

  private void init(Map<String, Object> properties) {

    JobsCouchServer couchServer = new JobsCouchServer();
    properties.put(JobsCouchServer.class.getName(), couchServer);

    ExecutionContextManager ecm = new ExecutionContextManager();
    properties.put(ExecutionContextManager.class.getName(), ecm);

    DomainProfileStore domainProfileStore = new DomainProfileStore(couchServer, databaseName);
    properties.put(DomainProfileStore.class.getName(), domainProfileStore);

    DomainDatabaseConfig config = new DomainDatabaseConfig(couchServer, ecm, dbPrefix, dbSuffix);

    JobDefinitionStore jobDefinitionStore = new JobDefinitionStore(config);
    properties.put(JobDefinitionStore.class.getName(), jobDefinitionStore);

    JobExecutionRequestStore jobExecutionRequestStore = new JobExecutionRequestStore(config);
    properties.put(JobExecutionRequestStore.class.getName(), jobExecutionRequestStore);
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

  public String getDatabaseName() {
    return databaseName;
  }

  public String getDbPrefix() {
    return dbPrefix;
  }

  public String getDbSuffix() {
    return dbSuffix;
  }

  @Override
  public Map<String, Object> getProperties() {
    return properties;
  }

  @Override
  public Set<Class<?>> getClasses() {
    return classes;
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(Application app, Class<T> type) {
    return (T)app.getProperties().get(type.getName());
  }
}
