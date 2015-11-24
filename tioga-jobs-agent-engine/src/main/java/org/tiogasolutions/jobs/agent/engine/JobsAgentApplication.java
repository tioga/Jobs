/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package org.tiogasolutions.jobs.agent.engine;

import org.springframework.stereotype.Component;
import org.tiogasolutions.jobs.agent.engine.resources.RootResourceV1;
import org.tiogasolutions.jobs.agent.engine.support.JobsRequestFilter;
import org.tiogasolutions.jobs.agent.engine.support.JobsResponseFilter;

import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class JobsAgentApplication extends Application {

  private final Set<Class<?>> classes = new HashSet<>();
  private final Map<String, Object> properties = new HashMap<>();

  public JobsAgentApplication() {

    classes.add(JobsResponseFilter.class);
    classes.add(JobsRequestFilter.class);

    classes.add(RootResourceV1.class);
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
