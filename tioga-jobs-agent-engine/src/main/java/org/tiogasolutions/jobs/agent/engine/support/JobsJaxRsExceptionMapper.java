package org.tiogasolutions.jobs.agent.engine.support;

import org.tiogasolutions.lib.jaxrs.providers.TiogaJaxRsExceptionMapper;

import javax.ws.rs.ext.Provider;

@Provider
public class JobsJaxRsExceptionMapper extends TiogaJaxRsExceptionMapper {

  public JobsJaxRsExceptionMapper() {
  }

}
