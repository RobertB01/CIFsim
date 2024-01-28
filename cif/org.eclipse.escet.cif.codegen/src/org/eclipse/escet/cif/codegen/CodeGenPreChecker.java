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

package org.eclipse.escet.cif.codegen;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;

/**
 * CIF code generator precondition checker. Does not support component definition/instantiation.
 *
 * <p>
 * Derived classes may inherit to add additional preconditions.
 * </p>
 */
public class CodeGenPreChecker extends CifPreconditionChecker {
    /** Constructor for the {@link CodeGenPreChecker} class. */
    public CodeGenPreChecker() {
        super(
                // Specifications without automata are not supported.
                new SpecAutomataCountsCheck().setMinMaxAuts(1, Integer.MAX_VALUE),

                // Initialization predicates in components are not supported, except if it can be determined statically
                // that they are trivially true.
                new CompNoInitPredsCheck(true),

                // State invariants (in components as well as locations) are not supported, except if it can be
                // determined statically that they are trivially true.
                new InvNoSpecificInvsCheck() //
                        .ignoreNeverBlockingInvariants() //
                        .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.STATE, NoInvariantPlaceKind.ALL_PLACES),

                // Discrete variables with multiple initial values (including any) are not supported.
                new VarNoDiscWithMultiInitValuesCheck(),

                // External user-defined functions are not supported.
                new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.EXTERNAL),

                // Urgent locations are not supported.
                new LocNoUrgentCheck()

        //
        );
    }

