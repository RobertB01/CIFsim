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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.position.common.PositionUtils.toTextPosition;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.getAbsDescr;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.areDistinguishableTypes;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolParamExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolRef;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeParamRef;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeRef;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newVariableExpression;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/** ToolDef type checker context/scope. */
public class CheckerContext extends PositionObjectImpl {
    /** The ToolDef type checker to use. */
    public final ToolDefTypeChecker tchecker;

    /** The parent context, or {@code null} for the root context. */
    private final CheckerContext parent;

    /** The ToolDef metamodel object representing this context/scope. */
    private final PositionObject scope;

    /** Mapping from names to objects with those names. */
    private Map<String, List<PositionObject>> objects = map();

    /**
     * The objects that are referred or via which we referred something. This is a subset of the values to which
     * {@link #objects} maps.
     */
    private Set<PositionObject> referred = set();

    /**
     * Constructor for the {@link CheckerContext} class, for a root context.
     *
     * @param tchecker The ToolDef type checker to use.
     * @param script The ToolDef metamodel script representing this context/scope.
     */
    public CheckerContext(ToolDefTypeChecker tchecker, Script script) {
        this.tchecker = tchecker;
        this.parent = null;
        this.scope = script;
    }

    /**
     * Constructor for the {@link CheckerContext} class, for a non-root context.
     *
     * @param parent The parent context.
     * @param scope The ToolDef metamodel object representing this context/scope.
     */
    public CheckerContext(CheckerContext parent, PositionObject scope) {
        this.tchecker = parent.tchecker;
        this.parent = parent;
        this.scope = scope;
    }

    @Override
    public Position getPosition() {
        return scope.getPosition();
    }

    @Override
    public void setPosition(Position position) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the ToolDef metamodel object representing this context/scope.
     *
     * @return The ToolDef metamodel object representing this context/scope.
     */
    public PositionObject getScope() {
        return scope;
    }

    /**
     * Is this the root context?
     *
     * @return {@code true} if this is the root context, {@code false} otherwise.
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Returns the root type checker context.
     *
     * @return The root type checker context.
     */
    public CheckerContext getRoot() {
        CheckerContext root = this;
        while (!root.isRoot()) {
            root = root.parent;
        }
        Assert.notNull(root);
        return root;
    }

    /**
     * Reports a problem to the user, by adding the problem to the type checker problems.
     *
     * @param msg The message to use.
     * @param position The position for which to report the problem.
     * @param args The message arguments.
     */
    public void addProblem(Message msg, Position position, String... args) {
        addProblem(msg, toTextPosition(position), args);
    }

    /**
     * Reports a problem to the user, by adding the problem to the type checker problems.
     *
     * @param msg The message to use.
     * @param position The position for which to report the problem.
     * @param args The message arguments.
     */
    public void addProblem(Message msg, TextPosition position, String... args) {
        // Make sure the position information is valid for the current file.
        if (position == null || !position.location.equals(tchecker.getSourceFilePath())) {
            String inMsg = msg.severity.toString() + ": " + msg.format(args);
            Exception inner = new RuntimeException(inMsg);
            String exMsg = (position == null) ? "Missing position info" : "Position info wrong file";
            exMsg += ", or built-in library error.";
            throw new RuntimeException(exMsg, inner);
        }

        // Add the actual problem.
        tchecker.addProblem(msg.format(args), msg.severity, position);
    }

    /**
     * Adds a ToolDef declaration to the context.
     *
     * @param obj The ToolDef declaration to add. Must be a named object.
     */
    public void addDecl(PositionObject obj) {
        // Unwrap imported scripts.
        PositionObject wrappedObj = obj;
        if (wrappedObj instanceof CheckerContext) {
            obj = ((CheckerContext)wrappedObj).getScope();
        }

        // Get name.
        String name = ToolDefTextUtils.getName(obj);

        // Get existing entries in this context, with same name.
        List<PositionObject> others = objects.get(name);
        if (others == null) {
            others = list();
            objects.put(name, others);
        }

        // Check for conflicts. Only tools (including Java methods) may be
        // overloaded.
        SymbolKind objKind = getSymbolKind(obj);
        for (PositionObject other: others) {
            // Unwrap 'other'.
            PositionObject wrappedOther = other;
            if (wrappedOther instanceof CheckerContext) {
                other = ((CheckerContext)wrappedOther).getScope();
            }

            // Check for conflict.
            SymbolKind otherKind = getSymbolKind(other);
            if (objKind == SymbolKind.TOOL && otherKind == SymbolKind.TOOL) {
                // Check for different overloads.
                checkToolOverloads(obj, other);
            } else {
                // Conflict.
                addProblem(Message.DUPL_NAME, wrappedObj.getPosition(), name, ToolDefTextUtils.getDescr(obj),
                        ToolDefTextUtils.getDescr(other), ToolDefTextUtils.getAbsDescr(scope));
                addProblem(Message.DUPL_NAME, wrappedOther.getPosition(), name, ToolDefTextUtils.getDescr(other),
                        ToolDefTextUtils.getDescr(obj), ToolDefTextUtils.getAbsDescr(scope));
                throw new SemanticException();
            }
        }

        // No conflict. Add it.
        others.add(wrappedObj);
    }

