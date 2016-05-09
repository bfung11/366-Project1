import java.sql.*;
import java.text.ParseException;
import java.util.*;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@Named(value = "doctor")
@SessionScoped
@ManagedBean
public class Doctor extends Employee {
   public List<Employee> viewDoctorList() {
      System.out.println("viewDoctorList()");
      return super.viewEmployeeList(Employee.DOCTOR);
   }
   
   public String createDoctor() {
      super.createEmployee("Doctors");
      return "mainAdministrator";
   }

   public String deleteDoctor() {
      super.deleteEmployee(Employee.DOCTOR);
      return Admin.MAIN_ADMINISTRATOR_PAGE;
   }

   public boolean doesEmailExist() {
      return super.doesEmailExist();
   }

   public String doesIdExist() {
      super.doesIdExist("Doctors");
      return "mainAdministrator";
   }
   
   public String changePassword() {
       super.changeEmplPassword("Doctors");
       return "mainAdministrator";
   }

   public List<Shift> viewDoctorSchedule() {
       return super.viewSchedule("Doctors");
   }

   public List<String> getAllShifts() {
       return super.getAllPossibleShifts();
   }

   public List<String> getAllDates() {
      return super.getAllPossibleDates();
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
   
   public boolean canGetSDays(int id) {
      System.out.println("Sick id: " + id);
      return super.canGetSickDays(id);
   }

   public boolean canGetVacDays(int id) {
      System.out.println("Vac id: " + id);
      return super.canGetVacationDays(id);
   }
   
   public String getUsr() {
      return super.getUsername();
   }
}