// 식당별 사용자 구독 관리 규약
public interface ISubscriptionSubject {
    void subscribe(String restaurantId, String userId, IMenuEventObserver userView);
    boolean unsubscribe(String restaurantId, String userId);
    void notifySubscribers(MenuEvent event);
}
