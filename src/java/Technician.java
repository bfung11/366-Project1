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

   public String createTechnician() {
      super.createEmployee("Technicians");
      return "mainAdministrator";
   }

   public String doesIdExist() {
      super.doesIdExist("Technicians");
      return "mainAdministrator";
   }

   public String deleteTechnician() {
      super.deleteEmployee("Technicians");
      return "mainAdministrator";
   }

   public List<Employee> getTechnicianList() {
      return super.getEmployeeList("Technicians");
   }
}