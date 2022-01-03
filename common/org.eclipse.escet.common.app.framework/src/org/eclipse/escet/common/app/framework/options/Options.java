//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Maps.copy;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;

/** Generic option class. */
public class Options { // ignore checkstyle HideUtilityClassConstructor
    /**
     * Singleton instances for all options. Upon the first request for an instance of a certain option, a singleton
     * instance is created by the {@link #getInstance} option.
     */
    @SuppressWarnings("rawtypes")
    private static Map<Class, Option> instances = map();

    /** The actual stored options, as mapping from options to option values. */
    private Map<Option<?>, OptionValue<?>> opts = map();

    /**
     * Returns a copy of the stored options, as mapping from options to option values.
     *
     * @return A copy of the stored options, as mapping from options to option values.
     */
    public static Map<Option<?>, OptionValue<?>> getOptionMap() {
        return copy(AppEnv.getOptions().opts);
    }

    /**
     * Returns a value indicating whether a value is set for the given option.
     *
     * @param <T> The type of the data value for the option.
     * @param optionClass The class of the option to check for a value.
     * @return A value indicating whether a value is set for the given option.
     */
    @SuppressWarnings("rawtypes")
    public static <T> boolean hasValue(Class optionClass) {
        Option<T> option = getInstance(optionClass);
        return AppEnv.getOptions().opts.containsKey(option);
    }

    /**
     * Returns a value indicating whether a value is set for the given option.
     *
     * @param <T> The type of the data value for the option.
     * @param option The option to check for a value.
     * @return A value indicating whether a value is set for the given option.
     */
    public static <T> boolean hasValue(Option<T> option) {
        return AppEnv.getOptions().opts.containsKey(option);
    }

    /**
     * Returns the value of an option.
     *
     * <p>
     * This internal method exists to allow the {@link #get} method (which is public and static) to use the singleton
     * instance of this class. However, it also leaves open the possibility to for instance use multiple option
     * instances for multiple threads, etc.
     * </p>
     *
     * @param <T> The type of the data value for the option.
     * @param option The option to return the value for.
     * @return The value of the option.
     *
     * @throws AssertionError If the requested option does not have a value.
     */
    private <T> T getInternal(Option<T> option) {
        if (!opts.containsKey(option)) {
            String msg = fmt("Unknown option: %s. Please register it with the application.", option);
            throw new IllegalStateException(msg);
        }
        @SuppressWarnings("unchecked")
        OptionValue<T> value = (OptionValue<T>)opts.get(option);
        return value.getValue();
    }

    /**
     * Sets the value of an option.
     *
     * <p>
     * This internal method exists to allow the {@link #set} method (which is public and static) to use the singleton
     * instance of this class. However, it also leaves open the possibility to for instance use multiple option
     * instances for multiple threads, etc.
     * </p>
     *
     * @param <T> The type of the data value for the option.
     * @param option The option to set the value for.
     * @param value The value to set.
     */
    private <T> void setInternal(Option<T> option, T value) {
        opts.put(option, new OptionValue<>(option, value));
    }

    /**
     * Returns the value of an option.
     *
     * @param <T> The type of the data value for the option.
     * @param optionClass The class of the option to return the value for.
     * @return The value of the option.
     *
     * @throws AssertionError If the requested option does not have a value.
     */
    public static <T> T get(Class<? extends Option<T>> optionClass) {
        Option<T> option = getInstance(optionClass);
        return AppEnv.getOptions().getInternal(option);
    }

    /**
     * Returns the value of an option.
     *
     * @param <T> The type of the data value for the option.
     * @param option The option to return the value for.
     * @return The value of the option.
     *
     * @throws AssertionError If the requested option does not have a value.
     */
    public static <T> T get(Option<T> option) {
        return AppEnv.getOptions().getInternal(option);
    }

    /**
     * Sets the value of the option with the given name to the given value.
     *
     * @param <T> The type of the data value for the option.
     * @param optionClass The class of the option to set the value for.
     * @param value The value to set.
     */
    @SuppressWarnings("rawtypes")
    public static <T> void set(Class optionClass, T value) {
        Option<T> option = getInstance(optionClass);
        AppEnv.getOptions().setInternal(option, value);
    }

    /**
     * Sets the value of the option with the given name to the given value.
     *
     * @param <T> The type of the data value for the option.
     * @param option The option to set the value for.
     * @param value The value to set.
     */
    public static <T> void set(Option<T> option, T value) {
        AppEnv.getOptions().setInternal(option, value);
    }

