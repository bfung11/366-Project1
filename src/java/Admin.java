public class Admin extends Employee {
   public Admin() {
      super(Employee.ADMINISTRATOR);
   }

   public Admin(String username) {
      super(username);
   }

   public int getID() {
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
}