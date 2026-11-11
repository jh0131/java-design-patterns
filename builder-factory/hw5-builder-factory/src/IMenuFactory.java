public interface IMenuFactory {
    MenuItem main();
    MenuItem side();

    default MenuItem extra() {
        throw new UnsupportedOperationException("이 코너는 추가 메뉴를 제공하지 않습니다.");
    }
}
