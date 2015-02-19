//JAVA IS A LANGUAGE PEOPLE USE AT TIME

import java.util.*;

public class MadLib {

	ArrayList<String> wordsArray;
	ArrayList<Boolean> boolArray;
	short rating;

	// default constructor
	public MadLib() {
		System.out.println("Script Verification Failed (Error Code: 332)");
		System.exit(1);
		return;
	}

	// constructor
	public MadLib(String entered) {
		wordsArray = new ArrayList<>();
		boolArray = new ArrayList<>();
		int count = 0;
		Boolean flip = false;

		for (int c = 0; c < entered.length(); c++) {
			if (entered.charAt(c) == '%') {
				wordsArray.add(entered.substring(count, c));
				boolArray.add(flip);
				flip = !flip;
				count = flip ? c : c + 1;

			}

			/*
			 * if(c != entered.length() && entered.charAt(c) != ' '){
			 * wordtemp.append(entered.charAt(c)); } else {
			 * wordsArray.add(count, wordtemp.toString()); boolArray.add(count,
			 * wordtemp.charAt(0) == '^'); count++; wordtemp = new
			 * StringBuilder("");
			 */
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
		MadLib a = new MadLib(
				"I don't think that the %noun% is the future to our %emotion% and we should probably invest more time in the %noun% before %pronoun% die.");

		a.playNprint();
	}
}
