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

package org.eclipse.escet.cif.datasynth.settings;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.function.Supplier;

import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** CIF data-based synthesis settings. */
public class CifDataSynthesisSettings {
    /**
     * Function that indicates whether termination has been requested. Once it returns {@code true}, it must return
     * {@code true} also on subsequent calls.
     */
    public final Supplier<Boolean> shouldTerminate;

    /** Callback for debug output. */
    public final DebugNormalOutput debugOutput;

    /** Callback for normal output. */
    public final DebugNormalOutput normalOutput;

    /** Callback for warning output. */
    public final WarnOutput warnOutput;

    /**
     * Whether to warn for events that are never enabled in the input specification or always disabled by the
     * synthesized supervisor ({@code true}) or don't warn ({@code false}).
     */
    public final boolean doNeverEnabledEventsWarn;

    /** The order in which the fixed-point computations are to be performed during synthesis. */
    public final FixedPointComputationsOrder fixedPointComputationsOrder;

    /** Whether to perform forward reachability during synthesis ({@code true}) or omit it ({@code false}). */
    public final boolean doForwardReach;

    /** Whether to warn for plants that reference requirement state ({@code true}) or don't warn ({@code false}). */
    public final boolean doPlantsRefReqsWarn;

    /** The way that state requirement invariants are enforced. */
    public final StateReqInvEnforceMode stateReqInvEnforceMode;

    /** The name of the resulting supervisor automaton. */
    public final String supervisorName;

    /** The namespace of the resulting supervisor, or {@code null} to use the empty namespace. */
    public final String supervisorNamespace;

    /** The kinds of statistics to print. */
    public final EnumSet<SynthesisStatistics> synthesisStatistics;

    /**
     * Constructor for the {@link CifDataSynthesisSettings} class.
     *
     * @param shouldTerminate Function that indicates whether termination has been requested. Once it returns
     *     {@code true}, it must return {@code true} also on subsequent calls.
     * @param debugOutput Callback for debug output.
     * @param normalOutput Callback for normal output.
     * @param warnOutput Callback for warning output.
     * @param doNeverEnabledEventsWarn Whether to warn for events that are never enabled in the input specification or
     *     always disabled by the synthesized supervisor ({@code true}) or don't warn ({@code false}).
     * @param fixedPointComputationsOrder The order in which the fixed-point computations are to be performed during
     *     synthesis.
     * @param doForwardReach Whether to perform forward reachability during synthesis ({@code true}) or omit it
     *     ({@code false}).
     * @param doPlantsRefReqsWarn Whether to warn for plants that reference requirement state ({@code true}) or don't
     *     warn ({@code false}).
     * @param stateReqInvEnforceMode The way that state requirement invariants are enforced.
     * @param supervisorName The name of the resulting supervisor automaton. Must be a valid
     *     {@link CifValidationUtils#isValidIdentifier CIF identifier}.
     * @param supervisorNamespace The namespace of the resulting supervisor, or {@code null} to use the empty namespace.
     *     If not {@code null}, it must be a valid {@link CifValidationUtils#isValidName CIF name}.
     * @param synthesisStatistics The kinds of statistics to print.
     */
    public CifDataSynthesisSettings(Supplier<Boolean> shouldTerminate, DebugNormalOutput debugOutput,
            DebugNormalOutput normalOutput, WarnOutput warnOutput, boolean doNeverEnabledEventsWarn,
            FixedPointComputationsOrder fixedPointComputationsOrder, boolean doForwardReach,
            boolean doPlantsRefReqsWarn, StateReqInvEnforceMode stateReqInvEnforceMode, String supervisorName,
            String supervisorNamespace, EnumSet<SynthesisStatistics> synthesisStatistics)
    {
        // Store settings.
        this.shouldTerminate = shouldTerminate;
        this.debugOutput = debugOutput;
        this.normalOutput = normalOutput;
        this.warnOutput = warnOutput;
        this.doNeverEnabledEventsWarn = doNeverEnabledEventsWarn;
        this.fixedPointComputationsOrder = fixedPointComputationsOrder;
        this.doForwardReach = doForwardReach;
        this.doPlantsRefReqsWarn = doPlantsRefReqsWarn;
        this.stateReqInvEnforceMode = stateReqInvEnforceMode;
        this.supervisorName = supervisorName;
        this.supervisorNamespace = supervisorNamespace;
        this.synthesisStatistics = synthesisStatistics;

        // Check supervisor name.
        if (!CifValidationUtils.isValidIdentifier(supervisorName)) {
            String msg = fmt("Supervisor name \"%s\" is not a valid CIF identifier.", supervisorName);
            throw new InvalidOptionException(msg);
        }

        // Check supervisor namespace.
        if (supervisorNamespace != null && !CifValidationUtils.isValidName(supervisorNamespace)) {
            String msg = fmt("Supervisor namespace \"%s\" is invalid.", supervisorNamespace);
            throw new InvalidOptionException(msg);
        }
    }
}
