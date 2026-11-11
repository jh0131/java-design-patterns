# HW6: Command 장바구니

장바구니 조작을 명령 객체로 분리하고 실행 취소·다시 실행을 지원하는 Swing 프로그램이다.

| 패턴 역할 | 구현 |
| --- | --- |
| Command | `ICommand`: `execute()`, `undo()`, `name()` |
| Concrete Command | `AddItemCommand`, `ChangeQuantityCommand`, `RemoveItemCommand`, `ClearCartCommand` |
| Receiver | `Cart`: 메뉴·수량·합계와 장바구니 상태 관리 |
| Invoker | `CommandInvoker`: Undo·Redo 스택 관리 |
| Client | `MenuCartFrame`: 화면 입력을 명령 객체로 연결 |

- 담기, 수량 변경, 삭제, 전체 비우기를 명령으로 실행한다. 새 명령이 실행되면 Redo 이력은 초기화한다.
- `MacroCommand`는 여러 명령을 하나로 묶어 세트 메뉴를 담고, Undo할 때 역순으로 취소한다.
- **YourCode:** `CustomSet`, `CustomSetDialog`, `CustomSetStore`로 사용자 세트를 생성·편집·삭제하고 XML에 저장한다. 저장한 세트는 재실행 시 복원되며, 세트 담기는 하나의 매크로로 Undo·Redo한다.

실행 흐름은 **CSV 로딩 → 화면 조작 → 명령 생성 → Invoker 실행 → Cart 변경 → 화면 갱신**이다.

## 실행

JDK 17 이상에서 이 과제 폴더를 작업 디렉터리로 지정한다.

```sh
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin App
```

로컬 수업 자료의 `menus.csv`를 이 폴더에 둔다. 헤더는 `restaurantId,restaurantName,menuName,price`이다. 다른 경로의 CSV는 `java -cp bin App "경로/menus.csv"`로 지정할 수 있다.

사용자 세트는 CSV와 같은 폴더의 `custom-sets.xml`에 저장한다. CSV와 사용자 저장 데이터는 Git에 포함하지 않는다. Swing 화면은 데스크톱 환경에서 실행한다.
