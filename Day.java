/**
 * Day holds the date and all the possible shifts. Depending on the day of the 
 * week, some of the shifts may be null.
 * e.g. if it is Monday, then the sundayShift will be null
 * 
 * @author Brian Fung
 * @author Justin Zaman
 * @author Kevin Yang
 */

import java.util.*;

public class Day {
   private Calendar date;
   private DayShift earlyMorningShift;
   private DayShift morningShift;
   private DayShift lateMorningShift;
   private DayShift surgeryShift;
   private DayShift overnightShift;
   private DayShift sundayShift;
   private ArrayList<EmployeeTimeOff> doctorTimeOff;

   public Day(Calendar date) {
      this.date = date;

      if (isSunday()) {
         sundayShift = new DayShift();
      }
      else {
         earlyMorningShift = new DayShift();
         morningShift = new DayShift();
         lateMorningShift = new DayShift();
         surgeryShift = new DayShift();
      }

      overnightShift = new DayShift();
      doctorTimeOff = new ArrayList<EmployeeTimeOff>();
   }

   public boolean isSunday() {
      return date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
   }

   public void setShift(Shift shift) {
      switch(shift.getName()) {
         case "Early Morning Shift":
            earlyMorningShift = shift;
            break;
         case "Morning Shift":
            morningShift = shift;
            break;
         case "Late Morning Shift":
            lateMorningShift = shift;
            break;
         case "Surgery Shift":
            surgeryShift = shift;
            break; 
         case "Overnight Shift":
            overnightShift = shift;
            break;
         case "Sunday Shift":
            sundayShift = shift;
            break;
         default:
            System.out.println("Not a valid shift");
            break;
      }
   }

   public DayShift getShift(Shift shift) {
      switch(shift.getName()) {
         case "Early Morning Shift":
            return earlyMorningShift;
         case "Morning Shift":
            return morningShift;
         case "Late Morning Shift":
            return lateMorningShift;
         case "Surgery Shift":
            return surgeryShift;
         case "Overnight Shift":
            return overnightShift;
         case "Sunday Shift":
            return sundayShift;
         default:
            System.out.println("Not a valid shift");
            break;
      }
   }

   public void addDoctorTimeOff(EmployeeTimeOff timeoff) {
      doctorTimeOff.add(timeoff);
   }
}