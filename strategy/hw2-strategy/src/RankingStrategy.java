import java.util.List;

/* ==================== 전략 패턴: Strategy(전략 인터페이스) ====================
    교체 할 전략들의 인터페이스
*/

public interface RankingStrategy {

    String name();

    List<MenuItem> rank(List<MenuItem> items);

}
