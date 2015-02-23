import java.util.*;

public class MadLibSet {
	private static HashMap<String, MadLib> mad = new HashMap<>();
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
		return add("Untitled_" + units, end);
	}

	public static MadLib giveRandom() {
		ArrayList<MadLib> mads = new ArrayList<>(mad.values());

		return mads.get((new Random()).nextInt(mads.size()));
	}

	public static void main(String[] args) {
		MadLib a;
		try {
			a = new MadLib("Why don't you love ^noun as much as I do?");
			MadLibSet.add("First Key", a);
		} catch (BadMadLibDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}