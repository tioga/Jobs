package org.tiogasolutions.workhorse.agent.entities;

import com.couchace.core.api.CouchException;
import com.couchace.core.api.CouchServer;
import com.couchace.core.api.query.CouchViewQuery;
import com.couchace.core.api.response.GetEntityResponse;
import org.crazyyak.lib.couchace.DefaultCouchStore;

import java.util.List;

public class JobStore extends DefaultCouchStore<JobEntity> {

  public static final String JOB_DESIGN_NAME = "job";

  public JobStore(CouchServer couchServer) {
    super(couchServer, "workhorse", JobEntity.class);
  }

  @Override
  public String getDesignName() {
    return JOB_DESIGN_NAME;
  }

  public List<JobEntity> getAll() {

    String viewName = "byEntityType";

    CouchViewQuery couchViewQuery = CouchViewQuery
      .builder("entity", viewName)
      .key(new String[]{"job"})
      .includeDocs(true)
      .build();

    GetEntityResponse<JobEntity> response = database.get().entity(getEntityType(), couchViewQuery).execute();

    if (response.isNotFound()) {
      String msg = String.format("The view \"_design/%s:%s\" does not exist.", getDesignName(), viewName);
      throw new CouchException(response.getHttpStatus(), msg);

    } else if (response.isOk() == false) {
      throw new CouchException(response.getHttpStatus());
    }

    return response.getEntityList();
  }
}
