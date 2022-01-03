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

import static org.eclipse.escet.cif.common.CifScopeUtils.getRefObjFromRef;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.checkTypeCompat;
import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleType;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.common.CifTypeUtils.unwrapExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCastExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompInstWrapExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompParamWrapExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentDefType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictPair;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDistType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFieldExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFuncType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionCallExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newProjectionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newReceivedExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSelfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSetExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSetType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSliceExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStdLibFunctionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSwitchCase;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSwitchExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTauExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTimeExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.eclipse.escet.cif.typechecker.CifTypesTypeChecker.transCifType;
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.ALLOW_DIST;
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.ALLOW_EVENT;
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.NO_TIME;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.list2set;
import static org.eclipse.escet.common.java.Strings.str;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
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
import org.eclipse.escet.cif.parser.ast.expressions.ABinaryExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ABoolExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ACastExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ADictExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ADictPair;
import org.eclipse.escet.cif.parser.ast.expressions.AElifExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AEmptySetDictExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AFuncCallExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AIfExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AIntExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AListExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ANameExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AProjectionExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ARealExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AReceivedExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ASelfExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ASetExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ASliceExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AStdLibFunctionExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AStringExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ASwitchCase;
import org.eclipse.escet.cif.parser.ast.expressions.ASwitchExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ATauExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ATimeExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ATupleExpression;
import org.eclipse.escet.cif.parser.ast.expressions.AUnaryExpression;
import org.eclipse.escet.cif.typechecker.declwrap.EnumDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalEventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.TypeDeclWrap;
import org.eclipse.escet.cif.typechecker.scopes.AutDefScope;
import org.eclipse.escet.cif.typechecker.scopes.AutScope;
import org.eclipse.escet.cif.typechecker.scopes.CompInstScope;
import org.eclipse.escet.cif.typechecker.scopes.CompParamScope;
import org.eclipse.escet.cif.typechecker.scopes.GroupDefScope;
import org.eclipse.escet.cif.typechecker.scopes.SpecScope;
import org.eclipse.escet.cif.typechecker.scopes.SymbolScope;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.common.typechecker.SemanticProblem;

/** Type checker for CIF expressions. */
public class CifExprsTypeChecker {
    /** No type, to use as type checking hint. */
    public static final CifType NO_TYPE_HINT = null;

    /**
     * Boolean type, to use as type checking hint. This shared global instance has no position information, and is not
     * to be modified.
     */
    public static final BoolType BOOL_TYPE_HINT = newBoolType();

    /**
     * Integer type, to use as type checking hint. This shared global instance has no position information, has no
     * range, and is not to be modified.
     */
    public static final IntType INT_TYPE_HINT = newIntType();

    /**
     * Real type, to use as type checking hint. This shared global instance has no position information, and is not to
     * be modified.
     */
    public static final RealType REAL_TYPE_HINT = newRealType();

    /**
     * String type, to use as type checking hint. This shared global instance has no position information, and is not to
     * be modified.
     */
    public static final StringType STRING_TYPE_HINT = newStringType();

    /** Constructor for the {@link CifExprsTypeChecker} class. */
    private CifExprsTypeChecker() {
        // Static class.
    }

