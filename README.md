# approval-system

전자결재 시스템 백엔드 (Spring Boot + MariaDB + Redis)

## Tech Stack

- Java 17
- Spring Boot 3.4
- MariaDB
- Redis
- JWT

## 프로젝트 구조

```
approval-system/
├── docker/                    # MariaDB, Redis 컨테이너
├── docs/                      # API, ERD, 요구사항 문서
└── src/main/java/com/example/approval/
    ├── approval/              # 결재 도메인
    ├── auth/                  # 인증 (JWT)
    ├── department/            # 부서
    ├── user/                  # 사용자
    ├── notification/          # Redis 알림
    └── global/                # 공통 설정/예외/유틸
```

## 개발 순서

1. 프로젝트 생성 
2. Docker (MariaDB, Redis) 띄우기 — Docker 설치 시 `docker compose up -d`, 미설치 시 `standalone` 프로필 사용
3. User Entity 
4. Department Entity 
5. JWT 로그인 
6. Approval Entity 
7. 결재 생성 API 
8. 결재 승인 API 
9. 결재 반려 API 
10. Redis 알림 (스켈레톤 완료)

## 실행 방법

### 1. Docker 인프라 실행

```bash
cd docker
docker compose up -d
```

### 2. 애플리케이션 실행

**Docker 사용 (MariaDB + Redis)**

```bash
cd docker
docker compose up -d
./gradlew bootRun
```

**Docker 없이 빠른 실행 (H2 in-memory)**

```bash
./gradlew bootRun --args='--spring.profiles.active=standalone'
```

- API Base URL: `http://localhost:8080/api`
- 문서: `docs/` 폴더 참고

## API 문서

- [API 명세](docs/api.md)
- [ERD](docs/erd.md)
- [요구사항](docs/requirements.md)
- [아키텍처](docs/architecture.md)
