/**
 * Scheduler is used to schedule shifts.
 * 
 * @author Brian Fung
 * @author Justin Zaman
 * @author Kevin Yang
 */

import java.sql.ResultSet;
import java.util.*;
import java.time.*;

public class Scheduler {
   DBConnection connection;
   
   private final static int MAX_CALENDAR_DAYS = 28;
   private final static int MILLISECONDS_TO_DAYS = 1000 * 60 * 60 * 24;
   private final static int DAYS_PER_WEEK = 7;
   private final static int WORKABLE_SHIFTS_PER_WEEK = 4;
   private final static int INVALID = -1;
   private final static int TOTAL_SHIFTS_PER_WEEK = 32;
   private final static int TYPICAL_SHIFTS_PER_DAY = 5;
   private final static int NUM_SHIFTS_PER_WEEK = 32;

   
   //Indexing constants
   private final static int SUNDAY = 0;
   private final static int MONDAY = 1;
   private final static int TUESDAY = 2;
   private final static int WEDNESDAY = 3;
   private final static int THURSDAY = 4;
   private final static int FRIDAY = 5;
   private final static int SATURDAY = 6;
   
   private final static int EarlyMorning = 0;
   private final static int Morning = 1;
   private final static int LateMorning = 2;
   private final static int Surgery = 3;
   private final static int Overnight = 4;
   private final static int SundayMorning = 31;
   private final static int SundayOvernight = 30;
   
   private int[] SundayIndices;
   private int[] MondayIndices;
   private int[] TuesdayIndices;
   private int[] WednesdayIndices;
   private int[] ThursdayIndices;
   private int[] FridayIndices;
   private int[] SaturdayIndices;
   private int[] SurgeryIndices;
   private int[] OvernightIndices;

   private LocalDate startDate;
   private ArrayList<Shift> weekOne;
   private ArrayList<Shift> weekTwo;
   private ArrayList<Shift> weekThree;
   private ArrayList<Shift> weekFour;

   private LocalDate aDate;

   private Calendar requestedDay;
   private int id = 1;
   private ArrayList<Shift> calendar;
   //private ArrayList<Integer> docIDs;
   // look out 3 weeks in advance

   //TODO -- Probably have to update this
   public Scheduler() {
      initCalendar();
      initDayIndices();
   }

   private void initCalendar() {
      weekOne = new ArrayList<Shift>(NUM_SHIFTS_PER_WEEK);
      weekTwo = new ArrayList<Shift>(NUM_SHIFTS_PER_WEEK);
      weekThree = new ArrayList<Shift>(NUM_SHIFTS_PER_WEEK);
      weekFour = new ArrayList<Shift>(NUM_SHIFTS_PER_WEEK);
 
      initStartdate();
      aDate = LocalDate.parse(startDate.toString());

      initWeek(weekOne, aDate);
      initWeek(weekTwo, aDate);
      initWeek(weekThree, aDate);
      initWeek(weekFour, aDate);
   }

