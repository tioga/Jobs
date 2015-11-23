/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package org.tiogasolutions.jobs.kernel;

import org.tiogasolutions.jobs.agent.core.resources.RootResourceV1;
import org.tiogasolutions.jobs.agent.core.support.JobsFilter;
import org.tiogasolutions.jobs.agent.core.support.JobsJaxRsExceptionMapper;
import org.tiogasolutions.jobs.agent.core.support.JobsReaderWriterProvider;
import org.tiogasolutions.jobs.agent.core.view.LocalResourceMessageBodyWriter;
import org.tiogasolutions.jobs.agent.core.view.ThymeleafMessageBodyWriter;

import javax.ws.rs.core.Application;
import java.util.*;

public class JobsAgentApplication extends Application {

  private final Set<Class<?>> classes = new HashSet<>();
  private final Map<String, Object> properties = new HashMap<>();

  public JobsAgentApplication() {

    classes.add(JobsFilter.class);
    classes.add(RootResourceV1.class);
    classes.add(JobsReaderWriterProvider.class);
    classes.add(JobsJaxRsExceptionMapper.class);
    classes.add(ThymeleafMessageBodyWriter.class);
    classes.add(LocalResourceMessageBodyWriter.class);
  }

  @Override
  public Set<Class<?>> getClasses() {
    return classes;
  }

  @Override
  public Map<String, Object> getProperties() {
    return properties;
  }
}
