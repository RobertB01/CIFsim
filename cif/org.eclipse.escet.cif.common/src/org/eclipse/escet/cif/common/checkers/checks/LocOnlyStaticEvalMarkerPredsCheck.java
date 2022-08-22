//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifEvalUtils.evalPreds;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;

/** CIF check that allows marker predicates in locations only if they can be evaluated statically. */
public class LocOnlyStaticEvalMarkerPredsCheck extends CifCheck {
    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Skip location parameters.
        EObject parent = loc.eContainer();
        if (parent instanceof LocationParameter) {
            return;
        }

        // Check for violation.
        if (!loc.getMarkeds().isEmpty()) {
            try {
                evalPreds(loc.getMarkeds(), false, true);
            } catch (UnsupportedException e) {
                if (loc.getName() != null) {
                    violations.add(loc, "location has a marker predicate that can not be evaluated statically");
                } else {
                    violations.add((Automaton)loc.eContainer(),
                            "automaton has a location with a marker predicate that can not be evaluated statically");
                }
            } catch (CifEvalException e) {
                if (loc.getName() != null) {
                    violations.add(loc,
                            "static evaluation of a marker predicate in the location resulted in an evaluation error");
                } else {
                    violations.add((Automaton)loc.eContainer(), "static evaluation of a marker predicate in the "
                            + "location of the automaton resulted in an evaluation error");
                }
            }
        }
    }
}
