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

/** Group scope. */
public class GroupScope extends ParentScope<Group> {
    /**
     * Constructor for the {@link GroupScope} class.
     *
     * @param obj The CIF metamodel group object representing this scope.
     * @param parent The parent scope.
     * @param tchecker The CIF type checker to use.
     */
    public GroupScope(Group obj, ParentScope<?> parent, CifTypeChecker tchecker) {
        super(obj, parent, tchecker);
    }

    @Override
    protected String getScopeTypeName() {
        return "group";
    }

    @Override
    protected ComplexComponent getComplexComponent() {
        return obj;
    }

    @Override
    protected ComponentDef getComponentDef() {
        // Groups are not component definitions.
        throw new UnsupportedOperationException();
    }

    @Override
    protected Group getGroup() {
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
        return true;
    }

    @Override
    protected boolean isRootScope() {
        return false;
    }

    @Override
    public String getName() {
        return obj.getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(obj);
    }

    @Override
    public String getAbsText() {
        return fmt("group \"%s\"", getAbsName());
    }
}
