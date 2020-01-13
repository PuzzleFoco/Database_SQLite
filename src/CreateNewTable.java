import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateNewTable {

    private static final String CREATE_TABLE_SQL ="CREATE TABLE expenses ("
            + "category String,"
            + "reason String,"
            + "amount double,"
            + "expenseID int PRIMARY KEY)";

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/Users/Frieda.Schulz/IdeaProjects/Database_SQLite/db/ausgabendatenbank.db";

        Connection conn = null;
        Statement stmt = null;

        try {

            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();

            stmt.executeUpdate(CREATE_TABLE_SQL);

            System.out.println("Table created");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close connection
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
