import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
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
    private final Color FIELD_BG = new Color(50, 50, 80);
    private final Color ACCENT_TEAL = new Color(90, 200, 250);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_GRAY = new Color(170, 170, 190);
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 170);
    
    public IssueBook() {
        setTitle("Issue Book");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 600));
        createUI();
        loadComboData();
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
        gbc.insets = new Insets(10, 20, 5, 20); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; 
        gbc.gridy = 0;
        
        // Title
        JLabel title = new JLabel("📤 Issue Book", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        gbc.insets = new Insets(20, 20, 30, 20);
        mainPanel.add(title, gbc);
        
        gbc.insets = new Insets(5, 40, 5, 40);

        // Student
        gbc.gridy++;
        mainPanel.add(createLabel("Student ID"), gbc);
        gbc.gridy++;
        studentCombo = new JComboBox<>();
        styleCombo(studentCombo, "Select Student");
        studentCombo.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(studentCombo, gbc);
        
        // Book
        gbc.gridy++;
        mainPanel.add(createLabel("Book ID"), gbc);
        gbc.gridy++;
        bookCombo = new JComboBox<>();
        styleCombo(bookCombo, "Select Book");
        bookCombo.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(bookCombo, gbc);
        
        // Issue Date
        gbc.gridy++;
        mainPanel.add(createLabel("Issue Date (yyyy-MM-dd)"), gbc);
        gbc.gridy++;
        dateField = new PlaceholderTextField("yyyy-MM-dd");
        dateField.setPreferredSize(new Dimension(0, 45));
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dateField.setForeground(TEXT_WHITE);
        mainPanel.add(dateField, gbc);
        
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
        
        issueBtn = createBtn("Issue Book", ACCENT_TEAL);
        clearBtn = createBtn("Clear", new Color(66, 133, 244));
        backBtn = createBtn("Back", new Color(100, 100, 130));
        
        btnPanel.add(issueBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(backBtn);
        
        btnPanel.setPreferredSize(new Dimension(0, 45));
        mainPanel.add(btnPanel, gbc);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_GRAY);
        return lbl;
    }
    
    class PlaceholderTextField extends JTextField {
        private String placeholder;
        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setBackground(FIELD_BG);
            setForeground(PLACEHOLDER_COLOR);
            setCaretColor(TEXT_WHITE);
            setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(70, 70, 100), 1), new EmptyBorder(10, 15, 10, 15)));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setText(placeholder);
            
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(TEXT_WHITE);
                        setBorder(BorderFactory.createCompoundBorder(new LineBorder(ACCENT_TEAL, 2), new EmptyBorder(10, 15, 10, 15)));
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
                        // Divider
                        g2.setColor(new Color(70, 70, 100));
                        g2.drawLine(0, 5, 0, getHeight()-5);
                        // Arrow
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
                    setBackground(ACCENT_TEAL);
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
            
            PreparedStatement checkBook = conn.prepareStatement("SELECT COPIES FROM book WHERE BOOK_ID=?");
            checkBook.setString(1, bookId);
            ResultSet rs = checkBook.executeQuery();
            if (!rs.next() || rs.getInt("COPIES") <= 0) {
                showMsg("Book not available!", true); conn.rollback(); return;
            }
            
            PreparedStatement insertIssue = conn.prepareStatement("INSERT INTO issue (Stu_ID,BOOK_ID,ISSUE_DATE) VALUES (?,?,?)");
            insertIssue.setString(1, stuId);
            insertIssue.setString(2, bookId);
            insertIssue.setString(3, date);
            insertIssue.executeUpdate();
            
            PreparedStatement updateBook = conn.prepareStatement("UPDATE book SET COPIES=COPIES-1 WHERE BOOK_ID=?");
            updateBook.setString(1, bookId);
            updateBook.executeUpdate();
            
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
        messageLabel.setText(" ");
    }
    
    private void showMsg(String msg, boolean error) {
        messageLabel.setForeground(error ? new Color(255,85,85) : new Color(85,255,85));
        messageLabel.setText(msg);
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new IssueBook().setVisible(true)); }
}
