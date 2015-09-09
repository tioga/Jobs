package org.tiogasolutions.jobs.coordinator.engine.resources;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import static org.testng.Assert.*;

@Test
public class CoordinatorRootResourceV1Test extends ResourceTestSupport {

  public void testGetRoot() throws Exception {
    Response response = target().request().get();
    Assert.assertEquals(response.getStatus(), 200);
  }
}