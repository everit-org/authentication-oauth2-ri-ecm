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
package org.everit.authentication.oauth2.ri.ecm.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.everit.authentication.context.AuthenticationContext;
import org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames;
import org.everit.authentication.oauth2.ri.schema.qdsl.QOAuth2Provider;
import org.everit.authentication.oauth2.ri.schema.qdsl.QOAuth2ResourceMapping;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.querydsl.support.QuerydslSupport;
import org.osgi.framework.BundleContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;

/**
 * Servlet that shows the welcome.html page.
 */
@ExtendComponent
@Component
@Service(value = { Servlet.class, WelcomeServletComponent.class })
public class WelcomeServletComponent extends AbstractServlet {

  private AuthenticationContext authenticationContext;

  private String facebookUserInformatiomRequestUri;

  private String googleUserInformationRequestUri;

  private OAuth2SessionAttributeNames oauth2SessionAttributeNames;

  private QuerydslSupport querydslSupport;

  @Activate
  @Override
  public void activate(final BundleContext bundleContext) {
    super.activate(bundleContext);
  }

  private String getFullName(final HttpSession session, final String providerName) {

    String requestUri;
    if ("facebook".equals(providerName)) {
      requestUri = facebookUserInformatiomRequestUri;
    } else if ("google".equals(providerName)) {
      requestUri = googleUserInformationRequestUri;
    } else {
      return null;
    }

    String accessToken =
        String.valueOf(session.getAttribute(oauth2SessionAttributeNames.accessToken()));

    OAuthResourceResponse resourceResponse;

    try {
      OAuthClientRequest resourceRequest =
          new OAuthBearerClientRequest(requestUri)
              .setAccessToken(accessToken)
              .buildHeaderMessage();

      OAuthClient client = new OAuthClient(new URLConnectionClient());
      resourceResponse = client.resource(resourceRequest, "GET", OAuthResourceResponse.class);

    } catch (OAuthSystemException | OAuthProblemException e) {
      throw new RuntimeException(e);
    }

    JsonObject fromJson = new Gson().fromJson(resourceResponse.getBody(), JsonObject.class);
    return fromJson.get("name").toString();
  }

  @Override
  protected String getPageId() {
    return "welcome";
  }

  private String getProviderName(final HttpSession session) {
    return String.valueOf(session.getAttribute(oauth2SessionAttributeNames.providerName()));
  }

  private String getResourceMappings() {

    QOAuth2Provider qoAuth2Provider = QOAuth2Provider.oAuth2Provider;
    QOAuth2ResourceMapping qoAuth2ResourceMapping = QOAuth2ResourceMapping.oAuth2ResourceMapping;

    List<Tuple> resourceMappings = selectCurrentUserResourceMappings(
        qoAuth2Provider, qoAuth2ResourceMapping);

    StringBuilder rval = new StringBuilder();

    for (Tuple mapping : resourceMappings) {
      rval = rval.append("<tr>")
          .append("<td>" + mapping.get(qoAuth2ResourceMapping.resourceId) + "</td>")
          .append("<td>" + mapping.get(qoAuth2Provider.providerName) + "</td>")
          .append("<td>" + mapping.get(qoAuth2ResourceMapping.providerUniqueUserId) + "</td>")
          .append("</tr>");
    }

    return rval.toString();
  }

  private boolean isAuthenticated() {
    return authenticationContext.getCurrentResourceId() != authenticationContext
        .getDefaultResourceId();
  }

  private void renderContent(final HttpServletResponse resp, final String providerName,
      final String fullName, final String resourceMappings) throws IOException {

    String authenticatedResourceId = String.valueOf(authenticationContext.getCurrentResourceId());

    resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
    resp.setContentType("text/html");

    String content = pageContent.replace("${FULL_NAME}", fullName);
    content = content.replace("${PROVIDER_NAME}", providerName);
    content = content.replace("${AUTHENTICATED_RESOURCE_ID}", authenticatedResourceId);
    content = content.replace("${RESOURCE_MAPPINGS}", resourceMappings);

    PrintWriter writer = resp.getWriter();
    writer.write(content);
  }

  private List<Tuple> selectCurrentUserResourceMappings(
      final QOAuth2Provider qoAuth2Provider, final QOAuth2ResourceMapping qoAuth2ResourceMapping) {

    long authenticatedResourceId = authenticationContext.getCurrentResourceId();

    return querydslSupport.execute((connection, configuration) -> {

      return new SQLQuery<Tuple>(connection, configuration)
          .select(qoAuth2ResourceMapping.resourceId,
              qoAuth2Provider.providerName,
              qoAuth2ResourceMapping.providerUniqueUserId)
          .from(qoAuth2ResourceMapping)
          .innerJoin(qoAuth2ResourceMapping.fk.oauth2ResourceMappingProviderFk, qoAuth2Provider)
          .where(qoAuth2ResourceMapping.resourceId.eq(authenticatedResourceId))
          .fetch();
    });
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {

    if (!isAuthenticated()) {
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      resp.getWriter().write("401 - Unauthorized");
      return;
    }

    HttpSession session = req.getSession();

    String providerName = getProviderName(session);
    String fullName = getFullName(session, providerName);
    String resourceMappings = getResourceMappings();

    renderContent(resp, providerName, fullName, resourceMappings);
  }

  @ServiceRef(defaultValue = "")
  public void setAuthenticationContext(final AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;
  }

  @StringAttribute(defaultValue = "https://graph.facebook.com/v2.4/me")
  public void setFacebookUserInformatiomRequestUri(final String facebookUserInformatiomRequestUri) {
    this.facebookUserInformatiomRequestUri = facebookUserInformatiomRequestUri;
  }

  @StringAttribute(defaultValue = "https://www.googleapis.com/userinfo/v2/me")
  public void setGoogleUserInformationRequestUri(final String googleUserInformationRequestUri) {
    this.googleUserInformationRequestUri = googleUserInformationRequestUri;
  }

  @ServiceRef(defaultValue = "")
  public void setOAuth2SessionAttributeNames(
      final OAuth2SessionAttributeNames oauth2SessionAttributeNames) {
    this.oauth2SessionAttributeNames = oauth2SessionAttributeNames;
  }

  @ServiceRef(defaultValue = "")
  public void setQuerydslSupport(final QuerydslSupport querydslSupport) {
    this.querydslSupport = querydslSupport;
  }

}
