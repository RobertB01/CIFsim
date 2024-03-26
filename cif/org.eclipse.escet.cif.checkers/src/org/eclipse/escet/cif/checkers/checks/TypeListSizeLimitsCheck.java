//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/** Check that enforces lower and/or upper size limits to {@link ListType} on variables. */
public class TypeListSizeLimitsCheck extends CifCheck {
    /** Suggested value to use for specifying 'use meta-model limits'. */
    public static final int UNLIMITED = Integer.MIN_VALUE;

    /** Smallest allowed length of an array {@link ListType}. */
    private final int arrayLowestSize;

    /** Largest allowed length of an array {@link ListType}. */
    private final int arrayHighestSize;

    /** Smallest allowed length of a non-array {@link ListType}. */
    private final int nonArrayLowestSize;

    /** Largest allowed length of a non-array {@link ListType}. */
    private final int nonArrayHighestSize;

    /** Local walker to find list types in a variable type. */
    private final ListTypesGrabber listTypesGrabber;

    /**
     * Constructor of the {@link TypeListSizeLimitsCheck} class.
     *
     * @param arrayLowestSize If non-negative, the smallest allowed length of an array {@link ListType} for a variable.
     *     If negative or {@link #UNLIMITED} the meta-model limit is enforced.
     * @param arrayHighestSize If non-negative, the largest allowed length of an array {@link ListType} for a variable.
     *     If negative or {@link #UNLIMITED} the meta-model limit is enforced.
     * @param nonArrayLowestSize If non-negative, the smallest allowed length of a non-array {@link ListType} for a
     *     variable. If negative or {@link #UNLIMITED} the meta-model limit is enforced.
     * @param nonArrayHighestSize If non-negative, the largest allowed length of a non-array {@link ListType} for a
     *     variable. If negative or {@link #UNLIMITED} the meta-model limit is enforced.
     */
    public TypeListSizeLimitsCheck(int arrayLowestSize, int arrayHighestSize, int nonArrayLowestSize,
            int nonArrayHighestSize)
    {
        this.arrayLowestSize = arrayLowestSize;
        this.arrayHighestSize = arrayHighestSize;
        this.nonArrayLowestSize = nonArrayLowestSize;
        this.nonArrayHighestSize = nonArrayHighestSize;
        listTypesGrabber = new ListTypesGrabber();
    }

    @Override
    protected void preprocessDeclaration(Declaration decl, CifCheckViolations violations) {
        // Collect list types from the relevant declarations. Finish early when the declaration cannot have a list type
        // in their type.
        List<ListType> collectedListTypes;
        String declKind; // Kind of declaration.
        if (decl instanceof AlgVariable algVar) {
            collectedListTypes = listTypesGrabber.collectListTypes(algVar.getType());
            declKind = "algebraic variable";
        } else if (decl instanceof Constant constant) {
            collectedListTypes = listTypesGrabber.collectListTypes(constant.getType());
            declKind = "constant";
        } else if (decl instanceof ContVariable) {
            return; // Continuous variable does not have a list type.
        } else if (decl instanceof DiscVariable discVar) {
            collectedListTypes = listTypesGrabber.collectListTypes(discVar.getType());
            declKind = "discrete variable";
        } else if (decl instanceof EnumDecl) {
            return;
        } else if (decl instanceof Event event) {
            if (event.getType() == null) {
                return;
            }
            collectedListTypes = listTypesGrabber.collectListTypes(event.getType());
            declKind = "event";
        } else if (decl instanceof InputVariable inpVar) {
            collectedListTypes = listTypesGrabber.collectListTypes(inpVar.getType());
            declKind = "input variable";
        } else if (decl instanceof TypeDecl) {
            return; // Examined in variable declaration usage context.
        } else {
            throw new AssertionError("Unexpected declaration \"" + decl + "\" found.");
        }

        // No need to check anything with this declaration.
        if (collectedListTypes.isEmpty()) {
            return;
        }

        // Check limits on the found list types.
        declKind = "of " + declKind + "\"" + CifTextUtils.getAbsName(decl, false) + "\"";
        for (ListType listType: collectedListTypes) {
            if (listType.getLower() != null && listType.getLower().equals(listType.getUpper())) {
                // Array type.
                if (arrayLowestSize > 0 && listType.getLower() < arrayLowestSize) {
                    violations.add(listType, "Array type " + declKind + " is shorter than the minimum length of "
                            + arrayLowestSize + " elements");
                }
                if (arrayHighestSize > 0 && listType.getLower() > arrayHighestSize) {
                    violations.add(listType, "Array type " + declKind + " is longer than the maximum length of "
                            + arrayHighestSize + " elements");
                }
            } else {
                // Non-array type.
                if (nonArrayLowestSize > 0 && CifTypeUtils.getLowerBound(listType) < nonArrayLowestSize) {
                    violations.add(listType, "List type " + declKind + " can be shorter than the minimum length of "
                            + nonArrayLowestSize + " elements");
                }
                if (nonArrayHighestSize > 0 && CifTypeUtils.getUpperBound(listType) > nonArrayHighestSize) {
                    violations.add(listType, "List type " + declKind + " can be longer than the maximum length of "
                            + nonArrayHighestSize + " elements");
                }
            }
        }
    }

    /** Walker class to collect list types. */
    private static class ListTypesGrabber extends CifWalker {
        /** Temporary storage of collected {@link ListType}s. */
        private final List<ListType> listTypes = list();

        /**
         * Walk over the given type to collect {@link ListType}s.
         *
         * @param type Type to examine.
         * @return Collected list types in the given type. Returned data is also kept locally and will be destroyed on
         *     the next call to this method.
         */
        private List<ListType> collectListTypes(CifType type) {
            listTypes.clear();
            walkCifType(type);
            return listTypes;
        }

        @Override
        protected void preprocessListType(ListType listType) {
            listTypes.add(listType);
        }

        @Override
        protected void preprocessTypeRef(TypeRef typeRef) {
            walkCifType(typeRef.getType().getType()); // Also explore referenced type declarations.
        }
    }
}
