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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.common.java.Sets.copy;

import java.util.Collections;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/** Expression type checking context. */
public class ExprContext {
    /** Default (empty) expression context. */
    public static final ExprContext DEFAULT_CTXT = new ExprContext(Collections.emptySet(), null);

    /** The exceptional context conditions. Is immutable. */
    public final Set<Condition> conditions;

    /** The type of values received for the current edge, or {@code null} if not applicable. */
    public final CifType receiveType;

    /**
     * Constructor for the {@link ExprContext} class.
     *
     * @param conditions The exceptional context conditions. May be mutable.
     * @param receiveType The type of values received for this edge, or {@code null} if not applicable.
     */
    private ExprContext(Set<Condition> conditions, CifType receiveType) {
        this.conditions = Collections.unmodifiableSet(conditions);
        this.receiveType = receiveType;
    }

    /**
     * Creates a new expression context, equal to this expression context, but with the given exceptional expression
     * context condition added to it.
     *
     * @param condition The exceptional expression context condition to add.
     * @return The new expression context.
     */
    public ExprContext add(Condition condition) {
        if (conditions.contains(condition)) {
            return this;
        }

        Set<Condition> newConditions = copy(conditions);
        newConditions.add(condition);
        return new ExprContext(newConditions, receiveType);
    }

    /**
     * Creates a new expression context, equal to this expression context, but with the given exceptional expression
     * context condition removed from it.
     *
     * @param condition The exceptional expression context condition to remove.
     * @return The new expression context.
     */
    public ExprContext remove(Condition condition) {
        if (!conditions.contains(condition)) {
            return this;
        }

        Set<Condition> newConditions = copy(conditions);
        newConditions.remove(condition);
        return new ExprContext(newConditions, receiveType);
    }

    /**
     * Creates a new expression context, equal to this expression context, but with a newly set type of received values
     * for the current edge.
     *
     * @param receiveType The type of values received for the current edge, or {@code null} if not applicable.
     * @return The new expression context.
     */
    public ExprContext setReceiveType(CifType receiveType) {
        if (receiveType == this.receiveType) {
            return this;
        }
        return new ExprContext(conditions, receiveType);
    }

    /** Exceptional expression context conditions (what is/isn't allowed). */
    public enum Condition {
        /** Disallow use of variable 'time' (currently only in functions). */
        NO_TIME,

        /**
         * Allow use of distribution standard library functions (currently only in the initial values of discrete
         * variables, declared in automata).
         */
        ALLOW_DIST,

        /**
         * Allow use of event references (currently only in a few places where event references are explicitly the only
         * thing that is allowed).
         */
        ALLOW_EVENT;
    }
}
