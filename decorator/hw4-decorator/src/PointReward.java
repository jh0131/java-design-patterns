/* yourCode: 가격은 유지하고, 총 주문 금액의 1%와 주문 금액 10000원 이상시 bonus 100포인트를 설명에 추가한다.
 영수증에 적립 예정 포인트만 안내한다.
*/

public class PointReward extends MenuDecorator {

    private static final int BONUS_MIN_PRICE = 10000;
    private static final int BONUS_POINTS = 100;

    public PointReward(IMenu decoratedMenu) {
        super(decoratedMenu);
    }

    // 포인트는 결제 금액에서 차감하지 않으므로 안쪽 메뉴의 가격을 그대로 반환한다.

    @Override
    public int price() {
        return decoratedMenu.price();
    }

    /*
    1% 기본 적립포인트에 10,000원 이상 주문의 보너스 100P를 더한다.
     정수 나눗셈으로 1P 미만을 버리고, 메서드를 호출할 때의 현재 가격을 기준으로 계산.
    */
    public int rewardPoints() {
        int total = decoratedMenu.price();
        int points = total / 100;
        if (total >= BONUS_MIN_PRICE) {
            points += BONUS_POINTS;
        }
        return points;
    }

    // 기존 메뉴 설명 뒤에 적립 조건과 적립 예정 포인트를 붙인다.
    @Override
    public String description() {
        return decoratedMenu.description()
                + ", 적립 예정 " + rewardPoints() + "P (1% 적립, 10,000원 이상 보너스 100P)";
    }
}
