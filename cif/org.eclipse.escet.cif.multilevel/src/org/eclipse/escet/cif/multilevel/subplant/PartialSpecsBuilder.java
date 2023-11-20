//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.subplant;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Construct one or more partial specifications by copying parts from an original specification. */
public class PartialSpecsBuilder {
    /** The supplied original specification. */
    private final Specification origSpec;

    /**
     * Constructor of the {@link PartialSpecsBuilder} class.
     *
     * @param origSpec The supplied original specification.
     */
    public PartialSpecsBuilder(Specification origSpec) {
        this.origSpec = origSpec;
    }

    /**
     * Create a partial specification from scratch that contains at least the plant and requirement objects of the
     * original specification given to the method. In addition, the partial specification may be extended with other
     * elements to make it a valid CIF specification.
     *
     * @param neededObjects Original objects that must at least be copied to the created partial specification.
     * @return The constructed partial specification.
     */
    public Specification createPartialSpecification(List<PositionObject> neededObjects) {
        PartialSpecManager partialMgr = new PartialSpecManager(origSpec);

        // First stage copies the core needed objects. First add the automata, then the other objects. Almost certainly
        // the result will still link to objects from the original specification. Such objects are known as dangling
        // objects and get resolved after he first stage.

        // Copy the automata to avoid getting them added as Group by the manager.
        for (PositionObject element: neededObjects) {
            if (element instanceof Automaton aut) {
                partialMgr.copyAutomatonSkeleton(aut);
            }
        }

        // Copy the other objects that must be in the partial specification.
        for (PositionObject object: neededObjects) {
            if (object instanceof Automaton aut) {
                continue;
            } else if (object instanceof Invariant inv) {
                partialMgr.attachAddedToComponent(inv, partialMgr.deepcloneAndAdd(inv));
            } else if (object instanceof InputVariable inp) {
                partialMgr.attachAddedToComponent(inp, partialMgr.deepcloneAndAdd(inp));
            } else {
                throw new AssertionError("Encountered unexpected needed object to copy: \"" + object + "\".");
            }
        }
        return partialMgr.getPartialSpec();
    }
}
