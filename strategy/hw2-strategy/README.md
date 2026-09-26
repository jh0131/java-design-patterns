# HW2 Strategy Pattern

CSV 파일에서 메뉴 데이터를 읽은 뒤, Strategy 패턴을 이용해 여러 추천 기준을 교체하며 실행하는 프로그램이다. HW1에서 사용한 CSV 입력과 문자열 정규화 로직을 재사용하였다.

## 클래스 구조

```text
App (Client)
├─ Re_CsvMenuLoader
│  └─ Re_MenuTextNormalizer
├─ MenuItem
├─ RecommendationContext (Context)
│  └─ RankingStrategy (Strategy)
│     ├─ LowestPriceStrategy
│     ├─ HighestPriceStrategy
│     ├─ CheapestPerRestaurantStrategy
│     ├─ KeywordPreferenceStrategy
│     ├─ BudgetThenNameStrategy
│     ├─ RandomPickStrategy
│     └─ NewTargetPriceStrategy (YOUR CODE)
└─ ConsoleReport
```

- `MenuItem`: 식당 ID, 식당명, 메뉴명, 가격을 저장한다.
- `Re_CsvMenuLoader`, `Re_MenuTextNormalizer`: CSV를 읽고 메뉴 데이터를 정규화한다.
- `ConsoleReport`: 각 추천 전략의 상위 5개 결과를 출력한다.

## 전체 코드 흐름

```text
menus.csv
  → Re_CsvMenuLoader / Re_MenuTextNormalizer
  → List<MenuItem>
  → RecommendationContext
  → 선택된 RankingStrategy의 rank()
  → ConsoleReport
```

`App`은 전략 객체 목록을 만들고, 반복문에서 `setStrategy()`로 현재 전략을 교체한다. 이후 `recommend()`를 호출해 결과를 얻고 `ConsoleReport`로 출력한다.

## Strategy 패턴 구현

- `RankingStrategy`는 모든 추천 알고리즘이 구현하는 공통 인터페이스이다.
- 각 추천 기준은 별도의 ConcreteStrategy 클래스로 분리하였다.
- `RecommendationContext`는 구체적인 알고리즘을 알지 않고 현재 전략의 `rank()`에 추천 작업을 위임한다.
- 새로운 추천 기준은 `RankingStrategy` 구현 클래스를 추가한 뒤 `App`의 전략 목록에 등록하면 된다.

이 구조를 통해 하나의 조건문에 여러 알고리즘을 넣지 않고, 전략 객체만 교체하여 다른 추천 결과를 얻을 수 있다.

## YOUR CODE: 목표 가격 근접순

`NewTargetPriceStrategy`는 목표 가격과 실제 메뉴 가격의 차이가 작은 메뉴부터 추천한다. 현재 목표 가격은 5,500원이다.

정렬 기준은 다음과 같다.

1. 목표 가격과의 절대 차이 오름차순
2. 차이가 같으면 실제 가격 오름차순
3. 가격도 같으면 메뉴명 오름차순

원본 메뉴 목록이 바뀌지 않도록 복사본을 정렬하며, `App`에는 다음과 같이 등록하였다.

## 실행

IntelliJ에서 `App.main()`을 실행하면 된다. 별도의 경로를 전달하지 않으면 프로젝트 루트의 `menus.csv`를 사용한다.
