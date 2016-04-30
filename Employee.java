public class Employee {
   DBConnection connection;

   private Integer id;
   private String email;
   private String password;
   private String firstname;
   private String lastname;
   private String phone;

   public Employee(String type) {
      connection = new DBConnection();

      execQuery("SELECT * ")  
   }

   public Integer getID() {
      return id;
   }

   public String getEmail() {
      return email;
   }

   public String getPassword() {
      return password;
   }

   public String getFirstName() {
      return firstname;
   }

   public String getLastName() {
      return lastname;
   }

   public String getPhoneNumber() {
      return phone;
   }
}