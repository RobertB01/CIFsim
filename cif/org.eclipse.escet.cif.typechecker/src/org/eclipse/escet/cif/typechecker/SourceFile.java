//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.TypeChecker;

/** Metadata on a CIF source file. */
public class SourceFile {
    /**
     * The absolute local file system path or Eclipse Platform URI of the source file. It is normalized (i.e. does not
     * contain {@code "."} or {@code ".."} parts). For local file system paths, it contains platform specific path
     * separators.
     */
    public final String absPath;

    /** Whether the source file is the main file, and was thus not imported. */
    public final boolean main;

    /**
     * The position information of the import in the importing file, from which this source file is imported, or
     * {@code null} for the main file.
     */
    public final Position importingPos;

    /**
     * The position information of the import in the {@link #main} file to which to report problems for this file, or
     * {@code null} for main files.
     */
    public final Position problemPos;

    /**
     * Constructor for the {@link SourceFile} class.
     *
     * @param absPath The absolute local file system path or Eclipse Platform URI of the source file. It is normalized
     *     (i.e. does not contain {@code "."} or {@code ".."} parts). For local file system paths, it contains platform
     *     specific path separators.
     * @param main Whether the source file is the main file, and was thus not imported.
     * @param importingPos The position information of the import in the importing file, from which this source file is
     *     imported, or {@code null} for the main file.
     * @param problemPos The position information of the import in the main file to which to report problems for this
     *     file, or {@code null} for main files.
     */
    public SourceFile(String absPath, boolean main, Position importingPos, Position problemPos) {
        this.absPath = absPath;
        this.main = main;
        this.importingPos = importingPos;
        this.problemPos = problemPos;

        Assert.ifAndOnlyIf(main, problemPos == null);
        Assert.ifAndOnlyIf(main, importingPos == null);
    }

    /**
     * Returns the absolute local file system path or platform URI of the directory that contains the source file. It is
     * normalized (i.e. does not contain {@code "."} or {@code ".."} parts). For local file system paths, it contains
     * platform specific path separators.
     *
     * @return The absolute local file system path or platform URI of the directory that contains the source file.
     * @see TypeChecker#getSourceFileDir
     */
    public String getAbsDirPath() {
        // Works also for platform URIs.
        return Paths.getAbsFilePathDir(absPath);
    }

    /**
     * Returns a path to this source file, relative to the directory that contains the given main source file. A
     * relative path is returned, unless it doesn't exist, in which case an absolute path is returned instead. An
     * example of the latter is Windows paths with different drive prefixes.
     *
     * @param mainFile The main source file relative to which to get the path.
     * @return A path to this source file, relative to the given directory that contains the given main source file, if
     *     possible, and an absolute path otherwise. May be an absolute or relative local file system path, or an
     *     Eclipse platform URI. The result uses {@code "/"} as directory separator.
     * @see PlatformUriUtils#getRelativePath
     */
    public String getRelativePathFrom(SourceFile mainFile) {
        Assert.check(!main);
        Assert.check(mainFile.main);
        String mainAbsDirPath = mainFile.getAbsDirPath();
        return PlatformUriUtils.getRelativePath(absPath, mainAbsDirPath);
    }

    /**
     * Returns a path to the given target file or directory, relative to the directory that contains the this source
     * file. A relative path is returned, unless it doesn't exist, in which case an absolute path is returned instead.
     * An example of the latter is Windows paths with different drive prefixes.
     *
     * @param absPath The absolute path to the target file or directory. This path is turned into a relative path. May
     *     be an absolute local file system path, with both {@code "\"} and {@code "/"} as separators. May also be an
     *     Eclipse platform URI. Must be normalized (i.e. must not contain "." or ".." parts).
     * @return A path to the given target file or directory, relative to this source file if possible, and an absolute
     *     path otherwise. May be an absolute or relative local file system path, or an Eclipse platform URI. The result
     *     uses {@code "/"} as directory separator.
     * @see PlatformUriUtils#getRelativePath
     */
    public String getRelativePathTo(String absPath) {
        return PlatformUriUtils.getRelativePath(absPath, getAbsDirPath());
    }

    /**
     * Resolves a path against the directory that contains this source file.
     *
     * @param path The path to resolve. May be an absolute or relative local file system path, with both {@code "\"} and
     *     {@code "/"} as separators. May also be an Eclipse platform URI.
     * @return The absolute path that results from the resolving. May be an absolute local file system path, with only
     *     platform specific path separators. May also be an Eclipse platform URI. Is normalized (i.e. does not contain
     *     "." or ".." parts).
     * @see PlatformUriUtils#resolve
     */
    public String resolve(String path) {
        return PlatformUriUtils.resolve(path, getAbsDirPath());
    }

    @Override
    public String toString() {
        return fmt("SourceFile(absPath=%s, main=%s, importingPos=%s, problemPos=%s)", absPath, main, importingPos,
                problemPos);
    }
}
