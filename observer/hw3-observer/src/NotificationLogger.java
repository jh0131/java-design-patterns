// 허브가 발행한 이벤트를 받아 전체 이벤트 기록을 출력하는 관찰자

public class NotificationLogger implements IMenuEventObserver {

    @Override
    public void onEvent(MenuEvent event) {

        System.out.printf("[LOG] %s|%s|%s%n", event.type(), event.restaurantId(), event.message());
        // event.뒤의 메서드들은 레코드로 간소화 된 문법
    }
}
