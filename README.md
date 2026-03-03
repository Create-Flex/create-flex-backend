# Create Flex Backend

**Create-Flex**는 크리에이터 매니지먼트 전문 플랫폼으로, 크리에이터와 담당 직원(매니저/관리자)이 하나의 시스템에서 협업할 수 있도록 설계된 인트라넷 서비스입니다.

크리에이터의 업무 관리부터 인사·근태·휴가·건강까지, 크리에이터 생애주기 전반을 통합 관리합니다.
Java 17 + Spring Boot 3 기반으로 구축되었으며, WebSocket(STOMP), SSE, JWT 인증 등 실시간·보안 기능을 포함합니다.

프론트엔드 저장소: [create-flex-frontend](https://github.com/Create-Flex/create-flex-frontend)

---

## 프로젝트 개요

- **서비스 유형**: B2B 사내 크리에이터 관리 시스템 (인트라넷)
- **사용자 역할**: EMPLOYEE(일반 직원), MANAGER(매니저), ADMINISTRATOR(관리자), CREATOR(크리에이터)
- **핵심 가치**: 역할별 접근 제어, 실시간 협업, 크리에이터 생애주기 통합 관리

---

## 주요 기능

| 도메인 | 설명 | 주요 엔티티 |
|--------|------|------------|
| 인증 (`auth`) | 로그인·로그아웃·토큰 재발급 | - |
| 회원 (`member`) | 회원 기본 정보, 역할/상태 관리 | Member, MemberProfile, MemberCreatorDetail, MemberEmployeeDetail |
| 크리에이터 (`creator`) | 크리에이터 정보, 담당 매니저 연결 | - |
| 칸반 보드 (`creatorTodo`) | 컬럼/카드 CRUD + WebSocket 실시간 동기화 | CreatorTodoColumn, CreatorTodo |
| 휴가 (`vacation`) | 연차/반차/병가/경조사/워케이션 신청·승인 | Vacation, VacationFamily, VacationSick, VacationWorkation |
| 근태 (`attendance`) | 출퇴근 기록 관리 | Attendance |
| 건강 (`health`) | 건강검진 결과, 크리에이터 정신건강 관리 | Health, CreatorMentalHealth, CheckupSummary |
| 일정 (`schedule`) | 일정 캘린더 (크리에이터/직원) | Schedule, ScheduleVisitor |
| 채팅 (`chat`) | 채팅방 생성·참여, 메시지 | ChatRoom, ChatRoomMember, ChatMessage |
| 알림 (`notification`) | SSE 실시간 알림, 읽음 처리 | Notification |
| AI (`ai`) | AI 챗 (OpenAI 기반 크리에이터 분석) | - |
| 부서 (`department`) | 부서 관리 | Department |
| 직원 (`employee`) | 직원 프로필 조회 | - |
| 이미지 (`image`) | 이미지 업로드 (S3 Presigned URL) | - |
| QA 게시판 (`QnA`) | Q&A 게시판 | QABoard |

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| **언어** | Java 17 |
| **프레임워크** | Spring Boot 3.5.9 |
| **보안** | Spring Security + JWT (Access Token: Body, Refresh Token: HttpOnly Cookie) |
| **ORM** | Spring Data JPA |
| **데이터베이스** | MySQL 8.0 |
| **캐시 / 세션** | Spring Data Redis |
| **실시간 통신** | Spring WebSocket + STOMP (칸반), SSE (알림) |
| **파일 스토리지** | AWS S3 (Presigned URL) |
| **AI** | Spring AI + OpenAI GPT-4o-mini |
| **이메일** | Spring Mail (JavaMail) |
| **API 문서** | Swagger (springdoc-openapi 2.8.5) |
| **빌드 도구** | Gradle |

---

## 시스템 아키텍처

<img width="1122" height="561" alt="파이널 프로젝트 시스템 아키텍처" src="https://github.com/user-attachments/assets/68736eab-3e7f-4a76-b213-f72b3d4f917c" />

### 인증 흐름

```
로그인 요청 (사번 + 비밀번호)
    └── JWT 발급
         ├── Access Token  → Response Body (프론트: Zustand/메모리 보관, 새로고침 시 재발급)
         └── Refresh Token → HttpOnly Cookie (XSS 방어)
```

---

## 설치 및 실행

Java 17 이상이 필요합니다.

```bash
# 저장소 클론
git clone <repository-url>
cd create-flex-backend

# 빌드
./gradlew clean build

# 서버 실행
./gradlew bootRun
```

### 환경 설정

민감한 정보(DB 접속 정보, AWS 키, JWT 시크릿, OpenAI API 키 등)는 `application-secret.yaml`로 관리합니다.
`src/main/resources/` 하위에 아래 형식의 파일을 생성해야 합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://[DB_HOST]:[DB_PORT]/[DB_NAME]
    username: [DB_USER]
    password: [DB_PASSWORD]
  data:
    redis:
      host: [REDIS_HOST]
      port: [REDIS_PORT]
  ai:
    openai:
      api-key: [OPENAI_API_KEY]
cloud:
  aws:
    credentials:
      access-key: [AWS_ACCESS_KEY]
      secret-key: [AWS_SECRET_KEY]
    s3:
      bucket: [AWS_S3_BUCKET_NAME]
jwt:
  secret: [YOUR_JWT_SECRET_KEY]
```

> 서버 기본 포트는 `8888`이며, Swagger UI는 `http://localhost:8888/swagger-ui.html`에서 확인할 수 있습니다.

---

## 폴더 구조

도메인 중심 패키지 구조로 비즈니스 로직을 도메인별로 모듈화하였습니다.

```
create-flex-backend/
└── src/main/java/com/mcn/in4/
    ├── In4Application.java
    ├── config/                     # AWS S3, JavaMail 설정
    ├── global/
    │   ├── aws/                    # S3 업로드 유틸
    │   ├── config/                 # Security, WebSocket, Redis, Swagger, AI 설정
    │   ├── error/                  # 공통 예외 처리 (CustomException, ErrorCode)
    │   ├── security/               # JWT Filter, Token Provider
    │   ├── sse/                    # SseEmitters (알림 연결 풀)
    │   └── util/                   # Cookie 유틸
    └── domain/                     # 비즈니스 도메인 (20개)
        └── {domain}/
            ├── controller/         # REST 컨트롤러 + Swagger API 인터페이스
            ├── dto/
            │   ├── request/        # 요청 DTO
            │   └── response/       # 응답 DTO
            ├── entity/             # JPA 엔티티
            ├── repository/         # Spring Data JPA Repository
            └── service/            # 서비스 인터페이스 + 구현체
```

---

## 주요 기술 구현

### 칸반 보드 실시간 동기화 (WebSocket STOMP)
- STOMP `/pub/todo/move` 발행 → 백엔드 DB 업데이트 후 `/sub/creator-todo/{creatorId}` 브로드캐스트
- `clientUuid`로 자신이 보낸 메시지를 식별·무시하여 중복 업데이트 방지
- 카드 생성(`TODO_CREATED`), 수정(`TODO_UPDATED`), 삭제(`TODO_DELETED`)도 동일한 브로드캐스트 방식으로 실시간 동기화

### JWT 인증 보안 설계
- Access Token: Response Body 전달 → 프론트(Zustand 메모리) 보관, 새로고침 시 자동 재발급
- Refresh Token: `HttpOnly Cookie`로 발급 → XSS 공격으로부터 토큰 보호
- STOMP 인증: `StompHandler`가 WebSocket 연결 시 JWT 검증 (인터셉터 방식)

### SSE 실시간 알림
- `SseEmitters`로 연결 풀을 관리하여 다중 클라이언트 동시 지원
- 알림 트리거: 휴가 신청·승인·반려, 새 채팅 메시지 수신 등
- 연결 끊김 시 클라이언트 자동 재연결 처리

### 파일 업로드 (AWS S3 Presigned URL)
- 백엔드에서 Presigned URL 발급 후 클라이언트에서 S3로 직접 업로드
- 서버 부하 최소화 및 대용량 파일 업로드 지원

### AI 어시스턴트 (Spring AI + OpenAI)
- GPT-4o-mini 기반 사내 AI 어시스턴트
- 크리에이터 로그인 시 AI 자동 분석 결과 제공
- 사원·근태·휴가·일정 관련 Tool Function 연동
- 권한 없는 기능(Tool) 접근 시 통일된 거절 메시지 처리

---

## 역할별 접근 제어

| 역할 | 설명 |
|------|------|
| `ADMINISTRATOR` | 전체 관리자 — 모든 기능 접근, 연차 승인·반려, 부서/팀 관리 |
| `MANAGER` | 매니저 — 담당 크리에이터 관리, 칸반 조회, 휴가 승인 |
| `EMPLOYEE` | 일반 직원 — 개인 기능 중심 |
| `CREATOR` | 크리에이터 — 칸반 보드, 마이페이지, AI 분석 |

---

## 주요 API 엔드포인트

### 인증 (`/api/auth`)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/auth/login` | 로그인 (Access Token 반환, Refresh Token Cookie 설정) |
| POST | `/api/auth/logout` | 로그아웃 |
| POST | `/api/auth/reissue` | Access Token 재발급 |

### 크리에이터 (`/api/creators`)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/creators/{creatorId}` | 크리에이터 단건 조회 |
| PUT | `/api/creators/{creatorId}` | 크리에이터 정보 수정 |
| DELETE | `/api/creators/{creatorId}` | 크리에이터 삭제 |
| GET | `/api/creators/manager/{managerId}` | 담당 매니저의 크리에이터 목록 |
| GET | `/api/creators/my` | 내 크리에이터 정보 |

### 칸반 보드 (`/api/creator-todo` + WebSocket)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/creator-todo/{creatorId}` | 보드 전체 조회 (컬럼+카드) |
| DELETE | `/api/creator-todo/{todoId}` | 카드 삭제 |
| WS | `/pub/todo/move` | 카드 이동 (STOMP 발행) |
| WS | `/pub/todo/create` | 카드 생성 알림 |
| WS | `/pub/todo/update` | 카드 수정 알림 |
| WS | `/pub/todo/delete` | 카드 삭제 알림 |
| SUB | `/sub/creator-todo/{creatorId}` | 보드 실시간 구독 |

### 근태 (`/api/attendance`)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/attendance/dashboard/my` | 내 출퇴근 현황 |
| GET | `/api/attendance/dashboard/company` | 전사 출퇴근 현황 |
| POST | `/api/attendance/check-in` | 출근 |
| POST | `/api/attendance/check-out` | 퇴근 |

### 휴가 (`/api/vacations`)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/vacations/my` | 내 연차 신청 목록 |
| GET | `/api/vacations/my/remainder` | 잔여 연차 조회 |
| GET | `/api/vacations/my/stats` | 연차 통계 |
| PUT | `/api/admin/vacations/{vacationId}/approve` | 연차 승인 (관리자) |
| PUT | `/api/admin/vacations/{vacationId}/reject` | 연차 반려 (관리자) |

### 일정 (`/api/schedules`)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/schedules/me` | 내 일정 목록 |
| GET | `/api/schedules/creator` | 크리에이터 일정 목록 |
| POST | `/api/schedules/` | 일정 등록 |
| DELETE | `/api/schedules/{scheduleId}` | 일정 삭제 |

### 채팅 (`/chat`)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/chat/room` | 채팅방 생성 |
| GET | `/chat/rooms/my` | 내 채팅방 목록 |
| GET | `/chat/room/{roomId}/messages` | 채팅 메시지 조회 |
| PUT | `/chat/room/{roomId}/name` | 채팅방 이름 변경 |

### 알림 (`/api/notifications`)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/notifications/subscribe` | SSE 알림 구독 |
