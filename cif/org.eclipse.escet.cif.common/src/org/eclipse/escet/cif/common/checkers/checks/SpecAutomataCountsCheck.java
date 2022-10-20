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

/** CIF check that disallows specifications with not enough or too many automata. */
public class SpecAutomataCountsCheck extends CifCheckNoCompDefInst {
    /** Constant to denote that a boundary should not be modified. */
    public static final int NO_CHANGE = -1;

    /** Minimum total number of automata. */
    private int minAnyAuts;

    /** Maximum total number of automata. */
    private int maxAnyAuts;

    /** Minimum number of kindless automata. */
    private int minKindlessAuts;

    /** Maximum number of kindless automata. */
    private int maxKindlessAuts;

    /** Minimum number of plant automata. */
    private int minPlantAuts;

    /** Maximum number of plant automata. */
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

    /** Found number of kindless automata. */
    private int numKindlessAuts;

    /** Found number of plant automata. */
    private int numPlantAuts;

    /** Found number of requirement automata. */
    private int numRequirementAuts;

    /** Found number of supervisor automata. */
    private int numSupervisorAuts;

    /**
     * Constructor of the {@link SpecAutomataCountsCheck} class.
     *
     * <p>
     * The constructor imposes no limits to the minimum or maximum number of automata in a component.
     * </p>
     */
    public SpecAutomataCountsCheck() {
        minAnyAuts = 0;
        maxAnyAuts = Integer.MAX_VALUE;

        minKindlessAuts = 0;
        maxKindlessAuts = Integer.MAX_VALUE;

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
     * <p>
     * The valid range for the lower-bound is a non-negative integer less then {@link Integer#MAX_VALUE}, the valid
     * range of the upper-bound is equal or larger than the lower-bound.
     * </p>
     *
     * @param minValue Smallest valid number of automata of any kind that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @param maxValue Largest valid number of automata of any kind that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCountsCheck setMinMaxAuts(int minValue, int maxValue) {
        minAnyAuts = (minValue == NO_CHANGE) ? minAnyAuts : minValue;
        maxAnyAuts = (maxValue == NO_CHANGE) ? maxAnyAuts : maxValue;
        return this;
    }

    /**
     * Daisy-chain function to specify the number of allowed kindless automata.
     *
     * <p>
     * The valid range for the lower-bound is a non-negative integer less then {@link Integer#MAX_VALUE}, the valid
     * range of the upper-bound is equal or larger than the lower-bound.
     * </p>
     *
     * @param minValue Smallest valid number of kindless automata that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @param maxValue Largest valid number of kindless automata that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCountsCheck setMinMaxKindlessAuts(int minValue, int maxValue) {
        minKindlessAuts = (minValue == NO_CHANGE) ? minKindlessAuts : minValue;
        maxKindlessAuts = (maxValue == NO_CHANGE) ? maxKindlessAuts : maxValue;
        return this;
    }

    /**
     * Daisy-chain function to specify the number of allowed plant automata.
     *
     * <p>
     * The valid range for the lower-bound is a non-negative integer less then {@link Integer#MAX_VALUE}, the valid
     * range of the upper-bound is equal or larger than the lower-bound.
     * </p>
     *
     * @param minValue Smallest valid number of plant automata that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @param maxValue Largest valid number of plant automata that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCountsCheck setMinMaxPlantAuts(int minValue, int maxValue) {
        minPlantAuts = (minValue == NO_CHANGE) ? minPlantAuts : minValue;
        maxPlantAuts = (maxValue == NO_CHANGE) ? maxPlantAuts : maxValue;
        return this;
    }

    /**
     * Daisy-chain function to specify the number of allowed requirement automata.
     *
     * <p>
     * The valid range for the lower-bound is a non-negative integer less then {@link Integer#MAX_VALUE}, the valid
     * range of the upper-bound is equal or larger than the lower-bound.
     * </p>
     *
     * @param minValue Smallest valid number of requirement automata that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @param maxValue Largest valid number of requirement automata that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCountsCheck setMinMaxRequirementAuts(int minValue, int maxValue) {
        minRequirementAuts = (minValue == NO_CHANGE) ? minRequirementAuts : minValue;
        maxRequirementAuts = (maxValue == NO_CHANGE) ? maxRequirementAuts : maxValue;
        return this;
    }

    /**
     * Daisy-chain function to specify the number of allowed supervisor automata.
     *
     * <p>
     * The valid range for the lower-bound is a non-negative integer less then {@link Integer#MAX_VALUE}, the valid
     * range of the upper-bound is equal or larger than the lower-bound.
     * </p>
     *
     * @param minValue Smallest valid number of supervisor automata that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @param maxValue Largest valid number of supervisor automata that should be available in the specification, use
     *     {@link #NO_CHANGE} to skip changing the value.
     * @return The class instance, for daisy-chaining.
     */
    public SpecAutomataCountsCheck setMinMaxSupervisorAuts(int minValue, int maxValue) {
        minSupervisorAuts = (minValue == NO_CHANGE) ? minSupervisorAuts : minValue;
        maxSupervisorAuts = (maxValue == NO_CHANGE) ? maxSupervisorAuts : maxValue;
        return this;
    }

    @Override
    protected void preprocessSpecification(Specification spec, CifCheckViolations violations) {
        // Nothing found yet.
        numAnyAuts = 0;
        numKindlessAuts = 0;
        numPlantAuts = 0;
        numRequirementAuts = 0;
        numSupervisorAuts = 0;

        // Check sanity of the bounds.
        Assert.check(minAnyAuts >= 0);
        Assert.check(minKindlessAuts >= 0);
        Assert.check(minPlantAuts >= 0);
        Assert.check(minRequirementAuts >= 0);
        Assert.check(minSupervisorAuts >= 0);

        Assert.check(minAnyAuts < Integer.MAX_VALUE);
        Assert.check(minKindlessAuts < Integer.MAX_VALUE);
        Assert.check(minPlantAuts < Integer.MAX_VALUE);
        Assert.check(minRequirementAuts < Integer.MAX_VALUE);
        Assert.check(minSupervisorAuts < Integer.MAX_VALUE);

        Assert.check(minAnyAuts <= maxAnyAuts);
        Assert.check(minKindlessAuts <= maxKindlessAuts);
        Assert.check(minPlantAuts <= maxPlantAuts);
        Assert.check(minRequirementAuts <= maxRequirementAuts);
        Assert.check(minSupervisorAuts <= maxSupervisorAuts);
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        // Verify found counts against allowed counts and report any violation.
        checkAndReport(numAnyAuts, minAnyAuts, maxAnyAuts, "", violations);
        checkAndReport(numKindlessAuts, minKindlessAuts, maxKindlessAuts, "kindless ", violations);
        checkAndReport(numRequirementAuts, minRequirementAuts, maxRequirementAuts, "requirement ", violations);
        checkAndReport(numPlantAuts, minPlantAuts, maxPlantAuts, "plant ", violations);
        checkAndReport(numSupervisorAuts, minSupervisorAuts, maxSupervisorAuts, "supervisor ", violations);
    }

    /**
     * Check the actual number of automata against the minimum and maximum boundaries, and report any violations.
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
                requiredMesg = new LiteralMessage("specification does not have exactly 0 %sautomata", kindText);
            } else if (minAuts == 1) {
                requiredMesg = new LiteralMessage("specification does not have exactly 1 %sautomaton", kindText);
            } else {
                requiredMesg = new LiteralMessage("specification does not have exactly %d %sautomata", minAuts,
                        kindText);
            }
        } else if (maxAuts == Integer.MAX_VALUE) { // Unbounded upper limit.
            if (minAuts == 1) {
                requiredMesg = new LiteralMessage("specification does not have at least 1 %sautomaton", kindText);
            } else {
                requiredMesg = new LiteralMessage("specification does not have at least %d %sautomata", minAuts,
                        kindText);
            }
        } else { // Some finite different lower and upper limits.
            if (maxAuts == 1) { // Implies minAuts == 0
                requiredMesg = new LiteralMessage("specification does not have at most 1 %sautomaton", kindText);
            } else if (minAuts == 0) {
                requiredMesg = new LiteralMessage("specification does not have at most %d %sautomata", maxAuts,
                        kindText);
            } else {
                requiredMesg = new LiteralMessage("specification does not have at least %d and at most %d %sautomata",
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
                numKindlessAuts++;
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
