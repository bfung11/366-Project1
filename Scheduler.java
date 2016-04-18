public class Scheduler {
   private final static int MAX_CALENDAR_DAYS = 28;

   private ArrayList<Day> calendar;
   private ArrayList<Employee> doctors;
   private ArrayList<EmployeeShft> doctorShifts;
   private ArrayList<EmployeePreferredShift> doctorPreferredShifts;
   private ArrayList<EmployeeTimeOff> doctorTimeOff;
   // look out 3 weeks in advance

   public Scheduler(Calendar startingDate) {
      Calendar date = startingDate;
      calendar = new ArrayList<Day>(MAX_CALENDAR_DAYS);
      for (int i = 0; i < MAX_CALENDAR_DAYS; ++i) {
         calendar.add(new Day(date));
         date.add(DAY_OF_MONTH, 1);
      }
      doctors = new ArrayList<Employee>();
      doctorShifts = new ArrayList<EmployeeShft>();
      doctorPreferredShifts = new ArrayList<EmployeePreferredShift>();
      doctorTimeOff = new ArrayList<EmployeeTimeOff>();
   }

   

   //TODO read in shifts
   //TODO check preferences
   //TODO calendar
}