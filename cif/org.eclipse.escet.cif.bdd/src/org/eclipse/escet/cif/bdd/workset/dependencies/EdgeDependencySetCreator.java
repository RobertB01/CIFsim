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

package org.eclipse.escet.cif.bdd.workset.dependencies;

import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.bdd.utils.CifBddReachability;

/** Edge dependency set creator for the {@link CifBddReachability reachability} workset algorithm. */
public interface EdgeDependencySetCreator {
    /**
     * Create the forward and backward edge dependency sets, and store them in the CIF/BDD specification.
     *
     * <p>
     * For each edge 'e', its forward dependencies are those edges that may become enabled after taking edge 'e'. The
     * backward dependencies are inverted with respect to the forward ones.
     * </p>
     *
     * <p>
     * Note that the workset algorithm removes 'e' from the workset after it has been applied, after adding the
     * dependencies of 'e'. Hence, it does not matter whether an edge has itself as a dependency or not.
     * </p>
     *
     * <p>
     * Ideally, we'd compute the exact dependencies, but with arbitrary guard predicates and arbitrary assigned values
     * in updates, this is typically too complex to analyze statically. Hence, we compute an over-approximation. The
     * larger the over-approximation, the more edges are needlessly applied, impacting the performance of reachability
     * computations. The dependencies must never be under-approximated, as that would make the workset algorithm
     * invalid, since it then may no longer reach all reachable states.
     * </p>
     *
     * @param cifBddSpec The CIF/BDD specification. Is modified in-place.
     * @param forwardEnabled Whether forward reachability is enabled. If it is disabled, the forward edge dependency
     *     sets are not computed and thus also not stored in the CIF/BDD specification.
     * @see CifBddSpec#worksetDependenciesBackward
     * @see CifBddSpec#worksetDependenciesForward
     */
    public void createAndStore(CifBddSpec cifBddSpec, boolean forwardEnabled);
}
