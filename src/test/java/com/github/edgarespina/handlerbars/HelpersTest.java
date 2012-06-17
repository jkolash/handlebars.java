package com.github.edgarespina.handlerbars;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.runners.Parameterized.Parameters;

import specs.Spec;
import specs.SpecTest;

public class HelpersTest extends SpecTest {

  public HelpersTest(final Spec spec) {
    super(spec);
  }

  @Override
  protected Handlebars configure(final Handlebars handlebars) {
    handlebars.registerHelper("list", new Helper<List<Object>>() {
      @Override
      public CharSequence apply(final List<Object> list, final Options options)
          throws IOException {
        StringBuilder buffer = new StringBuilder();
        if (!options.empty(list)) {
          buffer.append("<ul>\n");
          for (Object element : list) {
            buffer.append("  <li>\n    ").append(options.fn(element))
                .append("  </li>\n");
          }
          buffer.append("</ul>\n");
        }
        return buffer.toString();
      }
    });
    handlebars.registerHelper("fullName", new Helper<Map<String, Object>>() {
      @Override
      public CharSequence apply(final Map<String, Object> context,
          final Options options) throws IOException {
        return context.get("firstName") + " " + context.get("lastName");
      }
    });
    handlebars.registerHelper("agree_button",
        new Helper<Map<String, Object>>() {
          @Override
          public CharSequence apply(final Map<String, Object> context,
              final Options options)
              throws IOException {
            String text = "<button>I agree. I " + context.get("emotion") + " "
                + context.get("name") + "</button>";
            return Handlebars.safeString(text);
          }
        });
    handlebars.registerHelper("link",
        new Helper<Object>() {
          @Override
          public CharSequence apply(final Object context,
              final Options options)
              throws IOException {
            return "<a href='" + options.param(0) + "'>"
                + context + "</a>";
          }
        });
    handlebars.registerHelper("link-hash",
        new Helper<String>() {
          @Override
          public CharSequence apply(final String text,
              final Options options)
              throws IOException {
            StringBuilder classes = new StringBuilder();
            String sep = " ";
            for (Entry<String, Object> entry : options.hash()) {
              classes.append(entry.getKey()).append("=\"")
                  .append(entry.getValue()).append("\"").append(sep);
            }
            classes.setLength(classes.length() - sep.length());
            return new SafeString("<a " + classes + ">" + text + "</a>");
          }
        });
    return super.configure(handlebars);
  }

  @Parameters
  public static Collection<Object[]> data() {
    return data(HelpersTest.class, "helpers.yml");
  }
}