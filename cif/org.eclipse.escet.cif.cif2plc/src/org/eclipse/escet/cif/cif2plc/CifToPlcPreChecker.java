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

package org.eclipse.escet.cif.cif2plc;

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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.escet.cif.cif2plc.options.PlcOutputTypeOption;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifAddressableUtils.DuplVarAsgnException;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.common.checkers.checks.LocOnlySpecificInvariantsCheck;
import org.eclipse.escet.cif.common.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.common.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
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

/** CIF PLC code generator precondition checker. Does not support component definition/instantiation. */
public class CifToPlcPreChecker extends CifWalker {
    /** {@link FunctionCallExpression}.{@link FunctionCallExpression#getFunction() function} metamodel feature. */
    private static final EReference FCE_FUNC_REF = ExpressionsPackage.eINSTANCE.getFunctionCallExpression_Function();

    /** Precondition violations found so far. */
    private final List<String> problems = list();

    /**
     * The number of initial locations found for the automaton being checked. Only valid while checking an automaton. Is
     * set to {@code -1} to disable this check due to evaluation errors in initialization predicates.
     */
    private int initLocCount;

    /** The number of automata encountered. */
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
            String msg = "CIF PLC code generator failed due to unsatisfied preconditions:\n - "
                    + String.join("\n - ", problems);
            throw new UnsupportedException(msg);
        }
    }

    List<CifCheck> checks = list(
            // At least one automaton.
            new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

            // No initialization predicates in components.
            new CompNoInitPredsCheck(),

            // Disc variables must have single initial value.
            new VarNoDiscWithMultiInitValuesCheck(),

            // Allow state-event exclusion invariants only.
            new LocOnlySpecificInvariantsCheck(false, true),

            null // Temporary dummy value to allow the final comma.
            //
            );

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
            String msg = fmt("Unsupported automaton \"%s\": automata with multiple initial locations are currently "
                    + "not supported.", getAbsName(aut));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessFunction(Function func) {
        // Functions must have at least one parameter.
        if (func.getParameters().isEmpty()) {
            String msg = fmt("Unsupported function \"%s\": the function has no parameters.", getAbsName(func));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessAssignmentFuncStatement(AssignmentFuncStatement asgn) {
        // We use CifAddressableUtils.getRefs in the code generation, which
        // doesn't properly handle multi-assignments to different
        // non-overlapping parts of the same variable. In fact, that method
        // crashes on them. So, check here that isn't the case.
        Expression addr = asgn.getAddressable();
        try {
            CifAddressableUtils.getRefs(addr);
        } catch (DuplVarAsgnException ex) {
            // Get function.
            EObject parent = asgn.eContainer();
            while (!(parent instanceof InternalFunction)) {
                parent = parent.eContainer();
            }
            InternalFunction func = (InternalFunction)parent;

            // Add problem.
            String msg = fmt("Unsupported function \"%s\": the function has a multi-assignment that assigns multiple "
                    + "(non-overlapping) parts of a single variable.", getAbsName(func));
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessContinueFuncStatement(ContinueFuncStatement cfs) {
        // Get function.
        EObject parent = cfs.eContainer();
        while (!(parent instanceof InternalFunction)) {
            parent = parent.eContainer();
        }
        InternalFunction func = (InternalFunction)parent;

        // Add problem.
        String msg = fmt("Unsupported function \"%s\": the internal user-defined function contains a \"continue\" "
                + "statement.", getAbsName(func));
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
        // We support functions directly in function calls, but not as
        // data, to be passed around, stored in variables, etc.
        String msg = fmt("Unsupported type \"%s\": function types are currently not supported. That is, calling "
                + "functions is supported, but using them as data is not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessListType(ListType type) {
        // S7 transformation doesn't support list types. That is because S7 doesn't support functions returning arrays
        // and doesn't support arrays of arrays.
        if (PlcOutputTypeOption.isS7Output()) {
            String msg = fmt("Unsupported type \"%s\": list types are currently not supported for %s output.",
                    typeToStr(type), PlcOutputTypeOption.getPlcOutputType().dialogText);
            problems.add(msg);
            return;
        }

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
        // Could potentially use arrays in ST, but they have a fixed
        // length.
        String msg = fmt("Unsupported type \"%s\": set types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void preprocessStringType(StringType type) {
        // Could potentially use strings in ST, but they have limited
        // maximum length.
        String msg = fmt("Unsupported type \"%s\": string types are currently not supported.", typeToStr(type));
        problems.add(msg);
    }

    @Override
    protected void walkCifType(CifType type) {
        // Skip checking types of expressions, as the expressions are already
        // checked, and we don't transform the types of expressions. An
        // exception is the type of 'if' expressions, as they become the return
        // type of a function. Note that 'if' expressions with 'wrong' types
        // can't be introduced by linearization (for algebraic variables), as
        // algebraic variables of such types are unsupported as well. Since
        // 'switch' expressions are converted to 'if' expressions due to the
        // use of linearization, we also check their types as an exception.
        // Similarly, for the default values of functions, parameterless
        // functions may be introduced, but such use of function types is
        // unsupported as well.
        EObject parent = type.eContainer();
        if (parent instanceof Expression && !(parent instanceof IfExpression)
                && !(parent instanceof SwitchExpression))
        {
            return;
        }

        // Not a type of an expression.
        super.walkCifType(type);
    }

    @Override
    protected void preprocessStringExpression(StringExpression expr) {
        String msg = fmt("Unsupported expression \"%s\": string values are currently not supported.", exprToStr(expr));
        problems.add(msg);
    }

    @Override
    protected void preprocessCastExpression(CastExpression expr) {
        // Check supported.
        CifType ctype = normalizeType(expr.getChild().getType());
        CifType rtype = normalizeType(expr.getType());
        if (ctype instanceof IntType && rtype instanceof RealType) {
            // Integer to real conversion supported by PLC code.
            return;
        }
        if (CifTypeUtils.checkTypeCompat(ctype, rtype, RangeCompat.EQUAL)) {
            // Keeping the type the same is supported by removing the cast.
            return;
        }

        // Unsupported.
        String msg = fmt(
                "Unsupported expression \"%s\": casts from type \"%s\" to type \"%s\" are currently not supported.",
                exprToStr(expr), typeToStr(ctype), typeToStr(rtype));
        problems.add(msg);
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
                        || ltype instanceof EnumType)
                {
                    return;
                }
                break;
            }

            case ADDITION:
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
        String msg = fmt("Unsupported expression \"%s\": binary operator \"%s\" is currently not supported, or is not "
                + "supported for the operands that are used.", exprToStr(expr), operatorToStr(op));
        problems.add(msg);
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression expr) {
        // Check supported.
        CifType ctype = normalizeType(expr.getChild().getType());
        if (ctype instanceof TupleType) {
            return;
        } else if (ctype instanceof ListType && isArrayType((ListType)ctype)) {
            return;
        }

        // Unsupported.
        String msg = fmt("Unsupported expression \"%s\": projections on anything other than tuples and arrays is "
                + "currently not supported.", exprToStr(expr));
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
                case EXP:
                case LN:
                case LOG:
                case MAXIMUM:
                case MINIMUM:
                case POWER:
                case SQRT:
                case ACOS:
                case ASIN:
                case ATAN:
                case COS:
                case SIN:
                case TAN:
                    return;

                // Unsupported.
                case CEIL:
                case DELETE:
                case EMPTY:
                case FLOOR:
                case FORMAT:
                case POP:
                case ROUND:
                case SCALE:
                case SIGN:
                case SIZE:
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
        if (expr.eContainer() instanceof FunctionCallExpression && expr.eContainmentFeature() == FCE_FUNC_REF) {
            return;
        }

        String msg = fmt("Unsupported expression \"%s\": the use of functions as values is currently not supported.",
                exprToStr(expr));
        problems.add(msg);
    }
}
