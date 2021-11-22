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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.list2set;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF scoping utility methods. */
public class CifScopeUtils {
    /** Constructor for the {@link CifScopeUtils} class. */
    private CifScopeUtils() {
        // Static class.
    }

    /**
     * Is the given component an automaton? Correctly handles automaton instantiations.
     *
     * @param component The component to check.
     * @return {@code true} if the component is an automaton, {@code false} otherwise.
     * @see #getAutomaton
     */
    public static boolean isAutomaton(Component component) {
        // Follow component instantiations.
        while (component instanceof ComponentInst) {
            ComponentInst cinst = (ComponentInst)component;
            ComponentDef cdef = CifTypeUtils.getCompDefFromCompInst(cinst);
            component = cdef.getBody();
        }

        // Automaton.
        if (component instanceof Automaton) {
            return true;
        }

        // Group.
        Assert.check(component instanceof Group);
        return false;
    }

    /**
     * Get the automaton represented by the given component. Correctly handles automaton instantiations.
     *
     * @param component The component that represents an automaton.
     * @return The automaton represented by the component.
     * @see #isAutomaton
     */
    public static Automaton getAutomaton(Component component) {
        // Follow component instantiations.
        while (component instanceof ComponentInst) {
            ComponentInst cinst = (ComponentInst)component;
            ComponentDef cdef = CifTypeUtils.getCompDefFromCompInst(cinst);
            component = cdef.getBody();
        }

        // Automaton.
        if (component instanceof Automaton) {
            return (Automaton)component;
        }

        // Group.
        Assert.check(component instanceof Group);
        throw new RuntimeException("component is not an aut: " + component);
    }

    /**
     * Does the given group have any component definitions and/or component instantiations (recursively)?
     *
     * @param group The group.
     * @return {@code true} if the group has at least one component definition or component instantiation, {@code false}
     *     otherwise.
     */
    public static boolean hasCompDefInst(Group group) {
        // Check for component definitions.
        if (!group.getDefinitions().isEmpty()) {
            return true;
        }

        // Check all child components.
        for (Component child: group.getComponents()) {
            // Check for component instantiations.
            if (child instanceof ComponentInst) {
                return true;
            }

            // Automata don't have component definitions/instantiations.
            if (child instanceof Automaton) {
                continue;
            }

            // Recursively check child groups.
            Assert.check(child instanceof Group);
            if (hasCompDefInst((Group)child)) {
                return true;
            }
        }

        // No component definitions/instantiations found.
        return false;
    }

    /**
     * Returns the scope path of the given CIF object, consisting of the ancestor scopes. The elements of the returned
     * list are ordered from the scope closest to the given object, to the scope farthest away from the given object. If
     * the given object is itself a scope, it is not part of the returned list. The parent scope is the first element of
     * the returned list, and the root of the specification is always the last element.
     *
     * @param obj The CIF object. Must not be a {@link Specification}.
     * @return The scope path of the CIF object.
     * @see #getScope
     * @see #isScope
     */
    public static List<PositionObject> getScopePath(PositionObject obj) {
        // Parameter validation.
        Assert.check(!(obj instanceof Specification));

        // Keep getting scopes until we added the specification (the top level
        // scope).
        List<PositionObject> rslt = list();
        PositionObject cur = obj;
        do {
            cur = getScope(cur);
            rslt.add(cur);
        } while (!(cur instanceof Specification));

        // Return the scope path.
        Assert.check(!rslt.isEmpty());
        return rslt;
    }

    /**
     * Returns the common scope of the given scopes. If they share multiple (ancestor) scopes, the one farthest from the
     * specification root is returned.
     *
     * @param scope1 The first scope.
     * @param scope2 The second scope.
     * @return The common scope.
     * @throws IllegalArgumentException If no common scope is found.
     */
    public static PositionObject getCommonScope(PositionObject scope1, PositionObject scope2) {
        // Special case for same scope.
        if (scope1 == scope2) {
            return scope1;
        }

        // Get scope path for first scope.
        Set<PositionObject> path1;
        if (scope1 instanceof Specification) {
            path1 = set();
        } else {
            path1 = list2set(getScopePath(scope1));
        }
        path1.add(scope1);

        // Try to find a match for the scope paths of both scopes.
        if (path1.contains(scope2)) {
            return scope2;
        }

        List<PositionObject> path2 = getScopePath(scope2);
        for (PositionObject scope: path2) {
            if (path1.contains(scope)) {
                return scope;
            }
        }

        // No common scope found.
        throw new IllegalArgumentException("No common scope found.");
    }

    /**
     * Is the first given scope an ancestor of the second given scope? If they are the same scope, {@code false} is
     * returned.
     *
     * @param scope1 The first given scope.
     * @param scope2 The second given scope.
     * @return {@code true} if the first given scope an ancestor of the second given scope, {@code false} otherwise.
     */
    public static boolean isAncestorScope(PositionObject scope1, PositionObject scope2) {
        PositionObject cur = (PositionObject)scope2.eContainer();
        while (cur != null) {
            if (cur == scope1) {
                return true;
            }
            cur = (PositionObject)cur.eContainer();
        }
        return false;
    }

