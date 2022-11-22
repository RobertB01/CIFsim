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

package org.eclipse.escet.cif.common.checkers.checks;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.isAutRefExpr;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.java.Assert;

/** CIF check that does not allow certain expressions. */
public class ExprNoSpecificExprsCheck extends CifCheck {
    /** {@link FunctionCallExpression}.{@link FunctionCallExpression#getFunction() function} metamodel feature. */
    private static final EReference FCE_FUNC_REF = ExpressionsPackage.eINSTANCE.getFunctionCallExpression_Function();

    /** The expressions to disallow. */
    private final EnumSet<NoSpecificExpr> disalloweds;

    /**
     * Constructor for the {@link ExprNoSpecificExprsCheck} class.
     *
     * @param disalloweds The expressions to disallow.
     */
    public ExprNoSpecificExprsCheck(NoSpecificExpr... disalloweds) {
        this(EnumSet.copyOf(Arrays.asList(disalloweds)));
    }

    /**
     * Constructor for the {@link ExprNoSpecificExprsCheck} class.
     *
     * @param disalloweds The expressions to disallow.
     */
    public ExprNoSpecificExprsCheck(EnumSet<NoSpecificExpr> disalloweds) {
        this.disalloweds = disalloweds;
    }

    @Override
    protected void preprocessAlgVariableExpression(AlgVariableExpression algRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.ALG_VAR_REFS)) {
            addExprViolation(algRef, "algebraic variable reference", violations);
        }
    }

    @Override
    protected void preprocessFunctionExpression(FunctionExpression userDefFuncRef, CifCheckViolations violations) {
        EnumSet<NoSpecificExpr> funcFlags, funcAsDataFlags;
        String funcKind;
        if (userDefFuncRef.getFunction() instanceof InternalFunction) {
            funcFlags = EnumSet.of( //
                    NoSpecificExpr.FUNC_REFS, //
                    NoSpecificExpr.FUNC_REFS_USER_DEF, //
                    NoSpecificExpr.FUNC_REFS_USER_DEF_INT);
            funcAsDataFlags = EnumSet.of( //
                    NoSpecificExpr.FUNC_REFS_USER_DEF_AS_DATA, //
                    NoSpecificExpr.FUNC_REFS_USER_DEF_INT_AS_DATA);
            funcKind = "internal";
        } else {
            Assert.check(userDefFuncRef.getFunction() instanceof ExternalFunction);
            funcFlags = EnumSet.of( //
                    NoSpecificExpr.FUNC_REFS, //
                    NoSpecificExpr.FUNC_REFS_USER_DEF, //
                    NoSpecificExpr.FUNC_REFS_USER_DEF_EXT);
            funcAsDataFlags = EnumSet.of( //
                    NoSpecificExpr.FUNC_REFS_USER_DEF_AS_DATA, //
                    NoSpecificExpr.FUNC_REFS_USER_DEF_EXT_AS_DATA);
            funcKind = "external";
        }

        if (!isEmptyIntersection(disalloweds, funcFlags)) {
            addExprViolation(userDefFuncRef, funcKind + " user-defined function reference", violations);
        } else {
            if (!isEmptyIntersection(disalloweds, funcAsDataFlags) && !isUsedInFunctionCallContext(userDefFuncRef)) {
                addExprViolation(userDefFuncRef, funcKind + " user-defined function reference as data value",
                        violations);
            }
        }
    }

    /**
     * Is the provided function expression used in a function call context?
     *
     * @param funcExpr Function expression to check.
     * @return Whether the function expression is used in a function call context.
     */
    private boolean isUsedInFunctionCallContext(FunctionExpression funcExpr) {
        return (funcExpr.eContainer() instanceof FunctionCallExpression)
                && funcExpr.eContainmentFeature() == FCE_FUNC_REF;
    }

    @Override
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression stdLibRef,
            CifCheckViolations violations)
    {
        EnumSet<NoSpecificExpr> funcFlags = EnumSet.of(NoSpecificExpr.FUNC_REFS, NoSpecificExpr.FUNC_REFS_STD_LIB);
        if (!isEmptyIntersection(disalloweds, funcFlags)) {
            addExprViolation(stdLibRef, "standard library function reference", violations);
        }
    }

    @Override
    protected void preprocessBinaryExpression(BinaryExpression binExpr, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.BINARY_EXPRS)) {
            addExprViolation(binExpr, "binary expression", violations);
        }
    }

    @Override
    protected void preprocessBoolExpression(BoolExpression boolLit, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.BOOL_LITS)) {
            addExprViolation(boolLit, "boolean literal", violations);
        }
    }

    @Override
    protected void preprocessCastExpression(CastExpression castExpr, CifCheckViolations violations) {
        // Existence of the cast is not allowed.
        if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS)) {
            addExprViolation(castExpr, "cast expression", violations);
            return;
        }

        // Check for not having the "T -> T" cast case.
        CifType childType = castExpr.getChild().getType();
        CifType resultType = castExpr.getType();
        if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_NON_EQUAL_TYPE)) {
            if (!CifTypeUtils.checkTypeCompat(childType, resultType, RangeCompat.EQUAL)) {
                addExprViolation(castExpr, "type-changing cast expression", violations);
            }
            return; // T -> T case is allowed below, or user does not need more precise cast information.
        }

        // Normalize types to avoid doing that below at several points.
        childType = CifTypeUtils.normalizeType(childType);
        resultType = CifTypeUtils.normalizeType(resultType);

        // Handle the "X -> string" cast checks.
        if (resultType instanceof StringType) {
            if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_TO_STRING)) {
                addExprViolation(castExpr, "cast expression to string", violations);
                return;
            } else {
                if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_INT_TO_STRING)) {
                    if (childType instanceof IntType) {
                        addExprViolation(castExpr, "cast expression from integer to string", violations);
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_REAL_TO_STRING)) {
                    if (childType instanceof RealType) {
                        addExprViolation(castExpr, "cast expression from real to string", violations);
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_BOOLEAN_TO_STRING)) {
                    if (childType instanceof BoolType) {
                        addExprViolation(castExpr, "cast expression from boolean to string", violations);
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_AUTOMATON_TO_STRING)) {
                    if (!(castExpr.getChild() instanceof SelfExpression) && isAutRefExpr(castExpr.getChild())) {
                        addExprViolation(castExpr, "cast expression from explicit automaton reference to string",
                                violations);
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_SELF_TO_STRING)) {
                    if (castExpr.getChild() instanceof SelfExpression) {
                        addExprViolation(castExpr, "cast expression from automaton self reference to string",
                                violations);
                        return;
                    }
                }
            }
        }

        // Handle the "string -> X" cast checks.
        if (childType instanceof StringType) {
            if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_FROM_STRING)) {
                addExprViolation(castExpr, "cast expression from string", violations);
                return;
            } else {
                if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_STRING_TO_INT)) {
                    if (resultType instanceof IntType) {
                        addExprViolation(castExpr, "cast expression from string to integer", violations);
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_STRING_TO_REAL)) {
                    if (resultType instanceof RealType) {
                        addExprViolation(castExpr, "cast expression from string to real", violations);
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_STRING_TO_BOOLEAN)) {
                    if (resultType instanceof BoolType) {
                        addExprViolation(castExpr, "cast expression from string to boolean", violations);
                        return;
                    }
                }
            }
        } else if (childType instanceof IntType) {
            // Finally, deal with the "int -> real" cast.
            if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_INT_TO_REAL)) {
                if (resultType instanceof RealType) {
                    addExprViolation(castExpr, "cast expression from integer to real", violations);
                    return;
                }
            }
        }
    }

    @Override
    protected void preprocessComponentExpression(ComponentExpression compRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.COMP_REFS) || disalloweds.contains(NoSpecificExpr.COMP_REFS_EXPLICIT)) {
            addExprViolation(compRef, "component reference", violations);
        }
    }

    @Override
    protected void preprocessCompParamExpression(CompParamExpression compParamRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.COMP_PARAM_REFS)) {
            addExprViolation(compParamRef, "component parameter reference", violations);
        }
    }

    @Override
    protected void preprocessConstantExpression(ConstantExpression constRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.CONST_REFS)) {
            addExprViolation(constRef, "constant reference", violations);
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression contRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.CONT_VAR_REFS)) {
            addExprViolation(contRef, "continuous variable reference", violations);
        }
    }

    @Override
    protected void preprocessDictExpression(DictExpression dictLit, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.DICT_LITS)) {
            addExprViolation(dictLit, "dictionary literal", violations);
        }
    }

    @Override
    protected void preprocessDiscVariableExpression(DiscVariableExpression discRef, CifCheckViolations violations) {
        EObject parent = discRef.getVariable().eContainer();
        if (parent instanceof ComplexComponent) {
            if (disalloweds.contains(NoSpecificExpr.DISC_VAR_REFS)) {
                addExprViolation(discRef, "discrete variable reference", violations);
            }
        } else if (parent instanceof FunctionParameter) {
            if (disalloweds.contains(NoSpecificExpr.USER_DEF_FUNC_PARAM_REFS)) {
                addExprViolation(discRef, "user-defined function parameter reference", violations);
            }
        } else if (parent instanceof InternalFunction) {
            if (disalloweds.contains(NoSpecificExpr.INT_USER_DEF_FUNC_LOCAL_VAR_REFS)) {
                addExprViolation(discRef, "internal user-defined function local variable reference", violations);
            }
        } else {
            throw new RuntimeException("Unexpected disc var parent: " + parent);
        }
    }

    @Override
    protected void preprocessEnumLiteralExpression(EnumLiteralExpression enumLitRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.ENUM_LIT_REFS)) {
            addExprViolation(enumLitRef, "enumeration literal reference", violations);
        }
    }

    @Override
    protected void preprocessFieldExpression(FieldExpression fieldRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.TUPLE_FIELD_REFS)) {
            addExprViolation(fieldRef, "tuple field reference", violations);
        }
    }

    @Override
    protected void preprocessFunctionCallExpression(FunctionCallExpression funcCall, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.FUNC_CALLS)) {
            addExprViolation(funcCall, "function call", violations);
        }
    }

    @Override
    protected void preprocessIfExpression(IfExpression ifExpr, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.IF_EXPRS)) {
            addExprViolation(ifExpr, "conditional expression", violations);
        }
    }

    @Override
    protected void preprocessInputVariableExpression(InputVariableExpression inputRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.INPUT_VAR_REFS)) {
            addExprViolation(inputRef, "input variable reference", violations);
        }
    }

    @Override
    protected void preprocessIntExpression(IntExpression intLit, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.INT_LITS)) {
            addExprViolation(intLit, "integer number literal", violations);
        }
    }

    @Override
    protected void preprocessListExpression(ListExpression listLit, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.LIST_LITS)) {
            addExprViolation(listLit, "list literal", violations);
        }
    }

    @Override
    protected void preprocessLocationExpression(LocationExpression locRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.LOC_REFS)) {
            addExprViolation(locRef, "location reference", violations);
        }
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression projExpr, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS)) {
            addExprViolation(projExpr, "projection expression", violations);
        } else {
            if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_LISTS)) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof ListType) {
                    addExprViolation(projExpr, "list projection expression", violations);
                }
            } else {
                if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_LISTS_NON_ARRAY)) {
                    CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                    if (ctype instanceof ListType && !CifTypeUtils.isArrayType((ListType)ctype)) {
                        addExprViolation(projExpr, "non-array list projection expression", violations);
                    }
                }
            }

            if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_DICTS)) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof DictType) {
                    addExprViolation(projExpr, "dictionary projection expression", violations);
                }
            }
            if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_STRINGS)) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof StringType) {
                    addExprViolation(projExpr, "string projection expression", violations);
                }
            }
            if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_TUPLES)) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof TupleType) {
                    addExprViolation(projExpr, "tuple projection expression", violations);
                }
            } else {
                if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_TUPLES_INDEX)) {
                    CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                    CifType itype = CifTypeUtils.normalizeType(projExpr.getIndex().getType());
                    if (ctype instanceof TupleType && itype instanceof IntType) {
                        addExprViolation(projExpr, "tuple index-projection expression", violations);
                    }
                }
                if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_TUPLES_FIELD)) {
                    CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                    if (ctype instanceof TupleType && projExpr.getIndex() instanceof FieldExpression) {
                        addExprViolation(projExpr, "tuple field-projection expression", violations);
                    }
                }
            }
        }
    }

    @Override
    protected void preprocessRealExpression(RealExpression realLit, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.REAL_LITS)) {
            addExprViolation(realLit, "real number literal", violations);
        }
    }

    @Override
    protected void preprocessReceivedExpression(ReceivedExpression receivedExpr, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.RECEIVE_EXPRS)) {
            addExprViolation(receivedExpr, "received value expression", violations);
        }
    }

    @Override
    protected void preprocessSelfExpression(SelfExpression selfRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.COMP_REFS) || disalloweds.contains(NoSpecificExpr.COMP_REFS_SELF)) {
            addExprViolation(selfRef, "component reference", violations);
        }
    }

    @Override
    protected void preprocessSetExpression(SetExpression setLit, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.SET_LITS)) {
            addExprViolation(setLit, "set literal", violations);
        }
    }

    @Override
    protected void preprocessSliceExpression(SliceExpression sliceExpr, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.SLICE_EXPRS)) {
            addExprViolation(sliceExpr, "slice expression", violations);
        }
    }

    @Override
    protected void preprocessStringExpression(StringExpression stringLit, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.STRING_LITS)) {
            addExprViolation(stringLit, "string literal", violations);
        }
    }

    @Override
    protected void preprocessSwitchExpression(SwitchExpression switchExpr, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS)) {
            addExprViolation(switchExpr, "switch expression", violations);
        }
    }

    @Override
    protected void preprocessTimeExpression(TimeExpression timeRef, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.TIME_VAR_REFS)) {
            addExprViolation(timeRef, "time variable reference", violations);
        }
    }

    @Override
    protected void preprocessTupleExpression(TupleExpression tupleLit, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.TUPLE_LITS)) {
            addExprViolation(tupleLit, "tuple literal", violations);
        }
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression unExpr, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificExpr.UNARY_EXPRS)) {
            addExprViolation(unExpr, "unary expression", violations);
        }
    }

    /**
     * Add a violation for the given expression.
     *
     * @param expr The expression.
     * @param description The description of the expression.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolation(Expression expr, String description, CifCheckViolations violations) {
        violations.add(expr, new LiteralMessage("uses %s \"%s\"", description, exprToStr(expr)));
    }

    /** The expression to disallow. */
    public static enum NoSpecificExpr {
        /** Disallow references to algebraic variables. */
        ALG_VAR_REFS,

        /** Disallow references to all functions (user-defined and standard library ones). */
        FUNC_REFS,

        /** Disallow references to all user-defined functions (internal and external ones). */
        FUNC_REFS_USER_DEF,

        /** Disallow references to all user-defined functions as data value (internal and external ones). */
        FUNC_REFS_USER_DEF_AS_DATA,

        /** Disallow references to internal user-defined functions. */
        FUNC_REFS_USER_DEF_INT,

        /** Disallow references to internal user-defined functions as data value. */
        FUNC_REFS_USER_DEF_INT_AS_DATA,

        /** Disallow references to external user-defined functions. */
        FUNC_REFS_USER_DEF_EXT,

        /** Disallow references to external user-defined functions as data value. */
        FUNC_REFS_USER_DEF_EXT_AS_DATA,

        /** Disallow references to standard library functions. */
        FUNC_REFS_STD_LIB,

        /** Disallow all binary expressions. */
        BINARY_EXPRS,

        /** Disallow boolean literals. */
        BOOL_LITS,

        /** Disallow all cast expressions. */
        CAST_EXPRS,

        /** Disallow cast expressions that cast to a different type. */
        CAST_EXPRS_NON_EQUAL_TYPE,

        /** Disallow cast expressions that cast to string. */
        CAST_EXPRS_TO_STRING,

        /** Disallow cast expressions that cast from integer to string. */
        CAST_EXPRS_INT_TO_STRING,

        /** Disallow cast expressions that cast from real to string. */
        CAST_EXPRS_REAL_TO_STRING,

        /** Disallow cast expressions that cast from boolean to string. */
        CAST_EXPRS_BOOLEAN_TO_STRING,

        /** Disallow cast expressions that cast from explicit automaton reference (thus excluding 'self') to string. */
        CAST_EXPRS_AUTOMATON_TO_STRING,

        /** Disallow cast expressions that cast from automaton 'self' reference to string. */
        CAST_EXPRS_SELF_TO_STRING,

        /** Disallow cast expressions that cast from a string. */
        CAST_EXPRS_FROM_STRING,

        /** Disallow cast expressions that cast from string to integer. */
        CAST_EXPRS_STRING_TO_INT,

        /** Disallow cast expressions that cast from string to real. */
        CAST_EXPRS_STRING_TO_REAL,

        /** Disallow cast expressions that cast from string to boolean. */
        CAST_EXPRS_STRING_TO_BOOLEAN,

        /** Disallow cast expressions that cast from integer to real. */
        CAST_EXPRS_INT_TO_REAL,

        /** Disallow all component references (explicit components and 'self' references). */
        COMP_REFS,

        /** Disallow explicit component references. */
        COMP_REFS_EXPLICIT,

        /** Disallow component 'self' references. */
        COMP_REFS_SELF,

        /** Disallow component parameter references. */
        COMP_PARAM_REFS,

        /** Disallow constant references. */
        CONST_REFS,

        /** Disallow continuous variable references. */
        CONT_VAR_REFS,

        /** Disallow dictionary literals. */
        DICT_LITS,

        /** Disallow discrete variable references. */
        DISC_VAR_REFS,

        /** Disallow user-defined function parameter references. */
        USER_DEF_FUNC_PARAM_REFS,

        /** Disallow references to local variables of internal user-defined functions. */
        INT_USER_DEF_FUNC_LOCAL_VAR_REFS,

        /** Disallow enumeration literal references. */
        ENUM_LIT_REFS,

        /** Disallow tuple field references. */
        TUPLE_FIELD_REFS,

        /** Disallow function calls (for user-defined functions and standard library functions). */
        FUNC_CALLS,

        /** Disallow 'if' expressions (conditional expressions). */
        IF_EXPRS,

        /** Disallow input variable references. */
        INPUT_VAR_REFS,

        /** Disallow integer number literals. */
        INT_LITS,

        /** Disallow list literals. */
        LIST_LITS,

        /** Disallow location references. */
        LOC_REFS,

        /** Disallow projection expressions. */
        PROJECTION_EXPRS,

        /** Disallow projection expressions on lists. */
        PROJECTION_EXPRS_LISTS,

        /** Disallow projection expressions on non-array lists. */
        PROJECTION_EXPRS_LISTS_NON_ARRAY,

        /** Disallow projection expressions on dictionaries. */
        PROJECTION_EXPRS_DICTS,

        /** Disallow projection expressions on strings. */
        PROJECTION_EXPRS_STRINGS,

        /** Disallow projection expressions on tuples. */
        PROJECTION_EXPRS_TUPLES,

        /** Disallow projection expressions on tuples using index. */
        PROJECTION_EXPRS_TUPLES_INDEX,

        /** Disallow projection expressions on tuples using field. */
        PROJECTION_EXPRS_TUPLES_FIELD,

        /** Disallow real number expressions. */
        REAL_LITS,

        /** Disallow received value expressions. */
        RECEIVE_EXPRS,

        /** Disallow set literals. */
        SET_LITS,

        /** Disallow slice expressions. */
        SLICE_EXPRS,

        /** Disallow string literals. */
        STRING_LITS,

        /** Disallow switch expressions. */
        SWITCH_EXPRS,

        /** Disallow 'time' variable references. */
        TIME_VAR_REFS,

        /** Disallow tuple literal expressions. */
        TUPLE_LITS,

        /** Disallow all unary expressions. */
        UNARY_EXPRS,
    }
}
