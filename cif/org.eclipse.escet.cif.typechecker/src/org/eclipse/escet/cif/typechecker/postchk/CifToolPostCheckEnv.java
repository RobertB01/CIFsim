//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** {@link CifPostCheckEnv} for tools that are not the CIF type checker. */
public class CifToolPostCheckEnv extends CifPostCheckEnv {
    /** The absolute local file system path to the directory containing the CIF file to check. */
    private final String absCifDirPath;

    /** A description of the CIF specification. For instance, {@code "merged"} for a 'merged CIF specification'. */
    private final String specDescription;

    /** Error messages reported so far. */
    public Set<String> errors = set();

    /**
     * Constructor for the {@link CifToolPostCheckEnv} class.
     *
     * @param absCifDirPath The absolute local file system path to the directory containing the CIF file to check.
     * @param specDescription A description of the CIF specification. For instance, {@code "merged"} for a 'merged CIF
     *     specification'.
     */
    public CifToolPostCheckEnv(String absCifDirPath, String specDescription) {
        this.absCifDirPath = absCifDirPath;
        this.specDescription = specDescription;
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

    /**
     * Throw an {@link UnsupportedException} if there are any errors, or do nothing otherwise.
     *
     * @param operationFailedMessage A message describing the operation that failed. It should be a full sentence,
     *     starting with a capital letter, and ending with a period. It is used as message for a wrapping exception. If
     *     {@code null}, no wrapping exception is created, and the detailed exception is thrown directly.
     * @throws UnsupportedException If there are any errors.
     */
    public void throwUnsupportedExceptionIfAnyErrors(String operationFailedMessage) {
        if (errors.isEmpty()) {
            return;
        }

        String reasons = sortedstrings(errors).stream().map(m -> " - " + m).collect(Collectors.joining("\n"));
        String message = fmt("The %s CIF specification is invalid:\n%s", specDescription, reasons);
        if (operationFailedMessage == null) {
            throw new UnsupportedException(message);
        } else {
            throw new UnsupportedException(operationFailedMessage, new UnsupportedException(message));
        }
    }
}
