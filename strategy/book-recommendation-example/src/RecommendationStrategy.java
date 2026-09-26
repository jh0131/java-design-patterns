import java.util.List;

public interface RecommendationStrategy {

    String name();
    List<Book> recommend(List<Book> books);

}


