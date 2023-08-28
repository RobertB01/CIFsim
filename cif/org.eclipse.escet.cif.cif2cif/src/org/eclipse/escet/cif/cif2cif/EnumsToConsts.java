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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstantExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.emf.EMFHelper;

/**
 * In-place transformation that converts enumerations to integer constants.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * The transformation works as follows:
 * <ul>
 * <li>Enumeration declarations are replaced by constant integer declarations.</li>
 * <li>Enumeration type references are changed to integer types of range {@code [0..n-1]}, where {@code n} is the number
 * of literals of the enumeration.</li>
 * <li>Enumeration literal references are changed to constant references. If the referred enumeration literal is the
 * {@code n}-th literal in the corresponding enumeration, then the integer value is {@code n-1}. That is, the integer
 * value is the 0-based index of the enumeration literal in the enumeration declaration.</li>
 * </ul>
 * </p>
 *
 * <p>
 * If enumeration literals are renamed, this may influence value equality for {@link CifTypeUtils#areEnumsCompatible
 * compatible} enumerations. As such, either use this transformation before applying other transformations that perform
 * renaming on enumeration literals, or otherwise ensure that renaming does not result in an invalid specification.
 * </p>
 *
 * <p>
 * This transformation should not blow-up the size of the specification.
 * </p>
 *
 * <p>
 * The {@link ElimLocRefExprs} transformation may introduce new enumerations. Apply this transformation after that
 * transformation to eliminate them.
 * </p>
 *
 * @see MergeEnums
 */
public class EnumsToConsts extends EnumsToBase {
    /** Replacement map from enumeration literals to constants. */
    private Map<EnumLiteral, Constant> enumConst = map();

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Remove enumeration declarations and replace each literal with a constant declaration.
        List<Declaration> decls = comp.getDeclarations();
        Iterator<Declaration> declIter = decls.iterator();
        List<Declaration> newConstants = list();
        while (declIter.hasNext()) {
            Declaration decl = declIter.next();
            if (decl instanceof EnumDecl) {
                EnumDecl enumDecl = (EnumDecl)decl;
                List<EnumLiteral> lits = enumDecl.getLiterals();
                for (EnumLiteral lit: lits) {
                    newConstants.add(getConstant(lit));
                }
                declIter.remove();
            }
        }
        decls.addAll(newConstants);
    }

    @Override
    protected void walkEnumLiteralExpression(EnumLiteralExpression litRef) {
        EnumLiteral lit = litRef.getLiteral();

        // Replace literal reference by constant reference.
        ConstantExpression constExpr = newConstantExpression();
        constExpr.setConstant(getConstant(lit));
        constExpr.setType(getIntType(literalToInt(lit)));
        EMFHelper.updateParentContainment(litRef, constExpr);
    }

    @Override
    protected void walkEnumType(EnumType enumType) {
        replaceEnumTypeByIntType(enumType);
    }

    /**
     * Returns the constant that represents an enumeration literal. If such constant does not yet exist, it is created.
     *
     * @param lit The enumeration literal.
     * @return The constant corresponding to the supplied literal.
     */
    private Constant getConstant(EnumLiteral lit) {
        Constant constant = enumConst.get(lit);
        if (constant == null) {
            int value = literalToInt(lit);
            constant = newConstant(null, lit.getName(), null, getIntType(value), CifValueUtils.makeInt(value));
            enumConst.put(lit, constant);
        }
        return constant;
    }

    /**
     * Creates a ranged integer type with upper and lower value equal to {@code value}.
     *
     * @param value The value.
     * @return The newly created ranged integer type.
     */
    private IntType getIntType(int value) {
        return newIntType(value, null, value);
    }
}
