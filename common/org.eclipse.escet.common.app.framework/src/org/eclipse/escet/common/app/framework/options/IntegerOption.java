//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/** Base class for all integer valued options. */
public abstract class IntegerOption extends Option<Integer> {
    /** Whether the option has a special value, encoded as {@code null}. */
    protected final boolean hasSpecialValue;

    /**
     * The default value of the {@link Integer} valued option. May be {@code null} to indicate the special value, but
     * only if {@link #hasSpecialValue} is {@code true}.
     */
    protected final Integer defaultValue;

    /**
     * The default normal value of the {@link Integer} valued option. Must be {@code null} if and only if
     * {@link #hasSpecialValue} is {@code false}.
     */
    protected final Integer defaultNormalValue;

    /** The minimum allowed value. */
    protected final int minimumValue;

    /** The maximum allowed value. */
    protected final int maximumValue;

    /**
     * The command line syntax for the special value. Must not overlap with the syntax of integers. Must be {@code null}
     * if {@link #hasSpecialValue} is {@code false}.
     */
    protected final String specialValueSyntax;

    /**
     * The page increment value for the {@link Spinner spinner} for this option, in the option dialog. Ignored if
     * {@link #defaultValue #showInDialog} is {@code false}.
     */
    protected final int pageIncrementValue;

    /**
     * The description for this option, in the option dialog, or {@code null} if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogDescr;

    /**
     * The text of the label of the radio button for the special value. The text should represent that whenever the
     * radio button is selected, the special value is used. Must be {@code null} if {@link #hasSpecialValue} is
     * {@code false}, or if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogSpecialText;

    /**
     * The text of the label of the radio button for the normal value. The text should represent that whenever the radio
     * button is selected, the special value is not used. Must be {@code null} if {@link #hasSpecialValue} is
     * {@code false}, or if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogNormalText;

    /**
     * The label text of the label before the {@link Spinner spinner} for this option, in the option dialog. May be
     * {@code null} to indicate that no text label is needed. Must be {@code null} if {@link #showInDialog} is
     * {@code false}.
     */
    protected final String optDialogLabelText;

    /**
     * Constructor for the {@link IntegerOption} class, without a special value. Don't directly create instances of
     * derived classes. Use the {@link Options#getInstance} method instead.
     *
     * @param name The name of the option. Example: {@code "Number of runs"}.
     * @param description The description of the option. Example: {@code
     *      "The number of runs to perform. Must be in the range [0..65535].
     *      [DEFAULT=20]"}.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. Must not be "*".
     * @param cmdValue The name of the option value, for command line processing. Must not be {@code null}.
     * @param defaultValue The default value of the option. Mostly irrelevant if the option is mandatory.
     * @param minimumValue The minimum allowed value.
     * @param maximumValue The maximum allowed value.
     * @param pageIncrementValue The page increment value for the {@link Spinner spinner} for this option, in the option
     *     dialog. Ignored if {@link #defaultValue #showInDialog} is {@code false}.
     * @param showInDialog Whether to show the option in option dialogs.
     * @param optDialogDescr The description for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "The number of runs to perform."}.
     * @param optDialogLabelText The text of the label before the {@link Spinner spinner} for this option, in the option
     *     dialog. <b>If set, text must end with a colon.</b> May be {@code null} to indicate that no text label is
     *     needed. Must be {@code null} if {@link #showInDialog} is {@code false}. {@code "Runs:"}.
     */
    public IntegerOption(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            int defaultValue, int minimumValue, int maximumValue, int pageIncrementValue, boolean showInDialog,
            String optDialogDescr, String optDialogLabelText)
    {
        this(name, description, cmdShort, cmdLong, cmdValue, defaultValue, minimumValue, maximumValue,
                pageIncrementValue, showInDialog, optDialogDescr, optDialogLabelText, false, null, null, null, null);
    }

