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

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.common.java.DependencyOrderer;

/**
 * Constant orderer. Orders constants for their value dependency on other constants.
 *
 * <p>
 * Does not support component parameter/instantiation wrapping expressions.
 * </p>
 */
public class ConstantOrderer extends DependencyOrderer<Constant> {
    @Override
    protected Set<Constant> findDirectDependencies(Constant constant) {
        Set<Constant> rslt = set();
        collectConstants(constant.getValue(), rslt);
        return rslt;
    }

    /**
     * Collect constants referenced in the given expression, in the context of the values of constants.
     *
     * @param expr The expression.
     * @param constants The constant references found so far. Is modified in-place.
     */
    private static void collectConstants(Expression expr, Set<Constant> constants) {
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

        // Apply recursively.
        if (expr instanceof CastExpression) {
            collectConstants(((CastExpression)expr).getChild(), constants);
            return;
        }
        if (expr instanceof UnaryExpression) {
            collectConstants(((UnaryExpression)expr).getChild(), constants);
            return;
        }
        if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            collectConstants(bexpr.getLeft(), constants);
            collectConstants(bexpr.getRight(), constants);
            return;
        }
        if (expr instanceof IfExpression) {
            IfExpression iexpr = (IfExpression)expr;
            for (Expression guard: iexpr.getGuards()) {
                collectConstants(guard, constants);
            }
            collectConstants(iexpr.getThen(), constants);
            for (ElifExpression elif: iexpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    collectConstants(guard, constants);
                }
                collectConstants(elif.getThen(), constants);
            }
            collectConstants(iexpr.getElse(), constants);
            return;
        }
        if (expr instanceof SwitchExpression) {
            SwitchExpression sexpr = (SwitchExpression)expr;
            collectConstants(sexpr.getValue(), constants);
            for (SwitchCase cse: sexpr.getCases()) {
                if (cse.getKey() != null) {
                    collectConstants(cse.getKey(), constants);
                }
                collectConstants(cse.getValue(), constants);
            }
            return;
        }
        if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            collectConstants(pexpr.getChild(), constants);
            collectConstants(pexpr.getIndex(), constants);
            return;
        }
        if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;
            collectConstants(sexpr.getChild(), constants);
            if (sexpr.getBegin() != null) {
                collectConstants(sexpr.getBegin(), constants);
            }
            if (sexpr.getEnd() != null) {
                collectConstants(sexpr.getEnd(), constants);
            }
            return;
        }
        if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fexpr = (FunctionCallExpression)expr;
            for (Expression param: fexpr.getParams()) {
                collectConstants(param, constants);
            }
            collectConstants(fexpr.getFunction(), constants);
            return;
        }
        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                collectConstants(elem, constants);
            }
            return;
        }
        if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                collectConstants(elem, constants);
            }
            return;
        }
        if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression field: texpr.getFields()) {
                collectConstants(field, constants);
            }
            return;
        }
        if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                collectConstants(pair.getKey(), constants);
                collectConstants(pair.getValue(), constants);
            }
            return;
        }

        // Constants can refer to other constants.
        if (expr instanceof ConstantExpression) {
            constants.add(((ConstantExpression)expr).getConstant());
            return;
        }

        // References that may occur in constants. Note that enumeration
        // literals, fields (of tuple types), and standard library functions
        // don't have values. Also note that user-defined functions can't be
        // called/evaluated in constants.
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

        // Not allowed in values of constants:
        // - TimeExpression (part of runtime state)
        // - DiscVariableExpression (discrete variables part of runtime state, function parameters and local variables
        // of functions can not be accessed from outside of functions)
        // - AlgVariableExpression (no static evaluation, may refer to runtime state)
        // - ContVariableExpression (part of runtime state, includes derivatives)
        // - TauExpression (not allowed as value)
        // - LocationExpression (part of runtime state)
        // - EventExpression (not allowed as value)
        // - InputVariableExpression (no static evaluation, value unknown)
        // - ComponentExpression (no static evaluation, may refer to runtime state)
        // - ReceivedExpression (only allowed in updates of edges)
        // - SelfExpression (no static evaluation, may refer to runtime state)

        // Should have been eliminated:
        // - CompInstWrapExpression
        // - CompParamWrapExpression

        throw new RuntimeException("Unexpected expr: " + expr);
    }
}
