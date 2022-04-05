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

package org.eclipse.escet.cif.typechecker.scopes;

import static org.eclipse.escet.cif.common.CifExtFuncUtils.updateExtRefRelPaths;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.common.CifExtFuncUtils;
import org.eclipse.escet.cif.common.CifRelativePathUtils;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.parser.ast.AEquation;
import org.eclipse.escet.cif.parser.ast.iodecls.AIoDecl;
import org.eclipse.escet.cif.parser.ast.iodecls.print.APrint;
import org.eclipse.escet.cif.parser.ast.iodecls.print.APrintFile;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgCopy;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgFile;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgIn;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgMove;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgOut;
import org.eclipse.escet.cif.parser.ast.tokens.AStringToken;
import org.eclipse.escet.cif.typechecker.SourceFile;
import org.eclipse.escet.cif.typechecker.SymbolTableEntry;
import org.eclipse.escet.cif.typechecker.declwrap.DeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.InvariantTypeCheckInfo;
import org.eclipse.escet.common.java.Assert;

/**
 * Symbol scope merger. Used to merge imported symbol scopes into the main symbol scope.
 *
 * <p>
 * Some symbol table entries that exist in multiple files (such as groups), are merged. After all imports have been
 * processed, the source metadata and position information will be that of the main file, or the first imported file
 * that is encountered. For entries that can not be merged, an error is reported, and merging will fail.
 * </p>
 */
public class SymbolScopeMerger {
    /** Constructor for the {@link SymbolScopeMerger} class. */
    private SymbolScopeMerger() {
        // Static class.
    }

    /**
     * Merges the specification symbol scope for an imported source file into the specification symbol scope for the
     * main source file.
     *
     * @param mainScope The symbol scope of the main source file.
     * @param mainSource The metadata of the main source file.
     * @param impScope The symbol scope of the imported source file.
     * @param impSource The metadata of the imported source file.
     * @return The merged symbol scope, i.e. the main symbol scope.
     */
    public static SpecScope mergeSpecs(SpecScope mainScope, SourceFile mainSource, SpecScope impScope,
            SourceFile impSource)
    {
        // Fix relative file paths in imported file.
        fixRelativePaths(impScope, mainSource, impSource);

        // Merge scopes recursively.
        mergeScopes(mainScope, impScope, mainSource, impSource);

        // Since the imported symbol scope was merged into the main symbol
        // scope, we return the main symbol scope.
        return mainScope;
    }

    /**
     * Merges a symbol scope for an imported source file into a symbol scope for the main source file.
     *
     * @param mainScope The symbol scope of the main source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param impScope The symbol scope of the imported source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param mainSource The metadata of the main source file.
     * @param impSource The metadata of the imported source file.
     */
    private static void mergeScopes(ParentScope<?> mainScope, ParentScope<?> impScope, SourceFile mainSource,
            SourceFile impSource)
    {
        // Paranoia checking.
        Assert.check((mainScope instanceof SpecScope && impScope instanceof SpecScope)
                || (mainScope instanceof GroupScope && impScope instanceof GroupScope));

        // Merge data of the imported scope into the main scope.
        mergeChildDecls(mainScope, impScope);
        mergeChildNamelessInvs(mainScope, impScope);
        mergeChildScopes(mainScope, impScope, mainSource, impSource);
        mergeEquations(mainScope, impScope);
        mergeIoDecls(mainScope, impScope, mainSource, impSource);
        mainScope.astInitPreds.addAll(impScope.astInitPreds);
        mainScope.astMarkerPreds.addAll(impScope.astMarkerPreds);
    }

    /**
     * Merges child declarations of a symbol scope for an imported source file into a symbol scope for the main source
     * file.
     *
     * @param mainScope The symbol scope of the main source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param impScope The symbol scope of the imported source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     */
    private static void mergeChildDecls(ParentScope<?> mainScope, ParentScope<?> impScope) {
        Group mainGroup = mainScope.getGroup();
        List<Declaration> mainDecls = mainGroup.getDeclarations();
        List<Invariant> mainInvs = mainGroup.getInvariants();

        for (DeclWrap<?> declWrap: impScope.declarations.values()) {
            // Update symbol table.
            mainScope.addDeclaration(declWrap);
            declWrap.changeParent(mainScope);

            // Update metamodel.
            Object decl = declWrap.getObject();
            if (decl instanceof Declaration) {
                mainDecls.add((Declaration)decl);
            } else if (decl instanceof EnumLiteral) {
                // Enumeration is moved, so no need to move its literals.
            } else if (decl instanceof Invariant) {
                mainInvs.add((Invariant)decl);
            } else {
                throw new RuntimeException("Unexpected child decl: " + decl);
            }
        }
    }

