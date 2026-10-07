import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuItemLoader {

    private final List<MenuItem> items;

    public MenuItemLoader(Path csv) throws IOException {
        List<MenuItem> loadedItems = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csv, StandardCharsets.UTF_8)) {
            boolean headerSkipped = false;
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 첫 번째 내용 줄은 CSV 헤더이므로 건너뛴다.
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] columns = line.split(",", -1);
                if (columns.length != 4) {
                    throw new IOException("메뉴 CSV " + lineNumber + "번째 줄: 4개의 항목이 필요합니다.");
                }

                int price;
                try {
                    price = Integer.parseInt(columns[3].trim());
                } catch (NumberFormatException e) {
                    throw new IOException("메뉴 CSV " + lineNumber + "번째 줄: 가격은 정수여야 합니다.", e);
                }

                loadedItems.add(new MenuItem(
                        columns[0].trim(),
                        columns[1].trim(),
                        columns[2].trim(),
                        price
                ));
            }
        }

        items = Collections.unmodifiableList(loadedItems);
    }

    public List<MenuItem> all() {
        return items;
    }

    public List<MenuItem> cheapAddOns() {
        List<MenuItem> addOns = new ArrayList<>();
        for (MenuItem item : items) {
            if (item.price() <= 2500) {
                addOns.add(item);
            }
        }
        return Collections.unmodifiableList(addOns);
    }
}
