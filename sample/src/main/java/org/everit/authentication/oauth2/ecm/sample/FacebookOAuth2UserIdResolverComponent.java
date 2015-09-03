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
package org.everit.authentication.oauth2.ecm.sample;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.everit.authentication.oauth2.OAuth2UserIdResolver;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * The Facebook specific {@link OAuth2UserIdResolver} implementation.
 */
@Component
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@Service
public class FacebookOAuth2UserIdResolverComponent implements OAuth2UserIdResolver {

  private String requestURI;

  @Override
  public String getUniqueUserId(final String tokenType, final String accessToken,
      final Long accessTokenExpiresIn, final String refreshToken, final String scope) {
    OAuthResourceResponse resourceResponse;
    try {
      OAuthClientRequest resourceRequest =
          new OAuthBearerClientRequest(requestURI)
              .setAccessToken(accessToken)
              .buildHeaderMessage();

      OAuthClient client = new OAuthClient(new URLConnectionClient());
      resourceResponse = client.resource(resourceRequest, "GET", OAuthResourceResponse.class);
    } catch (OAuthSystemException | OAuthProblemException e) {
      throw new RuntimeException(e);
    }
    JsonObject fromJson = new Gson().fromJson(resourceResponse.getBody(), JsonObject.class);
    return fromJson.get("id").toString();
  }

  @StringAttribute(defaultValue = "https://graph.facebook.com/v2.4/me")
  public void setRequestURI(final String requestURI) {
    this.requestURI = requestURI;
  }

}
