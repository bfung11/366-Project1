public class Technician extends Employee {
   public Technician() {
      super(Employee.TECHNICIAN);
   }

   public Technician(String username) {
      super(username);
   }

   public String createTechnician() {
      super.createEmployee("Technicians");
      return "mainAdministrator";
   }
}