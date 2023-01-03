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

package org.eclipse.escet.cif.explorer.runtime;

import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;

/** A collected algebraic variable and its expressions (if they exist). */
public class CollectedAlgVariable {
    /** Algebraic variable reference. */
    public final AlgVariable algVar;

    /**
     * Value of the algebraic variable, for the case it has exactly one expression, else {@code null}. (Non-final to
     * allow setting it after construction.)
     */
    public Expression value;

    /**
     * Value of the variable for the case that each location has an expression, else {@code null}. (Public to allow
     * setting it after construction.)
     */
    public Map<Location, Expression> locMap = null;

    /** Index of the automaton containing the variable, or {@code -1} if not applicable. */
    public final int autIndex;

    /**
     * Constructor of the {@link CollectedAlgVariable} class.
     *
     * @param algVar Collected algebraic variable, or {@code -1} if not applicable.
     * @param autIndex Index of the automaton current location in {@link BaseState#locations}.
     */
    public CollectedAlgVariable(AlgVariable algVar, int autIndex) {
        this.algVar = algVar;
        value = algVar.getValue(); // May assign null.
        this.autIndex = autIndex;
    }

    /**
     * Retrieve the expression for the value of the algebraic variable.
     *
     * @param curLocs Current location of every automaton.
     * @return Expression for computing the value of the algebraic variable.
     */
    public Expression getExpression(Location[] curLocs) {
        return (value != null) ? value : getExpression(curLocs[autIndex]);
    }

    /**
     * Retrieve the expression for the value of the algebraic variable based on the current location.
     *
     * @param curLoc Current location of the containing automaton.
     * @return Expression for computing the value of the algebraic variable.
     */
    public Expression getExpression(Location curLoc) {
        Assert.check(value == null);
        return locMap.get(curLoc);
    }
}