    /**
     * Checks two tools with the same name to ensure they are different overloads, and can exist together in the same
     * context.
     *
     * @param obj1 The first tool.
     * @param obj2 The second tool.
     */
    private void checkToolOverloads(PositionObject obj1, PositionObject obj2) {
        // Get tools.
        Tool tool1 = (Tool)obj1;
        Tool tool2 = (Tool)obj2;

        // Get parameters.
        List<ToolParameter> params1 = tool1.getParameters();
        List<ToolParameter> params2 = tool2.getParameters();

        // If different number of parameters, then different overloads.
        if (params1.size() != params2.size()) {
            return;
        }

        // Check parameters pair wise.
        for (int i = 0; i < params1.size(); i++) {
            ToolParameter param1 = params1.get(i);
            ToolParameter param2 = params2.get(i);

            // If difference in variadicness, then different overloads.
            if (param1.isVariadic() != param2.isVariadic()) {
                return;
            }

            // Check for distinguishable types.
            if (areDistinguishableTypes(param1.getType(), param2.getType())) {
                return;
            }
        }

        // Can't distinguish overloads based on their parameters.
        addProblem(Message.TOOL_DUPL_OVERLOAD, obj1.getPosition(), getAbsDescr(obj1), getAbsDescr(obj2));
        addProblem(Message.TOOL_DUPL_OVERLOAD, obj2.getPosition(), getAbsDescr(obj1), getAbsDescr(obj2));
        throw new SemanticException();
    }

    /**
     * Returns the objects with the given name declared in this context. Only this context is used, not any of the
     * ancestors. If no objects with the given name are found, {@code null} is returned.
     *
     * <p>
     * In general, to resolve references in for instance expressions, one of the resolve methods should be used instead
     * of this method.
     * </p>
     *
     * @param name The name of the objects.
     * @return The objects, or {@code null}.
     */
    public List<PositionObject> getObjects(String name) {
        List<PositionObject> rslts = objects.get(name);
        if (rslts != null) {
            // Copy to prevent the list in 'objects' being changed.
            rslts = copy(rslts);

            // Unwrap imported scripts in the copied list.
            for (int i = 0; i < rslts.size(); i++) {
                PositionObject rslt = rslts.get(i);
                if (rslt instanceof CheckerContext) {
                    rslts.set(i, ((CheckerContext)rslt).getScope());
                }
            }
        }
        return rslts;
    }

    /**
     * Returns the importable objects declared in this context. Only this context is used, not any of the ancestors.
     *
     * <p>
     * In general, to resolve references in for instance expressions, one of the resolve methods should be used instead
     * of this method.
     * </p>
     *
     * @return The importable objects.
     */
    public List<PositionObject> getImportableObjects() {
        List<PositionObject> rslt = list();
        for (Entry<String, List<PositionObject>> entry: objects.entrySet()) {
            for (PositionObject obj: entry.getValue()) {
                if (obj instanceof Tool) {
                    rslt.add(obj);
                }
                if (obj instanceof TypeDecl) {
                    rslt.add(obj);
                }
            }
        }
        return rslt;
    }

    /**
     * Resolve the given reference text in this context, as built-in tool or operator.
     *
     * @param name The reference text.
     * @param position The position information for the reference text.
     * @return The results of resolving the reference, as tool references.
     */
    public List<ToolRef> resolveBuiltin(String name, Position position) {
        // Look up in type checker.
        List<PositionObject> tools = tchecker.builtins.get(name);

        // Create tool references.
        List<ToolRef> rslts = listc(tools.size());
        for (PositionObject tool: tools) {
            Assert.check(tool instanceof Tool);
            rslts.add(newToolRef(true, name, deepclone(position), (Tool)tool));
        }
        return rslts;
    }

