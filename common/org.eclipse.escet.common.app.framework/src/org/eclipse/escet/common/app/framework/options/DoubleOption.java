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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** Base class for all double valued options. */
public abstract class DoubleOption extends Option<Double> {
    /** Whether the option has a special value, encoded as {@code null}. */
    protected final boolean hasSpecialValue;

    /**
     * The default value of the {@link Double} valued option. May be {@code null} to indicate the special value, but
     * only if {@link #hasSpecialValue} is {@code true}.
     */
    protected final Double defaultValue;

    /**
     * The default normal value of the {@link Double} valued option. Must be {@code null} if {@link #hasSpecialValue} or
     * {@link #showInDialog} is {@code false}.
     */
    protected final Double defaultNormalValue;

    /** The minimum allowed value, or {@code null} for no restriction. */
    protected final Double minimumValue;

    /** The maximum allowed value, or {@code null} for no restriction. */
    protected final Double maximumValue;

    /**
     * The command line syntax for the special value. Must not overlap with the syntax of doubles. Must be {@code null}
     * if {@link #hasSpecialValue} is {@code false}.
     */
    protected final String specialValueSyntax;

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
     * The label text of the label before the text box for this option, in the option dialog. May be {@code null} to
     * indicate that no text label is needed. Must be {@code null} if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogLabelText;

    /**
     * Constructor for the {@link DoubleOption} class, without a special value. Don't directly create instances of
     * derived classes. Use the {@link Options#getInstance} method instead.
     *
     * @param name The name of the option. Example: {@code "Solver tolerance"}.
     * @param description The description of the option. Example: {@code
     *      "The absolute tolerance (maximum allowed absolute error) to use for
     *      the solver, as an integer or real number. [DEFAULT=1e-7]"}.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. Must not be "*".
     * @param cmdValue The name of the option value, for command line processing. Must not be {@code null}.
     * @param defaultValue The default value of the option. Mostly irrelevant if the option is mandatory.
     * @param minimumValue The minimum allowed value, or {@code null} for no restriction.
     * @param maximumValue The maximum allowed value, or {@code null} for no restriction.
     * @param showInDialog Whether to show the option in option dialogs.
     * @param optDialogDescr The description for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "The absolute tolerance (maximum allowed absolute
     *      error) to use for the solver, as an integer or real number."}.
     * @param optDialogLabelText The text of the label before the text box for this option, in the option dialog. <b>If
     *     set, text must end with a colon.</b> May be {@code null} to indicate that no text label is needed. Must be
     *     {@code null} if {@link #showInDialog} is {@code
     *      false}. Example: {@code "Tolerance:"}.
     */
    public DoubleOption(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            double defaultValue, Double minimumValue, Double maximumValue, boolean showInDialog, String optDialogDescr,
            String optDialogLabelText)
    {
        this(name, description, cmdShort, cmdLong, cmdValue, defaultValue, minimumValue, maximumValue, showInDialog,
                optDialogDescr, optDialogLabelText, false, null, null, null, null);
    }

