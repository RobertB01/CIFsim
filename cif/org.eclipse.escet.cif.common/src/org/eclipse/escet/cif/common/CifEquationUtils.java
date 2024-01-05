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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF equations utility methods. */
public class CifEquationUtils {
    /** Constructor for the {@link CifEquationUtils} class. */
    private CifEquationUtils() {
        // Static class.
    }

    /**
     * Returns the equations for an algebraic variable or for the derivative of a continuous variable, if any.
     *
     * @param var The algebraic or continuous variable. Must not be an algebraic parameter.
     * @return The equations, if any.
     */
    public static List<Equation> getEquations(PositionObject var) {
        // Precondition checking.
        Assert.check(var instanceof AlgVariable || var instanceof ContVariable);

        // Find equation in component.
        ComplexComponent comp = (ComplexComponent)var.eContainer();
        for (Equation eq: comp.getEquations()) {
            if (eq.getVariable() == var) {
                return list(eq);
            }
        }

        // If component is a specification or group, we found nothing.
        if (!(comp instanceof Automaton)) {
            return list();
        }

        // Find equations in the locations.
        Automaton aut = (Automaton)comp;
        List<Equation> eqns = listc(aut.getLocations().size());
        for (Location loc: aut.getLocations()) {
            for (Equation eq: loc.getEquations()) {
                if (eq.getVariable() == var) {
                    eqns.add(eq);
                    break;
                }
            }
        }
        return eqns;
    }

    /**
     * Returns the different possible values of an algebraic variable. The values may be retrieved from the declaration
     * itself, as well as from one or more equations.
     *
     * @param var The algebraic variable. Must not be an algebraic parameter.
     * @param allowIncomplete Whether to allow incomplete (and thus invalid) CIF specifications.
     * @return The different possible values of the algebraic variable. For invalid specifications, an empty sequence
     *     may be returned.
     */
    public static List<Expression> getValuesForAlgVar(AlgVariable var, boolean allowIncomplete) {
        // Get from value.
        if (var.getValue() != null) {
            return list(var.getValue());
        }

        // Find equation in component.
        ComplexComponent comp = (ComplexComponent)var.eContainer();
        for (Equation eq: comp.getEquations()) {
            if (eq.getVariable() == var) {
                return list(eq.getValue());
            }
        }

        // If component is a specification or group, we found nothing.
        if (!(comp instanceof Automaton)) {
            // Specification or group.
            if (!allowIncomplete) {
                String msg = "No value found for alg var: " + var;
                throw new RuntimeException(msg);
            }
            return list();
        }

        // Find equations in the locations.
        Automaton aut = (Automaton)comp;
        List<Expression> values = listc(aut.getLocations().size());
        for (Location loc: aut.getLocations()) {
            boolean found = false;
            for (Equation eq: loc.getEquations()) {
                if (eq.getVariable() == var) {
                    values.add(eq.getValue());
                    found = true;
                    break;
                }
            }

            if (found) {
                continue;
            }

            if (!allowIncomplete) {
                String msg = "No value found for alg var: " + var;
                throw new RuntimeException(msg);
            }
        }

        return values;
    }

