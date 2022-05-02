# java4ever:binding

[![JDK version](https://img.shields.io/badge/Java-17+-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/TON%20SDK-v1.28.1-orange)](https://github.com/tonlabs/TON-SDK/tree/1.28.0)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

Java Binding for TON-SDK via JSON-RPC interface. Native interconnection is not based on JNI derivatives but on
modern [Foreign Memory Access API](https://openjdk.java.net/jeps/393)

**Get quick help in our telegram
channel:** [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/ton\_sdk)

### Goals

* Provide Java binding for EVER-SDK based on modern Java native memory access
* Support any modern versions of EVER-SDK without rebuild of binding
* Support of custom EVER-SDK
* Support different ways to plug EVER-SDK library
* Support arbitrary complex objects (with deep inheritance hierarchies and extensive use of generic types)

### Download (Binding-only)

#### Gradle

```groovy
dependencies {
    implementation 'tech.deplant.binding:binding:1.1.5'
}
```

#### Maven

```xml

<dependency>
    <groupId>io.everails.binding</groupId>
    <artifactId>binding</artifactId>
    <version>1.1.5</version>
</dependency>
```

## Features

### Can be used with any TON-SDK version

You can use TON-SDK 1.16.0+ for your project or even load multiple libraries with different versions. You can use your
custom TON-SDK fork if you like. 