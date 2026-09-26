import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// SRP 원칙을 지키기 위해서 적재 코드 책임 분리

public class MenuLoader implements MenuDataLoader {

    @Override
    public List<String[]> load(String csv) throws Exception {

        List<String[]> rows = new ArrayList<>(); // 이전 결과가 남기 때문에 load 메서드 내에서 지역 변수로 생성


        // csv파일에서 데이터를 읽어와서 , 단위로 분리한 뒤 컬렉션에 저장

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8))) {

            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                rows.add(line.split(",", -1));
            }
            return rows;
        }
    }
}
