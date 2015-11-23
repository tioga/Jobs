/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.jobs.agent.engine.support;

import org.tiogasolutions.dev.domain.query.ListQueryResult;
import org.tiogasolutions.jobs.jackson.JobsObjectMapper;
import org.tiogasolutions.jobs.pub.DomainProfile;
import org.tiogasolutions.jobs.pub.JobExecutionRequest;
import org.tiogasolutions.lib.jaxrs.jackson.JacksonReaderWriterProvider;
import org.tiogasolutions.jobs.pub.JobDefinition;
import org.tiogasolutions.jobs.pub.JobParameters;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobsReaderWriterProvider extends JacksonReaderWriterProvider {

  public JobsReaderWriterProvider(@Context Application application) {
    super(new JobsObjectMapper(), Collections.singletonList(MediaType.APPLICATION_JSON_TYPE));
    addSupportedType(DomainProfile.class);

    addSupportedType(JobDefinition.class);
    addSupportedType(JobParameters.class);
    addSupportedType(JobExecutionRequest.class);

    addSupportedType(ListQueryResult.class);
  }
}
