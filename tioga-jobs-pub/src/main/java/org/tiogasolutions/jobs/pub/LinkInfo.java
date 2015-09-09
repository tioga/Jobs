package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public class LinkInfo {

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final URI href;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final String rel;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final String title;

  @JsonCreator
  public LinkInfo(@JsonProperty("rel") String rel,
                  @JsonProperty("href") URI href,
                  @JsonProperty("title") String title) {
    this.rel = rel;
    this.href = href;
    this.title = title;
  }

  public LinkInfo(String rel, URI href) {
    this.rel = rel;
    this.href = href;
    this.title = null;
  }

  public String getRel() {
    return rel;
  }

  public URI getHref() {
    return href;
  }

  public String getTitle() {
    return title;
  }
}
