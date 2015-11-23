/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package org.tiogasolutions.jobs.kernel;

import org.springframework.stereotype.Component;
import org.tiogasolutions.jobs.kernel.resources.RootResourceV1;
import org.tiogasolutions.jobs.kernel.support.JobsJaxRsExceptionMapper;
import org.tiogasolutions.jobs.kernel.support.JobsReaderWriterProvider;
import org.tiogasolutions.jobs.kernel.view.LocalResourceMessageBodyWriter;
import org.tiogasolutions.jobs.kernel.view.ThymeleafMessageBodyWriter;

import javax.ws.rs.core.Application;
import java.util.*;

@Component
public class JobsAgentApplication extends Application {

  private final Set<Class<?>> classes = new HashSet<>();
  private final Map<String, Object> properties = new HashMap<>();

  public JobsAgentApplication() {

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
