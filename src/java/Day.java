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
   private ShiftInDay earlyMorningShift;
   private ShiftInDay morningShift;
   private ShiftInDay lateMorningShift;
   private ShiftInDay surgeryShift;
   private ShiftInDay overnightShift;
   private ShiftInDay sundayShift;
   private ArrayList<EmployeeTimeOff> doctorTimeOff;

   public Day(Calendar date) {
      this.date = date;

      if (isSunday()) {
         sundayShift = new ShiftInDay();
      }
      else {
         earlyMorningShift = new ShiftInDay();
         morningShift = new ShiftInDay();
         lateMorningShift = new ShiftInDay();
         surgeryShift = new ShiftInDay();
      }

      overnightShift = new ShiftInDay();
      doctorTimeOff = new ArrayList<EmployeeTimeOff>();
   }

   public boolean isSunday() {
      return date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
   }

   public void setShift(Shift shift) {
      switch(shift.getName()) {
         case "Early Morning Shift":
            earlyMorningShift.setShift(shift);
            break;
         case "Morning Shift":
            morningShift.setShift(shift);
            break;
         case "Late Morning Shift":
            lateMorningShift.setShift(shift);
            break;
         case "Surgery Shift":
            surgeryShift.setShift(shift);
            break; 
         case "Overnight Shift":
            overnightShift.setShift(shift);
            break;
         case "Sunday Shift":
            sundayShift.setShift(shift);
            break;
         default:
            System.out.println("Not a valid shift");
            break;
      }
   }

   public ShiftInDay getShift(Shift shift) {
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
   
      return null;
   }

   public ShiftInDay getShift(String shiftName) {
      switch(shiftName) {
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
            System.out.println(shiftName + " is not a valid shift");
            break;
      }
   
      return null;
   }
   
   public void addDoctorTimeOff(EmployeeTimeOff timeoff) {
      doctorTimeOff.add(timeoff);
   }
   
   public boolean checkDoctorWorking(Integer docID) {
      if (earlyMorningShift.getFirstDoctor() == docID)
         return true;
      if (earlyMorningShift.getSecondDoctor() == docID)
         return true;
      
      if (morningShift.getFirstDoctor() == docID)
         return true;
      if (morningShift.getSecondDoctor() == docID)
         return true;
      
      if (lateMorningShift.getFirstDoctor() == docID)
         return true;
      if (lateMorningShift.getSecondDoctor() == docID)
         return true;
      
      if (surgeryShift.getFirstDoctor() == docID)
         return true;
      if (surgeryShift.getSecondDoctor() == docID)
         return true;
      
      if (overnightShift.getFirstDoctor() == docID)
         return true;
      if (overnightShift.getSecondDoctor() == docID)
         return true;
      
      if (sundayShift.getFirstDoctor() == docID)
         return true;
      if (sundayShift.getSecondDoctor() == docID)
         return true;
      
      return false;
   }
   
   public boolean checkOvernightDoctor(Integer docID) {
      if (overnightShift.getFirstDoctor() == docID)
         return true;
      if (overnightShift.getSecondDoctor() == docID)
         return true;
      return false;
   }
   
   public Calendar getDate() {
      return date;
   }
}
