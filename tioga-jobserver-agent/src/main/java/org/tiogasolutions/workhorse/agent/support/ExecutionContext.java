package org.tiogasolutions.workhorse.agent.support;

import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.workhorse.agent.entities.DomainProfileEntity;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ExecutionContext {

  private URI baseURI;
  private DomainProfileEntity domainProfileEntity;

  private UriInfo uriInfo;
  private HttpHeaders headers;

  private Application application;

  public ExecutionContext(Application application, UriInfo uriInfo, HttpHeaders headers, DomainProfileEntity domainProfileEntity) {
    this.domainProfileEntity = domainProfileEntity;
    this.uriInfo = uriInfo;
    this.headers = headers;
    this.application = application;

    String uri = uriInfo.getBaseUri().toASCIIString();
    this.baseURI = URI.create(StringUtils.substring(uri, 0, -1));
  }

  public DomainProfileEntity getDomainProfileEntity() {
    return domainProfileEntity;
  }

  public UriInfo getUriInfo() {
    return uriInfo;
  }

  public URI getBaseURI() {
    return baseURI;
  }

  public Application getApplication() {
    return application;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }
}
