import java.util.ArrayList;
import java.util.List;

// 핵심은 코치 한 명이 자신을 구독한 여러 크루에게 일괄적으로 알림을 보냄 -> observer 패턴

public class BettiCoach implements Coach {

    List<Crew> crews = new ArrayList<>();


    @Override
    public void subscribe(Crew crew) {
        crews.add(crew);
    }

    @Override
    public void unsubscribe(Crew crew) {
        crews.remove(crew);
    }

    @Override
    public void notifyCrew(String msg) {
        for (Crew crew : crews) {
            crew.update(msg);
        }
    }
}
