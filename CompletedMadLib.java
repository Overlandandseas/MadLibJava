import java.util.Calendar;


public class CompletedMadLib {
	private String user;
	private String completedMadLib;
	private String completedMadLibTitle;
	private String date;
	
	private int[] dateArray;
	// {Month, Day, Year, Hour, Minute, AM/PM}
	
	CompletedMadLib (MadLib madLib, String user) {
		this.completedMadLib = madLib.getFilledMadlib();
		this.completedMadLibTitle = madLib.getTitle();
		this.user = user;
		
		Calendar c = Calendar.getInstance();
		dateArray = new int[6];
		date = "";
		
		dateArray[0] = c.get(Calendar.MONTH);
		date = date.concat(Integer.toString(dateArray[0]));
		dateArray[1] = c.get(Calendar.DAY_OF_MONTH);
		date = date.concat("/"+Integer.toString(dateArray[1]));
		dateArray[2] = c.get(Calendar.YEAR);
		date = date.concat("/"+Integer.toString(dateArray[2]));
		dateArray[3] = c.get(Calendar.HOUR);
		if (dateArray[3] == 0)
			date = date.concat(" (12:");
		else
			date = date.concat(" ("+Integer.toString(dateArray[3])+":");
		dateArray[4] = c.get(Calendar.MINUTE);
		if (dateArray[4] < 10)
			date = date.concat("0");
		date = date.concat(Integer.toString(dateArray[4]));
		dateArray[5] = c.get(Calendar.AM_PM);
		if (dateArray[5] == 0) {
			date = date.concat(" AM)");
		} else {
			date = date.concat(" PM)");
		}
	}
	
	CompletedMadLib () {
		
	}
	

	@Override
	public String toString() {
		return user+", \""+completedMadLibTitle+"\", "+date;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCompletedMadLib() {
		return completedMadLib;
	}

	public void setCompletedMadLib(String completedMadLib) {
		this.completedMadLib = completedMadLib;
	}

	public String getCompletedMadLibTitle() {
		return completedMadLibTitle;
	}

	public void setCompletedMadLibTitle(String completedMadLibTitle) {
		this.completedMadLibTitle = completedMadLibTitle;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int[] getDateArray() {
		return dateArray;
	}

	public void setDateArray(int[] dateArray) {
		this.dateArray = dateArray;
		date = "";
		
		date = date.concat(Integer.toString(dateArray[0]));
		date = date.concat("/"+Integer.toString(dateArray[1]));
		date = date.concat("/"+Integer.toString(dateArray[2]));
		if (dateArray[3] == 0)
			date = date.concat(" (12:");
		else
			date = date.concat(" ("+Integer.toString(dateArray[3])+":");
		if (dateArray[4] < 10)
			date = date.concat("0");
		date = date.concat(Integer.toString(dateArray[4]));
		if (dateArray[5] == 0) {
			date = date.concat(" AM)");
		} else {
			date = date.concat(" PM)");
		}
	}

	

}
