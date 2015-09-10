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
package org.everit.authentication.oauth2.ecm.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.everit.authentication.oauth2.OAuth2UserIdResolver;
import org.everit.authentication.oauth2.ecm.OAuth2AuthenticationServletConstants;
import org.everit.authentication.oauth2.ri.OAuth2AuthenticationServletParameter;
import org.everit.authentication.oauth2.ri.OAuth2Communicator;
import org.everit.authentication.oauth2.ri.OAuth2Services;
import org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames;
import org.everit.authentication.oauth2.ri.internal.OAuth2AuthenticationServlet;
import org.everit.osgi.authentication.http.session.AuthenticationSessionAttributeNames;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.ReferenceConfigurationType;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ConfigurationException;
import org.everit.osgi.ecm.component.ServiceHolder;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.osgi.resource.resolver.ResourceIdResolver;
import org.everit.web.servlet.HttpServlet;
import org.osgi.framework.Constants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * OAuth2 Authentication Servlet component.
 */
@Component(
    componentId = OAuth2AuthenticationServletConstants.SERVICE_FACTORYPID_OAUTH2_AUTHENTICATION_SERVLET, // CS_DISABLE_LINE_LENGTH
    configurationPolicy = ConfigurationPolicy.FACTORY)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_SERVICE_DESCRIPTION),
    @StringAttribute(attributeId = OAuth2AuthenticationServletConstants.PROP_PROVIDER_NAME,
        defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_PROVIDER_NAME)
})
@Service(value = { Servlet.class, OAuth2AuthenticationServletComponent.class,
    OAuth2SessionAttributeNames.class })
public class OAuth2AuthenticationServletComponent extends HttpServlet
    implements OAuth2SessionAttributeNames {

  private static final String PARAM_PROVIDER_NAME = "providerName";

  private AuthenticationSessionAttributeNames authenticationSessionAttributeNames;

  private String failedUrl;

  private String loginEndpointPath;

  private Map<String, OAuth2AuthenticationServlet> oauth2AuthenticationServlets = new HashMap<>();

  private Map<String, OAuth2Communicator> oauth2Commuicators = new HashMap<>();

  private Map<String, OAuth2UserIdResolver> oauth2UserIdResolvers = new HashMap<>();

  private List<String> providers = new ArrayList<>();

  private String redirectEndpointPath;

  private Map<String, ResourceIdResolver> resourceIdResolvers = new HashMap<>();

  private String successUrl;

  /**
   * Component activator method.
   */
  @Activate
  public void activate() {
    oauth2AuthenticationServlets.clear();
    providers.forEach((providerName) -> {
      OAuth2AuthenticationServletParameter oauth2AuthenticationServletParameter =
          new OAuth2AuthenticationServletParameter()
              .authenticationSessionAttributeNames(authenticationSessionAttributeNames)
              .failedUrl(failedUrl)
              .loginEndpointPath(loginEndpointPath)
              .oauth2Communicator(oauth2Commuicators.get(providerName))
              .oauth2UserIdResolver(oauth2UserIdResolvers.get(providerName))
              .redirectEndpointPath(redirectEndpointPath)
              .resourceIdResolver(resourceIdResolvers.get(providerName))
              .successUrl(successUrl);

      OAuth2AuthenticationServlet oauth2AuthenticationServlet =
          new OAuth2AuthenticationServlet(oauth2AuthenticationServletParameter);
      oauth2AuthenticationServlets.put(providerName, oauth2AuthenticationServlet);
    });
  }

  private OAuth2AuthenticationServlet getFirstAuthenticationServlet() {
    return oauth2AuthenticationServlets.values()
        .stream()
        .findFirst()
        .orElseGet(() -> {
          throw new RuntimeException("Not find Authentication Servlet");
        });
  }

  @Override
  public String accessToken() {
    return getFirstAuthenticationServlet().accessToken();
  }

  @Override
  public String accessTokenExpiresIn() {
    return getFirstAuthenticationServlet().accessTokenExpiresIn();
  }

  @Override
  public String refreshToken() {
    return getFirstAuthenticationServlet().refreshToken();
  }

  @Override
  public String scope() {
    return getFirstAuthenticationServlet().scope();
  }

  @Override
  public String tokenType() {
    return getFirstAuthenticationServlet().tokenType();
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    OAuth2AuthenticationServlet oauth2AuthenticationServlet;
    if (oauth2AuthenticationServlets.size() == 1) {
      oauth2AuthenticationServlet = getFirstAuthenticationServlet();
    } else {
      String providerName = req.getParameter(PARAM_PROVIDER_NAME);
      oauth2AuthenticationServlet = oauth2AuthenticationServlets.get(providerName);
      if (oauth2AuthenticationServlet == null) {
        throw new IllegalArgumentException("Not supported provider [" + providerName + "]");
      }
    }
    oauth2AuthenticationServlet.service(req, resp);
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.SERVICE_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES, // CS_DISABLE_LINE_LENGTH
      defaultValue = "")
  public void setAuthenticationSessionAttributeNames(
      final AuthenticationSessionAttributeNames authenticationSessionAttributeNames) {
    this.authenticationSessionAttributeNames = authenticationSessionAttributeNames;
  }

  @StringAttribute(attributeId = OAuth2AuthenticationServletConstants.PROP_FAILED_URL,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_FAILED_URL)
  public void setFailedUrl(final String failedUrl) {
    this.failedUrl = failedUrl;
  }

  @StringAttribute(attributeId = OAuth2AuthenticationServletConstants.PROP_LOGIN_ENDPOINT_PATH,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_LOGIN_ENDPOINT_PATH)
  public void setLoginEndpointPath(final String loginEndpointPath) {
    this.loginEndpointPath = loginEndpointPath;
  }

  /**
   * Sets OAuth2 services.
   */
  @ServiceRef(attributeId = OAuth2AuthenticationServletConstants.SERVICE_OAUTH2_SERVICES_CLAUSE,
      configurationType = ReferenceConfigurationType.CLAUSE, dynamic = true)
  public void setOAuth2Services(final ServiceHolder<OAuth2Services>[] oauth2Services) {
    if ((oauth2Services == null) || (oauth2Services.length == 0)) {
      throw new ConfigurationException("OAuth2 services is empty.");
    }

    providers.clear();
    oauth2Commuicators.clear();
    resourceIdResolvers.clear();
    oauth2UserIdResolvers.clear();
    for (ServiceHolder<OAuth2Services> serviceHolder : oauth2Services) {
      String providerName = serviceHolder.getReferenceId();
      OAuth2Services service = serviceHolder.getService();
      if (service == null) {
        throw new ConfigurationException("Not found service");
      }
      oauth2Commuicators.put(providerName, service);
      resourceIdResolvers.put(providerName, service);
      oauth2UserIdResolvers.put(providerName, service);
      providers.add(providerName);
    }
  }

  @StringAttribute(attributeId = OAuth2AuthenticationServletConstants.PROP_REDIRECT_ENDPOINT_PATH,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_REDIRECT_ENDPOINT_PATH)
  public void setRedirectEndpointPath(final String redirectEndpointPath) {
    this.redirectEndpointPath = redirectEndpointPath;
  }

  @StringAttribute(attributeId = OAuth2AuthenticationServletConstants.PROP_SUCCESS_URL,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_SUCCESS_URL)
  public void setSuccessUrl(final String successUrl) {
    this.successUrl = successUrl;
  }

}