    /**
     * Merges child nameless invariants of a symbol scope for an imported source file into a symbol scope for the main
     * source file.
     *
     * @param mainScope The symbol scope of the main source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param impScope The symbol scope of the imported source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     */
    private static void mergeChildNamelessInvs(ParentScope<?> mainScope, ParentScope<?> impScope) {
        Group mainGroup = mainScope.getGroup();
        List<Invariant> mainInvs = mainGroup.getInvariants();

        for (InvariantTypeCheckInfo namelessInvariant: impScope.namelessInvariants) {
            // Update symbol table.
            mainScope.namelessInvariants.add(namelessInvariant);

            // Update metamodel.
            Invariant inv = namelessInvariant.mmInv;
            mainInvs.add(inv);
        }
    }

    /**
     * Merges child scopes of a symbol scope for an imported source file into a symbol scope for the main source file.
     *
     * @param mainScope The symbol scope of the main source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param impScope The symbol scope of the imported source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param mainSource The metadata of the main source file.
     * @param impSource The metadata of the imported source file.
     */
    private static void mergeChildScopes(ParentScope<?> mainScope, ParentScope<?> impScope, SourceFile mainSource,
            SourceFile impSource)
    {
        Group mainGroup = mainScope.getGroup();
        List<Declaration> mainDecls = mainGroup.getDeclarations();
        List<Component> mainComps = mainGroup.getComponents();
        List<ComponentDef> mainDefs = mainGroup.getDefinitions();

        for (SymbolScope<?> childScope: impScope.children.values()) {
            // Special case to merge groups.
            String name = childScope.getName();
            if (childScope instanceof GroupScope && mainScope.defines(name)) {
                SymbolTableEntry entry = mainScope.getEntry(name);
                if (entry instanceof GroupScope) {
                    mergeScopes((GroupScope)entry, (GroupScope)childScope, mainSource, impSource);
                    continue;
                }
            }

            // Update symbol table.
            mainScope.addChildScope(childScope);
            childScope.changeParent(mainScope);

            // Update metamodel.
            Object childObj = childScope.getObject();
            if (childObj instanceof Component) {
                mainComps.add((Component)childObj);
            } else if (childObj instanceof ComponentDef) {
                mainDefs.add((ComponentDef)childObj);
            } else if (childObj instanceof Declaration) {
                mainDecls.add((Declaration)childObj);
            } else {
                throw new RuntimeException("Unknown child obj: " + childObj);
            }
        }
    }

    /**
     * Merges equations of a symbol scope for an imported source file into a symbol scope for the main source file.
     *
     * @param mainScope The symbol scope of the main source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param impScope The symbol scope of the imported source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     */
    private static void mergeEquations(ParentScope<?> mainScope, ParentScope<?> impScope) {
        // Merge mappings, from names to AST representations of the equations.
        Set<Entry<String, List<AEquation>>> impEqnPairs;
        impEqnPairs = impScope.astEquations.entrySet();
        for (Entry<String, List<AEquation>> impEqnPair: impEqnPairs) {
            String name = impEqnPair.getKey();
            List<AEquation> impEqns = impEqnPair.getValue();

            List<AEquation> mainEqns = mainScope.astEquations.get(name);
            if (mainEqns == null) {
                mainScope.astEquations.put(name, impEqns);
            } else {
                mainEqns.addAll(impEqns);
            }
        }

        // Paranoia checking.
        Assert.check(mainScope.mmEquations.isEmpty());
        Assert.check(impScope.mmEquations.isEmpty());
    }

