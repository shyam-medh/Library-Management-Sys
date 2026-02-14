import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class availableBook extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color CARD_BG = new Color(40, 40, 65);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color ACCENT_BLUE = new Color(66, 133, 244);
    
    public availableBook() {
        setTitle("Available Books");
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
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        
        JLabel title = new JLabel("📖 Available Books", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        topPanel.add(title, gbc);
        
        // Search Row
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.weightx = 1.0;
        
        searchField = new JTextField("Search books...");
        searchField.setPreferredSize(new Dimension(0, 40));
        searchField.setBackground(new Color(45, 45, 70));
        searchField.setForeground(new Color(150, 150, 180));
        searchField.setCaretColor(TEXT_WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70,70,100)), BorderFactory.createEmptyBorder(5,15,5,15)));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search books...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_WHITE);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search books...");
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
        
        JButton refreshBtn = createBtn("Refresh", ACCENT_BLUE);
        refreshBtn.addActionListener(e -> loadData());
        topPanel.add(refreshBtn, gbc);
        
        gbc.gridx = 2;
        JButton backBtn = createBtn("Back", new Color(80, 80, 110));
        backBtn.addActionListener(e -> dispose());
        topPanel.add(backBtn, gbc);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // --- Table ---
        String[] columns = {"Book ID", "Name", "Author", "Category", "Total", "Issued", "Available"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 4) return Integer.class; // Sort numbers correctly
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
                            int cellWidth = getCellRect(viewRow, viewCol, false).width;
                            FontMetrics fm = getFontMetrics(getFont());
                            if (fm.stringWidth(text) > (cellWidth - 5)) {
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
                tip.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
                tip.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return tip;
            }
            
            // Location: Follow mouse with offset
             public Point getToolTipLocation(MouseEvent e) {
                return new Point(e.getX() + 15, e.getY() + 15);
            }
        };
        ToolTipManager.sharedInstance().registerComponent(table);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(60, 60, 90));
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(TEXT_WHITE);
        table.setFillsViewportHeight(true);
        
        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setDefaultRenderer((t, val, sel, foc, r, c) -> {
            JLabel lbl = new JLabel(val.toString(), SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(50, 50, 80));
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, ACCENT_BLUE));
            return lbl;
        });
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(DARK_BG); // Dark BG for area below rows
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // Margin inside scroll? No, margin around scroll.
        // Actually margin around scroll:
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(new EmptyBorder(0, 20, 20, 20)); // Margin Left/Right/Bottom
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
            
            // Note: Using the view 'available_books_view' as in previous code.
            // If view missing, this throws. But assuming persistent logic.
            // Fallback query logic:
            // "SELECT b.BOOK_ID, b.NAME, b.AUTHOR, b.CATEGORY, b.TOTAL_COPIES, ...?"
            // I'll stick to the previous code's query.
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM available_books_view");
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("BOOK_ID"), rs.getString("NAME"), rs.getString("AUTHOR"),
                        rs.getString("CATEGORY"), rs.getInt("TOTAL_COPIES"),
                        rs.getInt("Issued_Copies"), rs.getInt("Available_Copies")
                    });
                }
            } catch (SQLException e) {
                // Determine if it is 'view not found'
                if (e.getMessage().contains("doesn't exist")) {
                    // Fallback: Manually calculate
                     Statement st = conn.createStatement();
                     // Simplified query logic if view missing
                     ResultSet rs = st.executeQuery("SELECT b.BOOK_ID, b.NAME, b.AUTHOR, b.CATEGORY, b.COPIES as Available_Copies, b.TOTAL_COPIES, (b.TOTAL_COPIES - b.COPIES) as Issued_Copies FROM book b");
                     while (rs.next()) {
                         model.addRow(new Object[]{
                            rs.getString("BOOK_ID"), rs.getString("NAME"), rs.getString("AUTHOR"),
                            rs.getString("CATEGORY"), rs.getInt("TOTAL_COPIES"),
                            rs.getInt("Issued_Copies"), rs.getInt("Available_Copies")
                        });
                     }
                } else throw e;
            }
        } catch (SQLException ex) { 
             JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); 
        }
    }
    
    private void filterTable() {
        String search = searchField.getText();
        if (search.equals("Search books...")) search = "";
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        if (search.isEmpty()) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search));
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new availableBook().setVisible(true)); }
}
