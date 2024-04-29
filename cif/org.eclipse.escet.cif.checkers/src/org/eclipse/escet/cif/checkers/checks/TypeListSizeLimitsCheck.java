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
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
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
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/**
 * Check that enforces lower and/or upper size limits to {@link ListType} on {@link Declaration declarations}. Note this
 * includes function parameters and local variables in functions as well.
 */
public class TypeListSizeLimitsCheck extends CifCheckNoCompDefInst {
    /** Suggested value to use for specifying 'use meta-model limits'. */
    public static final int UNLIMITED = Integer.MIN_VALUE;

    /**
     * If non-negative, the smallest allowed length of an array {@link ListType} for a declaration. If negative or
     * {@link #UNLIMITED} the meta-model limit applies.
     */
    private final int arrayLowestSize;

    /**
     * If non-negative, the largest allowed length of an array {@link ListType} for a declaration. If negative or
     * {@link #UNLIMITED} the meta-model limit applies.
     */
    private final int arrayHighestSize;

    /**
     * If non-negative, the smallest allowed length of a non-array {@link ListType} for a declaration. If negative or
     * {@link #UNLIMITED} the meta-model limit applies.
     */
    private final int nonArrayLowestSize;

    /**
     * If non-negative, the largest allowed length of a non-array {@link ListType} for a declaration. If negative or
     * {@link #UNLIMITED} the meta-model limit applies.
     */
    private final int nonArrayHighestSize;

    /** Local walker to find list types in a type of a declaration. */
    private final ListTypesGrabber listTypesGrabber;

    /**
     * Constructor of the {@link TypeListSizeLimitsCheck} class.
     *
     * @param arrayLowestSize If non-negative, the smallest allowed length of an array {@link ListType} for a variable.
     *     If negative or {@link #UNLIMITED} the meta-model limit applies.
     * @param arrayHighestSize If non-negative, the largest allowed length of an array {@link ListType} for a variable.
     *     If negative or {@link #UNLIMITED} the meta-model limit applies.
     * @param nonArrayLowestSize If non-negative, the smallest allowed length of a non-array {@link ListType} for a
     *     variable. If negative or {@link #UNLIMITED} the meta-model limit applies.
     * @param nonArrayHighestSize If non-negative, the largest allowed length of a non-array {@link ListType} for a
     *     variable. If negative or {@link #UNLIMITED} the meta-model limit applies.
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
        if (decl instanceof AlgVariable algVar) {
            handleAlgVar(algVar, violations);
        } else if (decl instanceof Constant constant) {
            handleConstant(constant, violations);
        } else if (decl instanceof ContVariable) {
            return; // Continuous variable does not have a list type.
        } else if (decl instanceof DiscVariable discVar) {
            if (decl.eContainer() instanceof InternalFunction) {
                handleDiscVar(discVar, "function variable", violations);
            } else if (decl.eContainer() instanceof FunctionParameter) {
                handleDiscVar(discVar, "function parameter", violations);
            } else {
                handleDiscVar(discVar, "discrete variable", violations);
            }
        } else if (decl instanceof EnumDecl) {
            return;
        } else if (decl instanceof Event event) {
            if (event.getType() == null) {
                return;
            }
            handleChannel(event, violations);
        } else if (decl instanceof InputVariable inpVar) {
            handleInputVar(inpVar, violations);
        } else if (decl instanceof TypeDecl) {
            return; // Examined in declaration usage context.
        } else if (decl instanceof Function func) {
            handleFunction(func, violations);
        } else {
            throw new AssertionError("Unexpected declaration \"" + decl + "\" found.");
        }
    }

    /**
     * Process the type of an algebraic variable.
     *
     * @param algVar Algebraic variable to check.
     * @param violations Collected violations, may get extended in-place.
     */
    private void handleAlgVar(AlgVariable algVar, CifCheckViolations violations) {
        List<ListType> collectedListTypes = listTypesGrabber.collectListTypes(algVar.getType());
        if (collectedListTypes.isEmpty()) {
            return;
        }

        // Check limits on the found list types.
        for (ListType listType: collectedListTypes) {
            String typeCamelText = CifTypeUtils.isArrayType(listType) ? "Array" : "List";
            String text = isTypeInTypeDeclaration(listType)
                    ? fmt("%s type of algebraic variable \"%s\" allows", typeCamelText,
                            CifTextUtils.getAbsName(algVar, false))
                    : fmt("%s type of an algebraic variable allows", typeCamelText);
            report(listType, text, violations);
        }
    }

