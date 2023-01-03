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

import java.util.Iterator;
import java.util.List;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.common.emf.EMFHelper;

/**
 * In-place transformation that converts enumerations to ranged integers.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * The transformation works as follows:
 * <ul>
 * <li>Enumeration declarations are removed.</li>
 * <li>Enumeration type references are changed to integer types of range {@code [0..n-1]}, where {@code n} is the number
 * of literals of the enumeration.</li>
 * <li>Enumeration literal references are changed to integer values. If the referred enumeration literal is the
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
public class EnumsToInts extends EnumsToBase {
    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Remove enumeration declarations.
        List<Declaration> decls = comp.getDeclarations();
        Iterator<Declaration> declIter = decls.iterator();
        while (declIter.hasNext()) {
            Declaration decl = declIter.next();
            if (decl instanceof EnumDecl) {
                declIter.remove();
            }
        }
    }

    @Override
    protected void walkEnumLiteralExpression(EnumLiteralExpression litRef) {
        // Get enumeration literal.
        EnumLiteral lit = litRef.getLiteral();

        // Replace literal reference by integer expression.
        Expression intExpr = CifValueUtils.makeInt(literalToInt(lit));
        EMFHelper.updateParentContainment(litRef, intExpr);
    }

    @Override
    protected void walkEnumType(EnumType enumType) {
        replaceEnumTypeByIntType(enumType);
    }
}
