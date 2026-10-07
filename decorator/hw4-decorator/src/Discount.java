public class Discount extends MenuDecorator {

    private final int percent;

    public Discount(IMenu decoratedMenu, int percent) {
        super(decoratedMenu);
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("할인율은 0~100이어야 합니다.");
        }
        this.percent = percent;
    }

    @Override
    public int price() {
        // 원 단위 미만은 버리고, 곱셈은 long으로 계산한다.
        return (int) ((long) decoratedMenu.price() * (100 - percent) / 100);
    }

    @Override
    public String description() {
        return decoratedMenu.description() + ", 할인 " + percent + "%";
    }
}
