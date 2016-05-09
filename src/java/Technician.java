import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class Technician extends Employee {
   public Technician() {
      super(Employee.DOCTOR);
   }

   public Technician(String username) {
      super(username);
   }

   public List<Employee> viewTechnicianList() {
      return super.viewEmployeeList(Employee.TECHNICIAN);
   }

   public String createTechnician() {
      super.createEmployee("Technicians");
      return "mainAdministrator";
   }

   public String deleteTechnician() {
      super.deleteEmployee(Employee.TECHNICIAN);
      return Admin.MAIN_ADMINISTRATOR_PAGE;
   }

   public boolean doesEmailExist() {
      return super.doesEmailExist();
   }
   
   public String doesIdExist() {
      super.doesIdExist("Technicians");
      return "mainAdministrator";
   }

   public String changePassword() {
       super.changeEmplPassword("Technicians");
       return "mainAdministrator";
   }

   public boolean canGetSDays(int id) {
      return super.canGetSickDays(id);
   }

   public boolean canGetVacDays(int id) {
      return super.canGetVacationDays(id);
   }
}