public class Main {

    public static void main(String[] args) {

        Coach coach = new BettiCoach();

        Crew chulSu = new StudentCrew("철수");
        Crew youngHee = new StudentCrew("영희");

        coach.subscribe(chulSu);
        coach.subscribe(youngHee);


        coach.notifyCrew("코치가 밥을 먹습니다");

        coach.unsubscribe(chulSu);

        coach.notifyCrew("코치가 공부합니다");


    }
}