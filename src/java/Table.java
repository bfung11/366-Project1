public class Table {
   public final static String ID = "id";
   public final static String EMAIL = "email";
   public final static String USERNAME = "username";
   public final static String PASSWORD = "password";
   public final static String FIRSTNAME = "firstname";
   public final static String LASTNAME = "lastname";
   public final static String PHONE_NUMBER = "phonenumber";
   public final static String VACATION_DAYS = "vacationDaysLeft";
   public final static String SICK_DAYS = "sickDaysLeft";
   public final static String DATE = "date";
   public final static String SHIFT_ID = "shift";

   private static int employeeType;

   public static String getTableName(String secondPart) {
      String tablename = "";

      switch(employeeType) {
         case Employee.DOCTOR:
            tablename = "Doctor" + secondPart;
            break;
         case Employee.TECHNICIAN:
            tablename = "Technician" + secondPart;
            break;
         case Employee.ADMINISTRATOR:
            tablename = "Administrator" + secondPart;
            break;
         default:
            break;
      }

      return tablename;
   }

   public static String getTableName(int requestType) {
      String tablename = "";

      switch (requestType) {
         case Request.SICK_DAY:
            tablename = Table.getTableName("SickDays");
            break;
         case Request.VACATION_DAY: 
            tablename = Table.getTableName("VacationDays");
            break;
         case Request.PREFERRED_SHIFT: 
            tablename = Table.getTableName("PreferredShifts");
            break;
         default:
            System.out.println("Not a valid employeeType");
            break;
      }

      return tablename;
   }

   public static void setEmployeeType(int employeeType) {
      this.employeeType = employeeType;
   }

   public static int getEmployeeType() {
      return employeeType;
   }
}