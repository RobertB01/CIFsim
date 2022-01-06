//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;

import java.util.Iterator;
import java.util.List;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;

/**
 * In-place transformation that eliminates constants.
 *
 * <p>
 * Since constants are shortcuts for values, eliminating them could result in a blow-up of the size of the
 * specification.
 * </p>
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * Implementers note: There is no need to account for automaton (definition) 'self' references, as they can't be
 * statically evaluated, and thus may not occur in values of constants.
 * </p>
 */
public class ElimConsts extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating constants from a CIF specification with component definitions is currently not "
                    + "supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Perform actual transformation.
        walkSpecification(spec);
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Remove constant declarations.
        List<Declaration> decls = comp.getDeclarations();
        Iterator<Declaration> declIter = decls.iterator();
        while (declIter.hasNext()) {
            Declaration decl = declIter.next();
            if (decl instanceof Constant) {
                declIter.remove();
            }
        }
    }

    @Override
    protected void walkConstantExpression(ConstantExpression constRef) {
        // Get value of constant, and make a unique copy.
        Expression value = constRef.getConstant().getValue();
        value = deepclone(value);

        // Replace constant reference by the value of the constant.
        EMFHelper.updateParentContainment(constRef, value);

        // Process the value recursively.
        walkExpression(value);
    }
}
