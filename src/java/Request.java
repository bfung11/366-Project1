import java.util.*;

public class Request {
   public final static int TIME_OFF = 1;
   public final static int SICK_DAY = 2;
   public final static int VACATION_DAY = 3;
   public final static int PREFERRED_SHIFT = 4;

   private int type;
   private int doctorID;
   private Calendar date;
   private String shiftName;

   public void setType(int type) {
      this.type = type;
   }

   public int getType() {
      return type;
   }

   public void setDoctorID(int doctorID) {
      this.doctorID = doctorID;
   }

   public int getDoctorID() {
      return doctorID;
   }

   public void setDate(Calendar date) {
      this.date = date;
   }

   public Calendar getDate() {
      return date;
   }

   public void setShiftName(String shiftName) {
      this.shiftName = shiftName;
   }

   public String getShiftName() {
      return shiftName;
   }
}