    /**
     * Merges I/O declarations of a symbol scope for an imported source file into a symbol scope for the main source
     * file.
     *
     * @param mainScope The symbol scope of the main source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param impScope The symbol scope of the imported source file. Must be a specification or group scope. Group
     *     definition scopes are not supported.
     * @param mainSource The metadata of the main source file.
     * @param impSource The metadata of the imported source file.
     */
    private static void mergeIoDecls(ParentScope<?> mainScope, ParentScope<?> impScope, SourceFile mainSource,
            SourceFile impSource)
    {
        // Get mapping from absolute paths of I/O file declarations. We only
        // keep one I/O file declaration per type of I/O file declaration, for
        // each absolute path.
        Map<String, AIoDecl> svgFileDeclsMain = map();
        Map<String, AIoDecl> svgFileDeclsImp = map();
        Map<String, AIoDecl> printFileDeclsMain = map();
        Map<String, AIoDecl> printFileDeclsImp = map();

        for (AIoDecl ioDecl: mainScope.astIoDecls) {
            if (ioDecl instanceof ASvgFile) {
                String path = ((ASvgFile)ioDecl).svgPath.txt;
                path = mainSource.resolve(path);
                svgFileDeclsMain.put(path, ioDecl);
            } else if (ioDecl instanceof APrintFile) {
                String path = ((APrintFile)ioDecl).path.txt;
                if (!path.startsWith(":")) {
                    path = mainSource.resolve(path);
                }
                printFileDeclsMain.put(path, ioDecl);
            }
        }

        for (AIoDecl ioDecl: impScope.astIoDecls) {
            if (ioDecl instanceof ASvgFile) {
                String path = ((ASvgFile)ioDecl).svgPath.txt;
                path = impSource.resolve(path);
                svgFileDeclsImp.put(path, ioDecl);
            } else if (ioDecl instanceof APrintFile) {
                String path = ((APrintFile)ioDecl).path.txt;
                if (!path.startsWith(":")) {
                    path = impSource.resolve(path);
                }
                printFileDeclsImp.put(path, ioDecl);
            }
        }

        // Get common paths per I/O file declaration type, and remove
        // corresponding imported I/O file declarations. We thus remove at most
        // one imported I/O file declaration per I/O file declaration type, and
        // only if their resolved paths match.
        svgFileDeclsImp.keySet().retainAll(svgFileDeclsMain.keySet());
        printFileDeclsImp.keySet().retainAll(printFileDeclsMain.keySet());
        for (AIoDecl ioDecl: svgFileDeclsImp.values()) {
            impScope.astIoDecls.remove(ioDecl);
        }
        for (AIoDecl ioDecl: printFileDeclsImp.values()) {
            impScope.astIoDecls.remove(ioDecl);
        }

        // Merge I/O declarations of the imported scope into the main scope.
        mainScope.astIoDecls.addAll(impScope.astIoDecls);
    }

    /**
     * Fixes the relative file paths, to ensure they are valid for the main CIF specification, instead of for the
     * imported CIF specification.
     *
     * @param scope The scope to fix (recursively).
     * @param mainSource The metadata of the main source file.
     * @param impSource The metadata of the imported source file.
     * @see CifRelativePathUtils#adaptRelativePaths
     */
    private static void fixRelativePaths(ParentScope<?> scope, SourceFile mainSource, SourceFile impSource) {
        // Fix external user-defined functions and process scopes recursively.
        for (SymbolScope<?> child: scope.children.values()) {
            if (child instanceof FunctionScope) {
                // Skip internal user-defined functions.
                FunctionScope fscope = (FunctionScope)child;
                if (fscope.obj instanceof InternalFunction) {
                    continue;
                }

                // Fix external function implementation reference.
                fixRelativePaths((ExternalFunction)fscope.obj, mainSource, impSource);
            } else if (child instanceof ParentScope) {
                // Fix recursively.
                fixRelativePaths((ParentScope<?>)child, mainSource, impSource);
            }
        }

        // Fix I/O declarations.
        for (int i = 0; i < scope.astIoDecls.size(); i++) {
            AIoDecl ioDecl = scope.astIoDecls.get(i);
            ioDecl = fixRelativePaths(ioDecl, mainSource, impSource);
            scope.astIoDecls.set(i, ioDecl);
        }
    }

    /**
     * Fixes the relative file paths, to ensure they are valid for the main CIF specification, instead of for the
     * imported CIF specification.
     *
     * @param func The external user-defined function to fix (recursively).
     * @param mainSource The metadata of the main source file.
     * @param impSource The metadata of the imported source file.
     * @see CifExtFuncUtils#updateExtRefRelPaths
     */
    private static void fixRelativePaths(ExternalFunction func, SourceFile mainSource, SourceFile impSource) {
        String extRef = func.getFunction();
        String mainSpecDir = mainSource.getAbsDirPath();
        String impSpecDir = impSource.getAbsDirPath();
        extRef = updateExtRefRelPaths(extRef, impSpecDir, mainSpecDir);
        func.setFunction(extRef);
    }

