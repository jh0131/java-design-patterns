import java.util.ArrayDeque;
import java.util.Deque;

// Invoker: 성공한 명령만 히스토리에 기록한다.

public class CommandInvoker {
    private final Deque<ICommand> undoStack = new ArrayDeque<>();
    private final Deque<ICommand> redoStack = new ArrayDeque<>();

    public void execute(ICommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }
        // 명령이 실패하면 기존 스택을 유지하도록 성공한 뒤에 이동시킨다.
        ICommand command = undoStack.peek();
        command.undo();
        undoStack.pop();
        redoStack.push(command);
        return true;
    }

    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }
        ICommand command = redoStack.peek();
        command.execute();
        redoStack.pop();
        undoStack.push(command);
        return true;
    }
}
