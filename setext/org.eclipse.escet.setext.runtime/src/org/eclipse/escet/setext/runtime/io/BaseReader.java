//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.runtime.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.common.typechecker.SemanticProblem;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;
import org.eclipse.escet.common.typechecker.TypeChecker;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.Parser;
import org.eclipse.escet.setext.runtime.SyntaxWarning;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;

/**
 * Text file reader that tightly integrates with parsers generated using SeText and our own type checker interface.
 *
 * @param <T> The type of the actual reader class (a derived class of the {@link BaseReader} class).
 * @param <TAst> The type of the result of parsing.
 * @param <TRslt> The type of the result of type checking.
 * @param <TParser> The type of the parser.
 * @param <TChk> The type of the type checker.
 */
public abstract class BaseReader<T extends BaseReader<?, ?, ?, ?, ?>, TAst, TRslt, TParser extends Parser<TAst>,
        TChk extends TypeChecker<TAst, TRslt>>
{
    /**
     * The absolute or relative local file system path to the input file to parse. This path is used in the source for
     * position information.
     */
    private String path;

    /** The absolute local file system path to the input file to parse. */
    private String absPath;

    /** Whether to output debugging information during parsing . */
    private boolean debugParser;

    /** Whether warnings resulting from type checking should be suppressed (should not be printed to the console). */
    public boolean suppressWarnings;

    /**
     * The type checker used to check the input file. Is {@code null} until it is created on first use. Should not be
     * accessed until type checking completes successfully.
     */
    private TChk tchecker;

    /**
     * Creates a new parser.
     *
     * @return The newly created parser.
     */
    protected abstract TParser createParser();

    /**
     * Creates a new type checker.
     *
     * @return The newly created type checker.
     */
    protected abstract TChk createTypeChecker();

    /**
     * Returns the name of the language of the files being read by this reader.
     *
     * @return The name of the language of the files being read by this reader.
     */
    protected abstract String getLangName();

    /**
     * Returns the absolute local file system path to the input file to parse.
     *
     * @return The absolute local file system path to the input file to parse.
     * @throws IllegalStateException If the reader is not yet initialized.
     */
    public String getAbsFilePath() {
        if (absPath == null) {
            throw new IllegalStateException();
        }
        return absPath;
    }

    /**
     * Returns the absolute local file system path to the directory that contains the input file to parse.
     *
     * @return The absolute local file system path to the directory that contains the input file to parse.
     * @throws IllegalStateException If the reader is not yet initialized.
     */
    public String getAbsDirPath() {
        if (absPath == null) {
            throw new IllegalStateException();
        }
        return Paths.getAbsFilePathDir(absPath);
    }

    /**
     * Initializes the reader. This method requires the application framework.
     *
     * <p>
     * The input file to read is obtained via the {@link InputFileOption}. Debugging output of the parser is controlled
     * via the {@link OutputMode} option.
     * </p>
     *
     * @return This reader, for method chaining.
     * @throws IllegalStateException If the reader is already initialized.
     */
    public T init() {
        return init(InputFileOption.getPath(), Paths.resolve(InputFileOption.getPath()), false);
    }

    /**
     * Initializes the reader. This method does not require the application framework.
     *
     * @param path The absolute or relative local file system path to the input file to parse. This path is used in the
     *     source for position information.
     * @param absPath The absolute local file system path to the input file to parse.
     * @param debugParser Whether to output debugging information during parsing.
     * @return This reader, for method chaining.
     * @throws IllegalStateException If the reader is already initialized.
     */
    @SuppressWarnings("unchecked")
    public T init(String path, String absPath, boolean debugParser) {
        if (this.path != null) {
            throw new IllegalStateException("Already initialized.");
        }

        this.path = path;
        this.absPath = absPath;
        this.debugParser = debugParser;

        return (T)this;
    }

    /**
     * Reads the input from the file set via the {@link #init} method. Parser warnings and type checking problems are
     * printed to the console, using the application framework.
     *
     * @return The result of reading the input file, parsing it, and type checking it.
     * @throws IllegalStateException If the reader is not yet initialized.
     * @throws SyntaxException If parsing fails.
     * @throws InvalidInputException If type checking fails.
     */
    public TRslt read() {
        // Check state.
        if (path == null) {
            throw new IllegalStateException("Not yet initialized.");
        }

        // Parse input file.
        DebugMode debugMode = debugParser ? DebugMode.PARSER : DebugMode.NONE;
        TParser parser = createParser();
        TAst ast = parser.parseFile(absPath, path, debugMode);

        // Report parser warnings.
        if (!suppressWarnings) {
            for (SyntaxWarning warning: parser.getWarnings()) {
                OutputProvider.warn(warning.toString());
            }
        }

        // Type check the AST.
        return tcheck(ast);
    }

    /**
     * Reads the input from a string. Parser warnings and type checking problems are printed to the console, using the
     * application framework.
     *
     * @param fileText The input file text to read.
     * @return The result of parsing the input text, and type checking it.
     * @throws IllegalStateException If the reader is not yet initialized.
     * @throws SyntaxException If parsing fails.
     * @throws InvalidInputException If type checking fails.
     */
    public TRslt read(String fileText) {
        // Check state.
        if (path == null) {
            throw new IllegalStateException("Not yet initialized.");
        }

        // Parse input file.
        DebugMode debugMode = debugParser ? DebugMode.PARSER : DebugMode.NONE;
        String src = fmt("File \"%s\": ", path);
        TParser parser = createParser();
        TAst ast = parser.parseString(fileText, absPath, src, debugMode);

        // Report parser warnings.
        if (!suppressWarnings) {
            for (SyntaxWarning warning: parser.getWarnings()) {
                OutputProvider.warn(warning.toString());
            }
        }

        // Type check the AST.
        return tcheck(ast);
    }

    /**
     * Type checks the given parse result. Type checking problems are printed to the console, using the application
     * framework.
     *
     * @param ast The parse result to check.
     * @return The type checked input.
     */
    private TRslt tcheck(TAst ast) {
        // Type check input file.
        tchecker = createTypeChecker();
        tchecker.setSourceFilePath(absPath);
        TRslt spec = tchecker.typeCheck(ast);

        List<SemanticProblem> problems = tchecker.getProblems();
        boolean fatal = false;
        for (SemanticProblem problem: problems) {
            if (problem.severity == SemanticProblemSeverity.WARNING) {
                if (!suppressWarnings) {
                    OutputProvider.warn(problem.toString());
                }
            } else {
                Assert.check(problem.severity == SemanticProblemSeverity.ERROR);
                OutputProvider.err("ERROR: " + problem.toString());
                fatal = true;
            }
        }

        if (fatal) {
            String msg = fmt("Failed to load %s file \"%s\": the file has errors.", getLangName(), path);
            throw new InvalidInputException(msg);
        }

        // Returned fully type checked specification.
        return spec;
    }

    /**
     * Returns the type checker used to check the input.
     *
     * @return The type checker.
     * @throws IllegalStateException If the type checker is not (yet) available.
     */
    public TChk getTypeChecker() {
        if (tchecker == null) {
            throw new IllegalStateException("Type checker not available.");
        }
        return tchecker;
    }
}
