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

package org.eclipse.escet.cif.cif2yed.options;

import java.util.EnumSet;

import org.eclipse.escet.common.app.framework.options.EnumSetOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Relations kinds option. */
public class RelationKindsOption extends EnumSetOption<RelationKind> {
    /** Constructor for the {@link RelationKindsOption} class. */
    public RelationKindsOption() {
        super(
                // name
                "Relations",

                // description
                "The kinds of relations to include in relations diagrams. "
                        + "Specify comma separated names of the relations. "
                        + "Specify \"event\" for event relations, and/or \"data\" for data relations. "
                        + "By default, all relations are included. Prefix a name with \"+\" to add it on top of "
                        + "the defaults, or with \"-\" to remove it from the defaults.",

                // cmdShort
                'r',

                // cmdLong
                "relations",

                // cmdValue
                "KIND",

                // defaultValue
                EnumSet.allOf(RelationKind.class),

                // showInDialog
                true,

                // optDialogDescr
                "The kinds of relations to include in relations diagrams.",

                // enumClass
                RelationKind.class);
    }

    @Override
    protected String getDialogText(RelationKind kind) {
        return kind.getDescription();
    }

    /**
     * Returns the kinds of relations to include in relations diagrams.
     *
     * @return The kinds of relations to include in relations diagrams.
     */
    public static EnumSet<RelationKind> getKinds() {
        return Options.get(RelationKindsOption.class);
    }
}
