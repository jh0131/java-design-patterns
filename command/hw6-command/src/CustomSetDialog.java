// --- yourCode ---
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public final class CustomSetDialog extends JDialog {
    private final List<MenuItem> menus;
    private final CustomSetStore store;
    private final List<CustomSet> sets;
    private final JComboBox<CustomSet> savedCombo = new JComboBox<>();
    private final JComboBox<MenuItem> menuCombo;
    private final JTextField nameField = new JTextField(20);
    private final JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"메뉴 ID", "메뉴명", "단가", "수량"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 3;
        }

        @Override
        public Class<?> getColumnClass(int column) {
            return column == 3 ? Integer.class : String.class;
        }
    };
    private final JTable table = new JTable(model);

    public CustomSetDialog(JFrame owner, List<MenuItem> menus, CustomSetStore store, List<CustomSet> saved) {
        super(owner, "사용자 세트 편집·저장", true);
        this.menus = List.copyOf(menus);
        this.store = store;
        this.sets = new ArrayList<>(saved);
        menuCombo = new JComboBox<>(menus.toArray(new MenuItem[0]));
        // 메뉴 목록에 식당명·메뉴명·가격을 표시한다.
        menuCombo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list,
                    Object value, int index, boolean selected, boolean focus) {
                super.getListCellRendererComponent(list, value, index, selected, focus);
                if (value instanceof MenuItem item) {
                    setText(String.format("[%s] %s %,d원", item.restaurantName(), item.name(), item.price()));
                }
                return this;
            }
        });
        JButton newButton = new JButton("새 세트");
        JButton deleteButton = new JButton("세트 삭제");
        JPanel selection = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savedCombo.setPrototypeDisplayValue(new CustomSet("저장한 사용자 세트 선택", List.of(new CustomSet.Entry("preview", 1))));
        selection.add(new JLabel("저장한 세트"));
        selection.add(savedCombo);
        selection.add(newButton);
        selection.add(deleteButton);
        JPanel naming = new JPanel(new FlowLayout(FlowLayout.LEFT));
        naming.add(new JLabel("세트 이름"));
        naming.add(nameField);
        JPanel adding = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("메뉴 추가");
        JButton removeButton = new JButton("선택 메뉴 삭제");
        adding.add(menuCombo);
        adding.add(new JLabel("수량"));
        adding.add(qtySpinner);
        adding.add(addButton);
        adding.add(removeButton);
        JPanel top = new JPanel(new GridLayout(3, 1));
        top.add(selection);
        top.add(naming);
        top.add(adding);
        table.setRowHeight(27);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("세트 구성 (수량 칸을 더블클릭하여 수정)"));
        JButton saveButton = new JButton("세트 저장");
        JButton closeButton = new JButton("닫기");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(new JLabel("저장한 변경만 적용됩니다."));
        bottom.add(saveButton);
        bottom.add(closeButton);
        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        savedCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSelection();
            }
        });
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savedCombo.setSelectedIndex(-1);
                loadSelection();
                nameField.requestFocusInWindow();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMenu();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();
                if (table.isEditing()) {
                    table.getCellEditor().cancelCellEditing();
                }
                for (int i = rows.length - 1; i >= 0; i--) {
                    model.removeRow(rows[i]);
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSet();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSet();
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        refreshChoices(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 480);
        setLocationRelativeTo(owner);
    }

    public List<CustomSet> savedSets() {
        return List.copyOf(sets);
    }

    private void refreshChoices(CustomSet selected) {
        savedCombo.removeAllItems();
        for (CustomSet set : sets) { savedCombo.addItem(set); }
        savedCombo.setSelectedItem(selected);
        loadSelection();
    }

    private void loadSelection() {
        if (table.isEditing()) { table.getCellEditor().cancelCellEditing(); }
        model.setRowCount(0);
        CustomSet selected = (CustomSet) savedCombo.getSelectedItem();
        nameField.setText(selected == null ? "" : selected.name());
        if (selected != null) {
            for (CustomSet.Entry entry : selected.entries()) {
                MenuItem item = findMenu(entry.menuId());
                model.addRow(new Object[]{entry.menuId(), item == null ? "현재 메뉴에 없음" : item.name(),
                        item == null ? "-" : String.format("%,d원", item.price()), entry.qty()});
            }
        }
    }

    private boolean commitQuantity() {
        if (table.isEditing() && !table.getCellEditor().stopCellEditing()) {
            showError(new IllegalArgumentException("수량 칸에 올바른 정수를 입력해주세요."));
            return false;
        }
        return true;
    }

    private void addMenu() {
        if (!commitQuantity()) { return; }
        try {
            qtySpinner.commitEdit();
            MenuItem item = (MenuItem) menuCombo.getSelectedItem();
            if (item == null) { throw new IllegalArgumentException("메뉴를 선택해주세요."); }
            int qty = (Integer) qtySpinner.getValue();
            for (int row = 0; row < model.getRowCount(); row++) {
                if (model.getValueAt(row, 0).equals(item.menuId())) {
                    model.setValueAt(Math.addExact((Integer) model.getValueAt(row, 3), qty), row, 3);
                    return;
                }
            }
            model.addRow(new Object[]{item.menuId(), item.name(), String.format("%,d원", item.price()), qty});
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void saveSet() {
        if (!commitQuantity()) { return; }
        try {
            List<CustomSet.Entry> entries = new ArrayList<>();
            for (int row = 0; row < model.getRowCount(); row++) {
                String id = (String) model.getValueAt(row, 0);
                if (findMenu(id) == null) {
                    throw new IllegalArgumentException("현재 메뉴에 없는 항목을 삭제해주세요: " + id);
                }
                entries.add(new CustomSet.Entry(id, (Integer) model.getValueAt(row, 3)));
            }
            CustomSet edited = new CustomSet(nameField.getText(), entries);
            List<CustomSet> updated = new ArrayList<>(sets);
            int index = sets.indexOf(savedCombo.getSelectedItem());
            if (index >= 0) { updated.set(index, edited); } else { updated.add(edited); }
            store.save(updated);
            sets.clear();
            sets.addAll(updated);
            refreshChoices(edited);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void deleteSet() {
        CustomSet selected = (CustomSet) savedCombo.getSelectedItem();
        if (selected == null) { return; }
        try {
            List<CustomSet> updated = new ArrayList<>(sets);
            updated.remove(selected);
            store.save(updated);
            sets.clear();
            sets.addAll(updated);
            refreshChoices(null);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "세트 편집 오류", JOptionPane.ERROR_MESSAGE);
    }

    private MenuItem findMenu(String menuId) {
        for (MenuItem item : menus) {
            if (item.menuId().equals(menuId)) {
                return item;
            }
        }
        return null;
    }
}
// --- yourCode 끝 ---
