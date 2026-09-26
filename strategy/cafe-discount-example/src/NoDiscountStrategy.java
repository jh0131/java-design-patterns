public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public int discount(int price) {
        return price;
    }

    @Override
    public String name() {
        return "NoDiscountStrategy : 할인 없음";
    }
}
