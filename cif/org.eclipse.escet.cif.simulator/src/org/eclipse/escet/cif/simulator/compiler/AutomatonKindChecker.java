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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.common.java.Assert;

/** Automaton kind checker. Warns about simulating requirements, etc. */
public class AutomatonKindChecker {
    /** Constructor for the {@link AutomatonKindChecker} class. */
    private AutomatonKindChecker() {
        // Static class.
    }

    /**
     * Recursively checks the component for requirement automata.
     *
     * @param comp The component.
     */
    public static void checkKinds(ComplexComponent comp) {
        if (comp instanceof Automaton) {
            // Warn about requirement automata.
            if (((Automaton)comp).getKind() == SupKind.REQUIREMENT) {
                warn("Automaton \"%s\" is a requirement, but will be simulated as a plant.", getAbsName(comp));
            }
        } else {
            // Check recursively.
            Assert.check(comp instanceof Group);
            for (Component child: ((Group)comp).getComponents()) {
                checkKinds((ComplexComponent)child);
            }
        }
    }
}
