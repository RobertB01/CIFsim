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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.getName;
import static org.eclipse.escet.cif.typechecker.scopes.SymbolScopeMerger.mergeSpecs;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.position.common.PositionUtils.toTextPosition;

import java.util.List;

import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.parser.ast.ASpecification;
import org.eclipse.escet.cif.typechecker.postchk.CifAnnotationsPostChecker;
import org.eclipse.escet.cif.typechecker.postchk.CifPostCheckEnv;
import org.eclipse.escet.cif.typechecker.postchk.CifPrintPostChecker;
import org.eclipse.escet.cif.typechecker.postchk.CifSvgPostChecker;
import org.eclipse.escet.cif.typechecker.postchk.CifTypeCheckerPostCheckEnv;
import org.eclipse.escet.cif.typechecker.postchk.CyclePostChecker;
import org.eclipse.escet.cif.typechecker.postchk.EventsPostChecker;
import org.eclipse.escet.cif.typechecker.postchk.SingleEventUsePerAutPostChecker;
import org.eclipse.escet.cif.typechecker.scopes.CompInstScope;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.cif.typechecker.scopes.SpecScope;
import org.eclipse.escet.cif.typechecker.scopes.SymbolScopeBuilder;
import org.eclipse.escet.common.app.framework.io.StdAppStream;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.EcoreTypeChecker;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.common.typechecker.SemanticProblem;

/** Type checker for the CIF language. */
public class CifTypeChecker extends EcoreTypeChecker<ASpecification, Specification> {
    /** Whether to enable debugging output for the CIF type checker. */
    public static final boolean DEBUG = false;

    /** The I/O declarations type checker to use. */
    public final IoDeclTypeChecker ioDeclChecker = new IoDeclTypeChecker(this);

    /** Information about the source files being used by the type checker. Is modified in-place. */
    public final SourceFiles sourceFiles = new SourceFiles();

    /** The symbol table that resulted from type checking, or {@code null} if not available. */
    private SpecScope symbolTable = null;

    /**
     * The cycle detection list, which contains the symbol table entries that are in progress of being type checked. If
     * an item that is to be type checked, is already found in this list, a cycle is detected. In that case, a
     * {@link SemanticProblem} should be added to this type checker. Note that this is only used for cycles in
     * constants, types, etc.
     *
     * @see #addToCycle
     * @see #removeFromCycle
     */
    private final List<SymbolTableEntry> cycle = list();

    /**
     * Adds an entry to the cycle detection list.
     *
     * @param entry The entry to add.
     * @throws SemanticException If a cycle is found.
     * @see #cycle
     * @see CyclePostChecker#addToCycle
     */
    public void addToCycle(SymbolTableEntry entry) {
        // If not yet present, just add it.
        if (!cycle.contains(entry)) {
            cycle.add(entry);
            return;
        }

        // Already present: cycle detected. Find ourselves in the cycle.
        int idx = cycle.indexOf(entry);
        Assert.check(idx >= 0);

        // Add error for each element in the found cycle.
        for (int i = idx; i < cycle.size(); i++) {
            SymbolTableEntry startEntry = cycle.get(i);
            StringBuilder txt = new StringBuilder();
            for (int j = i; j < cycle.size(); j++) {
                if (txt.length() > 0) {
                    txt.append(" -> ");
                }
                txt.append(getAbsName(cycle.get(j).getObject()));
            }
            for (int j = idx; j <= i; j++) {
                if (txt.length() > 0) {
                    txt.append(" -> ");
                }
                txt.append(getAbsName(cycle.get(j).getObject()));
            }
            addProblem(ErrMsg.DEF_USE_CYCLE, startEntry.getPosition(), getAbsName(startEntry.getObject()),
                    txt.toString());
        }
        throw new SemanticException();
    }

    /**
     * Removes an entry from the cycle detection list.
     *
     * @param entry The entry to remove.
     * @throws IllegalStateException If the entry to remove is not the top of the cycle detection list.
     * @see #cycle
     * @see CyclePostChecker#removeFromCycle
     */
    public void removeFromCycle(SymbolTableEntry entry) {
        SymbolTableEntry top = cycle.remove(cycle.size() - 1);
        if (top != entry) {
            throw new IllegalStateException("top of cycle != entry");
        }
    }

