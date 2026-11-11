// Command: 실행과 실행 취소를 같은 인터페이스로 다룬다.
public interface ICommand {
    void execute();
    void undo();
    String name();
}
