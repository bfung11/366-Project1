import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class Doctor extends Employee {
   public Doctor() {
      super(Employee.DOCTOR);
   }

   public Doctor(String username) {
      super(username);
   }

   public String createDoctor() {
      super.createEmployee("Doctors");
      return "mainAdministrator";
   }

   public String doesIdExist() {
      super.doesIdExist("Doctors");
      return "mainAdministrator";
   }

   public String deleteDoctor() {
      super.deleteEmployee("Doctors");
      return "mainAdministrator";
   }

   public List<Employee> getDoctorList() {
      return super.getEmployeeList("Doctors");
   }   
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