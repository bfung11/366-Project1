/**
 * Scheduler is used to schedule shifts.
 * 
 * @author Brian Fung
 * @author Justin Zaman
 * @author Kevin Yang
 */

import java.util.*;

public class Scheduler {
   private final static int MAX_CALENDAR_DAYS = 28;
   private final static int MILLISECONDS_TO_DAYS = 1000 * 60 * 60 * 24;
   private final static int DAYS_PER_WEEK = 7;
   private final static int MAX_SHIFTS_PER_WEEK = 4;
   private final static int INVALID = -1;

   private ArrayList<Day> calendar;
   private ArrayList<Shift> shifts;
   private ArrayList<Employee> doctors;
   private ArrayList<EmployeeShift> doctorShifts;
   private ArrayList<EmployeePreferredShift> doctorPreferredShifts;
   private ArrayList<EmployeeTimeOff> doctorTimeOff;
   // look out 3 weeks in advance

   public Scheduler(Calendar startingDate) {
      calendar = new ArrayList<Day>(MAX_CALENDAR_DAYS);
      shifts = new ArrayList<Shift>();
      //TODO read in shifts
      doctors = new ArrayList<Employee>();
      doctorShifts = new ArrayList<EmployeeShift>();
      doctorPreferredShifts = new ArrayList<EmployeePreferredShift>();
      doctorTimeOff = new ArrayList<EmployeeTimeOff>();
      for (int i = 0; i < doctorTimeOff.size(); ++i) {
         //TODO add timeoff for multiple days
      }

      addDaysToCalendar(startingDate);
      readDoctorShiftsFromDatabase(startingDate);
   }

  /*
   * Adds enough 28 days to the calendar
   */ 
   private void addDaysToCalendar(Calendar startingDate) {
      Calendar date = startingDate;

      for (int i = 0; i < MAX_CALENDAR_DAYS; ++i) {
         calendar.add(new Day(date));
         date.add(date.DAY_OF_MONTH, 1);
      }
   }

   private void readDoctorShiftsFromDatabase(Calendar startingDate) {
      DBConnection con = new DBConnection();
      // ResultSet set = con.execQuery("SELECT *" +
      //                               "FROM DoctorShifts");
      

      for (int i = 0; i < doctorShifts.size(); ++i) {
         EmployeeShift employeeShift = doctorShifts.get(i); //TODO assuming it has been read successfully
         Calendar shiftDate = employeeShift.getDate();
         //subtract shiftDate from startingDate to get index for calendar
         long newTime = shiftDate.getTimeInMillis() 
                         - startingDate.getTimeInMillis();
         int index = (int) newTime / MILLISECONDS_TO_DAYS;
         //use index in to get calendar day
         Day day = calendar.get(index);
         int shiftID = employeeShift.getShiftID();
         Shift shift = null;
         for (int j = 0; j < shifts.size(); ++j) {
            if (shifts.get(j).getID() == shiftID) {
               shift = shifts.get(j);
            }
         }
         day.setShift(shift);
      }
   }

   private void readDoctorPreferredShifts() {

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
      day = scheduleDay(newWeek.get(0), null, docIDs.get(3), null, null, docIDs.get(0));
      newWeek.set(0, day);
      
      //Schedule Monday
      day = scheduleDay(newWeek.get(1), docIDs.get(7), docIDs.get(4), docIDs.get(5),
              docIDs.get(6), docIDs.get(1));
      newWeek.set(1, day);
      
      //Schedule Tuesday
      day = scheduleDay(newWeek.get(2), docIDs.get(4), docIDs.get(5), docIDs.get(6),
              docIDs.get(0), docIDs.get(2));
      newWeek.set(2, day);
      
      //Schedule Wednesday
      day = scheduleDay(newWeek.get(3), docIDs.get(5), docIDs.get(6), docIDs.get(0),
              docIDs.get(1), docIDs.get(3));
      newWeek.set(3, day);

      //Schedule Thursday
      day = scheduleDay(newWeek.get(4), docIDs.get(7), docIDs.get(0), docIDs.get(1),
              docIDs.get(2), docIDs.get(4));
      newWeek.set(4, day);

      //Schedule Friday
      day = scheduleDay(newWeek.get(5), docIDs.get(8), docIDs.get(1), docIDs.get(2),
              docIDs.get(3), docIDs.get(5));
      newWeek.set(5, day);

      //Schedule Saturday
      day = scheduleDay(newWeek.get(6), docIDs.get(8), docIDs.get(2), docIDs.get(3),
              docIDs.get(4), docIDs.get(6));
      newWeek.set(6, day);

      return newWeek;
   }

   /* Schedules an entire day. Takes day to schedule, fills it, and returns.
    * Pass null for early morning shift to indicate Sunday.
    */
   private Day scheduleDay(Day day, Integer emShift, Integer mShift, Integer lmShift,
           Integer sShift, Integer oShift) {
      //Schedule Sunday
      if (emShift == null) {
         assignDoctorToShift(day, mShift, "Sunday Shift");
         assignDoctorToShift(day, oShift, "Overnight Shift");
      }
      //Schedule other days
      else {
         assignDoctorToShift(day, emShift, "Early Morning Shift");
         assignDoctorToShift(day, mShift, "Morning Shift");
         assignDoctorToShift(day, lmShift, "Late Morning Shift");
         assignDoctorToShift(day, sShift, "Sunday Shift");
         assignDoctorToShift(day, oShift, "Overnight Shift");
      }
      
      return day;
   }
   
