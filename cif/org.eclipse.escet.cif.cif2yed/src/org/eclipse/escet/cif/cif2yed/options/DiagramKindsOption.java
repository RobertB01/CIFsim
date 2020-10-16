//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2yed.options;

import java.util.EnumSet;

import org.eclipse.escet.common.app.framework.options.EnumSetOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Diagram kinds option. */
public class DiagramKindsOption extends EnumSetOption<DiagramKind> {
    /** Constructor for the {@link DiagramKindsOption} class. */
    public DiagramKindsOption() {
        super(
                // name
                "Diagram kinds",

                // description
                "The kinds of diagrams to generate. Specify comma separated names of the diagram kinds. "
                        + "Specify \"model\" for a model diagram, and/or \"relations\" for a relations diagram. "
                        + "By default, all diagrams are generated. "
                        + "Prefix a name with \"+\" to add it on top of the defaults, "
                        + "or with \"-\" to remove it from the defaults.",

                // cmdShort
                'k',

                // cmdLong
                "diagram-kinds",

                // cmdValue
                "KIND",

                // defaultValue
                EnumSet.allOf(DiagramKind.class),

                // showInDialog
                true,

                // optDialogDescr
                "The kinds of diagrams to generate.",

                // enumClass
                DiagramKind.class);
    }

    @Override
    protected String getDialogText(DiagramKind kind) {
        return kind.getDescription();
    }

    /**
     * Returns the kinds of diagrams to generate.
     *
     * @return The kinds of diagrams to generate.
     */
    public static EnumSet<DiagramKind> getKinds() {
        return Options.get(DiagramKindsOption.class);
    }
}
