import java.sql.*;
import java.text.ParseException;

public class Doctor extends Employee {
   public Doctor() {
      super(Employee.DOCTOR);
   }

   public Doctor(String username) {
      super(username);
   }

   public int getDoctorID() {
      return super.getID();
   }

   /*@Override
   public String getEmail() {
      return super.getEmail();
   }

   @Override
   public String getPassword() {
      return super.getPassword();
   }


   public String getLastname() {
      return super.getLastName();
   }

   public String getPhone() {
      return super.getPhoneNumber();
   }

   public void setPhone(String phoneNum) {
       
   }
   public int getTimeOff() {
      return super.getTimeOff();
   }
*/
   
    /*public String createCustomer() throws SQLException, ParseException {
        DBConnection dbcon = new DBConnection();
        Connection con = dbcon.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();

        PreparedStatement preparedStatement = con.prepareStatement("Insert into Doctors(email, password, ) values(?,?,?,?)");
        preparedStatement.setInt(1, customerID);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, address);
        preparedStatement.setDate(4, new java.sql.Date(created_date.getTime()));
        preparedStatement.executeUpdate();
        statement.close();
        con.commit();
        con.close();
        Util.invalidateUserSession();
        return "main";
    }*/
}