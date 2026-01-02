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
