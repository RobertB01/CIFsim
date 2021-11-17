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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEnumLiteral;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTuple;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.exceptions.InvalidModelException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Maps;
import org.eclipse.escet.common.java.Sets;

/** Expression evaluation code. */
public class ExpressionEval {
    /** Values of constants. */
    private Map<Constant, Object> constValues = map();

    /**
     * Evaluate an expression in the current state.
     *
     * @param expr Expression to evaluate.
     * @param state Current state context.
     * @param commVal Communicated value or {@code null}.
     * @return Value of the expression.
     * @throws CifEvalException When an expression cannot be evaluated.
     */
    public Object eval(Expression expr, BaseState state, Object commVal) throws CifEvalException {
        if (expr instanceof CastExpression) {
            return evalCast((CastExpression)expr, state, commVal);
        }

        if (expr instanceof TimeExpression) {
            return 0.0; // In an untimed system, time never progresses.
        }

        if (expr instanceof StringExpression) {
            StringExpression e = (StringExpression)expr;
            return e.getValue();
        }

        if (expr instanceof RealExpression) {
            RealExpression e = (RealExpression)expr;
            try {
                return CifMath.strToReal(e.getValue(), null);
            } catch (CifEvalException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (expr instanceof IntExpression) {
            IntExpression e = (IntExpression)expr;
            return e.getValue();
        }

        if (expr instanceof BoolExpression) {
            BoolExpression e = (BoolExpression)expr;
            return e.isValue();
        }

        if (expr instanceof UnaryExpression) {
            return evalUnary((UnaryExpression)expr, state, commVal);
        }

        if (expr instanceof BinaryExpression) {
            return evalBinary((BinaryExpression)expr, state, commVal);
        }

        if (expr instanceof IfExpression) {
            IfExpression e = (IfExpression)expr;
            if (evalGuards(e.getGuards(), state, commVal)) {
                return eval(e.getThen(), state, commVal);
            }
            for (ElifExpression ie: e.getElifs()) {
                if (evalGuards(ie.getGuards(), state, commVal)) {
                    return eval(ie.getThen(), state, commVal);
                }
            }
            return eval(e.getElse(), state, commVal);
        }

        if (expr instanceof SliceExpression) {
            return evalSlice((SliceExpression)expr, state, commVal);
        }

        if (expr instanceof FunctionCallExpression) {
            return evalFuncCall((FunctionCallExpression)expr, state, commVal);
        }

        if (expr instanceof ListExpression) {
            ListExpression e = (ListExpression)expr;
            List<Object> x = listc(e.getElements().size());
            for (Expression f: e.getElements()) {
                x.add(eval(f, state, commVal));
            }
            return x;
        }

        if (expr instanceof SetExpression) {
            SetExpression e = (SetExpression)expr;
            Set<Object> x = setc(e.getElements().size());
            for (Expression f: e.getElements()) {
                x.add(eval(f, state, commVal));
            }
            return x;
        }

        if (expr instanceof TupleExpression) {
            TupleExpression e = (TupleExpression)expr;
            CifTuple x = new CifTuple(e.getFields().size());
            for (Expression f: e.getFields()) {
                x.add(eval(f, state, commVal));
            }
            return x;
        }

        if (expr instanceof DictExpression) {
            DictExpression e = (DictExpression)expr;
            Map<Object, Object> x = mapc(e.getPairs().size());
            int dupCount = 0;
            for (DictPair p: e.getPairs()) {
                Object k = eval(p.getKey(), state, commVal);
                Object v = eval(p.getValue(), state, commVal);
                if (x.put(k, v) != null) {
                    dupCount++;
                }
            }
            if (dupCount != 0) {
                String msg = fmt("Dictionary literal has %d duplicate key%s.", dupCount, (dupCount > 1) ? "s" : "");
                throw new CifEvalException(msg, expr);
            }
            return x;
        }

        if (expr instanceof AlgVariableExpression) {
            AlgVariableExpression e = (AlgVariableExpression)expr;
            expr = state.getAlgExpression(e.getVariable());
            return eval(expr, state, commVal);
        }

        if (expr instanceof DiscVariableExpression) {
            DiscVariableExpression e = (DiscVariableExpression)expr;
            return state.getVarValue(e.getVariable());
        }

        if (expr instanceof ConstantExpression) {
            ConstantExpression e = (ConstantExpression)expr;
            Object val = constValues.get(e.getConstant());
            if (val == null) {
                val = eval(e.getConstant().getValue(), state, commVal);
                constValues.put(e.getConstant(), val);
            }
            return val;
        }

        if (expr instanceof ContVariableExpression) {
            ContVariableExpression e = (ContVariableExpression)expr;
            Assert.check(!e.isDerivative());
            return state.getVarValue(e.getVariable());
        }

        if (expr instanceof LocationExpression) {
            LocationExpression e = (LocationExpression)expr;
            int autIndex = state.explorer.indices.get(e.getLocation());
            return state.getCurrentLocation(autIndex) == e.getLocation();
        }

        if (expr instanceof EnumLiteralExpression) {
            EnumLiteralExpression e = (EnumLiteralExpression)expr;
            return new CifEnumLiteral(e.getLiteral());
        }

        if (expr instanceof ReceivedExpression) {
            return commVal;
        }

        if (expr instanceof ProjectionExpression) {
            return evalProjection((ProjectionExpression)expr, state, commVal);
        }

        if (expr instanceof SwitchExpression) {
            return evalSwitch((SwitchExpression)expr, state, commVal);
        }

        if (expr instanceof FunctionExpression) {
            return ((FunctionExpression)expr).getFunction();
        }

        // FieldExpression is handled by projection.
        // ElifExpression handled as part of the IfExpression.
        // ComponentExpression handled by cast/switch.
        // StdLibFunctionExpression handled by function call.

        // TauExpression never happens.
        // EventExpression never happens.
        // InputVariableExpression never happens.
        // CompInstWrapExpression never happens.
        // CompParamWrapExpression never happens.
        // CompParamExpression never happens.
        // SelfExpression never happens.

        String msg = fmt("Unexpected expression to evaluate: %s", expr);
        throw new RuntimeException(msg);
    }

    /**
     * Evaluate a sequence of guard expressions.
     *
     * @param es Guard expressions to evaluate.
     * @param state Context of the evaluation.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return {@code true} if all guards hold, {@code false} otherwise.
     * @throws CifEvalException When the guards cannot be evaluated.
     */
    private boolean evalGuards(List<Expression> es, BaseState state, Object commVal) throws CifEvalException {
        for (Expression e: es) {
            boolean b = (boolean)eval(e, state, commVal);
            if (!b) {
                return false;
            }
        }
        return true;
    }

    /**
     * Evaluate a slice expression.
     *
     * @param expr Slice expression to evaluate.
     * @param state Context of the evaluation.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return Value of the slice expression in the context.
     * @throws CifEvalException When the expression cannot be evaluated.
     */
    @SuppressWarnings("unchecked")
    private Object evalSlice(SliceExpression expr, BaseState state, Object commVal) throws CifEvalException {
        Integer begin = null;
        if (expr.getBegin() != null) {
            begin = (Integer)eval(expr.getBegin(), state, commVal);
        }

        Integer end = null;
        if (expr.getEnd() != null) {
            end = (Integer)eval(expr.getEnd(), state, commVal);
        }

        Object result = eval(expr.getChild(), state, commVal);
        if (result instanceof List) {
            return CifMath.slice((List<Object>)result, begin, end);
        } else {
            return CifMath.slice((String)result, begin, end);
        }
    }

    /**
     * Evaluates a function call expression.
     *
     * @param expr Function call expression to evaluate.
     * @param state Context of the evaluation.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return Result of the function call.
     * @throws CifEvalException When the expression cannot be evaluated.
     */
    private Object evalFuncCall(FunctionCallExpression expr, BaseState state, Object commVal) throws CifEvalException {
        // Evaluate arguments.
        Object[] ps = new Object[expr.getParams().size()];
        for (int i = 0; i < expr.getParams().size(); i++) {
            ps[i] = eval(expr.getParams().get(i), state, commVal);
        }

        // Evaluate function.
        Expression funcExpr = expr.getFunction();
        if (funcExpr instanceof StdLibFunctionExpression) {
            return evalStdLibFunc(expr, ps);
        } else {
            Object funcObj = eval(funcExpr, state, commVal);
            InternalFunction func = (InternalFunction)funcObj;
            return evalInternalFunc(state.explorer, expr, func, ps);
        }
    }

    /**
     * Evaluates a function call expression, for a standard library function.
     *
     * @param expr Standard library function call expression to evaluate.
     * @param ps The already evaluated parameters.
     * @return Result of the function call.
     * @throws CifEvalException When the expression cannot be evaluated.
     */
    @SuppressWarnings("unchecked")
    private Object evalStdLibFunc(FunctionCallExpression expr, Object[] ps) throws CifEvalException {
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
            case WEIBULL:
                // Precondition checker already made sure these don't occur.
                throw new RuntimeException("Shouldn't occur.");
        }
        String msg = "Unknown stdlib func: " + expr.getFunction();
        throw new RuntimeException(msg);
    }

    /**
     * Evaluates a function call expression, for an internal user-defined function.
     *
     * @param explorer Managing object of the exploration.
     * @param expr Internal user-defined function call expression to evaluate.
     * @param func The internal user-defined function to evaluate.
     * @param ps The already evaluated parameters.
     * @return Result of the function call.
     * @throws CifEvalException When the expression cannot be evaluated.
     */
    private Object evalInternalFunc(Explorer explorer, FunctionCallExpression expr, InternalFunction func, Object[] ps)
            throws CifEvalException
    {
        // Construct function state.
        Object[] vs = new Object[func.getVariables().size()];
        FunctionState state = new FunctionState(explorer, func, ps, vs);

        // Execute statements.
        Object rslt = execFuncStatements(func.getStatements(), state);
        Assert.notNull(rslt);
        return rslt;
    }

    /**
     * Executes function statements of an internal user-defined function.
     *
     * @param statements The statements to execute.
     * @param state Context of the evaluation.
     * @return Result of the execution if it executed a return statement, or {@code null} otherwise.
     * @throws CifEvalException When the statements cannot be executed.
     */
    private Object execFuncStatements(List<FunctionStatement> statements, FunctionState state) throws CifEvalException {
        for (FunctionStatement statement: statements) {
            Object rslt = evalFuncStatement(statement, state);
            if (rslt != null) {
                return rslt;
            }
        }
        return null;
    }

    /**
     * Executes a function statement of an internal user-defined function.
     *
     * @param statement The statement to execute.
     * @param state Context of the evaluation.
     * @return Result of the execution if it executed a return statement, or {@code null} otherwise.
     * @throws CifEvalException When the statement cannot be executed.
     */
    private Object evalFuncStatement(FunctionStatement statement, FunctionState state) throws CifEvalException {
        if (statement instanceof AssignmentFuncStatement) {
            // Assignment.
            AssignmentFuncStatement asgn = (AssignmentFuncStatement)statement;

            // Evaluate right hand side.
            Object rhs;
            try {
                rhs = eval(asgn.getValue(), state, null);
            } catch (CifEvalException ex) {
                String msg = fmt(
                        "Failed to compute value to assign for assignment \"%s := %s\" for function valuation \"%s\".",
                        CifTextUtils.exprToStr(asgn.getAddressable()), CifTextUtils.exprToStr(asgn.getValue()),
                        state.toString());
                throw new InvalidModelException(msg, ex);
            }

            // Check type range boundaries.
            state.explorer.checkTypeRangeBoundaries(asgn.getAddressable().getType(), asgn.getValue().getType(), true,
                    rhs, asgn.getAddressable(), asgn.getValue(), state);

            // Assign new value(s) to variable(s).
            FunctionState origState = new FunctionState(state);
            state.explorer.assignValue(rhs, asgn.getAddressable(), null, origState, state);

            // No return statement/value.
            return null;
        } else if (statement instanceof BreakFuncStatement) {
            // Break.
            throw new BreakException();
        } else if (statement instanceof ContinueFuncStatement) {
            // Continue.
            throw new ContinueException();
        } else if (statement instanceof IfFuncStatement) {
            // If.
            IfFuncStatement istat = (IfFuncStatement)statement;

            boolean condition = evalGuards(istat.getGuards(), state, null);
            if (condition) {
                List<FunctionStatement> body = istat.getThens();
                return execFuncStatements(body, state);
            }

            // Elif.
            for (ElifFuncStatement elif: istat.getElifs()) {
                condition = evalGuards(elif.getGuards(), state, null);
                if (condition) {
                    List<FunctionStatement> body = elif.getThens();
                    return execFuncStatements(body, state);
                }
            }

            // Else.
            return execFuncStatements(istat.getElses(), state);
        } else if (statement instanceof ReturnFuncStatement) {
            // Evaluate return values.
            List<Expression> es = ((ReturnFuncStatement)statement).getValues();
            List<Object> os = listc(es.size());
            for (Expression e: es) {
                os.add(eval(e, state, null));
            }

            // Return value or tuple of values.
            if (os.size() == 1) {
                return os.get(0);
            }
            CifTuple tuple = new CifTuple(os.size());
            tuple.addAll(os);
            return tuple;
        } else if (statement instanceof WhileFuncStatement) {
            // While.
            WhileFuncStatement wstat = (WhileFuncStatement)statement;
            while (true) {
                boolean condition = evalGuards(wstat.getGuards(), state, null);
                if (!condition) {
                    break;
                }

                if (AppEnv.isTerminationRequested()) {
                    throw new ExplorationTerminatedException();
                }

                try {
                    List<FunctionStatement> body = wstat.getStatements();
                    Object rslt = execFuncStatements(body, state);
                    if (rslt != null) {
                        return rslt;
                    }
                } catch (BreakException ex) {
                    break;
                } catch (ContinueException ex) {
                    continue;
                }
            }
            return null;
        } else {
            throw new RuntimeException("Unknown statement: " + statement);
        }
    }

    /** Exception that indicates a 'break' statement was executed. */
    private static class BreakException extends RuntimeException {
        // Nothing here.
    }

    /** Exception that indicates a 'continue' statement was executed. */
    private static class ContinueException extends RuntimeException {
        // Nothing here.
    }

    /**
     * Evaluate a cast expression.
     *
     * @param expr Cast expression to evaluate.
     * @param state Context of the evaluation.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return Value of the cast expression in the context.
     * @throws CifEvalException When the expression cannot be evaluated.
     */
    private Object evalCast(CastExpression expr, BaseState state, Object commVal) throws CifEvalException {
        // Handle cast from automaton reference to string as special case.
        Expression child = expr.getChild();
        if (CifTypeUtils.isAutRefExpr(child)) {
            // Get automaton.
            CifType ctype = child.getType();
            CifType nctype = CifTypeUtils.normalizeType(ctype);

            Assert.check(nctype instanceof ComponentType);
            Component comp = ((ComponentType)nctype).getComponent();
            Automaton aut = CifScopeUtils.getAutomaton(comp);

            // Return name of the current location of the automaton.
            int autIdx = state.explorer.indices.get(aut);
            Location loc = state.getCurrentLocation(autIdx);
            return CifLocationUtils.getName(loc);
        }

        // Normal case: evaluate child.
        Object crslt = eval(expr.getChild(), state, commVal);
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
            String msg = "Unknown cast: " + nctype + " to " + ntype;
            throw new RuntimeException(msg);
        }
    }

