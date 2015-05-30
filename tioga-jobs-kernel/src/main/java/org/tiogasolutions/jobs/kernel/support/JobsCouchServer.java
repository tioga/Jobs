package org.tiogasolutions.jobs.kernel.support;

import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpClient;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jersey.JerseyCouchHttpClient;
import org.tiogasolutions.dev.jackson.TiogaJacksonModule;
import org.tiogasolutions.jobs.jackson.JobsJacksonModule;
import org.tiogasolutions.jobs.kernel.entities.CouchServersConfig;
import org.tiogasolutions.lib.couchace.DefaultCouchServer;

import javax.inject.Named;
import java.io.IOException;

public class JobsCouchServer extends DefaultCouchServer {

  private JobsCouchServer(String url, String userName, String password) {
    super(new CouchSetup(url)
      .setUserName(userName)
      .setPassword(password)
      .setHttpClient(JerseyCouchHttpClient.class)
      .setJsonStrategy(new JacksonCouchJsonStrategy(
        new TiogaJacksonModule(),
        new JobsJacksonModule()
      )));
  }

  public static JobsCouchServer newMasterDb(CouchServersConfig config) {
    return new JobsCouchServer(config.getMasterUrl(), config.getMasterUserName(), config.getMasterPassword());
  }

  public static JobsCouchServer newDomainDb(CouchServersConfig config) {
    return new JobsCouchServer(config.getMasterUrl(), config.getMasterUserName(), config.getMasterPassword());
  }
}
