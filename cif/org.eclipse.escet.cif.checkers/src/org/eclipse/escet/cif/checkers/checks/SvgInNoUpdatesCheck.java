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

package org.eclipse.escet.cif.checkers.checks;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;

/** CIF check that does not allow SVG input mappings with updates. */
public class SvgInNoUpdatesCheck extends CifCheck {
    @Override
    protected void preprocessSvgIn(SvgIn svgIn, CifCheckViolations violations) {
        for (Update update: svgIn.getUpdates()) {
            violations.add(update, "SVG input mapping has an update");
        }
    }
}
