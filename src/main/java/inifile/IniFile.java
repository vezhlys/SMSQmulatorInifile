package inifile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The IniFile object contains all of the options a program may have, and which
 * may be read from /written to a ".ini" file.. The options in the ini file must
 * have the format <code>option_name = option_value</code>. Note that option
 * names will be compared in a case independent manner so "option_name" is the
 * same as "OPTION_NAME".
 * <p>
 *
 * An option NAME or VALUE must not contain the equal sign ("=").
 * <p>
 * To use this, first you create this object, which maintains an options map.
 * Then you may or may not predefine all options the program may have with their
 * default values and descriptions (if any).
 * <p>
 * Then you read the ini file which changes the options according to the ones
 * present in the ini file.
 * <p>
 * When reading the ini file, you have a choice concerning options that have not
 * been predefined before reading the ini file: they may either be ignored, or
 * be added to the options. The former makes sure that the IniFile object only
 * contains options which have been pre-defined by the programmer.
 * <p>
 * <p>
 * Comments in the ini file are preceded with "#" and are ignored, except if a
 * comment in an ini file immediately precedes an option: then it is taken to be
 * the description of the option. "Immediately precedes" means that the comment
 * is on the line immediately preceding the option, no other line (not even
 * blank one) may be between them.
 * <p>
 * It is also possible to set a filename as default filename for reading/writing
 * the ini file.
 *
 * @author and copyright (C) Wolfgang Lenerz 2011-2015
 */
@SuppressWarnings("serial")
public class IniFile implements Serializable {

	/**
	 * The map that contains the options.
	 */
	private final Map<String, Option> options = new LinkedHashMap<>();

	/**
	 * Filename of a default file for saving / loading the options.
	 */
	private String defaultFilename;

	/**
	 * All of these values evaluate to "true".
	 */
	private static final List<String> TRUE_VALUES = Arrays.asList("ja", "sí", "yes", "oui", "wahr", "verdadero",
			"true", "vrai", "1");

	/**
	 * Creates an empty IniFile object.
	 */
	public IniFile() {
	}

	/**
	 * This creates the IniFile object with one option.
	 *
	 * @param optionName
	 *            the name of the option. This, in lower case, will be the key in
	 *            the map. It must not be null or empty.
	 * @param value
	 *            a value for the option.
	 * @param description
	 *            a description for the option, which will, when saved to a file, be
	 *            written as a comment before the option.
	 *
	 * @throws IllegalArgumentException
	 *             if the option name is null or empty.
	 */
	public IniFile(final String optionName, final String value, final String description) {
		addOption(optionName, value, description);
	}

	/**
	 * This reads the default ini file and sets the options in the map. This tries
	 * to read the default ini file, if the name thereof was given (if not, afile
	 * not found exception will be raised).
	 * <p>
	 * The options in the ini file must have the format option_name = option_value.
	 * <p>
	 * This is equivalent to readIniFile (true).
	 *
	 * @throws IOException
	 *             any exception when reading the file or
	 *             <code>FileNotFoundException</code> if file can't be found.
	 */
	public final void readIniFile() throws IOException {
		readIniFile(true);
	}

	/**
	 * This reads the default ini file and sets the options in the map. This tries
	 * to read the default ini file, if the name thereof was given (if not, a file
	 * not found exception will be raised).
	 * <p>
	 * The options in the ini file must have the format option_name = option_value.
	 * <p>
	 *
	 * @param optionMustPreExist
	 *            if <code>true</code>, an option read in from the file must already
	 *            exist in the map to be taken into account. If <code>false</code>,
	 *            an as yet non existing option will be created and added to the
	 *            map.
	 *
	 * @throws IOException
	 *             any exception when reading the file or
	 *             <code>FileNotFoundException</code> if file can't be found.
	 */
	public final void readIniFile(final boolean optionMustPreExist) throws IOException {
		readIniFile(defaultFilename, optionMustPreExist);
	}

	/**
	 * This reads the ini file and sets the options in the map. The options in the
	 * inifile must have the format option_name = option_value.
	 * <p>
	 * An option must already exist in the map to be taken into account. This is
	 * equivalent to readIniFile (filename,true).
	 *
	 * @param filename
	 *            - the filename of the options file. If this can't be found, an
	 *            attempt to find it in the user's home dir will be made.
	 *            <p>
	 *
	 * @throws IOException
	 *             any IOException when reading the file or FileNotFoundException if
	 *             file can't be found.
	 */
	public final void readIniFile(final String filename) throws IOException {
		readIniFile(filename, true);
	}

