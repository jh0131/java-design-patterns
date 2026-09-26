import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/* ==================== YourCode ====================

 ==================== 전략 패턴: ConcreteStrategy(목표 가격 근접순) ====================
 RankingStrategy를 구현하여 목표 가격 근접순 알고리즘을 독립된 객체로 캡슐화한다.

*/

public class NewTargetPriceStrategy implements RankingStrategy {

    //목표 가격 설정
    private final int targetPrice;

    public NewTargetPriceStrategy(int targetPrice) {
        if (targetPrice < 0) {
            throw new IllegalArgumentException("목표 가격은 0원 이상이어야 합니다.");
        }
        this.targetPrice = targetPrice;
    }

    @Override
    public String name() {
        return "목표 가격 " + targetPrice + "원 근접순";
    }

    @Override
    public List<MenuItem> rank(List<MenuItem> items) {

        // ==================== 알고리즘 ====================
        // 목표 가격과의 차이가 작은 메뉴를 우선한다.
        // 차이가 같으면 더 저렴한 메뉴를, 가격도 같으면 메뉴명순으로 정렬한다.

        List<MenuItem> copy = new ArrayList<>(items);

        copy.sort(Comparator
                .comparingInt((MenuItem item) -> Math.abs(item.price() - targetPrice))
                .thenComparingInt(MenuItem::price)
                .thenComparing(MenuItem::name));
        // ==================== 알고리즘 ====================
        return copy;
    }
}
