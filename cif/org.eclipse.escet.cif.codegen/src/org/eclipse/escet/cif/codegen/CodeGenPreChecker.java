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
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithOneInitLocCheck;
import org.eclipse.escet.cif.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
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
                new LocNoUrgentCheck(),

                // Initialization predicates in locations that can not be statically evaluated are not supported.
                // Automata that do not have exactly one initial location are not supported.
                new AutOnlyWithOneInitLocCheck(),

                // Urgent edges are not supported.
                new EdgeNoUrgentCheck(),

                // Data types other than bool, int (with or without range), real, string, enumerations, tuples, and
                // arrays, are not supported. This applies to the data types of variables, parameters of functions,
                // return types of functions, etc.
                //
                // Void types are supported as well, as we do linearization, which eliminates them.
                new TypeNoSpecificTypesCheck( //
                        NoSpecificType.DICT_TYPES, //
                        NoSpecificType.DIST_TYPES, //
                        NoSpecificType.LIST_TYPES_NON_ARRAY, //
                        NoSpecificType.SET_TYPES),

                // Disallow certain unary expressions.
                new ExprNoSpecificUnaryExprsCheck(
                        // Sampling of distributions is not supported.
                        NoSpecificUnaryOp.SAMPLE),

                // Disallow certain binary expressions.
                new ExprNoSpecificBinaryExprsCheck(
                        // The use of the addition binary operators on anything other than numeric or string values is
                        // not supported.
                        NoSpecificBinaryOp.ADDITION_LISTS, //
                        NoSpecificBinaryOp.ADDITION_DICTS, //

                        // The use of the conjunction and disjunction binary operators on anything other than boolean
                        // values is not supported.
                        NoSpecificBinaryOp.CONJUNCTION_SETS, //
                        NoSpecificBinaryOp.DISJUNCTION_SETS, //

                        // The use of the element test and subset binary operators is not supported.
                        NoSpecificBinaryOp.ELEMENT_OF, //
                        NoSpecificBinaryOp.SUBSET, //

                        // The use of the equality and inequality binary operators on anything other than boolean,
                        // integer, real, string, or enumeration values is not supported.
                        NoSpecificBinaryOp.EQUAL_DICT, //
                        NoSpecificBinaryOp.EQUAL_LIST, //
                        NoSpecificBinaryOp.EQUAL_SET, //
                        NoSpecificBinaryOp.EQUAL_TUPLE, //
                        NoSpecificBinaryOp.UNEQUAL_DICT, //
                        NoSpecificBinaryOp.UNEQUAL_LIST, //
                        NoSpecificBinaryOp.UNEQUAL_SET, //
                        NoSpecificBinaryOp.UNEQUAL_TUPLE, //

                        // The use of the subtraction binary operators on anything other than numeric values is not
                        // supported.
                        NoSpecificBinaryOp.SUBTRACTION_LISTS, //
                        NoSpecificBinaryOp.SUBTRACTION_SETS, //
                        NoSpecificBinaryOp.SUBTRACTION_DICTS)

        //
        );
    }

//    /** Precondition violations found so far. */
//    protected List<String> problems = list();
//
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
