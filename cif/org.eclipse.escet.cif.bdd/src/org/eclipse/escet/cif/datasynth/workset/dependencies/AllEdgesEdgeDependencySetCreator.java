//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.workset.dependencies;

import static org.eclipse.escet.common.java.Lists.listc;

import org.eclipse.escet.cif.datasynth.spec.CifBddSpec;
import org.eclipse.escet.common.java.BitSets;

/**
 * An edge dependency set creator that for each edge has all edges as dependencies. It is trivially correct, but has the
 * worst possible performance.
 */
public class AllEdgesEdgeDependencySetCreator implements EdgeDependencySetCreator {
    @Override
    public void createAndStore(CifBddSpec cifBddSpec, boolean forwardEnabled) {
        // Backward.
        cifBddSpec.worksetDependenciesBackward = listc(cifBddSpec.orderedEdgesBackward.size());
        for (int i = 0; i < cifBddSpec.orderedEdgesBackward.size(); i++) {
            cifBddSpec.worksetDependenciesBackward.add(BitSets.ones(cifBddSpec.orderedEdgesBackward.size()));
        }

        // Forward.
        if (forwardEnabled) {
            cifBddSpec.worksetDependenciesForward = listc(cifBddSpec.orderedEdgesForward.size());
            for (int i = 0; i < cifBddSpec.orderedEdgesForward.size(); i++) {
                cifBddSpec.worksetDependenciesForward.add(BitSets.ones(cifBddSpec.orderedEdgesForward.size()));
            }
        }
    }
}
