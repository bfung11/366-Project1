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
   private final static int NUM_SHIFTS_PER_WEEK = 32;

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
   private LocalDate startDate;
   private ArrayList<Shift> weekOne;
   private ArrayList<Shift> weekTwo;
   private ArrayList<Shift> weekThree;
   private ArrayList<Shift> weekFour;

   private LocalDate aDate;

   public Employee(int type) {
      this.type = type;
      initCalendar();
   }

   public Employee(String username) {
      try {
         DBConnection connection = new DBConnection();
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

         initCalendar();

         con.close();
      } 
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void initCalendar() {
      weekOne = new ArrayList<Shift>(NUM_SHIFTS_PER_WEEK);
      weekTwo = new ArrayList<Shift>(NUM_SHIFTS_PER_WEEK);
      weekThree = new ArrayList<Shift>(NUM_SHIFTS_PER_WEEK);
      weekFour = new ArrayList<Shift>(NUM_SHIFTS_PER_WEEK);
 
      initStartdate();
      aDate = LocalDate.parse(startDate.toString());

      initWeek(weekOne, aDate);
      initWeek(weekTwo, aDate);
      initWeek(weekThree, aDate);
      initWeek(weekFour, aDate);
   }

   private void initStartdate() {
      try {
         DBConnection connection = new DBConnection();
         String query = "SELECT min(date) AS date from DoctorShifts";
         ResultSet result = connection.execQuery(query);
         if (result.next()) {
            startDate = LocalDate.parse(result.getDate(Table.DATE).toString());
            System.out.println("initStartDate(): " + result.getDate(Table.DATE));
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void initWeek(ArrayList<Shift> week, LocalDate startDate) {
      try {
         // get doctors
         DBConnection connection = new DBConnection();
         SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");

         String earliest = aDate.toString();
         aDate = aDate.plusDays(7);
         String latest = aDate.toString();
         System.out.println("earliest : " + earliest);
         System.out.println("latest : " + latest);
         String query = "select * from doctorShifts " + 
                        "where date >= '" + earliest + "' and date < '" + latest + "' " + 
                        "order by date ASC";
         ResultSet result = connection.execQuery(query);

         while (result.next()) {
            String shiftName  = result.getString(Table.SHIFT);
            java.sql.Date date = result.getDate(Table.DATE);
            if (!week.isEmpty()) {
               Shift shift = week.get(week.size() - 1);
               if (!shift.equals(date, shiftName)) {
                  Shift newShift = new Shift();
                  newShift.setShift(shiftName);
                  newShift.setDate(date);
                  newShift.setFirstDoctor(result.getInt(Table.ID));
                  week.add(newShift);
               }
               else {
                  shift.setSecondDoctor(result.getInt(Table.ID));
               }
            }
            else {
               Shift newShift = new Shift();
               newShift.setShift(shiftName);
               newShift.setDate(date);
               week.add(newShift);
            }
         }

         // get technicians
         query = "SELECT * FROM TechnicianShifts ORDER BY date ASC";
         result = connection.execQuery(query);
         while (result.next()) {
            for (int i = 0; i < week.size(); ++i) {
               Shift shift = week.get(i);
               if (shift.equals(result.getDate(Table.DATE), 
                                result.getString(Table.SHIFT))) {
                  int technician = result.getInt(Table.ID);
                  if (!shift.hasFirstTechnician()) {
                     shift.setFirstTechnician(technician);
                  }
                  else {
                     shift.setSecondTechnician(technician);
                  }
               }
            }
         }
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
      mySchedule.addAll(this.weekOne);
      mySchedule.addAll(this.weekTwo);
      mySchedule.addAll(this.weekThree);
      mySchedule.addAll(this.weekFour);
      return mySchedule;
   }

    public ArrayList<String> getWeekShifts() {
        ArrayList<Shift> shifts = new ArrayList<>();
        ArrayList<String> options = new ArrayList<>();
        shifts.addAll(this.weekOne);
        shifts.addAll(this.weekTwo);
        shifts.addAll(this.weekThree);
        shifts.addAll(this.weekFour);
        for (int i = 0; i < shifts.size(); i++) {
            options.add(shifts.get(i).getShift() + " " + shifts.get(i).getDateAsString());
        }
        return options;
    }
   
   private String convertTimeToString(Time time) {
      SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
      return formatter.format(time.getTime());
   }

   private void pushCalendarToDatabase() {
      pushWeekToDatabase(weekOne);
      pushWeekToDatabase(weekTwo);
      pushWeekToDatabase(weekThree);
      pushWeekToDatabase(weekFour);
   }

   private void pushWeekToDatabase(ArrayList<Shift> week) {
      try {
         DBConnection connection = new DBConnection();
         for (int index = 0; index < week.size(); ++index) {
            Shift shift = week.get(index);
            String querySecondHalf = "WHERE date = '" + shift.getDateAsString() + 
                                     "' and " + 
                                     "shift = '" + shift.getShift() + "'";

            String query = "UPDATE DoctorShifts SET id = " +  
                           shift.getFirstDoctor() + " " + querySecondHalf;
            connection.execQuery(query); 

            if (shift.hasSecondDoctor()) {
               query = "UPDATE DoctorShifts SET id = " +  
                        shift.getSecondDoctor() + " " + querySecondHalf;
               connection.execQuery(query);
            }

            query = "UPDATE TechnicianShifts SET id = " +  
                     shift.getFirstTechnician() + " " + querySecondHalf;
            connection.execQuery(query);

            if (shift.hasSecondTechnician()) {
               query = "UPDATE TechnicianShifts SET id = " +  
                        shift.getSecondTechnician() + " " + querySecondHalf;
               connection.execQuery(query);
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void viewSchedule() {
      printWeek(weekOne);
      printWeek(weekTwo);
      printWeek(weekThree);
      printWeek(weekFour);
   }

   private void printWeek(ArrayList<Shift> week) {
         System.out.println("Size: " + week.size());

      for (int i = 0; i < week.size(); ++i) {
         week.get(i).print();
      }
   }
}