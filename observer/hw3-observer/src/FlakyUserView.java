// 사용자 뷰 실패 상황을 확인하기 위한 관찰자
public class FlakyUserView implements IMenuEventObserver {
    private final String userId;

    public FlakyUserView(String userId) {
        this.userId = userId;
    }

    @Override
    public void onEvent(MenuEvent event) {
        throw new IllegalStateException(userId + " 단말 연결 끊김");
    }
}
