package org.tiogasolutions.jobs.agent.engine.support;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.jobs.kernel.config.SystemConfiguration;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileEntity;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileStore;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;
import org.tiogasolutions.jobs.pub.DomainProfile;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

import static org.slf4j.LoggerFactory.getLogger;
import static org.tiogasolutions.dev.common.EqualsUtils.objectsNotEqual;

/**
 * This is the "global" filter for the Engine. It's primary responsibility is
 * for managing the execution context over the entire lifecycle of a request.
 *
 * All other filters should be processed after this one.
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class JobsRequestFilter implements ContainerRequestFilter {

  private static final Logger log = getLogger(JobsRequestFilter.class);

  @Context
  private UriInfo uriInfo;

  @Autowired
  private ExecutionContextManager executionManager;

  @Autowired
  private DomainProfileStore domainProfileStore;

  @Autowired
  private SystemConfiguration systemConfiguration;

  public JobsRequestFilter() {
    log.info("Created.");
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    String baseUri = uriInfo.getBaseUri().toString();
    String requestUri = uriInfo.getRequestUri().toString();

    String path = requestUri.substring(baseUri.length() - 1);

    String clientContext = systemConfiguration.getClientContext();
    String adminContext = systemConfiguration.getAdminContext();

    try {
      if (path.equals(clientContext) || path.startsWith(clientContext+"/")) {
        authenticateClientRequest(requestContext);

      } else if (path.equals(adminContext) || path.startsWith(adminContext+"/")) {
        authenticateAdminRequest(requestContext);
      }
    } catch (NotAuthorizedException e) {
      e.printStackTrace();
      requestContext.abortWith(Response
          .status(Response.Status.UNAUTHORIZED)
          .header("WWW-Authenticate", "Basic realm=\"Jobs Agent\"")
          .type("text/plain")
          .entity("Not authorized")
          .build());
    }
  }

  private void authenticateClientRequest(ContainerRequestContext requestContext) {
    if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
      return; // do not authenticate OPTIONS calls.
    }

    String authHeader = requestContext.getHeaderString("Authorization");

    if (authHeader == null || authHeader.startsWith("Basic ") == false) {
      throw new NotAuthorizedException("Jobs Agent");
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

    DomainProfileEntity domainProfile = domainProfileStore.getByApiKey(apiUsername);

    if (domainProfile == null) {
      throw new NotAuthorizedException("API");

    } else if (objectsNotEqual(apiPassword, domainProfile.getApiPassword())) {
      throw new NotAuthorizedException("API");
    }

    final SecurityContext securityContext = requestContext.getSecurityContext();
    requestContext.setSecurityContext(new ClientSecurityContext(securityContext, domainProfile.toDomainProfile()));

    executionManager.create(domainProfile);
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
