import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class addBook extends JFrame implements ActionListener {
    private JTextField bookIdField, bookNameField, authorField, copiesField;
    private JComboBox<String> categoryCombo;
    private JButton addButton, clearButton, backButton;
    private JLabel messageLabel;
    
    private final Color DARK_BG = new Color(18, 18, 30);
    private final Color FIELD_BG = new Color(50, 50, 80);
    private final Color ACCENT_GREEN = new Color(52, 199, 89);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(170, 170, 190);
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 170);
    
    private final String[] CATEGORIES = {"Select Category", "Fiction", "Science", "Technology", "Biography", "Fantasy", "Romance", "Mystery", "Education", "Novel"};
    
    public addBook() {
        setTitle("Add New Book");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 700)); // Ensure it doesn't get too small
        createUI();
    }
    
    private void createUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setPaint(new GradientPaint(0, 0, DARK_BG, 0, getHeight(), new Color(25, 25, 45)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(mainPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 5, 20); // Top, Left, Bottom, Right
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; 
        gbc.gridy = 0;
        
        // Title
        JLabel title = new JLabel("➕ Add New Book", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        gbc.insets = new Insets(20, 20, 30, 20);
        mainPanel.add(title, gbc);
        
        gbc.insets = new Insets(5, 40, 5, 40); // Tighter insets for fields
        
        // Book ID
        gbc.gridy++;
        mainPanel.add(createLabel("Book ID"), gbc);
        gbc.gridy++;
        bookIdField = new PlaceholderTextField("Enter Book ID (e.g. B001)"); 
        bookIdField.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(bookIdField, gbc);
        
        // Category
        gbc.gridy++;
        mainPanel.add(createLabel("Category"), gbc);
        gbc.gridy++;
        categoryCombo = new JComboBox<>(CATEGORIES);
        styleCombo(categoryCombo, "Select Category");
        categoryCombo.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(categoryCombo, gbc);
        
        // Book Name
        gbc.gridy++;
        mainPanel.add(createLabel("Book Name"), gbc);
        gbc.gridy++;
        bookNameField = new PlaceholderTextField("Enter Title of the Book"); 
        bookNameField.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(bookNameField, gbc);
        
        // Author
        gbc.gridy++;
        mainPanel.add(createLabel("Author Name"), gbc);
        gbc.gridy++;
        authorField = new PlaceholderTextField("Enter Author Name"); 
        authorField.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(authorField, gbc);
        
        // Copies
        gbc.gridy++;
        mainPanel.add(createLabel("Number of Copies"), gbc);
        gbc.gridy++;
        copiesField = new PlaceholderTextField("Enter Quantity (e.g. 5)"); 
        copiesField.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(copiesField, gbc);
        
        // Message
        gbc.gridy++;
        gbc.insets = new Insets(15, 40, 15, 40);
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(messageLabel, gbc);
        
        // Buttons
        gbc.gridy++;
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        btnPanel.setOpaque(false);
        
        addButton = createBtn("Add Book", ACCENT_GREEN); 
        clearButton = createBtn("Clear", new Color(66, 133, 244)); 
        backButton = createBtn("Back", new Color(100, 100, 130)); 
        
        btnPanel.add(addButton);
        btnPanel.add(clearButton);
        btnPanel.add(backButton);
        
        btnPanel.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(btnPanel, gbc);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_GRAY);
        return lbl;
    }
    
    // ... [Rest of the class methods: PlaceholderTextField, styleCombo, createBtn, actionPerformed, etc.] ...
    // To save context space, I will re-include the necessary inner classes/methods fully.
    
    class PlaceholderTextField extends JTextField {
        private String placeholder;
        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setBackground(FIELD_BG);
            setForeground(PLACEHOLDER_COLOR);
            setCaretColor(TEXT_WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(70, 70, 100), 1), 
                new EmptyBorder(10, 15, 10, 15)
            ));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setText(placeholder);
            
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(TEXT_WHITE);
                        setBorder(BorderFactory.createCompoundBorder(new LineBorder(ACCENT_GREEN, 2), new EmptyBorder(10, 15, 10, 15)));
                    }
                }
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(PLACEHOLDER_COLOR);
                        setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(70, 70, 100), 1), new EmptyBorder(10, 15, 10, 15)));
                    } else {
                        setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(70, 70, 100), 1), new EmptyBorder(10, 15, 10, 15)));
                    }
                }
            });
        }
        public String getText() { return super.getText().equals(placeholder) ? "" : super.getText(); }
    }
    
    private void styleCombo(JComboBox<String> c, String placeholder) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 100)));
        c.setBackground(FIELD_BG);
        c.setForeground(TEXT_WHITE);
        c.setOpaque(false);
        
        c.setUI(new BasicComboBoxUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                g.setColor(FIELD_BG);
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
                super.paint(g, c);
            }

            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    @Override
                    public void paint(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(FIELD_BG);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setColor(new Color(70, 70, 100));
                        g2.drawLine(0, 5, 0, getHeight()-5);
                        g2.setColor(TEXT_GRAY);
                        int w = getWidth(), h = getHeight();
                        int[] x = {w/2-5, w/2+5, w/2};
                        int[] y = {h/2-3, h/2-3, h/2+3};
                        g2.fillPolygon(x, y, 3);
                        g2.dispose();
                    }
                };
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setContentAreaFilled(false);
                btn.setFocusable(false);
                return btn;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(FIELD_BG);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
            
            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = super.createScroller();
                        scroller.getViewport().setBackground(FIELD_BG);
                        scroller.setBackground(FIELD_BG);
                        scroller.setBorder(null);
                        scroller.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL) {
                            @Override
                            public Dimension getPreferredSize() { return new Dimension(8, super.getPreferredSize().height); }
                        });
                        scroller.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
                            protected void configureScrollBarColors() {
                                this.thumbColor = new Color(80, 80, 120);
                                this.trackColor = new Color(40, 40, 60);
                            }
                        });
                        return scroller;
                    }
                };
                popup.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 100)));
                return popup;
            }
        });
        
        c.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value != null && value.toString().equals(placeholder)) {
                    setForeground(PLACEHOLDER_COLOR);
                } else {
                    setForeground(TEXT_WHITE);
                }
                
                if (isSelected) {
                    setBackground(ACCENT_GREEN);
                } else {
                    setBackground(FIELD_BG);
                }
                
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
        bookIdField.setText("Enter Book ID (e.g. B001)"); bookIdField.setForeground(PLACEHOLDER_COLOR);
        categoryCombo.setSelectedIndex(0);
        bookNameField.setText("Enter Title of the Book"); bookNameField.setForeground(PLACEHOLDER_COLOR);
        authorField.setText("Enter Author Name"); authorField.setForeground(PLACEHOLDER_COLOR);
        copiesField.setText("Enter Quantity (e.g. 5)"); copiesField.setForeground(PLACEHOLDER_COLOR);
        messageLabel.setText(" ");
    }
    
    private void showMsg(String msg, boolean error) {
        messageLabel.setForeground(error ? new Color(255,85,85) : new Color(85,255,85));
        messageLabel.setText(msg);
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new addBook().setVisible(true)); }
}
