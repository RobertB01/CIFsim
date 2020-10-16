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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifExtFuncUtils.updateExtRefRelPaths;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.java.Assert;

/** CIF relative path utility methods. */
public class CifRelativePathUtils {
    /** Constructor for the {@link CifRelativePathUtils} class. */
    private CifRelativePathUtils() {
        // Static class.
    }

    /**
     * Adapts the relative file paths of a component (recursively), to ensure they are valid for the given new path,
     * instead of the given old path.
     *
     * <p>
     * See also {@code SymbolScopeMerger.fixRelativePaths} for similar code, mostly on AST representations.
     * </p>
     *
     * @param component The component to adapt (recursively). The component may contain component
     *     definitions/instantiations.
     * @param oldPath The absolute local file system path of the directory from which relative paths are currently
     *     interpreted.
     * @param newPath The absolute local file system path of the directory from which relative paths are to be
     *     interpreted, after this method returns.
     */
    public static void adaptRelativePaths(ComplexComponent component, String oldPath, String newPath) {
        // Adapt external user-defined functions.
        for (Declaration decl: component.getDeclarations()) {
            if (decl instanceof ExternalFunction) {
                adaptRelativePaths((ExternalFunction)decl, oldPath, newPath);
            }
        }

        // Adapt I/O declarations.
        for (IoDecl ioDecl: component.getIoDecls()) {
            adaptRelativePaths(ioDecl, oldPath, newPath);
        }

        // Process child components and component definitions recursively.
        if (component instanceof Group) {
            Group group = (Group)component;

            for (Component child: group.getComponents()) {
                if (child instanceof ComplexComponent) {
                    adaptRelativePaths((ComplexComponent)child, oldPath, newPath);
                } else {
                    // Component instantiations have no functions and I/O
                    // declarations.
                    Assert.check(child instanceof ComponentInst);
                }
            }

            for (ComponentDef cdef: group.getDefinitions()) {
                adaptRelativePaths(cdef.getBody(), oldPath, newPath);
            }
        }
    }

    /**
     * Adapts the relative file paths of a function, to ensure they are valid for the given new path, instead of the
     * given old path.
     *
     * @param func The external user-defined function to adapt.
     * @param oldPath The absolute local file system path of the directory from which relative paths are currently
     *     interpreted.
     * @param newPath The absolute local file system path of the directory from which relative paths are to be
     *     interpreted, after this method returns.
     */
    public static void adaptRelativePaths(ExternalFunction func, String oldPath, String newPath) {
        String extRef = func.getFunction();
        extRef = updateExtRefRelPaths(extRef, oldPath, newPath);
        func.setFunction(extRef);
    }

    /**
     * Adapts the relative file paths of an I/O declaration, to ensure they are valid for the given new path, instead of
     * the given old path.
     *
     * @param ioDecl The AST representation of the I/O declaration to adapt (recursively).
     * @param oldPath The absolute local file system path of the directory from which relative paths are currently
     *     interpreted.
     * @param newPath The absolute local file system path of the directory from which relative paths are to be
     *     interpreted, after this method returns.
     */
    public static void adaptRelativePaths(IoDecl ioDecl, String oldPath, String newPath) {
        if (ioDecl instanceof SvgFile) {
            // Adapt SVG file declaration file path.
            SvgFile svgFile = (SvgFile)ioDecl;
            String path = svgFile.getPath();
            path = Paths.resolve(path, oldPath);
            path = Paths.getRelativePath(path, newPath);
            svgFile.setPath(path);
        } else if (ioDecl instanceof SvgCopy) {
            // Skip if no SVG file specified.
            SvgCopy svgCopy = (SvgCopy)ioDecl;
            SvgFile svgFile = svgCopy.getSvgFile();
            if (svgFile == null) {
                return;
            }

            // Adapt SVG file declaration.
            adaptRelativePaths(svgFile, oldPath, newPath);
        } else if (ioDecl instanceof SvgMove) {
            // Skip if no SVG file specified.
            SvgMove svgMove = (SvgMove)ioDecl;
            SvgFile svgFile = svgMove.getSvgFile();
            if (svgFile == null) {
                return;
            }

            // Adapt SVG file declaration.
            adaptRelativePaths(svgFile, oldPath, newPath);
        } else if (ioDecl instanceof SvgOut) {
            // Skip if no SVG file specified.
            SvgOut svgOut = (SvgOut)ioDecl;
            SvgFile svgFile = svgOut.getSvgFile();
            if (svgFile == null) {
                return;
            }

            // Adapt SVG file declaration.
            adaptRelativePaths(svgFile, oldPath, newPath);
        } else if (ioDecl instanceof SvgIn) {
            // Skip if no SVG file specified.
            SvgIn svgIn = (SvgIn)ioDecl;
            SvgFile svgFile = svgIn.getSvgFile();
            if (svgFile == null) {
                return;
            }

            // Adapt SVG file declaration.
            adaptRelativePaths(svgFile, oldPath, newPath);
        } else if (ioDecl instanceof PrintFile) {
            // Get print file I/O declaration file path.
            PrintFile printFile = (PrintFile)ioDecl;
            String path = printFile.getPath();

            // Skip special paths.
            if (path.startsWith(":")) {
                return;
            }

            // Adapt print file I/O declaration file path.
            path = Paths.resolve(path, oldPath);
            path = Paths.getRelativePath(path, newPath);
            printFile.setPath(path);
        } else if (ioDecl instanceof Print) {
            // Skip if no print file specified.
            Print print = (Print)ioDecl;
            PrintFile printFile = print.getFile();
            if (printFile == null) {
                return;
            }

            // Adapt print file declaration.
            adaptRelativePaths(printFile, oldPath, newPath);
        } else {
            throw new RuntimeException("Unknown I/O decl: " + ioDecl);
        }
    }
}
