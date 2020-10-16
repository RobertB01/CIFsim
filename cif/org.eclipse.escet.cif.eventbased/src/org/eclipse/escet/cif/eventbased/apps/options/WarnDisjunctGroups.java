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

package org.eclipse.escet.cif.eventbased.apps.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to enable warning about groups of automata without shared events outside the group. */
@SuppressWarnings("javadoc")
public class WarnDisjunctGroups extends BooleanOption {
    private static final String NAME = "Disjunct groups check";

    private static final String OPTDIALOGDESCR = "Warn about groups of automata that have no shared events outside their group.";

    private static final String DESCRIPTION = OPTDIALOGDESCR + " [DEFAULT=NO]";

    private static final Character CMDSHORT = null;

    private static final String CMDLONG = "warn-disjunct-groups";

    private static final String CMDVALUE = "BOOL";

    private static final boolean DEFAULTVALUE = false;

    private static final boolean SHOWINDIALOG = true;

    private static final String OPTDIALOGCHECKBOXTEXT = "Enable warning";

    /** Constructor of the {@link WarnDisjunctGroups} class. */
    public WarnDisjunctGroups() {
        super(NAME, DESCRIPTION, CMDSHORT, CMDLONG, CMDVALUE, DEFAULTVALUE, SHOWINDIALOG, OPTDIALOGDESCR,
                OPTDIALOGCHECKBOXTEXT);
    }

    /**
     * Returns whether the option is enabled.
     *
     * @return A value indicating whether the option is enabled.
     */
    public static boolean isEnabled() {
        return Options.get(WarnDisjunctGroups.class);
    }
}
