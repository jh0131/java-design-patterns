import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


class MenuOrderFrameTemplate extends JFrame {

    private enum Option {
        SPICY, EXTRA_LARGE, ADDON, DISCOUNT
    }

    // 버튼을 누른 순간의 옵션 종류와 값을 저장한다.
    private static class SelectedOption {
        private final Option type;
        private final int value;
        private final String name;

        private SelectedOption(Option type, int value, String name) {
            this.type = type;
            this.value = value;
            this.name = name;
        }
    }

    // Swing 컴포넌트 (수정 불필요)
    private final JComboBox<MenuItem> baseBox = new JComboBox<>();
    private final JSpinner spicyLv = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
    private final JComboBox<MenuItem> addOnBox = new JComboBox<>();
    private final JSpinner discountPct = new JSpinner(new SpinnerNumberModel(10, 5, 30, 5));
    private final JTextArea out = new JTextArea(14, 40);

    // 지금까지 감싼 데코레이터 체인의 가장 바깥 겹. 버튼을 누를 때마다 한 겹씩 늘어난다.
    private IMenu current;

    // yourcode: 적립 안내의 선택 여부만 보관하여 중복 안내 없이 최종 가격으로 다시 계산한다.
    private boolean pointRewardEnabled;

    // 영수증의 단계별 줄 (단계 | 증감 | 누적)
    private final StringBuilder steps = new StringBuilder();

    // 순서 비교를 위해 버튼을 누를 때의 옵션 값을 보관한다.
    private MenuItem baseMenu;
    private final List<SelectedOption> wrapOrder = new ArrayList<>();

    // 화면을 구성하고 버튼을 아래 onXxx() 메서드에 연결한다. (수정 불필요)
    MenuOrderFrameTemplate(MenuItemLoader catalog) {
        super("HW4 Decorator");
        // 기본 메뉴는 전체 목록, 추가 메뉴는 2500원 이하 목록에서 고른다.
        for (MenuItem item : catalog.all()) {
            baseBox.addItem(item);
        }
        for (MenuItem item : catalog.cheapAddOns()) {
            addOnBox.addItem(item);
        }

        // 버튼
        JButton resetBtn = new JButton("초기화");
        JButton spicyBtn = new JButton("매운맛 추가");
        JButton xlBtn = new JButton("곱빼기 추가");
        JButton addOnBtn = new JButton("추가 메뉴");
        JButton discountBtn = new JButton("할인 적용");
        // yourcode: 결제 가격을 변경하지 않고 적립 예정 포인트 안내를 켜는 버튼이다.
        JButton pointsBtn = new JButton("적립 안내");
        addOnBtn.setEnabled(addOnBox.getItemCount() > 0);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new GridLayout(0, 1, 4, 4));
        top.setBorder(BorderFactory.createTitledBorder("메뉴 / 옵션"));
        top.add(row("Base", baseBox, resetBtn));
        top.add(row("매운맛 단계", spicyLv, spicyBtn));
        top.add(row("곱빼기 +1500", null, xlBtn));
        top.add(row("추가", addOnBox, addOnBtn));
        top.add(row("할인 %", discountPct, discountBtn));
        // yourcode: 적립 비율을 표시하고 보너스 조건은 영수증 설명에서 안내한다.
        top.add(row("포인트 1%", null, pointsBtn));

