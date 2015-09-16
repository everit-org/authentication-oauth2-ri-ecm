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

import org.everit.authentication.oauth2.OAuth2SessionAttributeNames;
import org.everit.authentication.oauth2.ecm.OAuth2SessionAttributeNamesConstants;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.osgi.framework.Constants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * ECM component for {@link OAuth2SessionAttributeNames} interface.
 */
@Component(
    componentId = OAuth2SessionAttributeNamesConstants.SERVICE_FACTORYPID_OAUTH2_SESSION_ATTRIBUTE_NAMES, // CS_DISABLE_LINE_LENGTH
    configurationPolicy = ConfigurationPolicy.FACTORY)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2SessionAttributeNamesConstants.DEFAULT_SERVICE_DESCRIPTION) })
@Service
public class OAuth2SessionAttributeNamesComponent implements OAuth2SessionAttributeNames {

  private String sessionAttrNameAccessToken;

  private String sessionAttrNameAccessTokenType;

  private String sessionAttrNameExpiresIn;

  private String sessionAttrNameRefreshToken;

  private String sessionAttrNameScope;

  private String sessionAttrProviderName;

  @Override
  public String accessToken() {
    return sessionAttrNameAccessToken;
  }

  @Override
  public String accessTokenType() {
    return sessionAttrNameAccessTokenType;
  }

  @Override
  public String expiresIn() {
    return sessionAttrNameExpiresIn;
  }

  @Override
  public String providerName() {
    return sessionAttrProviderName;
  }

  @Override
  public String refreshToken() {
    return sessionAttrNameRefreshToken;
  }

  @Override
  public String scope() {
    return sessionAttrNameScope;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_ACCESS_TOKEN,
      defaultValue = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_ACCESS_TOKEN)
  public void setSessionAttrNameAccessToken(final String sessionAttrNameAccessToken) {
    this.sessionAttrNameAccessToken = sessionAttrNameAccessToken;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_ACCESS_TOKEN_TYPE,
      defaultValue = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_ACCESS_TOKEN_TYPE)
  public void setSessionAttrNameAccessTokenType(final String sessionAttrNameAccessTokenType) {
    this.sessionAttrNameAccessTokenType = sessionAttrNameAccessTokenType;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_EXPIRES_IN,
      defaultValue = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_EXPIRES_IN)
  public void setSessionAttrNameExpiresIn(final String sessionAttrNameExpiresIn) {
    this.sessionAttrNameExpiresIn = sessionAttrNameExpiresIn;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_REFRESH_TOKEN,
      defaultValue = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_REFRESH_TOKEN)
  public void setSessionAttrNameRefreshToken(final String sessionAttrNameRefreshToken) {
    this.sessionAttrNameRefreshToken = sessionAttrNameRefreshToken;
  }

  @StringAttribute(attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_SCOPE,
      defaultValue = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_SCOPE)
  public void setSessionAttrNameScope(final String sessionAttrNameScope) {
    this.sessionAttrNameScope = sessionAttrNameScope;
  }

  @StringAttribute(
      attributeId = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_PROVIDER_NAME,
      defaultValue = OAuth2SessionAttributeNamesConstants.ATTR_SESSION_ATTR_NAME_PROVIDER_NAME)
  public void setSessionAttrProviderName(final String sessionAttrProviderName) {
    this.sessionAttrProviderName = sessionAttrProviderName;
  }

}