    /**
     * Initializes options to their default values, given the allowed options.
     *
     * <p>
     * The given options category must be a wrapper around the actual categories, and must not contain any options
     * itself. Only sub-categories may contain actual options.
     * </p>
     *
     * @param options The options category wrapper that contains all the categories and options that are allowed.
     * @param inclNonDlg Whether to also (re-)initialize options not shown in the option dialog.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void initialize(OptionCategory options, boolean inclNonDlg) {
        Map<String, Option<?>> lmap = map();
        options.fillLongOptMap(lmap);
        for (Option opt: lmap.values()) {
            if (!opt.showInDialog && !inclNonDlg) {
                continue;
            }
            set(opt, opt.getDefault());
        }
    }

    /**
     * Parses command line options, given the allowed options.
     *
     * <p>
     * The given options category must be a wrapper around the actual categories, and must not contain any options
     * itself. Only sub-categories may contain actual options.
     * </p>
     *
     * <p>
     * Prior to using this method, make sure all options are initialized, by using the {@link #initialize} method.
     * </p>
     *
     * @param options The options category wrapper that contains all the categories and options that are allowed.
     * @param args The command line arguments to process.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void parse(OptionCategory options, String[] args) {
        // Precondition checks.
        Assert.check(options.getOptions().isEmpty());

        // Construct option mappings.
        Map<Character, Option<?>> smap = map();
        Map<String, Option<?>> lmap = map();
        options.fillShortOptMap(smap);
        options.fillLongOptMap(lmap);

        // Process the arguments. Initially, we are not in an option, and
        // there is no last option information.
        boolean inOpt = false;
        Option lastOpt = null;
        String lastOptName = null;
        for (int i = 0; i < args.length; i++) {
            // Get the current argument.
            String arg = args[i];

            // Distinction on whether we are in an argument (need to process
            // the value), or not (need to process a new argument).
            if (!inOpt) {
                // We are not in an option, so we need to process a new
                // argument.
                if (arg.startsWith("--")) {
                    // Long option argument. Strip the "--" prefix, and
                    // split it at the "=" character, if any.
                    String argPart = arg.substring(2);
                    int idx = argPart.indexOf('=');
                    String argName;
                    String argValue;
                    if (idx == -1) {
                        argName = argPart;
                        argValue = null;
                    } else {
                        argName = argPart.substring(0, idx);
                        argValue = argPart.substring(idx + 1);
                    }

                    // Look up the long option, given its name, and set the
                    // current option name.
                    Option opt = lmap.get(argName);
                    String optName = "--" + argName;
                    if (opt == null) {
                        String msg = fmt("Unknown option: \"%s\"", arg);
                        throw new InvalidOptionException(msg);
                    }

                    // Option may or may not have a value.
                    if (opt.getCmdValue() == null) {
                        // Option has no value.
                        if (argValue != null) {
                            // Option has no value, but argument does.
                            String msg = fmt("No value expected for option: \"%s\"", arg);
                            throw new InvalidOptionException(msg);
                        }

                        // Option has no value, and neither does the argument.
                        set(opt, parseOptionValue(opt, optName, null));
                    } else {
                        // Option requires a value.
                        if (argValue == null) {
                            // Option requires a value, but argument has none.
                            String msg = fmt("Missing \"=\" character in: \"%s\"", arg);
                            throw new InvalidOptionException(msg);
                        }

                        // Option requires a value, and argument has one.
                        set(opt, parseOptionValue(opt, optName, argValue));
                    }
                } else if (arg.startsWith("-")) {
                    // Short option argument. Strip the "-" prefix, and get the
                    // option character.
                    String argPart = arg.substring(1);
                    if (argPart.length() == 0) {
                        String msg = fmt("Invalid option format: \"%s\"", arg);
                        throw new InvalidOptionException(msg);
                    }
                    Character c = argPart.charAt(0);

                    // Look up the long option, given its single character
                    // name, and set the current option name.
                    Option opt = smap.get(c);
                    String optName = "-" + c;
                    if (opt == null) {
                        String msg = fmt("Unknown option: \"%s\"", arg);
                        throw new InvalidOptionException(msg);
                    }

                    // Option may or may not have a value.
                    if (opt.getCmdValue() == null) {
                        // Option has no value.
                        if (argPart.length() > 1) {
                            // Option has no value, but argument does.
                            String msg = fmt("No value expected for option: \"%s\"", arg);
                            throw new InvalidOptionException(msg);
                        }
                        // Option has no value, and neither does the argument.
                        set(opt, parseOptionValue(opt, optName, null));
                    } else {
                        // Option requires a value.
                        if (argPart.length() > 1) {
                            // Option requires a value, and argument has one.
                            set(opt, parseOptionValue(opt, optName, argPart.substring(1)));
                        } else {
                            // Option requires a value, but argument has none.
                            // The value will be given by the next command
                            // line argument.
                            inOpt = true;
                            lastOpt = opt;
                            lastOptName = optName;
                        }
                    }
                } else {
                    // Remaining arguments. Look up special long option name
                    // "*".
                    Option opt = lmap.get("*");
                    if (opt == null) {
                        String msg = fmt("Non-option argument not expected: \"%s\"", arg);
                        throw new InvalidOptionException(msg);
                    }

                    // Get the current list of values from the option.
                    List<Object> curValues = (List<Object>)AppEnv.getOptions().opts.get(opt).getValue();

                    // Get the values from the current argument.
                    List<Object> newValues = (List<Object>)parseOptionValue(opt, null, arg);

                    // Add the new values to the old ones.
                    curValues.addAll(newValues);
                }
            } else {
                // Option value. Previous command line argument as a short
                // option argument without value. This is the value for that
                // option.
                set(lastOpt, parseOptionValue(lastOpt, lastOptName, arg));
                inOpt = false;
                lastOpt = null;
                lastOptName = null;
            }

            // Loop sanity check.
            Assert.implies(inOpt, lastOpt != null);
            Assert.implies(!inOpt, lastOpt == null);
            Assert.check((lastOpt == null) == (lastOptName == null));
        }

        // Make sure the command line is complete. It can only be incomplete
        // if the last command line argument was a short option argument
        // without a value, for an option that requires a value.
        if (inOpt) {
            Assert.notNull(lastOptName);
            String msg = fmt("Incomplete command line for option \"%s\".", lastOptName);
            throw new InvalidOptionException(msg);
        }
    }

    /**
     * Tries to parse an option, given a textual value.
     *
     * <p>
     * {@link ApplicationException} exceptions thrown by the {@link Option#parseValue} method are considered to indicate
     * invalid option values. The exceptions are caught, wrapped, and re-thrown.
     * </p>
     *
     * @param <T> The option of the option value.
     * @param opt The option to parse a value for.
     * @param optName The actual option name found on the command line, including {@code "--"} or {@code "-"} prefix. Is
     *     {@code null} for non-option/remaining arguments.
     * @param value The textual value to parse, or {@code null}.
     * @return The parsed option.
     * @throws InvalidOptionException If the value is invalid for the given option.
     *
     * @see Option#parseValue
     */
    private static <T> T parseOptionValue(Option<T> opt, String optName, String value) {
        try {
            return opt.parseValue(optName, value);
        } catch (Application.SuccessfulExitException e) {
            // Just re-throw and let the application framework's exception
            // framework catch it.
            throw e;
        } catch (ApplicationException e) {
            String msg;
            if (optName == null) {
                msg = fmt("Invalid value for argument: \"%s\".", value);
            } else {
                msg = fmt("Invalid value for option \"%s\": \"%s\".", optName, value);
            }
            throw new InvalidOptionException(msg, e);
        }
    }

