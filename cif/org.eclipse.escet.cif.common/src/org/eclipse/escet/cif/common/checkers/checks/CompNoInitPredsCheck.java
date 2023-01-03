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

package org.eclipse.escet.cif.common.checkers.checks;

import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;

/**
 * CIF check that does not allow initialization predicates in components, i.e., does not allow initialization predicates
 * outside of locations.
 */
public class CompNoInitPredsCheck extends CifCheck {
    /** Whether to ignore initialization predicates that trivially hold. */
    private final boolean ignoreTriviallyTrueInitPreds;

    /**
     * Constructor of the {@link CompNoInitPredsCheck} class.
     *
     * <p>
     * Using this constructor causes the check to also report initialization predicates that trivially hold.
     * </p>
     */
    public CompNoInitPredsCheck() {
        this(false);
    }

    /**
     * Constructor of the {@link CompNoInitPredsCheck} class.
     *
     * @param ignoreTriviallyTrueInitPreds Whether to ignore initialization predicates that trivially hold.
     */
    public CompNoInitPredsCheck(boolean ignoreTriviallyTrueInitPreds) {
        this.ignoreTriviallyTrueInitPreds = ignoreTriviallyTrueInitPreds;
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp, CifCheckViolations violations) {
        if (!comp.getInitials().isEmpty()) {
            if (!ignoreTriviallyTrueInitPreds || !CifValueUtils.isTriviallyTrue(comp.getInitials(), true, true)) {
                violations.add(comp, new ReportObjectTypeDescrMessage(),
                        new LiteralMessage("contains an initialization predicate"));
            }
        }
    }
}
