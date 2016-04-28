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
   //Creates a default schedule assuming no preferences. Uses hardcoded shift
   //blocks to schedule doctors.
   //IDEA -- Generalize this process
   public ArrayList<Day> makeDefaultSchedule(Calendar startDate, ArrayList<Integer> docIDs) {
      int i;
      Day day;
      ArrayList<Day> newWeek = new ArrayList<>();

      //Initialize days of week
      for (i = 0; i < 7; i++) {
          newWeek.set(i, new Day(startDate));
          startDate.add(Calendar.DATE, 1);
      }

      //IDEA: Create shift sets with a whole 4 shift schedule that contains overnight and surgery
      //and assign doctors to shift sets. Will have 2 doctors free to take the rest of the slots
      
      //Schedule Sunday
      newWeek.get(0) = scheduleDay(newWeek.get(0), null, docIDs.get(3), null, null, null, docIDs.get(0));
      
      //Schedule Monday
      newWeek.get(1) = scheduleDay(newWeek.get(1), docIDs.get(7), docIDs.get(4), docIDs.get(5),
              docIDs.get(6), docIDs.get(1));

      //Schedule Tuesday
      newWeek.get(2) = scheduleDay(newWeek.get(2), docIDs.get(4), docIDs.get(5), docIDs.get(6),
              docIDs.get(0), docIDs.get(2));
      
      //Schedule Wednesday
      newWeek.get(3) = scheduleDay(newWeek.get(3), docIDs.get(5), docIDs.get(6), docIDs.get(0),
              docIDs.get(1), docIDs.get(3));

      //Schedule Thursday
      newWeek.get(4) = scheduleDay(newWeek.get(4), docIDs.get(7), docIDs.get(0), docIDs.get(1),
              docIDs.get(2), docIDs.get(4));

      //Schedule Friday
      newWeek.get(5) = scheduleDay(newWeek.get(5), docIDs.get(8), docIDs.get(1), docIDs.get(2),
              docIDs.get(3), docIDs.get(5));

      //Schedule Saturday
      newWeek.get(6) = scheduleDay(newWeek.get(6), docIDs.get(8), docIDs.get(2), docIDs.get(3),
              docIDs.get(4), docIDs.get(6));

      return newWeek;
   }

   /* Schedules an entire day. Takes day to schedule, fills it, and returns.
    * Pass null for early morning shift to indicate Sunday.
    */
   private Day scheduleDay(Day day, Integer emShift, Integer mShift, Integer lmShift,
           Integer sShift, Integer oShift) {
      //Schedule Sunday
      if (emShift == null) {
         day.setSundayShift(mShift);
         day.setOvernightShift(oShift);
      }
      //Schedule other days
      else {
         day.setEMShift(emShift);
         day.setMShift(mShift);
         day.setLMShift(lmShift);
         day.setSurgeryShift(sShift);
         day.setOvernightShift(oShift);
      }
      
      return day;
   }

   //Need to specify some way to change a schedule
   //WARNING: Still a work in progress
   public ArrayList<Day> schedule(ArrayList<Day> schedule, ArrayList<Integer> docIDs) {
      int i;
      ArrayList<Integer> validIDs = new ArrayList<>();
      ArrayList<Integer> allIDs = new ArrayList<>();
      ArrayList<Day> newWeek = new ArrayList<>();
      
      //Associate number of shifts a doctor can still work with their id
      ArrayList<Integer> fourFree = new ArrayList<>();
      ArrayList<Integer> threeFree = new ArrayList<>();
      ArrayList<Integer> twoFree = new ArrayList<>();
      ArrayList<Integer> oneFree = new ArrayList<>();
      ArrayList<Integer> zeroFree = new ArrayList<>();
      
      
      
      //Get all doctor ids and assign to maximum number of shift availability
      for (i = 0; i < doctors.size(); i++) {
          allIDs.add(doctors.get(i));
          fourFree.add(doctors.get(i));
      }
      
      //Check that there is enough doctors
      if (allIDs.size() != 9) {
          System.out.println("Do not have 9 doctors...");
          return null;
      }
      
      //TODO -- Go through current schedule and move doctors to lists. If there
      //are multiple doctors on a shift, remove 1 and put them in a free list
      //Try to satisfy latest request by switching doctor with request with a
      //free doctor
      
      //TODO -- Case above fails
      //Brute force???
      
      //TODO -- Return result
      
   }
   //TODO read in shifts
   //TODO check preferences
   //TODO calendar
}


/*
//Need to specify some way to change a schedule
   public ArrayList<Day> schedule(ArrayList<Day> schedule, ArrayList<Integer> docIDs) {
      ArrayList<Integer> validIDs = new ArrayList<>();
      ArrayList<Integer> allIDs = new ArrayList<>();
      ArrayList<Day> newWeek = new ArrayList<>();
      
      //Associate number of shifts a doctor can still work with their id
      ArrayList<Integer> fourFree = new ArrayList<>();
      ArrayList<Integer> threeFree = new ArrayList<>();
      ArrayList<Integer> twoFree = new ArrayList<>();
      ArrayList<Integer> oneFree = new ArrayList<>();
      ArrayList<Integer> zeroFree = new ArrayList<>();
      
      int i;
      boolean surgeryOK, overnightOK, otherOK;
      boolean isValid = false;
      
      //Break point checks
      surgeryOK = overnightOK = otherOK = false;
      
      //Get all doctor ids and assign to maximum number of shift availability
      for (i = 0; i < doctors.size(); i++) {
          allIDs.add(doctors.get(i).id);
          fourFree.add(doctors.get(i).id);
      }
      
      //Check that there is enough doctors
      if (allIDs.size() != 9) {
          System.out.println("Do not have 9 doctors...");
          return null;
      }
      
      //The default schedule
      newWeek = makeDefaultSchedule(startDate, allIDs);
      
      
      //TODO -- Check preferences
      //When satisfying request, if they do not have lowest priority pref, also
      //schedule them to the surgery + overnight shifts if possible.
      
      //TODO -- Schedule surgery shift
      while (!otherOK) {
          while (!overnightOK) {
              while (!surgeryOK) {
                  //Order doctors with least to most availability for surgery shifts
                  
                  for (i = 0; i < doctors.size(); i++) {
                      
                  }
              }
          }
      }
      //TODO -- Schedule overnight shift
      //TODO -- Schedule rest
   }
   //TODO read in shifts
   //TODO check preferences
   //TODO calendar
*/


