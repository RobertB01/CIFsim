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

package org.eclipse.escet.tooldef.interpreter;

import static org.eclipse.escet.common.java.Lists.cast;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.typeToStr;
import static org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils.valueToStr;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NullExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ObjectType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef;
import org.eclipse.escet.tooldef.runtime.ExitException;
import org.eclipse.escet.tooldef.runtime.ToolDefException;
import org.eclipse.escet.tooldef.runtime.ToolDefList;
import org.eclipse.escet.tooldef.runtime.ToolDefMap;
import org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils;
import org.eclipse.escet.tooldef.runtime.ToolDefSet;
import org.eclipse.escet.tooldef.runtime.ToolDefTuple;

/** ToolDef interpreter expression evaluation. */
public class ToolDefEval {
    /** Constructor for the {@link ToolDefEval} class. */
    private ToolDefEval() {
        // Static class.
    }

    /**
     * Evaluates the given expression.
     *
     * @param expr The expression to evaluate.
     * @param ctxt The execution context.
     * @return The evaluation result.
     */
    public static Object eval(Expression expr, ExecContext ctxt) {
        if (expr instanceof BoolExpression) {
            BoolExpression bexpr = (BoolExpression)expr;
            return bexpr.isValue();
        } else if (expr instanceof CastExpression) {
            CastExpression cexpr = (CastExpression)expr;
            Object child = eval(cexpr.getChild(), ctxt);
            ToolDefType castType = cexpr.getType();

            try {
                return evalCast(child, castType);
            } catch (ToolDefException ex) {
                String msg = fmt("Can't cast value \"%s\" to type \"%s\".", valueToStr(child), typeToStr(castType));
                if (ex.getMessage() == null) {
                    throw new ToolDefException(msg);
                } else {
                    throw new ToolDefException(msg, ex);
                }
            }
        } else if (expr instanceof DoubleExpression) {
            DoubleExpression dexpr = (DoubleExpression)expr;
            return Double.parseDouble(dexpr.getValue());
        } else if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            List<Object> rslt = new ToolDefList<>(lexpr.getElements().size());
            for (Expression elem: lexpr.getElements()) {
                rslt.add(eval(elem, ctxt));
            }
            return rslt;
        } else if (expr instanceof MapExpression) {
            MapExpression mexpr = (MapExpression)expr;
            Map<Object, Object> rslt = new ToolDefMap<>(mexpr.getEntries().size());
            for (MapEntry entry: mexpr.getEntries()) {
                Object key = eval(entry.getKey(), ctxt);
                Object value = eval(entry.getValue(), ctxt);
                rslt.put(key, value);
            }
            return rslt;
        } else if (expr instanceof NullExpression) {
            return null;
        } else if (expr instanceof NumberExpression) {
            NumberExpression nexpr = (NumberExpression)expr;
            long value = Long.parseLong(nexpr.getValue());
            if (Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE) {
                return (int)value;
            }
            return value;
        } else if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            Object child = eval(pexpr.getChild(), ctxt);
            Object idx = eval(pexpr.getIndex(), ctxt);

            if (child instanceof String) {
                String str = (String)child;

                // Normalize index and check for out of bounds.
                int normalizedIdx = (int)idx;
                if (normalizedIdx < 0) {
                    normalizedIdx = str.length() + normalizedIdx;
                }
                if (normalizedIdx < 0 || normalizedIdx >= str.length()) {
                    String msg = fmt("Index out of bounds: %s[%s].", valueToStr(str), valueToStr(idx));
                    throw new ToolDefException(msg);
                }

                // Return the character as a string.
                return str.substring(normalizedIdx, normalizedIdx + 1);
            } else if (child instanceof ToolDefList) {
                @SuppressWarnings("unchecked")
                List<Object> list = (ToolDefList<Object>)child;

                // Normalize index and check for out of bounds.
                int normalizedIdx = (int)idx;
                if (normalizedIdx < 0) {
                    normalizedIdx = list.size() + normalizedIdx;
                }
                if (normalizedIdx < 0 || normalizedIdx >= list.size()) {
                    String msg = fmt("Index out of bounds: %s[%s].", valueToStr(list), valueToStr(idx));
                    throw new ToolDefException(msg);
                }

                // Return the element.
                return list.get(normalizedIdx);
            } else if (child instanceof ToolDefMap) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> map = (ToolDefMap<Object, Object>)child;

