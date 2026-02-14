import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.prefs.Preferences;
import javax.swing.border.EmptyBorder;

public class HomePage extends JFrame {
    
    private String adminName;
    
    // Colors
    private final Color BG_DARK = new Color(18, 18, 30);
    private final Color CARD_BG = new Color(30, 30, 50);
    private final Color ACCENT_BLUE = new Color(66, 133, 244);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(180, 180, 200);
    
    // Stats variables
    private int totalBooks = 0;
    private int issuedBooks = 0;
    private int totalStudents = 0;
    
    public HomePage(String adminName) {
        this.adminName = adminName != null ? adminName : "Admin";
        fetchStatistics();
        initializeFrame();
        createComponents();
    }
    
    private void initializeFrame() {
        setTitle("Library Management System - Dashboard");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        // Set minimum size to prevent UI breaking
        setMinimumSize(new Dimension(800, 600));
    }
    
    private void fetchStatistics() {
        try (Connection conn = Connect.Connection()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUM(TOTAL_COPIES) FROM book");
                if (rs.next()) totalBooks = rs.getInt(1);
                if (rs.wasNull()) totalBooks = 0;
                rs.close();
                
                rs = stmt.executeQuery("SELECT COUNT(*) FROM issue WHERE RETURN_DATE IS NULL");
                if (rs.next()) issuedBooks = rs.getInt(1);
                rs.close();
                
                rs = stmt.executeQuery("SELECT COUNT(*) FROM student");
                if (rs.next()) totalStudents = rs.getInt(1);
                rs.close();
                stmt.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void createComponents() {
        // Main Background Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, BG_DARK, 0, getHeight(), new Color(25, 25, 45));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(mainPanel);
        
        // --- Content Container (GridBagLayout for centering/stretching) ---
        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setOpaque(false);
        contentContainer.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.add(contentContainer, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0); // Bottom margin
        
        // --- 1. Header Section ---
        JPanel headerPanel = createHeaderPanel();
        contentContainer.add(headerPanel, gbc);
        
        // --- 2. Stats Grid ---
        gbc.gridy++;
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0)); // 1 row, 4 cols, 20px gap
        statsPanel.setOpaque(false);
        
        statsPanel.add(createStatCard("Total Books", totalBooks, new Color(66, 133, 244)));
        statsPanel.add(createStatCard("Books Issued", issuedBooks, new Color(255, 159, 64)));
        statsPanel.add(createStatCard("Active Students", totalStudents, new Color(52, 199, 89)));
        statsPanel.add(createStatCard("Available", totalBooks - issuedBooks, new Color(153, 102, 255)));
        
        statsPanel.setPreferredSize(new Dimension(0, 140));
        contentContainer.add(statsPanel, gbc);
        
        // --- 3. Quick Actions Label ---
        gbc.gridy++;
        gbc.insets = new Insets(40, 0, 20, 0); // Top margin 40, Bottom 20
        JLabel actionsLabel = new JLabel("Quick Actions");
        actionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        actionsLabel.setForeground(TEXT_WHITE);
        contentContainer.add(actionsLabel, gbc);
        
        // --- 4. Quick Actions Grid ---
        gbc.gridy++;
        gbc.weighty = 1.0; // Consume remaining vertical space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal, not VERTICAL stretch excessively
        gbc.insets = new Insets(0, 0, 0, 0);
        
        // Expanded Grid: 2 Rows, 3 Columns
        JPanel actionsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        actionsPanel.setOpaque(false);
        
        // Row 1
        actionsPanel.add(createActionCard("Issue Book", "📥", new Color(66, 133, 244), () -> new IssueBook().setVisible(true)));
        actionsPanel.add(createActionCard("Return Book", "📤", new Color(153, 102, 255), () -> new ReturnBook().setVisible(true)));
        actionsPanel.add(createActionCard("View Books", "📚", new Color(255, 85, 85), () -> new availableBook().setVisible(true)));
        
        // Row 2
        actionsPanel.add(createActionCard("Add Book", "➕", new Color(52, 199, 89), () -> new addBook().setVisible(true)));
        actionsPanel.add(createActionCard("Add Student", "👤", new Color(255, 205, 86), () -> new studentRegistration().setVisible(true)));
        actionsPanel.add(createActionCard("View Students", "👥", new Color(64, 224, 208), () -> new studentDetails().setVisible(true)));
        
        // We only want this panel to take as much height as it needs (e.g. 200-240px total), 
        // not stretch to fill the whole screen bottom if there's lots of space.
        // So we add it to 'contentContainer' but maybe align it to TOP via wrapper?
        // GridBag with weighty=1.0 and fill=BOTH centered it vertically?
        // Let's rely on standard flow.
        
        contentContainer.add(actionsPanel, gbc);
    }
    
    // ... [Rest of methods: createHeaderPanel, createStatCard, createActionCard, createModernButton] ...
    // Re-including them to ensure full file integrity
    
    private JPanel createHeaderPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Hello, " + adminName.split(" ")[0]);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(TEXT_WHITE);
        
        JLabel dateLabel = new JLabel("Here's what's happening in your library today.");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateLabel.setForeground(TEXT_GRAY);
        
        textPanel.add(welcomeLabel);
        textPanel.add(dateLabel);
        
        p.add(textPanel, BorderLayout.CENTER);
        
        JButton logoutBtn = createModernButton("Log Out", new Color(255, 85, 85));
        logoutBtn.setPreferredSize(new Dimension(100, 40));
        logoutBtn.addActionListener(e -> {
            try {
                Preferences prefs = Preferences.userNodeForPackage(Loading1.class);
                prefs.remove("saved_username");
                prefs.remove("saved_password");
                prefs.putBoolean("remember_me", false);
            } catch (Exception ex) { ex.printStackTrace(); }
            new Loading1().setVisible(true);
            dispose();
        });
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.add(logoutBtn);
        p.add(btnPanel, BorderLayout.EAST);
        
        return p;
    }
    
    private JPanel createStatCard(String title, int value, Color accent) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 6, getHeight(), 20, 20);
                g2.fillRect(4, 0, 4, getHeight());
            }
        };
       card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 25, 15, 15));
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLbl.setForeground(TEXT_GRAY);
        
        JLabel countLbl = new JLabel(String.valueOf(value));
        countLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        countLbl.setForeground(TEXT_WHITE);
        
        card.add(titleLbl, BorderLayout.NORTH);
        card.add(countLbl, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createActionCard(String title, String icon, Color accent, Runnable action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        
        JButton btn = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) g2.setColor(accent.brighter());
                else g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
        btn.setPreferredSize(new Dimension(100, 100));
        
        btn.addActionListener(e -> action.run());
        
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(TEXT_WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        card.add(btn);
        card.add(lbl);
        return card;
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
        btn.setForeground(TEXT_WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
