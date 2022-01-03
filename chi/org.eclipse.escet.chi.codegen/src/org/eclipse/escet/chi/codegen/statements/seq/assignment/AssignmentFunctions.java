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

package org.eclipse.escet.chi.codegen.statements.seq.assignment;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.BoolLiteral;
import org.eclipse.escet.chi.metamodel.chi.CallExpression;
import org.eclipse.escet.chi.metamodel.chi.CastExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelExpression;
import org.eclipse.escet.chi.metamodel.chi.ConstantReference;
import org.eclipse.escet.chi.metamodel.chi.DictionaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictionaryPair;
import org.eclipse.escet.chi.metamodel.chi.EnumValueReference;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FieldReference;
import org.eclipse.escet.chi.metamodel.chi.FunctionReference;
import org.eclipse.escet.chi.metamodel.chi.IntNumber;
import org.eclipse.escet.chi.metamodel.chi.ListExpression;
import org.eclipse.escet.chi.metamodel.chi.MatrixExpression;
import org.eclipse.escet.chi.metamodel.chi.MatrixRow;
import org.eclipse.escet.chi.metamodel.chi.ModelReference;
import org.eclipse.escet.chi.metamodel.chi.ProcessReference;
import org.eclipse.escet.chi.metamodel.chi.ReadCallExpression;
import org.eclipse.escet.chi.metamodel.chi.RealNumber;
import org.eclipse.escet.chi.metamodel.chi.SetExpression;
import org.eclipse.escet.chi.metamodel.chi.SliceExpression;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference;
import org.eclipse.escet.chi.metamodel.chi.StringLiteral;
import org.eclipse.escet.chi.metamodel.chi.TimeLiteral;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VariableReference;
import org.eclipse.escet.common.java.Assert;

/** Useful functions in the assignment code generation. */
public class AssignmentFunctions {
    /** Constructor of the {@link AssignmentFunctions} class. */
    private AssignmentFunctions() {
        // Static class.
    }

    /**
     * Construct a projection of a Java tuple value using the provided indices.
     *
     * @param tupleVar Java variable holding the tuple to project.
     * @param rhsIndices Indices of the projections to apply. {@code null} means 'no projections'.
     * @return Java expression denoting the selected part of the variable.
     */
    public static String makeTupleSelection(String tupleVar, List<Integer> rhsIndices) {
        if (rhsIndices == null) {
            return tupleVar;
        }

        for (int index: rhsIndices) {
            tupleVar += fmt(".var%d", index);
        }
        return tupleVar;
    }

    /**
     * Collect the used variables in an expression.
     *
     * @param expr Expression to check.
     * @param vars Found variables of the expression (modified in-place).
     */
    public static void expressionCollectVariables(Expression expr, Set<VariableDeclaration> vars) {
        if (expr instanceof BoolLiteral) {
            return;
        } else if (expr instanceof RealNumber) {
            return;
        } else if (expr instanceof IntNumber) {
            return;
        } else if (expr instanceof StringLiteral) {
            return;
        } else if (expr instanceof TimeLiteral) {
            return;
        } else if (expr instanceof StdLibFunctionReference) {
            return;
        } else if (expr instanceof ChannelExpression) {
            return;
        } else if (expr instanceof ModelReference) {
            return;
        } else if (expr instanceof ProcessReference) {
            return;
        } else if (expr instanceof FieldReference) {
            return;
        } else if (expr instanceof EnumValueReference) {
            return;
        } else if (expr instanceof FunctionReference) {
            return;
        } else if (expr instanceof ConstantReference) {
            return;
        }

        if (expr instanceof BinaryExpression) {
            BinaryExpression e = (BinaryExpression)expr;
            expressionCollectVariables(e.getLeft(), vars);
            expressionCollectVariables(e.getRight(), vars);
            return;
        }

        if (expr instanceof CallExpression) {
            CallExpression ce = (CallExpression)expr;
            expressionCollectVariables(ce.getFunction(), vars);
            for (Expression arg: ce.getArguments()) {
                expressionCollectVariables(arg, vars);
            }
            return;
        }

        if (expr instanceof DictionaryExpression) {
            DictionaryExpression de = (DictionaryExpression)expr;
            for (DictionaryPair pair: de.getPairs()) {
                expressionCollectVariables(pair.getKey(), vars);
                expressionCollectVariables(pair.getValue(), vars);
            }
            return;
        }

        if (expr instanceof TupleExpression) {
            TupleExpression te = (TupleExpression)expr;
            for (Expression tf: te.getFields()) {
                expressionCollectVariables(tf, vars);
            }
            return;
        }

        if (expr instanceof SetExpression) {
            SetExpression se = (SetExpression)expr;
            for (Expression elm: se.getElements()) {
                expressionCollectVariables(elm, vars);
            }
            return;
        }

        if (expr instanceof ListExpression) {
            ListExpression le = (ListExpression)expr;
            for (Expression elm: le.getElements()) {
                expressionCollectVariables(elm, vars);
            }
            return;
        }

        if (expr instanceof MatrixExpression) {
            MatrixExpression me = (MatrixExpression)expr;
            for (MatrixRow row: me.getRows()) {
                for (Expression elm: row.getElements()) {
                    expressionCollectVariables(elm, vars);
                }
            }
            return;
        }

        if (expr instanceof ReadCallExpression) {
            ReadCallExpression read = (ReadCallExpression)expr;
            if (read.getFile() != null) {
                expressionCollectVariables(read.getFile(), vars);
            }
            return;
        }

        if (expr instanceof SliceExpression) {
            SliceExpression se = (SliceExpression)expr;
            expressionCollectVariables(se.getSource(), vars);
            if (se.getStart() != null) {
                expressionCollectVariables(se.getStart(), vars);
            }
            if (se.getEnd() != null) {
                expressionCollectVariables(se.getEnd(), vars);
            }
            if (se.getStep() != null) {
                expressionCollectVariables(se.getStep(), vars);
            }
            return;
        }

        if (expr instanceof UnaryExpression) {
            UnaryExpression ue = (UnaryExpression)expr;
            expressionCollectVariables(ue.getChild(), vars);
            return;
        }

        if (expr instanceof VariableReference) {
            VariableReference vr = (VariableReference)expr;
            vars.add(vr.getVariable());
            return;
        }

        if (expr instanceof CastExpression) {
            CastExpression ce = (CastExpression)expr;
            expressionCollectVariables(ce.getExpression(), vars);
            return;
        }
        Assert.fail("Unknown type of expression encountered.");
    }
}
