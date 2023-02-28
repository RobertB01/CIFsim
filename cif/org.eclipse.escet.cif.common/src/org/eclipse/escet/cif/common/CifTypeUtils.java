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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDistType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFuncType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSetType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newVoidType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** CIF type and scoping related utility methods. */
public class CifTypeUtils {
    /** Constructor for the {@link CifTypeUtils} class. */
    private CifTypeUtils() {
        // Static class.
    }

    /**
     * Checks two CIF types for compatibility.
     *
     * <p>
     * For component (definition) types, if {@code type1} is a component definition type for component definition
     * {@code X}, then {@code type2} must be a component type for component {@code Y}, where {@code Y} is an
     * instantiation of {@code X}.
     * </p>
     *
     * @param type1 The first CIF type.
     * @param type2 The second CIF type.
     * @param rangeCompat The kind of ranged type range compatibility to check for integer and list types. May be
     *     {@code null} if the caller is sure no integer and list types need to be checked.
     * @return {@code true} if the types are compatible, {@code false} otherwise.
     */
    public static boolean checkTypeCompat(CifType type1, CifType type2, RangeCompat rangeCompat) {
        return checkTypeCompat(type1, type2, rangeCompat, rangeCompat);
    }

    /**
     * Checks two CIF types for compatibility.
     *
     * <p>
     * For component (definition) types, if {@code type1} is a component definition type for component definition
     * {@code X}, then {@code type2} must be a component type for component {@code Y}, where {@code Y} is an
     * instantiation of {@code X}.
     * </p>
     *
     * @param type1 The first CIF type.
     * @param type2 The second CIF type.
     * @param rangeCompatInt The kind of ranged type range compatibility to check for integer types. May be {@code null}
     *     if the caller is sure no integer types need to be checked.
     * @param rangeCompatList The kind of ranged type range compatibility to check for list types. May be {@code null}
     *     if the caller is sure no list types need to be checked.
     * @return {@code true} if the types are compatible, {@code false} otherwise.
     * @see TypeEqHashWrap
     */
    public static boolean checkTypeCompat(CifType type1, CifType type2, RangeCompat rangeCompatInt,
            RangeCompat rangeCompatList)
    {
        // BoolType.
        if (type1 instanceof BoolType && type2 instanceof BoolType) {
            return true;
        }

        // IntType.
        if (type1 instanceof IntType && type2 instanceof IntType) {
            IntType itype1 = (IntType)type1;
            IntType itype2 = (IntType)type2;
            int lower1 = getLowerBound(itype1);
            int lower2 = getLowerBound(itype2);
            int upper1 = getUpperBound(itype1);
            int upper2 = getUpperBound(itype2);
            switch (rangeCompatInt) {
                case IGNORE:
                    return true;

                case CONTAINED:
                    return lower1 <= lower2 && upper1 >= upper2;

                case EQUAL:
                    return lower1 == lower2 && upper1 == upper2;

                case OVERLAP:
                    return upper1 >= lower2 && lower1 <= upper2;

                default:
                    String msg = "Unknown rangeCompatInt: " + rangeCompatInt;
                    throw new RuntimeException(msg);
            }
        }

        // TypeRef.
        if (type1 instanceof TypeRef) {
            return checkTypeCompat(((TypeRef)type1).getType().getType(), type2, rangeCompatInt, rangeCompatList);
        }
        if (type2 instanceof TypeRef) {
            return checkTypeCompat(type1, ((TypeRef)type2).getType().getType(), rangeCompatInt, rangeCompatList);
        }

        // EnumType.
        if (type1 instanceof EnumType && type2 instanceof EnumType) {
            EnumDecl enum1 = ((EnumType)type1).getEnum();
            EnumDecl enum2 = ((EnumType)type2).getEnum();
            return CifTypeUtils.areEnumsCompatible(enum1, enum2);
        }

        // RealType.
        if (type1 instanceof RealType && type2 instanceof RealType) {
            return true;
        }

        // StringType.
        if (type1 instanceof StringType && type2 instanceof StringType) {
            return true;
        }

        // ListType.
        if (type1 instanceof ListType && type2 instanceof ListType) {
            ListType ltype1 = (ListType)type1;
            ListType ltype2 = (ListType)type2;
            int lower1 = getLowerBound(ltype1);
            int lower2 = getLowerBound(ltype2);
            int upper1 = getUpperBound(ltype1);
            int upper2 = getUpperBound(ltype2);
            switch (rangeCompatList) {
                case IGNORE:
                    // Ignore.
                    break;

                case CONTAINED:
                    if (lower1 > lower2 || upper1 < upper2) {
                        return false;
                    }
                    break;

                case EQUAL:
                    if (lower1 != lower2 || upper1 != upper2) {
                        return false;
                    }
                    break;

                case OVERLAP:
                    if (upper1 < lower2 || lower1 > upper2) {
                        return false;
                    }
                    break;

                default:
                    String msg = "Unknown rangeCompatList: " + rangeCompatList;
                    throw new RuntimeException(msg);
            }
            return checkTypeCompat(ltype1.getElementType(), ltype2.getElementType(), rangeCompatInt, rangeCompatList);
        }

        // SetType.
        if (type1 instanceof SetType && type2 instanceof SetType) {
            SetType stype1 = (SetType)type1;
            SetType stype2 = (SetType)type2;
            return checkTypeCompat(stype1.getElementType(), stype2.getElementType(), rangeCompatInt, rangeCompatList);
        }

        // FuncType.
        if (type1 instanceof FuncType && type2 instanceof FuncType) {
            FuncType ftype1 = (FuncType)type1;
            FuncType ftype2 = (FuncType)type2;

            // Check return types. 'OVERLAP' range compatibility is replaced by
            // 'CONTAINED' range compatibility.
            //
            // This fixes an issue with assignment of functions with ranged
            // result type to a variable potentially leading to range
            // constraint violation. For assignments, the value must still
            // have overlap with the variable. However, for function types,
            // the result type must be contained rather than have overlap.
            // This is unlikely to affect any users and current models, as it
            // requires that functions are used as values, the function has a
            // ranged result type, and the ranged result type has overlap
            // with the type of the variable but is not contained in it.
            RangeCompat returnCompatInt = rangeCompatInt;
            RangeCompat returnCompatList = rangeCompatList;
            if (returnCompatInt == RangeCompat.OVERLAP) {
                returnCompatInt = RangeCompat.CONTAINED;
            }
            if (returnCompatList == RangeCompat.OVERLAP) {
                returnCompatList = RangeCompat.CONTAINED;
            }
            CifType rtype1 = ftype1.getReturnType();
            CifType rtype2 = ftype2.getReturnType();
            if (!checkTypeCompat(rtype1, rtype2, returnCompatInt, returnCompatList)) {
                return false;
            }

            // Check parameter types.
            int cnt1 = ftype1.getParamTypes().size();
            int cnt2 = ftype2.getParamTypes().size();
            if (cnt1 != cnt2) {
                return false;
            }

            for (int i = 0; i < ftype1.getParamTypes().size(); i++) {
                CifType ptype1 = ftype1.getParamTypes().get(i);
                CifType ptype2 = ftype2.getParamTypes().get(i);
                if (!checkTypeCompat(ptype1, ptype2, rangeCompatInt, rangeCompatList)) {
                    return false;
                }
            }

            return true;
        }

        // DictType.
        if (type1 instanceof DictType && type2 instanceof DictType) {
            DictType dtype1 = (DictType)type1;
            DictType dtype2 = (DictType)type2;
            return checkTypeCompat(dtype1.getKeyType(), dtype2.getKeyType(), rangeCompatInt, rangeCompatList)
                    && checkTypeCompat(dtype1.getValueType(), dtype2.getValueType(), rangeCompatInt, rangeCompatList);
        }

        // TupleType.
        if (type1 instanceof TupleType && type2 instanceof TupleType) {
            TupleType ttype1 = (TupleType)type1;
            TupleType ttype2 = (TupleType)type2;

            int cnt1 = ttype1.getFields().size();
            int cnt2 = ttype2.getFields().size();
            if (cnt1 != cnt2) {
                return false;
            }

            for (int i = 0; i < ttype1.getFields().size(); i++) {
                CifType ftype1 = ttype1.getFields().get(i).getType();
                CifType ftype2 = ttype2.getFields().get(i).getType();
                if (!checkTypeCompat(ftype1, ftype2, rangeCompatInt, rangeCompatList)) {
                    return false;
                }
            }

            return true;
        }

        // DistType.
        if (type1 instanceof DistType && type2 instanceof DistType) {
            DistType dtype1 = (DistType)type1;
            DistType dtype2 = (DistType)type2;
            return checkTypeCompat(dtype1.getSampleType(), dtype2.getSampleType(), rangeCompatInt, rangeCompatList);
        }

        // CompParamWrapType. Just unwrap the wrapper, as type compatibility
        // of the wrapped types is not influenced by the wrappers.
        if (type1 instanceof CompParamWrapType) {
            return checkTypeCompat(((CompParamWrapType)type1).getReference(), type2, rangeCompatInt, rangeCompatList);
        }
        if (type2 instanceof CompParamWrapType) {
            return checkTypeCompat(type1, ((CompParamWrapType)type2).getReference(), rangeCompatInt, rangeCompatList);
        }

        // CompInstWrapType. Just unwrap the wrapper, as type compatibility
        // of the wrapped types is not influenced by the wrappers.
        if (type1 instanceof CompInstWrapType) {
            return checkTypeCompat(((CompInstWrapType)type1).getReference(), type2, rangeCompatInt, rangeCompatList);
        }
        if (type2 instanceof CompInstWrapType) {
            return checkTypeCompat(type1, ((CompInstWrapType)type2).getReference(), rangeCompatInt, rangeCompatList);
        }

        // ComponentType and ComponentDefType.
        if (type1 instanceof ComponentDefType && type2 instanceof ComponentType) {
            // Get component definition from first type.
            ComponentDef cdef1 = ((ComponentDefType)type1).getDefinition();

            // Get component instantiation from second type.
            Component comp2 = ((ComponentType)type2).getComponent();
            if (!(comp2 instanceof ComponentInst)) {
                return false;
            }
            ComponentInst inst = (ComponentInst)comp2;

            // Get component definition from component instantiation.
            ComponentDef cdef2 = CifTypeUtils.getCompDefFromCompInst(inst);

            // Do they refer to the same component definition?
            return cdef1 == cdef2;
        }

        // ComponentDefType and ComponentDefType.
        if (type1 instanceof ComponentDefType && type2 instanceof ComponentDefType) {
            // Get component definition from first type.
            ComponentDef cdef1 = ((ComponentDefType)type1).getDefinition();

            // Get component definition from second type.
            ComponentDef cdef2 = ((ComponentDefType)type2).getDefinition();

            // Do they refer to the same component definition?
            return cdef1 == cdef2;
        }

        // VoidType.
        if (type1 instanceof VoidType && type2 instanceof VoidType) {
            return true;
        }

        // Incompatible.
        return false;
    }

