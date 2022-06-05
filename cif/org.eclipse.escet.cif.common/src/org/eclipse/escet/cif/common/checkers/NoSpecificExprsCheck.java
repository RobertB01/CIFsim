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

package org.eclipse.escet.cif.common.checkers;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getNamedAncestorOrSelf;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
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
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check that does not allow certain expressions. */
public class NoSpecificExprsCheck extends CifCheck {
    /** Whether to disallow references to algebraic variables. */
    public boolean disallowAlgVarRefs;

    /** Whether to disallow references to all functions (user-defined and standard library ones). */
    public boolean disallowFuncRefs;

    /** Whether to disallow references to all user-defined functions (internal and external ones). */
    public boolean disallowFuncRefsUserDef;

    /** Whether to disallow references to internal user-defined functions. */
    public boolean disallowFuncRefsUserDefInt;

    /** Whether to disallow references to external user-defined functions. */
    public boolean disallowFuncRefsUserDefExt;

    /** Whether to disallow references to standard library functions. */
    public boolean disallowFuncRefsStdLib;

    /** Whether to disallow all binary expressions. */
    public boolean disallowBinExprs;

    /** Whether to disallow boolean literals. */
    public boolean disallowBoolLits;

    /** Whether to disallow all cast expressions. */
    public boolean disallowCastExprs;

    /** Whether to disallow cast expressions that cast to a different type. */
    public boolean disallowCastExprsNonEqualType;

    /** Whether to disallow all component references (explicit components and 'self' references). */
    public boolean disallowCompRefs;

    /** Whether to disallow explicit component references. */
    public boolean disallowCompRefsExplicit;

    /** Whether to disallow component 'self' references. */
    public boolean disallowCompRefsSelf;

    /** Whether to disallow component parameter references. */
    public boolean disallowCompParamRefs;

    /** Whether to disallow constant references. */
    public boolean disallowConstRefs;

    /** Whether to disallow continuous variable references. */
    public boolean disallowContVarRefs;

    /** Whether to disallow dictionary literals. */
    public boolean disallowDictLits;

    /** Whether to disallow discrete variable references. */
    public boolean disallowDiscVarRefs;

    /** Whether to disallow user-defined function parameter references. */
    public boolean disallowUserDefFuncParamRefs;

    /** Whether to disallow references to local variables of internal user-defined functions. */
    public boolean disallowIntUserDefFuncLocalVarRefs;

    /** Whether to disallow enumeration literal references. */
    public boolean disallowEnumLitRefs;

    /** Whether to disallow tuple field references. */
    public boolean disallowTupleFieldRefs;

    /** Whether to disallow function calls (for user-defined functions and standard library functions). */
    public boolean disallowFuncCalls;

    /** Whether to disallow 'if' expressions (conditional expressions). */
    public boolean disallowIfExprs;

    /** Whether to disallow input variable references. */
    public boolean disallowInputVarRefs;

    /** Whether to disallow integer number literals. */
    public boolean disallowIntLits;

    /** Whether to disallow list literals. */
    public boolean disallowListLits;

    /** Whether to disallow location references. */
    public boolean disallowLocRefs;

    /** Whether to disallow projection expressions. */
    public boolean disallowProjectionExprs;

    /** Whether to disallow projection expressions on lists. */
    public boolean disallowProjectionExprsLists;

    /** Whether to disallow projection expressions on dictionaries. */
    public boolean disallowProjectionExprsDicts;

    /** Whether to disallow projection expressions on strings. */
    public boolean disallowProjectionExprsStrings;

    /** Whether to disallow projection expressions on tuples. */
    public boolean disallowProjectionExprsTuples;

    /** Whether to disallow projection expressions on tuples using index. */
    public boolean disallowProjectionExprsTuplesIndex;

    /** Whether to disallow projection expressions on tuples using field. */
    public boolean disallowProjectionExprsTuplesField;

    /** Whether to disallow real number expressions. */
    public boolean disallowRealLits;

    /** Whether to disallow received value expressions. */
    public boolean disallowReceiveExprs;

    /** Whether to disallow set literals. */
    public boolean disallowSetLits;

    /** Whether to disallow slice expressions. */
    public boolean disallowSliceExprs;

    /** Whether to disallow string literals. */
    public boolean disallowStringLits;

    /** Whether to disallow switch expressions. */
    public boolean disallowSwitchExprs;

    /** Whether to disallow 'time' variable references. */
    public boolean disallowTimeVarRefs;

