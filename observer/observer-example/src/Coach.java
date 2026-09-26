// Coach = Subject (데이터가 변하는 객체)

public interface Coach {

    void subscribe(Crew crew);

    void unsubscribe(Crew crew);

    void notifyCrew(String msg);

}
