public class Scheduler {
   private final static int MAX_CALENDAR_DAYS = 28;
   private final static int MILLISECONDS_TO_DAYS = 1000 * 60 * 60 * 24;

   private ArrayList<Day> calendar;
   private ArrayList<Shift> shifts;
   private ArrayList<Employee> doctors;
   private ArrayList<EmployeeShift> doctorShifts;
   private ArrayList<EmployeePreferredShift> doctorPreferredShifts;
   private ArrayList<EmployeeTimeOff> doctorTimeOff;
   // look out 3 weeks in advance

   public Scheduler(Calendar startingDate) {
      Calendar date = startingDate;
      calendar = new ArrayList<Day>(MAX_CALENDAR_DAYS);
      shifts = new ArrayList<Shift>();
      //TODO read in shifts
      doctors = new ArrayList<Employee>();
      doctorShifts = new ArrayList<EmployeeShift>();
      //TODO read in EmployeeShift


      doctorPreferredShifts = new ArrayList<EmployeePreferredShift>();
      //TODO read in preferred shifts
      doctorTimeOff = new ArrayList<EmployeeTimeOff>();
      //TODO read in timeoff
      for (int i = 0; i < doctorTimeOff.size(); ++i) {
         //TODO add timeoff for multiple days
      }

      addCalendarDays();
      readDoctoShiftsFromDatabase();
   }

  /*
   * Adds enough 28 days to the calendar
   */ 
   private void addCalendarDays() {
      for (int i = 0; i < MAX_CALENDAR_DAYS; ++i) {
         calendar.add(new Day(date));
         date.add(DAY_OF_MONTH, 1);
      }
   }

   private void readDoctoShiftsFromDatabase() {
      for (int i = 0; i < doctorShifts.size(); ++i) {
         EmployeeShift employeeShift = doctorShifts.get(i); //TODO assuming it has been read successfully
         Calendar shiftDate = employeeShift.getDate();
         //subtract shiftDate from startingDate to get index for calendar
         long newTime = shiftDate.getTimeInMillis() 
                         - startingDate.getTimeInMillis();
         int index = newTime / MILLISECONDS_TO_DAYS;
         //use index in to get calendar day
         Day day = calendar.get(index);
         int shiftID = employeeShift.getShiftID();
         Shift shift;
         for (int i = 0; i < shifts.size(); ++i) {
            if (shifts.get(i).getID() == shiftID) {
               shift = shifts.get(i);
            }
         }
         Day.setShift(shift);
      }
   }


   //TODO read in shifts
   //TODO check preferences
   //TODO calendar
}