    /**
     * Process the type of a constant.
     *
     * @param constant Constant to check.
     * @param violations Collected violations, may get extended in-place.
     */
    private void handleConstant(Constant constant, CifCheckViolations violations) {
        List<ListType> collectedListTypes = listTypesGrabber.collectListTypes(constant.getType());
        if (collectedListTypes.isEmpty()) {
            return;
        }

        // Check limits on the found list types.
        for (ListType listType: collectedListTypes) {
            String typeCamelText = CifTypeUtils.isArrayType(listType) ? "Array" : "List";
            String text = isTypeInTypeDeclaration(listType)
                    ? fmt("%s type of constant \"%s\" allows", typeCamelText, CifTextUtils.getAbsName(constant, false))
                    : fmt("%s type of a constant allows", typeCamelText);
            report(listType, text, violations);
        }
    }

    /**
     * Process the type of a discrete variable, function parameter or function variable.
     *
     * @param discVar Discrete variable to check.
     * @param varKind Kind of variable.
     * @param violations Collected violations, may get extended in-place.
     */
    private void handleDiscVar(DiscVariable discVar, String varKind, CifCheckViolations violations) {
        List<ListType> collectedListTypes = listTypesGrabber.collectListTypes(discVar.getType());
        if (collectedListTypes.isEmpty()) {
            return;
        }

        // Check limits on the found list types.
        for (ListType listType: collectedListTypes) {
            String typeCamelText = CifTypeUtils.isArrayType(listType) ? "Array" : "List";
            String text = isTypeInTypeDeclaration(listType)
                    ? fmt("%s type of %s \"%s\" allows", typeCamelText, varKind,
                            CifTextUtils.getAbsName(discVar, false))
                    : fmt("%s type of a %s allows", typeCamelText, varKind);
            report(listType, text, violations);
        }
    }

    /**
     * Process the type of a channel.
     *
     * @param channel Channel to check.
     * @param violations Collected violations, may get extended in-place.
     */
    private void handleChannel(Event channel, CifCheckViolations violations) {
        List<ListType> collectedListTypes = listTypesGrabber.collectListTypes(channel.getType());
        if (collectedListTypes.isEmpty()) {
            return;
        }

        // Check limits on the found list types.
        for (ListType listType: collectedListTypes) {
            String typeCamelText = CifTypeUtils.isArrayType(listType) ? "Array" : "List";
            String text = isTypeInTypeDeclaration(listType)
                    ? fmt("%s type of channel \"%s\" allows", typeCamelText, CifTextUtils.getAbsName(channel, false))
                    : fmt("%s type of a channel allows", typeCamelText);
            report(listType, text, violations);
        }
    }

    /**
     * Process the type of an input variable.
     *
     * @param inputVar Input variable to check.
     * @param violations Collected violations, may get extended in-place.
     */
    private void handleInputVar(InputVariable inputVar, CifCheckViolations violations) {
        List<ListType> collectedListTypes = listTypesGrabber.collectListTypes(inputVar.getType());
        if (collectedListTypes.isEmpty()) {
            return;
        }

        // Check limits on the found list types.
        for (ListType listType: collectedListTypes) {
            String typeCamelText = CifTypeUtils.isArrayType(listType) ? "Array" : "List";
            String text = isTypeInTypeDeclaration(listType)
                    ? fmt("%s type of input variable \"%s\" allows", typeCamelText,
                            CifTextUtils.getAbsName(inputVar, false))
                    : fmt("%s type of an input variable allows", typeCamelText);
            report(listType, text, violations);
        }
    }

