//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifTypeUtils.isAutRefExpr;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifAnnotationUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
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
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;

/** CIF check that does not allow certain expressions. */
public class ExprNoSpecificExprsCheck extends CifCheck {
    /** {@link FunctionCallExpression}.{@link FunctionCallExpression#getFunction() function} metamodel feature. */
    private static final EReference FCE_FUNC_REF = ExpressionsPackage.eINSTANCE.getFunctionCallExpression_Function();

    /** The expressions to disallow. */
    private final EnumSet<NoSpecificExpr> disalloweds;

    /** Whether to disable checking of expressions in annotations. */
    private boolean ignoreAnnotations;

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

    /**
     * Disable checking of expressions in annotations.
     *
     * @return The check instance, for daisy-chaining.
     */
    public ExprNoSpecificExprsCheck ignoreAnnotations() {
        return ignoreAnnotations(true);
    }

    /**
     * Configure whether to disable checking of expressions in annotations.
     *
     * @param ignore {@code true} to disable, {@code false} to enable.
     * @return The check instance, for daisy-chaining.
     */
    public ExprNoSpecificExprsCheck ignoreAnnotations(boolean ignore) {
        this.ignoreAnnotations = ignore;
        return this;
    }

    @Override
    protected void preprocessAlgVariableExpression(AlgVariableExpression algRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(algRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.ALG_VAR_REFS)) {
            violations.add(algRef, "An algebraic variable reference is used");
        }
    }

    @Override
    protected void preprocessFunctionExpression(FunctionExpression userDefFuncRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(userDefFuncRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.FUNC_REFS)) {
            violations.add(userDefFuncRef, "A function reference is used");
        } else if (disalloweds.contains(NoSpecificExpr.FUNC_REFS_USER_DEF)) {
            violations.add(userDefFuncRef, "A user-defined function reference is used");
        } else {
            boolean asData = !isUsedInFunctionCallContext(userDefFuncRef);
            boolean isInternal = userDefFuncRef.getFunction() instanceof InternalFunction;
            boolean isExternal = userDefFuncRef.getFunction() instanceof ExternalFunction;

            if (disalloweds.contains(NoSpecificExpr.FUNC_REFS_USER_DEF_AS_DATA) && asData) {
                violations.add(userDefFuncRef, "A user-defined function reference is used as a data value");
            }

            if (disalloweds.contains(NoSpecificExpr.FUNC_REFS_USER_DEF_INT) && isInternal) {
                violations.add(userDefFuncRef, "An internal user-defined function reference is used");
            } else if (disalloweds.contains(NoSpecificExpr.FUNC_REFS_USER_DEF_INT_AS_DATA) && isInternal && asData) {
                violations.add(userDefFuncRef, "An internal user-defined function reference is used as a data value");
            }

            if (disalloweds.contains(NoSpecificExpr.FUNC_REFS_USER_DEF_EXT) && isExternal) {
                violations.add(userDefFuncRef, "An external user-defined function reference is used");
            } else if (disalloweds.contains(NoSpecificExpr.FUNC_REFS_USER_DEF_EXT_AS_DATA) && isExternal && asData) {
                violations.add(userDefFuncRef, "An external user-defined function reference is used as a data value");
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
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(stdLibRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.FUNC_REFS)) {
            violations.add(stdLibRef, "A function reference is used");
        } else if (disalloweds.contains(NoSpecificExpr.FUNC_REFS_STD_LIB)) {
            violations.add(stdLibRef, "A standard library function reference is used");
        }
    }

    @Override
    protected void preprocessBinaryExpression(BinaryExpression binExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(binExpr)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.BINARY_EXPRS)) {
            violations.add(binExpr, "A binary expression is used");
        }
    }

    @Override
    protected void preprocessBoolExpression(BoolExpression boolLit, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(boolLit)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.BOOL_LITS)) {
            violations.add(boolLit, "A boolean literal is used");
        }
    }

    @Override
    protected void preprocessCastExpression(CastExpression castExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(castExpr)) {
            return;
        }

        // Existence of the cast is not allowed.
        if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS)) {
            violations.add(castExpr, "A cast expression is used");
            return;
        }

        // Check for not having the "T -> T" cast case.
        CifType childType = castExpr.getChild().getType();
        CifType resultType = castExpr.getType();
        if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_NON_EQUAL_TYPE)) {
            if (!CifTypeUtils.checkTypeCompat(childType, resultType, RangeCompat.EQUAL)) {
                violations.add(castExpr, "A type-changing cast expression is used");
            }
            return; // T -> T case is allowed below, or user does not need more precise cast information.
        }

        // Normalize types to avoid doing that below at several points.
        childType = CifTypeUtils.normalizeType(childType);
        resultType = CifTypeUtils.normalizeType(resultType);

        // Handle the "X -> string" cast checks.
        if (resultType instanceof StringType) {
            if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_TO_STRING)) {
                violations.add(castExpr, "A cast expression to string is used");
                return;
            } else {
                if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_INT_TO_STRING)) {
                    if (childType instanceof IntType) {
                        violations.add(castExpr, "A cast expression from integer to string is used");
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_REAL_TO_STRING)) {
                    if (childType instanceof RealType) {
                        violations.add(castExpr, "A cast expression from real to string is used");
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_BOOLEAN_TO_STRING)) {
                    if (childType instanceof BoolType) {
                        violations.add(castExpr, "A cast expression from boolean to string is used");
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_AUTOMATON_TO_STRING)) {
                    if (!(castExpr.getChild() instanceof SelfExpression) && isAutRefExpr(castExpr.getChild())) {
                        violations.add(castExpr,
                                "A cast expression from explicit automaton reference to string is used");
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_SELF_TO_STRING)) {
                    if (castExpr.getChild() instanceof SelfExpression) {
                        violations.add(castExpr, "A cast expression from automaton self reference to string is used");
                        return;
                    }
                }
            }
        }

        // Handle the "string -> X" cast checks.
        if (childType instanceof StringType) {
            if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_FROM_STRING)) {
                violations.add(castExpr, "A cast expression from string is used");
                return;
            } else {
                if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_STRING_TO_INT)) {
                    if (resultType instanceof IntType) {
                        violations.add(castExpr, "A cast expression from string to integer is used");
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_STRING_TO_REAL)) {
                    if (resultType instanceof RealType) {
                        violations.add(castExpr, "A cast expression from string to real is used");
                        return;
                    }
                } else if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_STRING_TO_BOOLEAN)) {
                    if (resultType instanceof BoolType) {
                        violations.add(castExpr, "A cast expression from string to boolean is used");
                        return;
                    }
                }
            }
        }

        // Finally, deal with the "int -> real" cast.
        if (childType instanceof IntType) {
            if (disalloweds.contains(NoSpecificExpr.CAST_EXPRS_INT_TO_REAL)) {
                if (resultType instanceof RealType) {
                    violations.add(castExpr, "A cast expression from integer to real is used");
                    return;
                }
            }
        }
    }

    @Override
    protected void preprocessComponentExpression(ComponentExpression compRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(compRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.COMP_REFS)) {
            violations.add(compRef, "A component reference is used");
        } else if (disalloweds.contains(NoSpecificExpr.COMP_REFS_EXPLICIT)) {
            violations.add(compRef, "An explicit component reference is used");
        }
    }

    @Override
    protected void preprocessCompParamExpression(CompParamExpression compParamRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(compParamRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.COMP_PARAM_REFS)) {
            violations.add(compParamRef, "A component parameter reference is used");
        }
    }

    @Override
    protected void preprocessConstantExpression(ConstantExpression constRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(constRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.CONST_REFS)) {
            violations.add(constRef, "A constant reference is used");
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression contRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(contRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.CONT_VAR_REFS)) {
            violations.add(contRef, "A continuous variable reference is used");
        }
    }

    @Override
    protected void preprocessDictExpression(DictExpression dictLit, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(dictLit)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.DICT_LITS)) {
            violations.add(dictLit, "A dictionary literal is used");
        }
    }

    @Override
    protected void preprocessDiscVariableExpression(DiscVariableExpression discRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(discRef)) {
            return;
        }

        // Do the check.
        EObject parent = discRef.getVariable().eContainer();
        if (parent instanceof ComplexComponent) {
            if (disalloweds.contains(NoSpecificExpr.DISC_VAR_REFS)) {
                violations.add(discRef, "A discrete variable reference is used");
            }
        } else if (parent instanceof FunctionParameter) {
            if (disalloweds.contains(NoSpecificExpr.USER_DEF_FUNC_PARAM_REFS)) {
                violations.add(discRef, "A user-defined function parameter reference is used");
            }
        } else if (parent instanceof InternalFunction) {
            if (disalloweds.contains(NoSpecificExpr.INT_USER_DEF_FUNC_LOCAL_VAR_REFS)) {
                violations.add(discRef, "An internal user-defined function local variable reference is used");
            }
        } else {
            throw new RuntimeException("Unexpected disc var parent: " + parent);
        }
    }

    @Override
    protected void preprocessEnumLiteralExpression(EnumLiteralExpression enumLitRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(enumLitRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.ENUM_LIT_REFS)) {
            violations.add(enumLitRef, "An enumeration literal reference is used");
        }
    }

    @Override
    protected void preprocessFieldExpression(FieldExpression fieldRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(fieldRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.TUPLE_FIELD_REFS)) {
            violations.add(fieldRef, "A tuple field reference is used");
        }
    }

    @Override
    protected void preprocessFunctionCallExpression(FunctionCallExpression funcCall, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(funcCall)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.FUNC_CALLS)) {
            violations.add(funcCall, "A function call is used");
        }
    }

    @Override
    protected void preprocessIfExpression(IfExpression ifExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(ifExpr)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.IF_EXPRS)) {
            violations.add(ifExpr, "An 'if' expression is used");
        }
    }

    @Override
    protected void preprocessInputVariableExpression(InputVariableExpression inputRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(inputRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.INPUT_VAR_REFS)) {
            violations.add(inputRef, "An input variable reference is used");
        }
    }

    @Override
    protected void preprocessIntExpression(IntExpression intLit, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(intLit)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.INT_LITS)) {
            violations.add(intLit, "An integer number literal is used");
        }
    }

    @Override
    protected void preprocessListExpression(ListExpression listLit, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(listLit)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.LIST_LITS)) {
            violations.add(listLit, "A list literal is used");
        }
    }

    @Override
    protected void preprocessLocationExpression(LocationExpression locRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(locRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.LOC_REFS)) {
            violations.add(locRef, "A location reference is used");
        }
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression projExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(projExpr)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS)) {
            violations.add(projExpr, "A projection expression is used");
        } else {
            if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_LISTS)) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof ListType) {
                    violations.add(projExpr, "A list projection expression is used");
                }
            } else {
                if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_LISTS_NON_ARRAY)) {
                    CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                    if (ctype instanceof ListType && !CifTypeUtils.isArrayType((ListType)ctype)) {
                        violations.add(projExpr, "A non-array list projection expression is used");
                    }
                }
            }
            if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_DICTS)) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof DictType) {
                    violations.add(projExpr, "A dictionary projection expression is used");
                }
            }
            if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_STRINGS)) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof StringType) {
                    violations.add(projExpr, "A string projection expression is used");
                }
            }
            if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_TUPLES)) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof TupleType) {
                    violations.add(projExpr, "A tuple projection expression is used");
                }
            } else {
                if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_TUPLES_INDEX)) {
                    CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                    CifType itype = CifTypeUtils.normalizeType(projExpr.getIndex().getType());
                    if (ctype instanceof TupleType && itype instanceof IntType) {
                        violations.add(projExpr, "A tuple index-projection expression is used");
                    }
                }
                if (disalloweds.contains(NoSpecificExpr.PROJECTION_EXPRS_TUPLES_FIELD)) {
                    CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                    if (ctype instanceof TupleType && projExpr.getIndex() instanceof FieldExpression) {
                        violations.add(projExpr, "A tuple field-projection expression is used");
                    }
                }
            }
        }
    }

    @Override
    protected void preprocessRealExpression(RealExpression realLit, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(realLit)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.REAL_LITS)) {
            violations.add(realLit, "A real number literal is used");
        }
    }

    @Override
    protected void preprocessReceivedExpression(ReceivedExpression receivedExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(receivedExpr)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.RECEIVE_EXPRS)) {
            violations.add(receivedExpr, "A received value expression is used");
        }
    }

    @Override
    protected void preprocessSelfExpression(SelfExpression selfRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(selfRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.COMP_REFS)) {
            violations.add(selfRef, "A component reference is used");
        } else if (disalloweds.contains(NoSpecificExpr.COMP_REFS_SELF)) {
            violations.add(selfRef, "A component 'self' reference is used");
        }
    }

    @Override
    protected void preprocessSetExpression(SetExpression setLit, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(setLit)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.SET_LITS)) {
            violations.add(setLit, "A set literal is used");
        }
    }

    @Override
    protected void preprocessSliceExpression(SliceExpression sliceExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(sliceExpr)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.SLICE_EXPRS)) {
            violations.add(sliceExpr, "A slice expression is used");
        }
    }

    @Override
    protected void preprocessStringExpression(StringExpression stringLit, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(stringLit)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.STRING_LITS)) {
            violations.add(stringLit, "A string literal is used");
        }
    }

    @Override
    protected void preprocessSwitchExpression(SwitchExpression switchExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(switchExpr)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS)) {
            violations.add(switchExpr, "A switch expression is used");
        } else {
            // 'switch' value is an automaton.
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_BOOL)) {
                if (CifTypeUtils.isAutRefExpr(switchExpr.getValue())) {
                    violations.add(switchExpr, "A switch expression is used on an automaton");
                }
            }

            // 'switch' on (part of its) value that has a certain type.
            CifType valueType = CifTypeUtils.normalizeType(switchExpr.getValue().getType());
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_BOOL)) {
                if (CifTypeUtils.hasType(valueType, BoolType.class::isInstance)) {
                    violations.add(switchExpr, "A switch expression is used with a boolean typed (part of its) value");
                }
            }
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_DICT)) {
                if (CifTypeUtils.hasType(valueType, DictType.class::isInstance)) {
                    violations.add(switchExpr,
                            "A switch expression is used with a dictionary typed (part of its) value");
                }
            }
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_ENUM)) {
                if (CifTypeUtils.hasType(valueType, EnumType.class::isInstance)) {
                    violations.add(switchExpr,
                            "A switch expression is used with an enumeration typed (part of its) value");
                }
            }
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_INT)) {
                if (CifTypeUtils.hasType(valueType, IntType.class::isInstance)) {
                    violations.add(switchExpr, "A switch expression is used with an integer typed (part of its) value");
                }
            } else {
                if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_INT_RANGED)) {
                    if (CifTypeUtils.hasType(valueType,
                            t -> t instanceof IntType itype && !CifTypeUtils.isRangeless(itype)))
                    {
                        violations.add(switchExpr,
                                "A switch expression is used with a ranged integer typed (part of its) value");
                    }
                }
                if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_INT_RANGELESS)) {
                    if (CifTypeUtils.hasType(valueType,
                            t -> t instanceof IntType itype && CifTypeUtils.isRangeless(itype)))
                    {
                        violations.add(switchExpr,
                                "A switch expression is used with a rangeless integer typed (part of its) value");
                    }
                }
            }
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_LIST)) {
                if (CifTypeUtils.hasType(valueType, ListType.class::isInstance)) {
                    violations.add(switchExpr, "A switch expression is used with a list typed (part of its) value");
                }
            } else {
                if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_LIST_ARRAY)) {
                    if (CifTypeUtils.hasType(valueType,
                            t -> t instanceof ListType ltype && CifTypeUtils.isArrayType(ltype)))
                    {
                        violations.add(switchExpr,
                                "A switch expression is used with an array list typed (part of its) value");
                    }
                }
                if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_LIST_NON_ARRAY)) {
                    if (CifTypeUtils.hasType(valueType,
                            t -> t instanceof ListType ltype && !CifTypeUtils.isArrayType(ltype)))
                    {
                        violations.add(switchExpr,
                                "A switch expression is used with a non-array list typed (part of its) value");
                    }
                }
            }
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_REAL)) {
                if (CifTypeUtils.hasType(valueType, RealType.class::isInstance)) {
                    violations.add(switchExpr, "A switch expression is used with a real typed (part of its) value");
                }
            }
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_SET)) {
                if (CifTypeUtils.hasType(valueType, SetType.class::isInstance)) {
                    violations.add(switchExpr, "A switch expression is used with a set typed (part of its) value");
                }
            }
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_STRING)) {
                if (CifTypeUtils.hasType(valueType, StringType.class::isInstance)) {
                    violations.add(switchExpr, "A switch expression is used with a string typed (part of its) value");
                }
            }
            if (disalloweds.contains(NoSpecificExpr.SWITCH_EXPRS_TUPLE)) {
                if (CifTypeUtils.hasType(valueType, TupleType.class::isInstance)) {
                    violations.add(switchExpr, "A switch expression is used with a tuple typed (part of its) value");
                }
            }
        }
    }

    @Override
    protected void preprocessTimeExpression(TimeExpression timeRef, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(timeRef)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.TIME_VAR_REFS)) {
            violations.add(timeRef, "A 'time' variable reference is used");
        }
    }

    @Override
    protected void preprocessTupleExpression(TupleExpression tupleLit, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(tupleLit)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.TUPLE_LITS)) {
            violations.add(tupleLit, "A tuple literal is used");
        }
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression unExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(unExpr)) {
            return;
        }

        // Do the check.
        if (disalloweds.contains(NoSpecificExpr.UNARY_EXPRS)) {
            violations.add(unExpr, "A unary expression is used");
        }
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

        /** Disallow switch expressions, on automata. */
        SWITCH_EXPRS_AUT,

        /** Disallow switch expressions, on tuples (recursively in the switch value's type). */
        SWITCH_EXPRS_BOOL,

        /** Disallow switch expressions, on dictionaries (recursively in the switch value's type). */
        SWITCH_EXPRS_DICT,

        /** Disallow switch expressions, on enumerations (recursively in the switch value's type). */
        SWITCH_EXPRS_ENUM,

        /** Disallow switch expressions, on integers (recursively in the switch value's type). */
        SWITCH_EXPRS_INT,

        /** Disallow switch expressions, on ranged integers (recursively in the switch value's type). */
        SWITCH_EXPRS_INT_RANGED,

        /** Disallow switch expressions, on rangeless integers (recursively in the switch value's type). */
        SWITCH_EXPRS_INT_RANGELESS,

        /** Disallow switch expressions, on lists (recursively in the switch value's type). */
        SWITCH_EXPRS_LIST,

        /** Disallow switch expressions, on array lists (recursively in the switch value's type). */
        SWITCH_EXPRS_LIST_ARRAY,

        /** Disallow switch expressions, on non-array lists (recursively in the switch value's type). */
        SWITCH_EXPRS_LIST_NON_ARRAY,

        /** Disallow switch expressions, on reals (recursively in the switch value's type). */
        SWITCH_EXPRS_REAL,

        /** Disallow switch expressions, on sets (recursively in the switch value's type). */
        SWITCH_EXPRS_SET,

        /** Disallow switch expressions, on strings (recursively in the switch value's type). */
        SWITCH_EXPRS_STRING,

        /** Disallow switch expressions, on tuples (recursively in the switch value's type). */
        SWITCH_EXPRS_TUPLE,

        /** Disallow 'time' variable references. */
        TIME_VAR_REFS,

        /** Disallow tuple literal expressions. */
        TUPLE_LITS,

        /** Disallow all unary expressions. */
        UNARY_EXPRS,
    }
}
