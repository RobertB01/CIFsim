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

package org.eclipse.escet.cif.checkers.checks;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;

/** CIF check that does not allow certain parameters of component definitions. */
public class CompDefNoSpecificParamsCheck extends CifCheck {
    /** The parameters to disallow. */
    private final EnumSet<NoSpecificCompDefParam> disalloweds;

    /**
     * Constructor for the {@link CompDefNoSpecificParamsCheck} class.
     *
     * @param disalloweds The parameters to disallow.
     */
    public CompDefNoSpecificParamsCheck(NoSpecificCompDefParam... disalloweds) {
        this(EnumSet.copyOf(Arrays.asList(disalloweds)));
    }

    /**
     * Constructor for the {@link CompDefNoSpecificParamsCheck} class.
     *
     * @param disalloweds The parameters to disallow.
     */
    public CompDefNoSpecificParamsCheck(EnumSet<NoSpecificCompDefParam> disalloweds) {
        this.disalloweds = disalloweds;
    }

    @Override
    protected void preprocessAlgParameter(AlgParameter param, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificCompDefParam.ALGEBRAIC)) {
            violations.add(param, "A component definition has an algebraic parameter");
        }
    }

    @Override
    protected void preprocessComponentParameter(ComponentParameter param, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificCompDefParam.COMPONENT)) {
            violations.add(param, "A component definition has a component parameter");
        }
    }

    @Override
    protected void preprocessEventParameter(EventParameter param, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificCompDefParam.EVENT)) {
            violations.add(param, "A component definition has an event parameter");
        }
    }

    @Override
    protected void preprocessLocationParameter(LocationParameter param, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificCompDefParam.LOCATION)) {
            violations.add(param, "A component definition has a location parameter");
        }
    }

    /** The parameters to disallow. */
    public static enum NoSpecificCompDefParam {
        /** Disallow algebraic parameters. */
        ALGEBRAIC,

        /** Disallow component parameters. */
        COMPONENT,

        /** Disallow event parameters. */
        EVENT,

        /** Disallow location parameters. */
        LOCATION,
    }
}
