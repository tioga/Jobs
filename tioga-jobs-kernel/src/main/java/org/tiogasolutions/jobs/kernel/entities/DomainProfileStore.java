package org.tiogasolutions.jobs.kernel.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.dev.common.id.TimeUuidIdGenerator;
import org.tiogasolutions.jobs.kernel.config.CouchServersConfig;
import org.tiogasolutions.jobs.kernel.support.JobsCouchServer;
import org.tiogasolutions.lib.couchace.DefaultCouchStore;
import org.tiogasolutions.lib.couchace.support.CouchUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

@Component
public class DomainProfileStore extends DefaultCouchStore<DomainProfileEntity> {

  public static final String DOMAIN_PROFILE_DESIGN_NAME = "domainProfile";

  private final String databaseName;

  @Autowired
  public DomainProfileStore(CouchServersConfig config) {
    super(JobsCouchServer.newMasterDb(config), DomainProfileEntity.class);
    this.databaseName = config.getMasterDatabaseName();
  }

  @Override
  public String getDesignName() {
    return DOMAIN_PROFILE_DESIGN_NAME;
  }

  @Override
  public String getDatabaseName() {
    return databaseName;
  }

  @Override
  public JobsCouchServer getCouchServer() {
    return (JobsCouchServer)super.getCouchServer();
  }

  @Override
  public void createDatabase(CouchDatabase database) {
    CouchUtils.createDatabase(database, new TimeUuidIdGenerator(),
      "/jobs-agent/json-docs/domainProfile-test.json");

    List<String> entities = Arrays.asList("entity", "domainProfile");
    CouchUtils.validateDesign(database, entities, "/jobs-agent/design-docs/", "-design.json");
  }

  public List<DomainProfileEntity> getAll() {
    return getEntityResponse("entity", "byEntityType", singletonList(DOMAIN_PROFILE_DESIGN_NAME)).getEntityList();
  }

  public DomainProfileEntity getByDomainName(String domainName) {
    List<DomainProfileEntity> list = super.getEntities("byDomainName", domainName);
    return list.isEmpty() ? null : list.get(0);
  }

  public DomainProfileEntity getByApiKey(String byApiKey) {
    List<DomainProfileEntity> list = super.getEntities("byApiKey", byApiKey);
    return list.isEmpty() ? null : list.get(0);
  }
}
