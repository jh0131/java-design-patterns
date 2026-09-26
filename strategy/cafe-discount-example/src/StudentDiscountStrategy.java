public class StudentDiscountStrategy implements DiscountStrategy {

    @Override
    public int discount(int price) {
        return price - price / 10;
    }

    @Override
    public String name() {
        return "StudentDiscountStrategy : 학생 10% 할인";
    }
}
