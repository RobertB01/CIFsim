//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.annotations.builtin;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifAnnotationUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProblemReporter;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProvider;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProviderHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/**
 * Annotation provider for "controller:properties" annotations.
 *
 * <p>
 * A "controller:properties" annotation adds properties of a controller to the CIF specification. The annotation may
 * only be added to the specification, and at most one such annotation is allowed. It supports the following arguments,
 * and no other arguments are allowed:
 * <ul>
 * <li>boundedResponse: Whether the specification has bounded response. Must have a boolean literal value. A
 * {@code true} value indicates the specification has bounded response, and a {@code false} value indicates it does not
 * have it. If this argument is present and its value is {@code true}, the {@code uncontrollablesBound} and
 * {@code controllablesBound} arguments must also be present. If the argument is not present, or its value is
 * {@code false}, these other two arguments must not be present.</li>
 * <li>uncontrollablesBound: The bound on the number of transitions for uncontrollable events. Must have a non-negative
 * integer literal value. This argument may only be present if the {@code boundedResponse} argument is also present, and
 * its value is {@code true}.</li>
 * <li>controllablesBound: The bound on the number of transitions for controllable events. Must have a non-negative
 * integer literal value. This argument may only be present if the {@code boundedResponse} argument is also present, and
 * its value is {@code true}.</li>
 * <li>confluence: Whether the specification has confluence. Must have a boolean literal value. A {@code true} value
 * indicates the specification has confluence, and a {@code false} value indicates it may not have it.</li>
 * <li>finiteResponse: Whether the specification has finite response, or may not. Must have a boolean literal value. A
 * {@code true} value indicates the specification has finite response, and a {@code false} value indicates it may not
 * have it.</li>
 * <li>nonBlockingUnderControl: Whether the specification is non-blocking under control. Must have a boolean literal
 * value. A {@code true} value indicates the specification is non-blocking under control, and a {@code false} value
 * indicates is not.</li>
 * </ul>
 * It is allowed for the annotation to not have any arguments, but in that case the annotation can also just be removed.
 * </p>
 */
