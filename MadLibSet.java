import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class MadLibSet {
	private static HashMap<String, MadLib> mad = new HashMap<>();
	private static LinkedList<CompletedMadLib> completed = new LinkedList<>();
	private static int units = 0;

	// YAY FOR HASHMAPS
	// what else can A set do?

	public static Boolean add(String key, MadLib ent) {
		// This returns a Bool if the add was successful for now.
		// Later this should throw an exception.
		if (key == null) {
			units++;
			ent.setTitle("Untitled_" + units);
		} else {
			ent.setTitle(key);
		}
		if (mad.containsKey(ent.getTitle())) {
			System.out.println("Key already exists.");
			return false;
		} else {
			mad.put(ent.getTitle(), ent);
			return true;
		}
	}
	
	public static MadLib getPlayable(String key) {
		MadLib mlib = mad.get(key);
		if (mlib != null) {
			try {
				mlib = new MadLib(mlib.getTitle(), mlib.toString());
				return mlib;
			} catch (BadMadLibDataException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void addCompleted(MadLib complete, String user_name){
		completed.addFirst(new CompletedMadLib(complete, user_name));
	}
	//NOT AT ALL HOW YOU WOULD DO IT
	// public static Sting giveRandomComp(){
	// 	ArrayList<String> strings = new ArrayList<>(fil.values());
	// 	return strings.get((new Random()).nextInt(strings.size()));
	// }
	public static MadLib giveRandom() {
		ArrayList<MadLib> mads = new ArrayList<>(mad.values());
		return mads.get((new Random()).nextInt(mads.size()));
	}

	public static void printList(){
		System.out.println(MadLibSet.getList());
	}
	
	public static String getList() {
		String list = "";
		list = list.concat("+--------------------+\n");
		for(String s : mad.keySet()){
			if(s.length() > 18)
				list = list.concat("| " + s.substring(0, 15) + "... |\n");
			else {
				list = list.concat("| " + s);
				for(int c = 0; c < 19 - s.length(); c++)
					list = list.concat(" ");
				list = list.concat("|\n");
			}
		}
		list = list.concat("+--------------------+\n");
		return list;
	}
	
	public static boolean saveAll() {
		try {
			PrintWriter printWriter = new PrintWriter("MadLibs.txt", "UTF-8");
			
			Set<String> keySet = mad.keySet();
			for (String key : keySet) {
				printWriter.println( key+" : "+(mad.get(key)) );
			}
			printWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean loadAll() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("MadLibs.txt"));
			Scanner parser;
			String line;
			String key;
			String raw;
			while ( (line = reader.readLine()) != null) {
				parser = new Scanner(line);
				parser.useDelimiter(" : ");
				key = parser.next();
				raw = parser.next();
				//System.out.printf("key: %s, raw: %s\n", key, raw);
				MadLib temp = new MadLib(raw);
				temp.setTitle(key);
				MadLibSet.add(key, temp);
				parser.close();
			}
			
			reader.close();
			return true;
		} catch (FileNotFoundException e) {
			MadLibSet.saveAll();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadMadLibDataException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		MadLib a, b, c;
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
