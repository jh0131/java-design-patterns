import java.util.Objects;

/* CSV 한 행의 메뉴 정보를 하나의 객체로 묶어 전달하기 위한 데이터 객체
레코드는 자바 16이후로 지원되는 문법으로, getter setter 생성자등 자주 쓰는 문법을 간소화 시켜줌
코드 흐름 : menus.csv 한 줄 → MenuItem 객체 → 추천 전략에서 정렬/필터링 → 화면 출력
 */

public record MenuItem(String id, String restaurantName, String name, int price) {

    public MenuItem {
        id = Objects.requireNonNull(id, "식당 ID가 필요합니다.");
        restaurantName = Objects.requireNonNull(restaurantName, "식당 이름이 필요합니다.");
        name = Objects.requireNonNull(name, "메뉴 이름이 필요합니다.");
        if (price < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }
}
