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

/**
 * Automatic mode choice algorithm option. The integer value should be interpreted as follows:
 * <ul>
 * <li>-1 to choose the a random transition with a random seed.</li>
 * <li>-2 to choose the first transition.</li>
 * <li>-3 to choose the last transition.</li>
 * <li>Any non-negative integer to choose a random transition with that number as the seed.</li>
 * </ul>
 */
public class AutoAlgoOption extends Option<Integer> {
    /** Constructor for the {@link AutoAlgoOption} class. */
    public AutoAlgoOption() {
        super("Automatic mode choice algorithm",
                "The automatic mode choice algorithm specifies how the automatic input mode chooses which transition "
                        + "to take. Specify \"random\" (default), \"first\", \"last\", or \"random:NNN\". "
                        + "Random with a number NNN in the interval [0..2^30], uses NNN as the seed of the random "
                        + "generator. If the number is omitted, a random seed is used.",
                'a', "auto-algo", "AUTOALGO", true);
    }

    /**
     * Returns the automatic mode choice algorithm.
     *
     * @return The automatic mode choice algorithm.
     * @see AutoAlgoOption
     */
    public static int getAutoAlgo() {
        return Options.get(AutoAlgoOption.class);
    }

    @Override
    public Integer getDefault() {
        return -1;
    }

    @Override
    public Integer parseValue(String optName, String value) {
        if (value.toLowerCase(Locale.US).equals("random")) {
            return -1;
        }
        if (value.toLowerCase(Locale.US).equals("first")) {
            return -2;
        }
        if (value.toLowerCase(Locale.US).equals("last")) {
            return -3;
        }

        checkValue(value.startsWith("random:"), "Unknown automatic mode choice algorithm.");
        String v = value.substring("random:".length());

        int nr;
        try {
            nr = Integer.parseInt(v);
        } catch (NumberFormatException e) {
            String msg = fmt("Invalid seed value \"%s\".", value);
            throw new InvalidOptionException(msg);
        }

        if (nr < 0 || (1 << 30) < nr) {
            String msg = fmt("Seed value %d is not in the interval [0..2^30].", nr);
            throw new InvalidOptionException(msg);
        }

        return nr;
    }

    @Override
    public String[] getCmdLine(Object value) {
        int nr = (Integer)value;
        if (nr == -1) {
            return new String[] {"--auto-algo=random"};
        }
        if (nr == -2) {
            return new String[] {"--auto-algo=first"};
        }
        if (nr == -3) {
            return new String[] {"--auto-algo=last"};
        }
        Assert.check(nr >= 0);
        Assert.check(nr <= (1 << 30));
        return new String[] {"--auto-algo=random:" + nr};
    }

    @Override
    public OptionGroup<Integer> createOptionGroup(Composite page) {
        return new AutoAlgoOptionGroup(page);
    }

    /** Option dialog option group for the {@link AutoAlgoOption}. */
    private class AutoAlgoOptionGroup extends OptionGroup<Integer> implements SelectionListener {
        /** Radio button for 'first'. */
        Button firstButton;

        /** Radio button for 'last'. */
        Button lastButton;

        /** Radio button for 'random with random seed'. */
        Button randomButton;

        /** Radio button for the 'random with specific seed'. */
        Button seedButton;

        /** Label before the seed value spinner. */
        Label seedLabel;

        /** Seed value spinner. */
        Spinner seedSpinner;

        /**
         * Constructor for the {@link AutoAlgoOptionGroup} class.
         *
         * @param page The options page that is the parent of this option group.
         */
        public AutoAlgoOptionGroup(Composite page) {
            super(page, AutoAlgoOption.this);
        }

        @Override
        protected void addComponents(Group group) {
            firstButton = new Button(group, SWT.RADIO);
            lastButton = new Button(group, SWT.RADIO);
            randomButton = new Button(group, SWT.RADIO);
            seedButton = new Button(group, SWT.RADIO);

            firstButton.setText("First transition");
            lastButton.setText("Last transition");
            randomButton.setText("Random transition (random seed)");
            seedButton.setText("Random transition (specific seed)");

            firstButton.addSelectionListener(this);
            lastButton.addSelectionListener(this);
            randomButton.addSelectionListener(this);
            seedButton.addSelectionListener(this);

            seedLabel = new Label(group, SWT.NULL);
            seedLabel.setText("Seed:");
            seedSpinner = new Spinner(group, SWT.BORDER);
            seedSpinner.setMinimum(0);
            seedSpinner.setMaximum(1 << 30);

            layoutGeneric(
                    new Object[]
                    {firstButton, lastButton, randomButton, seedButton, new Control[] {seedLabel, seedSpinner}});
        }

        @Override
        public String getDescription() {
            return "The automatic mode choice algorithm specifies how the automatic input mode chooses which "
                    + "transition to take.";
        }

        @Override
        public void setToValue(Integer value) {
            // Select the correct button.
            Button button;

            if (value == -1) {
                button = randomButton;
            } else if (value == -2) {
                button = firstButton;
            } else if (value == -3) {
                button = lastButton;
            } else {
                button = seedButton;
            }

            button.setSelection(true);

            Event event = new Event();
            event.widget = button;
            widgetSelected(new SelectionEvent(event));

            // Set the spinner value.
            int spinnerValue = (button != seedButton) ? 0 : value;
            Assert.check(spinnerValue >= 0);
            Assert.check(spinnerValue <= (1 << 30));
            seedSpinner.setSelection(spinnerValue);
        }

        @Override
        public String[] getCmdLine() {
            if (randomButton.getSelection()) {
                return new String[] {"--auto-algo=random"};
            }
            if (firstButton.getSelection()) {
                return new String[] {"--auto-algo=first"};
            }
            if (lastButton.getSelection()) {
                return new String[] {"--auto-algo=last"};
            }
            Assert.check(seedButton.getSelection());
            return new String[] {"--auto-algo=random:" + seedSpinner.getSelection()};
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            seedSpinner.setEnabled(e.widget == seedButton);
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    }
}
