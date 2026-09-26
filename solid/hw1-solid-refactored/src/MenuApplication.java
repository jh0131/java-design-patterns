import java.util.List;

// 내가 추가한 코드(yourcode)
public class MenuApplication {

    // 파일 적재는 MenuDataLoader 인터페이스에 의존하고,
    // 나머지 협력 객체도 직접 생성하지 않고 생성자로 주입받는다. (DIP 원칙)

    private final MenuDataLoader loader;
    private final MenuNormalizer normalizer;
    private final MenuAggregator aggregator;
    private final MenuReporter reporter;

    public MenuApplication(MenuDataLoader loader,
                           MenuNormalizer normalizer,
                           MenuAggregator aggregator,
                           MenuReporter reporter) {
        this.loader = loader;
        this.normalizer = normalizer;
        this.aggregator = aggregator;
        this.reporter = reporter;
    }

    public void run(String csv) throws Exception {

        List<String[]> rows = loader.load(csv);

        for (String[] cols : rows) {
            String restaurantName = normalizer.normalizeRestaurantName(cols[1]);
            String menuName = normalizer.normalizeMenuName(cols[2]);
            int price = normalizer.normalizePrice(cols[3]);

            aggregator.aggregate(restaurantName, menuName, price);
        }

        reporter.report(aggregator);
    }
}
