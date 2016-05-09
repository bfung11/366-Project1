import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class Technician extends Employee {
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

   public List<String> getAllShifts() {
       return super.getAllPossibleShifts();
   }
      
   public List<String> getAllDates() {
      return super.getAllPossibleDates();
   }
}