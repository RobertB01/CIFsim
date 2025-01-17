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

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** A CIF/BDD typed variable. Represents a CIF typed state object in a BDD representation. */
public abstract class CifBddTypedVariable extends CifBddVariable {
    /**
     * The CIF object that corresponds to this CIF/BDD variable. Must be a {@link CifTextUtils#getName named} CIF
     * object.
     */
    public final PositionObject obj;

    /** The normalized type of the variable. */
    public final CifType type;

    /**
     * Constructor for the {@link CifBddTypedVariable} class.
     *
     * @param obj The CIF object that corresponds to this CIF/BDD variable. Must be a {@link CifTextUtils#getName named}
     *     CIF object.
     * @param type The normalized type of the variable.
     * @param count The number of potential values of the variable.
     * @param lower The lower bound (minimum value) of the variable.
     * @param upper The upper bound (maximum value) of the variable.
     */
    public CifBddTypedVariable(PositionObject obj, CifType type, int count, int lower, int upper) {
        super(obj, count, lower, upper);
        this.obj = obj;
        this.type = type;
    }

    @Override
    public int getDomainSize() {
        if (type instanceof BoolType) {
            // [0..1] with '0' for 'false' and '1' for 'true'.
            Assert.check(count == 2);
            return count;
        } else if (type instanceof IntType) {
            // [0..upper] for int[lower..upper].
            Assert.check(0 <= lower);
            Assert.check(lower <= upper);
            return upper + 1;
        } else if (type instanceof EnumType) {
            // [0..n-1] for enumeration with 'n' literals.
            EnumDecl enumDecl = ((EnumType)type).getEnum();
            Assert.check(count > 0);
            Assert.check(count == enumDecl.getLiterals().size());
            return count;
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    @Override
    protected String toStringInternal() {
        return fmt("%s \"%s\" of type \"%s\"", getKindText(), name, getTypeText());
    }

    @Override
    public String getTypeText() {
        return CifTextUtils.typeToStr(type);
    }
}
