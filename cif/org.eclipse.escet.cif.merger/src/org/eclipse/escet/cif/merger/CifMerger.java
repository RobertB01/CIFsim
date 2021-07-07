//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.merger;

import static org.eclipse.escet.cif.common.CifEvalUtils.objToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.controllabilityToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstantExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteralExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTypeRef;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.cif2cif.RefReplace;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF merger. */
public class CifMerger {
    /**
     * Whether to check the result of {@link #getChildrenMap} for consistency against
     * {@link CifScopeUtils#getSymbolNamesForScope}, for debugging.
     */
    private static final boolean CHECK_GET_CHILDREN_MAP_CONSISTENCY = false;

    /**
     * Mapping from declarations to reference expressions for the merged declarations by which they are replaced. Only
     * declarations that are to be replaced are included in the mapping.
     *
     * @see RefReplace
     */
    private Map<PositionObject, Expression> refExprReplacements = map();

    /**
     * Mapping from declarations to reference types for the merged type declarations by which they are replaced. Only
     * declarations that are to be replaced are included in the mapping.
     *
     * @see RefReplace
     */
    private Map<Declaration, CifType> refTypeReplacements = map();

    /**
     * Merges two specification. Preconditions:
     * <ul>
     * <li>The specifications must not contain component definitions/instantiations.</li>
     * <li>SVG file declarations must only exist inside other CIF/SVG declarations, and not directly in components.</li>
     * <li>Print file declarations must only exist inside other print I/O declarations, and not directly in
     * components.</li>
     * <li>The relative paths of both specifications must be relative to the same directory.</li>
     * </ul>
     *
     * @param spec1 The first specification.
     * @param spec2 The second specification.
     * @return The merged specification.
     * @throws UnsupportedException If merging fails.
     */
    public Specification merge(Specification spec1, Specification spec2) {
        // Merge specifications.
        Specification merged = (Specification)merge((ComplexComponent)spec1, (ComplexComponent)spec2);

        // Reroute references for merged declarations.
        RefReplace replacer = new RefReplace(refExprReplacements, refTypeReplacements, Collections.emptyMap());
        replacer.transform(merged);

        // Return the merged specification.
        return merged;
    }

    /**
     * Merges two {@link #checkMergeCompatibility compatible} components. For preconditions, see
     * {@link #merge(Specification, Specification)}.
     *
     * @param comp1 The first component.
     * @param comp2 The second component.
     * @return The merged component.
     * @throws UnsupportedException If merging fails.
     */
    private ComplexComponent merge(ComplexComponent comp1, ComplexComponent comp2) {
        // Figure out in which direction to merge.
        ComplexComponent mergedComp;
        ComplexComponent otherComp;
        if (comp1 instanceof Group && comp2 instanceof Group) {
            mergedComp = comp1;
            otherComp = comp2;
        } else if (comp1 instanceof Automaton && comp2 instanceof Automaton) {
            // Method precondition violation.
            throw new RuntimeException("Merging two automata not allowed.");
        } else if (comp1 instanceof Automaton && comp2 instanceof Group) {
            checkGroupForAutMerge((Group)comp2);

            mergedComp = comp1;
            otherComp = comp2;
        } else if (comp1 instanceof Group && comp2 instanceof Automaton) {
            checkGroupForAutMerge((Group)comp1);

            mergedComp = comp2;
            otherComp = comp1;
        } else {
            String msg = fmt("Unknown components: %s / %s", comp1, comp2);
            throw new RuntimeException(msg);
        }

        // Merge initialization/marker predicates.
        mergedComp.getInitials().addAll(otherComp.getInitials());
        mergedComp.getMarkeds().addAll(otherComp.getMarkeds());

        // Merge invariants.
        mergedComp.getInvariants().addAll(otherComp.getInvariants());

        // Merge equations.
        mergedComp.getEquations().addAll(otherComp.getEquations());

        // Merge I/O declarations. Note that due to preconditions, we don't
        // have to account for merging file declarations, as they are only
        // present inside other I/O declarations, not directly in components.
        mergedComp.getIoDecls().addAll(otherComp.getIoDecls());

        // Merge children.
        mergeChildren(comp1, comp2, mergedComp);

        // Return the merged group.
        return mergedComp;
    }

