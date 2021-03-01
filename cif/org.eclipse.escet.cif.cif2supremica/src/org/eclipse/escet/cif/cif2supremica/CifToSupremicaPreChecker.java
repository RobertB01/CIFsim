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

package org.eclipse.escet.cif.cif2supremica;

import static org.eclipse.escet.cif.common.CifEvalUtils.evalPreds;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.exprsToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.getComponentText1;
import static org.eclipse.escet.cif.common.CifTextUtils.getLocationText1;
import static org.eclipse.escet.cif.common.CifTextUtils.invToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifInvariantUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
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
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** CIF to Supremica transformation precondition checker. */
public class CifToSupremicaPreChecker extends CifWalker {
    /** Precondition violations found so far. */
    private final List<String> problems = list();

    /** Mapping from discrete variables to their marked values. Filled during checking. */
    private Map<DiscVariable, List<Expression>> markeds = map();

    /**
     * The number of initial locations found for the automaton being checked. Only valid while checking an automaton. Is
     * set to {@code -1} to disable this check due to evaluation errors in initialization predicates.
     */
    private int initLocCount;

    /**
     * Checks the CIF specification to make sure it satisfies the preconditions for the transformation.
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
            String msg = "CIF to Supremica transformation failed due to unsatisfied preconditions:\n - "
                    + StringUtils.join(problems, "\n - ");
            throw new UnsupportedException(msg);
        }
    }

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
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Initialization predicates only in locations.
        if (!comp.getInitials().isEmpty()) {
            String msg = fmt("Unsupported %s: initialization predicates are currently only supported in locations.",
                    getComponentText1(comp));
            problems.add(msg);
        }

        // Check for supported/duplicate variable marking predicates.
        for (Expression marked: comp.getMarkeds()) {
            boolean supported = processComponentMarker(marked);
            if (!supported) {
                String msg = fmt("Unsupported %s: unsupported marker predicate \"%s\".", getComponentText1(comp),
                        exprToStr(marked));
                problems.add(msg);
            }
        }

        // Check supervisory and invariant kinds of invariants.
        for (Invariant inv: comp.getInvariants()) {
            SupKind supKind = CifInvariantUtils.getSupKind(inv);
            if (supKind != SupKind.REQUIREMENT) {
                String kindTxt = (supKind == SupKind.NONE) ? "kindless" : CifTextUtils.kindToStr(supKind);
                String msg = fmt("Unsupported %s: unsupported %s invariant \"%s\".", getComponentText1(comp), kindTxt,
                        invToStr(inv, false));
                problems.add(msg);
            }

            InvKind invKind = inv.getInvKind();
            if (invKind != InvKind.STATE) {
                String msg = fmt("Unsupported %s: unsupported state/event exclusion invariant \"%s\".",
                        getComponentText1(comp), invToStr(inv, false));
                problems.add(msg);
            }
        }
    }

    /**
     * Processes the given marker predicate of a component. The predicate should be of the form 'discrete_variable =
     * marked_value'. Other forms are not supported. If supported, the variable and marked value are added to
     * {@link #markeds}. Also checks for duplicate marked values for a single discrete variable.
     *
     * @param marker The marker predicate to process.
     * @return Whether the marker predicate is supported ({@code true}) or not ({@code false}). Duplicate marked values
     *     do not affect the result of this method.
     */
    private boolean processComponentMarker(Expression marker) {
        // Analyze.
        if (!(marker instanceof BinaryExpression)) {
            return false;
        }
        BinaryExpression bexpr = (BinaryExpression)marker;
        if (bexpr.getOperator() != BinaryOperator.EQUAL) {
            return false;
        }
        if (!(bexpr.getLeft() instanceof DiscVariableExpression)) {
            return false;
        }

        // Add to supported marker predicates mapping.
        DiscVariableExpression vref = (DiscVariableExpression)bexpr.getLeft();
        DiscVariable var = vref.getVariable();
        List<Expression> values = markeds.get(var);
        if (values == null) {
            values = list();
            markeds.put(var, values);
        }
        values.add(bexpr.getRight());

        // We only support at most one marker value. Report duplicates only
        // once per variable.
        if (values.size() == 2) {
            String msg = fmt("Unsupported declaration \"%s\": discrete variables with multiple marker predicates are "
                    + "currently not supported.", getAbsName(var));
            problems.add(msg);
        }

        // The predicate is supported, although it may be a duplicate.
        return true;
    }

    @Override
    protected void preprocessAutomaton(Automaton aut) {
        // Check kind.
        if (aut.getKind() == SupKind.NONE) {
            String msg = fmt("Unsupported automaton \"%s\": kindless/regular automata are currently not supported.",
                    getAbsName(aut));
            problems.add(msg);
        }

        // Reset initial locations counter.
        initLocCount = 0;
    }

