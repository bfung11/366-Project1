public class Admin extends Employee {
   public final static String MAIN_ADMINISTRATOR_PAGE = "mainAdministrator";

   public List<Employee> getDoctorLis() {
      return super.getEmployeeList(Employee.DOCTOR);
   }

   public List<Employee> getTechnicianList() {
      return super.getEmployeeList(Employee.TECHNICIAN);
   }

   private List<Employee> getEmployeeList(int employeeType) {
      List<Employee> list = new ArrayList<Employee>();

      try {
         String tablename = Table.getTableNameFromType(employeeType) + "s";
         String query = "SELECT * from " + tablename;
         DBConnection con = new DBConnection();
         ResultSet result = con.execQuery(query);

         while (result.next()) {
            Employee emp = EmployeeFactory.createEmployee(employeeType);

            String email = result.getString(Table.EMAIL);
            emp.setId(result.getInt(Table.ID));
            emp.setEmail(email);
            emp.setLastName(result.getString(Table.LASTNAME));
            emp.setPhoneNumber(result.getString(Table.PHONE_NUMBER));

            String passwordQuery = "select * from Login where email = '" + email + "'";
            ResultSet passwordResult = con.execQuery(passwordQuery);

            if (passwordResult.next()) {
               emp.setUsername(passwordResult.getString(Table.USERNAME));
               emp.setPassword(passwordResult.getString(Table.PASSWORD));
            }

            list.add(emp);
         }
      }
      catch(Exception e) {
         e.printStackTrace();
      }

      return list;
   }

   private void deleteEmployee(String tablename) {
      try {
         String query = "DELETE from " + tablename + " " +
                        "WHERE id = " + id;
         DBConnection con = new DBConnection();
         con.execUpdate(query);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}