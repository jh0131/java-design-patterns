import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MenuItemLoader {
    private static final Map<VenueType, Set<String>> SIDES =
            new EnumMap<>(VenueType.class);
    private static final Map<VenueType, Set<String>> EXTRAS =
            new EnumMap<>(VenueType.class);

    static {
        // PDF에 지정된 반찬과 추가 메뉴만 등록한다. 나머지는 MAIN이다.
        Set<String> gukbapSides = new HashSet<>();
        gukbapSides.add("왕만두");
        SIDES.put(VenueType.GUKBAP, gukbapSides);

        Set<String> coverSides = new HashSet<>();
        coverSides.add("고기든든");
        SIDES.put(VenueType.COVER, coverSides);

        Set<String> bunsikSides = new HashSet<>();
        bunsikSides.add("라볶이");
        SIDES.put(VenueType.BUNSIK, bunsikSides);

        Set<String> chineseSides = new HashSet<>();
        chineseSides.add("미니탕수육");
        SIDES.put(VenueType.CHINESE, chineseSides);

        Set<String> gukbapExtras = new HashSet<>();
        gukbapExtras.add("편육한접시");
        EXTRAS.put(VenueType.GUKBAP, gukbapExtras);
    }

    private final List<MenuItem> items = new ArrayList<>();

    public MenuItemLoader(Path csv) throws IOException {
        // 제공된 CSV 형식: restaurantId,restaurantName,menuName,price
        List<String> lines = Files.readAllLines(csv, StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] fields = line.split(",", -1);
            if (fields.length != 4) {
                throw new IllegalArgumentException("CSV " + (i + 1) + "행: 항목은 4개여야 합니다.");
            }
            String id = fields[0].trim();
            String restaurantName = fields[1].trim();
            String name = fields[2].trim();
            int price = Integer.parseInt(fields[3].trim());
            items.add(new MenuItem(id, restaurantName, name, price));
        }
    }

    public List<MenuItem> all() {
        return Collections.unmodifiableList(items);
    }

    public MenuItem get(VenueType venue, String menu, MenuSlot slot) {
        if (venue == null || slot == null) {
            throw new IllegalArgumentException("코너와 슬롯은 필수입니다.");
        }
        for (MenuItem item : items) {
            if (item.restaurantName().equals(venue.name) && item.name().equals(menu)) {
                if (roleOf(item) != slot) {
                    throw new IllegalArgumentException("슬롯 불일치: " + menu + " / 요청 " + slot);
                }
                return item;
            }
        }
        throw new IllegalArgumentException("메뉴 없음: " + venue.name + " / " + menu);
    }

    public static MenuSlot roleOf(MenuItem item) {
        VenueType venue = VenueType.ofRestaurant(item.restaurantName());
        return role(venue, item.name());
    }

    private static MenuSlot role(VenueType venue, String menu) {
        Set<String> sides = SIDES.get(venue);
        if (sides != null && sides.contains(menu)) {
            return MenuSlot.SIDE;
        }
        Set<String> extras = EXTRAS.get(venue);
        if (extras != null && extras.contains(menu)) {
            return MenuSlot.EXTRA;
        }
        return MenuSlot.MAIN;
    }
}
