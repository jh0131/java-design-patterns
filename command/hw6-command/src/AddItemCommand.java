public class AddItemCommand implements ICommand {
    private final Cart cart;
    private final String menuId;
    private final String itemName;
    private final int unitPrice;
    private final int qty;
    private boolean existedBefore;
    private int previousQty;

    public AddItemCommand(Cart cart, String menuId, String itemName, int unitPrice, int qty) {
        this.cart = cart;
        this.menuId = menuId;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }

    @Override
    public void execute() {
        CartLine existing = cart.getLine(menuId);
        existedBefore = existing != null;
        previousQty = existedBefore ? existing.getQty() : 0;
        cart.add(menuId, itemName, unitPrice, qty);
    }

    @Override
    public void undo() {
        if (existedBefore) {
            cart.changeQty(menuId, previousQty);
        } else {
            cart.remove(menuId);
        }
    }

    @Override
    public String name() {
        return "AddItem(" + itemName + " x" + qty + ")";
    }
}
