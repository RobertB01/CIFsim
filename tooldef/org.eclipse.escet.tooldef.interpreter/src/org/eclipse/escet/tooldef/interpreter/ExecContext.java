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

package org.eclipse.escet.tooldef.interpreter;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;

/** ToolDef execution context. */
public class ExecContext {
    /** The ToolDef interpreter application. */
    public final ToolDefInterpreterApp interpreter;

    /** The parent execution context, or {@code null} if not available. */
    private final ExecContext parent;

    /**
     * Valuation that maps variables and tool parameters to their current values. May be {@code null} to indicate an
     * empty mapping.
     */
    private Map<PositionObject, Object> valuation = null;

    /**
     * Constructor for the {@link ExecContext} class.
     *
     * @param interpreter The ToolDef interpreter application.
     */
    public ExecContext(ToolDefInterpreterApp interpreter) {
        this.interpreter = interpreter;
        this.parent = null;
    }

    /**
     * Constructor for the {@link ExecContext} class.
     *
     * @param parent The parent execution context.
     */
    public ExecContext(ExecContext parent) {
        Assert.notNull(parent);
        Assert.notNull(parent.interpreter);
        this.interpreter = parent.interpreter;
        this.parent = parent;
    }

    /**
     * Is termination of the application requested?
     *
     * @return {@code true} if termination is requested, {@code false} otherwise.
     */
    public boolean isTerminationRequested() {
        return interpreter.isTerminationRequested();
    }

    /**
     * Adds a variable to the execution context.
     *
     * @param var The variable to add.
     * @param value The initial value of the variable.
     */
    public void addVariable(Variable var, Object value) {
        if (valuation == null) {
            valuation = map();
        }
        Assert.check(!valuation.containsKey(var));
        valuation.put(var, value);
    }

    /**
     * Adds a tool parameter to the execution context.
     *
     * @param param The tool parameter to add.
     * @param value The initial value of the tool parameter.
     */
    public void addToolParam(ToolParameter param, Object value) {
        if (valuation == null) {
            valuation = map();
        }
        Assert.check(!valuation.containsKey(param));
        valuation.put(param, value);
    }

    /**
     * Returns the current value of a variable or tool parameter.
     *
     * @param obj The variable or tool parameter.
     * @return The current value.
     */
    public Object getValue(PositionObject obj) {
        // Use value from this context.
        if (valuation != null && valuation.containsKey(obj)) {
            return valuation.get(obj);
        }

        // Delegate to parent context.
        if (parent != null) {
            return parent.getValue(obj);
        }

        // Variable or tool parameter not found.
        throw new RuntimeException("Variable or tool parameter not found: " + obj);
    }

    /**
     * Updates the value of a variable or tool parameter.
     *
     * @param obj The variable or tool parameter.
     * @param value The new value.
     */
    public void updateValue(PositionObject obj, Object value) {
        // Update value in this context.
        if (valuation != null && valuation.containsKey(obj)) {
            valuation.put(obj, value);
            return;
        }

        // Delegate to parent context.
        if (parent != null) {
            parent.updateValue(obj, value);
            return;
        }

        // Variable or tool parameter not found.
        throw new RuntimeException("Variable or tool parameter not found: " + obj);
    }
}
