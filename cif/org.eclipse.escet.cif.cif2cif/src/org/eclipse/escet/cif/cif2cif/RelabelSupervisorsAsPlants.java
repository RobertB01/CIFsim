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

package org.eclipse.escet.cif.cif2cif;

import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/** In-place transformation that relabels supervisor automata and invariants as plants. */
public class RelabelSupervisorsAsPlants extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    @Override
    protected void preprocessAutomaton(Automaton aut) {
        if (aut.getKind() == SupKind.SUPERVISOR) {
            aut.setKind(SupKind.PLANT);
        }
    }

    @Override
    protected void preprocessInvariant(Invariant inv) {
        if (inv.getSupKind() == SupKind.SUPERVISOR) {
            inv.setSupKind(SupKind.PLANT);
        }
    }
}
