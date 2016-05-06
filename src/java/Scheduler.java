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
   private final static int TYPICAL_SHIFTS_PER_DAY = 5;
   
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

   private Calendar requestedDay;
   private int id = 1;
   private ArrayList<Shift> calendar;
   //private ArrayList<Integer> docIDs;
   // look out 3 weeks in advance

   public Scheduler(Calendar startingDate) {
      initShifts();
      initDayIndices();
   }

   private void initShifts() {
      calendar = new ArrayList<Shift>();

      try {
         // get doctors
         DBConnection connection = new DBConnection();
         String query = "SELECT * FROM DoctorShifts";
         ResultSet result = connection.execQuery(query);

         while (result.next()) {
            Shift shift = new Shift();
            shift.setShift(result.getString(Table.SHIFT));
            shift.setDate(result.getDate(Table.DATE));
            shift.setDoctor(result.getInt(Table.ID));
            calendar.add(shift);
         }

         // get technicians
         query = "SELECT * FROM TechnicianShifts";
         result = connection.execQuery(query);
         while (result.next()) {
            for (int i = 0; i < calendar.size(); ++i) {
               Shift shift = calendar.get(i);
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
      int docID;
      Calendar date;
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
   
   private boolean checkPreferredShift(int[] schedules, int docID, Calendar date, String shiftName) {
      int dayOfWeek;
      int[] dayIndices;
      int shiftOffset;
      
      dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
      
      switch(shiftName) {
         case Shift.EARLY:
            shiftOffset = EarlyMorning;
            break;
         case Shift.MORNING:
            shiftOffset = Morning;
            break;
         case Shift.LATE:
            shiftOffset = LateMorning;
            break;
         case Shift.SURGERY:
            shiftOffset = Surgery;
            break;
         case Shift.OVERNIGHT:
            shiftOffset = Overnight;
            if (dayOfWeek == Calendar.SUNDAY)
               shiftOffset = SundayOvernight;
            break;
         case Shift.SUNDAY:
            shiftOffset = SundayMorning;
            break;
         default:
            System.out.println("Got a weird shift name: " + shiftName);
            return false;    
      }
            
      if (shiftOffset != SundayOvernight && shiftOffset != SundayMorning) {
         shiftOffset = dayOfWeek * TYPICAL_SHIFTS_PER_DAY + shiftOffset;
      }
      
      return (schedules[shiftOffset] == docID); 
   }
   
   private boolean checkTimeOff(int[] schedules, int docID, Calendar date) {
      int dayOfWeek;
      int[] dayIndices;
      int i;
      
      dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
      
      switch(dayOfWeek) {
         case Calendar.SUNDAY:
            dayIndices = SundayIndices;
            break;
         case Calendar.MONDAY:
            dayIndices = MondayIndices;
            break;
         case Calendar.TUESDAY:
            dayIndices = TuesdayIndices;
            break;
         case Calendar.WEDNESDAY:
            dayIndices = WednesdayIndices;
            break;
         case Calendar.THURSDAY:
            dayIndices = ThursdayIndices;
            break;
         case Calendar.FRIDAY:
            dayIndices = FridayIndices;
            break;
         default:
            dayIndices = SaturdayIndices;
            break;
      }
      
      for (i = 0; i < dayIndices.length; i++) {
         if (dayIndices[i] == docID)
            return true;
      }
      return false;
   }

      for (int i = 0; i < MAX_CALENDAR_DAYS; ++i) {
         calendar.add(new Day(date));
         date.add(date.DAY_OF_MONTH, 1);
      }
   }

   private void readDoctorShiftsFromDatabase(Calendar startingDate) {
      // DBConnection con = new DBConnection();
      // // ResultSet set = con.execQuery("SELECT *" +
      // //                               "FROM DoctorShifts");
      

      // for (int i = 0; i < doctorShifts.size(); ++i) {
      //    EmployeeShift employeeShift = doctorShifts.get(i); //TODO assuming it has been read successfully
      //    Calendar shiftDate = employeeShift.getDate();
      //    //subtract shiftDate from startingDate to get index for calendar
      //    long newTime = shiftDate.getTimeInMillis() 
      //                    - startingDate.getTimeInMillis();
      //    int index = (int) newTime / MILLISECONDS_TO_DAYS;
      //    //use index in to get calendar day
      //    Day day = calendar.get(index);
      //    String shiftName = employeeShift.getShift();
      //    Shift shift = null;
      //    for (int j = 0; j < shifts.size(); ++j) {
      //       if (shifts.get(j).get() == shiftID) {
      //          shift = shifts.get(j);
      //       }
      //    }
      //    day.setShift(shift);
      // }
   }

   private void readDoctorPreferredShifts() {
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