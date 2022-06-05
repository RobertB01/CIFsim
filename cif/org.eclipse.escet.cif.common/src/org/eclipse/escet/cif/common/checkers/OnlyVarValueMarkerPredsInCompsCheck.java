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

package org.eclipse.escet.cif.common.checkers;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * CIF check that allows marker predicates in components only if they are of the form 'discrete_variable =
 * marked_value', and only at most one per discrete variable.
 */
public class OnlyVarValueMarkerPredsInCompsCheck extends CifCheck {
    /** Mapping from discrete variables to their marked values. Used to detect duplicates. */
    private Map<DiscVariable, Expression> markeds;

    @Override
    public void setViolations(Set<CifCheckViolation> violations) {
        // Initialization.
        super.setViolations(violations);
        markeds = map();
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        for (Expression marked: comp.getMarkeds()) {
            // The only supported form is 'discrete_variable = marked_value'.
            if (!(marked instanceof BinaryExpression)) {
                addViolation(comp, "component has a marker predicate that is not of the form "
                        + "\"discrete_variable = marked_value\"");
                continue;
            }
            BinaryExpression bexpr = (BinaryExpression)marked;
            if (bexpr.getOperator() != BinaryOperator.EQUAL || !(bexpr.getLeft() instanceof DiscVariableExpression)) {
                addViolation(comp, "component has a marker predicate that is not of the form "
                        + "\"discrete_variable = marked_value\"");
                continue;
            }

            // Add to mapping, to detect duplicates.
            DiscVariableExpression vref = (DiscVariableExpression)bexpr.getLeft();
            DiscVariable var = vref.getVariable();
            Expression newValue = bexpr.getRight();
            Expression previousValue = markeds.get(var);
            if (previousValue == null) {
                markeds.put(var, newValue);
            } else {
                addViolation(var, "discrete variable has multiple predicates to specify its marked values");
            }
        }
    }
}
