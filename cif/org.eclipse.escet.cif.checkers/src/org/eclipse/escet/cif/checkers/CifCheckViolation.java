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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Objects;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check condition violation. */
public class CifCheckViolation {
    /** The CIF object for which the violation is to be reported. */
    private final PositionObject reportObject;

    /** The message describing the violation. */
    private final String message;

    /**
     * The line of text of the textual specification that contains (the start) of the CIF object for which the violation
     * is reported. Is {@code null} if violation concerns the entire specification.
     */
    private final String specLine;

    /**
     * Constructor for the {@link CifCheckViolation} class.
     *
     * @param reportObject The CIF object for which the violation is to be reported.
     * @param message The message describing the violation. Should in principle start with a capital letter, but not end
     *     with a period to end the sentence.
     * @param specLine The line of text of the textual specification that contains (the start) of the CIF object for
     *     which the violation is reported. Should be {@code null} if violation concerns the entire specification.
     * @see CifCheckViolations#add
     */
    public CifCheckViolation(PositionObject reportObject, String message, String specLine) {
        Assert.notNull(reportObject);
        Assert.notNull(reportObject.getPosition());
        Assert.notNull(message);
        Assert.areEqual(reportObject instanceof Specification, specLine == null);
        this.reportObject = reportObject;
        this.message = message;
        this.specLine = specLine;
    }

    /**
     * Returns the CIF object on which the violation is to be reported.
     *
     * @return The CIF object.
     */
    public PositionObject getReportObject() {
        return reportObject;
    }

    /**
     * Returns the context where the violation occurs.
     *
     * @return The violation context.
     */
    public CifCheckViolationContext getContext() {
        boolean entireSpec = reportObject instanceof Specification;
        PositionObject contextObject = entireSpec ? null : CifTextUtils.getNamedAncestor(reportObject);
        return new CifCheckViolationContext(entireSpec, contextObject);
    }

    /**
     * Returns the message describing the violation.
     *
     * @return The message describing the violation.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the line of text of the textual specification that contains (the start) of the CIF object for which the
     * violation is reported, or {@code null} if violation concerns the entire specification.
     *
     * @return The line, or {@code null}.
     */
    public String getSpecLine() {
        return specLine;
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
        return this.reportObject == that.reportObject && this.message.equals(that.message);
        // 'specLine' is left out, as it should always be the same for same 'reportObject'.
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportObject, message);
        // 'specLine' is left out, as for 'equals'.
    }

    @Override
    public String toString() {
        Position position = reportObject.getPosition();
        String positionText = PositionUtils.pos2str(position);
        return fmt("%s: %s.", positionText, message);
    }
}
