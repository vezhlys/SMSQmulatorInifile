package inifile;

/**
 * The IniFile object contains all of the options a program may have, and which may be read from /written to 
 * a ".ini" file..
 * The options in the ini file must have the format <code>option_name = option_value</code>. Note that option names 
 * will be compared in a case independent manner so "option_name" is the same as "OPTION_NAME".<p>
 * 
 * An option NAME or VALUE must not contain the equal sign ("=").
 * <p>
 * To use this, first you create this object, which maintains an options map. Then you may or may not predefine all 
 * options the program may have with their default values and descriptions (if any).<p>
 * Then you read the ini file which changes the options according to the ones present in the ini file.<p>
 * When reading the ini file, you have a choice concerning options that have not been predefined before reading
 * the ini file: they may either be ignored, or be added to the options. The former makes sure that the IniFile object
 * only contains options which have been pre-defined by the programmer.<p>
 *<p>
 * Comments in the ini file are preceded with "#" and are ignored, except if a comment in an ini file immediately 
 * precedes an option: then it is taken to be the description of the option. "Immediately precedes" means that the 
 * comment is on the line immediately preceding the option, no other line (not even blank one) may be between them.
 * <p>
 * It is also possible to set a filename as default filename for reading/writing the ini file.
 * 
 * @author and copyright (C) Wolfgang Lenerz 2011-2015
 */

public class IniFile implements java.io.Serializable
{
    /**
     * The map that contains the options.
     */
    private final java.util.Map<String, Option> options = new java.util.LinkedHashMap<String, Option>();
   
    /**
    * Filename of a default file for saving / loading the options.
    */
    private String filename=null;

    /**
     * All of these values evaluate to "true".
     */
    private final static String []trueValues={"ja","sí","yes","oui","wahr","verdadero","true","vrai","1"};

    
    
    /**
     * Creates an empty IniFile object.
     */
    public IniFile()
    {
    }
    
    /**
     * This creates the IniFile object with one option.
     * 
     * @param optionName the name of the option. This, in lower case, will be the key in the map. It must not be null or empty.
     * @param value a value for the option.
     * @param description a description for the option, which will, when saved to a file, be written as a comment before the option.
     * 
     * @throws IllegalArgumentException if the option name is null or empty.
     */
    public IniFile(String optionName, String value, String description)
    {
        addOption (optionName, value,  description);
    }

    
    /**
     * This reads the default ini file and sets the options in the map.
     * This tries to read the default ini file, if the name thereof was given (if not, afile not found exception
     * will be raised).<p>
     * The options in the ini file must have the format option_name = option_value.<p>
     * This is equivalent to readIniFile (true).
     * 
     * @throws Exception any exception when reading the file or <code>FileNotFoundException</code> if file can't be found.
     */
    public final void readIniFile () throws Exception
    {
        if (this.filename==null  || this.filename.isEmpty())
            throw new java.io.FileNotFoundException();
        readIniFile (new java.io.File(this.filename),true);
    }
    
    /**
     * This reads the default ini file and sets the options in the map.
     * This tries to read the default ini file, if the name thereof was given (if not, a file not found exception
     * will be raised).<p>
     * The options in the ini file must have the format option_name = option_value.<p>
     * 
     * @param optionMustPreExist if <code>true</code>, an option read in from the file must already exist in the map 
     * to be taken into account. If <code>false</code>, an as yet non existing option will be created and added to the map.
  
     * @throws Exception any exception when reading the file or <code>FileNotFoundException</code> if file can't be found.
     */
    public final void readIniFile (boolean optionMustPreExist) throws Exception
    {
        if (this.filename==null  || this.filename.isEmpty())
            throw new java.io.FileNotFoundException();
        readIniFile (new java.io.File(this.filename),optionMustPreExist);
    }
    
    /**
     * This reads the ini file and sets the options in the map.
     * The options in the inifile must have the format option_name = option_value.<p>
     * An option must already exist in the map to be taken into account. This is equivalent to readIniFile (filename,true).
     * 
     * @param filename - the filename of the options file. If this can't be found, an attempt to find 
     * it in the user's home dir will be made.<p>
     * 
     * @throws Exception any exception when reading the file or FIleNotFoundException if file can't be found.
     */
    public void readIniFile (String filename) throws Exception
    {
        readIniFile (new java.io.File(filename),true);
    }
    
