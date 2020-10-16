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

package org.eclipse.escet.cif.typechecker.scopes;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;

/** Specification scope. */
public class SpecScope extends ParentScope<Specification> {
    /**
     * Constructor for the {@link SpecScope} class.
     *
     * @param obj The CIF metamodel specification object representing this scope.
     * @param tchecker The CIF type checker to use.
     */
    public SpecScope(Specification obj, CifTypeChecker tchecker) {
        super(obj, null, tchecker);
    }

    @Override
    protected String getScopeTypeName() {
        return "spec";
    }

    @Override
    protected ComplexComponent getComplexComponent() {
        return obj;
    }

    @Override
    protected Group getGroup() {
        return obj;
    }

    @Override
    protected ComponentDef getComponentDef() {
        // Specifications are not component definitions.
        throw new UnsupportedOperationException();
    }

    @Override
    protected Automaton getAutomaton() {
        // Specifications are not automata.
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
        return true;
    }

    @Override
    public String getName() {
        // Specification has no name. We shouldn't need the name, as it can't
        // be referenced.
        throw new UnsupportedOperationException("spec has no name");
    }

    @Override
    public String getAbsName() {
        // Specification has no name. We shouldn't need the name, as it can't
        // be referenced.
        throw new UnsupportedOperationException("spec has no abs name");
    }

    @Override
    public String getAbsText() {
        return "the top level scope of the specification";
    }
}