    /**
     * Returns the different possible derivatives of a continuous variable. The derivatives may be retrieved from the
     * declaration itself, as well as from one or more equations.
     *
     * @param var The continuous variable.
     * @param allowIncomplete Whether to allow incomplete (and thus invalid) CIF specifications.
     * @return The different possible derivatives of the continuous variable.
     */
    public static List<Expression> getDerivativesForContVar(ContVariable var, boolean allowIncomplete) {
        // Get from derivative.
        if (var.getDerivative() != null) {
            return list(var.getDerivative());
        }

        // Find equation in component.
        ComplexComponent comp = (ComplexComponent)var.eContainer();
        for (Equation eq: comp.getEquations()) {
            if (eq.getVariable() == var) {
                return list(eq.getValue());
            }
        }

        // If component is a specification or group, we found nothing.
        if (!(comp instanceof Automaton)) {
            // Specification or group.
            if (!allowIncomplete) {
                String msg = "No der found for cont var: " + var;
                throw new RuntimeException(msg);
            }
            return list();
        }

        // Find equations in the locations.
        Automaton aut = (Automaton)comp;
        List<Expression> values = listc(aut.getLocations().size());
        for (Location loc: aut.getLocations()) {
            boolean found = false;
            for (Equation eq: loc.getEquations()) {
                if (eq.getVariable() == var) {
                    values.add(eq.getValue());
                    found = true;
                    break;
                }
            }

            if (found) {
                continue;
            }

            if (!allowIncomplete) {
                String msg = "No der found for cont var: " + var;
                throw new RuntimeException(msg);
            }
        }

        return values;
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
     * @param var The algebraic variable. Must not be an algebraic parameter.
     * @return The value of the algebraic variable as a single expression.
     */
    public static Expression getSingleValueForAlgVar(AlgVariable var) {
        // Get from value.
        if (var.getValue() != null) {
            return var.getValue();
        }

        // Find equation in component.
        ComplexComponent comp = (ComplexComponent)var.eContainer();
        for (Equation eq: comp.getEquations()) {
            if (eq.getVariable() == var) {
                return eq.getValue();
            }
        }

        // Find equations in the locations.
        Automaton aut = (Automaton)comp;
        List<Expression> values = listc(aut.getLocations().size());
        for (Location loc: aut.getLocations()) {
            boolean found = false;
            for (Equation eq: loc.getEquations()) {
                if (eq.getVariable() == var) {
                    values.add(eq.getValue());
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }
            throw new RuntimeException("No value found for alg var: " + var);
        }

        // If single location, then result is easy.
        if (values.size() == 1) {
            return first(values);
        }

        // Need to create conditional expression.
        IfExpression ifExpr = newIfExpression();
        ifExpr.setType(deepclone(var.getType()));

        // Set guard.
        Location loc0 = first(aut.getLocations());
        Expression value0 = first(values);

        LocationExpression guard0 = newLocationExpression();
        guard0.setType(newBoolType());
        guard0.setLocation(loc0);

        ifExpr.getGuards().add(guard0);

        // Set 'then' expression.
        ifExpr.setThen(deepclone(value0));

        // Set 'elifs'.
        for (int i = 1; i < values.size() - 1; i++) {
            // Get location and value.
            Location loc = aut.getLocations().get(i);
            Expression value = values.get(i);

            // Create 'elif', and add it.
            ElifExpression elifExpr = newElifExpression();
            ifExpr.getElifs().add(elifExpr);

            // Set guard.
            LocationExpression guard = newLocationExpression();
            guard.setType(newBoolType());
            guard.setLocation(loc);
            elifExpr.getGuards().add(guard);

            // Set 'then'.
            elifExpr.setThen(deepclone(value));
        }

        // Set else.
        ifExpr.setElse(deepclone(last(values)));

        // Return value of algebraic variable.
        return ifExpr;
    }

    /**
     * Returns the derivative of a continuous variable as a single expression. The derivative may be retrieved from the
     * declaration itself, or from one or more equations.
     *
     * <p>
     * This method may create deep clones, but only does so if it is needed to avoid the expressions from changing
     * containment.
     * </p>
     *
     * @param var The continuous variable.
     * @return The derivative of the continuous variable as a single expression.
     */
    public static Expression getSingleDerivativeForContVar(ContVariable var) {
        // Get from derivative.
        if (var.getDerivative() != null) {
            return var.getDerivative();
        }

        // Find equation in component.
        ComplexComponent comp = (ComplexComponent)var.eContainer();
        for (Equation eq: comp.getEquations()) {
            if (eq.getVariable() == var) {
                return eq.getValue();
            }
        }

        // Find equations in the locations.
        Automaton aut = (Automaton)comp;
        List<Expression> values = listc(aut.getLocations().size());
        for (Location loc: aut.getLocations()) {
            boolean found = false;
            for (Equation eq: loc.getEquations()) {
                if (eq.getVariable() == var) {
                    values.add(eq.getValue());
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }
            throw new RuntimeException("No der found for cont var: " + var);
        }

        // If single location, then result is easy.
        if (values.size() == 1) {
            return first(values);
        }

        // Need to create conditional expression.
        IfExpression ifExpr = newIfExpression();
        ifExpr.setType(newRealType());

        // Set guard.
        Location loc0 = first(aut.getLocations());
        Expression value0 = first(values);

        LocationExpression guard0 = newLocationExpression();
        guard0.setType(newBoolType());
        guard0.setLocation(loc0);

        ifExpr.getGuards().add(guard0);

        // Set 'then' expression.
        ifExpr.setThen(deepclone(value0));

        // Set 'elifs'.
        for (int i = 1; i < values.size() - 1; i++) {
            // Get location and value.
            Location loc = aut.getLocations().get(i);
            Expression value = values.get(i);

            // Create 'elif', and add it.
            ElifExpression elifExpr = newElifExpression();
            ifExpr.getElifs().add(elifExpr);

            // Set guard.
            LocationExpression guard = newLocationExpression();
            guard.setType(newBoolType());
            guard.setLocation(loc);
            elifExpr.getGuards().add(guard);

            // Set 'then'.
            elifExpr.setThen(deepclone(value));
        }

        // Set else.
        ifExpr.setElse(deepclone(last(values)));

        // Return derivative of continuous variable.
        return ifExpr;
    }

    /**
     * Does the given algebraic variable, or derivative of the given continuous variable, have equations in locations?
     *
     * @param var The algebraic or continuous variable. Must not be an algebraic parameter.
     * @return {@code true} if (the derivative of) the variable has equations in locations, {@code false} otherwise.
     */
    public static boolean hasLocationEquations(PositionObject var) {
        // Precondition checking.
        Assert.check(var instanceof AlgVariable || var instanceof ContVariable);

        // If parent component is a specification or group, there are no locations.
        ComplexComponent comp = (ComplexComponent)var.eContainer();
        if (!(comp instanceof Automaton)) {
            return false;
        }

        // Find equations in the locations. Either all locations have an equation for (the derivative of) the variable,
        // or no location has.
        Automaton aut = (Automaton)comp;
        Location loc = first(aut.getLocations());
        for (Equation eq: loc.getEquations()) {
            if (eq.getVariable() == var) {
                return true;
            }
        }

        // Found nothing.
        return false;
    }
}
