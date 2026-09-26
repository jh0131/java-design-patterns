import java.util.Map;
import java.util.Set;

// 허브의 Observer이자 사용자 뷰에 대한 Subject
public class SubscriptionManager implements IMenuEventObserver, ISubscriptionSubject {
    // TODO: 식당 ID -> 사용자 ID -> 사용자 뷰 저장소 초기화
    private Map<String, Map<String, IMenuEventObserver>> viewsByRestaurant;

    @Override
    public void subscribe(String restaurantId, String userId, IMenuEventObserver userView) {
        // TODO: 구독 등록
        throw new UnsupportedOperationException("TODO: subscribe");
    }

    @Override
    public boolean unsubscribe(String restaurantId, String userId) {
        // TODO: 구독 해제
        throw new UnsupportedOperationException("TODO: unsubscribe");
    }

    @Override
    public void notifySubscribers(MenuEvent event) {
        // TODO: 해당 식당의 구독자 통지
        throw new UnsupportedOperationException("TODO: notifySubscribers");
    }

    @Override
    public void onEvent(MenuEvent event) {
        // TODO: 허브에서 받은 이벤트 처리
        throw new UnsupportedOperationException("TODO: onEvent");
    }

    public int subscriberCount(String restaurantId) {
        // TODO: 식당의 구독자 수 조회
        throw new UnsupportedOperationException("TODO: subscriberCount");
    }

    public Set<String> subscriptionsOf(String userId) {
        // TODO: 사용자가 구독한 식당 ID 조회
        throw new UnsupportedOperationException("TODO: subscriptionsOf");
    }
}
