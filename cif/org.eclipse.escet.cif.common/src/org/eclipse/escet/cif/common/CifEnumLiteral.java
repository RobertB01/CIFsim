//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;

/**
 * CIF enumeration literal value. Used to represent enumeration literals for the {@link CifEvalUtils} class. Note that
 * this class wraps the enumeration literals from the metamodel, to ensure proper value equality.
 */
public class CifEnumLiteral {
    /** The enumeration literal. */
    public final EnumLiteral literal;

    /**
     * Cached value of the names of the literals of the enumeration (declaration) of which the {@code #literal} is a
     * part. Is {@code null} if not yet computed.
     */
    private List<String> literalNames;

    /**
     * Cached value of the hash code of the names of the literals of the enumeration (declaration) of which the
     * {@code #literal} is a part. Is {@code null} if not yet computed.
     */
    private Integer literalNamesHash;

    /**
     * Constructor for the {@link CifEnumLiteral} class.
     *
     * @param literal The enumeration literal.
     */
    public CifEnumLiteral(EnumLiteral literal) {
        this.literal = literal;
    }

    @Override
    public boolean equals(Object obj) {
        // Optimize for same instances.
        if (this == obj) {
            return true;
        }

        // Make sure objects are of same type.
        if (!(obj instanceof CifEnumLiteral)) {
            return false;
        }
        CifEnumLiteral other = (CifEnumLiteral)obj;

        // If names are not equal, they are definitely not equal.
        if (!literal.getName().equals(other.literal.getName())) {
            return false;
        }

        // Names are equal. Make sure enumerations are compatible as well.
        return getLiteralNames().equals(other.getLiteralNames());
    }

    @Override
    public int hashCode() {
        return literal.getName().hashCode() ^ getLiteralNamesHash();
    }

    /**
     * Returns the names of the literals of the enumeration (declaration) of which the {@code #literal} is a part.
     *
     * @return The names of the literals.
     */
    private List<String> getLiteralNames() {
        // If not yet cached, compute it and store it in the cache.
        if (literalNames == null) {
            EnumDecl enumDecl = (EnumDecl)literal.eContainer();
            literalNames = CifTypeUtils.getLiteralNames(enumDecl);
        }

        // Return the cached value.
        return literalNames;
    }

    /**
     * Returns the hash code of the names of the literals of the enumeration (declaration) of which the {@code #literal}
     * is a part.
     *
     * @return The hash code of the names of the literals.
     */
    private int getLiteralNamesHash() {
        // If not yet cached, compute it and store it in the cache.
        if (literalNamesHash == null) {
            literalNamesHash = getLiteralNames().hashCode();
        }

        // Return the cached value.
        return literalNamesHash;
    }
}
