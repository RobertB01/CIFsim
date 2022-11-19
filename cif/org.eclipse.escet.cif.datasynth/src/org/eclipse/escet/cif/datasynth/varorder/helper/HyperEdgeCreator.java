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
import java.util.Set;

import org.eclipse.escet.cif.datasynth.spec.SynthesisDiscVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisInputVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisLocPtrVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

public class HyperEdgeCreator {
    /**
     * Add a hyper-edge for the given CIF variable objects. Creating and adding a hyper-edge is skipped if no CIF
     * variable objects are provided.
     *
     * @param vars The CIF variable objects.
     */
    private void addHyperEdge(Set<PositionObject> vars) {
        // Skip creation of hyper-edges without any variables.
        if (vars.isEmpty()) {
            return;
        }

        // Create bit set.
        BitSet hyperEdge = new BitSet(variables.size());
        for (PositionObject var: vars) {
            int matchIdx = -1;
            for (int i = 0; i < variables.size(); i++) {
                // Get synthesis variable.
                SynthesisVariable synthVar = variables.get(i);
                Assert.notNull(synthVar == null);

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
            Assert.check(matchIdx >= 0);
            hyperEdge.set(matchIdx);
        }

        // Add hyper-edge.
        hyperEdges.add(hyperEdge);
    }
}
