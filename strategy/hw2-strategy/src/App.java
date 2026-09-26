import java.nio.file.Path;
import java.util.List;

/* 전체 객체를 조립하고 실행학 전체 실행 흐름을 관리하는 Client 클래스.
==================== 전략 패턴: Client의 전략 생성 ====================
        <전체 흐름 >
      CSV 파일 경로 설정
            ↓
      CSV에서 메뉴 목록 불러오기
            ↓
      추천 전략 객체들 생성
            ↓
      Context에 전략을 하나씩 설정
            ↓
         추천 실행
            ↓
      상위 5개 결과 출력
*/

public class App {

    public static void main(String[] args) throws Exception {

        Path path = Path.of(args.length > 0 ? args[0] : "menus.csv");
        Re_CsvMenuLoader loader = new Re_CsvMenuLoader(path);
        List<MenuItem> items = loader.load();


        //  RankingStrategy 인터페이스를 구현한 여러 알고리즘 객체를 준비한다.

        List<RankingStrategy> strategies = List.of(
                new LowestPriceStrategy(),
                new HighestPriceStrategy(),
                new CheapestPerRestaurantStrategy(),
                new KeywordPreferenceStrategy(new String[]{"덮밥", "카츠", "국밥"}),
                new BudgetThenNameStrategy(5000),
                new RandomPickStrategy(42L),

                // ==================== YOUR CODE ====================
                //yourCode 객체 생성
                new NewTargetPriceStrategy(5500)
        );


        // ==================== Context 생성 및 전략 교체 ====================
        RecommendationContext context = new RecommendationContext(strategies.getFirst());
        ConsoleReport report = new ConsoleReport(System.out);

        for (RankingStrategy strategy : strategies) {
            // Client가 원하는 전략을 선택해 Context에 주입한다.
            context.setStrategy(strategy);
            // Context는 주입된 전략에 추천 작업을 위임한다.
            List<MenuItem> rankedItems = context.recommend(items);
            report.printTopRanking(strategy.name(), rankedItems);
        }
    }
}
