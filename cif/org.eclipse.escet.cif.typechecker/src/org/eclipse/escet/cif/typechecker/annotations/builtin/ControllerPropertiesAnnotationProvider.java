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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAnnotation;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Optional;

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
            argsOk &= checkBoolArg(annotation, boundedResponse, reporter);
        }
        if (uncontrollablesBound != null) {
            argsOk &= checkIntArg(annotation, uncontrollablesBound, reporter);
        }
        if (controllablesBound != null) {
            argsOk &= checkIntArg(annotation, controllablesBound, reporter);
        }
        if (confluence != null) {
            argsOk &= checkBoolArg(annotation, confluence, reporter);
        }
        if (finiteResponse != null) {
            argsOk &= checkBoolArg(annotation, finiteResponse, reporter);
        }

        // Check for combinations of arguments.
        if (argsOk) {
            boolean hasBoundedResponse = boundedResponse != null
                    && ((BoolExpression)boundedResponse.getValue()).isValue();
            String boundedResponseText = (boundedResponse == null) ? "is not indicated"
                    : ((BoolExpression)boundedResponse.getValue()).isValue() ? "is \"true\"" : "is \"false\"";

            if (hasBoundedResponse && uncontrollablesBound == null) {
                reporter.reportProblem(annotation,
                        fmt("missing an argument named \"uncontrollablesBound\", since \"boundedResponse\" %s.",
                                boundedResponseText),
                        boundedResponse.getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
            }
            if (hasBoundedResponse && controllablesBound == null) {
                reporter.reportProblem(annotation,
                        fmt("missing an argument named \"controllablesBound\", since \"boundedResponse\" %s.",
                                boundedResponseText),
                        boundedResponse.getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
            }
            if (!hasBoundedResponse && uncontrollablesBound != null) {
                reporter.reportProblem(annotation,
                        fmt("unsupported argument named \"uncontrollablesBound\", since \"boundedResponse\" %s.",
                                boundedResponseText),
                        uncontrollablesBound.getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
            }
            if (!hasBoundedResponse && controllablesBound != null) {
                reporter.reportProblem(annotation,
                        fmt("unsupported argument named \"controllablesBound\", since \"boundedResponse\" %s.",
                                boundedResponseText),
                        controllablesBound.getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
            }
        }
    }

    /**
     * Check an boolean-typed argument.
     *
     * @param annotation The annotation that has the annotation argument.
     * @param arg The annotation argument to check.
     * @param reporter The reporter to use to report problems in the annotation argument.
     * @return Whether the argument is OK, and no violations are reported for it by this method.
     */
    private boolean checkBoolArg(Annotation annotation, AnnotationArgument arg, AnnotationProblemReporter reporter) {
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
     * Check an integer-typed argument.
     *
     * @param annotation The annotation that has the annotation argument.
     * @param arg The annotation argument to check.
     * @param reporter The reporter to use to report problems in the annotation argument.
     * @return Whether the argument is OK, and no violations are reported for it by this method.
     */
    private boolean checkIntArg(Annotation annotation, AnnotationArgument arg, AnnotationProblemReporter reporter) {
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

    @Override
    public final void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Make sure there is only one controller properties annotation on the specification.
        long count = spec.getAnnotations().stream().filter(anno -> anno.getName().equals(annotationName)).count();
        if (count > 1) {
            reporter.reportProblem(annotationName,
                    fmt("the specification has more than one controller properties annotation, namely %d.",
                            count),
                    spec.getPosition(), SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
        }
    }

    /**
     * Adds a controller properties annotation to the given specification.
     *
     * <p>
     * The specification must not yet contain a controller properties annotation. The added annotation does not have any
     * arguments.
     * </p>
     *
     * @param spec The specification.
     * @return The newly created controller properties annotation that is added to the specification.
     */
    private static Annotation addControllerProperties(Specification spec) {
        Annotation anno = newAnnotation();
        anno.setName("controller:properties");
        spec.getAnnotations().add(anno);
        return anno;
    }

    /**
     * Get the controller properties annotation of the given specification, if it has one.
     *
     * @param spec The specification.
     * @return The controller properties annotation, if it has one.
     */
    public static Optional<Annotation> getControllerProperties(Specification spec) {
        return spec.getAnnotations().stream().filter(anno -> anno.getName().equals("controller:properties"))
                .findFirst();
    }

    /**
     * Does the given specification have controller properties?
     *
     * @param spec The specification.
     * @return {@code true} if the specification has controller properties, {@code false} if it does not.
     */
    public static boolean hasControllerProperties(Specification spec) {
        return getControllerProperties(spec).isPresent();
    }

    /**
     * Returns whether the given specification has bounded response, if known.
     *
     * @param spec The specification.
     * @return An optional, with {@code true} if the specification has bounded response, {@code false} if it does not
     *     have bounded response, or no value if it is not known whether the specification has bounded response.
     */
    public static Optional<Boolean> hasBoundedResponse(Specification spec) {
        Optional<Annotation> anno = getControllerProperties(spec);
        if (anno.isEmpty()) {
            return Optional.empty();
        }
        AnnotationArgument arg = CifAnnotationUtils.getArgument(anno.get(), "boundedResponse");
        if (arg == null) {
            return Optional.empty();
        }
        return Optional.of(((BoolExpression)arg.getValue()).isValue());
    }

    /**
     * Returns whether the given specification has confluence, if known.
     *
     * @param spec The specification.
     * @return An optional, with {@code true} if the specification has confluence, {@code false} if it may not have
     *     confluence, or no value if it is not known whether the specification has confluence.
     */
    public static Optional<Boolean> hasConfluence(Specification spec) {
        Optional<Annotation> anno = getControllerProperties(spec);
        if (anno.isEmpty()) {
            return Optional.empty();
        }
        AnnotationArgument arg = CifAnnotationUtils.getArgument(anno.get(), "confluence");
        if (arg == null) {
            return Optional.empty();
        }
        return Optional.of(((BoolExpression)arg.getValue()).isValue());
    }

    /**
     * Returns whether the given specification has finite response, if known.
     *
     * @param spec The specification.
     * @return An optional, with {@code true} if the specification has finite response, {@code false} if it may not have
     *     finite response, or no value if it is not known whether the specification has finite response.
     */
    public static Optional<Boolean> hasFiniteResponse(Specification spec) {
        Optional<Annotation> anno = getControllerProperties(spec);
        if (anno.isEmpty()) {
            return Optional.empty();
        }
        AnnotationArgument arg = CifAnnotationUtils.getArgument(anno.get(), "finiteResponse");
        if (arg == null) {
            return Optional.empty();
        }
        return Optional.of(((BoolExpression)arg.getValue()).isValue());
    }

    /**
     * Returns the uncontrollable events bound for the given specification, if known.
     *
     * @param spec The specification.
     * @return An optional, with the non-negative bound if the specification has bounded response, or no value if it is
     *     not known whether the specification has bounded response.
     */
    public static Optional<Integer> getUncontrollablesBound(Specification spec) {
        Optional<Annotation> anno = getControllerProperties(spec);
        if (anno.isEmpty()) {
            return Optional.empty();
        }
        AnnotationArgument arg = CifAnnotationUtils.getArgument(anno.get(), "uncontrollablesBound");
        if (arg == null) {
            return Optional.empty();
        }
        return Optional.of(((IntExpression)arg.getValue()).getValue());
    }

    /**
     * Returns the controllable events bound for the given specification, if known.
     *
     * @param spec The specification.
     * @return An optional, with the non-negative bound if the specification has bounded response, or no value if it is
     *     not known whether the specification has bounded response.
     */
    public static Optional<Integer> getControllablesBound(Specification spec) {
        Optional<Annotation> anno = getControllerProperties(spec);
        if (anno.isEmpty()) {
            return Optional.empty();
        }
        AnnotationArgument arg = CifAnnotationUtils.getArgument(anno.get(), "controllablesBound");
        if (arg == null) {
            return Optional.empty();
        }
        return Optional.of(((IntExpression)arg.getValue()).getValue());
    }

    /**
     * Set the bounded response property of the specification.
     *
     * @param spec The specification.
     * @param uncontrollablesBound The non-negative uncontrollable events bound, or {@code null} if the specification
     *     does not have bounded response.
     * @param controllablesBound The non-negative controllable events bound, or {@code null} if the specification does
     *     not have bounded response.
     */
    @SuppressWarnings("null")
    public static void setBoundedResponse(Specification spec, Integer uncontrollablesBound,
            Integer controllablesBound)
    {
        // Sanity check.
        Assert.areEqual(controllablesBound == null, uncontrollablesBound == null);

        // Set 'boundedResponse'.
        boolean boundedResponse = controllablesBound != null;
        Optional<Annotation> optAnno = getControllerProperties(spec);
        Annotation anno = optAnno.isEmpty() ? addControllerProperties(spec) : optAnno.get();
        CifAnnotationUtils.setArgument(anno, "boundedResponse", CifValueUtils.makeBool(boundedResponse));

        // Set or remove '(un)controllablesBound'.
        if (boundedResponse) {
            CifAnnotationUtils.setArgument(anno, "uncontrollablesBound", CifValueUtils.makeInt(uncontrollablesBound));
            CifAnnotationUtils.setArgument(anno, "controllablesBound", CifValueUtils.makeInt(controllablesBound));
        } else {
            CifAnnotationUtils.removeArgument(anno, "uncontrollablesBound");
            CifAnnotationUtils.removeArgument(anno, "controllablesBound");
        }
    }

    /**
     * Set the confluence property of the specification.
     *
     * @param spec The specification.
     * @param confluence Whether the specification has confluence ({@code true}) or may not have it ({@code false}).
     */
    public static void setConfluence(Specification spec, boolean confluence) {
        Optional<Annotation> optAnno = getControllerProperties(spec);
        Annotation anno = optAnno.isEmpty() ? addControllerProperties(spec) : optAnno.get();
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
        Optional<Annotation> optAnno = getControllerProperties(spec);
        Annotation anno = optAnno.isEmpty() ? addControllerProperties(spec) : optAnno.get();
        CifAnnotationUtils.setArgument(anno, "finiteResponse", CifValueUtils.makeBool(finiteResponse));
    }
}
