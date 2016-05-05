/**
 * Scheduler is used to schedule shifts.
 * 
 * @author Brian Fung
 * @author Justin Zaman
 * @author Kevin Yang
 */

import java.sql.ResultSet;
import java.util.*;

public class Scheduler {
   DBConnection connection;
   
   private final static int MAX_CALENDAR_DAYS = 28;
   private final static int MILLISECONDS_TO_DAYS = 1000 * 60 * 60 * 24;
   private final static int DAYS_PER_WEEK = 7;
   private final static int MAX_SHIFTS_PER_WEEK = 4;
   private final static int INVALID = -1;
   private final static int TOTAL_SHIFTS_PER_WEEK = 32;
   private final static int SUNDAY = 0;
   private final static int MONDAY = 1;
   private final static int TUESDAY = 2;
   private final static int WEDNESDAY = 3;
   private final static int THURSDAY = 4;
   private final static int FRIDAY = 5;
   private final static int SATURDAY = 6;
   
   private int[] SundayIndices;
   private int[] MondayIndices;
   private int[] TuesdayIndices;
   private int[] WednesdayIndices;
   private int[] ThursdayIndices;
   private int[] FridayIndices;
   private int[] SaturdayIndices;
   private int[] SurgeryIndices;
   private int[] OvernightIndices;

   private Calendar requestedDay;
   private int id = 1;
   //private ArrayList<Integer> docIDs;
   // look out 3 weeks in advance

   //TODO -- Probably have to update this
   public Scheduler(Calendar startingDate) {
      //calendar = new ArrayList<Day>(MAX_CALENDAR_DAYS);
      shifts = new ArrayList<Shift>();
      //TODO read in shifts
      doctors = new ArrayList<Employee>();
      doctorShifts = new ArrayList<EmployeeShift>();
      doctorPreferredShifts = new ArrayList<EmployeePreferredShift>();
      doctorTimeOff = new ArrayList<EmployeeTimeOff>();
      //docIDs = new ArrayList<Integer>();
      for (int i = 0; i < doctorTimeOff.size(); ++i) {
         //TODO add timeoff for multiple days
      }

      //addDaysToCalendar(startingDate);
      initDayIndices();
   }
   
   //Associate the 32 shift indices with special attributes like day, overnight,
   //or surgery
   private void initDayIndices() {
      SundayIndices = new int[] {30, 31};
      MondayIndices = new int[] {0, 1, 2, 3, 4};
      TuesdayIndices = new int[] {5, 6, 7, 8, 9};
      WednesdayIndices = new int[] {10, 11, 12, 13, 14};
      ThursdayIndices = new int[] {15, 16, 17, 18, 19};
      FridayIndices = new int[] {20, 21, 22, 23, 24};
      SaturdayIndices = new int[] {25, 26, 27, 28, 29};
      
      OvernightIndices = new int[] {4, 9, 14, 19, 24};
      SurgeryIndices = new int[] {3, 8, 13, 18, 23};
   }

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
      String tablename = getTableName(requestType);

      try {
         DBConnection connection = new DBConnection();
         String query = "SELECT * " + 
                        "FROM " + tablename + " " + 
                        "WHERE id = " + id + " and " + 
                        " "
         ResultSet result = connection.execQuery(query);

         while (result.next()) {
            Request request = new Request();
            request.setType(requestType);
            request.setDoctorID(id);
            request.setDate(result.getDate(Table.DATE));

            if (requestType == Request.PREFERRED_SHIFT) {
               request.setShift(result.getString(Table.SHIFT))
            }
            list.add(request);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return list;
   }
   
   public boolean generateSchedule(Calendar day, ArrayList<Integer> docIDs, 
           ArrayList<Request> requestList) {
      int highestDocIndex = docIDs.size() - 1;
      int[] weekShifts = new int[TOTAL_SHIFTS_PER_WEEK];
      int i;
      boolean isGoodSchedule;
      
      for (i = 0; i < weekShifts.length; i++) {
         weekShifts[i] = 0;
      }
      
      while (weekShifts.length > 0) {
         weekShifts = incrementPermutation(weekShifts, highestDocIndex);
         isGoodSchedule = true;
         
         //Check overnights
         if(!checkOvernightConstraint(weekShifts)) {
            isGoodSchedule = false;
         }
         
         //Check surgeries
         if(!checkSurgeryConstraint(weekShifts)) {
            isGoodSchedule = false;
         }
         
         //Check no docs working same day and no day shifts day after overnight
         if(!checkSingleShiftPerDayConstraint(weekShifts)) {
            isGoodSchedule = false;
         }
         
         //Check no doctor working more than 4 shifts
         if(!checkMaxShiftConstraint(weekShifts, highestDocIndex)) {
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
            //Push schedule
            //Push new request
            return true;
         }
      }

      return false;
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
            if (numShifts >= MAX_SHIFTS_PER_WEEK) {
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
   
   //Updates assigned shift from oldID to newID
   private void pushSchedule(Integer newID, Integer oldID, Calendar requestedDay, 
           String table) {
      
      try {
         connection = new DBConnection();

         String query = "UPDATE " + table + " " + 
                        "SET id = " + newID + " " + 
                        "WHERE id = " + oldID + " " + 
                        "AND date = " + requestedDay;
             
         connection.execUpdate(query);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}