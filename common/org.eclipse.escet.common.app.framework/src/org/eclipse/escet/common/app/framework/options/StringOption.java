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

import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** Base class for all string valued options. */
public abstract class StringOption extends Option<String> {
    /** The default value of the string valued option. */
    protected final String defaultValue;

    /**
     * Setting to control representation of the empty value. {@code true} to represent empty strings as {@code null}, or
     * {@code false} to represent {@code null} as empty string.
     */
    protected final boolean emptyAsNull;

    /**
     * The description for this option, in the option dialog, or {@code null} if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogDescr;

    /**
     * The label text of the label before the text box for this option, in the option dialog. May be {@code null} to
     * indicate that no text label is needed. Must be {@code null} if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogLabelText;

    /**
     * Constructor for the {@link StringOption} class. Don't directly create instances of derived classes. Use the
     * {@link Options#getInstance} method instead.
     *
     * @param name The name of the option. Example: {@code "Supervisor name"}.
     * @param description The description of the option. Example: {@code
     *      "The name to use for the generated supervisor automaton.
     *      [DEFAULT=\"supervisor\"]"}.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. Must not be "*".
     * @param cmdValue The name of the option value, for command line processing. Must not be {@code null}.
     * @param defaultValue The default value of the option. Mostly irrelevant if the option is mandatory.
     * @param emptyAsNull Setting to control representation of the empty value. {@code true} to represent empty strings
     *     as {@code null}, or {@code false} to represent {@code null} as empty string.
     * @param showInDialog Whether to show the option in option dialogs.
     * @param optDialogDescr The description for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "The name to use for the generated supervisor
     *      automaton."}.
     * @param optDialogLabelText The text of the label before the text box for this option, in the option dialog. <b>If
     *     set, text must end with a colon.</b> May be {@code null} to indicate that no text label is needed. Must be
     *     {@code null} if {@link #showInDialog} is {@code
     *      false}. If not {@code null}, Example: {@code "Name:"}.
     */
    public StringOption(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            String defaultValue, boolean emptyAsNull, boolean showInDialog, String optDialogDescr,
            String optDialogLabelText)
    {
        super(name, description, cmdShort, cmdLong, cmdValue, showInDialog);
        Assert.check(!cmdLong.equals("*"));
        Assert.check(cmdValue != null);
        Assert.ifAndOnlyIf(showInDialog, optDialogDescr != null);
        Assert.implies(!showInDialog, optDialogLabelText == null);
        if (optDialogLabelText != null) {
            Assert.check(optDialogLabelText.endsWith(":"));
        }
        this.defaultValue = defaultValue;
        this.emptyAsNull = emptyAsNull;
        this.optDialogDescr = optDialogDescr;
        this.optDialogLabelText = optDialogLabelText;
    }

    @Override
    public String getDefault() {
        return defaultValue;
    }

    @Override
    public String parseValue(String optName, String value) {
        if (value == null && !emptyAsNull) {
            return "";
        }
        if (value != null && value.isEmpty() && emptyAsNull) {
            return null;
        }
        return value;
    }

    @Override
    public String[] getCmdLine(Object value) {
        String v = (String)value;
        String s = (v == null) ? "" : v;
        return new String[] {"--" + cmdLong + "=" + s};
    }

    @Override
    public OptionGroup<String> createOptionGroup(Composite page) {
        // Paranoia checking.
        Assert.check(showInDialog);

        // Construct option group.
        return new OptionGroup<>(page, this) {
            Label valueLabel;

            Text valueText;

            @Override
            protected void addComponents(Group group) {
                valueText = new Text(group, SWT.SINGLE | SWT.BORDER);

                if (optDialogLabelText == null) {
                    layoutGeneric(new Object[] {valueText});
                } else {
                    valueLabel = new Label(group, SWT.NULL);
                    valueLabel.setText(optDialogLabelText);
                    layoutGeneric(new Object[] {new Control[] {valueLabel, valueText}}, 0);
                }
            }

            @Override
            public String getDescription() {
                return optDialogDescr;
            }

            @Override
            public void setToValue(String value) {
                valueText.setText(value == null ? "" : value);
            }

            @Override
            public String[] getCmdLine() {
                String arg = valueText.getText();
                return new String[] {"--" + cmdLong + "=" + arg};
            }
        };
    }
}
