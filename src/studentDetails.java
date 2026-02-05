import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

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
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, DARK_BG, 0, getHeight(), new Color(25, 25, 45)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        
        JLabel title = new JLabel("📋 Student Details", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        title.setBounds(0, 20, 900, 40);
        mainPanel.add(title);
        
        // Search bar
        searchField = new JTextField("Search students...");
        searchField.setBounds(50, 75, 600, 40);
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
        mainPanel.add(searchField);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(670, 75, 100, 40);
        refreshBtn.setBackground(ACCENT_ORANGE);
        refreshBtn.setForeground(new Color(30, 30, 30));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setOpaque(true);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadData());
        mainPanel.add(refreshBtn);
        
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(780, 75, 80, 40);
        backBtn.setBackground(new Color(80, 80, 110));
        backBtn.setForeground(TEXT_WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        mainPanel.add(backBtn);
        
        // Table
        String[] columns = {"Student ID", "Name", "Contact", "Total Issued", "Returned", "Currently Issued"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(60, 60, 90));
        table.setSelectionBackground(ACCENT_ORANGE);
        table.setSelectionForeground(new Color(30, 30, 30));
        
        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel lbl = new JLabel(val.toString(), SwingConstants.CENTER);
                lbl.setOpaque(true);
                lbl.setBackground(new Color(50, 50, 80));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, ACCENT_ORANGE));
                return lbl;
            }
        });
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 130, 800, 400);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 90)));
        mainPanel.add(scroll);
        
        setContentPane(mainPanel);
    }
    
    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = Connect.Connection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM student_details_view");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("Stu_ID"), rs.getString("NAME"), rs.getString("CONTACT"),
                    rs.getInt("Total_Issued"), rs.getInt("Returned_Books"), rs.getInt("Currently_Issued")
                });
            }
        } catch (SQLException ex) { 
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); 
        }
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
