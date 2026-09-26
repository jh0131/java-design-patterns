import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// ==================== 전략 패턴: ConcreteStrategy(예산 이하 메뉴명순) ====================
// RankingStrategy를 구현하여 예산 필터링 알고리즘을 독립된 객체로 캡슐화한다.

public class BudgetThenNameStrategy implements RankingStrategy {

    private final int maxPrice;

    public BudgetThenNameStrategy(int maxPrice) {
        if (maxPrice < 0) {
            throw new IllegalArgumentException("예산은 0원 이상이어야 합니다.");
        }
        this.maxPrice = maxPrice;
    }

    @Override
    public String name() {
        return "예산 " + maxPrice + "원 이하 -> 메뉴명순";
    }

    @Override
    public List<MenuItem> rank(List<MenuItem> items) {
        // ==================== 알고리즘 ====================

        // 예산 이하의 메뉴만 선별한 뒤 메뉴명 오름차순으로 정렬한다.
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem item : items) {
            if (item.price() <= maxPrice) {
                result.add(item);
            }
        }
        result.sort(Comparator.comparing(MenuItem::name));
        // ==================== 알고리즘 ====================
        return result;
    }
}
