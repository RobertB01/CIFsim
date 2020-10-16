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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.tokens.AName;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.SymbolTableEntry;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Component parameter scope. */
public class CompParamScope extends SymbolScope<ComponentParameter> {
    /** The parameter type reference text. */
    private final AName typeRef;

    /**
     * The resolved component definition scope, which is the type of this component parameter, or {@code null} if not
     * yet resolved.
     */
    private ParentScope<?> typeScope;

    /** Whether this parameter is used (referenced from anywhere). */
    public boolean used = false;

    /**
     * Constructor for the {@link CompParamScope} class.
     *
     * @param obj The CIF metamodel component parameter object representing this scope.
     * @param typeRef The parameter type reference text.
     * @param parent The parent component definition scope.
     * @param tchecker The CIF type checker to use.
     */
    public CompParamScope(ComponentParameter obj, AName typeRef, ParentScope<?> parent, CifTypeChecker tchecker) {
        super(obj, parent, tchecker);
        Assert.check(parent instanceof AutDefScope || parent instanceof GroupDefScope);
        this.typeRef = typeRef;
    }

    /**
     * Returns the scope of the component definition that is the type of this component parameter.
     *
     * @return The scope of the component definition.
     */
    public ParentScope<?> getCompDefScope() {
        Assert.notNull(typeScope);
        return typeScope;
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
        return fmt("component parameter \"%s\"", getAbsName());
    }

    @Override
    public Box toBox() {
        return new TextBox(fmt("[ compparam scope \"%s\" for: %s ]", getName(), obj));
    }

    @Override
    public void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // Resolve the parameter type, in the parent scope of the component
        // definition, not in the parent scope of the component parameter.
        SymbolTableEntry entry = parent.getParent().resolve(typeRef.position, typeRef.name, tchecker);

        if (!(entry instanceof AutDefScope || entry instanceof GroupDefScope)) {
            tchecker.addProblem(ErrMsg.COMP_PARAM_INVALID_TYPE, typeRef.position, getAbsName());
            throw new SemanticException();
        }

        // We don't type check the resolved component definition 'for use', as
        // we just use it as a 'via' reference.
        typeScope = (ParentScope<?>)entry;

        // Set the type on the component parameter.
        CifType paramType = parent.getParent().resolveAsType(typeRef.name, typeRef.position, "", tchecker);
        obj.setType(paramType);

        // This scope is now 'via' checked, and thus checked 'for use'. Since
        // there is no additional checking needed, it is also fully checked.
        status = CheckStatus.FULL;
    }

    @Override
    public void tcheckFull() {
        // The 'for use' check already fully checks the component parameter
        // scope.
        tcheckForUse();
    }

    @Override
    protected SymbolTableEntry resolve1(Position position, String id, String done, CifTypeChecker tchecker,
            SymbolScope<?> origScope)
    {
        // Something is resolved via this parameter, so the parameter is used.
        used = true;

        // Check it enough to be able to resolve 'via' this scope.
        tcheckForUse();

        // Paranoia checking.
        if (done.isEmpty()) {
            // We can only reference first things VIA this scope, not in it.
            throw new IllegalArgumentException("done");
        }

        if (origScope != null) {
            // We can only reference first things VIA this scope, not in it.
            throw new IllegalArgumentException("origScope");
        }

        // Resolve via the component parameter. Any result will be in scope.
        return typeScope.resolve1(position, id, done, tchecker, origScope);
    }

    @Override
    public void detectCompDefInstCycles(List<ParentScope<?>> cycle) {
        // Nothing to do here. This is a reference to a component that is
        // created elsewhere, and goes outside of the containment hierarchy.
        // As such, this is already checked elsewhere.
    }
}
