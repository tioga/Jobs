package org.tiogasolutions.jobs.pub;

import org.testng.annotations.Test;

@Test
public class ActionTypeTest {

  public void validateType() {
    for (ActionType actionType : ActionType.values()) {
      actionType.getType();
    }
  }
}