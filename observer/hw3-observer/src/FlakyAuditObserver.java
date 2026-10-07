
// 옵저버 하나가 실패해도 허브가 다른 옵저버에게 계속 알림을 알리는지 확인하는 클래스

public class FlakyAuditObserver implements IMenuEventObserver {

    @Override
    public void onEvent(MenuEvent event) {
        if (event.type() == MenuEventType.PRICE_CHANGED) {

            throw new IllegalStateException("감사 서버 응답 없음");
        }
    }
}
