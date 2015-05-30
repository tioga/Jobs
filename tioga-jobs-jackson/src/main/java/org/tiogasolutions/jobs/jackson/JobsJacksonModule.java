package org.tiogasolutions.jobs.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.tiogasolutions.jobs.pub.JobAction;

/**
* Created by jacobp on 8/4/2014.
*/
public class JobsJacksonModule extends SimpleModule {
  public JobsJacksonModule() {
    setMixInAnnotation(JobAction.class, JobActionMixin.class);
  }
}
