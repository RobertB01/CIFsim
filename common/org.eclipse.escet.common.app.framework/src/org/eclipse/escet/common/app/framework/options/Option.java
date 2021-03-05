//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.common.app.framework.options;

import java.util.Comparator;

import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.swt.widgets.Composite;

/**
 * Base class for all options.
 *
 * <p>
 * For each option, singleton instances are automatically created by the {@link Options#getInstance} method. No other
 * instances should be created. The {@link Options#getInstance} method requires a constructor without arguments.
 * </p>
 *
 * @param <T> The type of the data value for the option.
 */
public abstract class Option<T> {
    /**
     * The name of the option. This may be displayed in help messages, and user interfaces. Example:
     * {@code "Test mode"}.
     */
    protected final String name;

    /**
     * The description of the option. This may be displayed in help messages, and user interfaces. Example:
     * {@code "Whether to enable test mode
     * (BOOL=yes), or disable it (BOOL=no). Test modes disables visual
     * interfaces etc etc. [DEFAULT=no]"}.
     */
    protected final String description;

    /**
     * The short (one letter) option name, for command line processing. May be {@code null} if the option has no short
     * name.
     */
    protected final Character cmdShort;

    /**
     * The long option name (excluding the "--" prefix), for command line processing. Must not be {@code null}. May be
     * {@code "*"} to indicate that it should capture the remaining arguments.
     */
    protected final String cmdLong;

    /**
     * The name of the option value, for command line processing. May be {@code null} if the option doesn't require a
     * value.
     */
    protected final String cmdValue;

    /** Whether to show the option in option dialogs. */
    protected final boolean showInDialog;

    /**
     * Constructor for the {@link Option} class.
     *
     * @param name The name of the option.
     * @param description The description of the option.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. May be {@code "*"} to indicate that it should capture the remaining arguments.
     * @param cmdValue The name of the option value, for command line processing. May be {@code null} if the option
     *     doesn't require a value.
     * @param showInDialog Whether to show the option in option dialogs.
     */
    public Option(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            boolean showInDialog)
    {
        Assert.notNull(cmdLong);
        Assert.check(!(cmdLong.equals("*") && (cmdShort != null)));
        Assert.check(!cmdLong.startsWith("-"));
        this.name = name;
        this.description = description;
        this.cmdShort = cmdShort;
        this.cmdLong = cmdLong;
        this.cmdValue = cmdValue;
        this.showInDialog = showInDialog;
    }

    /**
     * Returns The name of the option. This may be displayed in help messages, and user interfaces.
     *
     * @return The name of the option. This may be displayed in help messages, and user interfaces.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the option. This may be displayed in help messages, and user interfaces.
     *
     * @return The description of the option. This may be displayed in help messages, and user interfaces.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the short (one letter) option name, for command line processing. May be {@code null} if the option has no
     * short name.
     *
     * @return The short (one letter) option name, for command line processing. May be {@code null} if the option has no
     *     short name.
     */
    public Character getCmdShort() {
        return cmdShort;
    }

    /**
     * Returns the long option name (excluding the "--" prefix), for command line processing. Must not be {@code null}.
     * May be {@code "*"} to indicate that it should capture the remaining arguments.
     *
     * @return The long option name (excluding the "--" prefix), for command line processing. Must not be {@code null}.
     *     May be {@code "*"} to indicate that it should capture the remaining arguments.
     */
    public String getCmdLong() {
        return cmdLong;
    }

    /**
     * Returns the name of the option value, for command line processing. May be {@code null} if the option doesn't
     * require a value.
     *
     * @return The name of the option value, for command line processing. May be {@code null} if the option doesn't
     *     require a value.
     */
    public String getCmdValue() {
        return cmdValue;
    }

    /**
     * Returns the value associated with this option.
     *
     * @return The value associated with this option.
     */
    public T get() {
        return Options.get(this);
    }

    /**
     * Returns the default value for this option.
     *
     * @return The default value for this option.
     */
    public abstract T getDefault();

