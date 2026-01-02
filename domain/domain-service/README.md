# Domain Service Module

## 규약
- 도메인에서 저장/조회 요청이 시작되는 진입점이다.
- persistence 모듈(RDB, Redis 등)을 필요에 따라 연결한다.
- 저장/조회 흐름을 조합하고 선택 기준을 정한다.
