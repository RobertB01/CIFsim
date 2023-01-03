//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;

import java.util.List;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;

/**
 * Basic functionality for converting enumerations into other types. This class can be extended to implement specific
 * conversion types.
 */
public abstract class EnumsToBase extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating enumerations from a CIF specification with component definitions is currently "
                    + "not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Eliminate enumerations.
        walkSpecification(spec);
    }

    @Override
    protected abstract void preprocessComplexComponent(ComplexComponent comp);

    @Override
    protected abstract void walkEnumLiteralExpression(EnumLiteralExpression litRef);

    @Override
    protected abstract void walkEnumType(EnumType enumType);

    /**
     * Returns the 0-based index of the enumeration literal in the enumeration declaration.
     *
     * @param lit The enumeration literal.
     * @return The 0-based index of the enumeration literal in the enumeration declaration.
     */
    protected static int literalToInt(EnumLiteral lit) {
        // Get enumeration declaration and its literals.
        EnumDecl enumDecl = (EnumDecl)lit.eContainer();
        List<EnumLiteral> literals = enumDecl.getLiterals();

        // Get index of literal.
        int idx = literals.indexOf(lit);
        Assert.check(idx >= 0);

        return idx;
    }

    /**
     * Replaces an enumeration type by a ranged integer type. The range is equal to {@code [0..n-1]}, where {@code n} is
     * the number of literals of the enumeration.
     *
     * @param enumType The enumeration type to convert.
     */
    protected static void replaceEnumTypeByIntType(EnumType enumType) {
        // Construct integer type.
        IntType intType = newIntType();
        intType.setLower(0);
        intType.setUpper(enumType.getEnum().getLiterals().size() - 1);

        // Replace enumeration type by integer type.
        EMFHelper.updateParentContainment(enumType, intType);
    }
}
