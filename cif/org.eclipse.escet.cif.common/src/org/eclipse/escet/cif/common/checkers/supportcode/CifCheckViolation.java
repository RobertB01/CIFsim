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

package org.eclipse.escet.cif.common.checkers.supportcode;

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
    private final PositionObject cifObject;

    /** The message describing the violation. */
    private final String message;

    /**
     * Constructor for the {@link CifCheckViolation} class.
     *
     * @param cifObject The named CIF object for which the violation is reported, or {@code null} to report it for the
     *     CIF specification.
     * @param message The message describing the violation. E.g., {@code "event is a channel"},
     *     {@code "automaton is a kindless automaton, lacking a supervisory kind"} or
     *     {@code "specification has no automata"}.
     */
    public CifCheckViolation(PositionObject cifObject, String message) {
        this.cifObject = cifObject;
        this.message = message;
        if (cifObject != null) {
            Assert.check(CifTextUtils.hasName(cifObject), cifObject);
        }
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
        return this.cifObject == that.cifObject && this.message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cifObject, message);
    }

    @Override
    public String toString() {
        String name = (cifObject == null) ? "specification" : "\"" + CifTextUtils.getAbsName(cifObject) + "\"";
        return fmt("%s: %s.", name, message);
    }
}
