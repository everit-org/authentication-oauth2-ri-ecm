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

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.everit.authentication.oauth2.AccessTokenResponse;
import org.everit.authentication.oauth2.OAuth2Communicator;
import org.everit.authentication.oauth2.ecm.OAuth2Constants;
import org.everit.authentication.oauth2.ri.OAuth2OltuCommunicatorImpl;
import org.everit.authentication.oauth2.ri.OAuth2ResourceIdResolverImpl;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.osgi.props.PropertyManager;
import org.everit.osgi.querydsl.support.QuerydslSupport;
import org.everit.osgi.resource.ResourceService;
import org.everit.osgi.resource.resolver.ResourceIdResolver;
import org.everit.transaction.propagator.TransactionPropagator;
import org.osgi.framework.Constants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * ECM component for {@link OAuth2ResourceIdResolverImpl} and {@link OAuth2OltuCommunicatorImpl}.
 */
@Component(
    componentId = OAuth2Constants.SERVICE_FACTORYPID_OAUTH2,
    configurationPolicy = ConfigurationPolicy.FACTORY)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2Constants.DEFAULT_SERVICE_DESCRIPTION) })
@Service
public class OAuth2Component implements OAuth2Communicator, ResourceIdResolver {

  private String authorizationEndpoint;

  private String clientId;

  private String clientSecret;

  private OAuth2Communicator oAuth2Communicator;

  private PropertyManager propertyManager;

  private String providerName;

  private QuerydslSupport querydslSupport;

  private ResourceIdResolver resourceIdResolver;

  private ResourceService resourceService;

  private String scope;

  private String tokenEndpoint;

  private TransactionPropagator transactionPropagator;

  private String userInformationRequestURI;

  /**
   * Component activator method that instantiates the wrapped {@link ResourceIdResolver} and
   * {@link OAuth2Communicator}.
   */
  @Activate
  public void activate() {
    resourceIdResolver = new OAuth2ResourceIdResolverImpl(
        providerName, propertyManager, resourceService, transactionPropagator, querydslSupport);

    oAuth2Communicator =
        new OAuth2OltuCommunicatorImpl(providerName, clientId, clientSecret, authorizationEndpoint,
            tokenEndpoint, scope, userInformationRequestURI);
  }

  @Override
  public String buildAuthorizationURL(final String redirectURL) {
    return oAuth2Communicator.buildAuthorizationURL(redirectURL);
  }

  @Override
  public Optional<AccessTokenResponse> getAccessToken(final HttpServletRequest req,
      final String redirectUri) {
    return oAuth2Communicator.getAccessToken(req, redirectUri);
  }

  @Override
  public String getProviderName() {
    return oAuth2Communicator.getProviderName();
  }

  @Override
  public Optional<Long> getResourceId(final String uniqueIdentifier) {
    return resourceIdResolver.getResourceId(uniqueIdentifier);
  }

  @Override
  public Optional<String> getUniqueUserId(final AccessTokenResponse accessTokenResponse) {
    return oAuth2Communicator.getUniqueUserId(accessTokenResponse);
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_AUTHORITZATION_ENDPOINT)
  public void setAuthorizationEndpoint(final String authorizationEndpoint) {
    this.authorizationEndpoint = authorizationEndpoint;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_CLIENT_ID)
  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_CLIENT_SECRET)
  public void setClientSecret(final String clientSecret) {
    this.clientSecret = clientSecret;
  }

  @ServiceRef(attributeId = OAuth2Constants.ATTR_PROPERTY_MANAGER,
      defaultValue = "")
  public void setPropertyManager(final PropertyManager propertyManager) {
    this.propertyManager = propertyManager;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_PROVIDER_NAME)
  public void setProviderName(final String providerName) {
    this.providerName = providerName;
  }

  @ServiceRef(attributeId = OAuth2Constants.ATTR_QUERYDSL_SUPPORT,
      defaultValue = "")
  public void setQuerydslSupport(final QuerydslSupport querydslSupport) {
    this.querydslSupport = querydslSupport;
  }

  @ServiceRef(attributeId = OAuth2Constants.ATTR_RESOURCE_SERVICE,
      defaultValue = "")
  public void setResourceService(final ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_SCOPE)
  public void setScope(final String scope) {
    this.scope = scope;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_TOKEN_ENDPOINT)
  public void setTokenEndpoint(final String tokenEndpoint) {
    this.tokenEndpoint = tokenEndpoint;
  }

  @ServiceRef(attributeId = OAuth2Constants.ATTR_TRANSACTION_PROPAGATOR,
      defaultValue = "")
  public void setTransactionPropagator(final TransactionPropagator transactionPropagator) {
    this.transactionPropagator = transactionPropagator;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_USER_INFORMATION_REQUEST_URI)
  public void setUserInformationRequestURI(final String userInformationRequestURI) {
    this.userInformationRequestURI = userInformationRequestURI;
  }

}