    /**
     * Constructor for the {@link DoubleOption} class. Don't directly create instances of derived classes. Use the
     * {@link Options#getInstance} method instead.
     *
     * @param name The name of the option. Example: {@code "Solver tolerance"}.
     * @param description The description of the option. Example: {@code
     *      "The absolute tolerance (maximum allowed absolute error) to use for
     *      the solver. Specify \"auto\" to use an automatically determined
     *      tolerance, or an integer or real number to use as absolute
     *      tolerance. [DEFAULT=auto]"}.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. Must not be "*".
     * @param cmdValue The name of the option value, for command line processing. Must not be {@code null}.
     * @param defaultValue The default value of the option. Mostly irrelevant if the option is mandatory. May be
     *     {@code null} to indicate the special value, but only if {@link #hasSpecialValue} is {@code true}.
     * @param minimumValue The minimum allowed value, or {@code null} for no restriction.
     * @param maximumValue The maximum allowed value, or {@code null} for no restriction.
     * @param showInDialog Whether to show the option in option dialogs.
     * @param optDialogDescr The description for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "The absolute tolerance (maximum allowed absolute
     *      error) to use for the solver."}.
     * @param optDialogLabelText The text of the label before the text box for this option, in the option dialog. <b>If
     *     set, text must end with a colon.</b> May be {@code null} to indicate that no text label is needed. Must be
     *     {@code null} if {@link #showInDialog} is {@code
     *      false}. Example: {@code "Tolerance:"}.
     * @param hasSpecialValue Whether the option has a special value, encoded as {@code null}.
     * @param defaultNormalValue The default normal value of the {@link Double} valued option. Must be {@code null} if
     *     and only if {@link #hasSpecialValue} is {@code false}.
     * @param specialValueSyntax The command line syntax for the special value. Must not overlap with the syntax of
     *     doubles. Must be {@code null} if {@link #hasSpecialValue} or {@link #showInDialog} is {@code false}.
     * @param optDialogSpecialText The text of the label of the radio button for the special value. The text should
     *     represent that whenever the radio button is selected, the special value is used. Must be {@code null} if
     *     {@link #hasSpecialValue} is {@code false}, or if {@link #showInDialog} is {@code false}. Example: {@code
     *      "Use automatically determined tolerance"}.
     * @param optDialogNormalText The text of the label of the radio button for the normal value. The text should
     *     represent that whenever the radio button is selected, the special value is not used. Must be {@code null} if
     *     {@link #hasSpecialValue} is {@code false}, or if {@link #showInDialog} is {@code false}. Example: {@code
     *      "Use custom tolerance (integer or real number)"}.
     */
    public DoubleOption(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            Double defaultValue, Double minimumValue, Double maximumValue, boolean showInDialog, String optDialogDescr,
            String optDialogLabelText, boolean hasSpecialValue, Double defaultNormalValue, String specialValueSyntax,
            String optDialogSpecialText, String optDialogNormalText)
    {
        super(name, description, cmdShort, cmdLong, cmdValue, showInDialog);
        Assert.notNull(cmdLong);
        Assert.check(!cmdLong.equals("*"));
        Assert.notNull(cmdValue);
        Assert.implies(defaultValue == null, hasSpecialValue);
        if (defaultValue != null) {
            Assert.check(!Double.isInfinite(defaultValue));
            Assert.check(!Double.isNaN(defaultValue));
        }
        if (defaultValue != null && defaultValue == 0.0) {
            // Avoid -0.0.
            defaultValue = 0.0;
        }
        Assert.ifAndOnlyIf(!showInDialog || !hasSpecialValue, defaultNormalValue == null);
        if (defaultNormalValue != null) {
            Assert.check(!Double.isInfinite(defaultNormalValue));
            Assert.check(!Double.isNaN(defaultNormalValue));
        }
        if (defaultNormalValue != null && defaultNormalValue == 0.0) {
            // Avoid -0.0.
            defaultNormalValue = 0.0;
        }
        if (minimumValue != null) {
            Assert.check(!Double.isInfinite(minimumValue));
            Assert.check(!Double.isNaN(minimumValue));
            if (defaultValue != null) {
                Assert.check(minimumValue <= defaultValue);
            }
        }
        if (maximumValue != null) {
            Assert.check(!Double.isInfinite(maximumValue));
            Assert.check(!Double.isNaN(maximumValue));
            if (defaultValue != null) {
                Assert.check(defaultValue <= maximumValue);
            }
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
        this.optDialogDescr = optDialogDescr;
        this.optDialogLabelText = optDialogLabelText;
        this.hasSpecialValue = hasSpecialValue;
        this.defaultNormalValue = defaultNormalValue;
        this.specialValueSyntax = specialValueSyntax;
        this.optDialogSpecialText = optDialogSpecialText;
        this.optDialogNormalText = optDialogNormalText;
    }

    @Override
    public Double getDefault() {
        return defaultValue;
    }

    @Override
    public Double parseValue(String optName, String value) {
        // Special value handling.
        if (hasSpecialValue) {
            if (value.equals(specialValueSyntax)) {
                return null;
            }
        }

        // Normal value.
        double v;
        try {
            v = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            String msg = fmt("Invalid real number \"%s\".", value);
            throw new InvalidOptionException(msg);
        }

        checkValue(!Double.isInfinite(v), "Infinite values are not allowed.");
        checkValue(!Double.isNaN(v), "NaN is not allowed.");

        if (minimumValue != null) {
            checkValue(v >= minimumValue, v + " < " + minimumValue);
        }
        if (maximumValue != null) {
            checkValue(v <= maximumValue, v + " > " + maximumValue);
        }

        if (v == 0.0) {
            return 0.0; // Special case to avoid -0.0.
        }

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
    public OptionGroup<Double> createOptionGroup(Composite page) {
        // Paranoia checking.
        Assert.check(showInDialog);

        // Construct option group.
        return new DoubleOptionGroup(page);
    }

    /** Option dialog option group for the {@link DoubleOption}. */
    private class DoubleOptionGroup extends OptionGroup<Double> implements SelectionListener {
        /** Radio button for the special value, if applicable. */
        private Button specialButton;

        /** Radio button for the normal value, if applicable. */
        private Button normalButton;

        /** Label before the value text box, if applicable. */
        private Label valueLabel;

        /** The value text box. */
        private Text valueText;

        /**
         * Constructor for the {@link DoubleOptionGroup} class.
         *
         * @param page The options page that is the parent of this option group.
         */
        public DoubleOptionGroup(Composite page) {
            super(page, DoubleOption.this);
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

            valueText = new Text(group, SWT.SINGLE | SWT.BORDER);

            Control[] valueLayout;
            if (optDialogLabelText == null) {
                valueLayout = new Control[] {valueText};
            } else {
                valueLabel = new Label(group, SWT.NULL);
                valueLabel.setText(optDialogLabelText);
                valueLayout = new Control[] {valueLabel, valueText};
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
        public void setToValue(Double value) {
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

            if (value == 0.0) {
                valueText.setText("0");
            } else {
                valueText.setText(value.toString().toLowerCase(Locale.US));
            }
        }

        @Override
        public String[] getCmdLine() {
            if (hasSpecialValue && specialButton.getSelection()) {
                return new String[] {"--" + cmdLong + "=" + specialValueSyntax};
            }

            String arg = valueText.getText();
            return new String[] {"--" + cmdLong + "=" + arg};
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            if (e.widget == specialButton) {
                valueText.setEnabled(false);
            } else if (e.widget == normalButton) {
                valueText.setEnabled(true);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    }
}
