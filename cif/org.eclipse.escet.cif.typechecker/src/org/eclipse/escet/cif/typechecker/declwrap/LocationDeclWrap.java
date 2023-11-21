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

package org.eclipse.escet.cif.typechecker.declwrap;

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifAnnotationsTypeChecker;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.java.Assert;

/**
 * Location declaration wrapper. Nameless locations are not added to the symbol table, and as such, no
 * {@link LocationDeclWrap} is created for them.
 */
public class LocationDeclWrap extends DeclWrap<Location> {
    /** The CIF AST representation of the location. */
    private final ALocation astDecl;

    /**
     * Constructor for the {@link LocationDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecl The CIF AST representation of the location.
     * @param mmDecl The CIF metamodel representation of the location.
     */
    public LocationDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, ALocation astDecl, Location mmDecl) {
        super(tchecker, scope, mmDecl);
        Assert.notNull(mmDecl.getName());
        this.astDecl = astDecl;
    }

    @Override
    public String getName() {
        return mmDecl.getName();
    }

    @Override
    public String getAbsName() {
        // Locations in the symbol table always have a name.
        return CifTextUtils.getAbsName(mmDecl);
    }

    @Override
    public void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // This declaration is now checked for use.
        status = CheckStatus.USE;
    }

    @Override
    public void tcheckFull() {
        // First, check 'for use', and make sure we haven't checked it before.
        tcheckForUse();
        if (isCheckedFull()) {
            return;
        }

        // Type check and add the annotations.
        List<Annotation> annos = CifAnnotationsTypeChecker.transAnnotations(astDecl.annotations, this, scope, tchecker);
        mmDecl.getAnnotations().addAll(annos);

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }
}
