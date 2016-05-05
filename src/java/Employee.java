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

@Named(value = "employee")
@SessionScoped
@ManagedBean
public class Employee {
   DBConnection connection;

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
   private Calendar startDate;
   private ArrayList<Integer> coworkerIDs;

   public Employee(int type) {
      this.type = type;
   }

   public Employee(String username) {
      try {
         connection = new DBConnection();
         Connection con = connection.getConnection();

         String tablename = "Doctors";
         String query = "select * from " + tablename + 
                        "where username = " + username;
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
         
         id = result.getInt(Table.ID);
         email = result.getString(Table.EMAIL);
         this.username = username;
         password = getPassword(tablename);
         firstname = result.getString(Table.FIRSTNAME);
         lastname = result.getString(Table.LASTNAME);
         phonenumber = result.getString(Table.PHONE_NUMBER);
         vacationDays = result.getInt(Table.VACATION_DAYS);
         sickDays = result.getInt(Table.SICK_DAYS);

         con.close();
      } 
      catch (Exception e) {
         e.printStackTrace();
      }
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

         int type = getTypeByTablename(tablename);

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

   private int getTypeByTablename(String tablename) {
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

   public ArrayList<String> getSchedule() {
      ArrayList<String> mySchedule = getEmployeeSchedule();
      return mySchedule;
   }

   private ArrayList<String> getEmployeeSchedule() {
      ArrayList<String> mySchedule = new ArrayList<>();
      int i;
      String thisEmpShifts;
      String otherEmpShifts;
      String otherEmpInfo;
      String resultLine;
      
      try {
         connection = new DBConnection();
         Connection con = connection.getConnection();

         // get tablename
         thisEmpShifts = "DoctorShifts";
         otherEmpShifts = "TechnicianShifts";
         otherEmpInfo = "Technicians";
         if (type == TECHNICIAN) {
            thisEmpShifts = "TechnicianShifts";
            otherEmpShifts = "DoctorShifts";
            otherEmpInfo = "Doctors";
          }

         String query = "select DoctorShifts.date, fromTime, toTime, " + otherEmpInfo
                 + ".name from DoctorShifts, TechnicianShifts, Shifts, " + otherEmpInfo 
                 + " where " + thisEmpShifts + ".id = " + this.id + " and " + otherEmpInfo
                 + ".id = " + otherEmpShifts + ".id and DoctorShifts.date = "
                 + "TechnicianShifts.date and DoctorShifts.shift = Shifts.name";
         ResultSet result =
            connection.execQuery(query);

         // add shifts to a list in date, fromTime, toTime, name of coworker format
         while(result.next()) {  
            resultLine = "";
            resultLine = resultLine + result.getDate(1);
            resultLine = resultLine + " " + result.getTime(2);
            resultLine = resultLine + " " + result.getTime(3);
            resultLine = resultLine + " " + result.getString(4);
            mySchedule.add(resultLine);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      
      return mySchedule;

   }

   public boolean canChoosePreferredShift() {
      // generate schedule and then find out
      return false;
   }

   // TODO : can choose preferred times
   public void choosePreferredTimes() {

   }

   public boolean canTakeVacation(String username, EmployeeShift shift) {
      if (hasVacationDays()) {
         getCoworkerIDs();
         Table.setEmployeeType(type);
         return schedule.requestDayOff(coworkerIDs, id, shift.getDate(), 
                 Table.getTableName("Shifts"));
      }
      return hasVacationDays();
   }

   private boolean hasVacationDays() {
      return vacationDays > 0;
   }

   public boolean canTakeSickDay(Employee employee, EmployeeShift shift) {
      if (hasSickDays()) {
         getCoworkerIDs();
         Table.setEmployeeType(type);
         return schedule.requestDayOff(coworkerIDs, id, shift.getDate(), 
                 Table.getTableName("Shifts"));
      }
      return hasSickDays();
   }

   private boolean hasSickDays() {
      return sickDays > 0;
   }

  /* 
   * @precondition assumes that sick day is granted;
   */
   public void takeSickDay(EmployeeShift shift) {

      try {
         //update TimeOff
         --sickDays;
         Table.setEmployeeType(type);
         String tablename = Table.getTableName("s");
         String query = "UPDATE " + tablename + " " +
                        "SET sickDays = " + sickDays + " " +
                        "WHERE id = " + id + ";";
         connection.execUpdate(query);

         //update
         tablename = Table.getTableName("TimeOff");
         String date = convertDateToString(shift.getDate());
         Shift genericShift = new Shift(shift.getShift());
         String fromTime = convertTimeToString(genericShift.getFromTime());
         String toTime = convertTimeToString(genericShift.getToTime());
         query = "INSERT INTO "  + tablename + " " +
                      "VALUES (" + id + ", " 
                                 + date + ", "
                                 + fromTime + ", " 
                                 + date + ", "
                                 + toTime + ", "
                                 + "'sickDay'" + ")";
         connection.execUpdate(query);  
      }
      catch (Exception e) {

      }
   }
   
   private void getCoworkerIDs() {
      String table;
      coworkerIDs = new ArrayList<>();
      
      try {
         connection = new DBConnection();
         Connection con = connection.getConnection();

         // get tablename
         table = "Doctors";
         if (type == TECHNICIAN) {
            table = "Technicians";
          }

         String query = "select id from " + table;
         ResultSet result =
            connection.execQuery(query);

         // add shifts to a list in date, fromTime, toTime, name of coworker format
         while(result.next()) {  
            coworkerIDs.add(result.getInt(1));
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private String convertDateToString(Calendar date) {
      SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
      return formatter.format(date.getTime());
   }

   private String convertTimeToString(Time time) {
      SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
      return formatter.format(time.getTime());
   }
}