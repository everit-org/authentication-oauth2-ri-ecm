# authentication-oauth2-ecm
**DRAFT**

[ECM][1] based OSGi components for [authentication-oauth2-ri][2].

# Modules
* core: ECM based components.
* sample: sample with ECM components.

# Usage
It's simple to use components as you can see in sample application. You can see how to configure the components and how work solution.

## Build 

Build sample module.

```
mvn clean bundle:install
```

Navigate to bin folder in sample module.
```
cd sample/target/sample/target/eosgi-dist/equinoxtest/bin
``` 

Run server.
```
./runConsole.sh or runConsole.bat
```

## Configurable Components

Configurable components in authentication-oauth2-ecm project. You can configurable components in https://localhost:4848/system/console/configMgr.
We already configurate components, but you must be finish. Only type client ID and client secret the OAuth2Component after you register a client in [Google][3] and/or [Facebook][4].
When you register client in Google must be add https://localhost:8443/oauth2-redirect?providerName=google redirect URL. In Facebook the redirect URL is https://localhost:8443/oauth2-redirect?providerName=facebook.

### DefaultOAuth2UserIdResolverComponent

That's component provide OAuth2UserIdResolver that obtain userID from OAuth2 server.

#### Settings
* **Provider Name**: the OAuth2 provider name. Example: google.
* **userInformationRequestURI !!name!!**: the request URI from we obtain the unique user ID. Example: https://www.googleapis.com/userinfo/v2/me.

### OAuth2Component

That's component responsible to help configuration. Provides ResourceIdResolver, OAuth2Communicator and OAuth2UserIdResolver (wrapped the DefaultOAuth2UserIdResolverComponent).
For the reason provides many interface it's has many configuration settings.


#### Settings
* **Provider Name**: the OAuth2 provider name.
* **Client Id**: The client ID of the registered client (application) in OAuth2 server.
* **Client Secret**: The client secret of the registered client (application) in OAuth2 server.
* **Redirect Endpoint**: The redirect endpoint which registered in OAuth2 server.
* **Authorization Endpoint**: The authorization endpoint of OAuth2 server.
* **Token Endpoint**: The token endpoint of OAuth2 server. 
* **Scope**: the scope of the access request. OAuth2 server specific.
* **oauth2UserIdResolverWrapped !!name!!**:
* **Resource Service OSGi filter**:
* **Querydsl Support OSGi filter**:
* **Transaction Helper OSGi filter**:

### OAuth2AuthenticationServletComponent

That's component provide OAuth2AuthenticationServlet that manage OAuth2 authentication. It can be used two ways:
* only create one instance (recommended),
* create many instance (separate OAuth2 servers).

**In sample we create only one instance.**

#### Settings
* **Provider Name**: the OAuth2 provider name. Example: all (if has create one instance).
* **Success URL**: The URL where the user will be redirected by default in case of a successful authentication.
* **Failed URL**: The URL where the user will be redirected by default in case of a failed authentication. 
* **Login Endpoint Path**: The servlet path where the user start authentication process.
* **Redirect Endpoint Path**: The servlet path where the OAuth2 server redirect. 
* **oAuth2Services.clause.name**: List of OAuth2Services (OAuth2Components).

If add only one oAuth2Services.clause.name not use OAuth2AuthenticationServlet _providerName_ parameter, but if add more than one oAuth2Services.clause.name use and requires _providerName_ parameter.

## Test

If configurate components to use application. Only to be open https://localhost:8443/index page.

[1]: https://everitorg.wordpress.com/2015/03/24/everit-component-model-1-0-0-release/
[2]: https://github.com/everit-org/authentication-oauth2-ri
[3]: https://developers.google.com/identity/sign-in/web/devconsole-project
[4]: https://developers.facebook.com/docs/apps/register