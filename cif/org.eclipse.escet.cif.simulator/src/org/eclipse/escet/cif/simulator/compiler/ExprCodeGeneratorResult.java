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
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Triple.triple;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Triple;

/**
 * The code generated for an expression. Parts of the code may be assigned to extra methods, to prevent issues with Java
 * code size limits, such as too much code in a single method.
 *
 * @param subExprs The code to place in extra methods. Each triple consists of the expression code, the corresponding
 *     method name, and the return type of the method.
 * @param currentExprText The expression code that is below the {@link #LIMIT} and thus not (yet) assigned to an extra
 *     method.
 * @param type The expression's type for which the code was generated.
 * @param numNodes Number of visited expression tree nodes that are captured by the generated code. Is reset each time
 *     code is assigned to an extra method.
 */
public record ExprCodeGeneratorResult(List<Triple<String, String, String>> subExprs, String currentExprText,
        CifType type, int numNodes)
{
    /** The base name used for generating names for the extra methods. */
    public static final String METHOD_BASE_NAME = "evalExpression";

    /** The limit after which generated code should be wrapped in separate method. */
    private static final int LIMIT = 1000;

    /**
     * Constructor for the {@link ExprCodeGeneratorResult} class.
     *
     * @param currentExprText The initial expression code.
     * @param type The type of the code.
     */
    public ExprCodeGeneratorResult(String currentExprText, CifType type) {
        this(list(), currentExprText, type, 1);
    }

    /**
     * Merge {@link ExprCodeGeneratorResult}s together. The order of the supplied results should match with the order of
     * placeholders in the format string.
     *
     * @param mergeFormatString The code format string that represents the merging of the results.
     * @param type The type of the code.
     * @param ctxt The compiler context to use.
     * @param results The {@link ExprCodeGeneratorResult}s to be merged into this result.
     * @return A merged result.
     */
    public static ExprCodeGeneratorResult merge(String mergeFormatString, CifType type, CifCompilerContext ctxt,
            ExprCodeGeneratorResult... results)
    {
        return merge(mergeFormatString, type, ctxt, Arrays.asList(results));
    }

    /**
     * Merge {@link ExprCodeGeneratorResult}s together. The order of the supplied results should match with the order of
     * placeholders in the format string.
     *
     * @param mergeFormatString The format code string that represents the merging of the results.
     * @param type The type of the code.
     * @param ctxt The compiler context to use.
     * @param results The {@link ExprCodeGeneratorResult}s to be merged into this result.
     * @return A merged result.
     */
    public static ExprCodeGeneratorResult merge(String mergeFormatString, CifType type, CifCompilerContext ctxt,
            List<ExprCodeGeneratorResult> results)
    {
        // TODO I am using/abusing fmt to check the whether the number of placeholders match the number of arguments.
        // Should we do this check ourselves or provide better exception catching here?

        if (results.isEmpty()) {
            return new ExprCodeGeneratorResult(fmt(mergeFormatString), type);
        }

        // Prepare the merge.
        Assert.check(LIMIT > results.size()); // Otherwise the merged result will never fit within the limit.
        List<ExprCodeGeneratorResult> resultsCopy = list(); // Make copy so we can mutate the list.
        resultsCopy.addAll(results);
        while (!areUnderTheLimit(resultsCopy)) {
            // Identify the largest result.
            ExprCodeGeneratorResult largest = getLargestResult(resultsCopy);
            ExprCodeGeneratorResult newLargest = createMethod(largest, ctxt);
            for (int index; (index = resultsCopy.indexOf(largest)) >= 0;) {
                resultsCopy.set(index, newLargest);
            }
        }

        String exprText = fmt(mergeFormatString, resultsCopy.toArray(new ExprCodeGeneratorResult[0]));

        // Perform the actual merge.
        // We want to keep the multiplicity of duplicates for the total number of nodes in the merged results, but we
        // dont't want duplicates in mergedSubExprs.
        List<Triple<String, String, String>> mergedSubExprs = list();
        int mergedNumNodes = 0;
        Set<ExprCodeGeneratorResult> seen = set();
        for (ExprCodeGeneratorResult result: resultsCopy) {
            if (seen.add(result)) {
                mergedSubExprs.addAll(result.subExprs());
            }
            mergedNumNodes += result.numNodes();
        }

        return new ExprCodeGeneratorResult(mergedSubExprs, exprText, type, mergedNumNodes);
    }

    /**
     * Assign the supplied expression code to a new method.
     *
     * @param result The result to encapsulate with a new method.
     * @param ctxt The compiler context to use.
     * @return New result where the current expression code is assigned to a new method, if possible.
     */
    private static ExprCodeGeneratorResult createMethod(ExprCodeGeneratorResult result, CifCompilerContext ctxt) {
        // Skip if expr is null, as we cannot fetch a proper return type.
        // TODO See if we can get rid of having potential null expressions.
        Assert.notNull(result.type()); // TODO To see which expressions are still problematic.
        if (result.type() == null) {
            return result;
        }

        List<Triple<String, String, String>> newSubExprs = result.subExprs();
        String methodName = fmt("%s%d", METHOD_BASE_NAME, ctxt.atomicIntegerGenerator.getAndIncrement());
        newSubExprs.add(triple(result.currentExprText(), methodName, gencodeType(result.type(), ctxt)));

        return new ExprCodeGeneratorResult(newSubExprs, fmt("%s(state)", methodName), result.type(), 1);
    }

    /**
     * Check whether {@link ExprCodeGeneratorResult}s would fit together without reaching the limit when being merged.
     *
     * @param results The {@link ExprCodeGeneratorResult}s to check.
     * @return {@code true} if merging the results would remain under the limit, otherwise {@code false}.
     */
    private static boolean areUnderTheLimit(List<ExprCodeGeneratorResult> results) {
        int total = 0;
        for (ExprCodeGeneratorResult result: results) {
            total += result.numNodes;
        }
        return total < LIMIT;
    }

    /**
     * Get the index of the largest result in terms of number of nodes (or the first of them in case there are
     * multiple).
     *
     * @param results The results from which to return the largest one.
     * @return The index of the largest result in terms of number of nodes (or the first of them in case there are
     *     multiple).
     */
    private static ExprCodeGeneratorResult getLargestResult(List<ExprCodeGeneratorResult> results) {
        Assert.check(!results.isEmpty());

        ExprCodeGeneratorResult largest = results.get(0);
        int largestSize = results.get(0).numNodes();
        for (int i = 1; i < results.size(); i++) {
            if (results.get(i).numNodes() > largestSize) {
                largest = results.get(i);
                largestSize = largest.numNodes();
            }
        }

        return largest;
    }

    /**
     * Convert a list of {@link ExprCodeGeneratorResult}s into a list of strings, where each string is the code
     * generated.
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
