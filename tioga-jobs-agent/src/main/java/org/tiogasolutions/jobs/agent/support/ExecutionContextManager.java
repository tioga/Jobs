package org.tiogasolutions.jobs.agent.support;

import org.tiogasolutions.jobs.agent.entities.DomainProfileEntity;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

public class ExecutionContextManager {

  private final ThreadLocal<ExecutionContext> threadLocal = new ThreadLocal<>();

  public ExecutionContext create(DomainProfileEntity domainProfileEntity) {
    ExecutionContext context = new ExecutionContext(domainProfileEntity);
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
