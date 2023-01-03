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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newPrintFile;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;

import java.util.Iterator;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that pushes print file declarations inwards into the print declarations.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * Print file declarations that do not apply to any print declarations, are lost.
 * </p>
 *
 * <p>
 * Print declarations for which there are no print file declarations that applies to them, get the default
 * ({@code ":stdout"}) as local file declarations.
 * </p>
 */
public class PrintFileIntoDecls implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Pushing print file declarations inwards for a CIF specification with component definitions "
                    + "is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Push inwards, recursively.
        push(spec, null);
    }

    /**
     * Pushes print file declarations inwards, for the given component, recursively.
     *
     * @param comp The component.
     * @param printFile The currently active print file declaration (from the parent component), or {@code null} if not
     *     available/applicable.
     */
    private void push(ComplexComponent comp, PrintFile printFile) {
        // Get print file declaration that applies to this component. Also
        // remove it from the component.
        Iterator<IoDecl> ioIter = comp.getIoDecls().iterator();
        while (ioIter.hasNext()) {
            IoDecl ioDecl = ioIter.next();
            if (ioDecl instanceof PrintFile) {
                // Found print file declaration, so update active one.
                printFile = (PrintFile)ioDecl;

                // Remove print file declaration from the component.
                ioIter.remove();
            }
        }

        // Push into print declarations of this scope.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            Assert.check(!(ioDecl instanceof PrintFile));
            if (ioDecl instanceof Print) {
                push((Print)ioDecl, printFile);
            }
        }

        // Recursively push for child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                push((ComplexComponent)child, printFile);
            }
        }
    }

    /**
     * Pushes print file declarations inwards, into the given print declaration.
     *
     * @param print The print declaration into which to push the print file declaration.
     * @param printFile The currently active print file declaration (from the component in which the print declaration
     *     is declared), or {@code null} if not available.
     */
    private void push(Print print, PrintFile printFile) {
        // If print declaration already has its own print file declaration, we
        // are done.
        if (print.getFile() != null) {
            return;
        }

        // Add a local print file declaration.
        if (printFile == null) {
            // Add default.
            printFile = newPrintFile();
            printFile.setPosition(deepclone(print.getPosition()));
            printFile.setPath(":stdout");
            print.setFile(printFile);
        } else {
            // Put in a copy of the print file declaration.
            print.setFile(deepclone(printFile));
        }
    }
}