    /**
     * Parses a textual value for this option.
     *
     * <p>
     * {@link ApplicationException} thrown by this method are automatically caught, and are assumed to indicate an
     * invalid value for the option. The message of the exception should indicate why the value is invalid. The option
     * framework will wrap it to indicate which option had an invalid value. Preferably, use the {@link #checkValue}
     * method to perform validation of the option value.
     * </p>
     *
     * <p>
     * If the option does not have a value ({@link #cmdValue} equals {@code null}), then this method is called with
     * {@code null} for the value, if the option is provided on the command line.
     * </p>
     *
     * @param optName The option name, as actually encountered. This may be the short form (if applicable), or the long
     *     form.
     * @param value The textual value to parse.
     * @return The parsed value, or {@code null}.
     * @see Options#parseOptionValue
     */
    public abstract T parseValue(String optName, String value);

    /**
     * Checks an option value for validity, based on the given condition value. This method is typically used in the
     * {@link #parseValue} and {@link #verifyValue} methods.
     *
     * @param condition The condition that must hold.
     * @param message The error message in case the condition does not hold.
     * @throws InvalidOptionException If the condition does not hold.
     */
    protected static void checkValue(boolean condition, String message) {
        if (condition) {
            return;
        }
        Assert.notNull(message);
        throw new InvalidOptionException(message);
    }

    /**
     * Perform post-processing on the current value of the option. Post-processing is performed after parsing (zero or
     * more times), and before verification of option values.
     *
     * <p>
     * It is allowed to retrieve the values of other options using {@link Options#get}, if needed. This allows for
     * setting a value for an option that was not explicitly given a value (during parsing), based on the value of one
     * or more other options.
     * </p>
     *
     * <p>
     * The standard implementation does nothing. Derived classes may override this method to perform actual
     * post-processing. If they do, they should overwrite the value of the option, by using the {@link Options#set}
     * method.
     * </p>
     *
     * @param value The current value of this option.
     *
     * @see #parseValue
     * @see #verifyValue
     */
    public void postProcessValue(T value) {
        // By default, no post-processing is performed.
    }

    /**
     * Verifies the current value of the option, after parsing (zero or more times) and post-processing.
     *
     * <p>
     * It is allowed to retrieve the values of other options using {@link Options#get}, if needed. If the value is
     * invalid, or if the combined state of all options is inconsistent, this method should throw an
     * {@link InvalidOptionException}. Preferably, use the {@link #checkValue} method to perform validation of the
     * option value.
     * </p>
     *
     * @param value The current value of this option. This is the value to check.
     * @throws InvalidOptionException If the value is invalid, or if the combined state of all options is inconsistent.
     *     The message of the exception should indicate why the value is invalid. The option framework will wrap it to
     *     indicate which option had an invalid value. Preferably, use the {@link #checkValue} method to perform
     *     validation of the option value.
     *
     * @see #parseValue
     * @see #postProcessValue
     */
    public void verifyValue(T value) {
        // By default, there is no problem.
    }

    /**
     * Returns the command line arguments, such that when parsed, result in the given value for this option.
     *
     * <p>
     * Even though the passed value is of type {@link Object}, it may be assumed that it is of the correct type for the
     * option. As such, it can be safely cast to the option value type.
     * </p>
     *
     * <p>
     * Note that command line arguments must be position independent, as the order in which option groups are asked for
     * the command line arguments, is not guaranteed to be fixed.
     * </p>
     *
     * <p>
     * In general, returning an empty sequence for the command line is unwanted, as parsing that has no effect. To make
     * sure the option gets the correct value, an argument for the option has to actually be parsed. Thus, even for the
     * default value of an option, returning an empty command line would result in undesired behavior.
     * </p>
     *
     * <p>
     * For options that don't have a value, such as the {@code --help} option, returning an empty sequence is allowed.
     * </p>
     *
     * @param value The value to obtain the command line arguments for.
     * @return The command line arguments, such that when parsed, result in the given value for this option.
     * @see OptionGroup#getCmdLine
     */
    public abstract String[] getCmdLine(Object value);

    /**
     * Creates and returns an option group for this option, for use in an {@link OptionDialog}.
     *
     * @param page The option page to add the option group to.
     * @return An option group for this option, for use in an {@link OptionDialog}.
     */
    public abstract OptionGroup<T> createOptionGroup(Composite page);

    /** Sorter that can sort options by their long names. */
    public static final OptionSorter SORTER = new OptionSorter();

    /** Class for sorting options by their long names, in a smart case insensitive manner. */
    @SuppressWarnings("rawtypes")
    protected static class OptionSorter implements Comparator<Option> {
        @Override
        public int compare(Option arg0, Option arg1) {
            return Strings.SORTER.compare(arg0.getCmdLong(), arg1.getCmdLong());
        }
    }
}
