import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ==================== 전략 패턴: ConcreteStrategy(식당별 최저가) ====================
// RankingStrategy를 구현하여 식당별 최저가 선별 알고리즘을 독립된 객체로 캡슐화한다.

public class CheapestPerRestaurantStrategy implements RankingStrategy {

    // 알고리즘에서 최저가를 판단하고 최종 결과를 정렬할 때 공통으로 사용하는 비교 기준이다.
    private static final Comparator<MenuItem> PRICE_THEN_NAME =
            Comparator.comparingInt(MenuItem::price)
                    .thenComparing(MenuItem::name);

    @Override
    public String name() {
        return "식당별 최저가 1개";
    }

    @Override
    public List<MenuItem> rank(List<MenuItem> items) {
        // ==================== 알고리즘 ====================
        // 식당 ID를 키로 사용해 각 식당에서 가장 저렴한 메뉴 하나만 저장한다.
        Map<String, MenuItem> cheapestByRestaurant = new HashMap<>();

        for (MenuItem item : items) {
            MenuItem normalized = normalizeItem(item);
            cheapestByRestaurant.merge(
                    normalized.id(),
                    normalized,
                    (current, candidate) ->
                            PRICE_THEN_NAME.compare(current, candidate) <= 0
                                    ? current
                                    : candidate
            );
        }

        List<MenuItem> result = new ArrayList<>(cheapestByRestaurant.values());
        result.sort(PRICE_THEN_NAME);
        // ==================== 알고리즘 ====================
        return result;
    }

    // 알고리즘 보조 로직: 비교 전에 식당 ID, 식당명, 메뉴명의 공백을 정규화한다.
    // PDF 6쪽 클래스 도표에 제시된 보조 메서드이다.
    private MenuItem normalizeItem(MenuItem item) {
        return new MenuItem(
                item.id().trim(),
                item.restaurantName().trim().replaceAll("\\s+", " "),
                item.name().trim().replaceAll("\\s+", " "),
                item.price()
        );
    }
}
