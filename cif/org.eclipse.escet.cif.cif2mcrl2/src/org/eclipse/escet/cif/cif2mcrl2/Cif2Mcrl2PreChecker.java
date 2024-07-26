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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

/** Class for performing checks whether the specification can be used as input for the CIF to mCRL2 transformation. */
public class Cif2Mcrl2PreChecker {
    /** Found problems in the specification. */
    public List<String> problems = null;

    /** Constructor of the {@link Cif2Mcrl2PreChecker} class. */
    public Cif2Mcrl2PreChecker() {
        // Nothing to do.
    }

    /**
     * Perform checks whether the specification is usable for performing a CIF to mCRL2 transformation.
     *
     * @param spec Specification to check.
     * @throws InvalidInputException If the specification violates the pre-conditions.
     */
    public void checkSpec(Specification spec) {
        problems = list();
        checkGroup(spec);

        if (problems.isEmpty()) {
            return;
        }
        // If we have any problems, the specification is unsupported.
        Collections.sort(problems, Strings.SORTER);
        if (!problems.isEmpty()) {
            String msg = "CIF to mCRL2 transformation failed due to unsatisfied preconditions:\n - "
                    + String.join("\n - ", problems);
            throw new UnsupportedException(msg);
        }
    }

    /**
     * Unfold and check a group.
     *
     * @param grp Group to check and unfold.
     */
    private void checkGroup(Group grp) {
        // Definitions should be eliminated already.
        Assert.check(grp.getDefinitions().isEmpty());
        checkComponent(grp);

        for (Component c: grp.getComponents()) {
            if (c instanceof Automaton) {
                Automaton aut = (Automaton)c;
                checkAutomaton(aut);
                continue;
            } else if (c instanceof Group) {
                Group g = (Group)c;
                checkGroup(g);
                continue;
            }

            // ComponentInst should not happen, as DefInst has been eliminated.
            throw new RuntimeException("Unexpected type of Component.");
        }
    }

    /**
     * Check whether the automaton satisfies all pre-conditions of the CIF to mCRL2 transformation.
     *
     * @param aut Automaton to check.
     */
    private void checkAutomaton(Automaton aut) {
        checkComponent(aut);
    }

    /**
     * Verify that the given component does not have elements that are not supported in the translation.
     *
     * @param comp Component to check.
     */
    private void checkComponent(ComplexComponent comp) {
        // IO declarations should be eliminated already.
        Assert.check(comp.getIoDecls().isEmpty());
    }
}
