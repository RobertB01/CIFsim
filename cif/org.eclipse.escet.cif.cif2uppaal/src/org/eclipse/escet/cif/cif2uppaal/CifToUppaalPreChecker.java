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

package org.eclipse.escet.cif.cif2uppaal;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.exprsToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.getComponentText1;
import static org.eclipse.escet.cif.common.CifTextUtils.getLocationText1;
import static org.eclipse.escet.cif.common.CifTextUtils.getLocationText2;
import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
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
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** CIF to UPPAAL transformation precondition checker. */
public class CifToUppaalPreChecker extends CifWalker {
    /** Precondition violations found so far. */
    private final List<String> problems = list();

    /**
     * The number of initial locations found for the automaton being checked. Only valid while checking an automaton. Is
     * set to {@code -1} to disable this check due to evaluation errors in initialization predicates.
     */
    private int initLocCount;

    /** The number of automata found so far. */
    private int autCount;

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
            String msg = "CIF to UPPAAL transformation failed due to unsatisfied preconditions:\n - "
                    + StringUtils.join(problems, "\n - ");
            throw new UnsupportedException(msg);
        }
    }

    @Override
    protected void preprocessSpecification(Specification spec) {
        // Reset automaton count.
        autCount = 0;
    }

    @Override
    protected void postprocessSpecification(Specification spec) {
        // At least one automaton.
        if (autCount == 0) {
            String msg = "Specifications without automata are currently not supported.";
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessEvent(Event event) {
        // No channels.
        if (event.getType() != null) {
            String msg = fmt("Unsupported event \"%s\": event is a channel (has a data type).", getAbsName(event));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Initialization predicates in components.
        if (!comp.getInitials().isEmpty()) {
            String msg = fmt("Unsupported %s: initialization predicates in components are currently not supported.",
                    getComponentText1(comp));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton aut) {
        // Found an automaton.
        autCount++;

        // Reset initial locations counter.
        initLocCount = 0;
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
            String msg = fmt("Unsupported automaton \"%s\": automata with multiple (%d) initial locations are "
                    + "currently not supported.", getAbsName(aut), initLocCount);
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable var) {
        // Discrete variables that represent function parameters or local
        // variables of functions are ignored here, as functions are already
        // reported separately.
        EObject parent = var.eContainer();
        if (!(parent instanceof ComplexComponent)) {
            return;
        }

        // Discrete variables with multiple initial values are not supported.
        VariableValue values = var.getValue();
        if (values != null && values.getValues().size() != 1) {
            String msg = fmt("Unsupported declaration \"%s\": discrete variables with multiple potential initial "
                    + "values are currently not supported.", getAbsName(var));
            problems.add(msg);
        }

        // Variables in the initial values are not supported, so we evaluate
        // it all away. Ensure that is possible.
        if (values != null && values.getValues().size() == 1) {
            Expression value = values.getValues().get(0);
            if (!CifValueUtils.hasSingleValue(value, true, true)) {
                String msg = fmt("Unsupported declaration \"%s\": initial value \"%s\" of the discrete variable "
                        + "is too complex to evaluate statically.", getAbsName(var), exprToStr(value));
                problems.add(msg);
            } else {
                try {
                    CifEvalUtils.eval(value, true);
                } catch (CifEvalException e) {
                    String msg = fmt("Unsupported declaration \"%s\": evaluation of the initial value "
                            + "\"%s\" of the discrete variable failed.", getAbsName(var), exprToStr(value));
                    problems.add(msg);
                }
            }
        }
    }

    @Override
    protected void preprocessEnumDecl(EnumDecl enumDecl) {
        // Enumerations not supported.
        String msg = fmt("Unsupported declaration \"%s\": enumerations are currently not supported.",
                getAbsName(enumDecl));
        problems.add(msg);
    }

    @Override
    protected void preprocessContVariable(ContVariable var) {
        // Continuous variables not supported.
        String msg = fmt("Unsupported declaration \"%s\": continuous variables are currently not supported.",
                getAbsName(var));
        problems.add(msg);
    }

    @Override
    protected void preprocessAlgVariable(AlgVariable var) {
        // Algebraic variables not supported.
        String msg = fmt("Unsupported declaration \"%s\": algebraic variables are currently not supported.",
                getAbsName(var));
        problems.add(msg);
    }

    @Override
    protected void preprocessInputVariable(InputVariable var) {
        // Input variables not supported.
        String msg = fmt("Unsupported declaration \"%s\": input variables are currently not supported.",
                getAbsName(var));
        problems.add(msg);
    }

    @Override
    protected void preprocessFunction(Function func) {
        // User-defined functions not supported.
        String msg = fmt("Unsupported declaration \"%s\": user-defined functions are currently not supported.",
                getAbsName(func));
        problems.add(msg);
    }

    @Override
    protected void preprocessLocation(Location loc) {
        // Initialization.
        List<Expression> initials = loc.getInitials();
        boolean initial = false;
        try {
            initial = initials.isEmpty() ? false : CifEvalUtils.evalPreds(loc.getInitials(), true, true);
        } catch (UnsupportedException e) {
            // Can only fail if there is at least one predicate.
            String msg = fmt("Initialization predicates \"%s\" of %s are too complex to evaluate statically.",
                    exprsToStr(initials), getLocationText2(loc));
            problems.add(msg);

            // Disable initial location count checking.
            initLocCount = -1;
        } catch (CifEvalException e) {
            // Can only fail if there is at least one predicate.
            String msg = fmt("Evaluation of the initialization predicates \"%s\" of %s failed.", exprsToStr(initials),
                    getLocationText2(loc));
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
        // No urgent edges.
        if (edge.isUrgent()) {
            Location loc = (Location)edge.eContainer();
            String msg = fmt("Unsupported %s: urgent edges are currently not supported.", getLocationText1(loc));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessAssignment(Assignment asgn) {
        // Check for multi-assignment and partial variable assignment.
        if (asgn.getAddressable() instanceof TupleExpression || asgn.getAddressable() instanceof ProjectionExpression) {
            // Get location.
            EObject ancestor = asgn;
            while (!(ancestor instanceof Location)) {
                ancestor = ancestor.eContainer();
            }
            Assert.check(ancestor instanceof Location);
            Location loc = (Location)ancestor;

            // Get specific problem text.
            String problem = null;
            if (asgn.getAddressable() instanceof TupleExpression) {
                problem = "multi-assignments";
            } else if (asgn.getAddressable() instanceof ProjectionExpression) {
                problem = "partial variable assignments";
            }
            Assert.notNull(problem);

            // Add problem.
            String msg = fmt("Unsupported %s: edges with %s are currently not supported.", getLocationText1(loc),
                    problem);
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

        String msg = fmt("Unsupported %s: edges with conditional updates ('if' updates) are currently not supported.",
                getLocationText1((Location)loc));
        problems.add(msg);
    }

    @Override
    protected void preprocessEnumType(EnumType type) {
        String msg = fmt("Unsupported type \"%s\": enumeration types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessRealType(RealType type) {
        String msg = fmt("Unsupported type \"%s\": real types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessStringType(StringType type) {
        String msg = fmt("Unsupported type \"%s\": string types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessListType(ListType type) {
        String msg = fmt("Unsupported type \"%s\": list types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessSetType(SetType type) {
        String msg = fmt("Unsupported type \"%s\": set types are currently not supported.", typeToStr(type));
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
    protected void preprocessDictType(DictType type) {
        String msg = fmt("Unsupported type \"%s\": dictionary types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessTupleType(TupleType type) {
        // Tuples, tuple types, and multi-assignments are unsupported.
        String msg = fmt("Unsupported type \"%s\": tuple types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessDistType(DistType type) {
        String msg = fmt("Unsupported type \"%s\": distribution types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessRealExpression(RealExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": real number expressions are currently not supported.",
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
    protected void preprocessUnaryExpression(UnaryExpression expr) {
        UnaryOperator op = expr.getOperator();
        switch (op) {
            // Always boolean.
            case INVERSE:
                return;

            // Check integer argument/result.
            case NEGATE:
            case PLUS: {
                CifType ctype = normalizeType(expr.getChild().getType());
                if (ctype instanceof IntType) {
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
        String msg = fmt("Unsupported expression \"%s\": unary operator \"%s\" is currently not supported, or is not "
                + "supported for the operand that is used.", exprToStr(expr), operatorToStr(op));
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

            // Check integer arguments.
            case GREATER_EQUAL:
            case GREATER_THAN:
            case LESS_EQUAL:
            case LESS_THAN:
            case EQUAL:
            case UNEQUAL:
            case ADDITION:
            case SUBTRACTION:
            case MULTIPLICATION: {
                CifType ltype = normalizeType(expr.getLeft().getType());
                CifType rtype = normalizeType(expr.getRight().getType());
                if (ltype instanceof IntType && rtype instanceof IntType) {
                    return;
                }
                break;
            }

            // Always integer.
            case INTEGER_DIVISION:
            case MODULUS:
                return;

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
        String msg = fmt("Unsupported expression \"%s\": binary operator \"%s\" is currently not supported, or is not "
                + "supported for the operands that are used.", exprToStr(expr), operatorToStr(op));
        problems.add(msg);
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": projection expressions are currently not supported.",
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
    protected void preprocessSetExpression(SetExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": set expressions are currently not supported.",
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
    protected void preprocessDictExpression(DictExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": dictionary expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessSwitchExpression(SwitchExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": 'switch' expressions are currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }
}
