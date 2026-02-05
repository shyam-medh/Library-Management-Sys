import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class addBook extends JFrame implements ActionListener {
    private JTextField bookIdField, bookNameField, authorField, copiesField;
    private JComboBox<String> categoryCombo;
    private JButton addButton, clearButton, backButton;
    private JLabel messageLabel;
    
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color CARD_BG = new Color(40, 40, 65);
    private final Color FIELD_BG = new Color(45, 45, 70);
    private final Color ACCENT_GREEN = new Color(52, 199, 89);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(180, 180, 200);
    
    private final String[] CATEGORIES = {"Select Category", "Fiction", "Science", "Technology", "Biography", "Fantasy", "Romance", "Mystery", "Education", "Novel"};
    
    public addBook() {
        setTitle("Add New Book");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        createUI();
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
        
        JLabel title = new JLabel("➕ Add New Book", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        title.setBounds(0, 40, 600, 40);
        mainPanel.add(title);
        
        int y = 120;
        mainPanel.add(createLabel("Book ID", 100, y));
        bookIdField = createField(); bookIdField.setBounds(100, y + 25, 400, 45);
        mainPanel.add(bookIdField);
        
        y += 85;
        mainPanel.add(createLabel("Category", 100, y));
        categoryCombo = new JComboBox<>(CATEGORIES);
        styleCombo(categoryCombo);
        categoryCombo.setBounds(100, y + 25, 400, 45);
        mainPanel.add(categoryCombo);
        
        y += 85;
        mainPanel.add(createLabel("Book Name", 100, y));
        bookNameField = createField(); bookNameField.setBounds(100, y + 25, 400, 45);
        mainPanel.add(bookNameField);
        
        y += 85;
        mainPanel.add(createLabel("Author Name", 100, y));
        authorField = createField(); authorField.setBounds(100, y + 25, 400, 45);
        mainPanel.add(authorField);
        
        y += 85;
        mainPanel.add(createLabel("Number of Copies", 100, y));
        copiesField = createField(); copiesField.setBounds(100, y + 25, 400, 45);
        mainPanel.add(copiesField);
        
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setBounds(100, 560, 400, 25);
        mainPanel.add(messageLabel);
        
        addButton = createBtn("Add Book", ACCENT_GREEN); addButton.setBounds(100, 600, 120, 45);
        clearButton = createBtn("Clear", new Color(66, 133, 244)); clearButton.setBounds(240, 600, 120, 45);
        backButton = createBtn("Back", new Color(100, 100, 130)); backButton.setBounds(380, 600, 120, 45);
        mainPanel.add(addButton); mainPanel.add(clearButton); mainPanel.add(backButton);
        
        setContentPane(mainPanel);
    }
    
    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_GRAY);
        lbl.setBounds(x, y, 200, 20);
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
                setBackground(isSelected ? new Color(66, 133, 244) : new Color(50, 50, 80));
                setForeground(new Color(255, 255, 255));
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return this;
            }
        });
    }
    
    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(new Color(255, 255, 255));
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) addBookToDB();
        else if (e.getSource() == clearButton) clearFields();
        else if (e.getSource() == backButton) dispose();
    }
    
    private void addBookToDB() {
        String id = bookIdField.getText().trim();
        String cat = (String) categoryCombo.getSelectedItem();
        String name = bookNameField.getText().trim();
        String author = authorField.getText().trim();
        String copiesStr = copiesField.getText().trim();
        
        if (id.isEmpty() || cat.equals("Select Category") || name.isEmpty() || author.isEmpty() || copiesStr.isEmpty()) {
            showMsg("All fields are required!", true); return;
        }
        int copies;
        try { copies = Integer.parseInt(copiesStr); if (copies <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { showMsg("Invalid number of copies!", true); return; }
        
        try (Connection conn = Connect.Connection()) {
            if (conn == null) { showMsg("Database connection failed!", true); return; }
            PreparedStatement check = conn.prepareStatement("SELECT BOOK_ID FROM book WHERE BOOK_ID=?");
            check.setString(1, id);
            if (check.executeQuery().next()) { showMsg("Book ID already exists!", true); return; }
            
            PreparedStatement pst = conn.prepareStatement("INSERT INTO book (BOOK_ID,CATEGORY,NAME,AUTHOR,COPIES) VALUES (?,?,?,?,?)");
            pst.setString(1, id); pst.setString(2, cat); pst.setString(3, name); pst.setString(4, author); pst.setInt(5, copies);
            if (pst.executeUpdate() > 0) { showMsg("Book added successfully!", false); clearFields(); }
        } catch (SQLException ex) { showMsg("Error: " + ex.getMessage(), true); }
    }
    
    private void clearFields() {
        bookIdField.setText(""); categoryCombo.setSelectedIndex(0); bookNameField.setText("");
        authorField.setText(""); copiesField.setText(""); messageLabel.setText("");
    }
    
    private void showMsg(String msg, boolean error) {
        messageLabel.setForeground(error ? new Color(255,85,85) : new Color(85,255,85));
        messageLabel.setText(msg);
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new addBook().setVisible(true)); }
}
