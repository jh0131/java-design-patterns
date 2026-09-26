public class MenuNormalizer {

    // SRP 원칙을 지키기 위한 정규화 로직 책임 분리
    // 메뉴 정규화 로직 코드

    public String normalizeRestaurantName(String raw) {

        return raw.trim().replaceAll("\\s+", " ");

    }

    // 마찬가지로 static 제거
    public String normalizeMenuName(String raw) {

        String name = raw.trim().replaceAll("\\s+", " ").toLowerCase();
        return name.replaceAll("\\(\\s*밥\\s*포함\\s*\\)", "(밥포함)");
    }

    public int normalizePrice(String raw) {

        return Integer.parseInt(raw.trim());
    }

}
