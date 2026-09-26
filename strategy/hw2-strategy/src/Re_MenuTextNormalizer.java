import java.util.Objects;

// HW1의 MenuNormalizer에서 사용하던 문자열 정규화 로직을 옮겼다.
// 재사용한 코드임을 표시하기 위해 클래스 이름 앞에 Re_를 붙였다.

public class Re_MenuTextNormalizer {

    public String collapseSpaces(String raw) {
        Objects.requireNonNull(raw, "정규화할 문자열이 필요합니다.");
        return raw.trim().replaceAll("\\s+", " ");
    }

    public String normalizeMenuName(String raw) {
        String name = collapseSpaces(raw).toLowerCase();
        return name.replaceAll("\\(\\s*밥\\s*포함\\s*\\)", "(밥포함)");
    }
}
