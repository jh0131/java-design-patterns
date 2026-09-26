
// 가격 구간 판정이 MenuAggregator 내부에 직접 구현되어 있어서,
// 가격 기준 변경 시 집계 코드도 수정해야 하므로 PriceBucket enum으로 분리함.
// 이를 통해 가격 정책의 변경 영향을 줄이고 OCP를 개선한다.

// 내가 추가한 코드(yourcode)
public enum PriceBucket {

    UP_TO_3900(3900, "~3900"),
    UP_TO_5900(5900, "~5900"),
    UP_TO_6900(6900, "~6900"),
    OVER_6900(Integer.MAX_VALUE, "6900+");


    // 필드의 직접 접근은 private으로 제한하고,
    // 외부에는 필요한 label 값만 getter로 제공한다.

    private final int maxPrice;
    private final String label;

    PriceBucket(int maxPrice, String label) {
        this.maxPrice = maxPrice;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PriceBucket from(int price) {
        for (PriceBucket bucket : PriceBucket.values()) {
            if (price <= bucket.maxPrice) {
                return bucket;
            }
        }

        throw new IllegalArgumentException("처리할 수 없는 가격: " + price);
    }
}
