//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.java.CompositeCifWalker;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** CIF checker. Checks whether a given CIF specification satisfies certain {@link CifCheck conditions}. */
public class CifChecker extends CompositeCifWalker {
    /** The conditions to check. */
    private final CifCheck[] conditions;

    /**
     * Constructor for the {@link CifChecker} class.
     *
     * @param conditions The conditions to check.
     */
    public CifChecker(List<CifCheck> conditions) {
        this(conditions.toArray(i -> new CifCheck[i]));
    }

    /**
     * Constructor for the {@link CifChecker} class.
     *
     * @param conditions The conditions to check.
     */
    public CifChecker(CifCheck[] conditions) {
        super(conditions);
        this.conditions = conditions;
    }

    /**
     * Check whether a given CIF specification satisfies the given conditions.
     *
     * @param spec The CIF specification to check.
     * @return The violations.
     */
    public Set<CifCheckViolation> check(Specification spec) {
        // Initialize.
        Set<CifCheckViolation> violations = set();
        for (CifCheck condition: conditions) {
            condition.setViolations(violations);
        }

        // Check specification for condition violations.
        walkSpecification(spec);

        // Return the violations.
        return violations;
    }

    /**
     * Reports a tool failed to execute due to precondition violations indicating the specification is unsupported.
     *
     * @param violations The precondition violations. Must not be empty.
     * @param toolName The human-readable name of the tool.
     * @throws UnsupportedException Always thrown.
     */
    public static void reportPreconditionViolations(Set<CifCheckViolation> violations, String toolName) {
        Assert.check(!violations.isEmpty());
        List<String> messages = violations.stream().map(v -> "Unsupported " + v.toString())
                .collect(Collectors.toList());
        Collections.sort(messages, Strings.SORTER);
        String msg = toolName + " failed due to unsatisfied preconditions:\n - " + String.join("\n - ", messages);
        throw new UnsupportedException(msg);
    }
}
