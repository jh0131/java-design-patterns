# HW4: Decorator 메뉴 주문

기본 메뉴 객체를 옵션 객체로 차례대로 감싸 가격과 설명을 확장하는 Swing 주문 프로그램이다.

| 패턴 역할 | 구현 |
| --- | --- |
| Component | `IMenu`: `price()`, `description()` |
| Concrete Component | `BaseMenu`: 기본 메뉴 이름과 가격 |
| Decorator | `MenuDecorator`: 감싼 `IMenu`를 보관 |
| Concrete Decorator | `Spicy`, `ExtraLarge`, `AddOn`, `Discount`, `PointReward` |
| Client | `MenuOrderFrameTemplate`: 버튼 선택에 따라 체인 구성 |

실행 흐름은 **menus.csv 로딩 → 기본 메뉴 선택 → 버튼을 누를 때마다 현재 메뉴를 감싸기 → price()/description() 호출 → 영수증 갱신**이다.

- `Spicy`: 단계당 500원 추가, `ExtraLarge`: 1,500원 추가.
- `AddOn`: 선택한 추가 메뉴의 가격과 이름을 추가한다.
- `Discount`: 감싼 메뉴의 현재 가격에 할인율을 적용하며 원 미만은 버린다.
- `PointReward`: 결제 금액을 유지하고 1% 적립 예정 포인트를 설명에 추가한다. 10,000원 이상이면 100P를 더한다.

옵션은 클릭 순서대로 적용되고 중복 적용도 가능하다. 따라서 할인 전후에 추가 옵션을 적용하면 합계가 달라지며 화면에서 할인 먼저/나중 결과를 비교한다. 적립 안내는 최종 메뉴를 바깥에서 감싸 계산하고, 다시 눌러도 중복 적립하지 않는다.

## 실행

JDK 16 이상에서 프로젝트 폴더를 작업 디렉터리로 지정하고 실행한다.

```sh
javac -encoding UTF-8 -d bin src/*.java
java -cp bin App
```

`menus.csv`는 프로젝트 루트에 필요하다. 첫 내용 줄은 헤더이며 이후 줄은 `id,restaurantName,name,price`의 4개 항목으로 구성한다. GitHub 저장소에서는 기존 관리 기준에 따라 CSV를 제외하므로 로컬 입력 파일을 별도로 준비해야 한다.
