public class EmployeeFactory {
   public static Employee createEmployee(String username) {

      switch(EmployeeFactory.getType(username)) {
         case Employee.DOCTOR:
            return new Doctor(username);
            break;
         case Employee.TECHNICIAN:
            return new Technician(username);
            break;
         case Employee.ADMINISTRATOR:
            return new Admin(username);
            break;
         default:
            break;
      }
   }

   public static int getType(String username) {
      int type = Employee.DOCTOR;

      try {
         boolean isCorrectType = 
            EmployeeFactory.tryQuery("Doctors", username);

         if (!isCorrectType) {
            type = Employee.TECHNICIAN;
            isCorrectType = EmployeeFactory.tryQuery("Technicians", username);

            if (!isCorrectType) {
               isCorrectType = Employee.ADMINISTRATOR;
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return type;
   }

   public static boolean tryQuery(String tablename, String username) {
         DBConnection connection = new DBConnection();
         String query = "select * from " + tablename + 
                        "where username = " + username;
         ResultSet result = connection.execQuery(query);
         connection.close();

         return result.next();
   }
}

