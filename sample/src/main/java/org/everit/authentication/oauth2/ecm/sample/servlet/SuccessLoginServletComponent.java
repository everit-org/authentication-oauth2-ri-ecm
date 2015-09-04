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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.everit.authentication.oauth2.ecm.sample.internal.FacebookOAuth2UserIdResolverComponent;
import org.everit.authentication.oauth2.ecm.sample.internal.GoogleOAuth2UserIdResolverComponent;
import org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames;
import org.everit.authentication.oauth2.ri.schema.qdsl.QOAuth2UserMapping;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.component.ServiceHolder;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.osgi.querydsl.support.QuerydslSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Projections;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Servlet that shows the index page.
 */
@Component
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@Service(value = { Servlet.class, SuccessLoginServletComponent.class })
public class SuccessLoginServletComponent extends AbstractServlet {

  public static class User {

    public String providerName;

    public long resourceId;

    public String uniqueUserId;

    public User providerName(final String providerName) {
      this.providerName = providerName;
      return this;
    }

    public User resourceId(final long resourceId) {
      this.resourceId = resourceId;
      return this;
    }

    public User uniqueUserId(final String uniqueUserId) {
      this.uniqueUserId = uniqueUserId;
      return this;
    }

  }

  private String facebookRequestUri;

  private String googleRequestUri;

  private OAuth2SessionAttributeNames oauth2SessionAttributeNames;

  private QuerydslSupport querydslSupport;

  @Activate
  @Override
  public void activate(final BundleContext bundleContext) {
    super.activate(bundleContext);
  }

  private String getFullName(final HttpServletRequest req, final String requestUri) {
    HttpSession session = req.getSession();
    String accessToken =
        session.getAttribute(oauth2SessionAttributeNames.oauth2AccessToken()).toString();
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
    return "success_login";
  }

  private void renderContent(final HttpServletResponse resp, final String fullName)
      throws IOException {
    resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
    resp.setContentType("text/html");
    Map<String, Object> vars = new HashMap<>();
    vars.put("fullName", fullName);
    vars.put("users", selectAllSavedUser());
    vars.put("hasLogged", true); // TODO

    pageTemplate.render(resp.getWriter(), vars, null);
  }

  private List<User> selectAllSavedUser() {
    return querydslSupport.execute((connection, configuration) -> {
      QOAuth2UserMapping oauth2UserMapping = QOAuth2UserMapping.oAuth2UserMapping;
      return new SQLQuery(connection, configuration)
          .from(oauth2UserMapping)
          .list(Projections.fields(User.class, oauth2UserMapping.resourceId,
              oauth2UserMapping.providerName,
              oauth2UserMapping.providerUniqueUserId.as("uniqueUserId")));
    });
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    String providerName = req.getParameter("providerName");
    String fullName = null;
    if ("facebook".equals(providerName)) {
      fullName = getFullName(req, facebookRequestUri);
    } else if ("google".equals(providerName)) {
      fullName = getFullName(req, googleRequestUri);
    } else {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    renderContent(resp, fullName);
  }

  /**
   * TODO .
   */
  @ServiceRef(defaultValue = "")
  public void setFacebookRequestUri(
      final ServiceHolder<FacebookOAuth2UserIdResolverComponent> serviceHolder) {
    ServiceReference<FacebookOAuth2UserIdResolverComponent> serviceReference =
        serviceHolder.getReference();
    facebookRequestUri = serviceReference.getProperty("requestUri").toString();
  }

  /**
   * TODO .
   */
  @ServiceRef(defaultValue = "")
  public void setGoogleRequestUri(
      final ServiceHolder<GoogleOAuth2UserIdResolverComponent> serviceHolder) {
    ServiceReference<GoogleOAuth2UserIdResolverComponent> serviceReference =
        serviceHolder.getReference();
    googleRequestUri = serviceReference.getProperty("requestUri").toString();
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
