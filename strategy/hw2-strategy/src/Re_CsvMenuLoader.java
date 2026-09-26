import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// HW1의 CSV 읽기를 재사용하고, 정규화된 MenuItem 목록을 반환한다.
// 재사용한 코드임을 표시하기 위해 클래스 이름 앞에 Re_를 붙였다.

public class Re_CsvMenuLoader {

    private static final int RESTAURANT_ID = 0;
    private static final int RESTAURANT = 1;
    private static final int MENU = 2;
    private static final int PRICE = 3;

    private final Path path;
    private final Re_MenuTextNormalizer normalizer = new Re_MenuTextNormalizer();

    public Re_CsvMenuLoader(Path path) {
        this.path = Objects.requireNonNull(path, "CSV 파일 경로가 필요합니다.");
    }

    public List<MenuItem> load() throws IOException {
        List<MenuItem> items = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            reader.readLine();
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;

                String[] cols = line.split(",", -1);
                if (cols.length != 4) {
                    throw new IOException(lineNumber + "번째 줄의 CSV 열 개수가 올바르지 않습니다.");
                }
                items.add(new MenuItem(
                        cols[RESTAURANT_ID].trim(),
                        normalizer.collapseSpaces(cols[RESTAURANT]),
                        normalizer.normalizeMenuName(cols[MENU]),
                        Integer.parseInt(cols[PRICE].trim())
                ));
            }
        }
        return List.copyOf(items);
    }
}
