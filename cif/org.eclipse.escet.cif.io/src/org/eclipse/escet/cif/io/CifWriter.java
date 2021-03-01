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

package org.eclipse.escet.cif.io;

import static org.eclipse.escet.cif.prettyprinter.CifPrettyPrinter.INDENT;

import org.eclipse.escet.cif.common.CifRelativePathUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.prettyprinter.CifPrettyPrinter;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.box.StreamCodeBox;

/** CIF writer. */
public class CifWriter {
    /** Constructor for the {@link CifWriter} class. */
    private CifWriter() {
        // Static class.
    }

    /**
     * Writes a CIF specification to a file.
     *
     * @param spec The CIF specification to write. Is modified in-place by adapting relative path to the directory that
     *     contains the output file.
     * @param absFilePath The path to the output file to which to write the specification. Must be an absolute local
     *     file system path.
     * @param specPath The absolute local file system path against which to resolve the relative paths used in the CIF
     *     specification.
     */
    public static void writeCifSpec(Specification spec, String absFilePath, String specPath) {
        // Adapt relative paths.
        String absDirPath = Paths.getAbsFilePathDir(absFilePath);
        CifRelativePathUtils.adaptRelativePaths(spec, specPath, absDirPath);

        // Pretty print the CIF specification directly to the file.
        AppStream stream = new FileAppStream(absFilePath);
        StreamCodeBox code = new StreamCodeBox(stream, INDENT);
        CifPrettyPrinter.boxSpec(spec, code);

        // Close the file stream.
        code.close();
    }
}
