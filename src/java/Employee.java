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


@Named(value = "employee")
@SessionScoped
@ManagedBean
public class Employee {
   DBConnection connection;

   public static int DOCTOR = 1;
   public static int TECHNICIAN = 2;

   private final static String ID_TABLENAME = "id";
   private final static String EMAIL_TABLENAME = "email";
   private final static String PASSWORD_TABLENAME = "password";
   private final static String FIRSTNAME_TABLENAME = "firstname";
   private final static String LASTNAME_TABLENAME = "lastname";
   private final static String PHONE_TABLENAME = "phone";
   private final static String TIMEOFF_TABLENAME = "timeoff";

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
      connection = new DBConnection();
      Connection con = connection.getConnection();

      String tablename = "Doctors";
      if (type == TECHNICIAN) {
         tablename = "Technicians";
      }

      try {
         ResultSet result = 
            connection.execQuery("select max(id) from " + tablename);
         id = result.getInt(ID_TABLENAME);
         email = result.getString(EMAIL_TABLENAME);
         password = result.getString(PASSWORD_TABLENAME);
         firstname = result.getString(FIRSTNAME_TABLENAME);
         lastname = result.getString(LASTNAME_TABLENAME);
         phone = result.getString(PHONE_TABLENAME);
         timeoff = result.getInt(TIMEOFF_TABLENAME);
      }
      catch(Exception e) {
         e.printStackTrace();
      }
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
}