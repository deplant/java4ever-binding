# AckiNacki SDK for Java

[![JDK version](https://img.shields.io/badge/Java-21-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.45.0-orange)](https://github.com/tonlabs/ever-sdk)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

* Discuss in
  Telegram: [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat\_en)
* Read full
  docs: [![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-binding/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-binding)

This is a Java binding project for
[EVER-SDK](https://github.com/tonlabs/ever-sdk) library adapted for 
[AckiNacki](https://everscale.network/), blockchain 
network.

This binding uses [JSON-RPC](https://github.com/tonlabs/ever-sdk/blob/master/docs/for-binding-developers/json_interface.md) interface of EVER-SDK.
Native calls are based on modern [Foreign Function & Memory API](https://openjdk.org/jeps/454).

This artifact provides full binding functionality, but doesn't include 
higher level helpers for development, tests or fast prototyping. Try our larger [Java4Ever](https://github.com/deplant/java4ever-framework) framework 
that is based on this binding for easier work with TVM blockchains.

### Goals

* Provide Java binding for EVER-SDK based on modern Java native memory access
* Support any modern versions of EVER-SDK without rebuild of binding itself
* Support custom EVER-SDK binaries

## Quick start

#### Prerequisites

* Install **JDK 21** ([link](https://adoptium.net/temurin/releases?version=20))

#### Add java4ever to your Maven of Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-binding:3.0.0'
}
```

* Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-binding</artifactId>
    <version>3.0.0</version>
</dependency>
```

### Creating EVER-SDK Context

If you use default EVER-SDK lib (_latest EVER-SDK that is included in the distribution_), 
you can create EverSdkContext object as following:

```java
EverSdkContext ctx = EverSdkContext.builder()
        .setConfigJson(configJson)
        .buildNew();
```

To use custom one, specify it in buildNew() method:

```java
EverSdkContext ctx = EverSdkContext.builder()
        .setConfigJson(configJson)
        .buildNew(new AbsolutePathLoader(Path.of("\home\ton\lib\libton_client.so")));
```

Variants of loading ton_client lib:
* `AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB")` - path from Environment variable
* `AbsolutePathLoader.ofUserDir("libton_client.so")` - file from ~ (user home)
* `new AbsolutePathLoader(Path.of("\home\ton\lib\libton_client.so"))` - any absolute path
* `new JavaLibraryPathLoader("ton_client");` - gets library from java.library.path JVM argument

### Calling EVER-SDK methods

It's very simple, just type ModuleName.methodName (list of modules and methods is here: [EVER-SDK API Summary](https://github.com/tonlabs/ever-sdk/blob/master/docs/SUMMARY.md) ). 
Note that method names are converted from snake_case to camelCase. Then pass EverSdkContext object as 1st parameter. That's all.

```java
Client.version(contextId);
```

## Support of new and custom SDK versions

### Custom EVER-SDK libs (no new API handles)

Just use your custom SDK binary instead of current. 
**java4ever-binding** is not bound to exact version of SDK.

### Support of new API handles

- Clone this repo
- Run `gradlew generateEverSdkApi -D--enable-preview -D--enable-native-access=java4ever.binding`


## Tests

- Clone this repo
- Run `gradlew test --tests "tech.deplant.java4ever.unit.*" -D--enable-preview -D--enable-native-access=java4ever.binding`

## Logging

**java4ever-binding** uses the JPL - [JDK Platform Loggging](https://openjdk.org/jeps/264) facade,
so can be easily bridged to any logging framework of your choice.