    /**
     * Resolve the given reference text in this context, as a type.
     *
     * @param name The reference text.
     * @param position The position information for the reference text.
     * @return The result of resolving the reference, as a type.
     */
    public ToolDefType resolveType(String name, Position position) {
        // Resolve.
        List<PositionObject> resolveds = resolve(name, position, this, true);

        // Check valid object, and create type.
        ToolDefType rslt = null;
        for (PositionObject resolved: resolveds) {
            if (resolved instanceof TypeDecl) {
                Assert.check(rslt == null);
                rslt = newTypeRef(false, deepclone(position), (TypeDecl)resolved);
            } else if (resolved instanceof TypeParam) {
                Assert.check(rslt == null);
                rslt = newTypeParamRef(false, deepclone(position), (TypeParam)resolved);
            } else {
                addProblem(Message.INVALID_REF, position, name, "type", getAbsDescr(resolved),
                        getSymbolKind(resolved).toString(), "type");
                throw new SemanticException();
            }
        }
        Assert.notNull(rslt);
        return rslt;
    }

    /**
     * Resolve the given reference text in this context, as a value.
     *
     * @param name The reference text.
     * @param position The position information for the reference text.
     * @return The result of resolving the reference, as an expression.
     */
    public Expression resolveValue(String name, Position position) {
        // Resolve.
        List<PositionObject> resolveds = resolve(name, position, this, false);

        // Check valid object, and create expression.
        Expression rslt = null;
        for (PositionObject resolved: resolveds) {
            if (resolved instanceof Variable) {
                Assert.check(rslt == null);
                Variable var = (Variable)resolved;
                rslt = newVariableExpression(deepclone(position), deepclone(var.getType()), var);
            } else if (resolved instanceof ToolParameter) {
                Assert.check(rslt == null);
                ToolParameter param = (ToolParameter)resolved;
                rslt = newToolParamExpression(param, deepclone(position), deepclone(param.getType()));
            } else {
                addProblem(Message.INVALID_REF, position, name, "value", getAbsDescr(resolved),
                        getSymbolKind(resolved).toString(), "value");
                throw new SemanticException();
            }
        }
        Assert.notNull(rslt);
        return rslt;
    }

    /**
     * Resolve the given reference text in this context, as tools.
     *
     * @param name The reference text.
     * @param position The position information for the reference text.
     * @return The results of resolving the reference, as tool references.
     */
    public List<ToolRef> resolveTool(String name, Position position) {
        // Resolve.
        List<PositionObject> resolveds = resolve(name, position, this, true);

        // Check valid objects, and create tool references.
        List<ToolRef> rslts = listc(resolveds.size());
        for (PositionObject resolved: resolveds) {
            if (resolved instanceof Tool) {
                rslts.add(newToolRef(false, name, deepclone(position), (Tool)resolved));
            } else {
                addProblem(Message.INVALID_REF, position, name, "tool", getAbsDescr(resolved),
                        getSymbolKind(resolved).toString(), "tool");
                throw new SemanticException();
            }
        }
        return rslts;
    }

