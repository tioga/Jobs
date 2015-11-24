package org.tiogasolutions.jobs.agent.grizzly;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.tiogasolutions.app.common.AppPathResolver;
import org.tiogasolutions.app.common.AppUtils;
import org.tiogasolutions.jobs.agent.engine.JobsAgentApplication;
import org.tiogasolutions.runners.grizzly.GrizzlyServer;
import org.tiogasolutions.runners.grizzly.GrizzlyServerConfig;
import org.tiogasolutions.runners.grizzly.spring.ApplicationResolver;
import org.tiogasolutions.runners.grizzly.spring.GrizzlySpringServer;
import org.tiogasolutions.runners.grizzly.spring.ServerConfigResolver;

import java.nio.file.Path;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.*;

public class JobsAgentServer {

  private static final Logger log = getLogger(JobsAgentServer.class);

  public static void main(String...args) throws Exception {

    // Priority #1, configure default logging levels. This will be
    // overridden later when/if the logback.xml is found and loaded.
    AppUtils.initLogback(Level.WARN);

    // Assume we want by default INFO on when & how the grizzly server
    // is started. Possibly overwritten by logback.xml if used.
    AppUtils.setLogLevel(Level.INFO, JobsAgentServer.class);
    AppUtils.setLogLevel(Level.INFO, GrizzlySpringServer.GRIZZLY_CLASSES);

    // Load the resolver which gives us common tools for identifying the
    // runtime & config directories, logback.xml, etc.
    AppPathResolver resolver = new AppPathResolver("jobs-agent.");
    Path runtimeDir = resolver.resolveRuntimePath();
    Path configDir = resolver.resolveConfigDir(runtimeDir);

    // Re-init logback if we can find the logback.xml
    Path logbackFile = AppUtils.initLogback(configDir, "jobs.log.config", "logback.xml");

    // Locate the spring file for this app.
    String springConfigPath = resolver.resolveSpringPath(configDir, null);
    String activeProfiles = resolver.resolveSpringProfiles(); // defaults to "hosted"

    log.info("Starting server:\n" +
        "  *  Runtime Dir:  {}\n" +
        "  *  Config Dir:   {}\n" +
        "  *  Logback File: {}\n" +
        "  *  Spring Path ({}):  {}", runtimeDir, configDir, logbackFile, activeProfiles, springConfigPath);

    // Create an instance of the grizzly server.
    GrizzlySpringServer grizzlyServer = new GrizzlySpringServer(
        ServerConfigResolver.fromClass(GrizzlyServerConfig.class),
        ApplicationResolver.fromClass(JobsAgentApplication.class),
        activeProfiles,
        springConfigPath
    );

    grizzlyServer.packages("org.tiogasolutions.jobs");

    if (Arrays.asList(args).contains("-shutdown")) {
      GrizzlyServer.shutdownRemote(grizzlyServer.getConfig());
      log.warn("Shutting down server at {}:{}", grizzlyServer.getConfig().getHostName(), grizzlyServer.getConfig().getShutdownPort());
      System.exit(0);
      return;
    }

    // Lastly, start the server.
    grizzlyServer.start();
  }
}
