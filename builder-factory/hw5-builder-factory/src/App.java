import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {
        Path csv = Paths.get("menus.csv");
        MenuItemLoader loader = new MenuItemLoader(csv);

        // 1. Factory가 코너별 대표 메뉴를 공급한다.
        Map<VenueType, IMenuFactory> factories = new EnumMap<>(VenueType.class);
        factories.put(VenueType.GUKBAP, new GukbapFactory(loader));
        factories.put(VenueType.COVER, new CoverFactory(loader));
        factories.put(VenueType.BUNSIK, new BunsikFactory(loader));
        factories.put(VenueType.CHINESE, new ChineseFactory(loader));

        System.out.println("=== 코너별 대표 메뉴 ===");
        for (VenueType venue : factories.keySet()) {
            IMenuFactory factory = factories.get(venue);
            System.out.println("[" + venue.name + "] 주메뉴: " + factory.main()
                    + ", 반찬: " + factory.side());
            if (venue == VenueType.GUKBAP) {
                System.out.println("  추가: " + factory.extra());
            }
        }

        IMenuFactory guk = factories.get(VenueType.GUKBAP);
        IMenuFactory cover = factories.get(VenueType.COVER);
        IMenuFactory bunsik = factories.get(VenueType.BUNSIK);
        IMenuFactory chinese = factories.get(VenueType.CHINESE);

        // 2. MealSet.Builder가 한 끼 세트의 규칙을 검사한다.
        MealSet lunch = set(VenueType.GUKBAP, guk.main(), guk.side(), null);
        System.out.println("\n[성공] " + lunch);

        System.out.println("\n=== 세트 생성 실패 예제 ===");
        try {
            MenuItem fakeExtra = new MenuItem("FAKE", "바비든든", "가짜 추가 메뉴", 1000);
            set(VenueType.COVER, cover.main(), cover.side(), fakeExtra);
        } catch (IllegalStateException e) {
            System.out.println("[예상 실패] " + e.getMessage());
        }
        try {
            set(VenueType.GUKBAP, guk.main(), guk.side(), guk.extra());
        } catch (IllegalStateException e) {
            System.out.println("[예상 실패] " + e.getMessage());
        }
        try {
            set(VenueType.GUKBAP, guk.main(), null, null);
        } catch (IllegalStateException e) {
            System.out.println("[예상 실패] " + e.getMessage());
        }
        try {
            set(VenueType.CHINESE, chinese.main(), guk.side(), null);
        } catch (IllegalStateException e) {
            System.out.println("[예상 실패] " + e.getMessage());
        }
        try {
            set(VenueType.GUKBAP, guk.side(), guk.side(), null);
        } catch (IllegalStateException e) {
            System.out.println("[예상 실패] " + e.getMessage());
        }

        // 3. WeekPlan.Builder가 월요일부터 금요일까지 식단을 조립한다.
        WeekPlan week = new WeekPlan.Builder()
                .on(DayOfWeek.MONDAY, lunch)
                .on(DayOfWeek.TUESDAY, set(VenueType.BUNSIK, bunsik.main(), bunsik.side(), null))
                .on(DayOfWeek.WEDNESDAY, set(VenueType.COVER, cover.main(), cover.side(), null))
                .on(DayOfWeek.THURSDAY, set(VenueType.CHINESE, chinese.main(), chinese.side(), null))
                .on(DayOfWeek.FRIDAY, set(VenueType.GUKBAP,
                        loader.get(VenueType.GUKBAP, "닭칼국수", MenuSlot.MAIN), guk.side(), null))
                .build();
        System.out.println("\n=== 주간 식단 ===");
        System.out.println(week);

        try {
            new WeekPlan.Builder().on(DayOfWeek.MONDAY, lunch).build();
        } catch (IllegalStateException e) {
            System.out.println("\n[예상 실패] " + e.getMessage());
        }

        // yourcode 시작: 설정한 예산으로 주간 식단의 생성 결과와 차액을 확인한다.
        System.out.println("\n=== yourcode: 주간 예산 제한 검사 ===");
        WeekPlan.Builder budgetBuilder = new WeekPlan.Builder();
        for (DayOfWeek day : week.getMeals().keySet()) {
            budgetBuilder.on(day, week.getMeals().get(day));
        }

        int budget = 40000;
        boolean useBudget = true; // false이면 예산을 지정하지 않는다.

        try {
            if (useBudget) {
                budgetBuilder.budget(budget);
            }
            WeekPlan withinBudget = budgetBuilder.build();
            if (useBudget) {
                int difference = budget - withinBudget.total();
                String differenceText;
                if (difference == 0) {
                    differenceText = "0원";
                } else {
                    differenceText = difference + "원 여유";
                }
                System.out.println("[성공] 예산 " + budget + "원 / 주간 총액 "
                        + withinBudget.total() + "원 / 차이 " + differenceText);
            } else {
                System.out.println("[성공] 예산 미지정 / 주간 총액 "
                        + withinBudget.total() + "원 / 비교하지 않음");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[입력 오류] " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("[실패] " + e.getMessage());
        }
        // yourcode 끝
    }

    private static MealSet set(VenueType venue, MenuItem main, MenuItem side, MenuItem extra) {
        return new MealSet.Builder()
                .venue(venue)
                .main(main)
                .side(side)
                .extra(extra)
                .build();
    }
}
