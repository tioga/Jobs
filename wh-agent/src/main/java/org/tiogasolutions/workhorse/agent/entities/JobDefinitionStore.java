package org.tiogasolutions.workhorse.agent.entities;

import com.couchace.core.api.CouchDatabase;
import com.couchace.core.api.CouchException;
import com.couchace.core.api.CouchServer;
import com.couchace.core.api.query.CouchViewQuery;
import com.couchace.core.api.response.GetEntityResponse;
import org.crazyyak.dev.common.ReflectUtils;
import org.crazyyak.dev.common.id.TimeUuidIdGenerator;
import org.crazyyak.lib.couchace.DefaultCouchStore;
import org.crazyyak.lib.couchace.support.CouchUtils;
import org.tiogasolutions.workhorse.agent.support.ExecutionContextManager;
import org.tiogasolutions.workhorse.agent.support.WhCouchServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

public class JobDefinitionStore extends DefaultCouchStore<JobDefinitionEntity> {

  public static final String JOB_DESIGN_NAME = "jobDefinition";

  private final ExecutionContextManager ecm;
  private final String dbNamePrefix;
  private final String dbNameSuffix;

  public JobDefinitionStore(WhCouchServer couchServer, ExecutionContextManager ecm, String dbNamePrefix, String dbNameSuffix) throws Exception {
    super(couchServer, JobDefinitionEntity.class);

    this.ecm = ecm;
    this.dbNamePrefix = (dbNamePrefix == null) ? "" : dbNamePrefix;
    this.dbNameSuffix = (dbNameSuffix == null) ? "" : dbNameSuffix;
  }

  @Override
  public String getDesignName() {
    return JOB_DESIGN_NAME;
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
    CouchUtils.createDatabase(database, new TimeUuidIdGenerator(),
      "/wh-agent/json-docs/jobDefinition-prod.json");
    CouchUtils.validateDesign(database, singletonList("entity"), "/wh-agent/design-docs/", "-design.json");
  }

  public List<JobDefinitionEntity> getAll() {
    return getEntityResponse("entity", "byEntityType", emptyList()).getEntityList();
  }
}