    /**
     * Are the two given enumerations compatible? That is, do they have the same enumeration literals (same amount, and
     * with same names, in same order)?
     *
     * <p>
     * If two enumerations are compatible, they are also type compatible for the type checker. Furthermore, literals
     * from the one enumeration can then be compared to the literals of the other enumeration.
     * </p>
     *
     * @param enum1 The first enumeration.
     * @param enum2 The second enumeration.
     * @return {@code true} if the enumerations are compatible, {@code false} otherwise.
     */
    public static boolean areEnumsCompatible(EnumDecl enum1, EnumDecl enum2) {
        if (enum1.getLiterals().size() != enum2.getLiterals().size()) {
            return false;
        }

        List<String> lits1 = getLiteralNames(enum1);
        List<String> lits2 = getLiteralNames(enum2);
        return lits1.equals(lits2);
    }

    /**
     * Returns the names of the enumeration literals of an enumeration (declaration).
     *
     * @param enumDecl The enumeration (declaration).
     * @return The names of the enumeration literals of an enumeration (declaration).
     */
    public static List<String> getLiteralNames(EnumDecl enumDecl) {
        List<String> lits = listc(enumDecl.getLiterals().size());
        for (EnumLiteral lit: enumDecl.getLiterals()) {
            lits.add(lit.getName());
        }
        return lits;
    }

    /**
     * Hashes the given type. Type ranges are ignored.
     *
     * <p>
     * Component types and component definition types are currently not supported by this method.
     * </p>
     *
     * @param type The type.
     * @return The hash of the type.
     */
    public static int hashType(CifType type) {
        return hashType(type, true, true);
    }

    /**
     * Hashes the given type.
     *
     * <p>
     * Component types and component definition types are currently not supported by this method.
     * </p>
     *
     * @param type The type.
     * @param ignoreRanges Whether to ignore integer and list type ranges.
     * @return The hash of the type.
     */
    public static int hashType(CifType type, boolean ignoreRanges) {
        return hashType(type, ignoreRanges, ignoreRanges);
    }

    /**
     * Hashes the given type.
     *
     * <p>
     * Component types and component definition types are currently not supported by this method.
     * </p>
     *
     * @param type The type.
     * @param ignoreRangesInt Whether to ignore integer type ranges.
     * @param ignoreRangesList Whether to ignore list type ranges.
     * @return The hash of the type.
     */
    public static int hashType(CifType type, boolean ignoreRangesInt, boolean ignoreRangesList) {
        if (type instanceof BoolType) {
            return 1 << 0;
        }
        if (type instanceof RealType) {
            return 1 << 6;
        }
        if (type instanceof StringType) {
            return 1 << 9;
        }
        if (type instanceof VoidType) {
            return 1 << 12;
        }

        if (type instanceof IntType) {
            int rslt = 1 << 3;
            if (!ignoreRangesInt) {
                IntType itype = (IntType)type;
                rslt ^= getLowerBound(itype) + 13;
                rslt ^= getUpperBound(itype) + 27;
            }
            return rslt;
        }

        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            int rslt = (1 << 15) + hashType(ltype.getElementType(), ignoreRangesInt, ignoreRangesList);
            if (!ignoreRangesList) {
                rslt ^= getLowerBound(ltype) + 13;
                rslt ^= getUpperBound(ltype) + 27;
            }
            return rslt;
        }