    /**
     * Checks whether the given expression is statically evaluable, given the static semantic constraints of the CIF
     * language. Note that for instance algebraic variables may not be statically evaluated, even if they can only have
     * a single value. Distribution functions and sampling are also explicitly excluded.
     *
     * <p>
     * Note that this method is more strict than the {@link CifValueUtils#hasSingleValue} method, which for instance
     * allows static evaluation of the algebraic variables, if the algebraic variable has a single value. For the type
     * checker, evaluation of algebraic variables is explicitly excluded by language constraints.
     * </p>
     *
     * @param expr The expression to check.
     * @param tchecker The CIF type checker to use. May be {@code null} to not throw exceptions, but use the return
     *     value instead.
     * @return Whether the expression is statically evaluable ({@code true}) or not ({@code false}).
     * @throws SemanticException If the given expression is not statically evaluable, and a type checker is provided.
     *
     * @see CifValueUtils#hasSingleValue
     * @see CifEvalUtils#eval
     */
    public static boolean checkStaticEvaluable(Expression expr, CifTypeChecker tchecker) {
        if (expr instanceof BoolExpression) {
            // Can be statically evaluated without problems.
            return true;
        } else if (expr instanceof IntExpression) {
            // Can be statically evaluated without problems.
            return true;
        } else if (expr instanceof RealExpression) {
            // Can be statically evaluated without problems.
            return true;
        } else if (expr instanceof StringExpression) {
            // Can be statically evaluated without problems.
            return true;
        } else if (expr instanceof TimeExpression) {
            // Time changes value and is thus not allowed here.
            if (tchecker == null) {
                return false;
            }
            tchecker.addProblem(ErrMsg.STATIC_EVAL_TIME, expr.getPosition());
            throw new SemanticException();
        } else if (expr instanceof CastExpression) {
            // Depends on child.
            CastExpression cexpr = (CastExpression)expr;
            return checkStaticEvaluable(cexpr.getChild(), tchecker);
        } else if (expr instanceof UnaryExpression) {
            // Depends on child.
            UnaryExpression uexpr = (UnaryExpression)expr;
            if (uexpr.getOperator() == UnaryOperator.SAMPLE) {
                if (tchecker == null) {
                    return false;
                }
                tchecker.addProblem(ErrMsg.STATIC_EVAL_SAMPLE, expr.getPosition());
                throw new SemanticException();
            }
            return checkStaticEvaluable(uexpr.getChild(), tchecker);
        } else if (expr instanceof BinaryExpression) {
            // Depends on children.
            BinaryExpression bexpr = (BinaryExpression)expr;
            if (!checkStaticEvaluable(bexpr.getLeft(), tchecker)) {
                return false;
            }
            return checkStaticEvaluable(bexpr.getRight(), tchecker);
        } else if (expr instanceof IfExpression) {
            IfExpression ifExpr = (IfExpression)expr;

            // if/then
            for (Expression guard: ifExpr.getGuards()) {
                if (!checkStaticEvaluable(guard, tchecker)) {
                    return false;
                }
            }
            if (!checkStaticEvaluable(ifExpr.getThen(), tchecker)) {
                return false;
            }

            // elif/then
            for (ElifExpression elif: ifExpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    if (!checkStaticEvaluable(guard, tchecker)) {
                        return false;
                    }
                }
                if (!checkStaticEvaluable(elif.getThen(), tchecker)) {
                    return false;
                }
            }

            // else
            return checkStaticEvaluable(ifExpr.getElse(), tchecker);
        } else if (expr instanceof SwitchExpression) {
            SwitchExpression switchExpr = (SwitchExpression)expr;
            if (!checkStaticEvaluable(switchExpr.getValue(), tchecker)) {
                return false;
            }

            for (SwitchCase cse: switchExpr.getCases()) {
                if (cse.getKey() != null) {
                    if (!checkStaticEvaluable(cse.getKey(), tchecker)) {
                        return false;
                    }
                }
                if (!checkStaticEvaluable(cse.getValue(), tchecker)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            if (!checkStaticEvaluable(pexpr.getChild(), tchecker)) {
                return false;
            }
            if (pexpr.getIndex() instanceof FieldExpression) {
                // Can be statically evaluated without problems.
                return true;
            } else {
                return checkStaticEvaluable(pexpr.getIndex(), tchecker);
            }
        } else if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;
            if (!checkStaticEvaluable(sexpr.getChild(), tchecker)) {
                return false;
            }
            if (sexpr.getBegin() != null) {
                if (!checkStaticEvaluable(sexpr.getBegin(), tchecker)) {
                    return false;
                }
            }
            if (sexpr.getEnd() != null) {
                if (!checkStaticEvaluable(sexpr.getEnd(), tchecker)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fcexpr = (FunctionCallExpression)expr;

            if (fcexpr.getFunction() instanceof StdLibFunctionExpression) {
                StdLibFunctionExpression stdlib = (StdLibFunctionExpression)fcexpr.getFunction();
                StdLibFunction func = stdlib.getFunction();
                if (CifTypeUtils.isDistFunction(func)) {
                    if (tchecker == null) {
                        return false;
                    }
                    tchecker.addProblem(ErrMsg.STATIC_EVAL_DIST, expr.getPosition(), CifTextUtils.functionToStr(func));
                    throw new SemanticException();
                }
            } else {
                // User-defined functions can't be statically evaluated, as
                // we don't know whether they ever finish evaluation.
                if (tchecker == null) {
                    return false;
                }
                Expression fexpr = fcexpr.getFunction();
                tchecker.addProblem(ErrMsg.STATIC_EVAL_FCALL_USER_DEF_FUNC, expr.getPosition(),
                        CifTextUtils.exprToStr(fexpr));
                throw new SemanticException();
            }

            for (Expression param: fcexpr.getParams()) {
                if (!checkStaticEvaluable(param, tchecker)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                if (!checkStaticEvaluable(elem, tchecker)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                if (!checkStaticEvaluable(elem, tchecker)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression elem: texpr.getFields()) {
                if (!checkStaticEvaluable(elem, tchecker)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                if (!checkStaticEvaluable(pair.getKey(), tchecker)) {
                    return false;
                }
                if (!checkStaticEvaluable(pair.getValue(), tchecker)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof ConstantExpression) {
            // Can be statically evaluated without problems.
            return true;
        } else if (expr instanceof DiscVariableExpression) {
            // Discrete variables, function parameters, and local variables of
            // functions can all either change value or get different values,
            // and are thus not allowed here.
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();
            EObject parent = var.eContainer();
            String varTypeText;
            if (parent instanceof ComplexComponent) {
                varTypeText = "discrete variable";
            } else if (parent instanceof InternalFunction) {
                varTypeText = "variable";
            } else if (parent instanceof FunctionParameter) {
                varTypeText = "function parameter";
            } else {
                String msg = "Unknown disc var parent: " + parent;
                throw new RuntimeException(msg);
            }

            // Report problem with absolute name.
            if (tchecker == null) {
                return false;
            }
            tchecker.addProblem(ErrMsg.STATIC_EVAL_DISC_VAR, expr.getPosition(), varTypeText, getAbsName(var));
            throw new SemanticException();
        } else if (expr instanceof AlgVariableExpression) {
            // Algebraic variables can change value and are thus not allowed
            // here.
            if (tchecker == null) {
                return false;
            }
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
            tchecker.addProblem(ErrMsg.STATIC_EVAL_ALG_VAR, expr.getPosition(), getAbsName(var));
            throw new SemanticException();
        } else if (expr instanceof ContVariableExpression) {
            // Continuous variables can change value and are thus not allowed
            // here. Derivatives are not allowed either.
            if (tchecker == null) {
                return false;
            }
            ContVariableExpression cvexpr = (ContVariableExpression)expr;
            ContVariable cvar = cvexpr.getVariable();
            String derTxt = cvexpr.isDerivative() ? "the derivative of " : "";
            tchecker.addProblem(ErrMsg.STATIC_EVAL_CONT_VAR, expr.getPosition(), derTxt, getAbsName(cvar));
            throw new SemanticException();
        } else if (expr instanceof TauExpression) {
            throw new RuntimeException("Tau is not relevant for static eval.");
        } else if (expr instanceof LocationExpression) {
            // Locations can change value and are thus not allowed here. Note
            // that in expressions we can only refer to named locations, and as
            // such we can ask for an absolute name for the error message.
            if (tchecker == null) {
                return false;
            }
            Location loc = ((LocationExpression)expr).getLocation();
            tchecker.addProblem(ErrMsg.STATIC_EVAL_LOC, expr.getPosition(), getAbsName(loc));
            throw new SemanticException();
        } else if (expr instanceof EnumLiteralExpression) {
            // Can be statically evaluated without problems.
            return true;
        } else if (expr instanceof EventExpression) {
            // Events may not be used as values.
            throw new RuntimeException("Event is not relevant for static eval.");
        } else if (expr instanceof FieldExpression) {
            String msg = "Unexpected field expr: proj expr should handle it.";
            throw new RuntimeException(msg);
        } else if (expr instanceof StdLibFunctionExpression) {
            // Can be statically evaluated without problems.
            return true;
        } else if (expr instanceof FunctionExpression) {
            // Can be statically evaluated without problems.
            return true;
        } else if (expr instanceof InputVariableExpression) {
            // Input variables can change value and are thus not allowed here.
            if (tchecker == null) {
                return false;
            }
            InputVariable var = ((InputVariableExpression)expr).getVariable();
            tchecker.addProblem(ErrMsg.STATIC_EVAL_INPUT_VAR, expr.getPosition(), getAbsName(var));
            throw new SemanticException();
        } else if (expr instanceof ComponentExpression) {
            // The current location of an automaton can change during
            // simulation, and automaton references are thus not allowed here.
            // Other component expressions are not allowed at all, but that
            // should have been checked.
            if (tchecker == null) {
                return false;
            }
            Component comp = ((ComponentExpression)expr).getComponent();
            Automaton aut = CifScopeUtils.getAutomaton(comp);
            tchecker.addProblem(ErrMsg.STATIC_EVAL_AUT_REF, expr.getPosition(), getAbsName(aut));
            throw new SemanticException();
        } else if (expr instanceof CompParamExpression) {
            // This always refers to the same component parameter. However,
            // we don't know what component it is instantiated with. And it
            // maybe instantiated with various different components for
            // different instantiations. As such, we don't know its value.
            if (tchecker == null) {
                return false;
            }
            ComponentParameter compParam = ((CompParamExpression)expr).getParameter();
            tchecker.addProblem(ErrMsg.STATIC_EVAL_COMP_PARAM, expr.getPosition(), getAbsName(compParam));
            throw new SemanticException();
        } else if (expr instanceof CompInstWrapExpression) {
            // Peel of the wrapper, as static evaluability of the wrapped
            // values is not influenced by the wrappers.
            Expression rexpr = ((CompInstWrapExpression)expr).getReference();
            return checkStaticEvaluable(rexpr, tchecker);
        } else if (expr instanceof CompParamWrapExpression) {
            // Peel of the wrapper, as static evaluability of the wrapped
            // values is not influenced by the wrappers.
            Expression rexpr = ((CompParamWrapExpression)expr).getReference();
            return checkStaticEvaluable(rexpr, tchecker);
        } else if (expr instanceof ReceivedExpression) {
            // Received values can change for every communication, and are thus
            // not allowed here.
            if (tchecker == null) {
                return false;
            }
            tchecker.addProblem(ErrMsg.STATIC_EVAL_RCV_VALUE, expr.getPosition());
            throw new SemanticException();
        } else if (expr instanceof SelfExpression) {
            // The current location of an automaton can change during
            // simulation, and 'self' is thus not allowed here.
            if (tchecker == null) {
                return false;
            }
            tchecker.addProblem(ErrMsg.STATIC_EVAL_SELF, expr.getPosition());
            throw new SemanticException();
        } else {
            // Unknown expr.
            throw new RuntimeException("Unknown expr: " + expr);
        }
    }

    /**
     * Transforms an expression and performs type checking on it, after type checking of the CIF specification has
     * finished (post type checking use).
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use, and in which to temporarily store reported problems, if any.
     * @param sourceFile The source file metadata for the file in which to report the problems. Must match position
     *     locations of reported problems. May be {@code null} if the caller is sure transforming won't generate any
     *     problems.
     * @return The CIF metamodel expression or {@code null} if type checking failed and did not produce a result, and
     *     any reported problems (may have warnings and errors, even if type checking produced a result).
     * @see #transExpression
     */
    public static Pair<Expression, List<SemanticProblem>> postTransExpression(AExpression expr, CifType hint,
            SymbolScope<?> scope, ExprContext context, CifTypeChecker tchecker, SourceFile sourceFile)
    {
        // Prepare the type checker for post type checking use.
        tchecker.preparePostUse();
        if (sourceFile != null) {
            tchecker.sourceFiles.add(sourceFile);
        }

        // Transform the expression.
        Expression rslt = null;
        List<SemanticProblem> problems;
        try {
            rslt = transExpression(expr, hint, scope, context, tchecker);
        } catch (SemanticException ex) {
            // Ignore.
        } finally {
            // Always finalize post use.
            problems = tchecker.finalizePostUse();
            if (sourceFile != null) {
                tchecker.sourceFiles.remove(sourceFile);
            }
        }

        // Success.
        return pair(rslt, problems);
    }

    /**
     * Transforms an expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     * @see #postTransExpression
     */
    public static Expression transExpression(AExpression expr, CifType hint, SymbolScope<?> scope, ExprContext context,
            CifTypeChecker tchecker)
    {
        if (expr instanceof ABoolExpression) {
            return transBoolExpression((ABoolExpression)expr, hint, scope);
        } else if (expr instanceof AIntExpression) {
            return transIntExpression((AIntExpression)expr, hint, scope, tchecker);
        } else if (expr instanceof ARealExpression) {
            return transRealExpression((ARealExpression)expr, hint, scope, tchecker);
        } else if (expr instanceof AStringExpression) {
            return transStringExpression((AStringExpression)expr, hint, scope);
        } else if (expr instanceof ATimeExpression) {
            return transTimeExpression((ATimeExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ACastExpression) {
            return transCastExpression((ACastExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof AUnaryExpression) {
            return transUnaryExpression((AUnaryExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ABinaryExpression) {
            return transBinaryExpression((ABinaryExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof AIfExpression) {
            return transIfExpression((AIfExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ASwitchExpression) {
            return transSwitchExpression((ASwitchExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof AProjectionExpression) {
            return transProjExpression((AProjectionExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ASliceExpression) {
            return transSliceExpression((ASliceExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof AFuncCallExpression) {
            return transFuncCallExpression((AFuncCallExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof AListExpression) {
            return transListExpression((AListExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof AEmptySetDictExpression) {
            return transEmptySetDictExpression((AEmptySetDictExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ASetExpression) {
            return transSetExpression((ASetExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ATupleExpression) {
            return transTupleExpression((ATupleExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ADictExpression) {
            return transDictExpression((ADictExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ANameExpression) {
            return transNameExpression((ANameExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ATauExpression) {
            return transTauExpression((ATauExpression)expr, hint, scope);
        } else if (expr instanceof AReceivedExpression) {
            return transReceivedExpression((AReceivedExpression)expr, hint, scope, context, tchecker);
        } else if (expr instanceof ASelfExpression) {
            return transSelfExpression((ASelfExpression)expr, hint, scope, context, tchecker);
        } else {
            throw new RuntimeException("Unknown expr: " + expr);
        }
    }

    /**
     * Transforms a boolean expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @return The CIF metamodel expression.
     */
    private static BoolExpression transBoolExpression(ABoolExpression expr, CifType hint, SymbolScope<?> scope) {
        BoolType type = newBoolType();
        type.setPosition(copyPosition(expr.position));

        BoolExpression rslt = newBoolExpression();
        rslt.setPosition(expr.position);
        rslt.setValue(expr.value);
        rslt.setType(type);

        return rslt;
    }

    /**
     * Transforms an integer expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     * @see CifMath#strToInt
     */
    private static Expression transIntExpression(AIntExpression expr, CifType hint, SymbolScope<?> scope,
            CifTypeChecker tchecker)
    {
        // Parse value.
        int value;
        try {
            value = Integer.parseInt(expr.value);
        } catch (NumberFormatException e) {
            tchecker.addProblem(ErrMsg.INT_VALUE_OVERFLOW, expr.position, Numbers.formatNumber(expr.value));
            throw new SemanticException();
        }

        // Construct metamodel representation.
        IntExpression intExpr = newIntExpression();
        intExpr.setPosition(expr.position);
        intExpr.setValue(value);

        // Set type. Ignore integer type hints, including their range bounds.
        IntType type = newIntType();
        type.setPosition(copyPosition(expr.position));
        type.setLower(value);
        type.setUpper(value);
        intExpr.setType(type);

        // Automatically widen to 'real' if provided as type hint.
        CifType nhint = normalizeHint(hint);
        if (nhint instanceof RealType) {
            // Change to real literal, as that is simpler, shorter, and easier
            // to read than a cast.
            Assert.check(value >= 0);
            RealExpression realExpr = newRealExpression();
            realExpr.setValue(str(value) + ".0");
            realExpr.setPosition(intExpr.getPosition());
            realExpr.setType(newRealType());
            return realExpr;
        }

        // Return integer literal metamodel representation.
        return intExpr;
    }

    /**
     * Transforms a real expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     * @see CifMath#strToReal
     */
    private static RealExpression transRealExpression(ARealExpression expr, CifType hint, SymbolScope<?> scope,
            CifTypeChecker tchecker)
    {
        double value;
        try {
            value = Double.parseDouble(expr.value);
            Assert.check(!Double.isNaN(value));
        } catch (NumberFormatException e) {
            // Should never happen for results from the CIF scanner.
            throw new RuntimeException(e);
        }

        if (Double.isInfinite(value)) {
            tchecker.addProblem(ErrMsg.REAL_VALUE_OVERFLOW, expr.position, expr.value);
            throw new SemanticException();
        }

        Assert.check(value >= 0.0);

        RealType type = newRealType();
        type.setPosition(copyPosition(expr.position));

        RealExpression rslt = newRealExpression();
        rslt.setPosition(expr.position);
        rslt.setValue(expr.value);
        rslt.setType(type);
        return rslt;
    }

    /**
     * Transforms a string expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @return The CIF metamodel expression.
     */
    private static StringExpression transStringExpression(AStringExpression expr, CifType hint, SymbolScope<?> scope) {
        StringType type = newStringType();
        type.setPosition(copyPosition(expr.position));

        StringExpression rslt = newStringExpression();
        rslt.setPosition(expr.position);
        rslt.setValue(expr.value);
        rslt.setType(type);

        return rslt;
    }

    /**
     * Transforms a time expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static TimeExpression transTimeExpression(ATimeExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Check for invalid use.
        if (context != null && context.conditions.contains(NO_TIME)) {
            tchecker.addProblem(ErrMsg.TIME_IN_FUNC, expr.position);
            throw new SemanticException();
        }

        // Transform.
        RealType type = newRealType();
        type.setPosition(copyPosition(expr.position));

        TimeExpression rslt = newTimeExpression();
        rslt.setPosition(expr.position);
        rslt.setType(type);

        return rslt;
    }

    /**
     * Transforms a cast expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static CastExpression transCastExpression(ACastExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Transform target type.
        CifType ttype = transCifType(expr.type, scope, tchecker);
        CifType nttype = CifTypeUtils.normalizeType(ttype);

        // Get type hint for child, based on cast target type. Ignore result
        // type hint.
        CifType childHint = null;
        if (nttype instanceof BoolType) {
            // t (= bool) -> t (= bool)
            // string -> bool
        } else if (nttype instanceof IntType && isRangeless((IntType)nttype)) {
            // t (= int (rangeless)) -> t (= int (rangeless))
            // string -> int (rangeless)
        } else if (nttype instanceof RealType) {
            // t (= real) -> t (= real)
            // int -> real
            // string -> real
        } else if (nttype instanceof StringType) {
            // t (= string) -> t (= string)
            // bool -> string
            // int -> string
            // real -> string
            // automaton (self) reference -> string
        } else {
            // t -> t
            childHint = ttype;
        }

        // Transform child.
        Expression child = transExpression(expr.child, childHint, scope, context, tchecker);
        CifType ctype = child.getType();
        CifType nctype = CifTypeUtils.normalizeType(ctype);

        // Check allowed child and result type combinations.
        boolean allowed = false;
        if (checkTypeCompat(nctype, nttype, RangeCompat.EQUAL)) {
            // t -> t
            allowed = true;
        } else if (nttype instanceof BoolType) {
            // string -> bool
            if (nctype instanceof StringType) {
                allowed = true;
            }
        } else if (nttype instanceof IntType && isRangeless((IntType)nttype)) {
            // string -> int (rangeless)
            if (nctype instanceof StringType) {
                allowed = true;
            }
        } else if (nttype instanceof RealType) {
            // int -> real
            // string -> real
            if (nctype instanceof IntType) {
                allowed = true;
            }
            if (nctype instanceof StringType) {
                allowed = true;
            }
        } else if (nttype instanceof StringType) {
            // bool -> string
            // int -> string
            // real -> string
            // automaton (self) reference -> string
            if (nctype instanceof BoolType) {
                allowed = true;
            }
            if (nctype instanceof IntType) {
                allowed = true;
            }
            if (nctype instanceof RealType) {
                allowed = true;
            }
            if (CifTypeUtils.isAutRefExpr(child)) {
                allowed = true;
            }
        }

        if (!allowed) {
            tchecker.addProblem(ErrMsg.CAST_INVALID_TYPES, expr.position, CifTextUtils.typeToStr(ctype),
                    CifTextUtils.typeToStr(ttype));
            throw new SemanticException();
        }

        // Create cast expression.
        CastExpression rslt = newCastExpression();
        rslt.setChild(child);
        rslt.setPosition(expr.position);
        rslt.setType(ttype);
        return rslt;
    }

    /** Mapping of unary operators from ASCII representation to metamodel representation. */
    private static final Map<String, UnaryOperator> UNOP_MAP;

    static {
        UNOP_MAP = map();

        UNOP_MAP.put("not", UnaryOperator.INVERSE);
        UNOP_MAP.put("-", UnaryOperator.NEGATE);
        UNOP_MAP.put("+", UnaryOperator.PLUS);
        UNOP_MAP.put("sample", UnaryOperator.SAMPLE);
    }

    /**
     * Transforms a unary expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static UnaryExpression transUnaryExpression(AUnaryExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Transform operator.
        UnaryOperator op = UNOP_MAP.get(expr.operator);
        Assert.notNull(op);

        // Get type hint for child.
        CifType nhint = normalizeHint(hint);
        CifType childHint = null;
        switch (op) {
            case INVERSE:
                // bool -> bool
                childHint = BOOL_TYPE_HINT;
                break;

            case NEGATE:
                // int[l..u] -> int[-u..-l]
                // int -> int
                // real -> real

                // Assumes type ranges are ignored.
                childHint = hint;
                break;

            case PLUS:
                // int[l..u] -> int[l..u]
                // int -> int
                // real -> real
                childHint = hint;
                break;

            case SAMPLE:
                // dist t -> tuple(t, dist t)
                if (nhint instanceof TupleType) {
                    TupleType ttype = (TupleType)nhint;
                    if (ttype.getFields().size() == 2) {
                        childHint = ttype.getFields().get(1).getType();
                    }
                }
                break;
        }

        // Transform child.
        Expression child = transExpression(expr.child, childHint, scope, context, tchecker);

        // Initialize result.
        UnaryExpression rslt = newUnaryExpression();
        rslt.setPosition(expr.position);
        rslt.setChild(child);
        rslt.setOperator(op);

        // Type of the result depends on the operator.
        CifType ctype = child.getType();
        CifType nctype = CifTypeUtils.normalizeType(ctype);

        switch (op) {
            case INVERSE: {
                // bool -> bool

                if (!(nctype instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.UNOP_INVALID_CHILD_TYPE, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ctype));
                    throw new SemanticException();
                }

                rslt.setType(deepclone(ctype));
                break;
            }

            case NEGATE: {
                // int[l..u] -> int[-u..-l]
                // int -> int
                // real -> real

                if (!(nctype instanceof IntType) && !(nctype instanceof RealType)) {
                    tchecker.addProblem(ErrMsg.UNOP_INVALID_CHILD_TYPE, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ctype));
                    throw new SemanticException();
                }

                if (nctype instanceof IntType) {
                    IntType itype = (IntType)nctype;

                    IntType type = newIntType();
                    type.setPosition(copyPosition(expr.position));

                    rslt.setType(type);

                    if (!isRangeless(itype)) {
                        int lower = itype.getLower();
                        int upper = itype.getUpper();

                        if (lower == Integer.MIN_VALUE) {
                            // Possible overflow.
                            tchecker.addProblem(ErrMsg.UNOP_NEGATE_OVERFLOW, expr.position,
                                    CifTextUtils.typeToStr(ctype));
                            throw new SemanticException();
                        }

                        type.setLower(-upper);
                        type.setUpper(-lower);
                    }
                } else {
                    RealType type = newRealType();
                    type.setPosition(copyPosition(expr.position));
                    rslt.setType(type);
                }

                break;
            }

            case PLUS: {
                // int[l..u] -> int[l..u]
                // int -> int
                // real -> real

                if (!(nctype instanceof IntType) && !(nctype instanceof RealType)) {
                    tchecker.addProblem(ErrMsg.UNOP_INVALID_CHILD_TYPE, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ctype));
                    throw new SemanticException();
                }

                rslt.setType(deepclone(ctype));
                break;
            }

            case SAMPLE: {
                // dist t -> tuple(t, dist t)

                if (!(nctype instanceof DistType)) {
                    tchecker.addProblem(ErrMsg.UNOP_INVALID_CHILD_TYPE, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ctype));
                    throw new SemanticException();
                }

                DistType dtype = (DistType)nctype;
                CifType type = makeTupleType(list(dtype.getSampleType(), ctype));
                rslt.setType(deepclone(type));

                break;
            }
        }

        // All done.
        return rslt;
    }

    /** Mapping of binary operators from ASCII representation to metamodel representation. */
    private static final Map<String, BinaryOperator> BINOP_MAP;

    static {
        BINOP_MAP = map();

        BINOP_MAP.put("=>", BinaryOperator.IMPLICATION);
        BINOP_MAP.put("<=>", BinaryOperator.BI_CONDITIONAL);
        BINOP_MAP.put("or", BinaryOperator.DISJUNCTION);
        BINOP_MAP.put("and", BinaryOperator.CONJUNCTION);
        BINOP_MAP.put("<", BinaryOperator.LESS_THAN);
        BINOP_MAP.put("<=", BinaryOperator.LESS_EQUAL);
        BINOP_MAP.put(">", BinaryOperator.GREATER_THAN);
        BINOP_MAP.put(">=", BinaryOperator.GREATER_EQUAL);
        BINOP_MAP.put("=", BinaryOperator.EQUAL);
        BINOP_MAP.put("!=", BinaryOperator.UNEQUAL);
        BINOP_MAP.put("+", BinaryOperator.ADDITION);
        BINOP_MAP.put("-", BinaryOperator.SUBTRACTION);
        BINOP_MAP.put("*", BinaryOperator.MULTIPLICATION);
        BINOP_MAP.put("/", BinaryOperator.DIVISION);
        BINOP_MAP.put("div", BinaryOperator.INTEGER_DIVISION);
        BINOP_MAP.put("mod", BinaryOperator.MODULUS);
        BINOP_MAP.put("sub", BinaryOperator.SUBSET);
        BINOP_MAP.put("in", BinaryOperator.ELEMENT_OF);
    }

    /**
     * Transforms a binary expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static BinaryExpression transBinaryExpression(ABinaryExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Transform operator.
        BinaryOperator op = BINOP_MAP.get(expr.operator);
        Assert.notNull(op);

        // Get type hint for left child.
        CifType nhint = normalizeHint(hint);
        CifType leftHint = null;
        switch (op) {
            case IMPLICATION:
            case BI_CONDITIONAL:
                // bool, bool -> bool
                leftHint = hint;
                break;

            case CONJUNCTION:
            case DISJUNCTION:
                // bool, bool -> bool
                // set t, set t -> set t
                leftHint = hint;
                break;

            case LESS_THAN:
            case LESS_EQUAL:
            case GREATER_THAN:
            case GREATER_EQUAL:
                // int/real, int/real -> bool
                break;

            case EQUAL:
            case UNEQUAL:
                // t, t -> bool
                break;

            case ADDITION:
                // int[l1..u1], int[l2..u2] -> int[l1+l2..u1+u2]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real
                // list t, list t -> list t
                // list[l1..u1] t, list[l2..u2] t -> list[l1+l2..u1+u2] t
                // string, string -> string
                // dict(k:v), dict(k:v) -> dict(k:v)

                // Assumes type ranges are ignored.
                leftHint = hint;
                break;

            case SUBTRACTION:
                // int[l1..u1], int[l2..u2] -> int[l1-u2..u1-l2]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real
                // set t, set t -> set t
                // dict(k:v), dict(k:v) -> dict(k:v)
                // dict(k:v), set k -> dict(k:v)
                // dict(k:v), list k -> dict(k:v)
                // dict(k:v), list[l..u] k -> dict(k:v)

                // Assumes type ranges are ignored.
                leftHint = hint;
                break;

            case MULTIPLICATION:
                // int[l1..u1], int[l2..u2] -> int[ min(l1*l2, l1*u2, u1*l2, u1*u2) .. max(l1*l2, l1*u2, u1*l2, u1*u2) ]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real

                // Assumes type ranges are ignored.
                leftHint = hint;
                break;

            case DIVISION:
                // int/real, int/real -> real
                break;

            case INTEGER_DIVISION:
                // int[l1..u1], int[l2..u2] -> int[
                // min{x div y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0} ..
                // max{x div y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0}
                // ]
                //
                // int, int -> int

                // Assumes type ranges are ignored.
                leftHint = hint;
                break;

            case MODULUS:
                // int[l1..u1], int[l2..u2] -> int[
                // min{x mod y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0} ..
                // max{x mod y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0}
                // ]
                //
                // int, int -> int

                // Assumes type ranges are ignored.
                leftHint = hint;
                break;

            case ELEMENT_OF:
                // t, list t -> bool
                // t, list[l..u] t -> bool
                // t, set t -> bool
                // k, dict(k:v) -> bool
                break;

            case SUBSET:
                // set t, set t -> bool

                // Can't provide a set type, as the child type is unknown.
                break;
        }

        // Transform left child.
        Expression left = transExpression(expr.left, leftHint, scope, context, tchecker);
        CifType ltype = left.getType();
        CifType nltype = CifTypeUtils.normalizeType(ltype);

        // Get type hint for right child.
        CifType rightHint = null;
        switch (op) {
            case IMPLICATION:
            case BI_CONDITIONAL:
                // bool, bool -> bool
                rightHint = ltype;
                break;

            case CONJUNCTION:
            case DISJUNCTION:
                // bool, bool -> bool
                // set t, set t -> set t
                rightHint = ltype;
                break;

            case LESS_THAN:
            case LESS_EQUAL:
            case GREATER_THAN:
            case GREATER_EQUAL:
                // int/real, int/real -> bool
                break;

            case EQUAL:
            case UNEQUAL:
                // t, t -> bool
                rightHint = ltype;
                break;

            case ADDITION:
                // int[l1..u1], int[l2..u2] -> int[l1+l2..u1+u2]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real
                // list t, list t -> list t
                // list[l1..u1] t, list[l2..u2] t -> list[l1+l2..u1+u2] t
                // string, string -> string
                // dict(k:v), dict(k:v) -> dict(k:v)

                // Assumes type ranges are ignored.
                if (nhint instanceof IntType || nhint instanceof RealType) {
                    rightHint = hint;
                } else {
                    rightHint = ltype;
                }
                break;

            case SUBTRACTION:
                // int[l1..u1], int[l2..u2] -> int[l1-u2..u1-l2]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real
                // set t, set t -> set t
                // dict(k:v), dict(k:v) -> dict(k:v)
                // dict(k:v), set k -> dict(k:v)
                // dict(k:v), list k -> dict(k:v)

                // Assumes type ranges are ignored.
                if (nhint instanceof IntType || nhint instanceof RealType) {
                    rightHint = hint;
                } else if (!(nhint instanceof DictType)) {
                    rightHint = ltype;
                }
                break;

            case MULTIPLICATION:
                // int[l1..u1], int[l2..u2] -> int[ min(l1*l2, l1*u2, u1*l2, u1*u2) .. max(l1*l2, l1*u2, u1*l2, u1*u2) ]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real

                // Assumes type ranges are ignored.
                rightHint = hint;
                break;

            case DIVISION:
                // int/real, int/real -> real
                break;

            case INTEGER_DIVISION:
                // int[l1..u1], int[l2..u2] -> int[
                // min{x div y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0} ..
                // max{x div y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0}
                // ]
                //
                // int, int -> int

                // Assumes type ranges are ignored.
                rightHint = hint;
                break;

            case MODULUS:
                // int[l1..u1], int[l2..u2] -> int[
                // min{x mod y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0} ..
                // max{x mod y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0}
                // ]
                //
                // int, int -> int

                // Assumes type ranges are ignored.
                rightHint = hint;
                break;

            case ELEMENT_OF:
                // t, list t -> bool
                // t, list[l..u] t -> bool
                // t, set t -> bool
                // k, dict(k:v) -> bool
                break;

            case SUBSET:
                // set t, set t -> bool
                rightHint = ltype;
                break;
        }

        // Transform right child.
        Expression right = transExpression(expr.right, rightHint, scope, context, tchecker);
        CifType rtype = right.getType();
        CifType nrtype = CifTypeUtils.normalizeType(rtype);

        // Initialize result.
        BinaryExpression rslt = newBinaryExpression();
        rslt.setPosition(expr.position);
        rslt.setLeft(left);
        rslt.setRight(right);
        rslt.setOperator(op);

        // Type of the result depends on the operator.
        switch (op) {
            case IMPLICATION:
            case BI_CONDITIONAL: {
                // bool, bool -> bool

                if (!(nltype instanceof BoolType) || !(nrtype instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                BoolType type = newBoolType();
                type.setPosition(copyPosition(expr.position));

                rslt.setType(type);
                break;
            }

            case CONJUNCTION:
            case DISJUNCTION: {
                boolean allowed = false;
                if (nltype instanceof BoolType && nrtype instanceof BoolType) {
                    // bool, bool -> bool
                    allowed = true;
                } else if (nltype instanceof SetType && nrtype instanceof SetType) {
                    // set t, set t -> set t
                    SetType lstype = (SetType)nltype;
                    SetType rstype = (SetType)nrtype;
                    allowed = checkTypeCompat(lstype.getElementType(), rstype.getElementType(), RangeCompat.IGNORE);
                }

                if (!allowed) {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                rslt.setType(CifTypeUtils.mergeTypes(ltype, rtype));
                break;
            }

            case LESS_THAN:
            case LESS_EQUAL:
            case GREATER_THAN:
            case GREATER_EQUAL: {
                // int/real, int/real -> bool

                if ((!(nltype instanceof IntType) && !(nltype instanceof RealType))
                        || (!(nrtype instanceof IntType) && !(nrtype instanceof RealType)))
                {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                BoolType type = newBoolType();
                type.setPosition(copyPosition(expr.position));

                rslt.setType(type);
                break;
            }

            case EQUAL:
            case UNEQUAL: {
                // t, t -> bool

                if (!CifTypeUtils.supportsValueEquality(ltype) || !CifTypeUtils.supportsValueEquality(rtype)
                        || !checkTypeCompat(ltype, rtype, RangeCompat.IGNORE))
                {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                BoolType type = newBoolType();
                type.setPosition(copyPosition(expr.position));

                rslt.setType(type);
                break;
            }

            case ADDITION: {
                // Figure out result type based on left/right types.
                CifType resultType = null;
                if (nltype instanceof IntType && nrtype instanceof IntType) {
                    // int[l1..u1], int[l2..u2] -> int[l1+l2..u1+u2]
                    // int, int -> int
                    IntType type = newIntType();
                    type.setPosition(copyPosition(expr.position));

                    resultType = type;

                    IntType litype = (IntType)nltype;
                    IntType ritype = (IntType)nrtype;
                    if (!isRangeless(litype) && !isRangeless(ritype)) {
                        long l1 = litype.getLower();
                        long u1 = litype.getUpper();
                        long l2 = ritype.getLower();
                        long u2 = ritype.getUpper();

                        long l = l1 + l2;
                        long u = u1 + u2;

                        if (l < Integer.MIN_VALUE || u > Integer.MAX_VALUE) {
                            tchecker.addProblem(ErrMsg.BINOP_OVERFLOW, expr.position, expr.operator,
                                    CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                            throw new SemanticException();
                        }

                        type.setLower((int)l);
                        type.setUpper((int)u);
                    }
                } else if ((nltype instanceof IntType || nltype instanceof RealType)
                        && (nrtype instanceof IntType || nrtype instanceof RealType))
                {
                    // int, real -> real
                    // real, int -> real
                    // real, real -> real
                    RealType type = newRealType();
                    type.setPosition(copyPosition(expr.position));
                    resultType = type;
                } else if (nltype instanceof ListType && nrtype instanceof ListType) {
                    // list t, list t -> list t
                    // list[l1..u1] t, list[l2..u2] t -> list[l1+l2..u1+u2] t
                    if (checkTypeCompat(ltype, rtype, RangeCompat.IGNORE)) {
                        resultType = CifTypeUtils.mergeTypes(ltype, rtype);
                    }

                    ListType lltype = (ListType)nltype;
                    ListType rltype = (ListType)nrtype;
                    if (resultType != null && !CifTypeUtils.isRangeless(lltype) && !CifTypeUtils.isRangeless(rltype)) {
                        long l1 = lltype.getLower();
                        long u1 = lltype.getUpper();
                        long l2 = rltype.getLower();
                        long u2 = rltype.getUpper();

                        long l = l1 + l2;
                        long u = u1 + u2;

                        if (u > Integer.MAX_VALUE) {
                            tchecker.addProblem(ErrMsg.BINOP_OVERFLOW, expr.position, expr.operator,
                                    CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                            throw new SemanticException();
                        }

                        ((ListType)resultType).setLower((int)l);
                        ((ListType)resultType).setUpper((int)u);
                    }
                } else if (nltype instanceof StringType && nrtype instanceof StringType) {
                    // string, string -> string
                    resultType = CifTypeUtils.mergeTypes(ltype, rtype);
                } else if (nltype instanceof DictType && nrtype instanceof DictType) {
                    // dict(k:v), dict(k:v) -> dict(k:v)
                    if (checkTypeCompat(ltype, rtype, RangeCompat.IGNORE)) {
                        resultType = CifTypeUtils.mergeTypes(ltype, rtype);
                    }
                }

                // If not a valid combination, give error.
                if (resultType == null) {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                // Set the result type on the result.
                rslt.setType(resultType);

                break;
            }

            case SUBTRACTION: {
                // Figure out result type based on left/right types.
                CifType resultType = null;
                if (nltype instanceof IntType && nrtype instanceof IntType) {
                    // int[l1..u1], int[l2..u2] -> int[l1-u2..u1-l2]
                    // int, int -> int

                    IntType type = newIntType();
                    type.setPosition(copyPosition(expr.position));

                    resultType = type;

                    IntType litype = (IntType)nltype;
                    IntType ritype = (IntType)nrtype;
                    if (!isRangeless(litype) && !isRangeless(ritype)) {
                        long l1 = litype.getLower();
                        long u1 = litype.getUpper();
                        long l2 = ritype.getLower();
                        long u2 = ritype.getUpper();

                        long l = l1 - u2;
                        long u = u1 - l2;

                        if (l < Integer.MIN_VALUE || u > Integer.MAX_VALUE) {
                            tchecker.addProblem(ErrMsg.BINOP_OVERFLOW, expr.position, expr.operator,
                                    CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                            throw new SemanticException();
                        }

                        type.setLower((int)l);
                        type.setUpper((int)u);
                    }
                } else if ((nltype instanceof IntType || nltype instanceof RealType)
                        && (nrtype instanceof IntType || nrtype instanceof RealType))
                {
                    // int, real -> real
                    // real, int -> real
                    // real, real -> real
                    RealType type = newRealType();
                    type.setPosition(copyPosition(expr.position));
                    resultType = type;
                } else if (nltype instanceof SetType && nrtype instanceof SetType) {
                    // set t, set t -> set t
                    if (checkTypeCompat(ltype, rtype, RangeCompat.IGNORE)) {
                        resultType = CifTypeUtils.mergeTypes(ltype, rtype);
                    }
                } else if (nltype instanceof DictType && nrtype instanceof DictType) {
                    // dict(k:v), dict(k:v) -> dict(k:v)
                    if (checkTypeCompat(ltype, rtype, RangeCompat.IGNORE)) {
                        resultType = CifTypeUtils.mergeTypes(ltype, rtype);
                    }
                } else if (nltype instanceof DictType && nrtype instanceof SetType) {
                    // dict(k:v), set(k) -> dict(k:v)
                    DictType dtype = (DictType)nltype;
                    SetType stype = (SetType)nrtype;
                    if (checkTypeCompat(dtype.getKeyType(), stype.getElementType(), RangeCompat.IGNORE)) {
                        resultType = deepclone(ltype);
                    }
                } else if (nltype instanceof DictType && nrtype instanceof ListType) {
                    // dict(k:v), list k -> dict(k:v)
                    // dict(k:v), list[l..u] k -> dict(k:v)
                    DictType dtype = (DictType)nltype;
                    ListType listType = (ListType)nrtype;
                    if (checkTypeCompat(dtype.getKeyType(), listType.getElementType(), RangeCompat.IGNORE)) {
                        resultType = deepclone(ltype);
                    }
                }

                // If not a valid combination, give error.
                if (resultType == null) {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                // Set the result type on the result.
                rslt.setType(resultType);

                break;
            }

            case MULTIPLICATION: {
                // Figure out result type based on left/right types.
                CifType resultType = null;
                if (nltype instanceof IntType && nrtype instanceof IntType) {
                    // int[l1..u1], int[l2..u2] -> int[
                    // min(l1*l2, l1*u2, u1*l2, u1*u2).. max(l1*l2, l1*u2, u1*l2, u1*u2)
                    // ]
                    //
                    // int, int -> int

                    IntType type = newIntType();
                    type.setPosition(copyPosition(expr.position));

                    resultType = type;

                    IntType litype = (IntType)nltype;
                    IntType ritype = (IntType)nrtype;
                    if (!isRangeless(litype) && !isRangeless(ritype)) {
                        long l1 = litype.getLower();
                        long u1 = litype.getUpper();
                        long l2 = ritype.getLower();
                        long u2 = ritype.getUpper();

                        long l = min(l1 * l2, l1 * u2, u1 * l2, u1 * u2);
                        long u = max(l1 * l2, l1 * u2, u1 * l2, u1 * u2);

                        if (l < Integer.MIN_VALUE || u > Integer.MAX_VALUE) {
                            tchecker.addProblem(ErrMsg.BINOP_OVERFLOW, expr.position, expr.operator,
                                    CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                            throw new SemanticException();
                        }

                        type.setLower((int)l);
                        type.setUpper((int)u);
                    }
                } else if ((nltype instanceof IntType || nltype instanceof RealType)
                        && (nrtype instanceof IntType || nrtype instanceof RealType))
                {
                    // int, real -> real
                    // real, int -> real
                    // real, real -> real
                    RealType type = newRealType();
                    type.setPosition(copyPosition(expr.position));
                    resultType = type;
                }

                // If not a valid combination, give error.
                if (resultType == null) {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                // Set the result type on the result.
                rslt.setType(resultType);

                break;
            }

            case DIVISION: {
                // int/real, int/real -> real

                if ((!(nltype instanceof IntType) && !(nltype instanceof RealType))
                        || (!(nrtype instanceof IntType) && !(nrtype instanceof RealType)))
                {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                RealType type = newRealType();
                type.setPosition(copyPosition(expr.position));

                rslt.setType(type);
                break;
            }

            case INTEGER_DIVISION: {
                // int[l1..u1], int[l2..u2] -> int[
                // min{x div y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0} ..
                // max{x div y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0}
                // ]
                //
                // int, int -> int

                if (!(nltype instanceof IntType) || !(nrtype instanceof IntType)) {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                IntType type = newIntType();
                type.setPosition(copyPosition(expr.position));

                rslt.setType(type);

                IntType litype = (IntType)nltype;
                IntType ritype = (IntType)nrtype;
                if (!isRangeless(litype) && !isRangeless(ritype)) {
                    int l1 = litype.getLower();
                    int u1 = litype.getUpper();
                    int l2 = ritype.getLower();
                    int u2 = ritype.getUpper();

                    if (l2 == 0 && u2 == 0) {
                        // Always division by zero error.
                        tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                                CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                        throw new SemanticException();
                    }

                    int[] resultRange = getDivResultRange(l1, u1, l2, u2);

                    type.setLower(resultRange[0]);
                    type.setUpper(resultRange[1]);
                }

                break;
            }

            case MODULUS: {
                // int[l1..u1], int[l2..u2] -> int[
                // min{x mod y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0} ..
                // max{x mod y | x in [l1 .. u1], y in [l2 .. u2 ], y != 0}
                // ]
                //
                // int, int -> int

                if (!(nltype instanceof IntType) || !(nrtype instanceof IntType)) {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                IntType type = newIntType();
                type.setPosition(copyPosition(expr.position));

                rslt.setType(type);

                IntType litype = (IntType)nltype;
                IntType ritype = (IntType)nrtype;
                if (!isRangeless(litype) && !isRangeless(ritype)) {
                    int l1 = litype.getLower();
                    int u1 = litype.getUpper();
                    int l2 = ritype.getLower();
                    int u2 = ritype.getUpper();

                    if (l2 == 0 && u2 == 0) {
                        // Always division by zero error.
                        tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                                CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                        throw new SemanticException();
                    }

                    int[] resultRange = getModResultRange(l1, u1, l2, u2);

                    type.setLower(resultRange[0]);
                    type.setUpper(resultRange[1]);
                }

                break;
            }

            case ELEMENT_OF: {
                // Figure out result type based on left/right types.
                CifType resultType = null;
                if (nrtype instanceof ListType) {
                    // t, list t -> bool
                    // t, list[l..u] t -> bool

                    ListType listType = (ListType)nrtype;
                    if (checkTypeCompat(ltype, listType.getElementType(), RangeCompat.IGNORE)
                            && CifTypeUtils.supportsValueEquality(ltype))
                    {
                        resultType = newBoolType();
                        resultType.setPosition(copyPosition(expr.position));
                    }
                } else if (nrtype instanceof SetType) {
                    // t, set t -> bool

                    SetType stype = (SetType)nrtype;
                    if (checkTypeCompat(ltype, stype.getElementType(), RangeCompat.IGNORE)) {
                        resultType = newBoolType();
                        resultType.setPosition(copyPosition(expr.position));
                    }
                } else if (nrtype instanceof DictType) {
                    // k, dict(k:v) -> bool

                    DictType dtype = (DictType)nrtype;
                    if (checkTypeCompat(ltype, dtype.getKeyType(), RangeCompat.IGNORE)) {
                        resultType = newBoolType();
                        resultType.setPosition(copyPosition(expr.position));
                    }
                }

                // If not a valid combination, give error.
                if (resultType == null) {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                // Set the result type on the result.
                rslt.setType(resultType);

                break;
            }

            case SUBSET: {
                // set t, set t -> bool

                if (!(nltype instanceof SetType) || !(nrtype instanceof SetType)
                        || !checkTypeCompat(ltype, rtype, RangeCompat.IGNORE))
                {
                    tchecker.addProblem(ErrMsg.BINOP_INVALID_TYPES, expr.position, expr.operator,
                            CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
                    throw new SemanticException();
                }

                CifType type = newBoolType();
                type.setPosition(copyPosition(expr.position));

                rslt.setType(type);
                break;
            }
        }

        // All done.
        return rslt;
    }

    /**
     * Returns the result range for the 'div' function, given the input ranges.
     *
     * @param l1 The lower bound of the left child input range.
     * @param u1 The upper bound of the left child input range.
     * @param l2 The lower bound of the right child input range.
     * @param u2 The upper bound of the right child input range.
     * @return The lower and upper bound of the result range.
     */
    public static int[] getDivResultRange(int l1, int u1, int l2, int u2) {
        List<Integer> range1 = list(l1, u1);
        List<Integer> range2 = list(l2, u2);

        if (l1 <= Integer.MIN_VALUE + 1 && Integer.MIN_VALUE + 1 <= u2) {
            range1.add(Integer.MIN_VALUE + 1);
        }

        if (l2 <= -2 && -2 <= u2) {
            range2.add(-2);
        }
        if (l2 <= -1 && -1 <= u2) {
            range2.add(-1);
        }
        if (l2 <= 1 && 1 <= u2) {
            range2.add(1);
        }

        int minimum = Integer.MAX_VALUE;
        int maximum = Integer.MIN_VALUE;
        for (int x: range1) {
            for (int y: range2) {
                if (y == 0) {
                    continue; // Skip divide by zero.
                }
                if (x == Integer.MIN_VALUE && y == -1) {
                    continue; // Skip overlfow.
                }

                int rslt;
                try {
                    rslt = CifMath.div(x, y, null);
                } catch (CifEvalException e) {
                    // We should have checked for invalid input above.
                    throw new RuntimeException(e);
                }

                minimum = Math.min(minimum, rslt);
                maximum = Math.max(maximum, rslt);
            }
        }
        return new int[] {minimum, maximum};
    }

    /**
     * Returns the result range for the 'mod' function, given the input ranges.
     *
     * @param l1 The lower bound of the left child input range.
     * @param u1 The upper bound of the left child input range.
     * @param l2 The lower bound of the right child input range.
     * @param u2 The upper bound of the right child input range.
     * @return The lower and upper bound of the result range.
     */
    public static int[] getModResultRange(int l1, int u1, int l2, int u2) {
        // Get maximum value of second range.
        int max = Math.max(Math.abs(l2), Math.abs(u2));

        // Take into account that result is always of same sign as first range,
        // and that maximum value itself can never occur. Also, compensate
        // for values around zero, to make sure we don't to go the other
        // side of zero.
        int minimum = (l1 < 0) ? Math.min(0, -max + 1) : 0;
        int maximum = (u1 >= 0) ? Math.max(0, max - 1) : 0;
        return new int[] {minimum, maximum};
    }

    /**
     * Transforms a projection expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static ProjectionExpression transProjExpression(AProjectionExpression expr, CifType hint,
            SymbolScope<?> scope, ExprContext context, CifTypeChecker tchecker)
    {
        // Get type hint for child.
        // list t, int -> t
        // list t, int[a..b] -> t
        // list[l..u] t, int -> t
        // list[l..u] t, int[a..b] -> t
        // dict(k:v), k -> v
        // string, int -> string
        // tuple(t1 f1, t2 f2, ..., tn fn), int i -> ti
        // tuple(t1 f1, t2 f2, ..., tn fn), field fi -> ti
        CifType childHint = null;

        // Transform child.
        Expression child = transExpression(expr.child, childHint, scope, context, tchecker);
        CifType ctype = child.getType();
        CifType nctype = CifTypeUtils.normalizeType(ctype);

        // Check child type.
        if (!(nctype instanceof ListType) && !(nctype instanceof DictType) && !(nctype instanceof StringType)
                && !(nctype instanceof TupleType))
        {
            tchecker.addProblem(ErrMsg.PROJ_CHILD_TYPE, expr.position, CifTextUtils.typeToStr(ctype));
            throw new SemanticException();
        }

        // Special scoping rules for tuple field projections.
        Expression index = null;
        if (nctype instanceof TupleType && expr.index instanceof ANameExpression) {
            // tuple(t1 f1, t2 f2, ..., tn fn), field fi -> ti

            // Get tuple field name.
            ANameExpression nameExpr = (ANameExpression)expr.index;
            String name = nameExpr.name.name;

            // Look for matching field in tuple type.
            TupleType ttype = (TupleType)nctype;
            for (int i = 0; i < ttype.getFields().size(); i++) {
                Field field = ttype.getFields().get(i);
                if (field.getName() == null) {
                    continue;
                }

                if (field.getName().equals(name)) {
                    // Found a matching field.
                    IntType type = newIntType();
                    type.setPosition(copyPosition(nameExpr.position));
                    type.setLower(i);
                    type.setUpper(i);

                    FieldExpression fieldRef = newFieldExpression();
                    fieldRef.setField(field);
                    fieldRef.setPosition(nameExpr.position);
                    fieldRef.setType(type);
                    index = fieldRef;

                    // Make sure the field reference is not a derivative
                    // reference.
                    if (nameExpr.derivative) {
                        // Derivative of field not allowed.
                        tchecker.addProblem(ErrMsg.DER_OF_NON_CONT_VAR, nameExpr.position, "tuple field ", name);
                        // Non-fatal error.
                    }

                    // We found a match, so stop looking.
                    break;
                }
            }
        }

        // Transform index, if not already done.
        if (index == null) {
            // Get type hint for index.
            // list t, int -> t
            // list t, int[a..b] -> t
            // list[l..u] t, int -> t
            // list[l..u] t, int[a..b] -> t
            // dict(k:v), k -> v
            // string, int -> string
            // tuple(t1 f1, t2 f2, ..., tn fn), int i -> ti
            // tuple(t1 f1, t2 f2, ..., tn fn), field fi -> ti
            CifType indexHint = null;
            if (nctype instanceof ListType) {
                // Assumes type ranges are ignored.
                indexHint = INT_TYPE_HINT;
            } else if (nctype instanceof DictType) {
                indexHint = ((DictType)nctype).getKeyType();
            } else if (nctype instanceof StringType) {
                // Assumes type ranges are ignored.
                indexHint = INT_TYPE_HINT;
            } else if (nctype instanceof TupleType) {
                // Assumes type ranges are ignored.
                indexHint = INT_TYPE_HINT;
            }

            // Transform index expression.
            index = transExpression(expr.index, indexHint, scope, context, tchecker);
        }
        CifType itype = index.getType();
        CifType nitype = CifTypeUtils.normalizeType(itype);

        // Check index type, and get result type.
        CifType resultType;
        if (nctype instanceof ListType) {
            // list t, int -> t
            // list t, int[a..b] -> t
            // list[l..u] t, int -> t
            // list[l..u] t, int[a..b] -> t
            if (!(nitype instanceof IntType)) {
                tchecker.addProblem(ErrMsg.PROJ_INDEX_TYPE, expr.position, CifTextUtils.typeToStr(ctype),
                        CifTextUtils.typeToStr(itype), "int", "");
                throw new SemanticException();
            }

            ListType ltype = (ListType)nctype;
            IntType intType = (IntType)nitype;
            if (!CifTypeUtils.isRangeless(ltype) && !CifTypeUtils.isRangeless(intType)) {
                long listUpper = ltype.getUpper();
                long indexLower = intType.getLower();
                long indexUpper = intType.getUpper();

                // Verifying projection bounds only makes sense for sane bounds on the sub-expressions.
                if (listUpper >= 0 && indexLower <= indexUpper) {
                    // Check that the lowest possible index is not above the longest list size.
                    if (indexLower >= listUpper) {
                        tchecker.addProblem(ErrMsg.PROJ_LIST_OUT_OF_BOUNDS, expr.position,
                                CifTextUtils.typeToStr(nctype), CifTextUtils.typeToStr(nitype));
                        throw new SemanticException();
                    }
                    // Check that the negative index nearest to 0 is not above the longest list size.
                    if (indexUpper < 0) {
                        long normalizedIndex = indexUpper + listUpper;
                        if (normalizedIndex < 0) {
                            tchecker.addProblem(ErrMsg.PROJ_LIST_OUT_OF_BOUNDS, expr.position,
                                    CifTextUtils.typeToStr(nctype), CifTextUtils.typeToStr(nitype));
                            throw new SemanticException();
                        }
                    }
                }

                // This check is incomplete. Remaining cases are checked at runtime.
            }

            resultType = deepclone(((ListType)nctype).getElementType());
        } else if (nctype instanceof DictType) {
            // dict(k:v), k -> v
            CifType ktype = ((DictType)nctype).getKeyType();
            if (!checkTypeCompat(ktype, itype, RangeCompat.IGNORE)) {
                tchecker.addProblem(ErrMsg.PROJ_INDEX_TYPE, expr.position, CifTextUtils.typeToStr(ctype),
                        CifTextUtils.typeToStr(itype), CifTextUtils.typeToStr(ktype), "");
                throw new SemanticException();
            }

            resultType = deepclone(((DictType)nctype).getValueType());
        } else if (nctype instanceof StringType) {
            // string, int -> string
            if (!(nitype instanceof IntType)) {
                tchecker.addProblem(ErrMsg.PROJ_INDEX_TYPE, expr.position, CifTextUtils.typeToStr(ctype),
                        CifTextUtils.typeToStr(itype), "int", "");
                throw new SemanticException();
            }

            resultType = deepclone(ctype);
        } else if (nctype instanceof TupleType) {
            // tuple(t1 f1, t2 f2, ..., tn fn), int i -> ti
            // tuple(t1 f1, t2 f2, ..., tn fn), field fi -> ti
            if (index instanceof FieldExpression) {
                // Special case for tuple/field projection.
                Field field = ((FieldExpression)index).getField();
                resultType = deepclone(field.getType());
            } else {
                // Check index type.
                if (!(nitype instanceof IntType)) {
                    tchecker.addProblem(ErrMsg.PROJ_INDEX_TYPE, expr.position, CifTextUtils.typeToStr(ctype),
                            CifTextUtils.typeToStr(itype), "int", " (or a field name)");
                    throw new SemanticException();
                }

                // Statically evaluate index.
                checkStaticEvaluable(index, tchecker);

                int idx;
                try {
                    idx = (Integer)CifEvalUtils.eval(index, false);
                } catch (CifEvalException e) {
                    tchecker.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
                    throw new SemanticException();
                }

                // Check index ranges.
                TupleType ttype = (TupleType)nctype;
                if (idx < 0 || idx >= ttype.getFields().size()) {
                    tchecker.addProblem(ErrMsg.PROJ_TUPLE_INDEX_BOUNDS, expr.position, CifTextUtils.typeToStr(ctype),
                            Integer.toString(idx));
                    throw new SemanticException();
                }

                // Get result type.
                resultType = deepclone(ttype.getFields().get(idx).getType());
            }
        } else {
            throw new RuntimeException("Checks above should prevent this.");
        }

        // Create projection expression.
        ProjectionExpression rslt = newProjectionExpression();
        rslt.setChild(child);
        rslt.setIndex(index);
        rslt.setPosition(expr.position);
        rslt.setType(resultType);
        return rslt;
    }

    /**
     * Transforms a slice expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static SliceExpression transSliceExpression(ASliceExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Get type hint for child.
        // list t, int (ranged/rangeless/omitted), int (ranged/rangeless/omitted) -> list t
        // list[l] t, int ([y]/omitted), int ([z]/omitted) -> list[w] t, with w the only possible result size
        // list[l] t, int (other), int (other) -> list[0..l] t
        //
        // list[l..u] t, int ([y]/omitted, 0 <= y <= l), int ([z]/omitted, 0 <= z <= l)
        // -> list[v..w] t, with as narrow as possible v and w
        //
        // list[l..u] t, int (other), int (other) -> list[0..u] t
        // string, int, int -> string
        CifType childHint = hint;

        // Transform child.
        Expression child = transExpression(expr.child, childHint, scope, context, tchecker);
        CifType ctype = child.getType();
        CifType nctype = CifTypeUtils.normalizeType(ctype);

        // Check child type.
        if (!(nctype instanceof StringType) && !(nctype instanceof ListType)) {
            tchecker.addProblem(ErrMsg.SLICE_CHILD_TYPE, expr.position, CifTextUtils.typeToStr(ctype));
            throw new SemanticException();
        }

        // Transform begin.
        Expression begin = null;
        IntType bitype = null;
        if (expr.begin != null) {
            begin = transExpression(expr.begin, newIntType(), scope, context, tchecker);
            CifType btype = begin.getType();
            CifType nbtype = CifTypeUtils.normalizeType(btype);

            if (!(nbtype instanceof IntType)) {
                tchecker.addProblem(ErrMsg.SLICE_IDX_NON_INT, begin.getPosition(), "begin");
                throw new SemanticException();
            }
            bitype = (IntType)nbtype;
        }

        // Transform end.
        Expression end = null;
        IntType eitype = null;
        if (expr.end != null) {
            end = transExpression(expr.end, newIntType(), scope, context, tchecker);
            CifType etype = end.getType();
            CifType netype = CifTypeUtils.normalizeType(etype);

            if (!(netype instanceof IntType)) {
                tchecker.addProblem(ErrMsg.SLICE_IDX_NON_INT, end.getPosition(), "end");
                throw new SemanticException();
            }
            eitype = (IntType)netype;
        }

        // Create slice expression.
        SliceExpression rslt = newSliceExpression();
        rslt.setChild(child);
        rslt.setBegin(begin);
        rslt.setEnd(end);
        rslt.setPosition(expr.position);

        // Result type.
        if (nctype instanceof StringType) {
            // string, int, int -> string
            rslt.setType(deepclone(child.getType()));
        } else {
            Assert.check(nctype instanceof ListType);
            ListType ltype = (ListType)nctype;
            ListType rtype = deepclone(ltype);
            if (CifTypeUtils.isRangeless(ltype)) {
                // list t, int (ranged/rangeless/omitted), int (ranged/rangeless/omitted) -> list t
            } else {
                int l = ltype.getLower();
                int u = ltype.getUpper();
                if (l == u) {
                    if ((bitype == null
                            || (!CifTypeUtils.isRangeless(bitype) && bitype.getLower().equals(bitype.getUpper())))
                            && (eitype == null || (!CifTypeUtils.isRangeless(eitype)
                                    && eitype.getLower().equals(eitype.getUpper()))))
                    {
                        // list[l] t, int ([y]/omitted), int ([z]/omitted) -> list[w] t
                        // (with w the only possible result size)
                        Integer y = (bitype == null) ? null : bitype.getLower();
                        Integer z = (eitype == null) ? null : eitype.getLower();
                        int rsltSize = getSliceResultSize(l, y, z);
                        rtype.setLower(rsltSize);
                        rtype.setUpper(rsltSize);
                    } else {
                        // list[l] t, int (other), int (other) -> list[0..l] t
                        rtype.setLower(0);
                        rtype.setUpper(l);
                    }
                } else {
                    if ((bitype == null
                            || (!CifTypeUtils.isRangeless(bitype) && bitype.getLower().equals(bitype.getUpper())
                                    && 0 <= bitype.getLower() && bitype.getLower() <= l))
                            && (eitype == null
                                    || (!CifTypeUtils.isRangeless(eitype) && eitype.getLower().equals(eitype.getUpper())
                                            && 0 <= eitype.getLower() && eitype.getLower() <= l)))
                    {
                        // list[l..u] t, int ([y]/omitted, 0 <= y <= l), int ([z]/omitted, 0 <= z <= l)
                        // -> list[v..w] t (with as narrow as possible v and w)
                        Integer y = (bitype == null) ? null : bitype.getLower();
                        Integer z = (eitype == null) ? null : eitype.getLower();
                        int[] rsltRange = getSliceResultRange(l, u, y, z);
                        rtype.setLower(rsltRange[0]);
                        rtype.setUpper(rsltRange[1]);
                    } else {
                        // list[l..u] t, int (other), int (other) -> list[0..u] t
                        rtype.setLower(0);
                        rtype.setUpper(u);
                    }
                }
            }
            rslt.setType(rtype);
        }

        // Returned slice expression with computed result type.
        return rslt;
    }

    /**
     * Returns the only possible size of the list resulting from slicing.
     *
     * @param len The length of the list being sliced.
     * @param begin The begin index value, or {@code null}.
     * @param end The end index value, or {@code null}.
     * @return The only possible size of the list resulting from slicing.
     */
    public static int getSliceResultSize(int len, Integer begin, Integer end) {
        // Replace 'null' by defaults.
        int b = (begin == null) ? 0 : begin;
        int e = (end == null) ? len : end;

        // Handle negative indices.
        if (b < 0) {
            b = len + b;
        }
        if (e < 0) {
            e = len + e;
        }

        // Handle out of range and empty interval.
        if (b < 0) {
            b = 0;
        }
        if (e < 0) {
            e = 0;
        }

        if (b > len) {
            b = len;
        }
        if (e > len) {
            e = len;
        }

        if (b > e) {
            b = e;
        }

        // Return result size.
        return e - b;
    }

    /**
     * Returns the narrowest result range of the possible sizes of the lists resulting from slicing.
     *
     * @param l The lower bound on the length of the list being sliced.
     * @param u The upper bound on the length of the list being sliced.
     * @param begin The begin index value, or {@code null}. If not {@code null}, must be non-negative and less than or
     *     equal to {@code l}.
     * @param end The end index value, or {@code null}. If not {@code null}, must be non-negative and less than or equal
     *     to {@code l}.
     * @return The narrowest result range of the possible sizes of the lists resulting from slicing.
     */
    public static int[] getSliceResultRange(int l, int u, Integer begin, Integer end) {
        int s1 = getSliceResultSize(l, begin, end);
        int s2 = getSliceResultSize(u, begin, end);
        return new int[] {Math.min(s1, s2), Math.max(s1, s2)};
    }

    /**
     * Transforms a function call expression and performs type checking on it.
     *
     * @param expr The CIF AST function call expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static FunctionCallExpression transFuncCallExpression(AFuncCallExpression expr, CifType hint,
            SymbolScope<?> scope, ExprContext context, CifTypeChecker tchecker)
    {
        // Redirect based on function expression.
        if (expr.function instanceof AStdLibFunctionExpression) {
            return transFuncCallExpressionStdLib(expr, hint, scope, context, tchecker);
        } else {
            return transFuncCallExpressionFunc(expr, hint, scope, context, tchecker);
        }
    }

    /**
     * Transforms a function call expression and performs type checking on it, for a function expressions that is not a
     * standard library function reference.
     *
     * @param expr The CIF AST function call expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static FunctionCallExpression transFuncCallExpressionFunc(AFuncCallExpression expr, CifType hint,
            SymbolScope<?> scope, ExprContext context, CifTypeChecker tchecker)
    {
        // Initialize result.
        FunctionCallExpression rslt = newFunctionCallExpression();
        rslt.setPosition(expr.position);

        // Get AST arguments.
        List<AExpression> astArgs = (expr.arguments == null) ? Collections.emptyList() : expr.arguments;

        // Transform function expression.
        Expression fexpr = transExpression(expr.function, NO_TYPE_HINT, scope, context, tchecker);
        rslt.setFunction(fexpr);

        // Make sure it evaluates to a function.
        CifType ftype = fexpr.getType();
        CifType nftype = CifTypeUtils.normalizeType(ftype);
        if (!(nftype instanceof FuncType)) {
            tchecker.addProblem(ErrMsg.FCALL_NON_FUNC, expr.position, CifTextUtils.typeToStr(ftype));
            throw new SemanticException();
        }

        // Check argument counts.
        FuncType funcType = (FuncType)nftype;
        int expectedCount = funcType.getParamTypes().size();
        if (astArgs.size() != expectedCount) {
            String funcText = "of type \"" + CifTextUtils.typeToStr(funcType) + "\"";
            tchecker.addProblem(ErrMsg.FCALL_WRONG_ARG_COUNT, expr.position, funcText,
                    String.valueOf(expectedCount) + " argument" + (expectedCount == 1 ? "" : "s"),
                    String.valueOf(astArgs.size()) + (astArgs.size() == 1 ? " argument is" : " arguments are"));
            throw new SemanticException();
        }

        // Transform arguments.
        List<Expression> args = listc(astArgs.size());
        for (int i = 0; i < astArgs.size(); i++) {
            AExpression astArg = astArgs.get(i);
            CifType argHint = funcType.getParamTypes().get(i);
            args.add(transExpression(astArg, argHint, scope, context, tchecker));
        }
        rslt.getParams().addAll(args);

        // Check argument types.
        for (int i = 0; i < expectedCount; i++) {
            CifType expectedType = funcType.getParamTypes().get(i);
            CifType actualType = args.get(i).getType();
            if (!checkTypeCompat(expectedType, actualType, RangeCompat.CONTAINED)) {
                String funcText = "of type \"" + CifTextUtils.typeToStr(funcType) + "\"";

                List<String> argTypes = list();
                for (Expression arg: args) {
                    argTypes.add("\"" + CifTextUtils.typeToStr(arg.getType()) + "\"");
                }

                tchecker.addProblem(ErrMsg.FCALL_WRONG_ARG_TYPES, expr.position, funcText,
                        StringUtils.join(argTypes, ", "));
                throw new SemanticException();
            }
        }

        // Set type of the function call expression.
        rslt.setType(deepclone(((FuncType)nftype).getReturnType()));

        // Return function call expression.
        return rslt;
    }

    /** Mapping of standard library functions from ASCII representation to metamodel representation. */
    private static final Map<String, StdLibFunction> FUNC_MAP;

    static {
        FUNC_MAP = map();

        // Trigonometric functions.
        FUNC_MAP.put("acos", StdLibFunction.ACOS);
        FUNC_MAP.put("acosh", StdLibFunction.ACOSH);
        FUNC_MAP.put("asin", StdLibFunction.ASIN);
        FUNC_MAP.put("asinh", StdLibFunction.ASINH);
        FUNC_MAP.put("atan", StdLibFunction.ATAN);
        FUNC_MAP.put("atanh", StdLibFunction.ATANH);
        FUNC_MAP.put("cos", StdLibFunction.COS);
        FUNC_MAP.put("cosh", StdLibFunction.COSH);
        FUNC_MAP.put("sin", StdLibFunction.SIN);
        FUNC_MAP.put("sinh", StdLibFunction.SINH);
        FUNC_MAP.put("tan", StdLibFunction.TAN);
        FUNC_MAP.put("tanh", StdLibFunction.TANH);

        // General functions.
        FUNC_MAP.put("abs", StdLibFunction.ABS);
        FUNC_MAP.put("cbrt", StdLibFunction.CBRT);
        FUNC_MAP.put("ceil", StdLibFunction.CEIL);
        FUNC_MAP.put("del", StdLibFunction.DELETE);
        FUNC_MAP.put("empty", StdLibFunction.EMPTY);
        FUNC_MAP.put("exp", StdLibFunction.EXP);
        FUNC_MAP.put("floor", StdLibFunction.FLOOR);
        FUNC_MAP.put("fmt", StdLibFunction.FORMAT);
        FUNC_MAP.put("ln", StdLibFunction.LN);
        FUNC_MAP.put("log", StdLibFunction.LOG);
        FUNC_MAP.put("max", StdLibFunction.MAXIMUM);
        FUNC_MAP.put("min", StdLibFunction.MINIMUM);
        FUNC_MAP.put("pop", StdLibFunction.POP);
        FUNC_MAP.put("pow", StdLibFunction.POWER);
        FUNC_MAP.put("round", StdLibFunction.ROUND);
        FUNC_MAP.put("scale", StdLibFunction.SCALE);
        FUNC_MAP.put("sign", StdLibFunction.SIGN);
        FUNC_MAP.put("size", StdLibFunction.SIZE);
        FUNC_MAP.put("sqrt", StdLibFunction.SQRT);

        // Distribution functions.
        FUNC_MAP.put("bernoulli", StdLibFunction.BERNOULLI);
        FUNC_MAP.put("beta", StdLibFunction.BETA);
        FUNC_MAP.put("binomial", StdLibFunction.BINOMIAL);
        FUNC_MAP.put("constant", StdLibFunction.CONSTANT);
        FUNC_MAP.put("erlang", StdLibFunction.ERLANG);
        FUNC_MAP.put("exponential", StdLibFunction.EXPONENTIAL);
        FUNC_MAP.put("gamma", StdLibFunction.GAMMA);
        FUNC_MAP.put("geometric", StdLibFunction.GEOMETRIC);
        FUNC_MAP.put("lognormal", StdLibFunction.LOG_NORMAL);
        FUNC_MAP.put("normal", StdLibFunction.NORMAL);
        FUNC_MAP.put("poisson", StdLibFunction.POISSON);
        FUNC_MAP.put("random", StdLibFunction.RANDOM);
        FUNC_MAP.put("triangle", StdLibFunction.TRIANGLE);
        FUNC_MAP.put("uniform", StdLibFunction.UNIFORM);
        FUNC_MAP.put("weibull", StdLibFunction.WEIBULL);
    }

    /**
     * Transforms a function call expression and performs type checking on it, for a function expressions that is a
     * standard library function reference.
     *
     * @param astCall The CIF AST function call expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static FunctionCallExpression transFuncCallExpressionStdLib(AFuncCallExpression astCall, CifType hint,
            SymbolScope<?> scope, ExprContext context, CifTypeChecker tchecker)
    {
        // Get standard library function.
        AStdLibFunctionExpression astStdLib = (AStdLibFunctionExpression)astCall.function;
        StdLibFunction func = FUNC_MAP.get(astStdLib.function);
        Assert.notNull(func);

        // Check for invalid use.
        if (CifTypeUtils.isDistFunction(func) && (context == null || !context.conditions.contains(ALLOW_DIST))) {
            // Invalid use of distribution standard library function found.
            tchecker.addProblem(ErrMsg.STDLIB_OCCURRENCE, astStdLib.position, CifTextUtils.functionToStr(func));
            // Non-fatal error.
        }

        // Initialize result.
        FunctionCallExpression mmCall = newFunctionCallExpression();
        mmCall.setPosition(astCall.position);

        StdLibFunctionExpression mmStdLib = newStdLibFunctionExpression();
        mmCall.setFunction(mmStdLib);
        mmStdLib.setFunction(func);
        mmStdLib.setPosition(astStdLib.position);

        // Get AST arguments.
        List<AExpression> astArgs = (astCall.arguments == null) ? Collections.emptyList() : astCall.arguments;

        // Check argument count.
        switch (func) {
            case ACOS:
            case ACOSH:
            case ASIN:
            case ASINH:
            case ATAN:
            case ATANH:
            case COS:
            case COSH:
            case SIN:
            case SINH:
            case TAN:
            case TANH:
                // real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case ABS:
                // int[l..u] -> int[ min{abs(x) | x in [l1 .. u1]} .. max{abs(x) | x in [l1 .. u1]} ]
                // int -> int
                // real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case CBRT:
                // real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case CEIL:
                // real -> int
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case DELETE:
                // list t, int -> list t
                // list t, int[a..b] -> list t
                // list[l..u] t, int -> list[l2..u2] t
                // list[l..u] t, int[a..b] -> list[l2..u2] t (with: l2 = max(0,l-1), u2 = max(0,u-1))
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case EMPTY:
                // list t -> bool
                // list[l..u] t -> bool
                // set t -> bool
                // dict(k:v) -> bool
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case EXP:
                // real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case FLOOR:
                // real -> int
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case FORMAT:
                // string -> string
                // string, t -> string
                // ...
                // string, t1, ..., tn -> string
                if (astArgs.isEmpty()) {
                    tchecker.addProblem(ErrMsg.FCALL_WRONG_ARG_COUNT, astStdLib.position, "\"fmt\"",
                            "at least 1 argument", "0 arguments are");
                    throw new SemanticException();
                }
                break;

            case LN:
            case LOG:
                // real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case MAXIMUM:
                // int[l1..u1], int[l2..u2] -> int[max(l1,l2)..max(u1,u2)]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case MINIMUM:
                // int[l1..u1], int[l2..u2] -> int[min(l1,l2)..min(u1,u2)]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case POP:
                // list t -> tuple(t, list t)
                // list[l..u] t -> tuple(t, list[max(0,l-1)..max(0,u-1)] t)
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case POWER:
                // int[l1..u1], int[l2..u2] -> int[
                // min{x^y | x in [l1 .. u1], y in [l2 .. u2 ]} ..
                // max{x^y | x in [l1 .. u1], y in [l2 .. u2 ]}
                // ]
                //
                // int/real, int/real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case ROUND:
                // real -> int
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case SCALE:
                // int/real (5 parameters) -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 5, tchecker);
                break;

            case SIGN:
                // int[l..u] -> int[sgn(l) .. sgn(u)]
                // int -> int[-1 .. 1]
                // real -> int[-1 .. 1]
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case SIZE:
                // string -> int
                // list t -> int
                // list[l..u] t -> int[l..u]
                // set t -> int
                // dict(k:v) -> int
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case SQRT:
                // real -> real
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case BERNOULLI:
                // real -> dist bool
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case BETA:
                // real, real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case BINOMIAL:
                // real, int -> dist int
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case CONSTANT:
                // bool -> dist bool
                // int[l..u] -> dist int
                // int -> dist int
                // real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case ERLANG:
                // int, real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case EXPONENTIAL:
                // real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case GAMMA:
                // real, real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case GEOMETRIC:
                // real -> dist int
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case LOG_NORMAL:
            case NORMAL:
                // real, real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case POISSON:
                // real -> dist int
                checkFuncCallArgCounts(astStdLib, astArgs, 1, tchecker);
                break;

            case RANDOM:
                // () -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 0, tchecker);
                break;

            case TRIANGLE:
                // real, real, real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 3, tchecker);
                break;

            case UNIFORM:
                // int, int -> dist int
                // real, real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;

            case WEIBULL:
                // real, real -> dist real
                checkFuncCallArgCounts(astStdLib, astArgs, 2, tchecker);
                break;
        }

        // Transform arguments.
        CifType nhint = normalizeHint(hint);
        CifType[] argHints = new CifType[astArgs.size()];
        List<Expression> args = listc(astArgs.size());
        CifType[] atypes = new CifType[astArgs.size()];
        CifType[] natypes = new CifType[astArgs.size()];
        for (int i = 0; i < astArgs.size(); i++) {
            // Get type hint for current argument. The function call result
            // hint type can be used, as well as the actual types of previous
            // arguments.
            switch (func) {
                case ACOS:
                case ACOSH:
                case ASIN:
                case ASINH:
                case ATAN:
                case ATANH:
                case COS:
                case COSH:
                case SIN:
                case SINH:
                case TAN:
                case TANH:
                    // real -> real
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case ABS:
                    // int[l..u] -> int[ min{abs(x) | x in [l1 .. u1]} .. max{abs(x) | x in [l1 .. u1]} ]
                    // int -> int
                    // real -> real

                    // Assumes type ranges are ignored.
                    argHints[0] = hint;
                    break;

                case CBRT:
                    // real -> real
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case CEIL:
                    // real -> int
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case DELETE:
                    // list t, int -> list t
                    // list t, int[a..b] -> list t
                    // list[l..u] t, int -> list[l2..u2] t
                    // list[l..u] t, int[a..b] -> list[l2..u2] t (with: l2 = max(0,l-1), u2 = max(0,u-1))

                    // Assumes type ranges are ignored.
                    if (i == 0) {
                        argHints[0] = hint;
                    }
                    if (i == 1) {
                        argHints[1] = INT_TYPE_HINT;
                    }
                    break;

                case EMPTY:
                    // list t -> bool
                    // list[l..u] t -> bool
                    // set t -> bool
                    // dict(k:v) -> bool
                    break;

                case EXP:
                    // real -> real
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case FLOOR:
                    // real -> int
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case FORMAT:
                    // string -> string
                    // string, t -> string
                    // ...
                    // string, t1, ..., tn -> string
                    if (i == 0) {
                        argHints[0] = STRING_TYPE_HINT;
                    }
                    break;

                case LN:
                case LOG:
                    // real -> real
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case MAXIMUM:
                    // int[l1..u1], int[l2..u2] -> int[max(l1,l2)..max(u1,u2)]
                    // int, int -> int
                    // int, real -> real
                    // real, int -> real
                    // real, real -> real

                    // Assumes type ranges are ignored.
                    argHints[i] = hint;
                    break;

                case MINIMUM:
                    // int[l1..u1], int[l2..u2] -> int[min(l1,l2)..min(u1,u2)]
                    // int, int -> int
                    // int, real -> real
                    // real, int -> real
                    // real, real -> real

                    // Assumes type ranges are ignored.
                    argHints[i] = hint;
                    break;

                case POP:
                    // list t -> tuple(t, list t)
                    // list[l..u] t -> tuple(t, list[max(0,l-1)..max(0,u-1)] t)
                    if (nhint instanceof TupleType) {
                        TupleType ttype = (TupleType)nhint;
                        if (ttype.getFields().size() == 2) {
                            // Assumes type ranges are ignored.
                            argHints[0] = ttype.getFields().get(1).getType();
                        }
                    }
                    break;

                case POWER:
                    // int[l1..u1], int[l2..u2] -> int[
                    // min{x^y | x in [l1 .. u1], y in [l2 .. u2 ]} ..
                    // max{x^y | x in [l1 .. u1], y in [l2 .. u2 ]}
                    // ]
                    // int/real, int/real -> real

                    // Assumes type ranges are ignored.
                    argHints[i] = hint;
                    break;

                case ROUND:
                    // real -> int
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case SCALE:
                    // int/real (5 parameters) -> real
                    break;

                case SIGN:
                    // int[l..u] -> int[sgn(l) .. sgn(u)]
                    // int -> int[-1 .. 1]
                    // real -> int[-1 .. 1]
                    break;

                case SIZE:
                    // string -> int
                    // list t -> int
                    // list[l..u] t -> int[l..u]
                    // set t -> int
                    // dict(k:v) -> int
                    break;

                case SQRT:
                    // real -> real
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case BERNOULLI:
                    // real -> dist bool
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case BETA:
                    // real, real -> dist real
                    argHints[i] = REAL_TYPE_HINT;
                    break;

                case BINOMIAL:
                    // real, int -> dist int

                    // Assumes type ranges are ignored.
                    if (i == 0) {
                        argHints[0] = REAL_TYPE_HINT;
                    }
                    if (i == 1) {
                        argHints[1] = INT_TYPE_HINT;
                    }
                    break;

                case CONSTANT:
                    // bool -> dist bool
                    // int[l..u] -> dist int
                    // int -> dist int
                    // real -> dist real
                    if (nhint instanceof DistType) {
                        // Assumes type ranges are ignored.
                        argHints[0] = ((DistType)nhint).getSampleType();
                    }
                    break;

                case ERLANG:
                    // int, real -> dist real

                    // Assumes type ranges are ignored.
                    if (i == 0) {
                        argHints[0] = INT_TYPE_HINT;
                    }
                    if (i == 1) {
                        argHints[1] = REAL_TYPE_HINT;
                    }
                    break;

                case EXPONENTIAL:
                    // real -> dist real
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case GAMMA:
                    // real, real -> dist real
                    argHints[i] = REAL_TYPE_HINT;
                    break;

                case GEOMETRIC:
                    // real -> dist int
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case LOG_NORMAL:
                case NORMAL:
                    // real, real -> dist real
                    argHints[i] = REAL_TYPE_HINT;
                    break;

                case POISSON:
                    // real -> dist int
                    argHints[0] = REAL_TYPE_HINT;
                    break;

                case RANDOM:
                    // () -> dist real
                    break;

                case TRIANGLE:
                    // real, real, real -> dist real
                    argHints[i] = REAL_TYPE_HINT;
                    break;

                case UNIFORM:
                    // int, int -> dist int
                    // real, real -> dist real
                    argHints[i] = hint;
                    break;

                case WEIBULL:
                    // real, real -> dist real
                    argHints[i] = REAL_TYPE_HINT;
                    break;
            }

            // Transform argument.
            AExpression astArg = astArgs.get(i);
            Expression arg = transExpression(astArg, argHints[i], scope, context, tchecker);
            args.add(arg);

            // Get argument type and normalized version.
            CifType atype = arg.getType();
            atypes[i] = atype;
            natypes[i] = CifTypeUtils.normalizeType(atype);
        }
        mmCall.getParams().addAll(args);

        // Initialize standard library function reference result type.
        FuncType resultType = newFuncType();
        mmStdLib.setType(resultType);
        resultType.setPosition(copyPosition(astStdLib.position));
        for (int i = 0; i < atypes.length; i++) {
            resultType.getParamTypes().add(deepclone(atypes[i]));
        }

        // Type of the result depends on the function and the parameters.
        switch (func) {
            case ACOS:
            case ACOSH:
            case ASIN:
            case ASINH:
            case ATAN:
            case ATANH:
            case COS:
            case COSH:
            case SIN:
            case SINH:
            case TAN:
            case TANH: {
                // real -> real

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                RealType type = newRealType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case ABS: {
                // int[l..u] -> int[ min{abs(x) | x in [l1 .. u1]} .. max{abs(x) | x in [l1 .. u1]} ]
                // int -> int
                // real -> real

                CifType ntype = natypes[0];
                if (!(ntype instanceof IntType) && !(ntype instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                if (ntype instanceof IntType) {
                    IntType itype = newIntType();
                    itype.setPosition(copyPosition(astStdLib.position));

                    resultType.setReturnType(itype);

                    if (!isRangeless((IntType)ntype)) {
                        int lower = ((IntType)ntype).getLower();
                        int upper = ((IntType)ntype).getUpper();

                        if (lower == Integer.MIN_VALUE) {
                            // Possible overflow.
                            tchecker.addProblem(ErrMsg.FCALL_ABS_OVERFLOW, astStdLib.position,
                                    CifTextUtils.typeToStr(atypes[0]));
                            throw new SemanticException();
                        }

                        int[] resultRange = getAbsResultRange(lower, upper);

                        itype.setLower(resultRange[0]);
                        itype.setUpper(resultRange[1]);
                    }
                } else {
                    Assert.check(ntype instanceof RealType);
                    RealType type = newRealType();
                    type.setPosition(copyPosition(astStdLib.position));
                    resultType.setReturnType(type);
                }

                break;
            }

            case CBRT: {
                // real -> real

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                RealType type = newRealType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case CEIL: {
                // real -> int

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                IntType type = newIntType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case DELETE: {
                // list t, int -> list t
                // list t, int[a..b] -> list t
                // list[l..u] t, int -> list[l2..u2] t
                // list[l..u] t, int[a..b] -> list[l2..u2] t (with: l2 = max(0,l-1), u2 = max(0,u-1))

                if (!(natypes[0] instanceof ListType) || !(natypes[1] instanceof IntType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                ListType ltype = (ListType)natypes[0];
                IntType itype = (IntType)natypes[1];
                if (!CifTypeUtils.isRangeless(ltype) && !CifTypeUtils.isRangeless(itype)) {
                    long u = ltype.getUpper();
                    long a = itype.getLower();
                    long b = itype.getUpper();

                    if (a >= 0 && b >= 0 && a >= u) {
                        // Index lower bound larger or equal to list size upper
                        // bound, so index for sure does not exist in the list.
                        // This check is incomplete. Remaining cases checked at
                        // runtime.
                        tchecker.addProblem(ErrMsg.FCALL_DELETE_OUT_OF_BOUNDS, astStdLib.position,
                                CifTextUtils.typeToStr(atypes[0]), CifTextUtils.typeToStr(atypes[1]));
                        throw new SemanticException();
                    }
                }

                ListType type = deepclone(ltype);
                if (!CifTypeUtils.isRangeless(type)) {
                    type.setLower(Math.max(0, type.getLower() - 1));
                    type.setUpper(Math.max(0, type.getUpper() - 1));
                }

                resultType.setReturnType(type);
                break;
            }

            case EMPTY: {
                // list t -> bool
                // list[l..u] t -> bool
                // set t -> bool
                // dict(k:v) -> bool

                if (!(natypes[0] instanceof ListType) && !(natypes[0] instanceof SetType)
                        && !(natypes[0] instanceof DictType))
                {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                BoolType type = newBoolType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case EXP: {
                // real -> real

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                RealType type = newRealType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case FLOOR: {
                // real -> int

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                IntType type = newIntType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case FORMAT: {
                // string -> string
                // string, t -> string
                // ...
                // string, t1, ..., tn -> string

                if (!(args.get(0) instanceof StringExpression)) {
                    tchecker.addProblem(ErrMsg.FCALL_FMT_NOT_PATTERN, args.get(0).getPosition());
                    throw new SemanticException();
                }

                List<CifType> valueTypes = listc(args.size() - 1);
                List<Position> valuePositions = listc(args.size() - 1);
                for (int i = 1; i < args.size(); i++) {
                    valueTypes.add(atypes[i]);
                    valuePositions.add(args.get(i).getPosition());
                }

                StringExpression pattern = (StringExpression)args.get(0);
                CifFormatPatternChecker.checkFormatPattern(tchecker, valueTypes, valuePositions, pattern.getValue(),
                        pattern.getPosition());

                StringType type = newStringType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case LN:
            case LOG: {
                // real -> real

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                RealType type = newRealType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case MAXIMUM: {
                // int[l1..u1], int[l2..u2] -> int[max(l1,l2)..max(u1,u2)]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real

                if ((!(natypes[0] instanceof IntType) && !(natypes[0] instanceof RealType))
                        || (!(natypes[1] instanceof IntType) && !(natypes[1] instanceof RealType)))
                {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                if (natypes[0] instanceof IntType && natypes[1] instanceof IntType) {
                    IntType type = newIntType();
                    type.setPosition(copyPosition(astStdLib.position));

                    resultType.setReturnType(type);

                    if (!isRangeless((IntType)natypes[0]) && !isRangeless((IntType)natypes[1])) {
                        int l1 = ((IntType)natypes[0]).getLower();
                        int u1 = ((IntType)natypes[0]).getUpper();
                        int l2 = ((IntType)natypes[1]).getLower();
                        int u2 = ((IntType)natypes[1]).getUpper();

                        int l = Math.max(l1, l2);
                        int u = Math.max(u1, u2);

                        type.setLower(l);
                        type.setUpper(u);
                    }
                } else {
                    RealType type = newRealType();
                    type.setPosition(copyPosition(astStdLib.position));
                    resultType.setReturnType(type);
                }

                break;
            }

            case MINIMUM: {
                // int[l1..u1], int[l2..u2] -> int[min(l1,l2)..min(u1,u2)]
                // int, int -> int
                // int, real -> real
                // real, int -> real
                // real, real -> real

                if ((!(natypes[0] instanceof IntType) && !(natypes[0] instanceof RealType))
                        || (!(natypes[1] instanceof IntType) && !(natypes[1] instanceof RealType)))
                {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                if (natypes[0] instanceof IntType && natypes[1] instanceof IntType) {
                    IntType type = newIntType();
                    type.setPosition(copyPosition(astStdLib.position));

                    resultType.setReturnType(type);

                    if (!isRangeless((IntType)natypes[0]) && !isRangeless((IntType)natypes[1])) {
                        int l1 = ((IntType)natypes[0]).getLower();
                        int u1 = ((IntType)natypes[0]).getUpper();
                        int l2 = ((IntType)natypes[1]).getLower();
                        int u2 = ((IntType)natypes[1]).getUpper();

                        int l = Math.min(l1, l2);
                        int u = Math.min(u1, u2);

                        type.setLower(l);
                        type.setUpper(u);
                    }
                } else {
                    RealType type = newRealType();
                    type.setPosition(copyPosition(astStdLib.position));
                    resultType.setReturnType(type);
                }

                break;
            }

            case POP: {
                // list t -> tuple(t, list t)
                // list[l..u] t -> tuple(t, list[max(0,l-1) .. max(0,u-1)] t)

                if (!(natypes[0] instanceof ListType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                ListType ltype = deepclone((ListType)natypes[0]);
                if (!CifTypeUtils.isRangeless(ltype)) {
                    ltype.setLower(Math.max(0, ltype.getLower() - 1));
                    ltype.setUpper(Math.max(0, ltype.getUpper() - 1));
                }

                CifType rtype = makeTupleType(list(ltype.getElementType(), ltype));
                resultType.setReturnType(deepclone(rtype));

                break;
            }

            case POWER: {
                // int[l1..u1], int[l2..u2] -> int[
                // min{x^y | x in [l1 .. u1], y in [l2 .. u2 ]} ..
                // max{x^y | x in [l1 .. u1], y in [l2 .. u2 ]}
                // ]
                //
                // int/real, int/real -> real

                if ((!(natypes[0] instanceof IntType) && !(natypes[0] instanceof RealType))
                        || (!(natypes[1] instanceof IntType) && !(natypes[1] instanceof RealType)))
                {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                boolean done = false;

                if (natypes[0] instanceof IntType && natypes[1] instanceof IntType && !isRangeless((IntType)natypes[0])
                        && !isRangeless((IntType)natypes[1]))
                {
                    int l1 = ((IntType)natypes[0]).getLower();
                    int u1 = ((IntType)natypes[0]).getUpper();
                    int l2 = ((IntType)natypes[1]).getLower();
                    int u2 = ((IntType)natypes[1]).getUpper();

                    // Negative exponents lead to reals.
                    if (l2 >= 0) {
                        // No overflow for exponent of 0 or 1. Use table
                        // for exponents up to and including 31.
                        if (l2 > 1) {
                            if (l2 <= 31) {
                                int[] range = POW_RANGES[l2 - 2];
                                Assert.check(range[0] == l2);
                                int minBase = range[1];
                                int maxBase = range[2];
                                if (l1 < minBase || u1 > maxBase) {
                                    // Possible overflow.
                                    tchecker.addProblem(ErrMsg.FCALL_POW_OVERFLOW, astStdLib.position,
                                            CifTextUtils.typeToStr(atypes[0]), CifTextUtils.typeToStr(atypes[1]));
                                    throw new SemanticException();
                                }
                            } else {
                                if (l1 < -1 || u1 > 1) {
                                    // Possible overflow.
                                    tchecker.addProblem(ErrMsg.FCALL_POW_OVERFLOW, astStdLib.position,
                                            CifTextUtils.typeToStr(atypes[0]), CifTextUtils.typeToStr(atypes[1]));
                                    throw new SemanticException();
                                }
                            }
                        }

                        int[] resultRange = getPowResultRange(l1, u1, l2, u2);

                        IntType itype = newIntType();
                        itype.setPosition(copyPosition(astStdLib.position));

                        resultType.setReturnType(itype);

                        itype.setLower(resultRange[0]);
                        itype.setUpper(resultRange[1]);

                        done = true;
                    }
                }

                if (!done) {
                    RealType type = newRealType();
                    type.setPosition(copyPosition(astStdLib.position));
                    resultType.setReturnType(type);
                }

                break;
            }

            case ROUND: {
                // real -> int

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                IntType type = newIntType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case SCALE: {
                // int/real (5 parameters) -> real

                for (int i = 0; i < args.size(); i++) {
                    if (!(natypes[i] instanceof IntType) && !(natypes[i] instanceof RealType)) {
                        addFcallArgProblem(astStdLib, args, tchecker);
                    }
                }

                RealType type = newRealType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case SIGN: {
                // int[l..u] -> int[sgn(l) .. sgn(u)]
                // int -> int[-1 .. 1]
                // real -> int[-1 .. 1]

                if (!(natypes[0] instanceof IntType) && !(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                IntType itype = newIntType();
                itype.setPosition(copyPosition(astStdLib.position));

                resultType.setReturnType(itype);

                if (natypes[0] instanceof IntType && !isRangeless((IntType)natypes[0])) {
                    int lower = ((IntType)natypes[0]).getLower();
                    int upper = ((IntType)natypes[0]).getUpper();

                    itype.setLower(CifMath.sign(lower));
                    itype.setUpper(CifMath.sign(upper));
                } else {
                    itype.setLower(-1);
                    itype.setUpper(1);
                }

                break;
            }

            case SIZE: {
                // string -> int
                // list t -> int
                // list[l..u] t -> int[l..u]
                // set t -> int
                // dict(k:v) -> int

                if (!(natypes[0] instanceof StringType) && !(natypes[0] instanceof ListType)
                        && !(natypes[0] instanceof SetType) && !(natypes[0] instanceof DictType))
                {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                IntType type = newIntType();
                type.setPosition(copyPosition(astStdLib.position));

                if (natypes[0] instanceof ListType && !CifTypeUtils.isRangeless((ListType)natypes[0])) {
                    ListType ltype = (ListType)natypes[0];
                    type.setLower(ltype.getLower());
                    type.setUpper(ltype.getUpper());
                }

                resultType.setReturnType(type);
                break;
            }

            case SQRT: {
                // real -> real

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                RealType type = newRealType();
                type.setPosition(copyPosition(astStdLib.position));
                resultType.setReturnType(type);
                break;
            }

            case BERNOULLI: {
                // real -> dist bool

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newBoolType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case BETA: {
                // real, real -> dist real

                if (!(natypes[0] instanceof RealType) || !(natypes[1] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newRealType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case BINOMIAL: {
                // real, int -> dist int

                if (!(natypes[0] instanceof RealType) || !(natypes[1] instanceof IntType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newIntType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case CONSTANT: {
                // bool -> dist bool
                // int[l..u] -> dist int
                // int -> dist int
                // real -> dist real

                if (!(natypes[0] instanceof BoolType) && !(natypes[0] instanceof IntType)
                        && !(natypes[0] instanceof RealType))
                {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));

                if (natypes[0] instanceof IntType && !isRangeless((IntType)natypes[0])) {
                    CifType stype = newIntType();
                    stype.setPosition(copyPosition(astStdLib.position));
                    type.setSampleType(stype);
                } else {
                    type.setSampleType(deepclone(atypes[0]));
                }

                resultType.setReturnType(type);
                break;
            }

            case ERLANG: {
                // int, real -> dist real

                if (!(natypes[0] instanceof IntType) || !(natypes[1] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newRealType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case EXPONENTIAL: {
                // real -> dist real

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newRealType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case GAMMA: {
                // real, real -> dist real

                if (!(natypes[0] instanceof RealType) || !(natypes[1] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newRealType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case GEOMETRIC: {
                // real -> dist int

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newIntType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case LOG_NORMAL:
            case NORMAL: {
                // real, real -> dist real

                if (!(natypes[0] instanceof RealType) || !(natypes[1] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newRealType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case POISSON: {
                // real -> dist int

                if (!(natypes[0] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newIntType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case RANDOM: {
                // () -> dist real

                CifType stype = newRealType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case TRIANGLE: {
                // real, real, real -> dist real

                if (!(natypes[0] instanceof RealType) || !(natypes[1] instanceof RealType)
                        || !(natypes[2] instanceof RealType))
                {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newRealType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }

            case UNIFORM: {
                // int, int -> dist int
                // real, real -> dist real

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));

                resultType.setReturnType(type);

                if (natypes[0] instanceof IntType && natypes[1] instanceof IntType) {
                    CifType stype = newIntType();
                    stype.setPosition(copyPosition(astStdLib.position));
                    type.setSampleType(stype);
                } else if (natypes[0] instanceof RealType && natypes[1] instanceof RealType) {
                    CifType stype = newRealType();
                    stype.setPosition(copyPosition(astStdLib.position));
                    type.setSampleType(stype);
                } else {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                break;
            }

            case WEIBULL: {
                // real, real -> dist real

                if (!(natypes[0] instanceof RealType) || !(natypes[1] instanceof RealType)) {
                    addFcallArgProblem(astStdLib, args, tchecker);
                }

                CifType stype = newRealType();
                stype.setPosition(copyPosition(astStdLib.position));

                DistType type = newDistType();
                type.setPosition(copyPosition(astStdLib.position));
                type.setSampleType(stype);

                resultType.setReturnType(type);
                break;
            }
        }

        // Set type of the function call expression.
        mmCall.setType(deepclone(resultType.getReturnType()));

        // Return function call expression.
        return mmCall;
    }

    /**
     * Checks the actual number of arguments against the expected number of arguments, for a standard library function,
     * and adds a problem to the type checker if the counts don't match.
     *
     * @param expr The CIF AST expression of the standard library function reference expression.
     * @param args The CIF AST function call arguments.
     * @param expected The expected number of arguments.
     * @param tchecker The CIF type checker to use.
     * @throws SemanticException If the actual number of arguments doesn't match the expected number of arguments.
     */
    private static void checkFuncCallArgCounts(AStdLibFunctionExpression expr, List<AExpression> args, int expected,
            CifTypeChecker tchecker)
    {
        if (args.size() != expected) {
            tchecker.addProblem(ErrMsg.FCALL_WRONG_ARG_COUNT, expr.position, "\"" + expr.function + "\"",
                    String.valueOf(expected) + " argument" + (expected == 1 ? "" : "s"),
                    String.valueOf(args.size()) + (args.size() == 1 ? " argument is" : " arguments are"));
            throw new SemanticException();
        }
    }

    /**
     * Adds a problem to the type checker for a standard library function call, where at least one of the arguments is
     * of a wrong type.
     *
     * @param expr The CIF AST expression of the standard library function reference expression.
     * @param args The already type checked function arguments.
     * @param tchecker The CIF type checker to use.
     * @throws SemanticException Always thrown.
     */
    private static void addFcallArgProblem(AStdLibFunctionExpression expr, List<Expression> args,
            CifTypeChecker tchecker)
    {
        List<String> argTypeTexts = list();
        for (Expression arg: args) {
            argTypeTexts.add("\"" + CifTextUtils.typeToStr(arg.getType()) + "\"");
        }
        tchecker.addProblem(ErrMsg.FCALL_WRONG_ARG_TYPES, expr.position, "\"" + expr.function + "\"",
                StringUtils.join(argTypeTexts, ", "));
        throw new SemanticException();
    }

    /**
     * Returns the result range for the 'abs' function, given the input range.
     *
     * @param lower The lower bound of the input range.
     * @param upper The upper bound of the input range.
     * @return The lower and upper bound of the result range.
     */
    public static int[] getAbsResultRange(int lower, int upper) {
        int rsltL = Math.min(Math.abs(lower), Math.abs(upper));
        int rsltU = Math.max(Math.abs(lower), Math.abs(upper));
        if (lower <= 0 && 0 <= upper) {
            rsltL = 0;
        }
        return new int[] {rsltL, rsltU};
    }

    /** Allowed 'pow' integer ranges. */
    public static final int[][] POW_RANGES = {
            // exp, min base, max base
            {2, -46340, 46340},
            {3, -1290, 1290},
            {4, -215, 215},
            {5, -73, 73},
            {6, -35, 35},
            {7, -21, 21},
            {8, -14, 14},
            {9, -10, 10},
            {10, -8, 8},
            {11, -7, 7},
            {12, -5, 5},
            {13, -5, 5},
            {14, -4, 4},
            {15, -4, 4},
            {16, -3, 3},
            {17, -3, 3},
            {18, -3, 3},
            {19, -3, 3},
            {20, -2, 2},
            {21, -2, 2},
            {22, -2, 2},
            {23, -2, 2},
            {24, -2, 2},
            {25, -2, 2},
            {26, -2, 2},
            {27, -2, 2},
            {28, -2, 2},
            {29, -2, 2},
            {30, -2, 2},
            {31, -2, 1},
            // end
    };

    /**
     * Returns the result range for the 'pow' function, given the input ranges. Only valid for 'pow' on integers with
     * ranges, where the second range does not include negative values.
     *
     * @param l1 The lower bound of the base input range.
     * @param u1 The upper bound of the base input range.
     * @param l2 The lower bound of the exponent input range.
     * @param u2 The upper bound of the exponent input range.
     * @return The lower and upper bound of the result range.
     */
    public static int[] getPowResultRange(int l1, int u1, int l2, int u2) {
        Assert.check(l2 >= 0);

        List<Integer> range1 = list(l1, u1);
        List<Integer> range2 = list(l2, u2);

        if (l1 <= 0 && 0 <= u1) {
            range1.add(0);
        }
        if (l2 <= 0 && 0 <= u2) {
            range2.add(0);
        }

        if (l1 + 1 <= u1) {
            range1.add(l1 + 1);
        }
        if (l2 + 1 <= u2) {
            range2.add(l2 + 1);
        }

        if (l1 <= u1 - 1) {
            range1.add(u1 - 1);
        }
        if (l2 <= u2 - 1) {
            range2.add(u2 - 1);
        }

        int minimum = Integer.MAX_VALUE;
        int maximum = Integer.MIN_VALUE;
        for (int x: range1) {
            for (int y: range2) {
                Assert.check(y >= 0);
                int rslt = (int)Math.pow(x, y);
                minimum = Math.min(minimum, rslt);
                maximum = Math.max(maximum, rslt);
            }
        }
        return new int[] {minimum, maximum};
    }

    /**
     * Transforms a list expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static ListExpression transListExpression(AListExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Get type hint for first child.
        CifType nhint = normalizeHint(hint);
        CifType childHint = null;
        if (nhint instanceof ListType) {
            childHint = ((ListType)nhint).getElementType();
        }

        // Transform elements.
        List<Expression> elems = listc(expr.elements.size());
        boolean first = true;
        for (AExpression aexpr: expr.elements) {
            Expression child = transExpression(aexpr, childHint, scope, context, tchecker);
            elems.add(child);
            if (first) {
                first = false;
                childHint = child.getType();
            }
        }

        // Get element type.
        CifType etype = null;
        if (elems.isEmpty()) {
            if (childHint != null) {
                etype = deepclone(childHint);
            } else {
                tchecker.addProblem(ErrMsg.EXPR_UNKNOWN_TYPE, expr.position, "[]");
                throw new SemanticException();
            }
        } else {
            for (Expression elem: elems) {
                if (etype == null) {
                    etype = deepclone(elem.getType());

                    // Check element type for first element.
                    if (CifTypeUtils.hasComponentLikeType(etype)) {
                        tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, expr.position, CifTextUtils.typeToStr(etype),
                                "elements of lists");
                        throw new SemanticException();
                    }
                } else {
                    if (!checkTypeCompat(etype, elem.getType(), RangeCompat.IGNORE)) {
                        tchecker.addProblem(ErrMsg.CONTAINER_EXPR_INCOMPAT_TYPES, elem.getPosition(),
                                CifTextUtils.typeToStr(etype), CifTextUtils.typeToStr(elem.getType()),
                                "elements of a list");
                        tchecker.addProblem(ErrMsg.CONTAINER_EXPR_INCOMPAT_TYPES, first(elems).getPosition(),
                                CifTextUtils.typeToStr(etype), CifTextUtils.typeToStr(elem.getType()),
                                "elements of a list");
                        throw new SemanticException();
                    }

                    etype = CifTypeUtils.mergeTypes(etype, elem.getType());
                }
            }
        }

        // Create list type. Ignore list type hint, including its range bounds.
        ListType type = newListType();
        type.setPosition(copyPosition(expr.position));
        type.setElementType(etype);
        type.setLower(elems.size());
        type.setUpper(elems.size());

        // Create list expression.
        ListExpression rslt = newListExpression();
        rslt.setPosition(expr.position);
        rslt.setType(type);
        rslt.getElements().addAll(elems);
        return rslt;
    }

    /**
     * Transforms an 'empty set or dictionary' expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static Expression transEmptySetDictExpression(AEmptySetDictExpression expr, CifType hint,
            SymbolScope<?> scope, ExprContext context, CifTypeChecker tchecker)
    {
        // Try to derive expr/type from type hint.
        CifType nhint = normalizeHint(hint);
        Expression rslt = null;
        if (nhint instanceof SetType) {
            rslt = newSetExpression();
        } else if (nhint instanceof DictType) {
            rslt = newDictExpression();
        } else {
            tchecker.addProblem(ErrMsg.EXPR_UNKNOWN_TYPE, expr.position, "{}");
            throw new SemanticException();
        }

        // Complete the metamodel representation and return it.
        rslt.setPosition(expr.position);
        rslt.setType(deepclone(hint));
        return rslt;
    }

    /**
     * Transforms a non-empty set expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static SetExpression transSetExpression(ASetExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Precondition check.
        Assert.check(!expr.elements.isEmpty());

        // Get type hint for first child.
        CifType nhint = normalizeHint(hint);
        CifType childHint = null;
        if (nhint instanceof SetType) {
            childHint = ((SetType)nhint).getElementType();
        }

        // Transform elements.
        List<Expression> elems = listc(expr.elements.size());
        boolean first = true;
        for (AExpression aexpr: expr.elements) {
            Expression child = transExpression(aexpr, childHint, scope, context, tchecker);
            elems.add(child);
            if (first) {
                first = false;
                childHint = child.getType();
            }
        }

        // Get element type.
        CifType etype = null;
        for (Expression elem: elems) {
            if (etype == null) {
                etype = deepclone(elem.getType());

                // Check element type for first element.
                if (!CifTypeUtils.supportsValueEquality(etype)) {
                    tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, expr.position, CifTextUtils.typeToStr(etype),
                            "elements of sets");
                    throw new SemanticException();
                }
            } else {
                if (!checkTypeCompat(etype, elem.getType(), RangeCompat.IGNORE)) {
                    tchecker.addProblem(ErrMsg.CONTAINER_EXPR_INCOMPAT_TYPES, elem.getPosition(),
                            CifTextUtils.typeToStr(etype), CifTextUtils.typeToStr(elem.getType()), "elements of a set");
                    tchecker.addProblem(ErrMsg.CONTAINER_EXPR_INCOMPAT_TYPES, first(elems).getPosition(),
                            CifTextUtils.typeToStr(etype), CifTextUtils.typeToStr(elem.getType()), "elements of a set");
                    throw new SemanticException();
                }

                etype = CifTypeUtils.mergeTypes(etype, elem.getType());
            }
        }

        // Create set type.
        SetType type = newSetType();
        type.setPosition(copyPosition(expr.position));
        type.setElementType(etype);

        // Create set expression.
        SetExpression rslt = newSetExpression();
        rslt.setPosition(expr.position);
        rslt.setType(type);
        rslt.getElements().addAll(elems);
        return rslt;
    }

    /**
     * Transforms a tuple expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static TupleExpression transTupleExpression(ATupleExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Get type hints for fields.
        CifType nhint = normalizeHint(hint);
        CifType[] fieldHints = new CifType[expr.elements.size()];
        if (nhint instanceof TupleType) {
            List<Field> fields = ((TupleType)nhint).getFields();
            int cnt = Math.min(fieldHints.length, fields.size());
            for (int i = 0; i < cnt; i++) {
                fieldHints[i] = fields.get(i).getType();
            }
        }

        // Transform elements.
        List<Expression> elems = listc(expr.elements.size());
        for (int i = 0; i < expr.elements.size(); i++) {
            AExpression aexpr = expr.elements.get(i);
            CifType fieldHint = fieldHints[i];
            Expression elem = transExpression(aexpr, fieldHint, scope, context, tchecker);
            elems.add(elem);
        }

        // Get tuple type.
        TupleType ttype = newTupleType();
        ttype.setPosition(copyPosition(expr.position));
        for (Expression elem: elems) {
            // Add nameless field to tuple type.
            Field field = newField();
            field.setPosition(copyPosition(elem.getPosition()));
            field.setType(deepclone(elem.getType()));
            ttype.getFields().add(field);

            // Check element type.
            if (CifTypeUtils.hasComponentLikeType(elem.getType())) {
                tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, expr.position, CifTextUtils.typeToStr(elem.getType()),
                        "fields of tuples");
                throw new SemanticException();
            }
        }

        // Create tuple expression.
        TupleExpression rslt = newTupleExpression();
        rslt.setPosition(expr.position);
        rslt.setType(ttype);
        rslt.getFields().addAll(elems);
        return rslt;
    }

    /**
     * Transforms a non-empty dictionary expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static DictExpression transDictExpression(ADictExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Precondition check.
        Assert.check(!expr.pairs.isEmpty());

        // Get type hints for first pair.
        CifType nhint = normalizeHint(hint);
        CifType keyHint = null;
        CifType valueHint = null;
        if (nhint instanceof DictType) {
            keyHint = ((DictType)nhint).getKeyType();
            valueHint = ((DictType)nhint).getValueType();
        }

        // Transform pairs.
        List<DictPair> pairs = listc(expr.pairs.size());
        boolean first = true;
        for (ADictPair apair: expr.pairs) {
            Expression key = transExpression(apair.key, keyHint, scope, context, tchecker);
            Expression value = transExpression(apair.value, valueHint, scope, context, tchecker);

            DictPair pair = newDictPair();
            pair.setPosition(apair.position);
            pair.setKey(key);
            pair.setValue(value);
            pairs.add(pair);

            if (first) {
                first = false;
                keyHint = key.getType();
                valueHint = value.getType();
            }
        }

        // Get key/value types.
        CifType ktype = null;
        CifType vtype = null;
        for (DictPair pair: pairs) {
            if (ktype == null) {
                ktype = deepclone(pair.getKey().getType());
                vtype = deepclone(pair.getValue().getType());

                // Check key type for first pair.
                if (!CifTypeUtils.supportsValueEquality(ktype)) {
                    tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, expr.position, CifTextUtils.typeToStr(ktype),
                            "keys of dictionaries");
                    throw new SemanticException();
                }

                // Check value type for first pair.
                if (CifTypeUtils.hasComponentLikeType(vtype)) {
                    tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, expr.position, CifTextUtils.typeToStr(vtype),
                            "values of dictionaries");
                    throw new SemanticException();
                }
            } else {
                // Key type.
                if (!checkTypeCompat(ktype, pair.getKey().getType(), RangeCompat.IGNORE)) {
                    tchecker.addProblem(ErrMsg.CONTAINER_EXPR_INCOMPAT_TYPES, pair.getKey().getPosition(),
                            CifTextUtils.typeToStr(ktype), CifTextUtils.typeToStr(pair.getKey().getType()),
                            "keys of a dictionary");
                    tchecker.addProblem(ErrMsg.CONTAINER_EXPR_INCOMPAT_TYPES, first(pairs).getKey().getPosition(),
                            CifTextUtils.typeToStr(ktype), CifTextUtils.typeToStr(pair.getKey().getType()),
                            "keys of a dictionary");
                    throw new SemanticException();
                }

                ktype = CifTypeUtils.mergeTypes(ktype, pair.getKey().getType());

                // Value type.
                if (!checkTypeCompat(vtype, pair.getValue().getType(), RangeCompat.IGNORE)) {
                    tchecker.addProblem(ErrMsg.CONTAINER_EXPR_INCOMPAT_TYPES, pair.getValue().getPosition(),
                            CifTextUtils.typeToStr(vtype), CifTextUtils.typeToStr(pair.getValue().getType()),
                            "values of a dictionary");
                    tchecker.addProblem(ErrMsg.CONTAINER_EXPR_INCOMPAT_TYPES, first(pairs).getValue().getPosition(),
                            CifTextUtils.typeToStr(vtype), CifTextUtils.typeToStr(pair.getValue().getType()),
                            "values of a dictionary");
                    throw new SemanticException();
                }

                vtype = CifTypeUtils.mergeTypes(vtype, pair.getValue().getType());
            }
        }

        // Create dictionary expression.
        DictType type = newDictType();
        type.setPosition(copyPosition(expr.position));
        type.setKeyType(ktype);
        type.setValueType(vtype);

        DictExpression rslt = newDictExpression();
        rslt.setPosition(expr.position);
        rslt.setType(type);
        rslt.getPairs().addAll(pairs);
        return rslt;
    }

    /**
     * Transforms an 'if' expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static IfExpression transIfExpression(AIfExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        IfExpression rslt = newIfExpression();
        rslt.setPosition(expr.position);

        // Guards.
        List<Expression> guards = rslt.getGuards();
        for (AExpression g: expr.guards) {
            Expression guard = transExpression(g, BOOL_TYPE_HINT, scope, context, tchecker);
            CifType t = guard.getType();
            CifType nt = CifTypeUtils.normalizeType(t);
            if (!(nt instanceof BoolType)) {
                tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                throw new SemanticException();
            }
            guards.add(guard);
        }

        // Then.
        Expression thenExpr = transExpression(expr.then, hint, scope, context, tchecker);
        CifType thenType = thenExpr.getType();
        rslt.setThen(thenExpr);

        if (CifTypeUtils.hasComponentLikeType(thenType)) {
            tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, expr.position, CifTextUtils.typeToStr(thenType),
                    "results of ifs");
            throw new SemanticException();
        }

        // Else.
        Expression elseExpr = transExpression(expr.elseExpr, hint, scope, context, tchecker);
        CifType elseType = elseExpr.getType();
        rslt.setElse(elseExpr);

        if (!(checkTypeCompat(thenType, elseType, RangeCompat.IGNORE))) {
            tchecker.addProblem(ErrMsg.IFEXPR_INCOMPAT_TYPES, elseExpr.getPosition(), CifTextUtils.typeToStr(thenType),
                    "else", CifTextUtils.typeToStr(elseType));
            throw new SemanticException();
        }

        // Elifs.
        List<ElifExpression> elifs = rslt.getElifs();
        for (AElifExpression elif: expr.elifs) {
            ElifExpression elifRslt = newElifExpression();
            elifRslt.setPosition(elif.position);
            elifs.add(elifRslt);

            // Guards.
            guards = elifRslt.getGuards();
            for (AExpression g: elif.guards) {
                Expression guard = transExpression(g, BOOL_TYPE_HINT, scope, context, tchecker);
                CifType t = guard.getType();
                CifType nt = CifTypeUtils.normalizeType(t);
                if (!(nt instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                    throw new SemanticException();
                }
                guards.add(guard);
            }

            // Elif/then.
            Expression elifExpr = transExpression(elif.then, hint, scope, context, tchecker);
            CifType elifType = elifExpr.getType();
            elifRslt.setThen(elifExpr);

            if (!(checkTypeCompat(thenType, elifType, RangeCompat.IGNORE))) {
                tchecker.addProblem(ErrMsg.IFEXPR_INCOMPAT_TYPES, elifExpr.getPosition(),
                        CifTextUtils.typeToStr(thenType), "elif", CifTextUtils.typeToStr(elifType));
                throw new SemanticException();
            }
        }

        // Compute type for 'if' expression. Note that we merge at least once,
        // and thus don't need to deep clone here.
        CifType mergedType = thenType;
        mergedType = CifTypeUtils.mergeTypes(mergedType, elseType);
        for (ElifExpression elif: rslt.getElifs()) {
            CifType elifType = elif.getThen().getType();
            mergedType = CifTypeUtils.mergeTypes(mergedType, elifType);
        }
        rslt.setType(mergedType);

        // Return 'if' expression.
        return rslt;
    }

    /**
     * Transforms a 'switch' expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static SwitchExpression transSwitchExpression(ASwitchExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        SwitchExpression rslt = newSwitchExpression();
        rslt.setPosition(expr.position);

        // Value.
        Expression value = transExpression(expr.value, NO_TYPE_HINT, scope, context, tchecker);
        rslt.setValue(value);

        // Check for 'value' being an automaton reference.
        boolean isAutRef = CifTypeUtils.isAutRefExpr(value);

        // Disallow component (definition) types, and check for value equality
        // of the 'value'.
        if (!isAutRef) {
            if (CifTypeUtils.hasComponentLikeType(value.getType())) {
                tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, expr.value.position,
                        CifTextUtils.typeToStr(value.getType()), "the control value of a switch");
                throw new SemanticException();
            }

            if (!CifTypeUtils.supportsValueEquality(value.getType())) {
                tchecker.addProblem(ErrMsg.SWITCH_NO_VALUE_EQ, expr.value.position, typeToStr(value.getType()));
                throw new SemanticException();
            }
        }

        // Cases.
        List<SwitchCase> cases = rslt.getCases();
        CifType firstCaseValueType = null;
        boolean caseErr = false;
        for (ASwitchCase acase: expr.cases) {
            // Transform and check case.
            SwitchCase cse;
            try {
                cse = transSwitchCase(expr, rslt.getValue(), acase, isAutRef, firstCaseValueType, hint, scope, context,
                        tchecker);
            } catch (SemanticException ex) {
                // If type checking fails for a case, continue with the next
                // one. Cases should be completely independent from each other.
                // Note that we skip adding the case as well.
                caseErr = true;
                continue;
            }

            // Add case.
            cases.add(cse);

            // Prepare for next case.
            if (firstCaseValueType == null) {
                firstCaseValueType = cse.getValue().getType();
            }
        }
        if (caseErr) {
            throw new SemanticException();
        }

        // Check for completeness and being overspecified.
        if (isAutRef) {
            // Check for incomplete/overspecified locations.
            checkSwitchLocsComplete(rslt, scope, tchecker);
        } else {
            // Check for missing 'else'.
            if (last(cases).getKey() != null) {
                tchecker.addProblem(ErrMsg.SWITCH_MISSING_ELSE, expr.position);
                throw new SemanticException();
            }
        }

        // Compute type for the 'switch' expression. If we don't merge, we need
        // to deep clone.
        CifType mergedType = null;
        for (SwitchCase cse: cases) {
            CifType caseType = cse.getValue().getType();
            mergedType = (mergedType == null) ? caseType : CifTypeUtils.mergeTypes(mergedType, caseType);
        }
        if (cases.size() == 1) {
            mergedType = deepclone(mergedType);
        }
        rslt.setType(mergedType);

        // Return 'switch' expression.
        return rslt;
    }

    /**
     * Transforms a 'switch' case and performs type checking on it.
     *
     * @param astSwitch The 'switch' AST expression.
     * @param switchValue The 'value' of the 'switch' expression.
     * @param astCase The CIF AST 'switch' case to transform.
     * @param isAutRef Is the value of the 'switch' expression an automaton reference?
     * @param firstCaseValueType The type of the value of the first case, or {@code null} if not applicable/available.
     * @param hint The expected type of the 'value' expression. Ignored if it doesn't fit. May be {@code null} if not
     *     available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static SwitchCase transSwitchCase(ASwitchExpression astSwitch, Expression switchValue, ASwitchCase astCase,
            boolean isAutRef, CifType firstCaseValueType, CifType hint, SymbolScope<?> scope, ExprContext context,
            CifTypeChecker tchecker)
    {
        // Create metamodel representation.
        SwitchCase cse = newSwitchCase();
        cse.setPosition(astCase.position);

        // Key.
        AExpression astKey = astCase.key;
        if (astKey == null) {
            // 'else' case.
        } else if (isAutRef) {
            // Get automaton.
            Automaton aut = getAutomaton(switchValue, scope);

            // The key expression must be a location identifier. Keyword
            // escaping has already been removed by the parser. No other
            // expressions are allowed.
            if (!(astKey instanceof ANameExpression)) {
                tchecker.addProblem(ErrMsg.SWITCH_CASE_LOC_ID, astKey.position, getAbsName(aut));
                throw new SemanticException();
            }

            ANameExpression astNameExpr = (ANameExpression)astKey;
            if (astNameExpr.derivative) {
                tchecker.addProblem(ErrMsg.SWITCH_CASE_LOC_ID, astKey.position, getAbsName(aut));
                throw new SemanticException();
            }

            String locName = astNameExpr.name.name;
            if (!CifValidationUtils.isValidIdentifier(locName)) {
                tchecker.addProblem(ErrMsg.SWITCH_CASE_LOC_ID, astKey.position, getAbsName(aut));
                throw new SemanticException();
            }

            // Single identifier found. Get scope of referred automaton.
            AExpression astSwitchValue = astSwitch.value;
            SymbolScope<?> keyScope;
            boolean isSelf = astSwitchValue instanceof ASelfExpression;
            if (isSelf) {
                keyScope = scope;
            } else {
                // Get automaton reference text.
                Assert.check(astSwitchValue instanceof ANameExpression);
                String autRef = ((ANameExpression)astSwitchValue).name.name;

                // Resolve reference to a scope. No convoluted references check as we already know we have a single
                // identifier.
                SymbolTableEntry entry = scope.resolve(null, autRef, tchecker, null);
                keyScope = (SymbolScope<?>)entry;

                // Handle scopes that can only be used as 'via' scopes.
                if (keyScope instanceof CompInstScope) {
                    keyScope = ((CompInstScope)keyScope).getCompDefScope();
                } else if (keyScope instanceof CompParamScope) {
                    keyScope = ((CompParamScope)keyScope).getCompDefScope();
                }
            }

            // Transform expression, to use general reference expression type
            // and scope checking. We resolve in the 'key' scope, which is the
            // scope of the automaton referred to by the 'value' of the
            // 'switch' expression.
            Expression key = transExpression(astKey, NO_TYPE_HINT, keyScope, context, tchecker);
            cse.setKey(key);

            // Make sure it refers to a location.
            Expression ukey = unwrapExpression(key);
            if (!(ukey instanceof LocationExpression)) {
                tchecker.addProblem(ErrMsg.SWITCH_CASE_LOC_REF, astKey.position, getAbsName(getRefObjFromRef(key)),
                        getAbsName(aut));
                throw new SemanticException();
            }

            // Forbid location parameter reference.
            Location loc = ((LocationExpression)ukey).getLocation();
            if (!(loc.eContainer() instanceof Automaton)) {
                tchecker.addProblem(ErrMsg.SWITCH_CASE_LOC_REF, astKey.position, getAbsName(loc), getAbsName(aut));
                throw new SemanticException();
            }

            // Make sure it is a location of the correct automaton. This is a
            // paranoia check only, as it should hold by construction, as we
            // only allowed an identifier above, not a general name. Also,
            // location parameters, which can be referred to with an
            // identifier, have been forbidden above.
            Automaton locAut = (Automaton)loc.eContainer();
            Assert.check(locAut == aut);

            // Fix the scoping of the location reference expression.
            changeLocRefScope(key, switchValue);
        } else {
            // Transform expression.
            CifType keyHint = switchValue.getType();
            Expression key = transExpression(astKey, keyHint, scope, context, tchecker);
            cse.setKey(key);

            // Check type.
            if (!checkTypeCompat(switchValue.getType(), key.getType(), RangeCompat.IGNORE)) {
                tchecker.addProblem(ErrMsg.SWITCH_CASE_KEY_TYPE, astKey.position, typeToStr(key.getType()),
                        typeToStr(switchValue.getType()));
                throw new SemanticException();
            }
        }

        // Value.
        CifType valueHint = (firstCaseValueType == null) ? hint : firstCaseValueType;
        Expression valueExpr = transExpression(astCase.value, valueHint, scope, context, tchecker);
        cse.setValue(valueExpr);

        CifType caseValueType = valueExpr.getType();
        if (CifTypeUtils.hasComponentLikeType(caseValueType)) {
            tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, astCase.value.position, CifTextUtils.typeToStr(caseValueType),
                    "results of switches");
            throw new SemanticException();
        }

        // Check type of value against type of the value of the first case.
        if (firstCaseValueType != null) {
            boolean typeMatch = checkTypeCompat(firstCaseValueType, caseValueType, RangeCompat.IGNORE);
            if (!typeMatch) {
                tchecker.addProblem(ErrMsg.SWITCH_EXPR_INCOMPAT_TYPES, valueExpr.getPosition(),
                        CifTextUtils.typeToStr(caseValueType), CifTextUtils.typeToStr(firstCaseValueType));
                throw new SemanticException();
            }
        }

        // Return metamodel representation.
        return cse;
    }

    /**
     * Changes a location reference that is valid from the scope of the automaton that contains the location, to a
     * location reference that is valid from a 'switch' expression that refers to that automaton as its control value.
     *
     * @param locRef The location reference expression. Additional wrapping expressions may be added, replacing the
     *     expression in its containment.
     * @param autRef The automaton reference expression, valid from the scope of the 'switch' expression. It may be an
     *     automaton self reference.
     * @return The location reference, potentially with added wrapping expressions.
     */
    private static Expression changeLocRefScope(Expression locRef, Expression autRef) {
        // Automaton 'self' reference.
        if (autRef instanceof SelfExpression) {
            return locRef;
        }

        // Automaton parameter reference expression. Add a component parameter wrapping expression.
        if (autRef instanceof CompParamExpression) {
            // Create wrapping expression for automaton parameter reference.
            CompParamExpression expr = (CompParamExpression)autRef;
            CompParamWrapExpression wrap = newCompParamWrapExpression();

            // Put parameter in the new wrapper.
            wrap.setParameter(expr.getParameter());

            // Location references always have a boolean type.
            wrap.setType(newBoolType());

            // Replace current location reference by new wrapper.
            EMFHelper.updateParentContainment(locRef, wrap);

            // Put current location reference in new wrapper.
            wrap.setReference(locRef);

            // Return new wrapped location reference.
            return wrap;
        }

        // Wrapping expressions. Move inwards first, then add the wrapper.
        if (autRef instanceof CompParamWrapExpression) {
            // Recursively handle child of wrapped automaton reference.
            CompParamWrapExpression wrap1 = (CompParamWrapExpression)autRef;
            locRef = changeLocRefScope(locRef, wrap1.getReference());

            // Copy wrapping expression.
            CompParamWrapExpression wrap2 = newCompParamWrapExpression();
            wrap2.setParameter(wrap1.getParameter());

            // Location references always have a boolean type.
            wrap2.setType(newBoolType());

            // Replace current location reference by new wrapper.
            EMFHelper.updateParentContainment(locRef, wrap2);

            // Put current location reference in new wrapper.
            wrap2.setReference(locRef);

            // Return new wrapped location reference.
            return wrap2;
        }

        if (autRef instanceof CompInstWrapExpression) {
            // Recursively handle child of wrapped automaton reference.
            CompInstWrapExpression wrap1 = (CompInstWrapExpression)autRef;
            locRef = changeLocRefScope(locRef, wrap1.getReference());

            // Copy wrapping expression.
            CompInstWrapExpression wrap2 = newCompInstWrapExpression();
            wrap2.setInstantiation(wrap1.getInstantiation());

            // Location references always have a boolean type.
            wrap2.setType(newBoolType());

            // Replace current location reference by new wrapper.
            EMFHelper.updateParentContainment(locRef, wrap2);

            // Put current location reference in new wrapper.
            wrap2.setReference(locRef);

            // Return new wrapped location reference.
            return wrap2;
        }

        // Component reference.
        if (autRef instanceof ComponentExpression) {
            // Get component.
            Component comp = ((ComponentExpression)autRef).getComponent();

            // If its an automaton reference, no wrapping is needed, as
            // automata introduce a sub-scope, not a root scope.
            if (comp instanceof Automaton) {
                return locRef;
            }

            // If it is an automaton instantiation, we need to add a wrapping
            // expression for it. First, do some paranoia checking.
            Assert.check(comp instanceof ComponentInst);
            Automaton aut = CifScopeUtils.getAutomaton(comp);
            Assert.notNull(aut);

            // Create wrapping expression for automaton instantiation.
            CompInstWrapExpression wrap = newCompInstWrapExpression();
            wrap.setType(newBoolType());
            wrap.setInstantiation((ComponentInst)comp);

            // Replace current location reference by new wrapper.
            EMFHelper.updateParentContainment(locRef, wrap);

            // Put current location reference in new wrapper.
            wrap.setReference(locRef);

            // Return new wrapped location reference.
            return wrap;
        }

        // Unknown automaton reference.
        throw new RuntimeException("Unexpected aut ref/wrap expr: " + autRef);
    }

    /**
     * Type check a 'switch' expression on an automaton reference, for completeness of the locations. Also checks for
     * overspecified cases.
     *
     * @param switchExpr The expression to check. Must have an automaton reference (including 'self') as value.
     * @param scope The scope to resolve references in.
     * @param tchecker The CIF type checker to use.
     */
    private static void checkSwitchLocsComplete(SwitchExpression switchExpr, SymbolScope<?> scope,
            CifTypeChecker tchecker)
    {
        // Initialization. If automaton has only a single location, it is not
        // in the symbol table. If the automaton then hasn't been checked yet,
        // it is also not in the metamodel. So, we may end up with no locations
        // in 'todo'.
        Automaton aut = getAutomaton(switchExpr.getValue(), scope);
        Set<Location> todo = list2set(aut.getLocations());
        Map<Location, Position> done = map();
        Position elsePos = null;

        // Process all locations of the keys of the cases.
        List<SwitchCase> cases = switchExpr.getCases();
        for (SwitchCase cse: cases) {
            Expression locRef = cse.getKey();
            if (locRef == null) {
                // An 'else' case.
                Assert.check(elsePos == null);
                elsePos = cse.getPosition();
            } else {
                // Get location.
                locRef = unwrapExpression(locRef);
                Location loc = ((LocationExpression)locRef).getLocation();

                // Location is done, so no longer 'todo'.
                todo.remove(loc);

                // Check for duplicate and add to 'done'.
                Position prevPos = done.get(loc);
                if (prevPos != null) {
                    // Duplicate location.
                    tchecker.addProblem(ErrMsg.SWITCH_AUT_DUPL_LOC, locRef.getPosition(), getAbsName(loc));
                    tchecker.addProblem(ErrMsg.SWITCH_AUT_DUPL_LOC, prevPos, getAbsName(loc));
                    throw new SemanticException();
                } else {
                    // New location.
                    done.put(loc, locRef.getPosition());
                }
            }
        }

        // Check for incomplete mapping.
        if (!todo.isEmpty() && elsePos == null) {
            // Incomplete.
            for (Location loc: todo) {
                // Since we syntactically require at least one case, we either
                // specify a name, or 'else'. If we have an automaton with a
                // nameless location, we can't specify a valid name, so we can
                // only specify 'else'. As such, we either have an error, or no
                // missing locations. As such, in case we have an error, the
                // location will have a name.
                tchecker.addProblem(ErrMsg.SWITCH_AUT_MISSING_LOC, switchExpr.getPosition(), getAbsName(loc));
            }
            throw new SemanticException();
        }

        // Check for overspecified mapping. We have to account for 'todo'
        // initially being empty, by checking whether the automaton has any
        // locations. If it has none, then it has only a single nameless
        // location. We can't refer to that location, so we must have used an
        // 'else'.
        if (todo.isEmpty() && !aut.getLocations().isEmpty() && elsePos != null) {
            // Overspecified.
            tchecker.addProblem(ErrMsg.SWITCH_AUT_SUPERFLUOUS_ELSE, elsePos);
            // Non-fatal problem.
        }
    }

    /**
     * Get the automaton referred to by an automaton reference.
     *
     * @param autRef An automaton reference.
     * @param scope The scope from which the automaton reference is valid.
     * @return The referred automaton.
     */
    private static Automaton getAutomaton(Expression autRef, SymbolScope<?> scope) {
        // Get rid of wrapping expressions.
        autRef = unwrapExpression(autRef);

        // Handle 'self' reference.
        if (autRef instanceof SelfExpression) {
            if (scope instanceof AutScope) {
                return ((AutScope)scope).getObject();
            } else {
                Assert.check(scope instanceof AutDefScope);
                ComponentDef cdef = ((AutDefScope)scope).getObject();
                return (Automaton)cdef.getBody();
            }
        }

        // Handle component parameter reference.
        if (autRef instanceof CompParamExpression) {
            CifType t = CifTypeUtils.normalizeType(((CompParamExpression)autRef).getType());
            Assert.check(t instanceof ComponentDefType);
            ComponentDef cdef = ((ComponentDefType)t).getDefinition();
            return (Automaton)cdef.getBody();
        }

        // Handle direct automaton reference.
        Assert.check(autRef instanceof ComponentExpression);
        Component comp = ((ComponentExpression)autRef).getComponent();
        return CifScopeUtils.getAutomaton(comp);
    }

    /**
     * Transforms a name expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static Expression transNameExpression(ANameExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Resolve the referenced object.
        SymbolTableEntry entry = scope.resolve(expr.position, expr.name.name, tchecker, scope);

        // Check for invalid references.
        if (entry instanceof EnumDeclWrap || entry instanceof TypeDeclWrap) {
            tchecker.addProblem(ErrMsg.TYPE_REF_IN_EXPR, expr.position, entry.getAbsName());
            throw new SemanticException();
        }

        Assert.check(!(entry instanceof SpecScope));

        if (entry instanceof AutDefScope || entry instanceof GroupDefScope) {
            tchecker.addProblem(ErrMsg.COMPDEF_REF_IN_EXPR, expr.position, entry.getAbsName());
            throw new SemanticException();
        }

        if ((entry instanceof EventDeclWrap || entry instanceof FormalEventDeclWrap)
                && (context == null || !context.conditions.contains(ALLOW_EVENT)))
        {
            // Invalid use of event reference found. Can't use event as value.
            // This is fatal, to ensure that we don't process an expression
            // with an invalid event reference.
            tchecker.addProblem(ErrMsg.EVENT_OCCURRENCE, expr.position, entry.getAbsName());
            throw new SemanticException();
        }

        // Type check it 'for use', so that we may get the type of the resolved
        // object when creating the expression.
        entry.tcheckForUse();

        // Return reference as an expression.
        Expression rslt = scope.resolveAsExpr(expr.name.name, expr.position, "", tchecker);

        // If it is a continuous variable, then we should set the derivative
        // field. Otherwise, derivatives are not allowed.
        Expression urslt = CifTypeUtils.unwrapExpression(rslt);
        if (urslt instanceof ContVariableExpression) {
            ((ContVariableExpression)urslt).setDerivative(expr.derivative);
        } else if (expr.derivative) {
            // Derivative of non-continuous variable not allowed.
            tchecker.addProblem(ErrMsg.DER_OF_NON_CONT_VAR, expr.position, "", entry.getAbsName());
            // Non-fatal error.
        }

        // Return the reference expression, with all wrappings intact.
        return rslt;
    }

    /**
     * Transforms a 'tau' expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @return The CIF metamodel expression.
     */
    private static Expression transTauExpression(ATauExpression expr, CifType hint, SymbolScope<?> scope) {
        BoolType type = newBoolType();
        type.setPosition(copyPosition(expr.position));

        TauExpression rslt = newTauExpression();
        rslt.setPosition(expr.position);
        rslt.setType(type);

        return rslt;
    }

    /**
     * Transforms a received value expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static Expression transReceivedExpression(AReceivedExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Check context.
        if (context == null || context.receiveType == null) {
            tchecker.addProblem(ErrMsg.RCVD_VALUE_OCCURRENCE, expr.position);
            throw new SemanticException();
        }

        // Check type.
        CifType ntype = normalizeType(context.receiveType);
        if (ntype instanceof VoidType) {
            tchecker.addProblem(ErrMsg.RCVD_VALUE_VOID, expr.position);
            throw new SemanticException();
        }

        // Construct metamodel representation.
        ReceivedExpression rslt = newReceivedExpression();
        rslt.setPosition(expr.position);
        rslt.setType(deepclone(context.receiveType));
        return rslt;
    }

    /**
     * Transforms an automaton 'self' reference expression and performs type checking on it.
     *
     * @param expr The CIF AST expression to transform.
     * @param hint The expected type of the expression. Ignored if it doesn't fit. May be {@code null} if not available.
     * @param scope The scope to resolve references in.
     * @param context Expression context. May be {@code null} to indicate empty context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel expression.
     */
    private static Expression transSelfExpression(ASelfExpression expr, CifType hint, SymbolScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Check scope.
        if (!(scope instanceof AutScope) && !(scope instanceof AutDefScope)) {
            tchecker.addProblem(ErrMsg.SELF_OCCURRENCE, expr.position, scope.getAbsText());
            throw new SemanticException();
        }

        // Construct metamodel representation.
        SelfExpression rslt = newSelfExpression();
        rslt.setPosition(expr.position);
        if (scope instanceof AutScope) {
            Automaton aut = ((AutScope)scope).getObject();
            ComponentType type = newComponentType();
            type.setComponent(aut);
            rslt.setType(type);
        } else {
            Assert.check(scope instanceof AutDefScope);
            ComponentDef cdef = ((AutDefScope)scope).getObject();
            ComponentDefType type = newComponentDefType();
            type.setDefinition(cdef);
            rslt.setType(type);
        }
        return rslt;
    }

    /**
     * Normalizes a type hint. Can handle {@code null} hints.
     *
     * @param hint The expected type of an expression. May be {@code null} if not available.
     * @return The normalized type, or {@code null}.
     * @see CifTypeUtils#normalizeType
     */
    private static CifType normalizeHint(CifType hint) {
        return (hint == null) ? null : normalizeType(hint);
    }

    /**
     * Returns the minimum of some values.
     *
     * @param values The values for which to return the minimum.
     * @return The minimum of the values.
     */
    public static long min(long... values) {
        Assert.check(values.length > 0);
        long rslt = Integer.MAX_VALUE;
        for (long value: values) {
            rslt = (rslt <= value) ? rslt : value;
        }
        return rslt;
    }

    /**
     * Returns the maximum of some values.
     *
     * @param values The values for which to return the maximum.
     * @return The maximum of the values.
     */
    public static long max(long... values) {
        Assert.check(values.length > 0);
        long rslt = Integer.MIN_VALUE;
        for (long value: values) {
            rslt = (rslt >= value) ? rslt : value;
        }
        return rslt;
    }
}
