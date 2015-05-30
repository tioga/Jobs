/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tiogasolutions.jobs.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.tiogasolutions.jobs.pub.ActionType;
import org.tiogasolutions.jobs.pub.JobAction;

public class JobActionJacksonResolver implements TypeIdResolver {

  @Override
  public void init(JavaType baseType) {
  }

  @Override
  public String idFromValue(Object value) {
    JobAction action = (JobAction)value;
    return action.getActionType().name();
  }

  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    JobAction action = (JobAction)value;
    return action.getActionType().name();
  }

  @Override
  public String idFromBaseType() {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public JavaType typeFromId(String id) {
    return typeFromId(null, id);
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String id) {

    ActionType actionType = ActionType.valueOf(id);

    try {
      Class type = Class.forName(actionType.getTypeName());
      return TypeFactory.defaultInstance().uncheckedSimpleType(type);

    } catch (ClassNotFoundException e) {
      String msg = String.format("The action type %s's implementing type %s was not found", actionType, actionType.getTypeName());
      throw new UnsupportedOperationException(msg);
    }
  }

  @Override
  public JsonTypeInfo.Id getMechanism() {
    return JsonTypeInfo.Id.CUSTOM;
  }
}
