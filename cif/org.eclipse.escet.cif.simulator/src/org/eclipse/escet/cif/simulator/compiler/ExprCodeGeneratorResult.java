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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Triple.triple;

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Triple;

/**
 * The code generated for an expression. Parts of the code may be assigned to extra methods, to prevent issues with Java
 * code size limits, such as too much code in a single method.
 */
public class ExprCodeGeneratorResult {
    /** The base name used for generating names for the extra methods. */
    public static String methodBaseName = "evalExpression";

    /**
     * The counter with the next number to postfix to the {@link #methodBaseName base method name} to generate a unique
     * method name.
     */
    private static int counter = 0;

    /** The limit after which generated code should be wrapped in separate method. */
    private static int limit = 1000;

    /**
     * The code to place in extra methods. Each triple consists of the expression code, the corresponding method name,
     * and the return type of the method.
     */
    public List<Triple<String, String, String>> subExprs = list();

    /** The expression code that is below the {@link #limit} and thus not (yet) assigned to an extra method. */
    public String currentExprText;

    /**
     * The expression of the {@link #currentExprText}. May be {@code null} if the result represents a list of
     * expressions.
     */
    public Expression expr;

    /**
     * Number of visited expression tree nodes that are captured by {@code currentExprText}. Is reset each time code is
     * assigned to an extra method.
     */
    public int numNodes;

    /**
     * Constructor for the {@link ExprCodeGeneratorResult} class.
     *
     * @param exprCode The initial expression code.
     * @param expr The expression of the code.
     */
    public ExprCodeGeneratorResult(String exprCode, Expression expr) {
        currentExprText = exprCode;
        this.expr = expr;
        numNodes = 1;
    }

    /**
     * Constructor for the {@link ExprCodeGeneratorResult} class.
     *
     * @param result The {@link ExprCodeGeneratorResult} to copy.
     */
    public ExprCodeGeneratorResult(ExprCodeGeneratorResult result) {
        subExprs = result.subExprs;
        currentExprText = result.currentExprText;
        expr = result.expr;
        numNodes = result.numNodes;
    }

    /** Reset the method name postfix counter. */
    public static void resetCounter() {
        counter = 0;
    }

    /**
     * Set the base name used for generating names for the extra methods.
     *
     * @param baseName The new base name.
     */
    public void changeBaseName(String baseName) {
        methodBaseName = baseName;
    }

    // TODO Temporarily disabled non-static merge, because when we use a format string as input, we need the order of
    // results to add. 'others' has an order, but unclear where 'this' to insert...
//    /**
//     * Merge one or more {@link ExprCodeGeneratorResult}s into this result.
//     *
//     * @param mergeFormatString The code string that represents the merging of the results.
//     * @param type The output type of the new method, if created. If {@code null}, no new method is created.
//     * @param others The other {@link ExprCodeGeneratorResult} to be merged into this result.
//     */
//    public void mergeInto(String mergeFormatString, String type, ExprCodeGeneratorResult... others) {
//        mergeInto(mergeFormatString, type, Arrays.asList(others));
//    }
//
//    /**
//     * Merge one or more {@link ExprCodeGeneratorResult}s into this result.
//     *
//     * @param mergeFormatString The code string that represents the merging of the results.
//     * @param type The output type of the new method, if created. If {@code null}, no new method is created.
//     * @param others The other {@link ExprCodeGeneratorResult} to be merged into this result.
//     */
//    public void mergeInto(String mergeFormatString, String type, List<ExprCodeGeneratorResult> others) {
//        // With merging, the {@code subExprs} are added to this one. If the number of combined nodes exceeds the limit,
//        // the current expression code is assigned to a method and the new current code expression text starts with a
//        // call to this method.
//        // TODO We need the order of results to add. 'others' has an order, but unclear where 'this' to insert.
//        currentExprText = fmt(mergeFormatString);
//        for (ExprCodeGeneratorResult other: others) {
//            subExprs.addAll(other.subExprs);
//            numNodes += other.numNodes;
//        }
//
//        if (type != null && numNodes >= limit) {
//            createMethod(type);
//        }
//    }

    /**
     * Merge {@link ExprCodeGeneratorResult}s together. The order of the supplied results should match with the order of
     * placeholders in the format string.
     *
     * @param mergeFormatString The code format string that represents the merging of the results.
     * @param expr The expression of the code.
     * @param ctxt The compiler context to use.
     * @param results The {@link ExprCodeGeneratorResult}s to be merged into this result.
     * @return A merged result.
     */
    public static ExprCodeGeneratorResult merge(String mergeFormatString, Expression expr, CifCompilerContext ctxt,
            ExprCodeGeneratorResult... results)
    {
        return merge(mergeFormatString, expr, ctxt, Arrays.asList(results));
    }

