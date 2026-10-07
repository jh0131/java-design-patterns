import java.util.ArrayList;
import java.util.List;

// 실행 진입점

public class App {
    public static void main(String[] args) throws Exception {
        List<MenuEvent> events = MenuEventLoader.load("events.jsonl");

        CampusMenuHub campusMenuHub = new CampusMenuHub();
        NotificationLogger notificationLogger = new NotificationLogger();
        FlakyAuditObserver flakyAuditObserver = new FlakyAuditObserver();
        SubscriptionManager subscriptions = new SubscriptionManager();
        PriceChangeAlertObserver priceAlert = new PriceChangeAlertObserver(); // yourcode

        campusMenuHub.attach(notificationLogger);
        campusMenuHub.attach(flakyAuditObserver);
        campusMenuHub.attach(subscriptions);
        campusMenuHub.attach(priceAlert); // Observer 추가

        ConsoleAlertView alice = new ConsoleAlertView("alice");
        ConsoleAlertView bob = new ConsoleAlertView("bob");
        subscriptions.subscribe("R01", "alice", alice);
        subscriptions.subscribe("R06", "alice", alice);
        subscriptions.subscribe("R09", "bob", bob);
        subscriptions.subscribe("R03", "bob", bob);

        // 파일에서 읽은 이벤트를 하나씩 발행한다.
        for (MenuEvent event : events) {
            campusMenuHub.publish(event);
            Thread.sleep(500);
        }

        int alertsBeforeDetach = priceAlert.getAlertCount();
        System.out.println("[YOURCODE] 가격 분석 횟수: " + alertsBeforeDetach);
        campusMenuHub.detach(priceAlert); // Observer 삭제
        campusMenuHub.publish(new MenuEvent(
                MenuEventType.PRICE_CHANGED, "R09", "53", 4000, 4300, "폭풍라면 가격 재인상"));
        if (priceAlert.getAlertCount() != alertsBeforeDetach) {
            throw new IllegalStateException("삭제한 Observer가 이벤트를 받았습니다");
        }
        System.out.println("[YOURCODE] Observer 삭제 후 가격 분석 횟수: " + priceAlert.getAlertCount());

        // 한 사용자 뷰가 실패해도 다른 구독자가 알림을 받는지 확인한다.
        List<String> got = new ArrayList<>();
        subscriptions.subscribe("R01", "carol", e -> got.add("carol"));
        subscriptions.subscribe("R01", "dave", new FlakyUserView("dave"));
        subscriptions.subscribe("R01", "erin", e -> got.add("erin"));

        campusMenuHub.publish(new MenuEvent(
                MenuEventType.SOLD_OUT, "R01", "12", null, null, "돈까스 품절"));

        if (!got.contains("carol") || !got.contains("erin")) {
            throw new IllegalStateException("정상 사용자에게 알림이 전달되지 않았습니다");
        }
        System.out.println("[CHECK] 정상 수신: " + got);
        System.out.println("[CHECK] R01 구독자 수: " + subscriptions.subscriberCount("R01"));
        subscriptions.unsubscribe("R01", "dave");
        System.out.println("[CHECK] dave 구독 해제 후 R01 구독자 수: "
                + subscriptions.subscriberCount("R01"));
        System.out.println("[CHECK] alice가 구독한 식당: " + subscriptions.subscriptionsOf("alice"));
    }
}
