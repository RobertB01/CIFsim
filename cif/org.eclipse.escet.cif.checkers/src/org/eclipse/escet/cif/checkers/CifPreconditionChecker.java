//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

/**
 * CIF precondition checker. Checks whether a given CIF specification satisfies certain {@link CifCheck preconditions}.
 */
public class CifPreconditionChecker extends CifChecker {
    /**
     * Constructor for the {@link CifPreconditionChecker} class.
     *
     * @param termination Cooperative termination query function.
     * @param preconditions The preconditions to check.
     */
    public CifPreconditionChecker(Termination termination, List<CifCheck> preconditions) {
        super(termination, preconditions);
    }

    /**
     * Constructor for the {@link CifPreconditionChecker} class.
     *
     * @param termination Cooperative termination query function.
     * @param preconditions The preconditions to check.
     */
    public CifPreconditionChecker(Termination termination, CifCheck... preconditions) {
        super(termination, preconditions);
    }

    /**
     * Check whether a given CIF specification satisfies its preconditions. If any of the preconditions is violated,
     * report that the tool failed to execute due to precondition violations indicating that the specification is
     * unsupported.
     *
     * @param spec The CIF specification to check.
     * @param absSpecPath The absolute local file system path to the CIF file to check.
     * @param toolName The human-readable name of the tool.
     * @throws UnsupportedException If there are any precondition violations.
     */
    public void reportPreconditionViolations(Specification spec, String absSpecPath, String toolName) {
        // Check specification.
        CifCheckViolations violations = check(spec, absSpecPath);

        // Report unsupported specification, if there are any precondition violations.
        if (violations.hasViolations()) {
            String incompleteTxt = "";
            if (violations.isIncomplete()) {
                incompleteTxt = " (checking was prematurely terminated, so the report below may be incomplete)";
            }
            List<String> lines = list();
            lines.add(toolName + fmt(" failed due to unsatisfied preconditions%s:", incompleteTxt));
            lines.addAll(violations.createReport());
            throw new UnsupportedException(String.join("\n", lines));
        }
    }
}
