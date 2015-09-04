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
package org.everit.authentication.oauth2.ecm.sample.internal;

import java.util.function.Supplier;

import org.everit.osgi.authentication.context.AuthenticationPropagator;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Dummy implementation of {@link AuthenticationPropagator} to server configuration.
 */
@Component(configurationPolicy = ConfigurationPolicy.IGNORE)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@Service
public class Dummies implements AuthenticationPropagator {

  // TODO tests jetty bekonfigurálva féliaz oauth leírva hogy kell konfigurálni.
  // publikus login oldal, google,fb login, sikeres login. Teljes nev megjelentés irjuk ki a db-be
  // mentet rekordokat azon az oldalon még.
  // TODO loguot (token invalnidálás oauth serveren) + redirect a sessionauthenticationcomponentre
  // (logouturl-jére)
  // sessionauthenticationcompoenent a servletcontextfactorybe konfirurálni + filter.

  @Override
  public <T> T runAs(final long authenticatedResourceId, final Supplier<T> authenticatedAction) {
    return null;
  }

}
