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
 * Constants of the Facebook and Google OAuth2 User Id Resolver component.
 */
public final class OAuth2UserIdResolverConstants {

  public static final String DEFAULT_PROVIDER_NAME_FACEBOOK = "facebook";

  public static final String DEFAULT_PROVIDER_NAME_GOOGLE = "google";

  public static final String DEFAULT_SERVICE_DESCRIPTION_FACEBOOK =
      "Default Facebook OAuth2 User Id Resolver";

  public static final String DEFAULT_SERVICE_DESCRIPTION_GOOGLE =
      "Default Google OAuth2 User Id Resolver";

  public static final String PROP_PROVIDER_NAME = "provider.name";

  public static final String PROP_USER_INFORMATION_REQUEST_URI = "user.information.request.uri";

  public static final String SERVICE_FACTORYPID_FACEBOOK_OAUTH2_USER_ID_RESOLVER =
      "org.everit.authentication.oauth2.ecm.internal.FacebookOAuth2UserIdResolver";

  public static final String SERVICE_FACTORYPID_GOOGLE_OAUTH2_USER_ID_RESOLVER =
      "org.everit.authentication.oauth2.ecm.internal.GoogleOAuth2UserIdResolver";

  private OAuth2UserIdResolverConstants() {
  }
}
