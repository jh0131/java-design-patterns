public class AddOn extends MenuDecorator {

    private final String name;
    private final int add;

    public AddOn(IMenu decoratedMenu, String name, int add) {

        super(decoratedMenu);

        this.name = name;
        this.add = add;
    }

    @Override
    public int price() {

        return decoratedMenu.price() + add;
    }

    @Override
    public String description() {

        return decoratedMenu.description() + ", 추가 " + name;
    }
}
