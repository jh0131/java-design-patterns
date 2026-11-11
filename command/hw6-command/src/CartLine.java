public final class CartLine {
    private final String menuId;
    private final String name;
    private final int unitPrice;
    private int qty;

    public CartLine(String menuId, String name, int unitPrice, int qty) {
        if (menuId == null || menuId.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("메뉴 ID와 메뉴명은 필수입니다.");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("단가는 음수일 수 없습니다.");
        }
        this.menuId = menuId;
        this.name = name;
        this.unitPrice = unitPrice;
        setQty(qty);
    }

    public String getMenuId() { return menuId; }
    public String getName() { return name; }
    public int getUnitPrice() { return unitPrice; }
    public int getQty() { return qty; }

    public void setQty(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        this.qty = qty;
    }

    public int lineTotal() {
        return unitPrice * qty;
    }

    CartLine copy() {
        return new CartLine(menuId, name, unitPrice, qty);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %,d원 x %d = %,d원",
                menuId, name, unitPrice, qty, lineTotal());
    }
}
