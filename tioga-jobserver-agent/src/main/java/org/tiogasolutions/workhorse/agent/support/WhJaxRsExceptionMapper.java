package org.tiogasolutions.workhorse.agent.support;

import org.apache.log4j.Logger;
import org.tiogasolutions.lib.jaxrs.TiogaJaxRsExceptionMapper;

public class WhJaxRsExceptionMapper extends TiogaJaxRsExceptionMapper {

  public WhJaxRsExceptionMapper() {
    super(true);
  }

  @Override
  protected void logInfo(String msg, Throwable ex) {
    Logger.getLogger(WhJaxRsExceptionMapper.class).info(msg, ex);
  }

  @Override
  protected void logError(String msg, Throwable ex) {
    Logger.getLogger(WhJaxRsExceptionMapper.class).fatal(msg, ex);
  }
}
