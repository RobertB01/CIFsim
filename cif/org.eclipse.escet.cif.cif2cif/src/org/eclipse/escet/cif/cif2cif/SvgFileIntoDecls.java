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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;

import java.util.Iterator;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that pushes SVG file declarations inwards into the CIF/SVG declarations.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * SVG file declarations that do not apply to any other SVG declarations, are lost.
 * </p>
 */
public class SvgFileIntoDecls implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Pushing SVG file declarations inwards for a CIF specification with component definitions is "
                    + "currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Push inwards, recursively.
        push(spec, null);
    }

    /**
     * Pushes SVG file declarations inwards, for the given component, recursively.
     *
     * @param comp The component.
     * @param svgFile The currently active SVG file declaration (from the parent component), or {@code null} if not
     *     available/applicable.
     */
    private void push(ComplexComponent comp, SvgFile svgFile) {
        // Get SVG file declaration that applies to this component. Also
        // remove it from the component.
        Iterator<IoDecl> ioIter = comp.getIoDecls().iterator();
        while (ioIter.hasNext()) {
            IoDecl ioDecl = ioIter.next();
            if (ioDecl instanceof SvgFile) {
                // Found SVG file declaration, so update currently active one.
                svgFile = (SvgFile)ioDecl;

                // Remove SVG file declaration from the component.
                ioIter.remove();
            }
        }

        // Push into CIF/SVG declarations of this scope.
        if (svgFile != null) {
            for (IoDecl ioDecl: comp.getIoDecls()) {
                Assert.check(!(ioDecl instanceof SvgFile));
                if (ioDecl instanceof SvgCopy) {
                    push((SvgCopy)ioDecl, svgFile);
                } else if (ioDecl instanceof SvgMove) {
                    push((SvgMove)ioDecl, svgFile);
                } else if (ioDecl instanceof SvgOut) {
                    push((SvgOut)ioDecl, svgFile);
                } else if (ioDecl instanceof SvgIn) {
                    push((SvgIn)ioDecl, svgFile);
                }
            }
        }

        // Recursively push for child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                push((ComplexComponent)child, svgFile);
            }
        }
    }

    /**
     * Pushes SVG file declarations inwards, into the given CIF/SVG copy declaration.
     *
     * @param svgCopy The CIF/SVG copy declaration into which to push the SVG file declaration.
     * @param svgFile The currently active SVG file declaration (from the component in which the copy declaration is
     *     declared).
     */
    private void push(SvgCopy svgCopy, SvgFile svgFile) {
        // If copy declaration already has its own SVG file declaration, we are
        // done.
        if (svgCopy.getSvgFile() != null) {
            return;
        }

        // Put in a copy of the SVG file declaration.
        svgCopy.setSvgFile(deepclone(svgFile));
    }

    /**
     * Pushes SVG file declarations inwards, into the given CIF/SVG move declaration.
     *
     * @param svgMove The CIF/SVG move declaration into which to push the SVG file declaration.
     * @param svgFile The currently active SVG file declaration (from the component in which the move declaration is
     *     declared).
     */
    private void push(SvgMove svgMove, SvgFile svgFile) {
        // If move declaration already has its own SVG file declaration, we are
        // done.
        if (svgMove.getSvgFile() != null) {
            return;
        }

        // Put in a copy of the SVG file declaration.
        svgMove.setSvgFile(deepclone(svgFile));
    }

    /**
     * Pushes SVG file declarations inwards, into the given CIF/SVG output mapping.
     *
     * @param svgOut The CIF/SVG output mapping into which to push the SVG file declaration.
     * @param svgFile The currently active SVG file declaration (from the component in which the mapping is declared).
     */
    private void push(SvgOut svgOut, SvgFile svgFile) {
        // If mapping already has its own SVG file declaration, we are done.
        if (svgOut.getSvgFile() != null) {
            return;
        }

        // Put in a copy of the SVG file declaration.
        svgOut.setSvgFile(deepclone(svgFile));
    }

    /**
     * Pushes SVG file declarations inwards, into the given CIF/SVG input mapping.
     *
     * @param svgIn The CIF/SVG input mapping into which to push the SVG file declaration.
     * @param svgFile The currently active SVG file declaration (from the component in which the mapping is declared).
     */
    private void push(SvgIn svgIn, SvgFile svgFile) {
        // If mapping already has its own SVG file declaration, we are done.
        if (svgIn.getSvgFile() != null) {
            return;
        }

        // Put in a copy of the SVG file declaration.
        svgIn.setSvgFile(deepclone(svgFile));
    }
}
