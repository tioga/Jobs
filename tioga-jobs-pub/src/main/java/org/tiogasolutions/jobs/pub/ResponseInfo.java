package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseInfo {

  private final int status;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final String message;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final Map<String,LinkInfo> _links;

  @JsonCreator
  public ResponseInfo(@JsonProperty("status") int status,
                      @JsonProperty("message") String message,
                      @JsonProperty("_links") Map<String,LinkInfo> _links) {

    this.status = status;
    this.message = message;

    if (_links == null) {
      this._links = Collections.emptyMap();
    } else {
      this._links = Collections.unmodifiableMap(new LinkedHashMap<>(_links));
    }
  }

  public ResponseInfo(int status) {
    this(status, null, null);
  }

  public ResponseInfo(int status, String message) {
    this(status, message, null);
  }

  public ResponseInfo(int status, Map<String,LinkInfo> _links) {
    this(status, null, _links);
  }

  public ResponseInfo(int status, Exception ex) {
    this.status = status;
    this._links = Collections.emptyMap();

    if (ex == null) {
      this.message = null;

    } else if (ex.getMessage() != null) {
      this.message = ex.getMessage();

    } else {
      this.message = ex.getClass().getName();
    }
  }

  @SuppressWarnings("unused")
  public Map<String, LinkInfo> get_links() {
    return _links;
  }

  @SuppressWarnings("unused")
  public int getStatus() {
    return status;
  }

  @SuppressWarnings("unused")
  public String getMessage() {
    return message;
  }
}
