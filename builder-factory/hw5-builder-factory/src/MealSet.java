public class MealSet {
    private final MenuItem main;
    private final MenuItem side;
    private final MenuItem extra;
    private final VenueType venue;
    private static final int DISCOUNT = 500;

    private MealSet(Builder builder) {
        this.main = builder.main;
        this.side = builder.side;
        this.extra = builder.extra;
        this.venue = builder.venue;
    }

    public int subtotal() {
        return priceOf(main) + priceOf(side) + priceOf(extra);
    }

    public int discount() {
        if (main != null && side != null) {
            return DISCOUNT;
        }
        return 0;
    }

    public int total() {
        return subtotal() - discount();
    }

    private static int priceOf(MenuItem item) {
        if (item == null) {
            return 0;
        }
        return item.price();
    }

    private static String nameOf(MenuItem item) {
        if (item == null) {
            return "없음";
        }
        return item.name();
    }

    @Override
    public String toString() {
        String result = "[" + venue.name + "] 주메뉴: " + nameOf(main)
                + ", 반찬: " + nameOf(side) + ", 추가: " + nameOf(extra);
        if (discount() > 0) {
            return result + " / " + subtotal() + " - " + discount() + " = " + total() + "원";
        }
        return result + " / " + total() + "원";
    }

    public static class Builder {
        private MenuItem main;
        private MenuItem side;
        private MenuItem extra;
        private VenueType venue;

        public Builder venue(VenueType venue) {
            this.venue = venue;
            return this;
        }

        public Builder main(MenuItem main) {
            this.main = main;
            return this;
        }

        public Builder side(MenuItem side) {
            this.side = side;
            return this;
        }

        public Builder extra(MenuItem extra) {
            this.extra = extra;
            return this;
        }

        public MealSet build() {
            // PDF의 검사 순서: venue → R3 → 슬롯 → R1 → R4 → R2
            if (venue == null) {
                throw new IllegalStateException("venue 필수: 코너를 지정해야 합니다.");
            }
            if (venue == VenueType.COVER && extra != null) {
                throw new IllegalStateException("R3: 바비든든은 EXTRA를 넣을 수 없습니다.");
            }

            checkSlot(MenuSlot.MAIN, main);
            checkSlot(MenuSlot.SIDE, side);
            checkSlot(MenuSlot.EXTRA, extra);

            if (main == null || (side == null && extra == null)) {
                throw new IllegalStateException("R1: 주메뉴와 반찬 또는 추가 메뉴가 필요합니다.");
            }
            if (main != null && !main.restaurantName().equals(venue.name)) {
                throw new IllegalStateException("R4: 주메뉴의 코너가 일치하지 않습니다.");
            }
            if (side != null && !side.restaurantName().equals(venue.name)) {
                throw new IllegalStateException("R4: 반찬의 코너가 일치하지 않습니다.");
            }
            if (extra != null && !extra.restaurantName().equals(venue.name)) {
                throw new IllegalStateException("R4: 추가 메뉴의 코너가 일치하지 않습니다.");
            }

            // 객체를 만든 뒤 할인까지 반영된 total()로 검사한다.
            MealSet result = new MealSet(this);
            if (venue == VenueType.GUKBAP && result.total() > 8000) {
                throw new IllegalStateException("R2: 국밥 세트는 8000원 이하여야 합니다. ("
                        + result.total() + "원)");
            }
            return result;
        }

        private void checkSlot(MenuSlot want, MenuItem item) {
            if (item != null && MenuItemLoader.roleOf(item) != want) {
                throw new IllegalStateException("슬롯 불일치: " + item.name() + "은 "
                        + MenuItemLoader.roleOf(item) + " 메뉴입니다. (배치: " + want + ")");
            }
        }
    }
}
