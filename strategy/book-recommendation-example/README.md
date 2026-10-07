# Strategy: 도서 추천 기준

책 목록에 적용할 추천 알고리즘을 전략 객체로 분리하는 실습이다. 현재 일부만 구현되어 있다.

- `Book`: 제목, 가격, 평점을 보관하는 record.
- `RecommendationStrategy`: 전략 이름과 `recommend(books)`를 정의하는 공통 인터페이스.
- `CheapestFirstStrategy`: 가격 오름차순으로 정렬한 새 목록을 반환한다.
- `BookRecommender` (Context): 전략을 저장·교체하고 추천 작업을 위임하도록 설계된 클래스.

의도한 흐름은 **책 목록 준비 → 전략 주입 → recommend() 호출 → 선택한 전략의 결과 반환**이다. 추천 기준을 바꾸어도 Context의 사용 방식은 같다는 것이 Strategy 패턴의 핵심이다.

## 현재 구현 상태

`BookRecommender`의 생성자와 `setStrategy()`는 비어 있고, `recommend()`는 `null`을 반환한다. `HighestRatingStrategy`와 `AffordableHighRatingStrategy`도 빈 클래스이며 실행용 `main()`은 없다. 따라서 완성된 추천 프로그램은 아니며, 낮은 가격순 전략만 구현되어 있다.

JDK 16 이상에서 `javac -encoding UTF-8 -d bin src/*.java`로 소스 컴파일이 가능하다.
