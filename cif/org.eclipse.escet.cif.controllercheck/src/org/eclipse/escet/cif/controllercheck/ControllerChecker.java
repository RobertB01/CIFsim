//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.output.WarnOutput;

/** Controller properties checker. */
public class ControllerChecker {
    /** Constructor for the {@link ControllerChecker} class. */
    private ControllerChecker() {
        // Static class.
    }

    /**
     * Perform controller properties checks. At least one check must be enabled.
     *
     * @param spec The CIF specification to check.
     * @param specAbsPath The absolute local file system path to the CIF specification to check.
     * @param settings The settings to use.
     */
    public static void performChecks(Specification spec, String specAbsPath,
            ControllerCheckerSettings settings)
    {
        // Get some settings.
        Supplier<Boolean> shouldTerminate = settings.getShouldTerminate();
        WarnOutput warnOutput = settings.getWarnOutput();

        // Get checks to perform.
        boolean checkBoundedResponse = settings.getCheckBoundedResponse();
        boolean checkConfluence = settings.getCheckConfluence();
        boolean checkFiniteResponse = settings.getCheckFiniteResponse();
        boolean checkNonBlockingUnderControl = settings.getCheckNonBlockingUnderControl();

        // Ensure at least one check is enabled.
        if (!checkBoundedResponse && !checkNonBlockingUnderControl && !checkFiniteResponse && !checkConfluence) {
            throw new InvalidOptionException(
                    "No checks enabled. Enable one of the checks for the controller properties checker to check.");
        }

        // Preprocess and check the specification.
        spec = preprocessAndCheck(spec, specAbsPath, shouldTerminate, warnOutput);
    }

    /**
     * Preprocess and check the input specification of the controller properties checker.
     *
     * @param spec The specification to preprocess and check. Is modified in-place, but not as much as the result of
     *     this method.
     * @param specAbsPath The absolute local file system path to the CIF specification to check.
     * @param shouldTerminate Callback that indicates whether execution should be terminated on user request.
     * @param warnOutput Callback to send warnings to the user.
     * @return The preprocessed and checked specification, or {@code null} if termination was requested.
     */
    private static Specification preprocessAndCheck(Specification spec, String specAbsPath,
            Supplier<Boolean> shouldTerminate, WarnOutput warnOutput)
    {
        // Eliminate component definition/instantiation. This allows to perform precondition checks, as well as perform
        // annotation post checking.
        new ElimComponentDefInst().transform(spec);

        // Get the output specification, and the internal specification on which to perform the checks.
        // Copy the internal specification, to preserve the output specification.
        spec = EMFHelper.deepclone(spec);

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warnOutput.line("The specification contains CIF/SVG input declarations. These will be ignored.");
        }

        // Check preconditions that apply to all checks.
        ControllerCheckerPreChecker checker = new ControllerCheckerPreChecker(() -> shouldTerminate.get());
        checker.reportPreconditionViolations(spec, specAbsPath, "CIF controller properties checker");
        if (shouldTerminate.get()) {
            return null;
        }

        // Warn if specification doesn't look very useful:
        // - Due to preconditions, all events have controllability, but check for none of them being (un)controllable.
        Set<Event> specAlphabet = CifEventUtils.getAlphabet(spec);
        if (specAlphabet.stream().allMatch(e -> !e.getControllable())) {
            warnOutput.line("The alphabet of the specification contains no controllable events.");
        }
        if (specAlphabet.stream().allMatch(e -> e.getControllable())) {
            warnOutput.line("The alphabet of the specification contains no uncontrollable events.");
        }

        // Return the preprocessed and checked specification.
        return spec;
    }
}