    /**
     * Constructor for the {@link IntegerOption} class. Don't directly create instances of derived classes. Use the
     * {@link Options#getInstance} method instead.
     *
     * @param name The name of the option. Example: {@code "Number of runs"}.
     * @param description The description of the option. Example: {@code
     *      "The number of runs to perform. Specify \"auto\" to use an
     *      automatically determined number of runs, or a custom number of runs
     *      in the range [0..65535]. [DEFAULT=auto]"}.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. Must not be "*".
     * @param cmdValue The name of the option value, for command line processing. Must not be {@code null}.
     * @param defaultValue The default value of the option. Mostly irrelevant if the option is mandatory. May be
     *     {@code null} to indicate the special value, but only if {@link #hasSpecialValue} is {@code true}.
     * @param minimumValue The minimum allowed value.
     * @param maximumValue The maximum allowed value.
     * @param pageIncrementValue The page increment value for the {@link Spinner spinner} for this option, in the option
     *     dialog. Ignored if {@link #defaultValue #showInDialog} is {@code false}.
     * @param showInDialog Whether to show the option in option dialogs.
     * @param optDialogDescr The description for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "The number of runs to perform."}.
     * @param optDialogLabelText The text of the label before the {@link Spinner spinner} for this option, in the option
     *     dialog. <b>If set, text must end with a colon.</b> May be {@code null} to indicate that no text label is
     *     needed. Must be {@code null} if {@link #showInDialog} is {@code false}. {@code "Runs:"}.
     * @param hasSpecialValue Whether the option has a special value, encoded as {@code null}.
     * @param defaultNormalValue The default normal value of the {@link Integer} valued option. Must be {@code null} if
     *     and only if {@link #hasSpecialValue} is {@code false}.
     * @param specialValueSyntax The command line syntax for the special value. Must not overlap with the syntax of
     *     integers. Must be {@code null} if {@link #hasSpecialValue} or {@link #showInDialog} is {@code false}.
     * @param optDialogSpecialText The text of the label of the radio button for the special value. The text should
     *     represent that whenever the radio button is selected, the special value is used. Must be {@code null} if
     *     {@link #hasSpecialValue} is {@code false}, or if {@link #showInDialog} is {@code false}. Example: {@code "Use
     *      automatically determined number of runs"}.
     * @param optDialogNormalText The text of the label of the radio button for the normal value. The text should
     *     represent that whenever the radio button is selected, the special value is not used. Must be {@code null} if
     *     {@link #hasSpecialValue} is {@code false}, or if {@link #showInDialog} is {@code false}. Example: {@code
     *      "Use custom number of runs"}.
     */
    public IntegerOption(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            Integer defaultValue, int minimumValue, int maximumValue, int pageIncrementValue, boolean showInDialog,
            String optDialogDescr, String optDialogLabelText, boolean hasSpecialValue, Integer defaultNormalValue,
            String specialValueSyntax, String optDialogSpecialText, String optDialogNormalText)
    {
        super(name, description, cmdShort, cmdLong, cmdValue, showInDialog);
        Assert.notNull(cmdLong);
        Assert.check(!cmdLong.equals("*"));
        Assert.notNull(cmdValue);
        Assert.implies(defaultValue == null, hasSpecialValue);
        if (defaultValue != null) {
            Assert.check(minimumValue <= defaultValue);
            Assert.check(defaultValue <= maximumValue);
        }
        Assert.ifAndOnlyIf(!showInDialog || !hasSpecialValue, defaultNormalValue == null);
        if (defaultNormalValue != null) {
            Assert.check(minimumValue <= defaultNormalValue);
            Assert.check(defaultNormalValue <= maximumValue);
        }
        Assert.ifAndOnlyIf(showInDialog, optDialogDescr != null);
        Assert.implies(!showInDialog, optDialogLabelText == null);
        if (optDialogLabelText != null) {
            Assert.check(optDialogLabelText.endsWith(":"));
        }
        Assert.ifAndOnlyIf(hasSpecialValue, specialValueSyntax != null);
        Assert.ifAndOnlyIf(hasSpecialValue && showInDialog, optDialogSpecialText != null);
        Assert.ifAndOnlyIf(hasSpecialValue && showInDialog, optDialogNormalText != null);
        this.defaultValue = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.pageIncrementValue = pageIncrementValue;
        this.optDialogDescr = optDialogDescr;
        this.optDialogLabelText = optDialogLabelText;
        this.hasSpecialValue = hasSpecialValue;
        this.defaultNormalValue = defaultNormalValue;
        this.specialValueSyntax = specialValueSyntax;
        this.optDialogSpecialText = optDialogSpecialText;
        this.optDialogNormalText = optDialogNormalText;
    }

