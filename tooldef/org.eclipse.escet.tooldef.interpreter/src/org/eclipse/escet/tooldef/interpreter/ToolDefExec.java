//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.exprToStr;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.getAbsName;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.normalizeType;
import static org.eclipse.escet.tooldef.interpreter.ToolDefEval.eval;
import static org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils.valueToStr;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.Import;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.runtime.ExitException;
import org.eclipse.escet.tooldef.runtime.ToolDefException;
import org.eclipse.escet.tooldef.runtime.ToolDefList;
import org.eclipse.escet.tooldef.runtime.ToolDefMap;
import org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils;
import org.eclipse.escet.tooldef.runtime.ToolDefTuple;
import org.eclipse.escet.tooldef.runtime.builtins.BuiltInDataTools;

/** ToolDef interpreter statement execution. */
public class ToolDefExec {
    /** Constructor for the {@link ToolDefExec} class. */
    private ToolDefExec() {
        // Static class.
    }

    /**
     * Executes the given statements.
     *
     * @param statements The statements to execute.
     * @param ctxt The execution context.
     * @return The return value, or {@code null} if no 'return' statement encountered.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     * @throws BreakException In case a 'break' statement was encountered.
     * @throws ContinueException In case a 'continue' statement was encountered.
     */
    public static ToolDefReturnValue execute(List<Declaration> statements, ExecContext ctxt) {
        for (Declaration statement: statements) {
            ToolDefReturnValue retValue = execute(statement, ctxt);
            if (retValue != null) {
                return retValue;
            }
            if (ctxt.isTerminationRequested()) {
                return null;
            }
        }
        return null;
    }

    /**
     * Executes the given statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return The return value, or {@code null} if no 'return' statement encountered.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     * @throws BreakException In case a 'break' statement was encountered.
     * @throws ContinueException In case a 'continue' statement was encountered.
     */
    private static ToolDefReturnValue execute(Declaration statement, ExecContext ctxt) {
        // Skip declarations etc that are not actual statements.
        if (statement instanceof Import) {
            return null;
        } else if (statement instanceof Script) {
            return null;
        } else if (statement instanceof Tool) {
            return null;
        } else if (statement instanceof TypeDecl) {
            return null;
        }

        // Execute actual statements.
        if (statement instanceof AssignmentStatement) {
            return execute((AssignmentStatement)statement, ctxt);
        } else if (statement instanceof BreakStatement) {
            return execute((BreakStatement)statement, ctxt);
        } else if (statement instanceof ContinueStatement) {
            return execute((ContinueStatement)statement, ctxt);
        } else if (statement instanceof ExitStatement) {
            return execute((ExitStatement)statement, ctxt);
        } else if (statement instanceof ForStatement) {
            return execute((ForStatement)statement, ctxt);
        } else if (statement instanceof IfStatement) {
            return execute((IfStatement)statement, ctxt);
        } else if (statement instanceof ReturnStatement) {
            return execute((ReturnStatement)statement, ctxt);
        } else if (statement instanceof ToolInvokeStatement) {
            return execute((ToolInvokeStatement)statement, ctxt);
        } else if (statement instanceof Variable) {
            return execute((Variable)statement, ctxt);
        } else if (statement instanceof WhileStatement) {
            return execute((WhileStatement)statement, ctxt);
        }

        // Unknown statement.
        throw new RuntimeException("Unknown statement: " + statement);
    }

    /**
     * Executes the given assignment statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return {@code null} for no 'return' statement encountered.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     */
    private static ToolDefReturnValue execute(AssignmentStatement statement, ExecContext ctxt) {
        // Evaluate values.
        List<Object> values = new ToolDefList<>(statement.getValues().size());
        for (Expression valueExpr: statement.getValues()) {
            Object value;
            try {
                value = eval(valueExpr, ctxt);
            } catch (ToolDefException ex) {
                String msg = fmt("Failed to evaluate assignment value \"%s\".", exprToStr(valueExpr));
                throw new ToolDefException(msg, ex);
            }
            values.add(value);

            if (ctxt.isTerminationRequested()) {
                return null;
            }
        }

        // Get single value.
        Object value;
        if (values.size() == 1) {
            value = values.get(0);
        } else {
            value = ToolDefRuntimeUtils.makeTuple(values);
        }

        // Assign values to the addressables.
        Map<PositionObject, Object> newValues = map();
        assign(statement.getAddressables(), value, ctxt, newValues);
        for (Entry<PositionObject, Object> entry: newValues.entrySet()) {
            ctxt.updateValue(entry.getKey(), entry.getValue());
        }
        return null;
    }

