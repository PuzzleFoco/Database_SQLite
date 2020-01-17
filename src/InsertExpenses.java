import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public void insert(String category, java.sql.Date date, String reason, double amount, int expenseID) {
        String sql = "INSERT INTO expenses (category, date, reason, amount, expenseID) VALUES (?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setDate(2, (java.sql.Date) date);
            pstmt.setString(3, reason);
            pstmt.setDouble(4, amount);
            pstmt.setInt(5, expenseID);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            //System.err.println("got an exception");
            System.out.println(e.getMessage());
        }
    }

    public String selectCategory() throws InterruptedException {
        JFrame frame = new JFrame();
        final String[] category = new String[1];
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton jButton1 = new JButton("Select category");

        String[] mystring = { "groceries", "car", "beauty", "health care" };
        final JList jList1 = new JList(mystring);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                category[0] = jList1.getSelectedValue().toString();

                frame.dispose();
            }
        });

        frame.add(jList1, "Center");
        frame.add(jButton1,"South");

        frame.setSize(300, 200);
        frame.setVisible(true);

        while(category[0] == null) {Thread.sleep(1000);}
        return category[0];
    }

    public int createexpenseID(){
        int newID = 0;
        String sql = "SELECT expenseID FROM expenses";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                if(newID == rs.getInt("expenseID")){ newID = newID+1;
                System.out.println(newID);}
                else return newID;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return newID;
    }

    public void askfornewinsert() throws InterruptedException, ParseException {
        InsertExpenses app = new InsertExpenses();
        String category = selectCategory();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = JOptionPane.showInputDialog("When did you purchase the " + category + " products? (format yyyy-mm-dd €)");
        java.sql.Date date = new java.sql.Date(sdf.parse(dateInString).getTime());
        System.out.println(date);
        String reason = JOptionPane.showInputDialog("What " + category + " products did you purchase?");
        double amount = Double.parseDouble(JOptionPane.showInputDialog("How much did it cost? (format xx.yy €)"));
        int expenseID = createexpenseID();

        app.insert(category, date, reason, amount, expenseID);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, ParseException {
        InsertExpenses insertExpenses = new InsertExpenses();
        insertExpenses.askfornewinsert();
    }

}
