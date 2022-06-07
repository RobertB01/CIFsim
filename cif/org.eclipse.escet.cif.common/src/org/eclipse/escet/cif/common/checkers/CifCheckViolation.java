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
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check condition violation. */
public class CifCheckViolation {
    /**
     * The named CIF object for which the violation is reported, or {@code null} to report it for the CIF specification.
     */
    private final PositionObject object;

    /** The message describing the violation. */
    private final String message;

    /**
     * Constructor for the {@link CifCheckViolation} class.
     *
     * @param object The named CIF object for which the violation is reported, or {@code null} to report it for the CIF
     *     specification.
     * @param message The message describing the violation. E.g., {@code "event is a channel"},
     *     {@code "automaton is a kindless automaton, lacking a supervisory kind"} or
     *     {@code "specification has no automata"}.
     */
    public CifCheckViolation(PositionObject object, String message) {
        this.object = object;
        this.message = message;
        if (object != null) {
            Assert.check(CifTextUtils.hasName(object), object);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CifCheckViolation)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        CifCheckViolation that = (CifCheckViolation)obj;
        return this.object == that.object && this.message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, message);
    }

    @Override
    public String toString() {
        String name = (object == null) ? "specification" : "\"" + CifTextUtils.getAbsName(object) + "\"";
        return fmt("%s: %s.", name, message);
    }
}