    /**
     * Assigns new values to variables and tool parameters.
     *
     * @param addressables The addressables.
     * @param value The value for the addressables.
     * @param ctxt The execution context (current values).
     * @param newValues Mapping from variables and tool parameters to their new values.
     */
    private static void assign(List<Expression> addressables, Object value, ExecContext ctxt,
            Map<PositionObject, Object> newValues)
    {
        if (addressables.size() == 1) {
            // Single addressable.
            Expression addr = addressables.get(0);
            if (addr instanceof TupleExpression) {
                // Multiple addressables after all.
                TupleExpression taddr = (TupleExpression)addr;
                assign(taddr.getElements(), value, ctxt, newValues);
            } else {
                // Single variable addressable.
                assign(addr, value, ctxt, newValues);
            }
        } else {
            // Multiple addressables.
            Assert.check(value instanceof ToolDefTuple);
            ToolDefTuple tuple = (ToolDefTuple)value;
            List<Object> elems = ToolDefRuntimeUtils.unpackTuple(tuple);
            Assert.check(addressables.size() == elems.size());
            for (int i = 0; i < addressables.size(); i++) {
                assign(list(addressables.get(i)), elems.get(i), ctxt, newValues);
            }
        }
    }

    /**
     * Assign a new value to (a part of) a variable.
     *
     * @param addressable The addressable (variable reference with optional projections).
     * @param newValue The new value for the addressable.
     * @param ctxt The execution context.
     * @param newValues Mapping from variables and tool parameters to their new values.
     */
    private static void assign(Expression addressable, Object newValue, ExecContext ctxt,
            Map<PositionObject, Object> newValues)
    {
        // Collect the projections, while stripping them off.
        List<ProjectionExpression> projs = list();
        while (addressable instanceof ProjectionExpression) {
            ProjectionExpression paddr = (ProjectionExpression)addressable;
            projs.add(paddr);
            addressable = paddr.getChild();
        }
        Collections.reverse(projs);

        // Get variable or tool parameter being projected.
        PositionObject obj;
        if (addressable instanceof VariableExpression) {
            obj = ((VariableExpression)addressable).getVariable();
        } else if (addressable instanceof ToolParamExpression) {
            obj = ((ToolParamExpression)addressable).getParam();
        } else {
            throw new RuntimeException("Unknown addr var ref: " + addressable);
        }

        // Get current variable or tool parameter value.
        Object curValue = ctxt.getValue(obj);

        // Evaluate the projection indices.
        List<Object> indices = listc(projs.size());
        for (int i = 0; i < projs.size(); i++) {
            // Get projection.
            ProjectionExpression proj = projs.get(i);

            // Evaluate and add index.
            try {
                indices.add(eval(proj.getIndex(), ctxt));
            } catch (ToolDefException ex) {
                String msg = fmt("Failed to evaluate projection index \"%s\".", exprToStr(proj.getIndex()));
                throw new ToolDefException(msg, ex);
            }

            if (ctxt.isTerminationRequested()) {
                return;
            }
        }

        // Get the normalized types of the projected children.
        List<ToolDefType> childTypes = listc(projs.size());
        for (ProjectionExpression proj: projs) {
            ToolDefType type = normalizeType(proj.getChild().getType());
            childTypes.add(type);
        }

        // Compute and store new value.
        Object fullNewValue = computeNewValue(curValue, indices, 0, newValue);
        Assert.check(!newValues.containsKey(obj));
        newValues.put(obj, fullNewValue);
    }

