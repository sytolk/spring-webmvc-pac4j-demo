<p align="center">
  <img src="https://pac4j.github.io/pac4j/img/logo-spring-webmvc.png" width="300" />
</p>

This `spring-webmvc-pac4j-demo` project is a Spring Web MVC application secured by the [spring-webmvc-pac4j](https://github.com/pac4j/spring-webmvc-pac4j) security library with various authentication mechanisms: Facebook, Twitter, form, basic auth, CAS, SAML, OpenID Connect, JWT...

## Run and test

You can build the project and run the web app via jetty on [http://localhost:8080](http://localhost:8080) using the following commands:

    cd spring-webmvc-pac4j-demo
    mvn clean package jetty:run

For your tests, click on the "Protected url by **xxx**" link to start the login process with the **xxx** identity provider...

## You can find generated SP metadata in web server bin dir for example: /opt/apache-tomcat-7.0.70/bin
```
<property name="serviceProviderMetadataResourceFilepath" value="sp-metadata.xml" />
```
