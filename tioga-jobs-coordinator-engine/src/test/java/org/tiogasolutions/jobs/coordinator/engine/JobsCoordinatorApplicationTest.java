package org.tiogasolutions.jobs.coordinator.engine;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Map;

@Test
public class JobsCoordinatorApplicationTest {

  private JobsCoordinatorApplication app;

  @BeforeClass
  public void beforeClass() {
    URL resource = getClass().getResource(TestFactory.SPRING_CLASS_PATH);
    Assert.assertNotNull(resource);

    app = new JobsCoordinatorApplication("test", TestFactory.SPRING_CLASS_PATH);
  }

  public void testGetProperties() {

    Map<String, Object> properties = app.getProperties();
    Assert.assertNotNull(properties);

    Assert.assertEquals(properties.get("app.admin.context"), "/api/v1/admin");
    Assert.assertEquals(properties.get("app.client.context"), "/api/v1/client");
  }
}