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

package org.eclipse.escet.cif.common.checkers;

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.checkers.messages.CifCheckViolationMessage;
import org.eclipse.escet.cif.common.checkers.messages.SequenceMessage;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check condition violations. */
public class CifCheckViolations {
    /** The violations collected so far. */
    private final Set<CifCheckViolation> violations = set();

    /** The CIF specification being checked. */
    private final Specification spec;

    /**
     * Constructor for the {@link CifCheckViolations} class.
     *
     * @param spec The CIF specification being checked.
     */
    public CifCheckViolations(Specification spec) {
        Assert.notNull(spec);
        this.spec = spec;
    }

    /**
     * Returns whether any violations were collected so far.
     *
     * @return {@code true} if violations were collected, {@code false} otherwise.
     */
    public boolean hasViolations() {
        return !violations.isEmpty();
    }

    /**
     * Returns the violations collected so far.
     *
     * @return The violations.
     */
    public Stream<CifCheckViolation> getViolations() {
        return violations.stream();
    }

    /**
     * Add a violation.
     *
     * @param cifObject The CIF object for which the violation is to be reported. Note that:
     *     <ul>
     *     <li>If this object itself is not named, then the {@link CifTextUtils#getNamedSelfOrAncestor closest named
     *     ancestor} is used instead.</li>
     *     <li>If the object itself is not named, and has no named ancestor, the specification is used.</li>
     *     <li>To report a violation on an entire specification, either a {@link Specification} or {@code null} may be
     *     provided.</li>
     *     </ul>
     * @param messages The non-empty sequence of concatenated messages describing the violation. The message texts of
     *     the messages are trimmed and concatenated, with a space being added in between each two message texts if
     *     needed. The concatenated message text should in principle not start with a capital letter, nor end with a
     *     period to end the sentence.
     * @see CifCheckViolation#CifCheckViolation
     */
    public void add(PositionObject cifObject, CifCheckViolationMessage... messages) {
        Assert.check(messages.length > 0);
        CifCheckViolationMessage message = (messages.length == 1) ? messages[0]
                : new SequenceMessage(Arrays.asList(messages));
        cifObject = (cifObject != null) ? cifObject : spec;
        violations.add(new CifCheckViolation(cifObject, message));
    }
}
