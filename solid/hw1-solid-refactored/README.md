# HW1: SOLID 리팩터링

`menus.csv`의 적재·정규화·집계·출력을 역할별 클래스로 분리한 코드입니다.

## 구현 핵심

- SRP: `MenuLoader`, `MenuNormalizer`, `MenuAggregator`, `MenuReporter`가 각각 파일 적재, 값 정규화, 집계, 출력을 담당합니다.
- DIP: `MenuApplication`은 파일 로더의 구체 클래스 대신 `MenuDataLoader` 인터페이스에 의존합니다.
- `App`이 협력 객체를 생성해 `MenuApplication` 생성자에 주입합니다.
- 흐름: `App` → `MenuApplication.run()` → 적재 → 정규화 → 집계 → 출력.

SOLID는 설계 원칙이며, 이 과제는 책임 분리와 의존성 주입을 적용한 리팩터링 예제입니다.

## 실행
```bash
javac -encoding UTF-8 -d bin src/*.java
java -Dstdout.encoding=UTF-8 -cp bin App
```

프로젝트 루트에 `menus.csv`가 필요합니다. 리팩터링 전 코드는 형제 폴더 `HW1-Monolithic_Original`에 있습니다. 과제의 목표는 기존 출력 동작을 유지하면서 구조를 개선하는 것입니다.