    /**
     * This reads the ini file and sets the options in the map.
     * The options in the inifile must have the format option_name = option_value.<p>
     * An option must already exist in the map to be taken into account.
     * 
     * @param filename - the filename of the options file. If this can't be found, an attempt to find 
     * it in the user's home dir will be made.
     * @param optionMustPreExist if <code>true</code>, an option read in from the file must already exist in the map 
     * to be taken into account. If <code>false</code>, an as yet non existing option will be created and added to the map.
  
     * @throws Exception any exception when reading the file or FIleNotFoundException if file can't be found.
     */
    public void readIniFile (String filename,boolean optionMustPreExist) throws Exception
    {
        readIniFile (new java.io.File(filename),optionMustPreExist);
    }

    /**
     * This reads the ini file and sets the options in the map.
     * The options in the inifile must have the format option_name = option_value.
     * An option must already exist in the map to be taken into account. This is equivalent to readIniFile (filename,true).
     * 
     * @param file the options file.If this can't be found, an attempt to find it in the user's home dir will be made.
     * 
     * @throws Exception any exception when reading the file or FIleNotFoundException if file can't be found.
     */
    public void readIniFile (java.io.File file) throws Exception
    {
        readIniFile (file,true);
    }
    
    /**
     * This reads the ini file and sets the options in the map.
     * The options in the ini file must have the format option_name = option_value.<p>
     * 
     * @param file the options file. If this can't be found, it will be tried to find it in the user's home dir.
     * @param optionMustPreExist if <code>true</code>, an option read in from the file must already exist in the map 
     * to be taken into account. If <code>false</code>, an as yet non existing option will be created and added to the map.
     * 
     * @throws Exception any exception when reading the file or FIleNotFoundException if file can't be found.
     */
    public void readIniFile (java.io.File file, boolean optionMustPreExist) throws Exception
    {  
        if (!file.exists())                                 // if the file as such doesn't exist try it with that name in the user's home dir
        {
            String mfilename=file.getAbsolutePath();
            mfilename=System.getProperty( "user.home" )+java.io.File.separator+mfilename;
            file=new java.io.File(mfilename);
            if (!file.exists())
                throw new java.io.FileNotFoundException();
        }
        this.filename=file.getAbsolutePath();
        String temp;
        String []opts;
        String descr=null;
        Option opt;
        java.io.BufferedReader readfiles=null;
        try
        {
            readfiles = new java.io.BufferedReader (new java.io.FileReader (file));
            while ((temp = readfiles.readLine()) != null)   // simply read all data
            {
                temp=temp.replaceAll("\t", " ").trim();
                if (temp.equals(""))
                {
                    descr=null;
                    continue;                               // blank line or comment, do nothing
                }            
                if (temp.startsWith("#"))                   // line is a comment...
                {
                    descr=temp.substring(1).trim();
                    continue;                               // ... and could be a description
                }
                opts=temp.split("=", 2);                    // separate name and value
                if (opts==null || opts.length!=2 || opts[1].contains("="))
                    continue;                               // this wasn't a valid name value pair!
      //          String optname=opts[0].trim();
                opts[0]=opts[0].trim().toLowerCase();       // no superfluous spaces and lower case
                if (opts[0].equals("wdw_xsize"))
                    opts=opts;
                opts[1]=opts[1].trim();
                opt = this.options.get(opts[0]);            // is there an option like that?
                if (opt!=null)                              // yes
                {
                    opt.setOptionValue(opts[1]);            // set the option now
                    if (descr!=null)
                        opt.setDescription(descr);          // only change description if a new one exists      
                }
                else if(!optionMustPreExist)                // maybe add new option
                {
                    addOption(opts[0],opts[1],descr);
                }
                descr=null;                                 // no more description for next option
            }
            readfiles.close();
        }
        catch (Exception e)
        {
            try
            {
                if (readfiles!=null)
                    readfiles.close();
            }
            catch (Exception p)
            {/*nop*/}
            throw new Exception (e);
        }
    }

    /**
     * This tries to write an ini file to the file set as default filename.
     * If no such filename exists, nothing happens, no error or exception is generated.
     * Any other eror is also silently ignored.
     * The options in the inifile must have the format option_name = option_value.<p>
     */
    public void writeIniFileNoError()
    {
        if (this.filename== null || this.filename.isEmpty())
            return;
        try
        {
            writeIniFile (new java.io.File(this.filename));
        }
        catch (Exception e)
        {/*nop*/}
    }
    
