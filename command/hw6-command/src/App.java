import java.nio.file.Path;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        Path csv = args.length > 0 ? Path.of(args[0]) : Path.of("menus.csv");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    List<MenuItem> menus = new MenuItemLoader(csv).all();
                    // --- yourCode ---
                    Path customSets = csv.toAbsolutePath().getParent().resolve("custom-sets.xml");
                    new MenuCartFrame(menus, customSets).setVisible(true);
                    // --- yourCode 끝 ---
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "프로그램을 시작할 수 없습니다.\n" + ex.getMessage()
                                    + "\n메뉴 파일: " + csv.toAbsolutePath(),
                            "실행 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
