import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IssueBook extends JFrame implements ActionListener {
    private JComboBox<String> studentCombo, bookCombo;
    private JTextField dateField;
    private JButton issueBtn, clearBtn, backBtn;
    private JLabel messageLabel;
    
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color FIELD_BG = new Color(45, 45, 70);
    private final Color ACCENT_TEAL = new Color(90, 200, 250);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(180, 180, 200);
    
    public IssueBook() {
        setTitle("Issue Book");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        createUI();
        loadComboData();
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
        
        JLabel title = new JLabel("📤 Issue Book", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        title.setBounds(0, 40, 550, 40);
        mainPanel.add(title);
        
        int y = 120;
        mainPanel.add(createLabel("Student ID", 75, y));
        studentCombo = new JComboBox<>();
        styleCombo(studentCombo);
        studentCombo.setBounds(75, y + 25, 400, 45);
        mainPanel.add(studentCombo);
        
        y += 90;
        mainPanel.add(createLabel("Book ID", 75, y));
        bookCombo = new JComboBox<>();
        styleCombo(bookCombo);
        bookCombo.setBounds(75, y + 25, 400, 45);
        mainPanel.add(bookCombo);
        
        y += 90;
        mainPanel.add(createLabel("Issue Date (yyyy-MM-dd)", 75, y));
        dateField = createField();
        dateField.setBounds(75, y + 25, 400, 45);
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        mainPanel.add(dateField);
        
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setBounds(75, 410, 400, 25);
        mainPanel.add(messageLabel);
        
        issueBtn = createBtn("Issue Book", ACCENT_TEAL); issueBtn.setBounds(75, 450, 120, 45);
        clearBtn = createBtn("Clear", new Color(66, 133, 244)); clearBtn.setBounds(215, 450, 120, 45);
        backBtn = createBtn("Back", new Color(100, 100, 130)); backBtn.setBounds(355, 450, 120, 45);
        mainPanel.add(issueBtn); mainPanel.add(clearBtn); mainPanel.add(backBtn);
        
        setContentPane(mainPanel);
    }
    
    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_GRAY);
        lbl.setBounds(x, y, 300, 20);
        return lbl;
    }
    
    private JTextField createField() {
        JTextField f = new JTextField();
        f.setBackground(FIELD_BG);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(TEXT_WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70,70,100)), BorderFactory.createEmptyBorder(10,15,10,15)));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return f;
    }
    
    private void styleCombo(JComboBox<String> c) {
        c.setBackground(new Color(60, 60, 90));
        c.setForeground(new Color(255, 255, 255));
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 120)));
        c.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? new Color(90, 200, 250) : new Color(50, 50, 80));
                setForeground(new Color(255, 255, 255));
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return this;
            }
        });
    }
    
    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(new Color(30, 30, 30));
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }
    
    private void loadComboData() {
        try (Connection conn = Connect.Connection()) {
            if (conn == null) return;
            
            studentCombo.addItem("Select Student");
            Statement st1 = conn.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT Stu_ID, NAME FROM student");
            while (rs1.next()) studentCombo.addItem(rs1.getString("Stu_ID") + " - " + rs1.getString("NAME"));
            
            bookCombo.addItem("Select Book");
            Statement st2 = conn.createStatement();
            ResultSet rs2 = st2.executeQuery("SELECT BOOK_ID, NAME, COPIES FROM book WHERE COPIES > 0");
            while (rs2.next()) bookCombo.addItem(rs2.getString("BOOK_ID") + " - " + rs2.getString("NAME") + " (" + rs2.getInt("COPIES") + " available)");
        } catch (SQLException ex) { showMsg("Error loading data: " + ex.getMessage(), true); }
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == issueBtn) issueBook();
        else if (e.getSource() == clearBtn) clearFields();
        else if (e.getSource() == backBtn) dispose();
    }
    
    private void issueBook() {
        String student = (String) studentCombo.getSelectedItem();
        String book = (String) bookCombo.getSelectedItem();
        String date = dateField.getText().trim();
        
        if (student.equals("Select Student") || book.equals("Select Book") || date.isEmpty()) {
            showMsg("All fields are required!", true); return;
        }
        
        String stuId = student.split(" - ")[0];
        String bookId = book.split(" - ")[0];
        
        try (Connection conn = Connect.Connection()) {
            if (conn == null) { showMsg("Database connection failed!", true); return; }
            conn.setAutoCommit(false);
            
            // Check book availability
            PreparedStatement checkBook = conn.prepareStatement("SELECT COPIES FROM book WHERE BOOK_ID=?");
            checkBook.setString(1, bookId);
            ResultSet rs = checkBook.executeQuery();
            if (!rs.next() || rs.getInt("COPIES") <= 0) {
                showMsg("Book not available!", true); conn.rollback(); return;
            }
            
            // Insert issue record
            PreparedStatement insertIssue = conn.prepareStatement("INSERT INTO issue (Stu_ID,BOOK_ID,ISSUE_DATE) VALUES (?,?,?)");
            insertIssue.setString(1, stuId);
            insertIssue.setString(2, bookId);
            insertIssue.setString(3, date);
            insertIssue.executeUpdate();
            
            // Decrement copies
            PreparedStatement updateBook = conn.prepareStatement("UPDATE book SET COPIES=COPIES-1 WHERE BOOK_ID=?");
            updateBook.setString(1, bookId);
            updateBook.executeUpdate();
            
            // Update status if no copies left
            PreparedStatement updateStatus = conn.prepareStatement("UPDATE book SET STATUS='Not Available' WHERE BOOK_ID=? AND COPIES=0");
            updateStatus.setString(1, bookId);
            updateStatus.executeUpdate();
            
            conn.commit();
            showMsg("Book issued successfully!", false);
            refreshBookCombo();
        } catch (SQLException ex) { showMsg("Error: " + ex.getMessage(), true); }
    }
    
    private void refreshBookCombo() {
        bookCombo.removeAllItems();
        try (Connection conn = Connect.Connection()) {
            if (conn == null) return;
            bookCombo.addItem("Select Book");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT BOOK_ID, NAME, COPIES FROM book WHERE COPIES > 0");
            while (rs.next()) bookCombo.addItem(rs.getString("BOOK_ID") + " - " + rs.getString("NAME") + " (" + rs.getInt("COPIES") + " available)");
        } catch (SQLException ex) { /* ignore */ }
    }
    
    private void clearFields() {
        studentCombo.setSelectedIndex(0);
        bookCombo.setSelectedIndex(0);
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        messageLabel.setText("");
    }
    
    private void showMsg(String msg, boolean error) {
        messageLabel.setForeground(error ? new Color(255,85,85) : new Color(85,255,85));
        messageLabel.setText(msg);
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new IssueBook().setVisible(true)); }
}
