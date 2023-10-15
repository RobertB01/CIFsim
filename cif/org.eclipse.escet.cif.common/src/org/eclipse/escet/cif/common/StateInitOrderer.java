//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.common.java.DependencyOrderer;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * State initialization orderer. Orders state variables, automata, and locations for their initialization dependencies
 * on each other.
 *
 * <p>
 * For state variables, the initial values are taken into account. For automata, the (initial) locations are taken into
 * account. For locations, the initialization predicates are taken into account.
 * </p>
 *
 * <p>
 * Does not support component parameter/instantiation wrapping expressions.
 * </p>
 *
 * @see StateInitVarOrderer
 */
public class StateInitOrderer extends DependencyOrderer<PositionObject> {
    @Override
    protected Set<PositionObject> findDirectDependencies(PositionObject obj) {
        if (obj instanceof Declaration) {
            // If it has no explicit value, it gets the default value, which
            // means no dependencies on other state variables and locations. If
            // it has any value in its domain (discrete variables only), it
            // also has no dependencies. Otherwise, get all initial values.
            List<Expression> values;
            if (obj instanceof DiscVariable) {
                DiscVariable dvar = (DiscVariable)obj;
                if (dvar.getValue() == null) {
                    return set();
                }

                values = dvar.getValue().getValues();
                if (values == null) {
                    // Initial value is 'any', which has no dependencies.
                    return set();
                }
            } else if (obj instanceof InputVariable) {
                // Does not have a dependency to other variables or locations.
                return set();
            } else {
                ContVariable cvar = (ContVariable)obj;
                if (cvar.getValue() == null) {
                    return set();
                }
                values = list(cvar.getValue());
            }

            // Collect and return direct dependencies.
            Set<PositionObject> rslt = set();
            for (Expression value: values) {
                collectDependencies(value, rslt);
            }
            return rslt;
        } else if (obj instanceof Automaton) {
            // Dependencies are based on the (initial) locations. We optimize
            // to exclude locations without initialization predicates, as that
            // is a cheap check, but still has the potential to avoid adding
            // most of the locations.
            Automaton aut = (Automaton)obj;
            Set<PositionObject> rslt = set();
            for (Location loc: aut.getLocations()) {
                if (loc.getInitials().isEmpty()) {
                    continue;
                }
                rslt.add(loc);
            }
            return rslt;
        } else if (obj instanceof Location) {
            // Dependencies are based on the initialization predicates.
            Location loc = (Location)obj;
            Set<PositionObject> rslt = set();
            for (Expression init: loc.getInitials()) {
                collectDependencies(init, rslt);
            }
            return rslt;
        } else {
            throw new RuntimeException("Unexpected obj: " + obj);
        }
    }

