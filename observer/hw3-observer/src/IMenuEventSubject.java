// 허브의 관찰자 등록, 해제, 통지 규약

public interface IMenuEventSubject {

    void attach(IMenuEventObserver observer);

    void detach(IMenuEventObserver observer);

    void notifyObservers(MenuEvent event);
}
