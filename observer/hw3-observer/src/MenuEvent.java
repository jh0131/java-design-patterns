// 발생한 이벤트 한 건의 정보를 담는 데이터 객체(레코드)

public record MenuEvent(

        MenuEventType type,
        String restaurantId,
        String menuId,
        Integer oldPrice,
        Integer newPrice,
        String message

) {
}
