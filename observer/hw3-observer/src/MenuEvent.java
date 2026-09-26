// 전달할 이벤트 데이터의 구조
public record MenuEvent(
        MenuEventType type,
        String restaurantId,
        String menuId,
        Integer oldPrice,
        Integer newPrice,
        String message
) {
}
