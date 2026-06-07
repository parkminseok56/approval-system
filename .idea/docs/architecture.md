# Architecture - 전자결재 시스템

## 1. Overview

본 시스템은 전자결재 프로세스를 관리하는 백엔드 시스템이다.
사용자는 결재 요청을 생성하고, 지정된 결재 라인을 따라 승인/반려를 수행한다.

---

## 2. System Architecture

### 구성

- Spring Boot (Backend API)
- MariaDB (Relational Database)
- Redis (Caching / Session)
- Docker (Containerization)

---

## 3. Layer Structure

### Controller Layer
- API 요청 처리
- Request/Response 관리

### Service Layer
- 비즈니스 로직 처리
- 결재 흐름 제어

### Repository Layer
- JPA 기반 DB 접근

---

## 4. Core Flow

1. 사용자가 결재 요청 생성
2. 결재 라인 설정 (1~3단계)
3. 첫 번째 결재자 승인
4. 다음 결재자 순차 진행
5. 최종 승인 시 완료 처리

---

## 5. Data Flow

Client → Controller → Service → Repository → DB

Service → Redis (optional caching)

---

## 6. Key Design Decisions

- 결재 상태는 ENUM으로 관리
- 결재 라인은 순서(order_no) 기반 처리
- Redis는 대기 결재 목록 캐싱 용도
- 모든 요청은 JWT 기반 인증 처리