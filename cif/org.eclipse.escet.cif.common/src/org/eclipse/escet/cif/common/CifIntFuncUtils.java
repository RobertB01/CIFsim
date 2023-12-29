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

import static org.eclipse.escet.common.java.Sets.setc;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;

/** CIF internal function utility methods. */
public class CifIntFuncUtils {
    /** Constructor for the {@link CifIntFuncUtils} class. */
    private CifIntFuncUtils() {
        // Static class.
    }

    /**
     * Get the function parameters of the given function that may get modified in the function.
     *
     * @param func Function to analyze.
     * @return Function parameters that may get modified in the function.
     */
    public static Set<DiscVariable> getAssignedParameters(InternalFunction func) {
        Set<DiscVariable> parameters = setc(func.getParameters().size());

        Deque<FunctionStatement> notDone = new ArrayDeque<>();
        notDone.addAll(func.getStatements());

        while (true) {
            FunctionStatement stat = notDone.poll();
            if (stat == null) {
                break; // Done.
            }

            if (stat instanceof BreakFuncStatement) {
                continue;
            }
            if (stat instanceof ContinueFuncStatement) {
                continue;
            }
            if (stat instanceof ReturnFuncStatement) {
                continue;
            }

            if (stat instanceof IfFuncStatement) {
                IfFuncStatement s = (IfFuncStatement)stat;
                notDone.addAll(s.getThens());
                notDone.addAll(s.getElses());
                for (ElifFuncStatement es: s.getElifs()) {
                    notDone.addAll(es.getThens());
                }
                continue;
            }
            if (stat instanceof WhileFuncStatement) {
                WhileFuncStatement s = (WhileFuncStatement)stat;
                notDone.addAll(s.getStatements());
                continue;
            }
            if (stat instanceof AssignmentFuncStatement) {
                AssignmentFuncStatement s = (AssignmentFuncStatement)stat;
                getAssignedParameters(s, parameters);
                continue;
            }

            throw new RuntimeException("Unexpected/unsupported function statement: " + stat);
        }
        return parameters;
    }

    /**
     * Analyze the LHS of an assignment statement for updates to function parameters. Add such updates to the
     * {@code parameters} set.
     *
     * @param asg Assignment function statement to analyze.
     * @param parameters Function parameters already found to be assigned (updated in-place).
     */
    private static void getAssignedParameters(AssignmentFuncStatement asg, Set<DiscVariable> parameters) {
        Deque<Expression> notDone = new ArrayDeque<>();
        notDone.add(asg.getAddressable());

        while (true) {
            Expression lhs = notDone.poll();
            if (lhs == null) {
                break; // Done.
            }

            if (lhs instanceof ProjectionExpression) {
                ProjectionExpression pe = (ProjectionExpression)lhs;
                notDone.add(pe.getChild());
                continue;
            }
            if (lhs instanceof TupleExpression) {
                TupleExpression te = (TupleExpression)lhs;
                notDone.addAll(te.getFields());
                continue;
            }
            if (lhs instanceof DiscVariableExpression) {
                DiscVariableExpression ve = (DiscVariableExpression)lhs;
                DiscVariable var = ve.getVariable();
                EObject parent = var.eContainer();
                if (parent instanceof FunctionParameter) {
                    parameters.add(var);
                }
                continue;
            }

            throw new RuntimeException("Unexpected/unsupported LHS expression: " + lhs);
        }
    }

    /**
     * Returns whether the given discrete variable object represents a parameter of an internal user-defined function.
     *
     * @param discVar The discrete variable object.
     * @return {@code true} if the discrete variable object represents a parameter of an internal user-defined function,
     *     {@code false} otherwise.
     */
    public static boolean isFuncParam(DiscVariable discVar) {
        return discVar.eContainer() instanceof FunctionParameter;
    }

    /**
     * Returns whether the given discrete variable object represents a local variable of an internal user-defined
     * function.
     *
     * @param discVar The discrete variable object.
     * @return {@code true} if the discrete variable object represents a local variable of an internal user-defined
     *     function, {@code false} otherwise.
     */
    public static boolean isFuncLocalVar(DiscVariable discVar) {
        return discVar.eContainer() instanceof InternalFunction;
    }

    /**
     * Returns whether the given discrete variable object represents a parameter or local variable of an internal
     * user-defined function.
     *
     * @param discVar The discrete variable object.
     * @return {@code true} if the discrete variable object represents a parameter or local variable of an internal
     *     user-defined function, {@code false} otherwise.
     */
    public static boolean isFuncParamOrLocalVar(DiscVariable discVar) {
        return isFuncParam(discVar) || isFuncLocalVar(discVar);
    }
}