    /**
     * Returns the CIF scope object for the given CIF object. If the given object is itself a scope, its parent scope is
     * returned.
     *
     * @param obj The CIF object. Must not be a {@link Specification}.
     * @return The CIF scope object.
     * @throws IllegalArgumentException If the given object has no scope.
     * @see #isScope
     */
    public static PositionObject getScope(PositionObject obj) {
        Assert.check(!(obj instanceof Specification));

        PositionObject cur = (PositionObject)obj.eContainer();
        while (cur != null) {
            if (isScope(cur)) {
                return cur;
            }
            cur = (PositionObject)cur.eContainer();
        }

        throw new IllegalArgumentException("No scope found for: " + obj);
    }

    /**
     * Is the given CIF object a scope?
     *
     * <p>
     * Components, functions, and component definitions are scopes. Components that represent bodies of component
     * definitions are not scopes, as the component definitions themselves are the scopes. Component instantiations are
     * not scopes, as they don't contain anything (yet). They can be considered 'via' scopes, but not 'real' scopes.
     * </p>
     *
     * @param obj The CIF object for which to determine whether it is a scope.
     * @return {@code true} if the CIF object is a scope, {@code false} otherwise.
     */
    public static boolean isScope(PositionObject obj) {
        // Component definitions are scopes.
        if (obj instanceof ComponentDef) {
            return true;
        }

        // User-defined functions are scopes.
        if (obj instanceof Function) {
            return true;
        }

        // Component instantiations are explicitly not scopes.
        if (obj instanceof ComponentInst) {
            return false;
        }

        // Component are scopes, unless they represent the body of a component
        // definition.
        if (obj instanceof Component) {
            PositionObject parent = (PositionObject)obj.eContainer();
            return !(parent instanceof ComponentDef);
        }

        // Remaining CIF objects are not scopes.
        return false;
    }

    /**
     * Is the given CIF object a root scope?
     *
     * <p>
     * Specifications and component definitions are root scopes.
     * </p>
     *
     * @param obj The CIF object for which to determine whether it is a root scope.
     * @return {@code true} if the CIF object is a root scope, {@code false} otherwise.
     */
    public static boolean isRootScope(PositionObject obj) {
        if (obj instanceof Specification) {
            return true;
        }
        if (obj instanceof ComponentDef) {
            return true;
        }
        return false;
    }

    /**
     * Returns the root of the given (sub-)scope. If the given scope is itself a root scope, the given scope is
     * returned.
     *
     * @param scope The (sub-)scope.
     * @return The root of the given (sub-)scope.
     */
    public static PositionObject getScopeRoot(PositionObject scope) {
        // Paranoia checking.
        Assert.check(isScope(scope));

        // Keep going up one scope at a time until we found a root scope.
        PositionObject curScope = scope;
        while (curScope != null) {
            // Check scope itself being a root scope.
            if (isRootScope(curScope)) {
                return curScope;
            }

            // Check parent scope.
            curScope = getScope(curScope);
        }

        // Rootless scope.
        throw new RuntimeException("Rootless scope: " + scope);
    }

    /**
     * Returns the specification root of the given (sub-)scope.
     *
     * @param scope The (sub-)scope.
     * @return The specification root of the given (sub-)scope.
     */
    public static Specification getSpecRoot(PositionObject scope) {
        // Paranoia checking.
        Assert.check(isScope(scope));

        // Keep going up one scope at a time until we find the specification.
        PositionObject curScope = scope;
        while (curScope != null) {
            // Check scope itself being a specification.
            if (curScope instanceof Specification) {
                return (Specification)curScope;
            }

            // Check parent scope.
            curScope = getScope(curScope);
        }

        // Rootless scope.
        throw new RuntimeException("Rootless scope: " + scope);
    }

    /**
     * Returns the object with the given name, from the given scope.
     *
     * @param scope The scope. Must not be a 'via' scope (a component instantiation scope or component parameter scope).
     * @param name The name of the object to get. Must exist in the given scope.
     * @return The object.
     * @see #getSymbolNamesForScope
     */
    public static PositionObject getObject(PositionObject scope, String name) {
        // Complex components (specification, groups, automata).
        if (scope instanceof ComplexComponent) {
            ComplexComponent comp = (ComplexComponent)scope;

            // Declarations (incl. enum literals).
            for (Declaration decl: comp.getDeclarations()) {
                if (decl.getName().equals(name)) {
                    return decl;
                }

                if (decl instanceof EnumDecl) {
                    for (EnumLiteral lit: ((EnumDecl)decl).getLiterals()) {
                        if (lit.getName().equals(name)) {
                            return lit;
                        }
                    }
                }
            }
        }

        // Groups (incl. specifications).
        if (scope instanceof Group) {
            Group group = (Group)scope;

            // Components.
            for (Component child: group.getComponents()) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }

            // Component definitions.
            for (ComponentDef cdef: group.getDefinitions()) {
                if (cdef.getBody().getName().equals(name)) {
                    return cdef;
                }
            }
        }

        // Automata.
        if (scope instanceof Automaton) {
            Automaton aut = (Automaton)scope;

            // Locations.
            for (Location loc: aut.getLocations()) {
                String locName = loc.getName();
                if (locName != null) {
                    if (locName.equals(name)) {
                        return loc;
                    }
                }
            }
        }

        // Component definitions.
        if (scope instanceof ComponentDef) {
            ComponentDef compdef = (ComponentDef)scope;

            // Parameters.
            for (Parameter param: compdef.getParameters()) {
                if (CifTextUtils.getName(param).equals(name)) {
                    return param;
                }
            }

            // Body.
            return getObject(compdef.getBody(), name);
        }

