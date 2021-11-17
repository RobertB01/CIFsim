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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictPair;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteralExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSetExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
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
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
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
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Maps;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Strings;

/**
 * CIF expression evaluation utility methods.
 *
 * <p>
 * The following table lists per CIF type, the corresponding Java type, and the corresponding CIF expression class:
 * <table border="0">
 * <tr>
 * <th align="left" style="padding-right:50px;">CIF type</th>
 * <th align="left" style="padding-right:50px;">Java type</th>
 * <th align="left" style="padding-right:50px;">CIF class</th>
 * </tr>
 * <tr>
 * <td>{@link BoolType}</td>
 * <td>{@link Boolean}</td>
 * <td>{@link BoolExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link IntType}</td>
 * <td>{@link Integer}</td>
 * <td>{@link IntExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link EnumType}</td>
 * <td>{@link CifEnumLiteral}</td>
 * <td>{@link EnumLiteralExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link ListType}</td>
 * <td>{@link List}</td>
 * <td>{@link ListExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link StringType}</td>
 * <td>{@link String}</td>
 * <td>{@link StringExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link RealType}</td>
 * <td>{@link Double}</td>
 * <td>{@link RealExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link SetType}</td>
 * <td>{@link Set}</td>
 * <td>{@link SetExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link FuncType}</td>
 * <td>{@link Function}</td>
 * <td>{@link Function}</td>
 * </tr>
 * <tr>
 * <td>{@link DictType}</td>
 * <td>{@link Map}</td>
 * <td>{@link DictExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link TupleType}</td>
 * <td>{@link CifTuple}</td>
 * <td>{@link TupleExpression}</td>
 * </tr>
 * <tr>
 * <td>{@link DistType}</td>
 * <td>n/a</td>
 * <td>n/a</td>
 * </tr>
 * <tr>
 * <td>{@link ComponentType}</td>
 * <td>n/a</td>
 * <td>n/a</td>
 * </tr>
 * <tr>
 * <td>{@link ComponentDefType}</td>
 * <td>n/a</td>
 * <td>n/a</td>
 * </tr>
 * </table>
 * </p>
 *
 * <p>
 * Distributions can not be statically evaluated. Components and component definitions are not supposed to be used as
 * values. Standard library functions are not used as values, while user-defined functions are. User-defined functions
 * however, can not be statically evaluated, as they may never terminate.
 * </p>
 *
 * <p>
 * We use the {@link Set} class for sets. This does <em>not</em> give us problems for values {@code 1} and {@code 1.0},
 * as even though they are considered unequal, they are not of the same type and are thus never compared.
 * </p>
 */
public class CifEvalUtils {
    /** Constructor for the {@link CifEvalUtils} class. */
    private CifEvalUtils() {
        // Static class.
    }

    /**
     * Evaluates an expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return A fully evaluated CIF expression.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    public static Expression evalAsExpr(Expression expr, boolean initial) throws CifEvalException {
        Object rslt = eval(expr, initial);
        return valueToExpr(rslt, expr.getType());
    }

    /**
     * Converts a Java value, resulting from the {@link #eval} method, back to a CIF expression.
     *
     * @param value A Java value, resulting from the {@link #eval} method.
     * @param type The type of the original expression that was evaluated using the {@link #eval} method.
     * @return A CIF expression.
     */
    @SuppressWarnings("unchecked")
    public static Expression valueToExpr(Object value, CifType type) {
        // Normalize the type.
        CifType ntype = CifTypeUtils.normalizeType(type);

        // Convert based on the (normalized) type.
        if (ntype instanceof BoolType) {
            BoolExpression bexpr = newBoolExpression();
            bexpr.setValue((Boolean)value);
            bexpr.setType(newBoolType());
            return bexpr;
        } else if (ntype instanceof IntType) {
            // No problem to represent negative numbers by an 'IntExpression'.
            int i = (Integer)value;

            IntType itype = newIntType();
            itype.setLower(i);
            itype.setUpper(i);

            IntExpression iexpr = newIntExpression();
            iexpr.setValue(i);
            iexpr.setType(itype);

            return iexpr;
        } else if (ntype instanceof EnumType) {
            CifEnumLiteral lit = (CifEnumLiteral)value;
            EnumLiteralExpression rexpr = newEnumLiteralExpression();
            rexpr.setLiteral(lit.literal);
            rexpr.setType(deepclone(type));
            return rexpr;
        } else if (ntype instanceof ListType) {
            ListType ltype = (ListType)ntype;
            List<Object> lst = (List<Object>)value;

            List<Expression> elements = listc(lst.size());
            for (Object elem: lst) {
                elements.add(valueToExpr(elem, ltype.getElementType()));
            }

            ListExpression lexpr = newListExpression();
            lexpr.getElements().addAll(elements);
            lexpr.setType(deepclone(type));

            return lexpr;
        } else if (ntype instanceof StringType) {
            StringExpression sexpr = newStringExpression();
            sexpr.setValue((String)value);
            sexpr.setType(newStringType());
            return sexpr;
        } else if (ntype instanceof RealType) {
            // Special handling of negative values, which need to become
            // unary negations with real numbers.
            double dvalue = (Double)value;
            boolean negate = false;
            if (dvalue < 0) {
                dvalue = -dvalue;
                negate = true;
            }
            if (dvalue == -0.0) {
                dvalue = 0.0;
            }

            RealExpression rexpr = newRealExpression();
            rexpr.setValue(CifMath.realToStr(dvalue));
            rexpr.setType(newRealType());

            if (negate) {
                UnaryExpression uexpr = newUnaryExpression();
                uexpr.setOperator(UnaryOperator.NEGATE);
                uexpr.setChild(rexpr);
                uexpr.setType(newRealType());
                return uexpr;
            } else {
                return rexpr;
            }
        } else if (ntype instanceof SetType) {
            SetType stype = (SetType)ntype;
            Set<Object> set = (Set<Object>)value;

            List<Expression> elements = listc(set.size());
            for (Object elem: set) {
                elements.add(valueToExpr(elem, stype.getElementType()));
            }

            SetExpression sexpr = newSetExpression();
            sexpr.getElements().addAll(elements);
            sexpr.setType(deepclone(type));

            return sexpr;
        } else if (ntype instanceof FuncType) {
            // Standard library functions can not be used as values, so we
            // should have a user-defined function here.
            Function func = (Function)value;
            FunctionExpression fexpr = newFunctionExpression();
            fexpr.setFunction(func);
            fexpr.setType(deepclone(type));
            return fexpr;
        } else if (ntype instanceof DictType) {
            DictType dtype = (DictType)ntype;
            Map<Object, Object> map = (Map<Object, Object>)value;

            List<DictPair> pairs = listc(map.size());
            for (Entry<Object, Object> elem: map.entrySet()) {
                DictPair pair = newDictPair();
                pair.setKey(valueToExpr(elem.getKey(), dtype.getKeyType()));
                pair.setValue(valueToExpr(elem.getValue(), dtype.getValueType()));
                pairs.add(pair);
            }

            DictExpression dexpr = newDictExpression();
            dexpr.getPairs().addAll(pairs);
            dexpr.setType(deepclone(type));

            return dexpr;
        } else if (ntype instanceof TupleType) {
            TupleType ttype = (TupleType)ntype;
            CifTuple tpl = (CifTuple)value;

            List<Expression> elements = listc(tpl.size());
            for (int i = 0; i < tpl.size(); i++) {
                Object elem = tpl.get(i);
                Field field = ttype.getFields().get(i);
                elements.add(valueToExpr(elem, field.getType()));
            }

            TupleExpression texpr = newTupleExpression();
            texpr.getFields().addAll(elements);
            texpr.setType(deepclone(type));

            return texpr;
        } else if (ntype instanceof DistType) {
            // Distributions can not be created statically.
            throw new RuntimeException("Value of type dist unexpected.");
        } else {
            throw new RuntimeException("Unknown/unexpected type: " + ntype);
        }
    }

