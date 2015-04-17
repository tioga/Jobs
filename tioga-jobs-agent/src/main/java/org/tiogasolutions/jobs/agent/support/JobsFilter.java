package org.tiogasolutions.jobs.agent.support;

import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.jobs.agent.JobsApplication;
import org.tiogasolutions.jobs.agent.entities.DomainProfileEntity;
import org.tiogasolutions.jobs.agent.entities.DomainProfileStore;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.tiogasolutions.dev.common.EqualsUtils.objectsNotEqual;
import static org.tiogasolutions.dev.common.exceptions.ExceptionUtils.assertNotZeroLength;

/**
 * This is the "global" filter for the Engine. It's primary responsibility is
 * for managing the execution context over the entire lifecycle of a request.
 *
 * All other filters should be processed after this one.
 */
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class JobsFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private UriInfo uriInfo;
  private Application app;

  public JobsFilter() {
  }

  @Context
  private void init(Application app, UriInfo uriInfo) {
    this.app = app;
    this.uriInfo = uriInfo;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    String baseUri = uriInfo.getBaseUri().toString();
    String requestUri = uriInfo.getRequestUri().toString();
    String path = requestUri.substring(baseUri.length() - 1);

    Map<String,Object> properties = app.getProperties();
    String clientContext = assertNotZeroLength((String) properties.get("app.client.context"), "app.client.context");
    String adminContext = assertNotZeroLength((String) properties.get("app.admin.context"), "app.admin.context");

    if (path.equals(clientContext) || path.startsWith(clientContext+"/")) {
      authenticateClientRequest(requestContext);
    } else if (path.equals(adminContext) || path.startsWith(adminContext+"/")) {
      throw new UnsupportedOperationException("Not yet implemented.");
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    JobsApplication.get(app, ExecutionContextManager.class).clearContext();
  }

  private void authenticateClientRequest(ContainerRequestContext requestContext) {
    String authHeader = requestContext.getHeaderString("Authorization");

    if (authHeader == null || authHeader.startsWith("Basic ") == false) {
      throw new NotAuthorizedException("Notify");
    } else {
      authHeader = authHeader.substring(6);
    }

    byte[] bytes = DatatypeConverter.parseBase64Binary(authHeader);
    String basicAuth = new String(bytes, StandardCharsets.UTF_8);

    int pos = basicAuth.indexOf(":");

    String apiUsername;
    String apiPassword;

    if (pos < 0) {
      throw new NotAuthorizedException("API");
    }

    apiUsername = basicAuth.substring(0, pos);
    apiPassword = basicAuth.substring(pos+1);

    if (StringUtils.isBlank(apiUsername) || StringUtils.isBlank(apiPassword)) {
      throw new NotAuthorizedException("API");
    }

    DomainProfileStore store = (DomainProfileStore) app.getProperties().get(DomainProfileStore.class.getName());
    DomainProfileEntity domainProfile = store.getByApiKey(apiUsername);

    if (domainProfile == null) {
      throw new NotAuthorizedException("API");

    } else if (objectsNotEqual(apiPassword, domainProfile.getApiPassword())) {
      throw new NotAuthorizedException("API");
    }

    JobsApplication.get(app, ExecutionContextManager.class).create(domainProfile);
  }
}
