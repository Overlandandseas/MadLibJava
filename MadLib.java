//JAVA IS A LANGUAGE PEOPLE USE AT TIME

import java.util.*;

public class MadLib {

	private ArrayList<String> wordsArray;
	private ArrayList<Boolean> boolArray;
	private String entered;
	short rating;


	// default constructor
	public MadLib() throws BadMadLibDataException{
		throw new BadMadLibDataException("You must pass a string to create a new MadLib. The default constructor should never be called.");
	}

	// constructor
	public MadLib(String entered) throws BadMadLibDataException{
		wordsArray = new ArrayList<>();
		boolArray = new ArrayList<>();
		this.entered = entered;
		if(!entered.contains("%"))
			throw new BadMadLibDataException("Formatting error");

		recurseMadLib(entered, false);
		intify();
	}
	
	private void recurseMadLib(String ent, Boolean flip) throws BadMadLibDataException{
		if(ent.substring(0, ent.indexOf("%")).length() == 0 && flip)
			throw new BadMadLibDataException("Blank must have a type");
		wordsArray.add(ent.substring(0, ent.indexOf("%")));
		boolArray.add(flip);
		if(ent.substring(ent.indexOf("%")+1).contains("%"))
			recurseMadLib(ent.substring(ent.indexOf("%")+1), !flip);
		else if(!flip)
				throw new BadMadLibDataException("Formatting error");
				else{
					boolArray.add(false);
					wordsArray.add(ent.substring(ent.indexOf("%")+1));
					// recurseMadLib(ent.substring(ent.indexOf("%")+1), false);
				}
	}

	private int intify(){
		int i = 0;
		for(boolean b : boolArray)
			if(b)
				i++;
		return i;
	}
	public void playNstore(){
		MadLibSet.addCompleted(this, play());
	}


	public void playNprint() {
		System.out.println(play());
	}

//	public String play(MadLibsHandler handler){
//		return playRec(handler, new StringBuilder(), 0);
//	}
//
//	public String playRec(MadLibsHandler handler, StringBuilder s, int n){
//		if(boolArray.get(n)){
//			handler.sendString("Please enter a "+ wordsArray.get(n) + ":  ");
//			s.append(handler.receiveString());
//		} else
//			s.append(wordsArray.get(n));
//		if(n < wordsArray.size())
//			return playRec(handler, s, n++);
//		return s.toString();
//	}


	public String play() {
		StringBuilder temp = new StringBuilder();
		Scanner sc = new Scanner(System.in);
		// System.out.println("size: " + wordsArray.size());

		for (short c = 0; c < wordsArray.size(); c++) {
			// System.out.println("c: " + c);
			if (boolArray.get(c)) {
				System.out.printf("Please enter a "+ wordsArray.get(c) + ":  ");
				temp.append(sc.nextLine());
			} else {
				temp.append(wordsArray.get(c));
			}

		}
		sc.close();
		return temp.toString();
	}
	
	@Override
	public String toString() {
		return this.entered;
	}

	public static void main(String[] args) {
		try{
			// MadLib b = new MadLib("I don't think that% the %noun% in the future to our %emotion% and we should probably invest more time in the %noun% before %pronoun% die.");
			//  MadLib b = new MadLib("Right %DOES% %THIS% %BREAK% %ANYTHING?% lLl this is after here");
			//                      0123456789012345678901234567890123
			MadLib b = new MadLib("Hey %name%, I like your %body part%. Like a lot.");
			b.playNprint();
		} catch(BadMadLibDataException ex){
			ex.printStackTrace();
		}
		// a.playNprint();

	}
}
