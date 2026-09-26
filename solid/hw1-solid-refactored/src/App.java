public class App {

    //  적재, 정규화, 집계, 출력이 한 곳에 섞여 있다. (완료)
    //  1) SRP 위반: 네 가지 책임이 main()에 (완료)
    //  2) 전역 가변 상태: public static 컬렉션 (완료)
    //  3) OCP 위반: 가격/형식 변경 시 이 파일 수정 필요 (완료)
    //  4) DIP 위반: FileInputStream 직접 의존 (완료)
    //  5) 테스트 불가: System.out.println() 직접 사용하여, 콘솔이 아닌 다른 출력 방식 테스트 어려움 (완료)

    //  HW1-SOLID는 이 프로그램과 출력이 완전히 동일해야 한다.

    // 구체적인 객체 생성은 main에 모아두고 실제 실행 클래스와 main메서드가 있는 클래스를 분리.

    public static void main(String[] args) throws Exception {

        MenuDataLoader loader = new MenuLoader();
        MenuNormalizer normalizer = new MenuNormalizer();
        MenuAggregator aggregator = new MenuAggregator();
        MenuReporter reporter = new MenuReporter(System.out);

        MenuApplication app = new MenuApplication(loader, normalizer, aggregator, reporter);
        app.run("menus.csv");

    }
}
