import java.util.List;
import java.util.Objects;

/* ==================== 전략 패턴: Context ====================
 구체적인 전략(Strategy)을 몰라도, 주입받은 Strategy를 통해 추천을 실행한다.
 */

public class RecommendationContext {

    // 현재 사용할 전략을 Strategy 인터페이스 타입으로 보관한다.
    private RankingStrategy strategy;

    // 생성자에서 setter를 통해 전략 주입 받음
    public RecommendationContext(RankingStrategy strategy) {
        setStrategy(strategy);
    }

    // setter로 주입 받아서 다른 전략으로 스위칭 가능
    public void setStrategy(RankingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "추천 전략이 필요합니다.");
    }

    public List<MenuItem> recommend(List<MenuItem> items) {
        Objects.requireNonNull(items, "추천할 메뉴 목록이 필요합니다.");

        // Context는 알고리즘을 직접 구현하지 않고 현재 Strategy에 처리를 위임한다.
        return Objects.requireNonNull(strategy.rank(items), "전략은 추천 결과 목록을 반환해야 합니다.");
    }
}
