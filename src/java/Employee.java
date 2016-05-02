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

@Named(value = "employee")
@SessionScoped
@ManagedBean
public class Employee {
   DBConnection connection;

   public final static int DOCTOR = 1;
   public final static int TECHNICIAN = 2;
   public final static int ADMINISTRATOR = 3;

   private final static String ID_TABLENAME = "id";
   private final static String EMAIL_TABLENAME = "email";
   private final static String PASSWORD_TABLENAME = "password";
   private final static String FIRSTNAME_TABLENAME = "firstname";
   private final static String LASTNAME_TABLENAME = "lastname";
   private final static String PHONE_TABLENAME = "phone";
   private final static String VACATION_DAYS_TABLENAME = "vacationDays";
   private final static String SICK_DAYS_TABLENAME = "sickDays";
   private final static String DATE_TABLENAME = "date";
   private final static String SHIFTID_TABLENAME = "shift";

   private final static int MAX_NUM_VACATION_DAYS = 8;
   private final static int MAX_NUM_SICK_DAYS = 4;

   private int id;
   private String email;
   private String password;
   private String firstname;
   private String lastname;
   private String phone;
   private int vacationDays;
   private int sickDays;
   private int type;
   
   private Scheduler schedule;
   private Calendar startDate;

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
         
         id = result.getInt(ID_TABLENAME);
         email = result.getString(EMAIL_TABLENAME);
         password = result.getString(PASSWORD_TABLENAME);
         firstname = result.getString(FIRSTNAME_TABLENAME);
         lastname = result.getString(LASTNAME_TABLENAME);
         phone = result.getString(PHONE_TABLENAME);
         vacationDays = result.getInt(VACATION_DAYS_TABLENAME);
         sickDays = result.getInt(SICK_DAYS_TABLENAME);
         timeoff = result.getInt(TIMEOFF_TABLENAME);

         con.close();
      } 
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public int getType() {
      return type;
   }
   
   public int getID() {
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

   public int getTimeOff() {
      return timeoff;
   }

   public ArrayList<String> getSchedule() {
      ArrayList<String> mySchedule = getEmployeeSchedule();
      return mySchedule;
   }

   private ArrayList<String> getEmployeeSchedule() {
      ArrayList<String> mySchedule = new ArrayList<>();
      int i;
      String tableName1;
      String tableName2;
      String resultLine;
      
      try {
         connection = new DBConnection();
         Connection con = connection.getConnection();

         // get tablename
         tableName1 = "DoctorShifts";
         tableName2 = "TechnicianShifts";
         if (type == TECHNICIAN) {
            tableName1 = "TechnicianShifts";
          }

         String query = "select * from " + tableName1 + " where id = " + this.id;
         ResultSet result =
            connection.execQuery(query);

         // add shifts to a list
         while(result.next()) {  
            resultLine = "";
            resultLine = resultLine + Integer.toString(result.getInt(1));
            resultLine = resultLine + " " + result.getDate(2);
            resultLine = resultLine + " " + result.getString(3);
            mySchedule.add(resultLine);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      
      return mySchedule;

   }

   // private String getEmployeeNames() {

   // }

   // private String formatSchedule() {

   // }

   public boolean canChoosePreferredShift() {
      // generate schedule and then find out
      return false;
   }

   // TODO : can choose preferred times
   public void choosePreferredTimes() {

   }

   public boolean canTakeVacation(String username, EmployeeShift shift) {
      return hasVacationDays(); // TODO : && generateSchedule()
   }

   private boolean hasVacationDays() {
      return vacationDays > 0;
   }

   public boolean canTakeSickDay(Employee employee, EmployeeShift shift) {
      return hasSickDays(); // TODO : && generateSchedule()
   }

   private boolean hasSickDays() {
      return sickDays > 0;
   }

  /* 
   * @precondition assumes that sick day is granted;
   */
   public void takeSickDay(EmployeeShift shift) {
      //update TimeOff
      --sickDays;
      String tablename = getTableName("s");
      String query = "UPDATE " + tablename + " " +
                     "SET sickDays = " + sickDays;
                     "WHERE id = " + id + ";";
      connection.execUpdate(query);

      //update
      tablename = getTableName("TimeOff");
      String date = convertDateToString(shift.getDate());
      Shift genericShift = new Shift(shift.getShiftName());
      String fromTime = convertTimeToString(genericShift.getFromTime());
      String toTime = convertTimeToString(genericShift.getToTime());
      String query = "INSERT INTO "  + tablename + " " +
                     "(" + id + ", " 
                         + date + ", "
                         + fromTime + ", " 
                         + date + ", "
                         + toTime + ", "
                         + "'sickDay'" + ")";
      connection.execUpdate(query);  
   }

   private String convertDateToString(Calendar date) {
      SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
      return formatter.format(date.getTime());
   }

   private String convertTimeToString() {
      SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
      return formatter.format(date.getTime());
   }

   private String getTableName(String secondPart) {
      String tablename = "";

      switch(type) {
         case Employee.DOCTOR:
            tablename = "Doctor" + secondPart;
            break;
         case Employee.TECHNICIAN:
            tablename = "Technician" + secondPart;
            break;
         case Employee.ADMINISTRATOR:
            tablename = "Administrator" + secondPart;
            break;
         default:
            break;
      }

      return tablename;
   }
}