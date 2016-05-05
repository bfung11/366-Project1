import java.util.*;
import java.sql.*;

public class Shift {
   public final static String EARLY = "7:30 Shift";
   public final static String MORNING = "8:30 Shift";
   public final static String LATE = "9:30 Shift";
   public final static String OVERNIGHT = "Overnight Shift";
   public final static String SUNDAY = "Sunday Shift";
   public final static String SURGERY = "Surgery";

   private final static int NO_ID = -1;

   private String shift;
   private Calendar date;
   private int doctor;
   private int firstTechnician;
   private int secondTechnician;

   public Shift() {
      doctor = NO_ID;
      firstTechnician = NO_ID;
      secondTechnician = NO_ID;
   }

   public void setShift(String shift) {
      this.shift = shift;
   }

   public String getShift() {
      return shift;
   }

   public void setDate(java.sql.Date date) {
      this.date = new GregorianCalendar();
      this.date.setTime(date);
   }

   public Calendar getDate() {
      return date;
   }

   public void setDoctor(int doctor) {
      this.doctor = doctor;
   }

   public int getDoctor() {
      return doctor;
   }

   public boolean hasFirstTechnician() {
      return firstTechnician != NO_ID;
   }

   public void setFirstTechnician(int technician) {
      firstTechnician = technician;
   }

   public int getFirstTechnician() {
      return firstTechnician;
   }

   public void setSecondTechnician(int technician) {
      secondTechnician = technician;
   }

   public int getSecondTechnician() {
      return secondTechnician;
   }

   public boolean equals(java.sql.Date date, String shift) {
      Calendar cal = new GregorianCalendar();
      cal.setTime(date);
      return this.date.equals(cal) && this.shift.equals(shift);
   }
}
