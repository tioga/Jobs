package org.tiogasolutions.jobs.coordinator.server.grizzly;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.tiogasolutions.app.common.AppPathResolver;
import org.tiogasolutions.app.common.LogUtils;
import org.tiogasolutions.jobs.coordinator.engine.JobsCoordinatorApplication;
import org.tiogasolutions.lib.spring.jersey.JerseySpringBridge;
import org.tiogasolutions.runners.grizzly.GrizzlyServer;
import org.tiogasolutions.runners.grizzly.GrizzlyServerConfig;
import org.tiogasolutions.runners.grizzly.LoggerFacade;

import java.nio.file.Path;

import static org.slf4j.LoggerFactory.getLogger;

public class GrizzlyJobsCoordinatorServer {

  private static final Logger log = getLogger(GrizzlyJobsCoordinatorServer.class);

  public static final String DEFAULT_SPRING_FILE = "/org/tiogasolutions/jobs/coordinator/server/grizzly/coordinator-spring-config.xml";

  public static void main(String...args) throws Exception {

    // Priority #1, configure default logging levels. This will be overridden later
    // when/if the logback.xml is found and loaded.
    LogUtils.initLogback(Level.WARN);
    // Assume we want by default INFO on when & how the grizzly server is started
    ((ch.qos.logback.classic.Logger) getLogger(GrizzlyJobsCoordinatorServer.class)).setLevel(Level.INFO);
    ((ch.qos.logback.classic.Logger) getLogger(GrizzlyServer.class)).setLevel(Level.INFO);

    // Load the resolver which gives us common tools for identifying the
    // runtime & config directories, logback.xml, etc.
    AppPathResolver resolver = new AppPathResolver(getLogger(AppPathResolver.class)::info, "jobs.");

    Path runtimeDir = resolver.resolveRuntimePath();
    Path configDir = resolver.resolveConfigDir(runtimeDir);

    // Re-init logback if we can find the logback.xml
    Path logbackFile = LogUtils.initLogback(configDir, "jobs.log.config", "logback.xml");

    // Locate the spring file for this app or use DEFAULT_SPRING_FILE from the classpath if one is not found.
    String springConfigPath = resolver.resolveSpringPath(configDir, "classpath:" + DEFAULT_SPRING_FILE);
    String activeProfiles = resolver.resolveSpringProfiles(); // defaults to "hosted"

    log.info("Starting Notify Server:\n" +
      "  *  Runtime Dir:  {}\n" +
      "  *  Config Dir:   {}\n" +
      "  *  Logback File: {}\n" +
      "  *  Spring Path:  {}", runtimeDir, configDir, logbackFile, springConfigPath);

    // Create our application, initializing it with the specified spring file.
    JobsCoordinatorApplication application = new JobsCoordinatorApplication(activeProfiles, springConfigPath);

    // Get from the app an instance of the grizzly server config.
    GrizzlyServerConfig serverConfig = application.getBeanFactory().getBean(GrizzlyServerConfig.class);

    // Create a facade around Slf4j for the server's initialization routines.
    LoggerFacade loggerFacade = new LoggerFacade() {
      @Override public void info(String message) { getLogger(GrizzlyServer.class).info(message); }
      @Override public void warn(String message) { getLogger(GrizzlyServer.class).warn(message); }
      @Override public void error(String message, Throwable e) { getLogger(GrizzlyServer.class).error(message, e); }
    };

    // Create an instance of the grizzly server.
    GrizzlyServer grizzlyServer = new GrizzlyServer(application, serverConfig, loggerFacade);

    // Before we start it, register a hook for our jersey-spring bridge.
    JerseySpringBridge jerseySpringBridge = new JerseySpringBridge(application.getBeanFactory());
    grizzlyServer.getResourceConfig().register(jerseySpringBridge);

    // Lastly, start the server.
    grizzlyServer.start();
  }
}
