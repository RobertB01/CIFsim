//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.difference;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;

/**
 * In-place transformation that removes unused algebraic variables and their equations.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 */
public class RemoveUnusedAlgVariables extends CifWalker implements CifToCifTransformation {
    /** All algebraic variables encountered so far. */
    private Set<AlgVariable> allAlgVars = set();

    /** All algebraic variables used in expressions outside value expressions of other algebraic variables. */
    private Set<AlgVariable> allUsedAlgVars = set();

    /** Map of algebraic variables to the collection of algebraic variables used in its defining expressions. */
    private Map<AlgVariable, Set<AlgVariable>> algVarsReferredByAlgVar = map();

    /**
     * If not {@code null}, the algebraic variable owning the value expression or equation that is currently being
     * analyzed.
     */
    private AlgVariable analyzingAlgVar = null;

    /** Equations defining values for algebraic variables. */
    private final List<Equation> algEquations = list();

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating unused algebraic variables from a CIF specification with component "
                    + "definitions is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Walk the specification.
        walkSpecification(spec);

        // Mark chained algebraic variables as used if first of the chain is marked as used.
        boolean change = true;
        while (change) {
            change = false;
            for (Entry<AlgVariable, Set<AlgVariable>> entry: algVarsReferredByAlgVar.entrySet()) {
                if (allUsedAlgVars.contains(entry.getKey())) {
                    change = change || allUsedAlgVars.addAll(entry.getValue());
                }
            }
        }

        // Identify and remove unused algebraic variables.
        Set<AlgVariable> unusedAlgVars = difference(allAlgVars, allUsedAlgVars);
        for (AlgVariable uav: unusedAlgVars) {
            EMFHelper.removeFromParentContainment(uav);
        }

        // Also remove equations of unused algebraic variables.
        for (Equation eq: algEquations) {
            if (unusedAlgVars.contains(eq.getVariable())) {
                EMFHelper.removeFromParentContainment(eq);
            }
        }
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        allAlgVars.addAll(filter(comp.getDeclarations(), AlgVariable.class));
    }

    @Override
    protected void preprocessAlgVariable(AlgVariable av) {
        analyzingAlgVar = av;
    }

    @Override
    protected void postprocessAlgVariable(AlgVariable av) {
        analyzingAlgVar = null;
    }

    @Override
    protected void preprocessEquation(Equation eqn) {
        if (eqn.getVariable() instanceof AlgVariable) {
            analyzingAlgVar = (AlgVariable)eqn.getVariable();
            // Store equation (for algebraic variable) for (possible) removal later on.
            algEquations.add(eqn);
        }
    }

    @Override
    protected void postprocessEquation(Equation eqn) {
        analyzingAlgVar = null;
    }

    @Override
    protected void walkAlgVariableExpression(AlgVariableExpression algRef) {
        AlgVariable var = algRef.getVariable();
        // If the algebraic variable is already considered as being used, don't bother finding more uses.
        if (allUsedAlgVars.contains(var)) {
            return;
        }

        // Check whether the expression counts as use of the algebraic variable.
        if (analyzingAlgVar != null) {
            // This algebraic variable reference is part of an expression that defines a value of another algebraic
            // variable.
            Set<AlgVariable> vars = algVarsReferredByAlgVar.get(analyzingAlgVar);
            if (vars != null) {
                vars.add(var);
            } else {
                algVarsReferredByAlgVar.put(analyzingAlgVar, set(var));
            }
        } else {
            // This algebraic variable reference is *not* part of an expression that defines a value of another
            // algebraic variable.
            allUsedAlgVars.add(var);
        }
    }
}