    @Override
    public Integer getDefault() {
        return defaultValue;
    }

    @Override
    public Integer parseValue(String optName, String value) {
        // Special value handling.
        if (hasSpecialValue) {
            if (value.equals(specialValueSyntax)) {
                return null;
            }
        }

        // Normal value.
        int v;
        try {
            v = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String msg = fmt("Invalid integer number \"%s\".", value);
            throw new InvalidOptionException(msg);
        }
        checkValue(v >= minimumValue, v + " < " + minimumValue);
        checkValue(v <= maximumValue, v + " > " + maximumValue);
        return v;
    }

    @Override
    public String[] getCmdLine(Object value) {
        if (value == null) {
            Assert.check(hasSpecialValue);
            return new String[] {"--" + cmdLong + "=" + specialValueSyntax};
        } else {
            return new String[] {"--" + cmdLong + "=" + value};
        }
    }

    @Override
    public OptionGroup<Integer> createOptionGroup(Composite page) {
        // Paranoia checking.
        Assert.check(showInDialog);

        // Construct option group.
        return new IntegerOptionGroup(page);
    }

    /** Option dialog option group for the {@link IntegerOption}. */
    private class IntegerOptionGroup extends OptionGroup<Integer> implements SelectionListener {
        /** Radio button for the special value, if applicable. */
        private Button specialButton;

        /** Radio button for the normal value, if applicable. */
        private Button normalButton;

        /** Label before the value spinner, if applicable. */
        Label valueLabel;

        /** Value spinner. */
        Spinner valueSpinner;

        /**
         * Constructor for the {@link IntegerOptionGroup} class.
         *
         * @param page The options page that is the parent of this option group.
         */
        public IntegerOptionGroup(Composite page) {
            super(page, IntegerOption.this);
        }

        @Override
        protected void addComponents(Group group) {
            if (hasSpecialValue) {
                specialButton = new Button(group, SWT.RADIO);
                specialButton.setText(optDialogSpecialText);
                normalButton = new Button(group, SWT.RADIO);
                normalButton.setText(optDialogNormalText);
                specialButton.addSelectionListener(this);
                normalButton.addSelectionListener(this);
            }

            valueSpinner = new Spinner(group, SWT.BORDER);
            valueSpinner.setValues((defaultValue == null) ? defaultNormalValue : defaultValue, minimumValue,
                    maximumValue, 0, 1, pageIncrementValue);

            Control[] valueLayout;
            if (optDialogLabelText == null) {
                valueLayout = new Control[] {valueSpinner};
            } else {
                valueLabel = new Label(group, SWT.NULL);
                valueLabel.setText(optDialogLabelText);
                valueLayout = new Control[] {valueLabel, valueSpinner};
            }

            Object[] layout;
            if (hasSpecialValue) {
                layout = new Object[] {specialButton, normalButton, valueLayout};
            } else if (optDialogLabelText == null) {
                layout = valueLayout;
            } else {
                layout = new Object[] {valueLayout};
            }

            layoutGeneric(layout, hasSpecialValue ? 32 : 0);
        }

        @Override
        public String getDescription() {
            return optDialogDescr;
        }

        @Override
        public void setToValue(Integer value) {
            if (value == null) {
                Assert.check(hasSpecialValue);

                specialButton.setSelection(true);
                Event event = new Event();
                event.widget = specialButton;
                widgetSelected(new SelectionEvent(event));

                value = defaultNormalValue;
            } else if (hasSpecialValue) {
                normalButton.setSelection(true);
                Event event = new Event();
                event.widget = normalButton;
                widgetSelected(new SelectionEvent(event));
            }

            valueSpinner.setSelection(value);
        }

        @Override
        public String[] getCmdLine() {
            if (hasSpecialValue && specialButton.getSelection()) {
                return new String[] {"--" + cmdLong + "=" + specialValueSyntax};
            }

            int value = valueSpinner.getSelection();
            return new String[] {"--" + cmdLong + "=" + value};
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            if (e.widget == specialButton) {
                valueSpinner.setEnabled(false);
            } else if (e.widget == normalButton) {
                valueSpinner.setEnabled(true);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    }
}