    /**
     * This tries to write an ini file to the default file, if any
     * .
     * If not such file exists, it throws an exception.
     * The options in the ini file will have the format option_name = option_value.<p>
     * 
     * @throws Exception java.io.FileNotFoundException if no previous ini file existed , any exception from writing to the file
     */
    public void writeIniFile()throws Exception
    {
        if (this.filename== null)
            throw new java.io.FileNotFoundException();
        writeIniFile (new java.io.File(this.filename));
    }
    
    /**
     * This writes the ini file. Any existing options file of the same name will be overwritten.
     * 
     * @param filename - the filename of the options file.
     * The options in the inifile must have the format option_name = option_value.<p>
     * 
     * @throws Exception - any IO exception from file operations.
     */
    public void writeIniFile(String filename) throws Exception
    {
        writeIniFile (new java.io.File(filename));
    }

    /**
     * This writes all options out to an ini file.
     * 
     * The options in the inifile must have the format option_name = option_value.<p>
     * @param file - the file to write to.
     * 
     * @throws Exception - any IO exception from file operations.
     */
    public void writeIniFile(java.io.File file) throws Exception
    {
        java.io.PrintStream sout=null;
        try
        {
            sout=new java.io.PrintStream(new java.io.FileOutputStream (file));
            for (Option option :this.options.values())
            {
                sout.println("# "+option.getDescription()); // write the comment first
                sout.println(option.getName()+" = "+option.getValue());//then name = value pair
                sout.println();
            }
            sout.close();
        }
        catch (java.io.IOException e)
        {
            try
            {
                if (sout!=null)
                    sout.close();
            }
            catch (Exception p)
            {/*nop*/}
            throw new Exception (e);
        }
    }
    
    /**
     * This adds an option to the map. If this option already exists, it is overwritten with the new values.
     * 
     * @param optionName the name of the option. This, in lower case, will the the key in the map. It must not be null or empty.
     * @param value the value for the option.
     * @param description a description for the option, will, when saved to a file, be written as a comment before the option.
     * 
     * @throws IllegalArgumentException if the option name is null or empty.
     */
    public final void addOption(String optionName, String value, String description)
    {
        if (optionName==null || optionName.isEmpty())
            throw new IllegalArgumentException("Option name  must not be null or empty");
        optionName=optionName.trim();
        Option op = new Option(optionName, value, description); // make a new option
        this.options.put(optionName.toLowerCase(), op);         // and add it to, or replace it it, the map
    }

    
    /**
     * This changes an existing option by setting new values for the value and the description of the existing option..
     * If the option doesn't exists, nothing is changed and no error is raised. For options that don't exist, use addOption.
     *
     * @param option the option to set and containing the new values.
     */
    public void changeOption(Option option)
    {
        if (option==null)
            return;
        String optionName=option.getName().toLowerCase();
        if (this.options.get(optionName)!=null)
            this.options.put(optionName, option);
    }

    
    /**
     * Set a new value in an existing option.
     * 
     * @param optionName the name of the option. Name comparison is made on a lowercases basis.
     * If the option doesn't exists, nothing is changed and no error is raised.
     * @param value the value of the option.
     */
    public void setOptionValue(String optionName,String value)
    {
        if (optionName==null || optionName.isEmpty())
            return;
        Option option=this.options.get(optionName.trim().toLowerCase());
        if (option!=null)
            option.setOptionValue(value);
    }
    
    /**
     * Set a new description in an existing option.
     * 
     * @param optionName the name of the option. Name comparison is made on a lowercases basis.
     * If the option doesn't exists, nothing is changed and no error is raised.
     * @param description the description of the option.
     */
    public void setOptionDescription(String optionName,String description)
    {
        if (optionName==null || optionName.isEmpty())
            return;
        Option option=this.options.get(optionName.trim().toLowerCase());
        if (option!=null)
            option.setOptionValue(description);
    }
    
    /**
     * Check whether an option exists.
     * 
     * @param optionName the name of the option to check.
     * 
     * @return true if this option exists, else false.
     */
    public boolean checkOptionExists(String optionName)
    {
        if (optionName==null || optionName.isEmpty())
            return false;
        return this.options.get(optionName.trim().toLowerCase())!=null;
    }
    
