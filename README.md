# authentication-oauth2-ri-ecm

The EverIT authentication-oauth2 is a general solution for OAuth 2.0 
based on [EverIT Authentication][3].

Git repositories of the solution to support different implementations and 
easier usage:
* [authentication-oauth2-api][4]: API to be able to implement custom OAuth2 
authentication logic or provider specific solutions.
* [authentication-oauth2-ri][2]: The reference implementation of the API with 
implementation specific API and database schema. This implementation was 
tested with Google and Facebook.
* [authentication-oauth2-ri-ecm][5]: ECM based OSGi components of the 
reference implementation and a sample application.

# Modules

* core: the [ECM][1] based components.
* sample: sample application with ECM components.

# Quick Start Guide

The sample application is a pre-configured OSGi application based on EverIT 
solutions. The sample application must be registered in 
[Google Dev Console][6] and/or [Facebook App Registration][7] to obtain the 
```client id``` and ```client secret```. During the client application 
registration the following redirect URLs must be set in case of:
* Google: ```https://localhost:8443/sign-in-with-google/processRequestToken```
* Facebook: ```https://localhost:8443/sign-in-with-facebook/processRequestToken```

*(These two URLs are constructed based on the configuration of the sample 
application and can be changed.)*

To build and run the sample application the following commands must be 
executed on ```authentication-oauth2-ri-ecm```:

```
cd sample
mvn clean eosgi:dist
cd target/eosgi-dist/oauth2-sample-app/bin
runConsole.bat (or ./runConsole.sh)
```

Some configurtaion must be changed on the OSGi Web Console 
(```https://localhost:4848/system/console/configMgr```) after the successful 
client registrations and the application start. The ```client id``` and 
```client secret``` must be configured in the ```Everit OAuth2 Component``` 
(```org.everit.authentication.oauth2.ri.ecm.OAuth2```) components:
* Replace the ```MY_GOOGLE_CLIENT_ID``` and ```MY_GOOGLE_CLIENT_SECRET``` 
values with the ```client id``` and ```client secret``` obtained from 
Google on the [Google OAuth2 Component configuration][8].
* Replace the ```MY_FACEBOOK_CLIENT_ID``` and ```MY_FACEBOOK_CLIENT_SECRET``` 
values with ```client id``` and ```client secret``` obtained from 
Facebook on the [Facebook OAuth2 Component configuration][9].

