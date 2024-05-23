//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.EdgeNoMultiAssignCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeNoPartialVarAssignCheck;
import org.eclipse.escet.cif.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoChannelsCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.checkers.checks.VarNoContinuousCheck;

/**
 * Pre-condition checker for the controller properties checker, for checks that use an MDD representation of the CIF
 * specification.
 */
public class MddPreChecker extends CifPreconditionChecker {
    /** Constructor for the {@link MddPreChecker} class. */
    public MddPreChecker() {
        super(
                // Channels (events with data types) are not supported.
                new EventNoChannelsCheck(),

                // Continuous variables are not supported.
                new VarNoContinuousCheck(),

                // Functions are not supported.
                new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.INTERNAL, NoSpecificUserDefFunc.INTERNAL),

                // Multi-assignments and partial variable assignments are not supported.
                new EdgeNoMultiAssignCheck(),
                new EdgeNoPartialVarAssignCheck(),

                // Equations are not supported.
                new EqnNotAllowedCheck(),

                // Restrict allowed types:
                // - Only boolean, ranged integer, and enumeration types are supported.
                // - Component types (in switch expressions) are allowed as well.
                // - No need to disallow component definition types, as component definitions are already eliminated.
                // - No need to disallow void types of channel uses, as channel declarations are already disallowed.
                new TypeNoSpecificTypesCheck(
                        NoSpecificType.DICT_TYPES,
                        NoSpecificType.DIST_TYPES,
                        NoSpecificType.FUNC_TYPES,
                        NoSpecificType.INT_TYPES_RANGELESS,
                        NoSpecificType.LIST_TYPES,
                        NoSpecificType.REAL_TYPES,
                        NoSpecificType.SET_TYPES,
                        NoSpecificType.STRING_TYPES,
                        NoSpecificType.TUPLE_TYPES)
                                .ignoreAnnotations()
        //
        );
    }

//    /** Precondition violations found so far. */
//    public Set<String> problems = set();
//
//    /**
//     * Checks the CIF specification to make sure it satisfies the preconditions for the checker.
//     *
//     * @param spec The CIF specification to check.
//     * @throws UnsupportedException If a precondition is violated.
//     */
//    public void check(Specification spec) {
//        // Find precondition violations.
//        walkSpecification(spec);
//
//        // If we have any problems, the specification is unsupported.
//        if (!problems.isEmpty()) {
//            String msg = "CIF controller properties checker failed due to unsatisfied preconditions:\n - "
//                    + String.join("\n - ", sortedstrings(problems));
//            throw new UnsupportedException(msg);
//        }
//    }
//
//    /**
//     * Find the containing {@link Location} instance of an object.
//     *
//     * @param obj Object in a location.
//     * @return The containing {@link Location} instance.
//     */
//    private Location getContainingLocation(EObject obj) {
//        while (!(obj instanceof Location)) {
//            obj = obj.eContainer();
//        }
//        return (Location)obj;
//    }
//
//    // Declaration checks.
//
//    // Type checks.
//
//    // Expression checks.
//
//    @Override
//    protected void preprocessBinaryExpression(BinaryExpression expr) {
//        BinaryOperator op = expr.getOperator();
//        switch (op) {
//            // Always boolean.
//            case IMPLICATION:
//            case BI_CONDITIONAL:
//                return;
//
//            // Check boolean arguments.
//            case CONJUNCTION:
//            case DISJUNCTION: {
//                CifType ltype = normalizeType(expr.getLeft().getType());
//                CifType rtype = normalizeType(expr.getRight().getType());
//                if (ltype instanceof BoolType && rtype instanceof BoolType) {
//                    return;
//                }
//                break;
//            }
//
//            // Check rangeless integer arguments.
//            case ADDITION:
//            case SUBTRACTION:
//            case MULTIPLICATION:
//            case INTEGER_DIVISION:
//            case MODULUS: {
//                CifType ltype = normalizeType(expr.getLeft().getType());
//                CifType rtype = normalizeType(expr.getRight().getType());
//                if (ltype instanceof IntType && !isRangeless((IntType)ltype) && rtype instanceof IntType
//                        && !isRangeless((IntType)rtype))
//                {
//                    return;
//                }
//                break;
//            }
//
//            // Always OK, as same type operands, and types of operands checked elsewhere.
//            case EQUAL:
//            case UNEQUAL:
//                return;
//
//            // Check rangeless integer arguments.
//            case GREATER_EQUAL:
//            case GREATER_THAN:
//            case LESS_EQUAL:
//            case LESS_THAN: {
//                CifType ltype = normalizeType(expr.getLeft().getType());
//                CifType rtype = normalizeType(expr.getRight().getType());
//                if (ltype instanceof IntType && !isRangeless((IntType)ltype) && rtype instanceof IntType
//                        && !isRangeless((IntType)rtype))
//                {
//                    return;
//                }
//                break;
//            }
//
//            // Unsupported, regardless of types of operands.
//            case DIVISION: // Real division.
//            case ELEMENT_OF:
//            case SUBSET:
//                break;
//
//            // Error.
//            default:
//                throw new RuntimeException("Unknown bin op: " + op);
//        }
//
//        // Unsupported.
//        String msg = fmt("Unsupported expression \"%s\": binary operator \"%s\" is currently not supported, "
//                + "or is not supported for the operands that are used.", exprToStr(expr), operatorToStr(op));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessCastExpression(CastExpression expr) {
//        CifType ctype = expr.getChild().getType();
//        CifType rtype = expr.getType();
//        if (CifTypeUtils.checkTypeCompat(ctype, rtype, RangeCompat.EQUAL)) {
//            // Ignore casting to the child type.
//            return;
//        }
//
//        String msg = fmt("Unsupported expression \"%s\": cast expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessDictExpression(DictExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": dictionary expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessFunctionCallExpression(FunctionCallExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": function calls are currently not supported.", exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessListExpression(ListExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": list expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessProjectionExpression(ProjectionExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": projection expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessRealExpression(RealExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": real number expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessSetExpression(SetExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": set expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessSliceExpression(SliceExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": slice expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessStringExpression(StringExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": string literal expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessTimeExpression(TimeExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": the use of variable \"time\" is currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessTupleExpression(TupleExpression expr) {
//        String msg = fmt("Unsupported expression \"%s\": tuple expressions are currently not supported.",
//                exprToStr(expr));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessUnaryExpression(UnaryExpression expr) {
//        UnaryOperator op = expr.getOperator();
//        switch (op) {
//            // Always boolean.
//            case INVERSE:
//                return;
//
//            // Check rangeless integer argument.
//            case NEGATE:
//            case PLUS: {
//                CifType ctype = normalizeType(expr.getChild().getType());
//                if (ctype instanceof IntType && !isRangeless((IntType)ctype)) {
//                    return;
//                }
//                break;
//            }
//
//            // Unsupported.
//            case SAMPLE:
//                break;
//
//            // Error.
//            default:
//                throw new RuntimeException("Unknown un op: " + op);
//        }
//
//        // Unsupported.
//        String msg = fmt("Unsupported expression \"%s\": unary operator \"%s\" is currently not supported, "
//                + "or is not supported for the operand that is used.", exprToStr(expr), operatorToStr(op));
//        problems.add(msg);
//    }
}
