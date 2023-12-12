//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifTypeUtils.hashType;

import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;

/** CIF expressions utility methods. */
public class CifExprUtils {
    /** Constructor for the {@link CifExprUtils} class. */
    private CifExprUtils() {
        // Static class.
    }

    /**
     * Hashes the given expression.
     *
     * <p>
     * Component types and component definition types are currently not supported by this method.
     * </p>
     *
     * @param expr The expression.
     * @return The hash of the expression.
     */
    public static int hashExpr(Expression expr) {
        if (expr instanceof BoolExpression bExpr) {
            return bExpr.isValue() ? 1231 : 1237; // Same as 'java.lang.Boolean.hashCode'.
        } else if (expr instanceof IntExpression iExpr) {
            return iExpr.getValue(); // Same as 'java.lang.Integer.hashCode'.
        } else if (expr instanceof RealExpression rExpr) {
            return rExpr.getValue().hashCode();
        } else if (expr instanceof StringExpression sExpr) {
            return sExpr.getValue().hashCode();
        } else if (expr instanceof TimeExpression tExpr) {
            return 1 << 0;
        } else if (expr instanceof CastExpression cExpr) {
            return hashType(cExpr.getType()) + hashExpr(cExpr.getChild());
        } else if (expr instanceof UnaryExpression uExpr) {
            return uExpr.getOperator().hashCode() + hashExpr(uExpr.getChild());
        } else if (expr instanceof BinaryExpression bExpr) {
            return hashExpr(bExpr.getLeft()) + bExpr.getOperator().hashCode() + hashExpr(bExpr.getRight());
        } else if (expr instanceof IfExpression iExpr) {
            int rslt = 1 << 3;
            for (Expression guard: iExpr.getGuards()) {
                rslt += hashExpr(guard);
            }
            rslt += hashExpr(iExpr.getThen());
            for (ElifExpression elifExpr: iExpr.getElifs()) {
                for (Expression guard: elifExpr.getGuards()) {
                    rslt += hashExpr(guard);
                }
                rslt += hashExpr(elifExpr.getThen());
            }
            rslt += hashExpr(iExpr.getElse());
            return rslt;
        } else if (expr instanceof SwitchExpression sExpr) {
            int rslt = 1 << 6;
            rslt += hashExpr(sExpr.getValue());
            for (SwitchCase sCase: sExpr.getCases()) {
                if (sCase.getKey() != null) {
                    rslt += hashExpr(sCase.getKey());
                }
                rslt += hashExpr(sCase.getValue());
            }
            return rslt;
        } else if (expr instanceof ProjectionExpression pExpr) {
            return hashExpr(pExpr.getChild()) + hashExpr(pExpr.getIndex());
        } else if (expr instanceof SliceExpression sExpr) {
            int rslt = hashExpr(sExpr.getChild());
            if (sExpr.getBegin() != null) {
                rslt += hashExpr(sExpr.getBegin());
            }
            if (sExpr.getEnd() != null) {
                rslt += hashExpr(sExpr.getEnd());
            }
            return rslt;
        } else if (expr instanceof FunctionCallExpression fcExpr) {
            int rslt = 1 << 9;
            rslt += hashExpr(fcExpr.getFunction());
            for (Expression argument: fcExpr.getArguments()) {
                rslt += hashExpr(argument);
            }
            return rslt;
        } else if (expr instanceof ListExpression lExpr) {
            int rslt = 1 << 12;
            for (Expression element: lExpr.getElements()) {
                rslt += hashExpr(element);
            }
            return rslt;
        } else if (expr instanceof SetExpression sExpr) {
            int rslt = 1 << 15;
            for (Expression element: sExpr.getElements()) {
                rslt += hashExpr(element);
            }
            return rslt;
        } else if (expr instanceof TupleExpression tExpr) {
            int rslt = 1 << 18;
            for (Expression field: tExpr.getFields()) {
                rslt += hashExpr(field);
            }
            return rslt;
        } else if (expr instanceof DictExpression dExpr) {
            int rslt = 1 << 21;
            for (DictPair pair: dExpr.getPairs()) {
                rslt += hashExpr(pair.getKey()) + hashExpr(pair.getValue());
            }
            return rslt;
        } else if (expr instanceof ConstantExpression cExpr) {
            return cExpr.getConstant().hashCode();
        } else if (expr instanceof DiscVariableExpression dvExpr) {
            return dvExpr.getVariable().hashCode();
        } else if (expr instanceof AlgVariableExpression aExpr) {
            return aExpr.getVariable().hashCode();
        } else if (expr instanceof ContVariableExpression cExpr) {
            return cExpr.getVariable().hashCode();
        } else if (expr instanceof TauExpression) {
            return 1 << 24;
        } else if (expr instanceof LocationExpression lExpr) {
            return lExpr.getLocation().hashCode();
        } else if (expr instanceof EnumLiteralExpression elExpr) {
            return elExpr.getLiteral().hashCode();
        } else if (expr instanceof EventExpression eExpr) {
            return eExpr.getEvent().hashCode();
        } else if (expr instanceof FieldExpression fExpr) {
            return fExpr.getField().getName().hashCode();
        } else if (expr instanceof StdLibFunctionExpression slfExpr) {
            return slfExpr.getFunction().hashCode();
        } else if (expr instanceof FunctionExpression fExpr) {
            return fExpr.getFunction().hashCode();
        } else if (expr instanceof InputVariableExpression iExpr) {
            return iExpr.getVariable().hashCode();
        } else if (expr instanceof ComponentExpression cExpr) {
            return cExpr.getComponent().hashCode();
        } else if (expr instanceof CompParamExpression cpExpr) {
            return cpExpr.getParameter().hashCode();
        } else if (expr instanceof CompInstWrapExpression ciwExpr) {
            return ciwExpr.getInstantiation().hashCode() + hashExpr(ciwExpr.getReference());
        } else if (expr instanceof CompParamWrapExpression cpwExpr) {
            return cpwExpr.getParameter().hashCode() + hashExpr(cpwExpr.getReference());
        } else if (expr instanceof ReceivedExpression) {
            return 1 << 27;
        } else if (expr instanceof SelfExpression) {
            return 1 << 30;
        }

        throw new RuntimeException("Unexpected expression: " + expr.toString());
    }
}