    /**
     * Computes the new value of (a part of) a variable or tool parameter.
     *
     * @param curValue The current value of the (part of a) variable or tool parameter.
     * @param indices The projection indices (already processed ones as well as not yet processed ones).
     * @param i The 0-based index of the current projection. Is equal to the number of projections if no more
     *     projections need to be processed.
     * @param newValue The new value for the part of the variable or tool parameter after all projections have been
     *     processed.
     * @return The new value of the (part of a) variable or tool parameter.
     */
    private static Object computeNewValue(Object curValue, List<Object> indices, int i, Object newValue) {
        // Base case: no unprocessed projections.
        if (i == indices.size()) {
            return newValue;
        }

        // Recursive case: unprocessed projections.
        Object idx = indices.get(i);

        if (curValue instanceof ToolDefList) {
            // Get list.
            @SuppressWarnings("unchecked")
            List<Object> oldList = (List<Object>)curValue;

            // Normalize index.
            int normalizedIdx = (int)idx;
            if (normalizedIdx < 0) {
                normalizedIdx = oldList.size() + normalizedIdx;
            }

            // Check for out of bounds.
            if (normalizedIdx < 0 || normalizedIdx >= oldList.size()) {
                String msg = fmt("Index out of bounds: %s[%s].", valueToStr(oldList), valueToStr(idx));
                throw new ToolDefException(msg);
            }

            // Get child.
            Object oldChild = oldList.get(normalizedIdx);

            // Compute new child.
            Object newChild = computeNewValue(oldChild, indices, i + 1, newValue);

            // Re-wrap child.
            List<Object> newList = new ToolDefList<>(oldList);
            newList.set(normalizedIdx, newChild);
            return newList;
        } else if (curValue instanceof ToolDefMap) {
            // Get map.
            @SuppressWarnings("unchecked")
            Map<Object, Object> oldMap = (Map<Object, Object>)curValue;

            // Check for non-existing key, for all but last projection.
            if (i < indices.size() - 1 && !oldMap.containsKey(idx)) {
                String msg = fmt("Key not found: %s[%s].", valueToStr(oldMap), valueToStr(idx));
                throw new ToolDefException(msg);
            }

            // Get child. For the last projection, key may not exist, and we
            // get 'null'. This is no problem, as then the child is not used in
            // the recursive call.
            Object oldChild = oldMap.get(idx);

            // Compute new child.
            Object newChild = computeNewValue(oldChild, indices, i + 1, newValue);

            // Re-wrap child.
            Map<Object, Object> newMap = new ToolDefMap<>(oldMap);
            newMap.put(idx, newChild);
            return newMap;
        } else if (curValue instanceof ToolDefTuple) {
            // Get tuple and its elements.
            ToolDefTuple oldTuple = (ToolDefTuple)curValue;
            List<Object> oldElems = ToolDefRuntimeUtils.unpackTuple(oldTuple);

            // Get child. Can't fail as the type checker ensures the index is
            // correct.
            Object oldChild = oldElems.get((int)idx);

            // Compute new child.
            Object newChild = computeNewValue(oldChild, indices, i + 1, newValue);

            // Re-wrap child.
            List<Object> newElems = new ToolDefList<>(oldElems);
            newElems.set((int)idx, newChild);
            return ToolDefRuntimeUtils.makeTuple(newElems);
        } else {
            throw new RuntimeException("Unknown projectable: " + curValue);
        }
    }

    /**
     * Executes the given break statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return {@code null} for no 'return' statement encountered.
     * @throws BreakException Always thrown.
     */
    private static ToolDefReturnValue execute(BreakStatement statement, ExecContext ctxt) {
        throw new BreakException();
    }

    /**
     * Executes the given continue statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return {@code null} for no 'return' statement encountered.
     * @throws ContinueException Always thrown.
     */
    private static ToolDefReturnValue execute(ContinueStatement statement, ExecContext ctxt) {
        throw new ContinueException();
    }

    /**
     * Executes the given exit statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return {@code null} for no 'return' statement encountered.
     * @throws ExitException Always thrown.
     */
    private static ToolDefReturnValue execute(ExitStatement statement, ExecContext ctxt) {
        // If no exit code, use '0'.
        if (statement.getExitCode() == null) {
            throw new ExitException(0);
        }

        // Evaluate exit code.
        int exitCode;
        try {
            exitCode = (int)eval(statement.getExitCode(), ctxt);
        } catch (ToolDefException ex) {
            String msg = fmt("Failed to evaluate exit code \"%s\".", exprToStr(statement.getExitCode()));
            throw new ToolDefException(msg, ex);
        }

        // Return the exit code.
        throw new ExitException(exitCode);
    }

