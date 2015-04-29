package org.tiogasolutions.jobs.agent.core.resources;

import org.testng.annotations.Test;
import org.tiogasolutions.dev.common.OsUtils;
import org.tiogasolutions.jobs.agent.core.resources.JobVariable;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

@Test
public class JobVariableTest {

  public void testFindNext() {
    JobVariable variable = JobVariable.findFirst("This ${is} a ${test}.");
    assertNotNull(variable);
    assertEquals(variable.getText(), "${is}");
    assertEquals(variable.getName(), "is");

    variable = JobVariable.findFirst("This is a ${test}.");
    assertNotNull(variable);
    assertEquals(variable.getText(), "${test}");
    assertEquals(variable.getName(), "test");

    variable = JobVariable.findFirst("This is a test.");
    assertNull(variable);

    variable = JobVariable.findFirst("This ${is a} test.");
    assertNull(variable);
  }

  public void testReplace() {

    Map<String, String> map = new HashMap<>();

    try {
      JobVariable variable = JobVariable.findFirst("This is ${some.value}.");
      assertNotNull(variable);
      variable.replace(map, "This is ${some.value}.");
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "The property \"some.value\" was not found in the specified map, system properties or environment variables.");
    }

    String cmd = "This is a ${OS} machine.";
    JobVariable variable = JobVariable.findFirst(cmd);
    assertNotNull(variable);

    // The order of these tests is critical because the map overrides everything
    // and the system properties override the environment variables.

    if (OsUtils.isWindows()) {
      String text = variable.replace(map, cmd);
      assertEquals("This is a Windows_NT machine.", text);
    }

    System.setProperty("OS", "Hokey");
    assertNotNull(variable);
    String text = variable.replace(map, cmd);
    assertEquals("This is a Hokey machine.", text);

    map.put("OS", "Dumb Ass");
    assertNotNull(variable);
    text = variable.replace(map, cmd);
    assertEquals("This is a Dumb Ass machine.", text);
  }
}