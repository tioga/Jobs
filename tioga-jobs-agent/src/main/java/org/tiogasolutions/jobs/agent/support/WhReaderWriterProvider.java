/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.jobs.agent.support;

import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.dev.jackson.TiogaJacksonObjectMapper;
import org.tiogasolutions.lib.jaxrs.jackson.JacksonReaderWriterProvider;
import org.tiogasolutions.jobs.agent.view.LocalResource;
import org.tiogasolutions.jobs.agent.view.Thymeleaf;
import org.tiogasolutions.jobs.pub.JobDefinition;
import org.tiogasolutions.jobs.pub.JobExecution;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WhReaderWriterProvider extends JacksonReaderWriterProvider {

  public WhReaderWriterProvider(@Context Application application) {
    super(new TiogaJacksonObjectMapper(), Arrays.asList(MediaType.APPLICATION_JSON_TYPE));
    addSupportedType(JobDefinition.class);
    addSupportedType(JobExecution.class);

    addSupportedType(ListQueryResult.class);

    addSupportedType(Thymeleaf.class);
    addSupportedType(LocalResource.class);
  }
}
