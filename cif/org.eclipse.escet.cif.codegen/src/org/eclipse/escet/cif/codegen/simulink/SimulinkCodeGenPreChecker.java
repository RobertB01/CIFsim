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

package org.eclipse.escet.cif.codegen.simulink;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.isArrayType;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.codegen.CodeGenPreChecker;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.common.java.Assert;

/** Checker to check that the given CIF specification can be used for code generation with Simulink. */
public class SimulinkCodeGenPreChecker extends CodeGenPreChecker {
    /**
     * Test whether the given type is acceptable as type in simulink code generation.
     *
     * @param type Type to test.
     * @return Whether the type is supported.
     */
    public static boolean isGoodType(CifType type) {
        type = normalizeType(type);

        // Optionally, peel off up to two layers of 'list type' (for vector or
        // matrix of the element type).
        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            type = normalizeType(ltype.getElementType());
        }
        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            type = normalizeType(ltype.getElementType());
        }

        if (type instanceof BoolType) {
            return true;
        } else if (type instanceof IntType) {
            return true;
        } else if (type instanceof EnumType) {
            return true;
        } else if (type instanceof RealType) {
            return true;
        }
        return false;
    }

    /**
     * Get the number of rows from the given type, or {@code 0} if there is no rows list. Function also gives the length
     * of a Matlab vector.
     *
     * <p>
     * {@link #isGoodType} should hold.
     * </p>
     *
     * @param type Type to test.
     * @return Number of rows from the outer list, or else {@code 0}.
     */
    public static int getRowCount(CifType type) {
        type = normalizeType(type);

        Assert.check(isGoodType(type));
        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            if (isArrayType(ltype)) {
                return ltype.getLower();
            }
        }
        return 0;
    }

    /**
     * Get the number of columns from the given type, or {@code 0} if there is no column list.
     *
     * <p>
     * {@link #isGoodType} should hold. Also, for useful results, the {@link #getRowCount} method should not return
     * {@code 0}.
     * </p>
     *
     * @param type Type to test.
     * @return Number of columns from the inner list, or else {@code 0}.
     */
    public static int getColumnCount(CifType type) {
        type = normalizeType(type);

        Assert.check(isGoodType(type));
        if (type instanceof ListType) { // Peel off the rows if available.
            ListType ltype = (ListType)type;
            type = normalizeType(ltype.getElementType());
        }

        if (type instanceof ListType) { // If this holds, it is the inner array.
            ListType ltype = (ListType)type;
            if (isArrayType(ltype)) {
                return ltype.getLower();
            }
        }
        return 0;
    }

    @Override
    protected void walkInputVariable(InputVariable var) {
        if (!isGoodType(var.getType())) {
            String msg = fmt("Unsupported type \"%s\" found with input variable \"%s\".", typeToStr(var.getType()),
                    getAbsName(var));
            problems.add(msg);
            return;
        }
        super.walkInputVariable(var);
    }

    @Override
    protected void walkDiscVariable(DiscVariable var) {
        // A bad type will drop the discrete variable from the output.
        if (!isGoodType(var.getType())) {
            String msg = fmt("Unsupported output type \"%s\" found in discrete variable \"%s\", "
                    + "variable will be omitted from the output.", typeToStr(var.getType()), getAbsName(var));
            warn(msg);
        }

        super.walkDiscVariable(var);
    }

    @Override
    protected void walkAlgVariable(AlgVariable var) {
        // A bad type will drop the algebraic variable from the output.
        if (!isGoodType(var.getType())) {
            String msg = fmt("Unsupported output type \"%s\" found in algebraic variable \"%s\", "
                    + "variable will be omitted from the output.", typeToStr(var.getType()), getAbsName(var));
            warn(msg);
        }

        super.walkAlgVariable(var);
    }
}
