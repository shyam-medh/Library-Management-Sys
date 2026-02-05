import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main Dashboard - HomePage for Library Management System
 * Window Size: 1080x720
 * Dark theme with navigation cards
 */
public class HomePage extends JFrame {
    
    private String adminName;
    
    // Colors for dark theme
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color DARK_SECONDARY = new Color(30, 30, 50);
    private final Color CARD_BG = new Color(40, 40, 65);
    private final Color ACCENT_BLUE = new Color(66, 133, 244);
    private final Color ACCENT_GREEN = new Color(52, 199, 89);
    private final Color ACCENT_PURPLE = new Color(129, 52, 175);
    private final Color ACCENT_ORANGE = new Color(255, 149, 0);
    private final Color ACCENT_RED = new Color(255, 69, 58);
    private final Color ACCENT_TEAL = new Color(90, 200, 250);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(180, 180, 200);
    
    public HomePage(String adminName) {
        this.adminName = adminName != null ? adminName : "Admin";
        initializeFrame();
        createComponents();
    }
    
    private void initializeFrame() {
        setTitle("Library Management System - Dashboard");
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
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1080, 100);
        headerPanel.setOpaque(false);
        headerPanel.setLayout(null);
        
        // Library icon
        JLabel iconLabel = new JLabel("📚", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setBounds(40, 25, 60, 50);
        headerPanel.add(iconLabel);
        
        // System title
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setBounds(110, 25, 400, 40);
        headerPanel.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Admin Dashboard");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_GRAY);
        subtitleLabel.setBounds(110, 60, 200, 25);
        headerPanel.add(subtitleLabel);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + adminName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeLabel.setForeground(TEXT_GRAY);
        welcomeLabel.setBounds(800, 35, 250, 30);
        welcomeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(welcomeLabel);
        
        mainPanel.add(headerPanel);
        
        // Separator line
        JPanel separator = new JPanel();
        separator.setBounds(40, 100, 1000, 2);
        separator.setBackground(new Color(60, 60, 90));
        mainPanel.add(separator);
        
        // Content area with cards
        JPanel contentPanel = new JPanel();
        contentPanel.setBounds(40, 130, 1000, 500);
        contentPanel.setOpaque(false);
        contentPanel.setLayout(null);
        
        // Row 1 - Cards
        int cardWidth = 220;
        int cardHeight = 180;
        int gap = 30;
        int startX = 30;
        int row1Y = 30;
        int row2Y = row1Y + cardHeight + gap;
        
        // Card 1: Book Available
        JPanel card1 = createMenuCard("📖", "Book Available", "View all available books", ACCENT_BLUE, () -> {
            availableBook ab = new availableBook();
            ab.setVisible(true);
        });
        card1.setBounds(startX, row1Y, cardWidth, cardHeight);
        contentPanel.add(card1);
        
        // Card 2: Add New Book
        JPanel card2 = createMenuCard("➕", "Add New Book", "Add books to catalog", ACCENT_GREEN, () -> {
            addBook ab = new addBook();
            ab.setVisible(true);
        });
        card2.setBounds(startX + cardWidth + gap, row1Y, cardWidth, cardHeight);
        contentPanel.add(card2);
        
        // Card 3: Student Registration
        JPanel card3 = createMenuCard("👤", "Student Registration", "Register new students", ACCENT_PURPLE, () -> {
            studentRegistration sr = new studentRegistration();
            sr.setVisible(true);
        });
        card3.setBounds(startX + 2 * (cardWidth + gap), row1Y, cardWidth, cardHeight);
        contentPanel.add(card3);
        
        // Card 4: Student Details
        JPanel card4 = createMenuCard("📋", "Student Details", "View student information", ACCENT_ORANGE, () -> {
            studentDetails sd = new studentDetails();
            sd.setVisible(true);
        });
        card4.setBounds(startX + 3 * (cardWidth + gap), row1Y, cardWidth, cardHeight);
        contentPanel.add(card4);
        
        // Row 2 - Cards
        // Card 5: Issue Book
        JPanel card5 = createMenuCard("📤", "Issue Book", "Issue books to students", ACCENT_TEAL, () -> {
            IssueBook ib = new IssueBook();
            ib.setVisible(true);
        });
        card5.setBounds(startX, row2Y, cardWidth, cardHeight);
        contentPanel.add(card5);
        
        // Card 6: Return Book
        JPanel card6 = createMenuCard("📥", "Return Book", "Process book returns", new Color(175, 82, 222), () -> {
            ReturnBook rb = new ReturnBook();
            rb.setVisible(true);
        });
        card6.setBounds(startX + cardWidth + gap, row2Y, cardWidth, cardHeight);
        contentPanel.add(card6);
        
        // Card 7: Logout
        JPanel card7 = createMenuCard("🚪", "Logout", "Return to login screen", new Color(255, 159, 64), () -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                Loading1 login = new Loading1();
                login.setVisible(true);
                dispose();
            }
        });
        card7.setBounds(startX + 2 * (cardWidth + gap), row2Y, cardWidth, cardHeight);
        contentPanel.add(card7);
        
        // Card 8: Exit
        JPanel card8 = createMenuCard("❌", "Close", "Exit application", ACCENT_RED, () -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", 
                "Confirm Exit", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        card8.setBounds(startX + 3 * (cardWidth + gap), row2Y, cardWidth, cardHeight);
        contentPanel.add(card8);
        
        mainPanel.add(contentPanel);
        
        // Footer
        JLabel footerLabel = new JLabel("© 2024 Library Management System - All Rights Reserved", SwingConstants.CENTER);
        footerLabel.setBounds(0, 660, 1080, 30);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 130));
        mainPanel.add(footerLabel);
        
        add(mainPanel);
    }
    
    private JPanel createMenuCard(String icon, String title, String description, Color accentColor, Runnable action) {
        JPanel card = new JPanel() {
            private boolean hovered = false;
            
            {
                setOpaque(false);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                    
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        action.run();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background
                if (hovered) {
                    g2d.setColor(new Color(50, 50, 80));
                } else {
                    g2d.setColor(CARD_BG);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Top accent bar
                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, getWidth(), 5, 20, 20);
                g2d.fillRect(0, 3, getWidth(), 5);
                
                // Hover glow effect
                if (hovered) {
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
            }
        };
        card.setLayout(null);
        
        // Icon
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setBounds(0, 30, 220, 50);
        card.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setBounds(0, 90, 220, 30);
        card.add(titleLabel);
        
        // Description
        JLabel descLabel = new JLabel(description, SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_GRAY);
        descLabel.setBounds(0, 120, 220, 25);
        card.add(descLabel);
        
        return card;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage home = new HomePage("Test Admin");
            home.setVisible(true);
        });
    }
}
