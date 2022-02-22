//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2022 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;

/**
 * In-place transformation that removes unused algebraic variables and their
 * equations. Note that it does not remove algebraic parameters.
 *
 * <p>Since algebraic variables are shortcuts for expressions, unused algebraic
 * variables may clutter the model with obsolete information.</p>
 *
 * <p>Precondition: Specifications with component definitions/instantiations
 * are currently not supported.</p>
 *
 * <p>Precondition: Specifications with automaton 'self' references are
 * currently not supported.</p>
 *
 * @see ElimEquations
 */
public class RemoveUnusedAlgVariables extends CifWalker implements CifToCifTransformation {
    /** All algebraic variables encountered so far. */
    private Set<AlgVariable> allAlgVars = set();

    /**
     * All algebraic variables used in expressions not contained in value expressions
     * of other algebraic variables.
     */
    private Set<AlgVariable> allUsedAlgVars = set();

    /**
     * Map with algebraic variables where an entry pair ({@code key}, {@code value}), where {@code key} is a
     * single algebraic variable and {@code value} is a set of algebraic variables, has the interpretation that
     * the value expression of algebraic variable {@code key} contains algebraic variables from {@code value}.
     */
    private Map<AlgVariable, Set<AlgVariable>> algVarsReferredByAlgVar = map();

    /** The algebraic variable that is currently analyzed, {@code null} if no algebraic variable is analyzed. */
    private AlgVariable analyzingAlgVar = null;

    /** Equations of algebraic variables. */
    private final List<Equation> algEquations = list();

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating algebraic variables from a CIF specification with component definitions is "
                    + "currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Walk the specification.
        walkSpecification(spec);

        // Mark chained algebraic variables as used if first of the chain is marked as used.
        boolean change = true;
        while (change) {
            change = false;
            for (Entry<AlgVariable, Set<AlgVariable>> entry : algVarsReferredByAlgVar.entrySet()) {
                if (allUsedAlgVars.contains(entry.getKey())) {
                    change = change || allUsedAlgVars.addAll(entry.getValue());
                }
            }
        }

        // Identify and remove unused algebraic variables.
        Set<AlgVariable> unusedAlgVars = difference(allAlgVars, allUsedAlgVars);
        for (AlgVariable uav : unusedAlgVars) {
            EMFHelper.removeFromParentContainment(uav);
        }

        // Remove algebraic equations.
        for (Equation eq: algEquations) {
            if (unusedAlgVars.contains(eq.getVariable())) {
                EMFHelper.removeFromParentContainment(eq);
            }
        }
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        allAlgVars.addAll(filter(comp.getDeclarations(),
                                      AlgVariable.class));
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
        if (allUsedAlgVars.contains(var)) {
            return;
        }

        if (analyzingAlgVar != null) {
            // Expression is in alg var value expression.
            Set<AlgVariable> vars = algVarsReferredByAlgVar.get(analyzingAlgVar);
            if (vars != null) {
                vars.add(var);
            } else {
                algVarsReferredByAlgVar.put(analyzingAlgVar, set(var));
            }
        } else {
            // Expression is not in alg var value expression.
            allUsedAlgVars.add(var);
        }
    }

    @Override
    protected void preprocessSelfExpression(SelfExpression expr) {
        String msg = "Eliminating algebraic variables from a CIF specification with automaton \"self\" references is "
                + "currently not supported.";
        throw new CifToCifPreconditionException(msg);
    }
}