After the successful configuration the sample application can be accessed on 
[https://localhost:8443/index][10].

Continue reading for more information about detailed configuration and 
setup.

# Configurable Components

## Everit OAuth2 Component

Responsible for OAuth2 communication and resource id handling. Registers an 
```org.everit.authentication.oauth2.OAuth2Communicator``` and an 
```org.everit.osgi.resource.resolver.ResourceIdResolver``` OSGi Services.

### Configuration

* **Service Description**: The description of this component configuration. 
It is used to easily identify the service registered by this component.
(```service.description```)
* **Provider Name**: The OAuth2 provider name. This value:
  * (1) is saved to the database when a user is authenticated;
  * (2) is stored in the session to be able to identify the current provider;
  * (3) can be used to filter and wire the OSGi services belonging to the same 
provider, for e.g.: (oauth2.provider.name=google). 
(```oauth2.provider.name```).
* **Client Id**: The client Id of the registered client application in 
OAuth2 server. (```oauth2.client.id```)
* **Client Secret**: The client secret of the registered client 
application in OAuth2 server. (```oauth2.client.secret```)
* **Scope**: The value of the scope parameter is expressed as a list of 
space-delimited, case-sensitive strings. The strings are defined by the OAuth2 
server. (```oauth2.scope```)
* **Authorization Endpoint**: The authorization endpoint of OAuth2 server. 
(```oauth2.authorization.endpoint```)
* **Token Endpoint**: The token endpoint of OAuth2 server. 
(```oauth2.token.endpoint```)
* **User Information Request URL**: The URL of the OAuth2 server that provides 
information from the user. This URL is used to query the user ID on the OAuth2 
Server side. (```oauth2.user.information.request.url```)
* **Property Manager**: OSGi Service filter expression for 
```org.everit.osgi.props.PropertyManager```. (```propertyManager.target```)
* **Resource Service**: OSGi Service filter expression for 
```org.everit.osgi.resource.ResourceService```. (```resourceService.target```)
* **Querydsl Support**: OSGi Service filter expression for 
```org.everit.osgi.querydsl.support.QuerydslSupport```. (```querydslSupport.target```)
* **Transaction Propagator**: OSGi Service filter expression for 
```org.everit.transaction.propagator.TransactionPropagator```. (```transactionPropagator.target```)

## Everit OAuth2 Session Attribute Names Component

Session attribute names stored in and read from the 
```javax.servlet.http.HttpSession``` during the authentication process. 
These attribute names can be used to access the stored information 
(access token, etc.) to be able to communicate with the OAuth2 server. 
Registers an 
```org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames``` OSGi 
Service.

### Configuration

* **Service Description**: The description of this component configuration. 
It is used to easily identify the service registered by this component.
(```service.description```)
* **Provider Name**: The session attribute name of the provider. 
(```oauth2.session.attr.name.provider.name```)
* **Access Token**: The session attribute name of the access token. 
(```oauth2.session.attr.name.access.token```)
* **Access Token Type**: The session attribute name of the access token type. 
(```oauth2.session.attr.name.access.token.type```)
* **Expires In**: The session attribute name of the access token expires in. 
(```oauth2.session.attr.name.expires.in```)
* **Refresh Token**: The session attribute name of the refresh token. 
(```oauth2.session.attr.name.refresh.token```)
* **Scope**: The session attribute name of the scope. 
(```oauth2.session.attr.name.scope```)

## Everit OAuth2 Authentication Servlet Component

Implements OAuth2-based authentication mechanism as a Servlet. Registers a 
```javax.servlet.Servlet``` OSGi Service.

### Configuration

* **Service Description**: The description of this component configuration. 
It is used to easily identify the service registered by this component.
(```service.description```)
* **Provider Name**: The OAuth2 provider name. This value:
  * (1) is saved to the database when a user is authenticated;
  * (2) is stored in the session to be able to identify the current provider;
  * (3) can be used to filter and wire the OSGi services belonging to the same 
provider, for e.g.: (oauth2.provider.name=google). 
(```oauth2.provider.name```).
* **Success URL**: The URL where the user will be redirected in case of a 
successful authentication. (```oauth2.authentication.success.url```)
* **Failed URL**: The URL where the user will be redirected in case of a 
failed authentication. (```oauth2.authentication.failed.url```)
* **Request Token Path Info**: The path info of this servlet that is used to 
create the redirect URL for the OAuth2 server. The redirect URL will be 
constructed from the following information: 
```[PROTOCOL]://[HOST]:[PORT]/[SERVLET_URL_PATTERN]/[REQUEST_TOKEN_PATH_INFO]```.
(```oauth2.process.request.token.path.info```)
* **OAuth2 Session Attribute Names**: OSGi Service filter expression for 
```org.everit.authentication.oauth2.ri.OAuth2SessionAttributeNames```. 
(```oauth2SessionAttributeNames.target```)
* **OAuth2 Communicator**: OSGi Service filter expression for 
```org.everit.authentication.oauth2.OAuth2Communicator```. 
(```oauth2Communicator.target```)
* **Authentication Session Attribute Names**: OSGi Service filter expression 
for ```org.everit.osgi.authentication.http.session.AuthenticationSessionAttributeNames```. 
(```authenticationSessionAttributeNames.target```)
* **Resource Id Resolver**: OSGi Service filter expression for 
```org.everit.osgi.resource.resolver.ResourceIdResolver```. 
(```resourceIdResolver.target```)

## Everit Jetty ServletContextHandler Factory

The Servlets and Filters used for OAuth2 authentication can be configured in 
Everit Jetty ServletContextHandler Factory (provided by the 
[jetty-server-component][11]). This configuration explains the sample 
application but it can be applied in any other application easily.

These following Servlet and Filter clauses are used to register the Servlets 
and Filters to the Servlet Context. The clauses are built from three part:
* the identifier;
* the ```url-pattern``` attribute describes where Servlet or Filter listens on;
* the ```filter``` directive is used to determine which 
```javax.servlet.Servlet``` or ```javax.servlet.Filter``` OSGi service should 
be registered to the Servlet Context to handle the request on the specified 
```url-pattern```.

### Servlet Configuration

The following **Servlets (clause)** configurations are used in the sample 
application.

#### Sample application pages

##### index

This configuration serves the content of the ```index.html``` page with the 
```IndexServletComponent```. This is the login page where the 
"Sign in with Google" and "Sign in with Facebook" buttons are.

```
index;url-pattern=/index;filter:=(objectClass=org.everit.authentication.oauth2.ri.ecm.sample.IndexServletComponent)
```

##### welcome

This configuration serves the content of the ```welcome.html``` page with the 
```WelcomeServletComponent```. The user is redirected to this page if the 
authentication is successful. The welcome page (the Servlet) checks if the 
user is authenticated, queries the name of the user from the OAuth2 server 
and renders these information.

```
welcome;url-pattern=/welcome;filter:=(objectClass=org.everit.authentication.oauth2.ri.ecm.sample.WelcomeServletComponent)
```

##### failed

This configuration serves the content of the ```failed.html``` page with the 
```FailedServletComponent```. This is the page where the user is redirected if 
the authentication is failed.

```
failed;url-pattern=/failed;filter:=(objectClass=org.everit.authentication.oauth2.ri.ecm.sample.FailedServletComponent)
```

#### OAuth2 servlets

The ```OAuth2AuthenticationServlet``` is responsible for the communication 
between:
* the user (the browser) and the application;
* the application and the OAuth2 server.

It handles the ```sign-in-with-...``` requests of the user and redirects the 
user to the OAuth2 server. It also acquires the access token when the user 
grants the application.

**!!! Important !!!** The redirect URL used at client registration and sent 
to the OAuth2 server.

The redirect URL is constructed from the configuration of the application:

```
[PROTOCOL]://[HOST]:[PORT]/[SERVLET_URL_PATTERN]/[REQUEST_TOKEN_PATH_INFO]
```

* PROTOCOL: the protocol used by the application (http/https)
* HOST: the host of the application (localhost or a registered domain)
* PORT: the port where the connection received
* SERVLET_URL_PATTERN: the URL where the OAuth2 Servlet is registered 
(see below: /sign-in-with-google or /sign-in-with-facebook)
* REQUEST_TOKEN_PATH_INFO: configured in the OAuth2 Authentication Servlet 
(see above: /processRequestToken - by default)

###### sign-in-with-google

This configuration is responsible for OAuth2 Google Authentication.

```
sign-in-with-google;url-pattern=/sign-in-with-google/*;filter:=(oauth2.provider.name=google)
```

##### sign-in-with-facebook

This configuration is responsible for OAuth2 Facebook Authentication.

```
sign-in-with-facebook;url-pattern=/sign-in-with-facebook/*;filter:=(oauth2.provider.name=facebook)
```

#### Session Authentication Servlet

##### logout

This configuration handles the logout requests, when the user clicks on the 
logout button. It only invalidates the session of the user, it DOES NOT logs 
out the user on the OAuth2 server.

```
logout;url-pattern=/logout;filter:=(service.pid=org.everit.osgi.authentication.http.session.SessionAuthentication.18d78085-28e4-4b8b-990d-4ac424a585d0)
```

### Filter Configuration

#### Session Authentication Filter

This filter is applied on all (/*) requests received by the Jetty Connector. 
It determines if there is an authenticated resource Id (that were added by 
the ```OAuth2AuthenticationServlet``` previously) is assigned to the current 
session and executes the authenticated action 
(```chain.doFilter(request, response);```) in the name of it. Using this 
filter the ```AuthenticationContext.getCurrentResourceId()``` will return 
the resource Id assigned to the logged in user.

```
session-filter;url-pattern=/*;filter:=(service.pid=org.everit.osgi.authentication.http.session.SessionAuthentication.18d78085-28e4-4b8b-990d-4ac424a585d0)
```

# Wiring

The following diagram demonstrates the component configuration where the 
boxes represent the OSGi components and the arrows represent the provided 
OSGi services.

![wiring](https://github.com/everit-org/authentication-oauth2-ri-ecm/raw/master/img/oauth2-component-wiring.png)

# Relation with Everit Authentication

TBD.

[1]: https://everitorg.wordpress.com/2015/03/24/everit-component-model-1-0-0-release/
[2]: https://github.com/everit-org/authentication-oauth2-ri
[3]: https://everitorg.wordpress.com/2014/07/31/everit-authentication
[4]: https://github.com/everit-org/authentication-oauth2-api
[5]: https://github.com/everit-org/authentication-oauth2-ri-ecm
[6]: https://developers.google.com/identity/sign-in/web/devconsole-project
[7]: https://developers.facebook.com/docs/apps/register
[8]: https://localhost:4848/system/console/configMgr/org.everit.authentication.oauth2.ri.ecm.OAuth2.cfb8ede0-f72b-4106-a100-f18087606aff
[9]: https://localhost:4848/system/console/configMgr/org.everit.authentication.oauth2.ri.ecm.OAuth2.c7fb1164-ae00-45dd-af3c-556a6c440f78
[10]: https://localhost:8443/index
[11]: https://github.com/everit-org/jetty-server-component