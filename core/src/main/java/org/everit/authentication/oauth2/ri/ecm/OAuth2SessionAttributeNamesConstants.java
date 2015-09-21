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
 * Constants of the OAuth2 Session Attribute Names component.
 */
public final class OAuth2SessionAttributeNamesConstants {

  public static final String ATTR_SESSION_ATTR_NAME_ACCESS_TOKEN = "oauth2.access.token";

  public static final String ATTR_SESSION_ATTR_NAME_ACCESS_TOKEN_TYPE = "oauth2.access.token.type";

  public static final String ATTR_SESSION_ATTR_NAME_EXPIRES_IN = "oauth2.expires.in";

  public static final String ATTR_SESSION_ATTR_NAME_PROVIDER_NAME = "oauth2.provider.name";

  public static final String ATTR_SESSION_ATTR_NAME_REFRESH_TOKEN = "oauth2.refresh.token";

  public static final String ATTR_SESSION_ATTR_NAME_SCOPE = "oauth2.scope";

  public static final String DEFAULT_SERVICE_DESCRIPTION =
      "Default OAuth2 Session Attribute Names";

  public static final String SERVICE_FACTORYPID_OAUTH2_SESSION_ATTRIBUTE_NAMES =
      "org.everit.authentication.oauth2.ri.ecm.OAuth2SessionAttributeNames";

  private OAuth2SessionAttributeNamesConstants() {
  }

}
