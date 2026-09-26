public record Book(String title, int price,double rating) {

}

/*
java16 이후로 도입된 레코드
데이터를 저장하는 용도의 클래스를 짧게 선언하는 Java 문법으로
매개변수로 변수 주면, 생성자 getter,setter 등 자주 쓰는 문법이 자동으로 생성됨.

public final class Book {

    private final String title;
    private final int price;
    private final double rating;

    public Book(String title, int price, double rating) {
        this.title = title;
        this.price = price;
        this.rating = rating;
    }

    public String title() {
        return title;
    }

    public int price() {
        return price;
    }

    public double rating() {
        return rating;
    }
}
 */