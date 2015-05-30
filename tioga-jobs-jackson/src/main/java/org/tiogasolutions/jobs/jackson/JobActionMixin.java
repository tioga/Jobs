package org.tiogasolutions.jobs.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeInfo(use=JsonTypeInfo.Id.CUSTOM, property="actionType")
@JsonTypeIdResolver(JobActionJacksonResolver.class)
public abstract class JobActionMixin {
}