    /**
     * Executes the given for statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return The return value, or {@code null} if no 'return' statement encountered.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     */
    private static ToolDefReturnValue execute(ForStatement statement, ExecContext ctxt) {
        // Evaluate source.
        Object source;
        try {
            source = eval(statement.getSource(), ctxt);
        } catch (ToolDefException ex) {
            String msg = fmt("Failed to evaluate for statement source \"%s\".", exprToStr(statement.getSource()));
            throw new ToolDefException(msg, ex);
        }

        if (ctxt.isTerminationRequested()) {
            return null;
        }

        // Get actual source, which may be different from the evaluated source
        // value.
        ToolDefType sourceType = statement.getSource().getType();
        sourceType = ToolDefTypeUtils.normalizeType(sourceType);
        if (sourceType instanceof ListType) {
            // No change.
        } else if (sourceType instanceof SetType) {
            // No change.
        } else if (sourceType instanceof MapType) {
            // We iterate over the entries, not the map. Also, we need 2-tuples
            // instead of the entries.
            source = BuiltInDataTools.entries((Map<?, ?>)source);
        } else {
            throw new RuntimeException("Unknown for source type: " + sourceType);
        }

        // Iterate over the source.
        @SuppressWarnings("unchecked")
        Iterable<Object> iterable = (Iterable<Object>)source;
        for (Object elem: iterable) {
            // Create new execution context for the current iteration of the
            // body.
            ExecContext forCtxt = new ExecContext(ctxt);

            // Add variables.
            addForVariables(statement.getAddressables(), elem, forCtxt);

            // Execute body.
            List<Declaration> body = cast(statement.getStatements());
            try {
                ToolDefReturnValue retValue = execute(body, forCtxt);
                if (retValue != null) {
                    return retValue;
                }
            } catch (BreakException ex) {
                break;
            } catch (ContinueException ex) {
                continue;
            }

            if (ctxt.isTerminationRequested()) {
                return null;
            }
        }
        return null;
    }

    /**
     * Adds variables for the addressables of a 'for' statement to the given context of the 'for' statement.
     *
     * @param addressables The addressables.
     * @param value The value for the addressables.
     * @param ctxt The execution context.
     */
    private static void addForVariables(List<AddressableDecl> addressables, Object value, ExecContext ctxt) {
        if (addressables.size() == 1) {
            // Single addressable.
            AddressableDecl addr = addressables.get(0);
            if (addr instanceof TupleAddressableDecl) {
                // Multiple addressables after all.
                TupleAddressableDecl taddr = (TupleAddressableDecl)addr;
                addForVariables(taddr.getElements(), value, ctxt);
            } else {
                // Single variable addressable.
                Assert.check(addr instanceof VariableAddressableDecl);
                VariableAddressableDecl vaddr = (VariableAddressableDecl)addr;
                Variable var = vaddr.getVariable();
                ctxt.addVariable(var, value);
            }
        } else {
            // Multiple addressables.
            Assert.check(value instanceof ToolDefTuple);
            ToolDefTuple tuple = (ToolDefTuple)value;
            List<Object> elems = ToolDefRuntimeUtils.unpackTuple(tuple);
            Assert.check(addressables.size() == elems.size());
            for (int i = 0; i < addressables.size(); i++) {
                addForVariables(list(addressables.get(i)), elems.get(i), ctxt);
            }
        }
    }

    /**
     * Executes the given if statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return The return value, or {@code null} if no 'return' statement encountered.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     * @throws BreakException In case a 'break' statement was encountered.
     * @throws ContinueException In case a 'continue' statement was encountered.
     */
    private static ToolDefReturnValue execute(IfStatement statement, ExecContext ctxt) {
        // Evaluate 'if' condition.
        boolean condition;
        try {
            condition = (boolean)eval(statement.getCondition(), ctxt);
        } catch (ToolDefException ex) {
            String msg = fmt("Failed to evaluate 'if' condition \"%s\".", exprToStr(statement.getCondition()));
            throw new ToolDefException(msg, ex);
        }

        if (ctxt.isTerminationRequested()) {
            return null;
        }

        // Execute 'if' statements if 'if' condition holds.
        if (condition) {
            ExecContext ifCtxt = new ExecContext(ctxt);
            List<Declaration> body = cast(statement.getThens());
            return execute(body, ifCtxt);
        }

        // Process the 'elif' parts.
        for (ElifStatement elif: statement.getElifs()) {
            // Evaluate 'elif' condition.
            try {
                condition = (boolean)eval(elif.getCondition(), ctxt);
            } catch (ToolDefException ex) {
                String msg = fmt("Failed to evaluate 'elif' condition \"%s\".", exprToStr(elif.getCondition()));
                throw new ToolDefException(msg, ex);
            }

            if (ctxt.isTerminationRequested()) {
                return null;
            }

            // Execute 'elif' statements if 'elif' condition holds.
            if (condition) {
                ExecContext elifCtxt = new ExecContext(ctxt);
                List<Declaration> body = cast(elif.getThens());
                return execute(body, elifCtxt);
            }
        }

        // Execute 'else' statements. In the metamodel, we can't distinguish
        // absence of 'else' and an empty block of 'else' statements. However,
        // in all cases we can just execute the statements.
        ExecContext elseCtxt = new ExecContext(ctxt);
        List<Declaration> body = cast(statement.getElses());
        return execute(body, elseCtxt);
    }

