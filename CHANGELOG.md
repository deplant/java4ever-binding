### 2.1.0

- Updated EVER-SDK -> 1.43.3
- Updated Gradle -> 8.2
- Updated baseline JDK -> 20
- Helper for LibraryLoader construction
- Included EVER-SDK libs in distribution (Use DefaultLoader choice)
- Added generateEverSdkApi task for one-click EVER-SDK API generation
- Added generateFfiBridge task for one-click FFI Bridge to ton_client lib generation

### 1.8.0

- Support of EVER-SDK 1.43.2

### 1.7.0

- Fixes to SDK error parsing
- Fixes to Context serialization

### 1.6.0

- Updated EVER-SDK -> 1.42
- Fixed serialization of integer-based enums

### 1.5.0

- Updated EVER-SDK -> 1.41
- Updated dependency: Gradle -> 8.0.2
- Removed dependency: org.junit.platform:junit-platform-launcher
- Fixed incorrect "ABI version" tag serialization in AbiContract type
- Fixed BuilderOp.Integer incorrect param type
- Improved generator rules
- Added automatic setting of binding library info to EVER-SDK Context config

### 1.4.0

- Updated EVER-SDK to 1.40
- All binding code generation rewritten from JavaScript to pure Java (javapoet lib)
- Fixed missing types
- Fixed AbiEvent deserialization
- Changed Numbers to Integers
- Fixed Void type problems
- Fixed type inheritance for variants
- Updated dependency: Gradle -> 7.6

### 1.3.0

- Fixed parsing errors when decoding error messages of error 507
- Improved error code parsing for contract custom errors
- Context class is now a record
- Improved concurrent execution of requests (switched to AtomicInteger counter)
- Removed SLF4J logging, replaced with JDK9 Platform logging (less dependencies, more general facade)
- made lazy-style loggging to improved performance
- Switched to "api" type dependencies to export jackson to library users

### 1.2.0