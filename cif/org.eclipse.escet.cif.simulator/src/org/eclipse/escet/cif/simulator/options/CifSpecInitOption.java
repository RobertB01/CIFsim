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

import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionGroup;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/** CIF specification initialization option. */
public class CifSpecInitOption extends Option<List<String>> {
    /** Constructor for the {@link CifSpecInitOption} class. */
    public CifSpecInitOption() {
        super("CIF specification initialization",
                "Specify an initial value for a single discrete variable or input variable (\"var:value\", with "
                        + "\"var\" the absolute name of the variable and \"value\" a CIF literal), or an initial "
                        + "location for a single automaton (\"aut:loc\", with \"aut\" the absolute name of the "
                        + "automaton and \"loc\" a name of one of its locations).",
                null, "init", "INIT", true);
    }

    /**
     * Returns the user specified initializations.
     *
     * @return The user specified initializations.
     */
    public static List<String> getInits() {
        return Options.get(CifSpecInitOption.class);
    }

    @Override
    public List<String> getDefault() {
        return list();
    }

    @Override
    public List<String> parseValue(String optName, String value) {
        return concat(getInits(), value);
    }

    @Override
    public String[] getCmdLine(Object value) {
        @SuppressWarnings("unchecked")
        List<String> inits = (List<String>)value;
        String[] rslt = new String[inits.size()];
        for (int i = 0; i < rslt.length; i++) {
            rslt[i] = "--init=" + inits.get(i);
        }
        return rslt;
    }

    @Override
    public OptionGroup<List<String>> createOptionGroup(Composite page) {
        return new OptionGroup<>(page, Options.getInstance(CifSpecInitOption.class)) {
            Text initText;

            @Override
            protected void addComponents(Group group) {
                initText = new Text(group, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

                FormData grpData = new FormData();
                grpData.left = new FormAttachment(0, 0);
                grpData.right = new FormAttachment(100, 0);
                grpData.top = new FormAttachment(descrLabel, 0);
                grpData.bottom = new FormAttachment(initText, 100);
                initText.setLayoutData(grpData);
            }

            @Override
            public String getDescription() {
                return "The initializations, one per line. Each line specifies an initial value for a single discrete "
                        + "variable or input variable (\"var:value\", with \"var\" the absolute name of the variable "
                        + "and \"value\" a CIF literal), or an initial location for a single automaton "
                        + "(\"aut:loc\", with \"aut\" the absolute name of the automaton and \"loc\" a name of one of "
                        + "its locations).";
            }

            @Override
            public void setToValue(List<String> value) {
                // Join initializations together, one per line.
                String[] inits = value.toArray(new String[0]);
                initText.setText(String.join(Strings.NL, inits));
            }

            @Override
            public String[] getCmdLine() {
                // Get initializations, one per line. Skip empty lines.
                String txt = initText.getText().replace("\r", "");
                List<String> inits = list();
                for (String init: txt.split("\n")) {
                    if (!init.isEmpty()) {
                        inits.add("--init=" + init);
                    }
                }
                return inits.toArray(new String[0]);
            }
        };
    }
}
