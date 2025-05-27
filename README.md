# 구똑 - 구독을 똑똑하게 | https://www.guttok.site

## 📋 프로젝트 소개
"한눈에 보는 내 구독 서비스, 자동 알림으로 더 스마트하게!"    

이 프로젝트는 점차 다양해지는 구독 서비스와 고정 지출을 효율적이고 체계적으로 관리할 수 있도록 돕는 플랫폼입니다.   
사용자는 **구독 서비스 목록에서 선택**하거나 **직접 입력**하여, **연/월/주 단위**로 결제 주기를 설정하고 **결제 금액, 결제 수단, 결제 여부 등**을 편리하게 관리할 수 있습니다. 등록한 고정지출은 등록한 `결제 일 하루 전, 오전 09시에 리마인드 알림이 발송`되어 결제일을 놓치지 않도록 도와줍니다.   

## 👥 팀원 구성

| 이름   | 역할              | 담당 업무                                          |
| ------ | ----------------- | -------------------------------------------------- |
| 윤희은 | 프론트엔드 개발자 | 프로젝트 리딩, 기획, 디자인, 프론트엔드 개발, 배포 |
| 강주철 | 백엔드 개발자     | 기획, 디자인, 백엔드 개발, 서버 배포               |
| 오상민 | 백엔드 개발자     | 기획, 디자인, 백엔드 개발, 서버 배포               |

## 📌 주요 기능

- ### 사용자 관리
  - 회원 가입: 사용자 계정을 생성, 이메일 인증을 통해 인증이 확인 된 사용자만 가입을 허용, 비밀번호 암호화, 가입 시 default 권한 `USER`
  - 로그인, 로그아웃: Session, Spring Security 기반 인증/인가 처리
  - 알림 상태 수정: `true` ↔ `false` 상태 전환
  - 닉네임 수정: 중복되지 않는 닉네임에 대한 수정
  - 비밀번호 변경
  - 비밀번호 찾기: 인증코드를 이메일로 발송, 인증코드 일치 시 임시 세션을 발급하여 비밀번호 변경
  - 회원 탈퇴: 탈퇴 시 Soft Delete 적용
 
- ### 구독 서비스 관리
  - 구독 서비스 생성: 새로운 고정지출 추가
  - 구독 서비스 조회: 구독 서비스 리스트 응답, Like 기반 검색
  - 구독 항목 조회: 사용자가 등록한 구독 서비스, 직접 입력 항목 no-offset 기반 페이징 처리를 통한 리스트 응답
  - 구독 항목 수정: 생성한 구독 서비스의 세부정보의 단일 정보 또는 복수 정보 수정
  - 구독 항목 삭제: 삭제 시 Hard Delete 적용

- ### 결제 리마인드 이메일 발송
  - 알림을 허용한 사용자에게 구독 서비스 결제일 하루 전 오전 09시 이메일 발송

- ### 알림 정보
  - 알림 저장: 서비스 내 특정 이벤트(회원가입, 결제 리마인드)에 대한 알림 저장, default 상태 값 `UNREAD(읽지 않음)`
  - 알림 조회: 저장된 알림에 대한 리스트 조회, no-offset 기반 페이징 처리
  - 알림 상태 변경: `UNREAD` → `READ` 상태 전환, 저장된 모든 알림에 대한 벌크 업데이트 적용
  - 알림 삭제: `READ(읽음)` 상태의 알림에 대한 벌크 삭제 적용
 
- ### CI/CD
  - GitHub Actions를 통한 CI/CD 파이프라인 구축
  - main 병합 시 Docker Hub에 jar이미지 업로드
  - 배포 자동화 (예정)
 
## 🛠️ 기술 스택

- ### Backend
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white" alt="java">
  <img src="https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="spring_boot">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="gradle">
  <img src="https://img.shields.io/badge/spring_data_jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="spring_data_jpa">
  <img src="https://img.shields.io/badge/QueryDSL-3C87C9?style=for-the-badge&logo=queryDSL&logoColor=white" alt="QueryDSL">
  <img src="https://img.shields.io/badge/Spring_Security-%6DB33F.svg?style=for-the-badge&logo=SpringSecurity&logoColor=white" alt="SpringSecurity">
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white" alt="JUnit5">

- ### Database
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=Redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/H2-0107AF?style=for-the-badge&logo=H2&logoColor=white" alt="H2">

- ### DevOps
  <img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=AmazonWebServices&logoColor=white" alt="amazonwebservices">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white" alt="Docker">

- ### Tools
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white" alt="Swagger">
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white" alt="Git">
  <img src="https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=Jira&logoColor=white" alt="Jira">

## 🗺 Backend 서비스 아키텍처
![Image](https://github.com/user-attachments/assets/92c1d8f3-2d91-4a44-9495-8459b3797b52)

## 🚀 Backend 서버 실행 방법

### 1. 프로젝트 클론

```bash
git clone https://github.com/TeamGuttok/back.git
```

### 2. 프로젝트 경로 이동

```bash
cd back
```

### 3. `.env` 파일 생성
```java
// MySQL
MYSQL_URL=jdbc:mysql://localhost:포트/스키마
MYSQL_USERNAME=계정 이름
MYSQL_PASSWORD=비밀번호

// Redis
REDIS_HOST=호스트명
REDIS_PORT=포트
REDIS_PASSWORD=비밀번호

// AWS SES
ADMIN_EMAIL=이메일을 발송 할 이메일
AWS_REGION=ap-northeast-2 (지역을 서울로 지정)
AWS_ACCESS_KEY=IAM accessKey
AWS_SECRET_KEY=IAM secretKey

// docker-compose
DOCKER_MYSQL_PASSWORD=비밀번호
DOCKER_MYSQL_DATABASE=스키마
DOCKER_MYSQL_CHARSET=utf8mb4
DOCKER_MYSQL_COLLATION=utf8mb4_unicode_ci
DOCKER_REDIS_PASSWORD=비밀번호
DOCKER_TZ=Asia/Seoul (지역)

// docker hub
DOCKERHUB_USERNAME=jucheolkang
```

### 4. Docker Compose
```bash
$ docker-compose -f docker-compose-buile.yml up -d
```
해당 Docker Compose 명령을 통해 Docker Hub에 업로드 된 최신 이미지를 불러와 애플리케이션 서버를 실행할 수 있습니다.

## 📚 API 명세서
API 명세서는 Swagger를 통해 관리되며, 프로젝트 실행 후 아래 링크에서 확인할 수 있습니다.   
[API 명세서 링크](http://localhost:8080/swagger-ui/index.html)
