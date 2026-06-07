# API Documentation - 전자결재 시스템

본 문서는 전자결재 시스템의 REST API 명세를 정의한다.

---

# 1. Base Information

## Base URL

http://localhost:8080/api


## Authentication
모든 API는 JWT 인증을 사용한다.


Authorization: Bearer {token}


---

# 2. Auth API (인증)

## 2.1 회원가입

###  POST /auth/signup 



Request

```json
{
  "username": "test",
  "password": "1234",
  "name": "홍길동"
}
```

Response
```json
{
  "status": 200,
  "message": "SIGNUP_SUCCESS"
}
```
## 2.2 로그인
###  POST /auth/login

Request
```json
{
  "username": "test",
  "password": "1234"
}
```
Response
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}

```

## 2.3 내 정보 조회
###  GET /auth/me

Response
```json

{
  "id": 1,
  "username": "test",
  "name": "홍길동",
  "department": "개발팀"
}
```


---

# 3. User API

## 3.1 사용자 목록 조회

###  GET /users

Response

```json
[
  {
    "id": 1,
    "name": "홍길동",
    "department": "개발팀"
  }
]
```
---
# 4. Department API
## 4.1 부서 목록 조회
###  GET /departments

Response
```json
[
  {
    "id": 1,
    "name": "개발팀",
    "parentId": null
  },
  {
    "id": 2,
    "name": "백엔드팀",
    "parentId": 1
  }
]
```
---
# 5. Approval Request API (핵심)
## 5.1 결재 요청 생성
## POST /approvals

Request
```json
{
  "title": "휴가 신청",
  "content": "3일 휴가 요청",
  "approverIds": [2, 3]
}
```
Response
```json
{
  "id": 100,
  "status": "PENDING",
  "message": "APPROVAL_CREATED"
}
```
## 5.2 결재 목록 조회
### GET /approvals


### Query Parameters

| Name   | Type   | Required | Description |
|--------|--------|----------|-------------|
| status | String | No       | PENDING / IN_PROGRESS / APPROVED |
| page   | Int    | No       | default 0 |
| size   | Int    | No       | default 10 |

Response
```json
{
  "content": [
    {
      "id": 100,
      "title": "휴가 신청",
      "status": "IN_PROGRESS",
      "createdBy": "홍길동"
    }
  ],
  "page": 0,
  "size": 10
}
```
## 5.3 결재 상세 조회
### GET /approvals/{id}

Response
```json
{
  "id": 100,
  "title": "휴가 신청",
  "content": "3일 휴가 요청",
  "status": "IN_PROGRESS",
  "lines": [
    {
      "order": 1,
      "approver": "김철수",
      "status": "APPROVED"
    },
    {
      "order": 2,
      "approver": "이영희",
      "status": "PENDING"
    }
  ]
}
```
---
# 6. Approval Process API
## 6.1 승인 처리
### POST /approvals/{id}/approve

### Request
```json
{
  "comment": "승인합니다"
}
```

### Response
```json
{
  "status": "APPROVED",
  "nextApprover": "이영희"
}
```
## 6.2 반려 처리
## POST /approvals/{id}/reject
```json
{
  "comment": "반려합니다"
}
```
Response
```json
{
  "status": "REJECTED"
}
```
---
# 7. Approval Status Flow (승인 상태 처리 플로우)
```text
PENDING (대기)
  ↓
IN_PROGRESS (결재 진행 중)
  ↓
APPROVED (승인 완료)

PENDING / IN_PROGRESS / APPROVED
          ↓
     REJECTED (반려)
```
---
# 8. Error Response Format
### 공통 에러
```json
{
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Invalid input data"
}
```
### 인증 실패
```json
{
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Invalid token"
}
```
---
# 9. API Design Rules
* 모든 요청은 /api prefix 사용
* 인증 필요한 API는 JWT 필수
* Response는 항상 JSON 통일
* status는 enum으로 관리
* paging은 기본 제공
---
# 10. Versioning (추후 대비)
* /api/v1/approvals
