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

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** Input file (remaining arguments) option. Exactly one file expected. */
public class InputFileOption extends Option<List<String>> {
    /**
     * Constructor for the {@link InputFileOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public InputFileOption() {
        super("Input file", "The path to the input file.", null, "*", "FILE", true);
    }

    /**
     * Returns the path of the input file.
     *
     * @return The path of the input file.
     */
    public static String getPath() {
        List<String> files = Options.get(InputFileOption.class);
        return files.get(0);
    }

    /**
     * Returns the path of the input file, modified by removing the given input file extension (if present), and adding
     * the given replacement file extension.
     *
     * @param inExt Input file extension (including the dot).
     * @param replaceExt Replacement file extension (including the dot).
     * @return The modified input file path.
     */
    public static String getDerivedPath(String inExt, String replaceExt) {
        String rslt = getPath();
        if (rslt.endsWith(inExt)) {
            rslt = Strings.slice(rslt, 0, -inExt.length());
        }
        rslt += replaceExt;
        return rslt;
    }

    @Override
    public List<String> getDefault() {
        // No value provided yet (option is mandatory).
        return list();
    }

    @Override
    public List<String> parseValue(String optName, String value) {
        return list(value);
    }

    @Override
    public void verifyValue(List<String> value) {
        if (value.size() == 0) {
            String msg = "Exactly one input file was expected, but no input file was provided.";
            throw new InvalidOptionException(msg);
        }

        if (value.size() != 1) {
            List<String> inputs = copy(value);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, "\"" + inputs.get(i) + "\"");
            }

            String msg = fmt("Exactly one input file was expected, but %d input files were provided: %s.", value.size(),
                    String.join(", ", inputs));
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
