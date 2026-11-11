import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MenuItemLoader {
    private final List<MenuItem> items = new ArrayList<>();

    public MenuItemLoader(Path csv) throws IOException {
        List<String> lines = Files.readAllLines(csv, StandardCharsets.UTF_8);
        if (lines.isEmpty() || !lines.get(0).replace("\uFEFF", "").trim()
                .equals("restaurantId,restaurantName,menuName,price")) {
            throw new IllegalArgumentException("메뉴 CSV 헤더가 올바르지 않습니다.");
        }
        Set<String> ids = new HashSet<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(",", -1);
            if (fields.length != 4) {
                throw new IllegalArgumentException("CSV " + (i + 1) + "행: 항목은 4개여야 합니다.");
            }
            for (int j = 0; j < fields.length; j++) {
                fields[j] = fields[j].trim();
                if (fields[j].isEmpty()) {
                    throw new IllegalArgumentException("CSV " + (i + 1) + "행: 빈 항목이 있습니다.");
                }
            }
            int price;
            try {
                price = Integer.parseInt(fields[3]);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("CSV " + (i + 1) + "행: 가격은 정수여야 합니다.", ex);
            }
            if (price < 0) {
                throw new IllegalArgumentException("CSV " + (i + 1) + "행: 가격은 음수일 수 없습니다.");
            }
            MenuItem item = new MenuItem(fields[0], fields[1], fields[2], price);
            if (!ids.add(item.menuId())) {
                throw new IllegalArgumentException("중복 메뉴 ID입니다: " + item.menuId());
            }
            items.add(item);
        }
        if (items.isEmpty()) {
            throw new IllegalArgumentException("메뉴 CSV에 메뉴가 없습니다.");
        }
    }

    public List<MenuItem> all() {
        return List.copyOf(items);
    }
}
