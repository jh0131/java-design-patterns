public record BaseMenu(String name, int price) implements IMenu {

    @Override
    public String description() {
        return name;
    }
}