    @Override
    protected void postprocessAutomaton(Automaton aut) {
        // Exactly one initial location. This is required to ensure that the
        // elimination of location references in expressions does not introduce
        // additional initialization predicates.
        if (initLocCount == 0) {
            String msg = fmt("Unsupported automaton \"%s\": automata without an initial location are currently "
                    + "not supported.", getAbsName(aut));
            problems.add(msg);
        }

        if (initLocCount > 1) {
            String msg = fmt("Unsupported automaton \"%s\": automata with multiple (%d) initial locations are "
                    + "currently not supported.", getAbsName(aut), initLocCount);
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable var) {
        // Discrete variables with multiple initial values are not supported.
        // Actually, Supremica allows an initialization predicate, rather than
        // a value, and the latest version seems to require initialization
        // predicates instead of initial values. However, using
        // 'x == 1 | x != 1' for a variable 'x' in range '0..9', in the latest
        // version, seems to result in value '0' as initial value during
        // simulation, without any way to get an other initial value. To be
        // safe, we'll require a single initial value.
        //
        // Discrete variables that represent function parameters or local
        // variables of functions are ignored here, as functions are already
        // reported separately.
        EObject parent = var.eContainer();
        if (parent instanceof ComplexComponent) {
            VariableValue values = var.getValue();
            if (values != null && values.getValues().size() != 1) {
                String msg = fmt("Unsupported declaration \"%s\": discrete variables with multiple potential initial "
                        + "values are currently supported.", getAbsName(var));
                problems.add(msg);
            }
        }
    }

    @Override
    protected void preprocessContVariable(ContVariable var) {
        // Continuous variables not supported.
        String msg = fmt("Unsupported declaration \"%s\": continuous variables are currently unsupported.",
                getAbsName(var));
        problems.add(msg);
    }

    @Override
    protected void preprocessInputVariable(InputVariable var) {
        // Input variables not supported.
        String msg = fmt("Unsupported declaration \"%s\": input variables are currently unsupported.", getAbsName(var));
        problems.add(msg);
    }

    @Override
    protected void preprocessLocation(Location loc) {
        // Skip location parameters.
        EObject parent = loc.eContainer();
        if (parent instanceof LocationParameter) {
            return;
        }

        // No urgent locations.
        if (loc.isUrgent()) {
            String msg = fmt("Unsupported %s: urgent locations are currently unsupported.", getLocationText1(loc));
            problems.add(msg);
        }

        // Initialization.
        boolean initial = false;
        try {
            initial = loc.getInitials().isEmpty() ? false : evalPreds(loc.getInitials(), true, true);
        } catch (CifEvalException e) {
            // Can only fail if there is at least one predicate.
            String msg = fmt("Failed to evaluate initialization predicate(s): %s.", exprsToStr(loc.getInitials()));
            problems.add(msg);

            // Disable initial location count checking.
            initLocCount = -1;
        }

        if (initial && initLocCount != -1) {
            initLocCount++;
        }

        // Marked.
        if (!loc.getMarkeds().isEmpty()) {
            try {
                evalPreds(loc.getMarkeds(), false, true);
            } catch (CifEvalException e) {
                // Can only fail if there is at least one predicate.
                String msg = fmt("Failed to evaluate marker predicate(s): %s.", exprsToStr(loc.getInitials()));
                problems.add(msg);
            }
        }

        // No invariants in locations. We would need to change 'invariant X'
        // in location 'L' to 'invariant L => X' (in the automaton), but
        // references to locations are not supported by Supremica. We do
        // eliminate location references, so we could make a CIF to CIF
        // transformation that lifts invariants out of locations to the
        // surrounding automaton, and apply that transformation before the
        // elimination of location references.
        if (!loc.getInvariants().isEmpty()) {
            String msg = fmt("Unsupported %s: invariants in locations are currently unsupported.",
                    getLocationText1(loc));
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

        // No urgent edges.
        if (edge.isUrgent()) {
            Location loc = (Location)edge.eContainer();
            String msg = fmt("Unsupported %s: urgent edges are currently unsupported.", getLocationText1(loc));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessAssignment(Assignment asgn) {
        // Check for multi-assignment and partial variable assignment.
        if (asgn.getAddressable() instanceof TupleExpression) {
            // Multi-assignment unsupported.
            EObject loc = asgn;
            while (!(loc instanceof Location)) {
                loc = loc.eContainer();
            }
            Assert.check(loc instanceof Location);

            String msg = fmt("Unsupported %s: edges with multi-assignments are currently unsupported.",
                    getLocationText1((Location)loc));
            problems.add(msg);
        } else if (asgn.getAddressable() instanceof ProjectionExpression) {
            // Partial variable assignment unsupported.
            EObject loc = asgn;
            while (!(loc instanceof Location)) {
                loc = loc.eContainer();
            }
            Assert.check(loc instanceof Location);

            String msg = fmt("Unsupported %s: edges with partial variable assignments are currently unsupported.",
                    getLocationText1((Location)loc));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessIfUpdate(IfUpdate update) {
        // 'if' update unsupported.
        EObject loc = update;
        while (!(loc instanceof Location)) {
            loc = loc.eContainer();
        }
        Assert.check(loc instanceof Location);

        String msg = fmt("Unsupported %s: edges with 'if' updates are currently unsupported.",
                getLocationText1((Location)loc));
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
        // User-defined functions as well as all standard library functions
        // are unsupported.
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

            // Always OK, as same type operands, and types of operands checked
            // elsewhere.
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
    protected void preprocessIfExpression(IfExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": 'if' expressions are currently not supported.",
                exprToStr(expr));
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
    protected void preprocessSwitchExpression(SwitchExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": 'switch' expressions are currently not supported.",
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
