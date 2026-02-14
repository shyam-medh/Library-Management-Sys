import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import java.util.prefs.Preferences;
import javax.swing.Timer;

public class Loading1 extends JFrame implements ActionListener {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JCheckBox rememberMeCheckbox;
    private JLabel messageLabel;
    
    private Preferences prefs;
    private static final String PREF_USERNAME = "saved_username";
    private static final String PREF_PASSWORD = "saved_password";
    private static final String PREF_REMEMBER = "remember_me";
    
    private final Color ACCENT_COLOR = new Color(66, 133, 244);
    private final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color ERROR_COLOR = new Color(255, 85, 85);
    private final Color SUCCESS_COLOR = new Color(85, 255, 85);
    private final Color BG_DARK = new Color(18, 18, 30);

    public Loading1() {
        prefs = Preferences.userNodeForPackage(Loading1.class);
        initializeFrame();
        createComponents();
    }
    
    private void initializeFrame() {
        setTitle("Library Management System - Login");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));
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
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(18, 18, 30), 0, getHeight(), new Color(40, 40, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Overlay Gradient
                GradientPaint overlay = new GradientPaint(0, 0, new Color(66, 133, 244, 50), 0, getHeight(), new Color(129, 52, 175, 50));
                g2d.setPaint(overlay);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridwidth = GridBagConstraints.REMAINDER;
        gbcLeft.insets = new Insets(10, 0, 10, 0);
        
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        iconLabel.setForeground(TEXT_PRIMARY);
        leftPanel.add(iconLabel, gbcLeft);
        
        JLabel titleLabel = new JLabel("LIBRARY SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(TEXT_PRIMARY);
        leftPanel.add(titleLabel, gbcLeft);
        
        JLabel subtitleLabel = new JLabel("Manage. Organize. Excel.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        leftPanel.add(subtitleLabel, gbcLeft);
        
        mainPanel.add(leftPanel);

        // --- RIGHT SIDE: Login Form ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BG_DARK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40); // Horizontal padding
        gbc.weightx = 1.0;
        
        // Header
        JLabel headerLabel = new JLabel("Welcome Back");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(TEXT_PRIMARY);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(headerLabel, gbc);
        
        JLabel subHeaderLabel = new JLabel("Please enter your details to sign in.");
        subHeaderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subHeaderLabel.setForeground(TEXT_SECONDARY);
        subHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 40, 30, 40); // Extra bottom space
        rightPanel.add(subHeaderLabel, gbc);
        
        // Inputs
        gbc.insets = new Insets(10, 40, 10, 40);
        
        usernameField = new ModernTextField("Username");
        rightPanel.add(createInputContainer(usernameField), gbc);
        
        // Password
        JPanel passContainer = new JPanel(new BorderLayout());
        passContainer.setOpaque(false);
        passwordField = new ModernPasswordField("Password");
        passContainer.add(createInputContainer(passwordField), BorderLayout.CENTER);
        
        JToggleButton showPassBtn = new JToggleButton("👁");
        showPassBtn.setContentAreaFilled(false);
        showPassBtn.setBorderPainted(false);
        showPassBtn.setFocusPainted(false);
        showPassBtn.setForeground(TEXT_SECONDARY);
        showPassBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPassBtn.addActionListener(e -> passwordField.setEchoChar(showPassBtn.isSelected() ? (char)0 : '•'));
        
        // Add eye button next to field? Actually overlay is nicer but layout is hard.
        // Let's put it on the right edge inside the container.
        
        rightPanel.add(passContainer, gbc); // Just add the container for now
        
        // Remember Me
        rememberMeCheckbox = new JCheckBox("Keep me logged in");
        rememberMeCheckbox.setOpaque(false);
        rememberMeCheckbox.setForeground(TEXT_SECONDARY);
        rememberMeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rememberMeCheckbox.setFocusPainted(false);
        rightPanel.add(rememberMeCheckbox, gbc);
        
        // Login Button
        gbc.insets = new Insets(30, 40, 10, 40);
        loginButton = createModernButton("LOGIN", ACCENT_COLOR);
        loginButton.setPreferredSize(new Dimension(0, 50));
        loginButton.addActionListener(this);
        rightPanel.add(loginButton, gbc);
        
        // Signup Button
        gbc.insets = new Insets(10, 40, 10, 40);
        signupButton = createModernButton("CREATE ACCOUNT", new Color(60, 60, 80));
        signupButton.setPreferredSize(new Dimension(0, 50));
        signupButton.addActionListener(this);
        rightPanel.add(signupButton, gbc);
        
        // Message Label
        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(ERROR_COLOR);
        rightPanel.add(messageLabel, gbc);
        
        mainPanel.add(rightPanel);
        
        // Key Listener logic
        KeyAdapter enterListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) performLogin(); }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
    }
    
    private JPanel createInputContainer(JComponent field) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(70, 70, 100)));
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
             @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2d.setColor(baseColor.darker());
                else if (getModel().isRollover()) g2d.setColor(baseColor.brighter());
                else g2d.setColor(baseColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) performLogin();
        else if (e.getSource() == signupButton) { new UserSignup().setVisible(true); dispose(); }
    }
    
    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.equals("Username") || username.isEmpty() || password.equals("Password") || password.isEmpty()) {
            messageLabel.setText("Please enter username and password");
            return;
        }

        try (Connection conn = Connect.Connection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM admin WHERE USER_ID = ? AND PASSWORD = ?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                loginSuccess(rs.getString("NAME"), username, password);
                return;
            }
            
            pst.close(); rs.close();
            // Check Users
            pst = conn.prepareStatement("SELECT * FROM users WHERE USER_ID = ? AND PASSWORD = ?");
            pst.setString(1, username);
            pst.setString(2, password);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                loginSuccess(rs.getString("NAME"), username, password);
                return;
            }
            
            messageLabel.setText("Invalid credentials!");
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("Error: " + ex.getMessage());
        }
    }
    
    private void loginSuccess(String name, String username, String password) {
        messageLabel.setForeground(SUCCESS_COLOR);
        messageLabel.setText("Login successful! Welcome " + name);
        
        if (rememberMeCheckbox.isSelected()) {
            prefs.put(PREF_USERNAME, username);
            prefs.put(PREF_PASSWORD, password);
            prefs.putBoolean(PREF_REMEMBER, true);
        } else {
            prefs.remove(PREF_USERNAME);
            prefs.remove(PREF_PASSWORD);
            prefs.putBoolean(PREF_REMEMBER, false);
        }
        
        Timer delay = new Timer(500, e -> {
            new HomePage(name).setVisible(true);
            dispose();
        });
        delay.setRepeats(false);
        delay.start();
    }
    
    class ModernTextField extends JTextField {
        private String placeholder;
        public ModernTextField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(new EmptyBorder(10, 0, 10, 0));
            setForeground(TEXT_PRIMARY);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setCaretColor(TEXT_PRIMARY);
            setText(placeholder);
            setForeground(TEXT_SECONDARY);
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) { setText(""); setForeground(TEXT_PRIMARY); }
                }
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) { setText(placeholder); setForeground(TEXT_SECONDARY); }
                }
            });
        }
    }
    
    class ModernPasswordField extends JPasswordField {
        private String placeholder;
        public ModernPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(new EmptyBorder(10, 0, 10, 0));
            setForeground(TEXT_PRIMARY);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setCaretColor(TEXT_PRIMARY);
            setText(placeholder);
            setEchoChar((char) 0);
            setForeground(TEXT_SECONDARY);
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (String.valueOf(getPassword()).equals(placeholder)) { 
                        setText(""); 
                        setForeground(TEXT_PRIMARY); 
                        setEchoChar('•'); 
                    }
                }
                public void focusLost(FocusEvent e) {
                    if (String.valueOf(getPassword()).isEmpty()) { 
                        setText(placeholder); 
                        setForeground(TEXT_SECONDARY); 
                        setEchoChar((char) 0); 
                    }
                }
            });
        }
    }
    
    public static void main(String[] args) {
         try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){}
        
        SwingUtilities.invokeLater(() -> {
            Preferences prefs = Preferences.userNodeForPackage(Loading1.class);
            if (prefs.getBoolean(PREF_REMEMBER, false)) {
                String savedUser = prefs.get(PREF_USERNAME, "");
                if (!savedUser.isEmpty()) {
                     new HomePage(savedUser).setVisible(true);
                     return;
                }
            }
            new Loading1().setVisible(true);
        });
    }
}
