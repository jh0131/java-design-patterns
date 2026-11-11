public class ChangeQuantityCommand implements ICommand {
    private final Cart cart;
    private final String menuId;
    private final int newQty;
    private int previousQty;
    private int removedIndex;
    private CartLine removedSnapshot;

    public ChangeQuantityCommand(Cart cart, String menuId, int newQty) {
        this.cart = cart;
        this.menuId = menuId;
        this.newQty = newQty;
    }

    @Override
    public void execute() {
        CartLine existing = cart.getLine(menuId);
        if (existing == null) {
            throw new IllegalArgumentException("장바구니에 없는 메뉴입니다: " + menuId);
        }
        previousQty = existing.getQty();
        removedIndex = cart.indexOf(menuId);
        removedSnapshot = existing.copy();
        cart.changeQty(menuId, newQty);
    }

    @Override
    public void undo() {
        if (newQty <= 0) {
            cart.restoreLineAt(removedIndex, removedSnapshot);
        } else {
            cart.changeQty(menuId, previousQty);
        }
    }

    @Override
    public String name() {
        return "ChangeQuantity(" + menuId + " -> " + newQty + ")";
    }
}
