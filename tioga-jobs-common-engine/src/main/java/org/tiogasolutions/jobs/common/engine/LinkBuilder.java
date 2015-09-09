package org.tiogasolutions.jobs.common.engine;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.tiogasolutions.dev.common.ReflectUtils;
import org.tiogasolutions.dev.common.exceptions.ExceptionUtils;
import org.tiogasolutions.jobs.pub.LinkInfo;

import javax.ws.rs.core.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

public class LinkBuilder {

  private final Map<String,LinkInfo> links = new LinkedHashMap<>();
  private final UriInfo uriInfo;

  private String tempName;
  private String tempRel;
  private String tempTitle;

  public LinkBuilder(UriInfo uriInfo) {
    this.uriInfo = ExceptionUtils.assertNotNull(uriInfo, "uriInfo");
  }

  public Map<String,LinkInfo> toMap() {
    return Collections.unmodifiableMap(links);
  }

  public UriBuilder link(String name) {
    this.tempRel = null;
    this.tempName = name;
    this.tempTitle = null;
    return new UriBuilderProxy(uriInfo.getAbsolutePathBuilder());
  }

  protected void finish(URI href) {
    LinkInfo linkInfo = new LinkInfo(tempRel, href, tempTitle);
    links.put(tempName, linkInfo);

    this.tempRel = null;
    this.tempName = null;
    this.tempTitle = null;
  }

  public Link[] toLinkArray() {

    List<Link> list = new ArrayList<>();

    for (Map.Entry<String,LinkInfo> entry : links.entrySet()) {
      Link link = new CustomLink(
        entry.getValue().getHref(),
        entry.getKey(),
        entry.getValue().getRel(),
        entry.getValue().getTitle());

      list.add(link);
    }

    return ReflectUtils.toArray(Link.class, list);
  }

  public Response.ResponseBuilder addLinks(Response.ResponseBuilder builder) {

    for (Map.Entry<String,LinkInfo> entry : links.entrySet()) {

      if (entry.getValue().getRel() != null) {
        builder.link(entry.getValue().getHref(), entry.getValue().getRel());

      } else {
        builder.link(entry.getValue().getHref(), entry.getKey());
      }
    }

    return builder;
  }

  private class CustomLink extends Link {

    private final URI uri;
    private final Map<String,String> params = new LinkedHashMap<>();

    private CustomLink(URI uri, String name, String rel, String title) {
      this.uri = uri;
      params.put("name", name);
      if (rel != null) params.put("rel", rel);
      if (title != null) params.put("title", title);
    }

    @Override
    public URI getUri() {
      return uri;
    }

    @Override
    public UriBuilder getUriBuilder() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getRel() {
      return params.get("rel");
    }

    @Override
    public List<String> getRels() {
      if (params.containsKey("rel")) {
        return Collections.singletonList(getRel());
      } else {
        return Collections.emptyList();
      }
    }

    @Override
    public String getTitle() {
      return params.get("title");
    }

    @Override
    public String getType() {
      return params.get("type");
    }

    @Override
    public Map<String, String> getParams() {
      return params;
    }

    @Override
    public String toString() {
      return "CustomLink{" +
        "uri=" + uri +
        ", params=" + params +
        '}';
    }
  }

  private class UriBuilderProxy extends UriBuilder {

    private final UriBuilder proxy;

    private UriBuilderProxy(UriBuilder proxy) {
      this.proxy = proxy;
    }

    public UriBuilder name(String name) {
      LinkBuilder.this.tempName = name;
      return this;
    }

    public UriBuilder rel(String rel) {
      LinkBuilder.this.tempRel = rel;
      return this;
    }

    public UriBuilder title(String title) {
      LinkBuilder.this.tempTitle = title;
      return this;
    }

    @Override
    public UriBuilder clone() {
      proxy.clone();
      return this;
    }

    @Override
    public UriBuilder uri(URI uri) {
      proxy.uri(uri);
      return this;
    }

    @Override
    public UriBuilder uri(String uriTemplate) {
      proxy.uri(uriTemplate);
      return this;
    }

    @Override
    public UriBuilder scheme(String scheme) {
      proxy.scheme(scheme);
      return this;
    }

    @Override
    public UriBuilder schemeSpecificPart(String ssp) {
      proxy.schemeSpecificPart(ssp);
      return this;
    }

