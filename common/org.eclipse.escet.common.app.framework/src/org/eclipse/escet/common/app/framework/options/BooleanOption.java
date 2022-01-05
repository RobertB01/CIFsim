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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Locale;

import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/** Base class for all boolean options. Allows for both boolean options with a value, and without one. */
public abstract class BooleanOption extends Option<Boolean> {
    /** The default value of the boolean valued option. */
    protected final boolean defaultValue;

    /**
     * The description for this option, in the option dialog, or {@code null} if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogDescr;

    /**
     * The text of the check box for this option, in the option dialog, or {@code null} if {@link #showInDialog} is
     * {@code false}.
     */
    protected final String optDialogCheckboxText;

    /**
     * Constructor for the {@link BooleanOption} class. Don't directly create instances of derived classes. Use the
     * {@link Options#getInstance} method instead.
     *
     * @param name The name of the option. Example: {@code "Test mode"}.
     * @param description The description of the option. Example: {@code
     *      "Whether to enable test mode (BOOL=yes), or disable it (BOOL=no).
     *      Test modes disables visual interfaces etc. [DEFAULT=no]"}.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. Must not be "*".
     * @param cmdValue The name of the option value, for command line processing. May be {@code null} if the option
     *     doesn't require a value. Must be {@code "BOOL"} otherwise.
     * @param defaultValue The default value of the option. Mostly irrelevant if the option is mandatory.
     * @param showInDialog Whether to show the option in option dialogs.
     * @param optDialogDescr The description for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "Test modes disables visual interfaces etc."}.
     * @param optDialogCheckboxText The text of the check box for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "Enable test mode"}.
     */
    public BooleanOption(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            boolean defaultValue, boolean showInDialog, String optDialogDescr, String optDialogCheckboxText)
    {
        super(name, description, cmdShort, cmdLong, cmdValue, showInDialog);
        Assert.check(!cmdLong.equals("*"));
        Assert.check(cmdValue == null || cmdValue.equals("BOOL"));
        Assert.ifAndOnlyIf(showInDialog, optDialogDescr != null);
        Assert.ifAndOnlyIf(showInDialog, optDialogCheckboxText != null);
        this.defaultValue = defaultValue;
        this.optDialogDescr = optDialogDescr;
        this.optDialogCheckboxText = optDialogCheckboxText;
    }

    @Override
    public Boolean getDefault() {
        return defaultValue;
    }

    @Override
    public Boolean parseValue(String optName, String value) {
        if (cmdValue == null) {
            // No value expected. Return the non-default value.
            Assert.check(value == null);
            return !defaultValue;
        }

        // Value expected.
        Assert.notNull(value);
        String v = value.toLowerCase(Locale.US);
        if (v.equals("yes") || v.equals("true") || v.equals("on") || v.equals("1")) {
            return true;
        }
        if (v.equals("no") || v.equals("false") || v.equals("off") || v.equals("0")) {
            return false;
        }

        // Invalid value.
        String msg = fmt("\"%s\" is not a valid value for a boolean option.", value);
        throw new InvalidOptionException(msg);
    }

    @Override
    public String[] getCmdLine(Object value) {
        boolean v = (Boolean)value;
        String s = v ? "yes" : "no";
        return new String[] {"--" + cmdLong + "=" + s};
    }

    @Override
    public OptionGroup<Boolean> createOptionGroup(Composite page) {
        // Paranoia checking.
        Assert.check(showInDialog);

        // Construct option group.
        return new OptionGroup<>(page, this) {
            Button enabledButton;

            @Override
            protected void addComponents(Group group) {
                enabledButton = new Button(group, SWT.CHECK);
                enabledButton.setText(optDialogCheckboxText);

                layoutVertical(new Control[] {enabledButton});
            }

            @Override
            public String getDescription() {
                return optDialogDescr;
            }

            @Override
            public void setToValue(Boolean value) {
                enabledButton.setSelection(value);
            }

            @Override
            public String[] getCmdLine() {
                boolean value = enabledButton.getSelection();

                if (cmdValue == null) {
                    return (value != defaultValue) ? new String[] {"--" + cmdLong} : new String[] {};
                }

                String valueTxt = value ? "yes" : "no";
                return new String[] {"--" + cmdLong + "=" + valueTxt};
            }
        };
    }
}
