package org.tiogasolutions.jobs.kernel.entities;

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;
import org.tiogasolutions.jobs.kernel.support.JobsCouchServer;
import org.tiogasolutions.lib.couchace.DefaultCouchStore;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static java.util.Collections.singletonList;

@Named
public class JobExecutionRequestStore extends DefaultCouchStore<JobExecutionRequestEntity> {

  public static final String JOB_EXECUTION_REQUEST_DESIGN_NAME = "jobExecutionRequest";

  private final CouchServersConfig config;
  private final ExecutionContextManager ecm;

  @Inject
  public JobExecutionRequestStore(CouchServersConfig config, ExecutionContextManager ecm) {
    super(JobsCouchServer.newDomainDb(config), JobExecutionRequestEntity.class);
    this.ecm = ecm;
    this.config = config;
  }

  @Override
  public String getDesignName() {
    return JOB_EXECUTION_REQUEST_DESIGN_NAME;
  }

  @Override
  public String getDatabaseName() {
    DomainProfileEntity domainProfile = ecm.getExecutionContext().getDomainProfileEntity();
    return config.getDomainDatabasePrefix() + domainProfile.getDomainName().toLowerCase() + config.getDomainDatabaseSuffix();
  }

  @Override
  public JobsCouchServer getCouchServer() {
    return (JobsCouchServer)super.getCouchServer();
  }

  @Override
  public void createDatabase(CouchDatabase database) {
    config.createDatabase(database);
  }

  public List<JobExecutionRequestEntity> getAll() {
    return getEntityResponse("entity", "byEntityType", singletonList(JOB_EXECUTION_REQUEST_DESIGN_NAME)).getEntityList();
  }
}
