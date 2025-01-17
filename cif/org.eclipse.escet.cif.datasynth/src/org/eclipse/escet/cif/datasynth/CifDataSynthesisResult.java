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

package org.eclipse.escet.cif.datasynth;

import static org.eclipse.escet.cif.bdd.utils.BddUtils.bddToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Map;

import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.datasynth.settings.CifDataSynthesisSettings;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

import com.github.javabdd.BDD;

/** The result of CIF data-based synthesis. */
public class CifDataSynthesisResult {
    /** The CIF/BDD specification that represents the CIF specification. */
    public final CifBddSpec cifBddSpec;

    /** The settings to use. */
    public final CifDataSynthesisSettings settings;

    /** Controlled-behavior predicate of the system. */
    public BDD ctrlBeh;

    /** Initialization predicate of the controlled system. Is {@code null} if not yet or no longer available. */
    public BDD initialCtrl;

    /**
     * Initialization predicate to use for the output. Computed as a result of synthesis. Is {@code null} if not yet
     * available, or if no additional initialization predicate is to be added to the output.
     */
    public BDD initialOutput;

    /**
     * Mapping from the controllable events in the {@link CifBddSpec#alphabet} to the guards to use as output of
     * synthesis, when constructing the output CIF model. Is {@code null} until computed as part of synthesis.
     */
    public Map<Event, BDD> outputGuards;

    /**
     * The constructor for the {@link CifDataSynthesisResult} class.
     *
     * @param cifBddSpec The CIF/BDD specification that represents the CIF specification.
     * @param settings The settings to use.
     */
    public CifDataSynthesisResult(CifBddSpec cifBddSpec, CifDataSynthesisSettings settings) {
        this.cifBddSpec = cifBddSpec;
        this.settings = settings;
    }

    @Override
    public String toString() {
        return getCtrlBehText();
    }

    /**
     * Returns a textual representation of the {@link #ctrlBeh controlled behavior}. Uses {@code "State: "} as prefix.
     *
     * @return The textual representation.
     */
    public String getCtrlBehText() {
        return getCtrlBehText("State: ");
    }

    /**
     * Returns a textual representation of the {@link #ctrlBeh controlled behavior}.
     *
     * @param prefix The prefix to use, e.g. {@code "State: "} or {@code ""}.
     * @return The textual representation.
     */
    public String getCtrlBehText(String prefix) {
        StringBuilder txt = new StringBuilder();
        txt.append(prefix);
        String cbTxt = (ctrlBeh == null) ? "?" : bddToStr(ctrlBeh, cifBddSpec);
        txt.append(fmt("(controlled-behavior: %s)", cbTxt));
        return txt.toString();
    }
}
