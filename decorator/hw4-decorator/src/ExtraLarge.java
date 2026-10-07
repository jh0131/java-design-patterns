public class ExtraLarge extends MenuDecorator {

    public ExtraLarge(IMenu decoratedMenu) {
        super(decoratedMenu);
    }

    @Override
    public int price() {
        return decoratedMenu.price() + 1500;
    }

    @Override
    public String description() {
        return decoratedMenu.description() + ", 곱빼기";
    }
}
