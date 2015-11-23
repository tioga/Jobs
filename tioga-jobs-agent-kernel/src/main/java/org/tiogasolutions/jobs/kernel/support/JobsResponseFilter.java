package org.tiogasolutions.jobs.kernel.support;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.jobs.kernel.config.SystemConfiguration;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileEntity;
import org.tiogasolutions.jobs.kernel.entities.DomainProfileStore;
import org.tiogasolutions.jobs.pub.DomainProfile;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
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
public class JobsResponseFilter implements ContainerResponseFilter {

  private static final Logger log = getLogger(JobsResponseFilter.class);

  @Autowired
  private ExecutionContextManager executionManager;

  @Autowired
  private SystemConfiguration systemConfiguration;

  public JobsResponseFilter() {
    log.info("Created.");
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    executionManager.clearContext();
    responseContext.getHeaders().add("Access-Control-Allow-Origin", systemConfiguration.getAccessControlAllowOrigin());
    responseContext.getHeaders().add("Access-Control-Allow-Headers", "Accept, Content-Type, Authorization");
    responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET");
    responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
  }
}
