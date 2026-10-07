public record MenuItem(
        String id,
        String restaurantName,
        String name,
        int price
) {

    public String label() {
        return restaurantName + " / " + name + " (" + price + "원)";
    }

    public BaseMenu asBase() {
        return new BaseMenu(name, price);
    }

    @Override
    public String toString() {
        return label();
    }
}
