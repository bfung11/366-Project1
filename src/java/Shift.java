import java.util.*;
import java.sql.*;
import java.text.*;

public final class Shift {
   public final static String EARLY = "7:30 Shift";
   public final static String MORNING = "8:30 Shift";
   public final static String LATE = "9:30 Shift";
   public final static String OVERNIGHT = "Overnight Shift";
   public final static String SUNDAY = "Sunday Shift";
   public final static String SURGERY = "Surgery";

   private final static int NO_ID = -1;

   private String shift;
   private Calendar date;
   private int firstDoctor;
   private int secondDoctor;
   private int firstTechnician;
   private int secondTechnician;

   public Shift() {
      firstDoctor = NO_ID;
      secondDoctor = NO_ID;
      firstTechnician = NO_ID;
      secondTechnician = NO_ID;
   }

   public Shift(String shift, java.sql.Date date, int firstD, int secondD, int firstT, int secondT) {
       this.setShift(shift);
       this.setDate(date);
       this.setFirstDoctor(firstD);
       this.setSecondDoctor(secondD);
       this.setFirstTechnician(firstT);
       this.setSecondTechnician(secondT);
   }
   
   public final void setShift(String shift) {
      this.shift = shift;
   }

   public String getShift() {
      return shift;
   }

   public final void setDate(java.sql.Date date) {
      this.date = new GregorianCalendar();
      this.date.setTime(date);
   }

   public Calendar getDate() {
      return date;
   }

   public String getDateAsString() {
      SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
      return formatter.format(date.getTime());
   }

   public void setFirstDoctor(int doctor) {
      firstDoctor = doctor;
   }

   public int getFirstDoctor() {
      return firstDoctor;
   }

   public boolean hasFirstDoctor() {
      return firstDoctor != NO_ID;
   }

   public final void setSecondDoctor(int doctor) {
      secondDoctor = doctor;
   }

   public int getSecondDoctor() {
      return secondDoctor;
   }

   public boolean hasSecondDoctor() {
      return secondDoctor != NO_ID;
   }

   public final void setFirstTechnician(int technician) {
      firstTechnician = technician;
   }

   public int getFirstTechnician() {
      return firstTechnician;
   }

   public boolean hasFirstTechnician() {
      return firstTechnician != NO_ID;
   }

   public final void setSecondTechnician(int technician) {
      secondTechnician = technician;
   }

   public int getSecondTechnician() {
      return secondTechnician;
   }

   public boolean hasSecondTechnician() {
      return secondTechnician != NO_ID;
   }

   public boolean equals(java.sql.Date date, String shift) {
      Calendar cal = new GregorianCalendar();
      cal.setTime(date);
      return this.date.equals(cal) && this.shift.equals(shift);
   }
}
