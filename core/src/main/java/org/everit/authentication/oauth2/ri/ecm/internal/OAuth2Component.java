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

import org.everit.authentication.oauth2.OAuth2Communicator;
import org.everit.authentication.oauth2.ri.core.OAuth2OltuCommunicatorImpl;
import org.everit.authentication.oauth2.ri.core.OAuth2ResourceIdResolverImpl;
import org.everit.authentication.oauth2.ri.ecm.OAuth2Constants;
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
import org.everit.persistence.querydsl.support.QuerydslSupport;
import org.everit.props.PropertyManager;
import org.everit.resource.ResourceService;
import org.everit.resource.resolver.ResourceIdResolver;
import org.everit.transaction.propagator.TransactionPropagator;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * ECM component for {@link OAuth2ResourceIdResolverImpl} and {@link OAuth2OltuCommunicatorImpl}.
 */
@ExtendComponent
@Component(
    componentId = OAuth2Constants.SERVICE_FACTORYPID_OAUTH2,
    configurationPolicy = ConfigurationPolicy.FACTORY,
    label = "Everit OAuth2 Component",
    description = "Responsible for OAuth2 communication and resource id handling. Registers an "
        + "org.everit.authentication.oauth2.OAuth2Communicator and an "
        + "org.everit.osgi.resource.resolver.ResourceIdResolver OSGi Services.")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2Constants.DEFAULT_SERVICE_DESCRIPTION,
        priority = OAuth2Component.P01_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualServices({ @ManualService(OAuth2Communicator.class),
    @ManualService(ResourceIdResolver.class) })
public class OAuth2Component {

  public static final int P01_SERVICE_DESCRIPTION = 1;

  public static final int P02_PROVIDER_NAME = 2;

  public static final int P03_CLIENT_ID = 3;

  public static final int P04_CLIENT_SECRET = 4;

  public static final int P05_TOKEN_ENDPOINT = 5;

  public static final int P06_AUTHORITZATION_ENDPOINT = 6;

  public static final int P07_SCOPE = 7;

  public static final int P08_USER_INFORMATION_REQUEST_URL = 8;

  public static final int P09_RESOURCE_SERVICE = 9;

  public static final int P10_PROPERTY_MANAGER = 10;

  public static final int P11_QUERYDSL_SUPPORT = 11;

  public static final int P12_TRANSACTION_PROPAGATOR = 12;

  private String authorizationEndpoint;

  private String clientId;

  private String clientSecret;

  private ServiceRegistration<OAuth2Communicator> oAuth2CommunicatorSR;

  private PropertyManager propertyManager;

  private String providerName;

  private QuerydslSupport querydslSupport;

  private ServiceRegistration<ResourceIdResolver> resourceIdResolverSR;

  private ResourceService resourceService;

  private String scope;

  private String tokenEndpoint;

  private TransactionPropagator transactionPropagator;

  private String userInformationRequestURL;

  /**
   * Component activator method that instantiates the wrapped {@link ResourceIdResolver} and
   * {@link OAuth2Communicator} and registers them as OSGi services.
   */
  @Activate
  public void activate(final ComponentContext<OAuth2Component> componentContext) {
    Dictionary<String, Object> properties =
        new Hashtable<>(componentContext.getProperties());

    ResourceIdResolver resourceIdResolver = new OAuth2ResourceIdResolverImpl(
        providerName, propertyManager, resourceService, transactionPropagator, querydslSupport);

    OAuth2Communicator oAuth2Communicator =
        new OAuth2OltuCommunicatorImpl(providerName, clientId, clientSecret, authorizationEndpoint,
            tokenEndpoint, scope, userInformationRequestURL);

    resourceIdResolverSR = componentContext.registerService(
        ResourceIdResolver.class, resourceIdResolver, properties);
    oAuth2CommunicatorSR = componentContext.registerService(
        OAuth2Communicator.class, oAuth2Communicator, properties);
  }

