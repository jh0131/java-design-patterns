// 실패 상황을 확인하기 위한 감사 관찰자
public class FlakyAuditObserver implements IMenuEventObserver {
    @Override
    public void onEvent(MenuEvent event) {
        // TODO: 과제에서 지정한 실패 상황 구현
        throw new UnsupportedOperationException("TODO: onEvent");
    }
}
