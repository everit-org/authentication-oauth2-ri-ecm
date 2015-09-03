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

import org.everit.authentication.oauth2.ri.internal.OAuth2ResourceIdResolverImpl;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.osgi.props.PropertyManager;
import org.everit.osgi.querydsl.support.QuerydslSupport;
import org.everit.osgi.resource.ResourceService;
import org.everit.osgi.resource.resolver.ResourceIdResolver;
import org.everit.osgi.transaction.helper.api.TransactionHelper;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * OAuth2 ResourceIdResolver component.
 */
@Component(configurationPolicy = ConfigurationPolicy.REQUIRE)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@Service
public class OAuth2ResourceIdResolverComponent implements ResourceIdResolver {

  private PropertyManager propertyManager;

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
        resourceService, transactionHelper);
  }

  @Deactivate
  public void deactivate() {
    resourceIdResolver = null;
  }

  @Override
  public Optional<Long> getResourceId(final String uniqueIdentifier) {
    return resourceIdResolver.getResourceId(uniqueIdentifier);
  }

  @ServiceRef(defaultValue = "")
  public void setPropertyManager(final PropertyManager propertyManager) {
    this.propertyManager = propertyManager;
  }

  @ServiceRef(defaultValue = "")
  public void setQuerydslSupport(final QuerydslSupport querydslSupport) {
    this.querydslSupport = querydslSupport;
  }

  @ServiceRef(defaultValue = "")
  public void setResourceService(final ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @ServiceRef(defaultValue = "")
  public void setTransactionHelper(final TransactionHelper transactionHelper) {
    this.transactionHelper = transactionHelper;
  }

}
