//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

/** CIF check that allows state invariants in components only if they are requirement invariants. */
public class CompStateInvsOnlyReqsCheck extends CifCheck {
    @Override
    protected void preprocessComplexComponent(ComplexComponent comp, CifCheckViolations violations) {
        for (Invariant inv: comp.getInvariants()) {
            if (inv.getInvKind() == InvKind.STATE) {
                SupKind supKind = inv.getSupKind();
                if (supKind != SupKind.REQUIREMENT) {
                    String kindTxt = (supKind == SupKind.NONE) ? "kindless" : CifTextUtils.kindToStr(supKind);
                    if (comp instanceof Specification) {
                        violations.add(null,
                                fmt("top level scope of the specification has a %s state invariant", kindTxt));
                    } else {
                        violations.add(comp, fmt("component has a %s state invariant", kindTxt));
                    }
                }
            }
        }
    }
}
