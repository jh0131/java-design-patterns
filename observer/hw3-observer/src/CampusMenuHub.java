import java.util.ArrayList;
import java.util.List;

// 1단계 Subject: 등록된 관찰자들에게 이벤트 전달
public class CampusMenuHub implements IMenuEventSubject {

    private List<IMenuEventObserver> observers = new ArrayList<>();

    @Override
    public void attach(IMenuEventObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(IMenuEventObserver observer) {
        observers.remove(observer);
    }


    // publish 통해 알림을 등록된 관찰자(list에 저장된 observers)에게 전달.
    @Override
    public void notifyObservers(MenuEvent event) {

        for (IMenuEventObserver observer : List.copyOf(observers))
        // 순회할 observer를 복사
                    {
            try {
                observer.onEvent(event);
            } catch (RuntimeException e) {
                System.out.printf("[WARN] 관찰자 처리 실패: %s — %s%n",
                        observer.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    // 메뉴 이벤트 발행
    public void publish(MenuEvent event) {
        notifyObservers(event);
    }
}
