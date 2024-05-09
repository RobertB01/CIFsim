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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifEquationUtils.getDerivativesForContVar;
import static org.eclipse.escet.cif.common.CifEquationUtils.getValuesForAlgVar;
import static org.eclipse.escet.cif.common.CifTypeUtils.hashType;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleType;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictPair;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteralExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFuncType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionCallExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionParameter;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInternalFunction;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newReturnFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSetExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStdLibFunctionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.escet.cif.metamodel.cif.CifPackage;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
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
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
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
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.ListProductIterator;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** CIF value utility methods. */
public class CifValueUtils {
    /** Constructor for the {@link CifValueUtils} class. */
    private CifValueUtils() {
        // Static class.
    }

    /**
     * Is the given expression trivially true.
     *
     * @param expr The expression to check. Does not have to be a predicate.
     * @param initial Whether the check should only take into account the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state). See also {@link #hasSingleValue}.
     * @param checkRefs Whether references should be followed and checked recursively. If {@code false}, references to
     *     constants, variables, functions, etc are considered not to have a single value. If {@code true}, the
     *     references may or may not have a single value, depending on the declarations to which they refer. See also
     *     {@link #hasSingleValue}.
     * @return {@code true} if the expression is trivially true, {@code false} if it is non-trivial, trivially false, or
     *     not a predicate.
     */
    public static boolean isTriviallyTrue(Expression expr, boolean initial, boolean checkRefs) {
        if (!hasSingleValue(expr, initial, checkRefs)) {
            return false;
        }
        Object value;
        try {
            value = CifEvalUtils.eval(expr, initial);
        } catch (CifEvalException e) {
            // Failed to evaluate: we don't relay the failure, but
            // instead indicate that we can't get the single value.
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        return false;
    }

    /**
     * Is the given sequence of expressions trivially true. The expressions are assumed to have conjunction semantics.
     *
     * @param exprs The expressions to check. Do not have to be predicates.
     * @param initial Whether the check should only take into account the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state). See also {@link #hasSingleValue}.
     * @param checkRefs Whether references should be followed and checked recursively. If {@code false}, references to
     *     constants, variables, functions, etc are considered not to have a single value. If {@code true}, the
     *     references may or may not have a single value, depending on the declarations to which they refer. See also
     *     {@link #hasSingleValue}.
     * @return {@code true} if the sequence of expressions is trivially true (including empty sequence), {@code false}
     *     otherwise.
     */
    public static boolean isTriviallyTrue(List<Expression> exprs, boolean initial, boolean checkRefs) {
        // If all predicates are trivially true, then the entire conjunction is
        // trivially true.
        for (Expression expr: exprs) {
            if (!isTriviallyTrue(expr, initial, checkRefs)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is the given expression trivially false.
     *
     * @param expr The expression to check. Does not have to be a predicate.
     * @param initial Whether the check should only take into account the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state). See also {@link #hasSingleValue}.
     * @param checkRefs Whether references should be followed and checked recursively. If {@code false}, references to
     *     constants, variables, functions, etc are considered not to have a single value. If {@code true}, the
     *     references may or may not have a single value, depending on the declarations to which they refer. See also
     *     {@link #hasSingleValue}.
     * @return {@code true} if the expression is trivially false, {@code false} if it is non-trivial, trivially true, or
     *     not a predicate.
     */
    public static boolean isTriviallyFalse(Expression expr, boolean initial, boolean checkRefs) {
        if (!hasSingleValue(expr, initial, checkRefs)) {
            return false;
        }
        Object value;
        try {
            value = CifEvalUtils.eval(expr, initial);
        } catch (CifEvalException e) {
            // Failed to evaluate: we don't relay the failure, but
            // instead indicate that we can't get the single value.
            return false;
        }
        if (value instanceof Boolean) {
            return !(Boolean)value;
        }
        return false;
    }

    /**
     * Is the given sequence of expressions trivially false. The expressions are assumed to have conjunction semantics.
     *
     * @param exprs The expressions to check. Do not have to be predicates.
     * @param initial Whether the check should only take into account the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state). See also {@link #hasSingleValue}.
     * @param checkRefs Whether references should be followed and checked recursively. If {@code false}, references to
     *     constants, variables, functions, etc are considered not to have a single value. If {@code true}, the
     *     references may or may not have a single value, depending on the declarations to which they refer. See also
     *     {@link #hasSingleValue}.
     * @return {@code true} if the sequence of expressions is trivially false (does not include the empty sequence),
     *     {@code false} otherwise.
     */
    public static boolean isTriviallyFalse(List<Expression> exprs, boolean initial, boolean checkRefs) {
        // If one predicate is trivially false, then the entire conjunction is
        // trivially false.
        for (Expression expr: exprs) {
            if (isTriviallyFalse(expr, initial, checkRefs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indicates whether the expression always evaluates to the same value, or may evaluate to different values
     * depending on the run-time state of the system.
     *
     * <p>
     * Note that this method is more liberal than the {@code CifExprsTypeChecker.checkStaticEvaluable} method, as the
     * type checker does not allow static evaluation of algebraic variables, while this method allows it if the
     * algebraic variable has a single value.
     * </p>
     *
     * <p>
     * Standard library function expressions are not supported, as standard library functions can't be used as values.
     * Similarly, tau expressions and field expressions are not supported by this method.
     * </p>
     *
     * @param expr The expression to check.
     * @param initial Whether the check should only take into account the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param checkRefs Whether references should be followed and checked recursively. If {@code false}, references to
     *     constants, variables, functions, etc are considered not to have a single value. If {@code true}, the
     *     references may or may not have a single value, depending on the declarations to which they refer. See also
     *     {@link #hasSingleValue}.
     * @return {@code true} if the expression always evaluates to the same value; {@code false} if the expression may
     *     evaluate to different values at run-time or if it can not be statically determined whether the expression
     *     will always evaluate to the same value at run-time.
     */
    public static boolean hasSingleValue(Expression expr, boolean initial, boolean checkRefs) {
        if (expr instanceof BoolExpression) {
            return true;
        }
        if (expr instanceof IntExpression) {
            return true;
        }
        if (expr instanceof RealExpression) {
            return true;
        }
        if (expr instanceof StringExpression) {
            return true;
        }

        if (expr instanceof TimeExpression) {
            return initial;
        }

        if (expr instanceof CastExpression) {
            // Handle cast from automaton reference to string as special case.
            Expression child = ((CastExpression)expr).getChild();
            if (CifTypeUtils.isAutRefExpr(child)) {
                // Honor the 'check reference' option, regardless of whether
                // we use an explicit automaton reference or an implicit
                // automaton 'self' reference.
                if (!checkRefs) {
                    return false;
                }

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

                // Check for single location. We don't treat 'initial' special
                // here, as we would have to ask for the possible initial
                // locations (CifLocationUtils.getPossibleInitialLocs), to use
                // them instead of all locations. That method however needs to
                // determine whether the initialization predicates of the
                // location have a single value, to evaluate them to true or
                // false. It does so using this method. As such, that would
                // lead to an infinite recursion for initialization predicates
                // of locations, causing a stack overflow.
                return aut.getLocations().size() == 1;
            }

            // Normal case: depends on the child.
            CastExpression cexpr = (CastExpression)expr;
            return hasSingleValue(cexpr.getChild(), initial, checkRefs);
        }

        if (expr instanceof UnaryExpression) {
            // Depends on the child. Sampling is explicitly excluded here,
            // even though it theoretically has no side effects.
            UnaryExpression uexpr = (UnaryExpression)expr;
            if (uexpr.getOperator() == UnaryOperator.SAMPLE) {
                return false;
            }
            return hasSingleValue(uexpr.getChild(), initial, checkRefs);
        }

        if (expr instanceof BinaryExpression) {
            // Depends on the children.
            BinaryExpression bexpr = (BinaryExpression)expr;
            return hasSingleValue(bexpr.getLeft(), initial, checkRefs)
                    && hasSingleValue(bexpr.getRight(), initial, checkRefs);
        }

        if (expr instanceof IfExpression) {
            // It depends on the guards and values.
            IfExpression ifExpr = (IfExpression)expr;

            // if/then.
            boolean guardsValue = true;
            for (Expression guard: ifExpr.getGuards()) {
                if (!hasSingleValue(guard, initial, checkRefs)) {
                    return false;
                }

                // Guard has single value.
                boolean guardValue;
                try {
                    guardValue = (Boolean)CifEvalUtils.eval(guard, initial);
                } catch (CifEvalException e) {
                    // Failed to evaluate: we don't relay the failure, but
                    // instead indicate that we can't get the single value.
                    return false;
                }
                guardsValue = guardsValue && guardValue;
            }
            if (guardsValue) {
                // Single value, all true.
                return hasSingleValue(ifExpr.getThen(), initial, checkRefs);
            } // else: single value, all false: ignore 'then'.

            // elif/then.
            for (ElifExpression elif: ifExpr.getElifs()) {
                guardsValue = true;
                for (Expression guard: elif.getGuards()) {
                    if (!hasSingleValue(guard, initial, checkRefs)) {
                        return false;
                    }

                    // Guard has single value.
                    boolean guardValue;
                    try {
                        guardValue = (Boolean)CifEvalUtils.eval(guard, initial);
                    } catch (CifEvalException e) {
                        // Failed to evaluate: we don't relay the failure, but
                        // instead indicate that we can't get the single value.
                        return false;
                    }
                    guardsValue = guardsValue && guardValue;
                }
                if (guardsValue) {
                    // Single value, all true.
                    return hasSingleValue(elif.getThen(), initial, checkRefs);
                } // else: single value, all false: ignore 'then'.
            }

            // else
            return hasSingleValue(ifExpr.getElse(), initial, checkRefs);
        }

        if (expr instanceof SwitchExpression) {
            // It depends on the control value, keys, and values.
            SwitchExpression switchExpr = (SwitchExpression)expr;

            // Control value.
            Expression value = switchExpr.getValue();
            if (CifTypeUtils.isAutRefExpr(value)) {
                // Special case: automaton (self) reference.

                // Honor the 'check reference' option, regardless of whether
                // we use an explicit automaton reference or an implicit
                // automaton 'self' reference.
                if (!checkRefs) {
                    return false;
                }

                // Get automaton.
                CifType ctype = value.getType();
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

                // Check for single location. We don't treat 'initial' special
                // here, as we would have to ask for the possible initial
                // locations (CifLocationUtils.getPossibleInitialLocs), to use
                // them instead of all locations. That method however needs to
                // determine whether the initialization predicates of the
                // location have a single value, to evaluate them to true or
                // false. It does so using this method. As such, that would
                // lead to an infinite recursion for initialization predicates
                // of locations, causing a stack overflow.
                if (aut.getLocations().size() != 1) {
                    return false;
                }
            } else {
                // Normal case: just a value expression.
                if (!hasSingleValue(value, initial, checkRefs)) {
                    return false;
                }
            }

            // Cases.
            for (SwitchCase cse: switchExpr.getCases()) {
                if (cse.getKey() != null) {
                    if (!hasSingleValue(cse.getKey(), initial, checkRefs)) {
                        return false;
                    }
                }
                if (!hasSingleValue(cse.getValue(), initial, checkRefs)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof ProjectionExpression) {
            // In principle we only need a single value for the item/element
            // that is taken out, but we can't decide that statically, so we
            // require a single value for the entire child expression.
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            if (!hasSingleValue(pexpr.getChild(), initial, checkRefs)) {
                return false;
            }
            if (pexpr.getIndex() instanceof FieldExpression) {
                return true;
            }
            return hasSingleValue(pexpr.getIndex(), initial, checkRefs);
        }

        if (expr instanceof SliceExpression) {
            // In principle we only need single values for the items/elements
            // that are taken out, but we can't decide that statically, so we
            // require a single value for the entire child expression.
            SliceExpression sexpr = (SliceExpression)expr;
            if (!hasSingleValue(sexpr.getChild(), initial, checkRefs)) {
                return false;
            }
            if (sexpr.getBegin() != null) {
                if (!hasSingleValue(sexpr.getBegin(), initial, checkRefs)) {
                    return false;
                }
            }
            if (sexpr.getEnd() != null) {
                if (!hasSingleValue(sexpr.getEnd(), initial, checkRefs)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fcexpr = (FunctionCallExpression)expr;

            if (fcexpr.getFunction() instanceof StdLibFunctionExpression) {
                StdLibFunctionExpression stdlib = (StdLibFunctionExpression)fcexpr.getFunction();
                StdLibFunction func = stdlib.getFunction();
                if (CifTypeUtils.isDistFunction(func)) {
                    // Special case: distribution functions result in
                    // distributions with random seeds and thus do not produce
                    // unique values. They are explicitly excluded.
                    return false;
                }
            } else {
                // We can't statically evaluate user-defined functions,
                // as we don't know whether they will ever terminate.
                return false;
            }

            for (Expression arg: fcexpr.getArguments()) {
                if (!hasSingleValue(arg, initial, checkRefs)) {
                    return false;
                }
            }

            return true;
        }

        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                if (!hasSingleValue(elem, initial, checkRefs)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof SetExpression) {
            // For the set '{x, 1, 2}', with 'x' of type 'int[1..2]', we have
            // two possible sets: '{1, 1, 2}' and '{2, 1, 2}', both of which
            // are really set '{1, 2}'. Therefore, the original set has a
            // single value, but this can't be determined statically. Since
            // variable 'x' does not have a single value, 'false' is returned
            // for this set.
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                if (!hasSingleValue(elem, initial, checkRefs)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression elem: texpr.getFields()) {
                if (!hasSingleValue(elem, initial, checkRefs)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof DictExpression) {
            // For the dictionary '{x:3, 1:3, 2:3}', with 'x' of type
            // 'int[1..2]', we have two possible dictionaries:
            // '{1:3, 1:3, 2:3}' and '{2:3, 1:3, 2:3}', both of which
            // are really dictionary '{1:3, 2:3}'. Therefore, the original
            // dictionary has a single value, but this can't be determined
            // statically. Since variable 'x' does not have a single value,
            // 'false' is returned for this dictionary.
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                if (!hasSingleValue(pair.getKey(), initial, checkRefs)) {
                    return false;
                }
                if (!hasSingleValue(pair.getValue(), initial, checkRefs)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof ConstantExpression) {
            // We know for sure it has a single value (as all constants do),
            // but not if references are not to be checked/considered.
            if (!checkRefs) {
                return false;
            }

            // Depends on the value of the constant.
            Constant constant = ((ConstantExpression)expr).getConstant();
            return hasSingleValue(constant.getValue(), initial, checkRefs);
        }

        if (expr instanceof DiscVariableExpression) {
            // If we are not allowed to check the referred declaration, we
            // can't be sure.
            if (!checkRefs) {
                return false;
            }

            // This is either a discrete variable of an automaton, or a
            // parameter or local variable of a function.
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();

            // Discrete variables are part of the run-time state, local
            // variables of functions are part of the function evaluation
            // state, and function parameters can have different values given
            // multiple function calls. If however the type of the variable
            // allows only a single value, then we know for sure.
            if (hasSingleValue(var.getType())) {
                return true;
            }

            // If parameter, then we don't know.
            if (var.eContainer() instanceof FunctionParameter) {
                return false;
            }

            // In the initial state, we can check the initial values.
            if (!initial) {
                return false;
            }

            // If default value, then single value, derived from the type.
            if (var.getValue() == null) {
                // For functions, getting the default value from the type
                // results in the side effect of creating a new function for
                // the function type, with which we don't want to have to deal.
                if (CifTypeUtils.hasFunctionType(var.getType())) {
                    return false;
                }

                // For distributions, the single value is a 'constant' standard
                // library function call, which we explicitly exclude. See also
                // the case for the function call.
                if (CifTypeUtils.hasDistType(var.getType())) {
                    return false;
                }

                return true;
            }

            // If not exactly one possible initial value, then we don't know.
            if (var.getValue().getValues().size() != 1) {
                return false;
            }

            // Check the single value.
            Expression value = first(var.getValue().getValues());
            return hasSingleValue(value, initial, checkRefs);
        }

        if (expr instanceof AlgVariableExpression) {
            // If we are not allowed to check the referred declaration, we
            // can't be sure.
            if (!checkRefs) {
                return false;
            }

            // Get variable.
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();

            // Try value first.
            Expression value = var.getValue();
            if (value != null) {
                return hasSingleValue(value, initial, checkRefs);
            }

            // Algebraic variable parameter or value specified in equation.
            if (var.eContainer() instanceof Parameter) {
                // For algebraic parameters, it depends on the type.
                return hasSingleValue(var.getType());
            } else {
                // Find equation.
                ComplexComponent comp = (ComplexComponent)var.eContainer();
                for (Equation eq: comp.getEquations()) {
                    if (eq.getVariable() == var) {
                        return hasSingleValue(eq.getValue(), initial, checkRefs);
                    }
                }

                // Equations in the locations. This is not a single value, as
                // locations can change value.
                return false;
            }
        }

        if (expr instanceof ContVariableExpression) {
            // If we are not allowed to check the referred declaration, we
            // can't be sure.
            if (!checkRefs) {
                return false;
            }

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
                    return hasSingleValue(der, initial, checkRefs);
                }

                // Derivative specified in one or more equations. Find
                // equation in component.
                ComplexComponent comp = (ComplexComponent)var.eContainer();
                for (Equation eq: comp.getEquations()) {
                    if (eq.getVariable() == var) {
                        return hasSingleValue(eq.getValue(), initial, checkRefs);
                    }
                }

                // Equations in the locations. This is not a single value, as
                // locations can change value.
                return false;
            } else {
                // Reference to a continuous variable. This situation is
                // similar to the discrete variable case. The type however is
                // always 'real', which has infinitely many values. Only in
                // the initial state, can we check the initial value.
                if (!initial) {
                    return false;
                }

                // If default value, then 0.0, which is a single value.
                if (var.getValue() == null) {
                    return true;
                }

                // Check the single value.
                return hasSingleValue(var.getValue(), initial, checkRefs);
            }
        }

        if (expr instanceof TauExpression) {
            // We should never encounter 'tau' expressions in a value context.
            throw new RuntimeException("Tau expression in value context.");
        }

        if (expr instanceof LocationExpression) {
            // If we are not allowed to check the referred declaration, we
            // can't be sure.
            if (!checkRefs) {
                return false;
            }

            // Locations are part of the run-time state. If however the
            // location is the only location in its automaton, then the
            // expression always evaluates to true. If it is a parameter,
            // then we can never be sure. We don't treat single possible
            // initial locations special here, as we're not yet sure
            // that won't break any existing CIF code.
            Location loc = ((LocationExpression)expr).getLocation();
            EObject parent = loc.eContainer();
            if (parent instanceof Parameter) {
                return false;
            }

            Automaton aut = (Automaton)parent;
            return aut.getLocations().size() == 1;
        }

        if (expr instanceof EnumLiteralExpression) {
            return true;
        }

        if (expr instanceof EventExpression) {
            // We should never encounter events in a value context.
            throw new RuntimeException("Event expression in value context.");
        }

        if (expr instanceof FieldExpression) {
            String msg = "Unexpected field expr: proj expr should handle it.";
            throw new RuntimeException(msg);
        }

        if (expr instanceof StdLibFunctionExpression) {
            String msg = "Stdlib functions can not be used as values.";
            throw new RuntimeException(msg);
        }

        if (expr instanceof FunctionExpression) {
            // Note that the function call expression considers user-defined
            // functions as not having a single value, to avoid their
            // evaluation in a static context. This is to prevent evaluation
            // of user-defined functions that never terminate. This is
            // regardless of whether the user-defined function itself is
            // considered to have a single value.

            // If we are not allowed to check the referred declaration, we
            // are obligated to return false.
            if (!checkRefs) {
                return false;
            }

            // Function reference expressions reference exactly one function.
            return true;
        }

        if (expr instanceof InputVariableExpression) {
            // If we are not allowed to check the referred declaration, we
            // can't be sure.
            if (!checkRefs) {
                return false;
            }

            // Depends on the type.
            InputVariable var = ((InputVariableExpression)expr).getVariable();
            return hasSingleValue(var.getType());
        }

        if (expr instanceof ComponentExpression) {
            // A reference to a component always refers to the same component
            // and could evaluate to itself, but we don't support components
            // as values. However, special cases such as casting to string may
            // be handled by other expressions. Here, we handle the reference
            // itself, for instance for component parameters, not those special
            // cases.
            return false;
        }

        if (expr instanceof CompParamExpression) {
            // A reference to a component parameter always refers to the same
            // component parameter, but we don't support components parameters
            // as values. However, special cases such as casting to string
            // may be handled by other expressions. Here, we handle the reference
            // itself, similar to how we handle ComponentExpression.
            return false;
        }

        if (expr instanceof CompInstWrapExpression) {
            // Depends on the child. Just peel of the wrapper, as we don't
            // care how we get to a value. That is, the context via which we
            // reach a value doesn't influence whether or not it has a single
            // value.
            Expression rexpr = ((CompInstWrapExpression)expr).getReference();
            return hasSingleValue(rexpr, initial, checkRefs);
        }

        if (expr instanceof CompParamWrapExpression) {
            // Depends on the child. Just peel of the wrapper, as we don't
            // care how we get to a value. That is, the context via which we
            // reach a value doesn't influence whether or not it has a single
            // value.
            Expression rexpr = ((CompParamWrapExpression)expr).getReference();
            return hasSingleValue(rexpr, initial, checkRefs);
        }

        if (expr instanceof ReceivedExpression) {
            // Even though we refer to an implicitly declared variable, we
            // honor the 'check reference' option.
            if (!checkRefs) {
                return false;
            }

            // Depends on the type.
            return hasSingleValue(expr.getType());
        }

        if (expr instanceof SelfExpression) {
            // Automaton 'self' reference would result in an automaton as
            // value, which is a component, which we don't support as value.
            // However, special cases such as casting to string may be handled
            // by other expressions. Here, we handle the reference itself,
            // similar to how we handle ComponentExpression.
            return false;
        }

        throw new RuntimeException("Unknown expr: " + expr);
    }

    /**
     * Indicates whether the type represents a single value, or a set of possible values.
     *
     * <p>
     * Note that unlike the overload for expressions, this overload for types does not have a 'checkRefs' parameter, as
     * types are can never change at run-time. Also, the distinction we have for values, i.e. in certain cases not
     * wanting to inline e.g. constants as that makes it harder to change the resulting model, is not needed for types.
     * For similar reasons, this overload has no 'initial' parameter.
     * </p>
     *
     * @param type The type to check.
     * @return {@code true} if the type represents a single value, {@code false} otherwise.
     */
    public static boolean hasSingleValue(CifType type) {
        // Primitive types.
        if (type instanceof BoolType) {
            return false;
        }

        if (type instanceof IntType) {
            // Only has a single value if it has a single value range.
            IntType itype = (IntType)type;
            if (CifTypeUtils.isRangeless(itype)) {
                return false;
            }
            return itype.getLower().equals(itype.getUpper());
        }

        if (type instanceof RealType) {
            return false;
        }
        if (type instanceof StringType) {
            return false;
        }
        if (type instanceof VoidType) {
            return false;
        }

        // Container types.
        if (type instanceof ListType) {
            // Only has a single value if it is an array, and the element type
            // has a single value.
            ListType ltype = (ListType)type;
            if (CifTypeUtils.isRangeless(ltype)) {
                return false;
            }
            if (!ltype.getLower().equals(ltype.getUpper())) {
                return false;
            }
            return hasSingleValue(ltype.getElementType());
        }

        if (type instanceof SetType) {
            return false;
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            for (Field field: ttype.getFields()) {
                if (!hasSingleValue(field.getType())) {
                    return false;
                }
            }
            return true;
        }

        if (type instanceof DictType) {
            return false;
        }

        // Wrappings.
        if (type instanceof CompInstWrapType) {
            // Depends on the child. Just peel of the wrapper, as we don't
            // care how we get to a type. That is, the context via which we
            // reach a type doesn't influence whether or not it has a single
            // value.
            CifType rtype = ((CompInstWrapType)type).getReference();
            return hasSingleValue(rtype);
        }

        if (type instanceof CompParamWrapType) {
            // Depends on the child. Just peel of the wrapper, as we don't
            // care how we get to a type. That is, the context via which we
            // reach a type doesn't influence whether or not it has a single
            // value.
            CifType rtype = ((CompParamWrapType)type).getReference();
            return hasSingleValue(rtype);
        }

        // References.
        if (type instanceof ComponentDefType) {
            // Since there may be many instantiations, there may be many
            // values.
            return false;
        }

        if (type instanceof ComponentType) {
            // There is only one component that matches this type.
            return true;
        }

        if (type instanceof EnumType) {
            // Only has a single value if it has a single literal.
            return ((EnumType)type).getEnum().getLiterals().size() == 1;
        }

        if (type instanceof TypeRef) {
            // Peel of the type reference.
            CifType rtype = ((TypeRef)type).getType().getType();
            return hasSingleValue(rtype);
        }

        // Other types.
        if (type instanceof FuncType) {
            // There may be many functions with the same type.
            return false;
        }

        if (type instanceof DistType) {
            // There may be many distributions with the same type.
            return false;
        }

        // Unknown.
        throw new RuntimeException("Unknown type: " + type);
    }

    /**
     * Returns the default value for the given type.
     *
     * <p>
     * For more information, consult the CIF metamodel documentation, for the 'DiscVariable.value' feature.
     * </p>
     *
     * <p>
     * Wrapping types, component types, component definition types, and void types are unsupported.
     * </p>
     *
     * <p>
     * For function types, a function of the correct type is needed, which returns the default value for the return type
     * of the function. To avoid duplicates, and to allow for the newly created functions to be contained somewhere, the
     * {@code funcs} parameter is used (exposed to the caller). The newly created functions have no name.
     * </p>
     *
     * <p>
     * The resulting value is freshly created and does not need to be deep cloned.
     * </p>
     *
     * @param type The type for which to return the default value.
     * @param funcs The functions for default values of function types. If an appropriate function is already present,
     *     it is used. If no appropriate function is present, one is created, and it is added to this list (in-place
     *     modification).
     * @return The default value for the given type.
     */
    public static Expression getDefaultValue(CifType type, List<InternalFunction> funcs) {
        if (type instanceof BoolType) {
            return makeFalse();
        } else if (type instanceof IntType) {
            // Get default value.
            IntType itype = (IntType)type;
            int defaultValue;
            if (CifTypeUtils.isRangeless(itype)) {
                defaultValue = 0;
            } else {
                // Use the value closest to zero.
                if (itype.getLower() <= 0 && itype.getUpper() >= 0) {
                    defaultValue = 0;
                } else {
                    int lDistanceToZero = Math.abs(itype.getLower());
                    int uDistanceToZero = Math.abs(itype.getUpper());
                    defaultValue = (lDistanceToZero < uDistanceToZero) ? itype.getLower() : itype.getUpper();
                }
            }

            // Return a proper expression for the default value.
            return makeInt(defaultValue);
        } else if (type instanceof TypeRef) {
            return getDefaultValue(((TypeRef)type).getType().getType(), funcs);
        } else if (type instanceof EnumType) {
            EnumLiteralExpression rslt = newEnumLiteralExpression();
            rslt.setType(deepclone(type));

            EnumDecl enumDecl = ((EnumType)type).getEnum();
            rslt.setLiteral(first(enumDecl.getLiterals()));

            return rslt;
        } else if (type instanceof RealType) {
            RealExpression rslt = newRealExpression();
            rslt.setType(deepclone(type));

            rslt.setValue("0.0");
            return rslt;
        } else if (type instanceof StringType) {
            StringExpression rslt = newStringExpression();
            rslt.setType(deepclone(type));

            rslt.setValue("");
            return rslt;
        } else if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            ListExpression rslt = newListExpression();
            rslt.setType(deepclone(type));
            if (!CifTypeUtils.isRangeless(ltype)) {
                int lower = ltype.getLower();
                for (int i = 0; i < lower; i++) {
                    Expression elem = getDefaultValue(ltype.getElementType(), funcs);
                    rslt.getElements().add(elem);
                }
            }
            return rslt;
        } else if (type instanceof SetType) {
            SetExpression rslt = newSetExpression();
            rslt.setType(deepclone(type));
            return rslt;
        } else if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            ftype.getReturnType();

            // See if we can find a function that already exists.
            InternalFunction function = null;
            for (InternalFunction func: funcs) {
                // Get type of the function.
                FuncType t = newFuncType();
                CifType rt = makeTupleType(func.getReturnTypes(), null);
                t.setReturnType(rt);
                for (FunctionParameter param: func.getParameters()) {
                    CifType pt = param.getParameter().getType();
                    t.getParamTypes().add(deepclone(pt));
                }

                // If it matches, we found a correct function.
                if (CifTypeUtils.checkTypeCompat(t, ftype, RangeCompat.EQUAL)) {
                    function = func;
                    break;
                }
            }

            // If no function found, create new one, and add it.
            if (function == null) {
                function = newInternalFunction();

                for (int i = 0; i < ftype.getParamTypes().size(); i++) {
                    CifType ptype = ftype.getParamTypes().get(i);

                    DiscVariable pvar = newDiscVariable();
                    pvar.setName("p" + i);
                    pvar.setType(deepclone(ptype));

                    FunctionParameter param = newFunctionParameter();
                    param.setParameter(pvar);

                    function.getParameters().add(param);
                }

                CifType rtype = ftype.getReturnType();
                function.getReturnTypes().add(deepclone(rtype));

                Expression retValue = getDefaultValue(ftype.getReturnType(), funcs);

                ReturnFuncStatement stat = newReturnFuncStatement();
                stat.getValues().add(retValue);

                function.getStatements().add(stat);

                funcs.add(function);
            }

            // Create and return function reference expression.
            FunctionExpression rslt = newFunctionExpression();
            rslt.setFunction(function);
            rslt.setType(deepclone(ftype));

            return rslt;
        } else if (type instanceof DictType) {
            DictExpression rslt = newDictExpression();
            rslt.setType(deepclone(type));
            return rslt;
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;

            TupleExpression rslt = newTupleExpression();
            rslt.setType(deepclone(type));

            for (Field field: ttype.getFields()) {
                Expression value = getDefaultValue(field.getType(), funcs);
                rslt.getFields().add(value);
            }

            return rslt;
        } else if (type instanceof DistType) {
            // Get distribution type.
            DistType dtype = (DistType)type;

            // Create function type.
            FuncType ftype = newFuncType();
            ftype.setReturnType(deepclone(type));
            ftype.getParamTypes().add(deepclone(dtype.getSampleType()));

            // Create function expression.
            StdLibFunctionExpression func = newStdLibFunctionExpression();
            func.setFunction(StdLibFunction.CONSTANT);
            func.setType(ftype);

            // Get argument.
            Expression arg = getDefaultValue(dtype.getSampleType(), funcs);

            // Create function call expression.
            FunctionCallExpression rslt = newFunctionCallExpression();
            rslt.setType(deepclone(type));

            rslt.setFunction(func);
            rslt.getArguments().add(arg);

            return rslt;
        } else {
            // Component types, component definition types, wrapping types,
            // and void types are unsupported.
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Is the given expression an 'initial expression'? That is, is it an expression that is only used in the initial
     * state?
     *
     * <p>
     * Initial expressions include only the following cases:
     * <ul>
     * <li>Initial values of discrete and continuous variables.</li>
     * <li>Initialization predicates in components and locations.</li>
     * </ul>
     * </p>
     *
     * @param expr The expression to check.
     * @return {@code true} if the expression is an 'initial expression', {@code false} otherwise.
     */
    public static boolean isInitialExpr(Expression expr) {
        // Find non-expression ancestor, and the containment feature from
        // that parent to the given expression or some intermediate ancestor
        // of the expression.
        EObject parent = expr;
        EStructuralFeature feat = null;
        while (parent instanceof Expression) {
            feat = parent.eContainingFeature();
            parent = parent.eContainer();
        }
        Assert.notNull(feat);

        // Check for initial value of a discrete variable.
        if (parent instanceof VariableValue) {
            // No need to check the feature, as 'values' is the only feature.
            DiscVariable var = (DiscVariable)parent.eContainer();
            if (var.eContainer() instanceof ComplexComponent) {
                // Variable declaration in a component.
                return true;
            } else {
                // Local variable of a function. Can't be a function parameter
                // as they don't have values.
                return false;
            }
        }

        // Check for initial value of a continuous variable.
        if (parent instanceof ContVariable && feat == DeclarationsPackage.Literals.CONT_VARIABLE__VALUE) {
            return true;
        }

        // Check for initialization predicate in a component.
        if (parent instanceof ComplexComponent && feat == CifPackage.Literals.COMPLEX_COMPONENT__INITIALS) {
            return true;
        }

        // Check for initialization predicate in a location.
        if (parent instanceof Location && feat == AutomataPackage.Literals.LOCATION__INITIALS) {
            return true;
        }

        // Not one of the recognized cases.
        return false;
    }

    /**
     * Is the value of the given expression time constant?
     *
     * <p>
     * References to algebraic parameters are not supported by this method.
     * </p>
     *
     * @param expr The expression to check.
     * @param isInputVarTimeConstant Whether input variables should be regarded as time constant.
     * @return {@code true} if the value of the given expression is time constant, {@code false} if it is time
     *     inconstant (time dependent).
     */
    public static boolean isTimeConstant(Expression expr, Boolean isInputVarTimeConstant) {
        if (expr instanceof BoolExpression) {
            return true;
        }
        if (expr instanceof IntExpression) {
            return true;
        }
        if (expr instanceof RealExpression) {
            return true;
        }
        if (expr instanceof StringExpression) {
            return true;
        }

        if (expr instanceof TimeExpression) {
            return false;
        }

        if (expr instanceof CastExpression) {
            // Depends on the child.
            CastExpression cexpr = (CastExpression)expr;
            return isTimeConstant(cexpr.getChild(), isInputVarTimeConstant);
        }

        if (expr instanceof UnaryExpression) {
            // Depends on the child.
            UnaryExpression uexpr = (UnaryExpression)expr;
            return isTimeConstant(uexpr.getChild(), isInputVarTimeConstant);
        }

        if (expr instanceof BinaryExpression) {
            // Depends on the children.
            BinaryExpression bexpr = (BinaryExpression)expr;
            return isTimeConstant(bexpr.getLeft(), isInputVarTimeConstant)
                    && isTimeConstant(bexpr.getRight(), isInputVarTimeConstant);
        }

        if (expr instanceof IfExpression) {
            // It depends on the guards and values. We don't evaluate the
            // guards to see if they are trivially true/false, etc.
            IfExpression ifExpr = (IfExpression)expr;

            for (Expression guard: ifExpr.getGuards()) {
                if (!isTimeConstant(guard, isInputVarTimeConstant)) {
                    return false;
                }
            }

            if (!isTimeConstant(ifExpr.getThen(), isInputVarTimeConstant)) {
                return false;
            }

            for (ElifExpression elif: ifExpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    if (!isTimeConstant(guard, isInputVarTimeConstant)) {
                        return false;
                    }
                }

                if (!isTimeConstant(elif.getThen(), isInputVarTimeConstant)) {
                    return false;
                }
            }

            return isTimeConstant(ifExpr.getElse(), isInputVarTimeConstant);
        }

        if (expr instanceof SwitchExpression) {
            // It depends on the control value, keys, and values.
            SwitchExpression switchExpr = (SwitchExpression)expr;

            if (!isTimeConstant(switchExpr.getValue(), isInputVarTimeConstant)) {
                return false;
            }

            for (SwitchCase cse: switchExpr.getCases()) {
                if (cse.getKey() != null) {
                    if (!isTimeConstant(cse.getKey(), isInputVarTimeConstant)) {
                        return false;
                    }
                }
                if (!isTimeConstant(cse.getValue(), isInputVarTimeConstant)) {
                    return false;
                }
            }

            return true;
        }

        if (expr instanceof ProjectionExpression) {
            // Depends on the child and index.
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            if (!isTimeConstant(pexpr.getChild(), isInputVarTimeConstant)) {
                return false;
            }
            if (pexpr.getIndex() instanceof FieldExpression) {
                return true;
            }
            return isTimeConstant(pexpr.getIndex(), isInputVarTimeConstant);
        }

        if (expr instanceof SliceExpression) {
            // Depends on the child, begin index, and end index.
            SliceExpression sexpr = (SliceExpression)expr;
            if (!isTimeConstant(sexpr.getChild(), isInputVarTimeConstant)) {
                return false;
            }
            if (sexpr.getBegin() != null) {
                if (!isTimeConstant(sexpr.getBegin(), isInputVarTimeConstant)) {
                    return false;
                }
            }
            if (sexpr.getEnd() != null) {
                if (!isTimeConstant(sexpr.getEnd(), isInputVarTimeConstant)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof FunctionCallExpression) {
            // Depends on the function and arguments.
            FunctionCallExpression fcexpr = (FunctionCallExpression)expr;

            if (fcexpr.getFunction() instanceof StdLibFunctionExpression) {
                // Always time constant.
            } else {
                if (!isTimeConstant(fcexpr.getFunction(), isInputVarTimeConstant)) {
                    return false;
                }
            }

            for (Expression arg: fcexpr.getArguments()) {
                if (!isTimeConstant(arg, isInputVarTimeConstant)) {
                    return false;
                }
            }

            return true;
        }

        if (expr instanceof ListExpression) {
            // Depends on the elements.
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                if (!isTimeConstant(elem, isInputVarTimeConstant)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof SetExpression) {
            // Depends on the elements.
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                if (!isTimeConstant(elem, isInputVarTimeConstant)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof TupleExpression) {
            // Depends on the elements.
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression elem: texpr.getFields()) {
                if (!isTimeConstant(elem, isInputVarTimeConstant)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof DictExpression) {
            // Depends on the keys/values.
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                if (!isTimeConstant(pair.getKey(), isInputVarTimeConstant)) {
                    return false;
                }
                if (!isTimeConstant(pair.getValue(), isInputVarTimeConstant)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof ConstantExpression) {
            return true;
        }

        if (expr instanceof DiscVariableExpression) {
            return true;
        }

        if (expr instanceof AlgVariableExpression) {
            // Get variable.
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();

            // Try value first.
            Expression value = var.getValue();
            if (value != null) {
                return isTimeConstant(value, isInputVarTimeConstant);
            }

            // Algebraic variable parameters are unsupported.
            if (var.eContainer() instanceof Parameter) {
                // Algebraic parameters not supported by this method.
                throw new RuntimeException("unsupported alg param: " + var);
            }

            // Value(s) specified in equation(s).
            List<Expression> values = getValuesForAlgVar(var, false);
            for (Expression val: values) {
                if (!isTimeConstant(val, isInputVarTimeConstant)) {
                    return false;
                }
            }
            return true;
        }

        if (expr instanceof ContVariableExpression) {
            ContVariableExpression cexpr = (ContVariableExpression)expr;
            if (cexpr.isDerivative()) {
                // Get variable.
                ContVariable var = cexpr.getVariable();

                // Try derivative from declaration first.
                Expression deriv = var.getDerivative();
                if (deriv != null) {
                    return isTimeConstant(deriv, isInputVarTimeConstant);
                }

                // Derivative(s) specified in equation(s).
                List<Expression> derivs = getDerivativesForContVar(var, false);
                for (Expression d: derivs) {
                    if (!isTimeConstant(d, isInputVarTimeConstant)) {
                        return false;
                    }
                }
                return true;
            } else {
                // Even though the derivative could be zero, we just consider
                // it time dependent by definition.
                return false;
            }
        }

        if (expr instanceof TauExpression) {
            // We should never encounter 'tau' expressions in a value context.
            throw new RuntimeException("Tau expression in value context.");
        }

        if (expr instanceof LocationExpression) {
            // It is not possible to go to another location during time
            // transitions.
            return true;
        }

        if (expr instanceof EnumLiteralExpression) {
            return true;
        }

        if (expr instanceof EventExpression) {
            // We should never encounter event expressions in a value context.
            throw new RuntimeException("Event expression in value context.");
        }

        if (expr instanceof FieldExpression) {
            String msg = "Unexpected field expr: proj expr should handle it.";
            throw new RuntimeException(msg);
        }

        if (expr instanceof StdLibFunctionExpression) {
            String msg = "Stdlib functions can not be used as values.";
            throw new RuntimeException(msg);
        }

        if (expr instanceof FunctionExpression) {
            return true;
        }

        if (expr instanceof InputVariableExpression) {
            return isInputVarTimeConstant;
        }

        if (expr instanceof ComponentExpression) {
            return true;
        }

        if (expr instanceof CompParamExpression) {
            return true;
        }

        if (expr instanceof CompInstWrapExpression) {
            // Depends on the child, and not how we reach the child.
            Expression rexpr = ((CompInstWrapExpression)expr).getReference();
            return isTimeConstant(rexpr, isInputVarTimeConstant);
        }

        if (expr instanceof CompParamWrapExpression) {
            // Depends on the child, and not how we reach the child.
            Expression rexpr = ((CompParamWrapExpression)expr).getReference();
            return isTimeConstant(rexpr, isInputVarTimeConstant);
        }

        if (expr instanceof ReceivedExpression) {
            // Once the value is received, it doesn't change.
            return true;
        }

        if (expr instanceof SelfExpression) {
            // The current/active location does not change during time passage.
            return true;
        }

        throw new RuntimeException("Unknown expr: " + expr);
    }

    /**
     * Are the two expression structurally the same?
     *
     * @param expr1 The first expression to check.
     * @param expr2 The second expression to check.
     * @return {@code true} if the two expressions are structurally the same, {@code false} otherwise.
     * @see #hashExpr
     */
    public static Boolean areStructurallySameExpression(Expression expr1, Expression expr2) {
        if (!expr1.getClass().equals(expr2.getClass())) {
            return false;
        }

        if (expr1 instanceof BoolExpression bexpr1 && expr2 instanceof BoolExpression bexpr2) {
            return bexpr1.isValue() == bexpr2.isValue();
        }

        if (expr1 instanceof IntExpression iexpr1 && expr2 instanceof IntExpression iexpr2) {
            return iexpr1.getValue() == iexpr2.getValue();
        }

        if (expr1 instanceof RealExpression rExpr1 && expr2 instanceof RealExpression rexpr2) {
            return rExpr1.getValue().equals(rexpr2.getValue());
        }

        if (expr1 instanceof StringExpression sexpr1 && expr2 instanceof StringExpression sexpr2) {
            return sexpr1.getValue().equals(sexpr2.getValue());
        }

        if (expr1 instanceof TimeExpression && expr2 instanceof TimeExpression) {
            return true;
        }

        if (expr1 instanceof CastExpression cexpr1 && expr2 instanceof CastExpression cexpr2) {
            if (!areStructurallySameExpression(cexpr1.getChild(), cexpr2.getChild())) {
                return false;
            }

            return CifTypeUtils.areStructurallySameType(cexpr1.getType(), cexpr2.getType());
        }

        if (expr1 instanceof UnaryExpression uexpr1 && expr2 instanceof UnaryExpression uexpr2) {
            return uexpr1.getOperator() == uexpr2.getOperator()
                    && areStructurallySameExpression(uexpr1.getChild(), uexpr2.getChild());
        }

        if (expr1 instanceof BinaryExpression bexpr1 && expr2 instanceof BinaryExpression bexpr2) {
            return bexpr1.getOperator() == bexpr2.getOperator()
                    && areStructurallySameExpression(bexpr1.getLeft(), bexpr2.getLeft())
                    && areStructurallySameExpression(bexpr1.getRight(), bexpr2.getRight());
        }

        if (expr1 instanceof IfExpression iexpr1 && expr2 instanceof IfExpression iexpr2) {
            // Check same number of guards.
            if (iexpr1.getGuards().size() != iexpr2.getGuards().size()) {
                return false;
            }

            // Check same guards.
            for (int i = 0; i < iexpr1.getGuards().size(); i++) {
                if (!areStructurallySameExpression(iexpr1.getGuards().get(i), iexpr2.getGuards().get(i))) {
                    return false;
                }
            }

            // Check same then.
            if (!areStructurallySameExpression(iexpr1.getThen(), iexpr2.getThen())) {
                return false;
            }

            // Check same number of elifs.
            if (iexpr1.getElifs().size() != iexpr2.getElifs().size()) {
                return false;
            }

            // Check same elifs.
            for (int i = 0; i < iexpr1.getElifs().size(); i++) {
                ElifExpression elif1 = iexpr1.getElifs().get(i);
                ElifExpression elif2 = iexpr2.getElifs().get(i);

                // Check same number of guards.
                if (elif1.getGuards().size() != elif2.getGuards().size()) {
                    return false;
                }

                // Check same guards.
                for (int j = 0; j < elif1.getGuards().size(); j++) {
                    if (!areStructurallySameExpression(elif1.getGuards().get(j), elif2.getGuards().get(j))) {
                        return false;
                    }
                }

                // Check same then.
                if (!areStructurallySameExpression(elif1.getThen(), elif2.getThen())) {
                    return false;
                }
            }

            // Check same else.
            if (!areStructurallySameExpression(iexpr1.getElse(), iexpr2.getElse())) {
                return false;
            }

            return true;
        }

        if (expr1 instanceof SwitchExpression sexpr1 && expr2 instanceof SwitchExpression sexpr2) {
            // Check value.
            if (!areStructurallySameExpression(sexpr1.getValue(), sexpr2.getValue())) {
                return false;
            }

            // Check same number of cases.
            if (sexpr1.getCases().size() != sexpr2.getCases().size()) {
                return false;
            }

            // Check cases and values.
            for (int i = 0; i < sexpr1.getCases().size(); i++) {
                SwitchCase switchCase1 = sexpr1.getCases().get(i);
                SwitchCase switchCase2 = sexpr2.getCases().get(i);
                if ((switchCase1.getKey() == null) != (switchCase2.getKey() == null)) {
                    return false;
                }

                if (switchCase1.getKey() != null
                        && !areStructurallySameExpression(switchCase1.getKey(), switchCase2.getKey()))
                {
                    return false;
                }

                if (!areStructurallySameExpression(switchCase1.getValue(), switchCase2.getValue())) {
                    return false;
                }
            }

            return true;
        }

        if (expr1 instanceof ProjectionExpression pexpr1 && expr2 instanceof ProjectionExpression pexpr2) {
            return areStructurallySameExpression(pexpr1.getChild(), pexpr2.getChild())
                    && areStructurallySameExpression(pexpr1.getIndex(), pexpr2.getIndex());
        }

        if (expr1 instanceof SliceExpression sexpr1 && expr2 instanceof SliceExpression sexpr2) {
            if ((sexpr1.getBegin() == null) != (sexpr2.getBegin() == null)) {
                return false;
            }
            if (sexpr1.getBegin() != null && !areStructurallySameExpression(sexpr1.getBegin(), sexpr2.getBegin())) {
                return false;
            }

            if ((sexpr1.getEnd() == null) != (sexpr2.getEnd() == null)) {
                return false;
            }
            if (sexpr1.getEnd() != null && !areStructurallySameExpression(sexpr1.getEnd(), sexpr2.getEnd())) {
                return false;
            }

            return areStructurallySameExpression(sexpr1.getChild(), sexpr2.getChild());
        }

        if (expr1 instanceof FunctionCallExpression fcexpr1 && expr2 instanceof FunctionCallExpression fcexpr2) {
            if (!areStructurallySameExpression(fcexpr1.getFunction(), fcexpr2.getFunction())) {
                return false;
            }

            // Since only valid expressions are checked, and these are the same functions, they must have the same
            // number of arguments.
            for (int i = 0; i < fcexpr1.getArguments().size(); i++) {
                if (!areStructurallySameExpression(fcexpr1.getArguments().get(i), fcexpr2.getArguments().get(i))) {
                    return false;
                }
            }

            return true;
        }

        if (expr1 instanceof ListExpression lexpr1 && expr2 instanceof ListExpression lexpr2) {
            if (lexpr1.getElements().size() != lexpr2.getElements().size()) {
                return false;
            }

            for (int i = 0; i < lexpr1.getElements().size(); i++) {
                if (!areStructurallySameExpression(lexpr1.getElements().get(i), lexpr2.getElements().get(i))) {
                    return false;
                }
            }

            return true;
        }

        if (expr1 instanceof SetExpression sexpr1 && expr2 instanceof SetExpression sexpr2) {
            if (sexpr1.getElements().size() != sexpr2.getElements().size()) {
                return false;
            }

            for (int i = 0; i < sexpr1.getElements().size(); i++) {
                if (!areStructurallySameExpression(sexpr1.getElements().get(i), sexpr2.getElements().get(i))) {
                    return false;
                }
            }

            return true;
        }

        if (expr1 instanceof TupleExpression texpr1 && expr2 instanceof TupleExpression texpr2) {
            if (texpr1.getFields().size() != texpr2.getFields().size()) {
                return false;
            }

            for (int i = 0; i < texpr1.getFields().size(); i++) {
                if (!areStructurallySameExpression(texpr1.getFields().get(i), texpr2.getFields().get(i))) {
                    return false;
                }
            }

            return true;
        }

        if (expr1 instanceof DictExpression dexpr1 && expr2 instanceof DictExpression dexpr2) {
            if (dexpr1.getPairs().size() != dexpr2.getPairs().size()) {
                return false;
            }

            for (int i = 0; i < dexpr1.getPairs().size(); i++) {
                DictPair dictPair1 = dexpr1.getPairs().get(i);
                DictPair dictPair2 = dexpr2.getPairs().get(i);
                if (!areStructurallySameExpression(dictPair1.getKey(), dictPair2.getKey())
                        || !areStructurallySameExpression(dictPair1.getValue(), dictPair2.getValue()))
                {
                    return false;
                }
            }

            return true;
        }

        if (expr1 instanceof ConstantExpression cexpr1 && expr2 instanceof ConstantExpression cexpr2) {
            return cexpr1.getConstant() == cexpr2.getConstant();
        }

        if (expr1 instanceof DiscVariableExpression dexpr1 && expr2 instanceof DiscVariableExpression dexpr2) {
            return dexpr1.getVariable() == dexpr2.getVariable();
        }

        if (expr1 instanceof AlgVariableExpression aexpr1 && expr2 instanceof AlgVariableExpression aexpr2) {
            return aexpr1.getVariable() == aexpr2.getVariable();
        }

        if (expr1 instanceof ContVariableExpression cexpr1 && expr2 instanceof ContVariableExpression cexpr2) {
            return cexpr1.getVariable() == cexpr2.getVariable() && cexpr1.isDerivative() == cexpr2.isDerivative();
        }

        if (expr1 instanceof TauExpression && expr2 instanceof TauExpression) {
            return true;
        }

        if (expr1 instanceof LocationExpression lexpr1 && expr2 instanceof LocationExpression lexpr2) {
            return lexpr1.getLocation() == lexpr2.getLocation();
        }

        if (expr1 instanceof EnumLiteralExpression elexpr1 && expr2 instanceof EnumLiteralExpression elexpr2) {
            return elexpr1.getLiteral() == elexpr2.getLiteral();
        }

        if (expr1 instanceof EventExpression eexpr1 && expr2 instanceof EventExpression eexpr2) {
            return eexpr1.getEvent() == eexpr2.getEvent();
        }

        if (expr1 instanceof FieldExpression fexpr1 && expr2 instanceof FieldExpression fexpr2) {
            return Objects.equals(fexpr1.getField().getName(), fexpr2.getField().getName());
        }

        if (expr1 instanceof StdLibFunctionExpression slfexpr1 && expr2 instanceof StdLibFunctionExpression slfexpr2) {
            return slfexpr1.getFunction() == slfexpr2.getFunction();
        }

        if (expr1 instanceof FunctionExpression fexpr1 && expr2 instanceof FunctionExpression fexpr2) {
            return fexpr1.getFunction() == fexpr2.getFunction();
        }

        if (expr1 instanceof InputVariableExpression ivexpr1 && expr2 instanceof InputVariableExpression ivexpr2) {
            return ivexpr1.getVariable() == ivexpr2.getVariable();
        }

        if (expr1 instanceof ComponentExpression cexpr1 && expr2 instanceof ComponentExpression cexpr2) {
            return cexpr1.getComponent() == cexpr2.getComponent();
        }

        if (expr1 instanceof CompParamExpression cexpr1 && expr2 instanceof CompParamExpression cexpr2) {
            return cexpr1.getParameter() == cexpr2.getParameter();
        }

        if (expr1 instanceof CompInstWrapExpression ciwexpr1 && expr2 instanceof CompInstWrapExpression ciwexpr2) {
            return ciwexpr1.getInstantiation() == ciwexpr2.getInstantiation()
                    && areStructurallySameExpression(ciwexpr1.getReference(), ciwexpr2.getReference());
        }

        if (expr1 instanceof CompParamWrapExpression cpwexpr1 && expr2 instanceof CompParamWrapExpression cpwexpr2) {
            return cpwexpr1.getParameter() == cpwexpr2.getParameter()
                    && areStructurallySameExpression(cpwexpr1.getReference(), cpwexpr2.getReference());
        }

        if (expr1 instanceof ReceivedExpression && expr2 instanceof ReceivedExpression) {
            return true;
        }

        if (expr1 instanceof SelfExpression && expr2 instanceof SelfExpression) {
            return CifScopeUtils.getScope(expr1).equals(CifScopeUtils.getScope(expr2));
        }

        throw new RuntimeException("Unexpected expressions: " + expr1 + ", " + expr2);
    }

    /**
     * Hashes the given expression.
     *
     * <p>
     * Component types and component definition types are currently not supported by this method.
     * </p>
     *
     * @param expr The expression.
     * @return The hash of the expression.
     * @see #areStructurallySameExpression
     * @see CifTypeUtils#hashType
     */
    public static int hashExpr(Expression expr) {
        if (expr instanceof BoolExpression bExpr) {
            return bExpr.isValue() ? 1231 : 1237; // Same as 'java.lang.Boolean.hashCode'.
        } else if (expr instanceof IntExpression iExpr) {
            return iExpr.getValue(); // Same as 'java.lang.Integer.hashCode'.
        } else if (expr instanceof RealExpression rExpr) {
            return rExpr.getValue().hashCode();
        } else if (expr instanceof StringExpression sExpr) {
            return sExpr.getValue().hashCode();
        } else if (expr instanceof TimeExpression) {
            return 1 << 0;
        } else if (expr instanceof CastExpression cExpr) {
            return hashType(cExpr.getType()) + hashExpr(cExpr.getChild());
        } else if (expr instanceof UnaryExpression uExpr) {
            return uExpr.getOperator().hashCode() + hashExpr(uExpr.getChild());
        } else if (expr instanceof BinaryExpression bExpr) {
            return hashExpr(bExpr.getLeft()) + bExpr.getOperator().hashCode() + hashExpr(bExpr.getRight());
        } else if (expr instanceof IfExpression iExpr) {
            int rslt = 1 << 3;
            for (Expression guard: iExpr.getGuards()) {
                rslt += hashExpr(guard);
            }
            rslt += hashExpr(iExpr.getThen());
            for (ElifExpression elifExpr: iExpr.getElifs()) {
                for (Expression guard: elifExpr.getGuards()) {
                    rslt += hashExpr(guard);
                }
                rslt += hashExpr(elifExpr.getThen());
            }
            rslt += hashExpr(iExpr.getElse());
            return rslt;
        } else if (expr instanceof SwitchExpression sExpr) {
            int rslt = 1 << 6;
            rslt += hashExpr(sExpr.getValue());
            for (SwitchCase sCase: sExpr.getCases()) {
                if (sCase.getKey() != null) {
                    rslt += hashExpr(sCase.getKey());
                }
                rslt += hashExpr(sCase.getValue());
            }
            return rslt;
        } else if (expr instanceof ProjectionExpression pExpr) {
            return hashExpr(pExpr.getChild()) + hashExpr(pExpr.getIndex());
        } else if (expr instanceof SliceExpression sExpr) {
            int rslt = hashExpr(sExpr.getChild());
            if (sExpr.getBegin() != null) {
                rslt += hashExpr(sExpr.getBegin());
            }
            if (sExpr.getEnd() != null) {
                rslt += hashExpr(sExpr.getEnd());
            }
            return rslt;
        } else if (expr instanceof FunctionCallExpression fcExpr) {
            int rslt = 1 << 9;
            rslt += hashExpr(fcExpr.getFunction());
            for (Expression argument: fcExpr.getArguments()) {
                rslt += hashExpr(argument);
            }
            return rslt;
        } else if (expr instanceof ListExpression lExpr) {
            int rslt = 1 << 12;
            for (Expression element: lExpr.getElements()) {
                rslt += hashExpr(element);
            }
            return rslt;
        } else if (expr instanceof SetExpression sExpr) {
            int rslt = 1 << 15;
            for (Expression element: sExpr.getElements()) {
                rslt += hashExpr(element);
            }
            return rslt;
        } else if (expr instanceof TupleExpression tExpr) {
            int rslt = 1 << 18;
            for (Expression field: tExpr.getFields()) {
                rslt += hashExpr(field);
            }
            return rslt;
        } else if (expr instanceof DictExpression dExpr) {
            int rslt = 1 << 21;
            for (DictPair pair: dExpr.getPairs()) {
                rslt += hashExpr(pair.getKey()) + hashExpr(pair.getValue());
            }
            return rslt;
        } else if (expr instanceof ConstantExpression cExpr) {
            return cExpr.getConstant().hashCode();
        } else if (expr instanceof DiscVariableExpression dvExpr) {
            return dvExpr.getVariable().hashCode();
        } else if (expr instanceof AlgVariableExpression aExpr) {
            return aExpr.getVariable().hashCode();
        } else if (expr instanceof ContVariableExpression cExpr) {
            return cExpr.getVariable().hashCode() + (cExpr.isDerivative() ? 1231 : 1237);
        } else if (expr instanceof TauExpression) {
            return 1 << 24;
        } else if (expr instanceof LocationExpression lExpr) {
            return lExpr.getLocation().hashCode();
        } else if (expr instanceof EnumLiteralExpression elExpr) {
            return elExpr.getLiteral().hashCode();
        } else if (expr instanceof EventExpression eExpr) {
            return eExpr.getEvent().hashCode();
        } else if (expr instanceof FieldExpression fExpr) {
            int rslt = 1 << 27;
            String fieldName = fExpr.getField().getName();
            if (fieldName != null) {
                rslt += fieldName.hashCode();
            }
            return rslt;
        } else if (expr instanceof StdLibFunctionExpression slfExpr) {
            return slfExpr.getFunction().hashCode();
        } else if (expr instanceof FunctionExpression fExpr) {
            return fExpr.getFunction().hashCode();
        } else if (expr instanceof InputVariableExpression iExpr) {
            return iExpr.getVariable().hashCode();
        } else if (expr instanceof ComponentExpression cExpr) {
            return cExpr.getComponent().hashCode();
        } else if (expr instanceof CompParamExpression cpExpr) {
            return cpExpr.getParameter().hashCode();
        } else if (expr instanceof CompInstWrapExpression ciwExpr) {
            return ciwExpr.getInstantiation().hashCode() + hashExpr(ciwExpr.getReference());
        } else if (expr instanceof CompParamWrapExpression cpwExpr) {
            return cpwExpr.getParameter().hashCode() + hashExpr(cpwExpr.getReference());
        } else if (expr instanceof ReceivedExpression) {
            return 1 << 30;
        } else if (expr instanceof SelfExpression) {
            return 1 << 31;
        }

        throw new RuntimeException("Unexpected expression: " + expr.toString());
    }

    /**
     * Combines expressions using the {@link BinaryOperator#CONJUNCTION} operator. Note that expressions are not deep
     * cloned, and as such, their containment/parent may change.
     *
     * <p>
     * For an empty collection of expressions, 'true' is returned. Otherwise, all top-level 'and' children are collected
     * in a depth-first, left-to-right manner, and a balanced binary conjunction tree is created. This results in trees
     * with the smallest depth, preventing recursion depth problems.
     * </p>
     *
     * <p>
     * The operation is not optimized for 'true' and 'false' literals.
     * </p>
     *
     * @param exprs The expressions to combine.
     * @return The combined expression.
     */
    public static Expression createConjunction(List<Expression> exprs) {
        // For the downside of balanced trees, and potential solutions,
        // see the 'createConjunction(List, boolean)' method.

        return createConjunction(exprs, false);
    }

    /**
     * Combines expressions using the {@link BinaryOperator#CONJUNCTION} operator. Note that expressions are not deep
     * cloned, and as such, their containment/parent may change.
     *
     * <p>
     * For an empty collection of expressions, 'true' is returned. Otherwise, all top-level 'and' children are collected
     * in a depth-first, left-to-right manner, and a balanced binary conjunction tree is created. This results in trees
     * with the smallest depth, preventing recursion depth problems.
     * </p>
     *
     * @param exprs The expressions to combine.
     * @param optimize Whether to optimize for 'true' and 'false' literals.
     * @return The combined expression.
     */
    public static Expression createConjunction(List<Expression> exprs, boolean optimize) {
        // A downside of balanced trees is that a lot of parenthesis are
        // needed when pretty printing them, to ensure they can be properly
        // parsed back to balanced trees. Potential solutions:
        //
        // 1) Using left-recursive trees:
        // This however re-introduces the stack depth problem. It would thus
        // also require non-recursive algorithms. However, this would be a
        // too great development burden.
        //
        // 2) Introduce n-ary expressions expressions:
        // Replace binary ones by n-ary ones, to ensure a single unique
        // representation. The n-ary expressions Allow a flat representation
        // without pretty printing issues. N-ary expressions add a bit of
        // complexity to the code over binary ones.
        //
        // 3) Rebalancing when needed:
        // Use balanced trees in the tooling, but omit the parentheses when
        // pretty printing, for commutative operators. Instead of
        // '(a or b) or (c or d)', simply 'a or b or c or d' is produced by
        // the pretty printer. When parsed, due to left-recursive parsing, a
        // left-recursive tree results from parsing. In the AST callbacks,
        // the tree will have to be rebalanced to a balanced tree, to avoid
        // problems in the tooling (stack depth issue). The downside here is
        // that pretty printing and parsing does not round trip, as a
        // non-balanced tree is pretty printed and parsed to a balanced tree.
        // That is, serialization changes the model. The meaning of the model,
        // and evaluation order of the predicates won't be affected, but it is
        // not necessarily the same tree as we had before serialization and
        // deserialization. Furthermore, depending how it is implemented,
        // rebalancing expression trees may be costly.

        // No children.
        if (exprs.isEmpty()) {
            return makeTrue();
        }

        // General case. Flatten children.
        BinaryOperator operator = BinaryOperator.CONJUNCTION;
        List<Expression> flattenedChildren = flattenBinExpr(exprs, operator);

        // Optimize for 'true' and 'false' literals.
        if (optimize) {
            List<Expression> children = listc(flattenedChildren.size());
            for (Expression child: flattenedChildren) {
                boolean add = true;
                if (child instanceof BoolExpression) {
                    boolean value = ((BoolExpression)child).isValue();

                    // false and X = false
                    if (!value) {
                        return child;
                    }

                    // true and X = X
                    add = false;
                }
                if (add) {
                    children.add(child);
                }
            }
            if (children.isEmpty()) {
                return makeTrue();
            }
            flattenedChildren = children;
        }

        // Create balanced binary tree.
        int treeSize = flattenedChildren.size();
        return createBalancedBinaryTree(flattenedChildren, 0, treeSize, operator);
    }

    /**
     * Combines expressions using the {@link BinaryOperator#DISJUNCTION} operator. Note that expressions are not deep
     * cloned, and as such, their containment/parent may change.
     *
     * <p>
     * For an empty collection of expressions, 'false' is returned. Otherwise, all top-level 'or' children are collected
     * in a depth-first, left-to-right manner, and a balanced binary disjunction tree is created. This results in trees
     * with the smallest depth, preventing recursion depth problems.
     * </p>
     *
     * <p>
     * The operation is not optimized for 'true' and 'false' literals.
     * </p>
     *
     * @param exprs The expressions to combine.
     * @return The combined expression.
     */
    public static Expression createDisjunction(List<Expression> exprs) {
        // For the downside of balanced trees, and potential solutions,
        // see the 'createConjunction(List, boolean)' method.

        return createDisjunction(exprs, false);
    }

    /**
     * Combines expressions using the {@link BinaryOperator#DISJUNCTION} operator. Note that expressions are not deep
     * cloned, and as such, their containment/parent may change.
     *
     * <p>
     * For an empty collection of expressions, 'false' is returned. Otherwise, all top-level 'or' children are collected
     * in a depth-first, left-to-right manner, and a balanced binary disjunction tree is created. This results in trees
     * with the smallest depth, preventing recursion depth problems.
     * </p>
     *
     * @param exprs The expressions to combine.
     * @param optimize Whether to optimize for 'true' and 'false' literals.
     * @return The combined expression.
     */
    public static Expression createDisjunction(List<Expression> exprs, boolean optimize) {
        // For the downside of balanced trees, and potential solutions,
        // see the 'createConjunction(List, boolean)' method.

        // No children.
        if (exprs.isEmpty()) {
            return makeFalse();
        }

        // General case. Flatten children.
        BinaryOperator operator = BinaryOperator.DISJUNCTION;
        List<Expression> flattenedChildren = flattenBinExpr(exprs, operator);

        // Optimize for 'true' and 'false' literals.
        if (optimize) {
            List<Expression> children = listc(flattenedChildren.size());
            for (Expression child: flattenedChildren) {
                boolean add = true;
                if (child instanceof BoolExpression) {
                    boolean value = ((BoolExpression)child).isValue();

                    // true or X = true
                    if (value) {
                        return child;
                    }

                    // false or X = X
                    add = false;
                }
                if (add) {
                    children.add(child);
                }
            }
            if (children.isEmpty()) {
                return makeFalse();
            }
            flattenedChildren = children;
        }

        // Create balanced binary tree.
        int treeSize = flattenedChildren.size();
        return createBalancedBinaryTree(flattenedChildren, 0, treeSize, operator);
    }

    /**
     * Flattens the given expressions, by recursively cutting of an binary expressions for the given binary operator.
     * For trees with the given operator, the children are returned in a depth-first, left-to-right manner.
     *
     * @param exprs The expressions.
     * @param operator The binary operator.
     * @return The flattened expressions.
     */
    public static List<Expression> flattenBinExpr(List<Expression> exprs, BinaryOperator operator) {
        // Optimize for no expressions.
        if (exprs.size() == 0) {
            return Collections.emptyList();
        }

        // Collect recursively all children of the requested operator, in a
        // depth-first, left-to-right manner.
        ArrayDeque<Expression> todos = new ArrayDeque<>(exprs);

        List<Expression> children = listc(todos.size());
        while (!todos.isEmpty()) {
            Expression todo = todos.pollFirst();
            if (todo instanceof BinaryExpression) {
                BinaryExpression btodo = (BinaryExpression)todo;
                if (btodo.getOperator() == operator) {
                    todos.addFirst(btodo.getRight());
                    todos.addFirst(btodo.getLeft());
                    continue;
                }
            }
            children.add(todo);
        }

        // Return flattened children of the requested operator.
        return children;
    }

    /**
     * Creates a balanced binary expression tree from the given expressions, using the given binary operator. If the
     * given expressions have top level binary expressions for the given operator, the result may not be properly
     * balanced.
     *
     * <p>
     * Top level calls must provide '0' for 'lower' and 'n' for 'upper', given that 'exprs' contains 'n' elements, with
     * 'n > 0'. It must always hold that 'lower < upper'.
     * </p>
     *
     * <p>
     * Note that expressions are not deep cloned, and as such, their containment/parent may change.
     * </p>
     *
     * @param exprs The expressions to combine using the given binary operator. Must not be an EMF {@link EList}.
     * @param lower The 0-based lower bound index (inclusive) of the range of expressions to consider.
     * @param upper The 0-based upper bound index (exclusive) of the range of expressions to consider.
     * @param operator The binary operator. Must be a binary operator that has boolean results.
     * @return The balanced binary expression tree.
     */
    public static Expression createBalancedBinaryTree(List<Expression> exprs, int lower, int upper,
            BinaryOperator operator)
    {
        // Leaf case.
        Assert.check(lower < upper);
        if (lower + 1 == upper) {
            return exprs.get(lower);
        }

        // General case. Construct balanced disjunction tree.

        // Get mid point index.
        int size = upper - lower;
        int left = size / 2;
        int mid = lower + left;

        // Create binary expression, with recursive left/right results.
        BinaryExpression rslt = newBinaryExpression();
        rslt.setOperator(operator);
        rslt.setType(newBoolType());
        rslt.setLeft(createBalancedBinaryTree(exprs, lower, mid, operator));
        rslt.setRight(createBalancedBinaryTree(exprs, mid, upper, operator));
        return rslt;
    }

    /**
     * Creates the inverse of the given expression. Note that the expression is not deep cloned, and as such, its
     * containment/parent may change.
     *
     * @param expr The expression for which to construct the inverse.
     * @return The inverse of the expression.
     */
    public static Expression makeInverse(Expression expr) {
        UnaryExpression rslt = newUnaryExpression();
        rslt.setOperator(UnaryOperator.INVERSE);
        rslt.setType(newBoolType());
        rslt.setChild(expr);
        return rslt;
    }

    /**
     * Returns a newly created {@link BoolExpression} with 'false' value, without position information.
     *
     * @return The 'false' literal.
     */
    public static BoolExpression makeFalse() {
        BoolExpression rslt = newBoolExpression();
        rslt.setValue(false);
        rslt.setType(newBoolType());
        return rslt;
    }

    /**
     * Returns a newly created {@link BoolExpression} with 'true' value, without position information.
     *
     * @return The 'true' literal.
     */
    public static BoolExpression makeTrue() {
        BoolExpression rslt = newBoolExpression();
        rslt.setValue(true);
        rslt.setType(newBoolType());
        return rslt;
    }

    /**
     * Returns a newly created expression, without position information, for the given integer value. This method
     * properly handles negative values, as well as {@link Integer#MIN_VALUE}.
     *
     * @param value The integer value.
     * @return The expression.
     */
    public static Expression makeInt(int value) {
        // Special case for -2,147,483,648, which we can't represent using
        // unary negation over a positive value, as 2,147,483,648 is out of the
        // integer range.
        if (value == Integer.MIN_VALUE) {
            // Create and return expression for '-2,147,483,647 - 1'.
            Expression left = makeInt(value + 1);
            Expression right = makeInt(1);

            IntType binType = newIntType();
            binType.setLower(value);
            binType.setUpper(value);

            BinaryExpression bin = newBinaryExpression();
            bin.setOperator(BinaryOperator.SUBTRACTION);
            bin.setLeft(left);
            bin.setRight(right);
            bin.setType(binType);

            return bin;
        }

        // General work.
        int absValue = Math.abs(value);

        IntType absType = newIntType();
        absType.setLower(absValue);
        absType.setUpper(absValue);

        IntExpression absExpr = newIntExpression();
        absExpr.setValue(absValue);
        absExpr.setType(absType);

        if (value >= 0) {
            return absExpr;
        }

        // Additional work for negative values.
        IntType unType = newIntType();
        unType.setLower(value);
        unType.setUpper(value);

        UnaryExpression un = newUnaryExpression();
        un.setOperator(UnaryOperator.NEGATE);
        un.setChild(absExpr);
        un.setType(unType);

        return un;
    }

    /**
     * Returns a newly created expression, without position information, for the given real value. This method properly
     * handles negative values.
     *
     * @param value The finite Java double value.
     * @return The expression.
     */
    public static Expression makeReal(double value) {
        // Check preconditions.
        Assert.check(Double.isFinite(value));

        // General work.
        double absValue = Math.abs(value);
        RealExpression absExpr = newRealExpression();
        absExpr.setValue(CifMath.realToStr(absValue));
        absExpr.setType(newRealType());

        // If value is non-negative, we are done.
        if (value >= 0) {
            return absExpr;
        }

        // Additional work for negative values.
        UnaryExpression un = newUnaryExpression();
        un.setOperator(UnaryOperator.NEGATE);
        un.setChild(absExpr);
        un.setType(newRealType());
        return un;
    }

    /**
     * Creates a tuple expression for the given elements, if needed.
     *
     * <p>
     * The elements are not deep cloned, so there containment may change as a result of using this method.
     * </p>
     *
     * @param elements The elements. Must have at least one element.
     * @param position The position used for newly created expressions and types. The position itself is not used, only
     *     clones are used. May be {@code null} to not set position information.
     * @return If there is only one element, that element, or a tuple with the given elements otherwise.
     */
    public static Expression makeTuple(List<Expression> elements, Position position) {
        Assert.check(!elements.isEmpty());

        if (elements.size() == 1) {
            return first(elements);
        }

        TupleType tupleType = newTupleType();
        tupleType.setPosition(copyPosition(position));
        for (Expression element: elements) {
            Field field = newField();
            field.setPosition(copyPosition(position));
            field.setType(deepclone(element.getType()));
            tupleType.getFields().add(field);
        }

        TupleExpression tuple = newTupleExpression();
        tuple.setPosition(copyPosition(position));
        tuple.getFields().addAll(elements);
        tuple.setType(tupleType);
        return tuple;
    }

    /**
     * Returns the field being projected in a tuple projection.
     *
     * @param pexpr The tuple projection expression.
     * @return The field being projected.
     */
    public static Field getTupleProjField(ProjectionExpression pexpr) {
        Expression iexpr = pexpr.getIndex();
        if (iexpr instanceof FieldExpression) {
            return ((FieldExpression)iexpr).getField();
        } else {
            // Evaluate index.
            int idx;
            try {
                idx = (Integer)CifEvalUtils.eval(iexpr, false);
            } catch (CifEvalException e) {
                // This expression should be statically evaluable, as checked by the type checker.
                throw new RuntimeException(e);
            }

            // Get field.
            CifType ctype = normalizeType(pexpr.getChild().getType());
            return ((TupleType)ctype).getFields().get(idx);
        }
    }

    /**
     * Returns the index of the field being projected in a tuple projection.
     *
     * @param pexpr The tuple projection expression.
     * @return The index of the field being projected.
     */
    public static int getTupleProjIndex(ProjectionExpression pexpr) {
        TupleType tupType = (TupleType)normalizeType(pexpr.getChild().getType());
        Expression indexExpr = pexpr.getIndex();

        if (indexExpr instanceof FieldExpression fe) {
            return tupType.getFields().indexOf(fe.getField());
        } else {
            // Evaluate index.
            try {
                return (int)CifEvalUtils.eval(indexExpr, false);
            } catch (CifEvalException e) {
                // This expression should be statically evaluable, as checked by the type checker.
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns the possible number of unique values for the given type.
     *
     * <p>
     * This method supports type references, and wrapping types. It does not support non-value types, such as component
     * (definition) types, and void types.
     * </p>
     *
     * @param type The CIF type.
     * @return The possible number of values. May be {@link Double#POSITIVE_INFINITY}.
     * @see #getPossibleValues
     */
    public static double getPossibleValueCount(CifType type) {
        // Normalize type.
        type = normalizeType(type);

        // Simple types.
        if (type instanceof BoolType) {
            return 2;
        }
        if (type instanceof RealType) {
            return Double.POSITIVE_INFINITY;
        }
        if (type instanceof StringType) {
            return Double.POSITIVE_INFINITY;
        }

        if (type instanceof EnumType) {
            EnumDecl enumDecl = ((EnumType)type).getEnum();
            return enumDecl.getLiterals().size();
        }

        if (type instanceof IntType) {
            IntType itype = (IntType)type;
            int lower = CifTypeUtils.getLowerBound(itype);
            int upper = CifTypeUtils.getUpperBound(itype);
            return (double)upper - (double)lower + 1;
        }

        // Container types.
        if (type instanceof SetType) {
            SetType stype = (SetType)type;
            double ecnt = getPossibleValueCount(stype.getElementType());
            return Math.pow(2, ecnt);
        }

        if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            double kcnt = getPossibleValueCount(dtype.getKeyType());
            double vcnt = getPossibleValueCount(dtype.getValueType());
            return Math.pow(vcnt + 1, kcnt);
        }

        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            double ecnt = getPossibleValueCount(ltype.getElementType());
            int lower = CifTypeUtils.getLowerBound(ltype);
            int upper = CifTypeUtils.getUpperBound(ltype);
            if (lower == upper) {
                // Array.
                return Math.pow(ecnt, lower);
            } else {
                // Variable length list.

                if (Double.isInfinite(ecnt)) {
                    return ecnt;
                }

                // For 'list[l..u] t' with 'x' possible values for 't':
                // - x^l + x^(l+1) + x^(l+2) + ... + x^u
                // - x^0 + x^1 + x^2 + ... + x^u = (1 - x^(u+1)) / (1 - x) if u != 1 = u + 1 if u = 1
                // - x^l + x^(l+1) + x^(l+2) + ... + x^u = x^0 + x^1 + ... + x^(l-1) + x^l + x^(l+1) + ... + x^u
                double cnt = expSum(ecnt, (double)upper + 1);
                if (lower > 0) {
                    cnt -= expSum(ecnt, lower);
                }
                return cnt;
            }
        }

        if (type instanceof TupleType) {
            double cnt = 1;
            TupleType ttype = (TupleType)type;
            for (Field field: ttype.getFields()) {
                cnt *= getPossibleValueCount(field.getType());
            }
            return cnt;
        }

        if (type instanceof FuncType) {
            return Double.POSITIVE_INFINITY;
        }
        if (type instanceof DistType) {
            return Double.POSITIVE_INFINITY;
        }

        // ComponentDefType
        // ComponentType
        // VoidType
        throw new RuntimeException("Unexpected type: " + type);
    }

    /**
     * Computes the sum of exponents, i.e 'n^0 + n^1 + n^2 + ... + n^(m-1)'.
     *
     * <p>
     * This method is only meant to be used internally by {@link #getPossibleValueCount}, but is made public to allow
     * unit testing.
     * </p>
     *
     * @param n The base of the exponents. Must not be negative.
     * @param m The number of exponents to sum. Must not be negative.
     * @return The computed result.
     */
    public static double expSum(double n, double m) {
        Assert.check(n >= 0 || Double.isInfinite(n));
        Assert.check(m >= 0 || Double.isInfinite(m));
        return (n == 1) ? m : (1 - Math.pow(n, m)) / (1 - n);
    }

    /**
     * Returns the possible values for a given type.
     *
     * <p>
     * This method supports type references, and wrapping types. It does not support non-value types, such as component
     * (definition) types, and void types.
     * </p>
     *
     * <p>
     * All the returned values are different from all the other returned values. That is, only unique values are
     * returned.
     * </p>
     *
     * @param type The type.
     * @return The possible values.
     * @see #getPossibleValueCount
     */
    public static List<Expression> getPossibleValues(CifType type) {
        // Normalize type.
        type = normalizeType(type);

        // Simple types.
        if (type instanceof BoolType) {
            return list(makeFalse(), makeTrue());
        } else if (type instanceof EnumType) {
            EnumDecl enumDecl = ((EnumType)type).getEnum();
            List<EnumLiteral> lits = enumDecl.getLiterals();
            List<Expression> values = listc(lits.size());
            for (EnumLiteral lit: lits) {
                EnumLiteralExpression litRef = newEnumLiteralExpression();
                litRef.setLiteral(lit);
                litRef.setType(deepclone(type));
                values.add(litRef);
            }
            return values;
        } else if (type instanceof IntType) {
            IntType itype = (IntType)type;
            int lower = CifTypeUtils.getLowerBound(itype);
            int upper = CifTypeUtils.getUpperBound(itype);
            long cnt = (long)upper - (long)lower + 1;
            List<Expression> values = listc((int)cnt);
            for (int i = lower; i <= upper; i++) {
                values.add(makeInt(i));
            }
            return values;
        }

        // Container types.
        if (type instanceof ListType) {
            // Get possible values for the element type.
            ListType ltype = (ListType)type;
            List<Expression> elemValues = getPossibleValues(ltype.getElementType());

            // Get possible lengths.
            int lower = CifTypeUtils.getLowerBound(ltype);
            int upper = CifTypeUtils.getUpperBound(ltype);

            // Initialize resulting lists.
            List<Expression> lists = listc((int)getPossibleValueCount(type));

            // Add list without elements.
            if (lower == 0) {
                ListExpression emptyList = newListExpression();
                emptyList.setType(deepclone(type));
                lists.add(emptyList);
            }

            // Get possible values per length.
            List<List<Expression>> elemsValues = listc(upper);
            for (int len = lower; len <= upper; len++) {
                // Fill with for each element, the possible values.
                while (elemsValues.size() < len) {
                    elemsValues.add(elemValues);
                }

                // Create possible array values, by creating all possible
                // combinations of values for all elements.
                ListProductIterator<Expression> iter = new ListProductIterator<>(elemsValues);
                while (iter.hasNext()) {
                    List<Expression> elems = iter.next();
                    ListExpression array = newListExpression();
                    for (Expression elem: elems) {
                        array.getElements().add(deepclone(elem));
                    }
                    array.setType(deepclone(ltype));
                    lists.add(array);
                }
            }
            return lists;
        } else if (type instanceof SetType) {
            // Get possible values for the element type.
            SetType stype = (SetType)type;
            List<Expression> elemValues = getPossibleValues(stype.getElementType());

            // Construct power set for element values.
            List<List<Expression>> powerset = powerSet(elemValues);
            List<Expression> sets = listc(powerset.size());
            for (List<Expression> elems: powerset) {
                sets.add(newSetExpression(elems, null, deepclone(type)));
            }
            return sets;
        } else if (type instanceof DictType) {
            // Get possible values for the key and element types.
            DictType dtype = (DictType)type;
            List<Expression> keyValues = getPossibleValues(dtype.getKeyType());
            List<Expression> valueValues = getPossibleValues(dtype.getValueType());

            // Construct power set for key values.
            List<List<Expression>> powerset = powerSet(keyValues);

            // Initialize resulting dictionaries.
            List<Expression> dicts = listc((int)getPossibleValueCount(type));

            // Add dictionary without keys.
            DictExpression emptyDict = newDictExpression();
            emptyDict.setType(deepclone(type));
            dicts.add(emptyDict);

            // Add dictionaries per key/value pair.
            for (List<Expression> keys: powerset) {
                // Skip if empty keys.
                if (keys.isEmpty()) {
                    continue;
                }

                // Construct list with possible values per key.
                List<List<Expression>> combis = listc(keys.size());
                for (int i = 0; i < keys.size(); i++) {
                    combis.add(deepclone(valueValues));
                }

                // Add dictionaries for all different value combinations.
                ListProductIterator<Expression> iter = new ListProductIterator<>(combis);
                while (iter.hasNext()) {
                    // Construct dictionary.
                    DictExpression singleDict = newDictExpression();
                    singleDict.setType(deepclone(type));

                    // Add key/value pairs.
                    List<Expression> values = iter.next();
                    for (int i = 0; i < keys.size(); i++) {
                        Expression key = deepclone(keys.get(i));
                        Expression value = deepclone(values.get(i));
                        DictPair pair = newDictPair(key, null, value);
                        singleDict.getPairs().add(pair);
                    }

                    // Add dictionary.
                    dicts.add(singleDict);
                }
            }

            // Return dictionaries.
            return dicts;
        } else if (type instanceof TupleType) {
            // Get possible values per field, and count total number of
            // possible tuple values.
            TupleType ttype = (TupleType)type;
            List<List<Expression>> fieldsValues = listc(ttype.getFields().size());
            int cnt = 1;
            for (Field field: ttype.getFields()) {
                List<Expression> fieldValues = getPossibleValues(field.getType());
                fieldsValues.add(fieldValues);
                cnt *= fieldValues.size();
            }

            // Create possible tuple values, by creating all possible
            // combinations of values for all fields.
            List<Expression> values = listc(cnt);
            ListProductIterator<Expression> iter = new ListProductIterator<>(fieldsValues);
            while (iter.hasNext()) {
                List<Expression> elems = iter.next();
                values.add(makeTuple(deepclone(elems), null));
            }
            return values;
        }

        // Unsupported/infinite type.
        throw new RuntimeException("Unexpected type: " + type);
    }

    /**
     * Construct power set from a set of expressions.
     *
     * @param set The set, as a list of unique elements.
     * @return The power set, as lists of unique lists of unique elements.
     */
    private static List<List<Expression>> powerSet(List<Expression> set) {
        // Initialize power set.
        int cnt = (int)Math.pow(2, set.size());
        List<List<Expression>> powerset = listc(cnt);

        // Add all elements of the power set, and return it.
        BitSet combinations = new BitSet(set.size());
        do {
            List<Expression> singleSet = listc(combinations.cardinality());
            for (int i = 0; i < set.size(); i++) {
                if (combinations.get(i)) {
                    singleSet.add(deepclone(set.get(i)));
                }
            }
            powerset.add(singleSet);
        } while (incBitSet(combinations, set.size()));

        return powerset;
    }

    /**
     * Increments the non-negative integer value represented by the bitset, by one.
     *
     * @param bitset The bitset. Is modified in-place.
     * @param bitCnt The number of bits in the bit set.
     * @return Whether the bitset was incremented ({@code true}) or was reset to zero due to overflow ({@code false}).
     */
    private static boolean incBitSet(BitSet bitset, int bitCnt) {
        for (int i = 0; i < bitCnt; i++) {
            boolean b = bitset.get(i);
            bitset.set(i, !b);
            if (!b) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is the given expression a literal expression.
     *
     * <p>
     * An expression is essentially considered to be a literal expression if {@link CifEvalUtils#evalAsExpr} would
     * evaluate it to an expression that is structurally identical to the original expression, except that new
     * expression objects are created for it.
     * </p>
     *
     * <p>
     * However, whether an expression is a literal, is determined statically, for performance reasons. Concretely, the
     * following expressions are considered literals:
     * <ul>
     * <li>Boolean literals ({@link BoolExpression}).</li>
     * <li>Integer literals ({@link IntExpression}).</li>
     * <li>Negations ({@link UnaryExpression} with a {@link UnaryOperator#NEGATE} operator) of non-negative integer
     * literals ({@link IntExpression} with a non-negative {@link IntExpression#getValue() value}). For instance, in the
     * CIF ASCII syntax, '-5' is parsed as a negation over integer literal '5'. Note that
     * {@link CifEvalUtils#valueToExpr} would create a {@link IntExpression} with a negative value, and as such this
     * case is not 'fully simplified'.</li>
     * <li>Negations ({@link UnaryExpression} with a {@link UnaryOperator#NEGATE} operator) of non-negative real
     * literals ({@link RealExpression}). For instance, in the CIF ASCII syntax, '-5.0' is parsed as a negation over
     * real literal '5.0'. Note that {@link CifEvalUtils#valueToExpr} would create a new {@link RealExpression} with an
     * evaluated/normalized non-negative value, and as such this case is not 'fully simplified'.</li>
     * <li>Real literals ({@link RealExpression}). Note that {@link CifEvalUtils#valueToExpr} would create a new
     * {@link RealExpression} with an evaluated/normalized value, and as such this case is not 'fully simplified'.</li>
     * <li>String literals ({@link StringExpression}).</li>
     * <li>Enumeration literals ({@link EnumLiteralExpression}).</li>
     * <li>Function reference literals ({@link FunctionExpression}).</li>
     * <li>List literals ({@link ListExpression}) with literal elements.</li>
     * <li>Set literals ({@link SetExpression}) with literal elements. Note that {@link CifEvalUtils#evalAsExpr} would
     * evaluate the set and duplicate elements would be removed. Since this method performs static checks, sets may not
     * be 'fully simplified'.</li>
     * <li>Dictionary literals ({@link DictExpression}) with literal keys and values. Note that
     * {@link CifEvalUtils#evalAsExpr} would evaluate the dictionary and duplicate keys would be removed. Since this
     * method performs static checks, dictionaries may not be 'fully simplified'.</li>
     * <li>Tuple literals ({@link TupleExpression}) with literal fields.</li>
     * </ul>
     * </p>
     *
     * @param expr The expression.
     * @return {@code true} if the expression is a literal expression, {@code false} otherwise.
     */
    public static boolean isLiteralExpr(Expression expr) {
        // Simple literals.
        if (expr instanceof BoolExpression) {
            return true;
        }
        if (expr instanceof IntExpression) {
            return true;
        }
        if (expr instanceof RealExpression) {
            return true;
        }
        if (expr instanceof StringExpression) {
            return true;
        }
        if (expr instanceof EnumLiteralExpression) {
            return true;
        }
        if (expr instanceof FunctionExpression) {
            return true;
        }

        // Container literals.
        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                if (!isLiteralExpr(elem)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                if (!isLiteralExpr(elem)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                if (!isLiteralExpr(pair.getKey())) {
                    return false;
                }
                if (!isLiteralExpr(pair.getValue())) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression field: texpr.getFields()) {
                if (!isLiteralExpr(field)) {
                    return false;
                }
            }
            return true;
        }

        // Special case: negative numbers.
        if (expr instanceof UnaryExpression) {
            UnaryExpression uexpr = (UnaryExpression)expr;
            if (uexpr.getOperator() == UnaryOperator.NEGATE) {
                Expression child = uexpr.getChild();

                // In the CIF ASCII syntax, '-5' is parsed as a unary
                // numerical negation over an integer literal '5'. However,
                // CifEvalUtils.valueToExpr creates integer literals for
                // negative values. As such, this is not 'simplified' fully.
                if (child instanceof IntExpression) {
                    return ((IntExpression)child).getValue() >= 0;
                }

                // In the CIF ASCII syntax, '-5.0' is parsed as a unary
                // numerical negation over a real literal '5.0'.
                if (child instanceof RealExpression) {
                    return true;
                }
            }
        }

        // Other expressions. This might include some other literal values that
        // we could potentially handle, but we don't.
        return false;
    }
}
