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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Triple.triple;

import java.util.Arrays;
import java.util.List;

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
     * Number of visited expression tree nodes that are captured by {@code currentExprText}. Is reset each time code is
     * assigned to an extra method.
     */
    public int numNodes;

    /**
     * Constructor for the {@link ExprCodeGeneratorResult} class.
     *
     * @param exprCode The initial expression code.
     */
    public ExprCodeGeneratorResult(String exprCode) {
        currentExprText = exprCode;
        numNodes = 1;
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

    /**
     * Merge one or more {@link ExprCodeGeneratorResult}s into this result.
     *
     * @param mergeString The code string that represents the merging of the results.
     * @param type The output type of the new method, if created. If {@code null}, no new method is created.
     * @param others The other {@link ExprCodeGeneratorResult} to be merged into this object.
     */
    public void mergeInto(String mergeString, String type, ExprCodeGeneratorResult... others) {
        mergeInto(mergeString, type, Arrays.asList(others));
    }

    /**
     * Merge one or more {@link ExprCodeGeneratorResult}s into this result.
     *
     * @param mergeString The code string that represents the merging of the results.
     * @param type The output type of the new method, if created. If {@code null}, no new method is created.
     * @param others The other {@link ExprCodeGeneratorResult} to be merged into this object.
     */
    public void mergeInto(String mergeString, String type, List<ExprCodeGeneratorResult> others) {
        // With merging, the {@code subExprs} are added to this one. If the number of combined nodes exceeds the limit,
        // the current expression code is assigned to a method and the new current code expression text starts with a
        // call to this method.
        currentExprText = mergeString;
        for (ExprCodeGeneratorResult other: others) {
            subExprs.addAll(other.subExprs);
            numNodes = numNodes + other.numNodes;
        }

        if (type != null && numNodes >= limit) {
            createMethod(type);
        }
    }

    /**
     * Merge {@link ExprCodeGeneratorResult} together.
     *
     * <p>
     * With merging, the {@code subExprs} are added together. If the number of combined nodes exceeds the limit, the
     * current expression code is assigned to a method and the new current code expression text starts with a call to
     * this method.
     * </p>
     *
     * @param mergeString The code string that represents the merging of the results.
     * @param type The output type of the new method, if created. If {@code null}, no new method is created.
     * @param results The other {@link ExprCodeGeneratorResult} to be merged into this object.
     * @return A merged result object.
     */
    public static ExprCodeGeneratorResult merge(String mergeString, String type,
            List<ExprCodeGeneratorResult> results)
    {
        if (results.isEmpty()) {
            return new ExprCodeGeneratorResult(mergeString);
        }

        if (results.size() == 1) {
            results.get(0).updateCurrentExprText(mergeString, type);
            return results.get(0);
        }

        List<ExprCodeGeneratorResult> subResults = results.subList(0, results.size() - 1);
        ExprCodeGeneratorResult lastResult = results.get(results.size() - 1);
        lastResult.mergeInto(mergeString, type, subResults);
        return lastResult;
    }

    /**
     * Assign the current expression code to a new method.
     *
     * @param type The output type of the new method.
     */
    public void createMethod(String type) {
        String methodName = fmt("%s%d", methodBaseName, counter);
        subExprs.add(triple(currentExprText, methodName, type));
        currentExprText = fmt("%s(state)", methodName);
        counter++;
        numNodes = 1;
    }

    /**
     * Update the current expression code text and, if needed, create a new method.
     *
     * @param newExprText The text to replace the current expression code text.
     * @param type The output type of the new method, if created. If {@code null}, no new method is created.
     */
    public void updateCurrentExprText(String newExprText, String type) {
        currentExprText = newExprText;
        numNodes++;

        if (type != null && numNodes >= limit) {
            createMethod(type);
        }
    }

    /**
     * Check whether other {@link ExprCodeGeneratorResult} would fit into this one without reaching the limit.
     *
     * @param others The other {@link ExprCodeGeneratorResult}.
     * @return {@code true} if merging the others would remain under the limit, otherwise {@code false}.
     */
    public boolean doesFit(ExprCodeGeneratorResult... others) {
        int total = numNodes;
        for (ExprCodeGeneratorResult other: others) {
            total += other.numNodes;
        }
        return total < limit;
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
