import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin Yang
 */
public class DBConnection {

    public DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;
        }
    }

   /* 
    * 
    */
    // Connect to DB
    public Connection getConnection() {
        java.sql.Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://cslvm74.csc.calpoly.edu:5432/bfung", "postgres",
                    "");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }
        return connection;
    }
    
    
    // Executes query string and returns the result.
    public ResultSet execQuery(String query) throws SQLException {
        Connection con = this.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        Statement statement = con.createStatement();

        ResultSet result = statement.executeQuery(query);
        con.close(); // Not sure if allowed to close connection before result.
        // MAKE SURE to close result after using it! result.close();
        return result;
    }
    
    // Executes an update on the DB. This can be for deletes and changing data.
    public void execUpdate(String query) throws SQLException{
        Connection con = this.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();
        statement.executeUpdate(query);
        statement.close();
        con.commit();
        con.close();
    }
    
   /*
    * This main is to test the connection class to make sure it connects to 
    * the DB
    */
   /*
    public static void main (String[] args) {
        DBConnection dbcon = new DBConnection();
        Connection con = dbcon.getConnection();
        System.out.println(con);
        
        try {            
            dbcon.execUpdate("delete from Doctors where id = 14");

            dbcon.execUpdate("insert into Doctors("
                + "email, "
                + "password, "
                + "firstname, "
                + "lastname, "
                + "phone"
                + ") values('blah@gmail.com', 'abc', 'Bob', 'Johnny', '123')");
            ResultSet result = dbcon.execQuery("select * from Doctors where lastname = 'Johnny'");
            if (result.next() != false) {
                System.out.println(result.getString("lastname"));
            }
                    ResultSet result = dbcon.execQuery("select * from Doctors");
        if (result.next() != false) {
            System.out.println(result.getInt("id"));
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
    }
    */
}