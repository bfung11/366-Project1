import java.util.*;

public class Shift {
   private int id;
   private String name;
   private Calendar fromTime; //TODO may need to change datatypes
   private Calendar toTime; //TODO may need to change datatypes
   
   public Shift(int id, String name) {
      this.id = id;
      this.name = name;
   }

   public int getID() {
      return id;
   }

   public String getName() {
      return name;
   }
}
