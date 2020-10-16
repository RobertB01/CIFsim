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

package org.eclipse.escet.cif.explorer;

import org.eclipse.escet.cif.explorer.runtime.AbstractStateFactory;
import org.eclipse.escet.cif.explorer.runtime.BaseState;
import org.eclipse.escet.cif.explorer.runtime.Explorer;
import org.eclipse.escet.cif.explorer.runtime.ExplorerState;
import org.eclipse.escet.cif.explorer.runtime.InitialState;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Construct states for the explorer. */
public class ExplorerStateFactory extends AbstractStateFactory {
    /** Constructor of the {@link ExplorerStateFactory} class. */
    public ExplorerStateFactory() {
        super(false);
    }

    @Override
    public InitialState makeInitial(Explorer expl) {
        return new InitialState(expl);
    }

    @Override
    public ExplorerState makeExplorerState(Event event, BaseState prevState) {
        return new ExplorerState(prevState);
    }
}
