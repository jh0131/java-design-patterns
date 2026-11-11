public class GukbapFactory implements IMenuFactory {
    private final MenuItemLoader loader;

    public GukbapFactory(MenuItemLoader loader) {
        this.loader = loader;
    }

    @Override
    public MenuItem main() {
        return loader.get(VenueType.GUKBAP, "닭곰탕(밥 포함)", MenuSlot.MAIN);
    }

    @Override
    public MenuItem side() {
        return loader.get(VenueType.GUKBAP, "왕만두", MenuSlot.SIDE);
    }

    @Override
    public MenuItem extra() {
        return loader.get(VenueType.GUKBAP, "편육한접시", MenuSlot.EXTRA);
    }
}
