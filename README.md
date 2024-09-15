# stock-management
이 프로젝트는 재고 시스템에서 발생하는 동시성 문제를 해결하기 위해 Redis 기반 분산 락을 사용합니다.

### [JDK 버전] 프로젝트 기술
- [Java17] Spring Boot, Spring JPA, H2 DataBase, Spring Redisson, Spring Doc

#### Library 추가 이유
- Spring Redisson: 동시 주문시 데이터 일관성을 유지하기 위한 분산 락 사용을 위해 추가했습니다.
- Spring Doc: API 명세를 확인하기 위해 추가했습니다.
(http://localhost:8080/swagger-ui/index.html#)

### 코드 스타일
#### 1. 함수(또는 메서드)의 길이가 15라인을 넘어가지 않도록 구현한다

#### 2. else 예약어를 쓰지 않는다.

#### 3. switch/case로 구현을 허용하지 않는다.

#### 4. 3항 연산자를 사용하지 않는다.

#### 5. indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다. 2까지만 허용한다.

#### 6. 메서드, 변수명은 최대한 간소하게 작성한다.

### 커밋 컨벤션
- **head 형식**: `타입: 제목`
- **body 형식**: 해당 내용에 대해 간결하게 설명

#### 커밋 타입
- **feat**: 새로운 기능 추가
- **fix**: 버그 및 코드 수정
- **docs**: 문서 추가/수정
- **test**: 테스트 코드 작성
- **refactor**: 리팩토링 작업
- **remove**: 불필요한 파일 삭제


### v1 엔티티 흐름도
<img width="755" alt="image" src="https://github.com/user-attachments/assets/a6a7dce8-1582-4af2-b2cd-1a94615e2fc6">

### v1 데이터 흐름도
#### 주문 등록
<img width="1291" alt="image" src="https://github.com/user-attachments/assets/560ffecb-034f-4c93-84cd-2b6be87f60ef">


### v1 기능 구현 상세
#### 동기화 Lock 구현
- 주문 동기화 Lock이 사용인 상태로 계속 유지가 되면 Thread Block의 우려가 있어 30초만 Thread 대기

#### 주문 동기화 기능 구현
- 중복 확인: 현재 사용중인 Thread가 없다면 주문의 UUID를 통해 등록된 주문이 있는지 판단
- 주문 생성: 1개의 주문을 생성
  * 주문.주문상태[진행중]
- 주문 수량 생성: 1개 주문에 대한 주문 상품들의 처리
  * 재고 수량 - 주문 상품 수량 > 0
    * 재고 수량 - 주문 상품 수량 == 0: 상품.상품상태[재고부족], 상품.재고수량[-수량] 업데이트 / 주문 수량.출고상태[출고가능], 주문 수량.주문상품수량[+수량] 등록
    * else: 상품.재고수량[-수량] 업데이트 / 주문 수량.출고상태[출고가능], 주문 수량.주문상품수량[+수량] 등록
  * 재고 수량 - 주문 상품 수량 <= 0: 주문 수량.출고상태[출고불가] 등록
- 주문 상태 업데이트: 2가지로 분기해서 처리
  * 모든 주문 수량 상태가 출고 불가한 경우: 주문.주문상태[취소] 업데이트
  * 주문 수량 상태 중 하나라도 출고 가능일 경우: 주문.총주문금액[총금액] 업데이트

#### 예외 처리
- 중복 주문/취소에 대한 예외 처리
- 시스템 예외 처리

### 추후 기능 구현 목록
- 상품 CRUD 구현
- 각종 할인 정책 구현
- 모니터링을 위한 Prometheus & Grafana 적용
- 안전한 모니터링을 위해 Actuator JWT 인증 추가
  * Spring Security 추가 예정
- 회원가입& JWT인증을 통한 로그인 기능 구현
  * Spring Security 추가 예정
- 주문 취소 기능 구현
- 엑셀 주문 등록 구현