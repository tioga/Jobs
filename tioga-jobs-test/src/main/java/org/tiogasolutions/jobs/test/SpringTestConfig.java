package org.tiogasolutions.jobs.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.tiogasolutions.apis.bitly.BitlyApis;
import org.tiogasolutions.dev.jackson.TiogaJacksonTranslator;
import org.tiogasolutions.push.jackson.CpObjectMapper;
import org.tiogasolutions.push.kernel.config.CouchServersConfig;
import org.tiogasolutions.push.kernel.system.SessionStore;

import java.util.concurrent.TimeUnit;

@Profile("test")
@Configuration
public class SpringTestConfig {

  @Bean
  public CpObjectMapper cpObjectMapper() {
    return new CpObjectMapper();
  }

  @Bean
  public TiogaJacksonTranslator tiogaJacksonTranslator(CpObjectMapper objectMapper) {
    return new TiogaJacksonTranslator(objectMapper);
  }

  @Bean
  public SessionStore sessionStore() {
    return new SessionStore(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS));
  }

  @Bean
  public BitlyApis bitlyApis(TiogaJacksonTranslator translator) {
    return new BitlyApis(translator, "9f5ed9c08c695b4a017bfb432eea58876a5d40cb");
  }

  @Bean
  public CouchServersConfig couchServersConfig() {
    CouchServersConfig config = new CouchServersConfig();

    config.setMasterUrl("http://localhost:5984");
    config.setMasterUserName("test-user");
    config.setMasterPassword("test-user");
    config.setMasterDatabaseName("test-push");

    config.setDomainUrl("http://localhost:5984");
    config.setDomainUserName("test-user");
    config.setDomainPassword("test-user");
    config.setDomainDatabasePrefix("test-push-");

    return config;
  }
}
