package org.tiogasolutions.jobs.kernel.support;

import org.tiogasolutions.lib.jaxrs.TiogaJaxRsExceptionMapper;

public class JobsJaxRsExceptionMapper extends TiogaJaxRsExceptionMapper {

  public JobsJaxRsExceptionMapper() {
    super(true);
  }

  @Override
  protected void logInfo(String msg, Throwable ex) {
    System.out.println(msg);
  }

  @Override
  protected void logError(String msg, Throwable ex) {
    System.err.println(msg);
    ex.printStackTrace();
  }
}
