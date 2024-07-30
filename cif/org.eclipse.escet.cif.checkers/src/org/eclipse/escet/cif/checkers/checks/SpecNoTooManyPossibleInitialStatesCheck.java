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
import org.eclipse.escet.cif.common.CifValueUtils.Count;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;

/**
 * CIF check that disallows specifications with more than {@link Integer#MAX_VALUE} possible initial states. It
 * considers discrete variables, input variables and automata.
 */
public class SpecNoTooManyPossibleInitialStatesCheck extends CifCheckNoCompDefInst {
    /** The current best approximation of the number of possible initial states. */
    private double count = 1;

    /** Whether the {@link #count} is precise. */
    private boolean isPrecise = true;

    @Override
    protected void preprocessDiscVariable(DiscVariable discVar, CifCheckViolations violations) {
        Count varCount = CifValueUtils.getPossibleInitialValuesCount(discVar);
        count *= varCount.value();
        isPrecise &= varCount.isPrecise();
    }

    @Override
    protected void preprocessInputVariable(InputVariable inputVar, CifCheckViolations violations) {
        count *= CifValueUtils.getPossibleInitialValuesCount(inputVar);
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        Count autCount = CifValueUtils.getPossibleInitialLocationsCount(aut);
        count *= autCount.value();
        isPrecise &= autCount.isPrecise();
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        if (count > Integer.MAX_VALUE) {
            String countTxt;
            if (Double.isInfinite(count)) {
                violations.add(spec, "The specification has practically infinitely many possible initial states");
            } else {
                countTxt = CifMath.realToStr(count);
                if (!isPrecise) {
                    countTxt = "approximately " + countTxt;
                }
                violations.add(spec,
                        "The specification has %s possible initial states, more than the maximum of 2,147,483,647",
                        countTxt);
            }
        }
    }
}