        out.setEditable(false);
        out.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(out), BorderLayout.CENTER);

        // 버튼을 누르면 그 순간의 입력값으로 onXxx()가 호출된다.
        // 기본 메뉴를 바꾸거나 초기화하면 체인을 BaseMenu부터 다시 시작한다.
        baseBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        // 각 버튼은 current를 새 데코레이터로 한 겹 감싼다.
        // 값(단계, 할인율, 추가 메뉴)은 버튼을 누르는 순간의 값이 그 겹에 고정된다.
        spicyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSpicy((Integer) spicyLv.getValue());
            }
        });
        xlBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExtraLarge();
            }
        });
        addOnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem) addOnBox.getSelectedItem();
                if (item != null) {
                    onAddOn(item);
                }
            }
        });
        discountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDiscount((Integer) discountPct.getValue());
            }
        });

        // yourcode: 포인트 버튼을 기존 방식의 ActionListener로 연결한다.
        pointsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPointReward();
            }
        });

        reset();
        pack();
        setLocationRelativeTo(null);
    }

    // 선택한 기본 메뉴로 현재 주문과 영수증 단계를 초기화한다.
    private void reset() {
        MenuItem base = (MenuItem) baseBox.getSelectedItem();
        if (base != null) {
            onReset(base);
        } else {
            baseMenu = null;
            current = null;
            // yourcode: 메뉴가 없으면 적립 안내도 초기화한다.
            pointRewardEnabled = false;
            steps.setLength(0);
            wrapOrder.clear();
            out.setText("메뉴 목록이 비어 있습니다.");
        }
    }

    // 기본 메뉴로 주문을 새로 시작한다.
    // - base.asBase()로 체인의 시작점(가장 안쪽)을 만든다.
    // - 영수증을 비우고 Base 줄을 기록한 뒤 showReceipt(...)로 화면에 표시한다.
    private void onReset(MenuItem base) {
        baseMenu = base;
        current = base.asBase();
        // yourcode: 메뉴 변경 또는 초기화 시 이전 주문의 적립 안내를 해제한다.
        pointRewardEnabled = false;
        steps.setLength(0);
        wrapOrder.clear();
        steps.append(line("Base", 0, current.price()));
        showReceipt(steps.toString(), current.price(), current.description());
    }

    // 현재 메뉴를 Spicy(현재 메뉴, level)로 한 겹 감싼다.
    private void onSpicy(int level) {

        applyOption(new SelectedOption(Option.SPICY, level, ""), "매운맛 " + level + "단계");
    }

    // 현재 메뉴를 ExtraLarge(현재 메뉴)로 한 겹 감싼다.
    private void onExtraLarge() {

        applyOption(new SelectedOption(Option.EXTRA_LARGE, 0, ""), "곱빼기");
    }

    // 현재 메뉴를 AddOn(현재 메뉴, item.name(), item.price())로 한 겹 감싼다.
    private void onAddOn(MenuItem item) {

        applyOption(new SelectedOption(Option.ADDON, item.price(), item.name()), "추가 " + item.name());
    }

    // 현재 메뉴를 Discount(현재 메뉴, percent)로 한 겹 감싼다.
    private void onDiscount(int percent) {

        applyOption(new SelectedOption(Option.DISCOUNT, percent, ""), "할인 " + percent + "%");
    }

    // yourcode: 적립 안내를 켠 뒤 영수증을 갱신한다. 다시 눌러도 중복 적용하지 않는다.
    private void onPointReward() {
        if (current != null) {
            pointRewardEnabled = true;
            showReceipt(steps.toString(), current.price(), current.description());
        }
    }

    // 감싸기, 가격 변화 기록, 화면 갱신을 공통으로 처리한다.
    private void applyOption(SelectedOption option, String label) {
        if (current == null) {
            return;
        }

        int before = current.price();
        IMenu next = wrap(current, option);
        int total = next.price();
        String description = next.description();

        current = next;
        wrapOrder.add(option);

        steps.append(line(label, total - before, total));
        showReceipt(steps.toString(), total, description);
    }

    // 선택한 옵션에 맞는 데코레이터를 한 겹 추가한다.
    private static IMenu wrap(IMenu menu, SelectedOption option) {
        switch (option.type) {
            case SPICY:
                return new Spicy(menu, option.value);
            case EXTRA_LARGE:
                return new ExtraLarge(menu);
            case ADDON:
                return new AddOn(menu, option.name, option.value);
            case DISCOUNT:
                return new Discount(menu, option.value);
            default:
                throw new IllegalArgumentException("알 수 없는 옵션입니다.");
        }
    }

    // 일반 할인인지 확인한다.
    private static boolean isDiscount(SelectedOption option) {
        return option.type == Option.DISCOUNT;
    }

    // 그룹 안의 순서와 중복 옵션을 유지하면서 할인 위치만 바꾼다.
    private static IMenu wrapAll(IMenu menu, List<SelectedOption> sequence, boolean discountFirst) {
        for (SelectedOption option : sequence) {
            if (isDiscount(option) == discountFirst) {
                menu = wrap(menu, option);
            }
        }
        for (SelectedOption option : sequence) {
            if (isDiscount(option) != discountFirst) {
                menu = wrap(menu, option);
            }
        }
        return menu;
    }

    // 같은 옵션을 사용하되 모든 할인을 먼저/나중에 적용한 합계를 비교한다.
    private String orderComparison() {
        boolean hasDiscount = false;
        for (SelectedOption option : wrapOrder) {
            if (isDiscount(option)) {
                hasDiscount = true;
                break;
            }
        }
        if (baseMenu == null || !hasDiscount) {
            return "";
        }

        int discountFirst = wrapAll(baseMenu.asBase(), wrapOrder, true).price();
        int discountLast = wrapAll(baseMenu.asBase(), wrapOrder, false).price();
        return String.format("%n== 할인 적용 순서 비교 ==%n"
                        + "할인 먼저: %,d원%n할인 나중: %,d원%n차이(먼저 - 나중): %+,d원%n",
                discountFirst, discountLast, discountFirst - discountLast);
    }

    // 영수증 단계와 합계, 설명을 화면에 표시한다.
    // steps: line()으로 만든 줄들을 이어 붙인 문자열
    // total, description: 현재 메뉴의 price(), description() 결과
    private void showReceipt(String steps, int total, String description) {
        // yourcode: 가격 옵션을 모두 적용한 current를 가장 바깥에서 감싸 최종 금액 기준으로 안내한다.
        // 적립 안내 이후 다른 옵션을 눌러도 포인트와 보너스 조건이 새 합계에 맞춰 갱신된다.
        if (pointRewardEnabled && current != null) {
            IMenu rewardMenu = new PointReward(current);
            total = rewardMenu.price();
            description = rewardMenu.description();
        }
        out.setText("== 주문 ==\n" + steps
                + String.format("────────────────%n합계 %,d원%n%s%n", total, description)
                + orderComparison());
        out.setCaretPosition(0);
    }

    // 라벨, 입력 컨트롤, 버튼으로 한 행의 화면 구성 요소를 만든다.
    private static JPanel row(String label, Component field, Component button) {
        JPanel p = new JPanel(new BorderLayout(6, 0));
        JLabel l = new JLabel(label);
        l.setPreferredSize(new Dimension(90, l.getPreferredSize().height));
        p.add(l, BorderLayout.WEST);
        if (field != null) p.add(field, BorderLayout.CENTER);
        p.add(button, BorderLayout.EAST);
        return p;
    }

    // 영수증 한 줄: 단계 이름 | 증감 | 누적 가격
    private static String line(String label, int delta, int total) {
        String deltaText = delta == 0 ? "     " : String.format("%+,d", delta);
        return String.format("%-16s %6s  → %,6d원%n", label, deltaText, total);
    }
}
