//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.common.java.Assert;

/** Group definition scope. */
public class GroupDefScope extends ParentScope<ComponentDef> {
    /**
     * Constructor for the {@link GroupDefScope} class.
     *
     * @param obj The CIF metamodel component definition object representing this scope.
     * @param parent The parent scope.
     * @param tchecker The CIF type checker to use.
     */
    public GroupDefScope(ComponentDef obj, ParentScope<?> parent, CifTypeChecker tchecker) {
        super(obj, parent, tchecker);
        Assert.check(obj.getBody() instanceof Group);
    }

    @Override
    protected String getScopeTypeName() {
        return "groupdef";
    }

    @Override
    protected ComplexComponent getComplexComponent() {
        return obj.getBody();
    }

    @Override
    protected Group getGroup() {
        return (Group)obj.getBody();
    }

    @Override
    protected ComponentDef getComponentDef() {
        return obj;
    }

    @Override
    protected Automaton getAutomaton() {
        // Groups are not automata.
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ALocation> getAstLocs() {
        return null;
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
        return fmt("group definition \"%s\"", getAbsName());
    }

    /**
     * Fully type checks the parameters of this group definition scope, after ensuring that the scope itself is checked
     * 'for use'. Does not check the (other) parts of the body. This method may be invoked multiple times. This scope
     * checks the parameters each time this method is called. However, the parameters themselves only check themselves
     * at most once.
     *
     * @see AutDefScope#tcheckFullParams(ParentScope)
     */
    public void tcheckFullParams() {
        AutDefScope.tcheckFullParams(this);
    }
}
