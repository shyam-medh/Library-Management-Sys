import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class studentRegistration extends JFrame implements ActionListener {
    private JTextField stuIdField, nameField, contactField;
    private JButton registerBtn, clearBtn, backBtn;
    private JLabel messageLabel;
    
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color FIELD_BG = new Color(45, 45, 70);
    private final Color ACCENT_PURPLE = new Color(129, 52, 175);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(180, 180, 200);
    
    public studentRegistration() {
        setTitle("Student Registration");
        setSize(550, 550);
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
        
        JLabel title = new JLabel("👤 Student Registration", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        title.setBounds(0, 40, 550, 40);
        mainPanel.add(title);
        
        int y = 120;
        mainPanel.add(createLabel("Student ID", 75, y));
        stuIdField = createField(); stuIdField.setBounds(75, y + 25, 400, 45);
        mainPanel.add(stuIdField);
        
        y += 90;
        mainPanel.add(createLabel("Student Name", 75, y));
        nameField = createField(); nameField.setBounds(75, y + 25, 400, 45);
        mainPanel.add(nameField);
        
        y += 90;
        mainPanel.add(createLabel("Contact Number (10 digits)", 75, y));
        contactField = createField(); contactField.setBounds(75, y + 25, 400, 45);
        mainPanel.add(contactField);
        
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setBounds(75, 410, 400, 25);
        mainPanel.add(messageLabel);
        
        registerBtn = createBtn("Register", ACCENT_PURPLE); registerBtn.setBounds(75, 450, 120, 45);
        clearBtn = createBtn("Clear", new Color(66, 133, 244)); clearBtn.setBounds(215, 450, 120, 45);
        backBtn = createBtn("Back", new Color(100, 100, 130)); backBtn.setBounds(355, 450, 120, 45);
        mainPanel.add(registerBtn); mainPanel.add(clearBtn); mainPanel.add(backBtn);
        
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
        if (e.getSource() == registerBtn) registerStudent();
        else if (e.getSource() == clearBtn) clearFields();
        else if (e.getSource() == backBtn) dispose();
    }
    
    private void registerStudent() {
        String id = stuIdField.getText().trim();
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        
        if (id.isEmpty() || name.isEmpty() || contact.isEmpty()) {
            showMsg("All fields are required!", true); return;
        }
        if (!contact.matches("\\d{10}")) {
            showMsg("Contact must be 10 digits!", true); return;
        }
        
        try (Connection conn = Connect.Connection()) {
            if (conn == null) { showMsg("Database connection failed!", true); return; }
            
            PreparedStatement check = conn.prepareStatement("SELECT Stu_ID FROM student WHERE Stu_ID=?");
            check.setString(1, id);
            if (check.executeQuery().next()) { showMsg("Student ID already exists!", true); return; }
            
            PreparedStatement pst = conn.prepareStatement("INSERT INTO student (Stu_ID,NAME,CONTACT) VALUES (?,?,?)");
            pst.setString(1, id); pst.setString(2, name); pst.setString(3, contact);
            if (pst.executeUpdate() > 0) { showMsg("Student registered successfully!", false); clearFields(); }
        } catch (SQLException ex) { showMsg("Error: " + ex.getMessage(), true); }
    }
    
    private void clearFields() {
        stuIdField.setText(""); nameField.setText(""); contactField.setText(""); messageLabel.setText("");
    }
    
    private void showMsg(String msg, boolean error) {
        messageLabel.setForeground(error ? new Color(255,85,85) : new Color(85,255,85));
        messageLabel.setText(msg);
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new studentRegistration().setVisible(true)); }
}