   private void assignDoctorToShift(Day day, Integer docID, String shiftName) {
      ShiftInDay oldShift;
      
      oldShift = day.getShift(shiftName);
      
      if (oldShift.getFirstDoctor() > 0) {
         if (oldShift.getSecondDoctor() > 0) {
            System.out.println("Shift is full already.");
         }
         else {
            oldShift.setSecondDoctor(docID);
         }
      }
      else {
         oldShift.setFirstDoctor(docID);
      }
   }

   //Grants request for a day off for a doctor if possible
   public boolean requestDayOff(ArrayList<Day> schedule, ArrayList<Integer> docIDs,
           Integer requestingDoc, Day requestedDay) {
      int i;
      int j;
      int docID;
      int temp;
      int indexOfDay = 0;
      boolean isWorking = false;
      boolean isReplaced = false;
      boolean isSunday = requestedDay.isSunday();
      Day day;
      Day yesterday = null;
      ShiftInDay shift;
      Shift newShift;
      ArrayList<Integer> validIDs = new ArrayList<>();
      ArrayList<Integer> allIDs = new ArrayList<>();
      ArrayList<Day> newWeek = new ArrayList<>();
      Map<Integer, Integer> freeDays = new HashMap<>();
      String shiftType = null;
      String[] shiftNames = {"Early Morning Shift", "Morning Shift", "Late "
              + "Morning Shift", "Surgery Shift", "Overnight Shift"};
      
      //Store which doctors are working when
      Set[] dailyDocs = new Set[DAYS_PER_WEEK];
      for (i = 0; i < DAYS_PER_WEEK; i++) {
         dailyDocs[i] = new HashSet<>();
      }
      
      //Store which doctors are working overnight shifts from Sun-Sat [0-6]
      Integer[] overnightDocs = new Integer[DAYS_PER_WEEK];
              
      //Get all doctor ids and assign to maximum number of shift availability
      for (i = 0; i < docIDs.size(); i++) {
          allIDs.add(docIDs.get(i));
          freeDays.put(docIDs.get(i), MAX_SHIFTS_PER_WEEK);
      }
      
      //Check that there is enough doctors
      if (allIDs.size() != 9) {
          System.out.println("Do not have 9 doctors...");
          return false;
      }
      
      /* Helps with finding free doctors to fill up a shift
       * Go through current schedule and finds out which doctors work what days
       * and overnight shifts as well as remove doctors from extra shifts.
       * This frees them up to possibly take the shift that is being lost
       */
      for (i = 0; i < DAYS_PER_WEEK; i++) {
         day = schedule.get(i);
         if (day == requestedDay && !isSunday)
            yesterday = schedule.get(i - 1);
         for (j = 0; j < shiftNames.length; j++) {
            shift = day.getShift(shiftNames[j]);
            docID = shift.getFirstDoctor();
            if (day == requestedDay && docID == requestingDoc) {
               isWorking = true;
               indexOfDay = i;
               shiftType = shiftNames[j];
            }
            if (docID >= 0) {
               dailyDocs[i].add(docID);
               temp = freeDays.get(docID);
               freeDays.put(docID, temp - 1);
               if (shiftNames[j].equals("Overnight Shift")) {
                  overnightDocs[i] = docID;
               }
            }
            docID = shift.getSecondDoctor();
            if (docID >= 0) {
               shift.setSecondDoctor(INVALID);
            }
         }
      }
      
      //Case where doctor is already not working on the requested day
      if (!isWorking)
         return true;
         
      //Try to slot in free doctor into slot
      for (Map.Entry<Integer, Integer> entry : freeDays.entrySet()) {
         //If doctor has free days 
         if (entry.getValue() > 0) {
            //If doctor does not already work today
            if (!requestedDay.checkDoctorWorking(entry.getKey())) {
               //If doctor did not work overnight yesterday
               if (yesterday == null || !yesterday.checkOvernightDoctor(entry.getKey())) {
                  //Set doctor in shift
                  shift = schedule.get(indexOfDay).getShift(shiftType);
                  shift.setFirstDoctor(entry.getKey());
                  isReplaced = true;
                  freeDays.put(entry.getKey(), entry.getValue() - 1);
                  freeDays.put(requestingDoc, 1);
                  break;
               }
            }
         }
      }
      
      //Could not replace doctor
      if (!isReplaced) {
         //System.out.println("Request could not be granted.");
         return false;
      }

      //TODO -- Assign free doctors to second shifts
      /*
      for (Map.Entry<Integer, Integer> entry : freeDays.entrySet()) {
         //If doctor has free days 
         if (entry.getValue() > 0) {
            
            freeDays.put(entry.getKey(), entry.getValue() - 1);
         }
      }*/

      //Success if got here
      return true;
      
   }
   
   //TODO -- Grants request for doctor's preferred work time if possible
   public boolean requestWorkTime(ArrayList<Day> schedule, ArrayList<Integer> docIDs,
           Integer requestingDoc, Day requestedDay, String shiftName) {
      return false;
   }
}