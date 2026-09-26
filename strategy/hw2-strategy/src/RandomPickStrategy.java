import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// ==================== 전략 패턴: ConcreteStrategy(랜덤 추천) ====================
// RankingStrategy를 구현하여 무작위 추천 알고리즘을 독립된 객체로 캡슐화한다.
public class RandomPickStrategy implements RankingStrategy {

    private final long seed;

    public RandomPickStrategy(long seed) {
        this.seed = seed;
    }

    @Override
    public String name() {
        return "랜덤 픽(seed=" + seed + ")";
    }

    @Override
    public List<MenuItem> rank(List<MenuItem> items) {
        // ==================== 알고리즘 ====================
        // 원본 보호를 위해 목록을 복사하고, 같은 seed에 같은 결과가 나오도록 섞는다.
        List<MenuItem> copy = new ArrayList<>(items);
        Collections.shuffle(copy, new Random(seed));
        // ==================== 알고리즘 ====================
        return copy;
    }
}
