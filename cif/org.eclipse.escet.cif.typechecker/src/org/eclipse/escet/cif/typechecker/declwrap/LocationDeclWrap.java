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

package org.eclipse.escet.cif.typechecker.declwrap;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.java.Assert;

/**
 * Location declaration wrapper. Nameless locations are not added to the symbol table, and as such, no
 * {@link LocationDeclWrap} is created for them.
 */
public class LocationDeclWrap extends DeclWrap<Location> {
    /**
     * Constructor for the {@link LocationDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param mmDecl The CIF metamodel representation of the location.
     */
    public LocationDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, Location mmDecl) {
        super(tchecker, scope, mmDecl);
        Assert.notNull(mmDecl.getName());
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

        // Locations have no additional type, so nothing else to do here.
        status = CheckStatus.FULL;
    }

    @Override
    public void tcheckFull() {
        // The 'for use' check already fully checks the location.
        tcheckForUse();
    }
}
