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

package org.eclipse.escet.cif.io;

import static org.eclipse.escet.cif.prettyprinter.CifPrettyPrinter.INDENT;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.util.Locale;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.escet.cif.common.CifRelativePathUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.prettyprinter.CifPrettyPrinter;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.box.StreamCodeBox;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.emf.ecore.xmi.RealXMIResource;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;

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

        // Write as XMI, if it has an '.cifx' file extension.
        if (absFilePath.toLowerCase(Locale.US).endsWith(".cifx")) {
            // Put specification in an EMF resource.
            URI resourceUri = Paths.createEmfURI(absFilePath);
            ResourceSet resourceSet = new ResourceSetImpl();
            RealXMIResource resource = (RealXMIResource)resourceSet.createResource(resourceUri);
            resource.getContents().add(spec);

            // Normalize XMI ids for deterministic output.
            EMFHelper.normalizeXmiIds(resource);

            // Save resource.
            try {
                resource.save(null);
            } catch (IOException e) {
                throw new InvalidInputException(fmt("Failed to load CIF file \"%s\".", absFilePath), e);
            }

            // Cleanup.
            resource.getContents().remove(spec);
            return;
        }

        // Pretty print the CIF specification directly to the file.
        AppStream stream = new FileAppStream(absFilePath);
        StreamCodeBox code = new StreamCodeBox(stream, INDENT);
        CifPrettyPrinter.boxSpec(spec, code);

        // Close the file stream.
        code.close();
    }
}
