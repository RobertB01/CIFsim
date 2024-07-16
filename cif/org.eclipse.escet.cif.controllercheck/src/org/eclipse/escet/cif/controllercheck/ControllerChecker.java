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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.escet.cif.bdd.conversion.CifToBddConverter;
import org.eclipse.escet.cif.bdd.settings.AllowNonDeterminism;
import org.eclipse.escet.cif.bdd.settings.CifBddSettings;
import org.eclipse.escet.cif.bdd.settings.CifBddStatistics;
import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.bdd.utils.CifBddApplyPlantInvariants;
import org.eclipse.escet.cif.cif2cif.ElimAlgVariables;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimConsts;
import org.eclipse.escet.cif.cif2cif.ElimIfUpdates;
import org.eclipse.escet.cif.cif2cif.ElimLocRefExprs;
import org.eclipse.escet.cif.cif2cif.ElimMonitors;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.ElimStateEvtExclInvs;
import org.eclipse.escet.cif.cif2cif.ElimTypeDecls;
import org.eclipse.escet.cif.cif2cif.EnumsToInts;
import org.eclipse.escet.cif.cif2cif.RelabelSupervisorsAsPlants;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.controllercheck.boundedresponse.BoundedResponseCheck;
import org.eclipse.escet.cif.controllercheck.boundedresponse.BoundedResponseCheckConclusion;
import org.eclipse.escet.cif.controllercheck.confluence.ConfluenceCheck;
import org.eclipse.escet.cif.controllercheck.confluence.ConfluenceCheckConclusion;
import org.eclipse.escet.cif.controllercheck.finiteresponse.FiniteResponseCheck;
import org.eclipse.escet.cif.controllercheck.finiteresponse.FiniteResponseCheckConclusion;
import org.eclipse.escet.cif.controllercheck.mdd.MddDeterminismChecker;
import org.eclipse.escet.cif.controllercheck.mdd.MddPreChecker;
import org.eclipse.escet.cif.controllercheck.mdd.MddPrepareChecks;
import org.eclipse.escet.cif.controllercheck.nonblockingundercontrol.NonBlockingUnderControlCheck;
import org.eclipse.escet.cif.controllercheck.nonblockingundercontrol.NonBlockingUnderControlCheckConclusion;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

