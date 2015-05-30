package org.tiogasolutions.jobs.pub.actions;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tiogasolutions.dev.common.json.JsonTranslator;
import org.tiogasolutions.dev.jackson.TiogaJacksonTranslator;
import org.tiogasolutions.jobs.jackson.JobsObjectMapper;
import org.tiogasolutions.jobs.pub.ActionType;
import org.tiogasolutions.jobs.pub.OsAction;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Test
public class OsActionTest {

  public void testTranslation() {

    JobsObjectMapper objectMapper = new JobsObjectMapper();
    JsonTranslator translator = new TiogaJacksonTranslator(objectMapper);

    OsAction oldAction = OsAction.newAction("c:\\Windows\\system32\\cmd.exe /c dir", Paths.get("c:\\dvlp"), "my-lock", 10, TimeUnit.SECONDS);
    String json = translator.toJson(oldAction);
    Assert.assertEquals(json, EXPECTED_JSON);

    OsAction newAction = translator.fromJson(OsAction.class, json);

    Assert.assertEquals(newAction.getActionType(), ActionType.osCommand);
    Assert.assertEquals(newAction.getCommand(), "c:\\Windows\\system32\\cmd.exe /c dir");
    Assert.assertEquals(newAction.getWorkingDirectory().getPath(), "c:\\dvlp");
    Assert.assertEquals(newAction.getLock(), "my-lock");
    Assert.assertEquals(newAction.getTimeout(), 10);
    Assert.assertEquals(newAction.getTimeoutUnit(), TimeUnit.SECONDS);
  }

  @SuppressWarnings("FieldCanBeLocal")
  private final String EXPECTED_JSON =
    "{\n" +
    "  \"actionType\" : \"osCommand\",\n" +
    "  \"command\" : \"c:\\\\Windows\\\\system32\\\\cmd.exe /c dir\",\n" +
    "  \"workingDirectory\" : \"c:\\\\dvlp\",\n" +
    "  \"lock\" : \"my-lock\",\n" +
    "  \"timeout\" : 10,\n" +
    "  \"timeoutUnit\" : \"SECONDS\"\n" +
    "}";
}