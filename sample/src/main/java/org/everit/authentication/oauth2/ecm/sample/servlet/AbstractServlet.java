/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.authentication.oauth2.ecm.sample.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.MvelExpressionCompiler;
import org.everit.templating.CompiledTemplate;
import org.everit.templating.TemplateCompiler;
import org.everit.templating.html.HTMLTemplateCompiler;
import org.everit.templating.text.TextTemplateCompiler;
import org.everit.web.servlet.HttpServlet;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

public abstract class AbstractServlet extends HttpServlet {

  protected ClassLoader classLoader;

  protected CompiledTemplate pageTemplate;

  /**
   * Compiles the layout template and sets the classLoader member variable.
   */
  public void activate(final BundleContext bundleContext) {
    classLoader = bundleContext.getBundle().adapt(BundleWiring.class).getClassLoader();
    pageTemplate = compileTemplate("META-INF/webcontent/" + getPageId() + ".html");
  }

  /**
   * Compiles a template.
   *
   * @param templateName
   *          The url of the template resource.
   * @param classLoader
   *          The classLoader that is used to find the template resource.
   * @return The compiled template.
   */
  private CompiledTemplate compileTemplate(final String templateName) {
    MvelExpressionCompiler mvelExpressionCompiler = new MvelExpressionCompiler();

    Map<String, TemplateCompiler> inlineCompilers = new HashMap<>();
    inlineCompilers.put("text", new TextTemplateCompiler(mvelExpressionCompiler));

    HTMLTemplateCompiler htmlTemplateCompiler = new HTMLTemplateCompiler(mvelExpressionCompiler,
        inlineCompilers);
    ParserConfiguration parserConfiguration = new ParserConfiguration(classLoader);

    String template = readResourceContent(templateName);

    if (template == null) {
      return null;
    }

    CompiledTemplate result = htmlTemplateCompiler.compile(template, parserConfiguration);
    return result;
  }

  protected abstract String getPageId();

  /**
   * Reads the content of a resource into a String with UTF8 character encoding.
   *
   * @param resource
   *          The name of the resource.
   * @param classLoader
   *          The ClassLoader that sees the resource.
   * @return The content of the file.
   */
  private String readResourceContent(final String resource) {
    URL resourceURL = classLoader.getResource(resource);

    if (resourceURL == null) {
      return null;
    }

    try (InputStream is = resourceURL.openStream()) {
      final int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];
      int r = is.read(buffer);
      StringBuilder sb = new StringBuilder();
      while (r >= 0) {
        sb.append(new String(buffer, 0, r, StandardCharsets.UTF_8));
        r = is.read(buffer);
      }
      return sb.toString();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