  /**
   * Component deactivate method that unregisters the OSGi services.
   */
  @Deactivate
  public void deactivate() {
    if (resourceIdResolverSR != null) {
      resourceIdResolverSR.unregister();
    }
    if (oAuth2CommunicatorSR != null) {
      oAuth2CommunicatorSR.unregister();
    }
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_AUTHORITZATION_ENDPOINT,
      priority = P06_AUTHORITZATION_ENDPOINT,
      label = "Authorization Endpoint",
      description = "The authorization endpoint of OAuth2 server.")
  public void setAuthorizationEndpoint(final String authorizationEndpoint) {
    this.authorizationEndpoint = authorizationEndpoint;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_CLIENT_ID,
      priority = P03_CLIENT_ID,
      label = "Client Id",
      description = "The client Id of the registered client application in OAuth2 server.")
  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_CLIENT_SECRET,
      priority = P04_CLIENT_SECRET,
      label = "Client Secret",
      description = "The client secret of the registered client application in OAuth2 server.")
  public void setClientSecret(final String clientSecret) {
    this.clientSecret = clientSecret;
  }

  @ServiceRef(attributeId = OAuth2Constants.ATTR_PROPERTY_MANAGER,
      defaultValue = "",
      attributePriority = P10_PROPERTY_MANAGER,
      label = "Property Manager",
      description = "OSGi Service filter expression for org.everit.osgi.props.PropertyManager.")
  public void setPropertyManager(final PropertyManager propertyManager) {
    this.propertyManager = propertyManager;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_PROVIDER_NAME,
      priority = P02_PROVIDER_NAME,
      label = "Provider Name",
      description = "The OAuth2 provider name. This value: "
          + "(1) is saved to the database when a user is authenticated; "
          + "(2) is stored in the session to be able to identify the current provider; "
          + "(3) can be used to filter and wire the OSGi services belonging to the same provider, "
          + "for e.g.: (oauth2.provider.name=google).")
  public void setProviderName(final String providerName) {
    this.providerName = providerName;
  }

  @ServiceRef(attributeId = OAuth2Constants.ATTR_QUERYDSL_SUPPORT,
      defaultValue = "",
      attributePriority = P11_QUERYDSL_SUPPORT,
      label = "Querydsl Support",
      description = "OSGi Service filter expression for "
          + "org.everit.osgi.querydsl.support.QuerydslSupport.")
  public void setQuerydslSupport(final QuerydslSupport querydslSupport) {
    this.querydslSupport = querydslSupport;
  }

  @ServiceRef(attributeId = OAuth2Constants.ATTR_RESOURCE_SERVICE,
      defaultValue = "",
      attributePriority = P09_RESOURCE_SERVICE,
      label = "Resource Service",
      description = "OSGi Service filter expression for org.everit.osgi.resource.ResourceService.")
  public void setResourceService(final ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_SCOPE,
      priority = P07_SCOPE,
      label = "Scope",
      description = "The value of the scope parameter is expressed as a list of space-delimited, "
          + "case-sensitive strings. The strings are defined by the OAuth2 server.")
  public void setScope(final String scope) {
    this.scope = scope;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_TOKEN_ENDPOINT,
      priority = P05_TOKEN_ENDPOINT,
      label = "Token Endpoint",
      description = "The token endpoint of OAuth2 server.")
  public void setTokenEndpoint(final String tokenEndpoint) {
    this.tokenEndpoint = tokenEndpoint;
  }

  @ServiceRef(attributeId = OAuth2Constants.ATTR_TRANSACTION_PROPAGATOR,
      defaultValue = "",
      attributePriority = P12_TRANSACTION_PROPAGATOR,
      label = "Transaction Propagator",
      description = "OSGi Service filter expression for "
          + "org.everit.transaction.propagator.TransactionPropagator.")
  public void setTransactionPropagator(final TransactionPropagator transactionPropagator) {
    this.transactionPropagator = transactionPropagator;
  }

  @StringAttribute(attributeId = OAuth2Constants.ATTR_USER_INFORMATION_REQUEST_URL,
      priority = P08_USER_INFORMATION_REQUEST_URL,
      label = "User Information Request URL",
      description = "The URL of the OAuth2 server that provides information from the user. This "
          + "URL is used to query the user ID on the OAuth2 Server side.")
  public void setUserInformationRequestURL(final String userInformationRequestURL) {
    this.userInformationRequestURL = userInformationRequestURL;
  }

}
