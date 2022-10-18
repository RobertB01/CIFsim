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

package org.eclipse.escet.cif.common.checkers.checks;

import org.eclipse.escet.cif.common.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.common.java.Assert;

/** Disallow components with not enough or too many automata. */
public class SpecAutomataCounts extends CifCheckNoCompDefInst {
    /** Constant to denote that a boundary should not be modified. */
    public static final int NO_CHANGE = -1;

    /** Minimum total number of automata. */
    private int minAnyAuts;

    /** Maximum total number of automata. */
    private int maxAnyAuts;

    /** Minimum number of kind-less automata. */
    private int minKindLessAuts;

    /** Maximum number of kind-less automata. */
    private int maxKindLessAuts;

    /** Maximum number of plant automata. */
    private int minPlantAuts;

    /** Minimum number of plant automata. */
    private int maxPlantAuts;

    /** Minimum number of requirement automata. */
    private int minRequirementAuts;

    /** Maximum number of requirement automata. */
    private int maxRequirementAuts;

    /** Minimum number of supervisor automata. */
    private int minSupervisorAuts;

    /** Maximum number of supervisor automata. */
    private int maxSupervisorAuts;

    /** Found number of automata without considering their kind. */
    private int numAnyAuts;

    /** Found number of kind-less automata. */
    private int numKindLessAuts;

    /** Found number of plant automata. */
    private int numPlantAuts;

    /** Found number of requirement automata. */
    private int numRequirementAuts;

    /** Found number of supervisor automata. */
    private int numSupervisorAuts;

    /**
     * Constructor of the {@link SpecAutomataCounts} class.
     *
     * <p>
     * The constructor imposes no limits to the minimum or maximum number of automata in a component.
     * </p>
     */
    public SpecAutomataCounts() {
        minAnyAuts = 0;
        maxAnyAuts = Integer.MAX_VALUE;

        minKindLessAuts = 0;
        maxKindLessAuts = Integer.MAX_VALUE;

        minPlantAuts = 0;
        maxPlantAuts = Integer.MAX_VALUE;

        minRequirementAuts = 0;
        maxRequirementAuts = Integer.MAX_VALUE;

        minSupervisorAuts = 0;
        maxSupervisorAuts = Integer.MAX_VALUE;
    }

