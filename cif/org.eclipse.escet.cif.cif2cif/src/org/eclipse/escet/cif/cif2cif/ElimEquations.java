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

import org.eclipse.escet.cif.common.CifEquationUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/**
 * In-place transformation that eliminates equations for algebraic variables and derivatives of continuous variables.
 * Note that it does not eliminate the variables themselves.
 *
 * <p>
 * Since the values are just represented at a different place, this should not blow up the size of the specification,
 * even though we may need to use 'if' expressions.
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
 * @see ElimAlgVariables
 */
public class ElimEquations extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating equations from a CIF specification with component definitions is currently not "
                    + "supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Perform actual transformation.
        walkSpecification(spec);
    }

    @Override
    protected void postprocessComplexComponent(ComplexComponent comp) {
        // Remove equations from components.
        comp.getEquations().clear();
    }

    @Override
    protected void postprocessLocation(Location loc) {
        // Remove equations from locations.
        loc.getEquations().clear();
    }

    @Override
    protected void preprocessAlgVariable(AlgVariable var) {
        // If the variable already has a value with its declaration, there is
        // nothing to eliminate.
        if (var.getValue() != null) {
            return;
        }

        // Get value of algebraic variable from equations. Since we simply
        // move the values from the equations, there is no need to clone here.
        Expression value = CifEquationUtils.getSingleValueForAlgVar(var);

        // Set the value in the declaration itself.
        var.setValue(value);
    }

    @Override
    protected void preprocessContVariable(ContVariable var) {
        // If the variable already has a derivative with its declaration,
        // there is nothing to eliminate.
        if (var.getDerivative() != null) {
            return;
        }

        // Get derivative of continuous variable from equations. Since we
        // simply move the derivative expressions from the equations, there is
        // no need to clone here.
        Expression der = CifEquationUtils.getSingleDerivativeForContVar(var);

        // Set the derivative in the declaration itself.
        var.setDerivative(der);
    }

    @Override
    protected void walkEquation(Equation eqn) {
        // Skip walking over the features of equations, as the 'value' may
        // have been moved somewhere else. Also, there is no need to walk
        // over equations, as they will be removed anyway.
    }

    @Override
    protected void preprocessSelfExpression(SelfExpression expr) {
        String msg = "Eliminating equations from a CIF specification with automaton \"self\" references is currently "
                + "not supported.";
        throw new CifToCifPreconditionException(msg);
    }
}