   private void initStartdate() {
      try {
         DBConnection connection = new DBConnection();
         String query = "SELECT min(date) AS date from DoctorShifts";
         ResultSet result = connection.execQuery(query);
         if (result.next()) {
            startDate = LocalDate.parse(result.getDate(Table.DATE).toString());
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void initWeek(ArrayList<Shift> week, LocalDate startDate) {
      try {
         // get doctors
         DBConnection connection = new DBConnection();
         String earliest = aDate.toString();
         aDate = aDate.plusDays(7);
         String latest = aDate.toString();
         String query = "select * from doctorShifts " + 
                        "where date >= '" + earliest + "' and date < '" + latest + "' " + 
                        "order by date ASC";
         ResultSet result = connection.execQuery(query);

         while (result.next()) {
            String shiftName  = result.getString(Table.SHIFT);
            java.sql.Date date = result.getDate(Table.DATE);
            if (!week.isEmpty()) {
               Shift shift = week.get(week.size() - 1);
               if (!shift.equals(date, shiftName)) {
                  Shift newShift = new Shift();
                  newShift.setShift(shiftName);
                  newShift.setDate(date);
                  newShift.setFirstDoctor(result.getInt(Table.ID));
                  week.add(newShift);
               }
               else {
                  shift.setSecondDoctor(result.getInt(Table.ID));
               }
            }
            else {
               Shift newShift = new Shift();
               newShift.setShift(shiftName);
               newShift.setDate(date);
               week.add(newShift);
            }
         }

         // get technicians
         query = "SELECT * FROM TechnicianShifts ORDER BY date ASC";
         result = connection.execQuery(query);
         while (result.next()) {
            for (int i = 0; i < week.size(); ++i) {
               Shift shift = week.get(i);
               if (shift.equals(result.getDate(Table.DATE), 
                                result.getString(Table.SHIFT))) {
                  int technician = result.getInt(Table.ID);
                  if (!shift.hasFirstTechnician()) {
                     shift.setFirstTechnician(technician);
                  }
                  else {
                     shift.setSecondTechnician(technician);
                  }
               }
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   
   //Associate the 32 shift indices with special attributes like day, overnight,
   //or surgery AKA "Magic"
   private void initDayIndices() {
      SundayIndices = new int[] {30, 31};
      MondayIndices = new int[] {0, 1, 2, 3, 4};
      TuesdayIndices = new int[] {5, 6, 7, 8, 9};
      WednesdayIndices = new int[] {10, 11, 12, 13, 14};
      ThursdayIndices = new int[] {15, 16, 17, 18, 19};
      FridayIndices = new int[] {20, 21, 22, 23, 24};
      SaturdayIndices = new int[] {25, 26, 27, 28, 29};
      
      OvernightIndices = new int[] {4, 9, 14, 19, 24, 29, 30};
      SurgeryIndices = new int[] {3, 8, 13, 18, 23};
   }

   //WARNING: Uses Calendar
   private ArrayList<Request> getRequestsForWeek(Calendar requestedDay) {
      this.requestedDay = requestedDay;
      
      ArrayList<Request> list = new ArrayList<Request>();

      list.addAll(getRequests(Request.SICK_DAY));
      list.addAll(getRequests(Request.VACATION_DAY));
      list.addAll(getRequests(Request.PREFERRED_SHIFT));

      return list;
   }

   private ArrayList<Request> getRequests(int requestType) {
      ArrayList<Request> list = new ArrayList<Request>();
      String tablename = Table.getTableName(requestType);

      try {
         DBConnection connection = new DBConnection();
         String query = "SELECT * " + 
                        "FROM " + tablename + " " + 
                        "WHERE id = " + id + " and " + 
                        " ";
         ResultSet result = connection.execQuery(query);

         while (result.next()) {
            Request request = new Request();
            request.setType(requestType);
            request.setDoctorID(id);
            request.setDate(result.getDate(Table.DATE));

            if (requestType == Request.PREFERRED_SHIFT) {
               request.setShift(result.getString(Table.SHIFT));
            }
            list.add(request);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return list;
   }
   
   public boolean generateSchedule(ArrayList<Shift> shifts, ArrayList<Integer> empIDs, 
           ArrayList<Request> requestList, int employeeType) {
      int highestEmpIndex = empIDs.size() - 1;
      int[] weekShifts = new int[TOTAL_SHIFTS_PER_WEEK];
      int[] secondaryWeekShifts = new int[TOTAL_SHIFTS_PER_WEEK];
      Map<Integer, Integer> realToAlgorithmIDs = new HashMap<>();
      Map<Integer, Integer> algorithmToRealIDs = new HashMap<>();
      int i;
      boolean isGoodSchedule;
      
      //Creates a bi-directional map from algorithm ids (0-#empIDs) to real 
      //ids (arbitrary)
      for (i = 0; i < empIDs.size(); i++) {
         realToAlgorithmIDs.put(empIDs.get(i), i);
         algorithmToRealIDs.put(i, empIDs.get(i));
      }
      
      for (i = 0; i < weekShifts.length; i++) {
         weekShifts[i] = 0;
      }
      
      while (weekShifts.length > 0) {
         weekShifts = incrementPermutation(weekShifts, highestEmpIndex);
         isGoodSchedule = true;
         
         //Check overnights
         if(!checkOvernightConstraint(weekShifts)) {
            isGoodSchedule = false;
         }
         
         //Check surgeries
         if(!checkSurgeryConstraint(weekShifts)) {
            isGoodSchedule = false;
         }
         
         //Check no emps working same day and no day shifts day after overnight
         if(!checkSingleShiftPerDayConstraint(weekShifts)) {
            isGoodSchedule = false;
         }
         
         //Check no doctor working more than 4 shifts
         if(!checkMaxShiftConstraint(weekShifts, highestEmpIndex)) {
            isGoodSchedule = false;
         }
         
         
         if (isGoodSchedule) {
            for (Request request : requestList) {
               if (checkGoodRequest(weekShifts, request) == false) {
                  isGoodSchedule = false;
                  break;
               }
            }
         }
         
         if (isGoodSchedule) {
            //Put secondary shifts
            //SECSHIFT
            secondaryWeekShifts = assignSecondaryShifts(weekShifts, 
                    realToAlgorithmIDs, algorithmToRealIDs, requestList);
            //Update schedule with new doctor assignments
            pushSchedule(shifts, weekShifts, secondaryWeekShifts, 
                    employeeType, algorithmToRealIDs);
            return true;
         }
      }

      return false;
   }
   
   private int[] assignSecondaryShifts(int[] primaryShifts, 
           Map<Integer, Integer> realToAlgorithmIDs, 
           Map<Integer, Integer> algorithmToRealIDs,
           ArrayList<Request> requestList) {
      
      int[] secondaryShifts = new int[TOTAL_SHIFTS_PER_WEEK];
      ArrayList<Integer> shiftsRemaining = new ArrayList<>();
      int i;
      int docIndex;
      int realDocID;
      int shiftIndex;
      Request request;
      
      //Initialize secondary shifts with invalid ids
      for (i = 0; i < secondaryShifts.length; i++) {
         secondaryShifts[i] = INVALID;
      }
      
      //Initialize shifts remaining for each doctor
      for (i = 0; i < realToAlgorithmIDs.size(); i++) {
         shiftsRemaining.add(WORKABLE_SHIFTS_PER_WEEK);
      }
      
      //Decrement shifts remaining for doctors working a first shift
      for (i = 0; i < primaryShifts.length; i++) {
         docIndex = primaryShifts[i];
         shiftsRemaining.set(docIndex, shiftsRemaining.get(docIndex) - 1);
      }
      
      //Decrement shifts for doctors taking time off
      for (i = 0; i < requestList.size(); i++) {
         request = requestList.get(i);
         if (request.getType() != Request.PREFERRED_SHIFT) {
            realDocID = request.getDoctorID();
            docIndex = realToAlgorithmIDs.get(realDocID);
            shiftsRemaining.set(docIndex, shiftsRemaining.get(docIndex) - 1);
         }
      }
      
      //For all doctors with shifts remaining, add them to shift ensuring they
      //don't already work that day or the overnight of previous day
      for (i = 0; i < shiftsRemaining.size(); i++) {
         while (shiftsRemaining.get(i) > 0) {
            shiftIndex = addEmployeeToSecondaryShift(i, primaryShifts, secondaryShifts);
            if (shiftIndex != INVALID) {
               secondaryShifts[shiftIndex] = i;
            }
         }
      }
      
      return secondaryShifts;
   }
   
   private int addEmployeeToSecondaryShift(int empID, int[] primaryShifts, int[] secondaryShifts) {
      int[] dailyIDs;
      int shiftIndex = INVALID;
      
      if (shiftIndex == INVALID) {
         shiftIndex = addEmployeeToDay(SundayIndices, INVALID, primaryShifts, 
              secondaryShifts, empID);
      }

      if (shiftIndex == INVALID) {
         shiftIndex = addEmployeeToDay(MondayIndices, SundayOvernight, primaryShifts, 
              secondaryShifts, empID);
      }
      
      if (shiftIndex == INVALID) {
         shiftIndex = addEmployeeToDay(TuesdayIndices, MondayIndices[Overnight], primaryShifts, 
              secondaryShifts, empID);
      }
      
      if (shiftIndex == INVALID) {
         shiftIndex = addEmployeeToDay(WednesdayIndices, TuesdayIndices[Overnight], primaryShifts, 
              secondaryShifts, empID);
      }
      
      if (shiftIndex == INVALID) {
         shiftIndex = addEmployeeToDay(ThursdayIndices, WednesdayIndices[Overnight], primaryShifts, 
              secondaryShifts, empID);
      }
      
      if (shiftIndex == INVALID) {
         shiftIndex = addEmployeeToDay(FridayIndices, ThursdayIndices[Overnight], primaryShifts, 
              secondaryShifts, empID);
      }
      
      if (shiftIndex == INVALID) {
         shiftIndex = addEmployeeToDay(SaturdayIndices, FridayIndices[Overnight], primaryShifts, 
              secondaryShifts, empID);
      }
      
      return shiftIndex;
   }

   private int addEmployeeToDay(int[] dailyIndices, int lastOvernight, 
           int[] primaryShifts, int[] secondaryShifts, int empID) {
      
      int i;
      int dayIndex;
      int potentialShift = INVALID;
      
      //Check if employee is in last night overnights
      if (lastOvernight != INVALID && primaryShifts[lastOvernight] == empID) {
         return INVALID;
      }
      //Check if employee is in primary shifts of this day
      for (i = 0; i < dailyIndices.length; i++) {
         dayIndex = dailyIndices[i];
         if (primaryShifts[dayIndex] == empID) {
            return INVALID;
         }
         if (secondaryShifts[dayIndex] == empID) {
            return INVALID;
         }
         else if (secondaryShifts[dayIndex] == INVALID && i != dailyIndices.length - 1) {
            potentialShift = dayIndex;
         }    
      }
      
      return potentialShift;
   }
   
   //Pushes good schedule's shift assignments to shift list
   private void pushSchedule(ArrayList<Shift> oldShifts, int[] newShifts, int[] secondaryShifts,
           int employeeType, Map<Integer, Integer> algorithmToRealIDs) {
      int i;
      int algorithmID;
      int realID;
      int shiftIndex;
      LocalDate day;
      String shiftName;
      Shift shift;
      
      //Assigns employee to first shift of this day and this shift
      for (i = 0; i < oldShifts.size(); i++) {
         shift = oldShifts.get(i);
         day = shift.getDate();
         shiftName = oldShifts.get(i).getShift();
         shiftIndex = getShiftIndex(day, shiftName);
         algorithmID = newShifts[shiftIndex];
         realID = algorithmToRealIDs.get(algorithmID);
         
         if (employeeType == Employee.DOCTOR) {
            shift.setFirstDoctor(realID);
         }
         else {
            shift.setFirstTechnician(realID);
         }
         
         //Assigns secondary shifts
         //SECSHIFT
         algorithmID = newShifts[shiftIndex];
         if (algorithmID != INVALID) {
            realID = algorithmToRealIDs.get(algorithmID);
            if (employeeType == Employee.DOCTOR) {
               shift.setSecondDoctor(realID);
            }
            else {
               shift.setSecondTechnician(realID);
            }
         }
      }
              
   }
   
   private int[] getDayIndices(LocalDate day) {
      DayOfWeek dayOfWeek = day.getDayOfWeek();
      
      switch(dayOfWeek) {
         case SUNDAY:
            return SundayIndices;
         case MONDAY:
            return MondayIndices;
         case TUESDAY:
            return TuesdayIndices;
         case WEDNESDAY:
            return WednesdayIndices;
         case THURSDAY:
            return ThursdayIndices;
         case FRIDAY:
            return FridayIndices;
         default:
            return SaturdayIndices;
      }
   }
   
   private int getShiftOffset(DayOfWeek dayOfWeek, String shiftName) {
      switch(shiftName) {
         case Shift.EARLY:
            return EarlyMorning;
         case Shift.MORNING:
            return Morning;
         case Shift.LATE:
            return LateMorning;
         case Shift.SURGERY:
            return Surgery;
         case Shift.OVERNIGHT:
            if (dayOfWeek == DayOfWeek.SUNDAY)
               return SundayOvernight;
            return Overnight;
         case Shift.SUNDAY:
            return SundayMorning;
         default:
            System.out.println("Got a weird shift name: " + shiftName);
            return INVALID;    
      }
   }
   private int getShiftIndex(LocalDate day, String shiftName) {
      int shiftIndex;
      int[] dayIndices;
      int shiftOffset;
      DayOfWeek dayOfWeek;
      
      dayOfWeek = day.getDayOfWeek();
      
      dayIndices = getDayIndices(day);
      shiftOffset = getShiftOffset(dayOfWeek, shiftName);
      
      if (dayOfWeek == DayOfWeek.SUNDAY) {
         shiftIndex = shiftOffset;
      }
      else {
         shiftIndex = dayIndices[shiftOffset];
      }
      
      return shiftIndex;
   }
   
   private int[] incrementPermutation(int[] perm, int maxIndex) {
      int ndx;
      for (ndx = TOTAL_SHIFTS_PER_WEEK - 1; ndx >= 0; ndx--) {
         if (perm[ndx] < maxIndex) {
            perm[ndx]++;
            break;
         }
      }
      if (perm[ndx] == maxIndex) {
         for(ndx = ndx - 1; ndx >= 0; ndx--) {
            perm[ndx]++;
            if (perm[ndx] < maxIndex) {
               break;
            }
            else if (ndx == 0) {
               return new int[0];
            }
         }
      }
      return perm;
   }
   
   private boolean checkGoodRequest(int[] schedules, Request request) {
      int docID;
      LocalDate date;
      String shiftName;
      
      docID = request.getDoctorID();
      date = request.getDate();
      
      if (request.getType() == Request.PREFERRED_SHIFT) {
         shiftName = request.getShift();
         return checkPreferredShift(schedules, docID, date, shiftName);
      }
      else {
         return checkTimeOff(schedules, docID, date);
      }
   }
   
   private boolean checkPreferredShift(int[] schedules, int docID, LocalDate date, String shiftName) {
      DayOfWeek dayOfWeek;
      int dayOffset;
      int[] dayIndices;
      int shiftOffset;
      
      dayOfWeek = date.getDayOfWeek();
      shiftOffset = this.getShiftOffset(dayOfWeek, shiftName);
            
      dayOffset = convertDayOfWeekToIndex(dayOfWeek);
      
      if (shiftOffset != SundayOvernight && shiftOffset != SundayMorning) {
         shiftOffset = dayOffset * TYPICAL_SHIFTS_PER_DAY + shiftOffset;
      }
      
      return (schedules[shiftOffset] == docID); 
   }
   
   private int convertDayOfWeekToIndex(DayOfWeek day) {
      switch (day) {
         case SUNDAY:
            return SUNDAY;
         case MONDAY:
            return MONDAY;
         case TUESDAY:
            return TUESDAY;
         case WEDNESDAY:
            return WEDNESDAY;
         case THURSDAY:
            return THURSDAY;
         case FRIDAY:
            return FRIDAY;
         default:
            return SATURDAY;
      }
   }
   
   private boolean checkTimeOff(int[] schedules, int docID, LocalDate date) {
      int dayOfWeek;
      int[] dayIndices;
      int i;
      
      dayIndices = getDayIndices(date);
      
      for (i = 0; i < dayIndices.length; i++) {
         if (dayIndices[i] == docID)
            return true;
      }
      return false;
   }

   private boolean checkOvernightConstraint(int[] schedules) {
      int i;
      int overnightShiftIndex;
      int docID;
      ArrayList<Integer> seen = new ArrayList<>();
      
      
      for (i = 0; i < OvernightIndices.length; i++) {
         overnightShiftIndex = OvernightIndices[i];
         docID = schedules[overnightShiftIndex];
         if (seen.contains(docID)) {
            return false;
         }
         seen.add(docID);
      }
      return true;
   }

   private boolean checkSurgeryConstraint(int[] schedules) {
      int i;
      int surgeryShiftIndex;
      int docID;
      ArrayList<Integer> seen = new ArrayList<>();
      
      
      for (i = 0; i < SurgeryIndices.length; i++) {
         surgeryShiftIndex = SurgeryIndices[i];
         docID = schedules[surgeryShiftIndex];
         if (seen.contains(docID)) {
            return false;
         }
         seen.add(docID);
      }
      return true;
   }

   private boolean checkMaxShiftConstraint(int[] schedules, int highestDocIndex) {
      int i;
      int j;
      int numShifts;
      
      for (i = 0; i <= highestDocIndex; i++) {
         numShifts = 0;
         for (j = 0; j < schedules.length; j++) {
            if (schedules[j] == i) {
               numShifts++;
            }
            if (numShifts >= WORKABLE_SHIFTS_PER_WEEK) {
               return false;
            }
         }
      }
      return true;
   }

   private boolean checkSingleShiftPerDayConstraint(int[] schedules) {
      ArrayList<Integer> dailyIDs = new ArrayList<>();
      int i;
      int j;
      int id;
      
      if (!checkDay(schedules, SundayIndices, INVALID)) {
         return false;
      }
      
      if (!checkDay(schedules, MondayIndices, SUNDAY)) {
         return false;
      }
      
      if (!checkDay(schedules, TuesdayIndices, MONDAY)) {
         return false;
      }
      
      if (!checkDay(schedules, WednesdayIndices, TUESDAY)) {
         return false;
      }
      
      if (!checkDay(schedules, ThursdayIndices, WEDNESDAY)) {
         return false;
      }
      
      if (!checkDay(schedules, FridayIndices, THURSDAY)) {
         return false;
      }
      
      if (!checkDay(schedules, SaturdayIndices, FRIDAY)) {
         return false;
      }
      
      return true;
   }
   
   private boolean checkDay(int[] schedules, int[] dayIndices, int prevDayIndex) {
      int i;
      int id;
      ArrayList<Integer> dailyIDs = new ArrayList<>();
      for (i = 0; i < dayIndices.length; i++) {
         id = schedules[dayIndices[i]];
         if (dailyIDs.contains(id)) {
            return false;
         }
         if (prevDayIndex >= 0 && id == OvernightIndices[prevDayIndex]) {
            return false;
         }
         dailyIDs.add(i);
      }
      return true;
   }

   private void pushCalendarToDatabase() {
      pushWeekToDatabase(weekOne);
      pushWeekToDatabase(weekTwo);
      pushWeekToDatabase(weekThree);
      pushWeekToDatabase(weekFour);
   }

   private void pushWeekToDatabase(ArrayList<Shift> week) {
      try {
         DBConnection connection = new DBConnection();
         for (int index = 0; index < week.size(); ++index) {
            Shift shift = week.get(index);
            String querySecondHalf = "WHERE date = '" + shift.getDateAsString() + 
                                     "' and " + 
                                     "shift = '" + shift.getShift() + "'";
            System.out.println("SecondHalf : " + querySecondHalf);
            String query = "UPDATE DoctorShifts SET id = " +  
                           shift.getFirstDoctor() + " " + querySecondHalf;
            connection.execQuery(query); 

            if (shift.hasSecondDoctor()) {
               query = "UPDATE DoctorShifts SET id = " +  
                        shift.getSecondDoctor() + " " + querySecondHalf;
               connection.execQuery(query);
            }

            if (shift.hasFirstTechnician()) {
               query = "UPDATE TechnicianShifts SET id = " +  
                        shift.getFirstTechnician() + " " + querySecondHalf;
               connection.execQuery(query);
            }
            
            if (shift.hasSecondTechnician()) {
               query = "UPDATE TechnicianShifts SET id = " +  
                        shift.getSecondTechnician() + " " + querySecondHalf;
               connection.execQuery(query);
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void viewSchedule() {
      // printWeek(weekOne);
      weekOne.get(0).setFirstDoctor(9);
      pushCalendarToDatabase();
   }

   private void printWeek(ArrayList<Shift> week) {
         System.out.println("Size: " + week.size());

      for (int i = 0; i < week.size(); ++i) {
         week.get(i).print();
      }
   }
}