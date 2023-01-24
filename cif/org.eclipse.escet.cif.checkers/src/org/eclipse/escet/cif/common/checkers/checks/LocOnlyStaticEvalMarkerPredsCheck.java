//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnAncestorMessage;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;

/** CIF check that allows marker predicates in locations only if they can be evaluated statically. */
public class LocOnlyStaticEvalMarkerPredsCheck extends CifCheck {
    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Note that location parameters never have marker predicates.
        if (!loc.getMarkeds().isEmpty()) {
            try {
                evalPreds(loc.getMarkeds(), false, true);
            } catch (UnsupportedException e) {
                // Report violation on the location, or on its automaton in case the location has no name.
                violations.add(loc, new ReportObjectTypeDescrMessage(), new LiteralMessage("has"),
                        new IfReportOnAncestorMessage("a location with"),
                        new LiteralMessage("a marker predicate that cannot be evaluated statically"));
            } catch (CifEvalException e) {
                // Report violation on the location, or on its automaton in case the location has no name.
                violations.add(loc, new ReportObjectTypeDescrMessage(), new LiteralMessage("has"),
                        new IfReportOnAncestorMessage("a location with"),
                        new LiteralMessage("a marker predicate that cannot be evaluated statically, "
                                + "as the evaluation resulted in an evaluation error"));
            }
        }
    }
}
