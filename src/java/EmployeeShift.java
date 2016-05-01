import java.util.*;

public class EmployeeShift {
   private int employeeID;
   private Calendar date; //TODO may need to change datatypes
   private int shiftID;

   public EmployeeShift(int employeeID, Calendar date, int shiftID) {
      this.employeeID = employeeID;
      this.date = date;
      this.shiftID = shiftID;
   }

   public int getEmployeeID() {
      return employeeID;
   }

   public Calendar getDate() {
      return date;
   }

   public int getShiftID() {
      return shiftID;
   }
}