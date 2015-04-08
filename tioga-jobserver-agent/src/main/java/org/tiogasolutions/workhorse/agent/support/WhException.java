package org.tiogasolutions.workhorse.agent.support;

public class WhException extends RuntimeException {
  public WhException() {
  }

  public WhException(String message) {
    super(message);
  }

  public WhException(String message, Throwable cause) {
    super(message, cause);
  }

  public WhException(Throwable cause) {
    super(cause);
  }

  public WhException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
