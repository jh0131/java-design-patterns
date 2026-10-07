public abstract class MenuDecorator implements IMenu {

    protected final IMenu decoratedMenu;

    protected MenuDecorator(IMenu decoratedMenu) {
        if (decoratedMenu == null) {
            throw new IllegalArgumentException("감쌀 메뉴가 필요합니다.");
        }
        this.decoratedMenu = decoratedMenu;
    }
}
