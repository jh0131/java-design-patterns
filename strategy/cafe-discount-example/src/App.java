public class App {
    static void main() {

        CafeOrder order = new CafeOrder(new NoDiscountStrategy());

        order.printPrice(5000);

        order.setStrategy(new StudentDiscountStrategy());
        order.printPrice(5000);

        order.setStrategy(new CouponDiscountStrategy());
        order.printPrice(5000);
    }
}
