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

import java.util.Comparator;
import java.util.Objects;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check condition violation context. */
public class CifCheckViolationContext implements Comparable<CifCheckViolationContext> {
    /** Whether the entire specification is the context. */
    private final boolean entireSpec;

    /** The description of the type of context, or {@code null} if {@link #entireSpec} is {@code true}. */
    private final String typeDescription;

    /**
     * The absolute name of the context. Is {@code null} if it is the top-level scope of the specification, or if
     * {@link #entireSpec} is {@code true}.
     */
    private final String absName;

    /**
     * Constructor for the {@link CifCheckViolationContext} class.
     *
     * @param entireSpec Whether the entire specification is the context.
     * @param contextObject The CIF object representing the context. Must be {@code null} if:
     *     <ul>
     *     <li>{@code entireSpec} is {@code true}.</li>
     *     <li>{@code entireSpec} is {@code false} and the top-level scope of the specification is the context.</li>
     *     </ul>
     */
    public CifCheckViolationContext(boolean entireSpec, PositionObject contextObject) {
        Assert.implies(entireSpec, contextObject == null);
        this.entireSpec = entireSpec;
        this.typeDescription = entireSpec ? null : (contextObject == null) ? "the top-level scope of the specification"
                : CifTextUtils.getTypeDescriptionForNamedObject(contextObject);
        this.absName = (entireSpec || contextObject == null) ? null : CifTextUtils.getAbsName(contextObject);
    }

    /**
     * Returns whether the entire specification is the context.
     *
     * @return {@code true} if the entire specification is the context, {@code false} otherwise.
     */
    public boolean isEntireSpec() {
        return entireSpec;
    }

    /**
     * Returns the description of the type of context, or {@code null} if {@link #isEntireSpec the entire specification
     * is the context}.
     *
     * @return The description of the type of context, or {@code null}.
     */
    public String getTypeDescription() {
        return typeDescription;
    }

    /**
     * Returns absolute name of the context, or {@code null} if:
     * <ul>
     * <li>The top-level scope of the specification.</li>
     * <li>{@link #isEntireSpec The entire specification is the context}.</li>
     * </ul>
     *
     * @return The absolute name or {@code null}.
     */
    public String getAbsName() {
        return absName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CifCheckViolationContext)) {
            return false;
        }
        CifCheckViolationContext that = (CifCheckViolationContext)obj;
        return this.entireSpec == that.entireSpec && Objects.equals(this.typeDescription, that.typeDescription)
                && Objects.equals(this.absName, that.absName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entireSpec, typeDescription, absName);
    }

    @Override
    public int compareTo(CifCheckViolationContext other) {
        return Comparator
                // Sort entire specification being the context, before other contexts.
                .comparing((CifCheckViolationContext c) -> !c.entireSpec)
                // Sort special contexts, not having an absolute name, before other contexts.
                .thenComparing(c -> c.absName != null)
                // Sort on absolute name. Contexts that have a name are compared with contexts that have a name, and
                // contexts that don't have a name are compared to contexts that don't have a name.
                .thenComparing(c -> c.absName)
                // Sort on type description. Contexts that have a type description are compared with contexts that have
                // a type description, and contexts that don't have a type description are compared to contexts that
                // don't have a type description.
                .thenComparing(c -> c.typeDescription)
                // Compare the contexts.
                .compare(this, other);
    }

    @Override
    public String toString() {
        if (entireSpec) {
            return "the specification";
        } else {
            return typeDescription + ((absName == null) ? "" : " \"" + absName + "\"");
        }
    }
}
