package org.tiogasolutions.jobs.kernel.support;

import org.tiogasolutions.lib.couchace.DefaultCouchServer;

import javax.inject.Named;
import java.io.IOException;

@Named
public class JobsCouchServer extends DefaultCouchServer {

  public JobsCouchServer() {
  }
}
