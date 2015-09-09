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

import org.everit.authentication.oauth2.OAuth2UserIdResolver;
import org.everit.authentication.oauth2.ecm.OAuth2UserIdResolverConstants;
import org.everit.authentication.oauth2.ri.internal.GoogleOAuth2UserIdResolverImpl;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.osgi.framework.Constants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Google OAuth2 User Id Resolver Component.
 */
@Component(
    componentId = OAuth2UserIdResolverConstants.SERVICE_FACTORYPID_GOOGLE_OAUTH2_USER_ID_RESOLVER,
    configurationPolicy = ConfigurationPolicy.REQUIRE)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = OAuth2UserIdResolverConstants.DEFAULT_SERVICE_DESCRIPTION_GOOGLE),
    @StringAttribute(attributeId = OAuth2UserIdResolverConstants.PROP_PROVIDER_NAME,
        defaultValue = OAuth2UserIdResolverConstants.DEFAULT_PROVIDER_NAME_GOOGLE)
})
@Service
public class GoogleOAuth2UserIdResolverComponent implements OAuth2UserIdResolver {

  private OAuth2UserIdResolver oauth2UserIdResolver;

  private String userInformationRequestURI;

  /**
   * Component activator method.
   */
  @Activate
  public void activate() {
    oauth2UserIdResolver = new GoogleOAuth2UserIdResolverImpl(userInformationRequestURI);
  }

  @Override
  public String getUniqueUserId(final String tokenType, final String accessToken,
      final Long accessTokenExpiresIn, final String refreshToken, final String scope) {
    return oauth2UserIdResolver.getUniqueUserId(tokenType, accessToken, accessTokenExpiresIn,
        refreshToken, scope);
  }

  @StringAttribute(attributeId = OAuth2UserIdResolverConstants.PROP_USER_INFORMATION_REQUEST_URI,
      defaultValue = "https://www.googleapis.com/userinfo/v2/me")
  public void setUserInformationRequestURI(final String userInformationRequestURI) {
    this.userInformationRequestURI = userInformationRequestURI;
  }

}
