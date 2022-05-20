
package com.septeto.iso3166.joiner;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class Joiner
{
	private UtilISO_3166_2_CFG cfg;

	public Joiner (UtilISO_3166_2_CFG cfg)
	{
		this.cfg = cfg;
	}

	public static void main (String[] args)
	{
		UtilISO_3166_2_CFG cfg = new UtilISO_3166_2_CFG ();
		cfg.loadCfg (args);

		Joiner j = new Joiner (cfg);
		j.join ();

	}

	private void join ()
	{
		JSONArray jarr = new JSONArray ();

		File folder = new File (cfg.splittedFilePath);
		for (final File fileEntry : folder.listFiles ())
		{
			if (fileEntry.isFile ())
			{
				String fileName = fileEntry.getName ();
				if (fileName.endsWith (".json"))
				{

					try
					{
						FileReader is = new FileReader (fileEntry.getAbsoluteFile (), StandardCharsets.UTF_8);
						JSONTokener tokener = new JSONTokener (is);
						JSONObject jObjCountry = new JSONObject (tokener);
						this.setDivisionsCode (jObjCountry);

						jarr.put (jObjCountry);

					}
					catch (IOException e)
					{
						e.printStackTrace ();
					}

				}
			}
		}

		try (FileWriter file = new FileWriter (cfg.joinedFilePath + cfg.joinedFileName, StandardCharsets.UTF_8))
		{
			// We can write any JSONArray or JSONObject instance to the file
			file.write (jarr.toString (2));
			file.flush ();

		}
		catch (IOException e)
		{
			e.printStackTrace ();
		}

	}

	private void setDivisionsCode (JSONObject jObjCountry)
	{
		Integer ccn3 = jObjCountry.getInt ("ccn3");
		JSONArray jArrDivisions = jObjCountry.getJSONArray ("divisions");

		boolean areNumericCodes = this.areLocalCodeNumeric (jArrDivisions);

		for (Object o : jArrDivisions)
		{
			if (o instanceof JSONObject)
			{
				JSONObject division = (JSONObject) o;
				String code = division.getString ("code");
				Integer numericCode = (areNumericCodes)? CodeCreator.makeNumericConversion (ccn3, code)
				                                       : CodeCreator.makeConversion (ccn3, code);

				if (numericCode != null)
				{
					division.put ("uid", numericCode);
				}

			}
		}

	}

	private boolean areLocalCodeNumeric (JSONArray jArrDivisions)
	{
		for (Object o : jArrDivisions)
		{
			if (o instanceof JSONObject)
			{
				JSONObject division = (JSONObject) o;
				String code = division.getString ("code");

				String[] parts = code.split ("-");

				try
				{
					Integer.parseInt (parts[1]);
				}
				catch (NumberFormatException nfe)
				{
					return false;
				}
			}
		}
		return true;
	}
}
