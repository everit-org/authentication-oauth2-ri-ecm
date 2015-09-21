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
package org.everit.authentication.oauth2.ri.ecm;

/**
 * Constants of the OAuth2 component.
 */
public final class OAuth2Constants {

  public static final String ATTR_AUTHORITZATION_ENDPOINT = "oauth2.authorization.endpoint";

  public static final String ATTR_CLIENT_ID = "oauth2.client.id";

  public static final String ATTR_CLIENT_SECRET = "oauth2.client.secret";

  public static final String ATTR_PROPERTY_MANAGER = "propertyManager.target";

  public static final String ATTR_PROVIDER_NAME = "oauth2.provider.name";

  public static final String ATTR_QUERYDSL_SUPPORT = "querydslSupport.target";

  public static final String ATTR_RESOURCE_SERVICE = "resourceService.target";

  public static final String ATTR_SCOPE = "oauth2.scope";

  public static final String ATTR_TOKEN_ENDPOINT = "oauth2.token.endpoint";

  public static final String ATTR_TRANSACTION_PROPAGATOR = "transactionPropagator.target";

  public static final String ATTR_USER_INFORMATION_REQUEST_URI =
      "oauth2.user.information.request.uri";

  public static final String DEFAULT_SERVICE_DESCRIPTION = "Default OAuth2 Component";

  public static final String SERVICE_FACTORYPID_OAUTH2 =
      "org.everit.authentication.oauth2.ri.ecm.OAuth2";

  private OAuth2Constants() {
  }

}
