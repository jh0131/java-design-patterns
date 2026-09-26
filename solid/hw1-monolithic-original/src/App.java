import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//  적재, 정규화, 집계, 출력이 한 곳에 섞여 있다. 
//  1) SRP 위반: 네 가지 책임이 main()에
//  2) 전역 가변 상태: public static 컬렉션
//  3) OCP 위반: 가격/형식 변경 시 이 파일 수정 필요
//  4) DIP 위반: FileInputStream 직접 의존
//  5) 테스트 불가: System.out.println() 직접 사용하여, 콘솔이 아닌 다른 출력 방식 테스트 어려움
//  HW1-SOLID는 이 프로그램과 출력이 완전히 동일해야 한다.

public class App {

    public static Map<String, Integer> restaurantMenuCount = new HashMap<>();
    public static Map<String, Integer> priceBucketCount = new HashMap<>();
    public static Map<String, Integer> gramFreq = new HashMap<>();
    public static List<String[]> rows = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String csv = "menus.csv";


        // csv파일에서 데이터를 읽어와서 , 단위로 분리한 뒤 컬렉션에 저장
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                rows.add(line.split(",", -1));
            }
        }




        for (String[] cols : rows) {
            String restaurantName = cols[1].trim().replaceAll("\\s+", " ");
            String menuName = normalizeMenuName(cols[2]);
            int price = Integer.parseInt(cols[3].trim());

            restaurantMenuCount.merge(restaurantName, 1, Integer::sum);

            String bucket;
            if (price <= 3900) bucket = "~3900";
            else if (price <= 5900) bucket = "~5900";
            else if (price <= 6900) bucket = "~6900";
            else bucket = "6900+";
            priceBucketCount.merge(bucket, 1, Integer::sum);

            for (String g : hangulBigrams(menuName)) {
                gramFreq.merge(g, 1, Integer::sum);
            }
        }

        System.out.println("== 식당별 메뉴 수 ==");
        for (var e : new TreeMap<>(restaurantMenuCount).entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }

        System.out.println("\n== 가격대 분포 ==");
        for (String b : List.of("~3900", "~5900", "~6900", "6900+")) {
            System.out.println(b + " : " + priceBucketCount.getOrDefault(b, 0));
        }

        System.out.println("\n== 메뉴명 한글 2-gram 빈도 (Top 10) ==");
        gramFreq.entrySet().stream()
                .sorted((a, b) -> {
                    int c = Integer.compare(b.getValue(), a.getValue());
                    return c != 0 ? c : a.getKey().compareTo(b.getKey());
                })
                .limit(10)
                .forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));
    }

    static String normalizeMenuName(String raw) {
        String name = raw.trim().replaceAll("\\s+", " ").toLowerCase();
        return name.replaceAll("\\(\\s*밥\\s*포함\\s*\\)", "(밥포함)");
    }

    static List<String> hangulBigrams(String menuName) {
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
}