	/**
	 * This reads the ini file and sets the options in the map. The options in the
	 * inifile must have the format option_name = option_value.
	 * <p>
	 * An option must already exist in the map to be taken into account.
	 *
	 * @param filename
	 *            - the filename of the options file. If this can't be found, an
	 *            attempt to find it in the user's home dir will be made.
	 * @param optionMustPreExist
	 *            if <code>true</code>, an option read in from the file must already
	 *            exist in the map to be taken into account. If <code>false</code>,
	 *            an as yet non existing option will be created and added to the
	 *            map.
	 *
	 * @throws IOException
	 *             any IOException when reading the file or FileNotFoundException if
	 *             file can't be found.
	 */
	public final void readIniFile(final String filename, final boolean optionMustPreExist) throws IOException {
		readIniFile(new File(filename), optionMustPreExist);
	}

	/**
	 * This reads the ini file and sets the options in the map. The options in the
	 * inifile must have the format option_name = option_value. An option must
	 * already exist in the map to be taken into account. This is equivalent to
	 * readIniFile (filename,true).
	 *
	 * @param file
	 *            the options file.If this can't be found, an attempt to find it in
	 *            the user's home dir will be made.
	 *
	 * @throws IOException
	 *             any exception when reading the file or FileNotFoundException if
	 *             file can't be found.
	 */
	public final void readIniFile(final File file) throws IOException {
		readIniFile(file, true);
	}

	/**
	 * This reads the ini file and sets the options in the map. The options in the
	 * ini file must have the format option_name = option_value.
	 * <p>
	 *
	 * @param file
	 *            the options file. If this can't be found, it will be tried to find
	 *            it in the user's home dir.
	 * @param optionMustPreExist
	 *            if <code>true</code>, an option read in from the file must already
	 *            exist in the map to be taken into account. If <code>false</code>,
	 *            an as yet non existing option will be created and added to the
	 *            map.
	 *
	 * @throws IOException
	 *             any exception when reading the file or FileNotFoundException if
	 *             file can't be found.
	 */
	public final void readIniFile(final File file, final boolean optionMustPreExist) throws IOException {
		// if the file as such doesn't exist try it with that name in the user's home
		// dir
		File rdFile = file;
		if (!rdFile.exists()) {
			String mfilename = rdFile.getAbsolutePath();
			mfilename = System.getProperty("user.home") + File.separator + mfilename;
			rdFile = new File(mfilename);
			if (!rdFile.exists()) {
				throw new FileNotFoundException();
			}
		}
		defaultFilename = rdFile.getAbsolutePath();
		try (BufferedReader readfiles = new BufferedReader(new FileReader(rdFile))) {
			String temp;
			String[] opts;
			String descr = null;
			// simply read all data
			while ((temp = readfiles.readLine()) != null) {
				temp = temp.replaceAll("\t", " ").trim();
				if (temp.equals("")) {
					descr = null;
					continue; // blank line or comment, do nothing
				}
				// line is a comment...
				if (temp.startsWith("#")) {
					descr = temp.substring(1).trim();
					continue; // ... and could be a description
				}
				opts = temp.split("=", 2); // separate name and value
				if (opts == null || opts.length != 2 || opts[1].contains("=")) {
					continue; // this wasn't a valid name value pair!
				}
				String optName = opts[0];
				String optValue = opts[1].trim();
				Option opt = getOption(optName); // is there an option like that?
				if (opt != null) {
					descr = (descr != null) ? descr : opt.getDescription();
					changeOption(new Option(optName, optValue, descr));
				} else if (!optionMustPreExist) {
					// add new option if option doesn't need to pre-exist
					addOption(optName, optValue, descr);
				}
				descr = null; // no more description for next option
			}
		}
	}

	/**
	 * This tries to write an ini file to the file set as default filename. If no
	 * such filename exists, nothing happens, no error or exception is generated.
	 * Any other eror is also silently ignored. The options in the inifile must have
	 * the format option_name = option_value.
	 * <p>
	 */
	public final void writeIniFileNoError() {
		try {
			if (defaultFilename != null && !defaultFilename.isEmpty()) {
				writeIniFile(new File(defaultFilename));
			}
		} catch (IOException e) {
			/* nop */
		}
	}

