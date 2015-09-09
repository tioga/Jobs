package org.tiogasolutions.jobs.common.engine;

import org.tiogasolutions.dev.common.exceptions.ExceptionUtils;
import org.tiogasolutions.jobs.pub.ResponseInfo;
import org.tiogasolutions.lib.jaxrs.TiogaJaxRsExceptionMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

  protected Response createResponse(int status, Throwable ex) {

    logException(ex, status);
    String message = ExceptionUtils.getMessage(ex);

    ResponseInfo responseInfo = new ResponseInfo(status, message);
    return Response.status(status).entity(responseInfo).type(MediaType.APPLICATION_JSON).build();
  }
}
