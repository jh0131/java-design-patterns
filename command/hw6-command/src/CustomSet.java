// --- yourCode ---
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 사용자 세트는 메뉴 ID와 수량을 보관한다. 가격은 담는 시점의 메뉴 가격을 사용한다.
public record CustomSet(String name, List<Entry> entries) {
    public CustomSet {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("세트 이름을 입력해주세요.");
        }
        name = name.trim();
        entries = List.copyOf(entries);
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("세트에 메뉴를 한 개 이상 추가해주세요.");
        }
        Set<String> ids = new HashSet<>();
        for (Entry entry : entries) {
            if (!ids.add(entry.menuId())) {
                throw new IllegalArgumentException("세트 안에 중복된 메뉴가 있습니다.");
            }
        }
    }

    public record Entry(String menuId, int qty) {
        public Entry {
            if (menuId == null || menuId.isBlank() || qty <= 0) {
                throw new IllegalArgumentException("메뉴 ID와 1 이상의 정수 수량이 필요합니다.");
            }
        }
    }

    public MacroCommand toCommand(Cart cart, List<MenuItem> menus) {
        List<ICommand> commands = new ArrayList<>();
        // 모든 메뉴를 먼저 검증한다. 없는 메뉴가 있으면 장바구니를 변경하지 않는다.
        for (Entry entry : entries) {
            MenuItem item = null;
            for (MenuItem menu : menus) {
                if (menu.menuId().equals(entry.menuId())) {
                    item = menu;
                    break;
                }
            }
            if (item == null) {
                throw new IllegalArgumentException(
                        "현재 메뉴에 없는 항목입니다. 세트를 수정해주세요: " + entry.menuId());
            }
            commands.add(new AddItemCommand(cart, item.menuId(), item.name(), item.price(), entry.qty()));
        }
        return new MacroCommand("사용자 세트: " + name, commands);
    }

    @Override
    public String toString() {
        return name + " (" + entries.size() + "종)";
    }
}
// --- yourCode 끝 ---
