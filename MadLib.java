//JAVA IS A LANGUAGE PEOPLE USE AT TIME

import java.util.*;
import java.io.*;
import java.net.*;

public class MadLib {

	ArrayList<String> wordsArray;
	ArrayList<Boolean> boolArray;
	int boolArray_int;
	short rating;


	// default constructor
	public MadLib() throws BadMadLibDataException{
		throw new BadMadLibDataException("You must pass a string to create a new MadLib. The default constructor should never be called.");
	}

	// constructor
	public MadLib(String entered) throws BadMadLibDataException{
		wordsArray = new ArrayList<>();
		boolArray = new ArrayList<>();
		if(!entered.contains("%"))
			throw new BadMadLibDataException("i hate you...");

		recurseMadLib(entered, false);
		intify();
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

	private void intify(){
		StingBuilder boolStr = new StringBuilder();
		boolStr.append("1");
		for(boolean b : boolArray){
			boolStr.append(b ? "1" : "0");
		}
		boolArray_int = Integer.parseInt(boolStr.toString(), 2);
	}
	public void playNstore(){
		MadLibSet.addCompleted(this, play());
	}
	public int getInt(){
		return boolArray_int;
	}
	public void playNprint() {
		System.out.println(play());
	}

	public String play(MadLibsHandler handler){
		return playRec(handler, new StringBuilder(), 0);
	}

	public String playRec(MadLibsHandler handler, StringBuilder s, int n){
		if(boolArray.get(n)){
			handler.sendString("Please enter a "+ wordsArray.get(n) + ":  ");
			s.append(handler.receiveString());
		} else
			s.append(wordsArray.get(n));
		if(n < wordsArray.size())
			return playRec(handler, s, n++);
		return s.toString();
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
		MadLib b = new MadLib("I don't think that% the %noun% in the future to our %emotion% and we should probably invest more time in the %noun% before %pronoun% die.");

			//  MadLib b = new MadLib("Right %DOES% %THIS% %BREAK% %ANYTHING?% lLl this is after here");
			//                      0123456789012345678901234567890123
			b.playNprint();
		} catch(BadMadLibDataException ex){
			ex.printStackTrace();
		}
		// a.playNprint();

	}
}
