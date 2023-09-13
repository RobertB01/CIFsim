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
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.java.Assert;

/**
 * The code generated for an expression. Parts of the code may be assigned to extra methods, to prevent issues with Java
 * code size limits, such as too much code in a single method.
 *
 * @param extraMethods List of new extra methods.
 * @param exprCode The expression code that is below the {@link #LIMIT} and thus not (yet) assigned to an extra method.
 * @param type The type of the generated code.
 * @param numNodes Number of visited expression tree nodes that are captured by the generated code. Is reset each time
 *     code is assigned to an extra method.
 */
public record ExprCodeGeneratorResult(List<ExtraMethod> extraMethods, String exprCode, CifType type,
        int numNodes)
{
    /** The base name used for generating names for the extra methods. */
    private static final String METHOD_BASE_NAME = "evalExpression";

    /** The limit after which generated code should be wrapped in separate method. */
    private static final int LIMIT = 1000;

    /**
     * Constructor for the {@link ExprCodeGeneratorResult} class.
     *
     * @param exprCode The initial expression code.
     * @param type The type of the generated code.
     */
    public ExprCodeGeneratorResult(String exprCode, CifType type) {
        this(list(), exprCode, type, 1);
    }

    /**
     * Merge {@link ExprCodeGeneratorResult}s together. The order of the supplied results should match with the order of
     * the placeholders in the format string.
     *
     * @param mergeFormatString The code format string that represents the merging of the results.
     * @param type The type of the generated code.
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
     * the placeholders in the format string.
     *
     * @param mergeFormatString The format code string that represents the merging of the results.
     * @param type The type of the generated code.
     * @param ctxt The compiler context to use.
     * @param results The {@link ExprCodeGeneratorResult}s to be merged into this result.
     * @return A merged result.
     */
    public static ExprCodeGeneratorResult merge(String mergeFormatString, CifType type, CifCompilerContext ctxt,
            List<ExprCodeGeneratorResult> results)
    {
        // TODO I am using/abusing fmt to check the whether the number of placeholders match the number of arguments.
        // Should we do this check ourselves or provide better exception catching here?

        // Optimization for no results to be merged (format string has the full code).
        if (results.isEmpty()) {
            return new ExprCodeGeneratorResult(fmt(mergeFormatString), type);
        }

        // Prepare the merge.
        Assert.check(LIMIT > results.size()); // Otherwise the merged result will never fit within the limit.
        List<ExprCodeGeneratorResult> resultsCopy = copy(results); // Make copy so we can mutate the list.
        while (!areUnderTheLimit(resultsCopy)) {
            // Identify the largest result.
            int indexLargest = getLargestResult(resultsCopy);
            ExprCodeGeneratorResult largest = resultsCopy.get(indexLargest);
            ExprCodeGeneratorResult newLargest = createMethod(largest, ctxt);
            resultsCopy.set(indexLargest, newLargest);

            // Replace all instances of the largest result (there may be duplicates) with the new method, keeping
            // the order of the results intact.
            for (int index = indexLargest + 1; index < resultsCopy.size(); index++) {
                if (resultsCopy.get(index).equals(largest)) {
                    resultsCopy.set(index, newLargest);
                }
            }
        }

        String exprText = fmt(mergeFormatString, resultsCopy.toArray(new ExprCodeGeneratorResult[resultsCopy.size()]));

        // Perform the actual merge.
        // We want to keep the multiplicity of duplicates for the total number of nodes in the merged results, but we
        // dont't want duplicates in mergedExtraMethods.
        List<ExtraMethod> mergedExtraMethods = list();
        int mergedNumNodes = 0;
        Set<ExprCodeGeneratorResult> seen = set();
        for (ExprCodeGeneratorResult result: resultsCopy) {
            if (seen.add(result)) {
                mergedExtraMethods.addAll(result.extraMethods());
            }
            mergedNumNodes += result.numNodes();
        }

        return new ExprCodeGeneratorResult(mergedExtraMethods, exprText, type, mergedNumNodes);
    }

    /**
     * Assign the supplied expression code to a new extra method.
     *
     * @param result The result to encapsulate with a new extra method.
     * @param ctxt The compiler context to use.
     * @return New result where the current expression code is assigned to a new extra method.
     */
    private static ExprCodeGeneratorResult createMethod(ExprCodeGeneratorResult result, CifCompilerContext ctxt) {
        // Sanity check: make sure a type is available.
        Assert.notNull(result.type());

        List<ExtraMethod> newSubExprs = result.extraMethods();
        String methodName = fmt("%s%d", METHOD_BASE_NAME, ctxt.exprCodeGenExtraMethodCounter.getAndIncrement());
        newSubExprs.add(new ExtraMethod(result.exprCode(), methodName, gencodeType(result.type(), ctxt)));

        return new ExprCodeGeneratorResult(newSubExprs, fmt("%s(state)", methodName), result.type(), 1);
    }

    /**
     * Check whether {@link ExprCodeGeneratorResult}s would fit together without reaching the limit.
     *
     * @param results The {@link ExprCodeGeneratorResult}s to check.
     * @return {@code true} if the combined counts would remain under the limit, otherwise {@code false}.
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
    private static int getLargestResult(List<ExprCodeGeneratorResult> results) {
        Assert.check(!results.isEmpty());

        int largestSize = results.get(0).numNodes();
        int largestIndex = 0;
        for (int i = 1; i < results.size(); i++) {
            if (results.get(i).numNodes() > largestSize) {
                largestSize = results.get(i).numNodes();
                largestIndex = i;
            }
        }

        return largestIndex;
    }

    @Override
    public String toString() {
        return exprCode;
    }

    /**
     * Extra method that encapsulate an expression.
     *
     * @param bodyCode The code to be put inside the body of the extra method.
     * @param name The name of the extra method.
     * @param type The return type of the extra method.
     */
    public record ExtraMethod(String bodyCode, String name, String type) {
    }
}