    /**
     * Merge {@link ExprCodeGeneratorResult}s together. The order of the supplied results should match with the order of
     * placeholders in the format string.
     *
     * @param mergeFormatString The format code string that represents the merging of the results.
     * @param expr The expression of the code.
     * @param ctxt The compiler context to use.
     * @param results The {@link ExprCodeGeneratorResult}s to be merged into this result.
     * @return A merged result.
     */
    public static ExprCodeGeneratorResult merge(String mergeFormatString, Expression expr, CifCompilerContext ctxt,
            List<ExprCodeGeneratorResult> results)
    {
        // TODO I am using/abusing fmt to check the whether the number of placeholders match the number of arguments.
        // Should we do this check ourselves or provide better exception catching here?

        if (results.isEmpty()) {
            return new ExprCodeGeneratorResult(fmt(mergeFormatString), expr);
        }

        if (results.size() == 1) {
            results.get(0).updateCurrentExprText(mergeFormatString, expr, ctxt);
            return results.get(0);
        }

        // Prepare the merge.
        Assert.check(limit > results.size()); // Otherwise the merged result will never fit within the limit.
        while (!doesFit(results)) {
            // Identify the largest result.
            ExprCodeGeneratorResult largest = getLargestResult(results);
            largest.createMethod(ctxt);
        }

        String exprText = fmt(mergeFormatString, results.toArray(new ExprCodeGeneratorResult[0]));

        // Perform the actual merge.
        List<ExprCodeGeneratorResult> subResults = results.subList(0, results.size() - 1);
        ExprCodeGeneratorResult lastResult = results.get(results.size() - 1);
        for (ExprCodeGeneratorResult result: subResults) {
            lastResult.subExprs.addAll(result.subExprs);
            lastResult.numNodes += result.numNodes;
        }
        lastResult.currentExprText = exprText;
        lastResult.expr = expr;
        return lastResult;
    }

    /**
     * Assign the current expression code to a new method.
     *
     * @param ctxt The compiler context to use.
     */
    public void createMethod(CifCompilerContext ctxt) {
        // Skip if expr is null, as we cannot fetch a proper return type.
        if (expr == null) {
            return;
        }

        String methodName = fmt("%s%d", methodBaseName, counter);
        subExprs.add(triple(currentExprText, methodName, gencodeType(expr.getType(), ctxt)));
        currentExprText = fmt("%s(state)", methodName);
        counter++;
        numNodes = 1;
    }

    /**
     * Update the current expression code text and, if needed, create a new method.
     *
     * @param formatString The format string in which the current expression code text is inserted.
     * @param expr The expression of the code.
     * @param ctxt The compiler context to use.
     */
    public void updateCurrentExprText(String formatString, Expression expr, CifCompilerContext ctxt) {
        // TODO We now rely upon fmt to check whether the number of placeholders match the number of arguments.
        // Should we do this check ourselves or provide better exception catching here?
        currentExprText = fmt(formatString, this);
        this.expr = expr;
        numNodes++;

        if (numNodes >= limit) {
            createMethod(ctxt);
        }
    }

    /**
     * Check whether {@link ExprCodeGeneratorResult}s would fit together without reaching the limit when being merged.
     *
     * @param results The {@link ExprCodeGeneratorResult}s to check.
     * @return {@code true} if merging the results would remain under the limit, otherwise {@code false}.
     */
    private static boolean doesFit(List<ExprCodeGeneratorResult> results) {
        int total = 0;
        for (ExprCodeGeneratorResult other: results) {
            total += other.numNodes;
        }
        return total < limit;
    }

    /**
     * Get the largest result in terms of number of nodes.
     *
     * @param results The results to search in.
     * @return The largest result in terms of number of nodes.
     */
    private static ExprCodeGeneratorResult getLargestResult(List<ExprCodeGeneratorResult> results) {
        ExprCodeGeneratorResult largest = null;
        int sizeLargest = 0;
        for (ExprCodeGeneratorResult result: results) {
            if (result.numNodes > sizeLargest) {
                largest = result;
            }
        }
        return largest;
    }

    /**
     * Convert a list of {@link ExprCodeGeneratorResult} into a list of strings, where each string is the
     * {@code currentExprText}.
     *
     * @param input The list to convert.
     * @return A list of strings.
     * @see #toString
     */
    public static List<String> convertToStringList(List<ExprCodeGeneratorResult> input) {
        List<String> result = listc(input.size());

        for (ExprCodeGeneratorResult elem: input) {
            result.add(elem.currentExprText);
        }
        return result;
    }

    @Override
    public String toString() {
        return currentExprText;
    }
}
