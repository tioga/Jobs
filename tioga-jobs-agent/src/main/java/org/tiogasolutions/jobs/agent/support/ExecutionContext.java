package org.tiogasolutions.jobs.agent.support;

import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.jobs.agent.entities.DomainProfileEntity;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ExecutionContext {

  private DomainProfileEntity domainProfileEntity;

  public ExecutionContext(DomainProfileEntity domainProfileEntity) {
    this.domainProfileEntity = domainProfileEntity;
  }

  public DomainProfileEntity getDomainProfileEntity() {
    return domainProfileEntity;
  }
}
