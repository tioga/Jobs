package org.tiogasolutions.workhorse.agent.support;

import com.couchace.core.api.CouchDatabase;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.crazyyak.dev.common.id.TimeUuidIdGenerator;
import org.crazyyak.lib.couchace.DefaultCouchServer;
import org.crazyyak.lib.couchace.support.CouchUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WhCouchServer extends DefaultCouchServer {

  private final String databaseName;
  private final CouchDatabase database;

  private static final List<String> designNames = Arrays.asList("entity");
  private static final String prefix = "/wh-agent/design-docs/";
  private static final String suffix = "-design.json";

  public WhCouchServer() throws IOException {
    this("workhorse");
  }

  private WhCouchServer(String databaseName) throws IOException {
    super();

    this.databaseName = ExceptionUtils.assertNotZeroLength(databaseName, "databaseName");
    this.database = database(databaseName);

    validateDatabases();
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public CouchDatabase getDatabase() {
    return database;
  }

  public void validateDatabases() throws IOException {
    String[] documents = new String[0];
    CouchUtils.createDatabase(database, new TimeUuidIdGenerator(), documents);
    CouchUtils.validateDesign(database, designNames, prefix, suffix);

    compactAndCleanAll();
  }

  public void compactAndCleanAll() {
    CouchUtils.compactAndCleanAll(database, designNames);
  }
}
