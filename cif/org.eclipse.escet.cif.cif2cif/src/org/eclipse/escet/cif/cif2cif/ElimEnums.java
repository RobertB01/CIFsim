//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import java.util.Iterator;
import java.util.List;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that eliminates enumerations.
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
 * <li>Enumeration literal references are changed to integer values. If the the referred enumeration literal is the
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
public class ElimEnums extends CifWalker implements CifToCifTransformation {
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
        // Get enumeration declaration and its literals.
        EnumLiteral lit = litRef.getLiteral();
        EnumDecl enumDecl = (EnumDecl)lit.eContainer();
        List<EnumLiteral> literals = enumDecl.getLiterals();

        // Get index of literal.
        int idx = -1;
        for (int i = 0; i < literals.size(); i++) {
            EnumLiteral literal = literals.get(i);
            if (literal.getName() == null) {
                continue;
            }
            if (literal.getName().equals(litRef.getLiteral().getName())) {
                idx = i;
                break;
            }
        }
        Assert.check(idx >= 0);

        // Replace literal reference by integer expression.
        Expression intExpr = CifValueUtils.makeInt(idx);
        EMFHelper.updateParentContainment(litRef, intExpr);
    }

    @Override
    protected void walkEnumType(EnumType enumType) {
        // Construct integer type.
        IntType intType = newIntType();
        intType.setLower(0);
        intType.setUpper(enumType.getEnum().getLiterals().size() - 1);

        // Replace enumeration type by integer type.
        EMFHelper.updateParentContainment(enumType, intType);
    }
}
