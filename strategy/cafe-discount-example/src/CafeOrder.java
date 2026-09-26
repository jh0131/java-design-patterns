public class CafeOrder {

    private DiscountStrategy strategy;

    public CafeOrder(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public void printPrice(int price) {

        System.out.println("원래가격 = " + price);
        System.out.println(strategy.discount(price));
        System.out.println(strategy.name());
    }
}
