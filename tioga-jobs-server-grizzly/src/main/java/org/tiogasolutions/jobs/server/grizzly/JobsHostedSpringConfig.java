package org.tiogasolutions.jobs.server.grizzly;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.tiogasolutions.dev.jackson.TiogaJacksonTranslator;
import org.tiogasolutions.jobs.jackson.JobsObjectMapper;
import org.tiogasolutions.jobs.kernel.config.CouchServersConfig;
import org.tiogasolutions.jobs.server.core.JobsCoordinatorApplication;
import org.tiogasolutions.runners.grizzly.GrizzlyServer;
import org.tiogasolutions.runners.grizzly.GrizzlyServerConfig;

import static org.tiogasolutions.dev.common.EnvUtils.findProperty;
import static org.tiogasolutions.dev.common.EnvUtils.requireProperty;

@Profile("hosted")
@Configuration
public class JobsHostedSpringConfig {

  @Bean
  public JobsObjectMapper notifyObjectMapper() {
    return new JobsObjectMapper();
  }

  @Bean
  public TiogaJacksonTranslator tiogaJacksonTranslator(JobsObjectMapper objectMapper) {
    return new TiogaJacksonTranslator(objectMapper);
  }


  @Bean(name="org.tiogasolutions.runners.grizzly.GrizzlyServerConfig")
  public GrizzlyServerConfig grizzlyServerConfig() {
    GrizzlyServerConfig config = new GrizzlyServerConfig();
    config.setHostName(findProperty("notify_hostName", "0.0.0.0"));
    config.setPort(Integer.valueOf(findProperty("notify_port", "8080")));
    config.setShutdownPort(Integer.valueOf(findProperty("notify_shutdownPort", "8081")));
    config.setContext(findProperty("notify_context", ""));
    config.setToOpenBrowser(false);
    return config;
  }

  @Bean(name = "org.tiogasolutions.notify.kernel.config.CouchServersConfig")
  public CouchServersConfig couchServersConfig() {
    CouchServersConfig config = new CouchServersConfig();

    config.setMasterUrl(requireProperty("jobs_masterUrl"));
    config.setMasterUserName(requireProperty("jobs_masterUsername"));
    config.setMasterPassword(requireProperty("jobs_masterPassword"));
    config.setMasterDatabaseName(requireProperty("jobs_masterDatabaseName"));

    config.setDomainUrl(requireProperty("jobs_domainUrl"));
    config.setDomainUserName(requireProperty("jobs_domainUsername"));
    config.setDomainPassword(requireProperty("jobs_domainPassword"));
    config.setDomainDatabasePrefix(requireProperty("jobs_domainDatabasePrefix"));

    return config;
  }

  @Bean
  public JobsCoordinatorApplication notifyApplication() {
    return new JobsCoordinatorApplication();
  }

  @Bean
  public GrizzlyServer grizzlyServer(GrizzlyServerConfig grizzlyServerConfig, JobsCoordinatorApplication application, ApplicationContext applicationContext) {

    ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
    resourceConfig.property("contextConfig", applicationContext);
    resourceConfig.packages("org.tiogasolutions.jobs.server");
    resourceConfig.register(RequestContextFilter.class, 1);

    return new GrizzlyServer(grizzlyServerConfig, resourceConfig);
  }
}
