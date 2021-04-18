//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.typechecker;

import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/**
 * Base class for type checkers.
 *
 * @param <TIn> The type of the abstract syntax tree (AST) input of the type checker.
 * @param <TOut> The type of the decorated abstract syntax tree (DAST) output of the type checker.
 */
public abstract class TypeChecker<TIn, TOut> {
    /** The semantic problems found so far. Is modified in-place. */
    private final Set<SemanticProblem> problems = set();

    /** Whether {@link #problems} contains any warnings. */
    private boolean hasWarning = false;

    /** Whether {@link #problems} contains any errors. */
    private boolean hasError = false;

    /**
     * Indicates whether the type checker has finished. After a type checker is finished, no new problems may be added
     * to it.
     *
     * <p>
     * This feature provides detection for adding problems to the 'wrong' type checker, for instance when multiple type
     * checkers are used to handle imports.
     * </p>
     */
    private boolean finished = false;

    /**
     * The absolute local file system path of the source file that was parsed, and thus resulted in the abstract syntax
     * tree (AST) that is the input for this type checker. Is {@code null} until it is set using the
     * {@link #setSourceFilePath} method.
     */
    private String sourceFilePath;

    /**
     * Is the type checker finished?
     *
     * @return {@code true} if the type checker is finished, {@code false} otherwise.
     * @see #finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Prepares the type checker for post type checking use. This is only allowed after the type checker has
     * {@link #finished} its normal type checking. This allows for checking of additional expressions etc, that are
     * defined outside the actual specification. This method should not be used to implement an import mechanism.
     *
     * <p>
     * The problems found so far are all removed, and the type checker is no longer in a {@link #finished} state.
     * </p>
     *
     * @throws IllegalStateException If the type checker has not yet finished its normal type checking.
     * @see #finalizePostUse
     */
    public void preparePostUse() {
        // Make sure type checker is finished before allowing post use.
        if (!finished) {
            throw new IllegalStateException("Not yet finished.");
        }

        // Prepare for post use.
        problems.clear();
        finished = false;
    }

    /**
     * Finalizes the type checker after post type checking use. This is only allowed after the type checker has
     * {@link #finished} its normal type checking. A call to this method should be used to finalize post type checking
     * use, which is started using a call to the {@link #preparePostUse} method.
     *
     * <p>
     * All problems reported to this type checker during post use, are returned to the caller and removed from this type
     * checker, and the state of this type checker is set to {@link #finished} again.
     * </p>
     *
     * @return The semantic problems that occurred during post use, if any.
     * @throws IllegalStateException If the type checker is not currently in a state that allows post type checking use.
     * @see #preparePostUse
     */
    public List<SemanticProblem> finalizePostUse() {
        // Make sure type checker is not yet finished.
        if (finished) {
            throw new IllegalStateException("Already finished.");
        }

        // Get a copy of the semantic problems from this type checker.
        List<SemanticProblem> rslt = sortedgeneric(problems);

        // Finalize the state of this type checker after post use.
        problems.clear();
        finished = true;

        // Return the problems that were found.
        return rslt;
    }

    /**
     * Sets the absolute local file system path of the source file.
     *
     * @param sourceFilePath The absolute local file system path of the source file.
     * @throws IllegalStateException If the source file path is already set.
     * @see #sourceFilePath
     */
    public void setSourceFilePath(String sourceFilePath) {
        if (this.sourceFilePath != null) {
            throw new IllegalStateException("Source file path already set.");
        }
        this.sourceFilePath = sourceFilePath;
    }

    /**
     * Returns the absolute local file system path of the source file.
     *
     * @return The absolute local file system path of the source file.
     * @throws IllegalStateException If the source file path is not set.
     * @see #sourceFilePath
     */
    public String getSourceFilePath() {
        if (sourceFilePath == null) {
            throw new IllegalStateException("Source file path not set.");
        }
        return sourceFilePath;
    }

    /**
     * Returns the absolute local file system path of the directory that contains the source file.
     *
     * @return The absolute local file system path of the directory that contains the source file.
     * @throws IllegalStateException If the source file path is not set.
     * @see #sourceFilePath
     */
    public String getSourceFileDir() {
        return Paths.getAbsFilePathDir(getSourceFilePath());
    }

    /**
     * Returns the source file name, without any path.
     *
     * @return The source file name, without any path.
     * @throws IllegalStateException If the source file path is not set.
     * @see #sourceFilePath
     */
    public String getSourceFileName() {
        return Paths.getFileName(getSourceFilePath());
    }

