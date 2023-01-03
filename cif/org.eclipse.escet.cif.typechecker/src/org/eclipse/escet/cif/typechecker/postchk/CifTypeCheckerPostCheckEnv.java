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

package org.eclipse.escet.cif.typechecker.postchk;

import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.SourceFile;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** {@link CifPostCheckEnv} for the CIF type checker. */
public class CifTypeCheckerPostCheckEnv extends CifPostCheckEnv {
    /** The CIF type checker to use. */
    private final CifTypeChecker tchecker;

    /**
     * Constructor for the {@link CifTypeCheckerPostCheckEnv} class.
     *
     * @param tchecker The CIF type checker to use.
     */
    public CifTypeCheckerPostCheckEnv(CifTypeChecker tchecker) {
        this.tchecker = tchecker;
    }

    @Override
    public void addProblem(ErrMsg message, Position position, String... args) {
        tchecker.addProblem(message, position, args);
    }

    @Override
    public String resolveImport(String path) {
        return tchecker.resolveImport(path);
    }

    @Override
    public String getFileReportPath(String absTargetPath, Position position) {
        // Get source file metadata for the source file in which the problem is
        // to be reported.
        String cifAbsFilePath = position.getLocation();
        SourceFile sourceFile = tchecker.sourceFiles.get(cifAbsFilePath);
        Assert.notNull(sourceFile);

        // Get relative path to the target file, from the CIF file in which the
        // problem is to be reported.
        return sourceFile.getRelativePathTo(absTargetPath);
    }
}
