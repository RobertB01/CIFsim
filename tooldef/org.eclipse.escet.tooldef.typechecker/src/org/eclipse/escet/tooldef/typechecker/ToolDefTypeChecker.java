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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolDefImport;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.common.typechecker.TypeChecker;
import org.eclipse.escet.setext.runtime.Token;
import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.Import;
import org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;

/** ToolDef type checker. */
public class ToolDefTypeChecker extends TypeChecker<Script, Script> {
    /**
     * Whether to cache the built-ins loading them only once ({@code true}) or reload them for every top level type
     * checker ({@code false}).
     */
    private static final boolean CACHE_BUILTINS = true;

    /**
     * The cached {@link #builtins}. Is {@code null} if not yet available or if caching is disabled
     * ({@link #CACHE_BUILTINS} is {@code false}). Is empty or partially filled while initializing. Access must be
     * performed in a tread safe manner using a lock on {@link #CACHED_BUILTINS_LOCK}.
     */
    private static Map<String, List<PositionObject>> cachedBuiltins = null;

    /** Thread safety lock for accessing {@link #cachedBuiltins}. */
    private static final Object CACHED_BUILTINS_LOCK = new Object();

    /** The prefix of URIs used to load built-ins from the runtime plug-in. */
    private static final String RUNTIME_BUILTINS_PREFIX = "tooldef://org.eclipse.escet.tooldef.runtime/org/eclipse/escet/tooldef/runtime/builtins/";

    /**
     * Mapping from built-in names to built-in tools and operators with those names. Is {@code null} until initialized.
     * Is empty or partially filled while initializing. Instance is shared with other type checkers that check imported
     * scripts, for performance reasons.
     */
    protected Map<String, List<PositionObject>> builtins = null;

    /**
     * Mapping from globally unique (absolute) reference texts of scripts to the root checker context resulting from
     * type checking them. Is {@code null} until the first import is being resolved. Instance is shared with other type
     * checkers that check imported scripts, for performance reasons.
     */
    protected Map<String, CheckerContext> scripts = null;

    /**
     * Import cycle detection array, which contains for each file in progress of being checked, a globally unique
     * (absolute) reference text, as well as a user-friendly (possibly relative) reference text. It also includes an
     * entry for the current type checker. It is {@code null} for the root type checker.
     */
    protected String[][] importCycle = null;

    /** The type checker context that resulted from type checking, or {@code null} if not available. */
    protected CheckerContext rootCtxt = null;

    @Override
    protected Script transRoot(Script script) {
        // Sanity checking.
        Assert.check(script.getName() == null);

        // Initialize context.
        CheckerContext ctxt = new CheckerContext(this, script);

        // Add built-ins, if not yet present.
        if (builtins == null) {
            addBuiltins(ctxt);
        }

        // Check statements.
        for (Declaration decl: copy(script.getDeclarations())) {
            tcheck(decl, ctxt);
        }

        // If we have errors, don't continue checking.
        if (hasError()) {
            throw new SemanticException();
        }

        // Warn about definitely non-reachable statements.
        ReachabilityChecker.warnNonReachable(script.getDeclarations(), true, ctxt);

        // Warn about definitely not referred/used objects.
        ctxt.checkUnused();

        // Store context for later retrieval.
        rootCtxt = ctxt;

        // Return the script.
        return script;
    }

    /**
     * Type check a ToolDef declaration.
     *
     * @param decl The ToolDef declaration.
     * @param ctxt The type checker context.
     */
    private static void tcheck(Declaration decl, CheckerContext ctxt) {
        if (decl instanceof Script) {
            // Should not be present in AST.
            throw new RuntimeException("Already resolved import? " + decl);
        } else if (decl instanceof Import) {
            ImportsChecker.tcheck((Import)decl, ctxt);
        } else if (decl instanceof Statement) {
            StatementsChecker.tcheck((Statement)decl, ctxt);
        } else if (decl instanceof Tool) {
            Assert.check(decl instanceof ToolDefTool);
            ToolsChecker.tcheck((ToolDefTool)decl, ctxt);
        } else if (decl instanceof TypeDecl) {
            TypeDeclsChecker.tcheck((TypeDecl)decl, ctxt);
        } else {
            throw new RuntimeException("Unknown decl: " + decl);
        }
    }

