# HW3 Observer 구조 틀

현재는 타입, 필드, 생성자, 메서드 선언과 TODO만 준비한 상태입니다.
미구현 메서드와 생성자의 UnsupportedOperationException은 임시 표시이며,
과제에서 요구하는 실패 처리의 구현이 아닙니다. 구현할 때 교체합니다.
App을 실행해도 아직 아무 동작을 하지 않습니다.

## 파일 역할

| 구분 | 파일 |
| --- | --- |
| 이벤트 데이터 | MenuEventType.java, MenuEvent.java |
| 파일 로딩 | MenuEventLoader.java |
| 공통 규약 | IMenuEventObserver.java, IMenuEventSubject.java, ISubscriptionSubject.java |
| 이벤트 허브 | CampusMenuHub.java |
| 식당별 구독 관리 | SubscriptionManager.java |
| 허브 관찰자 | NotificationLogger.java, FlakyAuditObserver.java |
| 사용자 뷰 | ConsoleAlertView.java, FlakyUserView.java |
| 실행 및 동작 확인 | App.java |

## 연결 구조

CampusMenuHub -> NotificationLogger / FlakyAuditObserver / SubscriptionManager

SubscriptionManager -> 해당 식당을 구독한 사용자 뷰

## 추후 준비

- 파일 로딩 단계에서 원본 events.jsonl을 이 프로젝트 루트(src와 같은 위치)에 복사합니다.
- record 사용을 위해 Java 17 이상을 권장합니다.
- yourcode는 아직 선정하거나 구현하지 않았습니다. App에 TODO만 남겼습니다.
- 최종 제출 전 Observer 추가/삭제, 실패 시 나머지 통지, yourcode 및 보고서 설명을 확인합니다.
