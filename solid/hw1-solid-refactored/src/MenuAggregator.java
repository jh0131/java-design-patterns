import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// SRP 원칙을 지키기 위한 집계 코드 책임 분리

public class MenuAggregator {


    // 집계 상태를 private 인스턴스 필드로 관리하여 전역 가변 상태를 제거한다.

    private final Map<String, Integer> restaurantMenuCount = new HashMap<>();
    private final Map<String, Integer> priceBucketCount = new HashMap<>();
    private final Map<String, Integer> gramFreq = new HashMap<>();


    public void aggregate(String restaurantName, String menuName, int price) {

        restaurantMenuCount.merge(restaurantName, 1, Integer::sum);

        PriceBucket bucket = PriceBucket.from(price);
        String label = bucket.getLabel();
        priceBucketCount.merge(label, 1, Integer::sum);

        for (String g : hangulBigrams(menuName)) {
            gramFreq.merge(g, 1, Integer::sum);
        }
    }


    // 클래스 내부에서만 사용하기 때문에 private로 닫음. (캡슐화)

    private List<String> hangulBigrams(String menuName) {

        String base = menuName.replaceAll("\\([^)]*\\)", "");
        StringBuilder hangul = new StringBuilder();

        for (int i = 0; i < base.length(); i++) {
            char ch = base.charAt(i);
            if (ch >= '가' && ch <= '힣') {
                hangul.append(ch);
            }
        }

        List<String> grams = new ArrayList<>();
        for (int i = 0; i < hangul.length() - 1; i++) {
            grams.add(hangul.substring(i, i + 2));
        }
        return grams;
    }

    public Map<String, Integer> getRestaurantMenuCount() {
        return restaurantMenuCount;
    }

    public Map<String, Integer> getPriceBucketCount() {
        return priceBucketCount;
    }

    public Map<String, Integer> getGramFreq() {
        return gramFreq;
    }
}
