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
package org.everit.authentication.oauth2.ecm;

/**
 * Constants of the OAuth2 Resource Id Resolver component.
 */
public final class OAuth2ResourceIdResolverConstants {

  public static final String DEFAULT_PROVIDER_NAME =
      "Write provider name. Example: google or facebok";

  public static final String DEFAULT_PROVIDER_NAME_TARGET = "(MUST_BE_SET=TO_SOMETHING)";

  public static final String DEFAULT_SERVICE_DESCRIPTION = "Default OAuth2 Resource Id Resolver";

  public static final String PROP_PROPERTY_MANAGER = "propertyManager.target";

  public static final String PROP_PROVIDER_NAME = "provider.name";

  public static final String PROP_PROVIDER_NAME_TARGET = "provider.name.target";

  public static final String PROP_QUERYDSL_SUPPORT = "querydslSupport.target";

  public static final String PROP_RESOURCE_SERVICE = "resourceService.target";

  public static final String PROP_TRANSACTION_HELPER = "transactionHelper.target";

  public static final String SERVICE_FACTORYPID_OAUTH2_RESOURCE_ID_RESOLVER =
      "org.everit.authentication.oauth2.ecm.internal.OAuth2ResourceIdResolver";

  private OAuth2ResourceIdResolverConstants() {
  }

}
