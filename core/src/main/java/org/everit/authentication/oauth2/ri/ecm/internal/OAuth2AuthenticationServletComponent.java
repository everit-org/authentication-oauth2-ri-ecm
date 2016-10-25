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

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.everit.authentication.http.session.AuthenticationSessionAttributeNames;
import org.everit.authentication.oauth2.OAuth2Communicator;
import org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames;
import org.everit.authentication.oauth2.ri.core.OAuth2AuthenticationServlet;
import org.everit.authentication.oauth2.ri.ecm.OAuth2AuthenticationServletConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.resource.resolver.ResourceIdResolver;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * ECM component for {@link OAuth2AuthenticationServlet}.
 */
@ExtendComponent
@Component(
    componentId = OAuth2AuthenticationServletConstants.SERVICE_FACTORYPID_OAUTH2_AUTHENTICATION_SERVLET, // CS_DISABLE_LINE_LENGTH
    configurationPolicy = ConfigurationPolicy.FACTORY,
    label = "Everit OAuth2 Authentication Servlet Component",
    description = "Implements OAuth2-based authentication mechanism as a Servlet. "
        + "Registers a javax.servlet.Servlet OSGi Service.")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = OAuth2AuthenticationServletComponent.P01_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component."),
    @StringAttribute(attributeId = OAuth2AuthenticationServletConstants.ATTR_PROVIDER_NAME,
        priority = OAuth2AuthenticationServletComponent.P02_PROVIDER_NAME,
        label = "Provider Name",
        description = "The OAuth2 provider name. This value: "
            + "(1) is saved to the database when a user is authenticated; "
            + "(2) is stored in the session to be able to identify the current provider; "
            + "(3) can be used to filter and wire the OSGi services belonging to the same provider,"
            + " for e.g.: (oauth2.provider.name=google).") })
@ManualServices(@ManualService(Servlet.class))
public class OAuth2AuthenticationServletComponent {

  public static final int P01_SERVICE_DESCRIPTION = 1;

  public static final int P02_PROVIDER_NAME = 2;

  public static final int P03_SUCCESS_URL = 3;

  public static final int P04_FAILED_URL = 4;

  public static final int P05_PROCESS_REQUEST_TOKEN_PATH_INFO = 5;

  public static final int P06_OAUTH2_COMMUNICATOR = 6;

  public static final int P07_RESOURCE_ID_RESOLVER = 7;

  public static final int P08_OAUTH2_SESSION_ATTRIBUTE_NAMES = 8;

  public static final int P09_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES = 9;

  private AuthenticationSessionAttributeNames authenticationSessionAttributeNames;

  private String failedUrl;

  private OAuth2Communicator oauth2Communicator;

  private OAuth2SessionAttributeNames oauth2SessionAttributeNames;

  private String processRequestTokenPathInfo;

  private ResourceIdResolver resourceIdResolver;

  private ServiceRegistration<Servlet> serviceRegistration;

  private String successUrl;

  /**
   * Component activator method that instantiates the wrapped {@link OAuth2AuthenticationServlet}
   * and registers the OSGi service.
   */
  @Activate
  public void activate(
      final ComponentContext<OAuth2AuthenticationServletComponent> componentContext) {
    Servlet oauth2AuthenticationServlet =
        new OAuth2AuthenticationServlet(successUrl, failedUrl, processRequestTokenPathInfo,
            oauth2Communicator,
            resourceIdResolver,
            authenticationSessionAttributeNames,
            oauth2SessionAttributeNames);

    Dictionary<String, Object> properties =
        new Hashtable<>(componentContext.getProperties());

    serviceRegistration = componentContext.registerService(
        Servlet.class, oauth2AuthenticationServlet, properties);
  }

  /**
   * Component deactivate method that unregisters the OSGi service.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES, // CS_DISABLE_LINE_LENGTH
      defaultValue = "",
      attributePriority = P09_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES,
      label = "Authentication Session Attribute Names",
      description = "OSGi Service filter expression for "
          + "org.everit.osgi.authentication.http.session.AuthenticationSessionAttributeNames.")
  public void setAuthenticationSessionAttributeNames(
      final AuthenticationSessionAttributeNames authenticationSessionAttributeNames) {
    this.authenticationSessionAttributeNames = authenticationSessionAttributeNames;
  }

  @StringAttribute(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_FAILED_URL,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_FAILED_URL,
      priority = P04_FAILED_URL,
      label = "Failed URL",
      description = "The URL where the user will be redirected in case of a failed authentication.")
  public void setFailedUrl(final String failedUrl) {
    this.failedUrl = failedUrl;
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_OAUTH2_COMMUNICATOR,
      defaultValue = "",
      attributePriority = P06_OAUTH2_COMMUNICATOR,
      label = "OAuth2 Communicator",
      description = "OSGi Service filter expression for "
          + "org.everit.authentication.oauth2.OAuth2Communicator.")
  public void setOauth2Communicator(final OAuth2Communicator oauth2Communicator) {
    this.oauth2Communicator = oauth2Communicator;
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_OAUTH2_SESSION_ATTRIBUTE_NAMES,
      defaultValue = "",
      attributePriority = P08_OAUTH2_SESSION_ATTRIBUTE_NAMES,
      label = "OAuth2 Session Attribute Names",
      description = "OSGi Service filter expression for "
          + "org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames.")
  public void setOauth2SessionAttributeNames(
      final OAuth2SessionAttributeNames oauth2SessionAttributeNames) {
    this.oauth2SessionAttributeNames = oauth2SessionAttributeNames;
  }

  @StringAttribute(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_PROCESS_REQUEST_TOKEN_PATH_INFO,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_PROCESS_REQUEST_TOKEN_PATH_INFO,
      priority = P05_PROCESS_REQUEST_TOKEN_PATH_INFO,
      label = "Request Token Path Info",
      description = "The path info of this servlet that is used to create the redirect URL for the "
          + "OAuth2 Server. The redirect URL will be constructed from the following information: "
          + "[PROTOCOL]://[HOST]:[PORT]/[SERVLET_URL_PATTERN]/[REQUEST_TOKEN_PATH_INFO].")
  public void setProcessRequestTokenPathInfo(final String processRequestTokenPathInfo) {
    this.processRequestTokenPathInfo = processRequestTokenPathInfo;
  }

  @ServiceRef(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_RESOURCE_ID_RESOLVER,
      defaultValue = "",
      attributePriority = P07_RESOURCE_ID_RESOLVER,
      label = "Resource Id Resolver",
      description = "OSGi Service filter expression for "
          + "org.everit.osgi.resource.resolver.ResourceIdResolver.")
  public void setResourceIdResolver(final ResourceIdResolver resourceIdResolver) {
    this.resourceIdResolver = resourceIdResolver;
  }

  @StringAttribute(
      attributeId = OAuth2AuthenticationServletConstants.ATTR_SUCCESS_URL,
      defaultValue = OAuth2AuthenticationServletConstants.DEFAULT_SUCCESS_URL,
      priority = P03_SUCCESS_URL,
      label = "Success URL",
      description = "The URL where the user will be redirected in case of a successful "
          + "authentication.")
  public void setSuccessUrl(final String successUrl) {
    this.successUrl = successUrl;
  }

}
