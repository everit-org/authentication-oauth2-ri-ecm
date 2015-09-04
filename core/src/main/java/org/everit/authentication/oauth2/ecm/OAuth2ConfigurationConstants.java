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
 * Constants of the OAuth2 Configuration component.
 */
public final class OAuth2ConfigurationConstants {

  public static final String DEFAULT_AUTHORIZATION_ENDPOINT =
      "https://accounts.google.com/o/oauth2/auth";

  public static final String DEFAULT_CLIENT_ID = "Copy from OAuth2 server.";

  public static final String DEFAULT_CLIENT_SECRET = "Copy form OAuth2 server.";

  public static final String DEFAULT_PROVIDER_NAME = "google";

  public static final String DEFAULT_REDIRECT_ENDPOINT =
      "Type redirect endpoint which registered in OAuth2 server";

  public static final String DEFAULT_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";

  public static final String DEFAULT_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token";

  public static final String PROP_AUTHORITZATION_ENDPOINT = "authorization.endpoint";

  public static final String PROP_CLIENT_ID = "client.id";

  public static final String PROP_CLIENT_SECRET = "client.secret";

  public static final String PROP_PROVIDER_NAME = "provider.name";

  public static final String PROP_REDIRECT_ENDPOINT = "redirect.endpoint";

  public static final String PROP_SCOPE = "scope";

  public static final String PROP_TOKEN_ENDPOINT = "token.endpoint";

  public static final String SERVICE_FACTORYPID_OAUTH2_CONFIGURATION =
      "org.everit.authentication.oauth2.ecm.internal.OAuth2Configuration";

  private OAuth2ConfigurationConstants() {
  }

}