    /**
     * Adds the built-in tools and operators to this type checker.
     *
     * @param ctxt The type checker context.
     */
    private void addBuiltins(CheckerContext ctxt) {
        // Use cache, if enabled.
        if (CACHE_BUILTINS) {
            // Use built-ins cache in thread safe manner.
            synchronized (CACHED_BUILTINS_LOCK) {
                // Fill cache, if not yet filled.
                if (cachedBuiltins == null) {
                    cachedBuiltins = map();
                    try {
                        addBuiltins(ctxt, cachedBuiltins);
                    } catch (Throwable ex) {
                        // Adding built-in failed. Reset cache to 'null' to
                        // make sure it is added again in the future. Makes
                        // development of the built-ins easier.
                        cachedBuiltins = null;
                        throw ex;
                    }
                }

                // Use cache.
                builtins = cachedBuiltins;
                return;
            }
        }

        // Don't use cache.
        builtins = map();
        addBuiltins(ctxt, builtins);
    }

    /**
     * Adds the built-in tools and operators to the given mapping.
     *
     * @param ctxt The type checker context.
     * @param builtins Mapping from built-in names to built-in tools and operators with those names. Is extended
     *     in-place.
     */
    private void addBuiltins(CheckerContext ctxt, Map<String, List<PositionObject>> builtins) {
        try {
            addBuiltIns(ctxt, RUNTIME_BUILTINS_PREFIX + "builtins_data.tooldef", builtins);
            addBuiltIns(ctxt, RUNTIME_BUILTINS_PREFIX + "builtins_file.tooldef", builtins);
            addBuiltIns(ctxt, RUNTIME_BUILTINS_PREFIX + "builtins_generic.tooldef", builtins);
            addBuiltIns(ctxt, RUNTIME_BUILTINS_PREFIX + "builtins_io.tooldef", builtins);
            addBuiltIns(ctxt, RUNTIME_BUILTINS_PREFIX + "builtins_operator.tooldef", builtins);
            addBuiltIns(ctxt, RUNTIME_BUILTINS_PREFIX + "builtins_path.tooldef", builtins);
        } catch (SemanticException ex) {
            throw new RuntimeException("Failed to load built-ins.", ex);
        }
    }

    /**
     * Adds built-in tools and operators to the given mapping.
     *
     * @param ctxt The type checker context.
     * @param importRef The import reference text that refers to the built-in ToolDef file from which to load the
     *     built-in tools and operators.
     * @param builtins Mapping from built-in names to built-in tools and operators with those names. Is extended
     *     in-place.
     */
    private static void addBuiltIns(CheckerContext ctxt, String importRef, Map<String, List<PositionObject>> builtins) {
        // Process the imports.
        Token impSrc = new Token(importRef, -1, null);
        ToolDefImport imp = newToolDefImport(null, null, null, impSrc);
        CheckerContext impCtxt = ImportsChecker.getScript(imp, ctxt);
        List<PositionObject> objs = impCtxt.getImportableObjects();
        for (PositionObject obj: objs) {
            // Skip imported Java tools.
            if (obj instanceof JavaTool) {
                continue;
            }
            Assert.check(obj instanceof ToolDefTool);
            ToolDefTool tool = (ToolDefTool)obj;

            // Remove '_' prefix of the name.
            String name = ToolDefTextUtils.getName(tool);
            Assert.check(name.startsWith("_"));
            name = name.substring(1);

            // Rename built-in operators with non-identifier names.
            switch (name) {
                case "plus":
                    name = "+";
                    break;
                case "dash":
                    name = "-";
                    break;
                case "star":
                    name = "*";
                    break;
                case "slash":
                    name = "/";
                    break;
                case "lt":
                    name = "<";
                    break;
                case "le":
                    name = "<=";
                    break;
                case "gt":
                    name = ">";
                    break;
                case "ge":
                    name = ">=";
                    break;
                case "eq":
                    name = "==";
                    break;
                case "ne":
                    name = "!=";
                    break;
            }

            // Set actual tool name.
            tool.setName(name);

            // Add tool to built-ins.
            List<PositionObject> others = builtins.get(name);
            if (others == null) {
                others = list();
                builtins.put(name, others);
            }
            others.add(tool);
        }
    }