import com.github.javabdd.BDDFactory;

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
     * @return The controller checker result, or {@code null} if termination was requested.
     */
    public static ControllerCheckerResult performChecks(Specification spec, String specAbsPath,
            ControllerCheckerSettings settings)
    {
        // Check settings.
        settings.check();

        // Get some settings.
        Supplier<Boolean> shouldTerminate = settings.getShouldTerminate();
        DebugNormalOutput normalOutput = settings.getNormalOutput();
        DebugNormalOutput debugOutput = settings.getDebugOutput();
        WarnOutput warnOutput = settings.getWarnOutput();

        // Get checks to perform.
        boolean checkBoundedResponse = settings.getCheckBoundedResponse();
        boolean checkConfluence = settings.getCheckConfluence();
        boolean checkFiniteResponse = settings.getCheckFiniteResponse();
        boolean checkNonBlockingUnderControl = settings.getCheckNonBlockingUnderControl();

        // Preprocess and check the specification.
        spec = preprocessAndCheck(spec, specAbsPath, shouldTerminate, warnOutput);

        // Check which representations are needed.
        boolean hasBddBasedChecks = checkBoundedResponse || checkNonBlockingUnderControl;
        boolean hasMddBasedChecks = checkFiniteResponse || checkConfluence;

        // Preparations for BDD-based checks.
        CifBddSpec cifBddSpec = null; // Used for BDD-based checks.
        if (hasBddBasedChecks) {
            debugOutput.line("Preparing for BDD-based checks...");
            cifBddSpec = convertToBdd(spec, settings);
            if (cifBddSpec == null) {
                return null;
            }
        }

        // Debug output: empty line between preparations.
        if (hasBddBasedChecks && hasMddBasedChecks) {
            debugOutput.line();
        }

        // Preparations for MDD-based checks.
        MddPrepareChecks mddPrepareChecks = null; // Used for MDD-based checks.
        if (hasMddBasedChecks) {
            debugOutput.line("Preparing for MDD-based checks...");
            boolean computeGlobalGuardedUpdates = checkConfluence;
            mddPrepareChecks = convertToMdd(spec, specAbsPath, computeGlobalGuardedUpdates, shouldTerminate);
            if (mddPrepareChecks == null) {
                return null;
            }
        }

        // Common initialization for the checks.
        int checksPerformed = 0;

        // Check bounded response.
        BoundedResponseCheckConclusion boundedResponseConclusion = null;
        if (checkBoundedResponse) {
            if (debugOutput.isEnabled() || checksPerformed > 0) {
                normalOutput.line();
            }
            normalOutput.line("Checking for bounded response...");
            boundedResponseConclusion = new BoundedResponseCheck().performCheck(cifBddSpec);
            checksPerformed++;
            if (boundedResponseConclusion == null || shouldTerminate.get()) {
                return null;
            }
        }

        // Check non-blocking under control.
        NonBlockingUnderControlCheckConclusion nonBlockingUnderControlConclusion = null;
        if (checkNonBlockingUnderControl) {
            if (debugOutput.isEnabled() || checksPerformed > 0) {
                normalOutput.line();
            }
            normalOutput.line("Checking for non-blocking under control...");
            nonBlockingUnderControlConclusion = new NonBlockingUnderControlCheck().performCheck(cifBddSpec);
            checksPerformed++;
            if (nonBlockingUnderControlConclusion == null || shouldTerminate.get()) {
                return null;
            }
        }

        // Clean up the BDD representation of the specification, now that it is not needed anymore.
        if (cifBddSpec != null) {
            cleanupBdd(cifBddSpec);
            cifBddSpec = null;
            if (shouldTerminate.get()) {
                return null;
            }
        }

        // Check finite response.
        FiniteResponseCheckConclusion finiteResponseConclusion = null;
        if (checkFiniteResponse) {
            if (debugOutput.isEnabled() || checksPerformed > 0) {
                normalOutput.line();
            }
            normalOutput.line("Checking for finite response...");
            finiteResponseConclusion = new FiniteResponseCheck().performCheck(mddPrepareChecks);
            checksPerformed++;
            if (finiteResponseConclusion == null || shouldTerminate.get()) {
                return null;
            }
        }

        // Check confluence.
        ConfluenceCheckConclusion confluenceConclusion = null;
        if (checkConfluence) {
            if (debugOutput.isEnabled() || checksPerformed > 0) {
                normalOutput.line();
            }
            normalOutput.line("Checking for confluence...");
            confluenceConclusion = new ConfluenceCheck().performCheck(mddPrepareChecks);
            checksPerformed++;
            if (confluenceConclusion == null || shouldTerminate.get()) {
                return null;
            }
        }

        // Sanity check.
        if (boundedResponseConclusion != null && finiteResponseConclusion != null) {
            if (finiteResponseConclusion.propertyHolds()) {
                Assert.check(boundedResponseConclusion.controllablesBound.isBounded());
            }
        }

        // Output the checker conclusions.
        normalOutput.line();
        normalOutput.line("CONCLUSION:");

        normalOutput.inc();
        if (boundedResponseConclusion != null) {
            boundedResponseConclusion.printResult();
        } else {
            normalOutput
                    .line("[UNKNOWN] Bounded response checking was disabled, bounded response property is unknown.");
        }
        normalOutput.dec();

        if ((boundedResponseConclusion != null && boundedResponseConclusion.hasDetails())
                || (nonBlockingUnderControlConclusion != null && nonBlockingUnderControlConclusion.hasDetails()))
        {
            normalOutput.line(); // Empty line between conclusions, if either of them prints details.
        }

        normalOutput.inc();
        if (nonBlockingUnderControlConclusion != null) {
            nonBlockingUnderControlConclusion.printResult();
        } else {
            normalOutput.line(
                    "[UNKNOWN] Non-blocking under control checking was disabled, non-blocking under control property is "
                            + "unknown.");
        }
        normalOutput.dec();

        if ((nonBlockingUnderControlConclusion != null && nonBlockingUnderControlConclusion.hasDetails())
                || (finiteResponseConclusion != null && finiteResponseConclusion.hasDetails()))
        {
            normalOutput.line(); // Empty line between conclusions, if either of them prints details.
        }

        normalOutput.inc();
        if (finiteResponseConclusion != null) {
            finiteResponseConclusion.printResult();
        } else {
            normalOutput.line("[UNKNOWN] Finite response checking was disabled, finite response property is unknown.");
        }
        normalOutput.dec();

        if ((finiteResponseConclusion != null && finiteResponseConclusion.hasDetails())
                || (confluenceConclusion != null && confluenceConclusion.hasDetails()))
        {
            normalOutput.line(); // Empty line between conclusions, if either of them prints details.
        }

        normalOutput.inc();
        if (confluenceConclusion != null) {
            confluenceConclusion.printResult();
        } else {
            normalOutput.line("[UNKNOWN] Confluence checking was disabled, confluence property is unknown.");
        }
        normalOutput.dec();

        // Return the result.
        return new ControllerCheckerResult(boundedResponseConclusion, confluenceConclusion, finiteResponseConclusion,
                nonBlockingUnderControlConclusion);
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

    /**
     * Convert a CIF specification to a BDD representation. Also performs some BDD-specific checks on the input
     * specification.
     *
     * @param spec The specification to convert. Must not be modified.
     * @param checkerSettings The controller properties checker settings.
     * @return The CIF/BDD specification, or {@code null} if termination was requested.
     */
    private static CifBddSpec convertToBdd(Specification spec, ControllerCheckerSettings checkerSettings) {
        Supplier<Boolean> shouldTerminate = checkerSettings.getShouldTerminate();

        // Use a copy of the specification.
        spec = EMFHelper.deepclone(spec);
        if (shouldTerminate.get()) {
            return null;
        }

        // Relabel supervisors as plants, to deal with them in the same way.
        new RelabelSupervisorsAsPlants().transform(spec);

        // Get CIF/BDD settings.
        CifBddSettings cifBddSettings = new CifBddSettings();
        cifBddSettings.setShouldTerminate(checkerSettings.getShouldTerminate());
        cifBddSettings.setDebugOutput(checkerSettings.getDebugOutput());
        cifBddSettings.setNormalOutput(checkerSettings.getNormalOutput());
        cifBddSettings.setWarnOutput(checkerSettings.getWarnOutput());
        cifBddSettings.setAllowNonDeterminism(AllowNonDeterminism.ALL);
        cifBddSettings.setCifBddStatistics(EnumSet.noneOf(CifBddStatistics.class));
        cifBddSettings.setDoPlantsRefReqsWarn(false);

        cifBddSettings.setModificationAllowed(false);

        // Pre-process the CIF specification:
        // - Does not warn about CIF/SVG specifications, as they have been removed already.
        // - Does not warn about plants referring to requirement state, as we disabled that check.
        CifToBddConverter.preprocess(spec, cifBddSettings.getWarnOutput(), cifBddSettings.getDoPlantsRefReqsWarn());

        // Convert the CIF specification to its BDD representation. Also checks BDD-specific preconditions.
        BDDFactory factory = CifToBddConverter.createFactory(cifBddSettings, Collections.emptyList(),
                Collections.emptyList());
        CifToBddConverter converter = new CifToBddConverter("CIF controller properties checker");
        CifBddSpec cifBddSpec = converter.convert(spec, cifBddSettings, factory);
        if (shouldTerminate.get()) {
            return null;
        }

        // Clean up no longer needed BDD predicates.
        cifBddSpec.freeIntermediateBDDs(true);
        if (shouldTerminate.get()) {
            return null;
        }

        // Apply the plant state/event exclusion invariants.
        CifBddApplyPlantInvariants.applyStateEvtExclPlantsInvs(cifBddSpec, "system", () -> null,
                cifBddSettings.getDebugOutput().isEnabled());
        if (shouldTerminate.get()) {
            return null;
        }

        // Initialize applying edges.
        for (CifBddEdge edge: cifBddSpec.edges) {
            edge.initApply();
            if (shouldTerminate.get()) {
                return null;
            }
        }

        // Return the CIF/BDD specification.
        return cifBddSpec;
    }

    /**
     * Convert a CIF specification to an MDD representation. Also performs some MDD-specific checks on the input
     * specification.
     *
     * @param spec The specification to convert. Must not be modified.
     * @param specAbsPath The absolute local file system path to the CIF specification to check.
     * @param computeGlobalGuardedUpdates Whether to compute global guarded updates.
     * @param shouldTerminate Callback that indicates whether execution should be terminated on user request.
     * @return The CIF/BDD specification, or {@code null} if termination was requested.
     */
    private static MddPrepareChecks convertToMdd(Specification spec, String specAbsPath,
            boolean computeGlobalGuardedUpdates, Supplier<Boolean> shouldTerminate)
    {
        // Use a copy of the specification.
        spec = EMFHelper.deepclone(spec);
        if (shouldTerminate.get()) {
            return null;
        }

        // Pre-processing.
        // CIF automata structure normalization.
        new ElimStateEvtExclInvs().transform(spec);
        new ElimMonitors().transform(spec);
        new ElimSelf().transform(spec);
        new ElimTypeDecls().transform(spec);

        final Function<Automaton, String> varNamingFunction = a -> "LP_" + a.getName();
        final Function<Automaton, String> enumNamingFunction = a -> "LOCS_" + a.getName();
        final Function<Location, String> litNamingFunction = l -> "LOC_" + l.getName();
        final boolean considerLocsForRename = true;
        final boolean addInitPreds = true;
        final boolean optimized = false;
        final Map<DiscVariable, String> lpVarToAbsAutNameMap = null;
        final boolean optInits = true;
        final boolean addEdgeGuards = true;
        final boolean copyAutAnnosToEnum = false;
        final boolean copyLocAnnosToEnumLits = false;
        new ElimLocRefExprs(varNamingFunction, enumNamingFunction, litNamingFunction, considerLocsForRename,
                addInitPreds, optimized, lpVarToAbsAutNameMap, optInits, addEdgeGuards, copyAutAnnosToEnum,
                copyLocAnnosToEnumLits).transform(spec);

        new EnumsToInts().transform(spec);
        if (shouldTerminate.get()) {
            return null;
        }

        // Simplify expressions.
        new ElimAlgVariables().transform(spec);
        new ElimConsts().transform(spec);
        new SimplifyValues().transform(spec);
        if (shouldTerminate.get()) {
            return null;
        }

        // Check preconditions.
        new MddPreChecker(() -> shouldTerminate.get())
                .reportPreconditionViolations(spec, specAbsPath, "CIF controller properties checker");
        if (shouldTerminate.get()) {
            return null;
        }

        // Eliminate if updates. This does not support multi-assignments or partial variable assignments.
        new ElimIfUpdates().transform(spec);
        if (shouldTerminate.get()) {
            return null;
        }

        // Non-determinism check.
        new MddDeterminismChecker(() -> shouldTerminate.get())
                .reportPreconditionViolations(spec, specAbsPath, "CIF controller properties checker");
        if (shouldTerminate.get()) {
            return null;
        }

        // Create MDD representation.
        MddPrepareChecks mddPrepareChecks = new MddPrepareChecks(computeGlobalGuardedUpdates);
        if (!mddPrepareChecks.compute(spec)) {
            return null;
        }

        // Return MDD representation.
        return mddPrepareChecks;
    }

    /**
     * Clean up the CIF/BDD specification.
     *
     * @param cifBddSpec The CIF/BDD specification.
     */
    private static void cleanupBdd(CifBddSpec cifBddSpec) {
        for (CifBddEdge edge: cifBddSpec.edges) {
            edge.cleanupApply();
        }
        cifBddSpec.freeAllBDDs();
    }
}
