import java.util.*;

public class MadLibSet {
	private static HashMap<String, MadLib> mad = new HashMap<>();
	private static HashMap<MadLib, ArrayList<String>> fil = new HashMap<>();
	private static int units = 0;

	// YAY FOR HASHMAPS
	// what else can A set do?

	public static Boolean add(String key, MadLib ent) {
		// This returns a Bool if the add was successful for now.
		// Later this should throw an exception.
		if (mad.containsKey(key)) {
			System.out.println("Key already exists.");
			return false;
		} else {
			mad.put(key, ent);
			return true;
		}
	}

	public static Boolean add(MadLib end) {
		return add("Untitled_" + units++, end);
	}

	public static void addCompleted(MadLib ent, String s){
		if(fil.get(ent) == null)
			fil.put(ent, new ArrayList<String>());
		fil.get(ent).add(s);
	}

	public static MadLib giveRandom() {
		ArrayList<MadLib> mads = new ArrayList<>(mad.values());
		return mads.get((new Random()).nextInt(mads.size()));
	}

	public static void printList(){
		System.out.println("+--------------------+");
		for(String s : mad.keySet()){
			if(s.length() > 18)
				System.out.println("| " + s.substring(0, 15) + "... |");
			else{
				System.out.print("| " + s);
				for(int c = 0; c < 19 - s.length(); c++)
					System.out.print(" ");
				System.out.println("|");
			}
			System.out.println("+--------------------+");
		}
	}

	public static void main(String[] args) {
		MadLib a;
		MadLib b;
		MadLib c;
		try {
			a = new MadLib("Why don't you love %plural noun% as much as I do?");
			MadLibSet.add("First Key", a);
			b = new MadLib("%noun% is good in moderation. However not enough %emotion% can kill the man.");
			MadLibSet.add("Second Test",  b);
			c = new MadLib("This is a %adjetive% Madlib and you love %something you hate%. 	");
			MadLibSet.add("The third Madlib has a long title that might be too long", c);
			MadLibSet.printList();
		} catch (BadMadLibDataException e) {
			e.printStackTrace();
		}




	}
}
