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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Maps.copy;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;

/**
 * Set of event references (event reference expressions). Ensures value equality. Doesn't support wrapping expressions.
 *
 * <p>
 * This class does not support specifications that have component definitions/instantiations. In particular, it can't
 * handle wrapping expressions for event references.
 * </p>
 *
 * <p>
 * Maintains the insertion order of the event reference expressions, for deterministic iteration order.
 * </p>
 */
public class EventRefSet implements Iterable<Expression> {
    /** The set of event references. */
    protected final Map<EventRefWrapper, EventRefWrapper> eventRefs;

    /** Constructor for the {@link EventRefSet} class. Constructs empty set. */
    public EventRefSet() {
        this(map());
    }

    /**
     * Constructor for the {@link EventRefSet} class.
     *
     * @param set The event reference set to copy.
     */
    public EventRefSet(EventRefSet set) {
        this(copy(set.eventRefs));
    }

    /**
     * Constructor for the {@link EventRefSet} class.
     *
     * @param eventRefs The event references with which to initialize the set.
     */
    private EventRefSet(Map<EventRefWrapper, EventRefWrapper> eventRefs) {
        this.eventRefs = eventRefs;
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
     * Removes an event reference from the set.
     *
     * @param eventRef The event reference to remove.
     * @return The removed event reference (equal to the provided event reference) or {@code null} if no equal event
     *     reference was in the set.
     */
    public Expression remove(Expression eventRef) {
        EventRefWrapper wrapper = new EventRefWrapper(eventRef);
        EventRefWrapper removed = eventRefs.remove(wrapper);
        return (removed == null) ? null : removed.eventRef;
    }

    /**
     * Removes from this set all of the event references that are contained in the specified event reference set.
     *
     * @param eventRefSet The event reference set.
     */
    public void removeAll(EventRefSet eventRefSet) {
        for (Expression eventRef: eventRefSet) {
            remove(eventRef);
        }
    }

    @Override
    public Iterator<Expression> iterator() {
        return new Iterator<>() {
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
     * are supported. Wrapping expressions are not supported.
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
            return CifEventUtils.areSameEventRefs(eventRef, other.eventRef);
        }

        @Override
        public int hashCode() {
            if (eventRef instanceof TauExpression) {
                return 0;
            } else if (eventRef instanceof EventExpression) {
                return ((EventExpression)eventRef).getEvent().hashCode();
            } else {
                String msg = "Unknown event ref expr: " + eventRef;
                throw new RuntimeException(msg);
            }
        }
    }
}