        // Functions.
        if (scope instanceof Function) {
            Function func = (Function)scope;

            // Parameters.
            for (FunctionParameter param: func.getParameters()) {
                if (param.getParameter().getName().equals(name)) {
                    return param;
                }
            }
        }

        if (scope instanceof InternalFunction) {
            InternalFunction func = (InternalFunction)scope;

            // Local variables.
            for (DiscVariable var: func.getVariables()) {
                if (var.getName().equals(name)) {
                    return var;
                }
            }
        }

        // Error.
        if (scope instanceof ComponentInst || scope instanceof Parameter) {
            String msg = "Inst/param scopes not allowed: " + scope;
            throw new IllegalArgumentException(msg);
        }

        // Name not found, or unknown scope. Error message not perfect for
        // specification scope, as it has an empty absolute name, but good
        // enough for an internal error message.
        String msg = fmt("Name \"%s\" not found in \"%s\".", name, CifTextUtils.getAbsName(scope));
        throw new IllegalArgumentException(msg);
    }

    /**
     * Returns the names of the symbols declared in the given scope.
     *
     * <p>
     * May not be applied to tuple types, as they don't introduce an actual scope.
     * </p>
     *
     * @param scope The scope. Must not be a 'via' scope (a component instantiation scope or component parameter scope).
     * @param scopeCache Cache with computation results for {@link #getSymbolNamesForScope}. May be {@code null} to not
     *     use a cache.
     * @return The names of the symbols.
     * @see #getObject
     */
    public static Set<String> getSymbolNamesForScope(PositionObject scope, ScopeCache scopeCache) {
        if (scopeCache != null) {
            Set<String> rslt = scopeCache.get(scope);
            if (rslt != null) {
                return rslt;
            }
            rslt = getUncachedSymbolNamesForScope(scope, scopeCache);
            scopeCache.put(scope, rslt);
            return rslt;
        }
        return getUncachedSymbolNamesForScope(scope, scopeCache);
    }

    /**
     * Compute and return the names of the symbols declared in the given scope in case there is no cached entry
     * available.
     *
     * @param scope The scope. Must not be a 'via' scope (a component instantiation scope or component parameter scope).
     * @param scopeCache Cache with computation results for {@link #getSymbolNamesForScope}. May be {@code null} to not
     *     use a cache.
     * @return The names of the symbols.
     * @see #getObject
     */
    private static Set<String> getUncachedSymbolNamesForScope(PositionObject scope, ScopeCache scopeCache) {
        Set<String> rslt = set();

        // Groups (incl. specifications).
        if (scope instanceof Group) {
            // Add components, declarations (incl. enum literals) and
            // component definitions.
            Group group = (Group)scope;
            for (Component comp: group.getComponents()) {
                rslt.add(comp.getName());
            }
            for (Declaration decl: group.getDeclarations()) {
                rslt.add(decl.getName());
                if (decl instanceof EnumDecl) {
                    for (EnumLiteral lit: ((EnumDecl)decl).getLiterals()) {
                        rslt.add(lit.getName());
                    }
                }
            }
            for (ComponentDef cdef: group.getDefinitions()) {
                rslt.add(cdef.getBody().getName());
            }
            return rslt;
        }

        // Automata.
        if (scope instanceof Automaton) {
            // Add declarations (incl. enum literals) and locations.
            Automaton aut = (Automaton)scope;
            for (Declaration decl: aut.getDeclarations()) {
                rslt.add(decl.getName());
                if (decl instanceof EnumDecl) {
                    for (EnumLiteral lit: ((EnumDecl)decl).getLiterals()) {
                        rslt.add(lit.getName());
                    }
                }
            }
            for (Location loc: aut.getLocations()) {
                String locName = loc.getName();
                if (locName != null) {
                    rslt.add(locName);
                }
            }
            return rslt;
        }

        // Component definitions.
        if (scope instanceof ComponentDef) {
            // Add parameters and symbol names for the body.
            ComponentDef compdef = (ComponentDef)scope;
            for (Parameter param: compdef.getParameters()) {
                rslt.add(CifTextUtils.getName(param));
            }
            Set<String> bodyNames;
            bodyNames = getSymbolNamesForScope(compdef.getBody(), scopeCache);
            rslt.addAll(bodyNames);
            return rslt;
        }

        // Functions.
        if (scope instanceof InternalFunction) {
            // Add parameters and local variables.
            InternalFunction func = (InternalFunction)scope;
            for (FunctionParameter param: func.getParameters()) {
                rslt.add(param.getParameter().getName());
            }
            for (DiscVariable var: func.getVariables()) {
                rslt.add(var.getName());
            }
            return rslt;
        }

        if (scope instanceof ExternalFunction) {
            // Add parameters.
            ExternalFunction func = (ExternalFunction)scope;
            for (FunctionParameter param: func.getParameters()) {
                rslt.add(param.getParameter().getName());
            }
            return rslt;
        }

        // Error.
        if (scope instanceof ComponentInst || scope instanceof Parameter) {
            String msg = "Inst/param scopes not allowed: " + scope;
            throw new IllegalArgumentException(msg);
        }

        throw new RuntimeException("Unknown scope: " + scope);
    }

    /**
     * Checks whether a given name, declared in the given 'top' scope, is also declared in one of the intermediate
     * scopes between the 'top' and 'current' scopes, or in the 'current' scope itself. If so, then the declaration in
     * the 'top' scope is hidden.
     *
     * @param name The name of the symbol to check for.
     * @param curScope The current scope.
     * @param topScope The top scope. Must be an ancestor of the current scope, and must explicitly not be equal to the
     *     current scope. This is the scope in which a symbol with the given name is declared, and for which we want to
     *     find out whether it is hidden.
     * @param scopeCache Cache with computation results for {@link #getSymbolNamesForScope}. May be {@code null} to not
     *     use a cache.
     * @return {@code true} if the name is hidden by one of the intermediate scopes or by the current scope,
     *     {@code false} otherwise.
     */
    public static boolean isNameHidden(String name, PositionObject curScope, PositionObject topScope,
            ScopeCache scopeCache)
    {
        List<PositionObject> scopePath = getScopePath(curScope);
        Assert.check(scopePath.contains(topScope));
        Assert.check(topScope != curScope);

        for (PositionObject scope: concat(curScope, scopePath)) {
            if (scope == topScope) {
                break;
            }

            // We found the current scope, or an intermediate scope.
            Set<String> symbolNames;
            symbolNames = getSymbolNamesForScope(scope, scopeCache);
            if (symbolNames.contains(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a reference in CIF ASCII syntax, for the referenced object, relative to the scope of the given current
     * (reference) object. This method does not handle references via component instantiations and component parameters.
     *
     * @param curObj The current (reference) object. Must be a reference expression, or a reference type.
     * @param refObj The referenced object.
     * @param scopeCache Cache with computation results for {@link #getSymbolNamesForScope}. May be {@code null} to not
     *     use a cache.
     * @return A reference in CIF ASCII syntax.
     */
    public static String getRefTxtFromObj(PositionObject curObj, PositionObject refObj, ScopeCache scopeCache) {
        // Paranoia checks.
        Assert.check(curObj instanceof Expression || curObj instanceof CifType);

        // Get scope for current (reference) object.
        PositionObject curScope = getScope(curObj);

        // Defer to other method.
        return getRefTxtFromScope(curScope, refObj, scopeCache);
    }

    /**
     * Returns a reference in CIF ASCII syntax, for the referenced object, relative to the given scope. This method does
     * not handle references via component instantiations and component parameters.
     *
     * @param scope The scope in which to resolve the reference.
     * @param refObj The referenced object.
     * @param scopeCache Cache with computation results for {@link #getSymbolNamesForScope}. May be {@code null} to not
     *     use a cache.
     * @return A reference in CIF ASCII syntax.
     */
    public static String getRefTxtFromScope(PositionObject scope, PositionObject refObj, ScopeCache scopeCache) {
        // Get basic information.
        PositionObject curScope = scope;
        PositionObject refScope = getScope(refObj);
        String refName = CifTextUtils.getName(refObj);

        // Same scope.
        if (curScope == refScope) {
            // It is directly visible in the current scope.
            return CifTextUtils.escapeIdentifier(refName);
        }

        // Ancestor scope.
        if (isAncestorScope(refScope, curScope)) {
            if (!isNameHidden(refName, curScope, refScope, scopeCache)) {
                // It is directly visible in the current scope.
                return CifTextUtils.escapeIdentifier(refName);
            }

            // Hidden. Use scope/root absolute reference.
            PositionObject scopeRoot = getScopeRoot(curScope);
            if (refScope == scopeRoot || isAncestorScope(scopeRoot, refScope)) {
                // Scope absolute reference.
                String refTxt = "." + getRelativeRefTxt(scopeRoot, refScope);
                refTxt = joinRefTxts(refTxt, CifTextUtils.escapeIdentifier(refName));
                return refTxt;
            } else {
                // Specification absolute reference.
                return "^" + CifTextUtils.getAbsName(refObj);
            }
        }

        // Get the common scope.
        PositionObject commonScope = getCommonScope(curScope, refScope);

        // Get child scope on scope path of the referenced object, via which
        // we should refer to the referenced object.
        List<PositionObject> scopePath = getScopePath(refObj);
        int commonIdx = scopePath.indexOf(commonScope);
        Assert.check(commonIdx != -1);
        int childIdx = commonIdx - 1;
        Assert.check(childIdx >= 0);
        PositionObject childScope = scopePath.get(childIdx);

        // Get reference to child scope.
        String childScopeName = CifTextUtils.getName(childScope);
        boolean hidden = (curScope == commonScope) ? false
                : isNameHidden(childScopeName, curScope, commonScope, scopeCache);
        String childRefTxt;
        if (!hidden) {
            // Child scope is directly visible in the current scope.
            childRefTxt = CifTextUtils.escapeIdentifier(childScopeName);
        } else {
            // Recursively resolve the child scope.
            childRefTxt = getRefTxtFromScope(curScope, childScope, scopeCache);
        }

        // Add relative reference to the referenced object.
        String relTxt = getRelativeRefTxt(childScope, refObj);
        return joinRefTxts(childRefTxt, relTxt);
    }

    /**
     * Returns a reference in CIF ASCII syntax, for the given 'via' reference.
     *
     * @param viaRef The 'via' reference expression or 'via' reference type.
     * @param scopeCache Cache with computation results for {@link #getSymbolNamesForScope}. May be {@code null} to not
     *     use a cache.
     * @return A reference in CIF ASCII syntax.
     */
    public static String getViaRefTxt(PositionObject viaRef, ScopeCache scopeCache) {
        // Get scope of the 'via' reference.
        PositionObject scope = getScope(viaRef);

        // Defer to other method.
        return getViaRefTxt(viaRef, scope, scopeCache);
    }

    /**
     * Returns a reference in CIF ASCII syntax, for the given 'via' reference, relative to the given scope.
     *
     * @param viaRef The 'via' reference (expression or type).
     * @param scope The scope in which to resolve the reference.
     * @param scopeCache Cache with computation results for {@link #getSymbolNamesForScope}. May be {@code null} to not
     *     use a cache.
     * @return A reference in CIF ASCII syntax.
     */
    public static String getViaRefTxt(PositionObject viaRef, PositionObject scope, ScopeCache scopeCache) {
        // Get 'via' scope (ComponentInst or ComponentParameter),
        // 'continuation' scope, and child reference (either a 'normal'
        // reference or another 'via' reference).
        PositionObject viaScope;
        ComponentDef contScope;
        PositionObject childRef;

        if (viaRef instanceof CompInstWrapExpression) {
            CompInstWrapExpression instRef = (CompInstWrapExpression)viaRef;
            ComponentInst inst = instRef.getInstantiation();
            viaScope = inst;

            contScope = CifTypeUtils.getCompDefFromCompInst(inst);

            childRef = instRef.getReference();
        } else if (viaRef instanceof CompParamWrapExpression) {
            CompParamWrapExpression paramRef = (CompParamWrapExpression)viaRef;
            ComponentParameter param = paramRef.getParameter();
            viaScope = param;

            CifType cdefType = CifTypeUtils.normalizeType(param.getType());
            Assert.check(cdefType instanceof ComponentDefType);
            contScope = ((ComponentDefType)cdefType).getDefinition();

            childRef = paramRef.getReference();
        } else if (viaRef instanceof CompInstWrapType) {
            CompInstWrapType instRef = (CompInstWrapType)viaRef;
            ComponentInst inst = instRef.getInstantiation();
            viaScope = inst;

            contScope = CifTypeUtils.getCompDefFromCompInst(inst);

            childRef = instRef.getReference();
        } else if (viaRef instanceof CompParamWrapType) {
            CompParamWrapType paramRef = (CompParamWrapType)viaRef;
            ComponentParameter param = paramRef.getParameter();
            viaScope = param;

            CifType cdefType = CifTypeUtils.normalizeType(param.getType());
            Assert.check(cdefType instanceof ComponentDefType);
            contScope = ((ComponentDefType)cdefType).getDefinition();

            childRef = paramRef.getReference();
        } else {
            throw new RuntimeException("Unknown 'via' reference: " + viaRef);
        }

        // Get reference text for the 'via' scope.
        String viaTxt = getRefTxtFromScope(scope, viaScope, scopeCache);

        // Is the child reference another 'via' reference?
        if (childRef instanceof CompInstWrapExpression || childRef instanceof CompParamWrapExpression
                || childRef instanceof CompInstWrapType || childRef instanceof CompParamWrapType)
        {
            String childTxt = getViaRefTxt(childRef, contScope, scopeCache);
            Assert.check(!childTxt.startsWith("^"));
            Assert.check(!childTxt.startsWith("."));
            return joinRefTxts(viaTxt, childTxt);
        }

        // Get final referenced object.
        PositionObject finalRefObj;
        if (childRef instanceof TypeRef) {
            finalRefObj = ((TypeRef)childRef).getType();
        } else if (childRef instanceof EnumType) {
            finalRefObj = ((EnumType)childRef).getEnum();
        } else if (childRef instanceof ComponentType) {
            finalRefObj = ((ComponentType)childRef).getComponent();
        } else if (childRef instanceof ComponentDefType) {
            finalRefObj = ((ComponentDefType)childRef).getDefinition();
        } else {
            finalRefObj = getRefObjFromRef((Expression)childRef);
        }

        // Child reference is a 'normal' reference.
        String childTxt = getRelativeRefTxt(contScope, finalRefObj);
        Assert.check(!childTxt.isEmpty());
        return joinRefTxts(viaTxt, childTxt);
    }

    /**
     * Returns a relative reference in CIF ASCII syntax, to the given target object, relative to the given source scope.
     * If the target object is equal to the source scope, {@code ""} is returned.
     *
     * @param sourceScope The source scope. Must be the target object, or one of its ancestor scopes.
     * @param targetObj The target object. Must be a named object.
     * @return A relative reference in CIF ASCII syntax.
     */
    public static String getRelativeRefTxt(PositionObject sourceScope, PositionObject targetObj) {
        // Special case for equal scopes.
        if (sourceScope == targetObj) {
            return "";
        }

        // Add scopes in between.
        List<PositionObject> scopePath = getScopePath(targetObj);
        Assert.check(scopePath.contains(sourceScope));
        StringBuilder rslt = new StringBuilder();
        for (PositionObject scope: scopePath) {
            if (scope == sourceScope) {
                break;
            }
            if (rslt.length() > 0) {
                rslt.insert(0, ".");
            }
            rslt.insert(0, CifTextUtils.escapeIdentifier(CifTextUtils.getName(scope)));
        }

        // Add target scope.
        Assert.check(!(targetObj instanceof Specification));
        return joinRefTxts(rslt.toString(), CifTextUtils.escapeIdentifier(CifTextUtils.getName(targetObj)));
    }

    /**
     * Returns the object referenced by the reference expression. Does not support wrapping expressions. Use
     * {@link CifTypeUtils#unwrapExpression} to get rid of them, before invoking this method.
     *
     * <p>
     * Note that {@link TauExpression} is not supported, as it does not refer to an object that is declared in the
     * specification. Similarly, {@link TimeExpression} and {@link StdLibFunctionExpression} are not supported.
     * </p>
     *
     * <p>
     * Note that {@link FieldExpression} is not supported, as it has special scoping rules, and callers should handle it
     * as a special case.
     * </p>
     *
     * <p>
     * Note that {@link SelfExpression} is supported.
     * </p>
     *
     * @param refExpr The reference expression.
     * @return The object referenced by the reference expression.
     * @see CifTypeUtils#isRefExpr
     */
    public static PositionObject getRefObjFromRef(Expression refExpr) {
        PositionObject refObj;
        if (refExpr instanceof ConstantExpression) {
            refObj = ((ConstantExpression)refExpr).getConstant();
        } else if (refExpr instanceof DiscVariableExpression) {
            refObj = ((DiscVariableExpression)refExpr).getVariable();
        } else if (refExpr instanceof AlgVariableExpression) {
            refObj = ((AlgVariableExpression)refExpr).getVariable();
        } else if (refExpr instanceof ContVariableExpression) {
            refObj = ((ContVariableExpression)refExpr).getVariable();
        } else if (refExpr instanceof LocationExpression) {
            refObj = ((LocationExpression)refExpr).getLocation();
        } else if (refExpr instanceof EnumLiteralExpression) {
            refObj = ((EnumLiteralExpression)refExpr).getLiteral();
        } else if (refExpr instanceof EventExpression) {
            refObj = ((EventExpression)refExpr).getEvent();
        } else if (refExpr instanceof FunctionExpression) {
            refObj = ((FunctionExpression)refExpr).getFunction();
        } else if (refExpr instanceof InputVariableExpression) {
            refObj = ((InputVariableExpression)refExpr).getVariable();
        } else if (refExpr instanceof ComponentExpression) {
            refObj = ((ComponentExpression)refExpr).getComponent();
        } else if (refExpr instanceof CompParamExpression) {
            refObj = ((CompParamExpression)refExpr).getParameter();
        } else if (refExpr instanceof SelfExpression) {
            CifType ctype = CifTypeUtils.normalizeType(refExpr.getType());
            refObj = ((ComponentType)ctype).getComponent();
        } else {
            // Wrappings and non-reference expressions not allowed.
            throw new RuntimeException("Invalid ref expr: " + refExpr);
        }

        return refObj;
    }

    /**
     * Returns the object referenced by the reference type. For wrapping types, the 'via object' is returned.
     *
     * @param refType The reference type.
     * @return The object referenced by the reference type.
     * @see CifTypeUtils#isRefType
     */
    public static PositionObject getRefObjFromRef(CifType refType) {
        PositionObject refObj;
        if (refType instanceof TypeRef) {
            refObj = ((TypeRef)refType).getType();
        } else if (refType instanceof EnumType) {
            refObj = ((EnumType)refType).getEnum();
        } else if (refType instanceof ComponentType) {
            refObj = ((ComponentType)refType).getComponent();
        } else if (refType instanceof ComponentDefType) {
            refObj = ((ComponentDefType)refType).getDefinition();
        } else if (refType instanceof CompParamWrapType) {
            refObj = ((CompParamWrapType)refType).getParameter();
        } else if (refType instanceof CompInstWrapType) {
            refObj = ((CompInstWrapType)refType).getInstantiation();
        } else {
            // Non-reference types not allowed.
            throw new RuntimeException("Invalid ref type: " + refType);
        }

        return refObj;
    }

    /**
     * Joins two or more references in CIF ASCII syntax together to a single reference.
     *
     * @param txts The references in CIF ASCII syntax. The first one may be an absolute or relative reference. The
     *     remaining ones must be relative references. Empty texts are silently ignored.
     * @return The single joined reference in CIF ASCII syntax.
     */
    public static String joinRefTxts(String... txts) {
        Assert.check(txts.length >= 2);

        StringBuilder rslt = new StringBuilder();

        boolean first = true;
        for (String txt: txts) {
            if (txt.isEmpty()) {
                continue;
            }

            if (first) {
                first = false;
                rslt.append(txt);
            } else {
                Assert.check(!txt.startsWith("."));
                Assert.check(!txt.startsWith("^"));
                char lastChar = rslt.charAt(rslt.length() - 1);
                if (lastChar != '.') {
                    rslt.append('.');
                }
                rslt.append(txt);
            }
        }

        return rslt.toString();
    }

    /**
     * Returns a unique name for a named object, given the old (non-unique) name, the names that are already in use, and
     * the names that should be avoided. Names are made unique by post-fixing them with numbers.
     *
     * @param oldName The old (non-unique) name.
     * @param usedNames The names that are already in use.
     * @param avoidNames The names that should be avoided.
     * @return The unique name for the named object.
     */
    public static String getUniqueName(String oldName, Set<String> usedNames, Set<String> avoidNames) {
        int nr = 2;
        while (true) {
            String newName = oldName + String.valueOf(nr);
            if (usedNames.contains(newName) || avoidNames.contains(newName)) {
                nr++;
                continue;
            }
            return newName;
        }
    }

    /**
     * Is the given reference expression a reference via/to a formal parameter of a component definition?
     *
     * @param refExpr The reference expression.
     * @return {@code true} if the reference expression refers via/to a formal parameter of a component definition,
     *     {@code false} otherwise.
     */
    public static boolean isParamRefExpr(Expression refExpr) {
        // Via component parameter reference expression.
        if (refExpr instanceof CompParamWrapExpression) {
            return true;
        }

        // Component parameter reference expression.
        if (refExpr instanceof CompParamExpression) {
            return true;
        }

        // Event parameter reference expression.
        if (refExpr instanceof EventExpression) {
            Event e = ((EventExpression)refExpr).getEvent();
            return e.eContainer() instanceof Parameter;
        }

        // Location parameter reference expression.
        if (refExpr instanceof LocationExpression) {
            Location l = ((LocationExpression)refExpr).getLocation();
            return l.eContainer() instanceof Parameter;
        }

        // Algebraic parameter reference expression.
        if (refExpr instanceof AlgVariableExpression) {
            AlgVariable a = ((AlgVariableExpression)refExpr).getVariable();
            return a.eContainer() instanceof Parameter;
        }

        // Not a reference via/to a formal parameter of a component definition.
        return false;
    }

    /**
     * Collects all reference expressions from the given expression. Note that the expressions are not followed to
     * collect additional references.
     *
     * <p>
     * Tau expressions, standard library function references, variable 'time' references, and received variable
     * references, are not collected. For wrapped references, the wrappers are collected as well.
     * </p>
     *
     * <p>
     * Field references are collected, even though they are not considered reference expressions by the
     * {@link CifTypeUtils#isRefExpr} method, as that method considers them a special case.
     * </p>
     *
     * @param expr The expression to check.
     * @param rslt The collected references. Is modified in-place.
     */
    public static void collectRefExprs(Expression expr, List<Expression> rslt) {
        if (expr instanceof BoolExpression) {
            // No references.
        } else if (expr instanceof IntExpression) {
            // No references.
        } else if (expr instanceof RealExpression) {
            // No references.
        } else if (expr instanceof StringExpression) {
            // No references.
        } else if (expr instanceof TimeExpression) {
            // No references.
        } else if (expr instanceof CastExpression) {
            // Recursive.
            collectRefExprs(((CastExpression)expr).getChild(), rslt);
        } else if (expr instanceof UnaryExpression) {
            // Recursive.
            collectRefExprs(((UnaryExpression)expr).getChild(), rslt);
        } else if (expr instanceof BinaryExpression) {
            // Recursive.
            BinaryExpression bexpr = (BinaryExpression)expr;
            collectRefExprs(bexpr.getLeft(), rslt);
            collectRefExprs(bexpr.getRight(), rslt);
        } else if (expr instanceof IfExpression) {
            // Recursive.
            IfExpression iexpr = (IfExpression)expr;
            for (Expression guard: iexpr.getGuards()) {
                collectRefExprs(guard, rslt);
            }
            collectRefExprs(iexpr.getThen(), rslt);
            for (ElifExpression elif: iexpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    collectRefExprs(guard, rslt);
                }
                collectRefExprs(elif.getThen(), rslt);
            }
            collectRefExprs(iexpr.getElse(), rslt);
        } else if (expr instanceof SwitchExpression) {
            // Recursive.
            SwitchExpression sexpr = (SwitchExpression)expr;
            collectRefExprs(sexpr.getValue(), rslt);
            for (SwitchCase cse: sexpr.getCases()) {
                if (cse.getKey() != null) {
                    collectRefExprs(cse.getKey(), rslt);
                }
                collectRefExprs(cse.getValue(), rslt);
            }
        } else if (expr instanceof ProjectionExpression) {
            // Recursive.
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            collectRefExprs(pexpr.getChild(), rslt);
            collectRefExprs(pexpr.getIndex(), rslt);
        } else if (expr instanceof SliceExpression) {
            // Recursive.
            SliceExpression sexpr = (SliceExpression)expr;
            collectRefExprs(sexpr.getChild(), rslt);
            if (sexpr.getBegin() != null) {
                collectRefExprs(sexpr.getBegin(), rslt);
            }
            if (sexpr.getEnd() != null) {
                collectRefExprs(sexpr.getEnd(), rslt);
            }
        } else if (expr instanceof FunctionCallExpression) {
            // Recursive.
            FunctionCallExpression fcexpr = (FunctionCallExpression)expr;
            collectRefExprs(fcexpr.getFunction(), rslt);
            for (Expression param: fcexpr.getParams()) {
                collectRefExprs(param, rslt);
            }
        } else if (expr instanceof ListExpression) {
            // Recursive.
            for (Expression elem: ((ListExpression)expr).getElements()) {
                collectRefExprs(elem, rslt);
            }
        } else if (expr instanceof SetExpression) {
            // Recursive.
            for (Expression elem: ((SetExpression)expr).getElements()) {
                collectRefExprs(elem, rslt);
            }
        } else if (expr instanceof TupleExpression) {
            // Recursive.
            for (Expression elem: ((TupleExpression)expr).getFields()) {
                collectRefExprs(elem, rslt);
            }
        } else if (expr instanceof DictExpression) {
            // Recursive.
            for (DictPair pair: ((DictExpression)expr).getPairs()) {
                collectRefExprs(pair.getKey(), rslt);
                collectRefExprs(pair.getValue(), rslt);
            }
        } else if (expr instanceof ConstantExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof DiscVariableExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof AlgVariableExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof ContVariableExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof TauExpression) {
            // No references.
        } else if (expr instanceof LocationExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof EnumLiteralExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof EventExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof FieldExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof StdLibFunctionExpression) {
            // No references.
        } else if (expr instanceof FunctionExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof InputVariableExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof ComponentExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof CompParamExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else if (expr instanceof CompInstWrapExpression) {
            // A wrapper with a reference.
            rslt.add(expr);

            // Recursive.
            CompInstWrapExpression wrap = (CompInstWrapExpression)expr;
            collectRefExprs(wrap.getReference(), rslt);
        } else if (expr instanceof CompParamWrapExpression) {
            // A wrapper with a reference.
            rslt.add(expr);

            // Recursive.
            CompParamWrapExpression wrap = (CompParamWrapExpression)expr;
            collectRefExprs(wrap.getReference(), rslt);
        } else if (expr instanceof ReceivedExpression) {
            // No references.
        } else if (expr instanceof SelfExpression) {
            // Directly a reference.
            rslt.add(expr);
        } else {
            throw new RuntimeException("Unknown expr: " + expr);
        }
    }

    /**
     * Recursively changes references in a type, to adapt them from being valid for a given original scope, to becoming
     * valid for a given new scope.
     *
     * @param type The type to change recursively. Is modified in-place. May also be replaced in its container, so the
     *     top level type must be contained.
     * @param wrapType The wrapping type to wrap around reference types, to ensure that the reference types are valid
     *     for the new scope. That is, the new scope can be reached from the original scope 'via' this wrapping type.
     * @param origScope The original scope.
     * @param newScope The new scope.
     * @see #isScope
     */
    public static void changeTypeScope(CifType type, CifType wrapType, PositionObject origScope,
            PositionObject newScope)
    {
        // Non-reference leaf types. Nothing to do.
        if (type instanceof BoolType) {
            return;
        } else if (type instanceof IntType) {
            return;
        } else if (type instanceof RealType) {
            return;
        } else if (type instanceof StringType) {
            return;
        }

        // Non-reference composite types. Apply recursively.
        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            CifType etype = ltype.getElementType();
            changeTypeScope(etype, wrapType, origScope, newScope);
            return;
        } else if (type instanceof SetType) {
            SetType stype = (SetType)type;
            CifType etype = stype.getElementType();
            changeTypeScope(etype, wrapType, origScope, newScope);
            return;
        } else if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            CifType rtype = ftype.getReturnType();
            changeTypeScope(rtype, wrapType, origScope, newScope);
            for (CifType ptype: ftype.getParamTypes()) {
                changeTypeScope(ptype, wrapType, origScope, newScope);
            }
            return;
        } else if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            CifType ktype = dtype.getKeyType();
            CifType vtype = dtype.getValueType();
            changeTypeScope(ktype, wrapType, origScope, newScope);
            changeTypeScope(vtype, wrapType, origScope, newScope);
            return;
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            for (Field field: ttype.getFields()) {
                CifType ftype = field.getType();
                changeTypeScope(ftype, wrapType, origScope, newScope);
            }
            return;
        } else if (type instanceof DistType) {
            DistType dtype = (DistType)type;
            CifType stype = dtype.getSampleType();
            changeTypeScope(stype, wrapType, origScope, newScope);
            return;
        }

        // Reference and wrapping types. Get the object referred to by the
        // reference type, or the 'via object' referred to by the wrapping
        // type.
        PositionObject obj = CifScopeUtils.getRefObjFromRef(type);
        PositionObject objScope = CifScopeUtils.getScope(obj);

        // Get root scopes of both the referred object, and the original scope
        // from which the reference is valid. If they match, then the referred
        // object is contained in the root scope of the original scope, and we
        // need to add a wrapping type to get to that scope. If the referred
        // object is itself that root scope, we shouldn't add wrapping, as we
        // can't refer to the root scope itself via a relative reference from
        // within that root scope.
        PositionObject objRoot = CifScopeUtils.getScopeRoot(objScope);
        PositionObject origRoot = CifScopeUtils.getScopeRoot(origScope);
        boolean addWrapping = (objRoot == origRoot) && (objRoot != obj);

        // Add wrapping, or verify that we really don't need it.
        if (addWrapping) {
            // Make copy of wrapper, to ensure we add a unique instance.
            CifType wrapCopy = EMFHelper.deepclone(wrapType);

            // Replace original type by wrapped type.
            EMFHelper.updateParentContainment(type, wrapCopy);

            // Add child to wrapper.
            if (wrapCopy instanceof CompInstWrapType) {
                CompInstWrapType instWrap = (CompInstWrapType)wrapCopy;
                instWrap.setReference(type);
            } else {
                Assert.check(wrapCopy instanceof CompParamWrapType);
                CompParamWrapType paramWrap = (CompParamWrapType)wrapCopy;
                paramWrap.setReference(type);
            }
        } else {
            // If the new root scope and referred object root scope are the
            // same, we can reference the referred object directly, and don't
            // need the wrapping.
            PositionObject newRoot = CifScopeUtils.getScopeRoot(newScope);
            if (newRoot == objRoot) {
                return;
            }

            // If the referred object root scope is an ancestor scope of the
            // new root scope, direct referencing is also possible.
            if (CifScopeUtils.isAncestorScope(objRoot, newRoot)) {
                return;
            }

            // Unexpected situation.
            throw new RuntimeException("Unexpected reference.");
        }
    }
}
