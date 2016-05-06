import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.util.Date;
import java.util.TimeZone;

import java.util.*;
import java.text.*;
import java.sql.*;
import java.time.*;

@Named(value = "employee")
@SessionScoped
@ManagedBean
public class Employee {
   public final static int DOCTOR = 1;
   public final static int TECHNICIAN = 2;
   public final static int ADMINISTRATOR = 3;

   private final static int MAX_NUM_VACATION_DAYS = 8;
   private final static int MAX_NUM_SICK_DAYS = 4;

   private int id;
   private String email;
   private String password;
   private String username;
   private String firstname;
   private String lastname;
   private String phonenumber;
   private int vacationDays;
   private int sickDays;
   private int type;
   
   private Scheduler schedule;

   public Employee(int type) {
      this.type = type;
   }

   public Employee(String username) {
      // try {
      //    DBConnection connection = new DBConnection();
      //    Connection con = connection.getConnection();

      //    this.username = username;
      //    getTypeFromUsername();
      //    id = result.getInt(Table.ID);
      //    email = result.getString(Table.EMAIL);
      //    password = getPassword(tablename);
      //    firstname = result.getString(Table.FIRSTNAME);
      //    lastname = result.getString(Table.LASTNAME);
      //    phonenumber = result.getString(Table.PHONE_NUMBER);
      //    vacationDays = result.getInt(Table.VACATION_DAYS);
      //    sickDays = result.getInt(Table.SICK_DAYS);

      //    con.close();
      // } 
      // catch (Exception e) {
      //    e.printStackTrace();
      // }
   }