    /**
     * Resolves a textual reference against this context. Note that {@code $} characters have already been removed by
     * the parser.
     *
     * @param name The textual reference to resolve.
     * @param position Position information for the textual reference.
     * @param ctxt The type checker context to which to report problems.
     * @param allowEscapeTool Whether to allow resolving to resolve at a higher level when the current level is a tool.
     * @return The resolved symbols.
     */
    @SuppressWarnings("null")
    private List<PositionObject> resolve(String name, Position position, CheckerContext ctxt, boolean allowEscapeTool) {
        // Get first identifier from 'name'.
        int curIdx = 0;
        int dotIdx = name.indexOf('.', curIdx);
        if (dotIdx == -1) {
            dotIdx = name.length();
        }
        String id = name.substring(curIdx, dotIdx);

        // Resolve the first identifier.
        List<PositionObject> rslts = null;
        CheckerContext resolveCtxt = this;
        while (true) {
            // Look in current context for matches.
            rslts = resolveCtxt.objects.get(id);
            if (rslts != null) {
                // Mark resolved objects as 'referred'. This can be final
                // results or a 'via' scope from which we continue resolving.
                for (PositionObject rslt: rslts) {
                    resolveCtxt.referred.add(rslt);
                }
                break;
            }

            // No match in current context. Try higher context, if allowed.
            if (!allowEscapeTool && resolveCtxt.getScope() instanceof Tool) {
                ctxt.addProblem(Message.RESOLVE_NOT_FOUND, position, id, getAbsDescr(scope), "");
                throw new SemanticException();
            }

            // No match in current context. Try higher context, if available.
            resolveCtxt = resolveCtxt.parent;
            if (resolveCtxt == null) {
                ctxt.addProblem(Message.RESOLVE_NOT_FOUND, position, id, getAbsDescr(scope),
                        isRoot() ? "" : " or at a higher level");
                throw new SemanticException();
            }
        }

        // If there is a part of the textual reference left, we should resolve
        // that via the scope we just resolved. Keep doing so until we have
        // resolved the entire textual reference.
        while (dotIdx < name.length()) {
            // Get next identifier.
            curIdx = dotIdx + 1;
            dotIdx = name.indexOf('.', curIdx);
            if (dotIdx == -1) {
                dotIdx = name.length();
            }
            id = name.substring(curIdx, dotIdx);

            // Make sure we are continuing resolving from a single scope.
            if (rslts.size() == 1) {
                // Make sure it is a scope.
                PositionObject rslt = first(rslts);
                if (!(rslt instanceof CheckerContext)) {
                    ctxt.addProblem(Message.RESOLVE_VIA_NON_SCRIPT, position, name.substring(curIdx),
                            getAbsDescr(rslt));
                    throw new SemanticException();
                }
            } else {
                // We don't have scope overloading, so we resolved something
                // other than a scope.
                Assert.check(rslts.size() > 1);

                // Get kinds. Should all be the same as we don't have
                // overloading for different kinds.
                SymbolKind kind = null;
                for (PositionObject rslt: rslts) {
                    SymbolKind rsltKind = getSymbolKind(rslt);
                    if (kind == null) {
                        kind = rsltKind;
                    } else {
                        Assert.check(kind == rsltKind);
                    }
                }

                // Report problem for first result.
                ctxt.addProblem(Message.RESOLVE_VIA_NON_SCRIPT, position, name.substring(curIdx),
                        getAbsDescr(first(rslts)));
                throw new SemanticException();
            }

            // Further resolve via the resolved scope.
            CheckerContext viaCtxt = (CheckerContext)first(rslts);
            rslts = viaCtxt.objects.get(id);
            if (rslts == null) {
                ctxt.addProblem(Message.RESOLVE_NOT_FOUND, position, id, getAbsDescr(viaCtxt.scope), "");
                throw new SemanticException();
            }

            // Mark resolved objects as 'referred'. This can be final results
            // or a 'via' scope from which we continue resolving.
            for (PositionObject rslt: rslts) {
                referred.add(rslt);
            }
        }

        // Peel of any scope wrappers.
        for (int i = 0; i < rslts.size(); i++) {
            PositionObject rslt = rslts.get(i);
            if (rslt instanceof CheckerContext) {
                rslts.set(i, ((CheckerContext)rslt).scope);
            }
        }

        // Return resolved results.
        Assert.check(!rslts.isEmpty());
        return rslts;
    }

    /**
     * Returns the kind of a ToolDef symbol.
     *
     * @param obj The ToolDef symbol. Must be a named object.
     * @return The kind of the symbol.
     */
    public static SymbolKind getSymbolKind(PositionObject obj) {
        // Containers.
        if (obj instanceof Script) {
            return SymbolKind.SCOPE;
        } else if (obj instanceof ForStatement) {
            return SymbolKind.SCOPE;
        } else if (obj instanceof IfStatement) {
            return SymbolKind.SCOPE;
        } else if (obj instanceof WhileStatement) {
            return SymbolKind.SCOPE;
        }

        // Tools.
        if (obj instanceof Tool) {
            return SymbolKind.TOOL;
        }

        // Types.
        if (obj instanceof TypeDecl) {
            return SymbolKind.TYPE;
        } else if (obj instanceof TypeParam) {
            return SymbolKind.TYPE;
        }

        // Values.
        if (obj instanceof ToolParameter) {
            return SymbolKind.VALUE;
        } else if (obj instanceof Variable) {
            return SymbolKind.VALUE;
        }

        // Unknown.
        throw new RuntimeException("Unexpected obj: " + obj);
    }

    /**
     * Check for unused declarations (objects that are never referred). We don't report declarations that may be
     * imported, as that would lead to potential false positives.
     */
    public void checkUnused() {
        for (Entry<String, List<PositionObject>> entry: objects.entrySet()) {
            for (PositionObject obj: entry.getValue()) {
                // Skip if object can be imported, to prevent false positives.
                if (obj instanceof Tool) {
                    continue;
                } else if (obj instanceof TypeDecl) {
                    continue;
                }

                // Skip if referred (not unused).
                if (referred.contains(obj)) {
                    continue;
                }

                // Not referred (unused). Report it.
                if (obj instanceof CheckerContext) {
                    obj = ((CheckerContext)obj).getScope();
                }
                addProblem(Message.UNUSED_DECL, obj.getPosition(), StringUtils.capitalize(getAbsDescr(obj)));
                // Non-fatal problem.
            }
        }
    }
}