    /**
     * Daisy-chain function to specify the number of allowed automata without considering their kind.
     *
     * @param minValue Smallest valid number of automata of any kind that should be available in the component.
     * @param maxValue Largest valid number of automata of any kind that should be available in the component.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCounts setMinMaxAuts(int minValue, int maxValue) {
        minAnyAuts = (minValue == NO_CHANGE) ? minAnyAuts : minValue;
        maxAnyAuts = (maxValue == NO_CHANGE) ? maxAnyAuts : maxValue;
        return this;
    }

    /**
     * Daisy-chain function to specify the number of allowed kind-less automata.
     *
     * @param minValue Smallest valid number of kind-less automata that should be available in the component.
     * @param maxValue Largest valid number of kind-less automata that should be available in the component.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCounts setMinMaxKindLessAuts(int minValue, int maxValue) {
        minKindLessAuts = (minValue == NO_CHANGE) ? minKindLessAuts : minValue;
        maxKindLessAuts = (maxValue == NO_CHANGE) ? maxKindLessAuts : maxValue;
        return this;
    }

    /**
     * Daisy-chain function to specify the number of allowed plant automata.
     *
     * @param minValue Smallest valid number of plant automata that should be available in the component.
     * @param maxValue Largest valid number of plant automata that should be available in the component.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCounts setMinMaxPlantAuts(int minValue, int maxValue) {
        minPlantAuts = (minValue == NO_CHANGE) ? minPlantAuts : minValue;
        maxPlantAuts = (maxValue == NO_CHANGE) ? maxPlantAuts : maxValue;
        return this;
    }

    /**
     * Daisy-chain function to specify the number of allowed requirement automata.
     *
     * @param minValue Smallest valid number of requirement automata that should be available in the component.
     * @param maxValue Largest valid number of requirement automata that should be available in the component.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCounts setMinMaxRequirementAuts(int minValue, int maxValue) {
        minRequirementAuts = (minValue == NO_CHANGE) ? minRequirementAuts : minValue;
        maxRequirementAuts = (maxValue == NO_CHANGE) ? maxRequirementAuts : maxValue;
        return this;
    }

    /**
     * Daisy-chain function to specify the number of allowed supervisor automata.
     *
     * @param minValue Smallest valid number of supervisor automata that should be available in the component.
     * @param maxValue Largest valid number of supervisor automata that should be available in the component.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCounts setMinMaxSupervisorAuts(int minValue, int maxValue) {
        minSupervisorAuts = (minValue == NO_CHANGE) ? minSupervisorAuts : minValue;
        maxSupervisorAuts = (maxValue == NO_CHANGE) ? maxSupervisorAuts : maxValue;
        return this;
    }

    @Override
    protected void preprocessSpecification(Specification spec, CifCheckViolations violations) {
        // Nothing found yet.
        numAnyAuts = 0;
        numKindLessAuts = 0;
        numPlantAuts = 0;
        numRequirementAuts = 0;
        numSupervisorAuts = 0;

        // Check sanity of the bounds.
        Assert.check(minAnyAuts < Integer.MAX_VALUE);
        Assert.check(minKindLessAuts < Integer.MAX_VALUE);
        Assert.check(minPlantAuts < Integer.MAX_VALUE);
        Assert.check(minRequirementAuts < Integer.MAX_VALUE);
        Assert.check(minSupervisorAuts < Integer.MAX_VALUE);

        Assert.check(minAnyAuts <= maxAnyAuts);
        Assert.check(minKindLessAuts <= maxKindLessAuts);
        Assert.check(minPlantAuts <= maxPlantAuts);
        Assert.check(minRequirementAuts <= maxRequirementAuts);
        Assert.check(minSupervisorAuts <= maxSupervisorAuts);
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        // Verify found counts against allowed counts and report any violation.
        checkAndReport(numAnyAuts, minAnyAuts, maxAnyAuts, "", violations);
        checkAndReport(numKindLessAuts, minKindLessAuts, maxKindLessAuts, "kindless ", violations);
        checkAndReport(numRequirementAuts, minRequirementAuts, maxRequirementAuts, "requirement ", violations);
        checkAndReport(numPlantAuts, minPlantAuts, maxPlantAuts, "plant ", violations);
        checkAndReport(numSupervisorAuts, minSupervisorAuts, maxSupervisorAuts, "supervisor ", violations);
    }

    /**
     * Check the actual number of automata against the minimum boundary, and report a violation if one is detected.
     *
     * @param numAuts Found number of automata.
     * @param minAuts Minimum required number of automata.
     * @param maxAuts Maximum allowed number of automata.
     * @param kindText Automaton prefix to express the kind in a violation report.
     * @param violations Storage of reported violations, may be extended in-place.
     */
    private void checkAndReport(int numAuts, int minAuts, int maxAuts, String kindText, CifCheckViolations violations) {
        if (minAuts == 0 && maxAuts == Integer.MAX_VALUE) {
            return; // [0, inf) range never fails.
        }

        // Construct the requirement explanatory text.
        LiteralMessage requiredMesg;
        if (minAuts == maxAuts) { // One specific count only.
            if (minAuts == 0) {
                requiredMesg = new LiteralMessage("specification does not allow %sautomata", kindText);
            } else if (minAuts == 1) {
                requiredMesg = new LiteralMessage("specification requires exactly 1 %sautomaton", kindText);
            } else {
                requiredMesg = new LiteralMessage("specification requires exactly %d %sautomata", minAuts, kindText);
            }
        } else if (maxAuts == Integer.MAX_VALUE) { // Unbounded upper limit.
            if (minAuts == 1) {
                requiredMesg = new LiteralMessage("specification requires at least 1 %sautomaton", kindText);
            } else {
                requiredMesg = new LiteralMessage("specification requires at least %d %sautomata", minAuts, kindText);
            }
        } else { // Some finite different lower and upper limits.
            if (maxAuts == 1) { // Implies nimAuts == 0
                requiredMesg = new LiteralMessage("specification requires at most 1 %sautomaton", kindText);
            } else if (minAuts == 0) {
                requiredMesg = new LiteralMessage("specification requires at at most %d %sautomata", maxAuts, kindText);
            } else {
                requiredMesg = new LiteralMessage("specification requires at least %d and at most %d %sautomata",
                        minAuts, maxAuts, kindText);
            }
        }

        // Check lower bound.
        if (numAuts < minAuts) {
            if (numAuts == 0) {
                violations.add(null, new LiteralMessage("no %sautomaton found,", kindText), requiredMesg);
            } else if (numAuts == 1) {
                violations.add(null, new LiteralMessage("1 %sautomaton found,", kindText), requiredMesg);
            } else {
                violations.add(null, new LiteralMessage("%d %sautomata found,", numAuts, kindText), requiredMesg);
            }
            return;
        }

        // Check upper bound.
        if (numAuts > maxAuts) {
            if (numAuts == 1) {
                violations.add(null, new LiteralMessage("1 %sautomaton found,", kindText), requiredMesg);
            } else {
                violations.add(null, new LiteralMessage("%d %sautomata found,", numAuts, kindText), requiredMesg);
            }
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        numAnyAuts++;
        switch (aut.getKind()) {
            case NONE:
                numKindLessAuts++;
                break;
            case PLANT:
                numPlantAuts++;
                break;
            case REQUIREMENT:
                numRequirementAuts++;
                break;
            case SUPERVISOR:
                numSupervisorAuts++;
                break;
            default:
                throw new AssertionError("Unexpected automaton SupKind found.");
        }
    }
}
