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

package org.eclipse.escet.cif.codegen.updates;

import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;

/** Static class for finding used and assigned declarations. */
public class FindDeclarationUsage {
    /** Constructor of the {@link FindDeclarationUsage} class. */
    private FindDeclarationUsage() {
        // Static class.
    }

    /**
     * Collect {@link Declaration} objects from the left hand side expression, and add them to the used or assigned
     * declarations.
     *
     * @param expr Left hand side expression to search for declarations.
     * @param rwDecls Found read and written declarations (modified in-place).
     */
    public static void collectAssign(Expression expr, ReadWriteDeclarations rwDecls) {
        while (expr instanceof ProjectionExpression) {
            ProjectionExpression expr2 = (ProjectionExpression)expr;
            collectUse(expr2.getIndex(), rwDecls.read);
            expr = expr2.getChild();
        }

        if (expr instanceof DiscVariableExpression) {
            rwDecls.written.add(VariableWrapper.makeVariableWrapper(expr));
            return;
        }

        if (expr instanceof ContVariableExpression) {
            rwDecls.written.add(VariableWrapper.makeVariableWrapper(expr));
            return;
        }

        if (expr instanceof TupleExpression) {
            TupleExpression expr2 = (TupleExpression)expr;
            for (Expression e: expr2.getFields()) {
                collectAssign(e, rwDecls);
            }
            return;
        }

        throw new RuntimeException("Unexpected assigned expression node " + str(expr));
    }

    /**
     * Collect {@link Declaration} objects from the expression, and add them to the used declarations.
     *
     * <p>
     * Only <em>time</em>, <em>discrete</em> variables, <em>algebraic</em> variables, and <em>continuous</em> variables
     * are collected since these are of interest to the code generator.
     * </p>
     *
     * @param expr Expression to search for declarations.
     * @param accessedDecls Collected declarations that are accessed for reading, modified in-place.
     */
    public static void collectUse(Expression expr, Set<VariableWrapper> accessedDecls) {
        if (expr instanceof BoolExpression) {
            return;
        } else if (expr instanceof IntExpression) {
            return;
        } else if (expr instanceof RealExpression) {
            return;
        } else if (expr instanceof StringExpression) {
            return;
        } else if (expr instanceof EnumLiteralExpression) {
            return;
        } else if (expr instanceof BaseFunctionExpression) {
            return;
        } else if (expr instanceof FieldExpression) {
            return;
        }

        // Constants and input variables are not relevant to collect since they
        // are always available and are never written by the controller.
        if (expr instanceof ConstantExpression) {
            return;
        } else if (expr instanceof InputVariableExpression) {
            return;
        }

        if (expr instanceof TimeExpression) {
            accessedDecls.add(VariableWrapper.makeVariableWrapper(expr));
            return;
        }

        if (expr instanceof CastExpression) {
            CastExpression expr2 = (CastExpression)expr;
            collectUse(expr2.getChild(), accessedDecls);
            return;
        }

        if (expr instanceof UnaryExpression) {
            UnaryExpression expr2 = (UnaryExpression)expr;
            collectUse(expr2.getChild(), accessedDecls);
            return;
        }

        if (expr instanceof BinaryExpression) {
            BinaryExpression expr2 = (BinaryExpression)expr;
            collectUse(expr2.getLeft(), accessedDecls);
            collectUse(expr2.getRight(), accessedDecls);
            return;
        }

        if (expr instanceof IfExpression) {
            IfExpression expr2 = (IfExpression)expr;
            collectUse(expr2.getGuards(), accessedDecls);
            collectUse(expr2.getThen(), accessedDecls);
            collectUseElifs(expr2.getElifs(), accessedDecls);
            collectUse(expr2.getElse(), accessedDecls);
            return;
        }

        if (expr instanceof ProjectionExpression) {
            ProjectionExpression expr2 = (ProjectionExpression)expr;
            collectUse(expr2.getChild(), accessedDecls);
            collectUse(expr2.getIndex(), accessedDecls);
            return;
        }

        if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression expr2 = (FunctionCallExpression)expr;
            collectUse(expr2.getFunction(), accessedDecls);
            collectUse(expr2.getParams(), accessedDecls);
            return;
        }

        if (expr instanceof ListExpression) {
            ListExpression expr2 = (ListExpression)expr;
            collectUse(expr2.getElements(), accessedDecls);
            return;
        }

        if (expr instanceof TupleExpression) {
            TupleExpression expr2 = (TupleExpression)expr;
            collectUse(expr2.getFields(), accessedDecls);
            return;
        }

        if (expr instanceof DiscVariableExpression) {
            accessedDecls.add(VariableWrapper.makeVariableWrapper(expr));
            return;
        }

        if (expr instanceof AlgVariableExpression) {
            accessedDecls.add(VariableWrapper.makeVariableWrapper(expr));
            return;
        }

        if (expr instanceof ContVariableExpression) {
            accessedDecls.add(VariableWrapper.makeVariableWrapper(expr));
            return;
        }

        throw new RuntimeException("Unexpected usage expression node " + str(expr));
    }

    /**
     * Collect {@link Declaration} objects from the expressions, and add them to the used declarations.
     *
     * @param exprs Expressions to search for declarations.
     * @param accessedDecls Collected declarations that are accessed for reading, modified in-place.
     */
    public static void collectUse(List<Expression> exprs, Set<VariableWrapper> accessedDecls) {
        for (Expression e: exprs) {
            collectUse(e, accessedDecls);
        }
    }

    /**
     * Collect {@link Declaration} objects from the expressions, and add them to the used declarations.
     *
     * @param elifs Expressions to search for declarations.
     * @param accessedDecls Collected declarations that are accessed for reading, modified in-place.
     */
    private static void collectUseElifs(List<ElifExpression> elifs, Set<VariableWrapper> accessedDecls) {
        for (ElifExpression elif: elifs) {
            collectUse(elif.getGuards(), accessedDecls);
            collectUse(elif.getThen(), accessedDecls);
        }
    }
}