public class ControllerPropertiesAnnotationProvider extends AnnotationProvider {
    /**
     * Constructor for the {@link ControllerPropertiesAnnotationProvider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public ControllerPropertiesAnnotationProvider(String annotationName) {
        super(annotationName);
    }

    @SuppressWarnings("null")
    @Override
    public final void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // Annotation must be on the specification.
        if (!(annotatedObject instanceof Specification)) {
            reporter.reportProblem(annotation, "controller properties annotation must be on the specification.",
                    annotation.getPosition(), SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
            return;
        }

        // Check provided arguments.
        AnnotationArgument boundedResponse = null;
        AnnotationArgument uncontrollablesBound = null;
        AnnotationArgument controllablesBound = null;
        AnnotationArgument confluence = null;
        AnnotationArgument finiteResponse = null;
        AnnotationArgument nonBlockingUnderControl = null;
        for (AnnotationArgument arg: annotation.getArguments()) {
            // Arguments must be named.
            if (arg.getName() == null) {
                reporter.reportProblem(annotation, "unsupported unnamed argument.", arg.getPosition(),
                        SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
                continue;
            }

            // Check for supported arguments. Note that the type checker already prevents duplicate arguments.
            switch (arg.getName()) {
                case "boundedResponse":
                    Assert.check(boundedResponse == null);
                    boundedResponse = arg;
                    break;
                case "uncontrollablesBound":
                    Assert.check(uncontrollablesBound == null);
                    uncontrollablesBound = arg;
                    break;
                case "controllablesBound":
                    Assert.check(controllablesBound == null);
                    controllablesBound = arg;
                    break;
                case "confluence":
                    Assert.check(confluence == null);
                    confluence = arg;
                    break;
                case "finiteResponse":
                    Assert.check(finiteResponse == null);
                    finiteResponse = arg;
                    break;
                case "nonBlockingUnderControl":
                    Assert.check(nonBlockingUnderControl == null);
                    nonBlockingUnderControl = arg;
                    break;
                default:
                    reporter.reportProblem(annotation, fmt("unsupported argument named \"%s\".", arg.getName()),
                            arg.getPosition(), SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                    break;
            }
        }

        // Check each supported argument, if present.
        boolean argsOk = true;
        if (boundedResponse != null) {
            argsOk &= AnnotationProviderHelper.checkBoolLiteralArg(annotation, boundedResponse, reporter);
        }
        if (uncontrollablesBound != null) {
            argsOk &= AnnotationProviderHelper.checkNonNegativeIntLiteralArg(annotation, uncontrollablesBound,
                    reporter);
        }
        if (controllablesBound != null) {
            argsOk &= AnnotationProviderHelper.checkNonNegativeIntLiteralArg(annotation, controllablesBound, reporter);
        }
        if (confluence != null) {
            argsOk &= AnnotationProviderHelper.checkBoolLiteralArg(annotation, confluence, reporter);
        }
        if (finiteResponse != null) {
            argsOk &= AnnotationProviderHelper.checkBoolLiteralArg(annotation, finiteResponse, reporter);
        }
        if (nonBlockingUnderControl != null) {
            argsOk &= AnnotationProviderHelper.checkBoolLiteralArg(annotation, nonBlockingUnderControl, reporter);
        }

        // Check for combinations of arguments.
        if (argsOk) {
            boolean hasBoundedResponse = boundedResponse != null
                    && ((BoolExpression)boundedResponse.getValue()).isValue();
            String boundedResponseText = (boundedResponse == null) ? "is not indicated"
                    : ((BoolExpression)boundedResponse.getValue()).isValue() ? "is \"true\"" : "is \"false\"";

            if (hasBoundedResponse) {
                // Check for missing arguments.
                if (uncontrollablesBound == null) {
                    String msg = fmt("missing an argument named \"uncontrollablesBound\", " +
                            "since \"boundedResponse\" %s.", boundedResponseText);
                    reporter.reportProblem(annotation, msg, boundedResponse.getPosition(),
                            SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                }
                if (controllablesBound == null) {
                    String msg = fmt("missing an argument named \"controllablesBound\", since \"boundedResponse\" %s.",
                            boundedResponseText);
                    reporter.reportProblem(annotation, msg, boundedResponse.getPosition(),
                            SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                }
            } else {
                // Check for not allowed arguments.
                if (uncontrollablesBound != null) {
                    String msg = fmt("unsupported argument named \"uncontrollablesBound\", " +
                            "since \"boundedResponse\" %s.", boundedResponseText);
                    reporter.reportProblem(annotation, msg, uncontrollablesBound.getPosition(),
                            SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                }
                if (controllablesBound != null) {
                    String msg = fmt("unsupported argument named \"controllablesBound\", since \"boundedResponse\" %s.",
                            boundedResponseText);
                    reporter.reportProblem(annotation, msg, controllablesBound.getPosition(),
                            SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                }
            }
        }
    }

    @Override
    public final void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Make sure there is only one controller properties annotation on the specification.
        long count = CifAnnotationUtils.getAnnotations(spec, annotationName).count();
        if (count > 1) {
            reporter.reportProblem(annotationName,
                    fmt("the specification has more than one controller properties annotation, namely %d.",
                            count),
                    spec.getPosition(), SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
        }
    }

    /**
     * Returns whether the given specification has bounded response, if known.
     *
     * @param spec The specification.
     * @return {@code true} if the specification has bounded response, {@code false} if it does not have bounded
     *     response, or {@code null} if it is not known whether the specification has bounded response.
     */
    public static Boolean hasBoundedResponse(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null : CifAnnotationUtils.tryGetArgument(anno, "boundedResponse");
        return (arg == null) ? null : ((BoolExpression)arg.getValue()).isValue();
    }

    /**
     * Returns whether the given specification has confluence, if known.
     *
     * @param spec The specification.
     * @return {@code true} if the specification has confluence for controllable events, {@code false} if it may not
     *     have confluence, or {@code null} if it is not known whether the specification has confluence.
     */
    public static Boolean hasConfluence(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null : CifAnnotationUtils.tryGetArgument(anno, "confluence");
        return (arg == null) ? null : ((BoolExpression)arg.getValue()).isValue();
    }

    /**
     * Returns whether the given specification has finite response, if known.
     *
     * @param spec The specification.
     * @return {@code true} if the specification has finite response, {@code false} if it may not have finite response,
     *     or {@code null} if it is not known whether the specification has finite response.
     */
    public static Boolean hasFiniteResponse(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null : CifAnnotationUtils.tryGetArgument(anno, "finiteResponse");
        return (arg == null) ? null : ((BoolExpression)arg.getValue()).isValue();
    }

    /**
     * Returns whether the given specification is non-blocking under control, if known.
     *
     * @param spec The specification.
     * @return {@code true} if the specification is non-blocking under control, {@code false} if it is not non-blocking
     *     under control, or {@code null} if it is not known whether the specification is non-blocking under control.
     */
    public static Boolean isNonBlockingUnderControl(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null
                : CifAnnotationUtils.tryGetArgument(anno, "nonBlockingUnderControl");
        return (arg == null) ? null : ((BoolExpression)arg.getValue()).isValue();
    }

