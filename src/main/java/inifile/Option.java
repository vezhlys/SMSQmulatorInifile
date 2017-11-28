package inifile;

import java.io.Serializable;

/**
 * This is an options class: a class holding a name, a value and a description,
 * all as strings.
 *
 * @author and copyright (C) Wolfgang Lenerz 2011-2015
 */
@SuppressWarnings("serial")
public class Option implements Serializable {

	/**
	 * The name of the option. This is trimmed, so that there are no leading or
	 * trailing spaces. Option names cannot change.
	 */
	private final String optionName;

	/**
	 * The value of the option. May be null or empty.
	 */
	private String value;

	/**
	 * The description of the option. May be null or empty.
	 */
	private String description;

	/**
	 * This creates an option with only a name.
	 *
	 * @param optionName
	 *            the name of the option.
	 *
	 * @throws IllegalArgumentException
	 *             if the option name is null or empty.
	 */
	public Option(final String optionName) throws IllegalArgumentException {
		if (optionName == null || optionName.isEmpty()) {
			throw new IllegalArgumentException("Option name must not be null or empty");
		}
		this.optionName = optionName.trim();
	}

	/**
	 * This creates an option with a name and a value.
	 *
	 * @param optionName
	 *            the name of the option.
	 * @param value
	 *            the value it is to have.
	 *
	 * @throws IllegalArgumentException
	 *             if the option name is null or empty.
	 */
	public Option(final String optionName, final String value) throws IllegalArgumentException {
		this(optionName);
		this.value = value;
	}

	/**
	 * This creates an option with a name, a value, and a description.
	 *
	 * @param optionName
	 *            the name of the option.
	 * @param value
	 *            the value it is to have.
	 * @param description
	 *            a descriptive string for the option.
	 *
	 * @throws IllegalArgumentException
	 *             if the option name is null or empty.
	 */
	public Option(final String optionName, final String value, final String description)
			throws IllegalArgumentException {
		this(optionName);
		this.value = value;
		this.description = description;
	}

	/**
	 * Set or change the value of an option.
	 *
	 * @param value
	 *            the string to set as value.
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

	/**
	 * Set or change the description of an option.
	 *
	 * @param description
	 *            the string to set as description.
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Get the value.
	 *
	 * @return the value as a String, may be <code>null</code> if there is none, or
	 *         empty string.
	 */
	public final String getValue() {
		return this.value;
	}

	/**
	 * Get the description.
	 *
	 * @return the descriptive String, may be <code>null</code> if there is none, or
	 *         empty string.
	 */
	public final String getDescription() {
		return this.description;
	}

	/**
	 * Gets the name of the option.
	 *
	 * @return the name as a String. This will neither be <code>null</code> nor an
	 *         empty string.
	 */
	public final String getName() {
		return this.optionName;
	}
}
