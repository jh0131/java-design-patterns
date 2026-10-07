// yourcode: 가격 변경액을 계산해 인상·인하 알림을 만드는 옵저버


public class PriceChangeAlertObserver implements IMenuEventObserver {

    // 정상적으로 처리한 가격 변경 이벤트의 개수
    private int alertCount;

    // 가격 변경 이벤트를 검사한 뒤 가격 차이를 계산하고 알림을 출력한다.
    @Override
    public void onEvent(MenuEvent event) {
        
        if (!isPriceChangeEvent(event)) {
            return;
        }

        int difference = calculateDifference(event);
        String change = determineChange(difference);

        printAlert(event, change, difference);
        alertCount++;
    }

    // 가격 변경 이벤트이며 이전·새 가격 정보가 모두 있는지 확인한다.
    private boolean isPriceChangeEvent(MenuEvent event) {
        return event.type() == MenuEventType.PRICE_CHANGED
                && event.oldPrice() != null
                && event.newPrice() != null;
    }

    // 새 가격에서 이전 가격을 빼 가격 변경액을 계산한다.
    private int calculateDifference(MenuEvent event) {
        return event.newPrice() - event.oldPrice();
    }

    // 가격 변경액의 부호에 따라 인상·인하 여부를 판단한다.
    private String determineChange(int difference) {
        if (difference > 0) {
            return "인상";
        }
        if (difference < 0) {
            return "인하";
        }
        return "변동 없음";
    }

    // 식당과 가격 변경 정보를 정해진 형식으로 콘솔에 출력한다.
    private void printAlert(MenuEvent event, String change, int difference) {
        System.out.printf("[YOURCODE] %s 가격 %s: %d원 → %d원 (차이 %d원)%n",
                event.restaurantId(), change, event.oldPrice(), event.newPrice(),
                Math.abs(difference));
    }

    // 지금까지 출력한 가격 변경 알림의 개수를 반환한다.
    public int getAlertCount() {
        return alertCount;
    }
}