                // Check non-existing key.
                if (!map.containsKey(idx)) {
                    String msg = fmt("Key not found: %s[%s].", valueToStr(map), valueToStr(idx));
                    throw new ToolDefException(msg);
                }

                // Return the value for the key.
                return map.get(idx);
            } else if (child instanceof ToolDefTuple) {
                // Return the element, which must exist.
                ToolDefTuple tuple = (ToolDefTuple)child;
                return tuple.getValue((int)idx);
            } else {
                throw new RuntimeException("Unexpected proj child: " + child);
            }
        } else if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            Set<Object> rslt = new ToolDefSet<>(sexpr.getElements().size());
            for (Expression elem: sexpr.getElements()) {
                rslt.add(eval(elem, ctxt));
            }
            return rslt;
        } else if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;
            Object child = eval(sexpr.getChild(), ctxt);
            Object begin = null;
            Object end = null;
            if (sexpr.getBegin() != null) {
                begin = eval(sexpr.getBegin(), ctxt);
            }
            if (sexpr.getEnd() != null) {
                end = eval(sexpr.getEnd(), ctxt);
            }

            if (child instanceof String) {
                // Copied from Strings.slice.
                String str = (String)child;
                Integer beginIndex = (Integer)begin;
                Integer endIndex = (Integer)end;

                // Get length once.
                int len = str.length();

                // Replace 'null' by defaults.
                int b = (beginIndex == null) ? 0 : beginIndex;
                int e = (endIndex == null) ? len : endIndex;

                // Handle negative indices.
                if (b < 0) {
                    b = len + b;
                }
                if (e < 0) {
                    e = len + e;
                }

                // Handle out of range and empty interval.
                if (b < 0) {
                    b = 0;
                }
                if (e < 0) {
                    e = 0;
                }

                if (b > len) {
                    b = len;
                }
                if (e > len) {
                    e = len;
                }

                if (b > e) {
                    b = e;
                }

                // Use normal substring from Java to get actual result.
                return str.substring(b, e);
            } else if (child instanceof ToolDefList) {
                // Copied from Lists.slice.
                @SuppressWarnings("unchecked")
                ToolDefList<Object> lst = (ToolDefList<Object>)child;
                Integer beginIndex = (Integer)begin;
                Integer endIndex = (Integer)end;

                // Get length once.
                int len = lst.size();

                // Replace 'null' by defaults.
                int b = (beginIndex == null) ? 0 : beginIndex;
                int e = (endIndex == null) ? len : endIndex;

                // Handle negative indices.
                if (b < 0) {
                    b = len + b;
                }
                if (e < 0) {
                    e = len + e;
                }

                // Handle out of range and empty interval.
                if (b < 0) {
                    b = 0;
                }
                if (e < 0) {
                    e = 0;
                }

                if (b > len) {
                    b = len;
                }
                if (e > len) {
                    e = len;
                }

                if (b > e) {
                    b = e;
                }

                // Using normal List.subList method to get the view.
                return lst.subList(b, e);
            } else {
                throw new RuntimeException("Unexpected slice child: " + child);
            }
        } else if (expr instanceof StringExpression) {
            StringExpression sexpr = (StringExpression)expr;
            return sexpr.getValue();
        } else if (expr instanceof ToolInvokeExpression) {
            return eval((ToolInvokeExpression)expr, ctxt);
        } else if (expr instanceof ToolParamExpression) {
            ToolParamExpression tpexpr = (ToolParamExpression)expr;
            return ctxt.getValue(tpexpr.getParam());
        } else if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            List<Object> elems = new ToolDefList<>(texpr.getElements().size());
            for (Expression elem: texpr.getElements()) {
                elems.add(eval(elem, ctxt));
            }
            return ToolDefRuntimeUtils.makeTuple(elems);
        } else if (expr instanceof VariableExpression) {
            VariableExpression vexpr = (VariableExpression)expr;
            return ctxt.getValue(vexpr.getVariable());
        } else {
            // Unknown or unsupported expression.
            throw new RuntimeException("Unknown/unsupported expr: " + expr);
        }
    }

    /**
     * Tries to cast the given value to the given type.
     *
     * @param value The value to cast.
     * @param type The type to which to cast.
     * @return The casted value.
     * @throws ToolDefException If the cast fails. The message of the exception may be {@code null}.
     * @see #normalizeJavaReturnValue
     */
    private static Object evalCast(Object value, ToolDefType type) {
        // Normalize type.
        type = ToolDefTypeUtils.normalizeType(type);

        // Null value.
        if (value == null) {
            if (type instanceof TypeParamRef) {
                // Must be 'T' to 'T' cast, as the type checker doesn't allow
                // any other '...' to 'T' casts.
                return null;
            }

            if (type.isNullable()) {
                return null;
            }

            String msg = "Can't cast \"null\" to a non-nullable type.";
            throw new ToolDefException(msg);
        }

        // Special cases. Type checker ensures child and target types are not
        // nullable.
        if (value instanceof String && type instanceof BoolType) {
            // Targ
            if (value.equals("true")) {
                return true;
            }
            if (value.equals("false")) {
                return false;
            }
            throw new ToolDefException("Text is not a boolean value.");
        } else if (value instanceof String && type instanceof IntType) {
            try {
                return Integer.parseInt((String)value);
            } catch (NumberFormatException ex) {
                String msg = "Text is not an integer value, or is outside the range of the \"int\" type.";
                throw new ToolDefException(msg);
            }
        } else if (value instanceof String && type instanceof LongType) {
            try {
                return Long.parseLong((String)value);
            } catch (NumberFormatException ex) {
                String msg = "Text is not a long value, or is outside the the range of the \"long\" type.";
                throw new ToolDefException(msg);
            }
        } else if (value instanceof String && type instanceof DoubleType) {
            try {
                double rslt = Double.parseDouble((String)value);
                if (Double.isNaN(rslt)) {
                    throw new ToolDefException("Text is not a number.");
                }
                if (Double.isInfinite(rslt)) {
                    throw new ToolDefException("Text is an infinite number.");
                }
                if (rslt == -0.0) {
                    rslt = 0.0;
                }
                return rslt;
            } catch (NumberFormatException ex) {
                String msg = "Text is not a double value, or is outside the the range of the \"double\" type.";
                throw new ToolDefException(msg);
            }
        }

        // Simple types.
        if (type instanceof BoolType) {
            // Only boolean fits.
            if (value instanceof Boolean) {
                return (boolean)value;
            }

            throw new ToolDefException(null);
        } else if (type instanceof IntType) {
            // Integer fits. Long and double might fit depending on their value.
            if (value instanceof Integer) {
                return (int)value;
            }

            if (value instanceof Long) {
                long l = (Long)value;
                if (Integer.MIN_VALUE <= l && l <= Integer.MAX_VALUE) {
                    return (int)l;
                }
                String msg = "The value is outside of the \"int\" type range.";
                throw new ToolDefException(msg);
            }

            if (value instanceof Double) {
                double d = (Double)value;
                int i = (int)d;
                double d2 = i;
                if (((Double)d).equals(d2)) {
                    return i;
                }
                String msg = "The value is outside of the \"int\" type range.";
                throw new ToolDefException(msg);
            }

            throw new ToolDefException(null);
        } else if (type instanceof LongType) {
            // Integer and long fit. Double might fit depending on its value.
            if (value instanceof Integer) {
                int i = (Integer)value;
                return (long)i;
            }

            if (value instanceof Long) {
                return (long)value;
            }

            if (value instanceof Double) {
                double d = (Double)value;
                long l = (long)d;
                double d2 = l;
                if (((Double)d).equals(d2)) {
                    return l;
                }
                String msg = "The value is outside of the \"long\" type range.";
                throw new ToolDefException(msg);
            }

            throw new ToolDefException(null);
        } else if (type instanceof DoubleType) {
            // Integer, long, and double fit.
            if (value instanceof Integer) {
                int i = (Integer)value;
                return (double)i;
            }

            if (value instanceof Long) {
                long l = (Long)value;
                return (double)l;
            }

            if (value instanceof Double) {
                return (double)value;
            }

            throw new ToolDefException(null);
        } else if (type instanceof StringType) {
            // Only string fits.
            if (value instanceof String) {
                return value;
            }

            throw new ToolDefException(null);
        } else if (type instanceof ObjectType) {
            // Everything fits.
            return value;
        }

        // Container types.
        if (type instanceof ListType) {
            if (!(value instanceof ToolDefList)) {
                throw new ToolDefException(null);
            }

            @SuppressWarnings("unchecked")
            List<Object> list = (ToolDefList<Object>)value;
            List<Object> rslt = null;

            ToolDefType elemType = ((ListType)type).getElemType();
            for (int i = 0; i < list.size(); i++) {
                // Process element recursively. If unchanged, move to next one.
                Object elem = list.get(i);
                Object newElem = evalCast(elem, elemType);
                if (elem == newElem) {
                    continue;
                }

                // Element changed. Copy list if not yet done, and set new
                // element.
                if (rslt == null) {
                    rslt = new ToolDefList<>(list);
                }
                rslt.set(i, newElem);
            }
            return (rslt == null) ? list : rslt;
        } else if (type instanceof SetType) {
            if (!(value instanceof ToolDefSet)) {
                throw new ToolDefException(null);
            }

            @SuppressWarnings("unchecked")
            Set<Object> set = (ToolDefSet<Object>)value;
            Set<Object> rslt = new ToolDefSet<>(set.size());

            ToolDefType elemType = ((SetType)type).getElemType();
            for (Object elem: set) {
                Object newElem = evalCast(elem, elemType);
                rslt.add(newElem);
            }
            Assert.check(rslt.size() == set.size());
            return rslt;
        } else if (type instanceof MapType) {
            if (!(value instanceof ToolDefMap)) {
                throw new ToolDefException(null);
            }

            @SuppressWarnings("unchecked")
            Map<Object, Object> map = (ToolDefMap<Object, Object>)value;
            Map<Object, Object> rslt = new ToolDefMap<>(map.size());

            ToolDefType keyType = ((MapType)type).getKeyType();
            ToolDefType valueType = ((MapType)type).getValueType();
            for (Entry<Object, Object> entry: map.entrySet()) {
                Object newKey = evalCast(entry.getKey(), keyType);
                Object newValue = evalCast(entry.getValue(), valueType);
                rslt.put(newKey, newValue);
            }
            Assert.check(rslt.size() == map.size());
            return rslt;
        } else if (type instanceof TupleType) {
            if (!(value instanceof ToolDefTuple)) {
                throw new ToolDefException(null);
            }

            ToolDefTuple tuple = (ToolDefTuple)value;

            List<Object> values = ToolDefRuntimeUtils.unpackTuple(tuple);
            List<ToolDefType> types = ((TupleType)type).getFields();
            if (values.size() != types.size()) {
                throw new ToolDefException(null);
            }
            for (int i = 0; i < values.size(); i++) {
                Object newValue = evalCast(values.get(i), types.get(i));
                values.set(i, newValue);
            }
            return ToolDefRuntimeUtils.makeTuple(values);
        }

        // Type parameters.
        if (type instanceof TypeParamRef) {
            // Must be 'T' to 'T' cast, as the type checker doesn't allow
            // any other '...' to 'T' casts.
            return value;
        }

        // Unknown/unsupported types.
        throw new RuntimeException("Unknown/unsupported type: " + type);
    }

    /**
     * Evaluates the given tool invocation expression.
     *
     * @param invoke The tool invocation expression to evaluate.
     * @param ctxt The execution context.
     * @return The evaluation result.
     */
    private static Object eval(ToolInvokeExpression invoke, ExecContext ctxt) {
        // Get tool.
        ToolRef toolRef = invoke.getTool();
        Tool tool = toolRef.getTool();

        // Special case for short circuit boolean operators.
        if (toolRef.isBuiltin()) {
            if (tool.getName().equals("and") || tool.getName().equals("or")) {
                ToolDefType t = invoke.getArguments().get(0).getValue().getType();
                t = ToolDefTypeUtils.normalizeType(t);
                if (t instanceof BoolType) {
                    return evalShortCircuit(invoke, ctxt);
                }
            }
        }

        // Initialize invocation arguments.
        int paramCnt = tool.getParameters().size();
        boolean[] specified = new boolean[paramCnt];
        Object[] args = new Object[paramCnt];

        // Evaluate given arguments and store at correct position in invocation
        // arguments list.
        int posIdx = 0;
        for (int i = 0; i < invoke.getArguments().size(); i++) {
            // Get argument.
            ToolArgument arg = invoke.getArguments().get(i);

            // Evaluate argument.
            Object value;
            try {
                value = eval(arg.getValue(), ctxt);
            } catch (ToolDefException ex) {
                String argTxt = (arg.getName() == null) ? Numbers.toOrdinal(i + 1) : "\"" + arg.getName() + "\"";
                String msg = fmt("Failed to evaluate %s argument for invocation of %s.", argTxt,
                        ToolDefTextUtils.getAbsDescr(toolRef));
                throw new ToolDefException(msg, ex);
            }

            if (ctxt.isTerminationRequested()) {
                throw new ExitException(0);
            }

            // Store argument.
            if (arg.getName() == null) {
                // Positional argument.
                specified[posIdx] = true;

                if (tool.getParameters().get(posIdx).isVariadic()) {
                    if (args[posIdx] == null) {
                        args[posIdx] = new ToolDefList<>();
                    }

                    @SuppressWarnings("unchecked")
                    List<Object> lst = (List<Object>)args[posIdx];
                    lst.add(value);
                } else {
                    args[posIdx] = value;
                    posIdx++;
                }
            } else {
                // Named argument.
                boolean found = false;
                for (int j = 0; j < paramCnt; j++) {
                    if (tool.getParameters().get(j).getName().equals(arg.getName())) {
                        found = true;
                        specified[j] = true;
                        args[j] = value;
                        break;
                    }
                }
                Assert.check(found);
            }
        }

        // Add default values for not specified optional parameters.
        for (int i = 0; i < paramCnt; i++) {
            // Skip parameters for which an argument was given.
            if (specified[i]) {
                continue;
            }

            // Handle unspecified variadic parameters.
            ToolParameter param = tool.getParameters().get(i);
            if (param.isVariadic()) {
                args[i] = new ToolDefList<>();
                continue;
            }

            // Evaluate default value of unspecified parameter.
            Object value;
            try {
                value = eval(param.getValue(), ctxt);
            } catch (ToolDefException ex) {
                String msg = fmt("Failed to evaluate default value for the \"%s\" parameter of %s.", param.getName(),
                        ToolDefTextUtils.getAbsDescr(toolRef));
                throw new ToolDefException(msg, ex);
            }
            args[i] = value;

            if (ctxt.isTerminationRequested()) {
                throw new ExitException(0);
            }
        }

        // Evaluate tool.
        Object rslt;
        try {
            if (tool instanceof ToolDefTool) {
                rslt = eval((ToolDefTool)tool, toolRef.isBuiltin(), args, ctxt);
            } else if (tool instanceof JavaTool) {
                rslt = eval((JavaTool)tool, args, invoke.getType(), ctxt);
            } else {
                throw new RuntimeException("Unknown tool: " + tool);
            }
        } catch (ToolDefException ex) {
            String msg = fmt("Failed to execute %s.", ToolDefTextUtils.getAbsDescr(toolRef));
            throw new ToolDefException(msg, ex);
        }

        // Return the result.
        return rslt;
    }

    /**
     * Evaluates an 'and' or 'or' binary operator for boolean arguments in a short circuit manner.
     *
     * @param invoke The tool invocation expression to evaluate.
     * @param ctxt The execution context.
     * @return The evaluation result.
     */
    private static Object evalShortCircuit(ToolInvokeExpression invoke, ExecContext ctxt) {
        // Paranoia checking.
        ToolRef toolRef = invoke.getTool();
        Tool tool = toolRef.getTool();
        Assert.check(invoke.getArguments().size() == 2);
        Assert.check(tool.getParameters().size() == 2);

        // Evaluate first argument.
        boolean value;
        try {
            value = (Boolean)eval(invoke.getArguments().get(0).getValue(), ctxt);
        } catch (ToolDefException ex) {
            String msg = fmt("Failed to evaluate 1st argument for %s.", ToolDefTextUtils.getAbsDescr(toolRef));
            throw new ToolDefException(msg, ex);
        }

        // Evaluate based on first argument.
        if (tool.getName().equals("and")) {
            // false and X = false
            if (!value) {
                return false;
            }
        } else if (tool.getName().equals("or")) {
            // true or X = true
            if (value) {
                return true;
            }
        } else {
            throw new RuntimeException("Unexpected tool: " + tool.getName());
        }

        // Evaluate second argument.
        try {
            value = (Boolean)eval(invoke.getArguments().get(1).getValue(), ctxt);
        } catch (ToolDefException ex) {
            String msg = fmt("Failed to evaluate 2nd argument for %s.", ToolDefTextUtils.getAbsDescr(toolRef));
            throw new ToolDefException(msg, ex);
        }

        // Evaluate based on second argument.
        // true and X = X
        // false or X = X
        return value;
    }

    /**
     * Evaluates a ToolDef tool invocation.
     *
     * @param tool The tool to invoke.
     * @param builtin Whether the tool is a built-in tool.
     * @param args The arguments for the tool.
     * @param ctxt The execution context from the invocation expression.
     * @return The return value.
     */
    private static Object eval(ToolDefTool tool, boolean builtin, Object[] args, ExecContext ctxt) {
        // Create new execution context for the tool. We create a fresh
        // context, and don't use the invocation context as parent context.
        ExecContext toolCtxt = new ExecContext(ctxt.interpreter);

        // Add parameters to the context.
        for (int i = 0; i < args.length; i++) {
            ToolParameter param = tool.getParameters().get(i);
            Object arg = args[i];
            toolCtxt.addToolParam(param, arg);
        }

        // Execute statements.
        List<Declaration> body = cast(tool.getStatements());
        if (ctxt.isTerminationRequested()) {
            throw new ExitException(0);
        }

        ToolDefReturnValue retValue = ToolDefExec.execute(body, toolCtxt);

        // Return proper result.
        if (retValue == null) {
            // No return statement encountered. No return value if no return
            // types.
            if (tool.getReturnTypes().isEmpty()) {
                return null;
            }

            // Missing return statement.
            String msg = "Execution of the tool did not encounter a \"return\" or \"exit\" statement.";
            throw new ToolDefException(msg);
        } else {
            return retValue.value;
        }
    }

    /**
     * Evaluates a Java tool invocation.
     *
     * @param tool The Java tool to invoke.
     * @param args The arguments for the tool.
     * @param resultType The expected result type. This is the type of the invoke expression (with type parameters
     *     filled in), not the result type of the tool (with type parameters still present).
     * @param ctxt The execution context from the invocation expression.
     * @return The return value.
     */
    private static Object eval(JavaTool tool, Object[] args, ToolDefType resultType, ExecContext ctxt) {
        // Get Java method.
        Method method = tool.getMethod();

        // Change argument for variadic parameter, if applicable.
        if (method.isVarArgs()) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>)args[args.length - 1];
            Object array = varargListToArray(method, list);
            args[args.length - 1] = array;
        }

        // Invoke Java method.
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        if (ctxt.isTerminationRequested()) {
            throw new ExitException(0);
        }

        Object rslt;
        try {
            rslt = method.invoke(null, args);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Java method invoke failed.", ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Java method invoke failed.", ex);
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof ToolDefException) {
                throw (ToolDefException)ex.getCause();
            } else if (ex.getCause() instanceof ExitException) {
                throw (ExitException)ex.getCause();
            } else {
                throw new RuntimeException("Java method invoke failed.", ex);
            }
        }

        if (ctxt.isTerminationRequested()) {
            throw new ExitException(0);
        }

        // Check and normalize resulting value.
        boolean hasReturnType = !tool.getReturnTypes().isEmpty();
        if (hasReturnType) {
            try {
                rslt = normalizeJavaReturnValue(rslt, resultType);
            } catch (RuntimeException ex) {
                String rsltTxt;
                try {
                    rsltTxt = (rslt == null) ? "null" : rslt.toString();
                } catch (Exception ignoreEx) {
                    String msg = fmt("Java method result doesn't fit in type \"%s\".",
                            ToolDefTextUtils.typeToStr(resultType));
                    throw new RuntimeException(msg, ex);
                }

                String msg = fmt("Java method result %s doesn't fit in type \"%s\".", rsltTxt,
                        ToolDefTextUtils.typeToStr(resultType));
                throw new RuntimeException(msg, ex);
            }
        }

        // Return resulting value.
        return rslt;
    }

    /**
     * Converts a {@code List<Object>} to a {@code Object[]}, for a varargs parameter, so that the resulting array can
     * be used as argument for the varargs parameter of the given Java method, for reflective invocation.
     *
     * @param method The Java method with a varargs parameter.
     * @param values The {@code List<Object>} with values for the varargs parameter.
     * @return The {@code Object[]} with values for the varargs parameter.
     */
    private static Object varargListToArray(Method method, List<Object> values) {
        // Get type of vararg parameter.
        Type[] paramTypes = method.getGenericParameterTypes();
        Type lastType = paramTypes[paramTypes.length - 1];

        // Get element type of varargs parameter.
        Class<?> elemType;
        if (lastType instanceof Class) {
            Class<?> cls = (Class<?>)lastType;
            Assert.check(cls.isArray());
            elemType = cls.getComponentType();
        } else if (lastType instanceof GenericArrayType) {
            GenericArrayType gat = (GenericArrayType)lastType;
            Type genElemType = gat.getGenericComponentType();
            ParameterizedType ptElemType = (ParameterizedType)genElemType;
            elemType = (Class<?>)ptElemType.getRawType();
        } else {
            throw new RuntimeException("Unknown array type: " + lastType);
        }

        // Create new array via reflection, to ensure proper element type. Then
        // set the values using reflection as well, and return the array.
        Object rslt = Array.newInstance(elemType, values.size());
        for (int i = 0; i < values.size(); i++) {
            Array.set(rslt, i, values.get(i));
        }
        return rslt;
    }

    /**
     * Check and normalize value resulting from an invocation of a Java tool.
     *
     * <p>
     * Java guarantees that the resulting value fits within the return type. However, for a result type of
     * '{@code List<T>}' ('{@code list? T}' in ToolDef), if we fill in '{@code bool}' for '{@code T}' in ToolDef, we
     * need to fill in '{@code Boolean}' in Java, which is '{@code bool?}' in ToolDef. Therefore, we may get more
     * {@code null} values than expected.
     * </p>
     *
     * <p>
     * We need to check for values returned by Java that fit in the type, but are invalid values in ToolDef (e.g.
     * {@code NaN}).
     * </p>
     *
     * <p>
     * We need to normalize values (e.g. '{@code -0.0}' to '{@code 0.0}').
     * </p>
     *
     * @param value The value to check and normalize.
     * @param type The return type of the Java method.
     * @return The normalized value.
     * @throws RuntimeException If the check fails. The message of the exception may be {@code null}.
     * @see #evalCast
     */
    private static Object normalizeJavaReturnValue(Object value, ToolDefType type) {
        // Normalize type.
        type = ToolDefTypeUtils.normalizeType(type);

        // Null value.
        if (value == null) {
            if (type instanceof TypeParamRef) {
                // FIXME: We need unification of parameter types against actual
                // values for tool invocations, which we need to store
                // in the execution context to allow later retrieval.
                // We can then retrieve them here, and fill in them in,
                // to check against actual types. We currently don't
                // do this and thus allow 'null' values that might now
                // fit the actual type.
                return null;
            }

            if (type.isNullable()) {
                return null;
            }

            throw new RuntimeException("not nullable");
        }

        // Simple types.
        if (type instanceof BoolType) {
            if (value instanceof Boolean) {
                return value;
            }

            throw new RuntimeException("not bool");
        } else if (type instanceof IntType) {
            if (value instanceof Integer) {
                return value;
            }

            throw new RuntimeException("not int");
        } else if (type instanceof LongType) {
            if (value instanceof Integer) {
                return value;
            }
            if (value instanceof Long) {
                return value;
            }

            throw new RuntimeException("not long");
        } else if (type instanceof DoubleType) {
            if (value instanceof Integer) {
                return value;
            }
            if (value instanceof Long) {
                return value;
            }
            if (value instanceof Double) {
                double d = (Double)value;
                if (Double.isNaN(d)) {
                    throw new RuntimeException("NaN");
                }
                if (Double.isInfinite(d)) {
                    throw new RuntimeException("inf");
                }
                return (d == -0.0) ? 0.0 : d;
            }

            throw new RuntimeException("not double");
        } else if (type instanceof StringType) {
            if (value instanceof String) {
                return value;
            }

            throw new RuntimeException("not string");
        } else if (type instanceof ObjectType) {
            return value;
        }

        // Container types.
        if (type instanceof ListType) {
            if (!(value instanceof ToolDefList)) {
                throw new RuntimeException("not list");
            }

            @SuppressWarnings("unchecked")
            List<Object> list = (ToolDefList<Object>)value;
            List<Object> rslt = null;

            ToolDefType elemType = ((ListType)type).getElemType();
            for (int i = 0; i < list.size(); i++) {
                // Process element recursively. If unchanged, move to next one.
                Object elem = list.get(i);
                Object newElem = normalizeJavaReturnValue(elem, elemType);
                if (elem == newElem) {
                    continue;
                }

                // Element changed. Copy list if not yet done, and set new
                // element.
                if (rslt == null) {
                    rslt = new ToolDefList<>(list);
                }
                rslt.set(i, newElem);
            }
            return (rslt == null) ? list : rslt;
        } else if (type instanceof SetType) {
            if (!(value instanceof ToolDefSet)) {
                throw new RuntimeException("not set");
            }

            @SuppressWarnings("unchecked")
            Set<Object> set = (ToolDefSet<Object>)value;
            Set<Object> rslt = new ToolDefSet<>(set.size());

            ToolDefType elemType = ((SetType)type).getElemType();
            for (Object elem: set) {
                Object newElem = normalizeJavaReturnValue(elem, elemType);
                rslt.add(newElem);
            }
            Assert.check(rslt.size() == set.size());
            return rslt;
        } else if (type instanceof MapType) {
            if (!(value instanceof ToolDefMap)) {
                throw new RuntimeException("not map");
            }

            @SuppressWarnings("unchecked")
            Map<Object, Object> map = (ToolDefMap<Object, Object>)value;
            Map<Object, Object> rslt = new ToolDefMap<>(map.size());

            ToolDefType keyType = ((MapType)type).getKeyType();
            ToolDefType valueType = ((MapType)type).getValueType();
            for (Entry<Object, Object> entry: map.entrySet()) {
                Object newKey = normalizeJavaReturnValue(entry.getKey(), keyType);
                Object newValue = normalizeJavaReturnValue(entry.getValue(), valueType);
                rslt.put(newKey, newValue);
            }
            Assert.check(rslt.size() == map.size());
            return rslt;
        } else if (type instanceof TupleType) {
            if (!(value instanceof ToolDefTuple)) {
                throw new RuntimeException("not tuple");
            }

            ToolDefTuple tuple = (ToolDefTuple)value;

            List<Object> values = ToolDefRuntimeUtils.unpackTuple(tuple);
            List<ToolDefType> types = ((TupleType)type).getFields();
            if (values.size() != types.size()) {
                throw new RuntimeException("different tuple sizes");
            }
            for (int i = 0; i < values.size(); i++) {
                Object newValue = normalizeJavaReturnValue(values.get(i), types.get(i));
                values.set(i, newValue);
            }
            return ToolDefRuntimeUtils.makeTuple(values);
        }

        // Type parameters.
        if (type instanceof TypeParamRef) {
            // FIXME: We need unification of parameter types against actual
            // values for tool invocations, which we need to store
            // in the execution context to allow later retrieval.
            // We can then retrieve them here, and fill in them in,
            // to check against actual types. We currently don't
            // do this and thus allow values that might now fit the
            // actual type.
            return value;
        }

        // Unknown/unsupported types.
        throw new RuntimeException("Unknown/unsupported type: " + type);
    }
}
