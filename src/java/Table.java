public class Table {
   public final static String USERNAME = "username";
   public final static String PASSWORD = "password";
   public final static String USER_TYPE = "user_type";
   public final static String FIRST_NAME = "first_name";
   public final static String LAST_NAME = "last_name";
   public final static String EMAIL = "email";
   public final static String PHONE_NUMBER = "phonenumber";
   public final static String VACATION_DAYS = "vacationDaysLeft";
   public final static String SICK_DAYS = "sickDaysLeft";
   public final static String DATE = "date";
   public final static String SHIFT = "shift";
   public final static String FROM_TIME = "fromTime";
   public final static String TO_TIME = "toTime";

   private static int emplType;

   public static String getTableNameFromType(int type) {
      String tablename = "";

      switch(type) {
         case Employee.DOCTOR:
            tablename = "Doctor";
            break;
         case Employee.TECHNICIAN:
            tablename = "Technician";
            break;
         case Employee.ADMINISTRATOR:
            tablename = "Administrator";
            break;
         default:
            break;
      }

      return tablename;
   }

   public static String getTableName(String secondPart) {
      String tablename = "";

      switch(emplType) {
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
            System.out.println("Not a valid emplType");
            break;
      }

      return tablename;
   }

   public static int getTypeByTablename(String tablename) {
      switch(tablename) {
         case "Doctors":
            return Employee.DOCTOR;
         case "Technicians":
            return Employee.TECHNICIAN;
         case "Administrators":
            return Employee.ADMINISTRATOR;
         default:
            return -1;
      }
   }

   public static void setEmployeeType(int employeeType) {
      emplType = employeeType;
   }

   public static int getEmployeeType() {
      return emplType;
   }
}