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

package org.eclipse.escet.cif.simulator.options;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.Locale;

import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionGroup;
import org.eclipse.escet.common.app.framework.options.Options;
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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/** Automatic mode time transition duration option. */
public class AutoTimeDurationOption extends Option<AutoTimeDuration> {
    /** Constructor for the {@link AutoTimeDurationOption} class. */
    public AutoTimeDurationOption() {
        super("Automatic mode time transition duration",
                "The automatic mode time transition duration specifies how the automatic input mode chooses the "
                        + "duration of a time transition that is to be taken. Specify \"max\" (default), to always "
                        + "choose the maximum allowed duration. Specify \"random:UPPER:NNN\" to randomly select "
                        + "durations using a uniform distribution with upper bound UPPER, and initial random "
                        + "generator seed NNN. UPPER most be a positive integer or real value, NNN must be in the "
                        + "interval [0..2^30]. The initial seed may be omitted to use a random seed.",
                null, "auto-time", "AUTOTIME", true);
    }

    /**
     * Returns the automatic mode time transition duration option value.
     *
     * @return The automatic mode time transition duration option value.
     * @see AutoTimeDurationOption
     */
    public static AutoTimeDuration getAutoTimeDuration() {
        return Options.get(AutoTimeDurationOption.class);
    }

    @Override
    public AutoTimeDuration getDefault() {
        return new AutoTimeDuration(false, null, null);
    }

    @Override
    public AutoTimeDuration parseValue(String optName, String value) {
        // Maximum.
        if (value.toLowerCase(Locale.US).equals("max")) {
            return new AutoTimeDuration(false, null, null);
        }

        // Random.
        checkValue(value.startsWith("random:"), "Unknown automatic mode time transition duration.");
        String v = value.substring("random:".length());

        // Get uniform distribution upper bound.
        int idx = v.indexOf(':');
        String upperTxt = (idx == -1) ? v : v.substring(0, idx);
        double upper;
        try {
            upper = Double.parseDouble(upperTxt);
        } catch (NumberFormatException e) {
            String msg = fmt("Invalid uniform distribution upper bound value \"%s\".", upperTxt);
            throw new InvalidOptionException(msg);
        }

        if (Double.isNaN(upper)) {
            String msg = "Uniform distribution upper bound value is NaN.";
            throw new InvalidOptionException(msg);
        }
        if (Double.isInfinite(upper)) {
            String msg = "Uniform distribution upper bound value is infinite.";
            throw new InvalidOptionException(msg);
        }
        if (upper <= 0) {
            String msg = fmt("Uniform distribution upper bound value %s is not a positive value.", upper);
            throw new InvalidOptionException(msg);
        }

        // Get initial distribution seed.
        Integer seed = null;
        if (idx != -1) {
            String seedTxt = v.substring(idx + 1);
            try {
                seed = Integer.parseInt(seedTxt);
            } catch (NumberFormatException e) {
                String msg = fmt("Invalid seed value \"%s\".", seedTxt);
                throw new InvalidOptionException(msg);
            }

            if (seed < 0 || (1 << 30) < seed) {
                String msg = fmt("Seed value %d is not in the interval [0..2^30].", seed);
                throw new InvalidOptionException(msg);
            }
        }

        // Return option value.
        return new AutoTimeDuration(true, upper, seed);
    }

    @Override
    public String[] getCmdLine(Object value) {
        AutoTimeDuration duration = (AutoTimeDuration)value;
        if (!duration.random) {
            return new String[] {"--auto-time=max"};
        }

        String arg = "--auto-time=random:";
        arg += str(duration.upper).toLowerCase(Locale.US);
        if (duration.seed != null) {
            arg += ":" + str(duration.seed);
        }
        return new String[] {arg};
    }

    @Override
    public OptionGroup<AutoTimeDuration> createOptionGroup(Composite page) {
        return new AutoTimeDurationOptionGroup(page);
    }

    /** Option dialog option group for the {@link AutoTimeDurationOption}. */
    private class AutoTimeDurationOptionGroup extends OptionGroup<AutoTimeDuration> implements SelectionListener {
        /** Radio button for 'maximum duration'. */
        Button maxButton;

