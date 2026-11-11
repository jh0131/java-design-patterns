public class BunsikFactory implements IMenuFactory {
    private final MenuItemLoader loader;

    public BunsikFactory(MenuItemLoader loader) {
        this.loader = loader;
    }

    @Override
    public MenuItem main() {
        return loader.get(VenueType.BUNSIK, "폭풍라면", MenuSlot.MAIN);
    }

    @Override
    public MenuItem side() {
        return loader.get(VenueType.BUNSIK, "라볶이", MenuSlot.SIDE);
    }
}
