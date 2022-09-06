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

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;

/**
 * CIF check that does not allow initialization predicates in components, i.e., does not allow initialization predicates
 * outside of locations.
 */
public class CompNoInitPredsCheck extends CifCheck {
    @Override
    protected void preprocessComplexComponent(ComplexComponent comp, CifCheckViolations violations) {
        if (!comp.getInitials().isEmpty()) {
            if (comp instanceof Specification) {
                violations.add(null, "top level scope of the specification contains an initialization predicate");
            } else {
                violations.add(comp, "component contains an initialization predicate");
            }
        }
    }
}
