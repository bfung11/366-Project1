public class Admin {
   private String username;
   private String password;
   private String firstName;
   private String lastName;
   private String phoneNumber;
   private String email;
   private String userType

   public void setUsername(String username) { this.username = username; }
   public String getUsername() { return username; }
   public void setPassword(String password) { this.password = password; }
   public String getPassword() { return password; }
   public void setUserType(String userType) { this.userType = userType; }
   public String getUserType() { return this.userType; }
   public void setFirstName(String firstName) { this.firstName = firstName; } 
   public String getFirstName() { return firstName; }
   public void setLastName(String lastName) { this.lastName = lastName; }
   public String getLastName() { return lastName; }
   public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
   public String getPhoneNumber() { return phoneNumber; }
   public void setEmail(String email) { this.email = email; }
   public String getEmail() { return email; }
}