    /**
     * Process the return types of an internal user-defined function.
     *
     * @param func Internal user-defined function to check.
     * @param violations Collected violations, may get extended in-place.
     */
    private void handleFunction(Function func, CifCheckViolations violations) {
        List<ListType> collectedListTypes = listTypesGrabber.collectListTypes(func.getReturnTypes());
        if (collectedListTypes.isEmpty()) {
            return;
        }

        // Check limits on the found list types.
        for (ListType listType: collectedListTypes) {
            String typeCamelText = CifTypeUtils.isArrayType(listType) ? "Array" : "List";
            String text = isTypeInTypeDeclaration(listType)
                    ? fmt("%s type of a return type of function \"%s\" allows", typeCamelText,
                            CifTextUtils.getAbsName(func, false))
                    : fmt("%s type of a return type of a function allows", typeCamelText);
            report(listType, text, violations);
        }
    }

    /**
     * Report any found violations of the give list type aginst the specific limits.
     *
     * @param listType Type to check.
     * @param explainText Text describing the object being checked.
     * @param violations Collected violations, may get extended in-place.
     */
    private void report(ListType listType, String explainText, CifCheckViolations violations) {
        if (CifTypeUtils.isArrayType(listType)) {
            // Array type.

            // Checking lower limit for 0 isn't needed, as the type checker ensures non-negative lower bound.
            if (arrayLowestSize > 0 && listType.getLower() < arrayLowestSize) {
                reportViolation(violations, listType, explainText, "less than", arrayLowestSize);
            }
            if (arrayHighestSize >= 0 && listType.getLower() > arrayHighestSize) {
                reportViolation(violations, listType, explainText, "more than", arrayHighestSize);
            }
        } else {
            // Non-array type.

            // Checking lower limit for 0 isn't needed, as the type checker ensures non-negative lower bound.
            if (nonArrayLowestSize > 0 && CifTypeUtils.getLowerBound(listType) < nonArrayLowestSize) {
                reportViolation(violations, listType, explainText, "less than", nonArrayLowestSize);
            }
            if (nonArrayHighestSize >= 0 && CifTypeUtils.getUpperBound(listType) > nonArrayHighestSize) {
                reportViolation(violations, listType, explainText, "more than", nonArrayHighestSize);
            }
        }
    }

    /**
     * Report a violation.
     *
     * @param violations Already collected violations, is extended in-place.
     * @param listType Object with the violation.
     * @param explainText Text explaining the object being reported.
     * @param boundaryKind Kind of boundary to report.
     * @param boundary Number of allowed elements.
     */
    private void reportViolation(CifCheckViolations violations, ListType listType, String explainText,
            String boundaryKind, int boundary)
    {
        String typeText = CifTypeUtils.isArrayType(listType) ? "arrays" : "lists";
        String pluralSuffix = (boundary == 1) ? "" : "s";
        violations.add(listType,
                fmt("%s %s with %s %d element%s", explainText, typeText, boundaryKind, boundary, pluralSuffix));
    }

    /**
     * Compute whether the given type is used as definition of a type declaration.
     *
     * @param type Type to examine.
     * @return Whether the type is part of a type definition.
     */
    private boolean isTypeInTypeDeclaration(CifType type) {
        EObject eObj = type;
        while (eObj != null && eObj instanceof CifType) {
            eObj = eObj.eContainer();
        }
        return eObj instanceof TypeDecl;
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

        /**
         * Walk over the given types to collect {@link ListType}s.
         *
         * @param types Types to examine.
         * @return Collected list types in the given types. Returned data is also kept locally and will be destroyed on
         *     the next call to this method.
         */
        private List<ListType> collectListTypes(List<CifType> types) {
            listTypes.clear();
            for (CifType type: types) {
                walkCifType(type);
            }
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