        if (type instanceof SetType) {
            return (1 << 18) + hashType(((SetType)type).getElementType(), ignoreRangesInt, ignoreRangesList);
        }

        if (type instanceof DictType) {
            return (1 << 21) + hashType(((DictType)type).getKeyType(), ignoreRangesInt, ignoreRangesList)
                    + hashType(((DictType)type).getValueType(), ignoreRangesInt, ignoreRangesList);
        }

        if (type instanceof TupleType) {
            int rslt = 1 << 24;
            for (Field field: ((TupleType)type).getFields()) {
                rslt += hashType(field.getType(), ignoreRangesInt, ignoreRangesList);
            }
            return rslt;
        }

        if (type instanceof DistType) {
            return (1 << 27) + hashType(((DistType)type).getSampleType(), ignoreRangesInt, ignoreRangesList);
        }

        if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            int rslt = 1 << 30 + hashType(ftype.getReturnType(), ignoreRangesInt, ignoreRangesList);
            for (CifType ptype: ftype.getParamTypes()) {
                rslt += hashType(ptype, ignoreRangesInt, ignoreRangesList);
            }
            return rslt;
        }

        if (type instanceof TypeRef) {
            return hashType(((TypeRef)type).getType().getType(), ignoreRangesInt, ignoreRangesList);
        }

        if (type instanceof EnumType) {
            return getLiteralNames(((EnumType)type).getEnum()).hashCode();
        }

        if (type instanceof CompParamWrapType) {
            return hashType(((CompParamWrapType)type).getReference(), ignoreRangesInt, ignoreRangesList);
        }

        if (type instanceof CompInstWrapType) {
            return hashType(((CompInstWrapType)type).getReference(), ignoreRangesInt, ignoreRangesList);
        }

        throw new RuntimeException("Unexpected type: " + type);
    }

    /**
     * Does the given type support value equality?
     *
     * <p>
     * Note that:
     * <ul>
     * <li>Reals support value equality, as checking for zero etc can be useful, although it should be used with extreme
     * care.</li>
     * <li>Function types and distribution types don't support value equality.</li>
     * <li>Components and component definitions are not supposed to be used as values, and as such, their types don't
     * support value equality.</li>
     * </ul>
     * </p>
     *
     * @param type The CIF type to check.
     * @return {@code true} if the CIF type supports value equality, {@code false} otherwise.
     */
    public static boolean supportsValueEquality(CifType type) {
        if (type instanceof BoolType) {
            return true;
        }
        if (type instanceof IntType) {
            return true;
        }

        if (type instanceof TypeRef) {
            return supportsValueEquality(((TypeRef)type).getType().getType());
        }

        if (type instanceof EnumType) {
            return true;
        }

        if (type instanceof RealType) {
            // It is supported, as there may be valid use cases for this, but
            // comparing reals for equality is dangerous in general, as we use
            // numeric representations for them (Java doubles).
            return true;
        }

        if (type instanceof StringType) {
            return true;
        }

        if (type instanceof ListType) {
            return supportsValueEquality(((ListType)type).getElementType());
        }

        if (type instanceof SetType) {
            return supportsValueEquality(((SetType)type).getElementType());
        }

        if (type instanceof FuncType) {
            return false;
        }

        if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            return supportsValueEquality(dtype.getKeyType()) && supportsValueEquality(dtype.getValueType());
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            for (Field field: ttype.getFields()) {
                if (!supportsValueEquality(field.getType())) {
                    return false;
                }
            }
            return true;
        }

        if (type instanceof DistType) {
            return false;
        }

        if (type instanceof ComponentDefType) {
            // Components and component definitions are not intended as values,
            // and as such don't support value equality.
            return false;
        }

        if (type instanceof ComponentType) {
            // Components and component definitions are not intended as values,
            // and as such don't support value equality.
            return false;
        }

        if (type instanceof CompInstWrapType) {
            // Just peel of the wrapper. For value equality, we don't care
            // about how we reach a type, but only what type we reach.
            CompInstWrapType wrapper = (CompInstWrapType)type;
            return supportsValueEquality(wrapper.getReference());
        }

        if (type instanceof CompParamWrapType) {
            // Just peel of the wrapper. For value equality, we don't care
            // about how we reach a type, but only what type we reach.
            CompParamWrapType wrapper = (CompParamWrapType)type;
            return supportsValueEquality(wrapper.getReference());
        }

        if (type instanceof VoidType) {
            // The 'void' type has no values.
            return false;
        }

        throw new RuntimeException("Unknown type: " + type);
    }

    /**
     * Does the given type have a component like (component or component definition) type, either directly, or
     * indirectly?
     *
     * @param type The CIF type to check.
     * @return {@code true} if the CIF type has a component like type, {@code false} otherwise.
     */
    public static boolean hasComponentLikeType(CifType type) {
        if (type instanceof BoolType) {
            return false;
        }
        if (type instanceof IntType) {
            return false;
        }

        if (type instanceof TypeRef) {
            return hasComponentLikeType(((TypeRef)type).getType().getType());
        }

        if (type instanceof EnumType) {
            return false;
        }
        if (type instanceof RealType) {
            return false;
        }
        if (type instanceof StringType) {
            return false;
        }

        if (type instanceof ListType) {
            return hasComponentLikeType(((ListType)type).getElementType());
        }

        if (type instanceof SetType) {
            return hasComponentLikeType(((SetType)type).getElementType());
        }

        if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            if (hasComponentLikeType(ftype.getReturnType())) {
                return true;
            }
            for (CifType paramType: ftype.getParamTypes()) {
                if (hasComponentLikeType(paramType)) {
                    return true;
                }
            }
            return false;
        }

        if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            return hasComponentLikeType(dtype.getKeyType()) || hasComponentLikeType(dtype.getValueType());
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            for (Field field: ttype.getFields()) {
                if (hasComponentLikeType(field.getType())) {
                    return true;
                }
            }
            return false;
        }

        if (type instanceof DistType) {
            return false;
        }

        if (type instanceof ComponentDefType) {
            return true;
        }
        if (type instanceof ComponentType) {
            return true;
        }

        if (type instanceof CompInstWrapType) {
            // Just peel of the wrapper. We don't care about how we reach a
            // type, but only what type we reach.
            CompInstWrapType wrapper = (CompInstWrapType)type;
            return hasComponentLikeType(wrapper.getReference());
        }

        if (type instanceof CompParamWrapType) {
            // Just peel of the wrapper. We don't care about how we reach a
            // type, but only what type we reach.
            CompParamWrapType wrapper = (CompParamWrapType)type;
            return hasComponentLikeType(wrapper.getReference());
        }

        if (type instanceof VoidType) {
            return false;
        }

        throw new RuntimeException("Unknown type: " + type);
    }

    /**
     * Does the given type have a function type, either directly, or indirectly?
     *
     * @param type The CIF type to check.
     * @return {@code true} if the CIF type has a function type, {@code false} otherwise.
     */
    public static boolean hasFunctionType(CifType type) {
        if (type instanceof BoolType) {
            return false;
        }
        if (type instanceof IntType) {
            return false;
        }

        if (type instanceof TypeRef) {
            return hasFunctionType(((TypeRef)type).getType().getType());
        }

        if (type instanceof EnumType) {
            return false;
        }
        if (type instanceof RealType) {
            return false;
        }
        if (type instanceof StringType) {
            return false;
        }

        if (type instanceof ListType) {
            return hasFunctionType(((ListType)type).getElementType());
        }

        if (type instanceof SetType) {
            return hasFunctionType(((SetType)type).getElementType());
        }

        if (type instanceof FuncType) {
            return true;
        }

        if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            return hasFunctionType(dtype.getKeyType()) || hasFunctionType(dtype.getValueType());
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            for (Field field: ttype.getFields()) {
                if (hasFunctionType(field.getType())) {
                    return true;
                }
            }
            return false;
        }

        if (type instanceof DistType) {
            return false;
        }

        if (type instanceof ComponentDefType) {
            return false;
        }
        if (type instanceof ComponentType) {
            return false;
        }

        if (type instanceof CompInstWrapType) {
            // Just peel of the wrapper. We don't care about how we reach a
            // type, but only what type we reach.
            CompInstWrapType wrapper = (CompInstWrapType)type;
            return hasFunctionType(wrapper.getReference());
        }

        if (type instanceof CompParamWrapType) {
            // Just peel of the wrapper. We don't care about how we reach a
            // type, but only what type we reach.
            CompParamWrapType wrapper = (CompParamWrapType)type;
            return hasFunctionType(wrapper.getReference());
        }

        if (type instanceof VoidType) {
            return false;
        }

        throw new RuntimeException("Unknown type: " + type);
    }

    /**
     * Does the given type have a distribution type, either directly, or indirectly?
     *
     * @param type The CIF type to check.
     * @return {@code true} if the CIF type has a distribution type, {@code false} otherwise.
     */
    public static boolean hasDistType(CifType type) {
        if (type instanceof BoolType) {
            return false;
        }
        if (type instanceof IntType) {
            return false;
        }

        if (type instanceof TypeRef) {
            return hasDistType(((TypeRef)type).getType().getType());
        }

        if (type instanceof EnumType) {
            return false;
        }
        if (type instanceof RealType) {
            return false;
        }
        if (type instanceof StringType) {
            return false;
        }

        if (type instanceof ListType) {
            return hasDistType(((ListType)type).getElementType());
        }

        if (type instanceof SetType) {
            return hasDistType(((SetType)type).getElementType());
        }

        if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            if (hasDistType(ftype.getReturnType())) {
                return true;
            }
            for (CifType paramType: ftype.getParamTypes()) {
                if (hasDistType(paramType)) {
                    return true;
                }
            }
            return false;
        }

        if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            return hasDistType(dtype.getKeyType()) || hasDistType(dtype.getValueType());
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            for (Field field: ttype.getFields()) {
                if (hasDistType(field.getType())) {
                    return true;
                }
            }
            return false;
        }

        if (type instanceof DistType) {
            return true;
        }

        if (type instanceof ComponentDefType) {
            return false;
        }
        if (type instanceof ComponentType) {
            return false;
        }

        if (type instanceof CompInstWrapType) {
            // Just peel of the wrapper. We don't care about how we reach a
            // type, but only what type we reach.
            CompInstWrapType wrapper = (CompInstWrapType)type;
            return hasDistType(wrapper.getReference());
        }

        if (type instanceof CompParamWrapType) {
            // Just peel of the wrapper. We don't care about how we reach a
            // type, but only what type we reach.
            CompParamWrapType wrapper = (CompParamWrapType)type;
            return hasDistType(wrapper.getReference());
        }

        if (type instanceof VoidType) {
            return false;
        }

        throw new RuntimeException("Unknown type: " + type);
    }

    /**
     * Merges two compatible types to get the union of both types. Note that:
     * <ul>
     * <li>The resulting types are either clones of (parts of) the input types, or they are freshly constructed (with
     * optional position information).</li>
     * <li>The fields of tuple types may have no names.</li>
     * <li>The two types must be contained in the same scope.</li>
     * <li>Component types and component definition types can not be merged.</li>
     * </ul>
     *
     * @param type1 The first type.
     * @param type2 The second type.
     * @param position The position used for newly created types. The position itself is not used, only clones are used.
     *     May be {@code null} to not set position information.
     * @return The merged type, which is the union of both types.
     */
    public static CifType mergeTypes(CifType type1, CifType type2, Position position) {
        // Normalize both types first, to get rid of top level type references
        // and wrapping types.
        CifType origType1 = type1;
        type1 = normalizeType(type1);
        type2 = normalizeType(type2);

        // Merge based on the mutual types.
        if (type1 instanceof BoolType && type2 instanceof BoolType) {
            return newBoolType(copyPosition(position));
        }

        if (type1 instanceof IntType && type2 instanceof IntType) {
            IntType itype1 = (IntType)type1;
            IntType itype2 = (IntType)type2;

            IntType itype = newIntType();
            itype.setPosition(copyPosition(position));
            if (!isRangeless(itype1) && !isRangeless(itype2)) {
                itype.setLower(Math.min(itype1.getLower(), itype2.getLower()));
                itype.setUpper(Math.max(itype1.getUpper(), itype2.getUpper()));
            }
            return itype;
        }

        if (type1 instanceof EnumType && type2 instanceof EnumType) {
            EnumDecl enum1 = ((EnumType)type1).getEnum();
            EnumDecl enum2 = ((EnumType)type2).getEnum();
            Assert.check(areEnumsCompatible(enum1, enum2));

            // Clone the first type, to get a valid reference (with proper
            // wrappings).
            return deepclone(origType1);
        }

        if (type1 instanceof RealType && type2 instanceof RealType) {
            return newRealType(copyPosition(position));
        }

        if (type1 instanceof StringType && type2 instanceof StringType) {
            return newStringType(copyPosition(position));
        }

        if (type1 instanceof ListType && type2 instanceof ListType) {
            ListType ltype1 = (ListType)type1;
            ListType ltype2 = (ListType)type2;

            ListType ltype = newListType();
            ltype.setPosition(copyPosition(position));
            if (!isRangeless(ltype1) && !isRangeless(ltype2)) {
                ltype.setLower(Math.min(ltype1.getLower(), ltype2.getLower()));
                ltype.setUpper(Math.max(ltype1.getUpper(), ltype2.getUpper()));
            }

            CifType etype = mergeTypes(ltype1.getElementType(), ltype2.getElementType(), position);
            ltype.setElementType(etype);
            return ltype;
        }

        if (type1 instanceof SetType && type2 instanceof SetType) {
            CifType etype = mergeTypes(((SetType)type1).getElementType(), ((SetType)type2).getElementType(), position);
            SetType stype = newSetType();
            stype.setPosition(copyPosition(position));
            stype.setElementType(etype);
            return stype;
        }

        if (type1 instanceof FuncType && type2 instanceof FuncType) {
            FuncType ftype1 = (FuncType)type1;
            FuncType ftype2 = (FuncType)type2;

            CifType rtype = mergeTypes(ftype1.getReturnType(), ftype2.getReturnType(), position);

            int cnt1 = ftype1.getParamTypes().size();
            int cnt2 = ftype2.getParamTypes().size();
            Assert.check(cnt1 == cnt2);
            List<CifType> ptypes = listc(cnt1);
            for (int i = 0; i < cnt1; i++) {
                ptypes.add(mergeTypes(ftype1.getParamTypes().get(i), ftype2.getParamTypes().get(i), position));
            }

            FuncType ftype = newFuncType();
            ftype.setPosition(copyPosition(position));
            ftype.setReturnType(rtype);
            ftype.getParamTypes().addAll(ptypes);
            return ftype;
        }

        if (type1 instanceof DictType && type2 instanceof DictType) {
            DictType dtype1 = (DictType)type1;
            DictType dtype2 = (DictType)type2;

            CifType ktype = mergeTypes(dtype1.getKeyType(), dtype2.getKeyType(), position);
            CifType vtype = mergeTypes(dtype1.getValueType(), dtype2.getValueType(), position);

            DictType dtype = newDictType();
            dtype.setPosition(copyPosition(position));
            dtype.setKeyType(ktype);
            dtype.setValueType(vtype);
            return dtype;
        }

        if (type1 instanceof TupleType && type2 instanceof TupleType) {
            TupleType ttype1 = (TupleType)type1;
            TupleType ttype2 = (TupleType)type2;

            int cnt1 = ttype1.getFields().size();
            int cnt2 = ttype2.getFields().size();
            Assert.check(cnt1 == cnt2);
            List<Field> fields = listc(cnt1);
            for (int i = 0; i < cnt1; i++) {
                Field field1 = ttype1.getFields().get(i);
                Field field2 = ttype2.getFields().get(i);
                CifType ftype = mergeTypes(field1.getType(), field2.getType(), position);

                // Construct new nameless field.
                Field field = newField();
                field.setPosition(copyPosition(position));
                field.setType(ftype);
                fields.add(field);
            }

            TupleType ttype = newTupleType();
            ttype.setPosition(copyPosition(position));
            ttype.getFields().addAll(fields);
            return ttype;
        }

        if (type1 instanceof DistType && type2 instanceof DistType) {
            CifType stype = mergeTypes(((DistType)type1).getSampleType(), ((DistType)type2).getSampleType(), position);
            DistType dtype = newDistType();
            dtype.setPosition(copyPosition(position));
            dtype.setSampleType(stype);
            return dtype;
        }

        if (type1 instanceof VoidType && type2 instanceof VoidType) {
            return newVoidType(copyPosition(position));
        }

        String msg = "Unknown/unexpected types: " + type1 + ", " + type2;
        throw new RuntimeException(msg);
    }

    /**
     * Returns the lower bound of the integer type, or {@link Integer#MIN_VALUE} if it is implicit.
     *
     * @param type The integer type.
     * @return The lower bound of the integer type.
     */
    public static int getLowerBound(IntType type) {
        Integer rslt = type.getLower();
        return (rslt == null) ? Integer.MIN_VALUE : rslt;
    }

    /**
     * Returns the lower bound of the list type, or {@code 0} if it is implicit.
     *
     * @param type The list type.
     * @return The lower bound of the list type.
     */
    public static int getLowerBound(ListType type) {
        Integer rslt = type.getLower();
        return (rslt == null) ? 0 : rslt;
    }

    /**
     * Returns the upper bound of the integer type, or {@link Integer#MAX_VALUE} if it is implicit.
     *
     * @param type The integer type.
     * @return The upper bound of the integer type.
     */
    public static int getUpperBound(IntType type) {
        Integer rslt = type.getUpper();
        return (rslt == null) ? Integer.MAX_VALUE : rslt;
    }

    /**
     * Returns the upper bound of the list type, or {@link Integer#MAX_VALUE} if it is implicit.
     *
     * @param type The list type.
     * @return The upper bound of the list type.
     */
    public static int getUpperBound(ListType type) {
        Integer rslt = type.getUpper();
        return (rslt == null) ? Integer.MAX_VALUE : rslt;
    }

    /**
     * Is the given integer type a rangeless integer type?
     *
     * @param type The integer type.
     * @return {@code true} if the integer type is rangeless, {@code false} otherwise.
     * @see #containsRangedType
     */
    public static boolean isRangeless(IntType type) {
        return type.getLower() == null;
    }

    /**
     * Is the given list type a rangeless list type?
     *
     * @param type The list type.
     * @return {@code true} if the list type is rangeless, {@code false} otherwise.
     * @see #containsRangedType
     */
    public static boolean isRangeless(ListType type) {
        return type.getLower() == null;
    }

    /**
     * Is the given list type an array type, that is, is it a list with specified and equal lower and upper bounds on
     * its length?
     *
     * @param type The list type.
     * @return {@code true} if the list type is an array type, {@code false} otherwise.
     */
    public static boolean isArrayType(ListType type) {
        return type.getLower() != null && type.getUpper() != null && type.getLower().equals(type.getUpper());
    }

    /**
     * Does the given CIF type contain a type with a range?
     *
     * @param type The CIF type.
     * @return {@code true} if the CIF type contains a type with a range, {@code false} otherwise.
     * @see #isRangeless
     */
    public static boolean containsRangedType(CifType type) {
        if (type instanceof BoolType) {
            return false;
        }

        if (type instanceof IntType) {
            return !isRangeless((IntType)type);
        }

        if (type instanceof TypeRef) {
            return containsRangedType(((TypeRef)type).getType().getType());
        }

        if (type instanceof EnumType) {
            return false;
        }
        if (type instanceof RealType) {
            return false;
        }
        if (type instanceof StringType) {
            return false;
        }

        if (type instanceof ListType) {
            return !isRangeless((ListType)type) || containsRangedType(((ListType)type).getElementType());
        }

        if (type instanceof SetType) {
            return containsRangedType(((SetType)type).getElementType());
        }

        if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            if (containsRangedType(ftype.getReturnType())) {
                return true;
            }
            for (CifType paramType: ftype.getParamTypes()) {
                if (containsRangedType(paramType)) {
                    return true;
                }
            }
            return false;
        }

        if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            return containsRangedType(dtype.getKeyType()) || containsRangedType(dtype.getValueType());
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            for (Field field: ttype.getFields()) {
                if (containsRangedType(field.getType())) {
                    return true;
                }
            }
            return false;
        }

        if (type instanceof DistType) {
            return false;
        }

        if (type instanceof ComponentDefType) {
            return false;
        }
        if (type instanceof ComponentType) {
            return false;
        }

        if (type instanceof CompInstWrapType) {
            // Just peel of the wrapper. We don't care about how we reach a
            // type, but only what type we reach.
            CompInstWrapType wrapper = (CompInstWrapType)type;
            return containsRangedType(wrapper.getReference());
        }

        if (type instanceof CompParamWrapType) {
            // Just peel of the wrapper. We don't care about how we reach a
            // type, but only what type we reach.
            CompParamWrapType wrapper = (CompParamWrapType)type;
            return containsRangedType(wrapper.getReference());
        }

        if (type instanceof VoidType) {
            return false;
        }

        throw new RuntimeException("Unknown type: " + type);
    }

    /**
     * Normalizes a CIF type by recursively stripping off top level type references and wrapping types.
     *
     * <p>
     * Note that the resulting type may not be a valid reference in the containment context of the original type.
     * </p>
     *
     * <p>
     * This method is particularly useful when comparing the result to another type for type compatibility, or checking
     * the result for being an instance of a certain (non-wrapped) type.
     * </p>
     *
     * @param type The CIF type to normalize.
     * @return The normalized CIF type.
     */
    public static CifType normalizeType(CifType type) {
        if (type instanceof TypeRef) {
            return normalizeType(((TypeRef)type).getType().getType());
        }
        if (type instanceof CompParamWrapType) {
            return normalizeType(((CompParamWrapType)type).getReference());
        }
        if (type instanceof CompInstWrapType) {
            return normalizeType(((CompInstWrapType)type).getReference());
        }
        return type;
    }

    /**
     * Normalizes a CIF type by recursively stripping off type references and wrapping types, not just at the top level
     * as {@link #normalizeType}, but also fully recursive in for instance list types.
     *
     * <p>
     * Note that the resulting type may not be a valid reference in the containment context of the original type.
     * </p>
     *
     * <p>
     * This method is particularly useful when comparing the result to another type for type compatibility, or checking
     * the result for being an instance of a certain (non-wrapped) type.
     * </p>
     *
     * <p>
     * There is no guarantee that new/fresh types are created by this method. If fresh types are needed, make sure to
     * {@link EMFHelper#deepclone deepclone} the result.
     * </p>
     *
     * @param type The CIF type to normalize.
     * @return The normalized CIF type.
     */
    public static CifType normalizeTypeRecursive(CifType type) {
        if (type instanceof BoolType) {
            return type;
        }
        if (type instanceof IntType) {
            return type;
        }
        if (type instanceof EnumType) {
            return type;
        }
        if (type instanceof RealType) {
            return type;
        }
        if (type instanceof StringType) {
            return type;
        }

        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            CifType netype = normalizeTypeRecursive(ltype.getElementType());
            ListType rslt = newListType();
            rslt.setLower(ltype.getLower());
            rslt.setUpper(ltype.getUpper());
            rslt.setElementType(deepclone(netype));
            return rslt;
        }

        if (type instanceof SetType) {
            SetType stype = (SetType)type;
            CifType netype = normalizeTypeRecursive(stype.getElementType());
            SetType rslt = newSetType();
            rslt.setElementType(deepclone(netype));
            return rslt;
        }

        if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            CifType nrtype = normalizeTypeRecursive(ftype.getReturnType());
            List<CifType> nptypes = listc(ftype.getParamTypes().size());
            for (CifType ptype: ftype.getParamTypes()) {
                nptypes.add(deepclone(normalizeTypeRecursive(ptype)));
            }
            FuncType rslt = newFuncType();
            rslt.setReturnType(deepclone(nrtype));
            rslt.getParamTypes().addAll(nptypes);
            return rslt;
        }

        if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            CifType nktype = normalizeTypeRecursive(dtype.getKeyType());
            CifType nvtype = normalizeTypeRecursive(dtype.getValueType());
            DictType rslt = newDictType();
            rslt.setKeyType(deepclone(nktype));
            rslt.setValueType(deepclone(nvtype));
            return rslt;
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            List<Field> nfields = listc(ttype.getFields().size());
            for (Field field: ttype.getFields()) {
                Field nfield = newField();
                nfield.setName(field.getName());
                CifType ftype = normalizeTypeRecursive(field.getType());
                nfield.setType(deepclone(ftype));
                nfields.add(nfield);
            }
            TupleType rslt = newTupleType();
            rslt.getFields().addAll(nfields);
            return rslt;
        }

        if (type instanceof DistType) {
            DistType dtype = (DistType)type;
            CifType nstype = normalizeTypeRecursive(dtype.getSampleType());
            DistType rslt = newDistType();
            rslt.setSampleType(deepclone(nstype));
            return rslt;
        }

        if (type instanceof ComponentType) {
            return type;
        }
        if (type instanceof ComponentDefType) {
            return type;
        }

        if (type instanceof TypeRef) {
            return normalizeTypeRecursive(((TypeRef)type).getType().getType());
        }

        if (type instanceof CompInstWrapType) {
            CompInstWrapType wrapper = (CompInstWrapType)type;
            return normalizeTypeRecursive(wrapper.getReference());
        }

        if (type instanceof CompParamWrapType) {
            CompParamWrapType wrapper = (CompParamWrapType)type;
            return normalizeTypeRecursive(wrapper.getReference());
        }

        if (type instanceof VoidType) {
            return type;
        }

        throw new RuntimeException("Unknown type: " + type);
    }

    /**
     * Returns the given CIF type, but with all top level wrapping types unwrapped (peeled of).
     *
     * @param type The CIF type to unwrap.
     * @return The unwrapped CIF type.
     */
    public static CifType unwrapType(CifType type) {
        if (type instanceof CompParamWrapType) {
            return unwrapType(((CompParamWrapType)type).getReference());
        }
        if (type instanceof CompInstWrapType) {
            return unwrapType(((CompInstWrapType)type).getReference());
        }
        return type;
    }

    /**
     * Returns the given expression, but with all top level wrapping expressions unwrapped (peeled of).
     *
     * @param expr The expression to unwrap.
     * @return The unwrapped expression.
     */
    public static Expression unwrapExpression(Expression expr) {
        if (expr instanceof CompParamWrapExpression) {
            return unwrapExpression(((CompParamWrapExpression)expr).getReference());
        }
        if (expr instanceof CompInstWrapExpression) {
            return unwrapExpression(((CompInstWrapExpression)expr).getReference());
        }
        return expr;
    }

    /**
     * Returns the component definition for a given component instantiation. Silently removes wrapping types.
     *
     * @param inst The component instantiation.
     * @return The component definition.
     */
    public static ComponentDef getCompDefFromCompInst(ComponentInst inst) {
        CifType type = inst.getDefinition();
        type = CifTypeUtils.unwrapType(type);
        Assert.check(type instanceof ComponentDefType);
        return ((ComponentDefType)type).getDefinition();
    }

    /**
     * Returns a value indicating whether the given expression refers to an automaton.
     *
     * <p>
     * {@link SelfExpression} instances as well as {@link ComponentExpression} and {@link CompParamExpression} instances
     * that refer to an automaton, are considered automaton references. Wrapping expression are supported.
     * </p>
     *
     * @param expr The expression.
     * @return {@code true} if the expression refers to an automaton, {@code false} otherwise.
     */
    public static boolean isAutRefExpr(Expression expr) {
        // 'self' (can't be wrapped).
        if (expr instanceof SelfExpression) {
            return true;
        }

        // Automaton reference (possibly wrapped, and via instantiations).
        expr = unwrapExpression(expr);
        if (expr instanceof ComponentExpression) {
            Component comp = ((ComponentExpression)expr).getComponent();
            return CifScopeUtils.isAutomaton(comp);
        }

        // Component parameter expression of type automaton definition.
        if (expr instanceof CompParamExpression) {
            CompParamExpression compParamExpr = (CompParamExpression)expr;

            // Component parameter expression must have a component definition type.
            CifType t = CifTypeUtils.normalizeType(compParamExpr.getType());
            Assert.check(t instanceof ComponentDefType);
            ComponentDef cdef = ((ComponentDefType)t).getDefinition();

            // The component definition can be for an automaton or for a group.
            return CifScopeUtils.isAutomaton(cdef.getBody());
        }

        // Expression does not refer to an automaton.
        return false;
    }

    /**
     * Returns a value indicating whether the given expression is a reference expression.
     *
     * <p>
     * Note that {@link TauExpression} is not considered a reference expression, as it does not refer to an object
     * declared in the specification. Similarly, {@link TimeExpression} and {@link StdLibFunctionExpression} are not
     * considered reference expressions.
     * </p>
     *
     * <p>
     * Note that {@link FieldExpression} is not considered a reference expression, as it has special scoping rules, and
     * callers should handle it as a special case.
     * </p>
     *
     * <p>
     * Note that {@link SelfExpression} is considered a reference expression.
     * </p>
     *
     * @param expr The expression.
     * @return {@code true} if the expression is a reference expression, and {@code false} otherwise.
     */
    public static boolean isRefExpr(Expression expr) {
        return expr instanceof ConstantExpression || expr instanceof DiscVariableExpression
                || expr instanceof AlgVariableExpression || expr instanceof ContVariableExpression
                || expr instanceof LocationExpression || expr instanceof EventExpression
                || expr instanceof EnumLiteralExpression || expr instanceof FunctionExpression
                || expr instanceof InputVariableExpression || expr instanceof ComponentExpression
                || expr instanceof CompParamExpression || expr instanceof CompParamWrapExpression
                || expr instanceof CompInstWrapExpression || expr instanceof SelfExpression;
    }

    /**
     * Returns a value indicating whether the given type is a reference type. Wrapping types are considered reference
     * types, as they refer to 'via objects'.
     *
     * @param type The type.
     * @return {@code true} if the type is a reference type, and {@code false} otherwise.
     */
    public static boolean isRefType(CifType type) {
        return type instanceof TypeRef || type instanceof EnumType || type instanceof ComponentType
                || type instanceof ComponentDefType || type instanceof CompParamWrapType
                || type instanceof CompInstWrapType;
    }

    /**
     * Returns a value indicating whether the given type is a container type.
     *
     * <p>
     * The type is first normalized. Then only the top level type is checked.
     * </p>
     *
     * @param type The type.
     * @return {@code true} if the type is a container type, and {@code false} otherwise.
     */
    public static boolean isContainerType(CifType type) {
        type = normalizeType(type);
        return type instanceof DictType || type instanceof ListType || type instanceof SetType
                || type instanceof TupleType;
    }

    /**
     * Returns a value indicating whether the given standard library function is a 'distribution function'. A
     * 'distribution function' is a function that results in a distribution. That is, it is a function that can be used
     * to construct a new distribution.
     *
     * @param func The standard library function.
     * @return {@code true} if the standard library function is a 'distribution function', {@code false} otherwise.
     */
    public static boolean isDistFunction(StdLibFunction func) {
        switch (func) {
            case ACOSH:
            case ACOS:
            case ASINH:
            case ASIN:
            case ATANH:
            case ATAN:
            case COSH:
            case COS:
            case SINH:
            case SIN:
            case TANH:
            case TAN:
            case ABS:
            case CBRT:
            case CEIL:
            case DELETE:
            case EMPTY:
            case EXP:
            case FLOOR:
            case FORMAT:
            case LN:
            case LOG:
            case MAXIMUM:
            case MINIMUM:
            case POP:
            case POWER:
            case ROUND:
            case SCALE:
            case SIGN:
            case SIZE:
            case SQRT:
                return false;

            case BERNOULLI:
            case BETA:
            case BINOMIAL:
            case CONSTANT:
            case ERLANG:
            case EXPONENTIAL:
            case GAMMA:
            case GEOMETRIC:
            case LOG_NORMAL:
            case NORMAL:
            case POISSON:
            case RANDOM:
            case TRIANGLE:
            case UNIFORM:
            case WEIBULL:
                return true;
        }
        throw new RuntimeException("Unknown stdlib func: " + func);
    }

    /**
     * Are the two types structurally the same?
     *
     * @param type1 The first type to check.
     * @param type2 The second type to check.
     * @return {@code true} if the two types are structurally the same, {@code false} otherwise.
     */
    public static Boolean areStructurallySameType(CifType type1, CifType type2) {
        if (!type1.getClass().equals(type2.getClass())) {
            return false;
        }

        if (type1 instanceof BoolType && type2 instanceof BoolType) {
            return true;
        }

        if (type1 instanceof IntType itype1 && type2 instanceof IntType itype2) {
            if ((itype1.getLower() == null) != (itype2.getLower() == null)) {
                return false;
            }
            if (itype1.getLower() != null && !itype1.getLower().equals(itype2.getLower())) {
                return false;
            }

            if ((itype1.getUpper() == null) != (itype2.getUpper() == null)) {
                return false;
            }
            if (itype1.getUpper() != null && !itype1.getUpper().equals(itype2.getUpper())) {
                return false;
            }

            return true;
        }

        if (type1 instanceof TypeRef typeRef1 && type2 instanceof TypeRef typeRef2) {
            return typeRef1.getType().equals(typeRef2.getType());
        }

        if (type1 instanceof EnumType etype1 && type2 instanceof EnumType etype2) {
            return etype1.getEnum().equals(etype2.getEnum());
        }

        if (type1 instanceof ListType ltype1 && type2 instanceof ListType ltype2) {
            if ((ltype1.getLower() == null) != (ltype2.getLower() == null)) {
                return false;
            }
            if (ltype1.getLower() != null && !ltype1.getLower().equals(ltype2.getLower())) {
                return false;
            }

            if ((ltype1.getUpper() == null) != (ltype2.getUpper() == null)) {
                return false;
            }
            if (ltype1.getUpper() != null && !ltype1.getUpper().equals(ltype2.getUpper())) {
                return false;
            }

            return areStructurallySameType(ltype1.getElementType(), ltype2.getElementType());
        }

        if (type1 instanceof StringType && type2 instanceof StringType) {
            return true;
        }

        if (type1 instanceof RealType && type2 instanceof RealType) {
            return true;
        }

        if (type1 instanceof SetType stype1 && type2 instanceof SetType stype2) {
            return areStructurallySameType(stype1.getElementType(), stype2.getElementType());
        }

        if (type1 instanceof FuncType ftype1 && type2 instanceof FuncType ftype2) {
            if (ftype1.getParamTypes().size() != ftype1.getParamTypes().size()) {
                return false;
            }

            // Check parameter types.
            for (int i = 0; i < ftype1.getParamTypes().size(); i++) {
                if (!areStructurallySameType(ftype1.getParamTypes().get(i), ftype2.getParamTypes().get(i))) {
                    return false;
                }
            }

            // Check return type.
            return areStructurallySameType(ftype1.getReturnType(), ftype2.getReturnType());
        }

        if (type1 instanceof VoidType && type2 instanceof VoidType) {
            return true;
        }

        if (type1 instanceof DictType dtype1 && type2 instanceof DictType dtype2) {
            return areStructurallySameType(dtype1.getKeyType(), dtype2.getKeyType())
                    && areStructurallySameType(dtype1.getValueType(), dtype2.getValueType());
        }

        if (type1 instanceof TupleType ttype1 && type2 instanceof TupleType ttype2) {
            if (ttype1.getFields().size() != ttype2.getFields().size()) {
                return false;
            }

            // Check fields.
            for (int i = 0; i < ttype1.getFields().size(); i++) {
                Field field1 = ttype1.getFields().get(i);
                Field field2 = ttype2.getFields().get(i);
                if (!field1.getName().equals(field2.getName())) {
                    return false;
                }

                if (!areStructurallySameType(field1.getType(), field2.getType())) {
                    return false;
                }
            }
        }

        if (type1 instanceof DistType dtype1 && type2 instanceof DistType dtype2) {
            return areStructurallySameType(dtype1.getSampleType(), dtype2.getSampleType());
        }

        if (type1 instanceof ComponentDefType cdtype1 && type2 instanceof ComponentDefType cdtype2) {
            return cdtype1.getDefinition().equals(cdtype2.getDefinition());
        }

        if (type1 instanceof ComponentType ctype1 && type2 instanceof ComponentType ctype2) {
            return ctype1.getComponent().equals(ctype2.getComponent());
        }

        if (type1 instanceof CompInstWrapType ciwtype1 && type2 instanceof CompInstWrapType ciwtype2) {
            return ciwtype1.getInstantiation().equals(ciwtype2.getInstantiation())
                    && areStructurallySameType(ciwtype1.getReference(), ciwtype2.getReference());
        }

        if (type1 instanceof CompParamWrapType cpwType1 && type2 instanceof CompParamWrapType cpwType2) {
            return cpwType1.getParameter().equals(cpwType2.getParameter())
                    && areStructurallySameType(cpwType1.getReference(), cpwType2.getReference());
        }

        throw new RuntimeException("Unexpected types: " + type1 + ", " + type2);
    }

    /**
     * Creates a tuple type for the given field types, if needed.
     *
     * <p>
     * The field types are not deep cloned, so their containment may change as a result of using this method.
     * </p>
     *
     * <p>
     * The newly created tuple type fields do not have a name.
     * </p>
     *
     * @param fieldTypes The field types. Must have at least one element.
     * @param position The position used for newly created types. The position itself is not used, only clones are used.
     *     May be {@code null} to not set position information.
     * @return If there is only one element, that element, or a tuple with the given elements otherwise.
     */
    public static CifType makeTupleType(List<CifType> fieldTypes, Position position) {
        Assert.check(!fieldTypes.isEmpty());

        if (fieldTypes.size() == 1) {
            return first(fieldTypes);
        }

        TupleType tupleType = newTupleType();
        tupleType.setPosition(copyPosition(position));
        for (CifType fieldType: fieldTypes) {
            Field field = newField();
            field.setPosition(copyPosition(position));
            field.setType(deepclone(fieldType));
            tupleType.getFields().add(field);
        }
        return tupleType;
    }

    /**
     * Creates a CIF type (possibly a tuple type) for the given values.
     *
     * <p>
     * The types of the values are deep cloned, so that their containment does not change.
     * </p>
     *
     * <p>
     * The newly created tuple type fields do not have a name.
     * </p>
     *
     * @param values The values. Must have at least one value.
     * @param position The position used for newly created types. The position itself is not used, only clones are used.
     *     May be {@code null} to not set position information.
     * @return If there is only one element, that element, or a tuple with the given elements otherwise.
     */
    public static CifType makeTupleTypeFromValues(List<Expression> values, Position position) {
        Assert.check(!values.isEmpty());

        if (values.size() == 1) {
            return deepclone(first(values).getType());
        }

        TupleType tupleType = newTupleType();
        tupleType.setPosition(copyPosition(position));
        for (Expression value: values) {
            Field field = newField();
            field.setPosition(copyPosition(position));
            field.setType(deepclone(value.getType()));
            tupleType.getFields().add(field);
        }
        return tupleType;
    }

    /**
     * Creates a {@link FuncType} for the given function. All types are deep cloned to ensure they remain in the
     * parameters and return types of the given function.
     *
     * @param func The function.
     * @param position The position used for newly created types. The position itself is not used, only clones are used.
     *     May be {@code null} to not set position information.
     * @return The type of the function.
     */
    public static FuncType makeFunctionType(Function func, Position position) {
        FuncType type = newFuncType();
        type.setPosition(copyPosition(position));
        for (FunctionParameter param: func.getParameters()) {
            CifType paramType = param.getParameter().getType();
            type.getParamTypes().add(deepclone(paramType));
        }
        type.setReturnType(makeTupleType(deepclone(func.getReturnTypes()), position));
        return type;
    }

    /**
     * Get the type of a variable-like declaration that holds value.
     *
     * <p>
     * <ul>
     * The following declarations are supported:
     * <li>{@link AlgVariable}</li>
     * <li>{@link Constant}</li>
     * <li>{@link ContVariable}</li>
     * <li>{@link DiscVariable}</li>
     * <li>{@link InputVariable}</li>
     * </ul>
     * </p>
     *
     * @param decl Declaration to inspect.
     * @return The type of the variable.
     */
    public static CifType getVariableType(Declaration decl) {
        if (decl instanceof AlgVariable) {
            AlgVariable algVar = (AlgVariable)decl;
            return algVar.getType();
        }
        if (decl instanceof Constant) {
            Constant constant = (Constant)decl;
            return constant.getType();
        }
        if (decl instanceof ContVariable) {
            return newRealType();
        }
        if (decl instanceof DiscVariable) {
            DiscVariable discVar = (DiscVariable)decl;
            return discVar.getType();
        }
        if (decl instanceof InputVariable) {
            InputVariable inputVar = (InputVariable)decl;
            return inputVar.getType();
        }
        throw new RuntimeException("Unknown variable: " + decl);
    }
}
