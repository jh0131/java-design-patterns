public class RemoveItemCommand implements ICommand {
    private final Cart cart;
    private final String menuId;
    private CartLine removed;
    private int removedIndex;

    public RemoveItemCommand(Cart cart, String menuId) {
        this.cart = cart;
        this.menuId = menuId;
    }

    @Override
    public void execute() {
        removedIndex = cart.indexOf(menuId);
        if (removedIndex < 0) {
            throw new IllegalArgumentException("장바구니에 없는 메뉴입니다: " + menuId);
        }
        removed = cart.remove(menuId);
    }

    @Override
    public void undo() {
        cart.restoreLineAt(removedIndex, removed);
    }

    @Override
    public String name() {
        return "RemoveItem(" + menuId + ")";
    }
}