    /**
     * Resolves an import.
     *
     * @param path The import to resolve. May be an absolute and relative local file system path, ToolDef URI
     *     (tooldef://plugin/local_path), or registered library (lib:lib_name).
     * @param pos Position information of the given path.
     * @param ctxt The type checker context.
     * @return The resolved and normalized import path.
     * @throws IllegalStateException If the source file path is not set.
     * @throws SemanticException If a registered library cannot be found, or multiple matching registered libraries are
     *     found.
     */
    public String resolveImport(String path, TextPosition pos, CheckerContext ctxt) {
        if (path.startsWith("lib:")) {
            // Get library name.
            String name = path.substring("lib:".length());

            // Get plug-in that contains the library.
            String pluginName = null;
            if (!Platform.isRunning()) {
                // If not running in OSGi, plug-in name is not used, so can
                // just use a dummy plug-in name. Use import path for easier
                // debugging.
                pluginName = path;
            } else {
                // Use extension registry to find registered libraries.
                IExtensionRegistry registry = RegistryFactory.getRegistry();
                String extensionPointId = "org.eclipse.escet.tooldef.library";
                IConfigurationElement[] libs;
                libs = registry.getConfigurationElementsFor(extensionPointId);

                // Get plug-ins with matching libraries.
                List<String> pluginNames = list();
                for (IConfigurationElement lib: libs) {
                    String libName = lib.getAttribute("name");
                    if (libName != null && libName.equals(name)) {
                        pluginNames.add(lib.getContributor().getName());
                    }
                }

                // Sort, for deterministic error messages.
                Collections.sort(pluginNames, Strings.SORTER);

                // Make sure we have exactly one matching library.
                if (pluginNames.isEmpty()) {
                    ctxt.addProblem(Message.IMPORT_LIB_NOT_FOUND, pos, name);
                    throw new SemanticException();
                } else if (pluginNames.size() > 1) {
                    for (int i = 1; i < pluginNames.size(); i++) {
                        ctxt.addProblem(Message.IMPORT_LIB_DUPL, pos, name, pluginNames.get(0), pluginNames.get(i));
                    }
                    throw new SemanticException();
                }

                // Set plug-in name.
                pluginName = pluginNames.get(0);
            }

            // Return normalized ToolDef URI.
            return fmt("tooldef://%s/%s.tooldef", pluginName, name);
        } else if (path.startsWith("tooldef://")) {
            // Imported file is specified using a URI with a 'tooldef' scheme.

            // Split imported file URI into parts.
            String pluginBoth = path.substring("tooldef://".length());
            int slashIdx = pluginBoth.indexOf('/');
            String pluginName = (slashIdx == -1) ? "" : pluginBoth.substring(0, slashIdx);
            String pluginPath = (slashIdx == -1) ? pluginBoth : pluginBoth.substring(slashIdx + 1);

            // Normalize path within plug-in.
            pluginPath = Paths.join("/" + pluginPath, ".");
            pluginPath = pluginPath.replace('\\', '/');

            // Combine parts again to form a normalized URI.
            return "tooldef://" + pluginName + pluginPath;
        } else if (getSourceFilePath().startsWith("tooldef://")) {
            // Current file is specified using a URI with a 'tooldef' scheme.
            if (Paths.isAbsolute(path)) {
                // Imported file is an absolute local file system path.
                // Already absolute, so no need to resolve against the current
                // file. Normalize the given path.
                return Paths.join(path, ".");
            } else {
                // Imported file is a relative path. Resolve it relative with
                // respect to the path of the current file, within the plug-in.

                // Split current file URI into parts.
                String pluginBoth = getSourceFilePath().substring("tooldef://".length());
                int slashIdx = pluginBoth.indexOf('/');
                String pluginName = (slashIdx == -1) ? "" : pluginBoth.substring(0, slashIdx);
                String pluginPath = (slashIdx == -1) ? pluginBoth : pluginBoth.substring(slashIdx + 1);

                // Strip off file name.
                pluginPath = Paths.getAbsFilePathDir("/" + pluginPath);

                // Resolve path, within plug-in.
                pluginPath = Paths.resolve(path, pluginPath);
                pluginPath = pluginPath.replace('\\', '/');

                // Combine parts again to form a URI with 'tooldef' scheme.
                return "tooldef://" + pluginName + pluginPath;
            }
        } else {
            // Both the current file and the imported file are specified using
            // local file system paths.
            return resolveImport(path);
        }
    }
}