    /**
     * Evaluate a unary expression.
     *
     * @param expr Expression to evaluate.
     * @param state Context of the evaluation.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return Value of the expression in the context.
     * @throws CifEvalException When the expression cannot be evaluated.
     */
    private Object evalUnary(UnaryExpression expr, BaseState state, Object commVal) throws CifEvalException {
        Object result = eval(expr.getChild(), state, commVal);
        switch (expr.getOperator()) {
            case INVERSE:
                return !(boolean)result;

            case NEGATE:
                if (result instanceof Integer) {
                    return CifMath.negate((Integer)result, expr);
                }
                return CifMath.negate((Double)result);

            case PLUS:
                return result;

            case SAMPLE: // Never happens.
                break;
        }
        String msg = fmt("Unexpected type of unary operator %s", expr.getOperator());
        throw new RuntimeException(msg);
    }

    /**
     * Evaluate a binary operator expression.
     *
     * @param expr Expression to evaluate.
     * @param state Evaluation context.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return Value of the operation.
     * @throws CifEvalException When the expression cannot be evaluated.
     */
    @SuppressWarnings("unchecked")
    private Object evalBinary(BinaryExpression expr, BaseState state, Object commVal) throws CifEvalException {
        Object l = eval(expr.getLeft(), state, commVal);

        // Handle short-circuit evaluation cases.
        switch (expr.getOperator()) {
            case IMPLICATION:
                if (!(Boolean)l) {
                    return true;
                }
                return eval(expr.getRight(), state, commVal);

            case DISJUNCTION:
                if (l instanceof Boolean) {
                    if ((Boolean)l) {
                        return true;
                    }
                    return eval(expr.getRight(), state, commVal);
                }
                break;

            case CONJUNCTION:
                if (l instanceof Boolean) {
                    if (!(Boolean)l) {
                        return false;
                    }
                    return eval(expr.getRight(), state, commVal);
                }
                break;

            default:
                break;
        }

        // Other (non short-circuit) cases.
        Object r = eval(expr.getRight(), state, commVal);
        switch (expr.getOperator()) {
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

            case BI_CONDITIONAL:
                return ((Boolean)l).equals(r);

            case CONJUNCTION: {
                // Set intersection.
                Set<Object> lset = (Set<Object>)l;
                Set<Object> rset = (Set<Object>)r;
                Set<Object> rslt = Sets.copy(lset);
                rslt.retainAll(rset);
                return rslt;
            }

            case DISJUNCTION: {
                // Set union.
                Set<Object> lset = (Set<Object>)l;
                Set<Object> rset = (Set<Object>)r;
                Set<Object> rslt = Sets.copy(lset);
                rslt.addAll(rset);
                return rslt;
            }

            case DIVISION: {
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return CifMath.divide(ld, rd, expr);
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

            case EQUAL:
                return l.equals(r);

            case GREATER_EQUAL: {
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return ld >= rd;
            }

            case GREATER_THAN: {
                // Note that int to double conversion is lossless.
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return ld > rd;
            }

            case INTEGER_DIVISION:
                return CifMath.div((Integer)l, (Integer)r, expr);

            case LESS_EQUAL: {
                // Note that int to double conversion is lossless.
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return ld <= rd;
            }

            case LESS_THAN: {
                // Note that int to double conversion is lossless.
                double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                return ld < rd;
            }

            case MODULUS:
                return CifMath.mod((Integer)l, (Integer)r, expr);

            case MULTIPLICATION:
                if (l instanceof Integer && r instanceof Integer) {
                    return CifMath.multiply((Integer)l, (Integer)r, expr);
                } else {
                    double ld = (l instanceof Integer) ? (Integer)l : (Double)l;
                    double rd = (r instanceof Integer) ? (Integer)r : (Double)r;
                    return CifMath.multiply(ld, rd, expr);
                }

            case SUBSET: {
                Set<Object> lset = (Set<Object>)l;
                Set<Object> rset = (Set<Object>)r;
                return rset.containsAll(lset);
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

            case UNEQUAL:
                return !l.equals(r);

            case IMPLICATION: // Already handled before.
                break; // Dies in the exception below.
        }
        String msg = "Unknown binary op: " + expr.getOperator();
        throw new RuntimeException(msg);
    }

    /**
     * Perform projection on a value.
     *
     * @param expr Projection to compute.
     * @param state Evaluation context.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return Result of the projection operator.
     * @throws CifEvalException When the evaluation cannot be performed.
     */
    @SuppressWarnings("unchecked")
    private Object evalProjection(ProjectionExpression expr, BaseState state, Object commVal) throws CifEvalException {
        Object child = eval(expr.getChild(), state, commVal);

        if (child instanceof CifTuple) {
            CifTuple tuple = (CifTuple)child;

            if (expr.getIndex() instanceof FieldExpression) {
                // Special case for tuple field projection.
                Field field = ((FieldExpression)expr.getIndex()).getField();
                TupleType ttype = (TupleType)field.eContainer();
                int idx = ttype.getFields().indexOf(field);
                Assert.check(idx < tuple.size());
                return tuple.get(idx);
            }
            int idx = (int)eval(expr.getIndex(), state, commVal);
            return tuple.get(idx);
        }

        Object idxVal = eval(expr.getIndex(), state, commVal);

        if (child instanceof List<?>) {
            return CifMath.project((List<Object>)child, (Integer)idxVal, expr);
        } else if (child instanceof Map) {
            return CifMath.project((Map<Object, Object>)child, idxVal, expr);
        } else if (child instanceof String) {
            return CifMath.project((String)child, (Integer)idxVal, expr);
        } else if (child instanceof CifTuple) {
            return CifMath.project((CifTuple)child, (Integer)idxVal, expr);
        } else {
            String msg = fmt("Unknown projection child: %s", expr);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Evaluate a switch expression.
     *
     * @param expr Expression to evaluate.
     * @param state Evaluation context.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return Result of the switch expression.
     * @throws CifEvalException When the evaluation cannot be performed.
     */
    private Object evalSwitch(SwitchExpression expr, BaseState state, Object commVal) throws CifEvalException {
        // For automata values, check the locations.
        if (expr.getValue().getType() instanceof ComponentType) {
            ComponentType ct = (ComponentType)expr.getValue().getType();
            int autIndex = state.explorer.indices.get(ct.getComponent());
            Location curLoc = state.getCurrentLocation(autIndex);

            // Compare keys with current location until a case matches.
            for (SwitchCase sc: expr.getCases()) {
                if (sc.getKey() != null) {
                    LocationExpression key = (LocationExpression)sc.getKey();
                    if (key.getLocation() != curLoc) {
                        continue;
                    }
                }

                // No key, or a matching key value.
                return eval(sc.getValue(), state, commVal);
            }
            throw new RuntimeException("Unhandled location in switch.");
        } else {
            // Get value, and compare with keys until a case matches.
            Object value = eval(expr.getValue(), state, commVal);
            for (SwitchCase sc: expr.getCases()) {
                if (sc.getKey() != null) {
                    Object keyValue = eval(sc.getKey(), state, commVal);
                    if (!value.equals(keyValue)) {
                        continue;
                    }
                }

                // No key, or a matching key value.
                return eval(sc.getValue(), state, commVal);
            }
        }
        throw new RuntimeException("Unhandled value in switch.");
    }
}
