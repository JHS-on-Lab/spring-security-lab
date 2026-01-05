# Spring Security Lab

#### Spring Boot와 Spring Security를 기반으로 JWT 인증/인가 구조를 학습하고 구현한 프로젝트입니다.

* Spring Security 동작 원리 이해
* JWT 기반 인증/인가 흐름 구현
* 예외 처리 및 계층 분리 연습

### Tech Stack

* Java 25
* Spring Boot 4.0.1
* Spring Data JPA
* Spring Security
* H2 Database (개발/테스트)
* JWT (JJWT 0.13.0)
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
 │   └─ jwt
 │       ├─ JwtProvider
 │       ├─ JwtFilter
 │       ├─ dto
 │       └─ exception
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
     │   └─ service
     ├─ dto
     ├─ mapper
     └─ exception
```
### Authentication & Authorization

* JWT 기반 인증 방식 사용
* Access Token을 Authorization: Bearer <token> 헤더로 전달
* Spring Security Filter에서 JWT 검증 후 SecurityContext에 인증 정보 저장
* Access Token + Refresh Token 방식을 사용

#### JWT Claim 구성

* subject: username
* role: 사용자 권한 (String)

```json
{
  "sub": "tester01",
  "role": "ROLE_USER"
}
```

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
        "email": "",
        "password": "$2a$10$hFw/WXlsNqIC0.xrxrxt2OCKaN42UFHEgJMtlt3MJCOI9XSRgrWsW",
        "provider": "LOCAL",
        "role": "ROLE_USER",
        "username": "tester1"
      },
      {
        "email": "",
        "password": "$2a$10$IOyY2cDp.ed/cW/uB9lGKeQY.v5DIy1kr8ZSBOXcNG4eL/xlEQQUS",
        "provider": "LOCAL",
        "role": "ROLE_USER",
        "username": "tester2"
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