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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;
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
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.INT_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.checkStaticEvaluable;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.cif.parser.ast.tokens.AIdentifier;
import org.eclipse.escet.cif.parser.ast.types.ABoolType;
import org.eclipse.escet.cif.parser.ast.types.ACifType;
import org.eclipse.escet.cif.parser.ast.types.ADictType;
import org.eclipse.escet.cif.parser.ast.types.ADistType;
import org.eclipse.escet.cif.parser.ast.types.AField;
import org.eclipse.escet.cif.parser.ast.types.AFuncType;
import org.eclipse.escet.cif.parser.ast.types.AIntType;
import org.eclipse.escet.cif.parser.ast.types.AListType;
import org.eclipse.escet.cif.parser.ast.types.ANamedType;
import org.eclipse.escet.cif.parser.ast.types.ARealType;
import org.eclipse.escet.cif.parser.ast.types.ASetType;
import org.eclipse.escet.cif.parser.ast.types.AStringType;
import org.eclipse.escet.cif.parser.ast.types.ATupleType;
import org.eclipse.escet.cif.parser.ast.types.AVoidType;
import org.eclipse.escet.cif.typechecker.declwrap.EnumDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.TypeDeclWrap;
import org.eclipse.escet.cif.typechecker.scopes.AutDefScope;
import org.eclipse.escet.cif.typechecker.scopes.GroupDefScope;
import org.eclipse.escet.cif.typechecker.scopes.SymbolScope;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Type checker for CIF types. */
public class CifTypesTypeChecker {
    /** Constructor for the {@link CifTypesTypeChecker} class. */
    private CifTypesTypeChecker() {
        // Static class.
    }

