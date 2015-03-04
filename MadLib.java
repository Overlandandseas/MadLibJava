//JAVA IS A LANGUAGE PEOPLE USE AT TIME

import java.util.*;

public class MadLib {

	ArrayList<String> wordsArray;
	ArrayList<Boolean> boolArray;
	short rating;


	// default constructor
	public MadLib() throws BadMadLibDataException{
		throw new BadMadLibDataException("You must pass a string to create a new MadLib. The default constructor should never be called.");
	}

	// constructor
	/*
	public MadLib(String entered) throws BadMadLibDataException{
		wordsArray = new ArrayList<>();
		boolArray = new ArrayList<>();
		int count = 0;
		Boolean flip = false;
		System.out.println(entered.length());
		for (int c = 0; c < entered.length(); c++) {
			if (entered.charAt(c) == '%'|| c == entered.length()) {
				wordsArray.add(entered.substring(count, c));
				boolArray.add(flip);
				if(entered.charAt(c) == '%')
					flip = !flip;
				System.out.println("C is " + c + ". And count is " + count + ".");
				System.out.println("Flip is " + flip);
				count = flip ? c : c + 1;
				if(count == c && !flip)
					throw new BadMadLibDataException("A blank space is 0 chars long! Can't do that.");
				//if(c != entered.length()-1)
				if(count == entered.length()-1 && flip)
					throw new BadMadLibDataException("You are missing a % somewhwere.");
			}
		}
	}*/

	public MadLib(String entered) throws BadMadLibDataException{
		wordsArray = new ArrayList<>();
		boolArray = new ArrayList<>();
		recurseMadLib(entered, false);
	}
	private void recurseMadLib(String ent, Boolean flip) throws BadMadLibDataException{
		if(ent.substring(0, ent.indexOf("%")).length() == 0 && flip)
			throw new BadMadLibDataException("A blank word is an empty string, can't do that.");
		wordsArray.add(ent.substring(0, ent.indexOf("%")));
		boolArray.add(flip);
		if(ent.substring(ent.indexOf("%")+1).contains("%"))
			recurseMadLib(ent.substring(ent.indexOf("%")+1), !flip);
		else if(!flip)
				throw new BadMadLibDataException("You are missing a % somewhwere.");
				else{
					boolArray.add(false);
					wordsArray.add(ent.substring(ent.indexOf("%")+1));
					// recurseMadLib(ent.substring(ent.indexOf("%")+1), false);
				}
	}

	public void playNstore(){
		MadLibSet.addCompleted(this, play());
	}
	public void playNprint() {
		System.out.println(play());
	}

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

	public static void main(String[] args) {
		try{
		MadLib b = new MadLib("I don't think that the %noun% in the future to our %emotion% and we should probably invest more time in the %noun% before %pronoun% die.");

			//  MadLib b = new MadLib("Right %DOES% %THIS% %BREAK% %ANYTHING?% lLl this is after here");
			//                      0123456789012345678901234567890123
			b.playNprint();
		} catch(BadMadLibDataException ex){
			ex.printStackTrace();
		}
		// a.playNprint();

	}
}
