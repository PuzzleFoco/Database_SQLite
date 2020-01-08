import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    public static void connect() {
        Connection conn = null;
        try{
            String url = "jdbc:sqlite:C:/Users/engelf2/IdeaProjects/Database/db/chinook.db";
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLIte has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(conn != null){
                    conn.close();
                }
            }catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args){
        connect();
    }
}