    /**
     * Resolves an absolute or relative local file system path of an import. If the path is relative, it is interpreted
     * relative to the directory that contains the source file being type checked by this type checker.
     *
     * @param path The absolute or relative local file system path to the file to import.
     * @return The absolute local file system path to the file to import.
     * @throws IllegalStateException If the source file path is not set.
     */
    public String resolveImport(String path) {
        return Paths.resolve(path, getSourceFileDir());
    }

    /**
     * Performs type checking on the root input object, transforming it from the abstract syntax tree (AST)
     * representation to a decorated abstract syntax tree (DAST) representation.
     *
     * @param rootObj The root input object on which to perform type checking.
     * @return The type checked result, or {@code null} in case of premature termination and other type checking
     *     failures.
     */
    public TOut typeCheck(TIn rootObj) {
        // Paranoia check.
        Assert.check(!finished, "can't start type checking: already finished");

        // Perform type checking.
        TOut rslt = null;
        try {
            rslt = transRoot(rootObj);
            Assert.notNull(rslt);
        } catch (SemanticException ex) {
            // Type checking prematurely terminated.
        }

        // Type checking has finished.
        finished = true;

        // Return type checking result, if any. If we have any errors, indicate
        // failure.
        boolean hasErrors = hasError();
        if (!hasErrors && rslt == null) {
            String msg = "Type checking resulted in 'null', without errors.";
            throw new RuntimeException(msg);
        }
        return hasErrors ? null : rslt;
    }

    /**
     * Performs type checking on the root input object, transforming it from the abstract syntax tree (AST)
     * representation to a decorated abstract syntax tree (DAST) representation.
     *
     * <p>
     * Unlike the public {@link #typeCheck} method, which is called by users of the type checker, this method is an
     * internal method of the type checker. This method implements the actual type checking, while the
     * {@link #typeCheck} method is just a wrapper method to handle the {@link SemanticException} exceptions.
     * </p>
     *
     * @param rootObj The root input object on which to perform type checking.
     * @return The type checked result. Must not be {@code null}.
     * @throws SemanticException May be thrown to indicate that a semantic problem has been detected, and that type
     *     checking is prematurely terminated. This exception is optional, as type checkers may also continue in case of
     *     failures. In such a case, they manually add semantic problems to the type checker, instead of using the
     *     exception.
     */
    protected abstract TOut transRoot(TIn rootObj);

    /**
     * Adds a semantic problem to the list of problems found so far.
     *
     * @param message The message describing the semantic problem.
     * @param severity The severity of the problem.
     * @param position Position information.
     */
    public void addProblem(String message, SemanticProblemSeverity severity, Position position) {
        addProblem(new SemanticProblem(message, severity, position));
    }

    /**
     * Adds a semantic problem to the list of problems found so far.
     *
     * @param problem The problem to add.
     */
    public void addProblem(SemanticProblem problem) {
        Assert.check(!finished, "can't add problem to finished type checker");

        problems.add(problem);

        switch (problem.severity) {
            case WARNING:
                hasWarning = true;
                break;
            case ERROR:
                hasError = true;
                break;
        }
    }

    /**
     * Adds a semantic warning to the list of problems found so far.
     *
     * @param message The message describing the semantic warning.
     * @param position Position information.
     */
    public void addWarning(String message, Position position) {
        addProblem(message, SemanticProblemSeverity.WARNING, position);
    }

    /**
     * Adds a semantic error to the list of problems found so far.
     *
     * @param message The message describing the semantic error.
     * @param position Position information.
     */
    public void addError(String message, Position position) {
        addProblem(message, SemanticProblemSeverity.ERROR, position);
    }

    /**
     * Returns the semantic problems found so far. The resulting list must never be modified in-place!
     *
     * @return The semantic problems found so far (sorted).
     */
    public List<SemanticProblem> getProblems() {
        return sortedgeneric(problems);
    }

    /**
     * Indicates whether a warning is present in the list of problems.
     *
     * @return {@code true} if a warning is present, {@code false} otherwise.
     * @see #problems
     * @see #hasWarning
     */
    public boolean hasWarning() {
        return hasWarning;
    }

    /**
     * Indicates whether an error is present in the list of problems.
     *
     * @return {@code true} if an error is present, {@code false} otherwise.
     * @see #problems
     * @see #hasError
     */
    public boolean hasError() {
        return hasError;
    }
}
