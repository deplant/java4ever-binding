# java4ever-binding

[![JDK version](https://img.shields.io/badge/Java-20-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.43.3-orange)](https://github.com/tonlabs/ever-sdk)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

* Discuss in
  Telegram: [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat\_en)
* Read full
  docs: [![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-binding/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-binding)

**java4ever-binding** is a Java Binding library for
[EVER-SDK](https://github.com/tonlabs/ever-sdk) framework of
[Everscale](https://everscale.network/) network via
[JSON-RPC](https://github.com/tonlabs/ever-sdk/blob/master/docs/for-binding-developers/json_interface.md) interface.
Native calls are based on modern [Foreign Function & Memory API](https://openjdk.org/jeps/434).
This artifact provide only binding functionality and is not suitable for large tests or fast prototyping.
There is a companion [java4ever-framework](https://github.com/deplant/java4ever-framework) library that should be used
together with this binding.

### Goals

* Provide Java binding for EVER-SDK based on modern Java native memory access
* Support any modern versions of EVER-SDK without rebuild of binding itself
* Support custom EVER-SDK binaries

## Quick start

#### Prerequisites

* Install **JDK 20** ([link](https://adoptium.net/temurin/releases?version=19))

#### Add java4ever to your Maven of Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-binding:2.1.0'
}
```

* Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-binding</artifactId>
    <version>2.1.0</version>
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
Client.version(ctx);
```


## Notes

### Custom EVER-SDK libs

EVER-SDK libs are included in the distribution, but if you want to use custom one - build **EVER-SDK** binary lib "**ton_client**"(.so/.dll) yourself (or
  get [precompiled one](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))


### Logging

java4ever-binding uses the JDK Platform Loggging (JEP 264: Platform Logging API and Service),
so can be easily bridged to any logging framework.