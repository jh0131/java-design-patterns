# Java Design Patterns

Java 프로그래밍 수업에서 작성한 SOLID 원칙 및 디자인 패턴 실습 코드 모음입니다.

## 구성

```text
solid/
├─ hw1-monolithic-original/   # 리팩터링 전 단일 클래스 구조
└─ hw1-solid-refactored/      # 역할별 클래스로 분리한 구조

strategy/
├─ hw2-strategy/              # 메뉴 추천 전략 과제
├─ cafe-discount-example/     # 카페 할인 정책 교체 실습
└─ book-recommendation-example/ # 도서 추천 전략 실습

observer/
├─ observer-example/          # Coach와 Crew를 이용한 기본 실습
└─ hw3-observer/              # 2단 Observer 체인과 구독 관리 과제

decorator/
└─ hw4-decorator/             # Swing 메뉴 주문과 옵션 데코레이터 과제
```

## 패턴 핵심

### SOLID

하나의 클래스에 섞여 있던 입력, 정규화, 집계, 출력 책임을 역할별 클래스로 분리합니다. 변경 이유가 다른 기능을 분리하여 코드의 확장성과 유지보수성을 높이는 것이 목적입니다.

### Strategy

변경 가능한 알고리즘을 공통 인터페이스 뒤로 분리합니다. Context는 구체적인 알고리즘을 직접 알지 않고, 주입된 Strategy에 작업을 위임합니다.

### Observer

Subject가 Observer를 등록하고 관리하다가 사건이 발생하면 Observer의 알림 메서드를 호출합니다. 기본 실습은 `update()`, 메뉴 이벤트 과제는 `onEvent()`를 사용합니다. Subject와 Observer는 구체 클래스 대신 인터페이스를 통해 연결됩니다.

### Decorator

공통 인터페이스를 구현하는 객체로 기존 객체를 감싸 기능을 추가합니다. 메뉴 주문에서는 가격과 설명을 확장하며, 옵션을 감싸는 순서에 따라 할인 결과가 달라집니다.

## 저장소 관리 기준

- Java 소스와 Markdown 문서만 관리합니다.
- 컴파일 결과물, IDE 설정, 환경 변수 파일은 `.gitignore`에서 제외합니다.
- CSV 및 JSONL 입력 데이터는 로컬 실습 자료이므로 저장소에 포함하지 않습니다.
- 각 과제의 세부 구조와 진행 상태는 하위 폴더의 README에서 확인할 수 있습니다.
