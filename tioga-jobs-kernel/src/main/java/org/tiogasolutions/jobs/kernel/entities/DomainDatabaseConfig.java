package org.tiogasolutions.jobs.kernel.entities;

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.dev.common.id.TimeUuidIdGenerator;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;
import org.tiogasolutions.jobs.kernel.support.JobsCouchServer;
import org.tiogasolutions.lib.couchace.support.CouchUtils;

import static java.util.Collections.singletonList;

public class DomainDatabaseConfig {

  private final JobsCouchServer couchServer;
  private final ExecutionContextManager ecm;
  private final String dbNamePrefix;
  private final String dbNameSuffix;

  public DomainDatabaseConfig(JobsCouchServer couchServer, ExecutionContextManager ecm, String dbNamePrefix, String dbNameSuffix) {
    this.couchServer = couchServer;
    this.ecm = ecm;
    this.dbNamePrefix = dbNamePrefix;
    this.dbNameSuffix = dbNameSuffix;
  }

  public JobsCouchServer getCouchServer() {
    return couchServer;
  }

  public ExecutionContextManager getEcm() {
    return ecm;
  }

  public String getDbNamePrefix() {
    return dbNamePrefix;
  }

  public String getDbNameSuffix() {
    return dbNameSuffix;
  }

  public void createDatabase(CouchDatabase database) {
    CouchUtils.createDatabase(database, new TimeUuidIdGenerator(),
      "/jobs-agent/json-docs/jobDefinition-test-a.json",
      "/jobs-agent/json-docs/jobDefinition-test-b.json");

    CouchUtils.validateDesign(database, singletonList("entity"), "/jobs-agent/design-docs/", "-design.json");
  }
}
