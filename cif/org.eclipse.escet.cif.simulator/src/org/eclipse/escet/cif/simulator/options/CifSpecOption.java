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

package org.eclipse.escet.cif.simulator.options;

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionGroup;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** CIF specification (single remaining argument) option. */
public class CifSpecOption extends Option<List<String>> {
    /** Constructor for the {@link CifSpecOption} class. */
    public CifSpecOption() {
        super("CIF specification",
                "The absolute or relative local file system path to the CIF specification (\".cif\" file) or compiled "
                        + "code file (\".cifcode\" file) to simulate.",
                null, "*", "CIFSPEC", true);
    }

    /**
     * Returns the absolute or relative local file system path to the CIF specification (".cif" file) or compiled code
     * file (".cifcode" file) to simulate.
     *
     * @return The absolute or relative local file system path.
     */
    public static String getCifSpecPath() {
        List<String> files = Options.get(CifSpecOption.class);
        return first(files);
    }

    /**
     * Returns the absolute or relative local file system path to the CIF specification (".cif" file) or compiled code
     * file (".cifcode" file) to simulate, modified by removing the {@code ".cif"} or {@code ".cifcode"} file extension
     * (if present), and adding the given replacement file extension.
     *
     * @param replaceExt Replacement file extension (including the dot).
     * @return The modified input file path.
     */
    public static String getDerivedPath(String replaceExt) {
        String rslt = getCifSpecPath();
        if (rslt.endsWith(".cif")) {
            rslt = Strings.slice(rslt, 0, -".cif".length());
        } else if (rslt.endsWith(".cifcode")) {
            rslt = Strings.slice(rslt, 0, -".cifcode".length());
        }
        rslt += replaceExt;
        return rslt;
    }

    @Override
    public List<String> getDefault() {
        return list();
    }

    @Override
    public List<String> parseValue(String optName, String value) {
        return list(value);
    }

    @Override
    public void verifyValue(List<String> value) {
        // No file provided.
        if (value.size() == 0) {
            String msg = "The CIF simulator expects exactly one CIF specification file (\".cif\" file) or compiled "
                    + "code file (\".cifcode\" file) to simulate, but no such file was provided.";
            throw new InvalidOptionException(msg);
        }

        // Too many files provided.
        if (value.size() != 1) {
            List<String> inputs = copy(value);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, "\"" + inputs.get(i) + "\"");
            }

            String msg = fmt(
                    "The CIF simulator expects exactly one CIF specification file (\".cif\" file) or compiled code "
                            + "file (\".cifcode\" file) to simulate, but %d files were provided: %s.",
                    value.size(), String.join(", ", inputs));
            throw new InvalidOptionException(msg);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public String[] getCmdLine(Object value) {
        List<String> files = (List<String>)value;
        return files.toArray(new String[] {});
    }

    @Override
    public OptionGroup<List<String>> createOptionGroup(Composite page) {
        return new OptionGroup<>(page, Options.getInstance(CifSpecOption.class)) {
            Label specPathLabel;

            Text specPathText;

            @Override
            protected void addComponents(Group group) {
                specPathLabel = new Label(group, SWT.NULL);
                specPathLabel.setText("Path:");
                specPathText = new Text(group, SWT.SINGLE | SWT.BORDER);
                layoutGeneric(new Object[] {new Control[] {specPathLabel, specPathText}}, 0);
            }

            @Override
            public String getDescription() {
                return "The absolute or relative local file system path to the CIF specification (\".cif\" file) "
                        + "or compiled code file (\".cifcode\" file) to simulate.";
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
                specPathText.setText(value.isEmpty() ? "" : first(value));
            }

            @Override
            public String[] getCmdLine() {
                String path = specPathText.getText();
                return path.isEmpty() ? new String[] {} : new String[] {path};
            }
        };
    }
}
