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

import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/** A CIF/BDD discrete variable. Represents a CIF discrete variable in a BDD representation. */
public class CifBddDiscVariable extends CifBddTypedVariable {
    /** The CIF variable that corresponds to this CIF/BDD variable. */
    public final DiscVariable var;

    /**
     * Constructor for the {@link CifBddDiscVariable} class.
     *
     * @param var The CIF discrete variable that corresponds to this CIF/BDD variable.
     * @param type The normalized type of the variable.
     * @param count The number of potential values of the variable.
     * @param lower The lower bound (minimum value) of the variable.
     * @param upper The upper bound (maximum value) of the variable.
     */
    public CifBddDiscVariable(DiscVariable var, CifType type, int count, int lower, int upper) {
        super(var, type, count, lower, upper);
        this.var = var;
    }

    @Override
    public String getKindText() {
        return "discrete variable";
    }
}
