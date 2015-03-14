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
	
	public static String getReadable(int number) {
		if (number <= completed.size() && number > 0)
			return completed.get(number-1).getCompletedMadLib();
		return null;
	}

	public static void addCompleted(CompletedMadLib c){
		completed.addFirst(c);
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

	public static void printMadLibList(){
		System.out.println(MadLibSet.getMadLibList());
	}
	
	public static void printCompletedList() {
		System.out.println(MadLibSet.getCompletedList());
	}
	
	public static String getMadLibList() {
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
	
	public static String getCompletedList() {
		String[] completedStrings = new String[completed.size()];
		int longest = 0;
		int i;
		for(i=0; i<completedStrings.length; i++) {
			completedStrings[i] = completed.get(i).toString();
			if (completedStrings[i].length() > longest)
				longest = completedStrings[i].length();
		}
		String list = "";
		list = list.concat("+-----");
		for (i=0; i<longest; i++)
			list = list.concat("-");
		list = list.concat("-+\n");
		int leftover;
		int n = 1;
		for(String s : completedStrings) {
			leftover = longest - s.length();
			list = list.concat("| "+n+" | " + s);
			for (i=0; i<leftover; i++)
				list = list.concat(" ");
			list = list.concat(" |\n");
			n++;
		}
		list = list.concat("+-----");
		for (i=0; i<longest; i++)
			list = list.concat("-");
		list = list.concat("-+\n");
		return list;
	}
	
	public static boolean saveMadLibs() {
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
	
	public static boolean saveCompleted() {
		try {
			PrintWriter printWriter = new PrintWriter("CompletedMadLibs.txt", "UTF-8");
			
			for (CompletedMadLib c : completed) {
				int[] dates = c.getDateArray();
				printWriter.println(c.getUser()+" : "+
									c.getCompletedMadLibTitle()+" : "+
									c.getCompletedMadLib()+" : "+
									dates[0]+" : "+
									dates[1]+" : "+
									dates[2]+" : "+
									dates[3]+" : "+
									dates[4]+" : "+
									dates[5]);
			}
			printWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean loadMadLibs() {
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
			MadLibSet.saveMadLibs();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadMadLibDataException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean loadCompleted() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("CompletedMadLibs.txt"));
			Scanner parser;
			String line;
			String user, title, madlib;
			while ( (line = reader.readLine()) != null) {
				parser = new Scanner(line);
				parser.useDelimiter(" : ");
				int[] dateArray = new int[6];
				user = parser.next();
				title = parser.next();
				madlib = parser.next();
				dateArray[0] = Integer.valueOf(parser.next());
				dateArray[1] = Integer.valueOf(parser.next());
				dateArray[2] = Integer.valueOf(parser.next());
				dateArray[3] = Integer.valueOf(parser.next());
				dateArray[4] = Integer.valueOf(parser.next());
				dateArray[5] = Integer.valueOf(parser.next());
				//System.out.printf("key: %s, raw: %s\n", key, raw);
				CompletedMadLib temp = new CompletedMadLib();
				temp.setUser(user);
				temp.setCompletedMadLib(madlib);
				temp.setCompletedMadLibTitle(title);
				temp.setDateArray(dateArray);
				
				MadLibSet.addCompleted(temp);
				parser.close();
			}
			reader.close();
			return true;
		} catch (FileNotFoundException e) {
			MadLibSet.saveCompleted();
		} catch (IOException e) {
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
			MadLibSet.printMadLibList();
		} catch (BadMadLibDataException e) {
			e.printStackTrace();
		}
		
	}
}
