package org.tiogasolutions.jobs.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.tiogasolutions.jobs.pub.JobAction;

public class JobsJacksonModule extends SimpleModule {

  public JobsJacksonModule() {
    setMixInAnnotation(JobAction.class, JobActionMixin.class);
  }

}