    /**
     * Transforms a type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    public static CifType transCifType(ACifType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        if (type instanceof ABoolType) {
            BoolType rslt = newBoolType();
            rslt.setPosition(type.position);
            return rslt;
        } else if (type instanceof AIntType) {
            return transIntType((AIntType)type, scope, tchecker);
        } else if (type instanceof ARealType) {
            RealType rslt = newRealType();
            rslt.setPosition(type.position);
            return rslt;
        } else if (type instanceof AStringType) {
            StringType rslt = newStringType();
            rslt.setPosition(type.position);
            return rslt;
        } else if (type instanceof AListType) {
            return transListType((AListType)type, scope, tchecker);
        } else if (type instanceof ASetType) {
            return transSetType((ASetType)type, scope, tchecker);
        } else if (type instanceof AFuncType) {
            return transFuncType((AFuncType)type, scope, tchecker);
        } else if (type instanceof ADictType) {
            return transDictType((ADictType)type, scope, tchecker);
        } else if (type instanceof ATupleType) {
            return transTupleType((ATupleType)type, scope, tchecker);
        } else if (type instanceof ADistType) {
            return transDistType((ADistType)type, scope, tchecker);
        } else if (type instanceof ANamedType) {
            return transNamedType((ANamedType)type, scope, tchecker);
        } else if (type instanceof AVoidType) {
            VoidType rslt = newVoidType();
            rslt.setPosition(type.position);
            return rslt;
        } else {
            throw new RuntimeException("Unknown type: " + type);
        }
    }

    /**
     * Transforms an integer type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    private static IntType transIntType(AIntType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Special case for integer types without a range.
        if (type.range == null) {
            IntType rslt = newIntType();
            rslt.setPosition(type.position);
            return rslt;
        }

        // Normal case for integer types with a range.
        Expression lowerExpr = transExpression(type.range.lower, INT_TYPE_HINT, scope, null, tchecker);
        Expression upperExpr = transExpression(type.range.upper, INT_TYPE_HINT, scope, null, tchecker);

        CifType lowerType = CifTypeUtils.normalizeType(lowerExpr.getType());
        CifType upperType = CifTypeUtils.normalizeType(upperExpr.getType());

        if (!(lowerType instanceof IntType)) {
            tchecker.addProblem(ErrMsg.TYPE_RANGE_BOUND_NON_INT, lowerExpr.getPosition(), "lower", "an integer");
            throw new SemanticException();
        }
        if (!(upperType instanceof IntType)) {
            tchecker.addProblem(ErrMsg.TYPE_RANGE_BOUND_NON_INT, upperExpr.getPosition(), "upper", "an integer");
            throw new SemanticException();
        }

        checkStaticEvaluable(lowerExpr, tchecker);
        checkStaticEvaluable(upperExpr, tchecker);

        int lower;
        int upper;
        try {
            lower = (Integer)CifEvalUtils.eval(lowerExpr, false);
        } catch (CifEvalException e) {
            tchecker.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
            throw new SemanticException();
        }

        try {
            upper = (Integer)CifEvalUtils.eval(upperExpr, false);
        } catch (CifEvalException e) {
            tchecker.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
            throw new SemanticException();
        }

        if (lower > upper) {
            tchecker.addProblem(ErrMsg.EMPTY_TYPE_RANGE, type.position, "integer", Numbers.formatNumber(lower),
                    Numbers.formatNumber(upper));
            throw new SemanticException();
        }

        IntType rslt = newIntType();
        rslt.setPosition(type.position);
        rslt.setLower(lower);
        rslt.setUpper(upper);
        return rslt;
    }

    /**
     * Transforms a list type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    private static ListType transListType(AListType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Create metamodel type.
        ListType rslt = newListType();
        rslt.setPosition(type.position);

        // Check range.
        if (type.range != null) {
            // Lower.
            Expression lowerExpr = transExpression(type.range.lower, INT_TYPE_HINT, scope, null, tchecker);
            CifType lowerType = CifTypeUtils.normalizeType(lowerExpr.getType());

            if (!(lowerType instanceof IntType)) {
                tchecker.addProblem(ErrMsg.TYPE_RANGE_BOUND_NON_INT, lowerExpr.getPosition(), "lower", "a list");
                throw new SemanticException();
            }

            checkStaticEvaluable(lowerExpr, tchecker);

            int lower;
            try {
                lower = (Integer)CifEvalUtils.eval(lowerExpr, false);
            } catch (CifEvalException e) {
                tchecker.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
                throw new SemanticException();
            }

            if (lower < 0) {
                tchecker.addProblem(ErrMsg.TYPE_RANGE_BOUND_NEG, lowerExpr.getPosition(), "lower",
                        Numbers.formatNumber(lower), "a list");
                throw new SemanticException();
            }

            // Upper.
            boolean hasUpper = false;
            int upper = 0;
            if (type.range.upper != null) {
                hasUpper = true;

                Expression upperExpr = transExpression(type.range.upper, INT_TYPE_HINT, scope, null, tchecker);
                CifType upperType = CifTypeUtils.normalizeType(upperExpr.getType());

                if (!(upperType instanceof IntType)) {
                    tchecker.addProblem(ErrMsg.TYPE_RANGE_BOUND_NON_INT, upperExpr.getPosition(), "upper", "a list");
                    throw new SemanticException();
                }

                checkStaticEvaluable(upperExpr, tchecker);

                try {
                    upper = (Integer)CifEvalUtils.eval(upperExpr, false);
                } catch (CifEvalException e) {
                    tchecker.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
                    throw new SemanticException();
                }

                if (upper < 0) {
                    tchecker.addProblem(ErrMsg.TYPE_RANGE_BOUND_NEG, upperExpr.getPosition(), "upper",
                            Numbers.formatNumber(upper), "a list");
                    throw new SemanticException();
                }
            }

            if (hasUpper && lower > upper) {
                tchecker.addProblem(ErrMsg.EMPTY_TYPE_RANGE, type.position, "list", Numbers.formatNumber(lower),
                        Numbers.formatNumber(upper));
                throw new SemanticException();
            }

            rslt.setLower(lower);
            rslt.setUpper(hasUpper ? upper : lower);
        }

        // Check element type.
        CifType etype = transCifType(type.elementType, scope, tchecker);
        if (CifTypeUtils.hasComponentLikeType(etype)) {
            tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, etype.getPosition(), CifTextUtils.typeToStr(etype),
                    "elements of lists");
            throw new SemanticException();
        }
        rslt.setElementType(etype);

        // Return fully type checked list type.
        return rslt;
    }

    /**
     * Transforms a set type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    private static SetType transSetType(ASetType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Get and check element type.
        CifType etype = transCifType(type.elementType, scope, tchecker);
        if (!CifTypeUtils.supportsValueEquality(etype)) {
            tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, etype.getPosition(), CifTextUtils.typeToStr(etype),
                    "elements of sets");
            throw new SemanticException();
        }

        // Create metamodel type.
        SetType stype = newSetType();
        stype.setPosition(type.position);
        stype.setElementType(etype);
        return stype;
    }

    /**
     * Transforms a function type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    private static FuncType transFuncType(AFuncType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Get and check return type.
        CifType rtype = transCifType(type.returnType, scope, tchecker);
        if (CifTypeUtils.hasComponentLikeType(rtype)) {
            tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, rtype.getPosition(), CifTextUtils.typeToStr(rtype),
                    "return values of functions");
            throw new SemanticException();
        }

        // Get and check parameter types.
        List<CifType> ptypes = list();
        for (ACifType paramType: type.paramTypes) {
            CifType ptype = transCifType(paramType, scope, tchecker);
            if (CifTypeUtils.hasComponentLikeType(ptype)) {
                tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, ptype.getPosition(), CifTextUtils.typeToStr(ptype),
                        "parameters of functions");
                throw new SemanticException();
            }
            ptypes.add(ptype);
        }

        // Create metamodel type.
        FuncType ftype = newFuncType();
        ftype.setPosition(type.position);
        ftype.setReturnType(rtype);
        ftype.getParamTypes().addAll(ptypes);
        return ftype;
    }

    /**
     * Transforms a dictionary type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    private static DictType transDictType(ADictType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Get and check key type.
        CifType ktype = transCifType(type.keyType, scope, tchecker);
        if (!CifTypeUtils.supportsValueEquality(ktype)) {
            tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, ktype.getPosition(), CifTextUtils.typeToStr(ktype),
                    "keys of dictionaries");
            throw new SemanticException();
        }

        // Get and check value type.
        CifType vtype = transCifType(type.valueType, scope, tchecker);
        if (CifTypeUtils.hasComponentLikeType(vtype)) {
            tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, vtype.getPosition(), CifTextUtils.typeToStr(vtype),
                    "values of dictionaries");
            throw new SemanticException();
        }

        // Create metamodel type.
        DictType dtype = newDictType();
        dtype.setPosition(type.position);
        dtype.setKeyType(ktype);
        dtype.setValueType(vtype);
        return dtype;
    }

    /**
     * Transforms a tuple type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    private static TupleType transTupleType(ATupleType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Get and check fields.
        List<Field> fields = list();
        Map<String, Position> fieldMap = map();
        for (AField afield: type.fields) {
            // Check field type.
            CifType ftype = transCifType(afield.type, scope, tchecker);
            if (CifTypeUtils.hasComponentLikeType(ftype)) {
                tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, ftype.getPosition(), CifTextUtils.typeToStr(ftype),
                        "fields of tuples");
                throw new SemanticException();
            }

            // Add fields for all the names.
            for (AIdentifier id: afield.names) {
                Field field = newField();
                field.setName(id.id);
                field.setPosition(id.position);
                field.setType(deepclone(ftype));
                fields.add(field);

                // Make sure the names of the fields are unique.
                Position prevPos = fieldMap.get(id.id);
                if (prevPos != null) {
                    // Name conflicts with other field.
                    tchecker.addProblem(ErrMsg.DUPLICATE_FIELD_NAME, prevPos, id.id);
                    tchecker.addProblem(ErrMsg.DUPLICATE_FIELD_NAME, id.position, id.id);
                    throw new SemanticException();
                }
                fieldMap.put(id.id, id.position);
            }
        }

        // Make sure we have at least two fields.
        Assert.check(!fields.isEmpty());
        if (fields.size() == 1) {
            tchecker.addProblem(ErrMsg.TUPLE_TYPE_ONE_FIELD, type.position);
            throw new SemanticException();
        }

        // Create metamodel type.
        TupleType ttype = newTupleType();
        ttype.setPosition(type.position);
        ttype.getFields().addAll(fields);
        return ttype;
    }

    /**
     * Transforms a distribution type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    private static DistType transDistType(ADistType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Get and check sample type.
        CifType stype = transCifType(type.sampleType, scope, tchecker);
        CifType nstype = CifTypeUtils.normalizeType(stype);
        if (nstype instanceof BoolType || (nstype instanceof IntType && isRangeless((IntType)nstype))
                || nstype instanceof RealType)
        {
            // Create metamodel type.
            DistType dtype = newDistType();
            dtype.setPosition(type.position);
            dtype.setSampleType(stype);
            return dtype;
        }

        tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, stype.getPosition(), CifTextUtils.typeToStr(stype),
                "samples of distributions");
        throw new SemanticException();
    }

    /**
     * Transforms a named type and performs type checking on it.
     *
     * @param type The CIF AST type to transform.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel type.
     */
    private static CifType transNamedType(ANamedType type, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Resolve the referenced object.
        SymbolTableEntry entry = scope.resolve(type.position, type.name.name, tchecker, scope);

        // Check for invalid reference.
        if (!(entry instanceof EnumDeclWrap) && !(entry instanceof TypeDeclWrap) && !(entry instanceof AutDefScope)
                && !(entry instanceof GroupDefScope))
        {
            tchecker.addProblem(ErrMsg.INVALID_TYPE_REF, type.position, entry.getAbsName());
            throw new SemanticException();
        }

        // Type check the referenced object 'for use', so that our caller may
        // get the type of the resolved object or may assume the resolved
        // object has a type.
        entry.tcheckForUse();

        // Return reference as a type.
        return scope.resolveAsType(type.name.name, type.position, "", tchecker);
    }
}
