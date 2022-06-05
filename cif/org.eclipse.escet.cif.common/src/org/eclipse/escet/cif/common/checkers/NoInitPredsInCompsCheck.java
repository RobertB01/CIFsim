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

package org.eclipse.escet.cif.common.checkers;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;

/**
 * CIF check that does not allow initialization predicates in components, i.e., does not allow initialization predicates
 * outside of locations.
 */
public class NoInitPredsInCompsCheck extends CifCheck {
    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        if (!comp.getInitials().isEmpty()) {
            addViolation(comp, "component contains an initialization predicate");
        }
    }
}
