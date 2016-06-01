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

   public List<Staff> listStaff() {
      ArrayList<Staff> list = new ArrayList<Staff>();

      try {
         String query = 
            "SELECT * " +
            "FROM authentications A, staff S " +
            "WHERE A.username = S.username"
         DBConnection connection = new DBConnection();
         ResultSet result = connection.executeQuery(query);

         while(result.next()) {
            Staff staff = new Staff();
            staff.setUsername(result.getString(Table.USERNAME));
            staff.setPassword(result.getString(Table.PASSWORD));
            staff.setUserType(result.getString(Table.USER_TYPE));
            staff.setFirstName(result.getString(Table.FIRST_NAME));
            staff.setLastName(result.getString(Table.LAST_NAME));
            staff.setPhoneNumber(result.getString(Table.PHONE_NUMBER));
            staff.setEmail(result.getString(Table.EMAIL));

            list.add(staff);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return list;
   }

   public void addStaff() {
      try {
         String query = 
            "INSERT INTO authentications " +
            "VALUES ('" + username + ', '
                        + password + "', '"
                        + userType + "')";
         DBConnection connection = new DBConnection();
         connection.executeUpdate(query);

         query = 
            "INSERT INTO staff " + 
            "VALUES ('" + username + "', '"
                        + email + "', '" 
                        + firstName + "', '" 
                        + lastName + "', '" + 
                        + phoneNumber + "')";
         connection.executeUpdate(query);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void deleteStaff() {
      try {
         String query = 
            "DELETE FROM authentications " + 
            "WHERE username = '" + username + "'";
         DBConnection connection = new DBConnection();
         connection.executeUpdate(query);

         query = 
            "DELETE FROM staff " +
            "WHERE username = '" + username + "'";
         connection.executeUpdate(query);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void changeStaffPassword() {
      try {
         String query = 
            "UPDATE authentications " + 
            "SET password = '" + password "' " +
            "WHERE username = '" + username + "'";
         DBConnection connection = new DBConnection();
         connection.executeUpdate(query);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

}