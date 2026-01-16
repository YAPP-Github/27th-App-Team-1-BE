# 27th-App-Team-1-BE

## Git Convention Rule

### Branch
- `main`: 배포/릴리즈 기준 브랜치
- `develop`: 통합 개발 브랜치
- `feature/{ticket}-{short-desc}`: 기능 개발
- `fix/{ticket}-{short-desc}`: 버그 수정
- `chore/{short-desc}`: 설정/문서/리팩터링

### Commit
- 형식: `type: summary`
- 예시: `feat: 회원 가입 기능 구현`

Types:
- `feat`: 기능 추가
- `fix`: 버그 수정
- `docs`: 문서 변경
- `refactor`: 리팩터링
- `test`: 테스트 추가/수정
- `chore`: 설정/빌드/기타

## Module Structure

```
application/        - 애플리케이션 진입점(API 계층)
domain/
  domain-service/   - 영속성 접근 진입점
  domain-rdb/       - RDB persistence 모듈
  domain-redis/     - Redis persistence 모듈
clients/            - 외부 API 연동
support/            - 로깅/모니터링 등 횡단 관심사
common/             - 공통 유틸/상수/예외
```

## How to Run

### 환경별 Docker Compose 파일

프로젝트는 환경별로 다른 docker-compose 파일을 사용합니다:

- **로컬 개발**: `docker-compose.yml` - networks 없이 호스트 DB 사용
- **CI/CD DEV**: `docker-compose-dev.yml` - `backend-network` 사용
- **CI/CD LIVE**: `docker-compose-live.yml` - `backend-network` 사용

### 로컬 개발 환경

1. `.env` 파일 생성 (프로젝트 루트에)

   ```bash
   # 데이터베이스 설정 (필수)
   SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/ndgl_dev?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true
   SPRING_DATASOURCE_USERNAME=your_username
   SPRING_DATASOURCE_PASSWORD=your_password
   ```

2. 애플리케이션 실행

   ```bash
   docker compose build
   docker compose up --build
   ```

3. 애플리케이션 중지

   ```bash
   docker compose down
   ```

### CI/CD 환경

CI/CD 워크플로우에서 자동으로 환경에 맞는 docker-compose 파일을 사용합니다:

- **DEV 환경**: `develop` 브랜치 push 시 `docker-compose-dev.yml` 사용
- **LIVE 환경**: `main` 브랜치에서 수동 배포 시 `docker-compose-live.yml` 사용

각 환경은 `backend-network` 안의 도커 DB를 사용합니다.
