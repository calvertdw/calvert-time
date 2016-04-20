package calvert.dw.time;

import calvert.dw.data.parsing.StringParser;

public class TimeInput
{
	/**
	 * Smart algorithm that sets the hour and minute of time with input string.
	 * 
	 * @param input
	 * @param time
	 * @return updated
	 */
	public static Time setHourAndMinuteFromString(String input, Time time)
	{
		// Delete spaces
		String input2 = "";
		for (int i = 0; i < input.length(); i++)
			if (input.charAt(i) != ' ')
				input2 += input.charAt(i);
		input = input2;
		
		// Count numbers
		int numbers = 0;
		for (int i = 0; i < input.length(); i++)
			if (input.charAt(i) >= '0' && input.charAt(i) <= '9')
				numbers++;
			else
				break;
		if (numbers == 0)
			return time;
		
		boolean containsPM = false;
		for (int i = 0; i < input.length(); i++)
			if (input.charAt(i) == '+' || input.charAt(i) == 'p' || input.charAt(i) == 'P')
				containsPM = true;
		
		// Seperate hours and minute intelligently
		String hourString = "";
		String minuteString = "";
		boolean onMinute = false;
		for (int i = 0; i < numbers && i < 4; i++)
		{
			if (hourString.length() < 1 && (input.charAt(i) == '1' || input.charAt(i) == '2'))
				hourString += input.charAt(i);
			else if (hourString.length() < 2 && !onMinute)
			{
				if (hourString.length() == 1 && input.charAt(i) > '2' && containsPM)
					minuteString += input.charAt(i);
				else
					hourString += input.charAt(i);
				onMinute = true;
			}
			else
				minuteString += input.charAt(i);
		}
		
		// Get hour and minute
		int hour = 0;
		if (hourString.length() > 0)
			hour = Integer.valueOf(hourString);
		int minute = 0;
		if (minuteString.length() > 0)
			minute = Integer.valueOf(minuteString);
		
		// Account for PM
		if (input.length() > numbers && (input.charAt(numbers) == 'p' || input.charAt(numbers) == '+' || input.charAt(numbers) == 'P') && hour + 12 < 24)
			hour += 12;
		
		// Set hour and minute
		time.setHour(hour);
		time.setMinute(minute);
		
		return time;
	}
	
	/**
	 * Gets time for formatted string such as "13March2013" and "1December2014".
	 * 
	 * @param date
	 * @return result
	 */
	public static Time getTimeFromFormattedDate(String date)
	{
		StringParser p = new StringParser(date);
		
		String day = p.parseWhile(StringParser.NUMBERS);
		String month = p.parseWhile(StringParser.ALL_LETTERS);
		String year = p.parseRest();
		
		return new Time(Integer.valueOf(year), Time.getMonthFromString(month), Integer.valueOf(day) - 1, 0, 0, 0);
	}
}
