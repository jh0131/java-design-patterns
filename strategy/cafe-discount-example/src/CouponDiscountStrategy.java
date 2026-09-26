public class CouponDiscountStrategy implements DiscountStrategy {

    @Override
    public int discount(int price) {

        return Math.max(0, price-1000);
    }

    @Override
    public String name() {
        return "CouponDiscountStrategy : 쿠폰 1000원 할인";
    }
}