    /**
     * Evaluates the given predicates to a boolean value, if the predicates statically have a single value. The
     * predicates are assumed to have conjunction semantics.
     *
     * @param preds The predicates to evaluate.
     * @param initial Whether the check and evaluation should only take into account the initial state ({@code true}) or
     *     any state ({@code false}, includes the initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @param checkRefs Whether references should be followed and checked recursively. If {@code false}, references to
     *     constants, variables (discrete, algebraic, and continuous), and functions are considered not to have a single
     *     value. If {@code true}, the references may or may not have a single value, depending on the declarations to
     *     which they refer. See also {@link CifValueUtils#hasSingleValue}.
     * @return {@code true} if the sequence of predicates is trivially true (including empty sequence), {@code false} if
     *     the sequence of predicates is trivially false.
     * @throws UnsupportedException If a predicates does not statically have a single value, and can thus not be
     *     evaluated statically.
     * @throws CifEvalException If static evaluation fails.
     */
    public static boolean evalPreds(List<Expression> preds, boolean initial, boolean checkRefs)
            throws CifEvalException
    {
        for (Expression pred: preds) {
            boolean value = evalPred(pred, initial, checkRefs);
            if (!value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Evaluates the given predicate to a boolean value, if the predicate statically has a single value.
     *
     * @param pred The predicate to evaluate.
     * @param initial Whether the check and evaluation should only take into account the initial state ({@code true}) or
     *     any state ({@code false}, includes the initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @param checkRefs Whether references should be followed and checked recursively. If {@code false}, references to
     *     constants, variables (discrete, algebraic, and continuous), and functions are considered not to have a single
     *     value. If {@code true}, the references may or may not have a single value, depending on the declarations to
     *     which they refer. See also {@link CifValueUtils#hasSingleValue}.
     * @return {@code true} if the predicate is trivially true, {@code false} if it is trivially false.
     * @throws UnsupportedException If the predicate does not statically have a single value, and can thus not be
     *     evaluated statically.
     * @throws CifEvalException If static evaluation fails.
     */
    public static boolean evalPred(Expression pred, boolean initial, boolean checkRefs) throws CifEvalException {
        // Check for single value, to allow static evaluation.
        if (!CifValueUtils.hasSingleValue(pred, initial, checkRefs)) {
            String msg = fmt("Predicate \"%s\" can not statically be evaluated to a single value.", exprToStr(pred));
            throw new UnsupportedException(msg);
        }

        // Evaluate statically to a boolean value.
        Object rslt = CifEvalUtils.eval(pred, initial);
        return (Boolean)rslt;
    }

    /**
     * Evaluates an expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    public static Object eval(Expression expr, boolean initial) throws CifEvalException {
        if (expr instanceof IntExpression) {
            return ((IntExpression)expr).getValue();
        } else if (expr instanceof BoolExpression) {
            return ((BoolExpression)expr).isValue();
        } else if (expr instanceof RealExpression) {
            RealExpression rexpr = (RealExpression)expr;
            double rslt;
            try {
                rslt = CifMath.strToReal(rexpr.getValue(), null);
            } catch (CifEvalException e) {
                // Should not happen, as type checker should have checked it.
                throw new RuntimeException(e);
            }
            return rslt;
        } else if (expr instanceof StringExpression) {
            return ((StringExpression)expr).getValue();
        } else if (expr instanceof TimeExpression) {
            // In initial state, is fixed at 0.0.
            if (initial) {
                return 0.0;
            }

            // In any state, we don't know the value (statically).
            String msg = "Cannot run-time eval time ref in non-initial state: " + expr;
            throw new RuntimeException(msg);
        } else if (expr instanceof CastExpression) {
            return evalCastExpr((CastExpression)expr, initial);
        } else if (expr instanceof UnaryExpression) {
            return evalUnaryExpr((UnaryExpression)expr, initial);
        } else if (expr instanceof BinaryExpression) {
            return evalBinaryExpr((BinaryExpression)expr, initial);
        } else if (expr instanceof IfExpression) {
            return evalIfExpr((IfExpression)expr, initial);
        } else if (expr instanceof SwitchExpression) {
            return evalSwitchExpr((SwitchExpression)expr, initial);
        } else if (expr instanceof ProjectionExpression) {
            return evalProjectionExpr((ProjectionExpression)expr, initial);
        } else if (expr instanceof SliceExpression) {
            return evalSliceExpr((SliceExpression)expr, initial);
        } else if (expr instanceof FunctionCallExpression) {
            return evalFuncCallExpr((FunctionCallExpression)expr, initial);
        } else if (expr instanceof ListExpression) {
            return evalListExpr((ListExpression)expr, initial);
        } else if (expr instanceof SetExpression) {
            return evalSetExpr((SetExpression)expr, initial);
        } else if (expr instanceof TupleExpression) {
            return evalTupleExpr((TupleExpression)expr, initial);
        } else if (expr instanceof DictExpression) {
            return evalDictExpr((DictExpression)expr, initial);
        } else if (expr instanceof ConstantExpression) {
            return eval(((ConstantExpression)expr).getConstant().getValue(), initial);
        } else if (expr instanceof DiscVariableExpression) {
            // This is either a discrete variable of an automaton, or a
            // parameter or local variable of a function.
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();

            // Discrete variables are part of the run-time state, local
            // variables of functions are part of the function evaluation
            // state, and function parameters can have different values given
            // multiple function calls. If however the type of the variable
            // allows only a single value, then we know for sure.
            CifType type = var.getType();
            if (CifValueUtils.hasSingleValue(type)) {
                return eval(type);
            }

            // Should not be a parameter.
            Assert.check(!(var.eContainer() instanceof FunctionParameter));

            // In the initial state, we can use the initial values.
            Assert.check(initial);

            // If default value, then get it from the type.
            if (var.getValue() == null) {
                List<InternalFunction> funcs = list();
                Expression value = CifValueUtils.getDefaultValue(type, funcs);
                Assert.check(funcs.isEmpty());
                return eval(value, initial);
            }

            // If exactly one possible initial value, use it.
            if (var.getValue().getValues().size() == 1) {
                Expression value = first(var.getValue().getValues());
                return eval(value, initial);
            }

            String msg = "Cannot run-time eval disc var ref: " + var;
            throw new RuntimeException(msg);
        } else if (expr instanceof AlgVariableExpression) {
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();

            // Try value first.
            Expression value = var.getValue();
            if (value != null) {
                return eval(value, initial);
            }

            // Algebraic variable parameter or value specified in equation.
            if (var.eContainer() instanceof Parameter) {
                return eval(var.getType());
            } else {
                // Find equation.
                ComplexComponent comp = (ComplexComponent)var.eContainer();
                for (Equation eq: comp.getEquations()) {
                    if (eq.getVariable() == var) {
                        return eval(eq.getValue(), initial);
                    }
                }

                // No value and no equation in the component. It can't be
                // equations in locations, as we are not allowed to evaluate
                // them here (since locations can change value).
                throw new RuntimeException("No equation found: " + var);
            }
        } else if (expr instanceof ContVariableExpression) {
            // Get variable and check whether this is a derivative reference.
            ContVariableExpression cexpr = (ContVariableExpression)expr;
            ContVariable var = cexpr.getVariable();
            boolean isDer = cexpr.isDerivative();

            // Result depends on whether or not this is a derivative reference
            // (similar to algebraic variable) or a continuous variable
            // reference (similar to discrete variable).
            if (isDer) {
                // Try derivative with declaration first.
                Expression der = var.getDerivative();
                if (der != null) {
                    return eval(der, initial);
                }

                // Derivative specified in one or more equations. Find
                // equation in component.
                ComplexComponent comp = (ComplexComponent)var.eContainer();
                for (Equation eq: comp.getEquations()) {
                    if (eq.getVariable() == var) {
                        return eval(eq.getValue(), initial);
                    }
                }

                // No derivative and no equation in the component. It can't be
                // equations in locations, as we are not allowed to evaluate
                // them here (since locations can change value).
                throw new RuntimeException("No equation found: " + var);
            } else {
                // In the initial state, we can use the initial value.
                Assert.check(initial);

                // If default value, then 0.0.
                if (var.getValue() == null) {
                    return 0.0;
                }

                // Use the specified initial value.
                return eval(var.getValue(), initial);
            }
        } else if (expr instanceof TauExpression) {
            String msg = "Cannot run-time eval tau ref: " + expr;
            throw new RuntimeException(msg);
        } else if (expr instanceof LocationExpression) {
            // Locations are part of the run-time state. If however the
            // location is the only location in its automaton, then the
            // expression always evaluates to true. If it is a parameter,
            // then we can never be sure. We don't treat single possible
            // initial locations special here, as we're not yet sure
            // that won't break any existing CIF code.
            Location loc = ((LocationExpression)expr).getLocation();
            EObject parent = loc.eContainer();
            if (parent instanceof Automaton) {
                Automaton aut = (Automaton)parent;
                if (aut.getLocations().size() == 1) {
                    return true;
                }
            }
            String msg = "Cannot run-time eval location ref: " + expr;
            throw new RuntimeException(msg);
        } else if (expr instanceof EnumLiteralExpression) {
            // Return referred enumeration literal, wrapped for value equality.
            EnumLiteral lit = ((EnumLiteralExpression)expr).getLiteral();
            return new CifEnumLiteral(lit);
        } else if (expr instanceof EventExpression) {
            String msg = "Cannot run-time eval event ref: " + expr;
            throw new RuntimeException(msg);
        } else if (expr instanceof FieldExpression) {
            String msg = "Field expr should be handled by projection expr.";
            throw new RuntimeException(msg);
        } else if (expr instanceof StdLibFunctionExpression) {
            String msg = "Cannot use stdlib func as value: " + expr;
            throw new RuntimeException(msg);
        } else if (expr instanceof FunctionExpression) {
            // Return the user-defined function.
            return ((FunctionExpression)expr).getFunction();
        } else if (expr instanceof InputVariableExpression) {
            // Value is unknown, so it must be the type that we can evaluate.
            InputVariable var = ((InputVariableExpression)expr).getVariable();
            return eval(var.getType());
        } else if (expr instanceof ComponentExpression) {
            // A reference to a component always refers to the same component
            // and could evaluate to itself, but we don't support components
            // as values. However, special cases such as casting to string may
            // be handled by other expressions.
            String msg = "Cannot use component as value: " + expr;
            throw new RuntimeException(msg);
        } else if (expr instanceof CompParamExpression) {
            String msg = "Cannot use component parameter as value: " + expr;
            throw new RuntimeException(msg);
        } else if (expr instanceof CompInstWrapExpression) {
            // Peel of the wrapper. Statically, for values, we don't care
            // about the wrapping, as it doesn't matter how you get to a
            // value.
            return eval(((CompInstWrapExpression)expr).getReference(), initial);
        } else if (expr instanceof CompParamWrapExpression) {
            // Peel of the wrapper. Statically, for values, we don't care
            // about the wrapping, as it doesn't matter how you get to a
            // value.
            return eval(((CompParamWrapExpression)expr).getReference(), initial);
        } else if (expr instanceof ReceivedExpression) {
            // Value is unknown, so it must be the type that we can evaluate.
            return eval(expr.getType());
        } else if (expr instanceof SelfExpression) {
            // Automaton 'self' reference would result in an automaton as
            // value, which is a component, which we don't support as value.
            // However, special cases such as casting to string may be handled
            // by other expressions.
            String msg = "Cannot use \"self\" as value: " + expr;
            throw new RuntimeException(msg);
        } else {
            throw new RuntimeException("Unknown expr: " + expr);
        }
    }

    /**
     * Evaluates a cast expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The cast expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    private static Object evalCastExpr(CastExpression expr, boolean initial) throws CifEvalException {
        // Handle cast from automaton reference to string as special case.
        Expression child = expr.getChild();
        if (CifTypeUtils.isAutRefExpr(child)) {
            // Get automaton.
            CifType ctype = child.getType();
            CifType nctype = CifTypeUtils.normalizeType(ctype);

            Automaton aut;
            if (nctype instanceof ComponentType) {
                Component comp = ((ComponentType)nctype).getComponent();
                aut = CifScopeUtils.getAutomaton(comp);
            } else {
                Assert.check(nctype instanceof ComponentDefType);
                ComponentDef cdef = ((ComponentDefType)nctype).getDefinition();
                aut = CifScopeUtils.getAutomaton(cdef.getBody());
            }

            // Get the single location. We don't treat 'initial' special here,
            // to ensure compatibility with CifValueUtils.hasSingleValue.
            Assert.check(aut.getLocations().size() == 1);
            Location loc = first(aut.getLocations());

            // Return name of the only location of the automaton.
            return CifLocationUtils.getName(loc);
        }

        // Normal case: evaluate child.
        Object crslt = eval(expr.getChild(), initial);
        CifType ctype = expr.getChild().getType();
        CifType nctype = CifTypeUtils.normalizeType(ctype);

        // Convert based on child/result type combination.
        CifType ntype = CifTypeUtils.normalizeType(expr.getType());
        if (nctype instanceof IntType && ntype instanceof RealType) {
            int value = (Integer)crslt;
            return CifMath.intToReal(value);
        } else if (nctype instanceof IntType && ntype instanceof StringType) {
            int value = (Integer)crslt;
            return CifMath.intToStr(value);
        } else if (nctype instanceof RealType && ntype instanceof StringType) {
            double value = (Double)crslt;
            return CifMath.realToStr(value);
        } else if (nctype instanceof BoolType && ntype instanceof StringType) {
            boolean value = (Boolean)crslt;
            return CifMath.boolToStr(value);
        } else if (nctype instanceof StringType && ntype instanceof IntType) {
            String value = (String)crslt;
            return CifMath.strToInt(value, expr);
        } else if (nctype instanceof StringType && ntype instanceof RealType) {
            String value = (String)crslt;
            return CifMath.strToReal(value, expr);
        } else if (nctype instanceof StringType && ntype instanceof BoolType) {
            String value = (String)crslt;
            return CifMath.strToBool(value, expr);
        } else if (CifTypeUtils.checkTypeCompat(nctype, ntype, RangeCompat.EQUAL)) {
            // Ignore casts to child type.
            return crslt;
        } else {
            String msg = "Unknown cast: " + nctype + ", " + ntype;
            throw new RuntimeException(msg);
        }
    }

    /**
     * Evaluates a unary expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The unary expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    private static Object evalUnaryExpr(UnaryExpression expr, boolean initial) throws CifEvalException {
        Object crslt = eval(expr.getChild(), initial);

        switch (expr.getOperator()) {
            case INVERSE:
                return !(Boolean)crslt;

            case NEGATE:
                if (crslt instanceof Integer) {
                    return CifMath.negate((Integer)crslt, expr);
                } else {
                    return CifMath.negate((Double)crslt);
                }

            case PLUS:
                // Discard the '+'. No overflow etc possible.
                return crslt;

            case SAMPLE: {
                String msg = "Cannot run-time eval sample unop: " + expr;
                throw new RuntimeException(msg);
            }

            default: {
                String msg = "Unknown unary op: " + expr.getOperator();
                throw new RuntimeException(msg);
            }
        }
    }

    /**
     * Evaluates a binary expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The binary expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    @SuppressWarnings("unchecked")
    private static Object evalBinaryExpr(BinaryExpression expr, boolean initial) throws CifEvalException {
        // First, handle cases that require short circuit evaluation.
        Object l = eval(expr.getLeft(), initial);

        switch (expr.getOperator()) {
            case IMPLICATION: {
                // Short circuit evaluation.
                if (!(Boolean)l) {
                    return true;
                }
                Object r = eval(expr.getRight(), initial);
                return r;
            }

            case DISJUNCTION: {
                // Boolean 'or'.
                if (l instanceof Boolean) {
                    // Short circuit evaluation.
                    if ((Boolean)l) {
                        return true;
                    }
                    Object r = eval(expr.getRight(), initial);
                    return r;
                }
                break;
            }

            case CONJUNCTION: {
                // Boolean 'and'.
                if (l instanceof Boolean) {
                    // Short circuit evaluation.
                    if (!(Boolean)l) {
                        return false;
                    }
                    Object r = eval(expr.getRight(), initial);
                    return r;
                }
                break;
            }

            default:
                // Just fall through to the remaining cases below.
        }

        // Second, handle remaining cases.
        Object r = eval(expr.getRight(), initial);

        switch (expr.getOperator()) {
            case BI_CONDITIONAL:
                return ((Boolean)l).equals(r);

            case DISJUNCTION: {
                // Set union.
                Set<Object> lset = (Set<Object>)l;
                Set<Object> rset = (Set<Object>)r;
                Set<Object> rslt = Sets.copy(lset);
                rslt.addAll(rset);
                return rslt;
            }

            case CONJUNCTION: {
                // Set intersection.
                Set<Object> lset = (Set<Object>)l;
                Set<Object> rset = (Set<Object>)r;
                Set<Object> rslt = Sets.copy(lset);
                rslt.retainAll(rset);
                return rslt;
            }

            case LESS_THAN: {
                // Note that int to double conversion is lossless.
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return ld < rd;
            }

            case LESS_EQUAL: {
                // Note that int to double conversion is lossless.
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return ld <= rd;
            }

            case GREATER_THAN: {
                // Note that int to double conversion is lossless.
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return ld > rd;
            }

            case GREATER_EQUAL: {
                // Note that int to double conversion is lossless.
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return ld >= rd;
            }

            case EQUAL:
                return l.equals(r);

            case UNEQUAL:
                return !l.equals(r);

            case ADDITION: {
                if (l instanceof Integer && r instanceof Integer) {
                    return CifMath.add((Integer)l, (Integer)r, expr);
                } else if (l instanceof List && r instanceof List) {
                    List<Object> llst = (List<Object>)l;
                    List<Object> rlst = (List<Object>)r;
                    List<Object> rslt;
                    rslt = listc(llst.size() + rlst.size());
                    rslt.addAll(llst);
                    rslt.addAll(rlst);
                    return rslt;
                } else if (l instanceof String && r instanceof String) {
                    return (String)l + (String)r;
                } else if (l instanceof Map && r instanceof Map) {
                    Map<Object, Object> lmap = (Map<Object, Object>)l;
                    Map<Object, Object> rmap = (Map<Object, Object>)r;
                    Map<Object, Object> rslt;
                    rslt = Maps.copy(lmap);
                    rslt.putAll(rmap);
                    return rslt;
                } else {
                    double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                    double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                    return CifMath.add(ld, rd, expr);
                }
            }

            case SUBTRACTION:
                if (l instanceof Integer && r instanceof Integer) {
                    return CifMath.subtract((Integer)l, (Integer)r, expr);
                } else if (l instanceof Set && r instanceof Set) {
                    Set<Object> lset = (Set<Object>)l;
                    Set<Object> rset = (Set<Object>)r;
                    Set<Object> rslt = Sets.copy(lset);
                    rslt.removeAll(rset);
                    return rslt;
                } else if (l instanceof Map && r instanceof Map) {
                    Map<Object, Object> lmap = (Map<Object, Object>)l;
                    Map<Object, Object> rmap = (Map<Object, Object>)r;
                    Map<Object, Object> rslt;
                    rslt = Maps.copy(lmap);
                    for (Object key: rmap.keySet()) {
                        rslt.remove(key);
                    }
                    return rslt;
                } else if (l instanceof Map && r instanceof List) {
                    Map<Object, Object> lmap = (Map<Object, Object>)l;
                    List<Object> rlst = (List<Object>)r;
                    Map<Object, Object> rslt;
                    rslt = Maps.copy(lmap);
                    for (Object key: rlst) {
                        rslt.remove(key);
                    }
                    return rslt;
                } else if (l instanceof Map && r instanceof Set) {
                    Map<Object, Object> lmap = (Map<Object, Object>)l;
                    Set<Object> rset = (Set<Object>)r;
                    Map<Object, Object> rslt;
                    rslt = Maps.copy(lmap);
                    for (Object key: rset) {
                        rslt.remove(key);
                    }
                    return rslt;
                } else {
                    double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                    double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                    return CifMath.subtract(ld, rd, expr);
                }

            case MULTIPLICATION:
                if (l instanceof Integer && r instanceof Integer) {
                    return CifMath.multiply((Integer)l, (Integer)r, expr);
                } else {
                    double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                    double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                    return CifMath.multiply(ld, rd, expr);
                }

            case DIVISION: {
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return CifMath.divide(ld, rd, expr);
            }

            case INTEGER_DIVISION:
                return CifMath.div((Integer)l, (Integer)r, expr);

            case MODULUS:
                return CifMath.mod((Integer)l, (Integer)r, expr);

            case SUBSET: {
                Set<Object> lset = (Set<Object>)l;
                Set<Object> rset = (Set<Object>)r;
                return rset.containsAll(lset);
            }

            case ELEMENT_OF: {
                if (r instanceof List) {
                    List<Object> list = (List<Object>)r;
                    return list.contains(l);
                } else if (r instanceof Set) {
                    Set<Object> set = (Set<Object>)r;
                    return set.contains(l);
                } else {
                    Map<Object, Object> dict = (Map<Object, Object>)r;
                    return dict.containsKey(l);
                }
            }

            default:
                String msg = "Unknown binary op: " + expr.getOperator();
                throw new RuntimeException(msg);
        }
    }

    /**
     * Evaluates an 'if' expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The 'if' expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    private static Object evalIfExpr(IfExpression expr, boolean initial) throws CifEvalException {
        // Evaluate guards.
        List<Expression> guards = expr.getGuards();
        boolean guardsTrue = true;
        for (Expression guard: guards) {
            boolean rslt = (Boolean)eval(guard, initial);
            if (!rslt) {
                guardsTrue = false;
                break;
            }
        }

        // All guards true, then return the evaluated 'then' expression.
        if (guardsTrue) {
            return eval(expr.getThen(), initial);
        }

        // Process elifs.
        for (ElifExpression elif: expr.getElifs()) {
            // Evaluate guards.
            guards = elif.getGuards();
            guardsTrue = true;
            for (Expression guard: guards) {
                boolean rslt = (Boolean)eval(guard, initial);
                if (!rslt) {
                    guardsTrue = false;
                    break;
                }
            }

            // All guards true, then return the evaluated 'then' expression.
            if (guardsTrue) {
                return eval(elif.getThen(), initial);
            }
        }

        // None of the guards is true, including for the elifs.
        return eval(expr.getElse(), initial);
    }

    /**
     * Evaluates a 'switch' expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The 'switch' expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    private static Object evalSwitchExpr(SwitchExpression expr, boolean initial) throws CifEvalException {
        // Process control value.
        Object value;
        if (CifTypeUtils.isAutRefExpr(expr.getValue())) {
            // Special case: automaton (self) reference, so no value.
            value = null;
        } else {
            // Normal case: just a value expression.
            value = eval(expr.getValue(), initial);
        }

        // Process cases.
        for (SwitchCase cse: expr.getCases()) {
            // Determine whether case matches.
            boolean match;
            if (cse.getKey() == null) {
                // 'else' always matches.
                match = true;
            } else if (value == null) {
                // If no control value, then we have exactly one location, and
                // any case that refers to that location automatically matches.
                match = true;
            } else {
                // Evaluate key expression and compare to control value.
                Object key = eval(cse.getKey(), initial);
                match = value.equals(key);
            }

            // Skip cases that don't match.
            if (!match) {
                continue;
            }

            // Case matches. Evaluate value.
            return eval(cse.getValue(), initial);
        }

        // Type checker should make sure that we always have a matching case.
        throw new RuntimeException("No switch case matches: " + expr);
    }

    /**
     * Evaluates a projection expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The projection expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    @SuppressWarnings("unchecked")
    private static Object evalProjectionExpr(ProjectionExpression expr, boolean initial) throws CifEvalException {
        // Evaluate child.
        Object crslt = eval(expr.getChild(), initial);

        // Special case for tuple field projection.
        if (crslt instanceof CifTuple && expr.getIndex() instanceof FieldExpression) {
            // Convert field reference to a 0-based index.
            Field field = ((FieldExpression)expr.getIndex()).getField();
            TupleType ttype = (TupleType)field.eContainer();
            int idx = ttype.getFields().indexOf(field);

            // Paranoia check on index.
            CifTuple tuple = (CifTuple)crslt;
            Assert.check(idx < tuple.size());

            // Perform actual projection on the tuple, using the index.
            return CifMath.project(tuple, idx, null);
        }

        // Evaluate index.
        Object irslt = eval(expr.getIndex(), initial);

        // Case distinction on child.
        if (crslt instanceof List) {
            return CifMath.project((List<Object>)crslt, (Integer)irslt, expr);
        } else if (crslt instanceof Map) {
            return CifMath.project((Map<Object, Object>)crslt, irslt, expr);
        } else if (crslt instanceof String) {
            return CifMath.project((String)crslt, (Integer)irslt, expr);
        } else if (crslt instanceof CifTuple) {
            return CifMath.project((CifTuple)crslt, (Integer)irslt, expr);
        } else {
            throw new RuntimeException("Unknown projection child.");
        }
    }

    /**
     * Evaluates a slice expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The slice expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    @SuppressWarnings("unchecked")
    private static Object evalSliceExpr(SliceExpression expr, boolean initial) throws CifEvalException {
        Object crslt = eval(expr.getChild(), initial);
        Integer begin = (expr.getBegin() == null) ? null : (Integer)eval(expr.getBegin(), initial);
        Integer end = (expr.getEnd() == null) ? null : (Integer)eval(expr.getEnd(), initial);

        if (crslt instanceof List) {
            return CifMath.slice((List<Object>)crslt, begin, end);
        } else {
            return CifMath.slice((String)crslt, begin, end);
        }
    }

    /**
     * Evaluates a function call expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The function call expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    @SuppressWarnings("unchecked")
    private static Object evalFuncCallExpr(FunctionCallExpression expr, boolean initial) throws CifEvalException {
        // Evaluate parameters.
        Object[] ps = new Object[expr.getParams().size()];
        for (int i = 0; i < expr.getParams().size(); i++) {
            ps[i] = eval(expr.getParams().get(i), initial);
        }

        // Paranoia check: can't statically evaluate user-defined
        // functions, as we don't know whether they will terminate.
        Assert.check(expr.getFunction() instanceof StdLibFunctionExpression);

        // Get standard library function.
        StdLibFunctionExpression stdlibExpr = (StdLibFunctionExpression)expr.getFunction();
        StdLibFunction func = stdlibExpr.getFunction();

        // Evaluate function.
        switch (func) {
            case ACOSH:
                return CifMath.acosh((Double)ps[0], expr);

            case ACOS:
                return CifMath.acos((Double)ps[0], expr);

            case ASINH:
                return CifMath.asinh((Double)ps[0], expr);

            case ASIN:
                return CifMath.asin((Double)ps[0], expr);

            case ATANH:
                return CifMath.atanh((Double)ps[0], expr);

            case ATAN:
                return CifMath.atan((Double)ps[0], expr);

            case COSH:
                return CifMath.cosh((Double)ps[0], expr);

            case COS:
                return CifMath.cos((Double)ps[0], expr);

            case SINH:
                return CifMath.sinh((Double)ps[0], expr);

            case SIN:
                return CifMath.sin((Double)ps[0], expr);

            case TANH:
                return CifMath.tanh((Double)ps[0], expr);

            case TAN:
                return CifMath.tan((Double)ps[0], expr);

            case ABS:
                if (ps[0] instanceof Integer) {
                    return CifMath.abs((Integer)ps[0], expr);
                } else {
                    return CifMath.abs((Double)ps[0]);
                }

            case CBRT:
                return CifMath.cbrt((Double)ps[0]);

            case CEIL:
                return CifMath.ceil((Double)ps[0], expr);

            case DELETE:
                return CifMath.delete((List<Object>)ps[0], (Integer)ps[1], expr);

            case EMPTY:
                if (ps[0] instanceof List) {
                    return ((List<Object>)ps[0]).isEmpty();
                } else if (ps[0] instanceof Set) {
                    return ((Set<Object>)ps[0]).isEmpty();
                } else {
                    return ((Map<Object, Object>)ps[0]).isEmpty();
                }

            case EXP:
                return CifMath.exp((Double)ps[0], expr);

            case FLOOR:
                return CifMath.floor((Double)ps[0], expr);

            case FORMAT: {
                Object[] args = new Object[ps.length - 1];
                System.arraycopy(ps, 1, args, 0, args.length);
                return CifMath.fmt((String)ps[0], args);
            }

            case LN:
                return CifMath.ln((Double)ps[0], expr);

            case LOG:
                return CifMath.log((Double)ps[0], expr);

            case MAXIMUM:
                if (ps[0] instanceof Integer && ps[1] instanceof Integer) {
                    return CifMath.max((Integer)ps[0], (Integer)ps[1]);
                } else {
                    double p0 = (ps[0] instanceof Integer) ? (Integer)ps[0] : (Double)ps[0];
                    double p1 = (ps[1] instanceof Integer) ? (Integer)ps[1] : (Double)ps[1];
                    return CifMath.max(p0, p1);
                }

            case MINIMUM:
                if (ps[0] instanceof Integer && ps[1] instanceof Integer) {
                    return CifMath.min((Integer)ps[0], (Integer)ps[1]);
                } else {
                    double p0 = (ps[0] instanceof Integer) ? (Integer)ps[0] : (Double)ps[0];
                    double p1 = (ps[1] instanceof Integer) ? (Integer)ps[1] : (Double)ps[1];
                    return CifMath.min(p0, p1);
                }

            case POP:
                return CifMath.pop((List<Object>)ps[0], expr);

            case POWER: {
                if (ps[0] instanceof Integer && ps[1] instanceof Integer) {
                    int ps1 = (Integer)ps[1];
                    if (ps1 >= 0) {
                        return CifMath.pow((Integer)ps[0], ps1, expr);
                    }
                }

                double p0 = (ps[0] instanceof Integer) ? (Integer)ps[0] : (Double)ps[0];
                double p1 = (ps[1] instanceof Integer) ? (Integer)ps[1] : (Double)ps[1];
                return CifMath.pow(p0, p1, expr);
            }

            case ROUND:
                return CifMath.round((Double)ps[0], expr);

            case SCALE: {
                double[] ds = new double[ps.length];
                for (int i = 0; i < ps.length; i++) {
                    ds[i] = (ps[i] instanceof Integer) ? (Integer)ps[i] : (Double)ps[i];
                }
                return CifMath.scale(ds[0], ds[1], ds[2], ds[3], ds[4], expr);
            }

            case SIGN:
                if (ps[0] instanceof Integer) {
                    return CifMath.sign((Integer)ps[0]);
                } else {
                    return CifMath.sign((Double)ps[0]);
                }

            case SIZE:
                if (ps[0] instanceof String) {
                    return ((String)ps[0]).length();
                } else if (ps[0] instanceof List) {
                    return ((List<Object>)ps[0]).size();
                } else if (ps[0] instanceof Set) {
                    return ((Set<Object>)ps[0]).size();
                } else {
                    return ((Map<Object, Object>)ps[0]).size();
                }

            case SQRT:
                return CifMath.sqrt((Double)ps[0], expr);

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
            case WEIBULL: {
                String msg = "Cannot run-time eval dist stdlib: " + func;
                throw new RuntimeException(msg);
            }
        }
        String msg = "Unknown stdlib func: " + expr.getFunction();
        throw new RuntimeException(msg);
    }

    /**
     * Evaluates a list expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The list expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    private static Object evalListExpr(ListExpression expr, boolean initial) throws CifEvalException {
        List<Object> list = listc(expr.getElements().size());
        for (Expression elem: expr.getElements()) {
            list.add(eval(elem, initial));
        }
        return list;
    }

    /**
     * Evaluates a set expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The set expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    private static Object evalSetExpr(SetExpression expr, boolean initial) throws CifEvalException {
        Set<Object> set = setc(expr.getElements().size());
        for (Expression elem: expr.getElements()) {
            set.add(eval(elem, initial));
        }
        return set;
    }

    /**
     * Evaluates a tuple expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The tuple expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @see CifValueUtils#hasSingleValue
     */
    private static Object evalTupleExpr(TupleExpression expr, boolean initial) throws CifEvalException {
        CifTuple tuple = new CifTuple(expr.getFields().size());
        for (Expression elem: expr.getFields()) {
            tuple.add(eval(elem, initial));
        }
        return tuple;
    }

    /**
     * Evaluates a dictionary expression. This method may only be used for statically evaluable expressions.
     *
     * @param expr The dictionary expression to evaluate. Must be fully typed.
     * @param initial Whether the evaluate in the initial state ({@code true}) or any state ({@code false}, includes the
     *     initial state). See also {@link CifValueUtils#hasSingleValue}.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @throws CifEvalException In case of expression evaluation failure.
     * @throws CifEvalException If a dictionary has duplicate keys.
     * @see CifValueUtils#hasSingleValue
     */
    private static Object evalDictExpr(DictExpression expr, boolean initial) throws CifEvalException {
        // Construct a 'Map'.
        Map<Object, Object> dict;
        dict = mapc(expr.getPairs().size());
        for (DictPair pair: expr.getPairs()) {
            dict.put(eval(pair.getKey(), initial), eval(pair.getValue(), initial));
        }

        // Check for duplicate keys.
        int expectedCount = expr.getPairs().size();
        int actualCount = dict.size();
        if (expectedCount != actualCount) {
            int duplicates = expectedCount - actualCount;
            Assert.check(duplicates > 0);
            String msg = fmt("Dictionary has %d duplicate key%s.", duplicates, (duplicates == 1) ? "" : "s");
            throw new CifEvalException(msg, expr);
        }

        // Return the 'Map'.
        return dict;
    }

    /**
     * Evaluates a type to its only possible value. This method may only be used for types that can have only a single
     * value.
     *
     * @param type The type to evaluate. Must be fully typed.
     * @return The evaluation result. For the possible types, see the {@link CifEvalUtils} class.
     * @see CifValueUtils#hasSingleValue(CifType)
     */
    public static Object eval(CifType type) {
        // Primitive types.
        if (type instanceof BoolType) {
            String msg = "Cannot run-time eval bool type: " + type;
            throw new RuntimeException(msg);
        }

        if (type instanceof IntType) {
            // Only has a single value if it has a single value range.
            IntType itype = (IntType)type;
            Assert.check(itype.getLower().equals(itype.getUpper()));
            return itype.getLower();
        }

        if (type instanceof RealType) {
            String msg = "Cannot run-time eval real type: " + type;
            throw new RuntimeException(msg);
        }

        if (type instanceof StringType) {
            String msg = "Cannot run-time eval string type: " + type;
            throw new RuntimeException(msg);
        }

        if (type instanceof VoidType) {
            String msg = "Cannot run-time eval void type: " + type;
            throw new RuntimeException(msg);
        }

        // Wrappings.
        if (type instanceof CompInstWrapType) {
            // Peel of wrapper. Statically, for values, we don't care
            // about the wrapping, as it doesn't matter how you get to a
            // value.
            CifType rtype = ((CompInstWrapType)type).getReference();
            return eval(rtype);
        }

        if (type instanceof CompParamWrapType) {
            // Peel of wrapper. Statically, for values, we don't care
            // about the wrapping, as it doesn't matter how you get to a
            // value.
            CifType rtype = ((CompParamWrapType)type).getReference();
            return eval(rtype);
        }

        // References.
        if (type instanceof ComponentDefType) {
            // Since there may be many instantiations, there may be many
            // values.
            String msg = "Cannot run-time eval compdef type: " + type;
            throw new RuntimeException(msg);
        }

        if (type instanceof ComponentType) {
            // There is only one component that matches this type, but we
            // don't use components as values.
            String msg = "Cannot run-time eval component type: " + type;
            throw new RuntimeException(msg);
        }

        if (type instanceof EnumType) {
            // Only has a single value if it has a single literal.
            EnumDecl edecl = ((EnumType)type).getEnum();
            List<EnumLiteral> literals = edecl.getLiterals();
            Assert.check(literals.size() == 1);
            EnumLiteral lit = literals.get(0);
            return new CifEnumLiteral(lit);
        }

        if (type instanceof TypeRef) {
            // Peel of the type reference.
            CifType rtype = ((TypeRef)type).getType().getType();
            return eval(rtype);
        }

        // Container types.
        if (type instanceof ListType) {
            // Only has a single value if it is an array, and the element type
            // has a single value.
            ListType ltype = (ListType)type;
            Assert.check(ltype.getLower().equals(ltype.getUpper()));
            List<Object> rslt = listc(ltype.getLower());
            for (int i = 0; i < ltype.getLower(); i++) {
                rslt.add(eval(ltype.getElementType()));
            }
            return rslt;
        }

        if (type instanceof SetType) {
            String msg = "Cannot run-time eval set type: " + type;
            throw new RuntimeException(msg);
        }

        if (type instanceof DictType) {
            String msg = "Cannot run-time eval dict type: " + type;
            throw new RuntimeException(msg);
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            CifTuple tuple = new CifTuple(ttype.getFields().size());
            for (Field field: ttype.getFields()) {
                tuple.add(eval(field.getType()));
            }
            return tuple;
        }

        // Other types.
        if (type instanceof DistType) {
            String msg = "Cannot run-time eval dist type: " + type;
            throw new RuntimeException(msg);
        }

        if (type instanceof FuncType) {
            // There may be many functions of the same type.
            String msg = "Cannot run-time eval func type: " + type;
            throw new RuntimeException(msg);
        }

        // Unknown.
        throw new RuntimeException("Unknown type: " + type);
    }

    /**
     * Converts Java objects representing CIF values to a textual representation derived from the CIF ASCII syntax.
     * References to declarations etc, are converted to their absolute name, with keyword escaping ({@code $}), and
     * without absolute reference prefixes ({@code ^}).
     *
     * <p>
     * This method, unlike the boxing methods of the {@code CifPrettyPrinter}, produces textual representations without
     * new line characters.
     * </p>
     *
     * <p>
     * Expressions are converted to string, and joined using {@code ", "}.
     * </p>
     *
     * @param objs The Java objects to convert.
     * @return The textual representation of the Java objects representing CIF values.
     * @see CifTextUtils#exprsToStr
     */
    public static String objsToStr(List<Object> objs) {
        List<String> txts = listc(objs.size());
        for (Object obj: objs) {
            txts.add(objToStr(obj));
        }
        return StringUtils.join(txts, ", ");
    }

    /**
     * Converts a Java object representing a CIF value to a textual representation derived from the CIF ASCII syntax.
     *
     * <p>
     * References to declarations etc, are converted to their absolute name, with keyword escaping ({@code $}), and
     * without absolute reference prefixes ({@code ^}).
     * </p>
     *
     * <p>
     * This method, unlike the boxing methods of the {@code CifPrettyPrinter}, produces textual representations without
     * new line characters.
     * </p>
     *
     * @param obj The Java object to convert.
     * @return The textual representation of the Java object representing a CIF value.
     * @see CifTextUtils#exprToStr
     */
    @SuppressWarnings("unchecked")
    public static String objToStr(Object obj) {
        // See also: CifPrettyPrinter

        if (obj instanceof Boolean) {
            return CifMath.boolToStr((Boolean)obj);
        }
        if (obj instanceof Integer) {
            return CifMath.intToStr((Integer)obj);
        }

        if (obj instanceof CifEnumLiteral) {
            EnumLiteral lit = ((CifEnumLiteral)obj).literal;
            return lit.getName();
        }

        if (obj instanceof CifTuple) {
            // Tuple before List, as a Tuple is also a List.
            return "(" + objsToStr((CifTuple)obj) + ")";
        }

        if (obj instanceof List) {
            // List after Tuple, as a Tuple is also a List.
            return "[" + objsToStr((List<Object>)obj) + "]";
        }

        if (obj instanceof String) {
            return "\"" + Strings.escape((String)obj) + "\"";
        }

        if (obj instanceof Double) {
            return CifMath.realToStr((Double)obj);
        }

        if (obj instanceof Set) {
            Set<Object> set = (Set<Object>)obj;
            List<String> txts = listc(set.size());
            for (Object elem: set) {
                txts.add(objToStr(elem));
            }
            return "{" + StringUtils.join(txts, ", ") + "}";
        }

        if (obj instanceof StdLibFunction) {
            return CifTextUtils.functionToStr((StdLibFunction)obj);
        }

        if (obj instanceof Function) {
            return CifTextUtils.getAbsName((Function)obj);
        }

        if (obj instanceof Map) {
            Map<Object, Object> dict = (Map<Object, Object>)obj;
            List<String> txts = listc(dict.size());
            for (Entry<Object, Object> entry: dict.entrySet()) {
                txts.add(objToStr(entry.getKey()) + ": " + objToStr(entry.getValue()));
            }
            return "{" + StringUtils.join(txts, ", ") + "}";
        }

        if (obj instanceof Component) {
            return CifTextUtils.getAbsName((Component)obj);
        }

        throw new RuntimeException("Unknown Java obj for CIF value: " + obj);
    }
}
