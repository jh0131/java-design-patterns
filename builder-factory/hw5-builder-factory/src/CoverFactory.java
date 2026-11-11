public class CoverFactory implements IMenuFactory {
    private final MenuItemLoader loader;

    public CoverFactory(MenuItemLoader loader) {
        this.loader = loader;
    }

    @Override
    public MenuItem main() {
        return loader.get(VenueType.COVER, "제육덮밥", MenuSlot.MAIN);
    }

    @Override
    public MenuItem side() {
        return loader.get(VenueType.COVER, "고기든든", MenuSlot.SIDE);
    }
}