    /**
     * Collect state variables and locations referenced in the given expression, in the context of the initial value of
     * a state variable or initialization predicate of a location.
     *
     * @param expr The expression.
     * @param deps The dependencies found so far. Is modified in-place.
     */
    private static void collectDependencies(Expression expr, Set<PositionObject> deps) {
        // Literals that don't contain expressions.
        if (expr instanceof BoolExpression) {
            return;
        }
        if (expr instanceof IntExpression) {
            return;
        }
        if (expr instanceof RealExpression) {
            return;
        }
        if (expr instanceof StringExpression) {
            return;
        }

        // Semi references that do not contain an expression.
        if (expr instanceof TimeExpression) {
            return;
        }

        // Apply recursively.
        if (expr instanceof CastExpression) {
            collectDependencies(((CastExpression)expr).getChild(), deps);
            return;
        }
        if (expr instanceof UnaryExpression) {
            collectDependencies(((UnaryExpression)expr).getChild(), deps);
            return;
        }
        if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            collectDependencies(bexpr.getLeft(), deps);
            collectDependencies(bexpr.getRight(), deps);
            return;
        }
        if (expr instanceof IfExpression) {
            IfExpression iexpr = (IfExpression)expr;
            for (Expression guard: iexpr.getGuards()) {
                collectDependencies(guard, deps);
            }
            collectDependencies(iexpr.getThen(), deps);
            for (ElifExpression elif: iexpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    collectDependencies(guard, deps);
                }
                collectDependencies(elif.getThen(), deps);
            }
            collectDependencies(iexpr.getElse(), deps);
            return;
        }
        if (expr instanceof SwitchExpression) {
            SwitchExpression sexpr = (SwitchExpression)expr;
            collectDependencies(sexpr.getValue(), deps);
            for (SwitchCase cse: sexpr.getCases()) {
                if (cse.getKey() != null) {
                    collectDependencies(cse.getKey(), deps);
                }
                collectDependencies(cse.getValue(), deps);
            }
            return;
        }
        if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            collectDependencies(pexpr.getChild(), deps);
            collectDependencies(pexpr.getIndex(), deps);
            return;
        }
        if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;
            collectDependencies(sexpr.getChild(), deps);
            if (sexpr.getBegin() != null) {
                collectDependencies(sexpr.getBegin(), deps);
            }
            if (sexpr.getEnd() != null) {
                collectDependencies(sexpr.getEnd(), deps);
            }
            return;
        }
        if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fexpr = (FunctionCallExpression)expr;
            for (Expression arg: fexpr.getArguments()) {
                collectDependencies(arg, deps);
            }
            collectDependencies(fexpr.getFunction(), deps);
            return;
        }
        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                collectDependencies(elem, deps);
            }
            return;
        }
        if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                collectDependencies(elem, deps);
            }
            return;
        }
        if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression field: texpr.getFields()) {
                collectDependencies(field, deps);
            }
            return;
        }
        if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                collectDependencies(pair.getKey(), deps);
                collectDependencies(pair.getValue(), deps);
            }
            return;
        }

        // State variables and location initialization predicates may use
        // constants in their initial values, but those constants may not refer
        // to state variables or locations.
        if (expr instanceof ConstantExpression) {
            return;
        }

        // References that may occur in the initial values of state variables
        // and initialization predicates of locations. Note that enumeration
        // literals, fields (of tuple types), and standard library functions
        // don't have values. Also note that user-defined functions can be
        // evaluated, but those functions are not allowed to access state
        // variables and locations.
        if (expr instanceof EnumLiteralExpression) {
            return;
        }
        if (expr instanceof FieldExpression) {
            return;
        }
        if (expr instanceof StdLibFunctionExpression) {
            return;
        }
        if (expr instanceof FunctionExpression) {
            return;
        }

        // State variables may, in their initial values, refer to other
        // state variables. They can not refer to local variables of functions,
        // and to parameters of functions. Also applies to initialization
        // predicates of locations.
        if (expr instanceof DiscVariableExpression) {
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();
            deps.add(var);
            return;
        }

        // State variables may refer to algebraic variables. Check recursively
        // in their value/equations. Also applies to initialization predicates
        // of locations.
        if (expr instanceof AlgVariableExpression) {
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
            List<Expression> values;
            values = CifEquationUtils.getValuesForAlgVar(var, false);
            for (Expression value: values) {
                collectDependencies(value, deps);
            }

            // If the value is defined via equations in locations, there is
            // a dependency to the initial location of the parent automaton.
            if (CifEquationUtils.hasLocationEquations(var)) {
                deps.add((Automaton)(var.eContainer()));
            }
            return;
        }

        // State variables may, in their initial values, refer to other
        // state variables. They may also refer to derivatives. Also applies to
        // initialization predicates of locations.
        if (expr instanceof ContVariableExpression) {
            ContVariableExpression cexpr = (ContVariableExpression)expr;
            ContVariable var = ((ContVariableExpression)expr).getVariable();
            if (cexpr.isDerivative()) {
                // Check recursively in the derivative/equations.
                List<Expression> derivs;
                derivs = CifEquationUtils.getDerivativesForContVar(var, false);
                for (Expression deriv: derivs) {
                    collectDependencies(deriv, deps);
                }

                // If the derivative is defined via equations in locations, there
                // is a dependency to the initial location of the parent automaton.
                if (CifEquationUtils.hasLocationEquations(var)) {
                    deps.add((Automaton)(var.eContainer()));
                }
                return;
            } else {
                // Add dependency.
                deps.add(var);
            }
            return;
        }

        // Input variables don't have dependencies to other variables or locations.
        if (expr instanceof InputVariableExpression) {
            return;
        }

        // Initial values of state variables and initialization predicates of
        // locations may refer to the (initial) locations of automata.
        if (expr instanceof LocationExpression) {
            Location loc = ((LocationExpression)expr).getLocation();
            deps.add(loc);
            return;
        }

        // Automaton (self) references refer to the current location of the
        // automata, and are similar to location references in that regard.
        if (expr instanceof ComponentExpression) {
            Component comp = ((ComponentExpression)expr).getComponent();
            if (!(comp instanceof Automaton)) {
                throw new RuntimeException("comp ref as value: " + comp);
            }
            deps.add(comp);
            return;
        }
        if (expr instanceof SelfExpression) {
            CifType ctype = CifTypeUtils.normalizeType((expr).getType());
            deps.add(((ComponentType)ctype).getComponent());
            return;
        }

        // Not allowed in the initial values of state variables and
        // initialization predicates of locations:
        // - TauExpression (not allowed as value)
        // - EventExpression (not allowed as value)
        // - ReceivedExpression (only allowed in updates of edges)

        // Should have been eliminated:
        // - CompParamExpression
        // - CompInstWrapExpression
        // - CompParamWrapExpression

        throw new RuntimeException("Unexpected expr: " + expr);
    }
}
