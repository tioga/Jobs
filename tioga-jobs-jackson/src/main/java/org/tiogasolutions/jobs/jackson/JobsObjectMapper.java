package org.tiogasolutions.jobs.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tiogasolutions.dev.jackson.TiogaJacksonInjectable;
import org.tiogasolutions.dev.jackson.TiogaJacksonModule;
import org.tiogasolutions.dev.jackson.TiogaJacksonObjectMapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class JobsObjectMapper extends TiogaJacksonObjectMapper {

  public JobsObjectMapper() {
    super(Arrays.asList(
                new TiogaJacksonModule(),
                new JobsJacksonModule()),
        Collections.<TiogaJacksonInjectable>emptyList());
  }

  protected JobsObjectMapper(Collection<? extends Module> modules, Collection<? extends TiogaJacksonInjectable> injectables) {
    super(modules, injectables);
  }

  @Override
  public ObjectMapper copy() {
    _checkInvalidCopy(JobsObjectMapper.class);
    return new JobsObjectMapper(getModules(), getInjectables());
  }
}
