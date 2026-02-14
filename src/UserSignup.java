import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import javax.swing.Timer;

/**
 * User Signup Screen - Responsive & Modern
 */
public class UserSignup extends JFrame implements ActionListener {
    
    // UI Components
    private JTextField usernameField, nameField, contactField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signupButton, backToLoginButton;
    private JLabel messageLabel;
    
    // Colors
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color ACCENT_GREEN = new Color(52, 199, 89);
    private final Color ACCENT_HOVER = new Color(72, 219, 109);
    private final Color ACCENT_BLUE = new Color(66, 133, 244);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(180, 180, 200);
    private final Color FIELD_BG = new Color(45, 45, 70);
    private final Color ERROR_RED = new Color(255, 85, 85);
    private final Color SUCCESS_GREEN = new Color(85, 255, 85);
    
    public UserSignup() {
        initializeFrame();
        createComponents();
    }
    
    private void initializeFrame() {
        setTitle("Library Management System - Sign Up");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }
    
    private void createComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        setContentPane(mainPanel);
        
        // --- LEFT SIDE: Branding ---
        JPanel leftPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, ACCENT_GREEN, getWidth(), getHeight(), new Color(52, 175, 129));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Decorative circles
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillOval(-100, -100, 400, 400);
                g2d.fillOval(getWidth()-200, getHeight()-200, 300, 300);
            }
        };
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridwidth = GridBagConstraints.REMAINDER;
        gbcLeft.insets = new Insets(10, 0, 10, 0);
        
        JLabel libraryIcon = new JLabel("📝");
        libraryIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        libraryIcon.setForeground(TEXT_WHITE);
        leftPanel.add(libraryIcon, gbcLeft);
        
        JLabel heading = new JLabel("Join Our Community");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setForeground(TEXT_WHITE);
        leftPanel.add(heading, gbcLeft);
        
        JLabel subheading = new JLabel("Create your account today!");
        subheading.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        subheading.setForeground(new Color(255, 255, 255, 200));
        leftPanel.add(subheading, gbcLeft);
        
        mainPanel.add(leftPanel);
        
        // --- RIGHT SIDE: Form ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(DARK_BG);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.weightx = 1.0;
        
        JLabel titleLabel = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_WHITE);
        gbc.insets = new Insets(20, 40, 30, 40);
        rightPanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(5, 40, 5, 40);
        
        rightPanel.add(createLabel("Full Name"), gbc);
        nameField = createStyledTextField();
        rightPanel.add(nameField, gbc);
        
        rightPanel.add(createLabel("Username"), gbc);
        usernameField = createStyledTextField();
        rightPanel.add(usernameField, gbc);
        
        rightPanel.add(createLabel("Contact Number"), gbc);
        contactField = createStyledTextField();
        rightPanel.add(contactField, gbc);
        
        // Passwords row
        JPanel passPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        passPanel.setOpaque(false);
        
        JPanel p1 = new JPanel(new BorderLayout()); p1.setOpaque(false);
        p1.add(createLabel("Password"), BorderLayout.NORTH);
        passwordField = createStyledPasswordField();
        p1.add(passwordField, BorderLayout.CENTER);
        
        JPanel p2 = new JPanel(new BorderLayout()); p2.setOpaque(false);
        p2.add(createLabel("Confirm Password"), BorderLayout.NORTH);
        confirmPasswordField = createStyledPasswordField();
        p2.add(confirmPasswordField, BorderLayout.CENTER);
        
        passPanel.add(p1);
        passPanel.add(p2);
        
        rightPanel.add(passPanel, gbc);
        
        // Message
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(ERROR_RED);
        gbc.insets = new Insets(10, 40, 10, 40);
        rightPanel.add(messageLabel, gbc);
        
        // Buttons
        gbc.insets = new Insets(20, 40, 10, 40);
        signupButton = createBtn("SIGN UP", ACCENT_GREEN);
        signupButton.setPreferredSize(new Dimension(0, 45));
        rightPanel.add(signupButton, gbc);
        
        gbc.insets = new Insets(10, 40, 10, 40);
        backToLoginButton = createBtn("BACK TO LOGIN", new Color(60, 60, 90));
        backToLoginButton.setPreferredSize(new Dimension(0, 45));
        // Custom styling for back button border
        backToLoginButton.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
        backToLoginButton.setBorderPainted(true);
        rightPanel.add(backToLoginButton, gbc);
        
        mainPanel.add(rightPanel);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_GRAY);
        return lbl;
    }
    
    private JTextField createStyledTextField() {
        JTextField f = new JTextField();
        f.setBackground(FIELD_BG);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(TEXT_WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new Color(70, 70, 100)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 40));
        return f;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setBackground(FIELD_BG);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(TEXT_WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new Color(70, 70, 100)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 40));
        return f;
    }
    
    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(bg.darker());
                else if (getModel().isRollover()) g2.setColor(bg.brighter());
                else g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT_WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) performSignup();
        else if (e.getSource() == backToLoginButton) {
            new Loading1().setVisible(true);
            dispose();
        }
    }
    
    // Original Logic Preserved
    private void performSignup() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String contact = contactField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (name.isEmpty() || username.isEmpty() || contact.isEmpty() || password.isEmpty()) { showError("Please fill in all fields"); return; }
        if (username.length() < 4) { showError("Username must be at least 4 characters"); return; }
        if (password.length() < 6) { showError("Password must be at least 6 characters"); return; }
        if (!password.equals(confirmPassword)) { showError("Passwords do not match"); confirmPasswordField.setText(""); return; }
        if (!contact.matches("\\d{10}")) { showError("Please enter a valid 10-digit contact number"); return; }
        
        try (Connection conn = Connect.Connection()) {
            if (conn == null) { showError("Database connection failed!"); return; }
            
            // Check Admin
            PreparedStatement pst = conn.prepareStatement("SELECT USER_ID FROM admin WHERE USERNAME = ?");
            pst.setString(1, username); // Note: Admin table usually has USERNAME column? Previous code used USER_ID in SELECT...
            // Wait, previous code: "SELECT USER_ID FROM admin WHERE USER_ID = ?"
            // AND "SELECT USER_ID FROM users WHERE USER_ID = ?"
            // Loading1 used: "SELECT * FROM admin WHERE USERNAME = ?"
            // Consistency issue. I should allow username to be checked against 'USERNAME' column for admin if that's what Loading1 uses.
            // But UserSignup used "USER_ID" column check.
            // I'll stick to what UserSignup had: "USER_ID".
            
            // Actually, best to replicate exact logic from previous `UserSignup`.
            // "SELECT USER_ID FROM admin WHERE USER_ID = ?"
            
            // Let's stick to the previous code logic to be safe.
             PreparedStatement check1 = conn.prepareStatement("SELECT USER_ID FROM admin WHERE USER_ID = ?");
             check1.setString(1, username);
             if (check1.executeQuery().next()) { showError("Username already exists!"); return; }
             
             PreparedStatement check2 = conn.prepareStatement("SELECT USER_ID FROM users WHERE USER_ID = ?");
             check2.setString(1, username);
             if (check2.executeQuery().next()) { showError("Username already exists!"); return; }
             
             PreparedStatement ins = conn.prepareStatement("INSERT INTO users (USER_ID, NAME, PASSWORD, CONTACT) VALUES (?, ?, ?, ?)");
             ins.setString(1, username); ins.setString(2, name); ins.setString(3, password); ins.setString(4, contact);
             
             if (ins.executeUpdate() > 0) {
                 messageLabel.setForeground(SUCCESS_GREEN);
                 messageLabel.setText("Account created! Redirecting...");
                 Timer t = new Timer(1500, evt -> { new Loading1().setVisible(true); dispose(); });
                 t.setRepeats(false); t.start();
             }
        } catch (SQLException ex) { showError("Error: " + ex.getMessage()); }
    }
    
    private void showError(String msg) {
        messageLabel.setForeground(ERROR_RED);
        messageLabel.setText(msg);
        shakeWindow();
    }

    private void shakeWindow() {
        final int originalX = getLocation().x;
        final int originalY = getLocation().y;
        Timer shakeTimer = new Timer(30, null);
        final int[] count = {0};
        shakeTimer.addActionListener(e -> {
            if (count[0] < 10) {
                int offsetX = (count[0] % 2 == 0) ? 10 : -10;
                setLocation(originalX + offsetX, originalY);
                count[0]++;
            } else { setLocation(originalX, originalY); shakeTimer.stop(); }
        });
        shakeTimer.start();
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new UserSignup().setVisible(true)); }
}