   public int getType() {
      return type;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getId() {
      return id;
   }

   public boolean doesIdExist(String tablename) {
      try {
         DBConnection con = new DBConnection();
         String query = "select * from " + tablename + " " +
                        "where id = " + id;
         ResultSet result = con.execQuery(query);

         return result.next();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return false;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getEmail() {
      return email;
   }

   public boolean doesEmailExist(String tablename) {
      try {
         DBConnection con = new DBConnection();
         String query = "select * from " + tablename + " " +
                        "where email = '" + email + "'";
         ResultSet result = con.execQuery(query);

         return result.next();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return false;
   }

   public void setUsername(String username) {
      this.username = username;

      try {
         String query = "";
         getTypeFromUsername();

         if (type == Employee.DOCTOR) {
            query = "select id from Doctors, Login where " + 
                    "Doctors.email = Login.email and " +
                    "username = '" + username + "'";
         }
         else {
            query = "select id from Technicians, Login where " + 
                    "Technicians.email = Login.email and " +
                    "username = '" + username + "'";
         }

         DBConnection connection = new DBConnection();
         ResultSet rs = connection.execQuery(query);
         if (rs.next()) {
            id = rs.getInt(Table.ID);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void getTypeFromUsername() {
      try {
         String tablename = "Doctors";
         String query = "select * from " + tablename + 
                        "where username = " + username;
         DBConnection connection = new DBConnection();
         ResultSet result =
            connection.execQuery(query);
         type = DOCTOR;

         if (result.next() == false) {
            tablename = "Technicians";
            query = "select * from " + tablename + 
                    "where username = " + username;

            result = connection.execQuery(query);
            type = TECHNICIAN;

            if (result.next() == false) {
               type = ADMINISTRATOR;
               result.next(); // result starts out before the first row
            } 
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public String getUsername() {
      return username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPassword() {
      return password;
   }

   private String getPassword(String tablename) {
      String password = "";

      try {
         // String query = "select * " + 
         //                "from Login, " + tablename + " " +
         //                "where username = '" + username + "' and " + 
         //                "Login.email = " + tablename + ".email";
         DBConnection connection = new DBConnection();
         String query = "select * from Doctors, Login where Doctors.email = Login.email and username = 'd1'";
         ResultSet result = connection.execQuery(query);
         if (result.next()) {
            password = result.getString(Table.PASSWORD);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return password;
   }

   public void setFirstName(String firstname) {
      this.firstname = firstname;
   }

   public String getFirstName() {
      return firstname;
   }

   public void setLastName(String lastname) {
      this.lastname = lastname;
   }

   public String getLastName() {
      return lastname;
   }

   public void setPhoneNumber(String phonenumber) {
      this.phonenumber = phonenumber;
   }

   public String getPhoneNumber() {
      return phonenumber;
   }

   public int getNumVacationDays() {
      return vacationDays;
   }

   public int getSickDays() {
      return sickDays;
   }

   public void createEmployee(String tablename) {
      try {
         DBConnection con = new DBConnection();
         String query = "INSERT INTO Login " +
                        "VALUES (" + "'" + email + "', "
                                   + "'" + username + "', "
                                   + "'" + password + "')";
         con.execUpdate(query);
         query = "INSERT INTO " + tablename +
                 "(email, firstname, lastname, phonenumber) " +
                 "VALUES (" + "'" + email + "', "
                            + "'" + firstname + "', "
                            + "'" + lastname + "', "
                            + "'" + phonenumber + "')";

         con.execUpdate(query);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void deleteEmployee(String tablename) {
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

   public void changeEmplPassword(String tablename) {
       try {
         // String query = "UPDATE " + tablename + " SET password = '" + this.password + "' " +
         //                "WHERE id = " + this.id;
         String query = "UPDATE Login SET password = '" + this.password + "' " +
                        "WHERE email = '" + email + "'";
         System.out.println("WHOA: " + query);
         DBConnection con = new DBConnection();
         con.execUpdate(query);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public List<Employee> getEmployeeList(String tablename) {
      List<Employee> list = new ArrayList<Employee>();

      try {
         String query = "SELECT * from " + tablename;
         DBConnection con = new DBConnection();
         ResultSet result = con.execQuery(query);

         int type = Table.getTypeByTablename(tablename);

         while (result.next()) {
            Employee emp = EmployeeFactory.createEmployee(type);

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

   public ArrayList<Shift> viewSchedule(String tablename) {
      ArrayList<Shift> mySchedule = new ArrayList<>();
      /* try {
         DBConnection dbconn = new DBConnection();
         String query = "";
         ResultSet rs = dbconn.execQuery(query);
         Shift shift;
         while (rs.next()) {
             //shift = new Shift(rs.getString("name"), rs.getDate("date"), rs.get);
            //mySchedule.add(rs.getDate("date").toString());
         }
     }
     catch (SQLException sqe) {
         sqe.printStackTrace();
     }
     */
      // mySchedule.addAll(this.weekOne);
      // mySchedule.addAll(this.weekTwo);
      // mySchedule.addAll(this.weekThree);
      // mySchedule.addAll(this.weekFour);
      return mySchedule;
   }

   public ArrayList<String> getWeekShifts() {
      ArrayList<Shift> shifts = new ArrayList<>();
      ArrayList<String> options = new ArrayList<>();
//      shifts.addAll(this.weekOne);
//      shifts.addAll(this.weekTwo);
//      shifts.addAll(this.weekThree);
//      shifts.addAll(this.weekFour);
      for (int i = 0; i < shifts.size(); i++) {
         options.add(shifts.get(i).getShift() + " " + shifts.get(i).getDateAsString());
      }
      return options;
   }

   public boolean canGetVacationDays() {
      boolean canGetTimeOff = false;

      try {
         String query = "select vacationDaysLeft from Doctors where id = id";

         if (type == Employee.TECHNICIAN) {
            query = "select vacationDaysLeft from Technicians where id = id";
         }

         DBConnection connection = new DBConnection();
         ResultSet rs = connection.execQuery(query);
         if (rs.next()) {
            int vacationDaysLeft = rs.getInt(Table.VACATION_DAYS);
            if (vacationDaysLeft > 0) {
//               Scheduler scheduler = new Scheduler();
               // canGetTimeOff = scheduler.generateSchedule();
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return canGetTimeOff;
   }

   public boolean canGetSickDays() {
      boolean canGetTimeOff = false;

      try {
         String query = "select sickDaysLeft from Doctors where id = id";

         if (type == Employee.TECHNICIAN) {
            query = "select sickDaysLeft from Technicians where id = id";
         }

         DBConnection connection = new DBConnection();
         ResultSet rs = connection.execQuery(query);
         if (rs.next()) {
            int vacationDaysLeft = rs.getInt(Table.SICK_DAYS);
            if (vacationDaysLeft > 0) {
               Scheduler scheduler = new Scheduler();
               // canGetTimeOff = scheduler.generateSchedule();
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return canGetTimeOff;
   }
}