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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.cif.common.CifEvalUtils.evalPreds;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.exprsToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.functionToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.getComponentText1;
import static org.eclipse.escet.cif.common.CifTextUtils.getLocationText1;
import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.isArrayType;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Strings;

/**
 * CIF code generator precondition checker. Does not support component definition/instantiation.
 *
 * <p>
 * Derived classes may inherit to add additional preconditions.
 * </p>
 */
public class CodeGenPreChecker extends CifWalker {
    /** Precondition violations found so far. */
    protected List<String> problems = list();

    /** The number of automata encountered. */
    protected int autCount;

    /**
     * The number of initial locations found for the automaton being checked. Only valid while checking an automaton. Is
     * set to {@code -1} to disable this check due to evaluation errors in initialization predicates.
     */
    protected int initLocCount;

    /**
     * Checks the CIF specification to make sure it satisfies the preconditions for the code generator.
     *
     * @param spec The CIF specification to check.
     * @throws UnsupportedException If a precondition is violated.
     */
    public void check(Specification spec) {
        // Find precondition violations.
        walkSpecification(spec);

        // If we have any problems, the specification is unsupported.
        Collections.sort(problems, Strings.SORTER);
        if (!problems.isEmpty()) {
            String msg = "CIF code generator failed due to unsatisfied preconditions:\n - "
                    + String.join("\n - ", problems);
            throw new UnsupportedException(msg);
        }
    }

