package org.tiogasolutions.jobs.pub;

public enum ActionType {

  osCommand("org.tiogasolutions.jobs.pub.OsAction");

  private final String typeName;

  private ActionType(String typeName) {
    this.typeName = typeName;
  }

  public String getTypeName() {
    return typeName;
  }

  public boolean isOsCommand() {
    return this == osCommand;
  }
}
