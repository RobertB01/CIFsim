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

package org.eclipse.escet.cif.datasynth.spec;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.common.java.Assert;

/**
 * A CIF/BDD location pointer variable. Represents (state of) a CIF automaton in a BDD representation.
 *
 * <p>
 * Location pointer variables are only created for CIF automata with at least two locations.
 * </p>
 */
public class CifBddLocPtrVariable extends CifBddVariable {
    /** The automaton for which this CIF/BDD variable is a location pointer. */
    public final Automaton aut;

    /**
     * A dummy, internally-created CIF discrete variable that corresponds to this CIF/BDD variable. Does not have a data
     * type.
     */
    public final DiscVariable var;

    /**
     * Constructor for the {@link CifBddLocPtrVariable} class.
     *
     * @param aut The automaton for which this CIF/BDD variable is a location pointer.
     * @param var A dummy, internally-created CIF discrete variable that corresponds to this CIF/BDD variable. Does not
     *     have a data type.
     */
    public CifBddLocPtrVariable(Automaton aut, DiscVariable var) {
        super(aut, aut.getLocations().size(), 0, aut.getLocations().size() - 1);

        this.aut = aut;
        this.var = var;

        Assert.check(var.getType() == null);
        Assert.check(aut.getLocations().size() > 1);
    }

    @Override
    public int getDomainSize() {
        // [0..n-1] for automaton with 'n' locations.
        Assert.check(count > 1);
        Assert.check(count == aut.getLocations().size());
        return count;
    }

    @Override
    public String getKindText() {
        return "location pointer";
    }

    @Override
    public String getTypeText() {
        return null;
    }

    @Override
    protected String toStringInternal() {
        return fmt("location pointer for automaton \"%s\"", name);
    }
}
