package org.tiogasolutions.jobs.agent.core.view;

import java.net.URL;

public class ThymeleafViewFactory {
  public static final String TAIL = ".html";
  public static final String ROOT = "/jobs-agent/view/";

  public static final String WELCOME = validate("welcome");

  private static String validate(String view) {
    String resource = ROOT+view+TAIL;
    URL url = ThymeleafViewFactory.class.getResource(resource);
    if (url == null) {
      String msg = String.format("The resource \"%s\" does not exist.", resource);
      throw new IllegalArgumentException(msg);
    }
    return view;
  }
}