    /**
     * Fixes the relative file paths, to ensure they are valid for the main CIF specification, instead of for the
     * imported CIF specification.
     *
     * @param ioDecl The AST representation of the I/O declaration to fix (recursively).
     * @param mainSource The metadata of the main source file.
     * @param impSource The metadata of the imported source file.
     * @return The 'fixed' I/O declaration.
     */
    private static AIoDecl fixRelativePaths(AIoDecl ioDecl, SourceFile mainSource, SourceFile impSource) {
        if (ioDecl instanceof ASvgFile) {
            // Fix SVG file I/O declaration file path.
            ASvgFile svgFile = (ASvgFile)ioDecl;
            String path = svgFile.svgPath.txt;
            path = impSource.resolve(path);
            path = mainSource.getRelativePathTo(path);

            // Create new SVG file I/O declaration.
            AStringToken newToken = new AStringToken(path, svgFile.svgPath.position, false);
            svgFile = new ASvgFile(newToken, svgFile.position);
            return svgFile;
        } else if (ioDecl instanceof ASvgCopy) {
            // Skip if no SVG file specified.
            ASvgCopy svgCopy = (ASvgCopy)ioDecl;
            if (svgCopy.svgFile == null) {
                return ioDecl;
            }

            // Fix SVG copy declaration relative paths.
            ASvgFile svgFile = svgCopy.svgFile;
            svgFile = (ASvgFile)fixRelativePaths(svgFile, mainSource, impSource);

            // Create new SVG copy declaration, with replaced SVG file.
            svgCopy = new ASvgCopy(svgCopy.svgId, svgCopy.pre, svgCopy.post, svgFile, svgCopy.position);
            return svgCopy;
        } else if (ioDecl instanceof ASvgMove) {
            // Skip if no SVG file specified.
            ASvgMove svgMove = (ASvgMove)ioDecl;
            if (svgMove.svgFile == null) {
                return ioDecl;
            }

            // Fix SVG move declaration relative paths.
            ASvgFile svgFile = svgMove.svgFile;
            svgFile = (ASvgFile)fixRelativePaths(svgFile, mainSource, impSource);

            // Create new SVG move declaration, with replaced SVG file.
            svgMove = new ASvgMove(svgMove.svgId, svgMove.x, svgMove.y, svgFile, svgMove.position);
            return svgMove;
        } else if (ioDecl instanceof ASvgOut) {
            // Skip if no SVG file specified.
            ASvgOut svgOut = (ASvgOut)ioDecl;
            if (svgOut.svgFile == null) {
                return ioDecl;
            }

            // Fix SVG file relative paths.
            ASvgFile svgFile = svgOut.svgFile;
            svgFile = (ASvgFile)fixRelativePaths(svgFile, mainSource, impSource);

            // Create new SVG output mapping, with replaced SVG file.
            svgOut = new ASvgOut(svgOut.svgId, svgOut.svgAttr, svgOut.svgTextPos, svgOut.value, svgFile,
                    svgOut.position);
            return svgOut;
        } else if (ioDecl instanceof ASvgIn) {
            // Skip if no SVG file specified.
            ASvgIn svgIn = (ASvgIn)ioDecl;
            if (svgIn.svgFile == null) {
                return ioDecl;
            }

            // Fix SVG file relative paths.
            ASvgFile svgFile = svgIn.svgFile;
            svgFile = (ASvgFile)fixRelativePaths(svgFile, mainSource, impSource);

            // Create new SVG input mapping, with replaced SVG file.
            svgIn = new ASvgIn(svgIn.svgId, svgIn.event, svgFile, svgIn.position);
            return svgIn;
        } else if (ioDecl instanceof APrintFile) {
            // Skip special paths.
            APrintFile printFile = (APrintFile)ioDecl;
            if (printFile.path.txt.startsWith(":")) {
                return ioDecl;
            }

            // Fix print file I/O declaration file path.
            String path = printFile.path.txt;
            path = impSource.resolve(path);
            path = mainSource.getRelativePathTo(path);

            // Create new print file I/O declaration.
            AStringToken newToken = new AStringToken(path, printFile.path.position, false);
            printFile = new APrintFile(newToken, printFile.position);
            return printFile;
        } else if (ioDecl instanceof APrint) {
            // Skip if no print file specified.
            APrint print = (APrint)ioDecl;
            if (print.file == null) {
                return ioDecl;
            }

            // Skip special paths.
            APrintFile printFile = print.file;
            if (printFile.path.txt.startsWith(":")) {
                return ioDecl;
            }

            // Fix print file relative paths.
            printFile = (APrintFile)fixRelativePaths(printFile, mainSource, impSource);

            // Create new print I/O declaration, with replaced print file.
            print = new APrint(print.txt, print.fors, print.when, printFile, print.position);
            return print;
        } else {
            throw new RuntimeException("Unknown I/O decl: " + ioDecl);
        }
    }
}
