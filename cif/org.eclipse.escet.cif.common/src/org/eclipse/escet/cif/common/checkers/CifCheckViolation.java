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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Objects;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.checkers.messages.CifCheckViolationMessage;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check condition violation. */
public class CifCheckViolation {
    /**
     * The provided CIF object for which the violation is to be reported. May be {@code null} to report for the CIF
     * specification. Is never a {@link Specification}. If it is not a named CIF object, the violation is reported on
     * its closest named ancestor.
     */
    private final PositionObject providedReportObject;

    /**
     * The named CIF object on which the violation is to be reported, or {@code null} to report it on the CIF
     * specification.
     */
    private final PositionObject actualReportObject;

    /** The message describing the violation. */
    private final CifCheckViolationMessage message;

    /**
     * Constructor for the {@link CifCheckViolation} class.
     *
     * @param cifObject The CIF object for which the violation is to be reported. Note that:
     *     <ul>
     *     <li>If this object itself is not named, then the {@link CifTextUtils#getNamedSelfOrAncestor closest named
     *     ancestor} is used instead.</li>
     *     <li>If the object itself is not named, and has no named ancestor, the specification is used.</li>
     *     <li>To report a violation on an entire specification, either a {@link Specification} or {@code null} may be
     *     provided.</li>
     *     </ul>
     * @param message The message describing the violation. The concatenated message should in principle not start with
     *     a capital letter, nor end with a period to end the sentence.
     * @see CifCheckViolations#add
     */
    public CifCheckViolation(PositionObject cifObject, CifCheckViolationMessage message) {
        // Store provided object. Normalize to single representation (null) for specifications.
        this.providedReportObject = (cifObject instanceof Specification) ? null : cifObject;

        // Store report object. Never a 'Specification'. May be 'null'.
        this.actualReportObject = (providedReportObject == null) ? null
                : CifTextUtils.getNamedSelfOrAncestor(providedReportObject);
        Assert.implies(this.actualReportObject != null, CifTextUtils.hasName(actualReportObject));

        // Store message.
        this.message = message;
    }

    /**
     * Is the violation being reported on the provided CIF object?
     *
     * @return {@code true} if the violation being reported on the provided CIF object, {@code false} if it is being
     *     reported on an ancestor of that object.
     */
    public boolean isReportOnSelf() {
        return providedReportObject == actualReportObject;
    }

    /**
     * Is the violation being reported on a CIF specification?
     *
     * @return {@code true} if it is being reported on a CIF specification, {@code false} if it is being reported on
     *     some named CIF object.
     */
    public boolean isReportOnSpecification() {
        return actualReportObject == null;
    }

    /**
     * Returns the named CIF object on which the violation is to be reported.
     *
     * @return The named CIF object on which the violation is to be reported.
     * @throws IllegalStateException If the violation is to be reported pm the CIF specification, rather than on a named
     *     CIF object.
     * @see #isReportOnSpecification
     */
    public PositionObject getReportObject() {
        if (actualReportObject == null) {
            throw new IllegalStateException();
        }
        return actualReportObject;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CifCheckViolation)) {
            return false;
        }
        CifCheckViolation that = (CifCheckViolation)obj;
        return this.providedReportObject == that.providedReportObject
                && this.actualReportObject == that.actualReportObject && this.message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providedReportObject, actualReportObject, message);
    }

    @Override
    public String toString() {
        String name = (actualReportObject == null) ? "specification"
                : "\"" + CifTextUtils.getAbsName(actualReportObject) + "\"";
        String messageText = message.getMessageText(this);
        return fmt("%s: %s.", name, messageText);
    }
}
