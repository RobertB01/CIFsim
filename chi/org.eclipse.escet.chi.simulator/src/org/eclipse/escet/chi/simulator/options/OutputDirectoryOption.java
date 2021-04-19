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

import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionGroup;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** Option class for specifying the output directory of the compiler. */
public class OutputDirectoryOption extends Option<String> {
    /**
     * Returns the path of the output directory, or {@code null} if not provided.
     *
     * @return The path of the output directory, or {@code null}.
     */
    public static String getPath() {
        return Options.get(OutputDirectoryOption.class);
    }

    /** Constructor for the {@link OutputDirectoryOption} class. */
    public OutputDirectoryOption() {
        super("Directory", // name
                "Output directory for generated Java files", // desc
                null, // cmd short
                "directory", // cmd long
                "DIR", // cmd Value
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
        return new String[] {"--directory=" + (String)value};
    }

    @Override
    public OptionGroup<String> createOptionGroup(Composite page) {
        return new OptionGroup<>(page, Options.getInstance(OutputDirectoryOption.class)) {
            Label fileLabel;

            Text fileText;

            @Override
            protected void addComponents(Group group) {
                fileLabel = new Label(group, SWT.NULL);
                fileLabel.setText("Directory path:");
                fileText = new Text(group, SWT.SINGLE | SWT.BORDER);
                layoutGeneric(new Object[] {new Control[] {fileLabel, fileText}}, 0);
            }

            @Override
            public String getDescription() {
                return "The output directory path.";
            }

            @Override
            public void setToValue(String value) {
                fileText.setText(value == null ? "" : value);
            }

            @Override
            public String[] getCmdLine() {
                return new String[] {"--directory=" + fileText.getText()};
            }
        };
    }
}
