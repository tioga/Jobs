package org.tiogasolutions.jobs.agent;

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchServer;
import org.tiogasolutions.couchace.core.api.request.CouchFeature;
import org.tiogasolutions.couchace.core.api.request.CouchFeatureSet;
import org.tiogasolutions.dev.common.DateUtils;
import org.tiogasolutions.jobs.agent.entities.DomainProfileStore;
import org.tiogasolutions.jobs.agent.entities.JobDefinitionStore;
import org.tiogasolutions.jobs.agent.entities.JobExecutionRequestStore;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.support.JobsCouchServer;
import org.tiogasolutions.lib.couchace.DefaultCouchServer;

import javax.ws.rs.core.Application;
import javax.xml.bind.DatatypeConverter;
import java.time.ZoneId;

public class TestFactory {

  public static final String API_KEY = "9999";
  public static final String API_PASSWORD = "unittest";

  public static final String DB_NAME = "testjobs";
  public static final String DB_PREFIX = "testjobs-";

  public static JobsApplication newApplication() {
    return new JobsApplication(DB_NAME, DB_PREFIX, "");
  }

  private final JobsApplication app;

  public TestFactory(JobsApplication app) throws Exception {

    this.app = app;

    CouchServer server = new DefaultCouchServer();

    CouchDatabase database = server.database(app.getDatabaseName(), CouchFeatureSet.builder().add(CouchFeature.ALLOW_DB_DELETE, true).build());
    if (database.exists()) {
      database.deleteDatabase();
    }

    database = server.database("testjobs-testing", CouchFeatureSet.builder().add(CouchFeature.ALLOW_DB_DELETE, true).build());
    if (database.exists()) {
      database.deleteDatabase();
    }
  }

  public String toHttpAuth(String username, String password) {
    byte[] value = (username + ":" + password).getBytes();
    return "Basic " + DatatypeConverter.printBase64Binary(value);
  }

  public JobsApplication getApp() {
    return app;
  }
}

