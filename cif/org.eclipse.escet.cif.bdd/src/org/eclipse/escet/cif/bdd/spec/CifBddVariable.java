//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.bdd.spec;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.bdd.utils.BddUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import com.github.javabdd.BDDDomain;

/** A CIF/BDD variable. Represents CIF state in a BDD representation. */
public abstract class CifBddVariable {
    /** The name of the CIF/BDD variable. */
    public final String name;

    /** The name of the CIF/BDD variable, without escaping. */
    public final String rawName;

    /**
     * The 0-based group index number. CIF/BDD variables are in the same group if their {@link #domain domains} are
     * interleaved. Is {@code -1} until actual value is set.
     */
    public int group = -1;

    /** The number of potential values of the variable. */
    public final int count;

    /** The lower bound (minimum value) of the variable. */
    public final int lower;

    /** The upper bound (maximum value) of the variable. */
    public final int upper;

    /**
     * The BDD domain of the variable. For updates, this represents the pre/old domain. Is {@code null} until actual
     * domain is set.
     */
    public BDDDomain domain;

    /**
     * The BDD domain of the variable. For updates, this represents the post/new domain. Is {@code null} until actual
     * domain is set.
     */
    public BDDDomain domainNew;

    /**
     * Constructor for the {@link CifBddVariable} class.
     *
     * @param obj The CIF object that corresponds to this CIF/BDD variable. Must be a {@link CifTextUtils#getName named}
     *     CIF object.
     * @param count The number of potential values of the variable.
     * @param lower The lower bound (minimum value) of the variable.
     * @param upper The upper bound (maximum value) of the variable.
     */
    public CifBddVariable(PositionObject obj, int count, int lower, int upper) {
        Assert.check(lower <= upper);
        Assert.check(count > 0);
        Assert.check(count == upper - lower + 1);

        this.name = CifTextUtils.getAbsName(obj);
        this.rawName = CifTextUtils.getAbsName(obj, false);
        this.count = count;
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Returns the size of the {@link #domain} and {@link #domainNew} to create for this CIF/BDD variable. This is the
     * not the number of BDD variables, but the number of actual distinct values that can be represented. For an integer
     * that can have values in the domain {@code [0..n]}, the size is {@code n + 1}. For an integer that can have values
     * in the domain {@code [l..u]}, the size is {@code u + 1}, as domains always start at zero lower bound.
     *
     * @return The size of the {@link #domain} and {@link #domainNew} to create for this CIF/BDD variable.
     */
    public abstract int getDomainSize();

    /**
     * Returns the number of BDD variables to use to represent this CIF/BDD variable. Is obtained from {@link #domain}
     * if present, and is computed otherwise.
     *
     * @return The number of BDD variables to use to represent this CIF/BDD variable.
     */
    public int getBddVarCount() {
        // Ask the domain, if it is present.
        if (domain != null) {
            return domain.varNum();
        }

        // Compute it.
        int size = getDomainSize();
        int maxValue = size - 1;
        return BddUtils.getMinimumBits(maxValue);
    }

    @Override
    public String toString() {
        return toString(0, 0, "Variable: ");
    }

    /**
     * Returns a textual representation of the kind of the CIF/BDD variable, relating to the kind of original CIF object
     * it corresponds to.
     *
     * @return The textual representation.
     */
    public abstract String getKindText();

    /**
     * Returns a textual representation of the type of the CIF/BDD variable. Returns {@code null} if the variable has no
     * type.
     *
     * @return The textual representation of the type, or {@code null}.
     */
    public abstract String getTypeText();

    /**
     * Returns a textual representation of the CIF/BDD variable.
     *
     * @param indentLevel The indentation level.
     * @param indentAmount The number of spaces to use per indentation level.
     * @param prefix The prefix to use, e.g. {@code "Variable: "} or {@code ""}.
     * @return The textual representation.
     */
    public String toString(int indentLevel, int indentAmount, String prefix) {
        return fmt("%s%s%s (group: %d, domain: %d+%d, BDD variables: %d, CIF/BDD values: %d/%d)",
                Strings.duplicate(" ", indentLevel * indentAmount), prefix, toStringInternal(), group,
                domain.getIndex(), domainNew.getIndex(), domain.varNum(), count, 1 << domain.varNum());
    }

    /**
     * Returns a textual representation of the CIF/BDD variable, to use as part of the output for {@link #toString}.
     *
     * @return The textual representation.
     */
    protected abstract String toStringInternal();
}
