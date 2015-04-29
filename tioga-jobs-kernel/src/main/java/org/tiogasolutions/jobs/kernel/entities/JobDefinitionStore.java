package org.tiogasolutions.jobs.kernel.entities;

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.jobs.kernel.support.JobsCouchServer;
import org.tiogasolutions.lib.couchace.DefaultCouchStore;

import java.util.List;

import static java.util.Collections.*;

public class JobDefinitionStore extends DefaultCouchStore<JobDefinitionEntity> {

  public static final String JOB_DEFINITION_DESIGN_NAME = "jobDefinition";

  private final DomainDatabaseConfig config;

  public JobDefinitionStore(DomainDatabaseConfig config) {
    super(config.getCouchServer(), JobDefinitionEntity.class);
    this.config = config;
  }

  @Override
  public String getDesignName() {
    return JOB_DEFINITION_DESIGN_NAME;
  }

  @Override
  public String getDatabaseName() {
    DomainProfileEntity domainProfile = config.getEcm().getExecutionContext().getDomainProfileEntity();
    return config.getDbNamePrefix() + domainProfile.getDomainName().toLowerCase() + config.getDbNameSuffix();
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
