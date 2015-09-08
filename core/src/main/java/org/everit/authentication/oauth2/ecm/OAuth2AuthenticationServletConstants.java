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
 * Constants of the OAuth2 Authentication Servlet component.
 */
public final class OAuth2AuthenticationServletConstants {

  public static final String DEFAULT_FAILED_URL = "/failed.html";

  public static final String DEFAULT_LOGIN_ENDPOINT_PATH = "/oauth2-auth";

  public static final String DEFAULT_PROVIDER_NAME =
      "Write provider name. Example: google or facebok";

  public static final String DEFAULT_REDIRECT_ENDPOINT_PATH = "/oauth2-redirect";

  public static final String DEFAULT_SERVICE_DESCRIPTION = "Default OAuth2 Authentication Servlet";

  public static final String DEFAULT_SUCCESS_URL = "/logged-in.html";

  public static final String PROP_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES =
      "authenticationSessionAttributeNames.target";

  public static final String PROP_FAILED_URL = "failed.url";

  public static final String PROP_LOG_SERVICE = "logService.target";

  public static final String PROP_LOGIN_ENDPOINT_PATH = "login.endpoint.path";

  public static final String PROP_OAUTH2_CONFIGURATION = "oauth2Configuration.target";

  public static final String PROP_OAUTH2_USER_ID_RESOLVER = "oauth2UserIdResolver.target";

  public static final String PROP_PROVIDER_NAME = "provider.name";

  public static final String PROP_REDIRECT_ENDPOINT_PATH = "redirect.endpoint.path";

  public static final String PROP_RESOURCE_ID_RESOLVER = "resourceIdResolver.target";

  public static final String PROP_SUCCESS_URL = "success.url";

  public static final String SERVICE_FACTORYPID_OAUTH2_AUTHENTICATION_SERVLET =
      "org.everit.authentication.oauth2.ecm.internal.OAuth2AuthenticationServlet";

  private OAuth2AuthenticationServletConstants() {
  }
}
