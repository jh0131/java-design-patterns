import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

// 허브의 Observer이자 사용자 뷰에 대한 Subject
public class SubscriptionManager implements IMenuEventObserver, ISubscriptionSubject {
    // 식당 ID -> 사용자 ID -> 사용자 뷰
    private Map<String, Map<String, IMenuEventObserver>> viewsByRestaurant = new LinkedHashMap<>();

    @Override
    public void subscribe(String restaurantId, String userId, IMenuEventObserver userView) {
        if (restaurantId == null || userId == null || userView == null) return;

        Map<String, IMenuEventObserver> views = viewsByRestaurant.get(restaurantId);
        if (views == null) {
            views = new LinkedHashMap<>();
            viewsByRestaurant.put(restaurantId, views);
        }
        views.put(userId, userView); // 같은 사용자 ID로 다시 구독하면 뷰를 교체한다.
    }

    @Override
    public boolean unsubscribe(String restaurantId, String userId) {
        Map<String, IMenuEventObserver> views = viewsByRestaurant.get(restaurantId);
        if (views == null || userId == null) return false;

        boolean removed = views.remove(userId) != null;
        if (views.isEmpty()) viewsByRestaurant.remove(restaurantId);
        return removed;
    }

    @Override
    public void notifySubscribers(MenuEvent event) {
        Map<String, IMenuEventObserver> views = viewsByRestaurant.get(event.restaurantId());
        if (views == null) return;

        // 통지 중 구독자가 추가·삭제되어도 이번 순회가 깨지지 않도록 복사한다.
        for (IMenuEventObserver view : new ArrayList<>(views.values())) {
            try {
                view.onEvent(event);
            } catch (RuntimeException e) {
                System.out.printf("[WARN] 구독라우터[%s] → %s 통지 실패: %s%n",
                        event.restaurantId(), view.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    @Override
    public void onEvent(MenuEvent event) {
        notifySubscribers(event);
    }

    public int subscriberCount(String restaurantId) {
        Map<String, IMenuEventObserver> views = viewsByRestaurant.get(restaurantId);
        return views == null ? 0 : views.size();
    }

    public Set<String> subscriptionsOf(String userId) {
        Set<String> restaurantIds = new LinkedHashSet<>();
        if (userId == null) return restaurantIds;

        for (Map.Entry<String, Map<String, IMenuEventObserver>> entry : viewsByRestaurant.entrySet()) {
            if (entry.getValue().containsKey(userId)) restaurantIds.add(entry.getKey());
        }
        return restaurantIds;
    }
}
