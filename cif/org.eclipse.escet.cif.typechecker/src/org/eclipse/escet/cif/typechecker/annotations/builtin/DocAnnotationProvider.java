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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.CifAnnotationUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProblemReporter;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProvider;
import org.eclipse.escet.common.java.exceptions.InvalidModelException;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/**
 * Annotation provider for "doc" annotations.
 *
 * <p>
 * A "doc" annotation adds documentation to a model element. It has at least one argument, and all arguments must be
 * statically-evaluable unnamed arguments of type 'string'.
 * </p>
 */
public class DocAnnotationProvider extends AnnotationProvider {
    /**
     * Constructor for the {@link DocAnnotationProvider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public DocAnnotationProvider(String annotationName) {
        super(annotationName);
    }

    @Override
    public final void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // Check for existence of arguments.
        if (annotation.getArguments().isEmpty()) {
            reporter.reportProblem(annotation, "missing an argument.", annotation.getPosition(),
                    SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
        }

        // Check provided arguments.
        for (AnnotationArgument arg: annotation.getArguments()) {
            // 1) Check for unnamed argument.
            if (arg.getName() != null) {
                reporter.reportProblem(annotation, "unsupported named argument.", arg.getPosition(),
                        SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
            }

            // 2) Check argument value.
            boolean doEvaluationCheck = true;

            // 2a) Check for argument having a string-typed value.
            CifType valueType = CifTypeUtils.normalizeType(arg.getValue().getType());
            if (!(valueType instanceof StringType)) {
                reporter.reportProblem(annotation,
                        fmt("argument must have a value of type \"string\", but has a value of type \"%s\".",
                                CifTextUtils.typeToStr(valueType)),
                        arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
                doEvaluationCheck = false;
            }

            // 2b) Check that argument can be statically evaluated.
            if (!CifValueUtils.hasSingleValue(arg.getValue(), false, true)) {
                reporter.reportProblem(annotation, "argument cannot be evaluated statically.",
                        arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
                doEvaluationCheck = false;
            }

            // 2c) Check for evaluation errors.
            if (doEvaluationCheck) {
                try {
                    getDoc(annotation);
                } catch (InvalidModelException e) {
                    CifEvalException evalErr = (CifEvalException)e.getCause();
                    String evalErrMsg = evalErr.toString();
                    if (!evalErrMsg.endsWith(".")) {
                        evalErrMsg += ".";
                    }
                    reporter.reportProblem(annotation,
                            fmt("argument cannot be evaluated statically, "
                                    + "as evaluating it results in an evaluation error: %s", evalErrMsg),
                            arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                }
            }
        }
    }

    @Override
    public final void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Do nothing.
    }

    /**
     * Returns the documentation texts of an annotated object.
     *
     * @param obj The annotated object. Must be a named object.
     * @return The documentation texts.
     * @throws InvalidModelException If a documentation text can not be evaluated.
     */
    public static List<String> getDocs(AnnotatedObject obj) {
        return CifAnnotationUtils.getAnnotations(obj, "doc").map(a -> getDoc(a)).toList();
    }

    /**
     * Returns the documentation text of a documentation annotation.
     *
     * @param docAnno The documentation annotation.
     * @return The documentation text.
     * @throws InvalidModelException If the documentation text can not be evaluated.
     */
    public static String getDoc(Annotation docAnno) {
        return docAnno.getArguments().stream().map(arg -> getDoc(arg)).collect(Collectors.joining("\n"));
    }

    /**
     * Returns the documentation text of a documentation annotation argument.
     *
     * @param docAnnoArg The documentation annotation argument.
     * @return The documentation text.
     * @throws InvalidModelException If the documentation text can not be evaluated.
     */
    private static String getDoc(AnnotationArgument docAnnoArg) {
        try {
            Object value = CifEvalUtils.eval(docAnnoArg.getValue(), false);
            return (String)value;
        } catch (CifEvalException e) {
            AnnotatedObject annotatedObj = (AnnotatedObject)docAnnoArg.eContainer().eContainer();
            if (CifTextUtils.hasName(annotatedObj)) {
                String msg = fmt("Failed to evaluate an argument of the \"doc\" annotation of \"%s\".",
                        CifTextUtils.getAbsName(annotatedObj));
                throw new InvalidModelException(msg, e);
            } else {
                throw new InvalidModelException("Failed to evaluate an argument of a \"doc\" annotation.", e);
            }
        }
    }

