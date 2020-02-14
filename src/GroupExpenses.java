import javax.swing.*;
import java.awt.event.ActionEvent;
import java.security.acl.Group;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;

public class GroupExpenses {

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


    public static void main(String[] args) throws InterruptedException, ParseException {
        GroupExpenses groupExpenses = new GroupExpenses();
        String selectedcategory = groupExpenses.selectCategory();

        double sum = 0;
        String sql = "SELECT date, amount FROM expenses  WHERE category == '" + selectedcategory + "'";
        int month = 1;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                LocalDate localDate = rs.getObject("date", LocalDate.class);
                // https://stackoverflow.com/questions/43039614/insert-fetch-java-time-localdate-objects-to-from-an-sql-database-such-as-h2
                System.out.println("Test2");
                //if (month == localDate.getMonthValue()){
                sum = sum + rs.getDouble("amount");//}
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());}

        System.out.println("You've spend " + sum + " € on " + selectedcategory + " products.");
    }
}
