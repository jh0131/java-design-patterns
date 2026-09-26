import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

// ==================== 전략 패턴: ConcreteStrategy(키워드 우선) ====================
// RankingStrategy를 구현하여 키워드 우선 알고리즘을 독립된 객체로 캡슐화한다.

public class KeywordPreferenceStrategy implements RankingStrategy {

    private final List<String> keywords;

    public KeywordPreferenceStrategy(String[] keywords) {
        Objects.requireNonNull(keywords, "키워드 배열이 필요합니다.");
        this.keywords = List.of(keywords.clone());
        if (this.keywords.isEmpty()) {
            throw new IllegalArgumentException("키워드를 하나 이상 지정해야 합니다.");
        }
    }

    @Override
    public String name() {
        return "키워드 우선(" + String.join("/", keywords) + ")";
    }

    @Override
    public List<MenuItem> rank(List<MenuItem> items) {

        // ==================== 알고리즘  ====================
        // 키워드가 포함된 메뉴를 먼저 배치하고, 같은 그룹은 가격과 메뉴명순으로 정렬한다.
        List<MenuItem> copy = new ArrayList<>(items);
        copy.sort(Comparator
                .comparing((MenuItem item) -> !matches(item.name()))
                .thenComparingInt(MenuItem::price)
                .thenComparing(MenuItem::name));
        // ==================== 알고리즘 ====================
        return copy;
    }

    // 알고리즘 보조 로직: 메뉴명에 지정된 키워드가 하나라도 포함되는지 검사한다.
    private boolean matches(String menuName) {
        return keywords.stream().anyMatch(menuName::contains);
    }
}
