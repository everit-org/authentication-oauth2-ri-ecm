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
package org.everit.authentication.oauth2.ri.ecm.internal;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.everit.authentication.oauth2.OAuth2Communicator;
import org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames;
import org.everit.authentication.oauth2.ri.core.OAuth2AuthenticationServlet;
import org.everit.authentication.oauth2.ri.ecm.OAuth2AuthenticationServletConstants;
import org.everit.osgi.authentication.http.session.AuthenticationSessionAttributeNames;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
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
 * ECM component for {@link OAuth2AuthenticationServlet}.
 */
@Component(
    componentId = OAuth2AuthenticationServletConstants.SERVICE_FACTORYPID_OAUTH2_AUTHENTICATION_SERVLET, // CS_DISABLE_LINE_LENGTH
    configurationPolicy = ConfigurationPolicy.FACTORY)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_SERVICE_DESCRIPTION),
    @StringAttribute(attributeId = OAuth2AuthenticationServletConstants.ATTR_PROVIDER_NAME) })
@Service(Servlet.class)
public class OAuth2AuthenticationServletComponent
    extends HttpServlet {

  private AuthenticationSessionAttributeNames authenticationSessionAttributeNames;

  private String failedUrl;

  private HttpServlet oauth2AuthenticationServlet;

  private OAuth2Communicator oauth2Communicator;

  private OAuth2SessionAttributeNames oauth2SessionAttributeNames;

  private String processRequestTokenPathInfo;

  private ResourceIdResolver resourceIdResolver;

  private String successUrl;

  /**
   * Component activator method that instantiates the wrapped {@link OAuth2AuthenticationServlet}.
   */
  @Activate
  public void activate() {
    oauth2AuthenticationServlet =
        new OAuth2AuthenticationServlet(successUrl, failedUrl, processRequestTokenPathInfo,
            oauth2Communicator,
            resourceIdResolver,
            authenticationSessionAttributeNames,
            oauth2SessionAttributeNames);
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    oauth2AuthenticationServlet.service(req, resp);
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES, // CS_DISABLE_LINE_LENGTH
      defaultValue = "")
  public void setAuthenticationSessionAttributeNames(
      final AuthenticationSessionAttributeNames authenticationSessionAttributeNames) {
    this.authenticationSessionAttributeNames = authenticationSessionAttributeNames;
  }

  @StringAttribute(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_SUCCESS_URL,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_SUCCESS_URL)
  public void setAuthenticationSuccessUrl(final String successUrl) {
    this.successUrl = successUrl;
  }

  @StringAttribute(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_FAILED_URL,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_FAILED_URL)
  public void setFailedUrl(final String failedUrl) {
    this.failedUrl = failedUrl;
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_OAUTH2_COMMUNICATOR,
      defaultValue = "")
  public void setOauth2Communicator(final OAuth2Communicator oauth2Communicator) {
    this.oauth2Communicator = oauth2Communicator;
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_OAUTH2_SESSION_ATTRIBUTE_NAMES,
      defaultValue = "")
  public void setOauth2SessionAttributeNames(
      final OAuth2SessionAttributeNames oauth2SessionAttributeNames) {
    this.oauth2SessionAttributeNames = oauth2SessionAttributeNames;
  }

  @StringAttribute(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_PROCESS_REQUEST_TOKEN_PATH_INFO,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_PROCESS_REQUEST_TOKEN_PATH_INFO)
  public void setProcessRequestTokenPathInfo(final String processRequestTokenPathInfo) {
    this.processRequestTokenPathInfo = processRequestTokenPathInfo;
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_RESOURCE_ID_RESOLVER,
      defaultValue = "")
  public void setResourceIdResolver(final ResourceIdResolver resourceIdResolver) {
    this.resourceIdResolver = resourceIdResolver;
  }

}
