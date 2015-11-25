package org.tiogasolutions.jobs.agent.grizzly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.tiogasolutions.jobs.jackson.JobsObjectMapper;
import org.tiogasolutions.jobs.kernel.config.SystemConfiguration;

@Profile("hosted")
@Configuration
public class GrizzlyHostedSpringConfig {

  @Bean
  public JobsObjectMapper notifyObjectMapper() {
    return new JobsObjectMapper();
  }

  @Bean
  public SystemConfiguration systemConfiguration() {
    return new SystemConfiguration("*", "/api/v1/client", "/api/v1/admin");
  }
}
