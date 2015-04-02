package org.tiogasolutions.workhorse.agent.support;

import org.tiogasolutions.workhorse.agent.entities.DomainProfileEntity;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

/**
 * Created by jacobp on 4/1/2015.
 */
public class ExecutionContextManager {

  private final ThreadLocal<ExecutionContext> threadLocal = new ThreadLocal<>();

  public ExecutionContext create(Application application, UriInfo uriInfo, HttpHeaders headers, DomainProfileEntity domainProfileEntity) {
    ExecutionContext context = new ExecutionContext(application, uriInfo, headers, domainProfileEntity);
    threadLocal.set(context);
    return context;
  }

  public ExecutionContext getExecutionContext() {
    return threadLocal.get();
  }

  public void clearContext() {
    threadLocal.remove();
  }
}
