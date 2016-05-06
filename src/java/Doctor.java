import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class Doctor extends Employee {
   public Doctor() {
      super(Employee.DOCTOR);
   }

   public Doctor(String username) {
      super(username);
   }

   public String createDoctor() {
      super.createEmployee("Doctors");
      return "mainAdministrator";
   }

   public String doesIdExist() {
      super.doesIdExist("Doctors");
      return "mainAdministrator";
   }

   public String deleteDoctor() {
      super.deleteEmployee("Doctors");
      return "mainAdministrator";
   }

   public String doesEmailExist() {
      super.doesEmailExist("Doctors");
      return "mainAdministrator";
   }
   
   public String changePassword() {
       super.changeEmplPassword("Doctors");
       return "mainAdministrator";
   }

   public void setVacOption(String option) {
      super.setOption(option);
   }
   
  public String getVacOption() {
      return super.getOption();
   }
   
   public void setSickOption(String option) {
      super.setOption(option);
   }
   
   public String getSickOption() {
      return super.getOption();
   }
   
   public boolean canGetSickDays() {
      return super.canGetSickDays();
   }

   public boolean canGetVacationDays() {
      return super.canGetVacationDays();
   }

   public List<Employee> getDoctorList() {
      return super.getEmployeeList("Doctors");
   }
   
   public List<Shift> getDoctorSchedule() {
       return super.viewSchedule("Doctors");
   }
   
   public List<String> getAllShifts() {
       return super.getWeekShifts();
   }
   
   public List<String> getAllDates() {
      return super.getShiftDates();
   }
   
   public String getUsr() {
      System.out.println("BLAH " + super.getUsername());
      return super.getUsername();
   }
}