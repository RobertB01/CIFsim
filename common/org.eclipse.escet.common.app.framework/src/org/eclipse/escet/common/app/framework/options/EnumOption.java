//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Base class for all enumeration valued options.
 *
 * <p>
 * By default, the {@link #parseValue} method uses the names of the enumeration constants as values for this option,
 * where the enumeration constants are assumed to have names consisting of upper case letters and underscore symbols
 * ({@code _}) only. In the values, any casing may be used, and a dash ({@code -}) may be used instead of the underscore
 * symbol ({@code _}).
 * </p>
 *
 * <p>
 * By default, the {@link #getCmdLine} method uses the names of the enumeration constants as values for this option, by
 * converting the names to lowercase, and replacing underscore symbols ({@code _}) by dashes ({@code -}).
 * </p>
 *
 * <p>
 * By default, the {@link #getDialogOrder} method returns the enumeration constants in the order that they are defined
 * in the enumeration, and this is the order in which the option values are displayed in the option dialog.
 * </p>
 *
 * @param <T> The enumeration that is used as values for this option.
 */
public abstract class EnumOption<T extends Enum<T>> extends Option<T> {
    /** The default value of the enumeration valued option. */
    protected final T defaultValue;

    /** The class of the enumeration. */
    protected final Class<T> enumClass;

    /**
     * The description for this option, in the option dialog, or {@code null} if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogDescr;

    /**
     * Constructor for the {@link EnumOption} class. Don't directly create instances of derived classes. Use the
     * {@link Options#getInstance} method instead.
     *
     * @param name The name of the option. Example: {@code "Solver algorithm"}.
     * @param description The description of the option. Example: {@code
     *      "The algorithm to use for the solver. Specify \"abc\" (default) to
     *      use the Abc solver, \"def\" to use the Def solver, or \"ghi\" to
     *      use the Ghi solver."} or {@code "The algorithm to use for the
     *      solver. Specify \"abc\" to use the Abc solver, \"def\" to use the
     *      Def solver, or \"ghi\" to use the Ghi solver. [DEFAULT=abc]"}.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. Must not be "*".
     * @param cmdValue The name of the option value, for command line processing. Must not be {@code null}.
     * @param defaultValue The default value of the option. Mostly irrelevant if the option is mandatory.
     * @param showInDialog Whether to show the option in option dialogs.
     * @param optDialogDescr The description for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "The algorithm to use for the solver."}.
     */
    @SuppressWarnings("unchecked")
    public EnumOption(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            T defaultValue, boolean showInDialog, String optDialogDescr)
    {
        super(name, description, cmdShort, cmdLong, cmdValue, showInDialog);
        Assert.check(!cmdLong.equals("*"));
        Assert.check(cmdValue != null);
        Assert.ifAndOnlyIf(showInDialog, optDialogDescr != null);
        this.defaultValue = defaultValue;
        this.enumClass = (Class<T>)defaultValue.getClass();
        this.optDialogDescr = optDialogDescr;
    }

    @Override
    public T getDefault() {
        return defaultValue;
    }

    @Override
    public T parseValue(String optName, String value) {
        value = value.toUpperCase(Locale.US).replace('-', '_');
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw new InvalidOptionException("Unknown option value.");
        }
    }

    @Override
    public String[] getCmdLine(Object value) {
        @SuppressWarnings("unchecked")
        T enumValue = (T)value;
        String rslt = enumValue.name().toLowerCase(Locale.US).replace('_', '-');
        return new String[] {"--" + cmdLong + "=" + rslt};
    }

    /**
     * Returns the values of the enumeration, in the order they are to be displayed in the option dialog.
     *
     * @return The values of the enumeration, in the order they are to be displayed in the option dialog.
     */
    protected T[] getDialogOrder() {
        return enumClass.getEnumConstants();
    }

    /**
     * Checks whether the given enumeration values cover all the possible values of the enumeration, and whether they
     * don't contain duplicates.
     *
     * @param values The enumeration values to check.
     */
    protected void checkEnumValues(T[] values) {
        // Make sure all value are accounted for.
        for (T value: enumClass.getEnumConstants()) {
            Assert.check(ArrayUtils.contains(values, value));
        }

        // Make sure we have no duplicates.
        Set<T> valueSet = setc(values.length);
        for (T value: values) {
            boolean added = valueSet.add(value);
            Assert.check(added);
        }
    }

    /**
     * Returns the text to use for the radio button for the given option value, in the option dialog.
     *
     * <p>
     * For options that are not shown in the option dialog, just return a dummy value ({@code null} for instance).
     * </p>
     *
     * <p>
     * Example text: {@code "Abc solver"}.
     * </p>
     *
     * @param value The option value.
     * @return The text to use for the radio button.
     */
    protected abstract String getDialogText(T value);

    @Override
    public OptionGroup<T> createOptionGroup(Composite page) {
        // Paranoia checking.
        Assert.check(showInDialog);

        // Construct option group.
        return new OptionGroup<T>(page, this) {
            Button[] buttons;

            T[] enumValues;

            @Override
            protected void addComponents(Group group) {
                enumValues = getDialogOrder();
                checkEnumValues(enumValues);

                buttons = new Button[enumValues.length];

                for (int i = 0; i < enumValues.length; i++) {
                    buttons[i] = new Button(group, SWT.RADIO);
                    buttons[i].setText(getDialogText(enumValues[i]));
                }

                layoutGeneric(buttons);
            }

            @Override
            public String getDescription() {
                return optDialogDescr;
            }

            @Override
            public void setToValue(T value) {
                int idx = ArrayUtils.indexOf(enumValues, value);
                buttons[idx].setSelection(true);
            }

            @Override
            public String[] getCmdLine() {
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].getSelection()) {
                        T value = enumValues[i];
                        return EnumOption.this.getCmdLine(value);
                    }
                }
                throw new RuntimeException("This point is never reached.");
            }
        };
    }
}
