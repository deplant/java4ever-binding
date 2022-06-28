# java4ever-binding

[![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-binding/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-binding)
[![JDK version](https://img.shields.io/badge/Java-17.0.2+-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.34.2+-orange)](https://github.com/tonlabs/TON-SDK/tree/1.28.0)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

**[GOSH Mirror](gosh://0:078d7efa815982bb5622065e7658f89b29ce8a24bce90e5ca0906cdfd2cc6358/deplant/java4ever-binding)**

**java4ever:binding** is a Java Binding library for
[EVER-SDK](https://github.com/tonlabs/ever-sdk) framework of
[Everscale](https://everscale.network/) network via
[JSON-RPC](https://github.com/tonlabs/ever-sdk/blob/master/docs/for-binding-developers/json_interface.md) interface.
Native interconnection is not based on JNI derivatives but on
modern [Foreign Memory Access API](https://openjdk.java.net/jeps/393).
This artifact provide only binding functionality and is not suitable for large tests or fast prototyping.
There is a companion [java4ever-framework](https://github.com/deplant/java4ever-framework) library that should be used
together with this binding.

**Get quick help in our telegram
channel:** [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat)

### Goals

* Provide Java binding for EVER-SDK based on modern Java native memory access
* Support any modern versions of EVER-SDK without rebuild of binding itself
* Support custom EVER-SDK binaries

### Prerequisites

* **JDK 17** (17.0.2 or higher)
* **EVER-SDK** binary lib "**ton_client**" (build it yourself from github or
  get [precomiled ones](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))
* **java.library.path** set. Add correct path to "**ton_client**" library as argument to Java
  run: `-Djava.library.path=<path_to_ton_client>`.

### Download (Binding-only)

#### Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-binding:1.1.2'
}
```

#### Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-binding</artifactId>
    <version>1.2.0</version>
</dependency>
```

### Can be used with any TON-SDK version

You can use TON-SDK 1.33.0+ for your project or even load multiple libraries with different versions. You can use your
custom TON-SDK fork if you like. You can use even lower versions, but we will not fix any issues with it.