
package com.septeto.iso3166.joiner;

public class CodeCreator
{

	private static final String	BASE_CHARS	= "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int	BASE_NUM	= BASE_CHARS.length ();

	// A VAlue of ZZZ woulkd be 46655, therefore, we add 5 zeros
	private static final int	CCN3_OFFSET	= 100000;

	private static Integer makeConversion (String subdivisionCode)
	{
		int numCode = 0;
		for (char c : subdivisionCode.toUpperCase ().toCharArray ())
		{
			numCode *= BASE_NUM;

			int idxOf = BASE_CHARS.indexOf (c);
			if (-1 == idxOf)
			{
				return null;
			}
			else
			{
				numCode += idxOf;
			}
		}

		return numCode;
	}

	public static Integer makeConversion (Integer ccn3, String fullCode)
	{
		String[] parts = fullCode.split ("-");

		try
		{
			return (ccn3 * CCN3_OFFSET) + CodeCreator.makeConversion (parts[1]);
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}

		return null;
	}

	public static Integer makeNumericConversion (Integer ccn3, String fullCode)
	{

		String[] parts = fullCode.split ("-");

		try
		{
			return (ccn3 * CCN3_OFFSET) + Integer.parseInt (parts[1]);
		}
		catch (NumberFormatException nfe)
		{
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}

		return null;

	}

}
