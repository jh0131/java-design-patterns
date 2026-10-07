# Strategy: 카페 할인 정책

할인 계산 방법을 객체로 분리하고, 주문 객체가 사용하는 전략을 교체하는 실습이다.

- `DiscountStrategy`: `discount(price)`와 `name()`을 정의하는 공통 인터페이스.
- `NoDiscountStrategy`, `StudentDiscountStrategy`, `CouponDiscountStrategy`: 할인 없음, 학생 10% 할인, 쿠폰 1,000원 할인을 구현한다. 쿠폰 적용 금액은 0원 미만으로 내려가지 않는다.
- `CafeOrder` (Context): 전략 객체를 보관하고 가격 계산을 `strategy.discount(price)`에 위임한다.
- `App`: `setStrategy()`로 전략을 바꾸며 5,000원 주문의 결과를 확인한다.

실행 흐름은 **전략 주입 → 가격 출력 → 전략 교체 → 같은 주문의 가격 재계산**이다. 할인 정책을 추가할 때는 공통 인터페이스를 구현하는 클래스를 추가하면 된다.

현재 `App`은 인자 없는 `static void main()` 형식이다. JDK 25 이상에서 `javac -encoding UTF-8 -d bin src/*.java`로 컴파일한 뒤 `java -cp bin App`으로 실행할 수 있다. 출력 금액은 차례대로 5,000원, 4,500원, 4,000원이다.
