package org.tiogasolutions.workhorse.agent.view;

import org.tiogasolutions.dev.common.IoUtils;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class LocalResourceMessageBodyWriter implements MessageBodyWriter<LocalResource> {

  @Context UriInfo uriInfo;

  public LocalResourceMessageBodyWriter() {
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return LocalResource.class.equals(type);
  }

  @Override
  public long getSize(LocalResource localResource, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(LocalResource localResource, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    String view = localResource.getView();
    String resource = "/push-server-app/view" + (view.startsWith("/") ? "" : "/") + view;
    InputStream is = getClass().getResourceAsStream(resource);

    if (is == null) {
      throw new NotFoundException("View: " + view);
    }

    byte[] bytes = IoUtils.toBytes(is);
    entityStream.write(bytes);
  }
}
