package org.tiogasolutions.jobs.agent.engine.support;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.jobs.kernel.config.SystemConfiguration;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

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
