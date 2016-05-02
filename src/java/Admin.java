public class Admin extends Employee {
   public Admin() {
      super(Employee.ADMINISTRATOR);
   }

   public Admin(String username) {
      super(username);
   }
}