    /**
     * Given the allowed options, this method performs post-processing on all the options.
     *
     * <p>
     * The given options category must be a wrapper around the actual categories, and must not contain any options
     * itself. Only sub-categories may contain actual options.
     * </p>
     *
     * <p>
     * Prior to using this method, make sure all options are initialized, by using the {@link #initialize} or
     * {@link #parse} method.
     * </p>
     *
     * @param options The options category wrapper that contains all the categories and options that are allowed.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void postProcess(OptionCategory options) {
        // Precondition checks.
        Assert.check(options.getOptions().isEmpty());

        // Construct option mappings.
        Map<String, Option<?>> lmap = map();
        options.fillLongOptMap(lmap);

        // Post-process all options.
        for (Option opt: lmap.values()) {
            opt.postProcessValue(get(opt));
        }
    }

    /**
     * Given the allowed options, this method verifies that all those options have valid and consistent values.
     *
     * <p>
     * The given options category must be a wrapper around the actual categories, and must not contain any options
     * itself. Only sub-categories may contain actual options.
     * </p>
     *
     * <p>
     * Prior to using this method, make sure all options are initialized, by using the {@link #initialize} or
     * {@link #parse} method. They should also have been post-processed using the {@link #postProcess} method.
     * </p>
     *
     * @param options The options category wrapper that contains all the categories and options that are allowed.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void verifyOptions(OptionCategory options) {
        // Precondition checks.
        Assert.check(options.getOptions().isEmpty());

        // Construct option mappings.
        Map<String, Option<?>> lmap = map();
        options.fillLongOptMap(lmap);

        // Verify all option values.
        for (Option opt: lmap.values()) {
            try {
                opt.verifyValue(get(opt));
            } catch (InvalidOptionException e) {
                String cmdLong = opt.getCmdLong();
                if (cmdLong.equals("*")) {
                    throw e;
                } else {
                    String msg = fmt("Invalid value for option \"--%s\".", cmdLong);
                    throw new InvalidOptionException(msg, e);
                }
            }
        }
    }

    /**
     * Returns the singleton instance of the requested option.
     *
     * @param <T> The type of the data value for the option.
     * @param optionClass The class of the option to get the singleton instance for.
     * @return The singleton instance of the requested option.
     */
    @SuppressWarnings("unchecked")
    public static <T> Option<T> getInstance(Class<?> optionClass) {
        synchronized (instances) {
            if (instances.containsKey(optionClass)) {
                // Return the already present singleton instance.
                Option<T> rslt = instances.get(optionClass);
                Assert.notNull(rslt);
                return rslt;
            }

            // Create a singleton instance, store it, and return it.
            Option<T> rslt;
            try {
                rslt = (Option<T>)optionClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                String msg = "Can't create singleton option instance.";
                throw new RuntimeException(msg, e);
            } catch (IllegalArgumentException e) {
                String msg = "Can't create singleton option instance.";
                throw new RuntimeException(msg, e);
            } catch (SecurityException e) {
                String msg = "Can't create singleton option instance.";
                throw new RuntimeException(msg, e);
            }
            instances.put(optionClass, rslt);
            return rslt;
        }
    }
}
