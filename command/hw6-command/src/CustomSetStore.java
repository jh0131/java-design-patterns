// --- yourCode ---
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

// JDK 기본 Properties XML을 사용하므로 별도 라이브러리가 필요 없다.
public final class CustomSetStore {
    private final Path file;

    public CustomSetStore(Path file) {
        this.file = file.toAbsolutePath();
    }

    public List<CustomSet> load() throws IOException {
        if (!Files.exists(file)) {
            return List.of();
        }
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(file)) {
            properties.loadFromXML(input);
        }
        try {
            int count = number(properties, "set.count");
            List<CustomSet> sets = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String prefix = "set." + i + ".";
                List<CustomSet.Entry> entries = new ArrayList<>();
                int size = number(properties, prefix + "item.count");
                for (int j = 0; j < size; j++) {
                    String item = prefix + "item." + j + ".";
                    entries.add(new CustomSet.Entry(Objects.requireNonNull(properties.getProperty(item + "id")),
                            number(properties, item + "qty")));
                }
                sets.add(new CustomSet(properties.getProperty(prefix + "name"), entries));
            }
            validateNames(sets);
            return List.copyOf(sets);
        } catch (RuntimeException ex) {
            throw new IOException("저장된 세트 데이터가 올바르지 않습니다.", ex);
        }
    }

    public void save(List<CustomSet> sets) throws IOException {
        validateNames(sets);
        Properties properties = new Properties();
        properties.setProperty("set.count", String.valueOf(sets.size()));
        for (int i = 0; i < sets.size(); i++) {
            CustomSet set = sets.get(i);
            String prefix = "set." + i + ".";
            properties.setProperty(prefix + "name", set.name());
            properties.setProperty(prefix + "item.count", String.valueOf(set.entries().size()));
            for (int j = 0; j < set.entries().size(); j++) {
                CustomSet.Entry entry = set.entries().get(j);
                String item = prefix + "item." + j + ".";
                properties.setProperty(item + "id", entry.menuId());
                properties.setProperty(item + "qty", String.valueOf(entry.qty()));
            }
        }
        // 임시 파일을 완성한 뒤 교체하여 저장 중 실패해도 기존 파일을 보존한다.
        Path temporary = Files.createTempFile(file.getParent(), "custom-sets-", ".tmp");
        try {
            try (OutputStream output = Files.newOutputStream(temporary)) {
                properties.storeToXML(output, "Lab6 custom sets", "UTF-8");
            }
            try {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    private static int number(Properties properties, String key) {
        int value = Integer.parseInt(Objects.requireNonNull(properties.getProperty(key)));
        if (value < 0) {
            throw new IllegalArgumentException("음수 개수는 허용하지 않습니다.");
        }
        return value;
    }

    private static void validateNames(List<CustomSet> sets) {
        Set<String> names = new HashSet<>();
        for (CustomSet set : sets) {
            if (!names.add(set.name())) {
                throw new IllegalArgumentException("이미 사용 중인 세트 이름입니다: " + set.name());
            }
        }
    }
}
// --- yourCode 끝 ---
