# Observer Pattern 실습 예제 정리

## 핵심 개념

옵저버 패턴의 핵심은 **Subject에서 사건이나 상태 변화가 발생했을 때, 등록된 여러 Observer에게 알리는 디자인 패턴**이다.

이 실습에서는 코치가 Subject이고 크루가 Observer이다.

| 옵저버 패턴 역할 | 구현 클래스/인터페이스 |
| --- | --- |
| Subject | `Coach` |
| Concrete Subject | `BettiCoach` |
| Observer | `Crew` |
| Concrete Observer | `StudentCrew` |
| 객체 생성 및 연결 | `Main` |

## 파일 구조

```text
src/
├─ Coach.java        # 구독, 구독 해제, 알림 기능 정의
├─ BettiCoach.java   # Crew 목록을 관리하고 알림 전송
├─ Crew.java         # update() 기능 정의
├─ StudentCrew.java  # 알림을 받았을 때의 동작 구현
└─ Main.java         # 객체 생성, 구독, 알림, 구독 해제 테스트
```

## 전체 실행 흐름

```text
1. Main에서 Coach와 Crew 객체를 생성한다.
2. coach.subscribe(crew)로 크루를 등록한다.
3. coach.notifyCrew(msg)로 사건을 알린다.
4. BettiCoach가 등록된 Crew 목록을 순회한다.
5. 각 Crew 객체의 update(msg)가 실행된다.
6. unsubscribe(crew)를 호출하면 이후 알림에서 제외된다.
```

중요한 알림 부분은 다음과 같다.

```java
public void notifyCrew(String msg) {
    for (Crew crew : crews) {
        crew.update(msg);
    }
}
```

## 헷갈렸던 내용

### `update()`는 누가 호출하는가?

`update()`는 크루가 스스로 호출하지 않는다. **코치가 등록된 크루들의 `update()`를 호출한다.**

- 호출을 시작하는 객체: `BettiCoach`
- 실제 메서드를 실행하는 객체: 각각의 `StudentCrew`

### 컬렉션이 필요한 이유

여러 옵저버를 동적으로 등록하고 해제하려면 Subject가 옵저버들을 기억해야 한다. Java에서는 일반적으로 컬렉션을 사용한다.

- `List`: 등록 순서를 유지하지만 같은 객체가 중복 등록될 수 있다.
- `Set`: 같은 객체의 중복 등록을 막을 수 있다.
- 배열: 사용할 수 있지만 크기 변경과 삭제가 불편하다.

컬렉션 자체가 패턴의 핵심은 아니다. 핵심은 **등록, 해제, 일괄 통지** 구조이다.

### `Coach`가 구체적인 크루를 몰라도 되는 이유

`BettiCoach`는 `StudentCrew`가 아닌 `Crew` 인터페이스에 의존한다. 따라서 새로운 `Crew` 구현 클래스가 추가되어도 코치 코드를 변경하지 않고 구독시킬 수 있다.

```java
Crew chulSu = new StudentCrew("철수");
coach.subscribe(chulSu);
```

이처럼 객체 사이의 의존성을 낮추는 것을 **느슨한 결합**이라고 한다.

## 현재 예제의 의미

현재는 `Main`이 `notifyCrew()`를 직접 호출하는 단순한 실습 구조이다. 실제 프로그램에서는 코치의 행동 메서드가 상태를 변경한 뒤 내부에서 알림을 보내는 형태가 더 자연스럽다.

```text
coach.eat()
→ 코치 상태 변경
→ notifyCrew() 호출
→ 모든 Crew의 update() 실행
```

## 정리

> Subject는 Observer 목록을 관리하고, 사건이 발생하면 구체적인 구현을 몰라도 모든 Observer의 `update()`를 호출한다.
