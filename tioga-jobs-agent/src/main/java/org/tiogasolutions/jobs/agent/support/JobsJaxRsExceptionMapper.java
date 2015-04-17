package org.tiogasolutions.jobs.agent.support;

import org.apache.log4j.Logger;
import org.tiogasolutions.lib.jaxrs.TiogaJaxRsExceptionMapper;

public class JobsJaxRsExceptionMapper extends TiogaJaxRsExceptionMapper {

  public JobsJaxRsExceptionMapper() {
    super(true);
  }

  @Override
  protected void logInfo(String msg, Throwable ex) {
    Logger.getLogger(JobsJaxRsExceptionMapper.class).info(msg, ex);
  }

  @Override
  protected void logError(String msg, Throwable ex) {
    Logger.getLogger(JobsJaxRsExceptionMapper.class).fatal(msg, ex);
  }
}
