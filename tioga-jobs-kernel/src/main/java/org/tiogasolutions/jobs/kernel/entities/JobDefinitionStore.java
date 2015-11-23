package org.tiogasolutions.jobs.kernel.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.jobs.kernel.config.CouchServersConfig;
import org.tiogasolutions.jobs.kernel.support.ExecutionContextManager;
import org.tiogasolutions.jobs.kernel.support.JobsCouchServer;
import org.tiogasolutions.lib.couchace.DefaultCouchStore;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static java.util.Collections.*;

@Component
public class JobDefinitionStore extends DefaultCouchStore<JobDefinitionEntity> {

  public static final String JOB_DEFINITION_DESIGN_NAME = "jobDefinition";

  private final CouchServersConfig config;
  private final ExecutionContextManager ecm;

  @Autowired
  public JobDefinitionStore(CouchServersConfig config, ExecutionContextManager ecm) {
    super(JobsCouchServer.newDomainDb(config), JobDefinitionEntity.class);
    this.ecm = ecm;
    this.config = config;
  }

  @Override
  public String getDesignName() {
    return JOB_DEFINITION_DESIGN_NAME;
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

  public List<JobDefinitionEntity> getAll() {
    return getEntityResponse("entity", "byEntityType", singletonList(JOB_DEFINITION_DESIGN_NAME)).getEntityList();
  }
}
