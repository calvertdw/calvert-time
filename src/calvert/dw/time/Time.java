package calvert.dw.time;

import java.util.Calendar;

import calvert.dw.data.parsing.StringParser;

public class Time
{
	// 2012 --> 2012
	// January --> 0
	// 1st --> 0
	// 11PM --> 23
	private boolean					isNull			= false;
	private int						year			= 2012;
	private int						month			= 0;
	private int						day				= 0;
	private int						hour			= 0;
	private int						minute			= 0;
	private int						second			= 0;
	public static final String[]	MONTHS			= {"January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October", "November", "December"};
	public static final int[]		DAYSINMONTHS	= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
			31										};
	public static final String[]	DAYSOFWEEK		= {"Saturday", "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday"		};
	public static final Integer		SATURDAY		= 0, SUNDAY = 1, MONDAY = 2, TUESDAY = 3,
			WEDNESDAY = 4, THURSDAY = 5, FRIDAY = 6;
	public static final String[]	DAYSOFWEEK_ABBR	= {"Sat", "Sun", "Mon", "Tues", "Wed", "Thurs",
			"Fri"									};
	
	/**
	 * Returns a new time with one day length.
	 */
	public static Time oneDay()
	{
		return new Time(0, 0, 1, 0, 0, 0);
	}
	
	/**
	 * Used for addition and subtraction.
	 */
	public static Time nDays(int n)
	{
		return new Time(0, 0, n, 0, 0, 0);
	}
	
	/**
	 * Initializes a time of 0.
	 */
	public Time()
	{
		formatTime();
	}
	
	public Time(int year, int month, int day, int hour, int minute, int second)
	{
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		formatTime();
	}
	
	public Time copy()
	{
		return new Time(year, month, day, hour, minute, second);
	}
	
	public void set(Time time)
	{
		year = time.year;
		month = time.month;
		day = time.day;
		hour = time.hour;
		minute = time.minute;
		second = time.second;
		formatTime();
	}
	
	/**
	 * Initializes Time with a raw time string.
	 * 
	 * year:month:day:hour:minute:second
	 * 
	 * @param rawTime
	 */
	public Time(String rawTime)
	{
		if (rawTime.length() > 0)
		{
			StringParser p = new StringParser(rawTime);
			year = Integer.valueOf(p.parseUntil(new char[] {':'}, new char[] {' '}));
			p.increment();
			month = Integer.valueOf(p.parseUntil(new char[] {':'}, new char[] {' '}));
			p.increment();
			day = Integer.valueOf(p.parseUntil(new char[] {':'}, new char[] {' '}));
			p.increment();
			hour = Integer.valueOf(p.parseUntil(new char[] {':'}, new char[] {' '}));
			p.increment();
			minute = Integer.valueOf(p.parseUntil(new char[] {':'}, new char[] {' '}));
			p.increment();
			second = Integer.valueOf(p.parseUntil(new char[] {' '}));
		}
		formatTime();
	}
	
	private void formatTime()
	{
		while (second > 59)
		{
			second -= 60;
			minute += 1;
		}
		while (minute > 59)
		{
			minute -= 60;
			hour += 1;
		}
		while (hour > 23)
		{
			hour -= 24;
			day += 1;
		}
		while (day >= getDaysInMonth())
		{
			day -= getDaysInMonth();
			month += 1;
		}
		while (month > 11)
		{
			month -= 12;
			year += 1;
		}
		setNull(false);
	}
	
	/**
	 * Returns the difference of time.
	 * 
	 * @param time
	 * @return difference
	 */
	public Time minus(Time time)
	{
		Time duration = this;
		duration.second -= time.getSecond();
		if (duration.second < 0)
		{
			duration.minute -= 1;
			duration.second += 60;
		}
		duration.minute -= time.getMinute();
		if (duration.minute < 0)
		{
			duration.hour -= 1;
			duration.minute += 60;
		}
		duration.hour -= time.getHour();
		if (duration.hour < 0)
		{
			duration.day -= 1;
			duration.hour += 24;
		}
		duration.day -= time.getDay();
		if (duration.day < 0)
		{
			duration.month -= 1;
			duration.day += duration.getDaysInMonth();
		}
		duration.month -= time.getMonth();
		if (duration.month < 0)
		{
			duration.year -= 1;
			duration.month += 12;
		}
		duration.year -= time.getYear();
		return duration;
	}
	
	/**
	 * Returns the sum of time.
	 * 
	 * @param time
	 * @return sum
	 */
	public Time plus(Time time)
	{
		Time total = this;
		total.second += time.getSecond();
		if (total.second > 59)
		{
			total.second -= 60;
			total.minute += 1;
		}
		total.minute += time.minute;
		if (total.minute > 59)
		{
			total.minute -= 60;
			total.hour += 1;
		}
		total.hour += time.hour;
		if (total.hour > 23)
		{
			total.hour -= 24;
			total.day += 1;
		}
		total.day += time.day;
		if (total.day > total.getDaysInMonth() - 1)
		{
			total.day -= getDaysInMonth();
			total.month += 1;
		}
		total.month += time.month;
		if (total.month > 11)
		{
			total.month -= 12;
			total.year += 1;
		}
		total.year += time.year;
		return total;
	}
	
	public boolean equals(Time time)
	{
		if (year == time.year && month == time.month && day == time.day && hour == time.hour && minute == time.minute && second == time.second)
			return true;
		else
			return false;
	}
	
	/**
	 * Returns true if this time is before time 't'.
	 */
	public Boolean isBefore(Time t)
	{
		if (year < t.getYear())
			return true;
		if (year > t.getYear())
			return false;
		if (month < t.getMonth())
			return true;
		if (month > t.getMonth())
			return false;
		if (day < t.getDay())
			return true;
		if (day > t.getDay())
			return false;
		if (hour < t.getHour())
			return true;
		if (hour > t.getHour())
			return false;
		if (minute < t.getMinute())
			return true;
		if (minute > t.getMinute())
			return false;
		if (second < t.getSecond())
			return true;
		if (second > t.getSecond())
			return false;
		return false;
	}
	
	/**
	 * Returns true if this time is after time 't'.
	 */
	public Boolean isAfter(Time t)
	{
		if (year < t.getYear())
			return false;
		if (year > t.getYear())
			return true;
		if (month < t.getMonth())
			return false;
		if (month > t.getMonth())
			return true;
		if (day < t.getDay())
			return false;
		if (day > t.getDay())
			return true;
		if (hour < t.getHour())
			return false;
		if (hour > t.getHour())
			return true;
		if (minute < t.getMinute())
			return false;
		if (minute > t.getMinute())
			return true;
		if (second < t.getSecond())
			return false;
		if (second > t.getSecond())
			return true;
		return false;
	}
	
	/**
	 * True if 00:00 AM.
	 * 
	 * @return midnight
	 */
	public boolean isMidnight()
	{
		if (hour + minute == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Sets time to midnight 00:00 AM.
	 * 
	 */
	public void setMidnight()
	{
		setHour(0);
		setMinute(0);
	}
	
	public static Time getCurrentTime()
	{
		return new Time(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND));
	}
	
	/**
	 * Returns year:month:day:hour:minute:second
	 */
	@Override
	public String toString()
	{
		return Integer.toString(getYear()) + ":" + Integer.toString(getMonth()) + ":" + Integer.toString(getDay()) + ":" + Integer.toString(getHour()) + ":" + Integer.toString(getMinute()) + ":" + Integer.toString(getSecond());
	}
	
	/**
	 * Gets total time in seconds for the minutes and hour portion.
	 * 
	 * @return
	 */
	public double toSecondsExcludeYearDayMonth()
	{
		int totalSeconds = 0;
		totalSeconds += second;
		totalSeconds += minute * 60;
		totalSeconds += hour * 3600;
		
		return totalSeconds;
	}
	
	/**
	 * [DayOfWeek], [Month] [Day], [Year] [HH]:[MM]:[SS] [AM/PM]
	 */
	public String getFormattedAll()
	{
		return getMonthString() + " " + Integer.toString(getDay() + 1) + ", " + Integer.toString(getYear()) + " " + getFormattedHoursMinutesSeconds();
	}
	
	/**
	 * [MM]/[DD]
	 */
	public String getFormattedMonthDay()
	{
		return Integer.toString(getMonth() + 1) + "/" + Integer.toString(getDay() + 1);
	}
	
	/**
	 * [Month] [Day], [Year]
	 */
	public String getFormattedDate()
	{
		return getMonthString() + " " + Integer.toString(getDay() + 1) + ", " + Integer.toString(getYear());
	}
	
	/**
	 * [DayOfWeek], [Month] [Day], [Year]
	 */
	public String getFormattedDate2()
	{
		return getDayOfWeek() + ", " + getMonthString() + " " + Integer.toString(getDay() + 1) + ", " + Integer.toString(getYear());
	}
	
	/**
	 * [DD]/[MM]/[YYYY]
	 */
	public String getFormattedDate3()
	{
		return String.format("%tM", (long) getDay()) + "/" + String.format("%tM", (long) getMonth()) + "/" + getYear();
	}
	
	/**
	 * [HH]:[MM]:[SS] [AM/PM]
	 * 
	 * @return
	 */
	public String getFormattedHoursMinutesSeconds()
	{
		int hour = getHour();
		String postFix = "AM";
		if (hour == 0)
			hour = 12;
		if (getHour() > 12)
		{
			hour -= 12;
			postFix = "PM";
		}
		return Integer.toString(hour) + ":" + String.format("%02d", (long) getMinute()) + ":" + String.format("%02d", (long) getSecond()) + " " + postFix;
	}
	
	/**
	 * [HH]:[MM]
	 * 
	 * @return formatted
	 */
	public String getFormattedHoursMinutesNoSuffix()
	{
		return Integer.toString(hour) + ":" + Integer.toString(minute);
	}
	
	/**
	 * [HH]:[MM] [AM/PM]
	 * 
	 * @return
	 */
	public String getFormattedHoursMinutes()
	{
		int hour = getHour();
		String postFix = "AM";
		if (hour == 0)
			hour = 12;
		else if (getHour() == 12)
			postFix = "PM";
		else if (getHour() > 12)
		{
			hour -= 12;
			postFix = "PM";
		}
		return Integer.toString(hour) + ":" + String.format("%02d", (long) getMinute()) + " " + postFix;
	}
	
	public Integer getDayOfWeekInt()
	{
		int daysSinceJan_1_2000 = 0;
		int i;
		for (i = 2000; i < year; i++)
		{
			daysSinceJan_1_2000 += getDaysInYear(i);
		}
		for (i = 0; i < month; i++)
		{
			daysSinceJan_1_2000 += getDaysInMonth(i);
		}
		daysSinceJan_1_2000 += day;
		return daysSinceJan_1_2000 % 7;
	}
	
	public String getDayOfWeek()
	{
		return DAYSOFWEEK[getDayOfWeekInt()];
	}
	
	public String getDayOfWeekAbbr()
	{
		return DAYSOFWEEK_ABBR[getDayOfWeekInt()];
	}
	
	public int getDaysInMonth()
	{
		if (getMonth() % 12 == 1 && (year - 2000) % 4 == 0) // Leap year
		{
			return 29;
		}
		else
			return DAYSINMONTHS[Math.abs(getMonth() % 12)];
	}
	
	public int getDaysInMonth(int month)
	{
		if (month % 12 == 1 && (year - 2000) % 4 == 0) // Leap year
		{
			return 29;
		}
		else
			return DAYSINMONTHS[month % 12];
	}
	
	public int getYear()
	{
		return year;
	}
	
	private int getDaysInYear(int year)
	{
		if ((year - 2000) % 4 == 0) // Leap year
			return 366;
		else
			return 365;
	}
	
	public void setYear(int year)
	{
		this.year = year;
		formatTime();
	}
	
	public static int getMonthFromString(String month)
	{
		for (int i = 0; i < 12; i++)
		{
			if (MONTHS[i].equals(month))
				return i;
		}
		return 0;
	}
	
	public int getMonth()
	{
		return month;
	}
	
	public static String getMonthStringStatic(int month)
	{
		return MONTHS[month];
	}
	
	public String getMonthString()
	{
		return MONTHS[getMonth()];
	}
	
	public String getMonthString(int numChars)
	{
		String monthName = "";
		for (int i = 0; i < numChars; i++)
		{
			monthName += MONTHS[getMonth()].charAt(i);
		}
		return monthName;
	}
	
	public void setMonth(int month)
	{
		this.month = month;
		formatTime();
	}
	
	public int getDay()
	{
		return day;
	}
	
	public void setDay(int day)
	{
		this.day = day;
		formatTime();
	}
	
	public int getHour()
	{
		return hour;
	}
	
	public void setHour(int hour)
	{
		this.hour = hour;
		formatTime();
	}
	
	public int getMinute()
	{
		return minute;
	}
	
	public void setMinute(int minute)
	{
		this.minute = minute;
		formatTime();
	}
	
	public int getSecond()
	{
		return second;
	}
	
	public void setSecond(int second)
	{
		this.second = second;
		formatTime();
	}
	
	public boolean isNull()
	{
		return isNull;
	}
	
	public void setNull(boolean isNull)
	{
		this.isNull = isNull;
	}
}