    /**
     * This returns the value of an option.
     * 
     * @param optionName the name of the option. Name comparison is made on a lowercases basis.
     * @return the option value as a string. May be <code>null</code> or an empty String. 
     * If the option doesn't exist <code>null</code> is returned.
     */
    public String getOptionValue(String optionName)
    {
        if (optionName==null || optionName.isEmpty())
            return null;
        Option option=this.options.get(optionName.trim().toLowerCase());
        return option==null?null : option.getValue();
    }
    
    /**
     * This returns the value of an option, trimmed to remove leading or trailing spaces.
     * 
     * @param optionName the name of the option. Name comparison is made on a lowercases basis.
     * @return the option value as a trimmed string. May be <code>null</code> or an empty String.
     */
    public String getTrimmedOptionValue(String optionName)
    {
        Option option=this.options.get(optionName.trim().toLowerCase());
        return option==null?null : option.getValue().trim();
    }
    
    /**
     * This returns the trimmed lower cased value of an option.
     * 
     * @param optionName - the name of the option. Name comparison is made on a lowercases basis.
     * @return the option value as a string. May be <code>null</code> or an empty String.
     */
    public String getLowerCasedOptionValue(String optionName)
    {
        Option option=this.options.get(optionName.trim().toLowerCase());
        return option==null?null : option.getValue().trim().toLowerCase();
    }
    
    /**
     * This tries to return an option value as an int.
     * If the value can't be converted to an int, the defaultValue passed as parameter is returned.
     * 
     * @param optionName - the name of the option. Name comparison is made on a lowercases basis.
     * @param defaultValue the default value that will be returned if the options value can't be converted to an int,  or the option doesn't exist.
     * 
     * @return the int of the option or that of the default value.
     */
    public int getOptionAsInt(String optionName,int defaultValue)
    {
        Option option=this.options.get(optionName.trim().toLowerCase());
        if (option==null)
            return defaultValue;
        String value=option.getValue().trim();
        try
        {
            int retval=Integer.parseInt(value);
            return retval;
        }
        catch (Exception e)
        {
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
     * @param optionName - the name of the option.
     * 
     * @return <code>true</code> if the option value is one of the above, else <code>false</code>.
     */
    public boolean getTrueOrFalse(String optionName)
    {
        if (optionName==null|| optionName.isEmpty())
            return false;
        Option option=this.options.get(optionName.toLowerCase());
        if (option==null)
            return false;
        String value=option.getValue();
        if (value==null)
            return false;
        value=value.toLowerCase();
        for (String s:IniFile.trueValues)
        {
            if (value.equals(s))
            {    
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get the option.
     * 
     * @param optionName the name of the option.
     * 
     * @return the option, or null if it doesn't exists.
     */
    public Option getOption(String optionName)
    {
        return this.options.get(optionName.trim().toLowerCase());
    }
       
    /**
     * This parses a command line where arguments are preceeded and separated by - or -- and sets the appropriate options, if any.
     * Each command line argument must be in the format : -name=value or --name=value with no spaces between
     * the name and the "=" and the "=" and the value.
     * 
     * @param args the command line arguments.
     */
    public void parseCommandLine(String [] args)
    {
        String[]opts;
        for (String cla:args)
        {
            cla=cla.trim();
            if (cla.isEmpty() ||(!cla.startsWith("-")))
                continue;
            cla=cla.startsWith("--")?cla.substring(2):cla.substring(1);
            opts=cla.split("=", 2);                         // separate name and value
            if (opts.length!=2)
                continue;                                   // this wasn't a valid name value pair!
          
            Option opt = options.get(opts[0].trim().toLowerCase());// is there an option like that?
            if (opt!=null)
                opt.setOptionValue(opts[1]);                     // set the option now
        }
    }

    /**
     * Set the default filename for reading/writing the ".ini" file.
     * 
     * @param filename the filename for the ".ini" file.
     */
    public void setFilename(String filename)
    {
        this.filename=filename;
    }
    
    /**
     * Get the default filename for reading/writing the ".ini" file.
     * 
     * @return the filename for the ".ini" file.
     */
    public String getFilename()
    {
        return this.filename;
    }
}
