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

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.everit.authentication.oauth2.OAuth2Configuration;
import org.everit.authentication.oauth2.OAuth2UserIdResolver;
import org.everit.authentication.oauth2.ecm.OAuth2AuthenticationConstants;
import org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames;
import org.everit.authentication.oauth2.ri.internal.OAuth2AuthenticationServlet;
import org.everit.osgi.authentication.http.session.AuthenticationSessionAttributeNames;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.osgi.resource.resolver.ResourceIdResolver;
import org.everit.web.servlet.HttpServlet;
import org.osgi.framework.Constants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * OAuth2 Authentication Servlet component.
 */
@Component(
    componentId = OAuth2AuthenticationConstants.SERVICE_FACTORYPID_OAUTH2_AUTHENTICATION_SERVLET,
    configurationPolicy = ConfigurationPolicy.FACTORY)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2AuthenticationConstants.DEFAULT_SERVICE_DESCRIPTION),
    @StringAttribute(attributeId = OAuth2AuthenticationConstants.PROP_PROVIDER_NAME,
        defaultValue = OAuth2AuthenticationConstants.DEFAULT_PROVIDER_NAME)
})
@Service(value = { Servlet.class, OAuth2AuthenticationServletComponent.class,
    OAuth2SessionAttributeNames.class })
public class OAuth2AuthenticationServletComponent extends HttpServlet
    implements OAuth2SessionAttributeNames {

  private AuthenticationSessionAttributeNames authenticationSessionAttributeNames;

  private String failedUrl;

  private String loginEndpointPath;

  private OAuth2AuthenticationServlet oauth2AuthenticationServlet;

  private OAuth2Configuration oauth2Configuration;

  private OAuth2UserIdResolver oauth2UserIdResolver;

  private String redirectEndpointPath;

  private ResourceIdResolver resourceIdResolver;

  private String successUrl;

  /**
   * Activator method.
   */
  @Activate
  public void activate() {
    oauth2AuthenticationServlet = new OAuth2AuthenticationServlet(
        authenticationSessionAttributeNames, failedUrl, loginEndpointPath, oauth2Configuration,
        redirectEndpointPath, oauth2UserIdResolver, resourceIdResolver, successUrl);
  }

  @Deactivate
  public void deactivate() {
    oauth2AuthenticationServlet = null;
  }

  @Override
  public void destroy() {
    super.destroy();
    oauth2AuthenticationServlet.destroy();
  }

  @Override
  public void init(final ServletConfig pConfig) throws ServletException {
    super.init(pConfig);
    oauth2AuthenticationServlet.init(pConfig);
  }

  @Override
  public String oauth2AccessToken() {
    return oauth2AuthenticationServlet.oauth2AccessToken();
  }

  @Override
  public String oauth2AccessTokenExpiresIn() {
    return oauth2AuthenticationServlet.oauth2AccessTokenExpiresIn();
  }

  @Override
  public String oauth2RefreshToken() {
    return oauth2AuthenticationServlet.oauth2RefreshToken();
  }

  @Override
  public String oauth2Scope() {
    return oauth2AuthenticationServlet.oauth2Scope();
  }

  @Override
  public String oauth2TokenType() {
    return oauth2AuthenticationServlet.oauth2TokenType();
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    oauth2AuthenticationServlet.service(req, resp);
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationConstants.PROP_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES,
      defaultValue = "")
  public void setAuthenticationSessionAttributeNames(
      final AuthenticationSessionAttributeNames authenticationSessionAttributeNames) {
    this.authenticationSessionAttributeNames = authenticationSessionAttributeNames;
  }

  @StringAttribute(attributeId = OAuth2AuthenticationConstants.PROP_FAILED_URL,
      defaultValue = OAuth2AuthenticationConstants.DEFAULT_FAILED_URL)
  public void setFailedUrl(final String failedUrl) {
    this.failedUrl = failedUrl;
  }

  @StringAttribute(attributeId = OAuth2AuthenticationConstants.PROP_LOGIN_ENDPOINT_PATH,
      defaultValue = OAuth2AuthenticationConstants.DEFAULT_LOGIN_ENDPOINT_PATH)
  public void setLoginEndpointPath(final String loginEndpointPath) {
    this.loginEndpointPath = loginEndpointPath;
  }

  @ServiceRef(attributeId = OAuth2AuthenticationConstants.PROP_OAUTH2_CONFIGURATION,
      defaultValue = "")
  public void setOAuth2Configuration(final OAuth2Configuration oauth2Configuration) {
    this.oauth2Configuration = oauth2Configuration;
  }

  @ServiceRef(attributeId = OAuth2AuthenticationConstants.PROP_REQUEST_URI_RESOLVER,
      defaultValue = "")
  public void setOAuth2UserIdResolver(final OAuth2UserIdResolver oauth2UserIdResolver) {
    this.oauth2UserIdResolver = oauth2UserIdResolver;
  }

  @StringAttribute(attributeId = OAuth2AuthenticationConstants.PROP_REDIRECT_ENDPOINT_PATH,
      defaultValue = OAuth2AuthenticationConstants.DEFAULT_REDIRECT_ENDPOINT_PATH)
  public void setRedirectEndpointPath(final String redirectEndpointPath) {
    this.redirectEndpointPath = redirectEndpointPath;
  }

  @ServiceRef(attributeId = OAuth2AuthenticationConstants.PROP_RESOURCE_ID_RESOLVER,
      defaultValue = "")
  public void setResourceIdResolver(final ResourceIdResolver resourceIdResolver) {
    this.resourceIdResolver = resourceIdResolver;
  }

  @StringAttribute(attributeId = OAuth2AuthenticationConstants.PROP_SUCCESS_URL,
      defaultValue = OAuth2AuthenticationConstants.DEFAULT_SUCCESS_URL)
  public void setSuccessUrl(final String successUrl) {
    this.successUrl = successUrl;
  }

}
