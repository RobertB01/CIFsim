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

package org.eclipse.escet.chi.simulator.options;

import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionGroup;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** Option for passing the model or xper instantiation text. */
public class StartupInstanceOption extends Option<String> {
    /** Constant describing the intended use of the option. */
    static final String DESCRIPTION = "The startup instantiation text, can be either a model or an xper "
            + "instance for example \"M(1.5, [2,3])\".";

    /**
     * Returns the startup instantiation text, or {@code null} if not provided.
     *
     * @return The startup instantiation text, or {@code null}.
     */
    public static String getStartupInstanceText() {
        String value = Options.get(StartupInstanceOption.class);
        if (value != null && value.isEmpty()) {
            value = null;
        }
        return value;
    }

    /** Constructor for the {@link StartupInstanceOption} class. */
    public StartupInstanceOption() {
        super("Startup", // name
                DESCRIPTION, // descr
                'i', // cmd short
                "instance", // cmd long
                "INSTANCE", // cmd value
                true); // show in dialog
    }

    @Override
    public String getDefault() {
        return null;
    }

    @Override
    public String parseValue(String optName, String value) {
        return value;
    }

    @Override
    public String[] getCmdLine(Object value) {
        if (value == null) {
            return new String[] {};
        }
        return new String[] {"--instance=" + (String)value};
    }

    @Override
    public OptionGroup<String> createOptionGroup(Composite page) {
        return new OptionGroup<>(page, Options.getInstance(StartupInstanceOption.class)) {
            Label instanceLabel;

            Text instanceText;

            @Override
            protected void addComponents(Group group) {
                instanceLabel = new Label(group, SWT.NULL);
                instanceLabel.setText("Instance:");
                instanceText = new Text(group, SWT.SINGLE | SWT.BORDER);
                layoutGeneric(new Object[] {new Control[] {instanceLabel, instanceText}}, 0);
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public void setToValue(String value) {
                instanceText.setText(value == null ? "" : value);
            }

            @Override
            public String[] getCmdLine() {
                return new String[] {"--instance=" + instanceText.getText()};
            }
        };
    }
}
