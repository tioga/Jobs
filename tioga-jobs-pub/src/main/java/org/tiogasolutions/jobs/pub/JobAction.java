package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.tiogasolutions.jobs.pub.ActionType;

public interface JobAction {

  public ActionType getActionType();

  public String getLock();

  @JsonIgnore
  String getLabel();
}
