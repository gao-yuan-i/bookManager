# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build and run
./mvnw spring-boot:run

# Build only (skip tests)
./mvnw package -DskipTests

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=BookManagementSystemApplicationTests
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Architecture

This is a **Spring Boot 3.1.5 + MyBatis-Plus + MySQL** backend for a library management system (图书管理系统). Java 17, Maven build, running on port 8080 by default.

### Key design decisions

**Authentication is handled outside Spring Security.** Spring Security is configured to permit all requests (`SecurityConfig.java:26`). Actual JWT authentication runs in `JwtInterceptor`, which intercepts all `/api/**` paths except `/api/login` and `/api/register`. The interceptor validates the `Authorization: Bearer <token>` header, then injects `userId`, `username`, and `role` as request attributes for downstream controllers to read. Spring Security is only used for `BCryptPasswordEncoder`.

**Three controller layers by role:**
- `AuthController` (`/api`) — public: login, register
- `UserBookController` (`/api/user`) — authenticated users: browse/borrow/return books, manage own profile
- `AdminController` (`/api/admin`) — admin operations: CRUD for books/users/borrow records

**DTO/VO separation:** Controllers receive DTOs (`LoginDTO`, `RegisterDTO`, `BookDTO`, `UserDTO`) and return VOs (`BookVO`, `UserVO`, `BorrowRecordVO`). The `UserVO` and `BookVO` never expose the password hash. `BorrowRecordVO` denormalizes user/book names and includes a human-readable `statusDesc`.

**Response format:** All endpoints use a standard `Result<T>` wrapper (`code`, `message`, `data`). Paginated endpoints use `PageResult<T>` (`total`, `list`). Static factory methods (`Result.success()`, `PageResult.of()`) are the expected way to construct responses.

**Error handling:** Business errors throw `BusinessException(code, message)`, caught by `GlobalExceptionHandler` and converted to `Result`. Generic exceptions return HTTP 500 with a generic message.

**MyBatis-Plus** is the ORM layer. Mappers extend `BaseMapper<T>`, services extend `ServiceImpl<M, T>` and implement custom `IService<T>` interfaces. Pagination is done via `Page<T>` objects passed to `baseMapper.selectPage()`. The pagination plugin is configured for MySQL in `MyBatisPlusConfig`.

**Enums:** `RoleEnum` (ADMIN/USER) and `BorrowStatusEnum` (BORROWING=1, RETURNED=2, OVERDUE=3). Status codes are stored as integers in the database, not enum ordinals.

### Layer summary

| Layer | Package | Role |
|---|---|---|
| Controller | `controller` | REST endpoints, delegates to services |
| Service | `service` / `service.impl` | Business logic, transaction management |
| Mapper | `mapper` | MyBatis-Plus data access |
| Entity | `entity` | Database table mapping (`@TableName`) |
| DTO | `dto` | Request body objects with `@Valid` annotations |
| VO | `vo` | Response objects (no sensitive fields) |
| Config | `config` | Security, CORS, MyBatis-Plus, Knife4j |
| Interceptor | `interceptor` | JWT authentication, web MVC config |
| Exception | `exception` | `BusinessException` + global handler |
| Common | `common` | `Result<T>` and `PageResult<T>` wrappers |
| Utils | `utils` | `JwtUtils` (generate/parse/validate tokens) |

### Database tables

`users`, `books`, `borrow_records` — configured in `application.yml` under `mybatis-plus.global-config.db-config` with logical delete on the `deleted` field, underscore-to-camel-case mapping, and `StdOutImpl` SQL logging.

### JWT configuration

In `application.yml`: `jwt.secret` and `jwt.expiration` (86400000ms = 24 hours). The `JwtUtils` constructor injects these via `@Value`.
