//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;

/**
 * CIF check that disallows specifications with more than {@link Integer#MAX_VALUE} possible initial states. It
 * considers discrete variables, input variables and automata.
 */
public class SpecNoTooManyPossibleInitialStatesCheck extends CifCheckNoCompDefInst {
    /** The current best approximation of the number of possible initial states. */
    private double count = 1;

    /** Whether the {@link #count} is approximate. */
    private boolean isApproximate = false;

    @Override
    protected void preprocessDiscVariable(DiscVariable discVar, CifCheckViolations violations) {
        // Get possible number of initial values of the variable.
        VariableValue varValue = discVar.getValue();
        double nrOfValues;
        if (varValue == null) {
            nrOfValues = 1; // Implicit default initial value.
        } else if (varValue.getValues().size() >= 1) {
            nrOfValues = varValue.getValues().size(); // Explicit initial values.
            isApproximate = true; // Some expressions may evaluate to the same value.
        } else {
            nrOfValues = CifValueUtils.getPossibleValueCount(discVar.getType());
        }

        // Update possible number of initial states.
        count *= nrOfValues;
    }

    @Override
    protected void preprocessInputVariable(InputVariable inputVar, CifCheckViolations violations) {
        // Get number of possible (initial) values.
        double nrOfValues = CifValueUtils.getPossibleValueCount(inputVar.getType());

        // Update possible number of initial states.
        count *= nrOfValues;
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        // Get possible initial locations of the automaton.
        int nrOfLocs = 0;
        for (Location loc: aut.getLocations()) {
            // Check if for sure not an initial location.
            if (loc.getInitials().isEmpty()) {
                continue;
            }
            if (CifValueUtils.isTriviallyFalse(loc.getInitials(), true, true)) {
                continue;
            }

            // Check if for sure an initial location.
            if (CifValueUtils.isTriviallyTrue(loc.getInitials(), true, true)) {
                nrOfLocs++;
                continue;
            }

            // Potentially an initial location.
            nrOfLocs++;
            isApproximate = true;
        }

        // Update possible number of initial states.
        count *= nrOfLocs;
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        if (count > Integer.MAX_VALUE) {
            String countTxt;
            if (Double.isInfinite(count)) {
                violations.add(spec, "The specification has practically infinitely many possible initial states");
            } else {
                countTxt = CifMath.realToStr(count);
                if (isApproximate) {
                    countTxt = "approximately " + countTxt;
                }
                violations.add(spec,
                        "The specification has %s possible initial states, which is more than 2,147,483,647",
                        countTxt);
            }
        }
    }
}
