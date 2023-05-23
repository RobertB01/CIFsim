//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators;

/** Interface for the generator that construct PLC code for performing CIF events in the PLC. */
public interface TransitionGenerator {
    /**
     * Define the event transitions to generate.
     *
     * @param transitions Description of the event transitions that should be generated.
     */
    void setTransitions(CifEventTransition transitions);

    /** Generate the event transition functions. */
    void process();
}
