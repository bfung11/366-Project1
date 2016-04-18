/**
 * Day holds the date and all the possible shifts. Depending on the day of the 
 * week, some of the shifts may be null.
 * e.g. if it is Monday, then the sundayShift will be null
 * 
 * @author Brian Fung
 * @author Justin Zaman
 * @author Kevin Yang
 */

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

      if (iSunday()) {
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
      return date.get(Calendar.DAY_OF_WEEK) == Calendar.Sunday;
   }

   public DayShift getEarlyMorningShift() {
      return earlyMorningShift;
   }

   public DayShift getMorningShift() {
      return morningShift;
   }

   public DayShift getLateMorningShift() {
      return lateMorningShift;
   }

   public DayShift getSurgeryShift() {
      return surgeryShift;
   }

   public DayShift getOvernightShift() {
      return overnightShift;
   }

   public DayShift getSundayShift() {
      return sundayShift;
   }
}