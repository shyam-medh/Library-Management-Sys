import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Database Connection Factory for Library Management System
 * Provides centralized database connection management
 */
public class Connect {
    
    // Database configuration
    private static final String URL = "jdbc:mysql://localhost:3306/library2";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "#123$";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Creates and returns a new database connection
     * @return Connection object or null if connection fails
     */
    public static Connection Connection() {
        Connection connection = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName(DRIVER);
            
            // Establish connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver not found!\nPlease ensure mysql-connector-j is in the classpath.",
                "Driver Error", 
                JOptionPane.ERROR_MESSAGE);
            System.err.println("ClassNotFoundException: " + e.getMessage());
            e.printStackTrace();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Database connection failed!\nError: " + e.getMessage() + 
                "\n\nPlease check:\n1. MySQL server is running\n2. Database 'library2' exists\n3. Credentials are correct",
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        
        return connection;
    }
    
    /**
     * Closes a database connection safely
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Tests the database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        Connection conn = Connection();
        if (conn != null) {
            closeConnection(conn);
            return true;
        }
        return false;
    }
}
