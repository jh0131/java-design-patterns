import java.util.Comparator;
import java.util.List;

public class CheapestFirstStrategy implements RecommendationStrategy {


    @Override
    public String name() {
        return "Cheapest First 가격 낮은 순 ";
    }

    @Override
    public List<Book> recommend(List<Book> books) {
        return  books.stream().sorted(Comparator.comparingInt(Book::price))
                .toList();
    }
}
