# stock-management
이 프로젝트는 재고 시스템에서 발생하는 동시성 문제를 해결하기 위해 

Redis 기반 분산 락과 모니터링 툴을 사용하여 시스템의 안정성을 확보합니다.

### [JDK 버전] 프로젝트 기술
- [Java17] Spring Boot, Spring JPA, H2 DataBase, Spring Actuator, Spring Redisson, Prometheus, Grafana

#### Library 추가 이유
- Spring Actuator: 코드 개발 후 시스템 상태 확인 및 잘못된 지점을 확인하기 위해 추가했습니다.
- Spring Redisson: 동시 주문&취소 처리 시 데이터 일관성을 유지하기 위한 분산 락 사용을 위해 추가했습니다.
- Prometheus: JVM의 Metrics를 수집하는 목적으로 Prometheus를 추가했으며, Metrics를 수집 방법은 Pull 방식입니다.
- Grafana: Container 및 Redis의 상태를 시각화하기위해 모니터링 툴을 추가했습니다.  

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
<img width="871" alt="image" src="https://github.com/user-attachments/assets/c74f6da2-7043-45ce-bd12-cc180f40815b">

### v1 데이터 흐름도
#### 주문 등록
<img width="1240" alt="image" src="https://github.com/user-attachments/assets/74c489ae-7afa-4be6-8212-5abef082d95f">

#### 주문 취소
<img width="1133" alt="image" src="https://github.com/user-attachments/assets/5edc4abb-53cf-4ecb-b000-acd8fd2f7701">

#### DB 처리 흐름
<img width="390" alt="image" src="https://github.com/user-attachments/assets/71dccdf8-3519-452d-8790-279b45f49fe8">


### v1 기능 구현 상세
#### 주문 동기화 Lock 구현
- 하나의 상품에 대해 주문과 주문 취소가 동시에 될 수 있기때문에 사용자의 접근을 제어하기 위한 Lock을 구현
- 주문 동기화 Lock이 사용(true)인 상태로 계속 유지가 되면 Thread Block의 우려가 있어 30초만 Thread 대기

#### 주문 동기화 기능 구현
- Thread 상태 확인: 주문 동기화 Lock을 통해 현재 사용중인 Thread가 있는지 판단
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

#### 주문 취소 동기화 기능 구현
- Thread 상태 확인: 주문 동기화 Lock을 통해 현재 사용중인 Thread가 있는지 판단
- 중복 확인: 현재 취소하려는 주문이 이미 취소되었는지 판단
- 주문 취소: 1개 주문에 대한 취소 처리
  * 상품.상품상태[정상], 주문.주문상태[취소] 업데이트

#### 예외 처리
- 중복 주문/취소에 대한 예외 처리
  - 중복 주문 예외: 0001
  - 중복 주문 취소 예외: 0002
- Thread Lock에 대한 예외 처리
  - Thread Lock에 대한 예외: 1001
- 유효성 검사 예외 처리
  - 빈값일 경우의 예외: 2001
- 데이터베이스 예외 처리
  - 데이터베이스 예외: 3001
- 시스템 예외 처리
  - 시스템 예외: 9999
 
#### Prometheus 보안 설정
- Spring Actuator 별개 Port 설정 및 Base Path 변경

### 추후 기능 구현 목록
- 상품 CRUD 구현
- 각종 할인 정책 구현
- 안전한 모니터링을 위해 Actuator JWT 인증 추가
  * Spring Security 추가 예정
- 회원가입& JWT인증을 통한 로그인 기능 구현
  * Spring Security 추가 예정
