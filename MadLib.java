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
	public MadLib(String entered) throws BadMadLibDataException{
		wordsArray = new ArrayList<>();
		boolArray = new ArrayList<>();
		int count = 0;
		Boolean flip = false;

		for (int c = 0; c < entered.length(); c++) {
			if (entered.charAt(c) == '%' || c == entered.length()-1) {

				wordsArray.add(entered.substring(count, c));
				boolArray.add(flip);
				if(entered.charAt(c) == '%')
					flip = !flip;

				if(count == c && !flip)
					throw new BadMadLibDataException("A blank space is 0 chars long! Can't do that.");
				count = flip ? c : c + 1;
			}
			if(count == entered.length()-1 && flip)
				throw new BadMadLibDataException("You are missing a % somewhwere.");
		}
	}

	public void playNprint() {
		System.out.println(play());
	}

	public String play() {
		StringBuilder temp = new StringBuilder();
		Scanner sc = new Scanner(System.in);

		for (short c = 0; c < wordsArray.size(); c++) {
			if (boolArray.get(c)) {
				System.out.printf("Please enter a "
						+ wordsArray.get(c).substring(1) + ":  ");
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
		MadLib a = new MadLib("I don't think that the %noun% in the future to our %emotion% and we should probably invest more time in the %noun% before %pronoun% die.");

			//  MadLib b = new MadLib("%DOES%%THIS%%BREAK%%ANYTHING?%");
			a.playNprint();
		} catch(BadMadLibDataException ex){
			ex.printStackTrace();
		}
		// a.playNprint();

	}
}
