package org.tiogasolutions.jobs.agent.support;

public class JobsException extends RuntimeException {
  public JobsException() {
  }

  public JobsException(String message) {
    super(message);
  }

  public JobsException(String message, Throwable cause) {
    super(message, cause);
  }

  public JobsException(Throwable cause) {
    super(cause);
  }

  public JobsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
