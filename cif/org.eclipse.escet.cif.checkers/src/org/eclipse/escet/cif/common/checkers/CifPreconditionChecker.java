//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.CifChecker;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Strings;

/**
 * CIF precondition checker. Checks whether a given CIF specification satisfies certain {@link CifCheck preconditions}.
 */
public class CifPreconditionChecker extends CifChecker {
    /**
     * Constructor for the {@link CifPreconditionChecker} class.
     *
     * @param preconditions The preconditions to check.
     */
    public CifPreconditionChecker(List<CifCheck> preconditions) {
        super(preconditions);
    }

    /**
     * Constructor for the {@link CifPreconditionChecker} class.
     *
     * @param preconditions The preconditions to check.
     */
    public CifPreconditionChecker(CifCheck... preconditions) {
        super(preconditions);
    }

    /**
     * Check whether a given CIF specification satisfies its preconditions. If any of the preconditions is violated,
     * report that the tool failed to execute due to precondition violations indicating that the specification is
     * unsupported.
     *
     * @param spec The CIF specification to check.
     * @param toolName The human-readable name of the tool.
     * @throws UnsupportedException If there are any precondition violations.
     */
    public void reportPreconditionViolations(Specification spec, String toolName) {
        // Check specification.
        CifCheckViolations violations = check(spec);

        // Report unsupported specification, if there are any precondition violations.
        if (violations.hasViolations()) {
            Set<String> messages = violations.getViolations().map(v -> "Unsupported " + v.toString())
                    .collect(Collectors.toSet());
            List<String> sortedMessages = Sets.sortedgeneric(messages, Strings.SORTER);
            String msg = toolName + " failed due to unsatisfied preconditions:\n - "
                    + String.join("\n - ", sortedMessages);
            throw new UnsupportedException(msg);
        }
    }
}
