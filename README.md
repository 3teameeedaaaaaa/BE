# 🌊 Project Water (우상향) 📈
> **"주식 투자자의 불안을 확신으로, 인지왜곡을 깨뜨리는 AI 투자 심리 케어 서비스"**

본 프로젝트는 주식 투자 과정에서 발생하는 심리적 불안과 인지왜곡(Cognitive Distortion)을 AI 상담을 통해 진단하고, 객관적인 투자 판단을 돕기 위해 개발된 백엔드 시스템입니다.

---

## 🚀 Key Features (현재 구축 완료)

### 1. 지능형 데이터 모델링 (RDB Design)
* **7개 핵심 도메인 구축**: Member, Stock, Chat, Analysis, Board 등 체계적인 관계형 데이터베이스 설계 완료.
* **JPA Auditing 적용**: 모든 데이터의 생성 시간(`created_at`)을 자동 기록하여 유저의 심리 변화 추적 및 타임라인 정렬 기능 기반 마련.
* **고성능 검색 최적화**: 2,800여 개의 상장 종목 데이터 검색 성능 향상을 위해 `Stock` 테이블에 복합 인덱스(`Index`) 설정.

### 2. 확장 가능한 아키텍처 (Architecture)
* **Domain-Driven Design**: 기능별 패키지 분리를 통해 팀 협업 시 코드 충돌을 최소화하고 유지보수성 극대화.
* **CORS & 보안 환경**: 프론트엔드(Vite)와의 원활한 통신을 위한 교차 출처 자원 공유 및 세션 기반 인증 환경 조성.

---

## 🛠 Tech Stack (Back-end)

| Category | Technology |
| :--- | :--- |
| **Framework** | Spring Boot 3.5.13 |
| **Language** | Java 17 |
| **Database** | MySQL 8.0 |
| **ORM** | Spring Data JPA (Hibernate) |
| **API Doc** | Swagger / OpenAPI 3.0 |
| **Build Tool** | Gradle |

---

## 📊 Database ERD
![Database ERD](https://github.com/user-attachments/assets/485414b8-e2ec-47ce-83e4-6e1c5f8feb6a)
* *Reverse Engineering을 통해 도출된 객체 지향적 데이터 구조 (MySQL Workbench)*

---

## 📂 Directory Structure
```text
src/main/java/com/example/water/
├── domain/            # 각 기능별 도메인 (Entity, Repository, Service, Controller)
│   ├── member/        # 사용자 관리 (로그인/회원가입)
│   ├── stock/         # 주식 종목 검색 및 관리
│   ├── chat/          # 상담 세션 및 메시지 기록
│   ├── analysis/      # AI 분석 결과 및 인지왜곡 유형
│   └── board/         # 익명 커뮤니티 공간
├── global/            # 공통 설정 (BaseEntity, ErrorHandler, Config)
└── infra/             # 외부 API 연동 (Gemini AI, Stock 시세 API)
```
---

## 🏁 How to Run (Back-end)

### 1. Database Setup
```SQL
CREATE DATABASE water_db;
```

### 2. Configuration
* src/main/resources/application.yml 파일에서 MySQL 계정 정보를 수정하세요.
```yaml
spring:
  datasource:
    username: root
    password: YOUR_PASSWORD
```

### 3. Build & Run
```Bash
./gradlew bootRun
```
---