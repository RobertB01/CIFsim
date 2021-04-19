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

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

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

/** Input file (remaining arguments) option. Expect either 0 or 1 input file argument. */
public class InputFileOption extends Option<List<String>> {
    /**
     * Constructor for the {@link InputFileOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public InputFileOption() {
        super("Input file", "The path to the input file.", null, "*", "FILE", true);
    }

    /**
     * Returns the path of the input file if provided.
     *
     * @return The path of the input file, or {@code null} if no path was provided.
     */
    public static String getPath() {
        List<String> files = Options.get(InputFileOption.class);
        return files.isEmpty() ? null : files.get(0);
    }

    @Override
    public List<String> getDefault() {
        // No value provided yet.
        return list();
    }

    @Override
    public List<String> parseValue(String optName, String value) {
        return list(value);
    }

    @Override
    public void verifyValue(List<String> value) {
        // 0 or 1 arguments are allowed.
        if (value.size() > 1) {
            String msg = "";
            for (String s: value) {
                if (!msg.isEmpty()) {
                    msg += ", ";
                }
                msg += "\"" + s + "\"";
            }
            msg = fmt("At most one input file was expected, but %d input files were provided: %s.", value.size(), msg);
            throw new InvalidOptionException(msg);
        }
    }

    @Override
    public String[] getCmdLine(Object value) {
        @SuppressWarnings("unchecked")
        List<String> files = (List<String>)value;
        return files.toArray(new String[] {});
    }

    @Override
    public OptionGroup<List<String>> createOptionGroup(Composite page) {
        return new OptionGroup<>(page, Options.getInstance(InputFileOption.class)) {
            Label fileLabel;

            Text fileText;

            @Override
            protected void addComponents(Group group) {
                fileLabel = new Label(group, SWT.NULL);
                fileLabel.setText("Input file path:");
                fileText = new Text(group, SWT.SINGLE | SWT.BORDER);
                layoutGeneric(new Object[] {new Control[] {fileLabel, fileText}}, 0);
            }

            @Override
            public String getDescription() {
                return "The input file path.";
            }

            @Override
            public void setToValue(List<String> value) {
                // Since we display the file name in a single line text box,
                // we can't handle multiple file names. It will be verified
                // later, but since we can't handle it, we already check it
                // here. No file name is OK, as it can be provided via the
                // option dialog.
                if (value.size() > 1) {
                    verifyValue(value);
                }
                fileText.setText(value.isEmpty() ? "" : first(value));
            }

            @Override
            public String[] getCmdLine() {
                String path = fileText.getText();
                return path.isEmpty() ? new String[] {} : new String[] {path};
            }
        };
    }
}
