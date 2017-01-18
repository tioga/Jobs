package org.tiogasolutions.jobs.server.grizzly;

import java.net.URL;

public class JobsServerLauncher {

  public static void main(String...args) throws Throwable {
    URL location = JobsServer.class.getProtectionDomain().getCodeSource().getLocation();
    System.out.println("Starting application from " + location);

    if (location.getPath().endsWith(".jar")) {
      JarClassLoader jcl = new JarClassLoader();
      jcl.invokeStart(JobsServer.class.getName(), args);

    } else {
      JobsServer.main(args);
    }
  }
}