    @Override
    public UriBuilder userInfo(String ui) {
      proxy.userInfo(ui);
      return this;
    }

    @Override
    public UriBuilder host(String host) {
      proxy.host(host);
      return this;
    }

    @Override
    public UriBuilder port(int port) {
      proxy.port(port);
      return this;
    }

    @Override
    public UriBuilder replacePath(String path) {
      proxy.replacePath(path);
      return this;
    }

    @Override
    public UriBuilder path(String path) {
      proxy.path(path);
      return this;
    }

    @Override
    public UriBuilder path(Class resource) {
      proxy.path(resource);
      return this;
    }

    @Override
    public UriBuilder path(Class resource, String method) {
      proxy.path(resource, method);
      return this;
    }

    @Override
    public UriBuilder path(Method method) {
      proxy.path(method);
      return this;
    }

    @Override
    public UriBuilder segment(String... segments) {
      proxy.segment(segments);
      return this;
    }

    @Override
    public UriBuilder replaceMatrix(String matrix) {
      proxy.replaceMatrix(matrix);
      return this;
    }

    @Override
    public UriBuilder matrixParam(String name, Object... values) {
      proxy.matrixParam(name, values);
      return this;
    }

    @Override
    public UriBuilder replaceMatrixParam(String name, Object... values) {
      proxy.replaceMatrixParam(name, values);
      return this;
    }

    @Override
    public UriBuilder replaceQuery(String query) {
      proxy.replaceQuery(query);
      return this;
    }

    @Override
    public UriBuilder queryParam(String name, Object... values) {
      proxy.queryParam(name, values);
      return this;
    }

    @Override
    public UriBuilder replaceQueryParam(String name, Object... values) {
      proxy.replaceQueryParam(name, values);
      return this;
    }

    @Override
    public UriBuilder fragment(String fragment) {
      proxy.fragment(fragment);
      return this;
    }

    @Override
    public UriBuilder resolveTemplate(String name, Object value) {
      proxy.resolveTemplate(name, value);
      return this;
    }

    @Override
    public UriBuilder resolveTemplate(String name, Object value, boolean encodeSlashInPath) {
      proxy.resolveTemplate(name, value, encodeSlashInPath);
      return this;
    }

    @Override
    public UriBuilder resolveTemplateFromEncoded(String name, Object value) {
      proxy.resolveTemplateFromEncoded(name, value);
      return this;
    }

    @Override
    public UriBuilder resolveTemplates(Map<String, Object> templateValues) {
      proxy.resolveTemplates(templateValues);
      return this;
    }

    @Override
    public UriBuilder resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath) throws IllegalArgumentException {
      proxy.resolveTemplates(templateValues, encodeSlashInPath);
      return this;
    }

    @Override
    public UriBuilder resolveTemplatesFromEncoded(Map<String, Object> templateValues) {
      proxy.resolveTemplatesFromEncoded(templateValues);
      return this;
    }

    @Override
    public String toTemplate() {
      return proxy.toTemplate();
    }

    @Override
    public URI buildFromMap(Map<String, ?> values) {
      URI uri = proxy.buildFromMap(values);
      LinkBuilder.this.finish(uri);
      return uri;
    }

    @Override
    public URI buildFromMap(Map<String, ?> values, boolean encodeSlashInPath) throws IllegalArgumentException, UriBuilderException {
      URI uri = proxy.buildFromMap(values, encodeSlashInPath);
      LinkBuilder.this.finish(uri);
      return uri;
    }

    @Override
    public URI buildFromEncodedMap(Map<String, ?> values) throws IllegalArgumentException, UriBuilderException {
      URI uri = proxy.buildFromEncodedMap(values);
      LinkBuilder.this.finish(uri);
      return uri;
    }

    @Override
    public URI build(Object... values) throws IllegalArgumentException, UriBuilderException {
      URI uri = proxy.build(values);
      LinkBuilder.this.finish(uri);
      return uri;
    }

    @Override
    public URI build(Object[] values, boolean encodeSlashInPath) throws IllegalArgumentException, UriBuilderException {
      URI uri = proxy.build(values, encodeSlashInPath);
      LinkBuilder.this.finish(uri);
      return uri;
    }

    @Override
    public URI buildFromEncoded(Object... values) throws IllegalArgumentException, UriBuilderException {
      URI uri = proxy.buildFromEncoded(values);
      LinkBuilder.this.finish(uri);
      return uri;
    }
  }
}
