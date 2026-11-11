import java.util.List;

public class ClearCartCommand implements ICommand {
    private final Cart cart;
    private List<CartLine> snapshot;

    public ClearCartCommand(Cart cart) {
        this.cart = cart;
    }

    @Override
    public void execute() {
        snapshot = cart.snapshot();
        cart.replaceAll(List.of());
    }

    @Override
    public void undo() {
        cart.replaceAll(snapshot);
    }

    @Override
    public String name() {
        return "ClearCart";
    }
}