	/**
	 * This tries to write an ini file to the default file, if any . If not such
	 * file exists, it throws an exception. The options in the ini file will have
	 * the format option_name = option_value.
	 * <p>
	 *
	 * @throws FileNotFoundException
	 *             FileNotFoundException if no previous ini file existed , any
	 *             exception from writing to the file
	 * @throws IOException
	 *             IOException on file operation failures
	 */
	public final void writeIniFile() throws FileNotFoundException, IOException {
		if (defaultFilename == null) {
			throw new FileNotFoundException();
		}
		writeIniFile(new File(defaultFilename));
	}

	/**
	 * This writes the ini file. Any existing options file of the same name will be
	 * overwritten.
	 *
	 * @param filename
	 *            - the filename of the options file. The options in the inifile
	 *            must have the format option_name = option_value.
	 *            <p>
	 *
	 * @throws IOException
	 *             - any IO exception from file operations.
	 */
	public final void writeIniFile(final String filename) throws IOException {
		writeIniFile(new File(filename));
	}

	/**
	 * This writes all options out to an ini file.
	 *
	 * The options in the inifile must have the format option_name = option_value.
	 * <p>
	 *
	 * @param file
	 *            - the file to write to.
	 *
	 * @throws IOException
	 *             - any IO exception from file operations.
	 */
	public final void writeIniFile(final File file) throws IOException {
		try (PrintStream sout = new PrintStream(new FileOutputStream(file))) {
			for (Option option : options.values()) {
				sout.println("# " + option.getDescription()); // write the comment first
				sout.println(option.getName() + " = " + option.getValue()); // then name = value pair
				sout.println();
			}
		}
	}

	/**
	 * This adds an option to the map. If this option already exists, it is
	 * overwritten with the new values.
	 *
	 * @param optionName
	 *            the name of the option. This, in lower case, will the the key in
	 *            the map. It must not be null or empty.
	 * @param value
	 *            the value for the option.
	 * @param description
	 *            a description for the option, will, when saved to a file, be
	 *            written as a comment before the option.
	 *
	 * @throws IllegalArgumentException
	 *             if the option name is null or empty.
	 */
	public final void addOption(final String optionName, final String value, final String description) {
		if (optionName == null || optionName.isEmpty()) {
			throw new IllegalArgumentException("Option name  must not be null or empty");
		}
		addOption(new Option(optionName.trim(), value, description));
	}

	/**
	 * This adds an option to the map. If this option already exists, it is
	 * overwritten with the passed option value.
	 *
	 * @param option
	 *            Option to add
	 *
	 * @throws IllegalArgumentException
	 *             if the option is null.
	 */
	public final void addOption(final Option option) {
		if (option == null) {
			throw new IllegalArgumentException("Option must not be null");
		}
		options.put(option.getName().toLowerCase(), option); // and add it to, or replace it it, the map
	}

	/**
	 * This changes an existing option by setting new values for the value and the
	 * description of the existing option.. If the option doesn't exists, nothing is
	 * changed and no error is raised. For options that don't exist, use addOption.
	 *
	 * @param option
	 *            the option to set and containing the new values.
	 */
	public final void changeOption(final Option option) {
		if (option != null && checkOptionExists(option.getName())) {
			addOption(option);
		}
	}

	/**
	 * Set a new value in an existing option.
	 *
	 * @param optionName
	 *            the name of the option. Name comparison is made on a lowercases
	 *            basis. If the option doesn't exists, nothing is changed and no
	 *            error is raised.
	 * @param value
	 *            the value of the option.
	 */
	public final void setOptionValue(final String optionName, final String value) {
		Option option = getOption(optionName);
		if (option != null) {
			option.setValue(value);
		}
	}

	/**
	 * Set a new description in an existing option.
	 *
	 * @param optionName
	 *            the name of the option. Name comparison is made on a lowercases
	 *            basis. If the option doesn't exists, nothing is changed and no
	 *            error is raised.
	 * @param description
	 *            the description of the option.
	 */
	public final void setOptionDescription(final String optionName, final String description) {
		Option option = getOption(optionName);
		if (option != null) {
			option.setDescription(description);
		}
	}

	/**
	 * Check whether an option exists.
	 *
	 * @param optionName
	 *            the name of the option to check.
	 *
	 * @return true if this option exists, else false.
	 */
	public final boolean checkOptionExists(final String optionName) {
		return getOption(optionName) != null;
	}

