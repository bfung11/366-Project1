import java.sql.*;

public class EmployeeFactory {
   public static Employee createEmployee(String username) {
      return EmployeeFactory.createEmployee(EmployeeFactory.getType(username));
   }

   public static Employee createEmployee(int type) {
      switch(type) {
         case Employee.DOCTOR:
            return new Doctor();
         case Employee.TECHNICIAN:
            return new Technician();
         case Employee.ADMINISTRATOR:
            return new Admin();
         default:
            return null;
      }
   }

   public static int getType(String username) {
      int type = Employee.DOCTOR;

      boolean isCorrectType = 
         EmployeeFactory.tryQuery("Doctors", username);

      if (!isCorrectType) {
         type = Employee.TECHNICIAN;
         isCorrectType = EmployeeFactory.tryQuery("Technicians", username);

         if (!isCorrectType) {
            type = Employee.ADMINISTRATOR;
         }
      }

      return type;
   }

   public static boolean tryQuery(String tablename, String username) {
      boolean isCorrectType = false;

      try {
         DBConnection connection = new DBConnection();
         String query = "select * from " + tablename + 
                        " natural join login" +
                        " where username = '" + username + "'";

         ResultSet result = connection.execQuery(query);

         isCorrectType = result.next();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return isCorrectType;
   }
}

