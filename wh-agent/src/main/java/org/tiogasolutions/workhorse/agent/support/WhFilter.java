package org.tiogasolutions.workhorse.agent.support;

import org.crazyyak.dev.common.StringUtils;
import org.tiogasolutions.workhorse.agent.WhApplication;
import org.tiogasolutions.workhorse.agent.entities.DomainProfileEntity;
import org.tiogasolutions.workhorse.agent.entities.DomainStatus;

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

import static org.crazyyak.dev.common.EqualsUtils.objectsNotEqual;
import static org.crazyyak.dev.common.exceptions.ExceptionUtils.assertNotZeroLength;

/**
 * This is the "global" filter for the Engine. It's primary responsibility is
 * for managing the execution context over the entire lifecycle of a request.
 *
 * All other filters should be processed after this one.
 */
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class WhFilter implements ContainerRequestFilter, ContainerResponseFilter {

  @Context
  private UriInfo uriInfo;
  @Context
  private HttpHeaders headers;
  @Context
  Application application;

  public WhFilter() {
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    String baseUri = uriInfo.getBaseUri().toString();
    String requestUri = uriInfo.getRequestUri().toString();
    String path = requestUri.substring(baseUri.length() - 1);

    Map<String,Object> properties = application.getProperties();
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
    WhApplication.executionContextManager.clearContext();
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

    Object sysUsername = application.getProperties().get("system.username");
    Object sysPassword = application.getProperties().get("system.password");

    if (objectsNotEqual(apiUsername, sysUsername) || objectsNotEqual(apiPassword, sysPassword)) {
      throw new NotAuthorizedException("API");
    }

    DomainProfileEntity domainProfileEntity = new DomainProfileEntity(
      "id-123", null, "Prod", DomainStatus.ACTIVE, apiUsername, apiPassword, "workhorse-prod");

    WhApplication.executionContextManager.create(application, uriInfo, headers, domainProfileEntity);
  }
}
