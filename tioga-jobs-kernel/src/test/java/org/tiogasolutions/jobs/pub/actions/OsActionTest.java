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

    Path workingPath = OsUtils.isWindows() ? Paths.get("c:\\dvlp") : Paths.get("/dvlp");

    OsAction oldAction = OsAction.create("c:\\Windows\\system32\\cmd.exe /c dir", workingPath, "my-lock", 10, TimeUnit.SECONDS);
    String json = translator.toJson(oldAction);

    OsAction newAction = translator.fromJson(OsAction.class, json);

    String workingDir = OsUtils.isWindows() ? "c:\\dvlp" : "/dvlp";
    Assert.assertEquals(newAction.getWorkingDirectory().getPath(), workingDir);
    
    Assert.assertEquals(newAction.getActionType(), ActionType.OS_COMMAND);
    Assert.assertEquals(newAction.getCommand(), "c:\\Windows\\system32\\cmd.exe /c dir");
    Assert.assertEquals(newAction.getLock(), "my-lock");
    Assert.assertEquals(newAction.getTimeout(), 10);
    Assert.assertEquals(newAction.getTimeoutUnit(), TimeUnit.SECONDS);
  }
}
