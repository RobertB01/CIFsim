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

package org.eclipse.escet.chi.simulator.options;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionGroup;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** Option for passing the initial seed value. */
public class SeedValueOption extends Option<Integer> {
    /**
     * Returns the initial seed value, or {@code 0} if not provided.
     *
     * @return The initial seed value, or {@code 0} if not provided.
     */
    public static long getSeedValue() {
        Integer value = Options.get(SeedValueOption.class);
        if (value == null) {
            return 0;
        }
        return value;
    }

    /** Constructor for the {@link SeedValueOption} class. */
    public SeedValueOption() {
        super("Initial seed", // name
                "Initial seed value for the random number generators (0 means derive one from the system) "
                        + "[default=0].", // descr
                's', // cmd short
                "seed", // cmd long
                "SEED", // cmd value
                true); // show in dialog
    }

    @Override
    public Integer getDefault() {
        return 0;
    }

    @Override
    public Integer parseValue(String optName, String value) {
        Integer val;
        try {
            val = Integer.parseInt(value, 10);
        } catch (NumberFormatException e) {
            String msg = fmt("Cannot parse \"%s\" to an int number.", value);
            throw new InvalidOptionException(msg, e);
        }
        return val;
    }

    @Override
    public String[] getCmdLine(Object value) {
        if (value == null) {
            return new String[] {};
        }
        int val = (Integer)value;
        return new String[] {"--seed=" + String.valueOf(val)};
    }

    @Override
    public OptionGroup<Integer> createOptionGroup(Composite page) {
        return new OptionGroup<>(page, Options.getInstance(SeedValueOption.class)) {
            Label instanceLabel;

            Text instanceText;

            @Override
            protected void addComponents(Group group) {
                instanceLabel = new Label(group, SWT.NULL);
                instanceLabel.setText("Initial seed value:");
                instanceText = new Text(group, SWT.SINGLE | SWT.BORDER);
                layoutGeneric(new Object[] {new Control[] {instanceLabel, instanceText}}, 0);
            }

            @Override
            public String getDescription() {
                return "The initial seed (\"0\" means that one is generated).";
            }

            @Override
            public void setToValue(Integer value) {
                instanceText.setText(value == null ? "" : String.valueOf(value));
            }

            @Override
            public String[] getCmdLine() {
                return new String[] {"--seed=" + instanceText.getText()};
            }
        };
    }
}
