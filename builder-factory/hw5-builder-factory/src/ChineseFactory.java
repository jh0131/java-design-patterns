public class ChineseFactory implements IMenuFactory {
    private final MenuItemLoader loader;

    public ChineseFactory(MenuItemLoader loader) {
        this.loader = loader;
    }

    @Override
    public MenuItem main() {
        return loader.get(VenueType.CHINESE, "짜장면", MenuSlot.MAIN);
    }

    @Override
    public MenuItem side() {
        return loader.get(VenueType.CHINESE, "미니탕수육", MenuSlot.SIDE);
    }
}
