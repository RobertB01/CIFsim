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

package org.eclipse.escet.cif.codegen.updates;

import static org.eclipse.escet.cif.codegen.updates.FindDeclarationUsage.collectUse;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.common.java.Assert;

/**
 * Class holding the information about invalidating algebraic and derivative expressions due to variable or time change.
 */
public class AlgDerInvalidations {
    /**
     * For each algebraic or derivative variable, which other variables does it use to derive its value? Temporary
     * variable, is released after computing {@link #algDerMap}.
     */
    private Map<Declaration, Set<VariableWrapper>> algDerMap;

    /**
     * For a discrete, continuous, input, or time variable that is changed, the set of algebraic variables and
     * derivative expressions that become invalid.
     *
     * <p>
     * Variable is only available after calling {@link #computeAffects}, and should not be modified.
     * </p>
     */
    private Map<VariableWrapper, Set<VariableWrapper>> invalidatedAlgDerivs;

    /**
     * Compute which algebraic and derivative expressions become invalid when a discrete, continuous, input, or time
     * variable is modified.
     *
     * @param algVars Algebraic variables.
     * @param derivVars Derivative expressions.
     */
    public void computeAffects(List<AlgVariable> algVars, List<ContVariable> derivVars) {
        // Construct temporary map with algebraic and derivative variable usage.
        algDerMap = mapc(algVars.size() + derivVars.size());

        // For each algebraic variable expression, get the accessed variables (and time).
        for (AlgVariable algVar: algVars) {
            Set<VariableWrapper> accessed = set();
            collectUse(algVar.getValue(), accessed);
            algDerMap.put(algVar, accessed);
        }

        // For each derivative expression, get the accessed variables (and time).
        for (ContVariable derivVar: derivVars) {
            Set<VariableWrapper> accessed = set();
            collectUse(derivVar.getDerivative(), accessed);
            algDerMap.put(derivVar, accessed);
        }

        // Compute the invalidation map.
        invalidatedAlgDerivs = map();
        for (Declaration decl: algDerMap.keySet()) {
            // Remove the accessed algebraic and derivatives from the accessed set.
            Set<VariableWrapper> access = removeAlgDerivs(decl);

            // All variables in 'access' affect 'decl' directly or indirectly.
            VariableWrapper wrappedAlgDer = new VariableWrapper(decl, decl instanceof ContVariable);
            for (VariableWrapper var: access) {
                addInvalidated(var, wrappedAlgDer);
            }
        }

        algDerMap = null; // Drop the temporary map.
    }

    /**
     * Remove the algebraic and derivative variable accesses from the given variable, by adding the variable accesses
     * that the removed variable needs.
     *
     * <p>
     * Example:
     *
     * <pre>
     *     alg x = y + time;
     *     alg y = q + n;
     * </pre>
     *
     * It gives the algebraic/derivative map:
     *
     * <pre>
     *     x :-> y, time
     *     y :-> q, n
     * </pre>
     *
     * This function removes the used algebraic and derivative expressions, giving:
     *
     * <pre>
     *     x :-> q, n, time
     *     y :-> q, n
     * </pre>
     * </p>
     *
     * @param decl Algebraic or derivative variable to clean access.
     * @return The accessed variables after eliminating the algebraic and derivative variables.
     */
    private Set<VariableWrapper> removeAlgDerivs(Declaration decl) {
        Set<VariableWrapper> accessed = algDerMap.get(decl);
        Assert.notNull(accessed);

        // Clean the current set 'accessed' by removing the algebraic and
        // derivative entries from it, and replacing them by their clean
        // definition. Note that cycles do not exist here (enforced by the
        // CIF type checker, a simple recursive expansion routine is sufficient.
        Set<VariableWrapper> cleanSet = set();

        for (VariableWrapper varWrap: accessed) {
            if (varWrap.isDerivative() || varWrap.isAlgebraic()) {
                // Add the cleaned access set for the algebraic or derivative.
                Set<VariableWrapper> cleanAccess = removeAlgDerivs(varWrap.decl);
                cleanSet.addAll(cleanAccess);
            } else {
                // Not an algebraic or derivative, just copy the element.
                cleanSet.add(varWrap);
            }
        }

        algDerMap.put(decl, cleanSet);
        return cleanSet;
    }

    /**
     * Denote that changing a given variable (or time) invalidates the given algebraic or derivative.
     *
     * @param variable Variable to change ({@code null} means time).
     * @param algDer Algebraic variable or derivative that becomes invalid.
     */
    private void addInvalidated(VariableWrapper variable, VariableWrapper algDer) {
        Set<VariableWrapper> algDers = invalidatedAlgDerivs.get(variable);
        if (algDers == null) {
            algDers = set(algDer);
            invalidatedAlgDerivs.put(variable, algDers);
        } else {
            algDers.add(algDer);
        }
    }

    /**
     * Get the set algebraic variables and derivative expressions that become invalid after modifying the given
     * variable.
     *
     * @param d Variable that was changed.
     * @return Set of algebraic variables and derivative expressions that have become invalid.
     */
    public Set<VariableWrapper> getAffecting(VariableWrapper d) {
        Set<VariableWrapper> affecting = invalidatedAlgDerivs.get(d);
        if (affecting == null) {
            return Collections.emptySet();
        }
        return affecting;
    }
}
