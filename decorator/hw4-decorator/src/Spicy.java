public class Spicy extends MenuDecorator {

    private final int level;

    public Spicy(IMenu decoratedMenu, int level) {
        super(decoratedMenu);
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("매운맛 단계는 1~3이어야 합니다.");
        }
        this.level = level;
    }

    @Override
    public int price() {
        return decoratedMenu.price() + level * 500;
    }

    @Override
    public String description() {
        return decoratedMenu.description() + ", 매운맛 " + level + "단계";
    }
}
