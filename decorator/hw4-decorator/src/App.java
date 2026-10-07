import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Path;

public class App {

    public static void main(String[] args) throws Exception {
        Path csv = Path.of("menus.csv");
        MenuItemLoader catalog = new MenuItemLoader(csv);
        if (catalog.all().isEmpty()) {
            throw new IOException("menus.csv에 메뉴가 없습니다.");
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuOrderFrameTemplate frame = new MenuOrderFrameTemplate(catalog);
                frame.setVisible(true);
            }
        });
    }
}
