public record MenuItem(String restaurantId, String restaurantName, String name, int price) {
    // CSV의 restaurantId는 메뉴마다 반복되므로 메뉴명을 합쳐 고유 ID를 만든다.
    public String menuId() {
        return restaurantId + "-" + name;
    }
}
