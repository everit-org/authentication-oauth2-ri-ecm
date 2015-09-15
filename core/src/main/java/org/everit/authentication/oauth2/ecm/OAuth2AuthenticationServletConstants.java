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

  public static final String DEFAULT_FAILED_URL = "/failed";

  public static final String DEFAULT_PROCESS_REQUEST_TOKEN_PATH_INFO = "/processRequestToken";

  public static final String DEFAULT_SERVICE_DESCRIPTION = "Default OAuth2 Authentication Servlet";

  public static final String DEFAULT_SUCCESS_URL = "/welcome";

  public static final String PROP_FAILED_URL =
      "oauth2.authentication.failed.url";

  public static final String PROP_PROCESS_REQUEST_TOKEN_PATH_INFO =
      "oauth2.process.request.token.path.info";

  public static final String PROP_PROVIDER_NAME =
      "oauth2.provider.name";

  public static final String PROP_SUCCESS_URL =
      "oauth2.authentication.success.url";

  public static final String SERVICE_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES =
      "authenticationSessionAttributeNames.target";

  public static final String SERVICE_FACTORYPID_OAUTH2_AUTHENTICATION_SERVLET =
      "org.everit.authentication.oauth2.ecm.OAuth2AuthenticationServlet";

  public static final String SERVICE_OAUTH2_COMMUNICATOR =
      "oauth2Communicator.target";

  public static final String SERVICE_OAUTH2_SESSION_ATTRIBUTE_NAMES =
      "oauth2SessionAttributeNames.target";

  public static final String SERVICE_RESOURCE_ID_RESOLVER =
      "resourceIdResolver.target";

  private OAuth2AuthenticationServletConstants() {
  }
}
