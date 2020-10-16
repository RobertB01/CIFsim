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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Maps.copy;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;

/**
 * Set of event references (event reference expressions). Ensures value equality, and supports wrapping expressions.
 *
 * <p>
 * This class uses the {@link CifEventUtils#areSameEventRefs} method to determine value equality of event reference
 * expressions. As such, this class only works correctly for reference expressions that are contained in the same scope.
 * </p>
 *
 * <p>
 * Maintains the insertion order of the event reference expressions, for deterministic iteration order.
 * </p>
 */
public class EventRefSet implements Iterable<Expression> {
    /** The set of event references. */
    protected final Map<EventRefWrapper, EventRefWrapper> eventRefs;

    /**
     * The equality notion to use to determine whether two references via component parameters are equal, or
     * {@code null} if not applicable.
     */
    protected final EventEquality equality;

    /**
     * Constructor for the {@link EventRefSet} class. Constructs empty set.
     *
     * @param equality The equality notion to use to determine whether two references via component parameters are
     *     equal, or {@code null} if not applicable.
     */
    public EventRefSet(EventEquality equality) {
        this(map(), equality);
    }

    /**
     * Constructor for the {@link EventRefSet} class. Uses the same equality notion as the given set, to determine
     * whether two references via component parameters are equal.
     *
     * @param set The event reference set to copy.
     */
    public EventRefSet(EventRefSet set) {
        this(copy(set.eventRefs), set.equality);
    }

    /**
     * Constructor for the {@link EventRefSet} class.
     *
     * @param eventRefs The event references with which to initialize the set.
     * @param equality The equality notion to use to determine whether two references via component parameters are
     *     equal, or {@code null} if not applicable.
     */
    private EventRefSet(Map<EventRefWrapper, EventRefWrapper> eventRefs, EventEquality equality) {
        this.eventRefs = eventRefs;
        this.equality = equality;
    }

    /**
     * Is the set of event references empty?
     *
     * @return {@code true} if the set is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return eventRefs.isEmpty();
    }

    /**
     * Is the event reference contained in the set?
     *
     * @param eventRef The event reference.
     * @return {@code true} if the event reference is contained in the set, {@code false} otherwise.
     */
    public boolean contains(Expression eventRef) {
        EventRefWrapper wrapper = new EventRefWrapper(eventRef);
        return eventRefs.containsKey(wrapper);
    }

    /**
     * Get current representative for the given event reference, from the set.
     *
     * @param eventRef The event reference.
     * @return The current representative for the given event reference, from the set, or {@code null} if the event
     *     reference is not contained in the set.
     */
    public Expression get(Expression eventRef) {
        EventRefWrapper wrapper = new EventRefWrapper(eventRef);
        EventRefWrapper entry = eventRefs.get(wrapper);
        return (entry == null) ? null : entry.eventRef;
    }

    /**
     * Adds an event reference to the set.
     *
     * @param eventRef The event reference to add.
     * @return The previous event reference in the set, equal to the new event reference, or {@code null} if the event
     *     reference was not yet in the set.
     */
    public Expression add(Expression eventRef) {
        EventRefWrapper wrapper = new EventRefWrapper(eventRef);
        EventRefWrapper prev = eventRefs.put(wrapper, wrapper);
        return (prev == null) ? null : prev.eventRef;
    }

    /**
     * Removes an event reference to the set.
     *
     * @param eventRef The event reference to remove.
     * @return The removed event reference (equal to the provided event reference,) or {@code null} if no equal event
     *     reference was in the set.
     */
    public Expression remove(Expression eventRef) {
        EventRefWrapper wrapper = new EventRefWrapper(eventRef);
        EventRefWrapper removed = eventRefs.remove(wrapper);
        return (removed == null) ? null : removed.eventRef;
    }

    @Override
    public Iterator<Expression> iterator() {
        return new Iterator<Expression>() {
            private final Iterator<EventRefWrapper> iter = eventRefs.keySet().iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Expression next() {
                return iter.next().eventRef;
            }

            @Override
            public void remove() {
                iter.remove();
            }
        };
    }

    /**
     * Wrapper around an event reference expression. The wrapper implements value equality and hashing. Event references
     * as well as wrapping expressions are supported.
     *
     * @see EventRefSet
     * @see CifEventUtils#areSameEventRefs
     */
    private class EventRefWrapper {
        /** The event reference expression that is being wrapped. */
        public final Expression eventRef;

        /**
         * Constructor for the {@link EventRefWrapper} class.
         *
         * @param eventRef The event reference.
         */
        public EventRefWrapper(Expression eventRef) {
            this.eventRef = eventRef;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof EventRefWrapper)) {
                return false;
            }
            EventRefWrapper other = (EventRefWrapper)obj;
            return CifEventUtils.areSameEventRefs(eventRef, other.eventRef, equality);
        }

        @Override
        public int hashCode() {
            Expression expr = eventRef;
            int hash = 0;
            while (true) {
                hash *= 2;
                if (expr instanceof TauExpression) {
                    hash *= 17;
                    return hash;
                } else if (expr instanceof EventExpression) {
                    hash ^= ((EventExpression)expr).getEvent().hashCode();
                    return hash;
                } else if (expr instanceof CompInstWrapExpression) {
                    CompInstWrapExpression wrap = (CompInstWrapExpression)expr;
                    hash ^= wrap.getInstantiation().hashCode();
                    expr = wrap.getReference();
                } else if (expr instanceof CompParamWrapExpression) {
                    CompParamWrapExpression wrap = (CompParamWrapExpression)expr;
                    CifType ptype = wrap.getParameter().getType();
                    ptype = CifTypeUtils.normalizeType(ptype);
                    ComponentDefType cdefType = (ComponentDefType)ptype;
                    ComponentDef cdef = cdefType.getDefinition();
                    hash ^= cdef.hashCode();
                    expr = wrap.getReference();
                } else {
                    String msg = "Unknown event ref expr: " + eventRef;
                    throw new RuntimeException(msg);
                }
            }
        }
    }
}
