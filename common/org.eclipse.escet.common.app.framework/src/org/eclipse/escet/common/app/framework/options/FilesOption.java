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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/** Input files (remaining arguments) option. At least one file is expected. */
public class FilesOption extends Option<List<String>> {
    /**
     * Constructor for the {@link FilesOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public FilesOption() {
        super("Input files", "The absolute or relative local file system paths to the input files.", null, "*", "FILES",
                true);
    }

    /**
     * Returns the paths of the input files.
     *
     * @return The paths of the input files.
     */
    public static List<String> getPaths() {
        return Options.get(FilesOption.class);
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
        if (value.size() < 1) {
            String msg = fmt("At least one input file expected, but none provided.", value.size());
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
        return new OptionGroup<>(page, Options.getInstance(FilesOption.class)) {
            Text filesText;

            @Override
            protected void addComponents(Group group) {
                filesText = new Text(group, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

                FormData grpData = new FormData();
                grpData.left = new FormAttachment(0, 0);
                grpData.right = new FormAttachment(100, 0);
                grpData.top = new FormAttachment(descrLabel, 0);
                grpData.bottom = new FormAttachment(filesText, 100);
                filesText.setLayoutData(grpData);
            }

            @Override
            public String getDescription() {
                return "The input file paths, one per line.";
            }

            @Override
            public void setToValue(List<String> value) {
                // Join files together, one per line.
                String[] files = value.toArray(new String[0]);
                filesText.setText(String.join(Strings.NL, files));
            }

            @Override
            public String[] getCmdLine() {
                // Get files, one per line. Skip empty lines. Don't trim lines,
                // to allow for files starting/ending with whitespace.
                String txt = filesText.getText().replace("\r", "");
                List<String> files = list();
                for (String file: txt.split("\n")) {
                    if (!file.isEmpty()) {
                        files.add(file);
                    }
                }
                return files.toArray(new String[0]);
            }
        };
    }
}
