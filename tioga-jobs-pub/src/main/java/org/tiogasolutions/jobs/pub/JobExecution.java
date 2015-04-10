package org.tiogasolutions.jobs.pub;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jacobp on 4/1/2015.
 */
public class JobExecution {

  private final boolean completed;
  private final List<JobActionResult> results = new ArrayList<>();

  private JobExecution(boolean completed, Collection<? extends JobActionResult> results) {
    this.completed = completed;
    if (results != null) {
      this.results.addAll(results);
    }
  }

  @JsonIgnore
  public boolean isPending() {
    return completed == false;
  }

  public boolean isCompleted() {
    return completed;
  }

  public List<JobActionResult> getResults() {
    return results;
  }

  public static JobExecution pending() {
    return new JobExecution(false, Collections.emptyList());
  }

  public static JobExecution completed(Collection<? extends JobActionResult> results) {
    return new JobExecution(true, results);
  }
}