    @Override
    protected Specification transRoot(ASpecification rootObj) {
        // Initialization.
        symbolTable = null;

        // Phase: Build scope tree and metamodel objects tree for all named
        // objects in the specification. The first iteration builds the main
        // file and adds its imports. The second iteration processes those
        // level 1 imports and adds level 2 imports, etc. This breadth-first
        // order ensures that for each imported source file, we report problems
        // on the first import in the main file that leads to the shortest path
        // to the imported file. All trees are merged.
        SourceFile mainFile = new SourceFile(getSourceFilePath(), true, null, null);
        sourceFiles.add(mainFile);

        SpecScope scope = null;
        while (true) {
            // Obtain next level source files.
            List<SourceFile> sourcesToProcess = sourceFiles.drainQueue();
            if (sourcesToProcess.isEmpty()) {
                break;
            }

            // Process sources.
            for (SourceFile sourceFile: sourcesToProcess) {
                // Build source file. For imported sources, includes parsing.
                ASpecification astSpec = sourceFile.main ? rootObj : null;
                SpecScope fileScope = SymbolScopeBuilder.build(this, sourceFile, mainFile, astSpec);

                // Debugging.
                if (DEBUG) {
                    StdAppStream.OUT.println("### Symbol table:");
                    fileScope.toBox().write(StdAppStream.OUT);
                }

                // Merge.
                if (scope == null) {
                    scope = fileScope;
                } else {
                    scope = mergeSpecs(scope, mainFile, fileScope, sourceFile);
                }
            }
        }

        // Phase: Fully type check the scope with all type information,
        // cross-scope references, etc. This results in a fully type checked
        // symbol table.
        if (scope == null) {
            throw new AssertionError();
        }
        scope.tcheckFull();
        Assert.check(cycle.isEmpty());

        // If we have errors, don't continue checking.
        if (hasError()) {
            throw new SemanticException();
        }

        // Store resulting symbol table. Allows 'getEntryForObject' to retrieve
        // it for 'post' type checking use.
        symbolTable = scope;

        // Phase: Detect component definition/instantiation cycles. Imported
        // scopes are skipped.
        List<ParentScope<?>> cycle = list();
        scope.detectCompDefInstCycles(cycle);

        // Phase: Find unused declarations. Imported scopes are skipped.
        scope.findUnusedDecls();

        // Phase: Some constraints can only be checked after elimination of
        // component definition/instantiation. These checks are skipped if the
        // specification already has errors, to make sure the elimination of
        // component definition/instantiation won't fail on those errors.
        if (!hasError()) {
            // Deep cloning the entire specification is expensive! We avoid it
            // if possible.
            Specification specNoCompDef = scope.getObject();
            if (CifScopeUtils.hasCompDefInst(specNoCompDef)) {
                specNoCompDef = deepclone(specNoCompDef);
                new ElimComponentDefInst().transform(specNoCompDef);
            }

            // Create post check environment for the type checker.
            CifPostCheckEnv env = new CifTypeCheckerPostCheckEnv(this);

            // Check definition/use cycle constraints.
            CyclePostChecker.check(specNoCompDef, env);

            // Check SVG I/O declarations 'post' constraints. Type checker
            // checks types etc during 'normal' phase, but loading of SVG
            // files, and static evaluation of 'id' expressions can only be
            // done in the 'post' phase, as we need to ensure we have no
            // cycles in algebraic variables, etc.
            if (!hasError()) {
                new CifSvgPostChecker(env).check(specNoCompDef);
            }

            // Check print I/O declarations 'post' constraints.
            CifPrintPostChecker.check(specNoCompDef, env);

            // Check 'Automaton.uniqueUsagePerEvent' constraint.
            SingleEventUsePerAutPostChecker.check(specNoCompDef, env);

            // Checks various constraints related to events that are difficult to check when component
            // definition/instantiation is not yet eliminated.
            new EventsPostChecker().check(specNoCompDef, env);

            // Perform additional checks on annotations, using annotation providers.
            new CifAnnotationsPostChecker(env).check(specNoCompDef);
        }

        // Check specification to ensure all objects have position information.
        new PositionInfoPresenceChecker().check(scope.getObject());

        // Return type checking result.
        return scope.getObject();
    }

