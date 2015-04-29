package org.tiogasolutions.jobs.kernel.support;

import org.tiogasolutions.jobs.kernel.entities.DomainProfileEntity;

public class ExecutionContext {

  private DomainProfileEntity domainProfileEntity;

  public ExecutionContext(DomainProfileEntity domainProfileEntity) {
    this.domainProfileEntity = domainProfileEntity;
  }

  public DomainProfileEntity getDomainProfileEntity() {
    return domainProfileEntity;
  }
}
