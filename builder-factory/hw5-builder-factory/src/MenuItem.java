// PDF의 record를 일반 클래스로 표현한다.
public final class MenuItem {
    private final String id;
    private final String restaurantName;
    private final String name;
    private final int price;

    public MenuItem(String id, String restaurantName, String name, int price) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.name = name;
        this.price = price;
    }

    public String id() {
        return id;
    }

    public String restaurantName() {
        return restaurantName;
    }

    public String name() {
        return name;
    }

    public int price() {
        return price;
    }

    @Override
    public String toString() {
        return name + " (" + price + "원)";
    }
}
