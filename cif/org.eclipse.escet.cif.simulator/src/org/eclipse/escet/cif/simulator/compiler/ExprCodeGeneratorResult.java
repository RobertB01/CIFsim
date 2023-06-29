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
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.java.Pair;

/**
 * Keeps track of the generated code for expressions.
 */
public class ExprCodeGeneratorResult {
    /** Base name of the method names to be created. */
    public static String methodBaseName;

    /** Counter to postfix the method name with unique number. */
    private static int counter = 0;

    /** The limit after which generated code should be wrapped in separate method. */
    private static int limit = 10000000;

    /**
     * List of code that needs to go in a separate method, where each pair consists of the expression code and the
     * corresponding method name.
     */
    public List<Pair<String, String>> subExprs = list();

    /** The expression code currently being build and not (yet) assigned to a separate method call. */
    public String currentExprText = "";

    /**
     * Number of visited expression tree nodes that are captured by {@code currentExprText}. Is reset each time code is
     * assigned to a separate method.
     */
    public int numNodes = 0;

    /**
     * Constructor for the {@link ExprCodeGeneratorResult} class.
     *
     * @param exprCode The initial expression code.
     */
    public ExprCodeGeneratorResult(String exprCode) {
        currentExprText = exprCode;
        numNodes = 1;
    }

    /** Reset the method name counter. */
    public void resetCounter() {
        counter = 0;
    }

    /**
     * Change the base name used for generating new method names.
     *
     * @param baseName The new base name.
     */
    public void changeBaseName(String baseName) {
        methodBaseName = baseName;
    }

    /**
     * Merge one or more {@link ExprCodeGeneratorResult} into this object.
     *
     * <p>
     * With merging, the {@code subExprs} are added to this one. If the number of combined nodes exceeds the limit, the
     * current expression code is assigned to a method and the new current code expression text starts with a call to
     * this method.
     * </p>
     *
     * @param mergeString The code string that represents the merging of the results.
     * @param others The other {@link ExprCodeGeneratorResult} to be merged into this object.
     */
    public void merge(String mergeString, ExprCodeGeneratorResult... others) {
        currentExprText = mergeString;
        for (ExprCodeGeneratorResult other: others) {
            subExprs.addAll(other.subExprs);
            numNodes = numNodes + other.numNodes;
        }

        if (numNodes >= limit) {
            createMethod();
        }
    }

    /**
     * Merge one or more {@link ExprCodeGeneratorResult} into this object.
     *
     * <p>
     * With merging, the {@code subExprs} are added to this one. If the number of combined nodes exceeds the limit, the
     * current expression code is assigned to a method and the new current code expression text starts with a call to
     * this method.
     * </p>
     *
     * @param mergeString The code string that represents the merging of the results.
     * @param others The other {@link ExprCodeGeneratorResult} to be merged into this object.
     */
    public void merge(String mergeString, List<ExprCodeGeneratorResult> others) {
        currentExprText = mergeString;
        for (ExprCodeGeneratorResult other: others) {
            subExprs.addAll(other.subExprs);
            numNodes = numNodes + other.numNodes;
        }

        if (numNodes >= limit) {
            createMethod();
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
     * @param results The other {@link ExprCodeGeneratorResult} to be merged into this object.
     * @return A merged result object.
     */
    public static ExprCodeGeneratorResult mergeStatic(String mergeString, List<ExprCodeGeneratorResult> results) {
        if (results.isEmpty()) {
            return new ExprCodeGeneratorResult(mergeString);
        }

        if (results.size() == 1) {
            results.get(0).updateCurrentExprText(mergeString);
            return results.get(0);
        }

        ExprCodeGeneratorResult lastResult = results.remove(results.size() - 1);
        lastResult.merge(mergeString, results);
        return lastResult;
    }

    /** Assign the current expression code to a new method. */
    public void createMethod() {
        String methodName = fmt("%s%d", methodBaseName, counter);
        subExprs.add(pair(currentExprText, methodName));
        currentExprText = fmt("%s()", methodName);
        counter = counter + 1;
        numNodes = 1;
    }

    /**
     * Update the current expression code text and, if needed, create a new method.
     *
     * @param newExprText The text to replace the current expression code text.
     */
    public void updateCurrentExprText(String newExprText) {
        currentExprText = newExprText;
        numNodes = numNodes + 1;

        if (numNodes >= limit) {
            createMethod();
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
            total = total + other.numNodes;
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
