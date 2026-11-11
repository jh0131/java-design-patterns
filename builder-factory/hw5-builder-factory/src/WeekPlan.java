import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class WeekPlan {

    private final Map<DayOfWeek, MealSet> meals;
    private static final List<DayOfWeek> DAYS = Arrays.asList(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

    private WeekPlan(Builder builder) {
        // 빌더의 Map을 복사하여 빌더 수정이 완성된 식단에 영향을 주지 않게 한다.
        this.meals = Collections.unmodifiableMap(new EnumMap<>(builder.meals));
    }

    public Map<DayOfWeek, MealSet> getMeals() {
        return meals;
    }

    public int total() {
        int sum = 0;
        for (MealSet meal : meals.values()) {
            sum += meal.total();
        }
        return sum;
    }

    static String toKorean(DayOfWeek day) {
        switch (day) {
            case MONDAY: return "월";
            case TUESDAY: return "화";
            case WEDNESDAY: return "수";
            case THURSDAY: return "목";
            case FRIDAY: return "금";
            default: return day.name();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (DayOfWeek day : DAYS) {
            result.append(toKorean(day)).append(": ").append(meals.get(day)).append("\n");
        }
        result.append("주간 총액: ").append(total()).append("원");
        return result.toString();
    }

    public static class Builder {
        private final Map<DayOfWeek, MealSet> meals = new EnumMap<>(DayOfWeek.class);

        // yourcode 시작: -1은 예산을 지정하지 않았다는 뜻이다.
        private int budget = -1;

        public Builder budget(int budget) {
            if (budget < 0) {
                throw new IllegalArgumentException("예산은 0원 이상이어야 합니다.");
            }
            this.budget = budget;
            return this;
        }
        // yourcode 끝

        public Builder on(DayOfWeek day, MealSet meal) {
            if (day == null || !DAYS.contains(day)) {
                throw new IllegalArgumentException("요일은 월요일부터 금요일까지 지정해야 합니다.");
            }
            if (meal == null) {
                throw new IllegalArgumentException("한 끼 세트는 null일 수 없습니다.");
            }
            meals.put(day, meal);
            return this;
        }

        public WeekPlan build() {
            String missing = "";
            for (DayOfWeek day : DAYS) {
                if (!meals.containsKey(day)) {
                    missing += toKorean(day) + " ";
                }
            }
            if (!missing.isEmpty()) {
                throw new IllegalStateException("요일 누락: " + missing.trim());
            }
            // yourcode 시작: 요일 검사 후, 주간 총액이 예산 이내인지 검사한다.
            WeekPlan result = new WeekPlan(this);
            if (budget != -1 && result.total() > budget) {
                throw new IllegalStateException("주간 예산 초과: 총액 " + result.total()
                        + "원 / 예산 " + budget + "원 / 초과 "
                        + (result.total() - budget) + "원");
            }
            return result;
            // yourcode 끝
        }
    }
}
