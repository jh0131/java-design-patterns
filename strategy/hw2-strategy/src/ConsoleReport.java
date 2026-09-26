import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* HW1처럼 출력 대상을 주입받고 RankingStrategy를 통해서 전략을 결정하고,
RecommendationContext가 선택된 전략을 실행해서
최종적으로 나온 메뉴의 상위5개만 출력하는 클래스
 */

public class ConsoleReport {

    private static final int TOP_COUNT = 5;
    private final PrintStream out;

    public ConsoleReport(PrintStream out) {

        this.out = Objects.requireNonNull(out, "출력 대상이 필요합니다.");
    }

    // 추천
    public void printTopRanking(String strategyName, List<MenuItem> items) {
        Objects.requireNonNull(strategyName, "전략 이름이 필요합니다.");
        Objects.requireNonNull(items, "출력할 메뉴 목록이 필요합니다.");

        out.println("\n== " + strategyName + " Top-" + TOP_COUNT + " ==");
        if (items.isEmpty()) {
            out.println("추천 조건에 맞는 메뉴가 없습니다.");
            return;
        }
        for (int i = 0; i < Math.min(TOP_COUNT, items.size()); i++) {
            MenuItem item = items.get(i);
            out.printf("%d. %s | %s | %d원%n",
                    i + 1, item.restaurantName(), item.name(), item.price());
        }
    }
}