    /** Whether to disallow tuple literal expressions. */
    public boolean disallowTupleLits;

    /** Whether to disallow all unary expressions. */
    public boolean disallowUnExprs;

    @Override
    protected void preprocessAlgVariableExpression(AlgVariableExpression algRef) {
        if (disallowAlgVarRefs) {
            addExprViolation(algRef, "algebraic variable reference");
        }
    }

    @Override
    protected void preprocessFunctionExpression(FunctionExpression userDefFuncRef) {
        if ((disallowFuncRefs || disallowFuncRefsUserDef || disallowFuncRefsUserDefInt)
                && userDefFuncRef.getFunction() instanceof InternalFunction)
        {
            addExprViolation(userDefFuncRef, "internal user-defined function reference");
        }

        if ((disallowFuncRefs || disallowFuncRefsUserDef || disallowFuncRefsUserDefExt)
                && userDefFuncRef.getFunction() instanceof ExternalFunction)
        {
            addExprViolation(userDefFuncRef, "external user-defined function reference");
        }
    }

    @Override
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression stdLibRef) {
        if (disallowFuncRefs || disallowFuncRefsStdLib) {
            addExprViolation(stdLibRef, "standard library function reference");
        }
    }

    @Override
    protected void preprocessBinaryExpression(BinaryExpression binExpr) {
        if (disallowBinExprs) {
            addExprViolation(binExpr, "binary expression");
        }
    }

    @Override
    protected void preprocessBoolExpression(BoolExpression boolLit) {
        if (disallowBoolLits) {
            addExprViolation(boolLit, "boolean literal");
        }
    }

    @Override
    protected void preprocessCastExpression(CastExpression castExpr) {
        if (disallowCastExprs) {
            addExprViolation(castExpr, "cast expression");
        } else if (disallowCastExprsNonEqualType) {
            CifType ctype = castExpr.getChild().getType();
            CifType rtype = castExpr.getType();
            if (CifTypeUtils.checkTypeCompat(ctype, rtype, RangeCompat.EQUAL)) {
                // Ignore casting to the child type.
                return;
            }
            addExprViolation(castExpr, "type-changing cast expression");
        }
    }

    @Override
    protected void preprocessComponentExpression(ComponentExpression compRef) {
        if (disallowCompRefs || disallowCompRefsExplicit) {
            addExprViolation(compRef, "component reference");
        }
    }

    @Override
    protected void preprocessCompParamExpression(CompParamExpression compParamRef) {
        if (disallowCompParamRefs) {
            addExprViolation(compParamRef, "component parameter reference");
        }
    }

    @Override
    protected void preprocessConstantExpression(ConstantExpression constRef) {
        if (disallowConstRefs) {
            addExprViolation(constRef, "constant reference");
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression contRef) {
        if (disallowContVarRefs) {
            addExprViolation(contRef, "continuous variable reference");
        }
    }

    @Override
    protected void preprocessDictExpression(DictExpression dictLit) {
        if (disallowDictLits) {
            addExprViolation(dictLit, "dictionary literal");
        }
    }

    @Override
    protected void preprocessDiscVariableExpression(DiscVariableExpression discRef) {
        EObject parent = discRef.getVariable().eContainer();
        if (parent instanceof ComplexComponent) {
            if (disallowDiscVarRefs) {
                addExprViolation(discRef, "discrete variable reference");
            }
        } else if (parent instanceof FunctionParameter) {
            if (disallowUserDefFuncParamRefs) {
                addExprViolation(discRef, "user-defined function parameter reference");
            }
        } else if (parent instanceof InternalFunction) {
            if (disallowIntUserDefFuncLocalVarRefs) {
                addExprViolation(discRef, "internal user-defined function local variable reference");
            }
        } else {
            throw new RuntimeException("Unexpected disc var parent: " + parent);
        }
    }

    @Override
    protected void preprocessEnumLiteralExpression(EnumLiteralExpression enumLitRef) {
        if (disallowEnumLitRefs) {
            addExprViolation(enumLitRef, "enumeration literal reference");
        }
    }

    @Override
    protected void preprocessFieldExpression(FieldExpression fieldRef) {
        if (disallowTupleFieldRefs) {
            addExprViolation(fieldRef, "tuple field reference");
        }
    }

    @Override
    protected void preprocessFunctionCallExpression(FunctionCallExpression funcCall) {
        if (disallowFuncCalls) {
            addExprViolation(funcCall, "function call");
        }
    }

    @Override
    protected void preprocessIfExpression(IfExpression ifExpr) {
        if (disallowIfExprs) {
            addExprViolation(ifExpr, "conditional expression");
        }
    }

    @Override
    protected void preprocessInputVariableExpression(InputVariableExpression inputRef) {
        if (disallowInputVarRefs) {
            addExprViolation(inputRef, "input variable reference");
        }
    }

    @Override
    protected void preprocessIntExpression(IntExpression intLit) {
        if (disallowIntLits) {
            addExprViolation(intLit, "integer number literal");
        }
    }

    @Override
    protected void preprocessListExpression(ListExpression listLit) {
        if (disallowListLits) {
            addExprViolation(listLit, "list literal");
        }
    }

    @Override
    protected void preprocessLocationExpression(LocationExpression locRef) {
        if (disallowLocRefs) {
            addExprViolation(locRef, "location reference");
        }
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression projExpr) {
        if (disallowProjectionExprs) {
            addExprViolation(projExpr, "projection expression");
        } else {
            if (disallowProjectionExprsLists) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof ListType) {
                    addExprViolation(projExpr, "list projection expression");
                }
            }
            if (disallowProjectionExprsDicts) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof DictType) {
                    addExprViolation(projExpr, "dictionary projection expression");
                }
            }
            if (disallowProjectionExprsStrings) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof StringType) {
                    addExprViolation(projExpr, "string projection expression");
                }
            }
            if (disallowProjectionExprsTuples) {
                CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                if (ctype instanceof TupleType) {
                    addExprViolation(projExpr, "tuple projection expression");
                }
            } else {
                if (disallowProjectionExprsTuplesIndex) {
                    CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                    CifType itype = CifTypeUtils.normalizeType(projExpr.getIndex().getType());
                    if (ctype instanceof TupleType && itype instanceof IntType) {
                        addExprViolation(projExpr, "tuple index-projection expression");
                    }
                }
                if (disallowProjectionExprsTuplesField) {
                    CifType ctype = CifTypeUtils.normalizeType(projExpr.getChild().getType());
                    if (ctype instanceof TupleType && projExpr.getIndex() instanceof FieldExpression) {
                        addExprViolation(projExpr, "tuple field-projection expression");
                    }
                }
            }
        }
    }

    @Override
    protected void preprocessRealExpression(RealExpression realLit) {
        if (disallowRealLits) {
            addExprViolation(realLit, "real number literal");
        }
    }

    @Override
    protected void preprocessReceivedExpression(ReceivedExpression receivedExpr) {
        if (disallowReceiveExprs) {
            addExprViolation(receivedExpr, "received value expression");
        }
    }

    @Override
    protected void preprocessSelfExpression(SelfExpression selfRef) {
        if (disallowCompRefs || disallowCompRefsSelf) {
            addExprViolation(selfRef, "component reference");
        }
    }

    @Override
    protected void preprocessSetExpression(SetExpression setLit) {
        if (disallowSetLits) {
            addExprViolation(setLit, "set literal");
        }
    }

    @Override
    protected void preprocessSliceExpression(SliceExpression sliceExpr) {
        if (disallowSliceExprs) {
            addExprViolation(sliceExpr, "slice expression");
        }
    }

    @Override
    protected void preprocessStringExpression(StringExpression stringLit) {
        if (disallowStringLits) {
            addExprViolation(stringLit, "string literal");
        }
    }

    @Override
    protected void preprocessSwitchExpression(SwitchExpression switchExpr) {
        if (disallowSwitchExprs) {
            addExprViolation(switchExpr, "switch expression");
        }
    }

    @Override
    protected void preprocessTimeExpression(TimeExpression timeRef) {
        if (disallowTimeVarRefs) {
            addExprViolation(timeRef, "time variable reference");
        }
    }

    @Override
    protected void preprocessTupleExpression(TupleExpression tupleLit) {
        if (disallowTupleLits) {
            addExprViolation(tupleLit, "tuple literal");
        }
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression unExpr) {
        if (disallowUnExprs) {
            addExprViolation(unExpr, "unary expression");
        }
    }

    @Override
    protected void addViolation(PositionObject exprObj, String message) {
        throw new UnsupportedOperationException(); // Use addExprViolation.
    }

    /**
     * Add a violation for the given expression.
     *
     * @param expr The expression.
     * @param description The description of the expression.
     */
    private void addExprViolation(Expression expr, String description) {
        super.addViolation(getNamedAncestorOrSelf(expr), fmt("%s \"%s\"", description, exprToStr(expr)));
    }
}
