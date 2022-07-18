//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck;

import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;

/** Global guard MDD tree for an event. */
public class GlobalEventGuardUpdate {
    /** Event associated with the MDD trees in the instance. */
    public final Event event;

    /**
     * Global guard of the event, {@code null} if the guard is not initialized, else the required values of the model
     * variables to enable the event at model level.
     */
    private Node globalGuard = null;

    /**
     * Query whether the event has been initialized.
     *
     * @return Whether the event has been initialized.
     */
    public boolean isInitialized() {
        return globalGuard != null;
    }

    /**
     * Constructor of the {@link GlobalEventGuardUpdate} class.
     *
     * @param event Event associated with the MDD trees in the instance.
     */
    public GlobalEventGuardUpdate(Event event) {
        this.event = event;
    }

    /**
     * Update the guard by adding the given guard to it.
     *
     * @param additionalGuard Additional condition to apply to the guard, first addition is taken as-is, other additions
     *     restrict the guard.
     * @param tree MDD builder instance.
     */
    public void update(Node additionalGuard, Tree tree) {
        if (globalGuard == null) {
            globalGuard = additionalGuard;
        } else {
            globalGuard = tree.conjunct(globalGuard, additionalGuard);
        }
    }

    /**
     * Retrieve the global guard of the event, guard must have been initialized before by using {@link #update}.
     *
     * @return The global guard of the event.
     */
    public Node getGuard() {
        Assert.notNull(globalGuard);
        return globalGuard;
    }
}
