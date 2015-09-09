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

import org.everit.authentication.oauth2.OAuth2Configuration;
import org.everit.authentication.oauth2.ecm.OAuth2ConfigurationConstants;
import org.everit.authentication.oauth2.ecm.OAuth2ResourceIdResolverConstants;
import org.everit.authentication.oauth2.ri.internal.OAuth2ResourceIdResolverImpl;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ServiceHolder;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.osgi.props.PropertyManager;
import org.everit.osgi.querydsl.support.QuerydslSupport;
import org.everit.osgi.resource.ResourceService;
import org.everit.osgi.resource.resolver.ResourceIdResolver;
import org.everit.osgi.transaction.helper.api.TransactionHelper;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * OAuth2 Resource Id Resolver component.
 */
@Component(
    componentId = OAuth2ResourceIdResolverConstants.SERVICE_FACTORYPID_OAUTH2_RESOURCE_ID_RESOLVER,
    configurationPolicy = ConfigurationPolicy.FACTORY)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2ResourceIdResolverConstants.DEFAULT_SERVICE_DESCRIPTION),
    @StringAttribute(attributeId = OAuth2ResourceIdResolverConstants.PROP_PROVIDER_NAME,
        defaultValue = OAuth2ResourceIdResolverConstants.DEFAULT_PROVIDER_NAME)
})
@Service
public class OAuth2ResourceIdResolverComponent implements ResourceIdResolver {

  private PropertyManager propertyManager;

  private String providerName;

  private QuerydslSupport querydslSupport;

  private ResourceIdResolver resourceIdResolver;

  private ResourceService resourceService;

  private TransactionHelper transactionHelper;

  /**
   * Component activator method.
   */
  @Activate
  public void activate() {
    resourceIdResolver = new OAuth2ResourceIdResolverImpl(propertyManager, querydslSupport,
        resourceService, transactionHelper, providerName);
  }

  @Deactivate
  public void deactivate() {
    resourceIdResolver = null;
  }

  @Override
  public Optional<Long> getResourceId(final String uniqueIdentifier) {
    return resourceIdResolver.getResourceId(uniqueIdentifier);
  }

  @ServiceRef(attributeId = OAuth2ResourceIdResolverConstants.PROP_PROPERTY_MANAGER,
      defaultValue = "")
  public void setPropertyManager(final PropertyManager propertyManager) {
    this.propertyManager = propertyManager;
  }

  /**
   * Sets provider name from {@link OAuth2Configuration}.
   */
  @ServiceRef(attributeId = OAuth2ResourceIdResolverConstants.PROP_PROVIDER_NAME_TARGET,
      defaultValue = OAuth2ResourceIdResolverConstants.DEFAULT_PROVIDER_NAME_TARGET)
  public void setProviderName(final ServiceHolder<OAuth2Configuration> oauth2Configuration) {
    ServiceReference<OAuth2Configuration> serviceReference = oauth2Configuration.getReference();
    providerName = (String) serviceReference
        .getProperty(OAuth2ConfigurationConstants.PROP_PROVIDER_NAME);
  }

  @ServiceRef(attributeId = OAuth2ResourceIdResolverConstants.PROP_QUERYDSL_SUPPORT,
      defaultValue = "")
  public void setQuerydslSupport(final QuerydslSupport querydslSupport) {
    this.querydslSupport = querydslSupport;
  }

  @ServiceRef(attributeId = OAuth2ResourceIdResolverConstants.PROP_RESOURCE_SERVICE,
      defaultValue = "")
  public void setResourceService(final ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @ServiceRef(attributeId = OAuth2ResourceIdResolverConstants.PROP_TRANSACTION_HELPER,
      defaultValue = "")
  public void setTransactionHelper(final TransactionHelper transactionHelper) {
    this.transactionHelper = transactionHelper;
  }

}
