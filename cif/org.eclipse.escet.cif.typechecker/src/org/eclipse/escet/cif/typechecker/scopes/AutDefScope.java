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

package org.eclipse.escet.cif.typechecker.scopes;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.parser.ast.ACompDefDecl;
import org.eclipse.escet.cif.parser.ast.automata.AAutomatonBody;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.declwrap.DeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalAlgDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalEventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalLocationDeclWrap;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Automaton definition scope. */
public class AutDefScope extends ParentScope<ComponentDef> {
    /** The CIF AST automaton definition object representing this scope. */
    private final ACompDefDecl autDefDecl;

    /** The list of AST locations for this automaton. */
    private final List<ALocation> astLocs;

    /**
     * Constructor for the {@link AutDefScope} class.
     *
     * @param obj The CIF metamodel component definition object representing this scope.
     * @param autDefDecl The CIF AST automaton definition object representing this scope.
     * @param parent The parent scope.
     * @param tchecker The CIF type checker to use.
     */
    public AutDefScope(ComponentDef obj, ACompDefDecl autDefDecl, ParentScope<?> parent, CifTypeChecker tchecker) {
        super(obj, parent, tchecker);
        this.autDefDecl = autDefDecl;
        this.astLocs = ((AAutomatonBody)autDefDecl.body).locations;

        Assert.check(obj.getBody() instanceof Automaton);
    }

    @Override
    protected String getScopeTypeName() {
        return "autdef";
    }

    @Override
    protected ComplexComponent getComplexComponent() {
        return obj.getBody();
    }

    @Override
    protected Group getGroup() {
        // Automata are not groups.
        throw new UnsupportedOperationException();
    }

    @Override
    protected ComponentDef getComponentDef() {
        return obj;
    }

    @Override
    protected Automaton getAutomaton() {
        return (Automaton)obj.getBody();
    }

    @Override
    public List<ALocation> getAstLocs() {
        return astLocs;
    }

    @Override
    public void addChildScope(SymbolScope<?> scope) {
        Assert.check(scope instanceof CompParamScope);
        super.addChildScope(scope);
    }

    @Override
    protected boolean isSubScope() {
        return false;
    }

    @Override
    protected boolean isRootScope() {
        return false;
    }

    @Override
    public String getName() {
        return obj.getBody().getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(obj.getBody());
    }

    @Override
    public String getAbsText() {
        return fmt("automaton definition \"%s\"", getAbsName());
    }

    /**
     * Fully type checks the parameters of this automaton definition scope, after ensuring that the scope itself is
     * checked 'for use'. Does not check the (other) parts of the body. This method may be invoked multiple times. This
     * scope checks the parameters each time this method is called. However, the parameters themselves only check
     * themselves at most once.
     *
     * @see #tcheckFullParams(ParentScope)
     */
    public void tcheckFullParams() {
        tcheckFullParams(this);
    }

    /**
     * Fully type checks the parameters of this component definition scope, after ensuring that the scope itself is
     * checked 'for use'. Does not check the (other) parts of the body. This method may be invoked multiple times. This
     * scope checks the parameters each time this method is called. However, the parameters themselves only check
     * themselves at most once.
     *
     * @param compDefScope The component definition scope for which to check the parameters.
     * @see AutDefScope#tcheckFullParams()
     * @see GroupDefScope#tcheckFullParams()
     */
    public static void tcheckFullParams(ParentScope<?> compDefScope) {
        // Ensure the scope is checked 'for use'.
        compDefScope.tcheckForUse();

        // Check all algebraic, event, and location parameters.
        boolean failed = false;
        for (DeclWrap<?> decl: compDefScope.declarations.values()) {
            if (decl instanceof FormalAlgDeclWrap || decl instanceof FormalEventDeclWrap
                    || decl instanceof FormalLocationDeclWrap)
            {
                try {
                    decl.tcheckFull();
                } catch (SemanticException ex) {
                    failed = true;
                }
            }
        }

        // Check all component parameters.
        for (SymbolScope<?> child: compDefScope.children.values()) {
            if (child instanceof CompParamScope) {
                try {
                    child.tcheckFull();
                } catch (SemanticException ex) {
                    failed = true;
                }
            }
        }

        // Forward failure.
        if (failed) {
            throw new SemanticException();
        }
    }

    @Override
    protected void tcheckScopeFull() {
        AutScope.typeCheckAutomaton((AAutomatonBody)autDefDecl.body, getAutomaton(), this, tchecker);
    }
}
