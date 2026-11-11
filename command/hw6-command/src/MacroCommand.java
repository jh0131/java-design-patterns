import java.util.List;

// 여러 명령을 하나의 실행/Undo 단위로 묶는다.
public class MacroCommand implements ICommand {
    private final String macroName;
    private final List<ICommand> commands;

    public MacroCommand(String macroName, List<ICommand> commands) {
        this.macroName = macroName;
        this.commands = List.copyOf(commands);
    }

    @Override
    public void execute() {
        int completed = 0;
        try {
            for (ICommand command : commands) {
                command.execute();
                completed++;
            }
        } catch (RuntimeException ex) {
            // 실패한 명령은 상태를 변경하지 않는다. 완료된 명령만 역순으로 복구한다.
            for (int i = completed - 1; i >= 0; i--) {
                try {
                    commands.get(i).undo();
                } catch (RuntimeException rollbackFailure) {
                    ex.addSuppressed(rollbackFailure);
                }
            }
            throw ex;
        }
    }

    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

    @Override
    public String name() {
        return "Macro(" + macroName + ")";
    }
}
