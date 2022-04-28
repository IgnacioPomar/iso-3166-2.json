
package com.septeto.iso3166.joiner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Properties;


public class UtilISO_3166_2_CFG
{

	public String joinedFilePath;
	public String joinedFileName;
	public String splittedFilePath;
	public String iso6166_1_FilePath;

	private void getPropValues (Properties prop)
	{
		this.joinedFilePath = prop.getProperty ("joinedFilePath");
		this.joinedFileName = prop.getProperty ("joinedFileName");
		this.splittedFilePath = prop.getProperty ("splittedFilePath");
		this.iso6166_1_FilePath = prop.getProperty ("iso6166_1_FilePath");

	}

	// ----------------------------------------------------------
	private static final String	DEFUALT_CFG_FILE_NAME = "config.properties";

	private String				jarPath;

	public String getCfgFileName ()
	{
		return DEFUALT_CFG_FILE_NAME;
	}

	/**
	 * @param args
	 *            May be in the future parse the args to override the properties values
	 */
	public void loadCfg (String[] args)
	{
		Properties prop = new Properties ();
		this.loadFromFile (prop, getCfgFileName ());
		this.getPropValues (prop);
	}

	public UtilISO_3166_2_CFG ()
	{

		boolean isInsideDebugger = java.lang.management.ManagementFactory.getRuntimeMXBean ().getInputArguments ()
		        .toString ().indexOf ("-agentlib:jdwp") > 0;

		boolean isJarFile = this.setJarPath ();

		if (isInsideDebugger && !isJarFile)
		{
			// Significa que estamso en el eclipse y probablemente en la carpeta bin
			this.setDebugBinFolder ();
		}

	}

	/**
	 * Subimos dos niveles y cambiamos a carpeta de deppuracuión
	 */
	private void setDebugBinFolder ()
	{
		File jarFile = new File (jarPath);
		jarPath = jarFile.getParentFile ().getParentFile ().getPath ();

		// Incorporamos el directorio de depuración
		jarPath = jarPath + File.separator + "dbgBin" + File.separator;

		this.makeSureFolderExists (jarPath);

		// TODO: en pruebas. Considerar usar JNI https://stackoverflow.com/a/59397607/74785
		File directory = new File (jarPath).getAbsoluteFile ();
		System.setProperty ("user.dir", directory.getAbsolutePath ());

	}

	/**
	 * Carga lso parámetros que haya en el fichero .properties
	 * 
	 * @param prop
	 */
	protected void loadFromFile (Properties prop, String cfgFileName)
	{
		// Cargamos la confirguración desde disco (Si existe)
		try
		{
			InputStream is = new FileInputStream (getCfgFilepath (cfgFileName));
			prop.load (is);

		}
		catch (java.io.FileNotFoundException e)
		{
			// savePropFile (prop, cfgFileName);
		}
		catch (IOException e)
		{
			System.out.println (e.toString ());
		}

	}

	public String getJarPath ()
	{
		return this.jarPath;
	}

	private boolean setJarPath ()
	{
		boolean isJarFile = false;

		try
		{
			CodeSource codeSource = this.getClass ().getProtectionDomain ().getCodeSource ();
			this.jarPath = codeSource.getLocation ().toURI ().getPath ();

			// IN dev enviroment the jar wont exists, so the path is different
			if (jarPath.endsWith (".jar"))
			{
				File jarFile = new File (jarPath);
				jarPath = jarFile.getParentFile ().getPath ();
				isJarFile = true;
			}

			if (!(jarPath.endsWith ("\\") || jarPath.endsWith ("/")))
			{
				jarPath = jarPath + File.separator;
			}

			// Removving Windows shit
			String os = System.getProperty ("os.name").toLowerCase ();
			if (os.indexOf ("win") >= 0)
			{
				jarPath = jarPath.replace ("\\", "/");
				if (jarPath.charAt (0) == '/' && jarPath.charAt (2) == ':')
				{
					jarPath = jarPath.substring (1);
				}

			}

		}
		catch (URISyntaxException e)
		{
			e.printStackTrace ();
			jarPath = "." + File.separator;
		}
		return isJarFile;

	}

	private void makeSureFolderExists (String folderName)
	{
		File folder = new File (folderName);
		if (!folder.exists ())
		{
			folder.mkdir ();
			// YAGNI: folder.mkdirs(); //How many dirs we want to create
		}
	}

	public String getPrefixedFileName (String logFileName)
	{
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat ("yyyyMMdd");
		java.util.Date date = new java.util.Date ();

		return dateFormat.format (date) + "_" + logFileName;
	}

	public String getCfgFilepath (String cfgFileName)
	{
		String folderName = getJarPath () + "cfg/";
		this.makeSureFolderExists (folderName);

		return folderName + cfgFileName;
	}

}
