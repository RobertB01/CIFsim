//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.Invariant;

/** No initial state due to an invariant evaluating to 'false'. */
public class NoInitialInvReason extends NoInitialStateReason {
    /** The invariant that evaluated to 'false'. */
    public final Invariant inv;

    /**
     * Constructor for the {@link NoInitialInvReason} class.
     *
     * @param inv The invariant that evaluated to 'false'.
     */
    public NoInitialInvReason(Invariant inv) {
        this.inv = inv;
    }

    @Override
    public String getMessage() {
        return fmt("Invariant \"%s\" in initial state evaluates to false.", CifTextUtils.invToStr(inv, false));
    }
}
