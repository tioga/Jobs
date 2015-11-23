package org.tiogasolutions.jobs.agent.engine.resources;

import org.tiogasolutions.dev.common.EnvUtils;

import java.util.Map;

public class JobVariable {

  private final String text;
  private final String name;

  public JobVariable(String text) {
    this.text = text;
    this.name = text.substring(2, text.length()-1);
  }

  public String getText() {
    return text;
  }

  public String getName() {
    return name;
  }

  public static JobVariable findFirst(String text) {
    int posA = text.indexOf("${");
    if (posA < 0) return null;

    for (int posB = posA+2; posB < text.length(); posB++) {
      char chr = text.charAt(posB);

      if (Character.isWhitespace(chr)) {
        return null;

      } else if (chr == '}') {
        String key = text.substring(posA, posB+1);
        return new JobVariable(key);
      }
    }
    return null;
  }

  public String replace(Map<String,String> substitutionsMap, String command) {

    String value = findProperty(substitutionsMap);

    if (value == null) {
      String msg = String.format("The property \"%s\" was not found in the specified map, system properties or environment variables.", name);
      throw new IllegalArgumentException(msg);
    }
    return command.replace(text, value);
  }

  private String findProperty(Map<String,String> substitutionsMap) {

    // Check the map first.
    if (substitutionsMap.containsKey(name)) {
      return substitutionsMap.get(name);
    }

    // If not in the map then check the system properties and then environment.
    return EnvUtils.findProperty(name);
  }
}
