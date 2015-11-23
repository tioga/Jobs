package org.tiogasolutions.jobs.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.tiogasolutions.dev.jackson.TiogaJacksonTranslator;
import org.tiogasolutions.jobs.jackson.JobsObjectMapper;
import org.tiogasolutions.jobs.kernel.config.CouchServersConfig;

@Profile("test")
@Configuration
public class SpringTestConfig {

  @Bean
  public JobsObjectMapper cpObjectMapper() {
    return new JobsObjectMapper();
  }

  @Bean
  public TiogaJacksonTranslator tiogaJacksonTranslator(JobsObjectMapper objectMapper) {
    return new TiogaJacksonTranslator(objectMapper);
  }

  @Bean
  public CouchServersConfig couchServersConfig() {
    CouchServersConfig config = new CouchServersConfig();

    config.setMasterUrl("http://localhost:5984");
    config.setMasterUserName("test-user");
    config.setMasterPassword("test-user");
    config.setMasterDatabaseName("tioga-jobs");

    config.setDomainUrl("http://localhost:5984");
    config.setDomainUserName("test-user");
    config.setDomainPassword("test-user");
    config.setDomainDatabasePrefix("tioga-jobs-");

    return config;
  }
}
