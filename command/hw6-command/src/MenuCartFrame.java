import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
// --- yourCode ---
import java.nio.file.Path;
// --- yourCode 끝 ---

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

// Client. 버튼은 Command 객체를 만들어 executeCommand()에 넘기기만 한다.
// Cart를 직접 수정하지 않는다 (변경은 반드시 Command로만).
//
// 제공된 화면 배치를 유지하고 이벤트, 명령 실행, 화면 갱신을 구현한다.
@SuppressWarnings("serial") // 화면 객체를 파일로 직렬화하지 않는다.


public final class MenuCartFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    // 표의 열 번호
    private static final int COL_MENU_ID = 0;
    private static final int COL_QTY = 3;
    private static final int COL_MINUS = 5;
    private static final int COL_PLUS = 6;
    private static final int COL_DELETE = 7;

    private final Cart cart = new Cart();
    private final CommandInvoker invoker = new CommandInvoker();
    // Swing UI 요소
    private final JComboBox<MenuItem> menuCombo;
    private final JSpinner addQtySpinner;         // 담기 수량 (1~99)
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel totalLabel;
    private final JTextArea logArea;

    // --- yourCode ---
    private final CustomSetStore customSetStore;
    private final JComboBox<CustomSet> customSetCombo = new JComboBox<>();
    private final JButton manageSetsButton = new JButton("세트 편집·저장");
    private final JButton addSetButton = new JButton("세트 담기");
    private List<CustomSet> customSets = List.of();

    public MenuCartFrame(List<MenuItem> menuItems) {
        this(menuItems, Path.of("custom-sets.xml"));
    }

    public MenuCartFrame(List<MenuItem> menuItems, Path customSetFile) {
        customSetStore = new CustomSetStore(customSetFile);
        // --- yourCode 끝 ---
        setTitle("Cart Command + Swing Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== 상단 1행: 메뉴 선택 + 수량 + 담기 =====
        menuCombo = new JComboBox<>(menuItems.toArray(new MenuItem[0]));
        menuCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MenuItem m) {
                    setText(String.format("[%s] %s  %,d원", m.restaurantName(), m.name(), m.price()));
                }
                return this;
            }
        });
        addQtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        JButton btnAdd = new JButton("담기");

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("메뉴"));
        addPanel.add(menuCombo);
        addPanel.add(new JLabel("수량"));
        addPanel.add(addQtySpinner);
        addPanel.add(btnAdd);

        // ===== 상단 2행: 비우기 + 매크로 + Undo/Redo =====
        JButton btnClear = new JButton("장바구니 비우기");
        JButton btnMacro = new JButton("매크로 (늘 먹던 세트)");
        JButton btnUndo = new JButton("Undo");
        JButton btnRedo = new JButton("Redo");

        JPanel cmdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmdPanel.add(btnClear);
        cmdPanel.add(btnMacro);
        cmdPanel.add(btnUndo);
        cmdPanel.add(btnRedo);

        JPanel topContainer = new JPanel(new GridLayout(2, 1));
        topContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        topContainer.add(addPanel);
        topContainer.add(cmdPanel);
        // --- yourCode ---
        ((GridLayout) topContainer.getLayout()).setRows(3);
        topContainer.add(createCustomSetPanel(menuItems));
        // --- yourCode 끝 ---
        add(topContainer, BorderLayout.NORTH);

        // ===== 중앙: 장바구니 목록 (JTable) — 각 줄 오른쪽에 [−] [+] [삭제] 버튼 =====
        String[] columnNames = {"메뉴 ID", "메뉴명", "단가", "수량", "금액", "", "", ""};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // 표에서 직접 수정 금지 — 변경은 Command로만
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);   // 열 순서 고정 (열 번호로 버튼 판별)

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int col = 2; col <= 4; col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(rightRenderer);
        }

        // 버튼 열: 셀을 버튼 모양으로 그린다 (글자는 열 번호로 정함)
        TableCellRenderer buttonRenderer = new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                String label = switch (tbl.convertColumnIndexToModel(col)) {
                    case COL_MINUS -> "−";
                    case COL_PLUS -> "+";
                    default -> "삭제";
                };
                JButton b = new JButton(label);
                b.setMargin(new Insets(0, 0, 0, 0));
                return b;
            }
        };
        for (int col = COL_MINUS; col <= COL_DELETE; col++) {
            TableColumn c = table.getColumnModel().getColumn(col);
            c.setCellRenderer(buttonRenderer);
            c.setPreferredWidth(col == COL_DELETE ? 60 : 40);
            c.setMaxWidth(col == COL_DELETE ? 60 : 40);
        }

        // 버튼 열 클릭 → 해당 줄에 대한 Command 실행
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row < 0 || col < 0) {
                    return;
                }
                row = table.convertRowIndexToModel(row);
                col = table.convertColumnIndexToModel(col);
                String menuId = (String) tableModel.getValueAt(row, COL_MENU_ID);
                int qty = (Integer) tableModel.getValueAt(row, COL_QTY);

                switch (col) {
                    // 1개에서 수량을 줄이면 삭제되고, Undo 시 원래 위치로 복구된다.
                    case COL_MINUS -> executeCommand(new ChangeQuantityCommand(cart, menuId, qty - 1));
                    case COL_PLUS -> executeCommand(new ChangeQuantityCommand(cart, menuId, qty + 1));
                    case COL_DELETE -> executeCommand(new RemoveItemCommand(cart, menuId));
                    default -> { }
                }
            }
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("장바구니 내역"));
        add(tableScroll, BorderLayout.CENTER);

        // ===== 하단: 총 금액 + 로그 =====
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        totalLabel = new JLabel("총 금액: 0원", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        logArea = new JTextArea(6, 50);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("명령 실행 로그"));

        bottomPanel.add(totalLabel, BorderLayout.NORTH);
        bottomPanel.add(logScroll, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== 매크로용 메뉴 =====
        MenuItem jeoyuk = find(menuItems, "R05-제육덮밥");
        MenuItem ramyeon = find(menuItems, "R09-폭풍라면");
        MenuItem mandu = find(menuItems, "R01-왕만두");

        // ===== 이벤트: 모든 변경은 Command 객체를 만들어 executeCommand()로 넘긴다 =====

        // 선택한 메뉴를 스피너 수량만큼 담는다.
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem) menuCombo.getSelectedItem();
                if (item == null) {
                    log("[실패] 메뉴를 선택해주세요.");
                    return;
                }
                int qty = spinnerValue(addQtySpinner);
                executeCommand(new AddItemCommand(cart, item.menuId(), item.name(), item.price(), qty));
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new ClearCartCommand(cart));
            }
        });

        // 세 메뉴를 하나의 Undo/Redo 단위로 실행한다.
        btnMacro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new MacroCommand("늘 먹던 세트", List.of(
                        new AddItemCommand(cart, jeoyuk.menuId(), jeoyuk.name(), jeoyuk.price(), 1),
                        new AddItemCommand(cart, ramyeon.menuId(), ramyeon.name(), ramyeon.price(), 1),
                        new AddItemCommand(cart, mandu.menuId(), mandu.name(), mandu.price(), 1)
                )));
            }
        });

        btnUndo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    log(invoker.undo() ? "[Undo] 최근 명령을 취소했습니다." : "[Undo] 취소할 명령이 없습니다.");
                } catch (Exception ex) {
                    log("[실패] Undo: " + ex.getMessage());
                } finally {
                    updateView();
                }
            }
        });

        btnRedo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    log(invoker.redo() ? "[Redo] 명령을 다시 실행했습니다." : "[Redo] 다시 실행할 명령이 없습니다.");
                } catch (Exception ex) {
                    log("[실패] Redo: " + ex.getMessage());
                } finally {
                    updateView();
                }
            }
        });

        // --- yourCode ---
        try {
            refreshCustomSets(customSetStore.load());
        } catch (Exception ex) {
            // 잘못된 파일을 빈 목록으로 덮어쓰지 않도록 세트 기능만 중지한다.
            manageSetsButton.setEnabled(false);
            addSetButton.setEnabled(false);
            log("[세트 불러오기 실패] " + ex.getMessage() + " 파일 확인 후 다시 실행해주세요.");
        }
        // --- yourCode 끝 ---
        updateView();
    }

    // --- yourCode ---
    private JPanel createCustomSetPanel(List<MenuItem> menuItems) {
        customSetCombo.setPreferredSize(new java.awt.Dimension(240, 25));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("사용자 세트"));
        panel.add(customSetCombo);
        panel.add(addSetButton);
        panel.add(manageSetsButton);
        manageSetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomSetDialog dialog = new CustomSetDialog(MenuCartFrame.this, menuItems, customSetStore, customSets);
                dialog.setVisible(true);
                if (!customSets.equals(dialog.savedSets())) {
                    refreshCustomSets(dialog.savedSets());
                    log("[사용자 세트] 저장한 세트 목록을 갱신했습니다.");
                }
            }
        });
        addSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomSet selected = (CustomSet) customSetCombo.getSelectedItem();
                if (selected == null) {
                    return;
                }
                try {
                    executeCommand(selected.toCommand(cart, menuItems));
                } catch (Exception ex) {
                    log("[실패] " + ex.getMessage());
                    updateView();
                }
            }
        });
        return panel;
    }

    private void refreshCustomSets(List<CustomSet> saved) {
        customSets = List.copyOf(saved);
        customSetCombo.removeAllItems();
        for (CustomSet set : customSets) { customSetCombo.addItem(set); }
        addSetButton.setEnabled(!customSets.isEmpty());
    }
    // --- yourCode 끝 ---

    private static MenuItem find(List<MenuItem> items, String menuId) {
        for (MenuItem item : items) {
            if (item.menuId().equals(menuId)) {
                return item;
            }
        }
        throw new IllegalArgumentException("매크로에 필요한 메뉴가 없습니다: " + menuId);
    }

    // 스피너에 키보드로 입력한 숫자는 Enter나 포커스 이동 전까지 getValue()에 반영되지 않는다.
    // 버튼을 누르는 순간 입력값을 확정(commitEdit)한 뒤 읽는다.
    private static int spinnerValue(JSpinner spinner) {
        try {
            spinner.commitEdit();
        } catch (java.text.ParseException ex) {
            // 숫자가 아닌 입력이면 기존 값을 사용
        }
        return (Integer) spinner.getValue();
    }

    // 명령의 성공/실패를 로그에 남기고 항상 현재 상태로 화면을 갱신한다.
    private void executeCommand(ICommand cmd) {
        try {
            invoker.execute(cmd);
            log("[실행] " + cmd.name());
        } catch (Exception ex) {
            log("[실패] " + ex.getMessage());
        } finally {
            updateView();
        }
    }

    // Cart의 읽기 전용 조회 결과를 표와 총 금액에 반영한다.
    private void updateView() {
        tableModel.setRowCount(0);
        for (CartLine line : cart.getLines()) {
            tableModel.addRow(new Object[]{
                    line.getMenuId(), line.getName(),
                    String.format("%,d원", line.getUnitPrice()), line.getQty(),
                    String.format("%,d원", line.lineTotal()), "−", "+", "삭제"
            });
        }
        totalLabel.setText(String.format("총 금액: %,d원", cart.total()));
    }

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
