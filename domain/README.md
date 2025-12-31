# Domain Module (Parent)

## 규약
- 도메인 하위 모듈을 묶는 상위 디렉터리다.
- 실제 구현 코드는 하위 모듈에 위치한다.
- `domain-service`는 도메인에서 영속성 접근을 시작하는 진입점이다.
- `domain-rdb`, `domain-redis` 같은 모듈은 persistence adapter로서 실제 저장소 접근을 담당한다.
- `domain-service`가 필요한 persistence 모듈을 선택/조합해 사용한다.
