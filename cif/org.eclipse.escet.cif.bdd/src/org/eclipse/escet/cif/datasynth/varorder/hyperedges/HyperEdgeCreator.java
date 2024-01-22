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

package org.eclipse.escet.cif.datasynth.varorder.hyperedges;

import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.datasynth.spec.CifBddDiscVariable;
import org.eclipse.escet.cif.datasynth.spec.CifBddInputVariable;
import org.eclipse.escet.cif.datasynth.spec.CifBddLocPtrVariable;
import org.eclipse.escet.cif.datasynth.spec.CifBddVariable;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Automatic variable ordering hyper-edge creator. */
public abstract class HyperEdgeCreator {
    /** The CIF specification. Must not be modified. */
    private final Specification spec;

    /** The CIF/BDD variables. */
    private final List<CifBddVariable> variables;

    /** Per CIF/BDD variable CIF object, the index into the bitset for a hyper-edge. */
    private final Map<PositionObject, Integer> cifBddVarBitIndices;

    /**
     * Constructor for the {@link HyperEdgeCreator} class.
     *
     * @param spec The CIF specification. Must not be modified.
     * @param variables The CIF/BDD variables.
     */
    public HyperEdgeCreator(Specification spec, List<CifBddVariable> variables) {
        this.spec = spec;
        this.variables = variables;

        cifBddVarBitIndices = mapc(variables.size());
        for (int i = 0; i < variables.size(); i++) {
            CifBddVariable cifBddVar = variables.get(i);
            if (cifBddVar instanceof CifBddDiscVariable discCifBddVar) {
                cifBddVarBitIndices.put(discCifBddVar.var, i);
            } else if (cifBddVar instanceof CifBddInputVariable inputCifBddVar) {
                cifBddVarBitIndices.put(inputCifBddVar.var, i);
            } else if (cifBddVar instanceof CifBddLocPtrVariable locPtrCifBddVar) {
                cifBddVarBitIndices.put(locPtrCifBddVar.aut, i);
            } else {
                throw new RuntimeException("Unknown CIF/BDD variable: " + cifBddVar);
            }
        }
    }

    /**
     * Returns the CIF specification.
     *
     * @return The CIF specification.
     */
    protected Specification getSpecification() {
        return spec;
    }

    /**
     * Returns the CIF/BDD variables.
     *
     * @return The CIF/BDD variables.
     */
    protected List<CifBddVariable> getVariables() {
        return variables;
    }

    /**
     * Create hyper-edges.
     *
     * @return The hyper-edges. Each bitset represents a hyper-edge. Within each hyper-edge, there are bits
     *     corresponding to the CIF/BDD variables of the {@link #getSpecification CIF specification}, indicating whether
     *     each variable is included in the hyper-edge or not. The bit indices in the bitsets correspond to the indices
     *     of the {@link #getVariables CIF/BDD variables}.
     */
    public abstract List<BitSet> getHyperEdges();

    /**
     * Add a hyper-edge for the given CIF/BDD variable CIF objects. Creating and adding a hyper-edge is skipped if no
     * CIF objects are provided.
     *
     * @param edgeVars The CIF/BDD variable CIF objects for which to create a new hyper-edge. This must be a subset of
     *     the variables represented by {@link #getVariables}.
     * @param hyperEdges The collection of hyper-edges so far, gets expanded in-place.
     */
    protected void addHyperEdge(Collection<PositionObject> edgeVars, List<BitSet> hyperEdges) {
        // Skip creation of hyper-edges without any variables.
        if (edgeVars.isEmpty()) {
            return;
        }

        // Create and add hyper-edge.
        BitSet hyperEdge = new BitSet(cifBddVarBitIndices.size());
        for (PositionObject var: edgeVars) {
            int bitIdx = cifBddVarBitIndices.get(var);
            hyperEdge.set(bitIdx);
        }
        hyperEdges.add(hyperEdge);
    }
}