//    /** Precondition violations found so far. */
//    protected List<String> problems = list();
//
//    /**
//     * The number of initial locations found for the automaton being checked. Only valid while checking an automaton. Is
//     * set to {@code -1} to disable this check due to evaluation errors in initialization predicates.
//     */
//    protected int initLocCount;
//
//    /**
//     * Checks the CIF specification to make sure it satisfies the preconditions for the code generator.
//     *
//     * @param spec The CIF specification to check.
//     * @throws UnsupportedException If a precondition is violated.
//     */
//    public void check(Specification spec) {
//        // Find precondition violations.
//        walkSpecification(spec);
//
//        // If we have any problems, the specification is unsupported.
//        Collections.sort(problems, Strings.SORTER);
//        if (!problems.isEmpty()) {
//            String msg = "CIF code generator failed due to unsatisfied preconditions:\n - "
//                    + String.join("\n - ", problems);
//            throw new UnsupportedException(msg);
//        }
//    }
//
//    @Override
//    protected void preprocessLocation(Location loc) {
//        // Initialization.
//        boolean initial = false;
//        try {
//            initial = loc.getInitials().isEmpty() ? false : evalPreds(loc.getInitials(), true, true);
//        } catch (CifEvalException e) {
//            // Can only fail if there is at least one predicate.
//            String msg = fmt("Failed to (statically) evaluate initialization predicate(s): %s.",
//                    exprsToStr(loc.getInitials()));
//            problems.add(msg);
//
//            // Disable initial location count checking.
//            initLocCount = -1;
//        }
//
//        if (initial && initLocCount != -1) {
//            initLocCount++;
//        }
//    }
//
//    @Override
//    protected void preprocessEdge(Edge edge) {
//        // Urgency.
//        if (edge.isUrgent()) {
//            Location loc = (Location)edge.eContainer();
//            String msg = fmt("Unsupported %s: urgent edges are currently not supported.", getLocationText1(loc));
//            problems.add(msg);
//        }
//    }
//
//    @Override
//    protected void preprocessAutomaton(Automaton aut) {
//        // Reset initial locations counter.
//        initLocCount = 0;
//    }
//
//    @Override
//    protected void postprocessAutomaton(Automaton aut) {
//        // Exactly one initial location.
//        if (initLocCount == 0) {
//            String msg = fmt("Unsupported automaton \"%s\": automata without an initial location are currently "
//                    + "not supported.", getAbsName(aut));
//            problems.add(msg);
//        }
//
//        if (initLocCount > 1) {
//            String msg = fmt("Unsupported automaton \"%s\": automata with multiple initial locations are "
//                    + "currently not supported.", getAbsName(aut));
//            problems.add(msg);
//        }
//    }
//
//    @Override
//    protected void preprocessDictType(DictType type) {
//        String msg = fmt("Unsupported type \"%s\": dictionary types are currently not supported.", typeToStr(type));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessDistType(DistType type) {
//        String msg = fmt("Unsupported type \"%s\": distribution types are currently not supported.", typeToStr(type));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessFuncType(FuncType type) {
//        // We support functions directly in function calls, but not as
//        // data, to be passed around, stored in variables, etc. Note that
//        // types of expressions are skipped, so the function type of a function
//        // reference on which a call is performed directly, is allowed.
//        String msg = fmt("Unsupported type \"%s\": function types are currently not supported. That is, calling "
//                + "functions is supported, but using them as data is not supported.", typeToStr(type));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessListType(ListType type) {
//        // We support arrays.
//        if (isArrayType(type)) {
//            return;
//        }
//
//        // All other list types are not supported.
//        String msg = fmt("Unsupported type \"%s\": non-array list types are currently not supported.", typeToStr(type));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessSetType(SetType type) {
//        String msg = fmt("Unsupported type \"%s\": set types are currently not supported.", typeToStr(type));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void walkCifType(CifType type) {
//        // Skip checking types of expressions, as the expressions are already
//        // checked, and we don't transform the types of expressions.
//        EObject parent = type.eContainer();
//        if (parent instanceof Expression) {
//            return;
//        }
//
//        // Not a type of an expression.
//        super.walkCifType(type);
//    }
//
//    @Override
//    protected void preprocessUnaryExpression(UnaryExpression expr) {
//        // Check supported.
//        UnaryOperator op = expr.getOperator();
//        switch (op) {
//            case INVERSE:
//            case NEGATE:
//            case PLUS:
//                return;
//
//            case SAMPLE:
//                break;
//        }
//
//        // Unsupported.
//        String msg = fmt("Unsupported expression \"%s\": unary operator \"%s\" is currently not supported.",
//                exprToStr(expr), operatorToStr(op));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessBinaryExpression(BinaryExpression expr) {
//        // Check supported.
//        BinaryOperator op = expr.getOperator();
//        switch (op) {
//            case IMPLICATION:
//            case BI_CONDITIONAL:
//            case LESS_THAN:
//            case LESS_EQUAL:
//            case GREATER_THAN:
//            case GREATER_EQUAL:
//            case MULTIPLICATION:
//            case DIVISION:
//            case INTEGER_DIVISION:
//            case MODULUS:
//                return;
//
//            case DISJUNCTION:
//            case CONJUNCTION: {
//                CifType ltype = normalizeType(expr.getLeft().getType());
//                if (ltype instanceof BoolType) {
//                    return;
//                }
//                break;
//            }
//
//            case EQUAL:
//            case UNEQUAL: {
//                CifType ltype = normalizeType(expr.getLeft().getType());
//                if (ltype instanceof BoolType || ltype instanceof IntType || ltype instanceof RealType
//                        || ltype instanceof EnumType || ltype instanceof StringType)
//                {
//                    return;
//                }
//                break;
//            }
//
//            case ADDITION: {
//                CifType ltype = normalizeType(expr.getLeft().getType());
//                if (ltype instanceof IntType || ltype instanceof RealType || ltype instanceof StringType) {
//                    return;
//                }
//                break;
//            }
//
//            case SUBTRACTION: {
//                CifType ltype = normalizeType(expr.getLeft().getType());
//                if (ltype instanceof IntType || ltype instanceof RealType) {
//                    return;
//                }
//                break;
//            }
//
//            case ELEMENT_OF:
//            case SUBSET:
//                break;
//        }
//
//        // Unsupported.
//        String msg = fmt("Unsupported expression \"%s\": binary operator \"%s\" is currently not supported, "
//                + "or is not supported for the operands that are used.", exprToStr(expr), operatorToStr(op));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessProjectionExpression(ProjectionExpression expr) {
//        // Check supported.
//        CifType ctype = normalizeType(expr.getChild().getType());
//        if (ctype instanceof TupleType) {
//            return;
//        } else if (ctype instanceof StringType) {
//            return;
//        } else if (ctype instanceof ListType && isArrayType((ListType)ctype)) {
//            return;
//        }
//
//        // Unsupported.
//        String msg = fmt("Unsupported expression \"%s\": projections on anything other than tuples, arrays, and "
//                + "strings is currently not supported.", exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessSliceExpression(SliceExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": slicing is currently not supported.", exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessFunctionCallExpression(FunctionCallExpression expr) {
//        // Check supported.
//        Expression fexpr = expr.getFunction();
//        if (fexpr instanceof FunctionExpression) {
//            return;
//        } else if (fexpr instanceof StdLibFunctionExpression) {
//            // Check supported stdlib.
//            StdLibFunctionExpression lExpr = (StdLibFunctionExpression)fexpr;
//            StdLibFunction stdlib = lExpr.getFunction();
//            switch (stdlib) {
//                // Supported.
//                case ABS:
//                case CBRT:
//                case CEIL:
//                case EXP:
//                case FLOOR:
//                case FORMAT:
//                case LN:
//                case LOG:
//                case MAXIMUM:
//                case MINIMUM:
//                case POWER:
//                case ROUND:
//                case SCALE:
//                case SIGN:
//                case SQRT:
//                case ACOS:
//                case ASIN:
//                case ATAN:
//                case COS:
//                case SIN:
//                case TAN:
//                    return;
//
//                // Conditionally supported.
//                case EMPTY: {
//                    CifType atype = normalizeType(expr.getArguments().get(0).getType());
//                    if (atype instanceof ListType && isArrayType((ListType)atype)) {
//                        return;
//                    }
//                    break;
//                }
//
//                case SIZE: {
//                    CifType atype = normalizeType(expr.getArguments().get(0).getType());
//                    if (atype instanceof StringType) {
//                        return;
//                    } else if (atype instanceof ListType && isArrayType((ListType)atype)) {
//                        return;
//                    }
//                    break;
//                }
//
//                // Unsupported.
//                case DELETE:
//                case POP:
//                case ACOSH:
//                case ASINH:
//                case ATANH:
//                case COSH:
//                case SINH:
//                case TANH:
//                    break;
//
//                // Distributions (unsupported).
//                case BERNOULLI:
//                case BETA:
//                case BINOMIAL:
//                case CONSTANT:
//                case ERLANG:
//                case EXPONENTIAL:
//                case GAMMA:
//                case GEOMETRIC:
//                case LOG_NORMAL:
//                case NORMAL:
//                case POISSON:
//                case RANDOM:
//                case TRIANGLE:
//                case UNIFORM:
//                case WEIBULL:
//                    break;
//            }
//
//            // Unsupported stdlib.
//            String msg = fmt(
//                    "Unsupported expression \"%s\": standard  library function \"%s\" is currently not "
//                            + "supported, or is not supported for the arguments that are used.",
//                    exprToStr(expr), functionToStr(stdlib));
//            problems.add(msg);
//            return;
//        }
//
//        // Unsupported function call.
//        String msg = fmt(
//                "Unsupported expression \"%s\": function calls on anything other than standard library functions "
//                        + "and internal user-defined functions is currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessSetExpression(SetExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": sets are currently not supported.", exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessDictExpression(DictExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": dictionaries are currently not supported.", exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessFunctionExpression(FunctionExpression expr) {
//        // Function references used as the function to call in function call
//        // expressions are allowed. All other uses of function references
//        // (functions as values), are not supported.
//        if (expr.eContainer() instanceof FunctionCallExpression) {
//            FunctionCallExpression fcexpr = (FunctionCallExpression)expr.eContainer();
//            if (fcexpr.getFunction() == expr) {
//                return;
//            }
//        }
//
//        String msg = fmt("Unsupported expression \"%s\": the use of functions as values is currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessPrint(Print print) {
//        if (print.getTxtPre() != null && print.getWhenPost() != null) {
//            String msg = "Unsupported print declaration: print declarations with pre/source state text and "
//                    + "post/target state filtering (\"when\") are currently not supported.";
//            problems.add(msg);
//        }
//
//        if (print.getTxtPost() != null && print.getWhenPre() != null) {
//            String msg = "Unsupported print declaration: print declarations with post/target state text and "
//                    + "pre/source state filtering (\"when\") are currently not supported.";
//            problems.add(msg);
//        }
//    }
}
