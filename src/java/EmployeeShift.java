import java.util.*;

public class EmployeeShift {
   private int employeeID;
   private Calendar date; //TODO may need to change datatypes
   private String shift;

   public EmployeeShift(int employeeID, Calendar date, String shift) {
      this.employeeID = employeeID;
      this.date = date;
      this.shift = shift;
   }

   public int getEmployeeID() {
      return employeeID;
   }

   public Calendar getDate() {
      return date;
   }

   public String getShift() {
      return shift;
   }
}