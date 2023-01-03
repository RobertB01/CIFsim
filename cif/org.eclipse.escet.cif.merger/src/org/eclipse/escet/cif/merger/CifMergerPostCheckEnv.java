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

package org.eclipse.escet.cif.merger;

import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedstrings;

import java.util.Set;

import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.postchk.CifPostCheckEnv;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** {@link CifPostCheckEnv} for the CIF merger. */
public class CifMergerPostCheckEnv extends CifPostCheckEnv {
    /** The absolute local file system path to the directory containing the merged CIF file. */
    private final String absCifDirPath;

    /** Error messages reported so far. */
    public Set<String> errors = set();

    /**
     * Constructor for the {@link CifMergerPostCheckEnv} class.
     *
     * @param absCifDirPath The absolute local file system path to the directory containing the merged CIF file.
     */
    public CifMergerPostCheckEnv(String absCifDirPath) {
        this.absCifDirPath = absCifDirPath;
    }

    @Override
    public void addProblem(ErrMsg message, Position position, String... args) {
        String formattedMsg = message.format(args);
        if (message.getSeverity() == SemanticProblemSeverity.ERROR) {
            errors.add(formattedMsg);
        }
    }

    @Override
    public String resolveImport(String path) {
        return PlatformUriUtils.resolve(path, absCifDirPath);
    }

    @Override
    public String getFileReportPath(String absTargetPath, Position position) {
        return PlatformUriUtils.getRelativePath(absTargetPath, absCifDirPath);
    }

    /** Prints the reported errors to the console, if any. */
    public void printErrors() {
        if (errors.isEmpty()) {
            return;
        }

        // Print header.
        OutputProvider.err("Problems detected in merged CIF specification:");

        // Print errors, sorted and without duplicates.
        for (String error: sortedstrings(errors)) {
            OutputProvider.err(" - ERROR: " + error);
        }
    }
}
