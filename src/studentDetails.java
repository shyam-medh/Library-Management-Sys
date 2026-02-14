import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class studentDetails extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color CARD_BG = new Color(40, 40, 65);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color ACCENT_ORANGE = new Color(255, 149, 0);
    
    public studentDetails() {
        setTitle("Student Details");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createUI();
        loadData();
    }
    
    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setPaint(new GradientPaint(0, 0, DARK_BG, 0, getHeight(), new Color(25, 25, 45)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(mainPanel);
        
        // --- Top Panel (Title + Search) ---
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margin
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        
        JLabel title = new JLabel("📋 Student Details", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        topPanel.add(title, gbc);
        
        // Search Row
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.weightx = 1.0;
        
        searchField = new JTextField("Search students...");
        searchField.setPreferredSize(new Dimension(0, 40));
        searchField.setBackground(new Color(45, 45, 70));
        searchField.setForeground(new Color(150, 150, 180));
        searchField.setCaretColor(TEXT_WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70,70,100)), BorderFactory.createEmptyBorder(5,15,5,15)));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search students...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_WHITE);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search students...");
                    searchField.setForeground(new Color(150, 150, 180));
                }
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterTable(); }
        });
        topPanel.add(searchField, gbc);
        
        // Buttons
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        
        JButton refreshBtn = createBtn("Refresh", ACCENT_ORANGE);
        refreshBtn.setForeground(new Color(30,30,30)); // Dark text on orange
        refreshBtn.addActionListener(e -> loadData());
        topPanel.add(refreshBtn, gbc);
        
        gbc.gridx = 2;
        JButton backBtn = createBtn("Back", new Color(80, 80, 110));
        backBtn.addActionListener(e -> dispose());
        topPanel.add(backBtn, gbc);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // --- Table ---
        String[] columns = {"Student ID", "Name", "Contact", "Total Issued", "Returned", "Currently Issued"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3) return Integer.class;
                return String.class;
            }
        };
        
        table = new JTable(model) {
            // Smart Tooltip: Only show if text is truncated
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int viewRow = rowAtPoint(p);
                int viewCol = columnAtPoint(p);
                if (viewRow != -1 && viewCol != -1) {
                    try {
                        Object val = getValueAt(viewRow, viewCol);
                        if (val != null) {
                            String text = val.toString();
                            // Precise check: Is text wider than the cell?
                            int cellWidth = getCellRect(viewRow, viewCol, false).width;
                            FontMetrics fm = getFontMetrics(getFont());
                            int textWidth = fm.stringWidth(text);
                            
                            // If text width is greater than cell width (minus padding), show tooltip
                            if (textWidth > (cellWidth - 5)) {
                                tip = text;
                            }
                        }
                    } catch (RuntimeException e1) { }
                }
                return tip;
            }
            
            // Custom Dark Tooltip
            public JToolTip createToolTip() {
                JToolTip tip = super.createToolTip();
                tip.setBackground(new Color(40, 40, 65));
                tip.setForeground(Color.WHITE);
                tip.setBorder(BorderFactory.createLineBorder(ACCENT_ORANGE)); 
                tip.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return tip;
            }
             public Point getToolTipLocation(MouseEvent e) {
                return new Point(e.getX() + 15, e.getY() + 15);
            }
        };
        // CRITICAL: Enable tooltips mechanism
        ToolTipManager.sharedInstance().registerComponent(table);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(60, 60, 90));
        table.setSelectionBackground(ACCENT_ORANGE);
        table.setSelectionForeground(new Color(30, 30, 30));
        table.setFillsViewportHeight(true);
        
        // Header
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setDefaultRenderer((t, val, sel, foc, r, c) -> {
            JLabel lbl = new JLabel(val.toString(), SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(50, 50, 80));
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, ACCENT_ORANGE));
            return lbl;
        });
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(DARK_BG);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        tablePanel.add(scroll, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
    }
    
    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(90, 40));
        btn.setBackground(bg);
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = Connect.Connection()) {
            if (conn == null) { JOptionPane.showMessageDialog(this, "Database connection failed!"); return; }
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM student_details_view");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("Stu_ID"), rs.getString("NAME"), rs.getString("CONTACT"),
                    rs.getInt("Total_Issued"), rs.getInt("Returned_Books"), rs.getInt("Currently_Issued")
                });
            }
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }
    
    private void filterTable() {
        String search = searchField.getText();
        if (search.equals("Search students...")) search = "";
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        if (search.isEmpty()) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search));
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new studentDetails().setVisible(true)); }
}
