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

import org.everit.authentication.oauth2.OAuth2Configuration;
import org.everit.authentication.oauth2.ecm.OAuth2ConfigurationConstants;
import org.everit.authentication.oauth2.ri.internal.OAuth2ConfigurationImpl;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * OAuth2 configuration component.
 */
@Component(componentId = OAuth2ConfigurationConstants.SERVICE_FACTORYPID_OAUTH2_CONFIGURATION,
    configurationPolicy = ConfigurationPolicy.FACTORY)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@Service
public class OAuth2ConfigurationComponent implements OAuth2Configuration {

  private String authorizationEndpoint;

  private String clientId;

  private String clientSecret;

  private OAuth2Configuration oauth2Configuration;

  private String providerName;

  private String redirectEndpoint;

  private String scope;

  private String tokenEndpoint;

  /**
   * Component activator method.
   */
  @Activate
  public void activate() {
    oauth2Configuration = new OAuth2ConfigurationImpl(authorizationEndpoint, clientId, clientSecret,
        providerName, redirectEndpoint, scope, tokenEndpoint);
  }

  @Override
  public String authorizationEndpoint() {
    return oauth2Configuration.authorizationEndpoint();
  }

  @Override
  public String clientId() {
    return oauth2Configuration.clientId();
  }

  @Override
  public String clientSecret() {
    return oauth2Configuration.clientSecret();
  }

  public void deactivate() {
    oauth2Configuration = null;
  }

  @Override
  public String providerName() {
    return oauth2Configuration.providerName();
  }

  @Override
  public String redirectEndpoint() {
    return oauth2Configuration.redirectEndpoint();
  }

  @Override
  public String scope() {
    return oauth2Configuration.scope();
  }

  @StringAttribute(attributeId = OAuth2ConfigurationConstants.PROP_AUTHORITZATION_ENDPOINT,
      defaultValue = OAuth2ConfigurationConstants.DEFAULT_AUTHORIZATION_ENDPOINT)
  public void setAuthorizationEndpoint(final String authorizationEndpoint) {
    this.authorizationEndpoint = authorizationEndpoint;
  }

  @StringAttribute(attributeId = OAuth2ConfigurationConstants.PROP_CLIENT_ID,
      defaultValue = OAuth2ConfigurationConstants.DEFAULT_CLIENT_ID)
  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  @StringAttribute(attributeId = OAuth2ConfigurationConstants.PROP_CLIENT_SECRET,
      defaultValue = OAuth2ConfigurationConstants.DEFAULT_CLIENT_SECRET)
  public void setClientSecret(final String clientSecret) {
    this.clientSecret = clientSecret;
  }

  @StringAttribute(attributeId = OAuth2ConfigurationConstants.PROP_PROVIDER_NAME,
      defaultValue = OAuth2ConfigurationConstants.DEFAULT_PROVIDER_NAME)
  public void setProviderName(final String providerName) {
    this.providerName = providerName;
  }

  @StringAttribute(attributeId = OAuth2ConfigurationConstants.PROP_REDIRECT_ENDPOINT,
      defaultValue = OAuth2ConfigurationConstants.DEFAULT_REDIRECT_ENDPOINT)
  public void setRedirectEndpoint(final String redirectEndpoint) {
    this.redirectEndpoint = redirectEndpoint;
  }

  @StringAttribute(attributeId = OAuth2ConfigurationConstants.PROP_SCOPE,
      defaultValue = OAuth2ConfigurationConstants.DEFAULT_SCOPE)
  public void setScope(final String scope) {
    this.scope = scope;
  }

  @StringAttribute(attributeId = OAuth2ConfigurationConstants.PROP_TOKEN_ENDPOINT,
      defaultValue = OAuth2ConfigurationConstants.DEFAULT_TOKEN_ENDPOINT)
  public void setTokenEndpoint(final String tokenEndpoint) {
    this.tokenEndpoint = tokenEndpoint;
  }

  @Override
  public String tokenEndpoint() {
    return oauth2Configuration.tokenEndpoint();
  }

}
