/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.jobs.agent.engine.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.jobs.jackson.JobsObjectMapper;
import org.tiogasolutions.lib.jaxrs.jackson.TiogaReaderWriterProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobsReaderWriterProvider extends TiogaReaderWriterProvider {

  @Autowired
  public JobsReaderWriterProvider(JobsObjectMapper objectMapper) {
    super(objectMapper);
  }
}
