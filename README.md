# WebFlux Load Testing Project

Spring WebFlux 기반 고성능 API 서버의 한계를 강제 적용하여, 병목을 찾아 개선하는 과정을 기록하는 프로젝트입니다.

## 목표

초당 10만 건 처리를 목표로 쿠폰 발급 → 주문 → 실시간 채팅 순서로 구현하며 (일단 예정된 절차), 
각 단계에서 부하 테스트를 통해 병목을 발견하고 개선합니다.

## 기술 스택

- **Backend**: Spring Boot 4.0.5, Spring WebFlux, Java 21
- **DB**: MySQL + R2DBC (Non-blocking)
- **Cache**: Redis Cluster
- **Auth**: JWT (Access Token + Refresh Token Rotation)
- **Migration**: Flyway
- **Load Test**: k6
- **Monitoring**: Prometheus + Grafana

## 폴더 구조
```
src/main/java/com/testing/load/
├── auth/
│   ├── controller/         # AuthController (로그인, 회원가입, 재발급, 로그아웃)
│   ├── service/            # AuthService
│   └── dto/                # LoginRequest, RegisterRequest, TokenResponse
├── coupon/
│   ├── controller/         # CouponController
│   ├── service/            # CouponService, CouponIssueService
│   ├── repository/         # CouponRepository, CouponIssueRepository
│   ├── domain/             # Coupon, CouponIssue
│   └── dto/                # CouponRequest, CouponResponse, CouponIssueResponse
├── user/
│   ├── repository/         # UserRepository
│   ├── domain/             # User
│   └── dto/                # UserResponseDto
├── common/
│   ├── config/             # SecurityConfig, RedisConfig, R2dbcConfig, FlywayConfig, DataInitializer
│   ├── BaseEntity          # BaseEntity
│   ├── exception/          # GlobalExceptionHandler, BusinessException, ErrorCode
│   ├── jwt/                # JwtProvider, JwtAuthenticationFilter, TokenBlacklistService
│   └── properties/         # JwtProperties, AppProperties
k6-scripts/
├── setup-users.js          # 부하 테스트용 유저 대량 생성
└── coupon-issue.js         # 쿠폰 발급 부하 테스트
```