	/**
	 * This returns the value of an option.
	 *
	 * @param optionName
	 *            the name of the option. Name comparison is made on a lowercases
	 *            basis.
	 * @return the option value as a string. May be <code>null</code> or an empty
	 *         String. If the option doesn't exist <code>null</code> is returned.
	 */
	public final String getOptionValue(final String optionName) {
		Option option = getOption(optionName);
		return option == null ? null : option.getValue();
	}

	/**
	 * This returns the value of an option, trimmed to remove leading or trailing
	 * spaces.
	 *
	 * @param optionName
	 *            the name of the option. Name comparison is made on a lowercases
	 *            basis.
	 * @return the option value as a trimmed string. May be <code>null</code> or an
	 *         empty String.
	 */
	public final String getTrimmedOptionValue(final String optionName) {
		String value = getOptionValue(optionName);
		return value == null ? null : value.trim();
	}

	/**
	 * This returns the trimmed lower cased value of an option.
	 *
	 * @param optionName
	 *            - the name of the option. Name comparison is made on a lowercases
	 *            basis.
	 * @return the option value as a string. May be <code>null</code> or an empty
	 *         String.
	 */
	public final String getLowerCasedOptionValue(final String optionName) {
		String value = getTrimmedOptionValue(optionName);
		return value == null ? null : value.toLowerCase();
	}

	/**
	 * This tries to return an option value as an int. If the value can't be
	 * converted to an int, the defaultValue passed as parameter is returned.
	 *
	 * @param optionName
	 *            - the name of the option. Name comparison is made on a lowercases
	 *            basis.
	 * @param defaultValue
	 *            the default value that will be returned if the options value can't
	 *            be converted to an int, or the option doesn't exist.
	 *
	 * @return the int of the option or that of the default value.
	 */
	public final int getOptionAsInt(final String optionName, final int defaultValue) {
		String value = getTrimmedOptionValue(optionName);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * This returns a boolean : if the option value is one of :
	 * <ul>
	 * <li>yes</li>
	 * <li>ja</li>
	 * <li>oui</li>
	 * <li>sí</li>
	 * <li>true</li>
	 * <li>vrai</li>
	 * <li>wahr</li>
	 * <li>verdadero</li>
	 * <li>1</li>
	 * </ul>
	 * then this returns <code>true</code>, else it returns <code>false</code>.
	 *
	 * @param optionName
	 *            - the name of the option.
	 *
	 * @return <code>true</code> if the option value is one of the above, else
	 *         <code>false</code>.
	 */
	public final boolean getTrueOrFalse(final String optionName) {
		String value = getOptionValue(optionName);
		if (value != null) {
			for (String s : IniFile.TRUE_VALUES) {
				if (s.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the option.
	 *
	 * @param optionName
	 *            the name of the option.
	 *
	 * @return the option, or null if it doesn't exists.
	 */
	public final Option getOption(final String optionName) {
		if (optionName == null || optionName.isEmpty()) {
			return null;
		}
		return options.get(optionName.trim().toLowerCase());
	}

	/**
	 * This parses a command line where arguments are preceded and separated by - or
	 * -- and sets the appropriate options, if any. Each command line argument must
	 * be in the format : -name=value or --name=value with no spaces between the
	 * name and the "=" and the "=" and the value.
	 *
	 * @param args
	 *            the command line arguments.
	 */
	public final void parseCommandLine(final String[] args) {
		String[] opts;
		for (String cla : args) {
			cla = cla.trim();
			if (cla.startsWith("-")) {
				cla = cla.startsWith("--") ? cla.substring(2) : cla.substring(1);
				opts = cla.split("=", 2); // separate name and value
				// ignore if not valid pair
				if (opts.length == 2) {
					changeOption(getOption(opts[0]));
				}
			}
		}
	}

	/**
	 * Set the default filename for reading/writing the ".ini" file.
	 *
	 * @param filename
	 *            the filename for the ".ini" file.
	 */
	public final void setFilename(final String filename) {
		this.defaultFilename = filename;
	}

	/**
	 * Get the default filename for reading/writing the ".ini" file.
	 *
	 * @return the filename for the ".ini" file.
	 */
	public final String getFilename() {
		return defaultFilename;
	}
}
