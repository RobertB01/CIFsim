//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.annotations;

import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** Helper for annotation providers. */
public class AnnotationProviderHelper {
    /** Constructor for the {@link AnnotationProviderHelper} class. */
    private AnnotationProviderHelper() {
        // Static class.
    }

    /**
     * Check an annotation argument for being a boolean literal.
     *
     * @param annotation The annotation that has the annotation argument.
     * @param arg The annotation argument to check.
     * @param reporter The reporter to use to report problems in the annotation argument.
     * @return Whether the argument is OK, and no violations are reported for it by this method.
     */
    public static boolean checkBoolLiteralArg(Annotation annotation, AnnotationArgument arg,
            AnnotationProblemReporter reporter)
    {
        // Make sure the argument is a boolean literal.
        if (!(arg.getValue() instanceof BoolExpression)) {
            reporter.reportProblem(annotation, "argument value must be a boolean literal.",
                    arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
            return false;
        }
        return true;
    }

    /**
     * Check an annotation argument for being a non-negative integer literal.
     *
     * <p>
     * This checks for the value being an {@link IntExpression}. It therefore only supports non-negative integer values.
     * </p>
     *
     * @param annotation The annotation that has the annotation argument.
     * @param arg The annotation argument to check.
     * @param reporter The reporter to use to report problems in the annotation argument.
     * @return Whether the argument is OK, and no violations are reported for it by this method.
     */
    public static boolean checkNonNegativeIntLiteralArg(Annotation annotation, AnnotationArgument arg,
            AnnotationProblemReporter reporter)
    {
        // Make sure the argument is an integer literal.
        if (!(arg.getValue() instanceof IntExpression intLiteral)) {
            reporter.reportProblem(annotation, "argument value must be an integer literal.",
                    arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
            return false;
        }

        // All CIF integer literals must have non-negative values. Just do a sanity check.
        Assert.check(intLiteral.getValue() >= 0);
        return true;
    }
}
