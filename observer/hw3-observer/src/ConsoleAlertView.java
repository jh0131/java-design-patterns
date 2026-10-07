// 사용자별 콘솔 알림 뷰

public class ConsoleAlertView implements IMenuEventObserver {

    private final String userId;

    public ConsoleAlertView(String userId) {
        this.userId = userId;
    }

    @Override
    public void onEvent(MenuEvent event) {
        System.out.printf("[ALERT:%s] (%s) %s — %s%n",
                userId, event.type(), event.restaurantId(), event.message());
    }
}
