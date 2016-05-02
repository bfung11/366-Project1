import java.util.*;
import java.sql.*;

public class Shift {
   private final static String FROM_TIME_TABLENAME = "fromTime";
   private final static String TO_TIME_TABLENAME = "toTime";

   private String name;
   private Time fromTime; //TODO may need to change datatypes
   private Time toTime; //TODO may need to change datatypes
   
   public Shift(String name, Time fromTime, Time toTime) {
      this.name = name;
      this.fromTime = fromTime;
      this.toTime = toTime;
   }

   public Shift(String name) {
      try {
         DBConnection connection = new DBConnection();
         String query = "select * " +
                        "from Shifts " +
                        "where name = '" + name + "'";
         ResultSet result = connection.execQuery(query);
         this.name = name;
         this.fromTime = result.getTime(FROM_TIME_TABLENAME);
         this.toTime = result.getTime(TO_TIME_TABLENAME);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public String getName() {
      return name;
   }

   public Time getFromTime() {
      return fromTime;
   }

   public Time getToTime() {
      return toTime;
   }
}
