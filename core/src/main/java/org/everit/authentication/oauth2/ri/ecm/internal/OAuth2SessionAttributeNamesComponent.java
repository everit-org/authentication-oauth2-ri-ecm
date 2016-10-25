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

import org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames;
import org.everit.authentication.oauth2.ri.core.OAuth2SessionAttributeNameDTO;
import org.everit.authentication.oauth2.ri.ecm.OAuth2SessionAttributeNamesConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * ECM component for {@link OAuth2SessionAttributeNames} interface.
 */
@ExtendComponent
@Component(
    componentId = OAuth2SessionAttributeNamesConstants.SERVICE_FACTORYPID_OAUTH2_SESSION_ATTRIBUTE_NAMES, // CS_DISABLE_LINE_LENGTH
    configurationPolicy = ConfigurationPolicy.FACTORY,
    label = "Everit OAuth2 Session Attribute Names Component",
    description = "Session attribute names stored in and read from the "
        + "javax.servlet.http.HttpSession during the authentication process. "
        + "These attribute names can be used to access the stored information "
        + "(access token, etc.) to be able to communicate with the OAuth2 server. "
        + "Registers an org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames "
        + "OSGi Service.")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2SessionAttributeNamesConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = OAuth2SessionAttributeNamesComponent.P01_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualServices(@ManualService(OAuth2SessionAttributeNames.class))
public class OAuth2SessionAttributeNamesComponent {

  public static final int P01_SERVICE_DESCRIPTION = 1;

  public static final int P02_SESSION_ATTR_NAME_PROVIDER_NAME = 2;

  public static final int P03_SESSION_ATTR_NAME_ACCESS_TOKEN = 3;

  public static final int P04_SESSION_ATTR_NAME_ACCESS_TOKEN_TYPE = 4;

  public static final int P05_SESSION_ATTR_NAME_SCOPE = 5;

  public static final int P06_SESSION_ATTR_NAME_EXPIRES_IN = 6;

  public static final int P07_SESSION_ATTR_NAME_REFRESH_TOKEN = 7;

  private ServiceRegistration<OAuth2SessionAttributeNames> serviceRegistration;

  private String sessionAttrNameAccessToken;

  private String sessionAttrNameAccessTokenType;

  private String sessionAttrNameExpiresIn;

  private String sessionAttrNameProviderName;

  private String sessionAttrNameRefreshToken;

  private String sessionAttrNameScope;

  /**
   * Component activator method that instantiates the wrapped {@link OAuth2SessionAttributeNames}
   * and registers it as an OSGi service.
   */
  @Activate
  public void activate(
      final ComponentContext<OAuth2SessionAttributeNamesComponent> componentContext) {
    Dictionary<String, Object> properties =
        new Hashtable<>(componentContext.getProperties());

    OAuth2SessionAttributeNames oAuth2SessionAttributeNames =
        new OAuth2SessionAttributeNameDTO(sessionAttrNameProviderName, sessionAttrNameAccessToken,
            sessionAttrNameAccessTokenType, sessionAttrNameExpiresIn, sessionAttrNameRefreshToken,
            sessionAttrNameScope);

    serviceRegistration = componentContext.registerService(
        OAuth2SessionAttributeNames.class, oAuth2SessionAttributeNames, properties);
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

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_ACCESS_TOKEN,
      defaultValue = OAuth2SessionAttributeNamesConstants.DEFAULT_SESSION_ATTR_NAME_ACCESS_TOKEN,
      priority = P03_SESSION_ATTR_NAME_ACCESS_TOKEN,
      label = "Access Token",
      description = "The session attribute name of the access token.")
  public void setSessionAttrNameAccessToken(final String sessionAttrNameAccessToken) {
    this.sessionAttrNameAccessToken = sessionAttrNameAccessToken;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_ACCESS_TOKEN_TYPE,
      defaultValue = OAuth2SessionAttributeNamesConstants.DEFAULT_SESSION_ATTR_NAME_ACCESS_TOKEN_TYPE, // CS_DISABLE_LINE_LENGTH
      priority = P04_SESSION_ATTR_NAME_ACCESS_TOKEN_TYPE,
      label = "Access Token Type",
      description = "The session attribute name of the access token type.")
  public void setSessionAttrNameAccessTokenType(final String sessionAttrNameAccessTokenType) {
    this.sessionAttrNameAccessTokenType = sessionAttrNameAccessTokenType;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_EXPIRES_IN,
      defaultValue = OAuth2SessionAttributeNamesConstants.DEFAULT_SESSION_ATTR_NAME_EXPIRES_IN,
      priority = P06_SESSION_ATTR_NAME_EXPIRES_IN,
      label = "Expires In",
      description = "The session attribute name of the access token expires in.")
  public void setSessionAttrNameExpiresIn(final String sessionAttrNameExpiresIn) {
    this.sessionAttrNameExpiresIn = sessionAttrNameExpiresIn;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_REFRESH_TOKEN,
      defaultValue = OAuth2SessionAttributeNamesConstants.DEFAULT_SESSION_ATTR_NAME_REFRESH_TOKEN,
      priority = P07_SESSION_ATTR_NAME_REFRESH_TOKEN,
      label = "Refresh Token",
      description = "The session attribute name of the refresh token.")
  public void setSessionAttrNameRefreshToken(final String sessionAttrNameRefreshToken) {
    this.sessionAttrNameRefreshToken = sessionAttrNameRefreshToken;
  }

  @StringAttribute(attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_SCOPE,
      defaultValue = OAuth2SessionAttributeNamesConstants.DEFAULT_SESSION_ATTR_NAME_SCOPE,
      priority = P05_SESSION_ATTR_NAME_SCOPE,
      label = "Scope",
      description = "The session attribute name of the scope.")
  public void setSessionAttrNameScope(final String sessionAttrNameScope) {
    this.sessionAttrNameScope = sessionAttrNameScope;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_PROVIDER_NAME,
      defaultValue = OAuth2SessionAttributeNamesConstants.DEFAULT_SESSION_ATTR_NAME_PROVIDER_NAME,
      priority = P02_SESSION_ATTR_NAME_PROVIDER_NAME,
      label = "Provider Name",
      description = "The session attribute name of the provider.")
  public void setSessionAttrProviderName(final String sessionAttrProviderName) {
    sessionAttrNameProviderName = sessionAttrProviderName;
  }

}
