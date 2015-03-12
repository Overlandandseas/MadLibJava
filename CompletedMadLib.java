import java.util.Calendar;


public class CompletedMadLib {
	private String user;
	private MadLib madLib;
	private String date;
	
	private String month;
	private String day;
	private String year;
	private String hour;
	private String min;
	private String am_pm;
	
	CompletedMadLib (MadLib madLib, String user) {
		this.madLib = madLib;
		this.user = user;
		
		Calendar c = Calendar.getInstance();
		month = Integer.toString(c.get(Calendar.MONTH));
		day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		year = Integer.toString(c.get(Calendar.YEAR));
		hour = Integer.toString(c.get(Calendar.HOUR));
		min = Integer.toString(c.get(Calendar.MINUTE));
		am_pm = "AM";
		if (c.get(Calendar.AM_PM) == 1) {
			am_pm = "PM";
		}
		date = month+"/"+day+"/"+year+"("+hour+":"+min+am_pm+")";
	}
	
	public String getUser() {
		return user;
	}

	public MadLib getMadLib() {
		return madLib;
	}

	public String getDate() {
		return date;
	}

	public String getMonth() {
		return month;
	}

	public String getDay() {
		return day;
	}

	public String getYear() {
		return year;
	}

	public String getHour() {
		return hour;
	}

	public String getMin() {
		return min;
	}

	public String getAm_pm() {
		return am_pm;
	}
	
	
	
}
