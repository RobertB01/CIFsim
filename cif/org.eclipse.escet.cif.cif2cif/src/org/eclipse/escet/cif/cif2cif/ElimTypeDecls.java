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

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;

import java.util.Iterator;
import java.util.List;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;

/**
 * In-place transformation that eliminates type declarations.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * Since type declarations are shortcuts for types, eliminating them could result in a blow-up of the size of the
 * specification.
 * </p>
 *
 * <p>
 * <strong>Pre-condition explained</strong>
 * </p>
 *
 * <p>
 * The following specification, where component definitions/instantiations are still present, illustrates the need for
 * this precondition:
 *
 * <pre>
 * group def P():
 *   enum e = A;
 *
 *   group def Q():
 *     type t = e;
 *
 *     group def R():
 *       disc t v1;
 *       disc e v2;
 *       disc bool v3;
 *       enum e = A;
 *       invariant v1 = v2;
 *     end
 *   end
 * end
 * </pre>
 *
 * Here we have a variable {@code v1} that has type {@code t}, which is really enumeration {@code e} in {@code P}.
 * Elimination of type declaration {@code t} would make the type of variable {@code v1} equal to enumeration {@code e}.
 * However, enumeration {@code e} in {@code P} is not referenceable, as {@code e} refers to local enumeration {@code e}
 * instead. Root absolute and scope absolute references can also not refer to enumeration {@code e} in {@code P}.
 * </p>
 *
 * <p>
 * We could create a local copy of the enumeration, as enumerations are compatible as long as they have the same
 * literals. However, we would have to rename the new local enumeration to {@code e2} to make sure it doesn't conflict
 * with the already existing local enumeration {@code e}. Furthermore, enumeration literal {@code A} already exists, so
 * we need to rename the enumeration literal of the new enumeration. However, if we do that, we no longer have a
 * enumeration compatible with the original enumeration, and the invariant becomes invalid. As such, we don't know how
 * to eliminate type {@code t}.
 * </p>
 *
 * <p>
 * Besides the issues mentioned above, we would also have to take into account that we may refer to type declarations by
 * using component instantiations and/or component parameters, possible even multiple ones mixed together. Furthermore,
 * type declarations can also refer to other type declarations, possibly even inside list types, etc. All in all, there
 * are too many aspects that make it really complicated.
 * </p>
 */
public class ElimTypeDecls extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating type declarations from a CIF specification with component definitions is "
                    + "currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Perform actual transformation.
        walkSpecification(spec);
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Remove type declarations.
        List<Declaration> decls = comp.getDeclarations();
        Iterator<Declaration> declIter = decls.iterator();
        while (declIter.hasNext()) {
            Declaration decl = declIter.next();
            if (decl instanceof TypeDecl) {
                declIter.remove();
            }
        }
    }

    @Override
    protected void postprocessTypeRef(TypeRef typeRef) {
        // Post-process type reference.
        CifType type = typeRef.getType().getType();
        CifType ntype = CifTypeUtils.normalizeTypeRecursive(type);

        // Replace type reference by the normalized type.
        EMFHelper.updateParentContainment(typeRef, deepclone(ntype));
    }
}