    @Override
    protected void postprocessSpecification(Specification spec) {
        if (autCount == 0) {
            String msg = "Unsupported specification: specifications without automata are currently not supported.";
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Initialization.
        if (!CifValueUtils.isTriviallyTrue(comp.getInitials(), true, true)) {
            String msg = fmt("Unsupported %s: initialization predicates in components are currently not supported.",
                    getComponentText1(comp));
            problems.add(msg);
        }

        // State invariants, as state/event exclusion invariants are eliminated.
        List<Expression> invPreds = listc(comp.getInvariants().size());
        for (Invariant inv: comp.getInvariants()) {
            invPreds.add(inv.getPredicate());
        }
        if (!CifValueUtils.isTriviallyTrue(invPreds, false, true)) {
            String msg = fmt("Unsupported %s: state invariants in components are currently not supported.",
                    getComponentText1(comp));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable var) {
        // Skip all but discrete variables declared in components.
        EObject parent = var.eContainer();
        if (!(parent instanceof ComplexComponent)) {
            return;
        }

        // Check for no multiple initial values.
        VariableValue values = var.getValue();
        if (values != null && values.getValues().size() != 1) {
            String msg = fmt(
                    "Unsupported discrete variable \"%s\": the variable has multiple potential initial values.",
                    getAbsName(var));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessExternalFunction(ExternalFunction func) {
        String msg = fmt("Unsupported function \"%s\": external user-defined functions are currently not supported.",
                getAbsName(func));
        problems.add(msg);
    }

    @Override
    protected void preprocessLocation(Location loc) {
        // Urgency.
        if (loc.isUrgent()) {
            String msg = fmt("Unsupported %s: urgent locations are currently not supported.", getLocationText1(loc));
            problems.add(msg);
        }

        // State invariants, as state/event exclusion invariants are eliminated.
        List<Expression> invPreds = listc(loc.getInvariants().size());
        for (Invariant inv: loc.getInvariants()) {
            invPreds.add(inv.getPredicate());
        }
        if (!CifValueUtils.isTriviallyTrue(invPreds, false, true)) {
            String msg = fmt("Unsupported %s: state invariants in locations are currently not supported.",
                    getLocationText1(loc));
            problems.add(msg);
        }

        // Initialization.
        boolean initial = false;
        try {
            initial = loc.getInitials().isEmpty() ? false : evalPreds(loc.getInitials(), true, true);
        } catch (CifEvalException e) {
            // Can only fail if there is at least one predicate.
            String msg = fmt("Failed to (statically) evaluate initialization predicate(s): %s.",
                    exprsToStr(loc.getInitials()));
            problems.add(msg);

            // Disable initial location count checking.
            initLocCount = -1;
        }

        if (initial && initLocCount != -1) {
            initLocCount++;
        }
    }

    @Override
    protected void preprocessEdge(Edge edge) {
        // Urgency.
        if (edge.isUrgent()) {
            Location loc = (Location)edge.eContainer();
            String msg = fmt("Unsupported %s: urgent edges are currently not supported.", getLocationText1(loc));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton aut) {
        // Reset initial locations counter.
        initLocCount = 0;

        // One more automaton encountered.
        autCount++;
    }

    @Override
    protected void postprocessAutomaton(Automaton aut) {
        // Exactly one initial location.
        if (initLocCount == 0) {
            String msg = fmt("Unsupported automaton \"%s\": automata without an initial location are currently "
                    + "not supported.", getAbsName(aut));
            problems.add(msg);
        }

        if (initLocCount > 1) {
            String msg = fmt("Unsupported automaton \"%s\": automata with multiple initial locations are "
                    + "currently not supported.", getAbsName(aut));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessDictType(DictType type) {
        String msg = fmt("Unsupported type \"%s\": dictionary types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessDistType(DistType type) {
        String msg = fmt("Unsupported type \"%s\": distribution types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessFuncType(FuncType type) {
        // We support functions directly in function calls, but not as
        // data, to be passed around, stored in variables, etc. Note that
        // types of expressions are skipped, so the function type of a function
        // reference on which a call is performed directly, is allowed.
        String msg = fmt("Unsupported type \"%s\": function types are currently not supported. That is, calling "
                + "functions is supported, but using them as data is not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessListType(ListType type) {
        // We support arrays.
        if (isArrayType(type)) {
            return;
        }

        // All other list types are not supported.
        String msg = fmt("Unsupported type \"%s\": non-array list types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessSetType(SetType type) {
        String msg = fmt("Unsupported type \"%s\": set types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void walkCifType(CifType type) {
        // Skip checking types of expressions, as the expressions are already
        // checked, and we don't transform the types of expressions.
        EObject parent = type.eContainer();
        if (parent instanceof Expression) {
            return;
        }

        // Not a type of an expression.
        super.walkCifType(type);
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression expr) {
        // Check supported.
        UnaryOperator op = expr.getOperator();
        switch (op) {
            case INVERSE:
            case NEGATE:
            case PLUS:
                return;

            case SAMPLE:
                break;
        }

        // Unsupported.
        String msg = fmt("Unsupported expression \"%s\": unary operator \"%s\" is currently not supported.",
                exprToStr(expr), operatorToStr(op));
        problems.add(msg);
    }

    @Override
    protected void preprocessBinaryExpression(BinaryExpression expr) {
        // Check supported.
        BinaryOperator op = expr.getOperator();
        switch (op) {
            case IMPLICATION:
            case BI_CONDITIONAL:
            case LESS_THAN:
            case LESS_EQUAL:
            case GREATER_THAN:
            case GREATER_EQUAL:
            case MULTIPLICATION:
            case DIVISION:
            case INTEGER_DIVISION:
            case MODULUS:
                return;

            case DISJUNCTION:
            case CONJUNCTION: {
                CifType ltype = normalizeType(expr.getLeft().getType());
                if (ltype instanceof BoolType) {
                    return;
                }
                break;
            }

            case EQUAL:
            case UNEQUAL: {
                CifType ltype = normalizeType(expr.getLeft().getType());
                if (ltype instanceof BoolType || ltype instanceof IntType || ltype instanceof RealType
                        || ltype instanceof EnumType || ltype instanceof StringType)
                {
                    return;
                }
                break;
            }

            case ADDITION: {
                CifType ltype = normalizeType(expr.getLeft().getType());
                if (ltype instanceof IntType || ltype instanceof RealType || ltype instanceof StringType) {
                    return;
                }
                break;
            }

            case SUBTRACTION: {
                CifType ltype = normalizeType(expr.getLeft().getType());
                if (ltype instanceof IntType || ltype instanceof RealType) {
                    return;
                }
                break;
            }

            case ELEMENT_OF:
            case SUBSET:
                break;
        }

        // Unsupported.
        String msg = fmt("Unsupported expression \"%s\": binary operator \"%s\" is currently not supported, "
                + "or is not supported for the operands that are used.", exprToStr(expr), operatorToStr(op));
        problems.add(msg);
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression expr) {
        // Check supported.
        CifType ctype = normalizeType(expr.getChild().getType());
        if (ctype instanceof TupleType) {
            return;
        } else if (ctype instanceof StringType) {
            return;
        } else if (ctype instanceof ListType && isArrayType((ListType)ctype)) {
            return;
        }

        // Unsupported.
        String msg = fmt("Unsupported expression \"%s\": projections on anything other than tuples, arrays, and "
                + "strings is currently not supported.", exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessSliceExpression(SliceExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": slicing is currently not supported.", exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessFunctionCallExpression(FunctionCallExpression expr) {
        // Check supported.
        Expression fexpr = expr.getFunction();
        if (fexpr instanceof FunctionExpression) {
            return;
        } else if (fexpr instanceof StdLibFunctionExpression) {
            // Check supported stdlib.
            StdLibFunctionExpression lExpr = (StdLibFunctionExpression)fexpr;
            StdLibFunction stdlib = lExpr.getFunction();
            switch (stdlib) {
                // Supported.
                case ABS:
                case CBRT:
                case CEIL:
                case EXP:
                case FLOOR:
                case FORMAT:
                case LN:
                case LOG:
                case MAXIMUM:
                case MINIMUM:
                case POWER:
                case ROUND:
                case SCALE:
                case SIGN:
                case SQRT:
                case ACOS:
                case ASIN:
                case ATAN:
                case COS:
                case SIN:
                case TAN:
                    return;

                // Conditionally supported.
                case EMPTY: {
                    CifType ptype = normalizeType(expr.getParams().get(0).getType());
                    if (ptype instanceof ListType && isArrayType((ListType)ptype)) {
                        return;
                    }
                    break;
                }

                case SIZE: {
                    CifType ptype = normalizeType(expr.getParams().get(0).getType());
                    if (ptype instanceof StringType) {
                        return;
                    } else if (ptype instanceof ListType && isArrayType((ListType)ptype)) {
                        return;
                    }
                    break;
                }

                // Unsupported.
                case DELETE:
                case POP:
                case ACOSH:
                case ASINH:
                case ATANH:
                case COSH:
                case SINH:
                case TANH:
                    break;

                // Distributions (unsupported).
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
                    break;
            }

            // Unsupported stdlib.
            String msg = fmt(
                    "Unsupported expression \"%s\": standard  library function \"%s\" is currently not "
                            + "supported, or is not supported for the arguments that are used.",
                    exprToStr(expr), functionToStr(stdlib));
            problems.add(msg);
            return;
        }

        // Unsupported function call.
        String msg = fmt(
                "Unsupported expression \"%s\": function calls on anything other than standard library functions "
                        + "and internal user-defined functions is currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessSetExpression(SetExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": sets are currently not supported.", exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessDictExpression(DictExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": dictionaries are currently not supported.", exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessFunctionExpression(FunctionExpression expr) {
        // Function references used as the function to call in function call
        // expressions are allowed. All other uses of function references
        // (functions as values), are not supported.
        if (expr.eContainer() instanceof FunctionCallExpression) {
            FunctionCallExpression fcexpr = (FunctionCallExpression)expr.eContainer();
            if (fcexpr.getFunction() == expr) {
                return;
            }
        }

        String msg = fmt("Unsupported expression \"%s\": the use of functions as values is currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessPrint(Print print) {
        if (print.getTxtPre() != null && print.getWhenPost() != null) {
            String msg = "Unsupported print declaration: print declarations with pre/source state text and "
                    + "post/target state filtering (\"when\") are currently not supported.";
            problems.add(msg);
        }

        if (print.getTxtPost() != null && print.getWhenPre() != null) {
            String msg = "Unsupported print declaration: print declarations with post/target state text and "
                    + "pre/source state filtering (\"when\") are currently not supported.";
            problems.add(msg);
        }
    }
}
