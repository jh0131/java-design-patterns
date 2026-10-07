import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 과제에서 제공한 한 줄짜리 JSON 이벤트들을 읽는다.
public class MenuEventLoader {
    public static List<MenuEvent> load(String path) throws Exception {
        List<MenuEvent> events = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;

                try {
                    events.add(parse(line));
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException(lineNumber + "번째 이벤트 줄이 잘못되었습니다", e);
                }
            }
        }
        return events;
    }

    private static MenuEvent parse(String line) {
        String type = value(line, "type");
        String restaurantId = value(line, "restaurantId");
        String message = value(line, "message");
        if (type == null || restaurantId == null || message == null) {
            throw new IllegalArgumentException("필수 필드가 없습니다");
        }

        return new MenuEvent(
                MenuEventType.valueOf(type),
                restaurantId,
                value(line, "menuId"),
                integerValue(line, "oldPrice"),
                integerValue(line, "newPrice"),
                message);
    }

    private static Integer integerValue(String line, String key) {
        String found = value(line, key);
        return found == null ? null : Integer.valueOf(found);
    }

    // 과제 파일의 문자열과 정수 필드에서 값 하나를 찾는다. 없는 필드는 null이다.
    private static String value(String line, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(?:\"([^\"]*)\"|(-?\\d+))");
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) return null;
        return matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
    }
}
