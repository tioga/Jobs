package org.tiogasolutions.jobs.kernel.config;

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.dev.common.id.TimeUuidIdGenerator;
import org.tiogasolutions.lib.couchace.support.CouchUtils;

import static java.util.Collections.singletonList;

public class CouchServersConfig {

  private String masterUrl;
  private String masterUserName;
  private String masterPassword;
  private String masterDatabaseName;

  private String domainUrl;
  private String domainUserName;
  private String domainPassword;
  private String domainDatabasePrefix;

  public CouchServersConfig() {
  }

  public void createDatabase(CouchDatabase database) {
    CouchUtils.createDatabase(database, new TimeUuidIdGenerator(),
      "/jobs-agent/json-docs/jobDefinition-test-a.json",
      "/jobs-agent/json-docs/jobDefinition-test-b.json");

    CouchUtils.validateDesign(database, singletonList("entity"), "/jobs-agent/design-docs/", "-design.json");
  }

  public String getMasterUrl() {
    return masterUrl;
  }

  public void setMasterUrl(String masterUrl) {
    this.masterUrl = masterUrl;
  }

  public String getMasterUserName() {
    return masterUserName;
  }

  public void setMasterUserName(String masterUserName) {
    this.masterUserName = masterUserName;
  }

  public String getMasterPassword() {
    return masterPassword;
  }

  public void setMasterPassword(String masterPassword) {
    this.masterPassword = masterPassword;
  }

  public String getMasterDatabaseName() {
    return masterDatabaseName;
  }

  public void setMasterDatabaseName(String masterDatabaseName) {
    this.masterDatabaseName = masterDatabaseName;
  }

  public String getDomainUrl() {
    return domainUrl;
  }

  public void setDomainUrl(String domainUrl) {
    this.domainUrl = domainUrl;
  }

  public String getDomainUserName() {
    return domainUserName;
  }

  public void setDomainUserName(String domainUserName) {
    this.domainUserName = domainUserName;
  }

  public String getDomainPassword() {
    return domainPassword;
  }

  public void setDomainPassword(String domainPassword) {
    this.domainPassword = domainPassword;
  }

  public String getDomainDatabasePrefix() {
    return domainDatabasePrefix;
  }

  public void setDomainDatabasePrefix(String domainDatabasePrefix) {
    this.domainDatabasePrefix = domainDatabasePrefix;
  }
}
