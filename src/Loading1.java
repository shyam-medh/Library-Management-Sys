import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Login Screen - Entry Point for Library Management System
 * Window Size: 1080x720, centered, non-resizable
 * Dark theme with gradient background
 */
public class Loading1 extends JFrame implements ActionListener {
    
    // UI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox rememberMeCheckbox;
    private JLabel messageLabel;
    
    // Colors for dark theme
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color DARK_SECONDARY = new Color(30, 30, 50);
    private final Color ACCENT_BLUE = new Color(66, 133, 244);
    private final Color ACCENT_HOVER = new Color(86, 153, 255);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(180, 180, 200);
    private final Color FIELD_BG = new Color(45, 45, 70);
    private final Color ERROR_RED = new Color(255, 85, 85);
    
    public Loading1() {
        initializeFrame();
        createComponents();
    }
    
    private void initializeFrame() {
        setTitle("Library Management System - Login");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
    }
    
    private void createComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, DARK_BG, 0, getHeight(), new Color(25, 25, 45));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBounds(0, 0, 1080, 720);
        mainPanel.setLayout(null);
        
        // Left decorative panel
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, ACCENT_BLUE, getWidth(), getHeight(), new Color(129, 52, 175));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw decorative circles
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillOval(-100, -100, 400, 400);
                g2d.fillOval(200, 400, 300, 300);
            }
        };
        leftPanel.setBounds(0, 0, 480, 720);
        leftPanel.setLayout(null);
        
        // Library icon/image placeholder
        JLabel libraryIcon = new JLabel("📚", SwingConstants.CENTER);
        libraryIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
        libraryIcon.setForeground(TEXT_WHITE);
        libraryIcon.setBounds(140, 200, 200, 150);
        leftPanel.add(libraryIcon);
        
        // Welcome text on left panel
        JLabel welcomeLabel = new JLabel("Welcome to", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        welcomeLabel.setForeground(new Color(255, 255, 255, 200));
        welcomeLabel.setBounds(0, 360, 480, 35);
        leftPanel.add(welcomeLabel);
        
        JLabel systemNameLabel = new JLabel("Library System", SwingConstants.CENTER);
        systemNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        systemNameLabel.setForeground(TEXT_WHITE);
        systemNameLabel.setBounds(0, 395, 480, 50);
        leftPanel.add(systemNameLabel);
        
        JLabel taglineLabel = new JLabel("Manage. Organize. Excel.", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        taglineLabel.setForeground(new Color(255, 255, 255, 180));
        taglineLabel.setBounds(0, 450, 480, 30);
        leftPanel.add(taglineLabel);
        
        mainPanel.add(leftPanel);
        
        // Right panel - Login form
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(480, 0, 600, 720);
        rightPanel.setBackground(DARK_BG);
        rightPanel.setLayout(null);
        
        // Title
        JLabel titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setBounds(100, 150, 400, 35);
        rightPanel.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Admin Login");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(TEXT_GRAY);
        subtitleLabel.setBounds(100, 190, 200, 30);
        rightPanel.add(subtitleLabel);
        
        // Username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_GRAY);
        usernameLabel.setBounds(100, 260, 100, 25);
        rightPanel.add(usernameLabel);
        
        // Username field
        usernameField = new JTextField();
        usernameField.setBounds(100, 290, 400, 50);
        usernameField.setBackground(FIELD_BG);
        usernameField.setForeground(TEXT_WHITE);
        usernameField.setCaretColor(TEXT_WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 100), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rightPanel.add(usernameField);
        
        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_GRAY);
        passwordLabel.setBounds(100, 360, 100, 25);
        rightPanel.add(passwordLabel);
        
        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(100, 390, 400, 50);
        passwordField.setBackground(FIELD_BG);
        passwordField.setForeground(TEXT_WHITE);
        passwordField.setCaretColor(TEXT_WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 100), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rightPanel.add(passwordField);
        
        // Remember me checkbox
        rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setBounds(100, 455, 150, 25);
        rememberMeCheckbox.setBackground(DARK_BG);
        rememberMeCheckbox.setForeground(TEXT_GRAY);
        rememberMeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rememberMeCheckbox.setFocusPainted(false);
        rightPanel.add(rememberMeCheckbox);
        
        // Login button
        loginButton = new JButton("LOGIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(ACCENT_BLUE.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(ACCENT_HOVER);
                } else {
                    g2d.setColor(ACCENT_BLUE);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.setColor(TEXT_WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        loginButton.setBounds(100, 510, 400, 55);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setForeground(TEXT_WHITE);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        rightPanel.add(loginButton);
        
        // Message label for errors
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBounds(100, 580, 400, 25);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(ERROR_RED);
        rightPanel.add(messageLabel);
        
        // Footer
        JLabel footerLabel = new JLabel("© 2024 Library Management System", SwingConstants.CENTER);
        footerLabel.setBounds(100, 650, 400, 25);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 130));
        rightPanel.add(footerLabel);
        
        mainPanel.add(rightPanel);
        add(mainPanel);
        
        // Add Enter key listener
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            performLogin();
        }
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password");
            shakeWindow();
            return;
        }
        
        // Database validation
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            conn = Connect.Connection();
            
            if (conn == null) {
                messageLabel.setText("Database connection failed!");
                return;
            }
            
            String query = "SELECT * FROM admin WHERE USER_ID = ? AND PASSWORD = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                // Login successful
                messageLabel.setForeground(new Color(85, 255, 85));
                messageLabel.setText("Login successful! Redirecting...");
                
                // Get admin name for welcome message
                String adminName = rs.getString("NAME");
                
                // Open home page after brief delay
                Timer timer = new Timer(1000, evt -> {
                    HomePage homePage = new HomePage(adminName);
                    homePage.setVisible(true);
                    dispose();
                });
                timer.setRepeats(false);
                timer.start();
                
            } else {
                messageLabel.setForeground(ERROR_RED);
                messageLabel.setText("Invalid username or password!");
                shakeWindow();
                passwordField.setText("");
            }
            
        } catch (SQLException ex) {
            messageLabel.setText("Database error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
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
            } else {
                setLocation(originalX, originalY);
                shakeTimer.stop();
            }
        });
        shakeTimer.start();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            Loading1 login = new Loading1();
            login.setVisible(true);
        });
    }
}