    @Override
    protected String getEcoreFileExtension() {
        return "cifx";
    }

    /**
     * Returns the symbol table that resulted from type checking, or {@code null} if not available.
     *
     * @return The symbol table that resulted from type checking, or {@code null}.
     */
    public SpecScope getSymbolTable() {
        return symbolTable;
    }

    @Override
    public String resolveImport(String path) {
        // Let the main source file resolve the path.
        SourceFile mainFile = sourceFiles.get(getSourceFilePath());
        Assert.notNull(mainFile);
        return mainFile.resolve(path);
    }

    /**
     * Adds a semantic problem to the list of problems found so far.
     *
     * @param message The CIF type checker problem message describing the semantic problem.
     * @param position Position information of the problem.
     * @param args The arguments to use when formatting the problem message.
     */
    public void addProblem(ErrMsg message, Position position, String... args) {
        addProblem(message, toTextPosition(position), args);
    }

    /**
     * Adds a semantic problem to the list of problems found so far.
     *
     * @param message The CIF type checker problem message describing the semantic problem.
     * @param position Position information of the problem.
     * @param args The arguments to use when formatting the problem message.
     */
    public void addProblem(ErrMsg message, TextPosition position, String... args) {
        // Look up the source file.
        String sourcePath = position.location;
        SourceFile sourceFile = sourceFiles.get(sourcePath);
        if (sourceFile == null) {
            throw new RuntimeException("No SourceFile found: " + sourcePath);
        }

        // Get problem reporting position in the main file.
        TextPosition reportPos = sourceFile.main ? position : sourceFile.problemPos;

        // Format message, and prefix with file path if not the main file.
        String formattedMsg = message.format(args);
        if (!sourceFile.main) {
            SourceFile mainFile = sourceFiles.get(getSourceFilePath());
            String relPath = sourceFile.getRelativePathFrom(mainFile);
            formattedMsg = fmt("File \"%s\": %s", relPath, formattedMsg);
        }

        // Add actual problem.
        addProblem(formattedMsg, message.getSeverity(), reportPos);
    }

    /**
     * Returns the symbol table entry for the given CIF metamodel object.
     *
     * <p>
     * During type checking, no entries can live 'inside' a component instantiation. However, after elimination of
     * component definition/instantiation, for the 'post type checking phase', the contents of the component definition
     * is put in place of the component instantiation. Objects that are part of the component definition then live
     * 'inside' what was originally a component instantiation. As such, for them we ensure that we continue resolving in
     * the component definition entry instead of the component instantiation entry. However, if the instantiated
     * component is the provided object, we return the component instantiation scope, and not the component definition
     * scope.
     * </p>
     *
     * <p>
     * The symbol table must be available when calling this method. The symbol table is only available after the
     * 'normal' type checking phase has completed.
     * </p>
     *
     * @param obj The CIF metamodel object for which to get the symbol table entry. The object must be a named object
     *     for which a symbol table entry exists, or the specification itself.
     * @return The symbol table entry for the given CIF metamodel object.
     */
    public SymbolTableEntry getEntryForObject(PositionObject obj) {
        // Get symbol table.
        SpecScope specScope = getSymbolTable();
        Assert.notNull(specScope);

        // Special case for the specification.
        if (obj instanceof Specification) {
            return specScope;
        }

        // Walk the scope path to the object. Start with the specification
        // scope, which is the last element of the path.
        List<PositionObject> path = CifScopeUtils.getScopePath(obj);
        path.add(0, obj);
        SymbolTableEntry entry = specScope;
        Assert.check(last(path) instanceof Specification);
        for (int i = path.size() - 2; i >= 0; i--) {
            // Get next path element.
            PositionObject pathElem = path.get(i);

            // For component instantiation scopes, continue from the component
            // definition that it instantiates.
            if (entry instanceof CompInstScope) {
                CompInstScope ciscope = (CompInstScope)entry;
                entry = ciscope.getCompDefScope();
            }

            // Get the symbol table entry for the next path element.
            SymbolTableEntry child = ((ParentScope<?>)entry).getEntry(getName(pathElem));

            // Continue with child symbol table entry.
            entry = child;
        }

        // Return the symbol table entry for the metamodel object.
        Assert.check(entry != specScope);
        return entry;
    }
}
