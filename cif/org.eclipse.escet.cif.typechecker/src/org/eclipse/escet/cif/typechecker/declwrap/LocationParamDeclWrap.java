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

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;

/** Location parameter declaration wrapper. */
public class LocationParamDeclWrap extends DeclWrap<LocationParameter> {
    /**
     * Constructor for the {@link LocationParamDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param mmDecl The CIF metamodel representation of the location parameter.
     */
    public LocationParamDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, LocationParameter mmDecl) {
        super(tchecker, scope, mmDecl);
    }

    @Override
    public String getName() {
        return mmDecl.getLocation().getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(mmDecl.getLocation());
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
        // The 'for use' check already fully checks the location parameter.
        tcheckForUse();
    }
}
