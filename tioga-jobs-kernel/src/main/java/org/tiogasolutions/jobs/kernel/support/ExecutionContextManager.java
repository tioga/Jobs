package org.tiogasolutions.jobs.kernel.support;

import org.tiogasolutions.jobs.kernel.entities.DomainProfileEntity;

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
