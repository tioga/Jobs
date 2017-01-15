package org.tiogasolutions.jobs.agent.engine;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchServer;
import org.tiogasolutions.couchace.core.api.request.CouchFeature;
import org.tiogasolutions.couchace.core.api.request.CouchFeatureSet;
import org.tiogasolutions.jobs.kernel.config.CouchServersConfig;
import org.tiogasolutions.lib.couchace.DefaultCouchServer;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

@Component
@Profile("test")
public class TestFactory {

  public static final String API_KEY = "9999";
  public static final String API_PASSWORD = "unittest";

//  @Autowired
  public TestFactory(CouchServersConfig domainDbConfig) throws Exception {

    String sysDatabase = "test-jobs";
    String usrDatabase = domainDbConfig.getDomainDatabasePrefix() + "testing";

    CouchServer server = new DefaultCouchServer();

    for (String dbName : Arrays.asList(sysDatabase, usrDatabase)) {
      CouchDatabase database = server.database(dbName, CouchFeatureSet.builder()
        .add(CouchFeature.ALLOW_DB_DELETE, true)
        .build());

      if (database.exists()) {
        database.deleteDatabase();
      }
    }
  }

  public String toHttpAuth(String username, String password) {
    byte[] value = (username + ":" + password).getBytes();
    return "Basic " + DatatypeConverter.printBase64Binary(value);
  }
}

