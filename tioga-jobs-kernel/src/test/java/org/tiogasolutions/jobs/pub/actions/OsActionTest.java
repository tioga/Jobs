package org.tiogasolutions.jobs.pub.actions;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tiogasolutions.dev.common.OsUtils;
import org.tiogasolutions.dev.common.json.JsonTranslator;
import org.tiogasolutions.dev.jackson.TiogaJacksonTranslator;
import org.tiogasolutions.jobs.jackson.JobsObjectMapper;
import org.tiogasolutions.jobs.pub.ActionType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Test
public class OsActionTest {

  public void testTranslation() {

    JobsObjectMapper objectMapper = new JobsObjectMapper();
    JsonTranslator translator = new TiogaJacksonTranslator(objectMapper);

    Path workingDir = OsUtils.isWindows() ? Paths.get("c:\\dvlp") : Paths.get("/dvlp");

    OsAction oldAction = OsAction.create("c:\\Windows\\system32\\cmd.exe /c dir", workingDir, "my-lock", 10, TimeUnit.SECONDS);
    String json = translator.toJson(oldAction);

    if (OsUtils.isWindows()) Assert.assertEquals(json, EXPECTED_JSON_WINDOWS);
    else Assert.assertEquals(json, EXPECTED_JSON_LINUX);

    OsAction newAction = translator.fromJson(OsAction.class, json);

    Assert.assertEquals(newAction.getActionType(), ActionType.OS_COMMAND);
    Assert.assertEquals(newAction.getCommand(), "c:\\Windows\\system32\\cmd.exe /c dir");
    Assert.assertEquals(newAction.getWorkingDirectory().getPath(), "c:\\dvlp");
    Assert.assertEquals(newAction.getLock(), "my-lock");
    Assert.assertEquals(newAction.getTimeout(), 10);
    Assert.assertEquals(newAction.getTimeoutUnit(), TimeUnit.SECONDS);
  }

  private static final String EXPECTED_JSON_WINDOWS =
    "{\n" +
      "  \"actionType\" : \"OS_COMMAND\",\n" +
      "  \"command\" : \"c:\\\\Windows\\\\system32\\\\cmd.exe /c dir\",\n" +
      "  \"workingDirectory\" : \"c:\\\\dvlp\",\n" +
      "  \"lock\" : \"my-lock\",\n" +
      "  \"timeout\" : 10,\n" +
      "  \"timeoutUnit\" : \"SECONDS\"\n" +
      "}";

  private static final String EXPECTED_JSON_LINUX =
    "{\n" +
      "  \"actionType\" : \"OS_COMMAND\",\n" +
      "  \"command\" : \"c:\\\\Windows\\\\system32\\\\cmd.exe /c dir\",\n" +
      "  \"workingDirectory\" : \"/dvlp\",\n" +
      "  \"lock\" : \"my-lock\",\n" +
      "  \"timeout\" : 10,\n" +
      "  \"timeoutUnit\" : \"SECONDS\"\n" +
      "}";
}