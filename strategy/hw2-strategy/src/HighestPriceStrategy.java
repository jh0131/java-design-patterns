import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// ==================== 전략 패턴: ConcreteStrategy(최고가순) ====================
// RankingStrategy를 구현하여 최고가순 알고리즘을 독립된 객체로 캡슐화한다.

public class HighestPriceStrategy implements RankingStrategy {

    @Override
    public String name() {
        return "최고가순";
    }

    @Override
    public List<MenuItem> rank(List<MenuItem> items) {
        // ==================== 알고리즘 ====================
        // 원본 보호를 위해 목록을 복사한 뒤 가격 내림차순, 메뉴명 오름차순으로 정렬한다.
        List<MenuItem> copy = new ArrayList<>(items);
        copy.sort(Comparator.comparingInt(MenuItem::price)
                .reversed()
                .thenComparing(MenuItem::name));
        // ==================== 알고리즘 ====================
        return copy;
    }
}
