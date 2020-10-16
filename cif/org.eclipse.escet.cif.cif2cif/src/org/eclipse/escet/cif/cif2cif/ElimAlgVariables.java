//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifEquationUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * In-place transformation that eliminates algebraic variables and their equations. Note that it does not eliminate
 * algebraic parameters.
 *
 * <p>
 * Since algebraic variables are shortcuts for expressions, eliminating them could result in a blow-up of the size of
 * the specification.
 * </p>
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * Precondition: Specifications with automaton 'self' references are currently not supported.
 * </p>
 *
 * <p>
 * This transformation introduces 'if' expressions for algebraic variables that have an equation per location, for at
 * least two locations. The guards of such 'if' expressions have location references in them. To eliminate them, apply
 * the {@link ElimLocRefExprs} transformation after this transformation.
 * </p>
 *
 * @see ElimEquations
 */
public class ElimAlgVariables extends CifWalker implements CifToCifTransformation {
    /** The objects to remove. */
    private final List<PositionObject> objectsToRemove = list();

    /** Mapping from algebraic variables to their values. */
    private final Map<AlgVariable, Expression> algVarValueMap = map();

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

        // Remove algebraic variables and equations. We do this afterwards, as
        // we need their containment intact to retrieve their equations.
        for (PositionObject obj: objectsToRemove) {
            EMFHelper.removeFromParentContainment(obj);
        }
    }

    /**
     * Returns the value of an algebraic variable as a single expression. The value may be retrieved from the
     * declaration itself, or from one or more equations.
     *
     * <p>
     * This method may create deep clones, but only does so if it is needed to avoid the expressions from changing
     * containment.
     * </p>
     *
     * <p>
     * Unlike the {@link CifEquationUtils#getSingleValueForAlgVar} method, this method employs caching.
     * </p>
     *
     * @param var The algebraic variable. Must not be an algebraic parameter.
     * @return The value of the algebraic variable as a single expression.
     */
    private Expression getSingleValueForAlgVar(AlgVariable var) {
        // Try cache first.
        Expression rslt = algVarValueMap.get(var);
        if (rslt != null) {
            return rslt;
        }

        // Calculate result, cache it, and return it.
        rslt = CifEquationUtils.getSingleValueForAlgVar(var);
        algVarValueMap.put(var, rslt);
        return rslt;
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Store algebraic variable declarations for removal later on.
        objectsToRemove.addAll(filter(comp.getDeclarations(), AlgVariable.class));
    }

    @Override
    protected void preprocessEquation(Equation eqn) {
        if (eqn.getVariable() instanceof AlgVariable) {
            // Store equation (for algebraic variable) for removal later on.
            objectsToRemove.add(eqn);
        }
    }

    @Override
    protected void walkAlgVariableExpression(AlgVariableExpression algRef) {
        // Get value of algebraic variable, and make a unique copy.
        AlgVariable var = algRef.getVariable();
        Expression value = getSingleValueForAlgVar(var);
        value = deepclone(value);

        // Replace algebraic variable reference by the value of the algebraic
        // variable.
        EMFHelper.updateParentContainment(algRef, value);

        // Process the value recursively.
        walkExpression(value);
    }

    @Override
    protected void preprocessSelfExpression(SelfExpression expr) {
        String msg = "Eliminating algebraic variables from a CIF specification with automaton \"self\" references is "
                + "currently not supported.";
        throw new CifToCifPreconditionException(msg);
    }
}
