# authentication-oauth2-ecm

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
```“client id”``` and ```“client secret”```. During the client application 
registration the following redirect URLs must be set in case of:
* Google: [https://localhost:8443/sign-in-with-google/processRequestToken]
* Facebook: [https://localhost:8443/sign-in-with-facebook/processRequestToken]

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
client registrations and the application start. The ```“client id”``` and 
```“client secret”``` must be configured in the ```Everit OAuth2 Component``` 
(```org.everit.authentication.oauth2.ri.ecm.OAuth2```) components:
* Replace the ```“MY_GOOGLE_CLIENT_ID”``` and ```“MY_GOOGLE_CLIENT_SECRET”``` 
values with the ```“client id”``` and ```“client secret”``` obtained from 
Google on the Google OAuth2 Component configuration URL: 
```https://localhost:4848/system/console/configMgr/org.everit.authentication.oauth2.ri.ecm.OAuth2.cfb8ede0-f72b-4106-a100-f18087606aff```
* Replace the ```“MY_FACEBOOK_CLIENT_ID”``` and ```“MY_FACEBOOK_CLIENT_SECRET”``` 
values with ```“client id”``` and ```“client secret”``` obtained from 
Facebook on the Facebook OAuth2 Component configuration URL:
```https://localhost:4848/system/console/configMgr/org.everit.authentication.oauth2.ri.ecm.OAuth2.c7fb1164-ae00-45dd-af3c-556a6c440f78```

After the successful configuration the sample application can be accessed on 
```https://localhost:8443/index```.

For more information about detailed configuration and customization continue 
reading the Tutorial.

# Tutorial

TBD

[1]: https://everitorg.wordpress.com/2015/03/24/everit-component-model-1-0-0-release/
[2]: https://github.com/everit-org/authentication-oauth2-ri
[3]: https://everitorg.wordpress.com/2014/07/31/everit-authentication
[4]: https://github.com/everit-org/authentication-oauth2-api
[5]: https://github.com/everit-org/authentication-oauth2-ri-ecm
[6]: https://developers.google.com/identity/sign-in/web/devconsole-project
[7]: https://developers.facebook.com/docs/apps/register
[8]: https://localhost:4848/system/console/configMgr