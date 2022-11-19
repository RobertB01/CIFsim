//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.varorder.helper;

import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisDiscVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisInputVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisLocPtrVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Automatic variable ordering hyper-edge creator. */
abstract class HyperEdgeCreator {
    /**
     * Creates and returns hyper-edges for the given CIF specification.
     *
     * @param spec The CIF specification. Must not be modified.
     * @param variables The synthesis variables.
     * @return The hyper-edges.
     */
    abstract List<BitSet> getHyperEdges(Specification spec, List<SynthesisVariable> variables);

    /**
     * Add a hyper-edge for the given CIF variable objects. Creating and adding a hyper-edge is skipped if no CIF
     * variable objects are provided.
     *
     * @param variables The synthesis variables in the order in which they occur in a hyper-edge.
     * @param edgeVars The CIF variable objects for which to create a new hyper-edge. This must be a subset of the
     *     variables represented by {@code variables}.
     * @param hyperEdges The collection of hyper-edges to which to add the new hyper-edge.
     */
    protected void addHyperEdge(List<SynthesisVariable> variables, Collection<PositionObject> edgeVars,
            List<BitSet> hyperEdges)
    {
        // Skip creation of hyper-edges without any variables.
        if (edgeVars.isEmpty()) {
            return;
        }

        // Create bit set.
        BitSet hyperEdge = new BitSet(variables.size());
        for (PositionObject var: edgeVars) {
            int matchIdx = -1;
            for (int i = 0; i < variables.size(); i++) {
                // Get synthesis variable.
                SynthesisVariable synthVar = variables.get(i);
                Assert.notNull(synthVar);

                // Check for matching variable.
                boolean match = false;
                if (synthVar instanceof SynthesisDiscVariable) {
                    SynthesisDiscVariable sdv = (SynthesisDiscVariable)synthVar;
                    if (sdv.var == var) {
                        match = true;
                    }
                } else if (synthVar instanceof SynthesisInputVariable) {
                    SynthesisInputVariable siv = (SynthesisInputVariable)synthVar;
                    if (siv.var == var) {
                        match = true;
                    }
                } else if (synthVar instanceof SynthesisLocPtrVariable) {
                    SynthesisLocPtrVariable slpv = (SynthesisLocPtrVariable)synthVar;
                    if (slpv.aut == var) {
                        match = true;
                    }
                } else {
                    throw new RuntimeException("Unknown synthesis variable: " + synthVar);
                }

                // Done if match found.
                if (match) {
                    matchIdx = i;
                    break;
                }
            }

            // Must have found a match.
            Assert.check(matchIdx >= 0, var);
            hyperEdge.set(matchIdx);
        }

        // Add hyper-edge.
        hyperEdges.add(hyperEdge);
    }
}
