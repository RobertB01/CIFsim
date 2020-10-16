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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.common.java.DependencyOrderer;

/**
 * Function local variables orderer. Orders local variables of functions for their initial value dependency on other
 * local variables of the same function.
 *
 * <p>
 * Does not support component parameter/instantiation wrapping expressions. However, they should not occur (directly or
 * indirectly) in the initial values of local variables of functions anyway.
 * </p>
 */
public class FuncLocalVarOrderer extends DependencyOrderer<DiscVariable> {
    @Override
    protected Set<DiscVariable> findDirectDependencies(DiscVariable variable) {
        // If it has no explicit value, it gets the default value, which means
        // no dependencies on other local variables.
        if (variable.getValue() == null) {
            return set();
        }

        // Get initial value.
        Expression value = first(variable.getValue().getValues());

        // Collect and return direct dependencies.
        Set<DiscVariable> rslt = set();
        collectVariables(value, rslt);
        return rslt;
    }

    /**
     * Collect local variables of functions referenced in the given expression, in the context of the initial value of
     * local variable of a function.
     *
     * @param expr The expression.
     * @param variables The local variables of functions found so far. Is modified in-place.
     */
    private static void collectVariables(Expression expr, Set<DiscVariable> variables) {
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
        if (expr instanceof ReceivedExpression) {
            return;
        }

        // Apply recursively.
        if (expr instanceof CastExpression) {
            collectVariables(((CastExpression)expr).getChild(), variables);
            return;
        }
        if (expr instanceof UnaryExpression) {
            collectVariables(((UnaryExpression)expr).getChild(), variables);
            return;
        }
        if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            collectVariables(bexpr.getLeft(), variables);
            collectVariables(bexpr.getRight(), variables);
            return;
        }
        if (expr instanceof IfExpression) {
            IfExpression iexpr = (IfExpression)expr;
            for (Expression guard: iexpr.getGuards()) {
                collectVariables(guard, variables);
            }
            collectVariables(iexpr.getThen(), variables);
            for (ElifExpression elif: iexpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    collectVariables(guard, variables);
                }
                collectVariables(elif.getThen(), variables);
            }
            collectVariables(iexpr.getElse(), variables);
            return;
        }
        if (expr instanceof SwitchExpression) {
            SwitchExpression sexpr = (SwitchExpression)expr;
            collectVariables(sexpr.getValue(), variables);
            for (SwitchCase cse: sexpr.getCases()) {
                if (cse.getKey() != null) {
                    collectVariables(cse.getKey(), variables);
                }
                collectVariables(cse.getValue(), variables);
            }
            return;
        }
        if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            collectVariables(pexpr.getChild(), variables);
            collectVariables(pexpr.getIndex(), variables);
            return;
        }
        if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;
            collectVariables(sexpr.getChild(), variables);
            if (sexpr.getBegin() != null) {
                collectVariables(sexpr.getBegin(), variables);
            }
            if (sexpr.getEnd() != null) {
                collectVariables(sexpr.getEnd(), variables);
            }
            return;
        }
        if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fexpr = (FunctionCallExpression)expr;
            for (Expression param: fexpr.getParams()) {
                collectVariables(param, variables);
            }
            collectVariables(fexpr.getFunction(), variables);
            return;
        }
        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                collectVariables(elem, variables);
            }
            return;
        }
        if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                collectVariables(elem, variables);
            }
            return;
        }
        if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression field: texpr.getFields()) {
                collectVariables(field, variables);
            }
            return;
        }
        if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                collectVariables(pair.getKey(), variables);
                collectVariables(pair.getValue(), variables);
            }
            return;
        }

        // Local variables of functions may use constants in their values,
        // but those constants can not refer to the local variables of
        // functions, as constants are never defined in functions, and it is
        // not allowed to access local variables of a function from outside
        // of the function.
        if (expr instanceof ConstantExpression) {
            return;
        }

        // References that may occur in functions. Note that enumeration
        // literals, fields (of tuple types), and standard library functions
        // don't have values. Also note that user-defined functions can be
        // evaluated, but those other functions can't access the local
        // variables of this function.
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

        // Local variables of functions can not refer to discrete variables
        // (which are part of the runtime state). They may refer to other
        // local variables of the same function. They may also refer to
        // function parameters. Note that function parameters are given a
        // value by the function call, and the caller has no access to the
        // local variables of the function. Note that if local variables
        // are used as function parameters in a call to the same function or
        // a different function, there is no dependency, as each function call
        // has its own context.
        if (expr instanceof DiscVariableExpression) {
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();
            EObject parent = var.eContainer();
            if (parent instanceof InternalFunction) {
                variables.add(var);
            } // else: discrete variables, function parameters
            return;
        }

        // Not allowed in values of local variables of functions:
        // - TimeExpression (part of runtime state)
        // - AlgVariableExpression (not allowed in functions, may refer to runtime state)
        // - ContVariableExpression (part of runtime state, includes derivatives)
        // - TauExpression (not allowed as value)
        // - LocationExpression (part of runtime state)
        // - EventExpression (not allowed as value)
        // - InputVariableExpression (not allowed in functions, would cause side effects)
        // - ComponentExpression (not allowed in functions, would cause side effects)
        // - ReceivedExpression (only allowed in updates of edges)
        // - SelfExpression (only allowed in automata and automaton definitions)

        // Should have been eliminated:
        // - CompInstWrapExpression
        // - CompParamWrapExpression

        throw new RuntimeException("Unexpected expr: " + expr);
    }
}
