# HW5: Builder · Factory 주간 식단

코너별 Factory가 메뉴를 제공하고, Builder가 한 끼 세트와 월~금 주간 식단을 조립하는 콘솔 프로그램이다. 두 패턴을 함께 사용하는 과제이므로 한 프로젝트로 구성했다.

| 패턴 역할 | 구현 |
| --- | --- |
| Factory 인터페이스 | `IMenuFactory`: 주메뉴·반찬·추가 메뉴 제공 |
| Concrete Factory | `GukbapFactory`, `CoverFactory`, `BunsikFactory`, `ChineseFactory` |
| Builder / Product | `MealSet.Builder` / `MealSet`, `WeekPlan.Builder` / `WeekPlan` |
| Client | `App`: Factory에서 메뉴를 받아 Builder로 식단 구성 |

- `MealSet.Builder`는 필수 구성, 메뉴 슬롯과 코너 일치 여부, 바비든든 추가 메뉴 금지, 국밥 세트 8,000원 상한을 검사한다. 주메뉴와 반찬을 함께 고르면 500원을 할인한다.
- `WeekPlan.Builder`는 월~금 식단이 모두 있는지 확인한다.
- **YourCode:** `budget()`으로 주간 예산을 지정하고, 초과하면 식단 생성을 거부한다. 예산을 지정하지 않으면 금액 제한 없이 생성한다.

실행 흐름은 **CSV 로딩 → 코너별 Factory 선택 → 한 끼 세트 조립 → 주간 식단 조립 → 총액·예산 검사**이다. `App`에서 정상 생성과 규칙 위반 예제를 함께 실행한다.

## 실행

JDK 17 이상에서 이 과제 폴더를 작업 디렉터리로 지정한다.

```sh
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin App
```

로컬 수업 자료의 `menus.csv`를 이 폴더에 둔다. 헤더는 `restaurantId,restaurantName,menuName,price`이며, Factory와 `App`에서 사용하는 메뉴 이름이 포함되어 있어야 한다. CSV는 저장소 관리 기준에 따라 제외한다.
