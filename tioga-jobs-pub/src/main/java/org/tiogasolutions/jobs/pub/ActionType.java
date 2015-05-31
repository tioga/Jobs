package org.tiogasolutions.jobs.pub;

public enum ActionType {

  OS_COMMAND("org.tiogasolutions.jobs.pub.actions.OsAction"),
  WAIT_FOR_HTTP("org.tiogasolutions.jobs.pub.actions.WaitForHttpAction");

  private final String typeName;

  private ActionType(String typeName) {
    this.typeName = typeName;
  }

  public String getTypeName() {
    return typeName;
  }

  public Class getType() {
    try {
      return Class.forName(typeName);

    } catch (ClassNotFoundException e) {
      String msg = String.format("The action type %s's implementing type %s was not found", name(), typeName);
      throw new UnsupportedOperationException(msg);
    }
  }
}