        /** Radio button for 'random duration (random seed)'. */
        Button randomButton;

        /** Radio button for 'random duration (specific seed)'. */
        Button seedButton;

        /** Label before the upper bound value text box (random seed). */
        Label randomUpperLabel;

        /** Upper bound value text box (random seed). */
        Text randomUpperText;

        /** Label before the upper bound value text box (specific seed). */
        Label seedUpperLabel;

        /** Upper bound value text box (specific seed). */
        Text seedUpperText;

        /** Label before the seed value spinner. */
        Label seedLabel;

        /** Seed value spinner. */
        Spinner seedSpinner;

        /**
         * Constructor for the {@link AutoTimeDurationOptionGroup} class.
         *
         * @param page The options page that is the parent of this option group.
         */
        public AutoTimeDurationOptionGroup(Composite page) {
            super(page, AutoTimeDurationOption.this);
        }

        @Override
        protected void addComponents(Group group) {
            maxButton = new Button(group, SWT.RADIO);
            randomButton = new Button(group, SWT.RADIO);
            seedButton = new Button(group, SWT.RADIO);

            maxButton.setText("Maximum allowed duration");
            randomButton.setText("Random duration (random seed)");
            seedButton.setText("Random duration (specific seed)");

            maxButton.addSelectionListener(this);
            randomButton.addSelectionListener(this);
            seedButton.addSelectionListener(this);

            randomUpperLabel = new Label(group, SWT.NULL);
            randomUpperLabel.setText("Upper bound:");
            randomUpperText = new Text(group, SWT.SINGLE | SWT.BORDER);

            seedUpperLabel = new Label(group, SWT.NULL);
            seedUpperLabel.setText("Upper bound:");
            seedUpperText = new Text(group, SWT.SINGLE | SWT.BORDER);

            seedLabel = new Label(group, SWT.NULL);
            seedLabel.setText("Seed:");
            seedSpinner = new Spinner(group, SWT.BORDER);
            seedSpinner.setMinimum(0);
            seedSpinner.setMaximum(1 << 30);

            layoutGeneric(
                    new Object[]
                    {maxButton, randomButton, new Control[] {randomUpperLabel, randomUpperText}, seedButton,
                            new Control[]
                            {seedUpperLabel, seedUpperText}, new Control[] {seedLabel, seedSpinner}});
        }

        @Override
        public String getDescription() {
            return "The automatic mode time transition duration specifies how the automatic input mode chooses the "
                    + "duration of a time transition that is to be taken. When selecting a random duration, a uniform "
                    + "distribution is used, with a user-specified upper bound.";
        }

        @Override
        public void setToValue(AutoTimeDuration value) {
            // Select the correct button.
            Button button;

            if (!value.random) {
                button = maxButton;
            } else if (value.seed == null) {
                button = randomButton;
            } else {
                button = seedButton;
            }

            button.setSelection(true);

            Event event = new Event();
            event.widget = button;
            widgetSelected(new SelectionEvent(event));

            // Set the upper bound values.
            String upperTxt;
            if (value.upper != null) {
                upperTxt = str(value.upper).toLowerCase(Locale.US);
            } else {
                upperTxt = "10";
            }
            randomUpperText.setText(upperTxt);
            seedUpperText.setText(upperTxt);

            // Set the seed value.
            int seed = (value.seed == null) ? 0 : value.seed;
            Assert.check(seed >= 0);
            Assert.check(seed <= (1 << 30));
            seedSpinner.setSelection(seed);
        }

        @Override
        public String[] getCmdLine() {
            if (maxButton.getSelection()) {
                return new String[] {"--auto-time=max"};
            }
            if (randomButton.getSelection()) {
                return new String[] {"--auto-time=random:" + randomUpperText.getText()};
            }
            Assert.check(seedButton.getSelection());
            return new String[] {
                    "--auto-time=random:" + seedUpperText.getText() + ":" + str(seedSpinner.getSelection())};
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            randomUpperText.setEnabled(e.widget == randomButton);
            seedUpperText.setEnabled(e.widget == seedButton);
            seedSpinner.setEnabled(e.widget == seedButton);
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    }
}
