import java.sql.*;

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

   public String getEmail() {
      return super.getEmail();
   }

   public String getPassword() {
      return super.getPassword();
   }

   public String getFirstName() {
      return super.getFirstName();
   }

   public String getLastName() {
      return super.getLastName();
   }

   public String getPhoneNumber() {
      return super.getPhoneNumber();
   }

   public int getTimeOff() {
      return super.getTimeOff();
   }

   public String testConnection() {
      DBConnection connection = new DBConnection();
      Connection con = connection.getConnection();

      return "Hello Doctor!";
   }
}