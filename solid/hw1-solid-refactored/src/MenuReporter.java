import java.io.PrintStream;
import java.util.TreeMap;


// SRP 원칙을 지키기 위한 출력 부분 책임 분리

public class MenuReporter {

    private final PrintStream out;

    public MenuReporter(PrintStream out) {
        this.out = out;
    }


    public void report(MenuAggregator aggregator) {

        out.println("== 식당별 메뉴 수 ==");

        for (var e : new TreeMap<>(aggregator.getRestaurantMenuCount()).entrySet()) {
           out.println(e.getKey() + " : " + e.getValue());
        }

        out.println("\n== 가격대 분포 ==");

        for (PriceBucket bucket : PriceBucket.values()) {
            String label = bucket.getLabel();

            out.println(label + " : "
                    + aggregator.getPriceBucketCount().getOrDefault(label, 0));
        }

        out.println("\n== 메뉴명 한글 2-gram 빈도 (Top 10) ==");

        aggregator.getGramFreq().entrySet().stream().sorted((a, b) -> {

            int c = Integer.compare(b.getValue(), a.getValue());
                    return c != 0 ? c : a.getKey().compareTo(b.getKey());
                }).limit(10).forEach(e -> out.println(e.getKey() + " : " + e.getValue()));
        }
}
