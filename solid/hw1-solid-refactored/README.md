# HW1-Monolithic — God class (Before)

`../data/menus.csv`를 한 클래스에서 적재·정규화·집계·출력합니다.

## 학생이 찾아야 할 냄새
1. SRP 위반 — 네 가지 책임이 `main()` 하나에
2. 전역 가변 상태 — `public static` 컬렉션
3. OCP 위반 — 가격 구간·출력 형식 변경 시 이 파일을 수정해야 함
4. 테스트 불가 — 결과를 반환하지 않고 표준 출력에 직접 찍음

## 실행
```bash
javac -encoding UTF-8 -d bin src/*.java
java -Dstdout.encoding=UTF-8 -cp bin App
```

## 다음 단계
`HW1-SOLID`에서 Loader / Normalizer / Aggregator / Reporter로 분리합니다.
**출력은 한 글자도 달라지면 안 됩니다** — 그것이 리팩터링의 정의입니다.
