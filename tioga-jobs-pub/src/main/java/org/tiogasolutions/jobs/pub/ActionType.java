package org.tiogasolutions.jobs.pub;

/**
 * Created by jacobp on 4/1/2015.
 */
public enum ActionType {

  osCommand;

  private ActionType() {
  }

  public boolean isOsCommand() {
    return this == osCommand;
  }
}
