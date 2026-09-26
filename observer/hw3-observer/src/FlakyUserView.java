// 사용자 뷰 실패 상황을 확인하기 위한 관찰자
public class FlakyUserView implements IMenuEventObserver {
    private final String userId;

    public FlakyUserView(String userId) {
        // TODO: 사용자 정보 초기화
        throw new UnsupportedOperationException("TODO: constructor");
    }

    @Override
    public void onEvent(MenuEvent event) {
        // TODO: 과제에서 지정한 실패 상황 구현
        throw new UnsupportedOperationException("TODO: onEvent");
    }
}
