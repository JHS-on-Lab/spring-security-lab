# Spring Security Lab

#### Spring Boot와 Spring Security를 기반으로 JWT 인증/인가 구조를 학습하고 구현한 프로젝트입니다.

* Spring Security 동작 원리 이해
* JWT 기반 인증/인가 흐름 구현
* 예외 처리 및 계층 분리 연습
* OAuth2 소셜 로그인

### Tech Stack

* Java 25
* Spring Boot 4.0.1
* Spring Data JPA
* Spring Security
* H2 Database (개발/테스트)
* JWT (JJWT 0.13.0)
* OAtuh2 Client
* Swagger (Springdoc OpenAPI 3.0.1)
* Gradle
* Lombok

### Package Structure

```text
me.son.springsecuritylab
 ├─ auth
 │   ├─ controller
 │   ├─ domain
 │   │   └─ service
 │   ├─ dto 
 │   │  exception
 │   ├─ jwt
 │   │   ├─ JwtProvider
 │   │   ├─ JwtFilter
 │   │   ├─ JwtService 
 │   │   ├─ dto
 │   │   └─ exception
 │   └─ oauth2
 │       └─ handler
 │
 ├─ global
 │   ├─ exception
 │   ├─ paging
 │   ├─ response
 │   ├─ security
 │   │   ├─ config
 │   │   ├─ handler
 │   │   ├─ service
 │   │   └─ CustomUserDetails
 │   ├─ swagger
 │   └─ util
 │       └─ CookieUtil
 │
 └─ user
     ├─ controller
     ├─ domain
     │   ├─ entity
     │   │   └─ enums
     │   ├─ repository
     │   ├─ factory     
     │   └─ service
     ├─ dto
     ├─ mapper
     └─ exception
```

---

### Authentication & Authorization

* JWT 기반 인증 방식 사용
* Access Token을 Authorization: Bearer <token> 헤더로 전달
* Spring Security Filter에서 JWT 검증 후 SecurityContext에 인증 정보 저장
* Access Token + Refresh Token 방식을 사용
* Refresh Token은 HttpOnly Cookie로 관리

#### JWT Claim 구성

* subject: username
* role: 사용자 권한 (String)

```json
{
  "sub": "tester01",
  "role": "ROLE_USER"
}
```

#### OAuth2 소셜 로그인

- 지원 Provider
  - GOOGLE


- OAuth2 설계 방식
  - OAuth2 인증 성공 시 OAuth2LoginSuccessHandler에서 후처리
  - Provider + ProviderUserId 기반의 UserIdentity 엔티티로 계정 관리
  - OAuth2 로그인 이후 인증은 JWT 기반으로 통합
  - OAuth2 전용 UserDetails는 생성하지 않고, JWT 기반 단일 인증 주체 유지


- OAuth2 Success Flow
  1. OAuth2 Provider 인증 성공
  2. Provider 및 Provider User ID 추출
  3. UserIdentity 조회
  4. 기존 계정 → 로그인
  5. 이메일 중복 → 계정 연동 필요
  6. 신규 사용자 → User + UserIdentity 생성
  7. JWT 발급 
  8. Refresh Token → HttpOnly Cookie 
  9. Access Token → Redirect URL(Fragment)
  10. SPA 클라이언트로 Redirect


- OAuth2 Success Flow
  1. OAuth2 인증 실패 시 OAuth2LoginFailureHandler 실행
  2. 인증 정보 정리 후 실패 페이지로 Redirect

---

### Pagination (Paging)
사용자 목록 조회 API는 페이징 처리를 지원합니다. Spring Data JPA의 Page를 사용하고, 공통 응답 DTO(PageResponseDto)로 변환하여 반환합니다.

#### 요청 파라미터
| 파라미터 | 설명              | 기본값 |
| ---- | --------------- | --- |
| page | 페이지 번호 (0부터 시작) | 0   |
| size | 페이지당 데이터 수      | 10  |

#### 요청 예시

```http
GET /api/users?page=0&size=10
Authorization: Bearer {accessToken}
```

#### 응답 예시

```json
{
  "data": {
    "content": [
      {
        "id": "1",
        "username": "testuser1",
        "role": "ROLE_USER",
        "email": "",
        "createdAt": "2025-01-01T00:00:00"
      },
      {
        "id": "2",
        "username": "testuser2",
        "role": "ROLE_ADMIN",
        "email": "testuser2@gmail.com",
        "createdAt": "2025-01-01T00:00:00"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1
  },
  "message": null,
  "success": true
}
```

---

### API Documentation (Swagger)

Swagger(OpenAPI)를 적용하여
API 명세를 웹 UI로 확인할 수 있습니다.

#### Swagger 접속 URL

```text
http://localhost:8080/swagger-ui/index.html
```

#### Swagger에서 JWT 인증 사용 방법
1. Swagger UI 접속
2. Authorize 버튼 클릭
3. 아래 형식으로 토큰 입력

```text
Bearer {accessToken}
```

---

### Exception Handling
- 모든 비즈니스 예외는 BusinessException으로 통합
- 도메인별 ErrorCode enum으로 에러 코드 관리
- GlobalExceptionHandler에서 공통 응답 처리
- JWT 예외는 Security 계층에서 처리 후 공통 응답 반환

---

### TODO
- OAuth2 Redirect URI 정책 추가 (추후 SPA Client 개발 시 연동하여 개발 예정)
  - 클라이언트별 허용된 Redirect URI 관리
  - Open Redirect 취약점 방지
- OAuth2 Provider 확장
  - GitHub, Kakao 등 추가 예정
- 사용자 상태 기반 강제 로그아웃 처리
