//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.postchk;

import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.SourceFile;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;

/**
 * Environment for CIF type checker post phase checks. Enables abstracting away resolving of external files, problem
 * reporting, etc. Allows the post checks to be performed by the type checker, as well as other tools, such as the
 * merger tool.
 */
public abstract class CifPostCheckEnv {
    /**
     * Adds a semantic problem to the list of problems found so far.
     *
     * @param message The CIF type checker problem message describing the semantic problem.
     * @param position Position information of the problem. Should only be used by the type checker environment.
     * @param args The arguments to use when formatting the problem message.
     */
    public abstract void addProblem(ErrMsg message, Position position, String... args);

    /**
     * Resolves the path of an import. If the path is relative, it is interpreted relative to the directory that
     * contains the CIF file being checked.
     *
     * @param path The path to the CIF file to import. May be an absolute or relative local file system path, with both
     *     {@code \} and {@code /} as separators. May also be an Eclipse platform URI.
     * @return The {@link SourceFile#absPath absolute path} to the CIF file to import.
     * @throws IllegalStateException If the source CIF file path is not available.
     */
    public abstract String resolveImport(String path);

    /**
     * Returns the path to use to refer to the given target file, in an error message.
     *
     * <p>
     * For the type checker environment, the returned path may be relative to the CIF file obtained from the given
     * position information. When using that position information to report a problem, the relative (or absolute) path
     * obtained from this method, can be used in the error message, and will be valid for the CIF file in which the
     * problem is reported.
     * </p>
     *
     * @param absTargetPath The {@link SourceFile#absPath absolute path} to the target file for which to return a
     *     relative path.
     * @param position Position information for the CIF file in which to report the problem. Should only be used by the
     *     type checker environment.
     * @return A relative (or absolute if relative is not possible) path to the given target file, to be used in an
     *     error message.
     * @see PlatformUriUtils#getRelativePath
     */
    public abstract String getFileReportPath(String absTargetPath, Position position);
}
