# java4ever-binding

[![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-binding/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-binding)
[![JDK version](https://img.shields.io/badge/Java-19+-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.36+-orange)](https://github.com/tonlabs/TON-SDK/tree/1.28.0)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

**Get quick help in our telegram
channel:** [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat)

**java4ever-binding** is a Java Binding library for
[EVER-SDK](https://github.com/tonlabs/ever-sdk) framework of
[Everscale](https://everscale.network/) network via
[JSON-RPC](https://github.com/tonlabs/ever-sdk/blob/master/docs/for-binding-developers/json_interface.md) interface.
Native interconnection is not based on JNI derivatives but on
modern [Foreign Memory Access API](https://openjdk.java.net/jeps/393).
This artifact provide only binding functionality and is not suitable for large tests or fast prototyping.
There is a companion [java4ever-framework](https://github.com/deplant/java4ever-framework) library that should be used
together with this binding.

### Goals

* Provide Java binding for EVER-SDK based on modern Java native memory access
* Support any modern versions of EVER-SDK without rebuild of binding itself
* Support custom EVER-SDK binaries

## Quick start

#### Prerequisites

* Install **JDK 19** or higher ([link](https://adoptium.net/temurin/releases?version=19))
* Build **EVER-SDK** binary lib "**ton_client**"(.so/.dll) (or
  get [precomiled one](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))

#### Add java4ever to your Maven of Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-binding:1.1.2'
}
```

* Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-binding</artifactId>
    <version>1.2.0</version>
</dependency>
```

### Logging

java4ever-binding uses the [SLF4J](https://www.slf4j.org/) logging facade.
Users should therefore adopt a logging implementation like [Log4j](https://logging.apache.org/log4j/2.x/) or any other.
