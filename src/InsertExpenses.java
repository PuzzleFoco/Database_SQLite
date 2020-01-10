import javax.swing.*;
import java.sql.*;

public class InsertExpenses {

    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Users/Frieda.Schulz/IdeaProjects/Database_SQLite/db/ausgabendatenbank.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String reason, double amount, int expenseID) {
        String sql = "INSERT INTO expenses (reason, amount, expenseID) VALUES (?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reason);
            pstmt.setDouble(2, amount);
            pstmt.setInt(3, expenseID);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            //System.err.println("got an exception");
            System.out.println(e.getMessage());
        }
    }

    public int createexpenseID(){
        int existingID = 0;
        String sql = "SELECT MAX(expenseID) FROM expenses";


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) existingID = rs.getInt("MAX(expenseID)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return existingID+1;
    }

    public void askfornewinsert(){
        InsertExpenses app = new InsertExpenses();
        String reason = JOptionPane.showInputDialog("What did you purchase?");
        double amount = Double.parseDouble(JOptionPane.showInputDialog("How much did it cost?"));
        int expenseID = createexpenseID();

        app.insert(reason, amount, expenseID);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InsertExpenses insertExpenses = new InsertExpenses();
        insertExpenses.askfornewinsert();

    }

}