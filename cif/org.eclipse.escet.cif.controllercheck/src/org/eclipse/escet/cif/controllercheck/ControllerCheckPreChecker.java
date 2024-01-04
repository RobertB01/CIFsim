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

package org.eclipse.escet.cif.controllercheck;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.getComponentText1;
import static org.eclipse.escet.cif.common.CifTextUtils.getLocationText1;
import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

/** Pre-condition checker for the controller properties checker. */
public class ControllerCheckPreChecker extends CifWalker {
    /** Precondition violations found so far. */
    public Set<String> problems = set();

    /**
     * Checks the CIF specification to make sure it satisfies the preconditions for the checker.
     *
     * @param spec The CIF specification to check.
     * @throws UnsupportedException If a precondition is violated.
     */
    public void check(Specification spec) {
        // Find precondition violations.
        walkSpecification(spec);

        // If we have any problems, the specification is unsupported.
        if (!problems.isEmpty()) {
            String msg = "CIF controller properties check application failed due to unsatisfied preconditions:\n - "
                    + String.join("\n - ", sortedstrings(problems));
            throw new UnsupportedException(msg);
        }
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // State invariants, as state/event exclusion invariants have been eliminated.
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
    protected void preprocessEdge(Edge edge) {
        // Tau unsupported.
        if (edge.getEvents().isEmpty()) {
            problems.add("Unsupported event \"tau\": event is not controllable or uncontrollable (implicit use of "
                    + "\"tau\").");
        }
    }

    @Override
    protected void preprocessAssignment(Assignment asgn) {
        // Check for multi-assignment and partial variable assignment.
        if (asgn.getAddressable() instanceof TupleExpression) {
            // Multi-assignment unsupported.
            Location loc = getContainingLocation(asgn);
            String msg = fmt("Unsupported %s: edges with multi-assignments are currently unsupported.",
                    getLocationText1(loc));
            problems.add(msg);
        } else if (asgn.getAddressable() instanceof ProjectionExpression) {
            // Partial variable assignment unsupported.
            Location loc = getContainingLocation(asgn);
            String msg = fmt("Unsupported %s: edges with partial variable assignments are currently unsupported.",
                    getLocationText1(loc));
            problems.add(msg);
        }
    }

    /**
     * Find the containing {@link Location} instance of an object.
     *
     * @param obj Object in a location.
     * @return The containing {@link Location} instance.
     */
    private Location getContainingLocation(EObject obj) {
        while (!(obj instanceof Location)) {
            obj = obj.eContainer();
        }
        return (Location)obj;
    }

    @Override
    protected void preprocessLocation(Location loc) {
        // State invariants, as state/event exclusion invariants have been eliminated.
        List<Expression> invPreds = listc(loc.getInvariants().size());
        for (Invariant inv: loc.getInvariants()) {
            invPreds.add(inv.getPredicate());
        }
        if (!CifValueUtils.isTriviallyTrue(invPreds, false, true)) {
            String msg = fmt("Unsupported %s: state invariants in locations are currently not supported.",
                    getLocationText1(loc));
            problems.add(msg);
        }
    }

    // Declaration checks.

    @Override
    protected void preprocessEvent(Event event) {
        // Event must be controllable or uncontrollable.
        if (event.getControllable() == null) {
            String msg = fmt("Unsupported event \"%s\": event is not declared as controllable or uncontrollable.",
                    getAbsName(event));
            problems.add(msg);
        }

        // Event must not have a data type.
        if (event.getType() != null) {
            String msg = fmt("Unsupported event \"%s\": event is a channel (has a data type).", getAbsName(event));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessTauExpression(TauExpression obj) {
        problems.add(
                "Unsupported event \"tau\": event is not controllable or uncontrollable (explicit use of \"tau\").");
    }

    @Override
    protected void preprocessContVariable(ContVariable var) {
        // Continuous variables not supported.
        String msg = fmt("Unsupported declaration \"%s\": continuous variables are currently unsupported.",
                getAbsName(var));
        problems.add(msg);
    }

    @Override
    protected void preprocessFunction(Function func) {
        // User-defined functions are unsupported.
        String msg = fmt("Unsupported function \"%s\": user-defined functions are currently unsupported.",
                getAbsName(func));
        problems.add(msg);
    }

    @Override
    protected void preprocessEquation(Equation eqn) {
        // Equations are unsupported.
        String msg = fmt("Unsupported equation \"%s\": equations are currently unsupported.", getAbsName(eqn));
        problems.add(msg);
    }

    // Type checks.

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
        // User-defined functions as well as all standard library functions are unsupported.
        String msg = fmt("Unsupported type \"%s\": function types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessIntType(IntType type) {
        // Rangeless integer types unsupported.
        if (isRangeless(type)) {
            String msg = fmt("Unsupported type \"%s\": rangeless integer types are currently not supported.",
                    typeToStr(type));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessListType(ListType type) {
        String msg = fmt("Unsupported type \"%s\": list types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessRealType(RealType type) {
        String msg = fmt("Unsupported type \"%s\": real types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessSetType(SetType type) {
        String msg = fmt("Unsupported type \"%s\": set types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessStringType(StringType type) {
        String msg = fmt("Unsupported type \"%s\": string types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessTupleType(TupleType type) {
        // Tuples, tuple types, and multi-assignments are unsupported.
        String msg = fmt("Unsupported type \"%s\": tuple types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    // Expression checks.

    @Override
    protected void preprocessBinaryExpression(BinaryExpression expr) {
        BinaryOperator op = expr.getOperator();
        switch (op) {
            // Always boolean.
            case IMPLICATION:
            case BI_CONDITIONAL:
                return;

            // Check boolean arguments.
            case CONJUNCTION:
            case DISJUNCTION: {
                CifType ltype = normalizeType(expr.getLeft().getType());
                CifType rtype = normalizeType(expr.getRight().getType());
                if (ltype instanceof BoolType && rtype instanceof BoolType) {
                    return;
                }
                break;
            }

            // Check rangeless integer arguments.
            case ADDITION:
            case SUBTRACTION:
            case MULTIPLICATION:
            case INTEGER_DIVISION:
            case MODULUS: {
                CifType ltype = normalizeType(expr.getLeft().getType());
                CifType rtype = normalizeType(expr.getRight().getType());
                if (ltype instanceof IntType && !isRangeless((IntType)ltype) && rtype instanceof IntType
                        && !isRangeless((IntType)rtype))
                {
                    return;
                }
                break;
            }

            // Always OK, as same type operands, and types of operands checked elsewhere.
            case EQUAL:
            case UNEQUAL:
                return;

            // Check rangeless integer arguments.
            case GREATER_EQUAL:
            case GREATER_THAN:
            case LESS_EQUAL:
            case LESS_THAN: {
                CifType ltype = normalizeType(expr.getLeft().getType());
                CifType rtype = normalizeType(expr.getRight().getType());
                if (ltype instanceof IntType && !isRangeless((IntType)ltype) && rtype instanceof IntType
                        && !isRangeless((IntType)rtype))
                {
                    return;
                }
                break;
            }

            // Unsupported, regardless of types of operands.
            case DIVISION: // Real division.
            case ELEMENT_OF:
            case SUBSET:
                break;

            // Error.
            default:
                throw new RuntimeException("Unknown bin op: " + op);
        }

        // Unsupported.
        String msg = fmt("Unsupported expression \"%s\": binary operator \"%s\" is currently not supported, "
                + "or is not supported for the operands that are used.", exprToStr(expr), operatorToStr(op));
        problems.add(msg);
    }

    @Override
    protected void preprocessCastExpression(CastExpression expr) {
        CifType ctype = expr.getChild().getType();
        CifType rtype = expr.getType();
        if (CifTypeUtils.checkTypeCompat(ctype, rtype, RangeCompat.EQUAL)) {
            // Ignore casting to the child type.
            return;
        }

        String msg = fmt("Unsupported expression \"%s\": cast expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessDictExpression(DictExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": dictionary expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessFunctionCallExpression(FunctionCallExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": function calls are currently not supported.", exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessListExpression(ListExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": list expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": projection expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessRealExpression(RealExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": real number expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessSetExpression(SetExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": set expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessSliceExpression(SliceExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": slice expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessStringExpression(StringExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": string literal expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessTimeExpression(TimeExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": the use of variable \"time\" is currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessTupleExpression(TupleExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": tuple expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression expr) {
        UnaryOperator op = expr.getOperator();
        switch (op) {
            // Always boolean.
            case INVERSE:
                return;

            // Check rangeless integer argument.
            case NEGATE:
            case PLUS: {
                CifType ctype = normalizeType(expr.getChild().getType());
                if (ctype instanceof IntType && !isRangeless((IntType)ctype)) {
                    return;
                }
                break;
            }

            // Unsupported.
            case SAMPLE:
                break;

            // Error.
            default:
                throw new RuntimeException("Unknown un op: " + op);
        }

        // Unsupported.
        String msg = fmt("Unsupported expression \"%s\": unary operator \"%s\" is currently not supported, "
                + "or is not supported for the operand that is used.", exprToStr(expr), operatorToStr(op));
        problems.add(msg);
    }
}
