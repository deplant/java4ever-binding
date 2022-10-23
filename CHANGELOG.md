### 1.3.0

- Fixed parsing errors when decoding error messages of error 507
- Improved error code parsing for contract custom errors
- Context class is now a record
- Improved concurrent execution of requests (switched to AtomicInteger counter)
- Removed SLF4J logging, replaced with JDK9 Platform logging (less dependencies, more general facade)
- made lazy-style loggging to improved performance
- Switched to "api" type dependencies to export jackson to library users

### 1.2.0