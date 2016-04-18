/**
 * DayShift is used for the calendar in the scheduler. It holds only the ids
 * of the doctors and technicians working for that shift.
 * 
 * @author Brian Fung
 * @author Justin Zaman
 * @author Kevin Yang
 */

public class DayShift {
   private final static int NO_EMPLOYEE = -1;

   private int doctorID_1;
   private int doctorID_2;
   private int technicianID_1;
   private int technicianID_2;

   public DayShift() {
      doctorID_1 = NO_EMPLOYEE;
      doctorID_2 = NO_EMPLOYEE;
      technicianID_1 = NO_EMPLOYEE;
      technicianID_2 = NO_EMPLOYEE;
   }

   public void setFirstDoctor(int id) {
      doctorID_1 = id;
   }

   public int getFirstDoctor(int id) {
      return doctorID_1;
   }

   public void setSecondDoctor(int id) {
      doctorID_2 = id;
   }

   public int getSecondDoctor(int id) {
      return doctorID_2;
   }

   public void setFirstTechnician(int id) {
      technicianID_1 = id;
   }

   public int getFirstTechnician(int id) {
      return technicianID_1;
   }

   public void setSecondTechnician(int id) {
      technicianID_2 = id;
   }
   
   public int getSecondTechnician(int id) {
      return technicianID_2;
   }
}