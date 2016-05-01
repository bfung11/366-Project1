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
   private final static String TIMEOFF_TABLENAME = "timeoff";
   private final static String DATE_TABLENAME = "date";
   private final static String SHIFTID_TABLENAME = "shift";

   private int id;
   private String email;
   private String password;
   private String firstname;
   private String lastname;
   private String phone;
   private int timeoff; /* counted in hours */
   private int type;

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
            result.next(); // result starts out before the first row
         }

         id = result.getInt(ID_TABLENAME);
         email = result.getString(EMAIL_TABLENAME);
         password = result.getString(PASSWORD_TABLENAME);
         firstname = result.getString(FIRSTNAME_TABLENAME);
         lastname = result.getString(LASTNAME_TABLENAME);
         phone = result.getString(PHONE_TABLENAME);
         timeoff = result.getInt(TIMEOFF_TABLENAME);

         con.close();
      } 
      catch (Exception e) {
         e.printStackTrace();
      }

   }

   public boolean isDoctor() {
       return this.type == DOCTOR;
   }
   
   public boolean isTechnician() {
       return this.type == TECHNICIAN;
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

   public ArrayList<EmployeeShift> getSchedule() {
      ArrayList<EmployeeShift> list = new ArrayList<EmployeeShift>();

      try {
         connection = new DBConnection();
         Connection con = connection.getConnection();

         String tablename = "DoctorShifts";
         if (type == TECHNICIAN) {
            tablename = "TechnicianShifts";
         }

         String query = "select * from " + tablename + 
                        "where id = " + this.id;
         ResultSet result =
            connection.execQuery(query);

         while(result.next() == false) {  
            Calendar cal = new GregorianCalendar();
            cal.setTime(result.getDate(DATE_TABLENAME));         
            EmployeeShift es = 
               new EmployeeShift(result.getInt(ID_TABLENAME),
                                 cal,
                                 result.getInt(SHIFTID_TABLENAME));
            list.add(es);
         }

      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return list;
   }

   public boolean canChoosePreferredShift() {
      // generate schedule and then find out
      return false;
   }

   // TODO : can choose preferred times
   public void choosePreferredTimes() {

   }

   public void chooseTimeOff() {

   }

   public void addDoctor() {

   }

   public void addTechnician() {

   }

   public void getListofDoctors() {

   }

   public void getListOfTechnicians() {

   }
}