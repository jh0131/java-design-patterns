import java.util.List;

// 1단계 Subject: 등록된 관찰자들에게 이벤트 전달
public class CampusMenuHub implements IMenuEventSubject {
    // TODO: 관찰자 저장소 초기화
    private List<IMenuEventObserver> observers;

    @Override
    public void attach(IMenuEventObserver observer) {
        // TODO: 관찰자 등록
        throw new UnsupportedOperationException("TODO: attach");
    }

    @Override
    public void detach(IMenuEventObserver observer) {
        // TODO: 관찰자 해제
        throw new UnsupportedOperationException("TODO: detach");
    }

    @Override
    public void notifyObservers(MenuEvent event) {
        // TODO: 관찰자 통지
        throw new UnsupportedOperationException("TODO: notifyObservers");
    }

    public void publish(MenuEvent event) {
        // TODO: 이벤트 발행
        throw new UnsupportedOperationException("TODO: publish");
    }
}
