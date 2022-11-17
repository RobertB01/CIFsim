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

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.checkers.messages.CifCheckViolationMessage;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check condition violation. */
public class CifCheckViolation {
    /**
     * The provided CIF object for which the violation is to be reported. If it is not a named CIF object, the violation
     * is reported on its closest named ancestor. is never {@code null}, but may be a {@link Specification}.
     */
    private final PositionObject providedReportObject;

    /** The named CIF object, or a {@link Specification}, on which the violation is to be reported. */
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
     *     <li>Unlike {@link CifCheckViolations#add}, {@code null} is not allowed here.</li>
     *     </ul>
     * @param message The message describing the violation. The concatenated message should in principle not start with
     *     a capital letter, nor end with a period to end the sentence.
     * @see CifCheckViolations#add
     */
    public CifCheckViolation(PositionObject cifObject, CifCheckViolationMessage message) {
        // Store provided object.
        Assert.notNull(cifObject);
        this.providedReportObject = cifObject;

        // Determine and store report object.
        PositionObject actualReportObject = CifTextUtils.getNamedSelfOrAncestor(providedReportObject);
        if (actualReportObject == null) {
            actualReportObject = CifScopeUtils.getSpecification(providedReportObject);
        }
        Assert.check(actualReportObject instanceof Specification || CifTextUtils.hasName(actualReportObject));
        this.actualReportObject = actualReportObject;

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
        return actualReportObject instanceof Specification;
    }

    /**
     * Returns the named CIF object, or a {@link Specification}, on which the violation is to be reported.
     *
     * @return The named CIF object, or a {@link Specification}.
     */
    public PositionObject getReportObject() {
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
        String name = isReportOnSpecification() ? "specification"
                : "\"" + CifTextUtils.getAbsName(actualReportObject) + "\"";
        String messageText = message.getMessageText(this);
        Position position = actualReportObject.getPosition();
        String positionText = (position != null) ? fmt(" (%s)", PositionUtils.pos2str(position)) : "";
        return fmt("%s%s: %s.", name, positionText, messageText);
    }
}
