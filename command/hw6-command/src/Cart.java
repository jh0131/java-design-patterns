import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Receiver: 장바구니의 실제 상태를 관리한다. UI에서는 Command로만 변경한다.
public class Cart {
    private final Map<String, CartLine> lines = new LinkedHashMap<>();

    public void add(String menuId, String name, int unitPrice, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("담을 수량은 1 이상이어야 합니다.");
        }
        CartLine existing = lines.get(menuId);
        if (existing != null) {
            existing.setQty(Math.addExact(existing.getQty(), qty));
        } else {
            lines.put(menuId, new CartLine(menuId, name, unitPrice, qty));
        }
    }

    public void changeQty(String menuId, int qty) {
        CartLine line = lines.get(menuId);
        if (line == null) {
            throw new IllegalArgumentException("장바구니에 없는 메뉴입니다: " + menuId);
        }
        if (qty <= 0) {
            lines.remove(menuId);
        } else {
            line.setQty(qty);
        }
    }

    public CartLine remove(String menuId) {
        return lines.remove(menuId);
    }

    public int indexOf(String menuId) {
        int index = 0;
        for (String id : lines.keySet()) {
            if (id.equals(menuId)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void restoreLineAt(int index, CartLine line) {
        if (index < 0 || index > lines.size()) {
            throw new IndexOutOfBoundsException("복구 위치가 올바르지 않습니다: " + index);
        }
        if (lines.containsKey(line.getMenuId())) {
            throw new IllegalArgumentException("이미 담긴 메뉴입니다: " + line.getMenuId());
        }
        List<CartLine> restored = snapshot();
        restored.add(index, line.copy());
        replaceAll(restored);
    }

    // 수량까지 복사해야 이후 변경이 비우기 이전 상태에 영향을 주지 않는다.
    public List<CartLine> snapshot() {
        List<CartLine> result = new ArrayList<>();
        for (CartLine line : lines.values()) {
            result.add(line.copy());
        }
        return result;
    }

    public void replaceAll(List<CartLine> snapshot) {
        Map<String, CartLine> restored = new LinkedHashMap<>();
        for (CartLine line : snapshot) {
            if (restored.putIfAbsent(line.getMenuId(), line.copy()) != null) {
                throw new IllegalArgumentException("중복 메뉴 ID입니다: " + line.getMenuId());
            }
        }
        lines.clear();
        lines.putAll(restored);
    }

    // 복사본을 반환하여 조회한 객체로 실제 장바구니를 수정하지 못하게 한다.
    public CartLine getLine(String menuId) {
        CartLine line = lines.get(menuId);
        return line == null ? null : line.copy();
    }

    public List<CartLine> getLines() {
        return Collections.unmodifiableList(snapshot());
    }

    public int total() {
        int sum = 0;
        for (CartLine line : lines.values()) {
            sum += line.lineTotal();
        }
        return sum;
    }

    public void print() {
        for (CartLine line : lines.values()) {
            System.out.println(line);
        }
        System.out.printf("총 금액: %,d원%n", total());
    }
}
