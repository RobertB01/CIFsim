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

package org.eclipse.escet.cif.cif2cif;

import java.util.Iterator;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;

/**
 * In-place transformation that removes I/O declarations.
 *
 * @see RemovePrintDecls
 * @see RemoveCifSvgDecls
 */
public class RemoveIoDecls implements CifToCifTransformation {
    /** Whether to remove print and print file declarations. */
    private final boolean removePrintDecls;

    /** Whether to remove CIF/SVG declarations. */
    private final boolean removeCifSvgDecls;

    /** Whether CIF/SVG input declarations were removed in the last transformation. */
    private boolean svgInputDeclarationsWereRemoved = false;

    /** Constructor for the {@link RemoveIoDecls} class. Removes all I/O declarations. */
    public RemoveIoDecls() {
        this(true, true);
    }

    /**
     * Constructor for the {@link RemoveIoDecls} class.
     *
     * @param removePrintDecls Whether to remove print and print file declarations.
     * @param removeCifSvgDecls Whether to remove CIF/SVG declarations.
     */
    public RemoveIoDecls(boolean removePrintDecls, boolean removeCifSvgDecls) {
        this.removePrintDecls = removePrintDecls;
        this.removeCifSvgDecls = removeCifSvgDecls;
    }

    @Override
    public void transform(Specification spec) {
        svgInputDeclarationsWereRemoved = false;
        removeIoDecls(spec);
    }

    /**
     * Removes I/O declarations from the given component, recursively.
     *
     * @param comp The component for which to remove I/O declarations, recursively.
     */
    private void removeIoDecls(ComplexComponent comp) {
        // Remove I/O declarations of this component.
        if (!comp.getIoDecls().isEmpty()) {
            if (removePrintDecls && removeCifSvgDecls) {
                // Determine whether an SVG input declaration will be removed.
                for (IoDecl decl: comp.getIoDecls()) {
                    if (decl instanceof SvgIn) {
                        svgInputDeclarationsWereRemoved = true;
                    }
                }

                // Remove all declarations.
                comp.getIoDecls().clear();
            } else {
                Iterator<IoDecl> iter = comp.getIoDecls().iterator();
                while (iter.hasNext()) {
                    IoDecl decl = iter.next();
                    boolean remove = false;
                    remove |= removePrintDecls && (decl instanceof Print || decl instanceof PrintFile);
                    remove |= removeCifSvgDecls && (decl instanceof SvgCopy || decl instanceof SvgFile
                            || decl instanceof SvgIn || decl instanceof SvgOut || decl instanceof SvgMove);
                    if (remove) {
                        // Determine whether an SVG input declaration will be removed.
                        if (decl instanceof SvgIn) {
                            svgInputDeclarationsWereRemoved = true;
                        }

                        // Remove declaration.
                        iter.remove();
                    }
                }
            }
        }

        // Apply recursively to child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                if (child instanceof ComponentInst) {
                    continue;
                }
                removeIoDecls((ComplexComponent)child);
            }
        }

        // Apply recursively to child component definitions.
        if (comp instanceof Group) {
            for (ComponentDef def: ((Group)comp).getDefinitions()) {
                removeIoDecls(def.getBody());
            }
        }
    }

    /**
     * Returns whether CIF/SVG input declarations were removed in the last transformation.
     *
     * @return {@code true} if CIF/SVG input declarations were removed in the last transformation, {@code false}
     *     otherwise.
     */
    public boolean haveAnySvgInputDeclarationsBeenRemoved() {
        return svgInputDeclarationsWereRemoved;
    }
}