    /**
     * Returns the uncontrollable events bound for the given specification, if known.
     *
     * @param spec The specification.
     * @return The non-negative bound if the specification has bounded response, {@code null} otherwise (it does not
     *     have bounded response, or it is not known whether it has bounded response).
     */
    public static Integer getUncontrollablesBound(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null
                : CifAnnotationUtils.tryGetArgument(anno, "uncontrollablesBound");
        return (arg == null) ? null : ((IntExpression)arg.getValue()).getValue();
    }

    /**
     * Returns the controllable events bound for the given specification, if known.
     *
     * @param spec The specification.
     * @return The non-negative bound if the specification has bounded response, {@code null} otherwise (it does not
     *     have bounded response, or it is not known whether it has bounded response).
     */
    public static Integer getControllablesBound(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null : CifAnnotationUtils.tryGetArgument(anno, "controllablesBound");
        return (arg == null) ? null : ((IntExpression)arg.getValue()).getValue();
    }

    /**
     * Set the bounded response property of the specification.
     *
     * @param spec The specification.
     * @param uncontrollablesBound The non-negative uncontrollable events bound, or {@code null} if the specification
     *     does not have bounded response. Must be {@code null} if and only if {@code controllablesBound} is
     *     {@code null}.
     * @param controllablesBound The non-negative controllable events bound, or {@code null} if the specification does
     *     not have bounded response. Must be {@code null} if and only if {@code uncontrollablesBound} is {@code null}.
     */
    @SuppressWarnings("null")
    public static void setBoundedResponse(Specification spec, Integer uncontrollablesBound,
            Integer controllablesBound)
    {
        // Sanity check.
        Assert.areEqual(controllablesBound == null, uncontrollablesBound == null);

        // Set 'boundedResponse'.
        boolean boundedResponse = controllablesBound != null;
        Annotation anno = CifAnnotationUtils.getOrCreateSingleAnnotation(spec, "controller:properties");
        CifAnnotationUtils.setArgument(anno, "boundedResponse", CifValueUtils.makeBool(boundedResponse));

        // Set or remove '(un)controllablesBound'.
        if (boundedResponse) {
            CifAnnotationUtils.setArgument(anno, "uncontrollablesBound", CifValueUtils.makeInt(uncontrollablesBound));
            CifAnnotationUtils.setArgument(anno, "controllablesBound", CifValueUtils.makeInt(controllablesBound));
        } else {
            CifAnnotationUtils.tryRemoveArgument(anno, "uncontrollablesBound");
            CifAnnotationUtils.tryRemoveArgument(anno, "controllablesBound");
        }
    }

    /**
     * Set the confluence property of the specification.
     *
     * @param spec The specification.
     * @param confluence Whether the specification has confluence ({@code true}) or may not have it ({@code false}).
     */
    public static void setConfluence(Specification spec, boolean confluence) {
        Annotation anno = CifAnnotationUtils.getOrCreateSingleAnnotation(spec, "controller:properties");
        CifAnnotationUtils.setArgument(anno, "confluence", CifValueUtils.makeBool(confluence));
    }

    /**
     * Set the finite response property of the specification.
     *
     * @param spec The specification.
     * @param finiteResponse Whether the specification has finite response ({@code true}) or may not have it
     *     ({@code false}).
     */
    public static void setFiniteResponse(Specification spec, boolean finiteResponse) {
        Annotation anno = CifAnnotationUtils.getOrCreateSingleAnnotation(spec, "controller:properties");
        CifAnnotationUtils.setArgument(anno, "finiteResponse", CifValueUtils.makeBool(finiteResponse));
    }

    /**
     * Set the non-blocking under control property of the specification.
     *
     * @param spec The specification.
     * @param nonBlockingUnderControl Whether the specification is non-blocking under control ({@code true}) or not
     *     ({@code false}).
     */
    public static void setNonBlockingUnderControl(Specification spec, boolean nonBlockingUnderControl) {
        Annotation anno = CifAnnotationUtils.getOrCreateSingleAnnotation(spec, "controller:properties");
        CifAnnotationUtils.setArgument(anno, "nonBlockingUnderControl",
                CifValueUtils.makeBool(nonBlockingUnderControl));
    }

    /**
     * Remove the controller property annotation of the given specification, if it exists.
     *
     * @param spec The specification.
     */
    public static void remove(Specification spec) {
        CifAnnotationUtils.removeAnnotations(spec, "controller:properties");
    }
}