    /**
     * Executes the given return statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return The return value. Is never {@code null} although the {@link ToolDefReturnValue#value} may be
     *     {@code null}.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     */
    private static ToolDefReturnValue execute(ReturnStatement statement, ExecContext ctxt) {
        // Evaluate return values.
        List<Object> values = new ToolDefList<>(statement.getValues().size());
        for (Expression valueExpr: statement.getValues()) {
            Object value;
            try {
                value = eval(valueExpr, ctxt);
            } catch (ToolDefException ex) {
                String msg = fmt("Failed to evaluate return value \"%s\".", exprToStr(valueExpr));
                throw new ToolDefException(msg, ex);
            }
            values.add(value);
        }

        if (ctxt.isTerminationRequested()) {
            return null;
        }

        // Get single return value.
        Object value = null;
        if (values.size() == 1) {
            value = values.get(0);
        } else if (values.size() > 1) {
            value = ToolDefRuntimeUtils.makeTuple(values);
        }

        // Return the return value.
        return new ToolDefReturnValue(value);
    }

    /**
     * Executes the given tool invocation statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return {@code null} for no 'return' statement encountered.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     */
    private static ToolDefReturnValue execute(ToolInvokeStatement statement, ExecContext ctxt) {
        eval(statement.getInvocation(), ctxt);
        return null;
    }

    /**
     * Executes the given variable declaration statement.
     *
     * @param var The variable declaration
     * @param ctxt The execution context.
     * @return {@code null} for no 'return' statement encountered.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     */
    private static ToolDefReturnValue execute(Variable var, ExecContext ctxt) {
        // Get initial value.
        Object value;
        if (var.getValue() == null) {
            // Default value for its data type.
            value = ToolDefRuntimeUtils.getDefaultValue(var.getType());
        } else {
            // Explicit initial value.
            try {
                value = eval(var.getValue(), ctxt);
            } catch (ToolDefException ex) {
                String msg = fmt("Failed to evaluate initial value \"%s\" of variable \"%s\".",
                        exprToStr(var.getValue()), getAbsName(var));
                throw new ToolDefException(msg, ex);
            }
        }

        // Add variable to context.
        ctxt.addVariable(var, value);
        return null;
    }

    /**
     * Executes the given while statement.
     *
     * @param statement The statement to execute.
     * @param ctxt The execution context.
     * @return {@code null} for no 'return' statement encountered.
     * @throws ExitException In case an 'exit' statement was encountered, or execution should stop with a specific exit
     *     code for any other reason.
     */
    private static ToolDefReturnValue execute(WhileStatement statement, ExecContext ctxt) {
        while (true) {
            // Evaluate condition.
            boolean condition;
            try {
                condition = (boolean)eval(statement.getCondition(), ctxt);
            } catch (ToolDefException ex) {
                String msg = fmt("Failed to evaluate while condition \"%s\".", exprToStr(statement.getCondition()));
                throw new ToolDefException(msg, ex);
            }

            if (ctxt.isTerminationRequested()) {
                return null;
            }

            // Stop if condition doesn't hold.
            if (!condition) {
                break;
            }

            // Create new execution context for the current iteration of the
            // body.
            ExecContext whileCtxt = new ExecContext(ctxt);

            // Execute body.
            try {
                List<Declaration> body = cast(statement.getStatements());
                ToolDefReturnValue retValue = execute(body, whileCtxt);
                if (retValue != null) {
                    return retValue;
                }
            } catch (BreakException ex) {
                break;
            } catch (ContinueException ex) {
                continue;
            }

            if (ctxt.isTerminationRequested()) {
                return null;
            }
        }
        return null;
    }
}