    /**
     * Formatter helper class for {@code @doc} annotations.
     *
     * <p>
     * Often, the text of {@code @doc} annotations needs to be formatted to fit in a larger documentation context. This
     * class gives assistance in performing that task.
     * </p>
     * <p>
     * The class supports adding general header and footer lines, as well as separator lines before and after the text
     * lines of each {@code @doc} annotation. Finally, each line of the {@code @doc} annotation can be prefixed by text.
     * </p>
     * <p>
     * As an example, assume that the parameters of this class are set to:
     * <table>
     * <tr>
     * <td>headerLines:</td>
     * <td>{@code list("HEAD")}</td>
     * </tr>
     * <tr>
     * <td>footerLines:</td>
     * <td>{@code list("FOOT")}</td>
     * </tr>
     * <tr>
     * <td>preDocLines:</td>
     * <td>{@code list("PRE")}</td>
     * </tr>
     * <tr>
     * <td>docLinePrefix:</td>
     * <td>{@code "# "}</td>
     * </tr>
     * <tr>
     * <td>postDocLines:</td>
     * <td>{@code list("POST")}</td>
     * </tr>
     * </table>
     * <ul>
     * <li>Formatted output for an object without {@code @doc} annotations then produces <pre> HEAD
     * FOOT</pre></li>
     * <li>Formatted output for an object with {@code @doc("abc\ndef") @doc("xyz")} annotations then produces <pre> HEAD
     * PRE
     * # abc
     * # def
     * POST
     * PRE
     * # xyz
     * POST
     * FOOT</pre></li>
     * </ul>
     * </p>
     */
    public static class DocAnnotationFormatter {
        /** Lines above all doc annotation text of an object. */
        private final List<String> headerLines;

        /** Lines below all doc annotation text of an object. */
        private final List<String> footerLines;

        /** Lines above the text of a single {@code @doc} annotation. */
        private final List<String> preDocLines;

        /** Prefix to insert before a line from an {@code @doc} annotation. */
        private final String docLinePrefix;

        /** Lines below the text of a single {@code @doc} annotation. */
        private final List<String> postDocLines;

        /** Found {@code @doc} annotation lines for an object. */
        private List<String> docBlocks;

        /**
         * Constructor of the {@link DocAnnotationFormatter} class.
         *
         * @param headerLines Lines above all the formatted doc annotation text of an object. Value {@code null} can be
         *     used for not having header lines.
         * @param footerLines Lines below all the formatted doc annotation text of an object. Value {@code null} can be
         *     used for not having footer lines.
         * @param preDocLines Lines above the formatted text of a single {@code @doc} annotation. Value {@code null} can
         *     be used for not having pre-doc lines.
         * @param docLinePrefix Prefix to insert before a formatted line from an {@code @doc} annotation. Value
         *     {@code null} can be used for not having a doc prefix.
         * @param postDocLines Lines below the formatted text of a single {@code @doc} annotation. Value {@code null}
         *     can be used for not having post-doc lines.
         */
        public DocAnnotationFormatter(List<String> headerLines, List<String> footerLines,
                List<String> preDocLines, String docLinePrefix, List<String> postDocLines)
        {
            this.headerLines = (headerLines == null) ? List.of() : headerLines;
            this.footerLines = (footerLines == null) ? List.of() : footerLines;
            this.preDocLines = (preDocLines == null) ? List.of() : preDocLines;
            this.docLinePrefix = (docLinePrefix == null) ? "" : docLinePrefix;
            this.postDocLines = (postDocLines == null) ? List.of() : postDocLines;
            docBlocks = List.of();
        }

        /**
         * Retrieve the {@code @doc} annotations of the provided object.
         *
         * @param obj CIF object to query for its {@code @doc} annotations.
         * @return Whether documentation text was found.
         */
        public boolean getDocs(AnnotatedObject obj) {
            docBlocks = DocAnnotationProvider.getDocs(obj);
            return !docBlocks.isEmpty();
        }

        /**
         * Perform formatting of previously retrieved documentation using {@link #getDocs}, and return the formatted
         * result.
         *
         * @return The formatted text.
         * @see #getDocs
         * @see #getAndFormatDocs
         */
        public List<String> formatDocs() {
            List<String> lines = list();
            lines.addAll(headerLines);
            for (String docBlock: docBlocks) {
                lines.addAll(preDocLines);
                for (String line: docBlock.split("\\r?\\n")) {
                    lines.add(docLinePrefix + line);
                }
                lines.addAll(postDocLines);
            }
            lines.addAll(footerLines);

            return lines;
        }

        /**
         * Retrieve the {@code @doc} annotations of the provided object and perform formatting of it.
         *
         * @param obj CIF object to query for its {@code @doc} annotations.
         * @return The formatted text.
         */
        public List<String> getAndFormatDocs(AnnotatedObject obj) {
            getDocs(obj);
            return formatDocs();
        }
    }
}
