//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
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
import org.eclipse.escet.common.java.DependencyOrderer;

/**
 * State variable orderer. Orders state variables for their initial value dependency on other state variables.
 *
 * <p>
 * Does not support component parameter/instantiation wrapping expressions.
 * </p>
 *
 * <p>
 * Assumes automata have at most one initial location, which can be determined statically. The {@link StateInitOrderer}
 * does not have this assumption.
 * </p>
 */
public class StateInitVarOrderer extends DependencyOrderer<Declaration> {
    @Override
    protected Set<Declaration> findDirectDependencies(Declaration stateVar) {
        // If it has no explicit value, it gets the default value, which means
        // no dependencies on other state variables. If it has any value in
        // its domain (discrete variables only), it also has no dependencies
        // on other state variables. Otherwise, get all initial values.
        List<Expression> values;
        if (stateVar instanceof DiscVariable) {
            DiscVariable dvar = (DiscVariable)stateVar;
            if (dvar.getValue() == null) {
                return set();
            }

            values = dvar.getValue().getValues();
            if (values == null) {
                // Initial value is 'any', which has no dependencies.
                return set();
            }
        } else {
            ContVariable cvar = (ContVariable)stateVar;
            if (cvar.getValue() == null) {
                return set();
            }
            values = list(cvar.getValue());
        }

        // Collect and return direct dependencies.
        Set<Declaration> rslt = set();
        for (Expression value: values) {
            collectStateVars(value, rslt);
        }
        return rslt;
    }

    /**
     * Collect state variables referenced in the given expression, in the context of the initial value of a state
     * variable.
     *
     * @param expr The expression.
     * @param variables The state variables found so far. Is modified in-place.
     */
    private static void collectStateVars(Expression expr, Set<Declaration> variables) {
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
            collectStateVars(((CastExpression)expr).getChild(), variables);
            return;
        }
        if (expr instanceof UnaryExpression) {
            collectStateVars(((UnaryExpression)expr).getChild(), variables);
            return;
        }
        if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            collectStateVars(bexpr.getLeft(), variables);
            collectStateVars(bexpr.getRight(), variables);
            return;
        }
        if (expr instanceof IfExpression) {
            IfExpression iexpr = (IfExpression)expr;
            for (Expression guard: iexpr.getGuards()) {
                collectStateVars(guard, variables);
            }
            collectStateVars(iexpr.getThen(), variables);
            for (ElifExpression elif: iexpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    collectStateVars(guard, variables);
                }
                collectStateVars(elif.getThen(), variables);
            }
            collectStateVars(iexpr.getElse(), variables);
            return;
        }
        if (expr instanceof SwitchExpression) {
            SwitchExpression sexpr = (SwitchExpression)expr;
            collectStateVars(sexpr.getValue(), variables);
            for (SwitchCase cse: sexpr.getCases()) {
                if (cse.getKey() != null) {
                    collectStateVars(cse.getKey(), variables);
                }
                collectStateVars(cse.getValue(), variables);
            }
            return;
        }
        if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            collectStateVars(pexpr.getChild(), variables);
            collectStateVars(pexpr.getIndex(), variables);
            return;
        }
        if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;
            collectStateVars(sexpr.getChild(), variables);
            if (sexpr.getBegin() != null) {
                collectStateVars(sexpr.getBegin(), variables);
            }
            if (sexpr.getEnd() != null) {
                collectStateVars(sexpr.getEnd(), variables);
            }
            return;
        }
        if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fexpr = (FunctionCallExpression)expr;
            for (Expression param: fexpr.getParams()) {
                collectStateVars(param, variables);
            }
            collectStateVars(fexpr.getFunction(), variables);
            return;
        }
        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                collectStateVars(elem, variables);
            }
            return;
        }
        if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                collectStateVars(elem, variables);
            }
            return;
        }
        if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression field: texpr.getFields()) {
                collectStateVars(field, variables);
            }
            return;
        }
        if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                collectStateVars(pair.getKey(), variables);
                collectStateVars(pair.getValue(), variables);
            }
            return;
        }

        // State variables may use constants in their initial values, but those
        // constants may not refer to state variables.
        if (expr instanceof ConstantExpression) {
            return;
        }

        // References that may occur in the initial values of state variables.
        // Note that enumeration literals, fields (of tuple types), and
        // standard library functions don't have values. Also note that
        // user-defined functions can be evaluated, but those functions are not
        // allowed to access state variables.
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
        // and to parameters of functions.
        if (expr instanceof DiscVariableExpression) {
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();
            variables.add(var);
            return;
        }

        // State variables may refer to algebraic variables. Check recursively
        // in their value/equations.
        if (expr instanceof AlgVariableExpression) {
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
            List<Expression> values;
            values = CifEquationUtils.getValuesForAlgVar(var, false);
            for (Expression value: values) {
                collectStateVars(value, variables);
            }
            return;
        }

        // State variables may, in their initial values, refer to other
        // state variables. They may also refer to derivatives.
        if (expr instanceof ContVariableExpression) {
            ContVariableExpression cexpr = (ContVariableExpression)expr;
            ContVariable var = ((ContVariableExpression)expr).getVariable();
            if (cexpr.isDerivative()) {
                // Check recursively in the derivative/equations.
                List<Expression> derivs;
                derivs = CifEquationUtils.getDerivativesForContVar(var, false);
                for (Expression deriv: derivs) {
                    collectStateVars(deriv, variables);
                }
                return;
            } else {
                // Add dependency.
                variables.add(var);
            }
            return;
        }

        // Input variables don't have dependencies to other variables or locations.
        if (expr instanceof InputVariableExpression) {
            return;
        }

        // State variables may refer to the initial locations of automata.
        // We ensured elsewhere that they are available when initializing the
        // initial values of state variables. As such, there is no need to
        // consider them as dependencies.
        if (expr instanceof LocationExpression) {
            return;
        }

        // Automaton (self) references don't have a value. They refer to the
        // current location of the automata, and are similar to location
        // references in that regard.
        if (expr instanceof ComponentExpression) {
            return;
        }
        if (expr instanceof SelfExpression) {
            return;
        }

        // Not allowed in the initial values of state variables:
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
