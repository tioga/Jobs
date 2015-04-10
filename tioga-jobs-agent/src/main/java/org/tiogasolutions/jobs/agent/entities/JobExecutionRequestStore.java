package org.tiogasolutions.jobs.agent.entities;

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.dev.common.id.TimeUuidIdGenerator;
import org.tiogasolutions.jobs.agent.support.ExecutionContextManager;
import org.tiogasolutions.jobs.agent.support.WhCouchServer;
import org.tiogasolutions.lib.couchace.DefaultCouchStore;
import org.tiogasolutions.lib.couchace.support.CouchUtils;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class JobExecutionRequestStore extends DefaultCouchStore<JobExecutionRequestEntity> {

  public static final String JOB_EXECUTION_REQUEST_DESIGN_NAME = "jobExecutionRequest";

  private final ExecutionContextManager ecm;
  private final String dbNamePrefix;
  private final String dbNameSuffix;

  public JobExecutionRequestStore(WhCouchServer couchServer, ExecutionContextManager ecm, String dbNamePrefix, String dbNameSuffix) throws Exception {
    super(couchServer, JobExecutionRequestEntity.class);

    this.ecm = ecm;
    this.dbNamePrefix = (dbNamePrefix == null) ? "" : dbNamePrefix;
    this.dbNameSuffix = (dbNameSuffix == null) ? "" : dbNameSuffix;
  }

  @Override
  public String getDesignName() {
    return JOB_EXECUTION_REQUEST_DESIGN_NAME;
  }

  @Override
  public String getDatabaseName() {
    DomainProfileEntity domainProfile = ecm.getExecutionContext().getDomainProfileEntity();
    return dbNamePrefix + domainProfile.getDomainName().toLowerCase() + dbNameSuffix;
  }

  @Override
  public WhCouchServer getCouchServer() {
    return (WhCouchServer)super.getCouchServer();
  }

  @Override
  public void createDatabase(CouchDatabase database) {
    CouchUtils.createDatabase(database, new TimeUuidIdGenerator());
    // CouchUtils.validateDesign(database, singletonList("entity"), "/jobs-agent/design-docs/", "-design.json");
  }

  public List<JobExecutionRequestEntity> getAll() {
    return getEntityResponse("entity", "byEntityType", singletonList(JOB_EXECUTION_REQUEST_DESIGN_NAME)).getEntityList();
  }
}
