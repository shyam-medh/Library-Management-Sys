import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetupDatabase {
    public static void main(String[] args) {
        System.out.println("Initializing database setup...");
        
        try (Connection conn = Connect.Connection()) {
            if (conn != null) {
                // Create users table
                String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                        "USER_ID VARCHAR(50) PRIMARY KEY, " +
                        "NAME VARCHAR(100) NOT NULL, " +
                        "PASSWORD VARCHAR(100) NOT NULL, " +
                        "CONTACT VARCHAR(50), " +
                        "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                
                try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
                    pstmt.executeUpdate();
                    System.out.println("Successfully created/verified 'users' table.");
                }
                
                // Optional: Insert a test user if you want
                // String insertUserSQL = "INSERT IGNORE INTO users (USER_ID, NAME, PASSWORD, CONTACT) VALUES ('testuser', 'Test User', 'password', '1234567890')";
                // try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
                //     pstmt.executeUpdate();
                //     System.out.println("Test user checked/inserted.");
                // }
                
            } else {
                System.err.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