    /**
     * Merges two {@link #checkMergeCompatibility compatible} declarations.
     *
     * @param decl1 The first declaration.
     * @param decl2 The second declaration.
     * @return The merged declaration.
     * @throws UnsupportedException If merging fails.
     */
    private Declaration merge(Declaration decl1, Declaration decl2) {
        // Merge based on the type of declarations.
        if (decl1 instanceof Event && decl2 instanceof Event) {
            return merge((Event)decl1, (Event)decl2);
        } else if (decl1 instanceof InputVariable && decl2 instanceof InputVariable) {
            return merge((InputVariable)decl1, (InputVariable)decl2);
        } else if (decl1 instanceof DiscVariable && decl2 instanceof InputVariable) {
            return merge((DiscVariable)decl1, (InputVariable)decl2);
        } else if (decl1 instanceof InputVariable && decl2 instanceof DiscVariable) {
            return merge((DiscVariable)decl2, (InputVariable)decl1);
        } else if (decl1 instanceof ContVariable && decl2 instanceof InputVariable) {
            return merge((ContVariable)decl1, (InputVariable)decl2);
        } else if (decl1 instanceof InputVariable && decl2 instanceof ContVariable) {
            return merge((ContVariable)decl2, (InputVariable)decl1);
        } else if (decl1 instanceof AlgVariable && decl2 instanceof InputVariable) {
            return merge((AlgVariable)decl1, (InputVariable)decl2);
        } else if (decl1 instanceof InputVariable && decl2 instanceof AlgVariable) {
            return merge((AlgVariable)decl2, (InputVariable)decl1);
        } else if (decl1 instanceof Constant && decl2 instanceof InputVariable) {
            return merge((Constant)decl1, (InputVariable)decl2);
        } else if (decl1 instanceof InputVariable && decl2 instanceof Constant) {
            return merge((Constant)decl2, (InputVariable)decl1);
        } else if (decl1 instanceof Constant && decl2 instanceof Constant) {
            return merge((Constant)decl1, (Constant)decl2);
        } else if (decl1 instanceof TypeDecl && decl2 instanceof TypeDecl) {
            return merge((TypeDecl)decl1, (TypeDecl)decl2);
        } else if (decl1 instanceof EnumDecl && decl2 instanceof EnumDecl) {
            return merge((EnumDecl)decl1, (EnumDecl)decl2);
        } else if (decl1 instanceof EnumDecl && decl2 instanceof TypeDecl) {
            return merge((EnumDecl)decl1, (TypeDecl)decl2);
        } else if (decl1 instanceof TypeDecl && decl2 instanceof EnumDecl) {
            return merge((EnumDecl)decl2, (TypeDecl)decl1);
        } else {
            String msg = fmt("Unmergeable decls: %s / %s", decl1, decl2);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Merges two input variables. Also updates the replacement mappings.
     *
     * @param var1 The first input variable.
     * @param var2 The second input variable.
     * @return The merged input variable.
     * @throws UnsupportedException If merging fails.
     */
    private InputVariable merge(InputVariable var1, InputVariable var2) {
        // Check types.
        CifType type1 = var1.getType();
        CifType type2 = var2.getType();

        if (!CifTypeUtils.checkTypeCompat(type1, type2, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt(
                    "Merging input variables with name \"%s\" failed: types \"%s\" and \"%s\" are not compatible.",
                    getAbsName(var1), typeToStr(type1), typeToStr(type2));
            throw new UnsupportedException(msg);
        }

        // Add variable replacement to the mapping.
        InputVariableExpression varRef = newInputVariableExpression();
        varRef.setVariable(var1);
        varRef.setType(deepclone(type1));
        refExprReplacements.put(var2, varRef);

        // Return merged input variable.
        return var1;
    }

    /**
     * Merges a discrete variable with an input variable. Also updates the replacement mappings.
     *
     * @param discVar The discrete variable.
     * @param inputVar The input variable.
     * @return The merged discrete variable.
     * @throws UnsupportedException If merging fails.
     */
    private DiscVariable merge(DiscVariable discVar, InputVariable inputVar) {
        // Check types.
        CifType discType = discVar.getType();
        CifType inputType = inputVar.getType();

        if (!CifTypeUtils.checkTypeCompat(discType, inputType, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt(
                    "Merging a discrete variable and input variable with name \"%s\" failed: types "
                            + "\"%s\" and \"%s\" are not compatible.",
                    getAbsName(discVar), typeToStr(discType), typeToStr(inputType));
            throw new UnsupportedException(msg);
        }

        // Add variable replacements to the mapping.
        DiscVariableExpression varRef = newDiscVariableExpression();
        varRef.setVariable(discVar);
        varRef.setType(deepclone(discType));
        refExprReplacements.put(inputVar, varRef);

        // Return merged discrete variable.
        return discVar;
    }

    /**
     * Merges a location with an input variable. Also updates the replacement mappings.
     *
     * @param loc The location.
     * @param inputVar The input variable.
     * @return The (merged) location.
     * @throws UnsupportedException If merging fails.
     */
    private Location merge(Location loc, InputVariable inputVar) {
        // Paranoia checking to ensure we have a name.
        Assert.check(loc.getName() != null);

        // Check types.
        CifType type = inputVar.getType();
        CifType ntype = CifTypeUtils.normalizeType(type);
        if (!(ntype instanceof BoolType)) {
            // Unsupported.
            String msg = fmt("Merging a location and input variable with name \"%s\" failed: type \"%s\" of the "
                    + "input variable is not a boolean type.", getAbsName(loc), typeToStr(type));
            throw new UnsupportedException(msg);
        }

        // Add variable replacements to the mapping.
        LocationExpression locRef = newLocationExpression();
        locRef.setLocation(loc);
        locRef.setType(newBoolType());
        refExprReplacements.put(inputVar, locRef);

        // Return (merged) location.
        return loc;
    }

    /**
     * Merges a continuous variable with an input variable. Also updates the replacement mappings.
     *
     * @param contVar The continuous variable.
     * @param inputVar The input variable.
     * @return The merged continuous variable.
     * @throws UnsupportedException If merging fails.
     */
    private ContVariable merge(ContVariable contVar, InputVariable inputVar) {
        // Check types.
        CifType contType = newRealType();
        CifType inputType = inputVar.getType();

        if (!CifTypeUtils.checkTypeCompat(contType, inputType, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt(
                    "Merging a continuous variable and input variable with name \"%s\" failed: types "
                            + "\"%s\" and \"%s\" are not compatible.",
                    getAbsName(contVar), typeToStr(contType), typeToStr(inputType));
            throw new UnsupportedException(msg);
        }

        // Add variable replacements to the mapping.
        ContVariableExpression varRef = newContVariableExpression();
        varRef.setVariable(contVar);
        varRef.setType(deepclone(contType));
        refExprReplacements.put(inputVar, varRef);

        // Return merged continuous variable.
        return contVar;
    }

    /**
     * Merges an algebraic variable with an input variable. Also updates the replacement mappings.
     *
     * @param algVar The algebraic variable.
     * @param inputVar The input variable.
     * @return The merged algebraic variable.
     * @throws UnsupportedException If merging fails.
     */
    private AlgVariable merge(AlgVariable algVar, InputVariable inputVar) {
        // Check types.
        CifType algType = algVar.getType();
        CifType inputType = inputVar.getType();

        if (!CifTypeUtils.checkTypeCompat(algType, inputType, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt(
                    "Merging an algebraic variable and input variable with name \"%s\" failed: types "
                            + "\"%s\" and \"%s\" are not compatible.",
                    getAbsName(algVar), typeToStr(algType), typeToStr(inputType));
            throw new UnsupportedException(msg);
        }

        // Add variable replacements to the mapping.
        AlgVariableExpression varRef = newAlgVariableExpression();
        varRef.setVariable(algVar);
        varRef.setType(deepclone(algType));
        refExprReplacements.put(inputVar, varRef);

        // Return merged algebraic variable.
        return algVar;
    }

    /**
     * Merges a constant with an input variable. Also updates the replacement mappings.
     *
     * @param constant The constant.
     * @param inputVar The input variable.
     * @return The merged constant.
     * @throws UnsupportedException If merging fails.
     */
    private Constant merge(Constant constant, InputVariable inputVar) {
        // Check types.
        CifType constType = constant.getType();
        CifType inputType = inputVar.getType();

        if (!CifTypeUtils.checkTypeCompat(constType, inputType, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt("Merging a constant and input variable with name \"%s\" failed: types \"%s\" and \"%s\" "
                    + "are not compatible.", getAbsName(constant), typeToStr(constType), typeToStr(inputType));
            throw new UnsupportedException(msg);
        }

        // Add variable replacements to the mapping.
        ConstantExpression constRef = newConstantExpression();
        constRef.setConstant(constant);
        constRef.setType(deepclone(constType));
        refExprReplacements.put(inputVar, constRef);

        // Return merged constant.
        return constant;
    }

    /**
     * Merges two constants. Also updates the replacement mappings.
     *
     * @param constant1 The first constant.
     * @param constant2 The second constant.
     * @return The merged constant.
     * @throws UnsupportedException If merging fails.
     */
    private Constant merge(Constant constant1, Constant constant2) {
        // Check types.
        CifType type1 = constant1.getType();
        CifType type2 = constant2.getType();

        if (!CifTypeUtils.checkTypeCompat(type1, type2, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt("Merging constants with name \"%s\" failed: types \"%s\" and \"%s\" are not compatible.",
                    getAbsName(constant1), typeToStr(type1), typeToStr(type2));
            throw new UnsupportedException(msg);
        }

        // Check value.
        if (!CifTypeUtils.supportsValueEquality(type1)) {
            // Unsupported.
            String msg = fmt("Merging constants with name \"%s\" failed: values of type \"%s\" can not be compared, "
                    + "and are thus not supported.", getAbsName(constant1), typeToStr(type1));
            throw new UnsupportedException(msg);
        }

        Object value1;
        Object value2;
        try {
            value1 = CifEvalUtils.eval(constant1.getValue(), false);
            value2 = CifEvalUtils.eval(constant2.getValue(), false);
        } catch (CifEvalException e) {
            // Can't happen: type checker already evaluates constants.
            throw new RuntimeException(e);
        }

        if (!value1.equals(value2)) {
            // Unsupported.
            String msg = fmt("Merging constants with name \"%s\" failed: values \"%s\" and \"%s\" are not the same.",
                    getAbsName(constant1), objToStr(value1), objToStr(value2));
            throw new UnsupportedException(msg);
        }

        // Add variable replacements to the mapping.
        ConstantExpression const1Ref = newConstantExpression();
        const1Ref.setConstant(constant1);
        const1Ref.setType(deepclone(type1));
        refExprReplacements.put(constant2, const1Ref);

        // Return merged constant.
        return constant1;
    }

    /**
     * Merges two events. Also updates the replacement mappings.
     *
     * @param event1 The first event.
     * @param event2 The second event.
     * @return The merged event.
     * @throws UnsupportedException If merging fails.
     */
    private Event merge(Event event1, Event event2) {
        // Check types.
        CifType type1 = event1.getType();
        CifType type2 = event2.getType();

        if ((type1 == null) != (type2 == null)) {
            // Unsupported.
            String msg = fmt("Merging events with name \"%s\" failed: the event is declared without a data type in one "
                    + "specification and with a data type in another.", getAbsName(event1));
            throw new UnsupportedException(msg);
        }

        if (type1 != null && type2 != null && !CifTypeUtils.checkTypeCompat(type1, type2, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt("Merging events with name \"%s\" failed: types \"%s\" and \"%s\" are not compatible.",
                    getAbsName(event1), typeToStr(type1), typeToStr(type2));
            throw new UnsupportedException(msg);
        }

        // Check controllable.
        Boolean ctrl1 = event1.getControllable();
        Boolean ctrl2 = event2.getControllable();
        if (!Objects.equals(ctrl1, ctrl2)) {
            String msg = fmt(
                    "Merging events with name \"%s\" failed: the event is declared as \"%s\" in one "
                            + "specification and as \"%s\" in another.",
                    getAbsName(event1), controllabilityToStr(ctrl1), controllabilityToStr(ctrl2));
            throw new UnsupportedException(msg);
        }

        // Add event replacement to the mapping.
        EventExpression eventRef = newEventExpression();
        eventRef.setEvent(event1);
        eventRef.setType(newBoolType());
        refExprReplacements.put(event2, eventRef);

        // Return merged event.
        return event1;
    }

    /**
     * Merges two type declarations. Also updates the replacement mappings.
     *
     * @param typeDecl1 The first type declaration.
     * @param typeDecl2 The second type declaration.
     * @return The merged type declaration.
     * @throws UnsupportedException If merging fails.
     */
    private TypeDecl merge(TypeDecl typeDecl1, TypeDecl typeDecl2) {
        // Check types.
        CifType type1 = typeDecl1.getType();
        CifType type2 = typeDecl2.getType();

        if (!CifTypeUtils.checkTypeCompat(type1, type2, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt("Merging type declarations with name \"%s\" failed: types \"%s\" and \"%s\" are not "
                    + "compatible.", getAbsName(typeDecl1), typeToStr(type1), typeToStr(type2));
            throw new UnsupportedException(msg);
        }

        // Add type declaration replacement to the mapping.
        TypeRef typeRef = newTypeRef();
        typeRef.setType(typeDecl1);
        refTypeReplacements.put(typeDecl2, typeRef);

        // Return merged type declaration.
        return typeDecl1;
    }

    /**
     * Merges an enumeration with a type declaration. Also updates the replacement mappings.
     *
     * @param enumDecl The type declaration.
     * @param typeDecl The enumeration.
     * @return The merged enumeration.
     * @throws UnsupportedException If merging fails.
     */
    private EnumDecl merge(EnumDecl enumDecl, TypeDecl typeDecl) {
        // Check types.
        EnumType etype = newEnumType();
        etype.setEnum(enumDecl);
        CifType ttype = typeDecl.getType();

        if (!CifTypeUtils.checkTypeCompat(etype, ttype, RangeCompat.EQUAL)) {
            // Unsupported.
            String msg = fmt(
                    "Merging a type declaration and enumeration with name \"%s\" failed: type \"%s\" of the "
                            + "type declaration is not compatible with the type of the enumeration.",
                    getAbsName(enumDecl), typeToStr(ttype));
            throw new UnsupportedException(msg);
        }

        // Add type declaration replacement to the mapping.
        refTypeReplacements.put(typeDecl, etype);

        // Return merged enumeration.
        return enumDecl;
    }

    /**
     * Merges two enumerations. Also updates the replacement mappings.
     *
     * @param enum1 The first enumeration.
     * @param enum2 The second enumeration.
     * @return The merged enumeration.
     * @throws UnsupportedException If merging fails.
     */
    private EnumDecl merge(EnumDecl enum1, EnumDecl enum2) {
        // Check types.
        if (!CifTypeUtils.areEnumsCompatible(enum1, enum2)) {
            // Unsupported.
            String msg = fmt("Merging enumerations with name \"%s\" failed: the enumerations are not compatible.",
                    getAbsName(enum1));
            throw new UnsupportedException(msg);
        }

        // Add enumeration replacement to the mapping.
        EnumType enumRef = newEnumType();
        enumRef.setEnum(enum1);
        refTypeReplacements.put(enum2, enumRef);

        // Add enumeration literal replacements to the mapping.
        for (int i = 0; i < enum1.getLiterals().size(); i++) {
            EnumLiteral lit1 = enum1.getLiterals().get(i);
            EnumLiteral lit2 = enum2.getLiterals().get(i);

            EnumLiteralExpression litRef = newEnumLiteralExpression();
            litRef.setLiteral(lit1);
            litRef.setType(deepclone(enumRef));
            refExprReplacements.put(lit2, litRef);
        }

        // Return merged enumeration.
        return enum1;
    }

    /**
     * Merges the children of the given components, into the given merged component.
     *
     * @param comp1 The first component.
     * @param comp2 The second component.
     * @param merged The merged component. Must be either the first or the second component.
     * @throws UnsupportedException If merging fails.
     */
    private void mergeChildren(ComplexComponent comp1, ComplexComponent comp2, ComplexComponent merged) {
        // Parameter check.
        Assert.check(merged == comp1 || merged == comp2);

        // Check merge compatibility of the children.
        Map<String, PositionObject> childMap1 = getChildrenMap(comp1);
        Map<String, PositionObject> childMap2 = getChildrenMap(comp2);
        Set<String> overlappingChildNames = set();
        overlappingChildNames.addAll(childMap1.keySet());
        overlappingChildNames.retainAll(childMap2.keySet());
        for (String overlappingChildName: overlappingChildNames) {
            PositionObject child1 = childMap1.get(overlappingChildName);
            PositionObject child2 = childMap2.get(overlappingChildName);
            checkMergeCompatibility(child1, child2);
        }

        // Merge children.
        Set<String> childNames = set();
        childNames.addAll(childMap1.keySet());
        childNames.addAll(childMap2.keySet());
        for (String childName: childNames) {
            // Get child for both component (if it exists).
            EObject child1 = childMap1.get(childName);
            EObject child2 = childMap2.get(childName);

            // Merge children.
            if (child1 == null && child2 != null) {
                // Move to merged component.
                if (comp2 != merged) {
                    // From second to first. Can't be a location, as then
                    // the automaton is the 'merged component', and we don't
                    // need to move it (and we can't merge locations with
                    // locations).
                    if (child2 instanceof Component) {
                        ((Group)comp1).getComponents().add((Component)child2);
                    } else if (child2 instanceof Declaration) {
                        comp1.getDeclarations().add((Declaration)child2);
                    } else {
                        // Skip enum literals, as the enum itself is moved or
                        // merged.
                        Assert.check(child2 instanceof EnumLiteral);
                    }
                }
            } else if (child1 != null && child2 == null) {
                // Move to merged component. Can't be a location, as then
                // the automaton is the 'merged component', and we don't
                // need to move it (and we can't merge locations with
                // locations).
                if (comp1 != merged) {
                    // From first to second.
                    if (child1 instanceof Component) {
                        ((Group)comp2).getComponents().add((Component)child1);
                    } else if (child1 instanceof Declaration) {
                        comp2.getDeclarations().add((Declaration)child1);
                    } else {
                        // Skip enum literals, as the enum itself is moved or
                        // merged.
                        Assert.check(child1 instanceof EnumLiteral);
                    }
                }
            } else if (child1 != null && child2 != null) {
                // Perform actual merge.
                EObject mergedChild;
                boolean isLoc;
                if (child1 instanceof Component) {
                    isLoc = false;
                    mergedChild = merge((ComplexComponent)child1, (ComplexComponent)child2);
                } else if (child1 instanceof Location && child2 instanceof InputVariable) {
                    isLoc = true;
                    mergedChild = merge((Location)child1, (InputVariable)child2);
                } else if (child1 instanceof InputVariable && child2 instanceof Location) {
                    isLoc = true;
                    mergedChild = merge((Location)child2, (InputVariable)child1);
                } else if (child1 instanceof EnumLiteral && child2 instanceof EnumLiteral) {
                    // Skip, as literals are merged when merging enumerations.
                    continue;
                } else {
                    Assert.check(child1 instanceof Declaration);
                    isLoc = false;
                    mergedChild = merge((Declaration)child1, (Declaration)child2);
                }

                // Replace child in merged component. For locations, we check
                // that the replacement is not needed.
                EObject replaceChild = (comp1 == merged) ? child1 : child2;
                if (isLoc) {
                    Assert.check(replaceChild == mergedChild);
                } else {
                    EMFHelper.updateParentContainment(replaceChild, mergedChild);
                }
            } else {
                // Should never happen.
                throw new RuntimeException("Child not in either component?");
            }
        }
    }

    /**
     * Returns a mapping from the names of the children to the actual children, for the children of the given component.
     * The children that are returned are all named objects directly declared in the given component. That is, the
     * domain of the returned mapping is equal to the result of {@link CifScopeUtils#getSymbolNamesForScope} for the
     * component.
     *
     * @param comp The component.
     * @return The mapping.
     */
    private Map<String, PositionObject> getChildrenMap(ComplexComponent comp) {
        // Get group and automaton, if applicable.
        Group group = (comp instanceof Group) ? (Group)comp : null;
        Automaton aut = (comp instanceof Automaton) ? (Automaton)comp : null;

        // Get children size (does not take enumeration literals into account),
        // used to initialize the mapping, for performance reasons only.
        int size = comp.getDeclarations().size();
        if (group != null) {
            size += group.getComponents().size();
        }
        if (aut != null) {
            size += aut.getLocations().size();
        }

        // Initialize mapping from child names to children.
        Map<String, PositionObject> rslt = mapc(size);

        // Add child components of group.
        PositionObject prev;
        if (group != null) {
            for (Component child: group.getComponents()) {
                prev = rslt.put(child.getName(), child);
                Assert.check(prev == null);
            }
        }

        // Add locations of automaton.
        if (aut != null) {
            for (Location loc: aut.getLocations()) {
                if (loc.getName() == null) {
                    continue;
                }

                prev = rslt.put(loc.getName(), loc);
                Assert.check(prev == null);
            }
        }

        // Add child declarations of component, including enumeration literals.
        for (Declaration child: comp.getDeclarations()) {
            prev = rslt.put(child.getName(), child);
            Assert.check(prev == null);

            if (child instanceof EnumDecl) {
                for (EnumLiteral lit: ((EnumDecl)child).getLiterals()) {
                    prev = rslt.put(lit.getName(), lit);
                    Assert.check(prev == null);
                }
            }
        }

        // Checking on mapping.
        if (CHECK_GET_CHILDREN_MAP_CONSISTENCY) {
            Set<String> names;
            names = CifScopeUtils.getSymbolNamesForScope(comp, null);
            Assert.check(names.equals(rslt.keySet()));
        }

        // Return complete children mapping.
        return rslt;
    }

    /**
     * Checks a group that is to be merged with an automaton for unsupported children.
     *
     * @param group The group.
     * @throws UnsupportedException If merging fails.
     */
    private void checkGroupForAutMerge(Group group) {
        // Group must not have child components.
        if (!group.getComponents().isEmpty()) {
            String msg = fmt("Merging group with name \"%s\" into an automaton with the same name failed: the "
                    + "group has child components.", getAbsName(group));
            throw new UnsupportedException(msg);
        }

        // Group must not have functions.
        for (Declaration decl: group.getDeclarations()) {
            if (decl instanceof Function) {
                Function func = (Function)decl;
                String msg = fmt("Merging group with name \"%s\" into an automaton with the same name failed: "
                        + "the group contains function \"%s\".", getAbsName(group), func.getName());
                throw new UnsupportedException(msg);
            }
        }
    }

    /**
     * Checks two objects for merge compatibility. The check is shallow, as it is only concerned with the object types
     * (Java classes), and not their contents.
     *
     * @param obj1 The first object. Must be a component, declaration, enumeration literal, or location.
     * @param obj2 The second object. Must be a component, declaration, enumeration literal, or location.
     * @throws UnsupportedException If the objects are not merge compatible.
     */
    private void checkMergeCompatibility(PositionObject obj1, PositionObject obj2) {
        if (obj1 instanceof Group && obj2 instanceof Group) {
            // OK.
        } else if (obj1 instanceof Automaton && obj2 instanceof Group) {
            // OK.
        } else if (obj1 instanceof Group && obj2 instanceof Automaton) {
            // OK.
        } else if (obj1 instanceof Automaton && obj2 instanceof Automaton) {
            String msg = fmt("Merging objects with name \"%s\" failed: merging two automata is not supported.",
                    getAbsName(obj1));
            throw new UnsupportedException(msg);
        } else if (obj1 instanceof Event && obj2 instanceof Event) {
            // OK.
        } else if (obj1 instanceof AlgVariable && obj2 instanceof AlgVariable) {
            String msg = fmt(
                    "Merging objects with name \"%s\" failed: merging two algebraic variables is not supported.",
                    getAbsName(obj1));
            throw new UnsupportedException(msg);
        } else if (obj1 instanceof InputVariable && obj2 instanceof InputVariable) {
            // OK.
        } else if ((obj1 instanceof DiscVariable || obj1 instanceof ContVariable || obj1 instanceof AlgVariable
                || obj1 instanceof Constant || obj1 instanceof Location) && obj2 instanceof InputVariable)
        {
            // OK.
        } else if (obj1 instanceof InputVariable && (obj2 instanceof DiscVariable || obj2 instanceof ContVariable
                || obj2 instanceof AlgVariable || obj2 instanceof Constant || obj2 instanceof Location))
        {
            // OK.
        } else if (obj1 instanceof Constant && obj2 instanceof Constant) {
            // OK.
        } else if (obj1 instanceof TypeDecl && obj2 instanceof TypeDecl) {
            // OK.
        } else if (obj1 instanceof EnumDecl && obj2 instanceof EnumDecl) {
            // OK.
        } else if (obj1 instanceof EnumDecl && obj2 instanceof TypeDecl) {
            // OK.
        } else if (obj1 instanceof TypeDecl && obj2 instanceof EnumDecl) {
            // OK.
        } else if (obj1 instanceof EnumLiteral && obj2 instanceof EnumLiteral) {
            // Merging enumeration literals from different enumerations is not
            // supported. We need to check whether they are from enumerations
            // with the same name (we already know they are from the same
            // scope). If the enumerations have the same name, merging them
            // will already check for compatibility of the enumerations and
            // their literals. If they are from enumerations with different
            // names, they won't be merged, so their literals also can't be
            // merged either.
            EnumDecl enum1 = (EnumDecl)obj1.eContainer();
            EnumDecl enum2 = (EnumDecl)obj2.eContainer();
            if (!enum1.getName().equals(enum2.getName())) {
                String msg = fmt(
                        "Merging enumeration literals with names \"%s\" and \"%s\" failed: the literals are from "
                                + "enumerations \"%s\" and \"%s\", which have different names and are not merged.",
                        getAbsName(obj1), getAbsName(obj2), getAbsName(enum1), getAbsName(enum2));
                throw new UnsupportedException(msg);
            }
        } else if (obj1 instanceof ContVariable && obj2 instanceof ContVariable) {
            String msg = fmt(
                    "Merging objects with name \"%s\" failed: merging two continuous variables is not supported.",
                    getAbsName(obj1));
            throw new UnsupportedException(msg);
        } else if (obj1 instanceof Function && obj2 instanceof Function) {
            String msg = fmt("Merging objects with name \"%s\" failed: merging two functions is not supported.",
                    getAbsName(obj1));
            throw new UnsupportedException(msg);
        } else if (obj1 instanceof DiscVariable && obj2 instanceof DiscVariable) {
            String msg = fmt(
                    "Merging objects with name \"%s\" failed: merging two discrete variables is not supported.",
                    getAbsName(obj1));
            throw new UnsupportedException(msg);
        } else {
            Assert.check(obj1.getClass() != obj2.getClass());
            String msg = fmt("Merging objects with name \"%s\" failed: the objects are incompatible.",
                    getAbsName(obj1));
            throw new UnsupportedException(msg);
        }
    }
}
