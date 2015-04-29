package org.tiogasolutions.jobs.agent.core.support;

import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.jobs.agent.core.JobsApplication;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileEntity;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileStore;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;
import org.tiogasolutions.jobs.pub.DomainProfile;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
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
      authenticateAdminRequest(requestContext);
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

    final SecurityContext securityContext = requestContext.getSecurityContext();
    requestContext.setSecurityContext(new ClientSecurityContext(securityContext, domainProfile.toDomainProfile()));

    JobsApplication.get(app, ExecutionContextManager.class).create(domainProfile);
  }

  private void authenticateAdminRequest(ContainerRequestContext requestContext) {
    String authHeader = requestContext.getHeaderString("Authorization");

    if (authHeader == null) {
      throw new NotAuthorizedException("API");
    } else if (authHeader.startsWith("Basic ") == false) {
      throw new NotAuthorizedException("API");
    } else {
      authHeader = authHeader.substring(6);
    }

    byte[] bytes = DatatypeConverter.parseBase64Binary(authHeader);
    String basicAuth = new String(bytes, StandardCharsets.UTF_8);

    int pos = basicAuth.indexOf(":");

    String username;
    String password;

    if (pos < 0) {
      username = basicAuth;
      password = null;

    } else {
      username = basicAuth.substring(0, pos);
      password = basicAuth.substring(pos+1);
    }

    // throws NotAuthorizedException if not a valid username and password
    authorize(username, password);

    final SecurityContext securityContext = requestContext.getSecurityContext();
    requestContext.setSecurityContext(new AdminSecurityContext(securityContext, username));
  }

  private void authorize(String username, String password) {
    if ("admin".equals(username) == false ||  password.equals("North2South!") == false) {
      throw new NotAuthorizedException("ADMIN");
    }
  }

  private class AdminSecurityContext implements SecurityContext {
    private final boolean secure;
    private final String username;
    public AdminSecurityContext(SecurityContext securityContext, String username) {
      this.username = username;
      this.secure = securityContext.isSecure();
    }
    public String getUsername() { return username; }
    @Override public boolean isUserInRole(String role) { return false; }
    @Override public boolean isSecure() { return secure; }
    @Override public String getAuthenticationScheme() { return "BASIC_AUTH"; }
    @Override public Principal getUserPrincipal() { return this::getUsername; }
  }

  private class ClientSecurityContext implements SecurityContext {
    private final boolean secure;
    private final String domainName;
    private final Principal principal;
    public ClientSecurityContext(SecurityContext securityContext, DomainProfile domain) {
      this.secure = securityContext.isSecure();
      this.domainName = domain.getDomainName();
      this.principal = this::getDomainName;
    }
    public String getDomainName() {
      return domainName;
    }
    @Override public boolean isUserInRole(String role) {
      return false;
    }
    @Override public boolean isSecure() {
      return secure;
    }
    @Override public String getAuthenticationScheme() {
      return "BASIC_AUTH";
    }
    @Override public Principal getUserPrincipal() { return principal;}
  }
}
