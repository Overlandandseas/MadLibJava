import java.util.*;


public class MadLibSet {
  private static HashMap<String, MadLib> mad = new HashMap<>();
  //YAY FOR HASHMAPS

  public static Boolean add(String key, MadLib ent){
    //This returns a Bool if the add was successful for now.
    //Later this should throw an exception.
    if(mad.containsKey(key)){
      System.out.println("Key already exists.");
      return false;
    } else{
      mad.put(key, ent);
      return true;
    }
  }

  public static MadLib giveRandom(){

    ArrayList<MadLib> mads = new ArrayList<>(mad.values());

    return mads.get((new Random()).nextInt(mads.size()));
  }




  public static void main(String[] args){


  }

}
