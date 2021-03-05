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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/** No initial state due to an initialization predicate of a component evaluating to 'false'. */
public class NoInitialInitPredReason extends NoInitialStateReason {
    /** The initialization predicate that evaluated to 'false'. */
    public final Expression pred;

    /**
     * Constructor for the {@link NoInitialInitPredReason} class.
     *
     * @param pred The initialization predicate that evaluated to 'false'.
     */
    public NoInitialInitPredReason(Expression pred) {
        this.pred = pred;
    }

    @Override
    public String getMessage() {
        return fmt("Initialization predicate \"%s\" evaluates to false.", CifTextUtils.exprToStr(pred));
    }
}
