package org.tiogasolutions.jobs.coordinator.engine.resources;

import ch.qos.logback.classic.Level;
import org.glassfish.jersey.test.JerseyTest;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.testng.annotations.BeforeClass;
import org.tiogasolutions.app.common.LogUtils;
import org.tiogasolutions.jobs.coordinator.engine.JobsCoordinatorApplication;
import org.tiogasolutions.jobs.coordinator.engine.TestFactory;

import javax.ws.rs.core.Application;

public class ResourceTestSupport extends JerseyTest {

  // private AutowireCapableBeanFactory beanFactory;

  @BeforeClass
  public void beforeClass() throws Exception {
    super.setUp();
    // beanFactory.autowireBean(this);
  }

  @Override
  protected Application configure() {
    LogUtils.initLogback(Level.WARN);

    JobsCoordinatorApplication application = new JobsCoordinatorApplication("test", TestFactory.SPRING_CLASS_PATH);
    // beanFactory = (AutowireCapableBeanFactory)application.getBeanFactory();

    return application;
  }
}
