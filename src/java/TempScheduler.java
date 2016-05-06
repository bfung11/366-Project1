
// TODO : set requestedDay
public class TempScheduler {
   private Calendar requestedDay;
   private int id = 1;

   private ArrayList<Request> getRequestsForWeek(Calendar requestedDay) {
      this.requestedDay = requestedDay;
      
      ArrayList<Request> list = new ArrayList<Request>();

      list.addAll(getRequests(Request.SICK_DAY));
      list.addAll(getRequests(Request.VACATION_DAY));
      list.addAll(getRequests(Request.PREFERRED_SHIFT));

      return list;
   }

   private ArrayList<Request> getRequests(int requestType) {
      ArrayList<Request> list = new ArrayList<Request>();
      String tablename = getTableName(requestType);

      try {
         DBConnection connection = new DBConnection();
         String query = "SELECT * " + 
                        "FROM " + tablename + " " + 
                        "WHERE id = " + id + " and " + 
                        " "
         ResultSet result = connection.execQuery(query);

         while (result.next()) {
            Request request = new Request();
            request.setType(requestType);
            request.setDoctorID(id);
            request.setDate(result.getDate(Table.DATE));

            if (requestType == Request.PREFERRED_SHIFT) {
               request.setShift(result.getString(Table.SHIFT))
            }
            list.add(request);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return list;
   }

   private String getTableName(int requestType) {
      String tablename = "";

      switch (employeeType) {
         case Employee.DOCTOR:
            tablename = "Doctor";
            break;
         case Employee.TECHNICIAN:
            tablename = "Technician";
            break;
         default:
            System.out.println("Not a valid type");
            break;
      }

      switch (requestType) {
         case Request.SICK_DAY:
            tablename += "SickDays";
            break;
         case Request.VACATION_DAY: 
            tablename += "VacationDays";
            break;
         case Request.PREFERRED_SHIFT: 
            tablename += "PreferredShifts";
            break;
         default:
            System.out.println("Not a valid type");
            break;
      }

      return tablename;
   }

}